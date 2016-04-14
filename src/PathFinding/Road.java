package PathFinding;

import java.awt.Point;

public class Road {
	
	private int _id;
	private String _streetName;
	private Point _startingPoint;
	private Point _endPoint;
		
	public Road(int id, String streetName, Point startingPoint, Point endPoint){
		_id = id;
		_streetName = streetName;
		_startingPoint = startingPoint;
		_endPoint = endPoint;
	}
		
	public int getId(){
		return _id;
	}
	
	public String getStreetName(){
		return _streetName;
	}
	
	public Point getStartingPoint(){
		return _startingPoint;
	}
	
	public Point getEndPoint(){
		return _endPoint;
	}
}