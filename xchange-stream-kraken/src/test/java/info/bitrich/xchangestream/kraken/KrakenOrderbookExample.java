package info.bitrich.xchangestream.kraken;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.knowm.xchange.currency.CurrencyPair.*;

public class KrakenOrderbookExample {

  private static final Logger LOG = LoggerFactory.getLogger(KrakenOrderbookExample.class);

  private static Disposable subscribe(StreamingExchange krakenExchange, CurrencyPair currencyPair) {
          return krakenExchange
                  .getStreamingMarketDataService()
                  .getOrderBook(currencyPair, 25)
                  .subscribe(
                          s -> {
                              LOG.info("Received book with {} bids and {} asks", s.getBids().size(), s.getAsks().size());
                              if ( ! s.getBids().isEmpty() ){
                                  BigDecimal bestBid = s.getBids().iterator().next().getLimitPrice();
                                  BigDecimal bestAsk = s.getAsks().iterator().next().getLimitPrice();
                                  if (bestBid.compareTo(bestAsk) > 0 ) {
                                      LOG.warn("Crossed {} book, best bid {}, best ask {}", currencyPair, bestBid, bestAsk);
                                  }
                              }
                          },
                          throwable -> {
                              LOG.error("Fail to get ticker {}", throwable.getMessage(), throwable);
                          });
  }
  public static void main(String[] args) throws InterruptedException {

    ExchangeSpecification exchangeSpecification =
        new ExchangeSpecification(KrakenStreamingExchange.class);

    StreamingExchange krakenExchange =
        StreamingExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
    krakenExchange.connect().blockingAwait();

    subscribe(krakenExchange, BTC_USD);
    subscribe(krakenExchange, ETH_USD);
    subscribe(krakenExchange, BCH_USD);
    subscribe(krakenExchange, XRP_USD);
    subscribe(krakenExchange, LTC_USD);
    subscribe(krakenExchange, ADA_USD);
    subscribe(krakenExchange, ATOM_USD);
    subscribe(krakenExchange, DOGE_USD);
    subscribe(krakenExchange, OMG_USD);

    TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);

    krakenExchange.disconnect().subscribe(() -> LOG.info("Disconnected"));
  }
}
