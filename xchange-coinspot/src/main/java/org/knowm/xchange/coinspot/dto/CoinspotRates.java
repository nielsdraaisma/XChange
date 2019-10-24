package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public class CoinspotRates {
    public final String status;
    public final Map<String, CoinspotRate> prices;

    public CoinspotRates(
            @JsonProperty("status") String status,
            @JsonProperty("prices") Map<String, CoinspotRate> prices) {
        this.status = status;
        this.prices = prices;
    }

    public static class CoinspotRate {
        public final BigDecimal bid;
        public final BigDecimal ask;
        public final BigDecimal last;

        public CoinspotRate(
                @JsonProperty("bid") BigDecimal bid,
                @JsonProperty("ask") BigDecimal ask,
                @JsonProperty("last") BigDecimal last) {
            this.bid = bid;
            this.ask = ask;
            this.last = last;
        }
    }
}
