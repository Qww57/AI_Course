package InferenceEngine;
import AbstractAStar.AbstractAStar;
import AbstractAStar.AbstractNode;

/**
 * ClauseNode is a class extending the {@link AbstractNode} class 
 * in order to be able to use the {@link Clause} classes inside the
 * {@link DirectInferenceEngine} which extends the {@link AbstractAStar} 
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
		double score = _gScore + _fScore;
		double otherScore = o.getGScore() + o.getFScore();
		if (score > otherScore)
			return 1; 
		else if (score < otherScore)
			return -1; 
		else
			return 0;
	}
	
	@Override
	public String toString(){
		return "Clause node " + ((Clause) this._object).toString();
	}
}
