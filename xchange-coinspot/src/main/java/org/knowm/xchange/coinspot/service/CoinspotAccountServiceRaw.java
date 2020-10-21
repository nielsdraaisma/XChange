package org.knowm.xchange.coinspot.service;

import java.io.IOException;
import org.knowm.xchange.coinspot.CoinspotExchange;
import org.knowm.xchange.coinspot.dto.*;
import si.mazi.rescu.SynchronizedValueFactory;

class CoinspotAccountServiceRaw extends CoinspotBaseService {

  private SynchronizedValueFactory<Long> nonceFactory;

  public CoinspotAccountServiceRaw(CoinspotExchange exchange) {
    super(exchange);
    this.nonceFactory = exchange.getNonceFactory();
  }

  protected CoinspotBalancesResponse getBalances() throws IOException {
    return coinspotPrivate.getBalances(
        exchange.getExchangeSpecification().getApiKey(),
        digest,
        new CoinspotRequest(nonceFactory.createValue()));
  }
}
