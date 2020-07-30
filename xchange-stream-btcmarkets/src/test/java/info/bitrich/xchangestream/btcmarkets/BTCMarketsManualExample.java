package info.bitrich.xchangestream.btcmarkets;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BTCMarketsManualExample {
  private static final Logger logger = LoggerFactory.getLogger(BTCMarketsManualExample.class);

  public static void main(String[] args) {

    ExchangeSpecification defaultExchangeSpecification =
        new ExchangeSpecification(BTCMarketsStreamingExchange.class);

    AuthUtils.setApiAndSecretKey(defaultExchangeSpecification);

    StreamingExchange exchange =
        StreamingExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
    exchange.connect().blockingAwait();

    exchange
        .getStreamingMarketDataService()
        .getOrderBook(CurrencyPair.BTC_AUD)
        .forEach(
            orderBook -> {
              logger.info("First btc ask: {}", orderBook.getAsks().get(0));
              logger.info("First btc bid: {}", orderBook.getBids().get(0));
            });

    exchange
        .getStreamingMarketDataService()
        .getOrderBook(CurrencyPair.ETH_AUD)
        .forEach(
            orderBook -> {
              logger.info("First eth ask: {}", orderBook.getAsks().get(0));
              logger.info("First eth bid: {}", orderBook.getBids().get(0));
            });

    if (defaultExchangeSpecification.getApiKey() != null
        && defaultExchangeSpecification.getSecretKey() != null) {
      exchange
          .getStreamingTradeService()
          .getUserTrades(null)
          .forEach(
              userTrade -> {
                logger.info("userTrade: {}", userTrade);
              });
      exchange
          .getStreamingTradeService()
          .getOrderChanges(null)
          .forEach(
              orderChange -> {
                logger.info("orderChange: {}", orderChange);
              });
    }
    try {
      Thread.sleep(120000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    exchange.disconnect().subscribe(() -> logger.info("Disconnected from the Exchange"));
  }
}
