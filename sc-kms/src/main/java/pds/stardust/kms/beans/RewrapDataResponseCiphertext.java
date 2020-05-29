package pds.stardust.kms.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RewrapDataResponseCiphertext {

    @JsonProperty("ciphertext")
    private String ciphertext;

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }
}
