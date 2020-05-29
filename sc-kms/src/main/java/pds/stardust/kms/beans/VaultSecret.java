package pds.stardust.kms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VaultSecret {

    @JsonProperty("data")
    private Map<String,String> secret;

    public Map<String, String> getSecret() {
        return secret;
    }

    public void setSecret(Map<String, String> secret) {
        this.secret = secret;
    }

}
