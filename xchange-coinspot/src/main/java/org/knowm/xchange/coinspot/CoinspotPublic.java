package org.knowm.xchange.coinspot;

import org.knowm.xchange.coinspot.dto.CoinspotRates;
import org.knowm.xchange.coinspot.service.CoinspotException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;

@Produces({"application/json"})
@Path("/pubapi")
public interface CoinspotPublic {

    @GET
    @Path("/latest")
    CoinspotRates getRates() throws CoinspotException, IOException;

}
