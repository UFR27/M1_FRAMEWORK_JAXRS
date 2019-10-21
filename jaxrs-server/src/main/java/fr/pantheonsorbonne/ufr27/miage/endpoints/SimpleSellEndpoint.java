package fr.pantheonsorbonne.ufr27.miage.endpoints;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.pantheonsorbonne.ufr27.miage.ASs.AS;
import fr.pantheonsorbonne.ufr27.miage.AvailabilityNeutralResponse;
import fr.pantheonsorbonne.ufr27.miage.business.AmadeusBusiness;
import fr.pantheonsorbonne.ufr27.miage.business.AmadeusBusinessRandomImpl;

@Path("SS")
public class SimpleSellEndpoint {

	@Inject
	AmadeusBusiness business;

	@GET
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getSS(AvailabilityNeutralResponse anResp) {

		AS as = anResp.getNbPlacesRestantes().getAS().get(0);
		if (anResp.getNbPlacesRestantes().getAS().size() > 1) {
			return Response.status(400, "There should be only 1 AS in the request").build();
		}

		if (business.available(anResp.getOrigine(), anResp.getDestination(),
				anResp.getHeureDepart().toGregorianCalendar().getTime(), as.getKlass(), as.getSeat())) {
			return Response.ok().build();
		} else {
			return Response.status(410, "Seats are not available anymore").build();
		}

	}

}
