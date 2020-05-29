package pds.stardust.kms.services;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the Key Management Resource
 */
public class KeyManagementResourceIT extends AbstractVaultIT {

    @Test
    public void getDataEncryptionKey_OK() throws Exception {
        assertDataEncryptionKeyEquals();
    }

    @Test
    public void rotateKeys_OK() throws Exception {

        assertKeyEncryptionKeyVersion(1, readKeyEncryptionKeyProperties());

        assertDataEncryptionKey("vault:v1", readDataEncryptionKeySecret());

        assertDataEncryptionKeyEquals();

        mvc.perform(post("/keys/rotate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertDataEncryptionKeyEquals();

        assertKeyEncryptionKeyVersion(2, readKeyEncryptionKeyProperties());

        assertDataEncryptionKey("vault:v2", readDataEncryptionKeySecret());

    }

}
