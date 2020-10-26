package info.bitrich.xchangestream.coinjar;

import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.BaseTestConsumer;
import org.junit.Test;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinjarBalancesExample {

  private static final Logger logger = LoggerFactory.getLogger(CoinjarBalancesExample.class);

  @Test
  public void runTest() {
    ExchangeSpecification defaultExchangeSpecification =
        new ExchangeSpecification(CoinjarStreamingExchange.class);

    AuthUtils.setApiAndSecretKey(defaultExchangeSpecification);

    if (defaultExchangeSpecification.getApiKey() != null) {
      StreamingExchange exchange =
          StreamingExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
      exchange.connect().blockingAwait();
      StreamingAccountService streamingAccountService = exchange.getStreamingAccountService();

      Disposable disposable =
          streamingAccountService
              .getBalanceChanges(Currency.BTC)
              .map(
                  balance -> {
                    logger.info("Received balance {}", balance);
                    return balance;
                  })
              .test()
              .awaitCount(20, BaseTestConsumer.TestWaitStrategy.SLEEP_100MS, 1000 * 60 * 10)
              .assertNoErrors();
      disposable.dispose();
    }
  }
}
