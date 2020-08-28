package org.knowm.xchange.coinspot.dto;

public class CoinspotCancelOrderRequest extends CoinspotRequest {

  public final String id;

  public CoinspotCancelOrderRequest(long nonce, String id) {
    super(nonce);
    this.id = id;
  }
}
