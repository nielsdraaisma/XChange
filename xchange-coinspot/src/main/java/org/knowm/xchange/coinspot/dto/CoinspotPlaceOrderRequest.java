package org.knowm.xchange.coinspot.dto;

import java.math.BigDecimal;

public class CoinspotPlaceOrderRequest extends CoinspotRequest {

  public final String cointype;
  public final BigDecimal amount;
  public final BigDecimal rate;

  public CoinspotPlaceOrderRequest(
      long nonce, String cointype, BigDecimal amount, BigDecimal rate) {
    super(nonce);
    this.cointype = cointype;
    this.amount = amount;
    this.rate = rate;
  }
}
