package GenericAStar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import DirectInferenceEngine.Clause;
import DirectInferenceEngine.ClauseEvent;
import DirectInferenceEngine.Event;
import DirectInferenceEngine.Event.Status;

/**
 * This class is extending the {@link AStar} abstract class in order to 
 * deal with {@link Clause} objects. It is implementing an inference engine
 * based on refutation proof, tree structure and A* Algorithm.
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
public class IndirectInferenceEngine extends AStar {
	
	// Negation of the goal that will be used to conduct our resolution
	protected static ClauseNode startingPoint;

	public IndirectInferenceEngine(List<Clause> kBase) {
		super(kBase);
	}

	protected static List<Event> eventPool = new ArrayList<Event>();
	
	@Override
	protected void initializeSets(AbstractNode start, AbstractNode goal) {
		// We negate the abstract objective.
		System.out.println("Goal: " + goal.toString());
		startingPoint = negate(goal);
		System.out.println("Goal negated: " + startingPoint.toString());
		
		// Initializing all the sets
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
				//knowledgeBase.remove(clause);
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
 		printOpenSet();
	}

	/**
	 * DONE
	 */
	@Override
	protected boolean goalChecking(AbstractNode current, AbstractNode goal) {
		gScore.put(current, new Double(0));
    	fScore.put(current, heuristic_cost_estimate(current, goal));
    	current.setFScore(fScore.get(current).doubleValue());
    	current.setGScore(gScore.get(current).doubleValue());
		
    	startingPoint = solveClause((ClauseNode) current);
    	
    	// Checking if we are getting an empty clause
    	Clause startingClause = ((Clause) startingPoint.getObject());
		if (startingClause.getConclusion() == null && startingClause.getEvents().size() == 0){
    		cameFrom.put(goal, current);
    		System.out.println("We have have an empty clause, so we have our goal");
    		// results = reconstructPath(negate(goal));	    		
    		return true;
    	}
    	return false;
	}

	/**
	 * Something to fix, see with the tea problem 
	 * 
	 * @param current
	 * @return
	 */
	private static ClauseNode solveClause(ClauseNode current) {
		Clause clause = (Clause) current.getObject();
		
		if (negatedElements(clause) != 0){
			
			// Copying data in order to handle them
			List<ClauseEvent> startingEvents = ((Clause) startingPoint.getObject()).getEvents();
			List<ClauseEvent> copyStartingClause = new ArrayList<ClauseEvent>(startingEvents);			
			List<ClauseEvent> copyClauseEvents = new ArrayList<ClauseEvent>(clause.getEvents());
			
			// Elements to delete, if they are both in clauses
			for (int j = 0; j < startingEvents.size(); j++){
				
				for (int i = 0; i < clause.getEvents().size(); i++){				
				
					//TODO see why never true
					if (clause.getEvents().get(i).getEvent().getName() == startingEvents.get(j).getEvent().getName()
							&& clause.getEvents().get(i).getValue() != startingEvents.get(j).getValue()){
						copyStartingClause.remove(clause.getEvents().get(j));
						copyClauseEvents.remove(clause.getEvents().get(i));
					}
					
					// If in conclusion of current and events of starting clause
					if (clause.getConclusion().getEvent().getName() == startingEvents.get(j).getEvent().getName()
							&& clause.getConclusion().getValue() != startingEvents.get(j).getValue()){
						copyStartingClause.remove(startingEvents.get(j));
					}
				}			
			}
			
			// Creating the output clause
			List<ClauseEvent> newSPEvents = new ArrayList<ClauseEvent>();
			newSPEvents.addAll(copyStartingClause);
			newSPEvents.addAll(copyClauseEvents);
			ClauseNode newSP = new ClauseNode(new Clause("Goal", newSPEvents, null));
			
			System.out.println("Solved: " + newSP.toString());
			
			updateAllFCost();
			
			return newSP;
		}
		System.out.println("No solving: " + startingPoint.toString());
		return startingPoint;
	}
	
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
	
	protected static double staticHeuristicCost(AbstractNode node) {		
		double factor1 = unknownElements(((Clause) node.getObject()));
		double factor2 = negatedElements(((Clause) node.getObject()));
		double factor3 = numberElements(((Clause) node.getObject()));
		double score = factor1 * 100 + factor2 * 10 + factor3;
		System.out.println("Computing heuristic for " + node.toString() + " : " + score);
		return score; 
	}
	

	@Override
	protected List<AbstractNode> reconstructPath(AbstractNode current) {
		// Not needed here
		return null;
	}

	/**
	 * Children, if one of the elements is the negation inside
	 */
	@Override
	protected void findChildren(AbstractNode node) {
		
		Clause startingClause = ((Clause) startingPoint.getObject());
		
		List<Clause> copy = new ArrayList<Clause>(knowledgeBase);
		List<Clause> toDelete = new ArrayList<Clause>();
		
		// Loop on the database
		for (int i = 0; i < copy.size(); i++){
			Clause clause = copy.get(i);
			
			// Loop on the events of the starting clause
			for (int h = 0; h < startingClause.getEvents().size(); h++){
				
				boolean childFound = false;
				ClauseEvent startingEvents = startingClause.getEvents().get(h);
				
				for (int j = 0; j < clause.getEvents().size(); j++){
					ClauseEvent clauseEvent = clause.getEvents().get(j);
					if (clauseEvent.getEvent().getName() == startingEvents.getEvent().getName()
							&& clauseEvent.getValue() != startingEvents.getValue()){												
						childFound = true;
					}
				}
				
				if (clause.getConclusion().getEvent().getName() == startingEvents.getEvent().getName()
						&& clause.getConclusion().getValue() != startingEvents.getValue()){												
					childFound = true;
				}
				
				if (childFound == true){
					System.out.println("Child found " + clause.toString());
					ClauseNode child = new ClauseNode(clause);
					child.setParent(node);
					node.addChild(child);					
					toDelete.add(clause);
				}
			}
		}
		
		// Deleting the clauses from the database
		knowledgeBase.removeAll(toDelete);
	}

	@Override
	protected double dist_between(AbstractNode node1, AbstractNode node2) {
		return 0;
	}

	/**
	 * Low number of unknown elements inside the checked clause 
	 * number of elements of the startingPoint that are negated inside the current node
	 * number of elements inside the checked clause
	 */
	@Override
	protected double heuristic_cost_estimate(AbstractNode node, AbstractNode goal) {		
		double factor1 = unknownElements(((Clause) node.getObject()));
		double factor2 = negatedElements(((Clause) node.getObject()));
		double factor3 = numberElements(((Clause) node.getObject()));
		double score = factor1 * 100 + factor2 * 10 + factor3;
		System.out.println("Computing heuristic for " + node.toString() + " : " + score);
		return score; 
	}
	
	private static int unknownElements(Clause clause){
		int elements = 0;
		for(int i = 0; i < clause.getEvents().size(); i++){
			ClauseEvent clauseEvent = clause.getEvents().get(i);
			
			if (clauseEvent.getEvent().getStatus() == Status.UNKWON){
				elements = elements + 1;
			}
		}
		if (clause.getConclusion().getEvent().getStatus() == Status.UNKWON){
			elements = elements + 1;
		}
		return elements;
	}
	
	private static double numberElements(Clause clause) {
		double events = clause.getEvents().size();
		if (clause.getConclusion() != null){
			events++;
		}
		return events;
	}

	private static double negatedElements(Clause clause) {
		double negation = 0;
		Clause startingClause = ((Clause) startingPoint.getObject());
		
		// If the negation is made in the conclusion
		for (int j = 0; j < startingClause.getEvents().size(); j++){
			ClauseEvent startingEvent = startingClause.getEvents().get(j);
			if (startingEvent.getEvent().getName() == clause.getConclusion().getEvent().getName()
					&& startingEvent.getValue() != clause.getConclusion().getValue())
				negation++;
		}
		
		// If the negation is made in the events
		for (int i = 0; i < clause.getEvents().size(); i++){
			ClauseEvent clauseEvent = clause.getEvents().get(i);
			for (int j = 0; j < startingClause.getEvents().size(); j++){
				ClauseEvent startingEvent = startingClause.getEvents().get(j);
				if (startingEvent.getEvent().getName() == clauseEvent.getEvent().getName()
						&& startingEvent.getValue() != clauseEvent.getValue())
					negation++;
			}
		}
		return negation;
	}

	/**
	 * Negates and move it inside of the condition inside of the events
	 * 
	 * @param node
	 * @return
	 */
	private static ClauseNode negate(AbstractNode node) {
		Clause clause = (Clause) node.getObject();
		ClauseEvent conclusion = ((Clause) node.getObject()).getConclusion();
		
		if (clause.getConclusion().getValue() == true)
			conclusion.setValue(false);
		else 
			conclusion.setValue(true);
		
		List<ClauseEvent> conditions = new ArrayList<ClauseEvent>();
		conditions.add(conclusion);
		Clause newClause = new Clause("Working", conditions, null);
		return new ClauseNode(newClause);
	}
	
	/**
	 * Only used during the initialization of the algorithm with negated goal.
	 * Beware that in the negation, the goal is not inside the conclusion anymore,
	 * but is now inside the conditions.
	 * 
	 * @param clause
	 * @return
	 */
	private static boolean isInteresting(Clause clause){
		boolean interesting = false;
		String goalName = ((Clause) startingPoint.getObject()).getEvents().get(0).getEvent().getName();
		if (clause.getConclusion().getEvent().getName() == goalName){
			interesting = true;
		}
		return interesting;
	}
	
	/**
	 * SAME AS IN DIRECT
	 */
	private static void printOpenSet(){
 		System.out.println();
 		System.out.println("--- Verifications for the openSet ---");
 		System.out.println("Size of the initial open set: " + openSet.size());
 		Object[] openSetArray = openSet.toArray();
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
