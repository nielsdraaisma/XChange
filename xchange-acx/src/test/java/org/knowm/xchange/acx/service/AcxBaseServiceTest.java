package org.knowm.xchange.acx.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.knowm.xchange.acx.dto.AcxException;
import org.knowm.xchange.exceptions.FundsExceededException;

public class AcxBaseServiceTest {
  @Test
  public void testHandleError() {
    AcxException acxException = new AcxException(new AcxException.Error(2002L, "Out of funds"));
    assertThat(AcxBaseService.handleError(acxException)).isInstanceOf(FundsExceededException.class);
  }
}
