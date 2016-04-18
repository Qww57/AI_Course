package GenericAStar;
import DirectInferenceEngine.Clause;

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
