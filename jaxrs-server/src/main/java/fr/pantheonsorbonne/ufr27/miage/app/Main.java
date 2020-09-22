package fr.pantheonsorbonne.ufr27.miage.app;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

import fr.pantheonsorbonne.ufr27.miage.exceptions.DateParseException;
import fr.pantheonsorbonne.ufr27.miage.exceptions.ExceptionMapper;

/**
 * Main class.
 *
 */
public class Main {

	public static final String BASE_URI = "http://localhost:8080/";

	public static HttpServer startServer() {

		final ResourceConfig rc = new ResourceConfig()//
				.packages("fr.pantheonsorbonne.ufr27.miage.endpoints")//
				.register(DeclarativeLinkingFeature.class)//
				.register(ExceptionMapper.class)//
				.register(DateParseException.class)//
				.register(new AbstractBinder() {

					@Override
					protected void configure() {
						//bind(AmadeusBusinessImpl.class).to(AmadeusBusiness.class);

					}

				});

		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		Locale.setDefault(Locale.ENGLISH);
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		final HttpServer server = startServer();
		System.out.println(String.format(
				"Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...",
				BASE_URI));
		System.in.read();
		server.stop();
	}
}
