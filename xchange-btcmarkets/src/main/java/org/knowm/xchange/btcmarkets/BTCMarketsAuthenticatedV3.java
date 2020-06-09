package org.knowm.xchange.btcmarkets;

import java.io.IOException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.knowm.xchange.btcmarkets.dto.BTCMarketsException;
import org.knowm.xchange.btcmarkets.dto.v3.account.BTCMarketsAddressesResponse;
import org.knowm.xchange.btcmarkets.dto.v3.trade.BTCMarketsPlaceOrderRequest;
import org.knowm.xchange.btcmarkets.dto.v3.trade.BTCMarketsPlaceOrderResponse;
import org.knowm.xchange.btcmarkets.service.BTCMarketsDigestV3;
import si.mazi.rescu.SynchronizedValueFactory;

@Path("/v3/")
@Produces(MediaType.APPLICATION_JSON)
public interface BTCMarketsAuthenticatedV3 {

  @POST
  @Path("order/create")
  @Consumes(MediaType.APPLICATION_JSON)
  BTCMarketsPlaceOrderResponse placeOrder(
      @HeaderParam("BM-AUTH-APIKEY") String publicKey,
      @HeaderParam("BM-AUTH-TIMESTAMP") SynchronizedValueFactory<Long> nonceFactory,
      @HeaderParam("BM-AUTH-SIGNATURE") BTCMarketsDigestV3 signer,
      BTCMarketsPlaceOrderRequest order)
      throws BTCMarketsException, IOException;

  @GET
  @Path("addresses")
  BTCMarketsAddressesResponse depositAddress(
      @HeaderParam("BM-AUTH-APIKEY") String publicKey,
      @HeaderParam("BM-AUTH-TIMESTAMP") SynchronizedValueFactory<Long> nonceFactory,
      @HeaderParam("BM-AUTH-SIGNATURE") BTCMarketsDigestV3 signer,
      @QueryParam("assetName") String assetName)
      throws BTCMarketsException, IOException;
}
