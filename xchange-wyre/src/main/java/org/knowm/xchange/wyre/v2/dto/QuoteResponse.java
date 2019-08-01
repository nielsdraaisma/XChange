package org.knowm.xchange.wyre.v2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class QuoteResponse {
  @JsonProperty public final BigDecimal rate;

  public QuoteResponse(BigDecimal rate) {
    this.rate = rate;
  }
}
