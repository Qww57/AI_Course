package GenericAStar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import DirectInferenceEngine.Clause;
import DirectInferenceEngine.ClauseEvent;
import DirectInferenceEngine.Event;
import DirectInferenceEngine.Event.Status;

/**
 * This class is extending the {@link AStar} abstract class in order to 
 * deal with {@link Clause} objects. It is implementing an inference engine
 * based on direct proof, tree structure and A* Algorithm.
 * 
 * In order to use this class, {@link ClauseNode} should be used.
 * 
 * When starting the AStar algorithm, the startAStar function should not have
 * any starting point since, this one is guessed from the knowledge base, but 
 * should have the goal defined.
 * 
 * @author Quentin
 *
 */
@SuppressWarnings("boxing")
public class DirectInferenceEngine extends AStar {

	public DirectInferenceEngine(List<Clause> kBase) {
		super(kBase);
	}

	protected static List<Event> eventPool = new ArrayList<Event>();
	
	@Override
	protected void initializeSets(AbstractNode start, AbstractNode goal) {
		openSet = new PriorityQueue<AbstractNode>();
	    cameFrom = new HashMap<AbstractNode, AbstractNode>();
	    gScore = new HashMap<AbstractNode, Double>();
	    fScore = new HashMap<AbstractNode, Double>();
		closedSet = new ArrayList<>();
		eventPool = new ArrayList<>();
		
		// Initializing the event pool set 
		List<Clause> copy = new ArrayList<Clause>(knowledgeBase);
		for (int i = 0; i < copy.size(); i++){
			Clause clause = copy.get(i);
			
			// Getting the clause which are simple ones
			if (clause.getEvents().size() == 0){
				ClauseEvent clauseEvent = clause.getConclusion();
				Event simpleEvent = clauseEvent.getEvent();
				if (clauseEvent.getValue() == true){
					simpleEvent.setStatus(Status.TRUE);
				}
				else 
					simpleEvent.setStatus(Status.FALSE);
				eventPool.add(simpleEvent);
				knowledgeBase.remove(clause);
			}
		}			
		
		// Verifications
		printEventPool();	
	   	
	    // Initializing the openSet
 		copy = new ArrayList<Clause>(knowledgeBase);
 		for (int i =0; i< copy.size(); i++){
 			Clause clause = copy.get(i);
 			if (isInteresting(clause)){
 				ClauseNode newNode = new ClauseNode(clause);
 				
 				gScore.put(newNode, new Double(0));
 		    	fScore.put(newNode, heuristic_cost_estimate(newNode, goal));
 		    	newNode.setFScore(fScore.get(newNode).doubleValue());
 		    	newNode.setGScore(gScore.get(newNode).doubleValue());
 		    	
 		    	openSet.add(newNode);
 				knowledgeBase.remove(clause);
 			}
 		}
 		
 		// Verifications of the openSet
 		printOpenSet(openSet);		
	}

	@Override
	protected List<AbstractNode> reconstructPath(AbstractNode current) {
		/* Not needed in that one */
		return null;
	}

	@Override
	protected void findChildren(AbstractNode node) {
		ClauseNode _clauseNode = (ClauseNode) node;
		Clause _clause = (Clause) _clauseNode.getObject();
		
		Event event = _clause.getConclusion().getEvent();
		boolean conclusionValue = _clause.getConclusion().getValue();
				
		List<Clause> copy = new ArrayList<Clause>(knowledgeBase);
		List<Clause> toDelete = new ArrayList<Clause>();
		
		// Loop on the clause in the knowledge base
		for (int i = 0; i < copy.size(); i++){
			Clause clause = copy.get(i);
			
			boolean childFound = false;
			
			for (int j = 0; j < clause.getEvents().size(); j++){			
				if (clause.getEvents().get(j).getEvent().equals(event)){
					if (conclusionValue != clause.getEvents().get(j).getValue())
						childFound = true;
				} 
			}
			
			if (childFound == true){
				// System.out.println("Child found with " + clause.getEvents().get(i).getEvent().toString());
				ClauseNode child = new ClauseNode(clause);
				child.setParent(node);
				node.addChild(child);
				toDelete.add(clause);
			}
		}
		
		// Deleting the clauses from the database
		knowledgeBase.removeAll(toDelete);
	}

