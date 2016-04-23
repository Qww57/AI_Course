package InferenceEngine;

import java.util.ArrayList;
import java.util.List;

import AbstractAStar.AbstractNode;

/**
 * TODO Class that should be used in order to read and create knowledge bases 
 * from text file.
 * 
 * @author Quentin
 *
 */
@SuppressWarnings("unused")
public class KnowledgeBase {

	private List<Clause> _clauses;
	private static int _id;
	
	public KnowledgeBase(){	
		_id = 0;
	}
	
	/** 
	 * TODO Should read from text file
	 * 
	 * @param path
	 */
	public void readFromFile(String path){
		// not implemented
	}
	
	/**
	 * Should simplify all the clauses
	 */
	public void simplifyKnowledgeBase(){
		for (int i = 0; i < _clauses.size(); i++){
			simplifyClause(_clauses.get(i));
		}
	}
	
	/**
	 * Simplify the clause which have many times the same event with 
	 * the same status (for instance, a or a => a) and  which have many times 
	 * the same event with different status (for instance, b or a or no a => b)
	 * 
	 * @param clause
	 */
	private static void simplifyClause(Clause clause) {
		List<ClauseEvent> startingEvents = clause.getEvents();
		List<ClauseEvent> toDelete = new ArrayList<ClauseEvent>();
		
		// Checking for "a or no a" or " a or a" inside the events
		for (int i = 0; i < startingEvents.size(); i ++){
			for (int j = 0; j < startingEvents.size(); j++){
				// Checking for "a or a"
				if (startingEvents.get(i).getEvent().getName() == startingEvents.get(j).getEvent().getName() 
						&& startingEvents.get(i).getEvent().getStatus() == startingEvents.get(j).getEvent().getStatus()
						&& i != j
						&& i < j){
						toDelete.add(startingEvents.get(i));
				}		
				// Checking for "a or no a"
				if (startingEvents.get(i).getEvent().getName() == startingEvents.get(j).getEvent().getName() 
						&& startingEvents.get(i).getEvent().getStatus() != startingEvents.get(j).getEvent().getStatus()
						&& i != j){
						toDelete.add(startingEvents.get(i));
						toDelete.add(startingEvents.get(j));
				}
			}
			// Checking for "a or  a" with the conclusion
			if (startingEvents.get(i).getEvent().getName() == clause.getConclusion().getEvent().getName()
					&& startingEvents.get(i).getEvent().getStatus() == clause.getConclusion().getEvent().getStatus()){
				toDelete.add(startingEvents.get(i));
			}
		}
		startingEvents.removeAll(toDelete);
	}
}
