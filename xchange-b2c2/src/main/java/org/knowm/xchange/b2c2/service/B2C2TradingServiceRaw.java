package org.knowm.xchange.b2c2.service;

import org.knowm.xchange.b2c2.B2C2Exchange;
import org.knowm.xchange.b2c2.dto.trade.OrderRequest;
import org.knowm.xchange.b2c2.dto.trade.OrderResponse;

public class B2C2TradingServiceRaw extends B2C2BaseService {

    B2C2TradingServiceRaw(B2C2Exchange exchange) {
        super(exchange);
    }

    public OrderResponse placeLimitOrder(OrderRequest orderRequest) {
        return this.b2c2.order(this.authorizationHeader, orderRequest);
    }
}
