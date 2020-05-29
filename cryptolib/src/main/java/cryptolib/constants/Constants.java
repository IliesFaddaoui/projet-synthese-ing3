package cryptolib.constants;

import static cryptolib.common.AbstractNonInstantiableClass.NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION;

/**
 * This class defines the common constants used for the encryption/decryption
 * of sensitive data by providing the algorithm and it specification such as
 * the provider, the encryption mode, the nonce and key' length and so on.
 */
public class Constants {

    /**
     * This constructor hide the implicit public constructor thus preventing
     * the class from being instantiated.
     */
    private Constants() {
        throw new IllegalStateException(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION);
    }

    /**
     * Defines the provider that will be used to get the algorithm from.
     * Here the Bouncy Castle Provider.
     */
    public static final String BOUNCY_CASTLE_PROVIDER = "BC";

    /**
     * Defines the algorithm that will be used to encrypt/decrypt sensitive
     * data. Here the Advanced Encryption Standard (AES)
     */
    public static final String AES_ALGORITHM = "AES";

    /**
     * Defines the AES mode and padding. Here, the Galois/Counter mode will be
     * used instead of the ECB and CBC modes. GCM mode does not require padding.
     */
    public static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";

    /**
     * Defines the authentication tag length. The authentication tag is used to
     * prove the authentication and integrity of the message. It size must be
     * 128 bits in order to eliminate the risk of someone altering the message.
     */
    public static final int AES_GCM_AUTH_TAG_LENGTH = 128;

    /**
     * Defines the length of the nonce that will be used to ensure different key
     * streams. Here 12 bytes (96 bits) which is the recommended size for AES GCM.
     * Note that the nonce should never be reused with the same key and should be
     * truly random.
     */
    public static final int AES_GCM_NONCE_LENGTH = 12;

    /**
     * Defines the buffer size to store the nonce length. Here 4 bytes as integer
     * are commonly stored using a word of memory.
     */
    public static final int NONCE_LENGTH_BUFFER_SIZE = 4;

    /**
     * Defines the length of the key that will be used to encrypt/decrypt the data.
     * Here 32 bytes (256 bits)
     */
    public static final int AES_GCM_KEY_LENGTH = 32;

    /**
     * Defines the exception message that will be shown in case of a nonce
     * not meeting the requirement of the {AES_GCM_NONCE_LENGTH} constant.
     */
    public static final String INVALID_NONCE_LENGTH_EXCEPTION = "Invalid nonce length";

    /**
     * Defines the exception message that will be shown in case of a key
     * not meeting the requirement of the {AES_GCM_KEY_LENGTH} constant.
     */
    public static final String INVALID_KEY_LENGTH_EXCEPTION = "Invalid key length";

}
