package org.knowm.xchange.acx.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AcxDepositAddress {
  public final String address;

  public AcxDepositAddress(@JsonProperty("address") String address) {
    this.address = address;
  }
}
