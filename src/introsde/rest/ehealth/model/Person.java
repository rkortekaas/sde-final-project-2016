package introsde.rest.ehealth.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import introsde.rest.ehealth.dao.LifeCoachDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the "Person" database table.
 * 
 */
@Entity
@Table(name="\"Person\"")
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="\"lastname\"")
	private String lastname;

	@Column(name="\"name\"")
	private String name;

	@Temporal(TemporalType.DATE)
	@Column(name="\"birthdate\"")
	private Date birthdate;

	@Column(name="\"email\"")
	private String email;
	
	@Column(name="\"username\"")
	private String username;

	@Id
	@GeneratedValue(generator="sqlite_person")
    @TableGenerator(name="sqlite_person", table="sqlite_sequence",
        pkColumnName="name", valueColumnName="seq",
        pkColumnValue="Person")
	@Column(name="\"idPerson\"")
	private int idPerson;

	

	//bi-directional many-to-one association to HealthMeasureHistory
	@OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<HealthMeasureHistory> healthMeasureHistories;

	//bi-directional many-to-many association to MeasureDefinition
	@OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private List<LifeStatus> lifeStatus;

	public Person() {
	}
	
	// the GETTERS and SETTERS of all the private attributes
    
    public String getBirthdate(){
    	if(this.birthdate == null) {
    	      return null;
    	}
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(this.birthdate);
    }

    public void setBirthdate(String bd) throws ParseException{
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = format.parse(bd);
        this.birthdate = date;
    }

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIdPerson() {
		return this.idPerson;
	}

	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

//	@XmlElementWrapper(name = "measureHistory")
//	@XmlElement(name = "measure")
	@XmlTransient
	public List<HealthMeasureHistory> getHealthMeasureHistories() {
		return this.healthMeasureHistories;
	}

	public void setHealthMeasureHistories(List<HealthMeasureHistory> healthMeasureHistories) {
		this.healthMeasureHistories = healthMeasureHistories;
	}

	public HealthMeasureHistory addHealthMeasureHistory(HealthMeasureHistory healthMeasureHistory) {
		getHealthMeasureHistories().add(healthMeasureHistory);
		healthMeasureHistory.setPerson(this);

		return healthMeasureHistory;
	}

	public HealthMeasureHistory removeHealthMeasureHistory(HealthMeasureHistory healthMeasureHistory) {
		getHealthMeasureHistories().remove(healthMeasureHistory);
		healthMeasureHistory.setPerson(null);

		return healthMeasureHistory;
	}
	
	@XmlElementWrapper(name = "healthProfile")
	@XmlElement(name = "measureType")
	public List<LifeStatus> getLifeStatus() {
        return lifeStatus;
	}


	public void setLifeStatus(List<LifeStatus> lifeStatus) {
		this.lifeStatus = lifeStatus;
	}
	
	public static Person getPersonById(int personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        Person p = em.find(Person.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static List<Person> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }
    
    public static List<Person> getWithRange(String type, int min, int max){
    	if (max==0) max=100000;
    	EntityManager em = LifeCoachDao.instance.createEntityManager();
    	List<LifeStatus> list = em
				.createQuery("SELECT p FROM LifeStatus p WHERE p.measureDefinition.measureName = :type and CAST(p.value as decimal) >= :min"
						+ " and CAST(p.value as decimal) <= :max", LifeStatus.class)
				.setParameter("type", type)
				.setParameter("min", min)
				.setParameter("max", max).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    System.out.println(list.size());
	    List<Person> p = new ArrayList<>();
	    for (int i = 0; i<list.size();i++){
	    	p.add(list.get(i).getPerson());
	    }
	    System.out.println(p.size());
    	return p;
    }

    public static Person savePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        for(int i = 0;i<p.getLifeStatus().size();i++){
        	p.getLifeStatus().get(i).setPerson(p);
        	LifeStatus.updateLifeStatus(p.getLifeStatus().get(i));
        }
        
        return p;
    } 

    public static Person updatePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
    

}