package info.bitrich.xchangestream.btcmarkets;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.Lists;
import info.bitrich.xchangestream.btcmarkets.dto.BTCMarketsWebSocketOrderbookMessage;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;

public class BTCMarketsStreamingAdaptersTest extends TestCase {

  @Test
  public void testAdaptCurrencyPairToMarket() {
    String market = BTCMarketsStreamingAdapters.adaptCurrencyPairToMarketId(CurrencyPair.BTC_AUD);
    assertThat(market).isEqualTo("BTC-AUD");
  }

  @Test
  public void testAdaptOrderbookMessageToOrderbook() throws InvalidFormatException {
    BTCMarketsWebSocketOrderbookMessage message =
        new BTCMarketsWebSocketOrderbookMessage(
            "BTC-AUD",
            "2019-04-08T22:23:37.643Z",
            Lists.newArrayList(
                Lists.newArrayList(new BigDecimal("7418.46"), new BigDecimal("0.04")),
                Lists.newArrayList(new BigDecimal("7418.45"), new BigDecimal("0.56")),
                Lists.newArrayList(new BigDecimal("7100"), new BigDecimal("0.01"))),
            Lists.newArrayList(
                Lists.newArrayList(new BigDecimal("7437.53"), new BigDecimal("0.76")),
                Lists.newArrayList(new BigDecimal("7437.54"), new BigDecimal("0.3646349")),
                Lists.newArrayList(new BigDecimal("7446.94"), new BigDecimal("0.6")),
                Lists.newArrayList(new BigDecimal("7700"), new BigDecimal("0.1"))),
            "orderbook");
    OrderBook orderBook = BTCMarketsStreamingAdapters.adaptOrderbookMessageToOrderbook(message);
  }
}
