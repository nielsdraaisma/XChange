package org.knowm.xchange.wyre.v2.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.wyre.v2.dto.QuoteTransferRequest;
import org.knowm.xchange.wyre.v2.dto.TransferStatus;
import si.mazi.rescu.SynchronizedValueFactory;

class WyreMarketDataServiceRaw extends WyreBaseService {

  private final SynchronizedValueFactory<Long> nonceFactory;

  WyreMarketDataServiceRaw(Exchange exchange) {
    super(exchange);
    this.nonceFactory = exchange.getNonceFactory();
  }

  Map<String, BigDecimal> getRates() throws WyreException, IOException {
    return wyre.getRates("2", apiKey, digest, nonceFactory.createValue());
  }

  private String toPlainStringIfNotNull(BigDecimal value) {
    if (value != null) {
      return value.toPlainString();
    } else {
      return null;
    }
  }

  TransferStatus quoteTransfer(
      CurrencyPair currencyPair, BigDecimal sourceAmount, BigDecimal destAmount)
      throws WyreException {
    return wyre.quoteTransfer(
        "2",
        apiKey,
        digest,
        nonceFactory.createValue(),
        new QuoteTransferRequest(
            "account:" + this.accountId,
            currencyPair.base.getCurrencyCode(),
            toPlainStringIfNotNull(sourceAmount),
            "account:" + this.accountId,
            toPlainStringIfNotNull(destAmount),
            currencyPair.counter.getCurrencyCode()));
  }
}
