package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.MeasureDefinition;

import java.text.ParseException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class HistoryResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int id;
    String type;
    int mid;

    EntityManager entityManager; // only used if the application is deployed in a Java EE container

    public HistoryResource(UriInfo uriInfo, Request request,int id, String type, int mid , EntityManager em) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.entityManager = em;
        this.type = type;
        this.mid = mid;
    }

    public HistoryResource(UriInfo uriInfo, Request request,int id, String type, int mid) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.type = type;
        this.mid = mid;
    }


    
    // Application integration
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public HealthMeasureHistory getHM() {
    	HealthMeasureHistory hm = HealthMeasureHistory.getHistoryId(mid);
        if (hm == null || hm.getPerson().getIdPerson()!=id)
            throw new RuntimeException("Get: Person with " + id + " not found");
        if (! type.equals(hm.getMeasureDefName()))
        	throw new RuntimeException("Get: History with " + mid + " not found");
        return hm;
    }

    // for the browser
    @GET
    @Produces(MediaType.TEXT_XML)
    public HealthMeasureHistory getHMHTML() {
    	HealthMeasureHistory hm = HealthMeasureHistory.getHistoryId(mid);
        if (hm == null || hm.getPerson().getIdPerson()!=id)
            throw new RuntimeException("Get: Person with " + id + " not found");
        if (! type.equals(hm.getMeasureDefName()))
        	throw new RuntimeException("Get: History with " + mid + " not found");
        return hm;
    }
    
    
    @PUT
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public HealthMeasureHistory putMeasureHistory(HealthMeasureHistory history) throws ParseException {
        System.out.println("--> Updating HealthMeasureHistory... " +this.mid);
        Response res;
        history.setIdMeasureHistory(mid);
        history.setPerson(this.getPersonById(id));
        history.setMeasureDefinition(MeasureDefinition.getByType(type));
        if(history.getTimestamp() == null) history.setTimestamp(HealthMeasureHistory.getHistoryId(mid).getTimestamp());
        HealthMeasureHistory.updateHealthMeasureHistory(history);
        
        return history;
    }



    public Person getPersonById(int personId) {
        System.out.println("Reading person from DB with id: "+personId);

        // this will work within a Java EE container, where not DAO will be needed
        //Person person = entityManager.find(Person.class, personId); 

        Person person = Person.getPersonById(personId);
        System.out.println("Person: "+person.toString());
        return person;
    }
}