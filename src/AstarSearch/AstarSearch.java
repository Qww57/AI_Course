package AstarSearch;

import java.awt.Point;
import java.util.*;

/**
 * A* algorithm
 * 
 * @author Quentin
 *
 */
public class AstarSearch {	
	
	//Global ones for the algorithm
	private PriorityQueue<RoadNode> frontierQueue;
	private List<RoadNode> explored;
	
	public void AstarSearch(MapInfo map, Point startPoint, Point goal){
		RoadNode currentNode = new RoadNode(startPoint, goal);
		explored = new ArrayList<RoadNode>();
		frontierQueue = new PriorityQueue<>();
		
	}
	
	public void findChildren(RoadNode roadNode){
		
	}
	
}
