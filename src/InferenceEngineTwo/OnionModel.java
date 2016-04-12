package InferenceEngineTwo;

import java.lang.reflect.Array;
import java.util.*;

import InferenceEngineTwo.ClauseNode;
import InferenceEngineTwo.Clause;
import InferenceEngineTwo.Event.Status;
import InferenceEngineTwo.Node;

/**
 * A* algorithm based on the uniform A Star algorithm pseudo code described on:
 * https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode 
 * 
 * @author Quentin
 *
 */
@SuppressWarnings("unused")
public class OnionModel {	
			
	private static List<Node> results;
	private static List<Clause> knowledgeBase = new ArrayList<Clause>();
	private static List<Event> eventBase = new ArrayList<Event>();
	
	public OnionModel(List<Clause> kBase, List<Event> eBase){
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
		List<Event> trueSet = new ArrayList<>();
		
		// Initializing the true set 
		List<Clause> copy = new ArrayList<Clause>(knowledgeBase);
		for (int i = 0; i < copy.size(); i++){
			Clause clause = copy.get(i);
			// Getting the clause which are simple ones
			if (clause.getEvents().size() == 1){
				System.out.println("Clause of size 1: " + clause.toString());
				ClauseEvent clauseEvent = clause.getEvents().get(0);
				Event simpleEvent = clauseEvent.getEvent();
				if (clauseEvent.isCondition() == true){
					simpleEvent.setStatus(Status.TRUE);
				}
				else 
					simpleEvent.setStatus(Status.FALSE);
				trueSet.add(simpleEvent);
				knowledgeBase.remove(clause);
			}
		}
		
		// Verifications
		System.out.println();
		System.out.println("--- Verifications for the trueSet ---");
		System.out.println("Size of the initial true set: " + trueSet.size());
		for (int i = 0; i < trueSet.size(); i++){
			trueSet.get(i).print();
		}
		System.out.println("Clausal KB inside A Star after true set initialization: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		}
		
		// Set of discovered nodes that needs to be evaluated
		List<Clause> openSet = new ArrayList<Clause>();
		
		// Initializing the openSet
		copy = new ArrayList<Clause>(knowledgeBase);
		for (int i =0; i< copy.size(); i++){
			Clause clause = copy.get(i);
			if (isInteresting(trueSet, clause)){
				openSet.add(clause);
				knowledgeBase.remove(clause);
			}
		}
		
		// Verifications of the openSet
		System.out.println();
		System.out.println("--- Verifications for the openSet ---");
		System.out.println("Size of the initial open set: " + openSet.size());
		Object[] openSetArray = openSet.toArray();
		for (int i = 0; i < openSetArray.length; i++){
			Clause clause = (Clause) openSetArray[i];
			clause.print();
		}
		System.out.println("Clausal KB inside A Star after open set initialization: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		}
		
		// Treatment
		while(!openSet.isEmpty()){
			Clause clause = getBestClause(openSet);
		}
	    
		
		// End of the Algorithm
		
	    long longTime = System.currentTimeMillis(); 
	    System.out.println("End of inference engine in " + (longTime - startTime) 
	    		+ " ms using " + (closedSet.size() + 1 )+ " nodes");
		
	    return results; 
	}
	
	/**
	 * Return best clause and remove it from openSet
	 * @param openSet
	 * @return
	 */
	private Clause getBestClause(List<Clause> openSet) {
		// TODO Auto-generated method stub
		return null;
	}

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
	
	private static boolean isResolvable(Clause clause, List<Event> trueSet){
		if (clause.getEvents().size() - notMatchingElements(clause, trueSet) == 1)
			return true;
		return false;
	}
	
	/**
	 * Return the number of events that are for sure not satisfied
	 */
	private static int notMatchingElements(Clause clause, List<Event> trueSet){
		int elements = 0;
		for(int i = 0; i < elements; i++){
			ClauseEvent clauseEvent = clause.getEvents().get(i);
			
			// if should be true, is in the true event pool as true
			if (clauseEvent.isCondition() == true 
				&& clauseEvent.getEvent().getStatus() == Status.FALSE
				&& trueSet.contains(clauseEvent.getEvent())){
				elements = elements + 1;
			}
			// if should be false, is in the true event pool as false
			if (clauseEvent.isCondition() == false 
				&& clauseEvent.getEvent().getStatus() == Status.TRUE
				&& trueSet.contains(clauseEvent.getEvent())){
				elements = elements + 1;
			}
		}
		return elements;
	}
	
	/**
	 * Return the number of events that are still unknown
	 */
	private static int unknownElements(Clause clause, List<Event> trueSet){
		int elements = 0;
		for(int i = 0; i < elements; i++){
			ClauseEvent clauseEvent = clause.getEvents().get(i);
			
			// if should be true, is in the true event pool as true
			if (clauseEvent.getEvent().getStatus() == Status.UNKWON
				&& trueSet.contains(clauseEvent.getEvent())){
				elements = elements + 1;
			}
			// if should be false, is in the true event pool as false
			if (clauseEvent.isCondition() == false 
				&& clauseEvent.getEvent().getStatus() == Status.TRUE
				&& trueSet.contains(clauseEvent.getEvent())){
				elements = elements + 1;
			}
		}
		return elements;
	}
	
	/**
	 * Children of a node are the clause from the knowledgeBase 
	 * Where at least on of their events is in the parent node
	 * and that is resolvable
	 * @param node
	 */
	private static void findChildren(Node node){
		ClauseNode clauseNode = (ClauseNode) node;		
	}
	
	private static double dist_between(Node o1, Node o2){
		return 1;
	}
	
	private static double heuristic_cost_estimate(Node o1, Node goal){
		return 0;		
	}
	
	private static void printAllLists(PriorityQueue<Node>  openSet, List<Node> closedSet){
		System.out.println("-------");
		System.out.println("OpenSet: ");
		for (int i = 0; i < openSet.size(); i++){
			System.out.println(openSet.poll().toString());
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
}

/**
 * Class created in order to deal with double inside of HashMaps.
 * This cannot be done directly with double.
 * 
 * @author Quentin
 *
 */
class Distance{
	
	private double distance;
	
	public Distance(double distance){
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}	
}