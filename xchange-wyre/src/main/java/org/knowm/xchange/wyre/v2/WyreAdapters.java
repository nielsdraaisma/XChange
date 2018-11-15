package org.knowm.xchange.wyre.v2;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.wyre.v2.dto.TransferHistoryResponse;
import org.knowm.xchange.wyre.v2.dto.TransferStatus;
import org.knowm.xchange.wyre.v2.dto.account.WyreAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WyreAdapters {

  private static final Logger logger = LoggerFactory.getLogger(WyreAdapters.class);

  private WyreAdapters() {}

  private static Order.OrderStatus adaptOrderStatus(FundingRecord.Status status) {
    switch (status) {
      case PROCESSING:
        return Order.OrderStatus.NEW;
      case FAILED:
        return Order.OrderStatus.EXPIRED;
      case COMPLETE:
        return Order.OrderStatus.FILLED;
      default:
        logger.warn(
            "Unable to adapt FundingRecord.Status {} to Order.OrderStatus, returning UNKNOWN",
            status);
        return Order.OrderStatus.UNKNOWN;
    }
  }

  private static FundingRecord.Status adaptStatusToFundingRecordStatus(String wyreStatus) {
    FundingRecord.Status result = FundingRecord.Status.resolveStatus(wyreStatus);
    if (result == null && wyreStatus.equals("UNCONFIRMED")) {
      result = FundingRecord.Status.PROCESSING;
    } else if (result == null && wyreStatus.equals("EXPIRED")) {
      result = FundingRecord.Status.FAILED;
    } else if (result == null) {
      logger.warn("Unable to map wyre transfer status : {}", wyreStatus);
    }

    return result;
  }

  public static AccountInfo adaptAccountInfo(WyreAccount wyreAccount) {
    List<Balance> balances = new ArrayList<>();
    for (Map.Entry<String, BigDecimal> e : wyreAccount.getTotalBalances().entrySet()) {
      balances.add(new Balance(Currency.getInstance(e.getKey()), e.getValue()));
    }
    Wallet wallet = new Wallet(balances);
    return new AccountInfo(wyreAccount.getId(), wallet);
  }

  public static Ticker adaptTicker(Map<String, BigDecimal> rates, CurrencyPair currencyPair) {
    String mapKey = currencyPair.counter.getCurrencyCode() + currencyPair.base.getCurrencyCode();
    if (rates.containsKey(mapKey)) {
      return new Ticker.Builder().currencyPair(currencyPair).last(rates.get(mapKey)).build();
    } else {
      return null;
    }
  }

  public static List<FundingRecord> adaptFundingRecords(TransferStatus ts) {
    List<FundingRecord> result = new ArrayList<>();
    FundingRecord fundingRecord =
        new FundingRecord(
            ts.getDest().replaceAll("bitcoin:", ""),
            Date.from(Instant.ofEpochSecond(ts.getCreatedAt())),
            Currency.getInstance(ts.getSourceCurrency()),
            ts.getSourceAmount(),
            ts.getId(),
            null,
            FundingRecord.Type.WITHDRAWAL,
            adaptStatusToFundingRecordStatus(ts.getStatus()),
            null,
            ts.getFeeEquivalencies().get(ts.getSourceCurrency()),
            ts.getDesc());
    if (fundingRecord.getStatus() == null) {
      logger.warn("Unable to get FundingRecord status for Wyre transfer status {}", ts.getStatus());
    }
    result.add(fundingRecord);
    return result;
  }

  public static List<FundingRecord> adaptFundingRecords(TransferHistoryResponse r) {
    List<FundingRecord> result = new ArrayList<>();
    for (TransferStatus ts : r.getData()) {
      result.addAll(adaptFundingRecords(ts));
    }
    return result;
  }

  public static Order adaptTransferStatus(TransferStatus transferStatus) {
    CurrencyPair pair =
        new CurrencyPair(transferStatus.getSourceCurrency(), transferStatus.getDestCurrency());
    return new LimitOrder(
        null,
        transferStatus.getSourceAmount(),
        pair,
        transferStatus.getId(),
        new Date(transferStatus.getCreatedAt()),
        transferStatus.getExchangeRate(),
        transferStatus.getExchangeRate(),
        transferStatus.getDestAmount(),
        transferStatus.getTotalFees(),
        adaptOrderStatus(adaptStatusToFundingRecordStatus(transferStatus.getStatus())));
  }
}
