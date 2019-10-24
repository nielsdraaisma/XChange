package org.knowm.xchange.coinspot;

import org.knowm.xchange.coinspot.dto.CoinspotOrderbook;
import org.knowm.xchange.coinspot.dto.CoinspotRates;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;

public class CointspotAdapters {

    private CointspotAdapters() {
    }

    public static Ticker adaptTicker(CoinspotRates rates, CurrencyPair currencyPair) {
        if (currencyPair.base.getCurrencyCode().equals("AUD")) {
            String mapKey = currencyPair.counter.getCurrencyCode().toLowerCase();

            if (rates.getPrices().containsKey(mapKey)) {
                return new Ticker.Builder()
                        .currencyPair(currencyPair)
                        .last(rates.getPrices().get(mapKey).getLast())
                        .bid(rates.getPrices().get(mapKey).getBid())
                        .ask(rates.getPrices().get(mapKey).getAsk())
                        .build();
            } else {
                return null;
            }
        }
        return null;
    }

    public static OrderBook adaptOrderbook(CoinspotOrderbook orderbook, CurrencyPair currencyPair) {
        return null;
    }
}
