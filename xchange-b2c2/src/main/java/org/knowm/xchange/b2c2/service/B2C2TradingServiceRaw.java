package org.knowm.xchange.b2c2.service;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import org.knowm.xchange.b2c2.B2C2Exchange;
import org.knowm.xchange.b2c2.dto.trade.OrderRequest;
import org.knowm.xchange.b2c2.dto.trade.OrderResponse;
import org.knowm.xchange.b2c2.dto.trade.TradeResponse;
import org.knowm.xchange.currency.CurrencyPair;

public class B2C2TradingServiceRaw extends B2C2BaseServiceRaw {

  B2C2TradingServiceRaw(B2C2Exchange exchange) {
    super(exchange);
  }

  public OrderResponse order(OrderRequest orderRequest) throws IOException {
    try {
      return this.b2c2.order(this.authorizationHeader, orderRequest);
    } catch (B2C2Exception e) {
      throw handleException(e);
    }
  }

  public TradeResponse getTrade(String id) throws IOException {
    try {
      return this.b2c2.getTrade(this.authorizationHeader, id);
    } catch (B2C2Exception e) {
      throw handleException(e);
    }
  }

  public List<OrderResponse> getOrders(
      Date createdGte,
      Date createdLt,
      String clientOrderId,
      String orderType,
      CurrencyPair instrument,
      Long offset,
      Integer limit)
      throws IOException {
    ZonedDateTime createdGteZonedDateTime = null;
    ZonedDateTime createdLtZonedDateTime = null;
    String apiInstrument = null;
    if (instrument != null) {
      apiInstrument = toApiInstrument(instrument);
    }
    if (createdGte != null) {
      createdGteZonedDateTime = ZonedDateTime.ofInstant(createdGte.toInstant(), ZoneOffset.UTC);
    }
    if (createdLt != null) {
      createdLtZonedDateTime = ZonedDateTime.ofInstant(createdLt.toInstant(), ZoneOffset.UTC);
    }
    try {
      return b2c2.getOrders(
          this.authorizationHeader,
          createdGteZonedDateTime,
          createdLtZonedDateTime,
          clientOrderId,
          orderType,
          null,
          apiInstrument,
          offset,
          limit);
    } catch (B2C2Exception e) {
      throw handleException(e);
    }
  }
}
