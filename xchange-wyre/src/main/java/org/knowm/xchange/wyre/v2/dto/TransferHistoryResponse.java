package org.knowm.xchange.wyre.v2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TransferHistoryResponse {
  private final List<TransferStatus> data;
  private final Integer position;
  private final Integer recordsTotal;
  private final Integer recordsFiltered;

  public TransferHistoryResponse(
      @JsonProperty List<TransferStatus> data,
      @JsonProperty Integer position,
      @JsonProperty Integer recordsTotal,
      @JsonProperty Integer recordsFiltered) {
    this.data = data;
    this.position = position;
    this.recordsTotal = recordsTotal;
    this.recordsFiltered = recordsFiltered;
  }

  public List<TransferStatus> getData() {
    return data;
  }

  public Integer getPosition() {
    return position;
  }

  public Integer getRecordsTotal() {
    return recordsTotal;
  }

  public Integer getRecordsFiltered() {
    return recordsFiltered;
  }

  @Override
  public String toString() {
    return "TransferHistoryResponse{"
        + "data="
        + data
        + ", position="
        + position
        + ", recordsTotal="
        + recordsTotal
        + ", recordsFiltered="
        + recordsFiltered
        + '}';
  }
}
