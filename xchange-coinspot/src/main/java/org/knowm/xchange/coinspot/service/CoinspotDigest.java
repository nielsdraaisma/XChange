package org.knowm.xchange.coinspot.service;

import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.utils.DigestUtils;
import org.knowm.xchange.utils.HmacDigest;
import si.mazi.rescu.RestInvocation;

import javax.crypto.Mac;

public class CoinspotDigest extends BaseParamsDigest {

    public CoinspotDigest(String secretKeyBase64) throws IllegalArgumentException {
        super(secretKeyBase64, HMAC_SHA_512);
    }

    @Override
    public String digestParams(RestInvocation inv) {
        String requestBody = inv.getRequestBody();
        Mac mac = getMac();
        mac.update(requestBody.getBytes());
        return DigestUtils.bytesToHex(mac.doFinal());
    }
}
