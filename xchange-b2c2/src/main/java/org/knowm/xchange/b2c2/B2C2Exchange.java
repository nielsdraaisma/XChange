package org.knowm.xchange.b2c2;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.b2c2.service.B2C2AccountService;
import org.knowm.xchange.b2c2.service.B2C2MarketDataService;
import org.knowm.xchange.b2c2.service.B2C2TradingService;
import si.mazi.rescu.SynchronizedValueFactory;

public class B2C2Exchange extends BaseExchange implements Exchange {

  private String accountId;

  @Override
  protected void initServices() {
    this.accountService = new B2C2AccountService(this);
    this.marketDataService = new B2C2MarketDataService(this);
    this.tradeService = new B2C2TradingService(this);
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {
    return null;
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
