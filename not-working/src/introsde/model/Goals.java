package introsde.model;

import introsde.dao.LifeCoachDao;
import introsde.model.Person;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="Goals")
@NamedQuery(name="Goals.findAll", query="SELECT g FROM Goals h")
@XmlRootElement
public class Goals implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_mhistory")
	@TableGenerator(name="sqlite_mhistory", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="Goals")
	@Column(name="idGoal")
	private int idGoal;
	
	@Column(name="name")
	private String name;
	
	@Column(name="type")
	private String type;
	
	@Column(name="description")
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name="deadline")
	private Date deadline;

	// notice that we haven't included a reference to the history in Person
	// this means that we don't have to make this attribute XmlTransient
	
	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	public Goals() {
	}

	public int getIdGoal() {
		return this.idGoal;
	}

	public void setIdGoal(int idGoal) {
		this.idGoal = idGoal;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(Date date) {
		this.deadline = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Person getPerson() {
	    return person;
	}

	public void setPerson(Person param) {
	    this.person = param;
	}

	// database operations

}