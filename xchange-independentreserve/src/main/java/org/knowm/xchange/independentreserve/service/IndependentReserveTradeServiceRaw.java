package org.knowm.xchange.independentreserve.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.independentreserve.IndependentReserveAuthenticated;
import org.knowm.xchange.independentreserve.dto.trade.*;
import org.knowm.xchange.independentreserve.dto.trade.IndependentReserveTransaction.Type;
import org.knowm.xchange.independentreserve.util.ExchangeEndpoint;
import si.mazi.rescu.RestProxyFactory;

/** Author: Kamil Zbikowski Date: 4/13/15 */
public class IndependentReserveTradeServiceRaw extends IndependentReserveBaseService {
  private final int TRADE_HISTORY_PAGE_SIZE = 50;
  private final int TRANSACTION_HISTORY_PAGE_SIZE = 50;
  private final IndependentReserveDigest signatureCreator;
  private final IndependentReserveAuthenticated independentReserveAuthenticated;

  /**
   * Constructor
   *
   * @param exchange
   */
  protected IndependentReserveTradeServiceRaw(Exchange exchange) {
    super(exchange);

    this.independentReserveAuthenticated =
        RestProxyFactory.createProxy(
            IndependentReserveAuthenticated.class,
            exchange.getExchangeSpecification().getSslUri(),
            getClientConfig());
    this.signatureCreator =
        IndependentReserveDigest.createInstance(
            exchange.getExchangeSpecification().getSecretKey(),
            exchange.getExchangeSpecification().getApiKey(),
            exchange.getExchangeSpecification().getSslUri());
  }

  /**
   * @param primaryCurrency - optional primary currency code
   * @param secondaryCurrency - optional secondary currency code
   * @param pageIndex - The page index. Must be greater or equal to 1
   * @return
   * @throws IOException
   */
  public IndependentReserveOpenOrdersResponse getIndependentReserveOpenOrders(
      String primaryCurrency, String secondaryCurrency, int pageIndex) throws IOException {
    if (pageIndex <= 0) {
      throw new IllegalArgumentException("Page number in IndependentReserve should be positive.");
    }
    Long nonce = exchange.getNonceFactory().createValue();
    String apiKey = exchange.getExchangeSpecification().getApiKey();
    IndependentReserveOpenOrderRequest independentReserveOpenOrderRequest =
        new IndependentReserveOpenOrderRequest(
            apiKey, nonce, primaryCurrency, secondaryCurrency, pageIndex, TRADE_HISTORY_PAGE_SIZE);

    independentReserveOpenOrderRequest.setSignature(
        signatureCreator.digestParamsToString(
            ExchangeEndpoint.GET_OPEN_ORDERS,
            nonce,
            independentReserveOpenOrderRequest.getParameters()));

    IndependentReserveOpenOrdersResponse openOrders =
        independentReserveAuthenticated.getOpenOrders(independentReserveOpenOrderRequest);

    return openOrders;
  }

  public String independentReservePlaceLimitOrder(
      CurrencyPair currencyPair,
      Order.OrderType type,
      BigDecimal limitPrice,
      BigDecimal originalAmount)
      throws IOException {
    Long nonce = exchange.getNonceFactory().createValue();
    String apiKey = exchange.getExchangeSpecification().getApiKey();

    String orderType = null;
    if (type == Order.OrderType.ASK) {
      orderType = "LimitOffer";
    } else if (type == Order.OrderType.BID) {
      orderType = "LimitBid";
    }

    IndependentReservePlaceLimitOrderRequest independentReservePlaceLimitOrderRequest =
        new IndependentReservePlaceLimitOrderRequest(
            apiKey,
            nonce,
            currencyPair.base.getCurrencyCode(),
            currencyPair.counter.getCurrencyCode(),
            orderType,
            limitPrice.toString(),
            originalAmount.toString());
    independentReservePlaceLimitOrderRequest.setSignature(
        signatureCreator.digestParamsToString(
            ExchangeEndpoint.PLACE_MARKET_ORDER,
            nonce,
            independentReservePlaceLimitOrderRequest.getParameters()));

    IndependentReservePlaceOrderResponse independentReservePlaceLimitOrderResponse =
        independentReserveAuthenticated.placeLimitOrder(independentReservePlaceLimitOrderRequest);

    return independentReservePlaceLimitOrderResponse.getOrderGuid();
  }

