package org.knowm.xchange.b2c2.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class OrderRequest {
  @JsonProperty("client_order_id")
  public final String clientOrderId;

  @JsonProperty("quantity")
  public final String quantity; // Max 4 decimals

  @JsonProperty("side")
  public final OrderSide side;

  @JsonProperty("instrument")
  public final String instrument;

  @JsonProperty("order_type")
  public final String orderType; // Only FOK allowed

  @JsonProperty("price")
  public final String price;

  @JsonProperty("order_type")
  public final Boolean forceOpen;

  @JsonProperty("order_type")
  public final String validUntil;

  public OrderRequest(
      String clientOrderId,
      String quantity,
      OrderSide side,
      String instrument,
      String orderType,
      String price,
      Boolean forceOpen,
      String validUntil) {
    this.clientOrderId = clientOrderId;
    this.quantity = quantity;
    this.side = side;
    this.instrument = instrument;
    this.orderType = orderType;
    this.price = price;
    this.forceOpen = forceOpen;
    this.validUntil = validUntil;
  }

  public String getClientOrderId() {
    return clientOrderId;
  }

  public String getQuantity() {
    return quantity;
  }

  public OrderSide getSide() {
    return side;
  }

  public String getInstrument() {
    return instrument;
  }

  public String getOrderType() {
    return orderType;
  }

  public String getPrice() {
    return price;
  }

  public Boolean getForceOpen() {
    return forceOpen;
  }

  public String getValidUntil() {
    return validUntil;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    OrderRequest that = (OrderRequest) o;

    return new EqualsBuilder()
        .append(clientOrderId, that.clientOrderId)
        .append(quantity, that.quantity)
        .append(side, that.side)
        .append(instrument, that.instrument)
        .append(orderType, that.orderType)
        .append(price, that.price)
        .append(forceOpen, that.forceOpen)
        .append(validUntil, that.validUntil)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(clientOrderId)
        .append(quantity)
        .append(side)
        .append(instrument)
        .append(orderType)
        .append(price)
        .append(forceOpen)
        .append(validUntil)
        .toHashCode();
  }
}
