package GenericAStar;
import java.util.*;

import DirectInferenceEngine.Clause;
import GenericAStar.MapInfo;

	/**
 * A* algorithm based on the uniform A Star algorithm pseudo code described on:
 * https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode 
 * 
 * @author Quentin
 *
 */
public abstract class AStar{	
	
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
	
	// Hashmap remembering the previous node of the shortest path to get here
	protected static HashMap<AbstractNode, AbstractNode> cameFrom;
	
	// List of nodes as result of the solving algorithm
	protected static List<AbstractNode> results;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@SuppressWarnings("boxing")
	public List<AbstractNode> startAstar(AbstractNode start, AbstractNode goal){
		System.out.println("");
		System.out.println("Start of A star Algorithm");
		
		long startTime = System.currentTimeMillis(); 
		
		initializeSets(start, goal);
	    
	    while (!openSet.isEmpty()){
	    	// Get first node from openSet using the comparable from the node
	    	AbstractNode current = openSet.poll();
	    	
	 	    // Checking if we are getting the goal
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
						openSet.add(child);
						cameFrom.put(child, current);
						
						// Computing and saving the cost in the hash maps	
						double tentative_fScore = heuristic_cost_estimate(child, goal);
						gScore.put(child, tentative_gScore);
						fScore.put(child, tentative_fScore);
						
						// Saving the costs into the node
						child.setGScore(tentative_gScore);
						child.setFScore(tentative_fScore);
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
	
	protected abstract boolean goalChecking(AbstractNode current, AbstractNode goal);

	protected abstract void initializeSets(AbstractNode start, AbstractNode goal);

	protected abstract List<AbstractNode> reconstructPath(HashMap<AbstractNode, AbstractNode> _cameFrom, AbstractNode current);
	
	protected abstract void printAllLists(PriorityQueue<AbstractNode>  _openSet, List<AbstractNode> _closedSet);
	
	protected abstract void findChildren(AbstractNode node);
	
	protected abstract double dist_between(AbstractNode o1, AbstractNode o2);
	
	protected abstract double heuristic_cost_estimate(AbstractNode o1, AbstractNode goal);
}