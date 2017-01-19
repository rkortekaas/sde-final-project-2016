package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.MeasureDefinition;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/measureTypes")
@XmlRootElement
public class MeasureDefinitionResource {

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
    @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    public String getPersonsXML() {
        System.out.println("Getting list of measure definition in XML...");
        List<MeasureDefinition> md = MeasureDefinition.getAll();
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><measureTypes>";
        for (int i = 0; i < md.size(); i++){
        	result += "<measureType>" + md.get(i).getMeasureName() + "</measureType>" ;
        }
        result += "</measureTypes>";
        return result;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    public String getPersonsJSON() {
        System.out.println("Getting list of measure definition in JSON...");
        List<MeasureDefinition> md = MeasureDefinition.getAll();
        String result = "{\"measureType\": [";
        for (int i = 0; i < md.size()-1; i++){
        	result += "\"" + md.get(i).getMeasureName() + "\"," ;
        }
        result += "\"" +  md.get(md.size()-1).getMeasureName()+ "\"]}" ;
        return result;
    }


}