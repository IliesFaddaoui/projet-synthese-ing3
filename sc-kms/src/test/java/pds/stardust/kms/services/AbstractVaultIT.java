package pds.stardust.kms.services;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.vault.VaultContainer;
import pds.stardust.kms.utils.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
            "vault.token=00000000-0000-0000-0000-000000000000"
        })

/**
 * This class setup a Vault Docker container to run integration tests
 */
public abstract class AbstractVaultIT {

    @ClassRule
    public static VaultContainer<?> vaultContainer = new VaultContainer<>().withVaultToken("00000000-0000-0000-0000-000000000000");

    @Autowired
    MockMvc mvc;

    static String dataEncryptionKey;
    static ObjectMapper mapper;

    @BeforeAll
    /**
     *  Init the Vault container. Steps are below :
     *
     * - Create the vault secrets (ekeys) and transit (km) engines
     * - Generate a Key Encryption Key (KEK) and Data Encryption Key (DEK)
     * - Encrypt the Data Encryption Key with the Key Encryption Key
     * - Encode the result in base 64 and store it in dek secrets
     */
    public static void beforeAll() throws Exception {

        mapper = new ObjectMapper();

        vaultContainer.start();

        vaultContainer.execInContainer("vault", "secrets", "enable", "-version=1", "-path=ekeys", "kv");
        vaultContainer.execInContainer("vault", "secrets", "enable", "-path=km", "transit");
        vaultContainer.execInContainer("vault", "write", "-f", "km/keys/kek");
        vaultContainer.execInContainer("vault", "write", "-f", "km/keys/dek", "exportable=true");

        dataEncryptionKey = getDataEncryptionKey();

        setDataEncryptionKeySecret(
                StringUtils.stringToBase64(
                        encryptDataEncryptionKeyWitkKeyEncryptionKey()
                )
        );

        System.setProperty("vault.url", String.format("http://%s:%d/v1/", vaultContainer.getContainerIpAddress(), vaultContainer.getFirstMappedPort()));
    }

    /**
     * Assert the Data Encryption Key version
     *
     * @param version The expected version
     * @param dekProperties The Data Encryption Key properties
     * @throws Exception If the key' properties cannot be read
     */
    void assertDataEncryptionKey(String version, String dekProperties) throws Exception {

        String decodedKeyEncryptionKey = StringUtils.base64ToString(mapper.readTree(dekProperties).get("data").get("dek").asText());

        assertTrue(decodedKeyEncryptionKey.contains(version));
    }

    /**
     * Assert the Key Encryption Key version
     *
     * @param version The expected version
     * @param kekProperties The Data Encryption Key properties
     * @throws Exception If the key' properties cannot be read
     */
    void assertKeyEncryptionKeyVersion(int version, String kekProperties) throws Exception {
        assertEquals(version, mapper.readTree(kekProperties).get("data").get("latest_version").asInt());
    }

    /**
     * Assert that the initial Data Encryption Key is the same as the one stored in Vault
     *
     * @throws Exception If the request fail
     */
    void assertDataEncryptionKeyEquals() throws Exception {

        mvc.perform(get("/keys/dek")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("data").value(dataEncryptionKey));
    }

    /**
     * Read the Key Encryption Key (KEK) properties
     *
     * @return The KEK properties in JSON
     * @throws Exception If the exec command fail
     */
    String readKeyEncryptionKeyProperties() throws Exception {
        return vaultContainer.execInContainer("vault", "read", "km/keys/kek", "-format=json").getStdout().replace("\n", "");
    }

    /**
     * Read the Data Encryption Key (DEK) properties
     *
     * @return The DEK properties in JSON
     * @throws Exception If the exec command fail
     */
    String readDataEncryptionKeySecret() throws Exception {
        return vaultContainer.execInContainer("vault", "read", "ekeys/dek", "-format=json").getStdout().replace("\n", "");
    }

    /**
     * Set the Data Encryption Key (DEK) secret' value
     *
     * @param dataEncryptionKeyValue The DEK secret' value
     * @throws Exception If the exec command fail
     */
    private static void setDataEncryptionKeySecret(String dataEncryptionKeyValue) throws Exception {
        vaultContainer.execInContainer("vault", "kv", "put", "ekeys/dek", String.format("dek=%s", dataEncryptionKeyValue));
    }

    /**
     * Encrypt the Data Encryption Key (DEK) with the Key Encryption Key (KEK)
     *
     * @return The DEK encrypted
     * @throws Exception If the exec command fail
     */
    private static String encryptDataEncryptionKeyWitkKeyEncryptionKey() throws Exception {

        String stdout = vaultContainer.execInContainer("vault", "write", "km/encrypt/kek", String.format("plaintext=%s", dataEncryptionKey), "-format=json")
                .getStdout()
                .replace("\n", "");

        return mapper.readTree(stdout).get("data").get("ciphertext").asText();
    }

    /**
     * Get the Data Encryption Key (DEK)
     *
     * @return The DEK
     * @throws Exception If the exec command fail
     */
    private static String getDataEncryptionKey() throws Exception {

        String stdout = vaultContainer.execInContainer("vault", "read", "km/export/encryption-key/dek/1", "-format=json")
                        .getStdout()
                        .replace("\n", "");


        return mapper.readTree(stdout).get("data").get("keys").get("1").asText();
    }

}
