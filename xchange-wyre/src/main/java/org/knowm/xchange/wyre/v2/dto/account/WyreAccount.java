package org.knowm.xchange.wyre.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public class WyreAccount {

    private final String id;
    private final Map<String, BigDecimal> totalBalances;

    public WyreAccount(@JsonProperty("id") String id, @JsonProperty("totalBalances") Map<String, BigDecimal> totalBalances) {
        this.id = id;
        this.totalBalances = totalBalances;
    }

    public String getId() {
        return id;
    }

    public Map<String, BigDecimal> getTotalBalances() {
        return totalBalances;
    }

}
