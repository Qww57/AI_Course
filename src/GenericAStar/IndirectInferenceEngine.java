package GenericAStar;

import java.util.List;

import DirectInferenceEngine.Clause;

/**
 * This class is extending the {@link AStar} abstract class in order to 
 * deal with {@link Clause} objects. It is implementing an inference engine
 * based on refutation proof, tree structure and A* Algorithm.
 * 
 * In order to use this class, {@link ClauseNode} should be used.
 * 
 * When starting the AStar algorithm, the startAStar function should not have
 * any starting point since, this one is guessed from the knowledge base, but 
 * should have the goal defined.
 * 
 * @author Quentin
 *
 */
public class IndirectInferenceEngine extends AStar {

	public IndirectInferenceEngine(List<Clause> kBase) {
		super(kBase);
	}

	@Override
	protected void initializeSets(AbstractNode start, AbstractNode goal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean goalChecking(AbstractNode current, AbstractNode goal) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected List<AbstractNode> reconstructPath(AbstractNode current) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void findChildren(AbstractNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected double dist_between(AbstractNode node1, AbstractNode node2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double heuristic_cost_estimate(AbstractNode node, AbstractNode goal) {
		// TODO Auto-generated method stub
		return 0;
	}

}
