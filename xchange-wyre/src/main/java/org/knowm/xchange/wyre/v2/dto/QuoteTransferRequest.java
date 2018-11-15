package org.knowm.xchange.wyre.v2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteTransferRequest extends TransferRequest {
  @JsonProperty public final Boolean preview = true;

  public QuoteTransferRequest(
      String source,
      String sourceCurrency,
      String sourceAmount,
      String dest,
      String destAmount,
      String destCurrency) {
    super(source, sourceCurrency, sourceAmount, dest, destAmount, destCurrency);
  }
}
