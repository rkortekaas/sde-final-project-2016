package introsde.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.dao.LifeCoachDao;
import introsde.model.LifeStatus;

@Entity // indicates that this class is an entity to persist in DB
@Table(name="Person") // to what table must be persisted
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id // defines this attribute as the one that identifies the entity
	@GeneratedValue(generator="sqlite_person")
	@TableGenerator(name="sqlite_person", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="Person")
	@Column(name="idPerson")
	private int idPerson;
	@Column(name="name")
	private String name;
	@Column(name="lastname")
	private String lastname;
	@Column(name="username")
	private String username;
	@Temporal(TemporalType.DATE) // defines the precision of the date attribute
	@Column(name="birthdate")
	private Date birthdate;
	@Column(name="email")
	private String email;
	
	public int getIdPerson() {
		return idPerson;
	}
	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<LifeStatus> lifeStatus;
	
	@XmlElementWrapper(name = "healthProfile")
	@XmlElement(name="Measure")
	public List<LifeStatus> getLifeStatus() {
		return lifeStatus;
	}
	
	// querying the database
	
	public static Person getPersonById(int personId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		Person p = em.find(Person.class, personId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<Person> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
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