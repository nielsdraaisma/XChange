package org.knowm.xchange.coinspot;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.vavr.Function2;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.knowm.xchange.coinspot.dto.*;
import org.knowm.xchange.coinspot.service.CoinspotException;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.ExchangeUnavailableException;
import org.knowm.xchange.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinspotAdapters {

  private static final Logger LOG = LoggerFactory.getLogger(CoinspotAdapters.class);

  private static Comparator<LimitOrder> limitOrderComparator =
      Comparator.comparing(o -> o, LimitOrder::compareTo);

  private CoinspotAdapters() {}

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

  private static LimitOrder adaptMyOrder(CoinspotMyOrders.Order order, Order.OrderType orderType) {
    return new LimitOrder.Builder(
            orderType, new CurrencyPair(Currency.getInstance(order.coin), Currency.AUD))
        .id(order.id)
        .originalAmount(order.amount)
        .limitPrice(order.rate)
        .timestamp(DateUtils.fromMillisUtc(order.created))
        .build();
  }

  public static OpenOrders adaptMyOrders(CoinspotMyOrders myOrders) {
    List<LimitOrder> orders = new ArrayList<>();
    orders.addAll(
        myOrders.buyOrders.stream()
            .map(order -> adaptMyOrder(order, Order.OrderType.BID))
            .collect(Collectors.toList()));
    orders.addAll(
        myOrders.sellOrders.stream()
            .map(order -> adaptMyOrder(order, Order.OrderType.ASK))
            .collect(Collectors.toList()));
    return new OpenOrders(orders);
  }

  public static UserTrades adaptMyTransactionsResponse(
      CoinspotMyTransactionsResponse response, CurrencyPair currencyPair) {
    Predicate<UserTrade> matchesCurrencyPair =
        (userTrade) -> currencyPair == null || userTrade.getCurrencyPair().equals(currencyPair);

    Function2<CoinspotMyTransactionsResponse.Transaction, Order.OrderType, UserTrade>
        adaptUserTrades =
            (transaction, orderType) -> {
              Date timestamp = null;
              try {
                timestamp = DateUtils.fromISODateString(transaction.created);
              } catch (InvalidFormatException e) {
                LOG.warn("Caught exception while parsing coinspot response date", e);
              }
              return new UserTrade.Builder()
                  .type(orderType)
                  .originalAmount(transaction.amount)
                  .currencyPair(new CurrencyPair(transaction.market))
                  .timestamp(timestamp)
                  .feeCurrency(Currency.AUD)
                  .feeAmount(transaction.audfeeExGst.add(transaction.audGst))
                  .build();
            };
    List<UserTrade> userTrades = new ArrayList<>();
    userTrades.addAll(
        response.buyOrders.stream()
            .map(order -> adaptUserTrades.apply(order, Order.OrderType.BID))
            .filter(matchesCurrencyPair)
            .collect(Collectors.toList()));
    userTrades.addAll(
        response.sellOrders.stream()
            .map(order -> adaptUserTrades.apply(order, Order.OrderType.ASK))
            .filter(matchesCurrencyPair)
            .collect(Collectors.toList()));
    return new UserTrades(userTrades, Trades.TradeSortType.SortByTimestamp);
  }

  public static AccountInfo adaptBalances(CoinspotBalancesResponse response) {
    List<Balance> balances =
        response.balance.entrySet().stream()
            .map(
                entry ->
                    new Balance.Builder()
                        .available(entry.getValue())
                        .total(entry.getValue())
                        .currency(entry.getKey())
                        .build())
            .collect(Collectors.toList());
    Wallet wallet = Wallet.Builder.from(balances).build();
    return new AccountInfo(wallet);
  }
}
