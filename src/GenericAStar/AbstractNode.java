package GenericAStar;

import java.util.PriorityQueue;

public abstract class AbstractNode implements Comparable<AbstractNode>{

	protected Object _object;
	protected PriorityQueue<AbstractNode> _frontier;
	protected AbstractNode _parent;
	public double _gScore;
	protected double _fScore;
	
	public AbstractNode(Object object){
		this._object = object;
		_frontier = new PriorityQueue<>();
	}
	
	public double getGScore(){
		return _gScore;
	}
	
	public double getFScore(){
		return _fScore;
	}
	
	public PriorityQueue<AbstractNode> getChildren(){
		return _frontier;
	}
	
	public void addChild(AbstractNode child){
		if (!_frontier.contains(child))
			_frontier.add(child);
		else 
			System.out.println("Child already in the frontier");
	}
	
	public void setParent(AbstractNode parent){
		_parent = parent;
	}
	
	public AbstractNode getParent(){
		return _parent;
	}
	
	public Object getObject() {
		return _object;
	}
	
	public void setFScore(double fScore){
		_fScore = fScore;
	}
	
	public void setGScore(double gScore){
		_gScore = gScore;
	}
}