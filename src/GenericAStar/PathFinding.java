package GenericAStar;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import GenericAStar.MapInfo;
import PathFinding.Road;

public class PathFinding extends AStar {

	public PathFinding(MapInfo mapInfo) {
		super(mapInfo);
	}

	@SuppressWarnings("boxing")
	@Override
	protected void initializeSets(AbstractNode start, AbstractNode goal){
		openSet = new PriorityQueue<AbstractNode>();
		openSet.add(start);
		
		closedSet = new ArrayList<>();	
		cameFrom = new HashMap<AbstractNode, AbstractNode>();	    
	    gScore = new HashMap<AbstractNode, Double>();	    
	    fScore = new HashMap<AbstractNode, Double>();
	    
	    gScore.put(start, (double) 0);
    	fScore.put(start, heuristic_cost_estimate(start, goal));
    	start.setGScore(gScore.get(start));
    	start.setFScore(fScore.get(start));
	}

	@Override
	protected List<AbstractNode> reconstructPath(HashMap<AbstractNode, AbstractNode> _cameFrom, AbstractNode current) {
		System.out.println("Solution found - Reconstructing the path: ");
		
		List<AbstractNode> path = new ArrayList<AbstractNode>();
		AbstractNode currentNode = current;
		while (_cameFrom.getOrDefault(currentNode, null) != null){
			AbstractNode previousNode = _cameFrom.get(currentNode);
			path.add(previousNode);
			currentNode = previousNode;
		}
		System.out.println("Number of steps : " + path.size());
		
		Collections.reverse(path);		
		
		return path;
	}
	
	@Override
	protected void printAllLists(PriorityQueue<AbstractNode>  _openSet, List<AbstractNode> _closedSet){
		/* System.out.println("-------");
		System.out.println("OpenSet: ");
		for (int i = 0; i < _openSet.size(); i++){
			System.out.println(_openSet.poll().toString());
		}
		System.out.println("-------");
		System.out.println("ClosedSet: ");
		for (int i = 0; i < _closedSet.size(); i++){
			System.out.println(_closedSet.get(i).toString());
		}
		System.out.println("-------");
		System.out.println("Map base: ");
		for (int i = 0; i < roads.size(); i++){
			System.out.println(roads.get(i).toString());
		}
		System.out.println("-------"); */
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
		java.awt.Point point1 = (java.awt.Point) o1.getObject();
		java.awt.Point point2 = (java.awt.Point) o2.getObject();
		double dist = point1.getLocation().distance(point2);
		if (dist < 0)
			return -dist;
		return dist;
	}
	
	@Override 
	protected double heuristic_cost_estimate(AbstractNode o1, AbstractNode goal){
		Point point1 = (Point) o1.getObject();
		Point point2 = (Point) goal.getObject();
		return point1.distance(point2);	
	}

	@Override
	protected boolean goalChecking(AbstractNode current, AbstractNode goal){
    	// Checking if we are getting the goal
    	if (current.getObject().equals(goal.getObject())){
    		cameFrom.put(goal, current);
    		results = reconstructPath(cameFrom, goal);
    		printAllLists(openSet, closedSet);
    		return true;
    	}
    	return false;
	}
}
