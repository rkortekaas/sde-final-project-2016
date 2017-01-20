package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.LifeCoachDao;
import introsde.rest.ehealth.model.Person;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="\"Activity\"")
@NamedQuery(name="Activity.findAll", query="SELECT a FROM Activity a")
@XmlRootElement
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_mhistory")
	@TableGenerator(name="sqlite_mhistory", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="Activity")
	@Column(name="\"idActivity\"")
	private int idActivity;

	@Column(name="\"name\"")
	private String name;
	
	@Column(name="\"type\"")
	private String type;
	
	@Column(name="\"area\"")
	private String area;
	
	@Column(name="\"description\"")
	private String description;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"date\"")
	private Date date;

	@ManyToOne
	@JoinColumn(name = "\"idPerson\"", referencedColumnName = "\"idPerson\"")
	private Person person;

	public Activity() {
	}

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(this.date);
	}
	
    public void setDate(String da) throws ParseException{
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = format.parse(da);
        this.date = date;
    }

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	// db operations
	
    public static Activity saveActivity(Activity a) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(a);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return a;
    } 

    public static Activity updateActivity(Activity a) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        a=em.merge(a);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return a;
    }

    public static void removeActivity(Activity a) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        a=em.merge(a);
        em.remove(a);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
    public static List<Activity> getAll() {
    	EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Activity> list = em.createNamedQuery("Activity.findAll", Activity.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }
}