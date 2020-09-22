package fr.pantheonsorbonne.ufr27.miage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Hello world!
 *
 */
public class App {

	private static final Client client = ClientBuilder.newClient();
	private static final WebTarget webTarget = client.target("http://localhost:8080/");

	public static void main(String[] args) throws InterruptedException {

		App app = new App();
		app.run();

	}

	public static Entity<String> getGPSCoordinates() {

		return Entity.entity("48.840399, 2.341958", MediaType.TEXT_PLAIN);

	}

	public void run() throws InterruptedException {

		Client client = ClientBuilder.newClient();

		WebTarget target = client.target("http://localhost:8080/api/train/123123/location");

		

		for(;;) {
			Thread.sleep(1000);
			
			Train train = target.request().put(getGPSCoordinates()).readEntity(Train.class);
			System.out.println(train.getArrivalTime().toString());
		}
	}
}
