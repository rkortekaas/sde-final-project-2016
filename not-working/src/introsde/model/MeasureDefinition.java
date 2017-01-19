package introsde.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import introsde.dao.LifeCoachDao;
import introsde.model.LifeStatus;

@Entity
@Table(name="MeasureDefinition")
@NamedQuery(name="MeasureDefinition.findAll", query="SELECT m FROM MeasureDefinition m")
@XmlRootElement
public class MeasureDefinition implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="sqlite_measuredef")
	@TableGenerator(name="sqlite_measuredef", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="MeasureDefinition")
	private int idMeasureDef;
	@Column(name="measureName")
	private String measureName;
	@Column(name="measureType")
	private String measureType;
	
	public MeasureDefinition() {
	}
	
	public int getIdMeasureDef() {
		return idMeasureDef;
	}
	public void setIdMeasureDef(int idMeasureDef) {
		this.idMeasureDef = idMeasureDef;
	}
	
	public String getMeasureName() {
		return measureName;
	}
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
	public String getMeasureType() {
		return measureType;
	}
	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}
	
	@OneToMany(mappedBy="measureDefinition")
	private List<LifeStatus> lifestatus;
	
	@XmlTransient
	public List<LifeStatus> getLifestatus() {
		return lifestatus;
	}

	public void setLifestatus(List<LifeStatus> lifestatus) {
		this.lifestatus = lifestatus;
	}
	
	// querying the database
	
	public static MeasureDefinition getMeasureDefinitionById(int personId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		MeasureDefinition p = em.find(MeasureDefinition.class, personId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<MeasureDefinition> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<MeasureDefinition> list = em.createNamedQuery("MeasureDefinition.findAll", MeasureDefinition.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static MeasureDefinition saveMeasureDefinition(MeasureDefinition p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static MeasureDefinition updateMeasureDefinition(MeasureDefinition p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removeMeasureDefinition(MeasureDefinition p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}
	
}