	@Override
	protected double heuristic_cost_estimate(AbstractNode o1, AbstractNode goal) {
		Clause clause = (Clause) o1.getObject();
		return unknownElements(clause);
	}

	@Override
	protected double dist_between(AbstractNode o1, AbstractNode o2) {
		return 0;
	}
	
	@Override
	protected boolean goalChecking(AbstractNode current, AbstractNode goal) {
		
    	gScore.put(current, new Double(0));
    	fScore.put(current, heuristic_cost_estimate(current, goal));
    	current.setFScore(fScore.get(current).doubleValue());
    	current.setGScore(gScore.get(current).doubleValue());
		
		solveClause(current);
    	
    	// Checking if we are getting the goal
    	Clause goalClause = (Clause) goal.getObject();
		String goalName = goalClause.getConclusion().getEvent().getName();
    	boolean goalStatus = goalClause.getConclusion().getValue();
    	
		if ((getStatusOfEventFromtPool(goalName) == Status.TRUE && goalStatus == true)
				|| (getStatusOfEventFromtPool(goalName) == Status.FALSE && goalStatus == false)){
    		cameFrom.put(goal, current);
    		System.out.println("We have our goal: " + goalClause.getConclusion().getEvent().getName());
    		results = reconstructPath(goal);	    		
    		// printAllLists(openSet, closedSet);
    		return true;
    	}
    	return false;
	}
	
	/* PRIVATE METHODS */
	
	/**
	 * Function used in order to initialize the openSet. This function check if it would be 
	 * interesting or not to add a given clause inside the openSet. 
	 * An interesting clause is a clause that involves at least one single event in its 
	 * condition events that is already inside the event pool.
	 * 
	 * @param clause - clause we want to check
	 * @return boolean - true if the clause should be added to the closedSet, false if not.
	 */
	private static boolean isInteresting(Clause clause){
		boolean interesting = false;
		for(Iterator<Event> i = eventPool.iterator(); i.hasNext();) {
			Event event = i.next();
			for(Iterator<ClauseEvent> j = clause.getEvents().iterator(); j.hasNext();){
				ClauseEvent clauseEvent = j.next();
				if (clauseEvent.getEvent().equals(event)
						&& clauseEvent.getValue() == true 
						&& event.getStatus() == Status.FALSE){
					interesting = true;
				}
				if (clauseEvent.getEvent().equals(event)
						&& clauseEvent.getValue() == false 
						&& event.getStatus() == Status.TRUE){
					interesting = true;
				}
			}
		}
		return interesting;
	}
	
	/**
	 * Function used in order to solve a clause. To solve one clause, we need to 
	 * check the value of its condition events related to the event pool.
	 * If a clause can be solved, then its result is added to the event pool.
	 * 
	 * @param current - current node containing the clause we want to solve
	 */
	private static void solveClause(AbstractNode current) {
		
		simplifyClause(current);
		
		Clause clause = (Clause) current.getObject();
		
		if (unknownElements(clause) == 0){
			 
			System.out.println("Clause " + clause.toString() + " is solvable");
			
			// Check if we should add the conclusion in the pool event
			Status conditionEventStatus = Status.TRUE;
			
			// If one of the event status is matching then we should not add it
			// All the conditions events in the clause should be wrong in the event Pool in order to have
			// The result as true
			List<ClauseEvent> conditions = clause.getEvents();
			for (int i = 0; i < conditions.size(); i++){
				ClauseEvent clauseEvent = conditions.get(i);
				if (clauseEvent.getValue() == true 
						&& getStatusOfEventFromtPool(clauseEvent.getEvent().getName()) == Status.TRUE){
					conditionEventStatus = Status.FALSE;
				} 
				else if (clauseEvent.getValue() == false 
						&& getStatusOfEventFromtPool(clauseEvent.getEvent().getName()) == Status.FALSE){
					conditionEventStatus = Status.FALSE;
				}
			}
			
			// Resolving it
			Event solvedEvent = clause.getConclusion().getEvent();
			solvedEvent.setStatus(conditionEventStatus);
			eventPool.add(solvedEvent);
			System.out.println("Solved: " + solvedEvent.toString());
			
			updateAllFCost();
		}	
	}
	
