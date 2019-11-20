package org.knowm.xchange.wyre.v2.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;
import org.knowm.xchange.wyre.v2.WyreAdapters;
import org.knowm.xchange.wyre.v2.dto.TransferRequest;
import org.knowm.xchange.wyre.v2.dto.TransferStatus;
import org.knowm.xchange.wyre.v2.dto.WyreTradeHistoryParams;
import org.knowm.xchange.wyre.v2.dto.account.GetWalletsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WyreAccountService extends WyreAccountServiceRaw implements AccountService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Constructor
   *
   * @param exchange
   */
  public WyreAccountService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    return WyreAdapters.adaptAccountInfo(super.getWyreccountInfo());
  }

  @Override
  public TradeHistoryParams createFundingHistoryParams() {
    return new WyreTradeHistoryParams();
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    WyreTradeHistoryParams wyreParams = (WyreTradeHistoryParams) params;
    if (wyreParams.getTransactionId() != null) {
      // If a specific id is given, use the transferStatus api
      return WyreAdapters.adaptFundingRecords(
          super.getWyreTransferStatus(wyreParams.getTransactionId()));
    }
    // If no id present, use the history api
    List<FundingRecord> result = new ArrayList<>();
    for (GetWalletsResponse.Entry entry : super.listWallets().getData()) {
      result.addAll(WyreAdapters.adaptFundingRecords(super.getWyreTransferHistory(entry.getSrn())));
    }
    return result;
  }

  @Override
  public String withdrawFunds(Currency currency, BigDecimal amount, String address)
      throws IOException {
    String addressPrefix;
    if (currency.equals(Currency.BTC)) {
      addressPrefix = "bitcoin";
    } else if (currency.equals(Currency.ETH)) {
      addressPrefix = "ethereum";
    } else {
      throw new IllegalArgumentException(
          "Unable to determine adddress prefix for currency " + currency.getCurrencyCode());
    }
    TransferRequest transferRequest =
        new TransferRequest(
            "account:" + this.accountId,
            currency.getCurrencyCode(),
            null,
            addressPrefix + ":" + address,
            amount.toPlainString(),
            currency.getCurrencyCode());
    TransferStatus result = super.transfer(transferRequest);
    return result.getId();
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    String address;
    Currency currency;
    BigDecimal amount;
    if (params instanceof DefaultWithdrawFundsParams) {
      address = ((DefaultWithdrawFundsParams) params).address;
      currency = ((DefaultWithdrawFundsParams) params).currency;
      amount = ((DefaultWithdrawFundsParams) params).amount;
      return withdrawFunds(currency, amount, address);
    } else {
      logger.warn("Unable to withdrawFunds using params {}", params);
      return null;
    }
  }
}
