package info.bitrich.xchangestream.independentreserve;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.math.BigDecimalMath;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.independentreserve.dto.IndependentReserveWebSocketOrderEvent;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.utils.BigDecimalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndependentReserveStreamingMarketDataService implements StreamingMarketDataService {

  private static final Logger logger =
      LoggerFactory.getLogger(IndependentReserveStreamingMarketDataService.class);

  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  private final IndependentReserveStreamingService service;
  private final MarketDataService marketDataService;

  public IndependentReserveStreamingMarketDataService(
      MarketDataService marketDataService, IndependentReserveStreamingService service) {
    this.service = service;
    this.marketDataService = marketDataService;
  }

  private OrderBook handleOrderbookEvent(
      CurrencyPair currencyPair,
      Map<String, LimitOrder> bids,
      Map<String, LimitOrder> asks,
      AtomicLong nonce,
      IndependentReserveWebSocketOrderEvent event) {

    long expectedNonce = nonce.get();
    long nonceFromEvent = event.nonce;
    if (expectedNonce != -1 && nonceFromEvent != expectedNonce) {
      logger.warn(
          "Did not get expected nonce from channel - expected {} but got {}, clearing {} book and loading initial state", expectedNonce, nonceFromEvent, currencyPair);
      nonce.set(-1);
      bids.clear();
      asks.clear();
      this.loadInitialState(currencyPair, bids, asks);
    }
    nonce.set(event.nonce + 1);
    final Order.OrderType orderType;
    if (event.data.orderType.equals("LimitBid")) {
      orderType = Order.OrderType.BID;
    } else {
      orderType = Order.OrderType.ASK;
    }
    final Map<String, LimitOrder> orderMap;
    if (orderType == Order.OrderType.BID) {
      orderMap = bids;
    } else {
      orderMap = asks;
    }
    LimitOrder order;
    switch (event.event) {
      case IndependentReserveWebSocketOrderEvent.NEW_ORDER:
        order =
            new LimitOrder.Builder(orderType, currencyPair)
                .originalAmount(event.data.volume)
                .id(event.data.orderGuid)
                .limitPrice(event.data.price)
                .build();
        orderMap.put(event.data.orderGuid, order);
        // Remove any opposite orders to avoid crossed books
        Collection<String> crossedOrders;
        if (orderType == Order.OrderType.BID) {
           crossedOrders = asks.entrySet().stream().filter(o -> o.getValue().getLimitPrice().compareTo(event.data.price) < 0).map(Map.Entry::getKey).collect(Collectors.toList());

        } else {
          crossedOrders = bids.entrySet().stream().filter(o -> o.getValue().getLimitPrice().compareTo(event.data.price) > 0).map(Map.Entry::getKey).collect(Collectors.toList());
        }
        crossedOrders.forEach(id -> {
            bids.remove(id);
            asks.remove(id);
        });

        break;
      case IndependentReserveWebSocketOrderEvent.ORDER_CANCELED:
        orderMap.remove(event.data.orderGuid);
        break;
      case IndependentReserveWebSocketOrderEvent.ORDER_CHANGED:
        // Fully filled orders are treated as removal
        if (event.data.volume.compareTo(BigDecimal.ZERO) == 0) {
          orderMap.remove(event.data.orderGuid);
          break;
        }
        order = orderMap.get(event.data.orderGuid);
        if (order != null) {
          order =
              new LimitOrder.Builder(order.getType(), currencyPair)
                  .originalAmount(event.data.volume)
                  .id(event.data.orderGuid)
                  .limitPrice(order.getLimitPrice())
                  .build();
          orderMap.put(event.data.orderGuid, order);
        }
        break;
    }

    return new OrderBook(
        null, Lists.newArrayList(asks.values()), Lists.newArrayList(bids.values()));
  }

  private void loadInitialState(
      CurrencyPair currencyPair, Map<String, LimitOrder> bids, Map<String, LimitOrder> asks) {
    try {
      bids.clear();
      asks.clear();
      OrderBook orderBook = this.marketDataService.getOrderBook(currencyPair);
      orderBook
          .getBids()
          .forEach(
              bid -> {
                if (bid.getOriginalAmount().compareTo(BigDecimal.ZERO) > 0) {
                  bids.put(bid.getId(), bid);
                }
              });
      orderBook
          .getAsks()
          .forEach(
              bid -> {
                if (bid.getOriginalAmount().compareTo(BigDecimal.ZERO) > 0) {
                  asks.put(bid.getId(), bid);
                }
              });
      logger.info(
          "Loaded {} orderbook after subscribing to stream now have {} bids, {} asks",
          currencyPair,
          bids.size(),
          asks.size());
    } catch (IOException e) {
      logger.warn("Failed to load initial state for {} orderbook", currencyPair);
    }
  }

  @Override
  public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
    String channelName =
        "orderbook-"
            + currencyPair.base.toString().toLowerCase()
            + "-"
            + currencyPair.counter.toString().toLowerCase();
    final Map<String, LimitOrder> bids = Maps.newHashMap();
    final Map<String, LimitOrder> asks = Maps.newHashMap();
    final AtomicLong nonces = new AtomicLong(-1);

    Observable<JsonNode> subscription = service.subscribeChannel(channelName);
    this.loadInitialState(currencyPair, bids, asks);
    return subscription.map(
            node -> {
              IndependentReserveWebSocketOrderEvent orderEvent =
                  mapper.treeToValue(node, IndependentReserveWebSocketOrderEvent.class);
              return handleOrderbookEvent(currencyPair, bids, asks, nonces, orderEvent);
            });
  }

  @Override
  public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
    throw new NotAvailableFromExchangeException();
  }

  @Override
  public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
    throw new NotAvailableFromExchangeException();
  }
}
