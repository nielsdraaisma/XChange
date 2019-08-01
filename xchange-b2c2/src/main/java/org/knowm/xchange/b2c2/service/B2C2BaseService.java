package org.knowm.xchange.b2c2.service;

import org.knowm.xchange.b2c2.B2C2;
import org.knowm.xchange.b2c2.B2C2Exchange;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.RestProxyFactory;

public class B2C2BaseService extends BaseExchangeService<B2C2Exchange> implements BaseService {


    protected final B2C2 b2c2;
    final String authorizationHeader;
    // protected final String apiKey;

    /**
     * Constructor
     *
     * @param exchange
     */
    B2C2BaseService(B2C2Exchange exchange) {
        super(exchange);
        String apiKey = exchange.getExchangeSpecification().getApiKey();
        this.authorizationHeader = "Authorization: Token: " + apiKey;
        this.b2c2 =
                RestProxyFactory.createProxy(
                        B2C2.class,
                        exchange.getExchangeSpecification().getSslUri(),
                        getClientConfig());
    }

    protected ExchangeException handleError(B2C2Exception exception) {
        return null;
//        if (exception.getMessage().contains("You can only buy")) {
//            return new FundsExceededException(exception);
//
//        } else if (exception.getMessage().contains("Invalid limit exceeded")) {
//            return new RateLimitExceededException(exception);
//
//        } else if (exception.getMessage().contains("Invalid nonce")) {
//            return new NonceException(exception.getMessage());
//
//        } else if (exception.getMessage().contains("Internal server error")) {
//            return new InternalServerException(exception);
//
//        } else {
//            return new ExchangeException(exception);
//        }
    }
}
