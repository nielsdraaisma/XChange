package org.knowm.xchange.btcmarkets.service;

import java.io.IOException;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.btcmarkets.ExchangeUtils;
import org.knowm.xchange.dto.account.AccountInfo;

public class BTCMarketsAccountServiceIntegration {

  private Exchange exchange;

  public BTCMarketsAccountServiceIntegration() {
    exchange = ExchangeUtils.createExchangeFromProperties();
  }

  @Test
  public void testGetAccountInfo() throws IOException {
    AccountInfo accountInfo = exchange.getAccountService().getAccountInfo();
  }
}
