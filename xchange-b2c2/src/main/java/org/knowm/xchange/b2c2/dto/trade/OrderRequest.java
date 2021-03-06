package org.knowm.xchange.b2c2.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Model that reflects the placement of a new order. */
public final class OrderRequest {
  @JsonProperty("client_order_id")
  public final String clientOrderId;

  @JsonProperty("quantity")
  public final String quantity; // Max 4 decimals

  @JsonProperty("side")
  public final String side; // buy / sell

  @JsonProperty("instrument")
  public final String instrument;

  @JsonProperty("order_type")
  public final String orderType; // Only FOK and MKT allowed

  @JsonProperty("price")
  public final String price;

  @JsonProperty("force_open")
  public final Boolean forceOpen;

  @JsonProperty("valid_until")
  public final String validUntil;

  @JsonProperty("executing_unit")
  public final String executingUnit;

  public OrderRequest(
      String clientOrderId,
      String quantity,
      String side,
      String instrument,
      String orderType,
      String price,
      Boolean forceOpen,
      String validUntil,
      String executingUnit) {
    this.clientOrderId = clientOrderId;
    this.quantity = quantity;
    this.side = side;
    this.instrument = instrument;
    this.orderType = orderType;
    this.price = price;
    this.forceOpen = forceOpen;
    this.validUntil = validUntil;
    this.executingUnit = executingUnit;
  }
}
