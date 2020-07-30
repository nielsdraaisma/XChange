package info.bitrich.xchangestream.btcmarkets;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import info.bitrich.xchangestream.btcmarkets.dto.BTCMarketsWebSocketSubscribeMessage;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.btcmarkets.service.BTCMarketsDigestV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.mazi.rescu.SynchronizedValueFactory;

class BTCMarketsStreamingService extends JsonNettyStreamingService {
  static final String API_URI = "wss://socket.btcmarkets.net/v2";
  static final String CHANNEL_ORDERBOOK = "orderbook";
  static final String CHANNEL_ORDER_CHANGE = "orderChange";
  static final String CHANNEL_HEARTBEAT = "heartbeat";
  static final String MESSAGE_TYPE_SUBSCRIBE = "subscribe";
  static final String MESSAGE_TYPE_ADD_SUBSCRIPTION = "addSubscription";
  static final String MESSAGE_TYPE_REMOVE_SUBSCRIPTION = "removeSubscription";
  private static final Logger LOG = LoggerFactory.getLogger(BTCMarketsStreamingService.class);

  private final Set<String> subscribedOrderbooks = Sets.newConcurrentHashSet();
  private final Set<String> subscribedChannels =
      Sets.newConcurrentHashSet(Lists.newArrayList(CHANNEL_HEARTBEAT));

  // Required for request signing
  private final String apiKey;
  private final BTCMarketsDigestV3 digestV3;
  private final SynchronizedValueFactory<Long> nonceFactory;

  private BTCMarketsWebSocketSubscribeMessage buildSubscribeMessage() {
    if (subscribedOrderbooks.isEmpty()) {
      subscribedChannels.remove(CHANNEL_ORDERBOOK);
    } else {
      subscribedChannels.add(CHANNEL_ORDERBOOK);
    }
    Long timestamp = null;
    String key = null;
    String signature = null;
    // If any channel required authentication populate the required fields.
    if (subscribedChannels.contains(CHANNEL_ORDER_CHANGE) && digestV3 != null) {
      timestamp = nonceFactory.createValue();
      String stringToSign = "/users/self/subscribe" + "\n" + timestamp;
      key = apiKey;
      signature = digestV3.sign(stringToSign);
    }
    return new BTCMarketsWebSocketSubscribeMessage(
        MESSAGE_TYPE_SUBSCRIBE,
        new ArrayList<>(subscribedOrderbooks),
        Lists.newArrayList(subscribedChannels),
        timestamp,
        key,
        signature);
  }

  public BTCMarketsStreamingService(Exchange exchange) {
    super(API_URI);
    final ExchangeSpecification spec = exchange.getExchangeSpecification();
    this.nonceFactory = exchange.getNonceFactory();
    this.apiKey = spec.getApiKey();
    if (spec.getApiKey() != null && spec.getSecretKey() != null) {
      this.digestV3 = new BTCMarketsDigestV3(spec.getSecretKey());
    } else {
      this.digestV3 = null;
    }
    subscribeConnectionSuccess()
        .forEach(
            success -> {
              subscribeChannel("heartbeat");
            });
  }

  @Override
  protected String getChannelNameFromMessage(JsonNode message) {
    final String messageType = message.get("messageType").asText();
    if (messageType.startsWith(CHANNEL_ORDERBOOK)) {
      return messageType + ":" + message.get("marketId").asText();
    }
    return messageType;
  }

  @Override
  public String getSubscribeMessage(String channelName, Object... args) throws IOException {
    if (CHANNEL_ORDERBOOK.equals(channelName)) {
      subscribedOrderbooks.add(args[0].toString());
      LOG.debug("Now subscribed to orderbooks {}", subscribedOrderbooks);
      return objectMapper.writeValueAsString(buildSubscribeMessage());
    } else if (CHANNEL_ORDER_CHANGE.equals(channelName)) {
      subscribedChannels.add(channelName);
      LOG.debug("Now subscribed to channel {}", channelName);
      return objectMapper.writeValueAsString(buildSubscribeMessage());
    } else {
      throw new IllegalArgumentException(
          "Can't create subscribe messsage for channel " + channelName);
    }
  }

  public String getSubscriptionUniqueId(String channelName, Object... args) {
    if (CHANNEL_ORDERBOOK.equals(channelName)) {
      return channelName + ":" + args[0].toString();
    }
    return channelName;
  }

  @Override
  public String getUnsubscribeMessage(String channelName) throws IOException {
    if (channelName.startsWith(CHANNEL_ORDERBOOK)) {
      subscribedOrderbooks.remove(channelName);
      return objectMapper.writeValueAsString(buildSubscribeMessage());
    } else {
      return null;
    }
  }
}
