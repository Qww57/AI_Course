package GenericAStar;

import java.util.PriorityQueue;

/**
 * Class defining an abstract node that is used with {@link AStar} 
 * This class centered around some variables used in order to manage
 * with true structure (children, parent), elements linked to cost estimations
 * (gScore: previous cost and fScore: future cost estimation) and an object
 * that the node is describing.
 * 
 * @author Quentin
 *
 */
public abstract class AbstractNode implements Comparable<AbstractNode>{
	
	/* Class variables */
	
	protected Object _object;
	
	protected PriorityQueue<AbstractNode> _children;
	
	protected AbstractNode _parent;
	
	protected double _gScore;
	
	protected double _fScore;
	
	/* Constructor */
	
	public AbstractNode(Object object){
		this._object = object;
		_children = new PriorityQueue<>();
	}
	
	/* Getters and setters */
	
	public double getGScore(){
		return _gScore;
	}
	
	public double getFScore(){
		return _fScore;
	}
	
	public PriorityQueue<AbstractNode> getChildren(){
		return _children;
	}
	
	public void addChild(AbstractNode child){
		if (!_children.contains(child))
			_children.add(child);
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