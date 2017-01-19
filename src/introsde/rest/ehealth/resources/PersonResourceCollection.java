package introsde.rest.ehealth.resources;
import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.Person;

import java.io.IOException;
import java.util.List;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/person")
public class PersonResourceCollection {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    // will work only inside a Java EE application
    @PersistenceUnit(unitName="assignment")
    EntityManager entityManager;

    // will work only inside a Java EE application
    @PersistenceContext(unitName = "assignment",type=PersistenceContextType.TRANSACTION)
    private EntityManagerFactory entityManagerFactory;

    // Return the list of people to the user in the browser
    @GET
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public List<Person> getPersonsBrowser(@QueryParam("measureType") String measureType, @QueryParam("max") int max, @QueryParam("min") int min) {
    	List<Person> people = null;
    	if(max!=0 || min!=0){
    		System.out.println("Getting list of people with " + measureType +" between "+ min +" and "+ max );
            people = Person.getWithRange(measureType, min, max);  
        	
        }else{
        	System.out.println("Getting list of people...");
            people = Person.getAll();        	
        }
    	
        return people;
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public Person newPerson(Person person) throws IOException {
        System.out.println("Creating new person...");            
        return Person.savePerson(person);
    }

    // Defines that the next path parameter after the base url is
    // treated as a parameter and passed to the PersonResources
    // Allows to type http://localhost:599/base_url/1
    // 1 will be treaded as parameter todo and passed to PersonResource
    @Path("{personId}")
    public PersonResource getPerson(@PathParam("personId") int id) {
        return new PersonResource(uriInfo, request, id);
    }
    
    @Path("{personId}/{measureType}")
    public HistoryResourceCollection getHistoryPerson(@PathParam("personId") int id, @PathParam("measureType") String type, 
    		@QueryParam("before") String before, @QueryParam("after") String after) {
    	if (after!=null || before!=null) return new HistoryResourceCollection(uriInfo, request, id,type, before, after);
    	return new HistoryResourceCollection(uriInfo, request, id,type);
    }
    
    @Path("{personId}/{measureType}/{mid}")
    public HistoryResource getPerson(@PathParam("personId") int id,@PathParam("measureType") String type, @PathParam("mid") int mid) {
        return new HistoryResource(uriInfo, request, id,type,mid);
    }
}