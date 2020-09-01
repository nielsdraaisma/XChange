package org.knowm.xchange.omf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResponse {
  public final String data;

  public SearchResponse(@JsonProperty("data") String data) {
    this.data = data;
  }
}
