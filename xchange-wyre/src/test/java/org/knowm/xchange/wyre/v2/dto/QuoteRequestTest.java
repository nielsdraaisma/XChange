package org.knowm.xchange.wyre.v2.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

public class QuoteRequestTest extends WyreTest {

  @Test
  public void testSerialization() throws JsonProcessingException {
    QuoteRequest quoteRequest = new QuoteRequest("USD", "1", "BTC");

    String js = mapper.writeValueAsString(quoteRequest);
    assertThat(js)
        .isEqualTo("{\"sourceCurrency\":\"USD\",\"sourceAmount\":\"1\",\"destCurrency\":\"BTC\"}");
  }
}
