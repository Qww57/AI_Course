package Heureka.InferenceEngine;

import java.util.PriorityQueue;

public abstract class Node implements Comparable<Node>{

	/* TODO refactor to use here a type Object that will change 
	 * depending on the implementing class */
	protected Clause _clause;
	protected PriorityQueue<Node> _frontier;
	protected Node _parent;
	public double _gScore;
	protected double _fScore;
	
	public Node(Clause clause){
		this._clause = clause;
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
	
	public Clause getClause() {
		return _clause;
	}
	
	public void setFScore(double fScore){
		_fScore = fScore;
	}
	
	public void setGScore(double gScore){
		_gScore = gScore;
	}
}