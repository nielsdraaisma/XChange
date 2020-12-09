package org.knowm.xchange.coinspot;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.coinspot.service.CoinspotAccountService;
import org.knowm.xchange.coinspot.service.CoinspotMarketDataService;
import org.knowm.xchange.coinspot.service.CoinspotTradeService;

public class CoinspotExchange extends BaseExchange implements Exchange {

  @Override
  protected void initServices() {
    this.marketDataService = new CoinspotMarketDataService(this);
    this.tradeService = new CoinspotTradeService(this);
    this.accountService = new CoinspotAccountService(this);
  }


  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification =
        new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://www.coinspot.com.au");
    exchangeSpecification.setExchangeName("coinspot");
    exchangeSpecification.setExchangeDescription("Coinspot");

    return exchangeSpecification;
  }
}
