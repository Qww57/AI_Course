package InferenceEngineTwo;

import java.util.ArrayList;
import java.util.List;

/**
 * If events at least one clause event from the condition events is satisfied
 * then the conclusion event is obtained.
 * 
 * @author Quentin
 *
 */
public class Clause {

	private String clauseID;

	private List<ClauseEvent> events;
	
	private ClauseEvent conclusion;
	
	public Clause(String clauseID, List<ClauseEvent> events, ClauseEvent conclusion) {
		super();
		this.clauseID = clauseID;
		this.events = events;
		this.conclusion = conclusion;
	}
	
	public Clause(String clauseID, ClauseEvent conclusion) {
		super();
		this.clauseID = clauseID;
		this.events = new ArrayList<ClauseEvent>();
		this.conclusion = conclusion;
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

	public ClauseEvent getConclusion() {
		return conclusion;
	}

	public void setConclusion(ClauseEvent conclusion) {
		this.conclusion = conclusion;
	}
	
	public String toString(){
		String condition = "";
		if (events.size() != 0){
			for (int i = 0; i < events.size(); i++){
				if (i < events.size()){
					if (events.get(i).getValue() == false)
						condition += "no ";
					condition += events.get(i).getEvent().getName() + " or ";
				}
			}
		}
		condition += conclusion.getEvent().getName();
			
		return clauseID + " - " + condition;
	}
	
	public void print(){
		System.out.println(toString());
	}
}
