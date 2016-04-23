package InferenceEngine;

/**
 * An event is a fact. An event is something that is composed of a
 * string describing it and a status. The status of an event can be
 * either "true" or "false", but since we might not know its status, 
 * its status can also be "unkown".
 * 
 * @author Quentin
 *
 */
public class Event {

	/** Class members **/
	
	public enum Status {
		TRUE,
		FALSE,
		UNKWON
	}

	private String name;
	
	private Status status;

	/**
	 * Constructor of an event. By default the event status is set 
	 * to unknown. 
	 * 
	 * @param name - name of the event
	 */
	public Event(String name) {
		this.name = name;
		this.status = Status.UNKWON;
	}
	
	/** Getters and setters **/
	
	public String getName() {
		return name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String toString(){
		return "Event: " + name + " is " + status.toString();
	}
	
	public void print(){
		System.out.println(toString());
	}
}