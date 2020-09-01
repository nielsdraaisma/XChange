package org.knowm.xchange.omf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SearchRequest {
  @JsonProperty("DEFAULTTRADERID")
  public final String defaultTraderId = "DEFAULT";

  @JsonProperty("mode")
  public final String mode = "SEARCH";

  @JsonProperty("OUTSTANDINGTRADESIND")
  public final String outstandingTradesInd = "false";

  @JsonProperty("userType")
  public final String userType = "EXTERNAL";

  @JsonProperty("TRADENUMBER")
  public final String tradeNumber;

  @JsonProperty("TRADENUMBERTO")
  public final String tradeNumberTo;

  public SearchRequest(String tradeNumber, String tradeNumberTo) {
    this.tradeNumber = tradeNumber;
    this.tradeNumberTo = tradeNumberTo;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("defaultTraderId", defaultTraderId)
        .append("mode", mode)
        .append("outstandingTradesInd", outstandingTradesInd)
        .append("userType", userType)
        .append("tradeNumber", tradeNumber)
        .append("tradeNumberTo", tradeNumberTo)
        .toString();
  }
}
