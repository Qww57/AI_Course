package InferenceEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import AbstractAStar.AbstractAStar;
import AbstractAStar.AbstractNode;
import InferenceEngine.Event.Status;

/**
 * This class is extending the {@link AbstractAStar} abstract class in order to 
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
public class IndirectInferenceEngine extends AbstractInferenceEngine {
	
	// Negation of the goal that will be used to conduct our resolution
	protected static ClauseNode startingPoint;

	public IndirectInferenceEngine(List<Clause> kBase) {
		super(kBase);
	}
	
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
    		// We negate the goal again, just in order to print it
    		negate(goal);
    		System.out.println("We have have an empty clause, so we have our goal: " 
    				+ ((Clause) goal.getObject()).toString()); 
    		return true;
    	}
    	return false;
	}

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
					// System.out.println("Child found " + clause.toString());
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

	/**
	 * The heuristic function is dealing with the following three parameters:
	 * - Number of unknown elements inside the checked clause 
	 * - Number of elements of the startingPoint that are negated inside the current node
	 * - Number of elements inside the checked clause
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
	
	/* PRIVATE METHODS */
	
	/**
	 * This function is implementing the heuristic using a static method.
	 * 
	 * This is just because java wants us to use a static method for the fScore update in 
	 * updateAllFCost(), but the heuristic method cannot be turned to static since it 
	 * is declared as an abstract method in {@link AbstractAStar}. 
	 * This what we had to create a copy of the heuristic.
	 * 
	 * @param
	 */
	private static double staticHeuristicCost(AbstractNode node) {		
		double factor1 = unknownElements(((Clause) node.getObject()));
		double factor2 = negatedElements(((Clause) node.getObject()));
		double factor3 = numberElements(((Clause) node.getObject()));
		double score = factor1 * 100 + factor2 * 10 + factor3;
		return score; 
	}
	
	/**
	 * Return the number of events that are still unknown
	 * 
	 * @param clause - clause we want to check
	 * @return int - number of unknown elements
	 */
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
	
	/**
	 * Return the number of elements inside a clause
	 * 
	 * @param clause - clause we want to check
	 * @return int - number of elements
	 */
	private static double numberElements(Clause clause) {
		double events = clause.getEvents().size();
		if (clause.getConclusion() != null){
			events++;
		}
		return events;
	}

	/**
	 * Return the number of elements inside the clause that are negating 
	 * elements from our startingClause
	 * 
	 * @param clause - clause we want to check
	 * @return int - number of elements negating the ones of the startingClause
	 */
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
	 * Solving a clause. This function is merging the clause from the current node 
	 * and the one we are dealing with through the whole algorithm. It also removes
	 * the elements that are present with and without negation (for instance, 
	 * 
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
				
				ClauseEvent startingEvent = startingEvents.get(j);
				Event event = startingEvent.getEvent();
				boolean eventStatus = startingEvent.getValue();
				
				for (int i = 0; i < clause.getEvents().size(); i++){				
				
					ClauseEvent tmpClauseEvent = clause.getEvents().get(i);				
					if (tmpClauseEvent.getEvent().getName() == event.getName() 
							&& tmpClauseEvent.getValue() != eventStatus){
						copyStartingClause.remove(startingEvents.get(j));
						copyClauseEvents.remove(clause.getEvents().get(i));
					}
				}
				
				// If in conclusion of current and events of starting clause
				if (clause.getConclusion().getEvent().getName() == event.getName()
						&& clause.getConclusion().getValue() != eventStatus){
					copyStartingClause.remove(startingEvents.get(j));
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
	
	/**
	 * Update the value of the fScore for all elements that are still in the openSet.
	 * This function is used right after solving a new clause. In fact, if we can solve the 
	 * clause of the current node with the starting clause we had, then we have to update 
	 * all the fCosts.
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
}
