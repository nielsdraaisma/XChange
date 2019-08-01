package org.knowm.xchange.wyre.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class GetWalletsResponse {
  private final List<Entry> data;

  public GetWalletsResponse(@JsonProperty("data") List<Entry> data) {
    this.data = data;
  }

  public List<Entry> getData() {
    return data;
  }

  @Override
  public String toString() {
    return "GetWalletsResponse{" + "data=" + data + '}';
  }

  public static class Entry {
    private final String srn;
    private final Map<String, String> depositAddresses;

    public Entry(
        @JsonProperty("srn") String srn,
        @JsonProperty("depositAddresses") Map<String, String> depositAddresses) {
      this.srn = srn;
      this.depositAddresses = depositAddresses;
    }

    public String getSrn() {
      return srn;
    }

    public Map<String, String> getDepositAddresses() {
      return depositAddresses;
    }

    @Override
    public String toString() {
      return "Entry{" + "srn='" + srn + '\'' + ", depositAddresses=" + depositAddresses + '}';
    }
  }
}
