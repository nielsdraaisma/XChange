package org.knowm.xchange.coinspot;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.*;
import org.knowm.xchange.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinspotOrderbookIntegration {

  private static final Logger LOG = LoggerFactory.getLogger(CoinspotOrderbookIntegration.class);

  private Exchange exchange;
  private MarketDataService marketDataService;
  private TradeService tradeService;
  private AccountService accountService;

  @Before
  public void before() {
    exchange = ExchangeFactory.INSTANCE.createExchange(CoinspotExchange.class.getName());
    AuthUtils.setApiAndSecretKey(exchange.getExchangeSpecification());
    exchange = ExchangeFactory.INSTANCE.createExchange(exchange.getExchangeSpecification());
    marketDataService = exchange.getMarketDataService();
    tradeService = exchange.getTradeService();
    accountService = exchange.getAccountService();
  }

  @Test
  public void testGetOrderbook() throws Exception {
    OrderBook orderBook = marketDataService.getOrderBook(CurrencyPair.BTC_AUD);
    assertThat(orderBook.getAsks()).isNotEmpty();
    assertThat(orderBook.getBids()).isNotEmpty();
  }

  @Test
  public void testGetMyOrders() throws Exception {
    OpenOrders openOrders = tradeService.getOpenOrders();
    LOG.info("OpenOrders : {}", openOrders);
  }

  @Test
  public void testGetTradeHistoryWithNoParams() throws Exception {
    UserTrades userTrades = tradeService.getTradeHistory(null);
    LOG.info("UserTrades : {}", userTrades);
  }

  @Test
  public void testGetAccountInfo() throws Exception {
    AccountInfo accountInfo = accountService.getAccountInfo();
    LOG.info("accountInfo : {}", accountInfo);
  }

  @Test
  public void testGetTradeHistoryWithCurrencyPair() throws Exception {
    TradeHistoryParams params = tradeService.createTradeHistoryParams();
    if (params instanceof TradeHistoryParamCurrencyPair) {
      ((TradeHistoryParamCurrencyPair) params)
          .setCurrencyPair(new CurrencyPair(Currency.BTC, Currency.AUD));
    }
    UserTrades userTrades = tradeService.getTradeHistory(params);
    LOG.info("UserTrades : {}", userTrades);
  }

  @Test
  public void testPlaceBuyOrder() throws Exception {
    LimitOrder order =
        new LimitOrder.Builder(Order.OrderType.BID, CurrencyPair.BTC_AUD)
            .limitPrice(new BigDecimal("15000"))
            .originalAmount(new BigDecimal("0.0001"))
            .build();
    String orderId = tradeService.placeLimitOrder(order);
    LOG.info("Placed order {}", orderId);
    CancelOrderParams params = tradeService.createCancelOrderParams();
    if (params instanceof CancelOrderByIdParams) {
      ((CancelOrderByIdParams) params).setOrderId(orderId);
    }
    if (params instanceof CancelOrderByOrderTypeParams) {
      ((CancelOrderByOrderTypeParams) params).setOrderType(Order.OrderType.BID);
    }
    Boolean result = tradeService.cancelOrder(params);
    LOG.info("Cancel result : {}", result);
  }
}
