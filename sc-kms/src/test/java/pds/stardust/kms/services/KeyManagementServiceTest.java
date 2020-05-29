package pds.stardust.kms.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pds.stardust.kms.beans.*;
import pds.stardust.kms.config.VaultConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class KeyManagementServiceTest {

    @Mock
    private VaultConfig vaultConfig;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SecretManagementService secretManagementService;

    @InjectMocks
    private KeyManagementService keyManagementService;

    private final String VAULT_URL = "https://pds.stardust:8200/v1/";
    private final String VAULT_TOKEN = "00000000-0000-0000-0000-000000000000";
    private final String DATA_ENCRYPTION_KEY = "LgVzcCc0DXEnDAe62GcRYpT5+vklGE9tVeSBgFYy2Ds=";
    private final String KEY_ENCRYPTION_KEY_REWRAP_URL = "https://pds.stardust:8200/v1/km/rewrap/kek";
    private final String KEY_ENCRYPTION_KEY_DECRYPT_URL = "https://pds.stardust:8200/v1/km/decrypt/kek";
    private final String KEY_ENCRYPTION_KEY_ROTATE_URL = "https://pds.stardust:8200/v1/km/keys/kek/rotate";
    private final String DATA_ENCRYPTION_KEY_REWRAP_RESPONSE_VALUE = "vault:v2:GrHY6Kb41CGr9mxuZePVJiQj5vyh4TeTWRPv4ZSEE2re5wC1h4nFkTOV7a8xYpj58t3PCvmrhFGzZ/9ZwKiTgD9P/7dBoVar";
    private final String DATA_ENCRYPTION_KEY_SECRET_VALUE = "dmF1bHQ6djE6R3JIWTZLYjQxQ0dyOW14dVplUFZKaVFqNXZ5aDRUZVRXUlB2NFpTRUUycmU1d0MxaDRuRmtUT1Y3YTh4WXBqNTh0M1BDdm1yaEZHelovOVp3S2lUZ0Q5UC83ZEJvVmFy";


    @BeforeEach
    public void before() {

        Mockito.doReturn(VAULT_URL).when(vaultConfig).getUrl();
        Mockito.doReturn(VAULT_TOKEN).when(vaultConfig).getToken();

        final VaultSecretResponse geDataEncryptionKeySecretResponse = new VaultSecretResponse();
        geDataEncryptionKeySecretResponse.setData(DATA_ENCRYPTION_KEY_SECRET_VALUE);

        Mockito.doReturn(geDataEncryptionKeySecretResponse).when(secretManagementService).getDataEncryptionKey();

    }

    @Test
    void rotateKeys() throws Exception {

        Mockito.doReturn(new ResponseEntity<>(HttpStatus.OK))
                .when(restTemplate).exchange(
                Mockito.eq(KEY_ENCRYPTION_KEY_ROTATE_URL),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class),
                Mockito.eq(String.class)
        );

        final RewrapDataResponse rewrapDataResponse = new RewrapDataResponse();
        final RewrapDataResponseCiphertext rewrapDataResponseCiphertext = new RewrapDataResponseCiphertext();
        rewrapDataResponseCiphertext.setCiphertext(DATA_ENCRYPTION_KEY_REWRAP_RESPONSE_VALUE);
        rewrapDataResponse.setData(rewrapDataResponseCiphertext);

        Mockito.doReturn(new ResponseEntity<>(rewrapDataResponse, HttpStatus.OK))
                .when(restTemplate).exchange(
                Mockito.eq(KEY_ENCRYPTION_KEY_REWRAP_URL),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class),
                Mockito.eq(RewrapDataResponse.class)
        );

        Mockito.doNothing().when(secretManagementService).updateDataEncryptionKey(Mockito.any(VaultSecret.class));

        keyManagementService.rotateKeys();

    }

    @Test
    void readDataEncryptionKey() throws Exception {

        final DecryptDataResponse decryptDataResponse = new DecryptDataResponse();
        final DecryptDataResponsePlaintext decryptDataResponsePlaintext = new DecryptDataResponsePlaintext();
        decryptDataResponsePlaintext.setPlaintext(DATA_ENCRYPTION_KEY);

        decryptDataResponse.setData(decryptDataResponsePlaintext);

        Mockito.doReturn(new ResponseEntity<>(decryptDataResponse, HttpStatus.OK))
                .when(restTemplate).exchange(
                Mockito.eq(KEY_ENCRYPTION_KEY_DECRYPT_URL),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class),
                Mockito.eq(DecryptDataResponse.class)
        );

        final VaultSecretResponse readDataEncryptionKeyResponse = keyManagementService.readDataEncryptionKey();
        assertEquals(DATA_ENCRYPTION_KEY, readDataEncryptionKeyResponse.getData());

    }
}