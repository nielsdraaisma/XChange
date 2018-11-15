package org.knowm.xchange.wyre.v2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteRequest {
  @JsonProperty public final String sourceCurrency;
  @JsonProperty public final String sourceAmount;
  @JsonProperty public final String destCurrency;

  public QuoteRequest(String sourceCurrency, String sourceAmount, String destCurrency) {
    this.sourceCurrency = sourceCurrency;
    this.sourceAmount = sourceAmount;
    this.destCurrency = destCurrency;
  }
}
