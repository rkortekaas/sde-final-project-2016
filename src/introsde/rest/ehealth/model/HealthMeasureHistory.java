package introsde.rest.ehealth.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import introsde.rest.ehealth.dao.LifeCoachDao;

import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * The persistent class for the "HealthMeasureHistory" database table.
 * 
 */
@Entity
@Table(name="\"HealthMeasureHistory\"")
@NamedQuery(name="HealthMeasureHistory.findAll", query="SELECT h FROM HealthMeasureHistory h")
@XmlRootElement
public class HealthMeasureHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_mhistory")
	@TableGenerator(name="sqlite_mhistory", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="HealthMeasureHistory")
	@Column(name="\"idMeasureHistory\"")
	private int idMeasureHistory;

	@Temporal(TemporalType.DATE)
	@Column(name="\"timestamp\"")
	private Date timestamp;

	@Column(name="\"value\"")
	private String value;

	//bi-directional many-to-one association to MeasureDefinition
	@ManyToOne
	@JoinColumn(name = "\"idMeasureDefinition\"", referencedColumnName = "\"idMeasureDef\"")
	private MeasureDefinition measureDefinition;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "\"idPerson\"", referencedColumnName = "\"idPerson\"")
	private Person person;

	public HealthMeasureHistory() {
	}

	// the GETTERS and SETTERS of all the private attributes

	public int getIdMeasureHistory() {
		return this.idMeasureHistory;
	}

	public void setIdMeasureHistory(int idMeasureHistory) {
		this.idMeasureHistory = idMeasureHistory;
	}
	
	@XmlElement(name = "created")
	public String getTimestamp() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(this.timestamp);
	}
	
	public void setTimestamp(String timestamp) throws ParseException{
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        Date date = format.parse(timestamp);
        this.timestamp = date;
    }

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlTransient
	public MeasureDefinition getMeasureDefinition() {
		return this.measureDefinition;
	}
	
	@XmlElement(name = "measureName")
	public String getMeasureDefName() {
		return measureDefinition.getMeasureName();
	}

	public void setMeasureDefinition(MeasureDefinition measureDefinition) {
		this.measureDefinition = measureDefinition;
	}

	@XmlTransient
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public static HealthMeasureHistory getHistoryId(int historyId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        HealthMeasureHistory p = em.find(HealthMeasureHistory.class, historyId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }
	
    public static List<HealthMeasureHistory> getAll(int personId, String type) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<HealthMeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findAll", HealthMeasureHistory.class)
            .getResultList();
        
        LifeCoachDao.instance.closeConnections(em);
        for (int index = list.size()-1 ; index >= 0 ; index--){
        	if ((list.get(index).getPerson().getIdPerson()!=personId) || (!type.equals(list.get(index).getMeasureDefName())) ){
        		list.remove(index);
        	}
        }
        return list;
    }
    
    
    public static List<HealthMeasureHistory> getWithDate(int personId, String type, String before, String after) throws ParseException {
    	DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    	Date bf = null;
    	Date af = null;
    	if (before == null) bf = format.parse(format.format(new Date()));
    	else bf = format.parse(before);
    	if (after == null) af = format.parse(format.format(new Date(0)));
    	else af = format.parse(after);
        
    	EntityManager em = LifeCoachDao.instance.createEntityManager();
    	List<HealthMeasureHistory> list = em
				.createQuery("SELECT l FROM HealthMeasureHistory l WHERE l.measureDefinition.measureName = :type and l.person.idPerson = :id"
						+ " and l.timestamp >= :after and l.timestamp <= :before", HealthMeasureHistory.class)
				.setParameter("type", type)
				.setParameter("id", personId)
				.setParameter("before", bf)
				.setParameter("after", af).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
    }
    
    
    public static LifeStatus saveHistory(HealthMeasureHistory h, int id,String type) throws ParseException {
    	LifeStatus last = LifeStatus.getLastLifeStatus(id, type);
    	Person p =Person.getPersonById(id);

    	if(last!=null) {
    		LifeStatus.removeLifeStatus(last);
    	}
    	// create new lifestatus
    	LifeStatus nieuw = new LifeStatus();    	
    	nieuw.setPerson(p);    	
    	nieuw.setMeasureDef(MeasureDefinition.getByType(type));    	
    	nieuw.setValue(h.getValue()); 
    	LifeStatus ls = LifeStatus.saveLifeStatus(nieuw); // persist lifestatus
    	LifeStatus.refreshLifeStatus(p);
    	
    	// create new history    	
    	HealthMeasureHistory newHistory = new HealthMeasureHistory();
	    newHistory.setMeasureDefinition(ls.getMeasureDef());
	    newHistory.setPerson(p);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		newHistory.setTimestamp(sdf.format(new Date()));
	    newHistory.setValue(ls.getValue());
	    HealthMeasureHistory.refreshHealthMeasureHistory(p);
	    
	    // persist history	    
	    EntityManager em = LifeCoachDao.instance.createEntityManager();
	    EntityTransaction tx = em.getTransaction();
	    tx.begin();
	    em.persist(newHistory);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	        
	    return ls;
    } 
    
	public static void refreshHealthMeasureHistory(Person p){
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<HealthMeasureHistory> history = em
				.createQuery("SELECT l FROM HealthMeasureHistory l WHERE l.person.idPerson = :id", HealthMeasureHistory.class)
				.setParameter("id", p.getIdPerson()).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    p.setHealthMeasureHistories(history);
	    Person.updatePerson(p);
	}
	
	public static HealthMeasureHistory updateHealthMeasureHistory(HealthMeasureHistory h){
		EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        h=em.merge(h);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return h;
	}
    
}