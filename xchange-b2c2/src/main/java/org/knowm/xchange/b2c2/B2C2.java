package org.knowm.xchange.b2c2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.knowm.xchange.b2c2.dto.trade.*;
import org.knowm.xchange.b2c2.service.B2C2Exception;

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
  @Path("order/{id}")
  List<OrderResponse> getOrder(
      @HeaderParam("Authorization") String authorization, @PathParam("id") String id)
      throws B2C2Exception, IOException;

  @GET
  @Path("order/")
  List<OrderResponse> getOrders(
      @HeaderParam("Authorization") String authorization,
      @QueryParam("created_gte") ZonedDateTime createdGte,
      @JsonProperty("created__lt") ZonedDateTime createdLt,
      @JsonProperty("client_order_id") String clientOrderId,
      @JsonProperty("order_type") String orderType,
      @JsonProperty("executing_unit") String executingUnit,
      @JsonProperty("instrument") String instrument,
      @JsonProperty("offset") Long offset,
      @JsonProperty("limit") Integer limit)
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
