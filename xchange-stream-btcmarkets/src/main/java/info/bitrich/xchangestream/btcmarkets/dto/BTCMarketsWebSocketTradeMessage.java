package info.bitrich.xchangestream.btcmarkets.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BTCMarketsWebSocketTradeMessage {
  public final String marketId;
  public final Date timestamp;
  public final long tradeId;
  public final BigDecimal price;
  public final BigDecimal volume;
  public final String side;
  public final String messageType;

  public BTCMarketsWebSocketTradeMessage(
      @JsonProperty("marketId") String marketId,
      @JsonProperty("timestamp") Date timestamp,
      @JsonProperty("tradeId") long tradeId,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("volume") BigDecimal volume,
      @JsonProperty("side") String side,
      @JsonProperty("messageType") String messageType) {
    this.marketId = marketId;
    this.timestamp = timestamp;
    this.tradeId = tradeId;
    this.price = price;
    this.volume = volume;
    this.side = side;
    this.messageType = messageType;
  }
}
