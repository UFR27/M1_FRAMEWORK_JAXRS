package fr.pantheonsorbonne.ufr27.miage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/AN");
		WebTarget anWebTarget = webTarget.path("01FEB").path("BDX").path("CDG").path("1400");
		
		Response resp = anWebTarget.request(MediaType.APPLICATION_XML).get();
		AvailabilityNeutralResponses anResponses = (AvailabilityNeutralResponses) resp.readEntity(AvailabilityNeutralResponses.class);
		
		
		
		System.out.println(anResponses.toString());
				
	}
}
