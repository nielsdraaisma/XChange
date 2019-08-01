package org.knowm.xchange.wyre.v2.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.wyre.v2.Wyre;
import si.mazi.rescu.RestProxyFactory;

public class WyreBaseService extends BaseExchangeService implements BaseService {

  protected Wyre wyre;
  protected WyreDigest digest;

  protected String apiKey;
  protected String apiSecret;
  protected String accountId;

  public WyreBaseService(Exchange exchange) {
    super(exchange);
    this.wyre =
        RestProxyFactory.createProxy(
            Wyre.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());
    this.digest = new WyreDigest(exchange.getExchangeSpecification().getSecretKey());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.apiSecret =
        (String) exchange.getExchangeSpecification().getExchangeSpecificParametersItem("secret");
    this.accountId = exchange.getExchangeSpecification().getUserName();
  }
}
