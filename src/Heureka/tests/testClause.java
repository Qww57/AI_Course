package Heureka.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import Heureka.InferenceEngine.AstarSearchEngine.Heuristic;
import Heureka.InferenceEngine.AstarSearchEngine.Type;
import Heureka.InferenceEngine.AstarSearchEngine;
import Heureka.InferenceEngine.Node;
import Heureka.InferenceEngine.Clause;
import Heureka.InferenceEngine.ClauseNode;

public class testClause {

	private List<Clause> knowledgeBase = new ArrayList<Clause>();
	
	@SuppressWarnings("unused")
	@Test
	public void testExamples(){
		
		// Creating the knowledge base
		List<String>  conditions = Arrays.asList();
		Clause clause4 = new Clause(conditions, "0");
		knowledgeBase.add(clause4);
		
		conditions = Arrays.asList();
		Clause clause5 = new Clause(conditions, "1");
		knowledgeBase.add(clause5);
		
		conditions = Arrays.asList("0", "1");
		Clause clause6 = new Clause(conditions, "2");
		knowledgeBase.add(clause6);
		
		conditions = Arrays.asList("2", "5");
		Clause clause7 = new Clause(conditions, "3");
		knowledgeBase.add(clause7);
		
		conditions = Arrays.asList("3");
		Clause clause8 = new Clause(conditions, "5");
		knowledgeBase.add(clause8);
		
		conditions = Arrays.asList("5");
		Clause clause11 = new Clause(conditions, "10");
		knowledgeBase.add(clause11);
		
		conditions = Arrays.asList("17");
		Clause clause10 = new Clause(conditions, "15");
		knowledgeBase.add(clause10);
		
		conditions = Arrays.asList("10");
		Clause clause9 = new Clause(conditions, "15");
		knowledgeBase.add(clause9);
		
		conditions = Arrays.asList("2");
		Clause clause2 = new Clause(conditions, "< 17");
		knowledgeBase.add(clause2);
		
		conditions = Arrays.asList("2", "3");
		Clause clause3 = new Clause(conditions, "45");
		knowledgeBase.add(clause3);
		
		conditions = Arrays.asList("15");
		Clause goal = new Clause(conditions, "goal");
		knowledgeBase.add(goal);
		
		// Printing the knowledge base
		System.out.println("Print KB: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		}
		
		// Start the A star algorithm
		AstarSearchEngine search = new AstarSearchEngine(knowledgeBase, Heuristic.NULL, Type.PATHFINDING);
		ClauseNode goalNode = new ClauseNode(goal);
		List<Node> results = search.startAstar(goalNode);		
	}
	
	@SuppressWarnings("unused")
	private void example1(){
		List<String> conditions = Arrays.asList("b", "c");
		Clause goal = new Clause(conditions, "a");
		knowledgeBase.add(goal);
		
		conditions = Arrays.asList("b");
		Clause clause2 = new Clause(conditions, "b");
		knowledgeBase.add(clause2);
		
		conditions = Arrays.asList("c", "d");
		Clause clause3 = new Clause(conditions, "b");
		knowledgeBase.add(clause3);
		
		conditions = Arrays.asList();
		Clause clause4 = new Clause(conditions, "c");
		knowledgeBase.add(clause4);
		
		conditions = Arrays.asList();
		Clause clause5 = new Clause(conditions, "d");
		knowledgeBase.add(clause5);
	}
}
