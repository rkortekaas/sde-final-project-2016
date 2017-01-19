package introsde.rest.ehealth.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import introsde.rest.ehealth.dao.LifeCoachDao;
@Entity  // indicates that this class is an entity to persist in DB
@Table(name="\"LifeStatus\"") // to whate table must be persisted

@NamedQuery(name="LifeStatus.findAll", query="SELECT p FROM LifeStatus p")
@XmlRootElement
public class LifeStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    // @GeneratedValue(strategy=GenerationType.AUTO) 
    @GeneratedValue(generator="sqlite_LifeStatus")
    @TableGenerator(name="sqlite_LifeStatus", table="sqlite_sequence",
        pkColumnName="name", valueColumnName="seq",
        pkColumnValue="LifeStatus")
    @Column(name="\"idMeasure\"") // maps the following attribute to a column
    private int idMeasure;
    
    
    @ManyToOne
    @JoinColumn(name = "\"idMeasureDef\"", referencedColumnName = "\"idMeasureDef\"", insertable = true, updatable = true)
    private MeasureDefinition measureDefinition;
    
    @ManyToOne
	@JoinColumn(name="\"idPerson\"",referencedColumnName="\"idPerson\"")
    private Person person;
    
    @Column(name="\"value\"")
    private String value;
    
    // the GETTERS and SETTERS of all the private attributes
    @XmlTransient
    public int getIdMeasure() {
		return idMeasure;
	}

	public void setIdMeasure(int idMeasure) {
		this.idMeasure = idMeasure;
	}
	
	//@XmlTransient
	public MeasureDefinition getMeasureDef() {
		return measureDefinition;
	}
	@XmlElement(name = "measureName")
	public String getMeasureDefName() {
		return measureDefinition.getMeasureName();
	}

	public void setMeasureDef(MeasureDefinition MeasureDef) {
		this.measureDefinition = MeasureDef;
	}

	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	// QUERYING TO THE DATABASE
	
	public static LifeStatus getLifeStatusById(int lifestatusId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        LifeStatus p = em.find(LifeStatus.class, lifestatusId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static List<LifeStatus> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<LifeStatus> list = em.createNamedQuery("LifeStatus.findAll", LifeStatus.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static LifeStatus saveLifeStatus(LifeStatus ls) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(ls);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return ls;
    } 

    public static LifeStatus updateLifeStatus(LifeStatus ls) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        ls=em.merge(ls);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return ls;
    }

    public static void removeLifeStatus(LifeStatus ls) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        ls=em.merge(ls);
        em.remove(ls);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
	public static LifeStatus getLastLifeStatus(int id, String type){
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<LifeStatus> ls = em
				.createQuery("SELECT l FROM LifeStatus l WHERE l.measureDefinition.measureName = :type and l.person.idPerson = :id", LifeStatus.class)
				.setParameter("type", type)
				.setParameter("id", id).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    if (ls.isEmpty()){
	    	return null;
	    }
        return ls.get(0);
	}
	
	public static void refreshLifeStatus(Person p){
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<LifeStatus> ls = em
				.createQuery("SELECT l FROM LifeStatus l WHERE l.person.idPerson = :id", LifeStatus.class)
				.setParameter("id", p.getIdPerson()).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    p.setLifeStatus(ls);
	    Person.updatePerson(p);
	}

}