package org.knowm.xchange.wyre.v2.dto.account;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import org.knowm.xchange.wyre.v2.dto.WyreTest;

public class WyreAccountTest extends WyreTest {
  @Test
  public void testDeserialization() throws IOException {
    InputStream is =
        WyreAccountTest.class.getResourceAsStream(
            "/org/knowm/xchange/wyre/v2/dto/account/wyre-account.json");

    WyreAccount wyreAccount = mapper.readValue(is, WyreAccount.class);

    assertThat(wyreAccount.getId()).isEqualTo("121pd02kt0rnb24nclsg4bglanimurqp");
  }
}
