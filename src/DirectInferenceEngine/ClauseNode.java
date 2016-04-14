package DirectInferenceEngine;


public class ClauseNode extends Node {

	public ClauseNode(Clause clause) {
		super(clause);
	}
	
	@Override
	public int compareTo(Node o) {
		if ((_gScore + _fScore) > (o.getGScore() + o.getFScore()))
			return 1; 
		if ((_gScore + _fScore) < (o.getGScore() + o.getFScore()))
			return -1; 
		return 0;
	}
}
