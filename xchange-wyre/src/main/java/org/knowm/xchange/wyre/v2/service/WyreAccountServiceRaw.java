package org.knowm.xchange.wyre.v2.service;

import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.wyre.v2.dto.TransferHistoryResponse;
import org.knowm.xchange.wyre.v2.dto.TransferRequest;
import org.knowm.xchange.wyre.v2.dto.TransferStatus;
import org.knowm.xchange.wyre.v2.dto.account.*;
import si.mazi.rescu.SynchronizedValueFactory;

class WyreAccountServiceRaw extends WyreBaseService {

  private final SynchronizedValueFactory<Long> nonceFactory;

  WyreAccountServiceRaw(Exchange exchange) {
    super(exchange);
    this.nonceFactory = exchange.getNonceFactory();
  }

  WyreAccount getWyreccountInfo() throws WyreException, IOException {
    return wyre.getAccount("2", apiKey, digest, nonceFactory.createValue());
  }

  TransferStatus getWyreTransferStatus(String transactionId) {
    return wyre.getTransferStatus("2", apiKey, digest, nonceFactory.createValue(), transactionId);
  }

  TransferHistoryResponse getWyreTransferHistory(String walletSrn) {
    return wyre.getTransferHistory("2", apiKey, digest, nonceFactory.createValue(), walletSrn);
  }

  GetWalletsResponse listWallets() {
    return wyre.listWallets("2", apiKey, digest, nonceFactory.createValue());
  }

  TransferStatus transfer(TransferRequest request) {
    return wyre.transfer("2", apiKey, digest, nonceFactory.createValue(), request);
  }
}
