package automationframeworkexample.clients.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BonusCounts {
    @JsonProperty("All")
    private int all;
    @JsonProperty("RiskFree")
    private int riskFree;
    @JsonProperty("RealMoney")
    private int realMoney;
    @JsonProperty("FreeSpin")
    private int freeSpin;

    // getters / setters …

    @Override
    public String toString() {
        return "BonusCounts{all=" + all +
                ", riskFree=" + riskFree +
                ", realMoney=" + realMoney +
                ", freeSpin=" + freeSpin + '}';
    }
}
