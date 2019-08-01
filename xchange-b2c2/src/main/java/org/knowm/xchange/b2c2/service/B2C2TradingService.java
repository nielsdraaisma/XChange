package org.knowm.xchange.b2c2.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.b2c2.dto.trade.OrderRequest;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.trade.TradeService;

import java.io.IOException;
import java.util.UUID;

public class B2C2TradingService extends B2C2TradingServiceRaw implements TradeService {
    public B2C2TradingService(Exchange exchange) {

    }

    @Override
    public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
        String orderId = limitOrder.getId();
        if (orderId == null) {
            orderId = UUID.randomUUID().toString();
        }
        String side;
        switch (limitOrder.getType()) {
            case BID:
                side = "buy";
                break;
            case ASK:
                side = "sell";
                break;
        }
        OrderRequest request = new OrderRequest(orderId, limitOrder.getOriginalAmount(), )
        return super.placeLimitOrder()
    }
}
