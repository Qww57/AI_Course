package Heureka.InferenceEngine;

import java.util.List;

public class Clause {
	
	enum State{
		TRUE,
		FALSE, 
		UNKNOWN
	}

	/* Defining the KB with events related together with or relations */
	private List<String> eventconditions;
	private String eventResult;
	private State state;

	/**
	 * 
	 * @param conditions list of String describing the condition events
	 * 					 an empty list means that the event is true without condition
	 * @param result
	 */
	public Clause(List<String> conditions, String result) {
		super();
		this.eventconditions = conditions;
		this.eventResult = result;
		if (eventconditions.size() == 0)
			this.state = State.TRUE;
		else
			this.state = State.UNKNOWN;
	}

	public List<String> getConditions() {
		return eventconditions;
	}
	
	public String getResult(){
		return eventResult;
	}
	
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	
	@Override
	public String toString(){
		String condition = "";
		if (eventconditions.size() != 0){
			condition += " if ";
			for (int i = 0; i < eventconditions.size(); i++){

				if (i < eventconditions.size()- 1)
					condition += eventconditions.get(i) + " or ";
				else 
					condition += eventconditions.get(i);
			}
		}
			
		return eventResult + condition + " - " + state.toString();
	}
	
	public void print(){
		System.out.println(this.toString());
	}
}
