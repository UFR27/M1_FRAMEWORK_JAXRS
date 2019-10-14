package fr.pantheonsorbonne.ufr27.miage.endpoints;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.research.ws.wadl.Option;

import fr.pantheonsorbonne.ufr27.miage.AvailabilityNeutralResponse;
import fr.pantheonsorbonne.ufr27.miage.AvailabilityNeutralResponses;
import fr.pantheonsorbonne.ufr27.miage.business.AmadeusBusiness;
import fr.pantheonsorbonne.ufr27.miage.exceptions.DateParseException;
import fr.pantheonsorbonne.ufr27.miage.utils.Utils;
import fr.pantheonsorbonne.ufr27.miage.vo.ANFlightDTO;

@Path("AN")
public class AvailabilityEndpoint {

	@Inject
	AmadeusBusiness business;

	
	

	@GET
	@Path("{date}/{org}/{dest}/{time}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public AvailabilityNeutralResponses getAN(@PathParam("date") String date, //
			@PathParam("org") String org, //
			@PathParam("dest") String dest, //
			@PathParam("time") String time) throws JAXBException, DateParseException {

		Date theDate = Utils.parseDate(date, time);

		AvailabilityNeutralResponses responses = Utils.FACTORY.createAvailabilityNeutralResponses();
		for (ANFlightDTO dto : business.findFlights(org, dest, theDate)) {
			AvailabilityNeutralResponse resp = Utils.FACTORY.createAvailabilityNeutralResponse();

			resp.setDestination(dto.getArrival());
			resp.setOrigin(dto.getDeparture());

			XMLGregorianCalendar arrival = Utils.Date2XMLGregorianCalendar(dto.getArrivalTime());
			resp.setArrivalTime(arrival);

			XMLGregorianCalendar dep = Utils.Date2XMLGregorianCalendar(dto.getDepartureTime());
			resp.setDepartureTime(dep);

			Duration duration = Utils.getDuration(dto.getArrivalTime(), dto.getDepartureTime());
			resp.setDuration(duration);

			resp.setIdCompagnie(dto.getCompany());
			resp.setIdVol(BigInteger.valueOf(Long.valueOf(dto.getId())));

			responses.getResponses().add(resp);

		}

		return responses;

	}

}
