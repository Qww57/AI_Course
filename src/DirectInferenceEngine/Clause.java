package DirectInferenceEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Clause} is the representation of an implication as a clausal form.
 * It means that each clause is composed of a set of {@link Event}s linked 
 * together by or relations. Since some of the basic {@link Event}s can be denied, 
 * the clause is composed of {@link ClauseEvent} a structure that enable the 
 * negation of one {@link Event}.
 * 
 * Since a clause is, at first, an implication (for instance "a and no b implies c",
 * when translating it to a clausal form, we have then "no a or b or c").
 * We can so split the {@link ClauseEvent} composing it in two different types.
 * On the one hand, the conditional ClauseEvents (here, "no a" and "b") and the 
 * conclusion event (here "c").
 * 
 * An interesting thing with clause is that, in order to solve the clause (which 
 * means to have its ClauseEvent), we only need to have one of the {@link ClauseEvent}
 * satisfied.
 * 
 * @author Quentin
 *
 */
public class Clause {
	
	/** Class members **/

	private String clauseID;

	private List<ClauseEvent> events;
	
	private ClauseEvent conclusion;
	
	/** Constructors **/
	
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

	/** Getters and setters **/
	
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
		if (conclusion.getValue() == false)
			condition += "no ";		
		condition += conclusion.getEvent().getName();
			
		return clauseID + " - " + condition;
	}
	
	public void print(){
		System.out.println(toString());
	}
}
