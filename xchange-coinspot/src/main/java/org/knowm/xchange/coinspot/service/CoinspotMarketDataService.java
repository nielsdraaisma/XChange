package org.knowm.xchange.coinspot.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinspot.CointspotAdapters;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;

public class CoinspotMarketDataService extends CoinspotMarketDataServiceRaw implements MarketDataService {

    public CoinspotMarketDataService(Exchange exchange) {
        super(exchange);
    }

    @Override
    public Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {
        return CointspotAdapters.adaptTicker(super.getRates(), currencyPair);
    }

    @Override
    public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
        return CointspotAdapters.adaptOrderbook(super.getOrderbook(currencyPair), currencyPair);
    }
}
