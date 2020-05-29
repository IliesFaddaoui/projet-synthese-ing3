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
import pds.stardust.kms.beans.VaultSecret;
import pds.stardust.kms.beans.VaultSecretResponse;
import pds.stardust.kms.config.VaultConfig;
import pds.stardust.kms.constants.SecretsConstants;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SecretManagementServiceTest {

    @Mock
    private VaultConfig vaultConfig;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SecretManagementService secretManagementService;

    private final String VAULT_URL = "https://pds.stardust:8200/v1/";
    private final String VAULT_TOKEN = "00000000-0000-0000-0000-000000000000";
    private final String DATA_ENCRYPTION_KEY_SECRET_VALUE = "dmF1bHQ6djE6R3JIWTZLYjQxQ0dyOW14dVplUFZKaVFqNXZ5aDRUZVRXUlB2NFpTRUUycmU1d0MxaDRuRmtUT1Y3YTh4WXBqNTh0M1BDdm1yaEZHelovOVp3S2lUZ0Q5UC83ZEJvVmFy";
    private final String DATA_ENCRYPTION_KEY_SECRET_URL = "https://pds.stardust:8200/v1/ekeys/dek";

    @BeforeEach
    public void before() {

        Mockito.doReturn(VAULT_URL).when(vaultConfig).getUrl();
        Mockito.doReturn(VAULT_TOKEN).when(vaultConfig).getToken();

    }

    @Test
    void getDataEncryptionKey_OK() {

        final VaultSecret vaultSecret = new VaultSecret();

        vaultSecret.setSecret(new HashMap<>() {{
            put(SecretsConstants.DATA_ENCRYPTION_KEY_SECRET_KEY, DATA_ENCRYPTION_KEY_SECRET_VALUE);
        }});

        Mockito.doReturn(new ResponseEntity<>(vaultSecret, HttpStatus.OK))
                .when(restTemplate).exchange(
                Mockito.eq(DATA_ENCRYPTION_KEY_SECRET_URL),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(VaultSecret.class)
        );

        final VaultSecretResponse dataEncryptionKeySecretResponse = secretManagementService.getDataEncryptionKey();
        assertEquals(DATA_ENCRYPTION_KEY_SECRET_VALUE, dataEncryptionKeySecretResponse.getData());

    }

    @Test
    void updateDataEncryptionKey_OK() throws Exception {

        Mockito.doReturn(new ResponseEntity<>(HttpStatus.OK))
                .when(restTemplate).exchange(
                Mockito.eq(DATA_ENCRYPTION_KEY_SECRET_URL),
                Mockito.eq(HttpMethod.PUT),
                Mockito.any(HttpEntity.class),
                Mockito.eq(String.class)
        );

        final VaultSecret vaultSecret = new VaultSecret();
        final HashMap<String, String> secret = new HashMap<>();

        secret.put(SecretsConstants.DATA_ENCRYPTION_KEY_SECRET_KEY, DATA_ENCRYPTION_KEY_SECRET_VALUE);
        vaultSecret.setSecret(secret);

        secretManagementService.updateDataEncryptionKey(vaultSecret);

    }
}