package org.knowm.xchange.omf;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.omf.service.OMFTradeService;
import si.mazi.rescu.SynchronizedValueFactory;

public class OMFExchange extends BaseExchange implements Exchange {

  protected CookieStore cookieStore;

  protected String customerId;
  protected String customerMnemonic;

  @Override
  protected void initServices() {
    this.tradeService = new OMFTradeService(this);
    this.cookieStore = new BasicCookieStore();
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification =
        new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://www.omfx24.co.nz/");
    exchangeSpecification.setExchangeName("OMF");
    exchangeSpecification.setExchangeDescription("OMF is a FX Broker");
    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {
    // No private API implemented. Not needed for this exchange at the moment.
    return null;
  }

  public CookieStore getCookieStore() {
    return cookieStore;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getCustomerMnemonic() {
    return customerMnemonic;
  }

  public void setCustomerMnemonic(String customerMnemonic) {
    this.customerMnemonic = customerMnemonic;
  }
}
