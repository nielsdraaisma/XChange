package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoinspotRequest {
  @JsonProperty("nonce")
  public final long nonce;

  public CoinspotRequest(long nonce) {
    this.nonce = nonce;
  }
}
