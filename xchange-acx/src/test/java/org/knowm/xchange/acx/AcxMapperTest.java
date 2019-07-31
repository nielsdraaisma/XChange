package org.knowm.xchange.acx;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import org.junit.Test;
import org.knowm.xchange.acx.dto.marketdata.AcxOrder;
import org.knowm.xchange.dto.Order;

public class AcxMapperTest {

  private AcxMapper mapper = new AcxMapper();

  @Test
  public void shouldReturnPartiallyCancelled() {
    AcxOrder acxOrder =
        new AcxOrder(
            "1",
            "buy",
            "limit",
            BigDecimal.ONE,
            BigDecimal.TEN,
            "cancel",
            "btcaud",
            new Date(),
            BigDecimal.ONE,
            new BigDecimal("0.5"),
            new BigDecimal("0.5"),
            1);
    Order.OrderStatus status = mapper.mapOrderStatus(acxOrder);
    assertThat(status).isEqualTo(Order.OrderStatus.PARTIALLY_CANCELED);
  }

  @Test
  public void shouldReturnCancelled() {
    AcxOrder acxOrder =
        new AcxOrder(
            "1",
            "buy",
            "limit",
            BigDecimal.ONE,
            BigDecimal.TEN,
            "cancel",
            "btcaud",
            new Date(),
            BigDecimal.ONE,
            BigDecimal.ONE,
            BigDecimal.ZERO,
            0);
    Order.OrderStatus status = mapper.mapOrderStatus(acxOrder);
    assertThat(status).isEqualTo(Order.OrderStatus.CANCELED);
  }
}
