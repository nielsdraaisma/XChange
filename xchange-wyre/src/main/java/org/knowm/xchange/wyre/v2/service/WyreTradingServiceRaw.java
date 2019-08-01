package org.knowm.xchange.wyre.v2.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.wyre.v2.dto.TransferRequest;
import org.knowm.xchange.wyre.v2.dto.TransferStatus;
import si.mazi.rescu.SynchronizedValueFactory;

public class WyreTradingServiceRaw extends WyreBaseService {
  private final SynchronizedValueFactory<Long> nonceFactory;

  WyreTradingServiceRaw(Exchange exchange) {

    super(exchange);
    this.nonceFactory = exchange.getNonceFactory();
  }

  TransferStatus transfer(TransferRequest request) {
    return wyre.transfer("2", apiKey, digest, nonceFactory.createValue(), request);
  }

  TransferStatus getWyreTransferStatus(String transactionId) {
    return wyre.getTransferStatus("2", apiKey, digest, nonceFactory.createValue(), transactionId);
  }
}
