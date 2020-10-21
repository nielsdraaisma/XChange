package org.knowm.xchange.coinspot.service;

import java.io.IOException;
import java.util.Date;
import org.knowm.xchange.coinspot.CoinspotExchange;
import org.knowm.xchange.coinspot.dto.*;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import si.mazi.rescu.SynchronizedValueFactory;

class CoinspotTradeServiceRaw extends CoinspotBaseService {

  private SynchronizedValueFactory<Long> nonceFactory;

  public CoinspotTradeServiceRaw(CoinspotExchange exchange) {
    super(exchange);
    this.nonceFactory = exchange.getNonceFactory();
  }

  protected CoinspotMyOrders getMyOrders() throws IOException {
    return coinspotPrivate.getMyOrders(
        exchange.getExchangeSpecification().getApiKey(),
        digest,
        new CoinspotRequest(nonceFactory.createValue()));
  }

  protected CoinspotPlaceOrderResponse placeOrder(LimitOrder order) throws IOException {
    String coinType = order.getCurrencyPair().base.toString();
    CoinspotPlaceOrderRequest request =
        new CoinspotPlaceOrderRequest(
            nonceFactory.createValue(), coinType, order.getOriginalAmount(), order.getLimitPrice());
    if (order.getType() == Order.OrderType.BID) {
      return coinspotPrivate.placeBuyOrder(
          exchange.getExchangeSpecification().getApiKey(), digest, request);
    } else if (order.getType() == Order.OrderType.ASK) {
      return coinspotPrivate.placeSellOrder(
          exchange.getExchangeSpecification().getApiKey(), digest, request);
    } else {
      return null;
    }
  }

  protected boolean cancelOrder(String orderId, Order.OrderType orderType) throws IOException {
    CoinspotCancelOrderRequest request =
        new CoinspotCancelOrderRequest(nonceFactory.createValue(), orderId);
    CoinspotStatusResponse response;
    if (orderType == Order.OrderType.BID) {
      response =
          coinspotPrivate.cancelBuyOrder(
              exchange.getExchangeSpecification().getApiKey(), digest, request);
    } else if (orderType == Order.OrderType.ASK) {
      response =
          coinspotPrivate.cancelSellOrder(
              exchange.getExchangeSpecification().getApiKey(), digest, request);
    } else {
      throw new IllegalArgumentException("Invalid orderType given : " + orderType);
    }
    return "ok".equals(response.status);
  }

  protected CoinspotMyTransactionsResponse myTransactions(
      CurrencyPair currencyPair, Date startDate, Date endDate) throws IOException {
    CoinspotMyTransactionsRequest request =
        new CoinspotMyTransactionsRequest(nonceFactory.createValue(), startDate, endDate);
    if (currencyPair == null) {
      return coinspotPrivate.getMyTransactions(
          exchange.getExchangeSpecification().getApiKey(), digest, request);
    } else {
      return coinspotPrivate.getMyTransactionsForCoin(
          exchange.getExchangeSpecification().getApiKey(),
          digest,
          currencyPair.base.toString(),
          request);
    }
  }
}
