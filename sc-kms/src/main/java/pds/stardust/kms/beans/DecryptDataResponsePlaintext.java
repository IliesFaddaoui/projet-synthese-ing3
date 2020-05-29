package pds.stardust.kms.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecryptDataResponsePlaintext {

    @JsonProperty("plaintext")
    private String plaintext;

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }
}
