package info.bitrich.xchangestream.btcmarkets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BTCMarketsWebSocketOrderMessage {
  public final long orderId;
  public final String marketId;
  public final String side;
  public final String type;
  public final String openVolume;
  public final String status;
  public final String triggerStatus;
  public final List<Trade> trades;
  public final String timestamp;

  public final String messageType;

  public BTCMarketsWebSocketOrderMessage(
      @JsonProperty("orderId") long orderId,
      @JsonProperty("marketId") String marketId,
      @JsonProperty("side") String side,
      @JsonProperty("type") String type,
      @JsonProperty("openVolume") String openVolume,
      @JsonProperty("status") String status,
      @JsonProperty("triggerStatus") String triggerStatus,
      @JsonProperty("trades") List<Trade> trades,
      @JsonProperty("timestamp") String timestamp,
      @JsonProperty("messageType") String messageType) {
    this.orderId = orderId;
    this.marketId = marketId;
    this.side = side;
    this.type = type;
    this.openVolume = openVolume;
    this.status = status;
    this.triggerStatus = triggerStatus;
    this.trades = trades;
    this.timestamp = timestamp;
    this.messageType = messageType;
  }

  public static class Trade {
    public final long tradeId;
    public final String price;
    public final String volume;
    public final String fee;
    public final String liquidityType;

    public Trade(
        @JsonProperty("tradeId") long tradeId,
        @JsonProperty("price") String price,
        @JsonProperty("volume") String volume,
        @JsonProperty("fee") String fee,
        @JsonProperty("liquidityType") String liquidityType) {
      this.tradeId = tradeId;
      this.price = price;
      this.volume = volume;
      this.fee = fee;
      this.liquidityType = liquidityType;
    }
  }
}
