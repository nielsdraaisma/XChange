package info.bitrich.xchangestream.coinjar;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.core.StreamingTradeService;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.BaseTestConsumer;
import org.junit.Test;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinjarUserTradesExample {

  private static final Logger logger = LoggerFactory.getLogger(CoinjarUserTradesExample.class);

  @Test
  public void runTest() {
    ExchangeSpecification defaultExchangeSpecification =
        new ExchangeSpecification(CoinjarStreamingExchange.class);

    AuthUtils.setApiAndSecretKey(defaultExchangeSpecification);

    if (defaultExchangeSpecification.getApiKey() != null) {
      StreamingExchange exchange =
          StreamingExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
      exchange.connect().blockingAwait();
      StreamingTradeService streamingTradeService = exchange.getStreamingTradeService();

      Disposable disposable =
          streamingTradeService
              .getUserTrades(null)
                  .map(userTrade -> {
                          logger.info("Received userTrade {}", userTrade);
                          return userTrade;
                          })
              .test()
              .awaitCount(1, BaseTestConsumer.TestWaitStrategy.SLEEP_100MS, 1000 * 60 * 10)
              .assertNoErrors();
      disposable.dispose();
    }
  }
}
