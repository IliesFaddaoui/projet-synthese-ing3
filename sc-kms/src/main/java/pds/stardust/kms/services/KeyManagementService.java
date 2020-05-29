package pds.stardust.kms.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pds.stardust.kms.beans.*;
import pds.stardust.kms.config.VaultConfig;
import pds.stardust.kms.constants.KeysConstants;
import pds.stardust.kms.constants.SecretsConstants;
import pds.stardust.kms.utils.StringUtils;

import java.util.HashMap;
import java.util.Objects;

import static pds.stardust.kms.utils.HttpUtils.buildHttpHeaders;

/**
 * This class defines the common methods regarding key management.
 */
@Service
public class KeyManagementService implements IKeyManagementService {

    private final ObjectMapper mapper;
    private final VaultConfig vaultConfig;
    private final RestTemplate restTemplate;
    private final SecretManagementService secretManagementService;

    @Autowired
    public KeyManagementService(VaultConfig vaultConfig, RestTemplate restTemplate, SecretManagementService secretManagementService) {
        this.secretManagementService = secretManagementService;
        this.restTemplate = restTemplate;
        this.mapper = new ObjectMapper();
        this.vaultConfig = vaultConfig;
    }

    /**
     * Process the Key Encryption Key (KEK) rotation. Steps are below :
     *
     * 1 - Rotate the Key Encryption Key @See rotateKeyEncryptionKey()
     * 2 - Rewrap the Data Encryption Key (DEK) @See rewrapDataEncryptionKey()
     *
     * @throws Exception If any request body cannot be serialized in JSON
     */
    public void rotateKeys() throws Exception {

        this.rotateKeyEncryptionKey();
        this.rewrapDataEncryptionKey();
        // Update key configuration to only allow last version decryption

    }

    /**
     * Read the Data Encryption Key (DEK) by decrypting it with the Key Encryption Key (KEK)
     *
     * @return The data Encryption Key encoded in base 64
     * @throws Exception If the decrypt request body cannot be serialized in JSON
     */
    public VaultSecretResponse readDataEncryptionKey() throws Exception {

        DecryptDataRequest decryptDataRequest = new DecryptDataRequest(
                StringUtils.base64ToString(
                    secretManagementService.getDataEncryptionKey().getData()
                )
        );

        String body = mapper.writeValueAsString(decryptDataRequest);

        HttpEntity<String> httpEntity = new HttpEntity<>(
                body,
                buildHttpHeaders(vaultConfig.getToken())
        );

        String url = vaultConfig.getUrl() + KeysConstants.KEY_ENCRYPTION_KEY_DECRYPT_PATH;

        ResponseEntity<DecryptDataResponse> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, DecryptDataResponse.class);

        return new VaultSecretResponse(
                Objects.requireNonNull(response.getBody()).getData().getPlaintext()
        );

    }

    /**
     * Rotate the Key Encryption Key. A new version of the key will be created.
     * The Data Encryption will need to be rewrapped with this new version.
     */
    private void rotateKeyEncryptionKey() {

        HttpEntity<String> httpEntity = new HttpEntity<>(
                buildHttpHeaders(vaultConfig.getToken())
        );

        String url = vaultConfig.getUrl() + KeysConstants.KEY_ENCRYPTION_KEY_ROTATE_PATH;

        restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

    }

    /**
     * Rewrap the Data Encryption Key (DEK) to allow it to be decrypted with the
     * new Key Encryption Key (KEK) once it have been rotated. Steps are below :
     *
     * 1 - Retrieve the Data Encryption Key from Vault and decoded it
     * 2 - Request Vault to rewrap the Data Encryption Key
     * 3 - Encode the rewrapped Data Encryption Key and update its Vault secret
     *
     * @throws Exception If the rewrap request body cannot be serialized in JSON
     */
    private void rewrapDataEncryptionKey() throws Exception {

        RewrapDataRequest rewrapDataRequest = new RewrapDataRequest(
                StringUtils.base64ToString(
                        secretManagementService.getDataEncryptionKey().getData()
                )
        );

        String body = mapper.writeValueAsString(rewrapDataRequest);

        HttpEntity<String> httpEntity = new HttpEntity<>(
                body,
                buildHttpHeaders(vaultConfig.getToken())
        );

        String url = vaultConfig.getUrl() + KeysConstants.KEY_ENCRYPTION_KEY_REWRAP_PATH;

        ResponseEntity<RewrapDataResponse> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, RewrapDataResponse.class);

        VaultSecret vaultSecret = new VaultSecret();

        final HashMap<String, String> secret = new HashMap<>();
        secret.put(SecretsConstants.DATA_ENCRYPTION_KEY_SECRET_KEY, StringUtils.stringToBase64(Objects.requireNonNull(response.getBody()).getData().getCiphertext()));

        vaultSecret.setSecret(secret);

        secretManagementService.updateDataEncryptionKey(vaultSecret);

    }

}