  public String independentReservePlaceMarketOrder(
      CurrencyPair currencyPair, Order.OrderType type, BigDecimal originalAmount)
      throws IOException {
    Long nonce = exchange.getNonceFactory().createValue();
    String apiKey = exchange.getExchangeSpecification().getApiKey();

    String orderType = null;
    if (type == Order.OrderType.ASK) {
      orderType = "LimitOffer";
    } else if (type == Order.OrderType.BID) {
      orderType = "LimitBid";
    }

    IndependentReservePlaceMarketOrderRequest independentReservePlaceMarketOrderRequest =
        new IndependentReservePlaceMarketOrderRequest(
            apiKey,
            nonce,
            currencyPair.base.getCurrencyCode(),
            currencyPair.counter.getCurrencyCode(),
            orderType,
            originalAmount.toString());
    independentReservePlaceMarketOrderRequest.setSignature(
        signatureCreator.digestParamsToString(
            ExchangeEndpoint.PLACE_LIMIT_ORDER,
            nonce,
            independentReservePlaceMarketOrderRequest.getParameters()));

    IndependentReservePlaceOrderResponse independentReservePlaceLimitOrderResponse =
        independentReserveAuthenticated.placeMarketOrder(independentReservePlaceMarketOrderRequest);

    return independentReservePlaceLimitOrderResponse.getOrderGuid();
  }

  public boolean independentReserveCancelOrder(String orderId) throws IOException {
    Long nonce = exchange.getNonceFactory().createValue();
    String apiKey = exchange.getExchangeSpecification().getApiKey();

    IndependentReserveCancelOrderRequest independentReserveCancelOrderRequest =
        new IndependentReserveCancelOrderRequest(apiKey, nonce, orderId);

    independentReserveCancelOrderRequest.setSignature(
        signatureCreator.digestParamsToString(
            ExchangeEndpoint.CANCEL_ORDER,
            nonce,
            independentReserveCancelOrderRequest.getParameters()));

    IndependentReserveCancelOrderResponse independentReserveCancelOrderResponse =
        independentReserveAuthenticated.cancelOrder(independentReserveCancelOrderRequest);

    if (independentReserveCancelOrderResponse.getStatus() != null) {
      return independentReserveCancelOrderResponse.getStatus().equals("Cancelled")
          || independentReserveCancelOrderResponse.getStatus().equals("PartiallyFilledAndCancelled")
          || independentReserveCancelOrderResponse.getStatus().equals("Expired")
          || independentReserveCancelOrderResponse.getStatus().equals("Expired");
    } else {
      return false;
    }
  }

  public IndependentReserveTradeHistoryResponse getIndependentReserveTradeHistory(int pageIndex)
      throws IOException {
    if (pageIndex <= 0) {
      throw new IllegalArgumentException("Page number in IndependentReserve should be positive.");
    }
    Long nonce = exchange.getNonceFactory().createValue();
    String apiKey = exchange.getExchangeSpecification().getApiKey();

    IndependentReserveTradeHistoryRequest independentReserveTradeHistoryRequest =
        new IndependentReserveTradeHistoryRequest(
            apiKey, nonce, pageIndex, TRADE_HISTORY_PAGE_SIZE);

    independentReserveTradeHistoryRequest.setSignature(
        signatureCreator.digestParamsToString(
            ExchangeEndpoint.GET_TRADES,
            nonce,
            independentReserveTradeHistoryRequest.getParameters()));

    IndependentReserveTradeHistoryResponse trades =
        independentReserveAuthenticated.getTradeHistory(independentReserveTradeHistoryRequest);

    return trades;
  }

  public IndependentReserveTransactionsResponse getIndependentReserveTransactions(
      String accountGuid, Date fromTimestampUtc, Date toTimestampUtc, Type[] txTypes, int pageIndex)
      throws IOException {
    if (pageIndex <= 0) {
      throw new IllegalArgumentException("Page number in IndependentReserve should be positive.");
    }
    Long nonce = exchange.getNonceFactory().createValue();
    String apiKey = exchange.getExchangeSpecification().getApiKey();
    IndependentReserveTransactionsRequest req =
        new IndependentReserveTransactionsRequest(
            apiKey,
            nonce,
            accountGuid,
            fromTimestampUtc,
            toTimestampUtc,
            txTypes,
            pageIndex,
            TRANSACTION_HISTORY_PAGE_SIZE);
    req.setSignature(
        signatureCreator.digestParamsToString(
            ExchangeEndpoint.GET_TRANSACTIONS, nonce, req.getParameters()));
    return independentReserveAuthenticated.getTransactions(req);
  }

  public IndependentReserveOrderDetailsResponse getOrderDetails(String orderGuid)
      throws IOException {
    Long nonce = exchange.getNonceFactory().createValue();
    String apiKey = exchange.getExchangeSpecification().getApiKey();

    IndependentReserveOrderDetailsRequest request =
        new IndependentReserveOrderDetailsRequest(apiKey, nonce, orderGuid);
    request.setSignature(
        signatureCreator.digestParamsToString(
            ExchangeEndpoint.GET_ORDER_DETAILS, nonce, request.getParameters()));
    return independentReserveAuthenticated.orderDetails(request);
  }
}
