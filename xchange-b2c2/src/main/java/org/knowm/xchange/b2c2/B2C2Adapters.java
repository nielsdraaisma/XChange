package org.knowm.xchange.b2c2; // package org.knowm.xchange.b2c2;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class B2C2Adapters {

    private static final Logger logger = LoggerFactory.getLogger(B2C2Adapters.class);

    private B2C2Adapters() {
    }

    private static Order.OrderStatus adaptOrderStatus(String wyreStatus) {
        switch (wyreStatus) {
            case "COMPLETED":
                return Order.OrderStatus.FILLED;
            case "CANCEL":
                return Order.OrderStatus.CANCELED;
            default:
                return Order.OrderStatus.NEW;
        }
    }

    private static FundingRecord.Status adaptFundingRecordStatus(String wyreStatus) {
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
        String mapKey = currencyPair.counter.getCurrencyCode() +
                currencyPair.base.getCurrencyCode();
        if (rates.containsKey(mapKey)) {
            return new
                    Ticker.Builder().currencyPair(currencyPair).last(rates.get(mapKey)).build();
        } else {
            return null;
        }
    }

    public static List<FundingRecord> adaptFundingRecords(TransferStatus ts) {
        List<FundingRecord> result = new ArrayList<>();
        FundingRecord fundingRecord = new FundingRecord(
                ts.getDest().replaceAll("bitcoin:", ""),
                Date.from(Instant.ofEpochSecond(ts.getCreatedAt())),
                Currency.getInstance(ts.getSourceCurrency()),
                ts.getSourceAmount(),
                ts.getId(),
                null,
                FundingRecord.Type.WITHDRAWAL,
                adaptFundingRecordStatus(ts.getStatus()),
                null,
                ts.getFeeEquivalencies().get(ts.getSourceCurrency()),
                ts.getDesc()
        );
        if (fundingRecord.getStatus() == null) {
            logger.warn("Unable to get FundingRecord status for Wyre transfer status {}",
                    ts.getStatus());
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

    public static Order adaptOrder(TransferStatus ts) {
        return new LimitOrder(
                Order.OrderType.BID,
                ts.getDestAmount(),
                ts.getDestAmount(),
                new CurrencyPair(ts.getSourceCurrency(), ts.getDestCurrency()),
                ts.getId(),
                Date.from(Instant.ofEpochSecond(ts.getCreatedAt())),
                ts.getExchangeRate()
        );


//                new CurrencyPair(ts.getSourceCurrency(), ts.getDestCurrency()),
//                ts.getId(),
//                Date.from(Instant.ofEpochSecond(ts.getCreatedAt())),
//                ts.getExchangeRate(),
//                ts.getExchangeRate(),
//                ts.getDestAmount(),
//                ts.getTotalFees(),
//                adaptOrderStatus(ts.getStatus())

    }
}
