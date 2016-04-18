package GenericAStar;
import DirectInferenceEngine.Clause;

/**
 * ClauseNode is a class extending the {@link AbstractNode} class 
 * in order to be able to use the {@link Clause} classes inside the
 * {@link DirectInferenceEngine} which extends the {@link AStar} 
 * abstract class.
 * 
 * @author Quentin
 *
 */
public class ClauseNode extends AbstractNode {

	public ClauseNode(Clause clause) {
		super(clause);
	}
	
	@Override
	public int compareTo(AbstractNode o) {
		if ((_gScore + _fScore) > (o.getGScore() + o.getFScore()))
			return 1; 
		if ((_gScore + _fScore) < (o.getGScore() + o.getFScore()))
			return -1; 
		return 0;
	}
}
