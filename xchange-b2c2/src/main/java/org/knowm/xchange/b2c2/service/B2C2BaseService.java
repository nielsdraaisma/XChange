package org.knowm.xchange.b2c2.service;

import org.knowm.xchange.b2c2.B2C2;
import org.knowm.xchange.b2c2.B2C2Exchange;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.exceptions.*;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class B2C2BaseService extends BaseExchangeService<B2C2Exchange> implements BaseService {

  private static final Logger log = LoggerFactory.getLogger(B2C2BaseService.class);
  protected final B2C2 b2c2;

  final String authorizationHeader;

  B2C2BaseService(B2C2Exchange exchange) {
    super(exchange);
    String apiKey = exchange.getExchangeSpecification().getApiKey();
    if (apiKey != null) {
      this.authorizationHeader = "Token " + apiKey;
    } else {
      this.authorizationHeader = null;
    }
    this.b2c2 =
        ExchangeRestProxyBuilder.forInterface(B2C2.class, exchange.getExchangeSpecification())
            .build();
  }

  ExchangeException handleException(final B2C2Exception exception) {
    B2C2Exception.Error error = exception.errors.iterator().next();
    if (exception.getHttpStatusCode() == 404) {
      return new ExchangeException("Not found");
    }
    if (error.code != null) {
      switch (error.code) {
        case 1100:
          return new ExchangeSecurityException(error.message);
        case 1011:
          return new FundsExceededException("Not enough balance");
        case 1012:
          return new FundsExceededException("Max risk exposure reached");
        case 1013:
          return new FundsExceededException("Max credit exposure reached");
        case 1015:
          return new OrderNotValidException(
              "Too many decimals – We only allow four decimals in quantities.");
        case 1001:
          return new CurrencyPairNotValidException();
        case 1019:
          return new OrderAmountUnderMinimumException();
        case 1200:
          return new ExchangeUnavailableException("B2C2 API unavailable due to API maintenance");
        default:
          log.warn(
              "No exception mapping for B2C2 exception error code {}, throwing ExchangeException",
              error.code,
              exception);
          return new ExchangeException(String.valueOf(error.code), exception);
      }
    } else return new ExchangeException(exception);
  }
}
