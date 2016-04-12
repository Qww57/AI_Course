package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import InferenceEngineTwo.Node;
import InferenceEngineTwo.AstarSearchEngine;
import InferenceEngineTwo.AstarSearchEngine.Type;
import InferenceEngineTwo.AstarSearchEngine.Heuristic;
import InferenceEngineTwo.Clause;
import InferenceEngineTwo.ClauseEvent;
import InferenceEngineTwo.Event;

public class testClause2 {

	private List<Clause> knowledgeBase = new ArrayList<Clause>();
	
	@Test
	public void testClause(){
		
		// Creating the event base
		CreateKnowledgeBase();
		
		// Printing the knowledge base
		System.out.println("Print clausal KB: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		}
		
		// Start the A star algorithm
		AstarSearchEngine search = new AstarSearchEngine(knowledgeBase, Heuristic.NULL, Type.PATHFINDING);
		List<Node> results = search.startAstar();
	}
	
	private void CreateKnowledgeBase(){ 
		
		// Creating the events
		Event breakfast = new Event("breakfast");		 
		Event juice = new Event("juice");		 
		Event hotdrink = new Event("hotdrink");
		Event tea = new Event("tea");
		Event coffee = new Event("coffee");
		Event cream = new Event("cream");		 
		Event food = new Event("food");
		Event toast = new Event("toast");
		Event egg = new Event("egg");
		Event butter = new Event("butter");
		
		// Creating the clausal forms
		List<ClauseEvent> eventsOfOne = new ArrayList<ClauseEvent>();
		eventsOfOne.add(new ClauseEvent(false, hotdrink));
		eventsOfOne.add(new ClauseEvent(false, juice));
		eventsOfOne.add(new ClauseEvent(false, food));
		eventsOfOne.add(new ClauseEvent(true, breakfast));
		Clause one = new Clause("1", eventsOfOne);
		knowledgeBase.add(one);
		
		List<ClauseEvent> eventsOfTwo= new ArrayList<ClauseEvent>();
		eventsOfTwo.add(new ClauseEvent(false, hotdrink));
		eventsOfTwo.add(new ClauseEvent(false, food));
		eventsOfTwo.add(new ClauseEvent(true, breakfast));
		Clause two = new Clause("2", eventsOfTwo);
		knowledgeBase.add(two);
		
		List<ClauseEvent> eventsOfThree= new ArrayList<ClauseEvent>();
		eventsOfThree.add(new ClauseEvent(false, coffee));
		eventsOfThree.add(new ClauseEvent(false, cream));
		eventsOfThree.add(new ClauseEvent(true, hotdrink));
		Clause three = new Clause("3", eventsOfThree);
		knowledgeBase.add(three);
		
		List<ClauseEvent> eventsOfFour= new ArrayList<ClauseEvent>();
		eventsOfFour.add(new ClauseEvent(false, tea));
		eventsOfFour.add(new ClauseEvent(true, hotdrink));
		Clause four = new Clause("4", eventsOfFour);
		knowledgeBase.add(four);
		
		List<ClauseEvent> eventsOfFive= new ArrayList<ClauseEvent>();
		eventsOfFive.add(new ClauseEvent(false, toast));
		eventsOfFive.add(new ClauseEvent(false, butter));
		eventsOfFive.add(new ClauseEvent(true, food));
		Clause five = new Clause("5", eventsOfFive);
		knowledgeBase.add(five);
		
		List<ClauseEvent> eventsOfSix= new ArrayList<ClauseEvent>();
		eventsOfSix.add(new ClauseEvent(false, egg));
		eventsOfSix.add(new ClauseEvent(true, food));
		Clause six = new Clause("6", eventsOfSix);
		knowledgeBase.add(six);
		
		Clause seven = createSimpleClause("7", coffee);
		knowledgeBase.add(seven);
		
		Clause eight = createSimpleClause("8", tea);
		knowledgeBase.add(eight);
		
		Clause nine = createSimpleClause("9", toast);
		knowledgeBase.add(nine);
		
		Clause ten = createSimpleClause("10", butter);
		knowledgeBase.add(ten);
	}
	
	private static Clause createSimpleClause(String name, Event event){
		List<ClauseEvent> events = new ArrayList<ClauseEvent>();
		events.add(new ClauseEvent(true, event));
		Clause clause = new Clause(name, events);
		return clause;
	}
}
