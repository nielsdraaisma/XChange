package org.knowm.xchange.binance.service;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByCurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;

public class BinanceCancelOrderParams implements CancelOrderByIdParams, CancelOrderByCurrencyPair {
  private String orderId;
  private CurrencyPair pair;

  public BinanceCancelOrderParams() {}

  public BinanceCancelOrderParams(CurrencyPair pair, String orderId) {
    this.pair = pair;
    this.orderId = orderId;
  }

  @Override
  public CurrencyPair getCurrencyPair() {
    return pair;
  }

  @Override
  public String getOrderId() {
    return orderId;
  }

  @Override
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  @Override
  public void setCurrencyPair(CurrencyPair pair) {
    this.pair = pair;
  }
}
