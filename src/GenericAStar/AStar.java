package GenericAStar;
import java.util.*;

import DirectInferenceEngine.Clause;
import GenericAStar.MapInfo;

/**
 * This class is an abstract class providing the basis of an A* Algorithm
 * and a set of methods that should be overridden in some new classes.
 * 
 * The class relies on a tree structure based on the {@link AbstractNode} class.
 * The class is able to deal with cost estimations using the variables fScore and gScore. 
 * fScore is the estimation of the future cost based on heuristic estimations. 
 * gScore is the previous cost needed to reach until the current node.
 * 
 * The A* algorithm based on the uniform A Star algorithm pseudo code described on:
 * https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode 
 * 
 * @author Quentin
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes", "boxing" })
public abstract class AStar{	
	
	/** Class members **/
	
	// List of roads specific to the PathFinging 	
	protected static List<Object> roads;
	
	// List of clauses specific to the InferenceEngine 
	protected static List<Clause> knowledgeBase = new ArrayList<Clause>();

	// Set of discovered nodes that needs to be evaluated
	protected static PriorityQueue<AbstractNode> openSet;
	
	// Set of node that have already been evaluated
	protected static List<AbstractNode> closedSet;
	
	// For each node, estimation of going from this node to the goal using the heuristic
	protected static HashMap<AbstractNode, Double> fScore;
	
	// For each node, the cost of getting from the start node to that node
	protected static HashMap<AbstractNode, Double> gScore;
	
	// Hash map remembering the previous node of the shortest path to get here
	protected static HashMap<AbstractNode, AbstractNode> cameFrom;
	
	// List of nodes as result of the solving algorithm
	protected static List<AbstractNode> results;
	
	/** Constructor **/
	
	/**
	 * Constructor used in order to initialize the A Star Algorithm by providing the
	 * data base to perform treatments on.
	 * 
	 * @param data - data base 
	 */
	public AStar(Object data){
		// Creating a copy of the list of the inputs in order to delete some without consequences
		if (data instanceof MapInfo){
			MapInfo mapInfo = (MapInfo) data;
			roads = new ArrayList(mapInfo.getRoads());
		}
		if (data instanceof List<?>){
			List<Clause> clauses = (List<Clause>) data;
			knowledgeBase = new ArrayList<Clause>(clauses);
		}
	}
	
	/** Public method **/
	
	/**
	 * Function used in order to start the A Star algorithm.
	 * 
	 * @param start - starting node to perform computations on
	 * @param goal - goal we want to reach using A star
	 * @return List of nodes to follow to reach the goal node from the starting node
	 */
	public List<AbstractNode> start(AbstractNode start, AbstractNode goal){
		System.out.println("");
		System.out.println("Start of A star Algorithm");
		
		long startTime = System.currentTimeMillis(); 
		
		initializeSets(start, goal);
	    int counter = 1;
		System.out.println("Starting the loop");
	    while (!openSet.isEmpty()){    	
	    	
	    	// Get first node from openSet using the comparable from the node
	    	AbstractNode current = openSet.poll();
	    	System.out.println("");
	    	System.out.println("Iteration number: " + counter + " for: " + current.toString());
	    	counter++;
	 	    
	    	// Doing, some treatments and checking if we are getting the goal
	 	    if (goalChecking(current, goal)){
	 	    	break;
	 	    }
	 	    	    	
	    	// Updating the sets
	    	openSet.remove(current);
	    	closedSet.add(current);
	    	
	    	// Find children of current
	    	findChildren(current);
	    	
			for(Iterator<AbstractNode> i = current.getChildren().iterator(); i.hasNext();) {
				AbstractNode child = i.next();
				
				if (closedSet.contains(child)){
					// Do nothing, already in the close set
				}
				else {
					// Distance from start to the child
					double tentative_gScore = gScore.get(current) + dist_between(current, child);
					
					// Child not into the openSet
					if (!openSet.contains(child)){											
						
						// Computing and saving the cost in the hash maps	
						double tentative_fScore = heuristic_cost_estimate(child, goal);
						gScore.put(child, tentative_gScore);
						fScore.put(child, tentative_fScore);
						
						// Saving the costs into the node
						child.setGScore(tentative_gScore);
						child.setFScore(tentative_fScore);
						
						// Adding the child node to the openSet
						openSet.add(child);
						cameFrom.put(child, current);
					}
					// Child is in the openSet
					else {
						// If, best way to get to this node
						if (gScore.get(child) > tentative_gScore){
							cameFrom.put(child, current);
							
							// Computing and saving the cost in the hash maps	
							double tentative_fScore = heuristic_cost_estimate(child, goal);							
							gScore.put(child, tentative_gScore);
							fScore.put(child, tentative_fScore);
							
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
	
	/** Abstract methods **/

	/**
	 * Function used in order to initialize all class variable considering the starting
	 * point of the algorithm (if there is explicitly one) and the goal.
	 * 
	 * @param start - input of the algorithm
	 * @param goal - goal we want to reach using A star
	 */
	protected abstract void initializeSets(AbstractNode start, AbstractNode goal);
	
	/**
	 * Function used to perform some treatments on the current node in order to check 
	 * if it this node has helped us to get the goal of the algorithm.
	 * 
	 * @param current - current node the algorithm is dealing with
	 * @param goal - goal that we want to reach using A star
	 * @return boolean - true if the algorithm has reached the goal, false if it has not
	 */
	protected abstract boolean goalChecking(AbstractNode current, AbstractNode goal);
	
	/**
	 * Function used to recreate the path to follow in order to reach the goal. 
	 * This function is used after the goal has been found in the algorithm and is based 
	 * on the cameFrom hashMap.
	 * 
	 * @param current - current node the algorithm is dealing with 
	 * @return List of nodes to follow to reach the goal node from the starting node
	 */
	protected abstract List<AbstractNode> reconstructPath(AbstractNode current);
	
	/**
	 * This function is used to construct the tree structure of the A Star algorithm.
	 * This function is used in order to find the possible node children among the object
	 * set given as input to the algorithm in the method startAStar.
	 * 
	 * @param node - current node the algorithm is dealing with 
	 */
	protected abstract void findChildren(AbstractNode node);
	
	/**
	 * This function is used in order to set the value of the gScore function inside the 
	 * main loop of the A star algorithm. It computes the distance between two nodes given 
	 * as input.
	 * 
	 * @param node1 - first node
	 * @param node2 - second node
	 * @return double - distance between the two nodes
	 */
	protected abstract double dist_between(AbstractNode node1, AbstractNode node2);
	
	/**
	 * Heuristic function used to estimate the future cost (fCost) between the current node 
	 * we are dealing with and the goal node.
	 * 
	 * @param current - current node the algorithm is dealing with 
	 * @param goal - goal we want to reach using A star
	 * @return double - future cost estimation of the current node
	 */
	protected abstract double heuristic_cost_estimate(AbstractNode node, AbstractNode goal);
	
	/** Printers **/
	
	/**
	 * Function used in order to print the openSet, closedSet and input base. Mainly used for
	 * development and debugging purposes.
	 */
	protected void printAllLists() {
		System.out.println("-------");
		System.out.println("OpenSet: ");
		for (int i = 0; i < openSet.size(); i++){
			System.out.println(openSet.poll().getObject().toString());
		}
		System.out.println("-------");
		System.out.println("ClosedSet: ");
		for (int i = 0; i < closedSet.size(); i++){
			System.out.println(closedSet.get(i).getObject().toString());
		}
		System.out.println("-------");
		System.out.println("Knowledge base: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		}
		System.out.println("-------");		
	}
}