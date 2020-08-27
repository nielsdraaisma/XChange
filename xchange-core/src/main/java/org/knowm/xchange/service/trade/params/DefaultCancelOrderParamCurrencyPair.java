package org.knowm.xchange.service.trade.params;

import org.knowm.xchange.currency.CurrencyPair;

public class DefaultCancelOrderParamCurrencyPair implements CancelOrderByCurrencyPair {
  private CurrencyPair currencyPair;

  public DefaultCancelOrderParamCurrencyPair() {}

  public DefaultCancelOrderParamCurrencyPair(CurrencyPair currencyPair) {
    this.currencyPair = currencyPair;
  }

  @Override
  public CurrencyPair getCurrencyPair() {
    return currencyPair;
  }

  @Override
  public void setCurrencyPair(CurrencyPair currencyPair) {
    this.currencyPair = currencyPair;
  }
}
