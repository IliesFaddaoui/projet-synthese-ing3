package pds.stardust.kms.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VaultSecretResponse {

    @JsonProperty("data")
    public String data;

    public VaultSecretResponse(String data) {
        this.data = data;
    }

    public VaultSecretResponse() { }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
