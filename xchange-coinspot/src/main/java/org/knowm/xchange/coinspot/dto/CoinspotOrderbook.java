package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoinspotOrderbook {
    public final String status;

    public CoinspotOrderbook(@JsonProperty("status") String status) {
        this.status = status;
    }
}
