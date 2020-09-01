package org.knowm.xchange.omf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotResponse {
  public final String cc1ccy2spotRate;
  public final String cc1ccy2fwdRate;
  public final String customerCc2Amount;
  public final String customerCctCcy2fwdRate;
  public final String error;
  public final String errorFields;
  public final String errorDetail;
  public final String tradeNumber;
  public final String orderStatus;
  public final String tradeHeaderGuid;

  public SpotResponse(
      @JsonProperty("ErrorDetail") String errorDetail,
      @JsonProperty("ErrorFields") String errorFields,
      @JsonProperty("Error") String error,
      @JsonProperty("CCY1CCY2SPOTRATE") String cc1ccy2spotRate,
      @JsonProperty("CCY1CCY2FWDRATE") String cc1ccy2fwdRate,
      @JsonProperty("CUSTOMERCCY2AMOUNT") String customerCc2Amount,
      @JsonProperty("CUSTOMERCCY1CCY2FWDRATE") String customerCctCcy2fwdRate,
      @JsonProperty("TRADENUMBER") String tradeNumber,
      @JsonProperty("ORDERSTATUS") String orderStatus,
      @JsonProperty("TRADEHEADERGUID") String tradeHeaderGuid) {
    this.cc1ccy2spotRate = cc1ccy2spotRate;
    this.customerCc2Amount = customerCc2Amount;
    this.error = error;
    this.errorFields = errorFields;
    this.errorDetail = errorDetail;
    this.tradeNumber = tradeNumber;
    this.orderStatus = orderStatus;
    this.tradeHeaderGuid = tradeHeaderGuid;
    this.cc1ccy2fwdRate = cc1ccy2fwdRate;
    this.customerCctCcy2fwdRate = customerCctCcy2fwdRate;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("cc1ccy2spotRate", cc1ccy2spotRate)
        .append("cc1ccy2fwdRate", cc1ccy2fwdRate)
        .append("customerCc2Amount", customerCc2Amount)
        .append("customerCctCcy2fwdRate", customerCctCcy2fwdRate)
        .append("error", error)
        .append("errorFields", errorFields)
        .append("errorDetail", errorDetail)
        .append("tradeNumber", tradeNumber)
        .append("orderStatus", orderStatus)
        .append("tradeHeaderGuid", tradeHeaderGuid)
        .toString();
  }
}
