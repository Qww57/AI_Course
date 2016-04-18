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

public class DirectInferenceEngine extends AStar {

	public DirectInferenceEngine(List<Clause> kBase) {
		super(kBase);
	}

	protected static List<Event> eventPool = new ArrayList<Event>();
	
	@SuppressWarnings("boxing")
	@Override
	protected void initializeSets(AbstractNode start, AbstractNode goal) {
		// Set of node that have already been evaluated
		closedSet = new ArrayList<>();
		
		// Set of events that are true
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
		
		// Set of discovered nodes that needs to be evaluated
		openSet = new PriorityQueue<AbstractNode>();
		
		// Hashmap remembering the previous node of the shortest path to get here
	    cameFrom = new HashMap<AbstractNode, AbstractNode>();
	    
	    // For each node, the cost of getting from the start node to that node
	    gScore = new HashMap<AbstractNode, Double>();
	    
	    // For each node, the total cost of getting from the start node to the goal
	    fScore = new HashMap<AbstractNode, Double>();	
	   	
	    // Initializing the openSet
 		copy = new ArrayList<Clause>(knowledgeBase);
 		for (int i =0; i< copy.size(); i++){
 			Clause clause = copy.get(i);
 			if (isInteresting(eventPool, clause)){
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
	protected List<AbstractNode> reconstructPath(HashMap<AbstractNode, AbstractNode> cameFrom1, AbstractNode current) {
		/* Not needed in that one */
		return null;
	}

	@Override
	protected void printAllLists(PriorityQueue<AbstractNode> _openSet, List<AbstractNode> _closedSet1) {
		System.out.println("-------");
		System.out.println("OpenSet: ");
		for (int i = 0; i < _openSet.size(); i++){
			System.out.println(_openSet.poll().getObject().toString());
		}
		System.out.println("-------");
		System.out.println("ClosedSet: ");
		for (int i = 0; i < _closedSet1.size(); i++){
			System.out.println(_closedSet1.get(i).getObject().toString());
		}
		System.out.println("-------");
		System.out.println("Knowledge base: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		}
		System.out.println("-------");		
	}

	@Override
	protected void findChildren(AbstractNode node) {
		ClauseNode _clauseNode = (ClauseNode) node;
		Clause _clause = (Clause) _clauseNode.getObject();
		
		Event event = _clause.getConclusion().getEvent();
		boolean conclusionValue = _clause.getConclusion().getValue();
		
		// System.out.println("Finding children for clause: " + clauseNode.getClause().toString());
		// System.out.println("Using the conclusion: " + event.toString());
				
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
		return unknownElements(clause, eventPool);
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
    	// if (current.getClause().getConclusion().getEvent().getName().equals(goalName)){
    	if (getStatusOfEventFromtPool(goalName) == Status.TRUE){
    		cameFrom.put(goal, current);
    		System.out.println("We have our goal: " + goalClause.getConclusion().getEvent().getName());
    		results = reconstructPath(cameFrom, goal);	    		
    		// printAllLists(openSet, closedSet);
    		return true;
    	}
    	return false;
	}
	
	/* PRIVATE METHODS */
	
	private static boolean isInteresting(List<Event> trueSet, Clause clause){
		boolean interesting = false;
		for(Iterator<Event> i = trueSet.iterator(); i.hasNext();) {
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
	
	private static void solveClause(AbstractNode current) {
		Clause clause = (Clause) current.getObject();
		if (unknownElements(clause, eventPool) == 0){
			// To solve one clause, we need to check the value of its condition events related to the event pool
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
	 * Update the value of the fScore for all elements that are still in the openSet
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
	 * Just because java wanted me to use a static method for the FScore update in 
	 * updateAllFCost(), but I cannot put the heuristic method to static since it 
	 * is at first an abstract method, so I had to make a copy of it
	 */
	private static double staticHeuristicCost(AbstractNode o1){
		Clause clause = (Clause) o1.getObject();
		return unknownElements(clause, eventPool);
	}
	
	private static Status getStatusOfEventFromtPool(String description){
		Status status = Status.UNKWON;
		for (int i = 0; i < eventPool.size(); i++ ){
			if (eventPool.get(i).getName().equals(description)){
				status = eventPool.get(i).getStatus();
			}
		}
		return status;
	}
	
	/**
	 * Return the number of events that are still unknown
	 */
	private static int unknownElements(Clause clause, List<Event> trueSet){
		int elements = 0;
		for(int i = 0; i < clause.getEvents().size(); i++){
			ClauseEvent clauseEvent = clause.getEvents().get(i);
			
			if (clauseEvent.getEvent().getStatus() == Status.UNKWON){
				elements = elements + 1;
			}
		}
		return elements;
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
 		/*System.out.println("Clausal KB inside A Star after open set initialization: ");
 		 for (int i = 0; i < knowledgeBase.size(); i++){
 			knowledgeBase.get(i).print();
 		} */
 		System.out.println();
	}
	
	private static void printEventPool(){
		System.out.println();
		System.out.println("--- Verifications for the eventPool ---");
		System.out.println("Size of the initial true set: " + eventPool.size());
		for (int i = 0; i < eventPool.size(); i++){
			eventPool.get(i).print();
		}
		/* System.out.println("Clausal KB inside A Star after true set initialization: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		} */
	}

}