package InferenceEngine;

/**
 * A clause event is a class used in order to describe a
 * {@link Clause}. 
 * A {@link ClauseEvent} is basically an {@link Event} related to a boolean 
 * "negate" which describes if the {@link Event} is denied or 
 * not inside the {@link Clause}.
 * 
 * @author Quentin
 *
 */
public class ClauseEvent {
	
	/** Class members **/
	
	private Event event;
	
	private boolean value;
	
	/** Constructor **/
	
	public ClauseEvent(boolean bool, Event event){
		this.event = event;
		this.value = bool;
	}

	/** Getters and setters **/
	
	public synchronized Event getEvent() {
		return event;
	}

	public synchronized boolean getValue() {
		return value;
	}

	public synchronized void setValue(boolean condition) {
		this.value = condition;
	}
}