package AstarSearch;

import java.awt.Point;
import java.util.*;

/**
 * Class defining nodes for the A* algorithm
 * 
 * @author Quentin
 *
 */

public class RoadNode extends Node implements Comparable<RoadNode>{
	
	private int _id;
	
	private Point _position;

	private PriorityQueue<RoadNode> _frontier;
	
	private RoadNode _parent;
	
	private double goalDistance;
	
	RoadNode(Point point, Point goal, int roadId){
		_position = point;
		_id = roadId;
		goalDistance = _position.distance(goal);		
		_frontier = new PriorityQueue<>();
	}
	
	public int getId(){
		return _id;
	}
	
	public Point getPosition(){
		return _position;
	}
	
	public PriorityQueue<RoadNode> getChildren(){
		return _frontier;
	}
	
	public double getDistanceGoal(){
		return goalDistance;
	}
	
	// use poll to get the right one
	public void setChildren(PriorityQueue<RoadNode> children){
		_frontier =  children;
	}
	
	public void setParent(RoadNode parent){
		_parent = parent;
	}
	
	public RoadNode getParent(){
		return _parent;
	}
	
	@Override
	public int compareTo(RoadNode o) {
		if (this.goalDistance > o.goalDistance){ return -1; }
		if (this.goalDistance < o.goalDistance){ return 1; }
		return 0;
	}

}
