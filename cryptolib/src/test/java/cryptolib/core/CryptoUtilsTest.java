package cryptolib.core;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;

import static cryptolib.constants.Constants.*;
import static cryptolib.common.AbstractNonInstantiableClass.NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CryptoUtilsTest {

    private static SecureRandom secureRandom;
    private static SecretKey secretKey;
    private static String cipherText;
    private static String plainText;

    @BeforeAll
    static void beforeAll() {
        Security.addProvider(new BouncyCastleProvider());
        secureRandom = new SecureRandom();
        secretKey = generateSecretKey(32);
        plainText = "MESSAGE TO ENCRYPT";
        cipherText = "";
    }

    @Test
    @Order(1)
    void getRandomNonce_OK() {
        byte[] nonce = CryptoUtils.getRandomNonce();
        assertEquals(12, nonce.length);
    }

    @Test
    @Order(2)
    void encryptAesGcm_OK() throws Exception {

        assertEquals(32, secretKey.getEncoded().length);

        byte[] nonce = CryptoUtils.getRandomNonce();

        assertEquals(12, nonce.length);

        cipherText = Base64.toBase64String(CryptoUtils.encryptAesGcm(plainText.getBytes(StandardCharsets.UTF_8), secretKey, nonce));

    }

    @Test
    @Order(3)
    void decryptAesGcm_OK() throws Exception {

        byte[] decrypted = CryptoUtils.decryptAesGcm(Base64.decode(cipherText), secretKey);

        assertEquals(plainText, new String(decrypted, StandardCharsets.UTF_8));

    }

    @Test
    @Order(4)
    void encryptAesGcm_KO_invalid_nonce_length() {

        assertEquals(32, secretKey.getEncoded().length);

        byte[] nonce = getRandomBytesPrimitive(8);

        Exception exception = assertThrows(RuntimeException.class, () -> CryptoUtils.encryptAesGcm(plainText.getBytes(StandardCharsets.UTF_8), secretKey, nonce));

        assertEquals(INVALID_NONCE_LENGTH_EXCEPTION, exception.getMessage());

    }

    @Test
    @Order(5)
    void encryptAesGcm_KO_invalid_key_length() {

        SecretKey invalidSecretKey = generateSecretKey(16);

        byte[] nonce = CryptoUtils.getRandomNonce();

        Exception exception = assertThrows(RuntimeException.class, () -> CryptoUtils.encryptAesGcm(plainText.getBytes(StandardCharsets.UTF_8), invalidSecretKey, nonce));

        assertEquals(INVALID_KEY_LENGTH_EXCEPTION, exception.getMessage());

    }

    @Test
    @Order(6)
    void decryptAesGcm_KO_invalid_nonce_length() {

        byte[] nonce = getRandomBytesPrimitive(8);

        ByteBuffer byteBuffer = ByteBuffer.allocate(NONCE_LENGTH_BUFFER_SIZE + nonce.length);

        byteBuffer.putInt(nonce.length);
        byteBuffer.put(nonce);

        byte[] invalidCipherText = byteBuffer.array();

        Exception exception = assertThrows(RuntimeException.class, () -> CryptoUtils.encryptAesGcm(invalidCipherText, secretKey, nonce));

        assertEquals(INVALID_NONCE_LENGTH_EXCEPTION, exception.getMessage());

    }

    @Test
    @Order(7)
    void decryptAesGcm_KO_invalid_key_length() {

        SecretKey invalidSecretKey = generateSecretKey(16);

        Exception exception = assertThrows(RuntimeException.class, () -> CryptoUtils.decryptAesGcm(Base64.decode(cipherText), invalidSecretKey));

        assertEquals(INVALID_KEY_LENGTH_EXCEPTION, exception.getMessage());

    }

    @Test
    void cryptoUtilsInstantiation_KO() {

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {

            Constructor<CryptoUtils> constructor = CryptoUtils.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();

        });

        assertEquals(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION, exception.getTargetException().getMessage());
    }

    public static SecretKey generateSecretKey(int size) {
        byte[] key = getRandomBytesPrimitive(size);
        //Arrays.fill(key,(byte) 0);
        return new SecretKeySpec(key, AES_ALGORITHM);
    }

    private static byte[] getRandomBytesPrimitive(int size) {
        byte[] bytes = new byte[size];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

}