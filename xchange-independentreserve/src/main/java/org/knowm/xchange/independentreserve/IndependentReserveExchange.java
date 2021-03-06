package org.knowm.xchange.independentreserve;

import java.util.concurrent.TimeUnit;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.independentreserve.service.IndependentReserveAccountService;
import org.knowm.xchange.independentreserve.service.IndependentReserveMarketDataService;
import org.knowm.xchange.independentreserve.service.IndependentReserveTradeService;
import org.knowm.xchange.utils.nonce.CurrentTimeIncrementalNonceFactory;
import si.mazi.rescu.SynchronizedValueFactory;

public class IndependentReserveExchange extends BaseExchange implements Exchange {

  private SynchronizedValueFactory<Long> nonceFactory =
      new CurrentTimeIncrementalNonceFactory(TimeUnit.MILLISECONDS);

  @Override
  protected void initServices() {
    this.marketDataService = new IndependentReserveMarketDataService(this);
    this.tradeService = new IndependentReserveTradeService(this);
    this.accountService = new IndependentReserveAccountService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.independentreserve.com");
    exchangeSpecification.setHost("https://api.independentreserve.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("IndependentReserve");
    exchangeSpecification.setExchangeDescription(
        "Independent Reserve is a registered Australian company, underpinned by Australia's highly regulated financial sector.");
    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    return nonceFactory;
  }

  public void setNonceFactory(SynchronizedValueFactory<Long> nonceFactory) {
    this.nonceFactory = nonceFactory;
  }
}
