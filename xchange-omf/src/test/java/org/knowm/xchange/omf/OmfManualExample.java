package org.knowm.xchange.omf;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.omf.dto.SignOnResponse;
import org.knowm.xchange.omf.service.OMFTradeService;
import org.knowm.xchange.utils.AuthUtils;

public class OmfManualExample {

  private OMFTradeService tradeService;

  @Before
  public void before() {
    Exchange exchange = ExchangeFactory.INSTANCE.createExchange(OMFExchange.class.getName());
    AuthUtils.setUsernameAndPassword(exchange.getExchangeSpecification());
    exchange = ExchangeFactory.INSTANCE.createExchange(exchange.getExchangeSpecification());
    tradeService = (OMFTradeService) exchange.getTradeService();
  }

  @Test
  public void getSignOn() throws Exception {
    SignOnResponse result = tradeService.signIn();
    assertThat(result.customers.size()).isGreaterThan(0);
  }

  @Test
  public void getSpotBuy11AudWithAudVolume() throws Exception {
    tradeService.signIn();
    MarketOrder marketOrder =
        new MarketOrder.Builder(Order.OrderType.BID, new CurrencyPair(Currency.AUD, Currency.USD))
            .flag(OMFOrderFlags.SKIP_ACCEPT_SPOT_REQUEST)
            .userReference("11Aud to USD")
            .originalAmount(new BigDecimal(11))
            .build();
    tradeService.placeMarketOrder(marketOrder);
  }

  @Test
  public void getSpotBuy12UsdWithUsdVolume() throws Exception {
    tradeService.signIn();
    MarketOrder marketOrder =
        new MarketOrder.Builder(Order.OrderType.BID, new CurrencyPair(Currency.AUD, Currency.USD))
            .flag(OMFOrderFlags.SKIP_ACCEPT_SPOT_REQUEST)
            .flag(OMFOrderFlags.VOLUME_IN_COUNTER_CURRENCY)
            .userReference("Aud to 12 USD")
            .originalAmount(new BigDecimal(12))
            .build();
    tradeService.placeMarketOrder(marketOrder);
  }

  @Test
  public void searchOrder() throws Exception {
    tradeService.signIn();
    Collection<Order> orders = tradeService.getOrder("12345");
    assertThat(orders.size()).isEqualTo(1);
  }
}
