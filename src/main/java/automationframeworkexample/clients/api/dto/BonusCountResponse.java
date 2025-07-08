package automationframeworkexample.clients.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BonusCountResponse {

    private String error;

    @JsonProperty("error_code")
    private String errorCodeLegacy;

    private InnerResponse response;

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InnerResponse {

        private int errorCode;
        private String errorText;
        private BonusCounts response;
    }
}
