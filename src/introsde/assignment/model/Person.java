package introsde.assignment.model;

import introsde.assignment.dao.HealthDiaryDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table(name = "Person")
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
//@XmlRootElement(name = "person")
@XmlType(propOrder = {"personId", "firstname", "lastname", "birthdate", "currentHealth" })
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idPerson")
	private Long id;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "birthdate")
	private String birthdate;

	@Transient
	private CurrentHealth currentHealth;

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Measure> healthHistory;

	public Person() {
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@XmlElement(name="currentHealth")
	public CurrentHealth getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(CurrentHealth currentHealth) {
		this.currentHealth = currentHealth;
	}
	
	@XmlTransient
//	@XmlElement(name="healthHistory")
	public List<Measure> getHealthHistory() {
		return healthHistory;
	}

	public void setHealthHistory(List<Measure> history) {
		this.healthHistory = history;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public Long getPersonId() {
		return id;
	}

	public void setPersonId(Long id) {
		this.id = id;
	}

	public static Person getPersonById(Long m) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		Person p = em.find(Person.class, m);
		HealthDiaryDao.instance.closeConnections(em);
		return p;
	}

	public static List<Person> getAll() {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		System.out.println("--> Querying the database for all the people...");
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
				.getResultList();
		System.out.println("--> Closing connections of entity manager...");
		HealthDiaryDao.instance.closeConnections(em);
		return list;
	}

	public static List<String> getAllNames() {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		System.out.println("--> Querying the database for all people names...");
		List<Person> people = em.createNamedQuery("Person.findAll",
				Person.class).getResultList();
		List<String> list = new ArrayList<String>();
		for (Person p : people) {
			list.add(p.firstname);
		}
		System.out.println("--> Closing connections of entity manager...");
		HealthDiaryDao.instance.closeConnections(em);
		return list;
	}

	public static Person savePerson(Person p) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		HealthDiaryDao.instance.closeConnections(em);
		return p;
	}

	public static Person updatePerson(Person p) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		tx.commit();
		HealthDiaryDao.instance.closeConnections(em);
		return p;
	}

	public static void removePerson(Person p) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		em.remove(p);
		tx.commit();
		HealthDiaryDao.instance.closeConnections(em);
	}

	
	public static void getDetails(Person p){
		List<Measure> mHistory = p.getHealthHistory();
		CurrentHealth currentHealth = new CurrentHealth();

		boolean weightAdded = false;
		boolean heightAdded = false;
		boolean stepsAdded = false;
		boolean bmiAdded = false;
		boolean hrAdded = false;
		boolean bpAdded = false;

		for (int i = mHistory.size() - 1; i > 0; i--) {
			Measure measure = mHistory.get(i);
			measure.setMeasureValueType(measure.getMeasureDefinition().getMeasureType());
			measure.setMeasureType(measure.getMeasureDefinition().getMeasureName());
			if (((Measure) measure).getMeasureDefinition().getMeasureName()
					.equalsIgnoreCase("weight")
					&& !weightAdded) {
				currentHealth.add(measure);
				weightAdded = true;
			}
			if (((Measure) measure).getMeasureDefinition().getMeasureName()
					.equalsIgnoreCase("height")
					&& !heightAdded) {
				currentHealth.add(measure);
				heightAdded = true;
			}
			if (((Measure) measure).getMeasureDefinition().getMeasureName()
					.equalsIgnoreCase("steps")
					&& !stepsAdded) {
				currentHealth.add(measure);
				stepsAdded = true;
			}
			if (((Measure) measure).getMeasureDefinition().getMeasureName()
					.equalsIgnoreCase("bmi")
					&& !bmiAdded) {
				currentHealth.add(measure);
				bmiAdded = true;
			}
			if (((Measure) measure).getMeasureDefinition().getMeasureName()
					.equalsIgnoreCase("bloodpressure")
					&& !bpAdded) {
				currentHealth.add(measure);
				bpAdded = true;
			}
			if (((Measure) measure).getMeasureDefinition().getMeasureName()
					.equalsIgnoreCase("heartrate")
					&& !hrAdded) {
				currentHealth.add(measure);
				hrAdded = true;
			}
		}

		p.setCurrentHealth(currentHealth);
	}
}
