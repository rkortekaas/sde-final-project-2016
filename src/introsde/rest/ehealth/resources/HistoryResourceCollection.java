package introsde.rest.ehealth.resources;
import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.model.LifeStatus;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container

public class HistoryResourceCollection {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int id;
    String type;
    String before;
    String after;

    EntityManager entityManager; // only used if the application is deployed in a Java EE container

    public HistoryResourceCollection(UriInfo uriInfo, Request request,int id, String type, EntityManager em) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.entityManager = em;
        this.type = type;
    }
    
    public HistoryResourceCollection(UriInfo uriInfo, Request request,int id, String type) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.type = type;
    }
    
    public HistoryResourceCollection(UriInfo uriInfo, Request request,int id, String type, String before, String after) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.type = type;
        this.before = before;
        this.after = after;
    }
    
    // Application integration
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<HealthMeasureHistory> getHistory() throws ParseException {
    	List<HealthMeasureHistory> hm=null;
    	if (before!=null || after!=null){
    		hm = HealthMeasureHistory.getWithDate(id, type, before, after);
    	}else{
    		hm = HealthMeasureHistory.getAll(id, type);
    	}
 	
        if (hm.size()==0)
            throw new RuntimeException("Get: Person with " + id + " not found");
        return hm;
    }
    
    @POST
    @Produces({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public LifeStatus newHistory(HealthMeasureHistory history) throws IOException, ParseException {
        System.out.println("Creating new history to person with id="+id);            
        return HealthMeasureHistory.saveHistory(history, id, type);
    }

}
