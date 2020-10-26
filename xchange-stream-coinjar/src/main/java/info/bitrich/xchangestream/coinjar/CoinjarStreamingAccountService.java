package info.bitrich.xchangestream.coinjar;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.coinjar.dto.CoinjarWebSocketBalanceEvent;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import java.io.IOException;
import java.util.stream.Collectors;
import org.knowm.xchange.coinjar.CoinjarExchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CoinjarStreamingAccountService implements StreamingAccountService {

  private static final Logger logger =
      LoggerFactory.getLogger(CoinjarStreamingAccountService.class);

  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  private final CoinjarStreamingService service;
  private final AccountService accountService;

  public CoinjarStreamingAccountService(CoinjarExchange exchange, CoinjarStreamingService service) {
    this.service = service;
    this.accountService = exchange.getAccountService();
  }

  private final String balanceChannel = "private";

  @Override
  public Observable<Balance> getBalanceChanges(Currency currency, Object... args) {
    Observable<Balance> initial = Observable.empty();
    try {
      AccountInfo accountInfo = this.accountService.getAccountInfo();
      initial =
          Observable.fromIterable(
              accountInfo.getWallets().values().stream()
                  .flatMap(wallet -> wallet.getBalances().values().stream())
                  .collect(Collectors.toList()));
    } catch (IOException e) {
      logger.warn("Failed to get initial balances", e);
    }
    return initial.concatWith(
        service
            .subscribeChannel(balanceChannel)
            .doOnError(
                throwable -> {
                  logger.warn(
                      "encoutered error while subscribing to channel " + balanceChannel, throwable);
                })
            .filter(
                node -> node.has("event") && node.get("event").asText().equals("private:account"))
            .map(node -> mapper.treeToValue(node, CoinjarWebSocketBalanceEvent.class))
            .map(CoinjarStreamingAdapters::adaptBalance)
            .filter(balance -> currency == null || currency.equals(balance.getCurrency())));
  }
}
