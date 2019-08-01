package org.knowm.xchange.wyre.v2.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.wyre.v2.WyreAdapters;

public class WyreMarketDataService extends WyreMarketDataServiceRaw implements MarketDataService {

  /**
   * Wyre doesn't have an orderbook, we fake this by quoting for various amounts and returning that
   * as an orderbook. The list below contains the amounts that are quotes.
   */
  private final List<BigDecimal> orderBookQuoteAmounts;

  public WyreMarketDataService(Exchange exchange) {
    super(exchange);
    orderBookQuoteAmounts = new ArrayList<>();
    orderBookQuoteAmounts.add(BigDecimal.valueOf(0.1));
    orderBookQuoteAmounts.add(BigDecimal.valueOf(0.5));
    orderBookQuoteAmounts.add(BigDecimal.valueOf(1));
    orderBookQuoteAmounts.add(BigDecimal.valueOf(1.5));
    orderBookQuoteAmounts.add(BigDecimal.valueOf(2));
  }

  @Override
  public Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {
    return WyreAdapters.adaptTicker(super.getRates(), currencyPair);
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) {
    CurrencyPair invertedCurrencyPair = new CurrencyPair(currencyPair.counter, currencyPair.base);
    List<LimitOrder> orders =
        orderBookQuoteAmounts
            .stream()
            .flatMap(
                amount ->
                    Stream.of(
                        new LimitOrder(
                            Order.OrderType.BID,
                            amount,
                            currencyPair,
                            null,
                            null,
                            super.quoteTransfer(currencyPair, amount, null).getExchangeRate()),
                        new LimitOrder(
                            Order.OrderType.ASK,
                            amount,
                            currencyPair,
                            null,
                            null,
                            BigDecimal.ONE.divide(
                                super.quoteTransfer(invertedCurrencyPair, null, amount)
                                    .getExchangeRate(),
                                4,
                                RoundingMode.HALF_UP))))
            .collect(Collectors.toList());

    Stream<LimitOrder> bids =
        orders.stream().filter(limitOrder -> limitOrder.getType().equals(Order.OrderType.BID));
    Stream<LimitOrder> asks =
        orders.stream().filter(limitOrder -> limitOrder.getType().equals(Order.OrderType.ASK));

    return new OrderBook(null, asks, bids);
  }
}
