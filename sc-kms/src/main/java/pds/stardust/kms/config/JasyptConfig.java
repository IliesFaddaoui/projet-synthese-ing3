package pds.stardust.kms.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * This class defines the JasyptConfig bean that will be used to decrypt the properties
 */
@Configuration
public class JasyptConfig {

    /**
     * Defines the algorithm used to decrypt the properties.
     */
    private final String JASYPT_ENCRYPTOR_ALGORITHM = "PBEWITHSHA256AND128BITAES-CBC-BC";

    /**
     * Defines the environment variable containing the password
     * that have been used to encrypt the properties.
     */
    private final String JASYPT_ENCRYPTOR_PASSWORD = "JASYPT_ENCRYPTOR_PASSWORD";

    /**
     * Springboot environment to read properties
     */
    private final Environment env;

    @Autowired
    public JasyptConfig(Environment env) {
        this.env = env;
    }

    @Bean(name = "jasyptEncryptor")
    public StringEncryptor stringEncryptor() {

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(env.getProperty(JASYPT_ENCRYPTOR_PASSWORD));
        config.setAlgorithm(JASYPT_ENCRYPTOR_ALGORITHM);
        config.setProviderName("BC");
        config.setPoolSize(1);

        encryptor.setConfig(config);

        return encryptor;
    }

}
