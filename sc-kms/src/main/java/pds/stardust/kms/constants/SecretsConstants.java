package pds.stardust.kms.constants;

import pds.stardust.kms.common.AbstractNonInstantiableClass;

/**
 * This class defines the common constants used for Vault secret management.
 */
public class SecretsConstants extends AbstractNonInstantiableClass {

    /**
     * This constructor hide the implicit public constructor thus preventing
     * the class from being instantiated.
     */
    private SecretsConstants() {
        throw new IllegalStateException(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION);
    }

    /**
     * Vault Data Encryption Key (DEK) secret path
     */
    public final static String DATA_ENCRYPTION_KEY_SECRET_PATH = "ekeys/dek";

    /**
     * Vault Data Encryption Key (DEK) secret value key
     */
    public final static String DATA_ENCRYPTION_KEY_SECRET_KEY = "dek";

}
