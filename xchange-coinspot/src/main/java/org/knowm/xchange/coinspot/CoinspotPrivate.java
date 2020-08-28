package org.knowm.xchange.coinspot;

import java.io.IOException;
import javax.ws.rs.*;
import org.knowm.xchange.coinspot.dto.*;
import org.knowm.xchange.coinspot.service.CoinspotDigest;
import org.knowm.xchange.coinspot.service.CoinspotException;

@Produces({"application/json"})
@Path("/api")
@Consumes({"application/json"})
public interface CoinspotPrivate {

  @POST
  @Path("/orders")
  CoinspotOrderbook getOpenOrders(
      @HeaderParam("key") String apiKey,
      @HeaderParam("sign") CoinspotDigest digest,
      CoinspotRequest request)
      throws CoinspotException, IOException;

  /** A list of your open orders by coin type, it will return a maximum of 100 results */
  @GET
  @Path("/my/orders")
  CoinspotMyOrders getMyOrders(
      @HeaderParam("key") String apiKey,
      @HeaderParam("sign") CoinspotDigest digest,
      CoinspotRequest request)
      throws CoinspotException, IOException;

  @GET
  @Path("/my/balances")
  CoinspotBalancesResponse getBalances(
      @HeaderParam("key") String apiKey,
      @HeaderParam("sign") CoinspotDigest digest,
      CoinspotRequest request)
      throws CoinspotException, IOException;

  @POST
  @Path("/my/buy")
  CoinspotPlaceOrderResponse placeBuyOrder(
      @HeaderParam("key") String apiKey,
      @HeaderParam("sign") CoinspotDigest digest,
      CoinspotPlaceOrderRequest request)
      throws CoinspotException, IOException;

  @POST
  @Path("/my/sell")
  CoinspotPlaceOrderResponse placeSellOrder(
      @HeaderParam("key") String apiKey,
      @HeaderParam("sign") CoinspotDigest digest,
      CoinspotPlaceOrderRequest request)
      throws CoinspotException, IOException;

  @POST
  @Path("/my/buy/cancel")
  CoinspotStatusResponse cancelBuyOrder(
      @HeaderParam("key") String apiKey,
      @HeaderParam("sign") CoinspotDigest digest,
      CoinspotRequest request)
      throws CoinspotException, IOException;

  @POST
  @Path("/my/sell/cancel")
  CoinspotStatusResponse cancelSellOrder(
      @HeaderParam("key") String apiKey,
      @HeaderParam("sign") CoinspotDigest digest,
      CoinspotRequest request)
      throws CoinspotException, IOException;

  // Does not return order id's
  //    @GET
  //    @Path("/ro/my/transactions")
  //    CoinspotMyTransactionsResponse getMyTransactions(
  //            @HeaderParam("key") String apiKey,
  //            @HeaderParam("sign") CoinspotDigest digest,
  //            CoinspotMyTransactionsRequest request
  //    ) throws CoinspotException, IOException;

  @GET
  @Path("/ro/my/transactions")
  CoinspotMyTransactionsResponse getMyTransactions(
      @HeaderParam("key") String apiKey,
      @HeaderParam("sign") CoinspotDigest digest,
      CoinspotMyTransactionsRequest request)
      throws CoinspotException, IOException;

  @GET
  @Path("/ro/my/transactions/{coinType}")
  CoinspotMyTransactionsResponse getMyTransactionsForCoin(
      @HeaderParam("key") String apiKey,
      @HeaderParam("sign") CoinspotDigest digest,
      @PathParam("coinType") String coinType,
      CoinspotMyTransactionsRequest request)
      throws CoinspotException, IOException;
}
