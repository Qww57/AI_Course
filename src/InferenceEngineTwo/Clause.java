package InferenceEngineTwo;

import java.util.List;

public class Clause {

	private String clauseID;
	
	private List<ClauseEvent> events;
	
	public Clause(String clauseID, List<ClauseEvent> events) {
		super();
		this.clauseID = clauseID;
		this.events = events;
	}

	public synchronized String getClauseID() {
		return clauseID;
	}

	public synchronized void setClauseID(String clauseID) {
		this.clauseID = clauseID;
	}

	public synchronized List<ClauseEvent> getEvents() {
		return events;
	}

	public synchronized void setEvents(List<ClauseEvent> events) {
		this.events = events;
	}
	
	public String toString(){
		String condition = "";
		if (events.size() != 0){
			for (int i = 0; i < events.size(); i++){
				if (i < events.size()- 1){
					if (events.get(i).isCondition() == false)
						condition += "no ";
					condition += events.get(i).getEvent().getName() + " or ";
				}
				else{ 
					if (events.get(i).isCondition() == false)
						condition += "no ";
					condition += events.get(i).getEvent().getName();
				}
			}
		}
			
		return clauseID + " - " + condition;
	}
	
	public void print(){
		System.out.println(toString());
	}
}
