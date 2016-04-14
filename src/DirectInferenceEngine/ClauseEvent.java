package DirectInferenceEngine;

public class ClauseEvent {
		
	public ClauseEvent(boolean bool, Event event){
		this.event = event;
		this.value = bool;
	}
	
	private Event event;
	
	private boolean value;

	public synchronized Event getEvent() {
		return event;
	}

	public synchronized void setEvent(Event event) {
		this.event = event;
	}

	public synchronized boolean getValue() {
		return value;
	}

	public synchronized void setValue(boolean condition) {
		this.value = condition;
	}

}