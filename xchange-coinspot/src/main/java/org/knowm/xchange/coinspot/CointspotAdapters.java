package org.knowm.xchange.coinspot;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.knowm.xchange.coinspot.dto.CoinspotOrderbook;
import org.knowm.xchange.coinspot.dto.CoinspotRates;
import org.knowm.xchange.coinspot.service.CoinspotException;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.ExchangeUnavailableException;

public class CointspotAdapters {

  //    private static final Comparator<xCoinspotOrderbook.Order> orderComparator =
  // Comparator.comparing(o -> o.rate);

  private static Comparator<LimitOrder> limitOrderComparator =
      Comparator.comparing(o -> o, LimitOrder::compareTo);

  private CointspotAdapters() {}

  public static Ticker adaptTicker(CoinspotRates rates, CurrencyPair currencyPair) {
    if (currencyPair.base.getCurrencyCode().equals("AUD")) {
      String mapKey = currencyPair.counter.getCurrencyCode().toLowerCase();

      if (rates.prices.containsKey(mapKey)) {
        return new Ticker.Builder()
            .currencyPair(currencyPair)
            .last(rates.prices.get(mapKey).last)
            .bid(rates.prices.get(mapKey).bid)
            .ask(rates.prices.get(mapKey).ask)
            .build();
      } else {
        return null;
      }
    }
    return null;
  }

  private static List<LimitOrder> adaptOrderbookOrdders(
      List<CoinspotOrderbook.Order> orders, Order.OrderType side, CurrencyPair currencyPair) {
    List<LimitOrder> limitOrders =
        orders.stream()
            .map(
                o ->
                    new LimitOrder.Builder(side, currencyPair)
                        .limitPrice(o.rate)
                        .originalAmount(o.amount)
                        .remainingAmount(o.amount)
                        .build())
            .collect(Collectors.toList());
    if (side == Order.OrderType.BID) {
      limitOrders.sort(limitOrderComparator);
      return limitOrders.subList(limitOrders.size() - 100, limitOrders.size());
    } else {
      limitOrders.sort(limitOrderComparator);
      return limitOrders.subList(0, 100);
    }
  }

  public static OrderBook adaptOrderbook(CoinspotOrderbook orderbook, CurrencyPair currencyPair) {
    return new OrderBook(
        null,
        adaptOrderbookOrdders(orderbook.sellOrders, Order.OrderType.ASK, currencyPair),
        adaptOrderbookOrdders(orderbook.buyOrders, Order.OrderType.BID, currencyPair));
  }

  public static ExchangeException adaptError(CoinspotException e) {
    if (e.getHttpStatusCode() == 502) {
      return new ExchangeUnavailableException(e);
    } else {
      return new ExchangeException(e);
    }
  }
}
