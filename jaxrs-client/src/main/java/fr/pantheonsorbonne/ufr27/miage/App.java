package fr.pantheonsorbonne.ufr27.miage;

import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Hello world!
 *
 */
public class App {

	private static final Client client = ClientBuilder.newClient();
	private static final WebTarget webTarget = client.target("http://localhost:8080/");
	

	public static void main(String[] args) {

		App app = new App();
		app.run();

	}

	public void run() {
		try (Scanner scanner = new Scanner(System.in)) {

			System.out.println("Enter the number here:");
			while (!scanner.hasNextInt() && scanner.hasNext()) {

			}
		}
	}
}
