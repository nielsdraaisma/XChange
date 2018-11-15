package org.knowm.xchange.wyre.v2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRequest {
  @JsonProperty public final String source;
  @JsonProperty public final String sourceCurrency;
  @JsonProperty public final String sourceAmount;
  @JsonProperty public final String destAmount;
  @JsonProperty public final String dest;
  @JsonProperty public final String destCurrency;
  @JsonProperty public final Boolean autoConfirm = true;

  public TransferRequest(
      String source,
      String sourceCurrency,
      String sourceAmount,
      String dest,
      String destAmount,
      String destCurrency) {
    this.source = source;
    this.sourceCurrency = sourceCurrency;
    this.sourceAmount = sourceAmount;
    this.dest = dest;
    this.destAmount = destAmount;
    this.destCurrency = destCurrency;
  }
}
