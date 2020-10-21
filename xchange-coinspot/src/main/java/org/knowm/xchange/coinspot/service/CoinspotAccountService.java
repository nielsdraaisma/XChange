package org.knowm.xchange.coinspot.service;

import java.io.IOException;
import org.knowm.xchange.coinspot.CoinspotAdapters;
import org.knowm.xchange.coinspot.CoinspotExchange;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.*;

public class CoinspotAccountService extends CoinspotAccountServiceRaw implements AccountService {

  public CoinspotAccountService(CoinspotExchange exchange) {
    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    return CoinspotAdapters.adaptBalances(super.getBalances());
  }
}
