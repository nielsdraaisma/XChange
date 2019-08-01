package org.knowm.xchange.b2c2;

import org.knowm.xchange.b2c2.dto.trade.OrderRequest;
import org.knowm.xchange.b2c2.dto.trade.OrderResponse;
import org.knowm.xchange.b2c2.service.B2C2Exception;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;

@Path("/")
@Produces({"application/json"})
public interface B2C2 {

    @GET
    @Path("balance/")
    Map<String, String> getBalances(@HeaderParam("Authorization") String authorization)
            throws B2C2Exception, IOException;

    //    @GET
    //    @Path("rates")
    //    Map<String, BigDecimal> getRates(
    //            @HeaderParam("X-Api-Version") String apiVersion,
    //            @HeaderParam("X-Api-Key") String apiKey,
    //            @HeaderParam("X-Api-Signature") ParamsDigest signer,
    //            @QueryParam("timestamp") long timestamp)
    //            throws WyreException, IOException;
    //
    //    @GET
    //    @Path("transfer/{id}")
    //    TransferStatus getTransferStatus(
    //            @HeaderParam("X-Api-Version") String apiVersion,
    //            @HeaderParam("X-Api-Key") String apiKey,
    //            @HeaderParam("X-Api-Signature") ParamsDigest signer,
    //            @QueryParam("timestamp") long timestamp,
    //            @PathParam("id") String transactionId);
    //
    //    @GET
    //    @Path("transfers/{srn}")
    //    TransferHistoryResponse getTransferHistory(
    //            @HeaderParam("X-Api-Version") String apiVersion,
    //            @HeaderParam("X-Api-Key") String apiKey,
    //            @HeaderParam("X-Api-Signature") ParamsDigest signer,
    //            @QueryParam("timestamp") long timestamp,
    //            @PathParam("srn") String walletSrn);
    //
    //
    //    @GET
    //    @Path("wallets")
    //    GetWalletsResponse listWallets(
    //            @HeaderParam("X-Api-Version") String apiVersion,
    //            @HeaderParam("X-Api-Key") String apiKey,
    //            @HeaderParam("X-Api-Signature") ParamsDigest signer,
    //            @QueryParam("timestamp") long timestamp);
    //
    @POST
    @Path("order/")
    @Consumes(MediaType.APPLICATION_JSON)
    OrderResponse order(@HeaderParam("Authorization") String authorization, OrderRequest request);
    //
    //
    //    @POST
    //    @Path("transfers")
    //    @Consumes(MediaType.APPLICATION_JSON)
    //    TransferStatus transfer(
    //            @HeaderParam("X-Api-Version") String apiVersion,
    //            @HeaderParam("X-Api-Key") String apiKey,
    //            @HeaderParam("X-Api-Signature") ParamsDigest signer,
    //            @QueryParam("timestamp") long timestamp,
    //            TransferRequest request);

}
