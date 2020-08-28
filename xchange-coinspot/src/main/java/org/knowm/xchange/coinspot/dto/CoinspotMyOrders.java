package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinspotMyOrders {
  public final List<Order> buyOrders;
  public final List<Order> sellOrders;

  public CoinspotMyOrders(
      @JsonProperty("buyorders") List<Order> buyOrders,
      @JsonProperty("sellorders") List<Order> sellOrders) {
    this.buyOrders = buyOrders;
    this.sellOrders = sellOrders;
  }

  public static class Order {
    public final String id;
    public final BigDecimal amount;
    public final BigDecimal rate;
    public final BigDecimal total;
    public final String status;
    public final String coin;
    public final Long created;

    public Order(
        @JsonProperty("_id") String id,
        @JsonProperty("amount") BigDecimal amount,
        @JsonProperty("rate") BigDecimal rate,
        @JsonProperty("total") BigDecimal total,
        @JsonProperty("status") String status,
        @JsonProperty("coin") String coin,
        @JsonProperty("created") Long created) {
      this.id = id;
      this.amount = amount;
      this.rate = rate;
      this.total = total;
      this.status = status;
      this.coin = coin;
      this.created = created;
    }
  }
}
