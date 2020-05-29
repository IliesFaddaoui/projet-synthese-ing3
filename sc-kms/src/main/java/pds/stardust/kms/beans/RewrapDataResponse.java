package pds.stardust.kms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RewrapDataResponse {

    @JsonProperty("data")
    private RewrapDataResponseCiphertext data;

    public RewrapDataResponseCiphertext getData() {
        return data;
    }

    public void setData(RewrapDataResponseCiphertext data) {
        this.data = data;
    }
}
