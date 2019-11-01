package org.knowm.xchange.independentreserve.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndependentReserveGetDigitalCurrencyDepositAddressResponse {
  public final String depositAddress;

  public IndependentReserveGetDigitalCurrencyDepositAddressResponse(
      @JsonProperty("DepositAddress") String depositAddress) {
    this.depositAddress = depositAddress;
  }
}
