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
		WebTarget anWebTarget = webTarget.path("1").path("2").path("3").path("4");
		
		Response resp = anWebTarget.request(MediaType.APPLICATION_XML).get();
		AvailabilityNeutralResponse anResponse = (AvailabilityNeutralResponse) resp.readEntity(AvailabilityNeutralResponse.class);
		
		
		System.out.println(anResponse.toString());
				
	}
}
