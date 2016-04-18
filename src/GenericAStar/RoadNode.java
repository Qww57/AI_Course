package GenericAStar;
import java.awt.Point;

/**
 * RoadNode is a class extending the {@link AbstractNode} class 
 * in order to be able to use the end point of a {@link Road} element
 * as a node inside the {@link PathFinding} class (which extends 
 * {@link AStar}).
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
		if ((_gScore + _fScore) > (o._gScore + o._fScore))
			return 1; 
		if ((_gScore + _fScore) < (o._gScore + o._fScore))
			return -1; 
		return 0;
	}
}