package fr.pantheonsorbonne.ufr27.miage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.pantheonsorbonne.ufr27.miage.ASs.AS;

/**
 * Hello world!
 *
 */
public class App {

	private static final Client client = ClientBuilder.newClient();
	private static final WebTarget webTarget = client.target("http://localhost:8080/");
	private List<AvailabilityNeutralResponse> currentProposals;

	public static void main(String[] args) {

		App app = new App();
		app.run();

	}

	private void handleTTE(String tte) {
		System.out.println("TTE");
		System.out.println("tte:" + tte);
		//PUT CODE HERE

	}

	private void handleTTP(String ttp) {
		System.out.println("TTP");
		System.out.println("ttp:" + ttp);
		//PUT CODE HERE

	}

	private void handleTQT(String tqt) {
		System.out.println("TQT");
		System.out.println("tqt:" + tqt);
		//PUT CODE HERE

	}

	private void handleFXP() {
		System.out.println("FXP");
		//PUT CODE HERE

	}

	private void handleSS(String count, String klass, String line) {
		System.out.println("SS");
		System.out.println("count:" + count);
		System.out.println("klass:" + klass);
		System.out.println("line:" + line);
		//PUT CODE HERE
		
		

	}

	private void handleAN(String date, String org, String des, String time) {

		WebTarget anWebTarget = webTarget.path("AN").path(date).path(org).path(des);
		if (!time.isEmpty()) {
			anWebTarget = anWebTarget.path(time);
		} else {
			anWebTarget = anWebTarget.path("0000");
		}

		Response resp = anWebTarget.request().get();
		AvailabilityNeutralResponses anResponses = (AvailabilityNeutralResponses) resp
				.readEntity(AvailabilityNeutralResponses.class);

		List<AvailabilityNeutralResponse> currentProposals = new ArrayList<>(anResponses.getResponses());

		showAN(currentProposals);

	}

	private void showAN(List<AvailabilityNeutralResponse> currentProposals) {
		for (int i = 1; i <= currentProposals.size(); i++) {
			AvailabilityNeutralResponse r = currentProposals.get(i - 1);
			StringBuilder sb = new StringBuilder();
			for (AS as : r.getNbPlacesRestantes().getAS()) {
				sb.append(String.format("%s%d", as.getKlass(), Math.min(9, as.getSeat()))).append(" ");
			}

			System.out.println(String.format("%d\t%s %s %s \t %s %s \t %s \t %s \t %s", i, r.getIdentifiantCompanie(),
					r.getIdentifiantVol(), sb.toString(), r.getOrigine(), r.getDestination(),
					String.format("%2d:%2d", r.getHeureDepart().getHour(), r.getHeureDepart().getMinute()),
					r.getHeureArrivee(),
					String.format("%2d:%2d", r.getHeureArrivee().getHour(), r.getHeureArrivee().getMinute()),
					r.getDureeVol()));
		}
	}

	public void run() {
		try (Scanner scanner = new Scanner(System.in)) {

			System.out.println("Enter the number here:");
			while (!scanner.hasNextInt() && scanner.hasNext()) {
				String input = scanner.next();
				{

					Pattern p = Pattern.compile("AN(.{5})(.{3})(.{3})(.{0,4})");
					Matcher matcher = p.matcher(input);
					if (matcher.matches()) {
						String date = matcher.group(1);
						String org = matcher.group(2);
						String des = matcher.group(3);
						String time = matcher.group(4);

						handleAN(date, org, des, time);
						continue;
					}
				}
				{
					Pattern p = Pattern.compile("SS(\\d)([A-Z])(\\d)");
					Matcher matcher = p.matcher(input);
					if (matcher.matches()) {
						String count = matcher.group(1);
						String klass = matcher.group(2);
						String line = matcher.group(3);

						handleSS(count, klass, line);
						continue;
					}
				}

				{
					Pattern p = Pattern.compile("FXP");
					Matcher matcher = p.matcher(input);
					if (matcher.matches()) {

						handleFXP();
						continue;
					}
				}

				{
					Pattern p = Pattern.compile("TQT/(.{8})");
					Matcher matcher = p.matcher(input);
					if (matcher.matches()) {
						String tqt = matcher.group(1);

						handleTQT(tqt);
						continue;
					}
				}

				{
					Pattern p = Pattern.compile("TTP");
					Matcher matcher = p.matcher(input);
					if (matcher.matches()) {
						String ttp = matcher.group(1);

						handleTTP(ttp);
						continue;
					}
				}

				{
					Pattern p = Pattern.compile("TTE/(.{8})");
					Matcher matcher = p.matcher(input);
					if (matcher.matches()) {
						String tte = matcher.group(1);

						handleTTE(tte);
						continue;
					}
				}

			}
		}
	}
}
