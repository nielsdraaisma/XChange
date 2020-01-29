package org.knowm.xchange.coinspot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinspot.service.CoinspotMarketDataService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.utils.AuthUtils;

public class CoinspotOrderbookIntegration {

  static Exchange exchange;
  static CoinspotMarketDataService marketDataService;

  @BeforeClass
  public static void beforeClass() {
    exchange = ExchangeFactory.INSTANCE.createExchange(CoinspotExchange.class.getName());
    AuthUtils.setApiAndSecretKey(exchange.getExchangeSpecification());
    exchange = ExchangeFactory.INSTANCE.createExchange(exchange.getExchangeSpecification());
    marketDataService = (CoinspotMarketDataService) exchange.getMarketDataService();
  }

  @Test
  public void testGetOrderbook() throws Exception {
    OrderBook orderBook = marketDataService.getOrderBook(CurrencyPair.BTC_AUD);
    assertThat(orderBook.getAsks()).isNotEmpty();
    assertThat(orderBook.getBids()).isNotEmpty();
  }
}
