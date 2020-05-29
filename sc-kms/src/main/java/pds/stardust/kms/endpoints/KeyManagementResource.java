package pds.stardust.kms.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pds.stardust.kms.beans.VaultSecretResponse;
import pds.stardust.kms.services.KeyManagementService;

/**
 * This class defines the API endpoints for keys management. Exposed endpoints are :
 * - /keys/rotate for key Encryption Key (KEK) rotation process @See rotateKeys()
 * - /keys/dek to read the Data Encryption Key (DEK) @See readDataEncryptionKey()
 */
@RestController
public class KeyManagementResource {

    /**
     * The key management service
     */
    private final KeyManagementService keyManagementService;

    @Autowired
    public KeyManagementResource(KeyManagementService keyManagementService) {
        this.keyManagementService = keyManagementService;
    }

    /**
     * Process the Key Encryption Key (KEK) rotation. Steps are below :
     *
     * 1 - Rotate the Key Encryption Key @See rotateKeyEncryptionKey()
     * 2 - Rewrap the Data Encryption Key (DEK) @See rewrapDataEncryptionKey()
     *
     * @throws Exception If any request body cannot be serialized in JSON
     */
    @PostMapping("keys/rotate")
    public void rotateKeys() throws Exception {
        keyManagementService.rotateKeys();
    }

    /**
     * Read the Data Encryption Key (DEK) by decrypting it with the Key Encryption Key (KEK)
     *
     * @return The data Encryption Key encoded in base 64
     * @throws Exception If the decrypt request body cannot be serialized in JSON
     */
    @GetMapping("keys/dek")
    public VaultSecretResponse readDataEncryptionKey() throws Exception {
        return keyManagementService.readDataEncryptionKey();
    }

}
