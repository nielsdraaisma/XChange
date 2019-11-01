package org.knowm.xchange.coinspot.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinspot.dto.CoinspotOpenOrdersRequest;
import org.knowm.xchange.coinspot.dto.CoinspotOrderbook;
import org.knowm.xchange.coinspot.dto.CoinspotRates;
import org.knowm.xchange.coinspot.dto.CoinspotRequest;
import org.knowm.xchange.currency.CurrencyPair;
import si.mazi.rescu.SynchronizedValueFactory;

import java.io.IOException;

class CoinspotMarketDataServiceRaw extends CoinspotBaseService {

    private SynchronizedValueFactory<Long> nonceFactory;

    public CoinspotMarketDataServiceRaw(Exchange exchange) {
        super(exchange);
        this.nonceFactory = exchange.getNonceFactory();
    }

    CoinspotRates getRates() throws CoinspotException, IOException {
        return coinspotPublic.getRates();
    }

    CoinspotOrderbook getOrderbook(CurrencyPair currencyPair) throws CoinspotException, IOException {
        return coinspotPrivate.getOrders(apiKey, digest, new CoinspotOpenOrdersRequest(nonceFactory.createValue(), currencyPair.base.toString()));
    }

}
