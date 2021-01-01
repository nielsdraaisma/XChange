package org.knowm.xchange.btcmarkets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import si.mazi.rescu.HttpStatusExceptionSupport;

public class BTCMarketsException extends HttpStatusExceptionSupport {

  public final Boolean success;
  public final Integer errorCode;
  public final String errorMessage;
  public final String clientRequestId;
  public final Long id;
  public final List<BTCMarketsException> responses;

  public BTCMarketsException(
      @JsonProperty("success") Boolean success,
      @JsonProperty("errorMessage") String errorMessage,
      @JsonProperty("errorCode") Integer errorCode,
      @JsonProperty("clientRequestId") String clientRequestId,
      @JsonProperty("id") Long id,
      @JsonProperty("responses") List<BTCMarketsException> responses) {
    super(constructMsg(errorMessage, responses));
    this.success = success;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.clientRequestId = clientRequestId;
    this.id = id;
    this.responses = responses;
  }

  public BTCMarketsException(
          @JsonProperty("code") String code,
          @JsonProperty("message") String message
  ) {
    this.success = false;
    this.errorCode = null;
    this.errorMessage = code;
    this.clientRequestId = null;
    this.id = null;
    this.responses = null;

  }


  private static String constructMsg(String errorMessage, List<BTCMarketsException> responses) {
    final StringBuilder sb = new StringBuilder();
    if (errorMessage != null) {
      sb.append(errorMessage).append(": ");
    }
    if (responses != null) {
      for (BTCMarketsException response : responses) {
        if (!Boolean.TRUE.equals(response.success)) {
          sb.append(String.format("Id %d: %s", response.id, response.getMessage()));
        }
      }
    }
    return sb.toString();
  }

}
