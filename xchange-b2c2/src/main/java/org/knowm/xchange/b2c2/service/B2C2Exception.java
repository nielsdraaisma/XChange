package org.knowm.xchange.b2c2.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import si.mazi.rescu.HttpStatusExceptionSupport;

public class B2C2Exception extends HttpStatusExceptionSupport {

  public B2C2Exception(@JsonProperty("message") String reason) {
    super(reason);
  }
}
