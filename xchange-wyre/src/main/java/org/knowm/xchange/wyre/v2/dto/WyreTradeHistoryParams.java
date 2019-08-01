package org.knowm.xchange.wyre.v2.dto;

import org.knowm.xchange.service.trade.params.TradeHistoryParamTransactionId;

public class WyreTradeHistoryParams implements TradeHistoryParamTransactionId {

  private String transactionId = null;

  @Override
  public String getTransactionId() {
    return transactionId;
  }

  @Override
  public void setTransactionId(String txId) {
    transactionId = txId;
  }
}
