package introsde.rest.ehealth.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import introsde.rest.ehealth.dao.LifeCoachDao;

import java.util.List;


/**
 * The persistent class for the "MeasureDefinition" database table.
 * 
 */
@Entity
@Table(name="\"MeasureDefinition\"")
@NamedQuery(name="MeasureDefinition.findAll", query="SELECT m FROM MeasureDefinition m")
@XmlRootElement
public class MeasureDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_measuredef")
	@TableGenerator(name="sqlite_measuredef", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="MeasureDefinition")
	@Column(name="\"idMeasureDef\"")
	private int idMeasureDef;

	@Column(name="\"measureName\"")
	private String measureName;

	@Column(name="\"measureType\"")
	private String measureType;

	//bi-directional many-to-one association to HealthMeasureHistory
	@OneToMany(mappedBy="measureDefinition")
	private List<HealthMeasureHistory> healthMeasureHistories;

	//bi-directional many-to-many association to Person
	@OneToMany(mappedBy="measureDefinition")
	private List<LifeStatus> lifestatus;
	
	public MeasureDefinition() {
	}
	
	// the GETTERS and SETTERS of all the private attributes

	
	public int getIdMeasureDef() {
		return this.idMeasureDef;
	}

	public void setIdMeasureDef(int idMeasureDef) {
		this.idMeasureDef = idMeasureDef;
	}

	public String getMeasureName() {
		return this.measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
	@XmlTransient
	public String getMeasureType() {
		return this.measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	@XmlTransient
	public List<HealthMeasureHistory> getHealthMeasureHistories() {
		return this.healthMeasureHistories;
	}

	public void setHealthMeasureHistories(List<HealthMeasureHistory> healthMeasureHistories) {
		this.healthMeasureHistories = healthMeasureHistories;
	}

	public HealthMeasureHistory addHealthMeasureHistory(HealthMeasureHistory healthMeasureHistory) {
		getHealthMeasureHistories().add(healthMeasureHistory);
		healthMeasureHistory.setMeasureDefinition(this);

		return healthMeasureHistory;
	}

	public HealthMeasureHistory removeHealthMeasureHistory(HealthMeasureHistory healthMeasureHistory) {
		getHealthMeasureHistories().remove(healthMeasureHistory);
		healthMeasureHistory.setMeasureDefinition(null);

		return healthMeasureHistory;
	}

	@XmlTransient
	public List<LifeStatus> getLifestatus() {
		return lifestatus;
	}

	public void setLifestatus(List<LifeStatus> lifestatus) {
		this.lifestatus = lifestatus;
	}
	
	public static List<MeasureDefinition> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<MeasureDefinition> list = em.createNamedQuery("MeasureDefinition.findAll", MeasureDefinition.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static MeasureDefinition getByType(String type){
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		MeasureDefinition md = em.createQuery("SELECT m FROM MeasureDefinition m WHERE m.measureName=:type", MeasureDefinition.class)
				.setParameter("type", type).getSingleResult();
	    LifeCoachDao.instance.closeConnections(em);
	    
        return md;
	}


}