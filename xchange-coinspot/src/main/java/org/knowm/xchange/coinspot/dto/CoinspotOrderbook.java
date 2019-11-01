package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class CoinspotOrderbook {
    public final String status;
    public final List<Order> buyOrders;
    public final List<Order> sellOrders;

    public CoinspotOrderbook(
            @JsonProperty("status") String status,
            @JsonProperty("buyorders") List<Order> buyOrders,
            @JsonProperty("sellorders") List<Order> sellOrders) {
        this.status = status;
        this.buyOrders = buyOrders;
        this.sellOrders = sellOrders;
    }

    public static class Order {
        public final BigDecimal amount;
        public final BigDecimal rate;

        public Order(
                @JsonProperty("amount") BigDecimal amount,
                @JsonProperty("rate") BigDecimal rate) {
            this.amount = amount;
            this.rate = rate;
        }
    }
}
