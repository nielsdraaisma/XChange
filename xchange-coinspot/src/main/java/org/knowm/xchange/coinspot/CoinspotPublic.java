package org.knowm.xchange.coinspot;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.knowm.xchange.coinspot.dto.CoinspotRates;
import org.knowm.xchange.coinspot.service.CoinspotException;

@Produces({"application/json"})
@Path("/pubapi")
public interface CoinspotPublic {

  @GET
  @Path("/latest")
  CoinspotRates getRates() throws CoinspotException, IOException;
}
