package info.bitrich.xchangestream.coinjar.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinjarWebSocketBalanceEvent extends CoinjarEvent {

  public final String topic;

  public final String event;

  public final Integer ref;

  public final Payload payload;

  public CoinjarWebSocketBalanceEvent(
      @JsonProperty("topic") String topic,
      @JsonProperty("event") String event,
      @JsonProperty("ref") Integer ref,
      @JsonProperty("payload") Payload payload) {
    this.topic = topic;
    this.event = event;
    this.ref = ref;
    this.payload = payload;
  }

  public static class Payload {
    public final Account account;

    public Payload(@JsonProperty("account") Account account) {
      this.account = account;
    }

    public static class Account {

      public final String type;
      public final String number;
      public final String hold;
      public final String balance;
      public final String available;
      public final String assetCode;

      public Account(
          @JsonProperty("type") String type,
          @JsonProperty("number") String number,
          @JsonProperty("hold") String hold,
          @JsonProperty("balance") String balance,
          @JsonProperty("available") String available,
          @JsonProperty("asset_code") String assetCode) {
        this.type = type;
        this.number = number;
        this.hold = hold;
        this.balance = balance;
        this.available = available;
        this.assetCode = assetCode;
      }
    }
  }
}
