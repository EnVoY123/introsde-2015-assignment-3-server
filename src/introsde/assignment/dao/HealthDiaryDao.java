package introsde.assignment.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import introsde.assignment.model.Person;

public enum HealthDiaryDao {
	instance;
	private EntityManagerFactory emf;
	
	private HealthDiaryDao() {
		if (emf!=null) {
			emf.close();
		}
		emf = Persistence.createEntityManagerFactory("Assignment03");
	}
	
	public EntityManager createEntityManager() {
		try {
			return emf.createEntityManager();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;    
	}

	public void closeConnections(EntityManager em) {
		em.close();
	}

	public EntityTransaction getTransaction(EntityManager em) {
		return em.getTransaction();
	}
	
	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}
	
	public static Person getPersonById(Long personId) {
		EntityManager em = instance.createEntityManager();
		Person p = em.find(Person.class, personId);
		instance.closeConnections(em);
		return p;
	}
	
	public static List<Person> getAll() {
		EntityManager em = instance.createEntityManager();
	    List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
	    instance.closeConnections(em);
	    return list;
	}

}
