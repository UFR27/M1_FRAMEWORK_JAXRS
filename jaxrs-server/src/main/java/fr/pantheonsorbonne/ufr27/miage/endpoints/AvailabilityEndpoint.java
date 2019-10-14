package fr.pantheonsorbonne.ufr27.miage.endpoints;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

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

import java.text.DateFormatSymbols;
import fr.pantheonsorbonne.ufr27.miage.AvailabilityNeutralResponse;
import fr.pantheonsorbonne.ufr27.miage.AvailabilityNeutralResponses;
import fr.pantheonsorbonne.ufr27.miage.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.business.AmadeusBusiness;
import fr.pantheonsorbonne.ufr27.miage.exceptions.DateParseException;
import fr.pantheonsorbonne.ufr27.miage.vo.ANFlightDTO;

@Path("AN")
public class AvailabilityEndpoint {

	@Inject
	AmadeusBusiness business;

	private static final DateFormat FORMAT_DATE = new SimpleDateFormat("ddMM");
	private static final DateFormat FORMAT_TIME = DateFormat.getTimeInstance();
	private static final ObjectFactory FACTORY = new ObjectFactory();
	private static final DatatypeFactory DATATYPE_FACTORY;
	private static final ZoneId ZONE = ZoneId.systemDefault();
	private static final String[] shortMonthes = new DateFormatSymbols().getShortMonths();

	static {
		try {
			DATATYPE_FACTORY = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			// we are doomed, we cannot create a datatype factory
			throw new RuntimeException(e);
		}
	}

	private Date parseDate(String strDate, String time) throws DateParseException {
		Date theDate = parseDateOnly(strDate);
		if (time != null) {

			Pattern pattern = Pattern.compile("(\\d{2})(\\d{2})");
			Matcher matcher = pattern.matcher(time);
			if (matcher.matches()) {
				theDate.setHours(Integer.valueOf(matcher.group(1)));
				theDate.setMinutes(Integer.valueOf(matcher.group(1)));

			} else {
				throw new DateParseException();
			}

		}

		return theDate;

	}

	private Date parseDateOnly(String strDate) throws DateParseException {
		try {

			Pattern pattern = Pattern.compile("([0-9]{1,2})([A-Z]{3})");
			Matcher matcher = pattern.matcher(strDate);
			if (matcher.matches()) {

				String dom = matcher.group(1);
				String moy = matcher.group(2);
				OptionalInt monthIndex = IntStream.range(0, shortMonthes.length)
						.filter(i -> shortMonthes[i].toLowerCase().equals(moy.toLowerCase())).findFirst();
				if (!monthIndex.isPresent()) {
					throw new DateParseException();
				}
				int day = Integer.valueOf(dom);

				Calendar cal = Calendar.getInstance();
				cal.set(cal.get(Calendar.YEAR), monthIndex.getAsInt(), day);
				return cal.getTime();
			}
			throw new DateParseException();
		} catch (Exception e) {
			throw new DateParseException();
		}

	}

	@GET
	@Path("{date}/{org}/{dest}/{time}")
	@Produces(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public AvailabilityNeutralResponses getAN(@PathParam("date") String date, //
			@PathParam("org") String org, //
			@PathParam("dest") String dest, //
			@PathParam("time") String time) throws JAXBException, DateParseException {

		Date theDate = parseDate(date, time);
		// Date theDate = new Date(FORMAT_DATE.parse(date).getTime() +
		// FORMAT_TIME.parse(time).getTime());

		AvailabilityNeutralResponses responses = FACTORY.createAvailabilityNeutralResponses();
		for (ANFlightDTO dto : business.findFlights(org, dest, theDate)) {
			AvailabilityNeutralResponse resp = FACTORY.createAvailabilityNeutralResponse();

			resp.setDestination(dto.getArrival());
			resp.setOrigin(dto.getDeparture());

			XMLGregorianCalendar arrival = Date2XMLGregorianCalendar(dto.getArrivalTime());
			resp.setArrivalTime(arrival);

			XMLGregorianCalendar dep = Date2XMLGregorianCalendar(dto.getDepartureTime());
			resp.setDepartureTime(dep);

			Duration duration = getDuration(dto.getArrivalTime(), dto.getDepartureTime());
			resp.setDuration(duration);

			resp.setIdCompagnie(dto.getCompany());
			resp.setIdVol(BigInteger.valueOf(Long.valueOf(dto.getId())));

			responses.getResponses().add(resp);

		}

		return responses;

	}

	private Duration getDuration(Date arrivalTime, Date departureTime) {
		return DATATYPE_FACTORY.newDuration(arrivalTime.getTime() - departureTime.getTime());
	}

	private XMLGregorianCalendar Date2XMLGregorianCalendar(Date arrivalTime) {
		Instant instant = Instant.ofEpochMilli(arrivalTime.getTime());
		ZonedDateTime dateTime = instant.atZone(ZONE);
		GregorianCalendar c = GregorianCalendar.from(dateTime);
		XMLGregorianCalendar cal = DATATYPE_FACTORY.newXMLGregorianCalendar(c);
		return cal;
	}

}
