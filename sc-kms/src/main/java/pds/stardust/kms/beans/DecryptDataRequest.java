package pds.stardust.kms.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecryptDataRequest {

    @JsonProperty("ciphertext")
    private String ciphertext;

    public DecryptDataRequest(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public DecryptDataRequest() { }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }
}
