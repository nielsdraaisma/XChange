package org.knowm.xchange.b2c2.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {
  public final String orderId;
  public final String clientOrderId;
  public final BigDecimal quantity;
  public final String side;
  public final String instrument;
  public final BigDecimal price;
  public final BigDecimal executedPrice;
  public final List<Trade> trades;
  public final String created;
  public final String orderType;
  public final String executingUnit;

  public OrderResponse(
      @JsonProperty("order_id") String orderId,
      @JsonProperty("client_order_id") String clientOrderId,
      @JsonProperty("quantity") BigDecimal quantity,
      @JsonProperty("side") String side,
      @JsonProperty("instrument") String instrument,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("executed_price") BigDecimal executedPrice,
      @JsonProperty("trades") List<Trade> trades,
      @JsonProperty("created") String created,
      @JsonProperty("order_type") String orderType,
      @JsonProperty("executing_unit") String executingUnit) {
    this.orderId = orderId;
    this.clientOrderId = clientOrderId;
    this.quantity = quantity;
    this.side = side;
    this.instrument = instrument;
    this.price = price;
    this.executedPrice = executedPrice;
    this.trades = trades;
    this.created = created;
    this.orderType = orderType;
    this.executingUnit = executingUnit;
  }

  public static class Trade {
    public final String instrument;
    public final String tradeId;
    public final String rfqId;
    public final String created;
    public final BigDecimal price;
    public final BigDecimal quantity;
    public final String order;
    public final String side;

    public Trade(
        @JsonProperty("instrument") String instrument,
        @JsonProperty("trade_id") String tradeId,
        @JsonProperty("rfq_id") String rfqId,
        @JsonProperty("created") String created,
        @JsonProperty("price") BigDecimal price,
        @JsonProperty("quantity") BigDecimal quantity,
        @JsonProperty("order") String order,
        @JsonProperty("side") String side) {
      this.instrument = instrument;
      this.tradeId = tradeId;
      this.rfqId = rfqId;
      this.created = created;
      this.price = price;
      this.quantity = quantity;
      this.order = order;
      this.side = side;
    }
  }
}
