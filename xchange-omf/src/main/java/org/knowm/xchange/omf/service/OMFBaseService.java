package org.knowm.xchange.omf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.knowm.xchange.omf.OMFExchange;
import org.knowm.xchange.omf.dto.SignOnRequest;
import org.knowm.xchange.omf.dto.SignOnResponse;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;

public class OMFBaseService extends BaseExchangeService<OMFExchange> implements BaseService {
  protected final String signOnUrl = "https://www.omfx24.co.nz/?Hive=base&Method=signon";

  private final String accountId;
  protected HttpClient httpClient;
  protected ObjectMapper objectMapper;
  protected HttpClientContext context;

  public OMFBaseService(OMFExchange exchange) {
    super(exchange);
    this.httpClient = HttpClientBuilder.create().build();
    this.objectMapper = new ObjectMapper();
    this.context = HttpClientContext.create();
    context.setRequestConfig(RequestConfig.custom().setExpectContinueEnabled(true).build());
    context.setCookieStore(exchange.getCookieStore());
    if (exchange
        .getExchangeSpecification()
        .getExchangeSpecificParameters()
        .containsKey(OMFExchange.OMF_ACCOUNT_ID)) {
      accountId =
          exchange
              .getExchangeSpecification()
              .getExchangeSpecificParametersItem(OMFExchange.OMF_ACCOUNT_ID)
              .toString();
    } else {
      accountId = null;
    }
  }

  public SignOnResponse signIn() throws IOException {
    SignOnRequest signOnRequest =
        new SignOnRequest(
            exchange.getExchangeSpecification().getUserName(),
            exchange.getExchangeSpecification().getPassword());
    HttpPost request = new HttpPost(signOnUrl);
    request.setEntity(new ByteArrayEntity(objectMapper.writeValueAsBytes(signOnRequest)));

    HttpResponse response = httpClient.execute(request);
    SignOnResponse signOnResponse =
        objectMapper.readValue(response.getEntity().getContent(), SignOnResponse.class);
    for (SignOnResponse.Customer customer : signOnResponse.customers) {
      if (accountId == null || customer.customerId.equals(accountId)) {
        exchange.setCustomerId(signOnResponse.customers.get(0).customerId);
        exchange.setCustomerMnemonic(signOnResponse.customers.get(0).customerMnemonic);
        return signOnResponse;
      }
    }
    return null;
  }
}
