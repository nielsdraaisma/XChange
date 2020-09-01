package org.knowm.xchange.omf.service;

import java.io.IOException;
import java.math.BigDecimal;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HTTP;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.omf.OMFExchange;
import org.knowm.xchange.omf.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OMFTradeServiceRaw extends OMFBaseService {

  private static final Logger LOG = LoggerFactory.getLogger(OMFTradeServiceRaw.class);

  private static final String spotUrl = "https://www.omfx24.co.nz/?Hive=fx&Method=FXRFQOrderUpdate";
  private static final String fillUrl = "https://www.omfx24.co.nz/?Hive=fx&Method=FXOrderFill";
  private static final String searchUrl =
      "https://www.omfx24.co.nz/?Hive=trading&Method=SearchTradesClient";

  public OMFTradeServiceRaw(OMFExchange exchange) {
    super(exchange);
  }

  public SearchResponse search(String orderId) throws IOException {
    SearchRequest searchRequest = new SearchRequest(orderId, orderId);
    HttpPost request = new HttpPost(searchUrl);
    request.setConfig(RequestConfig.custom().setExpectContinueEnabled(true).build());
    request.setEntity(new ByteArrayEntity(objectMapper.writeValueAsBytes(searchRequest)));
    request.addHeader(HTTP.EXPECT_DIRECTIVE, HTTP.EXPECT_CONTINUE);

    LOG.info("Sending request {}", searchRequest);
    HttpResponse response = httpClient.execute(request);
    SearchResponse searchResponse =
        objectMapper.readValue(response.getEntity().getContent(), SearchResponse.class);
    LOG.info("Received response {}", searchResponse);
    return searchResponse;
  }

  public SpotResponse requestSpot(
      CurrencyPair currencyPair, Order.OrderType orderType, BigDecimal quantity)
      throws IOException {
    String customerDirection;
    String direction;
    if (orderType == Order.OrderType.ASK) {
      customerDirection = "BUY";
      direction = "SELL";
    } else {
      customerDirection = "SELL";
      direction = "BUY";
    }
    SpotRequest spotRequest =
        new SpotRequest(
            currencyPair.base.toString(),
            currencyPair.counter.toString(),
            customerDirection,
            direction,
            quantity.toPlainString(),
            exchange.getCustomerMnemonic(),
            exchange.getCustomerMnemonic());
    HttpPost request = new HttpPost(spotUrl);
    request.setConfig(RequestConfig.custom().setExpectContinueEnabled(true).build());
    request.setEntity(new ByteArrayEntity(objectMapper.writeValueAsBytes(spotRequest)));
    request.addHeader(HTTP.EXPECT_DIRECTIVE, HTTP.EXPECT_CONTINUE);

    LOG.info("Sending request {}", spotRequest);
    HttpResponse response = httpClient.execute(request);
    SpotResponse spotResponse =
        objectMapper.readValue(response.getEntity().getContent(), SpotResponse.class);
    LOG.info("Received response {}", spotResponse);
    return spotResponse;
  }

  public boolean acceptSpot(SpotResponse spotResponse) throws IOException {
    OrderFillRequest orderFillRequest =
        new OrderFillRequest(
            spotResponse.tradeHeaderGuid,
            spotResponse.customerCctCcy2fwdRate,
            spotResponse.cc1ccy2fwdRate,
            spotResponse.cc1ccy2spotRate);

    HttpPost request = new HttpPost(fillUrl);
    request.setConfig(RequestConfig.custom().setExpectContinueEnabled(true).build());
    request.setEntity(new ByteArrayEntity(objectMapper.writeValueAsBytes(orderFillRequest)));
    request.addHeader(HTTP.EXPECT_DIRECTIVE, HTTP.EXPECT_CONTINUE);

    LOG.info("Sending request {}", orderFillRequest);
    HttpResponse response = httpClient.execute(request);
    LOG.info("Received respone {}", response);
    return response.getStatusLine().getStatusCode() == 200;
  }
}
