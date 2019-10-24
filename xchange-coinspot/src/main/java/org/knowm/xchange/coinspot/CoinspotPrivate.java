package org.knowm.xchange.coinspot;

import org.knowm.xchange.coinspot.dto.CoinspotOrderbook;
import org.knowm.xchange.coinspot.dto.CoinspotRequest;
import org.knowm.xchange.coinspot.service.CoinspotDigest;
import org.knowm.xchange.coinspot.service.CoinspotException;

import javax.ws.rs.*;
import java.io.IOException;

@Produces({"application/json"})
@Path("/api")
@Consumes({"application/json"})
public interface CoinspotPrivate {

    @POST
    @Path("/orders")
    CoinspotOrderbook getOrders(
            @HeaderParam("key") String apiKey,
            @HeaderParam("sign") CoinspotDigest digest,
            CoinspotRequest request) throws CoinspotException, IOException;

}
