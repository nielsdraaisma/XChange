package org.knowm.xchange.b2c2.dto.trade;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderSide {
  BUY,
  SELL;

  @JsonCreator
  public static OrderSide getOrderSide(String s) {
    try {
      return OrderSide.valueOf(s.toUpperCase());
    } catch (Exception e) {
      throw new RuntimeException("Unknown order side " + s + ".");
    }
  }
}
