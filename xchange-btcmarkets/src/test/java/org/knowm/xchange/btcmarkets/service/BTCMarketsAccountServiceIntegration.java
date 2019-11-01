package org.knowm.xchange.btcmarkets.service;

import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.btcmarkets.ExchangeUtils;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class BTCMarketsAccountServiceIntegration {

    private Exchange exchange;

    public BTCMarketsAccountServiceIntegration() {
        exchange = ExchangeUtils.createExchangeFromProperties();
    }

    @Test
    public void testGetAccountInfo() throws IOException {
        AccountInfo accountInfo = exchange.getAccountService().getAccountInfo();
    }

    @Test
    public void testGetTradeHistory() throws IOException {
        BTCMarketsTradeService.HistoryParams tradeHistoryParams = (BTCMarketsTradeService.HistoryParams) exchange.getTradeService().createTradeHistoryParams();
        tradeHistoryParams.setStartId("0");
        tradeHistoryParams.setCurrencyPair(CurrencyPair.BTC_AUD);
        exchange.getTradeService().getTradeHistory(tradeHistoryParams);
    }

    @Test
    public void testGetDepositAddress() throws IOException {
        String address = exchange.getAccountService().requestDepositAddress(Currency.BTC);
        assertThat(address).isNotNull();
    }

}
