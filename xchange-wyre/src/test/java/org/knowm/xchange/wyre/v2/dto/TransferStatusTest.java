package org.knowm.xchange.wyre.v2.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class TransferStatusTest extends WyreTest {
  @Test
  public void testDeserialization() throws IOException {
    InputStream is =
        TransferStatusTest.class.getResourceAsStream(
            "/org/knowm/xchange/wyre/v2/dto/account/transfer-status.json");

    TransferStatus transferStatus = mapper.readValue(is, TransferStatus.class);

    assertThat(transferStatus.getId()).isEqualTo("TF-4F3HRUYPNFY");
    assertThat(transferStatus.getCreatedAt()).isEqualTo(1541552388000L);
  }
}
