package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinspotMyTransactionsResponse extends CoinspotStatusResponse {

  public final List<Transaction> buyOrders;
  public final List<Transaction> sellOrders;

  public CoinspotMyTransactionsResponse(
      @JsonProperty("status") String status,
      @JsonProperty("buyorders") List<Transaction> buyOrders,
      @JsonProperty("sellorders") List<Transaction> sellOrders) {
    super(status);
    this.buyOrders = buyOrders;
    this.sellOrders = sellOrders;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Transaction {
    public final String market;
    public final BigDecimal amount;
    public final String created;
    public final BigDecimal audfeeExGst;
    public final BigDecimal audGst;
    public final BigDecimal audtotal;

    public Transaction(
        @JsonProperty("market") String market,
        @JsonProperty("amount") BigDecimal amount,
        @JsonProperty("created") String created,
        @JsonProperty("audfeeExGst") BigDecimal audfeeExGst,
        @JsonProperty("audGst") BigDecimal audGst,
        @JsonProperty("audtotal") BigDecimal audtotal) {
      this.market = market;
      this.amount = amount;
      this.created = created;
      this.audfeeExGst = audfeeExGst;
      this.audGst = audGst;
      this.audtotal = audtotal;
    }
  }
}
