package BasicInferenceEngine;

public class ClauseNode extends Node{

	public ClauseNode(Clause clause) {
		super(clause);
	}
	
	@Override
	public int compareTo(Node o) {
		double score = _gScore + _fScore;
		double otherScore = o.getGScore() + o.getFScore();
		if (score > otherScore)
			return 1; 
		else if (score < otherScore)
			return -1; 
		else
			return 0;
	}
}
