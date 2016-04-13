package InferenceEngineTwo;

import java.lang.reflect.Array;
import java.util.*;

import InferenceEngineTwo.ClauseNode;
import InferenceEngineTwo.Clause;
import InferenceEngineTwo.Event.Status;
import InferenceEngineTwo.Node;

@SuppressWarnings("unused")
public class InferenceEngine {	
			
	private static List<Node> results;
	private static List<Clause> knowledgeBase = new ArrayList<Clause>();
	private static List<Event> eventBase = new ArrayList<Event>();
	private static List<Event> eventPool = new ArrayList<Event>();
	private static HashMap<Node, Integer> fScore = new HashMap<Node, Integer>();
	private static PriorityQueue<Node> openSet = new PriorityQueue<Node>();
	
	public InferenceEngine(List<Clause> kBase, List<Event> eBase){
		// Creating a copy of the list of roads in order to delete some without consequences
		knowledgeBase = new ArrayList<Clause>(kBase);
		eventBase = new ArrayList<Event>(eBase);
	}
	
	public List<Node> startAstar(Node goal){
		System.out.println("");
		System.out.println("Start of inference engine");		
		long startTime = System.currentTimeMillis(); 
		
		// Set of node that have already been evaluated
		List<Node> closedSet = new ArrayList<>();
		
		// Set of events that are true
		eventPool = new ArrayList<>();
		
		// Initializing the event pool set 
		List<Clause> copy = new ArrayList<Clause>(knowledgeBase);
		for (int i = 0; i < copy.size(); i++){
			Clause clause = copy.get(i);
			// Getting the clause which are simple ones
			if (clause.getEvents().size() == 0){
				// System.out.println("Event of pool event: " + clause.toString());
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
		openSet = new PriorityQueue<Node>();
		
		// Hashmap remembering the previous node of the shortest path to get here
	    HashMap<Node, Node> cameFrom = new HashMap<Node, Node>();
	    
	    // For each node, the cost of getting from the start node to that node
	    HashMap<Node, Integer> gScore = new HashMap<Node, Integer>();
	    
	    // For each node, the total cost of getting from the start node to the goal
	    fScore = new HashMap<Node, Integer>();	
	   	
	    // Initializing the openSet
 		copy = new ArrayList<Clause>(knowledgeBase);
 		for (int i =0; i< copy.size(); i++){
 			Clause clause = copy.get(i);
 			if (isInteresting(eventPool, clause)){
 				ClauseNode newNode = new ClauseNode(clause);
 				
 				gScore.put(newNode, new Integer(0));
 		    	fScore.put(newNode, heuristic_cost_estimate(newNode));
 		    	newNode.setFScore(fScore.get(newNode).doubleValue());
 		    	newNode.setGScore(gScore.get(newNode).doubleValue());
 		    	
 		    	openSet.add(newNode);
 				knowledgeBase.remove(clause);
 			}
 		}
 		
 		// Verifications of the openSet
 		printOpenSet(openSet);
	 	
 		System.out.println("--- Starting the inferences ---");
	    while (!openSet.isEmpty()){
	    	// Get first node from openSet using the comparable from the node
	    	Node current = openSet.poll();
	    	
	    	System.out.println("Checking the node: " + current.getClause().toString() + " - F: " + current.getFScore());
	    	
	    	gScore.put(current, new Integer(0));
	    	fScore.put(current, heuristic_cost_estimate(current));
	    	current.setFScore(fScore.get(current).doubleValue());
	    	current.setGScore(gScore.get(current).doubleValue());
	    		 
	    	// Checking if we are getting the goal
	    	if (current.getClause().getConclusion().getEvent().getName()
	    			.equals(goal.getClause().getConclusion().getEvent().getName())){
	    		cameFrom.put(goal, current);
	    		System.out.println("We have our goal: " + goal.getClause().getConclusion().getEvent().getName());
	    		// results = reconstructPath(cameFrom, goal);	    		
	    		// printAllLists(openSet, closedSet);
	    		break;
	    	}
	    		 
	    	solveClause(current);
	    	
	    	// Updating the sets
	    	openSet.remove(current);
	    	closedSet.add(current);
	    	
	    	// Find children of current node
	    	findChildren(current); 
	    	
			for(Iterator<Node> i = current.getChildren().iterator(); i.hasNext();) {
				Node child = i.next();
				
				if (closedSet.contains(child)){
					// Do nothing, already in the close set 
				}
				else {
					// Distance from start to the child
					Integer tentative_gScore = gScore.get(current);
					
					// Child not into the openSet
					if (!openSet.contains(child)){						
						openSet.add(child);
						cameFrom.put(child, current);
						
						// Computing and saving the cost in the hash maps	
						Integer tentative_fScore = heuristic_cost_estimate(child);
						gScore.put(child, tentative_gScore);
						fScore.put(child, tentative_fScore);
						
						// Saving the costs into the node
						child.setGScore(tentative_gScore.doubleValue());
						child.setFScore(tentative_fScore.doubleValue());
					}
					// Child is in the openSet
					else {
						// If, best way to get to this node
						if (gScore.get(child).doubleValue() > tentative_gScore.doubleValue()){
							cameFrom.put(child, current);
							
							// Computing and saving the cost in the hash maps	
							Integer tentative_fScore = heuristic_cost_estimate(child);							
							gScore.put(child, tentative_gScore);
							fScore.put(child, tentative_fScore);
							
							// Saving the costs into the node
							child.setGScore(tentative_gScore.doubleValue());
							child.setFScore(tentative_fScore.doubleValue());
						}
					}
				}
		    }
	    }
	
	    long longTime = System.currentTimeMillis(); 
	    System.out.println("End of inference engine in " + (longTime - startTime) 
	    		+ " ms using " + (closedSet.size() + 1 )+ " nodes");
		
	    return results; 
	}
	
	// DONE
	private static void solveClause(Node current) {
		if (unknownElements(current.getClause(), eventPool) == 0){
			// To solve one clause, we need to check the value of its condition events related to the event pool
			// System.out.println("Clause " + current.getClause().getClauseID() + " is solvable");
			
			// Check if we should add the conclusion in the pool event
			Status conditionEventStatus = Status.TRUE;
			
			// If one of the event status is matching then we should not add it
			// All the conditions events in the clause should be wrong in the event Pool in order to have
			// The result as true
			List<ClauseEvent> conditions = current.getClause().getEvents();
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
			Event solvedEvent = current.getClause().getConclusion().getEvent();
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
		PriorityQueue<Node> updatedOpenSet = new PriorityQueue<Node>();
		while(!openSet.isEmpty()){
			ClauseNode current = (ClauseNode) openSet.poll();
			fScore.put(current, heuristic_cost_estimate(current));
			current.setFScore(heuristic_cost_estimate(current).doubleValue());
			updatedOpenSet.add(current);
		}
		openSet = updatedOpenSet;
	}
	

	// DONE
	private static Status getStatusOfEventFromtPool(String description){
		Status status = Status.UNKWON;
		for (int i = 0; i < eventPool.size(); i++ ){
			if (eventPool.get(i).getName().equals(description)){
				status = eventPool.get(i).getStatus();
			}
		}
		return status;
	}
	
	// DONE
	
	private static List<Node> reconstructPath(HashMap<Node, Node> cameFrom, Node current){
		System.out.println("");
		System.out.println("Solution found - Reconstructing the path: ");
		
		List<Node> path = new ArrayList<Node>();
		Node currentNode = current;
		while (cameFrom.getOrDefault(currentNode, null) != null){
			Node previousNode = cameFrom.get(currentNode);
			path.add(previousNode);
			currentNode = previousNode;
		}
		System.out.println("Number of steps : " + path.size());
		
		Collections.reverse(path);
		
		for (int i = 0; i < path.size(); i++){
			System.out.println(i+1 + ") Go into: " + path.get(i).toString() + " - " + path.get(i).getClause().toString());
		}	
		
		return path;
	}
	
	
	// DONE
	private static boolean isInteresting(List<Event> trueSet, Clause clause){
		boolean interesting = false;
		for(Iterator<Event> i = trueSet.iterator(); i.hasNext();) {
			Event event = i.next();
			for(Iterator<ClauseEvent> j = clause.getEvents().iterator(); j.hasNext();){
				ClauseEvent clauseEvent = j.next();
				if (clauseEvent.getEvent().equals(event)){
					interesting = true;
					//System.out.println("Interesting clause: " + clause.toString() + " - Because of event: " + event.getName());
				}
			}
		}
		return interesting;
	}
	
	
	// DONE
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
	
	// DONE
	/**
	 * Children of a node are the clause from the knowledgeBase 
	 * Where at least on of their events is in the parent node
	 * and that is resolvable
	 * @param node
	 */
	private static void findChildren(Node node){
		ClauseNode clauseNode = (ClauseNode) node;
		Event event = clauseNode.getClause().getConclusion().getEvent();
		
		// System.out.println("Finding children for clause: " + clauseNode.getClause().toString());
		// System.out.println("Using the conclusion: " + event.toString());
		
		
		List<Clause> copy = new ArrayList<Clause>(knowledgeBase);
		List<Clause> toDelete = new ArrayList<Clause>();
		
		// Loop on the clause in the knowledge base
		for (int i = 0; i < copy.size(); i++){
			Clause clause = copy.get(i);
			
			boolean childFound = false;
			// System.out.println("Dealing with clause " + clause.getClauseID());
			for (int j = 0; j < clause.getEvents().size(); j++){			
				if (clause.getEvents().get(j).getEvent().equals(event)){
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
	
	// DONE
	
	private static Integer heuristic_cost_estimate(Node o1){
		int elements = unknownElements(o1.getClause(), eventPool);
		Integer cost = new Integer(elements);
		return cost;		
	}
	
	
	// DONE
	private static void printAllLists(PriorityQueue<Node>  openSet, List<Node> closedSet){
		System.out.println("-------");
		System.out.println("OpenSet: ");
		for (int i = 0; i < openSet.size(); i++){
			System.out.println(openSet.poll().getClause().toString());
		}
		System.out.println("-------");
		System.out.println("ClosedSet: ");
		for (int i = 0; i < closedSet.size(); i++){
			closedSet.get(i).getClause().print();
		}
		System.out.println("-------");
		System.out.println("Knowledge base: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		}
		System.out.println("-------");
	}
		
	// DONE
	private static void printOpenSet(PriorityQueue<Node> openSet){
 		System.out.println();
 		System.out.println("--- Verifications for the openSet ---");
 		System.out.println("Size of the initial open set: " + openSet.size());
 		Object[] openSetArray = openSet.toArray();
 		for (int i = 0; i < openSetArray.length; i++){
 			ClauseNode clauseNode = (ClauseNode) openSetArray[i];
 			Clause clause = clauseNode.getClause();
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
	
	// DONE
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