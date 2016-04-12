package InferenceEngineTwo;

public class Event {

	enum Status {
		TRUE,
		FALSE,
		UNKWON
	}
	
	private String name;
	
	private Status status;

	public Event(String name) {
		this.name = name;
		this.status = Status.UNKWON;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void print(){
		System.out.println("Event: " + name + " is " + status.toString());
	}
}

