package GenericAStar;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import GenericAStar.MapInfo;
import PathFinding.Road;

/**
 * This class is extending the {@link AStar} abstract class in order to 
 * deal with {@link Road} objects. It is implementing a path finding algorithm
 * based on tree structure and A* Algorithm.
 * 
 * In order to use this class, {@link RoadNode} should be used.
 * 
 * @author Quentin
 *
 */
@SuppressWarnings("boxing")
public class PathFinding extends AStar {

	public PathFinding(MapInfo mapInfo) {
		super(mapInfo);
	}

	@Override
	protected void initializeSets(AbstractNode start, AbstractNode goal){
		
		// Initializing the openSet with the starting point
		openSet = new PriorityQueue<AbstractNode>();
		openSet.add(start);
		
		// Initializing the other sets as empty lists or hash maps
		closedSet = new ArrayList<>();	
		cameFrom = new HashMap<AbstractNode, AbstractNode>();	    
	    gScore = new HashMap<AbstractNode, Double>();	    
	    fScore = new HashMap<AbstractNode, Double>();
	    
	    // Setting the fCost and gCost of the starting point
	    gScore.put(start, (double) 0);
    	fScore.put(start, heuristic_cost_estimate(start, goal));
    	start.setGScore(gScore.get(start));
    	start.setFScore(fScore.get(start));
	}

	@Override
	protected List<AbstractNode> reconstructPath(AbstractNode current) {
		System.out.println("Solution found - Reconstructing the path: ");
		
		List<AbstractNode> path = new ArrayList<AbstractNode>();
		AbstractNode currentNode = current;
		while (cameFrom.getOrDefault(currentNode, null) != null){
			AbstractNode previousNode = cameFrom.get(currentNode);
			path.add(previousNode);
			currentNode = previousNode;
		}
		System.out.println("Number of steps : " + path.size());
		
		Collections.reverse(path);		
		
		return path;
	}
	
	@Override
	protected void findChildren(AbstractNode node){
		
		for (int i = 0; i < roads.size(); i++){
			Road currentRoad = (Road) roads.get(i);
			if (currentRoad.getStartingPoint().equals(node.getObject())){
				RoadNode child = new RoadNode(currentRoad.getEndPoint());
				child.setParent(node);
				node.addChild(child);
				roads.remove(i);
			}
		}
	}
	
	@Override 
	protected double dist_between(AbstractNode o1, AbstractNode o2){
		Point point1 = (Point) o1.getObject();
		Point point2 = (Point) o2.getObject();
		double dist = point1.getLocation().distance(point2);
		if (dist < 0)
			return -dist;
		return dist;
	}
	
	@Override 
	protected double heuristic_cost_estimate(AbstractNode o1, AbstractNode goal){
		Point point1 = (Point) o1.getObject();
		Point point2 = (Point) goal.getObject();
		double dist = point1.getLocation().distance(point2);
		if (dist < 0)
			return -dist;
		return dist;
	}

	@Override
	protected boolean goalChecking(AbstractNode current, AbstractNode goal){
    	if (current.getObject().equals(goal.getObject())){
    		cameFrom.put(goal, current);
    		results = reconstructPath(goal);
    		printAllLists();
    		return true;
    	}   	
    	return false;
	}
}
