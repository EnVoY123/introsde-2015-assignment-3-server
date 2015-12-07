package introsde.assignment.soap;

import introsde.assignment.model.Measure;
import introsde.assignment.model.MeasureType;
import introsde.assignment.model.Person;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
// optional
public interface People {

	// Request #1:
	@WebMethod(operationName = "readPersonList")
	@WebResult(name = "people")
	public List<Person> readPersonList();

	// Request #2:
	@WebMethod(operationName = "readPerson")
	@WebResult(name = "person")
	public Person readPerson(@WebParam(name = "personId") long id);

	// Request #3:
	@WebMethod(operationName = "updatePerson")
	@WebResult(name = "uperson")
	public Person updatePerson(@WebParam(name = "person") Person person);

	// Request #4:
	@WebMethod(operationName = "createPerson")
	@WebResult(name = "crperson")
	public Person createPerson(@WebParam(name = "person") Person person);

	// Request #5:
	@WebMethod(operationName = "deletePerson")
	@WebResult(name = "dperson")
	public int deletePerson(@WebParam(name = "personId") long id);

	// Request #6:
	@WebMethod(operationName = "readPersonHistory")
	@WebResult(name = "healthProfile-history")
	public List<Measure> readPersonHistory(@WebParam(name = "personId") long id,
			@WebParam(name = "measureType") String measureType);

	// Request #7:
	@WebMethod(operationName = "readPersonMeasurement")
	@WebResult(name = "measure")
	public Measure readPersonMeasurement(@WebParam(name = "personId") long id,
			@WebParam(name = "measureType") String measureType,
			@WebParam(name = "mid") long mid);

	// Request #8:
	@WebMethod(operationName = "savePersonMeasurement")
	@WebResult(name = "smeasure")
	public Measure savePersonMeasurement(@WebParam(name = "personId") long id,
			@WebParam(name = "measure") Measure measure);

	// Request #9:
	@WebMethod(operationName = "readMeasureTypes")
	@WebResult(name = "measureType")
	public List<MeasureType> readMeasureTypes();

	// Request #10:
	@WebMethod(operationName = "updatePersonMeasure")
	@WebResult(name = "umeasure")
	public Measure updatePersonMeasure(@WebParam(name = "personId") long id,
			@WebParam(name = "measure") Measure m);

}