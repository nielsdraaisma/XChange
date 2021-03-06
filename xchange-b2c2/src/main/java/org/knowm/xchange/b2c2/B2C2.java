package org.knowm.xchange.b2c2;

import org.knowm.xchange.b2c2.dto.trade.*;
import org.knowm.xchange.b2c2.service.B2C2Exception;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Path("/")
@Produces({"application/json"})
public interface B2C2 {

    @GET
    @Path("balance/")
    Map<String, String> getBalances(@HeaderParam("Authorization") String authorization)
            throws B2C2Exception, IOException;

    @POST
    @Path("order/")
    @Consumes(MediaType.APPLICATION_JSON)
    OrderResponse order(@HeaderParam("Authorization") String authorization, OrderRequest request)
            throws B2C2Exception, IOException;

    @GET
    @Path("trade/{id}")
    TradeResponse getTrade(
            @HeaderParam("Authorization") String authorization,
            @PathParam("id") String id
    ) throws B2C2Exception, IOException;

    @GET
    @Path("trade/")
    List<TradeResponse> getTrades(
            @HeaderParam("Authorization") String authorization,
            @QueryParam("created_gte") ZonedDateTime createdGte,
            @QueryParam("created__lt") ZonedDateTime createdLt,
            @QueryParam("client_order_id") String clientOrderId,
            @QueryParam("order_type") String orderType,
            @QueryParam("executing_unit") String executingUnit,
            @QueryParam("instrument") String instrument,
            @QueryParam("offset") Long offset,
            @QueryParam("limit") Integer limit)
            throws B2C2Exception, IOException;

    @POST
    @Path("request_for_quote/")
    @Consumes(MediaType.APPLICATION_JSON)
    QuoteResponse quote(@HeaderParam("Authorization") String authorization, QuoteRequest request)
            throws B2C2Exception, IOException;

    @GET
    @Path("ledger/")
    List<LedgerItem> ledger(
            @HeaderParam("Authorization") String authorization,
            @QueryParam("offset") Long offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("type") String type,
            @QueryParam("since") String since)
            throws B2C2Exception, IOException;

    @GET
    @Path("instruments/")
    List<Instrument> instruments(@HeaderParam("Authorization") String authorization)
            throws B2C2Exception, IOException;

    @POST
    @Path("withdrawal/")
    @Consumes(MediaType.APPLICATION_JSON)
    WithdrawalResponse withdraw(
            @HeaderParam("Authorization") String authorization, WithdrawalRequest request)
            throws B2C2Exception, IOException;
}
