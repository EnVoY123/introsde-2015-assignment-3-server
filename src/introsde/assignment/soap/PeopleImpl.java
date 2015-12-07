package introsde.assignment.soap;

import introsde.assignment.model.CurrentHealth;
import introsde.assignment.model.Measure;
import introsde.assignment.model.MeasureType;
import introsde.assignment.model.Person;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "introsde.assignment.soap.People", serviceName = "PeopleService")
public class PeopleImpl implements People {

	// Request #1:
	@Override
	public List<Person> readPersonList() {
		System.out.println("Getting list of people...");

		List<Person> people = Person.getAll();

		for (Person person : people) {
			Person.getDetails(person);
		}

		return people;
	}

	// Request #2:
	@Override
	public Person readPerson(long id) {
		System.out.println("---> Reading Person by id = " + id);
		Person p = Person.getPersonById(id);
		if (p != null) {
			System.out.println("---> Found Person by id = " + id + " => "
					+ p.getFirstname());
		} else {
			System.out.println("---> Didn't find any Person with  id = " + id);
		}
		Person.getDetails(p);
		return p;
	}

	// Request #3:
	@Override
	public Person updatePerson(Person person) {
		if (person != null) {
			Person p = Person.updatePerson(person);
			return p;
		}
		return null;
	}

	// Request #4:
	@Override
	public Person createPerson(Person person) {
		if (person != null) {
			Person p = Person.savePerson(person);
			CurrentHealth ch = p.getCurrentHealth();
			if (ch != null) {
				System.out.println(ch.toString());
				for (Measure m : ch.get()) {
					m.setPerson(p);
					m.setMeasureDefinition(MeasureType
							.getMeasureDefinitionByName(m.getMeasureType()));
					if (m.getDateRegistered() == null) {
						DateFormat dateFormat = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date date = new Date();
						m.setDateRegistered(dateFormat.format(date));
					}
					Measure mNew = Measure.saveMeasureHistory(m);
					mNew.setMeasureValueType(m.getMeasureDefinition()
							.getMeasureType());
					mNew.setMeasureType(m.getMeasureDefinition()
							.getMeasureName());
				}
			}
			System.out.println(person.getPersonId() + "is created in database");
			return p;
		}
		return null;
	}

	// Request #5:
	@Override
	public int deletePerson(long id) {
		if (id != 0) {
			Person.removePerson(Person.getPersonById(id));
			System.out.println("Person with id " + id + "is deleted");
			return (int) id;
		}
		return -1;
	}

	// Request #6:
	@Override
	public List<Measure> readPersonHistory(long id, String measureType) {
		Person p = Person.getPersonById(id);
		if (p != null) {
			List<Measure> measureHistory = Measure
					.getAllMeasureHistoryOfPerson((int) id, measureType);
			System.out.println("MeasureHistory: " + measureHistory.toString());
			return measureHistory;
		}
		return null;
	}

	// Request #7:
	@Override
	public Measure readPersonMeasurement(long id, String measureType, long l) {
		Measure measureHistory = Measure.getMeasureHistoryOfPerson((int) id,
				measureType, l);
		if (measureHistory != null) {
			System.out.println("MeasureHistory: " + measureHistory.toString());
			return measureHistory;
		}
		return null;
	}

	// Request #8:
	@Override
	public Measure savePersonMeasurement(long id, Measure measure) {
		measure.setPerson(Person.getPersonById((long) id));
		measure.setMeasureDefinition(MeasureType
				.getMeasureDefinitionByName(measure.getMeasureType()));
		if (measure.getDateRegistered() == null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			measure.setDateRegistered(dateFormat.format(date));
		}
		Measure m = Measure.saveMeasureHistory(measure);
		m.setMeasureValueType(m.getMeasureDefinition().getMeasureType());
		m.setMeasureType(m.getMeasureDefinition().getMeasureName());
		return m;
	}

	// Request #9:
	@Override
	public List<MeasureType> readMeasureTypes() {
		System.out.println("Getting list of measure definition...");
		List<MeasureType> list = MeasureType.getAll();
		System.out.println("Measures: " + list.toString());
		return list;
	}

	// Request #10:
	@Override
	public Measure updatePersonMeasure(long id, Measure m) {
		Measure existing = Measure.getMeasureHistoryById(m.getMeasureId());
		// Measure temp = readPersonMeasurement(id, m.getMeasureDefinition()
		// .getMeasureName(), m.getMeasureId());
		if (existing == null) {
			return null;
			// } else if (temp == null) {
			// System.out.println("PUT: Person with id " + id
			// + " dont had measure with id " + m.getMeasureId());
			// return null;
		} else {
			m.setPerson(Person.getPersonById(id));
			m.setMeasureDefinition(MeasureType.getMeasureDefinitionByName(m
					.getMeasureType()));
			Measure measure = Measure.updateMeasureHistory(m);
			measure.setMeasureValueType(measure.getMeasureDefinition()
					.getMeasureType());
			measure.setMeasureType(measure.getMeasureDefinition()
					.getMeasureName());
			return measure;
		}
	}
}