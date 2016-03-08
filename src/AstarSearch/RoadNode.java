package AstarSearch;

import java.awt.Point;
import java.util.*;

/**
 * Class defining nodes for the A* algorithm
 * 
 * @author Quentin
 *
 */

public class RoadNode implements Comparable<RoadNode>{
	private Point _point;

	private Queue<RoadNode> _frontier;
	
	private double goalDistance;
	
	RoadNode(Point point, Point goal){
		_point = point;
		goalDistance = _point.distance(goal);
		
		_frontier = new PriorityQueue<>();
		// Compute frontier list
	}

	@Override
	public int compareTo(RoadNode arg0) {
		// TODO return this.goalDistance.compareTo(arg0)
		return 0;
	}	
}