	/**
	 * Simplify a clause: a v a => a  
	 * 
	 * @param current - current node containing the clause we want to solve
	 */
	private static void simplifyClause(AbstractNode current) {
		// TODO Auto-generated method stub		
	}

	/**
	 * Return the number of events that are still unknown
	 * 
	 * @param clause - clause we want to check
	 */
	private static int unknownElements(Clause clause){
		int elements = 0;
		for(int i = 0; i < clause.getEvents().size(); i++){
			ClauseEvent clauseEvent = clause.getEvents().get(i);
			
			if (clauseEvent.getEvent().getStatus() == Status.UNKWON){
				elements = elements + 1;
			}
		}
		return elements;
	}
	
	/**
	 * Update the value of the fScore for all elements that are still in the openSet.
	 * This function is used right after solving a new clause. In fact, if a clause has
	 * been solved then the event pool has changed and the number of unknown elements for 
	 * each clause of the openSet has also changed. We need so to update the fScore of 
	 * each clause of the openSet.
	 */	
	private static void updateAllFCost() {
		PriorityQueue<AbstractNode> updatedOpenSet = new PriorityQueue<AbstractNode>();
		while(!openSet.isEmpty()){
			ClauseNode current = (ClauseNode) openSet.poll();
			
			double newFScore = staticHeuristicCost(current);
			fScore.put(current, new Double(newFScore));
			current.setFScore(newFScore);
			
			updatedOpenSet.add(current);
		}
		openSet = updatedOpenSet;
	}
	
	/**
	 * This function is implementing the heuristic using a static method.
	 * 
	 * This is just because java wants us to use a static method for the fScore update in 
	 * updateAllFCost(), but the heuristic method cannot be turned to static since it 
	 * is declared as an abstract method in {@link AStar}. 
	 * This what we had to create a copy of the heuristic.
	 * 
	 * @param
	 */
	private static double staticHeuristicCost(AbstractNode node){
		Clause clause = (Clause) node.getObject();
		return unknownElements(clause);
	}
	
	/**
	 * TODO description
	 * 
	 * @param description
	 * @return
	 */
	private static Status getStatusOfEventFromtPool(String description){
		Status status = Status.UNKWON;
		for (int i = 0; i < eventPool.size(); i++ ){
			if (eventPool.get(i).getName().equals(description)){
				status = eventPool.get(i).getStatus();
			}
		}
		return status;
	}
	

	
	/* PRINTERS */
	
	private static void printOpenSet(PriorityQueue<AbstractNode> openSet1){
 		System.out.println();
 		System.out.println("--- Verifications for the openSet ---");
 		System.out.println("Size of the initial open set: " + openSet1.size());
 		Object[] openSetArray = openSet1.toArray();
 		for (int i = 0; i < openSetArray.length; i++){
 			ClauseNode clauseNode = (ClauseNode) openSetArray[i];
 			Clause clause = (Clause) clauseNode.getObject();
 			System.out.println(clause.toString() 
 					+ " - F: " + clauseNode.getFScore() 
 					+ " - G: " + clauseNode.getGScore());
 		}
 		System.out.println();
	}
	
	private static void printEventPool(){
		System.out.println();
		System.out.println("--- Verifications for the eventPool ---");
		System.out.println("Size of the initial true set: " + eventPool.size());
		for (int i = 0; i < eventPool.size(); i++){
			eventPool.get(i).print();
		}
		System.out.println();
	}
}