package org.knowm.xchange.btcmarkets.service;

import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.RestInvocation;

import javax.crypto.Mac;
import javax.ws.rs.HeaderParam;
import java.util.Base64;

public class BTCMarketsDigestV3 extends BaseParamsDigest {

  public BTCMarketsDigestV3(String secretKey) {
    super(decodeBase64(secretKey), HMAC_SHA_512);
  }

  @Override
  public String digestParams(RestInvocation inv) {
    final String timestamp = inv.getParamValue(HeaderParam.class, "BM-AUTH-TIMESTAMP").toString();
    final String method = inv.getHttpMethod();
    final String path = inv.getPath();
    final String requestBody = inv.getRequestBody();

    final String stringToSign = method + path + timestamp + requestBody;

    Mac mac = getMac();
    mac.update(stringToSign.getBytes());
    return Base64.getEncoder().encodeToString(mac.doFinal());
  }
}
