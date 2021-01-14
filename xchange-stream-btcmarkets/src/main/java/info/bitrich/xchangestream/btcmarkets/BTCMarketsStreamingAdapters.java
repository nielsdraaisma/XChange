package info.bitrich.xchangestream.btcmarkets;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import info.bitrich.xchangestream.btcmarkets.dto.BTCMarketsWebSocketOrderMessage;
import info.bitrich.xchangestream.btcmarkets.dto.BTCMarketsWebSocketOrderbookMessage;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import info.bitrich.xchangestream.btcmarkets.dto.BTCMarketsWebSocketTradeMessage;
import org.knowm.xchange.btcmarkets.BTCMarketsAdapters;
import org.knowm.xchange.btcmarkets.dto.trade.BTCMarketsOrder;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.utils.DateUtils;

class BTCMarketsStreamingAdapters {
  public static String adaptCurrencyPairToMarketId(CurrencyPair currencyPair) {
    return currencyPair.base.toString() + "-" + currencyPair.counter.toString();
  }

  public static OrderBook adaptOrderbookMessageToOrderbook(
      BTCMarketsWebSocketOrderbookMessage message) throws InvalidFormatException {
    CurrencyPair currencyPair = BTCMarketsAdapters.adaptCurrencyPair(message.marketId);
    BiFunction<List<BigDecimal>, Order.OrderType, LimitOrder> toLimitOrder =
        (strings, ot) ->
            new LimitOrder.Builder(ot, currencyPair)
                .originalAmount(strings.get(1))
                .limitPrice(strings.get(0))
                .build();

    return new OrderBook(
        DateUtils.fromISODateString(message.timestamp),
        message.asks.stream()
            .map((o) -> toLimitOrder.apply(o, Order.OrderType.ASK))
            .collect(Collectors.toList()),
        message.bids.stream()
            .map((o) -> toLimitOrder.apply(o, Order.OrderType.BID))
            .collect(Collectors.toList()));
  }

  public static List<UserTrade> adaptUserTrades(BTCMarketsWebSocketOrderMessage message) {
    BTCMarketsOrder.Side side = BTCMarketsOrder.Side.valueOf(message.side);
    CurrencyPair currencyPair = BTCMarketsAdapters.adaptCurrencyPair(message.marketId);

    return message.trades.stream()
        .map(
            trade -> {
              Date timestamp = null;
              try {
                timestamp = DateUtils.fromISODateString(message.timestamp);
              } catch (InvalidFormatException e) {
                e.printStackTrace();
              }
              return new UserTrade.Builder()
                  .orderId(Long.toString(message.orderId))
                  .currencyPair(currencyPair)
                  .type(BTCMarketsAdapters.adaptOrderType(side))
                  .id(Long.toString(trade.tradeId))
                  .price(new BigDecimal(trade.price))
                  .originalAmount(new BigDecimal(trade.volume))
                  .feeAmount(new BigDecimal(trade.fee))
                  .timestamp(timestamp)
                  .build();
            })
        .collect(Collectors.toList());
  }

  public static Order adaptOrder(BTCMarketsWebSocketOrderMessage message) {
    BTCMarketsOrder.Side orderSide = BTCMarketsOrder.Side.valueOf(message.side);
    Order.OrderType orderType = BTCMarketsAdapters.adaptOrderType(orderSide);
    CurrencyPair currencyPair = BTCMarketsAdapters.adaptCurrencyPair(message.marketId);
    Date timestamp = null;
    try {
      timestamp = DateUtils.fromISODateString(message.timestamp);
    } catch (InvalidFormatException e) {
      e.printStackTrace();
    }
    BigDecimal remainingAmount = new BigDecimal(message.openVolume);
    BigDecimal cumulativeAmount =
        message.trades.stream()
            .map(trade -> new BigDecimal(trade.volume))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal originalAmount = cumulativeAmount.add(remainingAmount);

    if ("Limit".equals(message.type)) {
      return new LimitOrder.Builder(orderType, currencyPair)
          .id(Long.toString(message.orderId))
          .remainingAmount(remainingAmount)
          .orderStatus(BTCMarketsAdapters.adaptOrderStatus(message.status))
          .cumulativeAmount(cumulativeAmount)
          .originalAmount(originalAmount)
          .timestamp(timestamp)
          .build();
    } else {
      return new MarketOrder.Builder(orderType, currencyPair)
          .id(Long.toString(message.orderId))
          .remainingAmount(remainingAmount)
          .orderStatus(BTCMarketsAdapters.adaptOrderStatus(message.status))
          .cumulativeAmount(cumulativeAmount)
          .originalAmount(originalAmount)
          .timestamp(timestamp)
          .build();
    }
  }

  public static Trade adaptTradeEvent(BTCMarketsWebSocketTradeMessage message) {
    return new Trade.Builder()
            .instrument(BTCMarketsAdapters.adaptCurrencyPair(message.marketId))
            .timestamp(message.timestamp)
            .id(Long.toString(message.tradeId))
            .price(message.price)
            .originalAmount(message.volume)
            .type(BTCMarketsAdapters.adaptOrderType(message.side))
            .build();
  }
}
