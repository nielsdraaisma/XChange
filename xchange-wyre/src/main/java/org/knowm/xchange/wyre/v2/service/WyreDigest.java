package org.knowm.xchange.wyre.v2.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;

public class WyreDigest implements ParamsDigest {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private String secretKey;

  WyreDigest(String secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {

    String url = restInvocation.getInvocationUrl();
    String reqData = restInvocation.getRequestBody() != null ? restInvocation.getRequestBody() : "";
    // Copy from https://docs.sendwyre.com/docs/authentication from here
    String data = url + reqData;
    //        logger.info("data : {}", data);

    try {
      Mac sha256Hmac = Mac.getInstance("HmacSHA256");
      SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
      sha256Hmac.init(key);

      byte[] macData = sha256Hmac.doFinal(data.getBytes("UTF-8"));

      String result = "";
      for (final byte element : macData) {
        result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
      }
      //            logger.info("Signature : {}", result);
      return result;

    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
}
