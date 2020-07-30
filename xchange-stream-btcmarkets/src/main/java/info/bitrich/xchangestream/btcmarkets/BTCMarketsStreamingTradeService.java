package info.bitrich.xchangestream.btcmarkets;

import static info.bitrich.xchangestream.btcmarkets.BTCMarketsStreamingService.CHANNEL_ORDER_CHANGE;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.btcmarkets.dto.BTCMarketsWebSocketOrderMessage;
import info.bitrich.xchangestream.core.StreamingTradeService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.UserTrade;

class BTCMarketsStreamingTradeService implements StreamingTradeService {

  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  private final BTCMarketsStreamingService service;

  public BTCMarketsStreamingTradeService(BTCMarketsStreamingService service) {
    this.service = service;
  }

  @Override
  public Observable<Order> getOrderChanges(CurrencyPair currencyPair, Object... args) {
    final String marketId;
    if (currencyPair != null) {
      marketId = BTCMarketsStreamingAdapters.adaptCurrencyPairToMarketId(currencyPair);
    } else {
      marketId = null;
    }
    return service
        .subscribeChannel(CHANNEL_ORDER_CHANGE)
        .map(node -> mapper.treeToValue(node, BTCMarketsWebSocketOrderMessage.class))
        .filter(orderEvent -> marketId == null || orderEvent.marketId.equals(marketId))
        .map(BTCMarketsStreamingAdapters::adaptOrder);
  }

  @Override
  public Observable<UserTrade> getUserTrades(CurrencyPair currencyPair, Object... args) {
    final String marketId;
    if (currencyPair != null) {
      marketId = BTCMarketsStreamingAdapters.adaptCurrencyPairToMarketId(currencyPair);
    } else {
      marketId = null;
    }
    return service
        .subscribeChannel(CHANNEL_ORDER_CHANGE)
        .map(node -> mapper.treeToValue(node, BTCMarketsWebSocketOrderMessage.class))
        .filter(orderEvent -> marketId == null || orderEvent.marketId.equals(marketId))
        .flatMap(
            message ->
                Observable.fromIterable(BTCMarketsStreamingAdapters.adaptUserTrades(message)));
  }
}
