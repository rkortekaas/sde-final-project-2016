package introsde.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import introsde.dao.LifeCoachDao;

@Entity // indicates that this class is an entity to persist in DB
@Table(name="LifeStatus") // to what table must be persisted
@NamedQuery(name="LifeStatus.findAll", query="SELECT l FROM LifeStatus l") // query is executed for getting all lifestatus's
@XmlRootElement(name="Measure")
public class LifeStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="sqlite_lifestatus")
	@TableGenerator(name="sqlite_lifestatus", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="LifeStatus")
	@Column(name="idMeasure")
	private int idMeasure;
	
	@ManyToOne
	@JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef", insertable = true, updatable = true)
	private MeasureDefinition measureDefinition;
	
	@ManyToOne
	@JoinColumn(name="idPerson",referencedColumnName="idPerson")
	private Person person;
	
	@Column(name="value")
	private Double value;
	
	public LifeStatus() {
	}
	
	 @XmlTransient
	public int getIdMeasure() {
		return this.idMeasure;
	}

	public void setIdMeasure(int idMeasure) {
		this.idMeasure = idMeasure;
	}

	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@XmlElement(name = "measureDef")
	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition param) {
		this.measureDefinition = param;
	}
	
	// we make this transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient 
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	// querying the database
	
	public static LifeStatus getLifeStatusById(int lifestatusId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		LifeStatus p = em.find(LifeStatus.class, lifestatusId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<LifeStatus> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<LifeStatus> list = em.createNamedQuery("LifeStatus.findAll", LifeStatus.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static LifeStatus saveLifeStatus(LifeStatus p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static LifeStatus updateLifeStatus(LifeStatus p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removeLifeStatus(LifeStatus p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}
}
