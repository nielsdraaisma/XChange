package org.knowm.xchange.coinspot.service;

import javax.crypto.Mac;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.utils.DigestUtils;
import si.mazi.rescu.RestInvocation;

public class CoinspotDigest extends BaseParamsDigest {

  public CoinspotDigest(String secretKeyBase64) throws IllegalArgumentException {
    super(secretKeyBase64, HMAC_SHA_512);
  }

  @Override
  public String digestParams(RestInvocation inv) {
    String requestBody = inv.getRequestBody();
    Mac mac = getMac();
    if (requestBody != null) {
      mac.update(requestBody.getBytes());
    }
    return DigestUtils.bytesToHex(mac.doFinal());
  }
}
