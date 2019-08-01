package org.knowm.xchange.wyre.v2.dto.account;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import org.knowm.xchange.wyre.v2.dto.WyreTest;

public class GetWalletsResponseTest extends WyreTest {

  @Test
  public void testDeserialization() throws IOException {
    InputStream is =
        GetWalletsResponseTest.class.getResourceAsStream(
            "/org/knowm/xchange/wyre/v2/dto/account/get-wallet-response.json");

    GetWalletsResponse getWalletsResponse = mapper.readValue(is, GetWalletsResponse.class);

    assertThat(getWalletsResponse.getData().size()).isEqualTo(2);
    assertThat(getWalletsResponse.getData().get(0).getSrn()).isEqualTo("wallet:WA-XM4L3JMUQGF");
    assertThat(getWalletsResponse.getData().get(0).getDepositAddresses().size()).isEqualTo(1);
    assertThat(getWalletsResponse.getData().get(0).getDepositAddresses().get("BTC"))
        .isEqualTo("1Q9TqsVwuZf6bYqtxxjqdataXx81x3Q1h7");

    assertThat(getWalletsResponse.getData().get(1).getSrn()).isEqualTo("wallet:WA-VXRYUHW6JPX");
    assertThat(getWalletsResponse.getData().get(1).getDepositAddresses().size()).isEqualTo(1);
    assertThat(getWalletsResponse.getData().get(1).getDepositAddresses().get("BTC"))
        .isEqualTo("13UtPuSaHCTsqRkwYjMbypSnPGkcyrni3r");
  }
}
