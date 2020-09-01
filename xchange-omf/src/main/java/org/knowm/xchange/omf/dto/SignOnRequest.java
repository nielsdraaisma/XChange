package org.knowm.xchange.omf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SignOnRequest {

  @JsonProperty("FORCESIGNON")
  public final String forceSignOn = "true";

  @JsonProperty("USERID")
  public final String userId;

  @JsonProperty("CODE")
  public final String code;

  public SignOnRequest(String userId, String code) {
    this.userId = userId;
    this.code = code;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("forceSignOn", forceSignOn)
        .append("userId", userId)
        .append("code", code)
        .toString();
  }
}
