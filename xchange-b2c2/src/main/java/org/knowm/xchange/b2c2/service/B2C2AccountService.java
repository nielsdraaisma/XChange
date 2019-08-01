package org.knowm.xchange.b2c2.service; // package org.knowm.xchange.b2c2.service;

import org.knowm.xchange.b2c2.B2C2Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.service.account.AccountService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class B2C2AccountService extends B2C2AccountServiceRaw implements AccountService {
    /**
     * Constructor
     *
     * @param exchange
     */
    protected B2C2AccountService(B2C2Exchange exchange) {
        super(exchange);
    }

    @Override
    public AccountInfo getAccountInfo() throws IOException {
        List<Wallet> wallets = new ArrayList<>();
        for (Map.Entry<String, String> entry : getAccountBalances().entrySet()) {
            wallets.add(
                    new Wallet(
                            new Balance(
                                    new Currency(entry.getKey()),
                                    new BigDecimal(
                                            entry.getValue())
                            )
                    )
            );
        }
        return new AccountInfo(wallets);
    }
}
