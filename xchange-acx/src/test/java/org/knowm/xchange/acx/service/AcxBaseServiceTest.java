package org.knowm.xchange.acx.service;

import org.junit.Test;
import org.knowm.xchange.acx.dto.AcxException;
import org.knowm.xchange.exceptions.FundsExceededException;

import static org.assertj.core.api.Assertions.assertThat;

public class AcxBaseServiceTest {
    @Test
    public void testHandleError() {
        AcxException acxException = new AcxException(new AcxException.Error(2002L, "Out of funds"));
        assertThat(AcxBaseService.handleError(acxException)).isInstanceOf(FundsExceededException.class);
    }
}