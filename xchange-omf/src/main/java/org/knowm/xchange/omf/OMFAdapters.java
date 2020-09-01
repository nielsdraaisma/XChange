package org.knowm.xchange.omf;

import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.*;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.MarketOrder;

public class OMFAdapters {

  public static List<Order> adaptSearchResult(String payload) {
    String[] parts = payload.split("\\|");
    String[] schemaParts = parts[0].split("~");
    Map<String, Integer> indexes = Maps.newHashMap();
    String[] dataPart = parts[1].split("~");
    Map<Integer, String> dataMap = Maps.newHashMap();

    // Parse schema
    for (int i = 1; i < schemaParts.length - 1; i = i + 3) {
      String fieldName = schemaParts[i];
      indexes.put(fieldName, (i / 3) + 1);
    }
    // Parse data
    for (int i = 0; i < dataPart.length; i = i + 2) {
      String value = dataPart[i];
      Integer index = (i / 2); // dataPart[i] ;
      dataMap.put(index, value);
    }
    String orderId = dataMap.get(indexes.get("TRADENUMBER"));
    String symbol = dataMap.get(indexes.get("SYMBOL"));
    String direction = dataMap.get(indexes.get("CUSTOMERDIRECTION"));
    String tradePrice = dataMap.get(indexes.get("TRADEPRICE"));
    String txDateTime = dataMap.get(indexes.get("TRANSACTIONDATETIME"));
    String originalAmount = dataMap.get(indexes.get("TRADECCY1AMOUNT"));
    Date txDate = null;
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
      simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
      txDate = simpleDateFormat.parse(txDateTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Order.OrderType orderType;
    if ("BUY".equals(direction)) {
      orderType = Order.OrderType.BID;
    } else {
      orderType = Order.OrderType.ASK;
    }

    Order order =
        new MarketOrder.Builder(orderType, new CurrencyPair(symbol))
            .id(orderId)
            .timestamp(txDate)
            .averagePrice(new BigDecimal(tradePrice))
            .originalAmount(new BigDecimal(originalAmount))
            .orderStatus(Order.OrderStatus.FILLED)
            .fee(BigDecimal.ZERO)
            .build();
    return Collections.singletonList(order);
  }
}
