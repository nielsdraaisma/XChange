package info.bitrich.xchangestream.coinjar.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinjarWebsocketTradeEvent {
  public final BigDecimal value;
  public final Date timestamp;
  public final long tid;
  public final String takerSide;
  public final BigDecimal size;
  public final BigDecimal price;

  public CoinjarWebsocketTradeEvent(
      @JsonProperty("value") BigDecimal value,
      @JsonProperty("timestamp") Date timestamp,
      @JsonProperty("tid") long tid,
      @JsonProperty("taker_side") String takerSide,
      @JsonProperty("size") BigDecimal size,
      @JsonProperty("price") BigDecimal price) {
    this.value = value;
    this.timestamp = timestamp;
    this.tid = tid;
    this.takerSide = takerSide;
    this.size = size;
    this.price = price;
  }
}
