package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;
import org.knowm.xchange.currency.Currency;

public class CoinspotBalancesResponse extends CoinspotStatusResponse {

  public final Map<Currency, BigDecimal> balance;

  public CoinspotBalancesResponse(
      @JsonProperty("status") String status,
      @JsonProperty("balance") Map<Currency, BigDecimal> balance) {
    super(status);
    this.balance = balance;
  }
}
