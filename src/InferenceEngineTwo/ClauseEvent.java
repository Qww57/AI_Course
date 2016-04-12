package InferenceEngineTwo;

public class ClauseEvent {
		
	public ClauseEvent(boolean bool, Event event){
		this.event = event;
		this.condition = bool;
	}
	
	private Event event;
	
	private boolean condition;

	public synchronized Event getEvent() {
		return event;
	}

	public synchronized void setEvent(Event event) {
		this.event = event;
	}

	public synchronized boolean isCondition() {
		return condition;
	}

	public synchronized void setCondition(boolean condition) {
		this.condition = condition;
	}

}