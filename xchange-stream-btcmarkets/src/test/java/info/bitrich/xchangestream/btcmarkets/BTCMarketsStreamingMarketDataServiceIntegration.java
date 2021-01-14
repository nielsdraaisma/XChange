package info.bitrich.xchangestream.btcmarkets;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import io.reactivex.observers.BaseTestConsumer;
import org.junit.Test;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class BTCMarketsStreamingMarketDataServiceIntegration {

    private final Logger logger = LoggerFactory.getLogger(BTCMarketsStreamingMarketDataServiceIntegration.class);

    @Test
    public void runTestBtcAud() {
        ExchangeSpecification defaultExchangeSpecification =
                new ExchangeSpecification(BTCMarketsStreamingExchange.class);

        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
        exchange.connect().blockingAwait();
        StreamingMarketDataService streamingMarketDataService =
                exchange.getStreamingMarketDataService();

        streamingMarketDataService
                .getOrderBook(CurrencyPair.BTC_AUD)
                .test()
                .awaitCount(10)
                .assertNoErrors();

    }


    @Test
    public void runTestBtcAudTrades() {
        ExchangeSpecification defaultExchangeSpecification =
                new ExchangeSpecification(BTCMarketsStreamingExchange.class);

        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
        exchange.connect().blockingAwait();
        StreamingMarketDataService streamingMarketDataService =
                exchange.getStreamingMarketDataService();

        streamingMarketDataService
                .getTrades(new CurrencyPair(Currency.BTC, Currency.AUD))
                .map(trade -> {
                    logger.info("Received trade {}", trade);
                    return trade;
                })
                .test()
                .awaitCount(10, BaseTestConsumer.TestWaitStrategy.SLEEP_100MS, Duration.ofMinutes(5).toMillis())
                .assertNoErrors();
    }
}
