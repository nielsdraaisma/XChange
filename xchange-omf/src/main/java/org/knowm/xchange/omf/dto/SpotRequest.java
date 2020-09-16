package org.knowm.xchange.omf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SpotRequest {
  @JsonProperty("CCYID1")
  public final String ccyid1;

  @JsonProperty("CCYID2")
  public final String ccyid2;

  /** BUY / SELL */
  @JsonProperty("CUSTOMERDIRECTION")
  public final String customerDirection;

  @JsonProperty("DIRECTION")
  public final String direction;

  @JsonProperty("DEFAULTTRADERID")
  public final String defaultTraderId = "DEFAULT";

  @JsonProperty("mode")
  public final String mode = "ADD";

  @JsonProperty("TRANSACTIONTYPE")
  public final String transactionType = "SPOT";

  @JsonProperty("userType")
  public final String userType = "EXTERNAL";

  @JsonProperty("VALUEDATE")
  public final String valueDate = "SPOT";

  /** Quantity is expressed in CCYID1 */
  @JsonProperty("QUANTITY")
  public final String quantity;

  @JsonProperty("CUSTOMERID")
  public final String customerId;

  @JsonProperty("CUSTOMERMNEMONIC")
  public final String customerMNemonic;

  @JsonProperty("EXTERNALREFERENCEID")
  public final String externalReferenceId;

  public SpotRequest(
      String ccyid1,
      String ccyid2,
      String customerDirection,
      String direction,
      String quantity,
      String customerId,
      String customerMNemonic,
      String externalReferenceId) {
    this.ccyid1 = ccyid1;
    this.ccyid2 = ccyid2;
    this.customerDirection = customerDirection;
    this.direction = direction;
    this.quantity = quantity;
    this.customerId = customerId;
    this.customerMNemonic = customerMNemonic;
    this.externalReferenceId = externalReferenceId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("ccyid1", ccyid1)
        .append("ccyid2", ccyid2)
        .append("customerDirection", customerDirection)
        .append("direction", direction)
        .append("defaultTraderId", defaultTraderId)
        .append("mode", mode)
        .append("transactionType", transactionType)
        .append("userType", userType)
        .append("valueDate", valueDate)
        .append("quantity", quantity)
        .append("customerId", customerId)
        .append("customerMNemonic", customerMNemonic)
        .append("externalReferenceId", externalReferenceId)
        .toString();
  }
}
