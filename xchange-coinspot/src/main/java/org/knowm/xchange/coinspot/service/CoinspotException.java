package org.knowm.xchange.coinspot.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import si.mazi.rescu.HttpStatusExceptionSupport;

public class CoinspotException extends HttpStatusExceptionSupport {

  public CoinspotException(@JsonProperty("message") String reason) {
    super(reason);
  }
}
