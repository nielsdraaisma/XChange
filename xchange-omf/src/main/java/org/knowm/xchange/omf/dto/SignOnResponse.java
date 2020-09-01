package org.knowm.xchange.omf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignOnResponse {
  public final List<Customer> customers;

  public SignOnResponse(@JsonProperty("customers") List<Customer> customers) {
    this.customers = customers;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Customer {
    public final String customerId;
    public final String customerMnemonic;

    public Customer(
        @JsonProperty("CustomerId") String customerId,
        @JsonProperty("CustomerMnemonic") String customerMnemonic) {
      this.customerId = customerId;
      this.customerMnemonic = customerMnemonic;
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("customers", customers).toString();
  }
}
