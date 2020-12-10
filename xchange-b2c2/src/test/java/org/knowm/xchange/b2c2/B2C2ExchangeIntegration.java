package org.knowm.xchange.b2c2;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.b2c2.service.B2C2TradingService;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class B2C2ExchangeIntegration {

  private Exchange exchange;

  private Logger logger = LoggerFactory.getLogger(B2C2ExchangeIntegration.class);

  @Before
  public void setUp() throws IOException {

    ExchangeSpecification defaultExchangeSpecification =
        new ExchangeSpecification(B2C2Exchange.class);
    defaultExchangeSpecification.setSslUri("https://api.uat.b2c2.net");

    AuthUtils.setApiAndSecretKey(defaultExchangeSpecification);

    exchange = ExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
  }

  @Test
  public void testGetAccountInfo() throws IOException {
    AccountInfo accountInfo = exchange.getAccountService().getAccountInfo();
    assertThat(accountInfo.getWallets().get("USD")).isNotNull();
    assertThat(accountInfo.getWallets().get("USD").getBalance(Currency.USD).getAvailable())
        .isGreaterThan(BigDecimal.ONE);
  }

  @Test
  public void testCreateAndRetrieveLimitOrder() throws IOException {
    if (!exchange.getExchangeSpecification().getSslUri().contains("uat")) {
      return;
    }
    LimitOrder limitOrder =
        new LimitOrder.Builder(Order.OrderType.BID, CurrencyPair.BTC_USD)
            .limitPrice(new BigDecimal("30000"))
            .originalAmount(new BigDecimal("0.1"))
            .userReference("test123")
            .build();
    String orderId = exchange.getTradeService().placeLimitOrder(limitOrder);

    Collection<Order> retrieved = exchange.getTradeService().getOrder(orderId);
    assertThat(retrieved).hasSize(1);
    Order retrievedOrder = retrieved.iterator().next();
    assertThat(retrievedOrder.getId()).isEqualTo(orderId);
    assertThat(retrievedOrder).isInstanceOf(LimitOrder.class);
    LimitOrder retrievedLimitOrder = (LimitOrder) retrievedOrder;
    assertThat(retrievedLimitOrder.getOriginalAmount()).isEqualByComparingTo(new BigDecimal("0.1"));
    assertThat(retrievedLimitOrder.getAveragePrice()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    assertThat(retrievedLimitOrder.getLimitPrice()).isEqualByComparingTo(new BigDecimal("30000"));
    assertThat(retrievedLimitOrder.getUserReference()).isEqualTo("test123");
  }

  @Test
  public void testCreateAndRetrieveMarketOrder() throws IOException {
    if (!exchange.getExchangeSpecification().getSslUri().contains("uat")) {
      return;
    }
    MarketOrder marketOrder =
        new MarketOrder.Builder(Order.OrderType.BID, CurrencyPair.BTC_USD)
            .originalAmount(new BigDecimal("0.1"))
            .userReference("test123")
            .build();
    String orderId = exchange.getTradeService().placeMarketOrder(marketOrder);

    Collection<Order> retrieved = exchange.getTradeService().getOrder(orderId);
    assertThat(retrieved).hasSize(1);
    Order retrievedOrder = retrieved.iterator().next();
    assertThat(retrievedOrder.getId()).isEqualTo(orderId);
    assertThat(retrievedOrder).isInstanceOf(LimitOrder.class);
    LimitOrder retrievedLimitOrder = (LimitOrder) retrievedOrder;
    assertThat(retrievedLimitOrder.getOriginalAmount()).isEqualByComparingTo(new BigDecimal("0.1"));
    assertThat(retrievedLimitOrder.getAveragePrice()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    assertThat(retrievedLimitOrder.getUserReference()).isEqualTo("test123");
  }

  @Test
  public void testListTrades() throws IOException {
    B2C2TradingService.B2C2TradeHistoryParams historyParams =
        (B2C2TradingService.B2C2TradeHistoryParams)
            exchange.getTradeService().createTradeHistoryParams();
    UserTrades userTrades;
    //    UserTrades userTrades = exchange.getTradeService().getTradeHistory(historyParams);
    //    logger.info("Got userTrades {}", userTrades);
    //
    //    historyParams.setStartTime(Date.from(ZonedDateTime.now().minusYears(10).toInstant()));
    //    userTrades = exchange.getTradeService().getTradeHistory(historyParams);
    //    logger.info("Got userTrades {}", userTrades);

    historyParams =
        (B2C2TradingService.B2C2TradeHistoryParams)
            exchange.getTradeService().createTradeHistoryParams();
    historyParams.setStartTime(Date.from(ZonedDateTime.now().minusYears(10).toInstant()));
    historyParams.setLimit(5);
    userTrades = exchange.getTradeService().getTradeHistory(historyParams);
    logger.info("Got userTrades {}", userTrades);

    historyParams.setOffset(5L);
    userTrades = exchange.getTradeService().getTradeHistory(historyParams);
    logger.info("Got userTrades {}", userTrades);
  }

  @Test
  public void testGetTicker() throws IOException {
    Ticker ticker = exchange.getMarketDataService().getTicker(CurrencyPair.BTC_USD);
    assertThat(ticker.getBid()).isNotNull();
    assertThat(ticker.getAsk()).isNotNull();
    assertThat(ticker.getAsk()).isGreaterThan(ticker.getBid());
  }

  @Test
  public void testGetOrderbook() throws IOException {
    OrderBook orderBook = exchange.getMarketDataService().getOrderBook(CurrencyPair.BTC_AUD);
    assertThat(orderBook.getBids().get(0).getLimitPrice())
        .isLessThan(orderBook.getAsks().get(0).getLimitPrice());
  }

  @Test
  public void testGetTradeHistory() throws IOException {
    B2C2TradingService.B2C2TradeHistoryParams tradeHistoryParams =
        (B2C2TradingService.B2C2TradeHistoryParams)
            exchange.getTradeService().createTradeHistoryParams();
    tradeHistoryParams.setOffset(0L);
    tradeHistoryParams.setLimit(10);
    UserTrades userTrades1 = exchange.getTradeService().getTradeHistory(tradeHistoryParams);
    assertThat(userTrades1.getUserTrades().size()).isEqualTo(10);

    tradeHistoryParams.setOffset(10L);
    tradeHistoryParams.setLimit(10);
    UserTrades userTrades2 = exchange.getTradeService().getTradeHistory(tradeHistoryParams);
    assertThat(userTrades2.getUserTrades().size()).isEqualTo(10);

    tradeHistoryParams.setOffset(0L);
    tradeHistoryParams.setLimit(20);
    UserTrades userTrades3 = exchange.getTradeService().getTradeHistory(tradeHistoryParams);
    assertThat(userTrades3.getUserTrades().size()).isEqualTo(20);
  }
}
