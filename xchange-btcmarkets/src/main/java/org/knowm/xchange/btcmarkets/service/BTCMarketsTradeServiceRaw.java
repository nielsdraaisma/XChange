package org.knowm.xchange.btcmarkets.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.btcmarkets.dto.BTCMarketsBaseResponse;
import org.knowm.xchange.btcmarkets.dto.trade.*;
import org.knowm.xchange.btcmarkets.dto.v3.trade.BTCMarketsPlaceOrderRequest;
import org.knowm.xchange.btcmarkets.dto.v3.trade.BTCMarketsPlaceOrderResponse;
import org.knowm.xchange.currency.CurrencyPair;

public class BTCMarketsTradeServiceRaw extends BTCMarketsBaseService {

  public BTCMarketsTradeServiceRaw(Exchange exchange) {
    super(exchange);
  }

  public BTCMarketsPlaceOrderResponse placeBTCMarketsOrder(
      String marketId,
      BigDecimal amount,
      BigDecimal price,
      BTCMarketsOrder.Side side,
      BTCMarketsOrder.Type type,
      String timeInForce)
      throws IOException {
    return btcmv3.placeOrder(
        exchange.getExchangeSpecification().getApiKey(),
        nonceFactory,
        this.signerV3,
        new BTCMarketsPlaceOrderRequest(
            marketId,
            price.toPlainString(),
            amount.toPlainString(),
            type.toString(),
            side.toString(),
            null,
            null,
            timeInForce,
            null,
            null,
            null));
  }

  public BTCMarketsOrders getBTCMarketsOpenOrders(
      CurrencyPair currencyPair, Integer limit, Long since) throws IOException {
    BTCMarketsOpenOrdersRequest request =
        new BTCMarketsOpenOrdersRequest(
            currencyPair.counter.getCurrencyCode(),
            currencyPair.base.getCurrencyCode(),
            limit,
            since);
    return btcm.getOpenOrders(
        exchange.getExchangeSpecification().getApiKey(), nonceFactory, signerV1, request);
  }

  public BTCMarketsBaseResponse cancelBTCMarketsOrder(Long orderId) throws IOException {
    return btcm.cancelOrder(
        exchange.getExchangeSpecification().getApiKey(),
        nonceFactory,
        signerV1,
        new BTCMarketsCancelOrderRequest(orderId));
  }

  public BTCMarketsTradeHistory getBTCMarketsUserTransactions(
      CurrencyPair currencyPair, Integer limit, Long since) throws IOException {
    return btcm.getTradeHistory(
        exchange.getExchangeSpecification().getApiKey(),
        nonceFactory,
        signerV2,
        currencyPair.base.getCurrencyCode(),
        currencyPair.counter.getCurrencyCode(),
        true,
        limit,
        since);
  }

  public BTCMarketsOrders getOrderDetails(List<Long> orderIds) throws IOException {
    BTCMarketsOrderDetailsRequest request = new BTCMarketsOrderDetailsRequest(orderIds);
    return btcm.getOrderDetails(
        exchange.getExchangeSpecification().getApiKey(), nonceFactory, signerV1, request);
  }

  private String newReqId() {
    return UUID.randomUUID().toString();
  }
}
