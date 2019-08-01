package org.knowm.xchange.wyre.v2;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.utils.nonce.CurrentTimeNonceFactory;
import org.knowm.xchange.wyre.v2.service.WyreAccountService;
import org.knowm.xchange.wyre.v2.service.WyreMarketDataService;
import org.knowm.xchange.wyre.v2.service.WyreTradingService;
import si.mazi.rescu.SynchronizedValueFactory;

public class WyreExchange extends BaseExchange implements Exchange {

  private static SynchronizedValueFactory<Long> nonceFactory = new CurrentTimeNonceFactory();

  @Override
  protected void initServices() {
    this.accountService = new WyreAccountService(this);
    this.marketDataService = new WyreMarketDataService(this);
    this.tradeService = new WyreTradingService(this);
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {
    return nonceFactory;
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification =
        new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://api.sendwyre.com");
    exchangeSpecification.setExchangeName("Wyre");
    exchangeSpecification.setExchangeDescription("Wyre.");

    return exchangeSpecification;
  }
}
