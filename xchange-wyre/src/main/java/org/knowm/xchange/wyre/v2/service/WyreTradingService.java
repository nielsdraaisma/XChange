package org.knowm.xchange.wyre.v2.service;

import java.util.Arrays;
import java.util.Collection;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;
import org.knowm.xchange.wyre.v2.WyreAdapters;
import org.knowm.xchange.wyre.v2.dto.TransferRequest;
import org.knowm.xchange.wyre.v2.dto.TransferStatus;

public class WyreTradingService extends WyreTradingServiceRaw implements TradeService {

  public WyreTradingService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) {
    String sourceCurrency;
    String destCurrency;
    if (limitOrder.getType().equals(Order.OrderType.BID)) {
      sourceCurrency = limitOrder.getCurrencyPair().counter.getCurrencyCode();
      destCurrency = limitOrder.getCurrencyPair().base.getCurrencyCode();
    } else {
      sourceCurrency = limitOrder.getCurrencyPair().base.getCurrencyCode();
      destCurrency = limitOrder.getCurrencyPair().counter.getCurrencyCode();
    }
    TransferRequest transferRequest =
        new TransferRequest(
            "account:" + this.accountId,
            sourceCurrency,
            null,
            "account:" + this.accountId,
            limitOrder.getOriginalAmount().toPlainString(),
            destCurrency);
    TransferStatus result = super.transfer(transferRequest);
    return result.getId();
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) {
    OrderQueryParams params = Arrays.asList(orderQueryParams).get(0);
    TransferStatus transferStatus = getWyreTransferStatus(params.getOrderId());
    return Arrays.asList(WyreAdapters.adaptTransferStatus(transferStatus));
  }
}
