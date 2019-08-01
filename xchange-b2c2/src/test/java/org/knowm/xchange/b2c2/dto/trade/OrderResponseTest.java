package org.knowm.xchange.b2c2.dto.trade;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderResponseTest {

    @Test
    public void testUnmarshal() throws IOException {
        InputStream is =
                OrderResponseTest.class.getResourceAsStream(
                        "/org/knowm/xchange/b2c2/dto/trade/OrderResponse.json");

        ObjectMapper mapper = new ObjectMapper();
        OrderResponse orderResponse = mapper.readValue(is, OrderResponse.class);

        assertThat(orderResponse.getOrderId()).isEqualTo("d4e41399-e7a1-4576-9b46-349420040e1a");
        assertThat(orderResponse.getClientOrderId()).isEqualTo("09f50d67-f5fd-497f-ac0d-3dfae9910383");
        assertThat(orderResponse.getQuantity()).isEqualTo(new BigDecimal("3.0000000000"));
        assertThat(orderResponse.getSide()).isEqualTo(OrderSide.BUY);
        assertThat(orderResponse.getInstrument()).isEqualTo("BTCUSD.SPOT");
        assertThat(orderResponse.getPrice()).isEqualTo(new BigDecimal("11000.00000000"));
        assertThat(orderResponse.getExecutedPrice()).isEqualTo(new BigDecimal("10457.651100000"));
        assertThat(orderResponse.getCreated()).isEqualTo(Date.from(Instant.parse("2018-02-06T16:07:50.122206Z")));
        assertThat(orderResponse.getTrades()).hasSize(1);

        OrderResponse.Trade trade = orderResponse.getTrades().get(0);
        assertThat(trade.getInstrument()).isEqualTo("BTCUSD.SPOT");
        assertThat(trade.getTradeId()).isEqualTo("b2c50b72-92d4-499f-b0a3-dee6b37378be");
        assertThat(trade.getRfqId()).isEqualTo("rfq1");
        assertThat(trade.getCreated()).isEqualTo(Date.from(Instant.parse("2018-02-26T14:27:53.675962Z")));
        assertThat(trade.getPrice()).isEqualTo(new BigDecimal("10457.65110000"));
        assertThat(trade.getQuantity()).isEqualTo(new BigDecimal("3.0000000000"));
        assertThat(trade.getOrder()).isEqualTo("d4e41399-e7a1-4576-9b46-349420040e1a");
        assertThat(trade.getSide()).isEqualTo(OrderSide.BUY);



    }
}
