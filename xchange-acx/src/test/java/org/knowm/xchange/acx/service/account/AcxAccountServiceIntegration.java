package org.knowm.xchange.acx.service.account;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.acx.ExchangeUtils;
import org.knowm.xchange.currency.Currency;

public class AcxAccountServiceIntegration {

  private Exchange exchange;

  public AcxAccountServiceIntegration() {
    exchange = ExchangeUtils.createExchangeFromProperties();
  }

  @Test
  public void testGetDepositAddress() throws IOException {
    String address = exchange.getAccountService().requestDepositAddress(Currency.BTC);
    assertThat(address).isNotNull();
  }
}
