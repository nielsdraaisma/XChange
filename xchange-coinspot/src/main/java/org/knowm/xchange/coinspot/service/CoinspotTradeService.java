package org.knowm.xchange.coinspot.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import org.knowm.xchange.coinspot.CoinspotAdapters;
import org.knowm.xchange.coinspot.CoinspotExchange;
import org.knowm.xchange.coinspot.dto.CoinspotPlaceOrderResponse;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.*;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

public class CoinspotTradeService extends CoinspotTradeServiceRaw implements TradeService {

  public CoinspotTradeService(CoinspotExchange exchange) {
    super(exchange);
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {
    return CoinspotAdapters.adaptMyOrders(super.getMyOrders());
  }

  @Override
  public OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
    return getOpenOrders();
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    CoinspotPlaceOrderResponse response = super.placeOrder(limitOrder);
    return response.id;
  }

  @Override
  public boolean cancelOrder(String orderId) {
    throw new NotAvailableFromExchangeException("Cancel order requires orderId and orderType");
  }

  @Override
  public boolean cancelOrder(CancelOrderParams cancelOrderParams) throws IOException {
    if (!(cancelOrderParams instanceof CancelOrderByIdParams)) {
      throw new IllegalArgumentException(
          "cancelOrderParams must be instance of CancelOrderByIdParams");
    }
    if (!(cancelOrderParams instanceof CancelOrderByOrderTypeParams)) {
      throw new IllegalArgumentException(
          "cancelOrderParams must be instance of CancelOrderByOrderTypeParams");
    }
    return super.cancelOrder(
        ((CancelOrderByIdParams) cancelOrderParams).getOrderId(),
        ((CancelOrderByOrderTypeParams) cancelOrderParams).getOrderType());
  }

  @Override
  public CancelOrderParams createCancelOrderParams() {
    return new CoinspotCancelOrderParams();
  }

  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {
    CurrencyPair currencyPair = null;
    Date startDate = null;
    Date endDate = null;
    if (params instanceof TradeHistoryParamCurrencyPair) {
      currencyPair = ((TradeHistoryParamCurrencyPair) params).getCurrencyPair();
    }
    if (params instanceof TradeHistoryParamsTimeSpan) {
      startDate = ((TradeHistoryParamsTimeSpan) params).getStartTime();
      endDate = ((TradeHistoryParamsTimeSpan) params).getEndTime();
    }
    return CoinspotAdapters.adaptMyTransactionsResponse(
        super.myTransactions(currencyPair, startDate, endDate), currencyPair);
  }

  @Override
  public TradeHistoryParams createTradeHistoryParams() {
    return new CoinspotTradeHistoryParams();
  }

  @Override
  public OrderQueryParams createOrdersQueryParams() {
    return null;
  }

  @Override
  public Collection<Order> getOrder(String... orderIds) throws IOException {
    return null;
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    return null;
  }

  public static class CoinspotTradeHistoryParams
      implements TradeHistoryParams, TradeHistoryParamCurrencyPair, TradeHistoryParamsTimeSpan {
    private CurrencyPair currencyPair;
    private Date startTime;
    private Date endTime;

    @Override
    public CurrencyPair getCurrencyPair() {
      return currencyPair;
    }

    @Override
    public void setCurrencyPair(CurrencyPair pair) {
      this.currencyPair = pair;
    }

    @Override
    public Date getStartTime() {
      return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
      this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
      return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
      this.endTime = endTime;
    }
  }

  public static class CoinspotCancelOrderParams
      implements CancelOrderParams, CancelOrderByIdParams, CancelOrderByOrderTypeParams {
    private String orderId;
    private Order.OrderType orderType;

    public CoinspotCancelOrderParams() {}

    public CoinspotCancelOrderParams(String orderId, Order.OrderType orderType) {
      this.orderId = orderId;
      this.orderType = orderType;
    }

    @Override
    public String getOrderId() {
      return orderId;
    }

    @Override
    public void setOrderId(String orderId) {
      this.orderId = orderId;
    }

    @Override
    public Order.OrderType getOrderType() {
      return orderType;
    }

    @Override
    public void setOrderType(Order.OrderType orderType) {
      this.orderType = orderType;
    }
  }
}
