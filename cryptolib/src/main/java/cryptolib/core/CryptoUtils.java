package cryptolib.core;


import cryptolib.common.AbstractNonInstantiableClass;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import static cryptolib.constants.Constants.*;

/**
 * This class provides an implementation of the AES GCM algorithm.
 * For more details on the algorithm specification @See Constants
 *
 * Methods provided are :
 *
 * @See encryptAesGcm(byte[] message, SecretKey secretKey, byte[] nonce)
 * @See decryptAesGcm(byte[] cipherMessage, SecretKey secretKey)
 * @See getRandomNonce()
 */
public class CryptoUtils extends AbstractNonInstantiableClass {

    /**
     * The cryptographically strong random number generator used to produce
     * non deterministic outputs.
     */
    private static SecureRandom secureRandom;

    /*
      Initialization block
     */
    static {
        secureRandom = new SecureRandom();
    }

    /**
     * This constructor hide the implicit public constructor thus preventing
     * the class from being instantiated.
     */
    private CryptoUtils() {
        throw new IllegalStateException(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION);
    }

    /**
     * Encrypt a message using the AES GCM algorithm with no padding. The key's
     * length must be 32 bytes (256 bits) and the nonce 12 bytes (96 bits).
     *
     * The output will be encoded to the format below.
     *
     * byte[] [x y y y y y y y y y y y y z z z ...] Where :
     *
     * - x represents the nonce length as byte
     * - y represents the nonce bytes
     * - z represents the content bytes (encrypted content nad authentication tag)
     *
     * @See buildCipherText(byte[] nonce, byte[] cipherText)
     *
     * @param message The byte representation of the message to encrypt
     * @param secretKey The key used to encrypt the message
     * @param nonce The nonce used to ensure different key streams
     * @return The encrypted message
     * @throws Exception
     */
    public static byte[] encryptAesGcm(byte[] message, SecretKey secretKey, byte[] nonce) throws Exception {

        validateInputsLength(secretKey.getEncoded().length, nonce.length);

        final Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING, BOUNCY_CASTLE_PROVIDER);

        GCMParameterSpec parameterSpec = new GCMParameterSpec(AES_GCM_AUTH_TAG_LENGTH, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        byte[] cipherText = cipher.doFinal(message);

        return buildCipherText(nonce, cipherText);
    }

    /**
     * Decrypt the cipher message using the AES GCM algorithm with no padding. The key's
     * length must be 32 bytes (256 bits).
     *
     * @param cipherMessage The encrypted message to decrypt
     * @param secretKey The key used to decrypt the cipher message
     * @return The plaintext message
     * @throws Exception
     */
    public static byte[] decryptAesGcm(byte[] cipherMessage, SecretKey secretKey) throws Exception {

        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
        int nonceLength = byteBuffer.getInt();

        validateInputsLength(secretKey.getEncoded().length, nonceLength);

        byte[] nonce = new byte[nonceLength];
        byteBuffer.get(nonce);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        final Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING, BOUNCY_CASTLE_PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey.getEncoded(), AES_ALGORITHM), new GCMParameterSpec(AES_GCM_AUTH_TAG_LENGTH, nonce));

        return cipher.doFinal(cipherText);
    }

    /**
     * Generate a random nonce
     *
     * @return A strong random 12 bytes (96 bites) nonce
     */
    public static byte[] getRandomNonce() {

        byte[] bytes = new byte[AES_GCM_NONCE_LENGTH];
        secureRandom.nextBytes(bytes);

        return bytes;
    }

    /**
     * Encode the cipher text by prepending it with the nonce's length and bytes
     *
     * @param nonce The nonce used to ensure different key streams during the encryption
     * @param cipherText The cipher text resulting from the encryption
     * @return A buffer array containing the cipher text prepended to the nonce's length and bytes
     */
    private static byte[] buildCipherText(byte[] nonce, byte[] cipherText) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(NONCE_LENGTH_BUFFER_SIZE + nonce.length + cipherText.length);

        byteBuffer.putInt(nonce.length);
        byteBuffer.put(nonce);
        byteBuffer.put(cipherText);

        return byteBuffer.array();
    }

    /**
     * Validate the length of the key and nonce provided to encrypt the data.
     * Key's length must be 32 bytes (256 bits) and nonce's length 12 bytes (96 bits)
     *
     * @param secretKeyLength The secret key's length
     * @param nonceLength The nonce length's
     */
    private static void validateInputsLength(int secretKeyLength, int nonceLength) {

        if (secretKeyLength != AES_GCM_KEY_LENGTH) {
            throw new IllegalArgumentException(INVALID_KEY_LENGTH_EXCEPTION);
        }

        if (nonceLength != AES_GCM_NONCE_LENGTH) {
            throw new IllegalArgumentException(INVALID_NONCE_LENGTH_EXCEPTION);
        }

    }

}
