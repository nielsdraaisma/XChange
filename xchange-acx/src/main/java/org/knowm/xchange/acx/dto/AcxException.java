package org.knowm.xchange.acx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import si.mazi.rescu.HttpStatusExceptionSupport;

public class AcxException extends HttpStatusExceptionSupport {

  public final Error error;

  public AcxException(@JsonProperty("error") Error error) {
    super(error != null ? error.message : null);
    this.error = error;
  }

  public static class Error {
    public final Long code;
    public final String message;

    public Error(@JsonProperty("code") Long code, @JsonProperty("message") String message) {
      this.code = code;
      this.message = message;
    }
  }
}
