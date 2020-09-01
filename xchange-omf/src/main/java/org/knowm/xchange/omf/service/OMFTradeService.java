package org.knowm.xchange.omf.service;

import java.io.IOException;
import java.util.Collection;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.omf.OMFAdapters;
import org.knowm.xchange.omf.OMFExchange;
import org.knowm.xchange.omf.dto.SearchResponse;
import org.knowm.xchange.omf.dto.SpotResponse;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

public class OMFTradeService extends OMFTradeServiceRaw implements TradeService {

  public OMFTradeService(OMFExchange exchange) {
    super(exchange);
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {
    signIn();
    SpotResponse spotResponse =
        requestSpot(
            marketOrder.getCurrencyPair(), marketOrder.getType(), marketOrder.getOriginalAmount());
    acceptSpot(spotResponse);
    return spotResponse.tradeNumber;
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    OrderQueryParams param = orderQueryParams[0];
    String orderId = param.getOrderId();
    signIn();
    SearchResponse searchResponse = super.search(orderId);
    return OMFAdapters.adaptSearchResult(searchResponse.data);
  }
}
