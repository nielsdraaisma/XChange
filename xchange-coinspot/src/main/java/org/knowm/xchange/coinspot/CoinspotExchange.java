package org.knowm.xchange.coinspot;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.utils.nonce.CurrentTimeNonceFactory;
import org.knowm.xchange.coinspot.service.CoinspotMarketDataService;
import si.mazi.rescu.SynchronizedValueFactory;

public class CoinspotExchange extends BaseExchange implements Exchange {

    private static SynchronizedValueFactory<Long> nonceFactory = new CurrentTimeNonceFactory();

    @Override
    protected void initServices() {
        this.marketDataService = new CoinspotMarketDataService(this);
    }

    @Override
    public SynchronizedValueFactory<Long> getNonceFactory() {
        return nonceFactory;
    }

    @Override
    public ExchangeSpecification getDefaultExchangeSpecification() {
        ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
        exchangeSpecification.setSslUri("https://www.coinspot.com.au");
        exchangeSpecification.setExchangeName("coinspot");
        exchangeSpecification.setExchangeDescription("Coinspot");

        return exchangeSpecification;
    }
}
