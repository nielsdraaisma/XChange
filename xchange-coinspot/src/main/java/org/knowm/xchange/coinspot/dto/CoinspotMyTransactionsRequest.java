package org.knowm.xchange.coinspot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class CoinspotMyTransactionsRequest extends CoinspotRequest {

  @JsonProperty("startdate")
  public final Date startDate;

  @JsonProperty("enddate")
  public final Date endDate;

  //    @JsonProperty("cointype")
  //    public final Currency coinType;

  public CoinspotMyTransactionsRequest(long nonce, Date startDate, Date endDate) {
    super(nonce);
    this.startDate = startDate;
    this.endDate = endDate;
    //        this.coinType = coinType;
  }
}
