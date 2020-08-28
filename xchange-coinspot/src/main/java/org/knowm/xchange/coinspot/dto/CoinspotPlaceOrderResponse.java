package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinspotPlaceOrderResponse {
  public final String id;

  public CoinspotPlaceOrderResponse(@JsonProperty("_id") String id) {
    this.id = id;
  }
}
