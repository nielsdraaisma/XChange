package org.knowm.xchange.acx.service.account;

import java.io.IOException;
import org.knowm.xchange.acx.AcxApi;
import org.knowm.xchange.acx.AcxMapper;
import org.knowm.xchange.acx.AcxSignatureCreator;
import org.knowm.xchange.acx.dto.account.AcxAccountInfo;
import org.knowm.xchange.acx.service.AcxBaseService;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import si.mazi.rescu.SynchronizedValueFactory;

public class AcxAccountService extends AcxBaseService implements AccountService {

  public AcxAccountService(
      SynchronizedValueFactory<Long> nonceFactory,
      AcxApi api,
      AcxMapper mapper,
      AcxSignatureCreator signatureCreator,
      String accessKey) {
    super(nonceFactory, api, mapper, signatureCreator, accessKey);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    long tonce = nonceFactory.createValue();
    AcxAccountInfo accountInfo = api.getAccountInfo(accessKey, tonce, signatureCreator);
    return mapper.mapAccountInfo(accountInfo);
  }

  @Override
  public TradeHistoryParams createFundingHistoryParams() {
    throw new NotAvailableFromExchangeException();
  }
}
