package org.knowm.xchange.coinspot.service;

import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.coinspot.CoinspotExchange;
import org.knowm.xchange.coinspot.CoinspotPrivate;
import org.knowm.xchange.coinspot.CoinspotPublic;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.FundsExceededException;
import org.knowm.xchange.exceptions.InternalServerException;
import org.knowm.xchange.exceptions.RateLimitExceededException;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;

public class CoinspotBaseService extends BaseExchangeService<CoinspotExchange>
    implements BaseService {

  protected CoinspotPublic coinspotPublic;
  protected CoinspotPrivate coinspotPrivate;
  protected String apiKey;
  protected CoinspotDigest digest;

  public CoinspotBaseService(CoinspotExchange exchange) {
    super(exchange);
    this.coinspotPublic =
        ExchangeRestProxyBuilder.forInterface(
                CoinspotPublic.class, exchange.getExchangeSpecification())
            .build();
    this.coinspotPrivate =
        ExchangeRestProxyBuilder.forInterface(
                CoinspotPrivate.class, exchange.getExchangeSpecification())
            .build();
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    if (exchange.getExchangeSpecification().getSecretKey() != null) {
      this.digest = new CoinspotDigest(exchange.getExchangeSpecification().getSecretKey());
    }
  }

  protected ExchangeException handleError(CoinspotException exception) {

    if (exception.getMessage().contains("Insufficient")) {
      return new FundsExceededException(exception);
    } else if (exception.getMessage().contains("Rate limit exceeded")) {
      return new RateLimitExceededException(exception);
    } else if (exception.getMessage().contains("Internal server error")) {
      return new InternalServerException(exception);
    } else {
      return new ExchangeException(exception);
    }
  }
}
