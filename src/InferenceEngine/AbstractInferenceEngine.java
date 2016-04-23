package InferenceEngine;

import java.util.ArrayList;
import java.util.List;

import AbstractAStar.AbstractAStar;
import AbstractAStar.AbstractNode;

public abstract class AbstractInferenceEngine extends AbstractAStar {

	protected static List<Event> eventPool = new ArrayList<Event>();
	
	public AbstractInferenceEngine(Object data) {
		super(data);
	}

	protected abstract void initializeSets(AbstractNode start, AbstractNode goal);

	protected abstract boolean goalChecking(AbstractNode current, AbstractNode goal);

	protected abstract void findChildren(AbstractNode node);
	
	@Override
	protected double dist_between(AbstractNode node1, AbstractNode node2){
		// Not used inside inference engines
		return 0;
	}
	
	@Override
	protected List<AbstractNode> reconstructPath(AbstractNode current){
		// Not used inside inference engines
		return null;
	}

	protected abstract double heuristic_cost_estimate(AbstractNode node, AbstractNode goal);

	/* Protected methods */
	
	protected static void printOpenSet(){
 		System.out.println();
 		System.out.println("--- Verifications for the openSet ---");
 		System.out.println("Size of the initial open set: " + openSet.size());
 		Object[] openSetArray = openSet.toArray();
 		for (int i = 0; i < openSetArray.length; i++){
 			ClauseNode clauseNode = (ClauseNode) openSetArray[i];
 			Clause clause = (Clause) clauseNode.getObject();
 			System.out.println(clause.toString() 
 					+ " - F: " + clauseNode.getFScore() 
 					+ " - G: " + clauseNode.getGScore());
 		}
 		System.out.println();
	}
	
	protected static void printEventPool(){
		System.out.println();
		System.out.println("--- Verifications for the eventPool ---");
		System.out.println("Size of the initial true set: " + eventPool.size());
		for (int i = 0; i < eventPool.size(); i++){
			eventPool.get(i).print();
		}
		System.out.println();
	}
}