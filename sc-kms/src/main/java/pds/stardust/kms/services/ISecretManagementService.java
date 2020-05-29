package pds.stardust.kms.services;

import pds.stardust.kms.beans.VaultSecret;
import pds.stardust.kms.beans.VaultSecretResponse;

/**
 * Interface for the Secret Management Service
 * @See SecretManagementService
 */
public interface ISecretManagementService {

    /**
     * Retrieve The Data Encryption Key (DEK) from Vault
     *
     * @return The Data Encryption Key encoded in base 64
     */
    VaultSecretResponse getDataEncryptionKey();

    /**
     * Update the Data Encryption Key (DEK) secret. Will be called during the key
     * rotation process.
     *
     * @param vaultSecret  The updated Vault Data Encryption Key secret
     * @throws Exception If the secret cannot be serialized in JSON
     */
    void updateDataEncryptionKey(VaultSecret vaultSecret) throws Exception;

}
