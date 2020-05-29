package pds.stardust.kms.constants;

import pds.stardust.kms.common.AbstractNonInstantiableClass;

/**
 * This class defines the common constants used for Vault key management.
 */
public class KeysConstants extends AbstractNonInstantiableClass {

    /**
     * This constructor hide the implicit public constructor thus preventing
     * the class from being instantiated.
     */
    private KeysConstants() {
        throw new IllegalStateException(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION);
    }

    /**
     * Vault API endpoint to rotate the Key Encryption Key (KEK)
     */
    public final static String KEY_ENCRYPTION_KEY_ROTATE_PATH = "km/keys/kek/rotate";

    /**
     * Vault API endpoint to rewrap data with the Key Encryption Key (KEK) after the key rotation
     */
    public final static String KEY_ENCRYPTION_KEY_REWRAP_PATH = "km/rewrap/kek";

    /**
     * Vault API endpoint to decrypt data with the Key Encryption Key (KEK)
     */
    public final static String KEY_ENCRYPTION_KEY_DECRYPT_PATH = "km/decrypt/kek";

}
