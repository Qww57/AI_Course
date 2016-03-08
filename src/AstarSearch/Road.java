package AstarSearch;

import java.awt.Point;
import java.util.List;

public class Road {
	
	private int _id;
	
	private String _streetName;
	
	private Point _startingPoint;
	
	private Point _endPoint;
	
	/*
	 * Distance between starting point and end point
	 */
	private double distance;
		
	public Road(int id, String streetName, Point startingPoint, Point endPoint){
		_id = id;
		_streetName = streetName;
		_startingPoint = startingPoint;
		_endPoint = endPoint;
		
		distance = _startingPoint.distance(endPoint);
	}
		
	/* Getters and setters */
	
	public void setStreetName(String streetName){
		_streetName = streetName;
	}
	
	public String getStreetName(){
		return _streetName;
	}
	
	public void setStartingPoint(Point startingPoint){
		_startingPoint = startingPoint;
	}
	
	public Point getStartingPoint(){
		return _startingPoint;
	}
	
	public void setEndPoint(Point endPoint){
		_endPoint = endPoint;
	}
	
	public Point getEndPoint(){
		return _endPoint;
	}
	
	// TO BE DELETED ?
	
	/*
	 * List of roads that have the same starting point as the end 
	 * point of this road
	 */
	private List<Road> _crossingRoads;
	
	public void setFrontier(List<Road> crossingRoads){
		_crossingRoads = crossingRoads;
	}
	
	public List<Road> getFrontier(){
		return _crossingRoads;
	}
	
}
