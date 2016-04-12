package Heureka.AStar;
import java.awt.Point;
import java.util.PriorityQueue;

public abstract class Node implements Comparable<Node>{

	/* TODO refactor to use here a type Object that will change 
	 * depending on the implementing class */
	protected Point _position;
	protected PriorityQueue<Node> _frontier;
	protected Node _parent;
	public double _gScore;
	protected double _fScore;
	
	public Node(Point position){
		this._position = position;
		_frontier = new PriorityQueue<>();
	}
	
	public double getGScore(){
		return _gScore;
	}
	
	public double getFScore(){
		return _fScore;
	}
	
	public PriorityQueue<Node> getChildren(){
		return _frontier;
	}
	
	public void addChild(Node child){
		if (!_frontier.contains(child))
			_frontier.add(child);
		else 
			System.out.println("Child already in the frontier");
	}
	
	public void setParent(Node parent){
		_parent = parent;
	}
	
	public Point getPosition() {
		return _position;
	}
	
	public void setFScore(double fScore){
		_fScore = fScore;
	}
	
	public void setGScore(double gScore){
		_gScore = gScore;
	}
}