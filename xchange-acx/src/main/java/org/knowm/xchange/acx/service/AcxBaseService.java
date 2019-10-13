package org.knowm.xchange.acx.service;

import org.knowm.xchange.acx.AcxApi;
import org.knowm.xchange.acx.AcxMapper;
import org.knowm.xchange.acx.AcxSignatureCreator;
import org.knowm.xchange.acx.dto.AcxException;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.FundsExceededException;
import si.mazi.rescu.SynchronizedValueFactory;

public class AcxBaseService {

  protected final AcxApi api;
  protected final AcxMapper mapper;
  protected final AcxSignatureCreator signatureCreator;
  protected final String accessKey;
  protected final SynchronizedValueFactory<Long> nonceFactory;

  public AcxBaseService(
      SynchronizedValueFactory<Long> nonceFactory,
      AcxApi api,
      AcxMapper mapper,
      AcxSignatureCreator signatureCreator,
      String accessKey) {
    this.api = api;
    this.mapper = mapper;
    this.signatureCreator = signatureCreator;
    this.accessKey = accessKey;
    this.nonceFactory = nonceFactory;
  }

  protected ExchangeException handleError(AcxException exception) {

    if (exception.error.code == 2002L) {
      return new FundsExceededException(exception.error.message);
    } else {
      return new ExchangeException(exception);
    }
  }
}
