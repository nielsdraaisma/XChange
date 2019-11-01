package org.knowm.xchange.coinspot.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinspot.CoinspotPrivate;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.FundsExceededException;
import org.knowm.xchange.exceptions.InternalServerException;
import org.knowm.xchange.exceptions.RateLimitExceededException;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.coinspot.CoinspotPublic;
import si.mazi.rescu.RestProxyFactory;
import si.mazi.rescu.SynchronizedValueFactory;

public class CoinspotBaseService extends BaseExchangeService implements BaseService {

    protected CoinspotPublic coinspotPublic;
    protected CoinspotPrivate coinspotPrivate;
    protected String apiKey;
    protected CoinspotDigest digest;

    public CoinspotBaseService(Exchange exchange) {
        super(exchange);
        this.coinspotPublic =
                RestProxyFactory.createProxy(CoinspotPublic.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());
        this.coinspotPrivate =
                RestProxyFactory.createProxy(CoinspotPrivate.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());
        this.apiKey = exchange.getExchangeSpecification().getApiKey();
        if ( exchange.getExchangeSpecification().getSecretKey() != null) {
            this.digest = new CoinspotDigest( exchange.getExchangeSpecification().getSecretKey());
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
