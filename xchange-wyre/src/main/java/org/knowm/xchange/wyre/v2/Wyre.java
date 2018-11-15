package org.knowm.xchange.wyre.v2;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.knowm.xchange.wyre.v2.dto.QuoteTransferRequest;
import org.knowm.xchange.wyre.v2.dto.TransferHistoryResponse;
import org.knowm.xchange.wyre.v2.dto.TransferRequest;
import org.knowm.xchange.wyre.v2.dto.TransferStatus;
import org.knowm.xchange.wyre.v2.dto.account.GetWalletsResponse;
import org.knowm.xchange.wyre.v2.dto.account.WyreAccount;
import org.knowm.xchange.wyre.v2.service.WyreException;
import si.mazi.rescu.ParamsDigest;

@Path("/v2/")
@Produces({"application/json"})
public interface Wyre {

  @GET
  @Path("account")
  WyreAccount getAccount(
      @HeaderParam("X-Api-Version") String apiVersion,
      @HeaderParam("X-Api-Key") String apiKey,
      @HeaderParam("X-Api-Signature") ParamsDigest signer,
      @QueryParam("timestamp") long timestamp)
      throws WyreException, IOException;

  @GET
  @Path("rates")
  Map<String, BigDecimal> getRates(
      @HeaderParam("X-Api-Version") String apiVersion,
      @HeaderParam("X-Api-Key") String apiKey,
      @HeaderParam("X-Api-Signature") ParamsDigest signer,
      @QueryParam("timestamp") long timestamp)
      throws WyreException, IOException;

  @GET
  @Path("transfer/{id}")
  TransferStatus getTransferStatus(
      @HeaderParam("X-Api-Version") String apiVersion,
      @HeaderParam("X-Api-Key") String apiKey,
      @HeaderParam("X-Api-Signature") ParamsDigest signer,
      @QueryParam("timestamp") long timestamp,
      @PathParam("id") String transactionId);

  @GET
  @Path("transfers/{srn}")
  TransferHistoryResponse getTransferHistory(
      @HeaderParam("X-Api-Version") String apiVersion,
      @HeaderParam("X-Api-Key") String apiKey,
      @HeaderParam("X-Api-Signature") ParamsDigest signer,
      @QueryParam("timestamp") long timestamp,
      @PathParam("srn") String walletSrn);

  @GET
  @Path("wallets")
  GetWalletsResponse listWallets(
      @HeaderParam("X-Api-Version") String apiVersion,
      @HeaderParam("X-Api-Key") String apiKey,
      @HeaderParam("X-Api-Signature") ParamsDigest signer,
      @QueryParam("timestamp") long timestamp);

  @POST
  @Path("transfers")
  @Consumes(MediaType.APPLICATION_JSON)
  TransferStatus quoteTransfer(
      @HeaderParam("X-Api-Version") String apiVersion,
      @HeaderParam("X-Api-Key") String apiKey,
      @HeaderParam("X-Api-Signature") ParamsDigest signer,
      @QueryParam("timestamp") long timestamp,
      QuoteTransferRequest request);

  @POST
  @Path("transfers")
  @Consumes(MediaType.APPLICATION_JSON)
  TransferStatus transfer(
      @HeaderParam("X-Api-Version") String apiVersion,
      @HeaderParam("X-Api-Key") String apiKey,
      @HeaderParam("X-Api-Signature") ParamsDigest signer,
      @QueryParam("timestamp") long timestamp,
      TransferRequest request);
}
