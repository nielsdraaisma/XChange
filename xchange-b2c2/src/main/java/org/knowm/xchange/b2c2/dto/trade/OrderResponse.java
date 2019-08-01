package org.knowm.xchange.b2c2.dto.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OrderResponse {
  public final String orderId;
  public final String clientOrderId;
  public final BigDecimal quantity;
  public final OrderSide side;
  public final String instrument;
  public final BigDecimal price;
  public final BigDecimal executedPrice;
  public final List<Trade> trades;
  public final Date created;

  public OrderResponse(
      @JsonProperty("order_id") String orderId,
      @JsonProperty("client_order_id") String clientOrderId,
      @JsonProperty("quantity") BigDecimal quantity,
      @JsonProperty("side") OrderSide side,
      @JsonProperty("instrument") String instrument,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("executed_price") BigDecimal executedPrice,
      @JsonProperty("trades") List<Trade> trades,
      @JsonProperty("created") Date created) {
    this.orderId = orderId;
    this.clientOrderId = clientOrderId;
    this.quantity = quantity;
    this.side = side;
    this.instrument = instrument;
    this.price = price;
    this.executedPrice = executedPrice;
    this.trades = trades;
    this.created = created;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getClientOrderId() {
    return clientOrderId;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public OrderSide getSide() {
    return side;
  }

  public String getInstrument() {
    return instrument;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getExecutedPrice() {
    return executedPrice;
  }

  public List<Trade> getTrades() {
    return trades;
  }

  public Date getCreated() {
    return created;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    OrderResponse that = (OrderResponse) o;

    return new EqualsBuilder()
        .append(orderId, that.orderId)
        .append(clientOrderId, that.clientOrderId)
        .append(quantity, that.quantity)
        .append(side, that.side)
        .append(instrument, that.instrument)
        .append(price, that.price)
        .append(executedPrice, that.executedPrice)
        .append(trades, that.trades)
        .append(created, that.created)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(orderId)
        .append(clientOrderId)
        .append(quantity)
        .append(side)
        .append(instrument)
        .append(price)
        .append(executedPrice)
        .append(trades)
        .append(created)
        .toHashCode();
  }

  public static class Trade {
    public final String instrument;
    public final String tradeId;
    public final String rfqId;
    public final Date created;
    public final BigDecimal price;
    public final BigDecimal quantity;
    public final String order;
    public final OrderSide side;

    public Trade(
        @JsonProperty("instrument") String instrument,
        @JsonProperty("trade_id") String tradeId,
        @JsonProperty("rfq_id") String rfqId,
        @JsonProperty("created") Date created,
        @JsonProperty("price") BigDecimal price,
        @JsonProperty("quantity") BigDecimal quantity,
        @JsonProperty("order") String order,
        @JsonProperty("side") OrderSide side) {
      this.instrument = instrument;
      this.tradeId = tradeId;
      this.rfqId = rfqId;
      this.created = created;
      this.price = price;
      this.quantity = quantity;
      this.order = order;
      this.side = side;
    }

    public String getInstrument() {
      return instrument;
    }

    public String getTradeId() {
      return tradeId;
    }

    public String getRfqId() {
      return rfqId;
    }

    public Date getCreated() {
      return created;
    }

    public BigDecimal getPrice() {
      return price;
    }

    public BigDecimal getQuantity() {
      return quantity;
    }

    public String getOrder() {
      return order;
    }

    public OrderSide getSide() {
      return side;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;

      if (o == null || getClass() != o.getClass()) return false;

      Trade trade = (Trade) o;

      return new EqualsBuilder()
          .append(instrument, trade.instrument)
          .append(tradeId, trade.tradeId)
          .append(rfqId, trade.rfqId)
          .append(created, trade.created)
          .append(price, trade.price)
          .append(quantity, trade.quantity)
          .append(order, trade.order)
          .append(side, trade.side)
          .isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder(17, 37)
          .append(instrument)
          .append(tradeId)
          .append(rfqId)
          .append(created)
          .append(price)
          .append(quantity)
          .append(order)
          .append(side)
          .toHashCode();
    }
  }
}
