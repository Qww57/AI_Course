package PathFinding;
import java.awt.Point;

import AbstractAStar.AbstractAStar;
import AbstractAStar.AbstractNode;

/**
 * RoadNode is a class extending the {@link AbstractNode} class 
 * in order to be able to use the end point of a {@link Road} element
 * as a node inside the {@link PathFinding} class (which extends 
 * {@link AbstractAStar}).
 * 
 * @author Quentin
 *
 */
public class RoadNode extends AbstractNode {
		
	public RoadNode(Point point){
		super(point);	
	}
	
	@Override
	public int compareTo(AbstractNode o) {
		if ((_gScore + _fScore) > (o.getGScore() + o.getFScore()))
			return 1; 
		if ((_gScore + _fScore) < (o.getGScore() + o.getFScore()))
			return -1; 
		return 0;
	}
	
	@Override
	public String toString(){
		return "Road node " + ((Point) this._object).toString();
	}
}