package BasicInferenceEngine;

import java.util.*;

import BasicInferenceEngine.Clause.State;
import BasicInferenceEngine.Node;

/**
 * A* algorithm based on the uniform A Star algorithm pseudo code described on:
 * https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode 
 * 
 * @author Quentin
 *
 */
@SuppressWarnings("unused")
public class AstarSearchEngine {	
	
	/* Different types of heuristic implemented
	 * 
	 */
	public enum Heuristic{
		NULL
	}
	
	// TODO implement all things related in FindChildren, heuristic etc
	public enum Type{
		PATHFINDING, 
		INFERENCE
	}
		
	private static List<Node> results;
	private static Heuristic chosenHeuristic;
	private static Type researchType;
	private static List<Clause> knowledgeBase = new ArrayList<Clause>();
	
	public AstarSearchEngine(List<Clause> kBase, Heuristic heuristic, Type type){
		// Creating a copy of the list of roads in order to delete some without consequences
		chosenHeuristic = heuristic;
		researchType = type;
		knowledgeBase = new ArrayList<Clause>(kBase);
	}
	
	/**
	 * 
	 * @param start
	 * @param goal
	 * @return
	 */
	public List<Node> startAstar(Node goal){
		System.out.println("");
		System.out.println("Start of A star Algorithm");
		
		long startTime = System.currentTimeMillis(); 
		
		// Set of node that have already been evaluated
		List<Node> closedSet = new ArrayList<>();
		
		// Set of discovered nodes that needs to be evaluated
		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		for (int i =0; i< knowledgeBase.size(); i++){
			Clause clause = knowledgeBase.get(i);
			if (clause.getState() == State.TRUE){
				openSet.add(new ClauseNode(clause));
				knowledgeBase.remove(i);
			}
		}
		
		// Hashmap remembering the previous node of the shortest path to get here
	    HashMap<Node, Node> cameFrom = new HashMap<Node, Node>();
	    
	    // For each node, the cost of getting from the start node to that node
	    HashMap<Node, Distance> gScore = new HashMap<Node, Distance>();
	    
	    // For each node, the total cost of getting from the start node to the goal
	    HashMap<Node, Distance> fScore = new HashMap<Node, Distance>();	
	   	    
	    while (!openSet.isEmpty()){
	    	// Get first node from openSet using the comparable from the node
	    	Node current = openSet.poll();
	    	
	    	System.out.println("Checking the node: " + current.getClause().toString());
	    	
	    	gScore.put(current, new Distance(0));
	    	fScore.put(current, new Distance (heuristic_cost_estimate(current, goal)));
	    	current.setFScore(fScore.get(current).getDistance());
	    	current.setGScore(gScore.get(current).getDistance());
	    		 
	    	// Checking if we are getting the goal
	    	if (current.getClause().getResult().equals(goal.getClause().getResult())){
	    		cameFrom.put(goal, current);
	    		results = reconstructPath(cameFrom, goal);	    		
	    		// printAllLists(openSet, closedSet);
	    		break;
	    	}
	    	
	    	// Updating the sets
	    	openSet.remove(current);
	    	closedSet.add(current);
	    	
	    	// Find children of current
	    	findChildren(current);
	    	
			for(Iterator<Node> i = current.getChildren().iterator(); i.hasNext();) {
				Node child = i.next();
				
				if (child.getClause().getState().equals(State.FALSE))
					closedSet.add(child);
				
				if (closedSet.contains(child)){
					// Do nothing, already in the close set 
				}
				else {
					// Distance from start to the child
					double tentative_gScore = gScore.get(current).getDistance() 
							+ dist_between(current, child);
					
					// Child not into the openSet
					if (!openSet.contains(child)){						
						openSet.add(child);
						cameFrom.put(child, current);
						
						// Computing and saving the cost in the hash maps	
						double tentative_fScore = heuristic_cost_estimate(child, goal);
						gScore.put(child, new Distance(tentative_gScore));
						fScore.put(child, new Distance(tentative_fScore));
						
						// Saving the costs into the node
						child.setGScore(tentative_gScore);
						child.setFScore(tentative_fScore);
					}
					// Child is in the openSet
					else {
						// If, best way to get to this node
						if (gScore.get(child).getDistance() > tentative_gScore){
							cameFrom.put(child, current);
							
							// Computing and saving the cost in the hash maps	
							double tentative_fScore = heuristic_cost_estimate(child, goal);							
							gScore.put(child, new Distance(tentative_gScore));
							fScore.put(child, new Distance(tentative_fScore));
							
							// Saving the costs into the node
							child.setGScore(tentative_gScore);
							child.setFScore(tentative_fScore);
						}
					}
				}   
		    }
	    }
	    
	    long longTime = System.currentTimeMillis(); 
	    System.out.println("End of A star Algorithm in " + (longTime - startTime) 
	    		+ " ms using " + (closedSet.size() + 1 )+ " nodes");
		
	    return results; 
	}
	
	/**
	 * 
	 * @param cameFrom
	 * @param current
	 * @return
	 */
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

	// TODO refactor to be more general and deal with all types of node not only roadNode
	private static void findChildren(Node node){
		ClauseNode clauseNode = (ClauseNode) node;
		for (int i = 0; i < knowledgeBase.size(); i++){
			Clause clause = knowledgeBase.get(i);
			String event = clauseNode.getClause().getResult();
			if (clause.getConditions().contains(event)){
				ClauseNode child = new ClauseNode(clause);
				child.getClause().setState(State.TRUE);
				child.setParent(node);
				node.addChild(child);
				knowledgeBase.remove(i);
			}
			String negation = "< " + event;
			if (clause.getConditions().contains(negation)){
				ClauseNode child = new ClauseNode(clause);
				child.getClause().setState(State.FALSE);
				child.setParent(node);
				node.addChild(child);
				knowledgeBase.remove(i);
			}
		}
	}
	
	// TODO refactor to be more general
	private static double dist_between(Node o1, Node o2){
		return 1;
	}
	
	// TODO refactor to be more general
	private static double heuristic_cost_estimate(Node o1, Node goal){
		switch (chosenHeuristic){
			case NULL:
				return 0;
			default:
				return 0;
		}	
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