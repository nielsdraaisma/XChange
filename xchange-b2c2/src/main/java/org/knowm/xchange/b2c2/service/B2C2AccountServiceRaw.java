package org.knowm.xchange.b2c2.service; // package org.knowm.xchange.b2c2.service;

import org.knowm.xchange.b2c2.B2C2Exchange;

import java.io.IOException;
import java.util.Map;

public class B2C2AccountServiceRaw extends B2C2BaseService {

    protected B2C2AccountServiceRaw(B2C2Exchange exchange) {
        super(exchange);
    }

    public Map<String, String> getAccountBalances() throws IOException {
        return b2c2.getBalances(this.authorizationHeader);
    }
}
