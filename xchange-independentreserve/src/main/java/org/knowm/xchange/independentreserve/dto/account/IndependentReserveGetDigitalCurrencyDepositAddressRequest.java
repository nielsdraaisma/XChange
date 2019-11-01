package org.knowm.xchange.independentreserve.dto.account;

import org.knowm.xchange.independentreserve.dto.auth.AuthAggregate;

public class IndependentReserveGetDigitalCurrencyDepositAddressRequest extends AuthAggregate {
  public IndependentReserveGetDigitalCurrencyDepositAddressRequest(
      String apiKey, Long nonce, String primaryCurrencyCode) {
    super(apiKey, nonce);
    this.parameters.put("primaryCurrencyCode", primaryCurrencyCode);
  }
}
