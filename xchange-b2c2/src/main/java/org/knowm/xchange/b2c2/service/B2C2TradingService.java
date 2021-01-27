package org.knowm.xchange.b2c2.service;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.knowm.xchange.b2c2.B2C2Adapters;
import org.knowm.xchange.b2c2.B2C2Exchange;
import org.knowm.xchange.b2c2.dto.trade.OrderRequest;
import org.knowm.xchange.b2c2.dto.trade.OrderResponse;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.*;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParam;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

public class B2C2TradingService extends B2C2TradingServiceRaw implements TradeService {
  public B2C2TradingService(B2C2Exchange exchange) {
    super(exchange);
  }

  private String placeOrderRequest(OrderRequest orderRequest) throws IOException {
    OrderResponse orderResponse = order(orderRequest);

    if (orderResponse.trades.size() == 1) {
      return orderRequest.clientOrderId;
    } else {
      throw new IllegalStateException(
          "Did not get expected number of trades from B2C2 order response, expected 1 got "
              + orderResponse.trades.size());
    }
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    OrderRequest orderRequest =
        new OrderRequest(
            UUID.randomUUID().toString(),
            limitOrder.getOriginalAmount().stripTrailingZeros().toPlainString(),
            B2C2Adapters.adaptSide(limitOrder.getType()),
            B2C2Adapters.adaptCurrencyPairToSpotInstrument(limitOrder.getCurrencyPair()),
            "FOK",
            limitOrder.getLimitPrice().stripTrailingZeros().toPlainString(),
            false,
            DateTimeFormatter.ISO_DATE_TIME.format(
                ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(15)),
            limitOrder.getUserReference());

    return placeOrderRequest(orderRequest);
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {
    OrderRequest orderRequest =
        new OrderRequest(
            UUID.randomUUID().toString(),
            marketOrder.getOriginalAmount().stripTrailingZeros().toPlainString(),
            B2C2Adapters.adaptSide(marketOrder.getType()),
            B2C2Adapters.adaptCurrencyPairToSpotInstrument(marketOrder.getCurrencyPair()),
            "MKT",
            null,
            false,
            DateTimeFormatter.ISO_DATE_TIME.format(
                ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(15)),
            marketOrder.getUserReference());

    return placeOrderRequest(orderRequest);
  }

  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {
    if (!(params instanceof B2C2TradeHistoryParams)) {
      throw new IllegalArgumentException("Invalid params given");
    }

    final B2C2TradeHistoryParams b2C2TradeHistoryParams = (B2C2TradeHistoryParams) params;

    List<UserTrade> userTrades =
        getOrders(
                b2C2TradeHistoryParams.getStartTime(),
                b2C2TradeHistoryParams.getEndTime(),
                null,
                null,
                b2C2TradeHistoryParams.currencyPair,
                b2C2TradeHistoryParams.offset,
                b2C2TradeHistoryParams.limit)
            .stream()
            .map(B2C2Adapters::adaptOrderResponseToUserTrade)
            .collect(Collectors.toList());

    return new UserTrades(
        userTrades, Trades.TradeSortType.SortByTimestamp); // TODO : Validate sort type
  }

  /**
   * this expects a B2C2 trade no as returned by the placeOrder methods, not the b2c2 order id as
   * used on the /order endpoints.
   */
  @Override
  public Collection<Order> getOrder(String... orderIds) throws IOException {
    if (orderIds.length > 1) {
      throw new IllegalArgumentException("Multiple orderIds not supported");
    }
    final String orderId = orderIds[0];
    return getTrade(orderId).stream()
        .map(B2C2Adapters::adaptTradeToLimitOrder)
        .collect(Collectors.toList());
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    List<Order> res = new ArrayList<>();
    for (OrderQueryParams orderQueryParam : orderQueryParams) {
      res.addAll(this.getOrder(new String[] {orderQueryParam.getOrderId()}));
    }
    return res;
  }

  @Override
  public OrderQueryParams createOrdersQueryParams() {
    return new DefaultQueryOrderParam();
  }

  @Override
  public TradeHistoryParams createTradeHistoryParams() {
    return new B2C2TradeHistoryParams();
  }

  public static class B2C2TradeHistoryParams
      implements TradeHistoryParams,
          TradeHistoryParamCurrencyPair,
          TradeHistoryParamOffset,
          TradeHistoryParamLimit,
          TradeHistoryParamsTimeSpan {
    private CurrencyPair currencyPair;
    private Long offset;
    private Integer limit;
    private Date since;

    @Override
    public CurrencyPair getCurrencyPair() {
      return currencyPair;
    }

    public void setCurrencyPair(CurrencyPair currencyPair) {
      this.currencyPair = currencyPair;
    }

    @Override
    public Long getOffset() {
      return offset;
    }

    @Override
    public void setOffset(Long offset) {
      this.offset = offset;
    }

    @Override
    public Integer getLimit() {
      return limit;
    }

    @Override
    public void setLimit(Integer limit) {
      this.limit = limit;
    }

    @Override
    public Date getStartTime() {
      return since;
    }

    @Override
    public void setStartTime(Date startTime) {
      this.since = startTime;
    }

    @Override
    public Date getEndTime() {
      return null;
    }

    @Override
    public void setEndTime(Date endTime) {
      throw new NotAvailableFromExchangeException();
    }
  }
}
