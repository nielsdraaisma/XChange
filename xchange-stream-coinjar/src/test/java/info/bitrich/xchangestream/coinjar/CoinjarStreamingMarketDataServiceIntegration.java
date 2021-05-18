package info.bitrich.xchangestream.coinjar;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinjarStreamingMarketDataServiceIntegration {

  private final Logger logger =
      LoggerFactory.getLogger(CoinjarStreamingMarketDataServiceIntegration.class);

  @Test
  public void runTestBtcAud() throws InterruptedException {
    ExchangeSpecification defaultExchangeSpecification =
        new ExchangeSpecification(CoinjarStreamingExchange.class);

    StreamingExchange exchange =
        StreamingExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
    exchange.connect().blockingAwait();
    StreamingMarketDataService streamingMarketDataService =
        exchange.getStreamingMarketDataService();

    Disposable btcOrderBookDisposable =
        streamingMarketDataService
            .getOrderBook(CurrencyPair.BTC_AUD)
            .test()
            .awaitCount(10)
            .assertNoErrors();
    Thread.sleep(5 * 60 * 1000);
    btcOrderBookDisposable.dispose();
  }

  @Test
  public void runTestUsdcAud() {
    ExchangeSpecification defaultExchangeSpecification =
        new ExchangeSpecification(CoinjarStreamingExchange.class);

    StreamingExchange exchange =
        StreamingExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
    exchange.connect().blockingAwait();
    StreamingMarketDataService streamingMarketDataService =
        exchange.getStreamingMarketDataService();

    Disposable usdtOrderBookDisposable =
        streamingMarketDataService
            .getOrderBook(new CurrencyPair(Currency.USDC, Currency.AUD))
            .test()
            .awaitCount(10)
            .assertNoErrors();
    usdtOrderBookDisposable.dispose();
  }

  @Test
  public void runTestBtcAudTrades() {
    ExchangeSpecification defaultExchangeSpecification =
        new ExchangeSpecification(CoinjarStreamingExchange.class);

    StreamingExchange exchange =
        StreamingExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
    exchange.connect().blockingAwait();
    StreamingMarketDataService streamingMarketDataService =
        exchange.getStreamingMarketDataService();

    Disposable usdtOrderBookDisposable =
        streamingMarketDataService
            .getTrades(new CurrencyPair(Currency.BTC, Currency.AUD))
            .map(
                trade -> {
                  logger.info("Received trade {}", trade);
                  return trade;
                })
            .test()
            .awaitCount(10)
            .assertNoErrors();
    usdtOrderBookDisposable.dispose();
  }
}
