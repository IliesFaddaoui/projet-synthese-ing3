package pds.stardust.kms.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RewrapDataRequest {

    @JsonProperty("ciphertext")
    private String ciphertext;

    public RewrapDataRequest(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public RewrapDataRequest() { }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }
}
