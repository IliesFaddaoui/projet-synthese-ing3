package pds.stardust.kms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecryptDataResponse {

    @JsonProperty("data")
    private DecryptDataResponsePlaintext data;

    public DecryptDataResponsePlaintext getData() {
        return data;
    }

    public void setData(DecryptDataResponsePlaintext data) {
        this.data = data;
    }
}
