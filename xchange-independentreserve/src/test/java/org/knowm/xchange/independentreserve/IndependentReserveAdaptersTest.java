package org.knowm.xchange.independentreserve;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.independentreserve.dto.trade.IndependentReserveOpenOrder;
import org.knowm.xchange.independentreserve.dto.trade.IndependentReserveOpenOrdersResponse;
import org.knowm.xchange.independentreserve.dto.trade.IndependentReserveOrderDetailsResponse;

public class IndependentReserveAdaptersTest {

  @Test
  public void adaptOrderDetails() throws InvalidFormatException {
    IndependentReserveOrderDetailsResponse orderDetailsResponse =
        new IndependentReserveOrderDetailsResponse(
            "abcf-123",
            "2014-09-23T12:39:34.3817763Z",
            "MarketBid",
            new BigDecimal(5.0),
            new BigDecimal(4.0),
            new BigDecimal(100),
            new BigDecimal(95),
            new BigDecimal(0),
            "PartiallyFilled",
            "Xbt",
            "Usd");
    MarketOrder order =
        (MarketOrder) IndependentReserveAdapters.adaptOrderDetails(orderDetailsResponse);
    assertThat(order.getId()).isEqualTo("abcf-123");
    assertThat(order.getTimestamp())
        .isEqualTo(Date.from(ZonedDateTime.of(2014, 9, 23, 12, 39, 34, 0, UTC).toInstant()));
    assertThat(order.getType()).isEqualTo(Order.OrderType.BID);
    assertThat(order.getOriginalAmount()).isEqualByComparingTo(new BigDecimal(5));
    assertThat(order.getCumulativeAmount()).isEqualByComparingTo(new BigDecimal(4));
    assertThat(order.getAveragePrice()).isEqualByComparingTo(new BigDecimal(95));
    assertThat(order.getFee()).isNull();
    assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.PARTIALLY_FILLED);
    assertThat(order.getCurrencyPair()).isEqualTo(new CurrencyPair("Xbt", "Usd"));
  }

  @Test
  public void adaptOpenOrders() throws InvalidFormatException {
    IndependentReserveOpenOrdersResponse openOrdersResponse =
            new IndependentReserveOpenOrdersResponse(
                    25,1,1, Lists.newArrayList(
                    new IndependentReserveOpenOrder(
                            new BigDecimal("455.48000000"),
                            "2014-05-05T09:35:22.4032405Z",
                            new BigDecimal("0.005"),
                            "58f9da9d-a12e-4362-afa8-f5c252ba1725",
                            "LimitBid",
                            new BigDecimal("0.345"),
                            new BigDecimal("455.48000000"),
                            "Xbt",
                            "Usd",
                            "PartiallyFilled",
                            new BigDecimal("612.62060000"),
                            new BigDecimal("1.34500000")
                    )
            )
);
    OpenOrders orders = IndependentReserveAdapters.adaptOpenOrders(openOrdersResponse);
    LimitOrder order = orders.getOpenOrders().get(0);
    assertThat(orders.getAllOpenOrders().size()).isEqualTo(1);
    assertThat(order.getId()).isEqualTo("58f9da9d-a12e-4362-afa8-f5c252ba1725");
    assertThat(order.getTimestamp())
            .isEqualTo(Date.from(ZonedDateTime.of(2014, 5, 5, 9, 35, 22, 0, UTC).toInstant()));
    assertThat(order.getType()).isEqualTo(Order.OrderType.BID);
    assertThat(order.getOriginalAmount()).isEqualByComparingTo(new BigDecimal("1.345"));
    assertThat(order.getCumulativeAmount()).isEqualByComparingTo(new BigDecimal(1));
    assertThat(order.getAveragePrice()).isEqualByComparingTo(new BigDecimal("455.48000000"));
    assertThat(order.getFee()).isNull();
    assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.PARTIALLY_FILLED);
    assertThat(order.getCurrencyPair()).isEqualTo(new CurrencyPair("Xbt", "Usd"));
  }
}
