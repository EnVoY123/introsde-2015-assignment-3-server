package introsde.assignment.model;

import introsde.assignment.dao.HealthDiaryDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "Measure")
@NamedQuery(name = "Measure.findAll", query = "SELECT l FROM Measure l")
//@XmlRootElement
public class Measure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idMeasure")
	private Long mid;

	@Column(name = "value")
	private String measureValue;

	@Column(name = "created")
	private String dateRegistered;

	@Transient
	private String measureType;

	// @Transient
	// private String measureValue;

	@Transient
	private String measureValueType;

	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	@OneToOne
	@JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef", insertable = true, updatable = true)
	private MeasureType measureDefinition;

	public Measure() {
	}

	// @XmlElement(name = "measureValue")
	// public String getMeasureValue() {
	// measureValue = Double.toString(getValue());
	// return measureValue;
	// }
	//
	// public void setMeasureValue(String measureValue) {
	// this.measureValue = measureValue;
	// }

	@XmlElement(name = "measureValueType")
	public String getMeasureValueType() {
		return measureValueType;
	}

	public void setMeasureValueType(String measureValueType) {
		this.measureValueType = measureValueType;
	}

	@XmlElement(name = "measureType")
	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	@XmlTransient
	public MeasureType getMeasureDefinition() {
		return measureDefinition;
	}

	public void setMeasureDefinition(MeasureType param) {
		this.measureDefinition = param;
	}

	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@XmlElement(name = "mid")
	public Long getMeasureId() {
		return mid;
	}

	public void setMeasureId(Long id) {
		this.mid = id;
	}

	@XmlElement(name = "measureValue")
	public String getMeasureValue() {
		return measureValue;
	}

	public void setMeasureValue(String value) {
		this.measureValue = value;
	}

	@XmlElement(name = "dateRegistered")
	public String getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(String dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public static Measure getMeasureHistoryById(Long l) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		Measure p = em.find(Measure.class, l);
		HealthDiaryDao.instance.closeConnections(em);
		return p;
	}

	public static List<Measure> getAll() {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		List<Measure> list = em.createNamedQuery("Measure.findAll",
				Measure.class).getResultList();
		HealthDiaryDao.instance.closeConnections(em);
		return list;
	}

	@SuppressWarnings("unchecked")
	public static List<Measure> getAllMeasureHistoryOfPerson(int id,
			String measure) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		System.out.println("searching...");

		int i = 0;
		for (MeasureType mType : MeasureType.getAll()) {
			if (((MeasureType) mType).getMeasureName()
					.equalsIgnoreCase(measure)) {
				i = mType.getIdMeasureDef();
			}
		}
		List<Measure> list = (List<Measure>) em.createNativeQuery(
				"SELECT * FROM Measure WHERE idPerson=" + id
						+ " AND idMeasureDef=" + i + " ORDER BY created DESC",
				Measure.class).getResultList();
		for (Measure m : list) {
			m.setMeasureValueType(m.getMeasureDefinition().getMeasureType());
			m.setMeasureType(m.getMeasureDefinition().getMeasureName());
		}
		HealthDiaryDao.instance.closeConnections(em);
		return list;
	}

	@SuppressWarnings("unchecked")
	public static Measure getMeasureHistoryOfPerson(int id, String measure,
			Long l) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		System.out.println("searching...");

		int i = 0;
		for (MeasureType mType : MeasureType.getAll()) {
			if (((MeasureType) mType).getMeasureName()
					.equalsIgnoreCase(measure)) {
				i = mType.getIdMeasureDef();
			}
		}
		List<Measure> list = (List<Measure>) em.createNativeQuery(
				"SELECT * FROM Measure WHERE idPerson=" + id
						+ " AND idMeasureDef=" + i + " AND idMeasure=" + l,
				Measure.class).getResultList();
		HealthDiaryDao.instance.closeConnections(em);
		for (Measure m : list) {
			m.setMeasureValueType(m.getMeasureDefinition().getMeasureType());
			m.setMeasureType(m.getMeasureDefinition().getMeasureName());
		}
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	
	public static Measure saveMeasureHistory(Measure p) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		HealthDiaryDao.instance.closeConnections(em);
		return p;
	}

	public static Measure updateMeasureHistory(Measure p) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		tx.commit();
		HealthDiaryDao.instance.closeConnections(em);
		return p;
	}

	public static void removeMeasureHistory(Measure p) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		em.remove(p);
		tx.commit();
		HealthDiaryDao.instance.closeConnections(em);
	}
}
