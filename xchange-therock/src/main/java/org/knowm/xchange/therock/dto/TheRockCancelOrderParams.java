package org.knowm.xchange.therock.dto;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByCurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;

public class TheRockCancelOrderParams implements CancelOrderByIdParams, CancelOrderByCurrencyPair {
  public CurrencyPair currencyPair;
  public String orderId;

  public TheRockCancelOrderParams() {}

  public TheRockCancelOrderParams(CurrencyPair currencyPair, Long orderId) {
    this.currencyPair = currencyPair;
    this.orderId = orderId.toString();
  }

  @Override
  public CurrencyPair getCurrencyPair() {
    return currencyPair;
  }

  @Override
  public String getOrderId() {
    return orderId;
  }

  @Override
  public void setCurrencyPair(CurrencyPair pair) {
    this.currencyPair = pair;
  }

  @Override
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
