package org.knowm.xchange.b2c2;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.knowm.xchange.b2c2.dto.trade.LedgerItem;
import org.knowm.xchange.b2c2.dto.trade.OrderResponse;
import org.knowm.xchange.b2c2.dto.trade.QuoteResponse;
import org.knowm.xchange.b2c2.dto.trade.TradeResponse;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.UserTrade;

public class B2C2Adapters {

  private static final Set<Currency> cryptos = new HashSet<>();
  private static final Map<String, CurrencyPair> instrumentsToCurrencypairs =
      new ConcurrentHashMap<>();
  private static final Map<CurrencyPair, String> currencyPairsToInstruments =
      new ConcurrentHashMap<>();

  static {
    cryptos.add(Currency.BTC);
    cryptos.add(Currency.BCH);
    cryptos.add(Currency.ETH);
    cryptos.add(Currency.XRP);
    cryptos.add(Currency.LTC);
    cryptos.add(Currency.EOS);
  }

  private B2C2Adapters() {}

  static boolean isPositive(final String string) {
    return isPositive(new BigDecimal(string));
  }

  static boolean isPositive(final BigDecimal bigDecimal) {
    return bigDecimal.abs().equals(bigDecimal);
  }

  static boolean isCrypto(final Currency currency) {
    return cryptos.contains(currency);
  }

  public static Order.OrderType adaptSide(final String side) {
    if (side.equals("buy")) {
      return Order.OrderType.BID;
    } else if (side.equals("sell")) {
      return Order.OrderType.ASK;
    } else {
      throw new IllegalArgumentException("Cannot adapt side " + side);
    }
  }

  public static String adaptSide(final Order.OrderType type) {
    if (type == Order.OrderType.ASK || type == Order.OrderType.EXIT_ASK) {
      return "sell";
    } else if (type == Order.OrderType.BID || type == Order.OrderType.EXIT_BID) {
      return "buy";
    } else {
      throw new IllegalArgumentException("Cannot adapt orderType " + type);
    }
  }

  public static CurrencyPair adaptInstrumentToCurrencyPair(final String instrument) {
    instrumentsToCurrencypairs.computeIfAbsent(
        instrument,
        i -> {
          String base = instrument.substring(0, 3);
          String counter = instrument.substring(3, 6);
          return new CurrencyPair(new Currency(base), new Currency(counter));
        });
    return instrumentsToCurrencypairs.get(instrument);
  }

  public static String adaptCurrencyPairToSpotInstrument(final CurrencyPair currencyPair) {
    currencyPairsToInstruments.computeIfAbsent(
        currencyPair,
        i -> currencyPair.base.toString() + currencyPair.counter.toString() + ".SPOT");
    return currencyPairsToInstruments.get(currencyPair);
  }

  private static Date nullableStringToDate(String s) {
    return Optional.ofNullable(s)
        .map(ZonedDateTime::parse)
        .map(ZonedDateTime::toInstant)
        .map(Date::from)
        .orElse(null);
  }

  public static Ticker adaptQuotesToTicker(
      CurrencyPair currencyPair, QuoteResponse bidResponse, QuoteResponse askResponse) {
    return new Ticker.Builder()
        .currencyPair(currencyPair)
        .timestamp(nullableStringToDate(bidResponse.created))
        .bidSize(new BigDecimal(bidResponse.quantity))
        .bid(new BigDecimal(bidResponse.price))
        .askSize(new BigDecimal(askResponse.quantity))
        .ask(new BigDecimal(askResponse.price))
        .build();
  }

  public static FundingRecord adaptLedgerItemToFundingRecord(LedgerItem item) {
    return new FundingRecord.Builder()
        .setInternalId(item.transactionId)
        .setDate(nullableStringToDate(item.created))
        .setDescription(item.reference)
        .setCurrency(new Currency(item.currency))
        .setAmount(new BigDecimal(item.amount))
        .build();
  }

  public static Order adaptOrderResponseToOrder(OrderResponse order) {
    LimitOrder.Builder builder =
        new LimitOrder.Builder(
                adaptSide(order.side), adaptInstrumentToCurrencyPair(order.instrument))
            .originalAmount(order.quantity)
            .id(order.clientOrderId)
            .limitPrice(order.price)
            .averagePrice(order.executedPrice)
            .userReference(order.executingUnit)
            .timestamp(nullableStringToDate(order.created));
    if (order.executedPrice != null) {
      builder =
          builder
              .cumulativeAmount(order.quantity)
              .averagePrice(order.executedPrice)
              .limitPrice(order.price);
    }
    return builder.build();
  }

  public static UserTrade adaptOrderResponseToUserTrade(OrderResponse order) {
    BigDecimal price = order.executedPrice;
    if (price == null) {
      price = order.price;
    }
    return new UserTrade.Builder()
        .currencyPair(adaptInstrumentToCurrencyPair(order.instrument))
        .feeAmount(BigDecimal.ZERO)
        .orderId(order.clientOrderId)
        .id(order.orderId)
        .originalAmount(order.quantity)
        .price(price)
        .timestamp(nullableStringToDate(order.created))
        .type(adaptSide(order.side))
        .orderUserReference(order.executingUnit)
        .build();
  }

  public static LimitOrder adaptTradeToLimitOrder(TradeResponse tradeResponse) {
    return new LimitOrder.Builder(
            adaptSide(tradeResponse.side), adaptInstrumentToCurrencyPair(tradeResponse.instrument))
        .timestamp(nullableStringToDate(tradeResponse.created))
        .id(tradeResponse.tradeId)
        .averagePrice(new BigDecimal(tradeResponse.price))
        .originalAmount(new BigDecimal(tradeResponse.quantity))
        .cumulativeAmount(new BigDecimal(tradeResponse.quantity))
        .build();
  }
}
