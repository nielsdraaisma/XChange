package org.knowm.xchange.coinjar.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinjar.ExchangeUtils;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;

public class CoinjarAccountServiceIntegration {

  private Exchange exchange;

  public CoinjarAccountServiceIntegration() {
    exchange = ExchangeUtils.createExchangeFromProperties();
  }

  @Test
  public void testGetAccountInfo() throws IOException {
    AccountInfo accountInfo = exchange.getAccountService().getAccountInfo();
  }

  @Test
  public void testGetTradeHistory() throws IOException {
    TradeHistoryParams tradeHistoryParams = exchange.getTradeService().createTradeHistoryParams();
    exchange.getTradeService().getTradeHistory(tradeHistoryParams);
  }

  @Test
  public void testGetDepositAddress() throws IOException {
    String address = exchange.getAccountService().requestDepositAddress(Currency.BTC);
    assertThat(address).isNotNull();
  }
}
