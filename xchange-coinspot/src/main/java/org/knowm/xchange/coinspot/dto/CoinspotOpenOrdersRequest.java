package org.knowm.xchange.coinspot.dto;

public class CoinspotOpenOrdersRequest extends CoinspotRequest {
  public final String cointype;

  public CoinspotOpenOrdersRequest(long nonce, String cointype) {
    super(nonce);
    this.cointype = cointype;
  }
}
