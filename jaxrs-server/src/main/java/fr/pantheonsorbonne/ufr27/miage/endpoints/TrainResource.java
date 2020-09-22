package fr.pantheonsorbonne.ufr27.miage.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import fr.pantheonsorbonne.ufr27.miage.ObjectFactory;
import fr.pantheonsorbonne.ufr27.miage.Train;

@Path("api/train/{id}")
public class TrainResource {

	int trainId;

	public TrainResource(@PathParam("id") int trainId) {
		this.trainId = trainId;
	}

	@PUT
	@Path("location")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response putTrain(String gpsCoordinate) throws DatatypeConfigurationException {

		System.out.println("gpsCoordinate=" + gpsCoordinate);
		Train t = new ObjectFactory().createTrain();

		XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar("2009-05-07T17:05:45.678Z");

		t.setArrivalTime(date2);

		return Response.ok(t).build();
	}

}
