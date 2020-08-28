package org.knowm.xchange.coinspot.service;

import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinspot.dto.CoinspotOpenOrdersRequest;
import org.knowm.xchange.coinspot.dto.CoinspotOrderbook;
import org.knowm.xchange.coinspot.dto.CoinspotRates;
import org.knowm.xchange.currency.CurrencyPair;
import si.mazi.rescu.SynchronizedValueFactory;

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
    return coinspotPrivate.getOpenOrders(
        apiKey,
        digest,
        new CoinspotOpenOrdersRequest(nonceFactory.createValue(), currencyPair.base.toString()));
  }
}
