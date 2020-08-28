package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoinspotStatusResponse {
  public final String status;

  public CoinspotStatusResponse(@JsonProperty("status") String status) {
    this.status = status;
  }
}
