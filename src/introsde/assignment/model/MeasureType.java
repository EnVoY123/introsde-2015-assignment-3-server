package introsde.assignment.model;

import introsde.assignment.dao.HealthDiaryDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "MeasureType")
@NamedQuery(name = "MeasureType.findAll", query = "SELECT m FROM MeasureType m")
//@XmlRootElement
public class MeasureType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "idMeasureDef")
	private int idMeasureDef;

	@Column(name = "measureName")
	private String measureName;

	@Column(name = "measureType")
	private String measureType;

	public MeasureType() {
	}

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

	public String getMeasureType() {
		return this.measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	public static MeasureType getMeasureDefinitionById(int id) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		MeasureType p = em.find(MeasureType.class, id);
		HealthDiaryDao.instance.closeConnections(em);
		return p;
	}

	public static MeasureType getMeasureDefinitionByName(String name) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		MeasureType measureType = null;
		for (MeasureType m : getAll()) {
			if (m.getMeasureName().equalsIgnoreCase(name))
				measureType = m;
		}
		HealthDiaryDao.instance.closeConnections(em);
		return measureType;
	}

	public static List<MeasureType> getAll() {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		List<MeasureType> list = em.createNamedQuery("MeasureType.findAll",
				MeasureType.class).getResultList();
		HealthDiaryDao.instance.closeConnections(em);
		return list;
	}

	public static List<String> getAllNames() {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		List<String> list = new ArrayList<String>();
		for (MeasureType p : getAll()) {
			list.add(p.measureName);
		}
		HealthDiaryDao.instance.closeConnections(em);
		return list;
	}

	public static MeasureType saveMeasureDefinition(MeasureType p) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		HealthDiaryDao.instance.closeConnections(em);
		return p;
	}

	public static MeasureType updateMeasureDefinition(MeasureType p) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		tx.commit();
		HealthDiaryDao.instance.closeConnections(em);
		return p;
	}

	public static void removeMeasureDefinition(MeasureType p) {
		EntityManager em = HealthDiaryDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		em.remove(p);
		tx.commit();
		HealthDiaryDao.instance.closeConnections(em);
	}
}
