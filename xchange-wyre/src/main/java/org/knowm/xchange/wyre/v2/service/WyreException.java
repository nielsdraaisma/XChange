package org.knowm.xchange.wyre.v2.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import si.mazi.rescu.HttpStatusExceptionSupport;

public class WyreException extends HttpStatusExceptionSupport {

  public WyreException(@JsonProperty("message") String reason) {
    super(reason);
  }
}
