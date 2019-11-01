package org.knowm.xchange.btcmarkets.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BTCMarketsAddressesResponse {
    public final String address;

    public BTCMarketsAddressesResponse(@JsonProperty("address") String address) {
        this.address = address;
    }
}
