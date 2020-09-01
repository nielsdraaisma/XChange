package org.knowm.xchange.omf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderFillRequest {
  @JsonProperty("userType")
  public final String userType = "EXTERNAL";

  @JsonProperty("mode")
  public final String mode = "FILL";

  @JsonProperty("DEFAULTTRADERID")
  public final String defaultTraderId = "DEFAULT";

  @JsonProperty("FWDPOINTS")
  public final String fwdPoints = "0";

  @JsonProperty("TRADEHEADERGUID")
  public final String tradeHeaderGuid;

  @JsonProperty("CUSTOMERFWDRATE")
  public final String customerFwdRate;

  @JsonProperty("FWDRATE")
  public final String fwdRate;

  @JsonProperty("SPOTRATE")
  public final String spotRate;

  public OrderFillRequest(
      String tradeHeaderGuid, String customerFwdRate, String fwdRate, String spotRate) {
    this.tradeHeaderGuid = tradeHeaderGuid;
    this.customerFwdRate = customerFwdRate;
    this.fwdRate = fwdRate;
    this.spotRate = spotRate;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("userType", userType)
        .append("mode", mode)
        .append("defaultTraderId", defaultTraderId)
        .append("fwdPoints", fwdPoints)
        .append("tradeHeaderGuid", tradeHeaderGuid)
        .append("customerFwdRate", customerFwdRate)
        .append("fwdRate", fwdRate)
        .append("spotRate", spotRate)
        .toString();
  }
}
