package introsde.assignment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name="currentHealth")
public class CurrentHealth implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "measure")
	List<Measure> measureType = new ArrayList<Measure>();
	
	public void add(Measure x){
		this.measureType.add(x);
	}
	
	public CurrentHealth(){
		
	}
	
	public List<Measure> get(){
		return measureType;
	}
	
	public void set(List<Measure> measureTypes){
		this.measureType = measureTypes;
	}
}
