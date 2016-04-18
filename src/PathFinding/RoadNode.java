package PathFinding;

import java.awt.Point;

import PathFinding.Node;

/**
 * Class defining nodes for the A* algorithm
 * 
 * @author Quentin
 *
 */

public class RoadNode extends Node {
		
	public RoadNode(Point point){
		super(point);	
	}
	
	/* TODO should override the getPosition returning the right output type */
	/*public Point getPosition(){
		return (Point) _property;
	}
	
	public void setPosition(Point position){
		_property = position;
	}*/
	
	@Override
	public int compareTo(Node o) {
		if ((_gScore + _fScore) > (o._gScore + o._fScore))
			return 1; 
		if ((_gScore + _fScore) < (o._gScore + o._fScore))
			return -1; 
		return 0;
	}
}