package GenericAStar;
import java.awt.Point;

/**
 * Class defining nodes for the A* algorithm
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