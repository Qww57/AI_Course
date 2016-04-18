package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import DirectInferenceEngine.Clause;
import DirectInferenceEngine.ClauseEvent;
import DirectInferenceEngine.Event;

import GenericAStar.AbstractNode;
import GenericAStar.ClauseNode;
import GenericAStar.DirectInferenceEngine;

@SuppressWarnings("unused")
public class testGenericDirectInference {
	
	private List<Clause> knowledgeBase;
	private List<Event> eventBase;	
	private Event goalEvent;
	
	@Before
	public void beforeTest(){
		knowledgeBase = new ArrayList<Clause>();
		eventBase = new ArrayList<Event>();	
		goalEvent = null;
		System.out.println();
		System.out.println();
	}
	
	@Test
	public void testBreakfast(){	
		System.out.println("--- BREAKFAST EXAMPLE ---");
		System.out.println("");
		CreateKnowledgeBase_Breakfast();
		
		// Printing the knowledge base
		System.out.println("Print clausal KB: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		}
		
		System.out.println();
		System.out.println("Our goal is: " + goalEvent.getName());
				
		ClauseEvent clauseEventGoal = new ClauseEvent(false, goalEvent);
		ClauseNode goal = new ClauseNode(new Clause("Goal", clauseEventGoal));
		
		DirectInferenceEngine search = new DirectInferenceEngine(knowledgeBase);
		List<AbstractNode> results = search.start(null , goal); 
	}
	
	@Test
	public void testNespresso(){
		System.out.println("--- NESPRESSO EXAMPLE ---");
		System.out.println("");
		CreateKnowledgeBase_Coffee();
		
		// Printing the knowledge base
		System.out.println("Print clausal KB: ");
		for (int i = 0; i < knowledgeBase.size(); i++){
			knowledgeBase.get(i).print();
		}
		
		System.out.println();
		System.out.println("Our goal is: " + goalEvent.getName());
		
		ClauseEvent clauseEventGoal = new ClauseEvent(false, goalEvent);
		ClauseNode goal = new ClauseNode(new Clause("Goal", clauseEventGoal));
		
		DirectInferenceEngine search = new DirectInferenceEngine(knowledgeBase);
		List<AbstractNode> results = search.start(null , goal); 
	}
	
	/**
	 * Method used to create the knowledge base for the breakfast example
	 */
	private void CreateKnowledgeBase_Breakfast(){ 
		
		// Creating the events
		Event breakfast = new Event("breakfast");	
		eventBase.add(breakfast);
		Event juice = new Event("juice");	
		eventBase.add(juice);
		Event hotdrink = new Event("hotdrink");
		eventBase.add(hotdrink);
		Event tea = new Event("tea");
		eventBase.add(tea);
		Event coffee = new Event("coffee");
		eventBase.add(coffee);
		Event cream = new Event("cream");		 
		eventBase.add(cream);
		Event food = new Event("food");
		eventBase.add(food);
		Event toast = new Event("toast");
		eventBase.add(toast);
		Event egg = new Event("egg");
		eventBase.add(egg);
		Event butter = new Event("butter");
		eventBase.add(butter);
		
		// Creating the clausal forms
		List<ClauseEvent> eventsOfOne = new ArrayList<ClauseEvent>();
		eventsOfOne.add(new ClauseEvent(false, hotdrink));
		eventsOfOne.add(new ClauseEvent(false, juice));
		eventsOfOne.add(new ClauseEvent(false, food));
		ClauseEvent conclusionOfOne =  new ClauseEvent(true, breakfast);
		Clause one = new Clause("1", eventsOfOne, conclusionOfOne);
		knowledgeBase.add(one);
		
		List<ClauseEvent> eventsOfTwo= new ArrayList<ClauseEvent>();
		eventsOfTwo.add(new ClauseEvent(false, hotdrink));
		eventsOfTwo.add(new ClauseEvent(false, food));
		ClauseEvent conclusionOfTwo =  new ClauseEvent(true, breakfast);
		Clause two = new Clause("2", eventsOfTwo, conclusionOfTwo);
		knowledgeBase.add(two);
		
		List<ClauseEvent> eventsOfThree= new ArrayList<ClauseEvent>();
		eventsOfThree.add(new ClauseEvent(false, coffee));
		eventsOfThree.add(new ClauseEvent(false, cream));
		ClauseEvent conclusionOfThree =  new ClauseEvent(true, hotdrink);
		Clause three = new Clause("3", eventsOfThree, conclusionOfThree);
		knowledgeBase.add(three);
		
		List<ClauseEvent> eventsOfFour= new ArrayList<ClauseEvent>();
		eventsOfFour.add(new ClauseEvent(false, tea));
		ClauseEvent conclusionOfFour =  new ClauseEvent(true, hotdrink);
		Clause four = new Clause("4", eventsOfFour, conclusionOfFour);
		knowledgeBase.add(four);
		
		List<ClauseEvent> eventsOfFive= new ArrayList<ClauseEvent>();
		eventsOfFive.add(new ClauseEvent(false, toast));
		eventsOfFive.add(new ClauseEvent(false, butter));
		ClauseEvent conclusionOfFive =  new ClauseEvent(true, food);
		Clause five = new Clause("5", eventsOfFive, conclusionOfFive);
		knowledgeBase.add(five);
		
		List<ClauseEvent> eventsOfSix= new ArrayList<ClauseEvent>();
		eventsOfSix.add(new ClauseEvent(false, egg));
		ClauseEvent conclusionOfSix =  new ClauseEvent(true, food);
		Clause six = new Clause("6", eventsOfSix, conclusionOfSix);
		knowledgeBase.add(six);
		
		Clause seven = createSimpleClause("7", true, coffee);
		knowledgeBase.add(seven);
		
		Clause eight = createSimpleClause("8", true, tea);
		knowledgeBase.add(eight);
		
		Clause nine = createSimpleClause("9", true, toast);
		knowledgeBase.add(nine);
		
		Clause ten = createSimpleClause("10", true, butter);
		knowledgeBase.add(ten);
		
		goalEvent = breakfast;
	}
	
	/**
	 * Method used to create the knowledge base for the Nespresso example from:
	 * http://www.cs.toronto.edu/~sheila/2542/w06/readings/ijcai_pblr.pdf
	 */
	private void CreateKnowledgeBase_Coffee(){ 
		
		// Creating the events
		Event okpump = new Event("ok-pump");	
		eventBase.add(okpump);
		Event onpump = new Event("on-pump");	
		eventBase.add(onpump);
		Event water = new Event("water");
		eventBase.add(water);
		Event manfill = new Event("man-fill");
		eventBase.add(manfill);
		Event okboiler = new Event("ok-boiler");
		eventBase.add(okboiler);
		Event onboiler = new Event("on-boiler");		 
		eventBase.add(onboiler);
		Event steam = new Event("steam");
		eventBase.add(steam);
		Event hotdrink = new Event("hotdrink");
		eventBase.add(hotdrink);
		Event tea = new Event("tea");
		eventBase.add(tea);
		Event coffee = new Event("coffee");
		eventBase.add(coffee);
		
		// Creating the clausal forms
		List<ClauseEvent> eventsOfOne = new ArrayList<ClauseEvent>();
		eventsOfOne.add(new ClauseEvent(false, onpump));
		eventsOfOne.add(new ClauseEvent(false, okpump));
		ClauseEvent conclusionOfOne =  new ClauseEvent(true, water);
		Clause one = new Clause("A1", eventsOfOne, conclusionOfOne);
		knowledgeBase.add(one);
		
		List<ClauseEvent> eventsOfTwo= new ArrayList<ClauseEvent>();
		eventsOfTwo.add(new ClauseEvent(false, manfill));
		ClauseEvent conclusionOfTwo =  new ClauseEvent(true, water);
		Clause two = new Clause("A2", eventsOfTwo, conclusionOfTwo);
		knowledgeBase.add(two);
		
		List<ClauseEvent> eventsOfThree= new ArrayList<ClauseEvent>();
		eventsOfThree.add(new ClauseEvent(false, manfill));
		ClauseEvent conclusionOfThree =  new ClauseEvent(false, onpump);
		Clause three = new Clause("A3", eventsOfThree, conclusionOfThree);
		knowledgeBase.add(three);
		
		List<ClauseEvent> eventsOfFour= new ArrayList<ClauseEvent>();
		eventsOfFour.add(new ClauseEvent(true, manfill));
		ClauseEvent conclusionOfFour =  new ClauseEvent(true, onpump);
		Clause four = new Clause("A4", eventsOfFour, conclusionOfFour);
		knowledgeBase.add(four);
		
		List<ClauseEvent> eventsOfFive= new ArrayList<ClauseEvent>();
		eventsOfFive.add(new ClauseEvent(false, water));
		eventsOfFive.add(new ClauseEvent(false, okboiler));
		eventsOfFive.add(new ClauseEvent(false, onboiler));
		ClauseEvent conclusionOfFive =  new ClauseEvent(true, steam);
		Clause five = new Clause("A5", eventsOfFive, conclusionOfFive);
		knowledgeBase.add(five);
		
		List<ClauseEvent> eventsOfSix= new ArrayList<ClauseEvent>();
		eventsOfSix.add(new ClauseEvent(true, water));
		ClauseEvent conclusionOfSix =  new ClauseEvent(false, steam);
		Clause six = new Clause("A6", eventsOfSix, conclusionOfSix);
		knowledgeBase.add(six);
		
		List<ClauseEvent> eventsOfSeven= new ArrayList<ClauseEvent>();
		eventsOfSeven.add(new ClauseEvent(true, onboiler));
		ClauseEvent conclusionOfSeven =  new ClauseEvent(false, steam);
		Clause seven = new Clause("A7", eventsOfSeven, conclusionOfSeven);
		knowledgeBase.add(seven);
		
		List<ClauseEvent> eventOfEight= new ArrayList<ClauseEvent>();
		eventOfEight.add(new ClauseEvent(true, okboiler));
		ClauseEvent conclusionOfEight =  new ClauseEvent(false, steam);
		Clause eight = new Clause("A8", eventOfEight, conclusionOfEight);
		knowledgeBase.add(eight);
		
		List<ClauseEvent> eventsOfNine= new ArrayList<ClauseEvent>();
		eventsOfNine.add(new ClauseEvent(false, steam));
		eventsOfNine.add(new ClauseEvent(false, coffee));
		ClauseEvent conclusionOfNine =  new ClauseEvent(true, hotdrink);
		Clause nine = new Clause("A9", eventsOfNine, conclusionOfNine);
		knowledgeBase.add(nine);
		
		List<ClauseEvent> eventsOfTen= new ArrayList<ClauseEvent>();
		eventsOfTen.add(new ClauseEvent(true, steam));
		eventsOfTen.add(new ClauseEvent(true, coffee));
		ClauseEvent conclusionOfTen =  new ClauseEvent(false, hotdrink);
		Clause ten = new Clause("A10", eventsOfTen, conclusionOfTen);
		knowledgeBase.add(ten);
		
		List<ClauseEvent> eventOfEleven= new ArrayList<ClauseEvent>();
		eventOfEleven.add(new ClauseEvent(true, tea));
		ClauseEvent conclusionOfEleven =  new ClauseEvent(true, coffee);
		Clause eleven = new Clause("A11", eventOfEleven, conclusionOfEleven);
		knowledgeBase.add(eleven);
		
		Clause twelve = createSimpleClause("A12", false, tea);
		knowledgeBase.add(twelve);
		
		Clause thirteen = createSimpleClause("A13", true, okboiler);
		knowledgeBase.add(thirteen);
		
		Clause fourteen = createSimpleClause("A14", true, onboiler);
		knowledgeBase.add(fourteen);
		
		Clause fifteen = createSimpleClause("A15", true, water);
		knowledgeBase.add(fifteen);
		
		goalEvent = hotdrink;
	}
	
	private static Clause createSimpleClause(String name, boolean status, Event event){
		return new Clause(name, new ClauseEvent(status, event));
	}
}
