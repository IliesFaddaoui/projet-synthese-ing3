package pds.stardust.kms.services;

import pds.stardust.kms.beans.VaultSecretResponse;

/**
 * Interface for the Key Management Service
 * @See KeyManagementService
 */
public interface IKeyManagementService {

    /**
     * Process the Key Encryption Key (KEK) rotation. Steps are below :
     *
     * 1 - Rotate the Key Encryption Key @See rotateKeyEncryptionKey()
     * 2 - Rewrap the Data Encryption Key (DEK) @See rewrapDataEncryptionKey()
     *
     * @throws Exception If any request body cannot be serialized in JSON
     */
    void rotateKeys() throws Exception;

    /**
     * Read the Data Encryption Key (DEK) by decrypting it with the Key Encryption Key (KEK)
     *
     * @return The data Encryption Key encoded in base 64
     * @throws Exception If the decrypt request body cannot be serialized in JSON
     */
    VaultSecretResponse readDataEncryptionKey() throws Exception;

}
