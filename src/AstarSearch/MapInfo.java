package AstarSearch;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Class instanciating a map and reading the roads from an resource text file
 * 
 * @author Quentin
 *
 */

public class MapInfo {
	
	private List<Road> _roads;
	
	private static int _id;
	
	public MapInfo(){	
		resetIndex();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "boxing" })
	public void readFromFile(String path){
		
		_roads = new ArrayList();
		_id = 0;
		
		try {
			// Reading one line
			for (String line : Files.readAllLines(Paths.get(path))) 
			{
				int i = 0;
				Point startingPoint = new Point();
				Point endPoint = new Point();
				String name = null;
				int xCoordinate = 0;
				
				// Getting values for the elements
				for (String part : line.split("\\s+")) {
					//System.out.println("Part: " + part);
					
					if (i == 0){
						xCoordinate = Integer.valueOf(part);
					}
					if (i == 1){
						int yCoordinate = Integer.valueOf(part);
						startingPoint = new Point(xCoordinate, yCoordinate);
					}
					if (i == 2){
						name = part;
					}
					if (i == 3){
						xCoordinate = Integer.valueOf(part); 
					}
					if (i == 4){
						int yCoordinate = Integer.valueOf(part); 
						endPoint = new Point(xCoordinate, yCoordinate);
					}
					i++;
				}
				
				// Creating the road element
				Road road = new Road(_id, name, startingPoint, endPoint);
				_roads.add(road);
				_id++;
			}
		} catch (IOException e) {
			System.out.println("Read: " + e.getMessage());
		}
	}

	public List<Road> getRoads(){
		return _roads;
	}
	
	public List<Road> getRoadsByName(String name){
		List<Road> roads = new ArrayList<Road>();

		for (int i = 0; i < _roads.size(); i ++){
			if (_roads.get(i).getStreetName().equals(name)){
				roads.add(_roads.get(i));
			}
		}
		
		return roads;
	}
	
	public Point getCrossingByStreetNames(String name1, String name2){

		Point point = new Point();
		List<Road> roads1 = getRoadsByName(name1);
		List<Road> roads2 = getRoadsByName(name2);
		
		for(int i = 0; i < roads1.size(); i++){
			for(int j = 0; j < roads2.size(); j++){
				if (roads1.get(i).getStartingPoint().getLocation().equals(roads2.get(j).getStartingPoint().getLocation()) 
						|| roads1.get(i).getStartingPoint().getLocation().equals(roads2.get(j).getEndPoint().getLocation())){
					point = roads1.get(i).getStartingPoint().getLocation();
				} 
				if (roads1.get(i).getEndPoint().getLocation().equals(roads2.get(j).getStartingPoint().getLocation()) 
						|| roads1.get(i).getEndPoint().getLocation().equals(roads2.get(j).getEndPoint().getLocation())){
					point = roads1.get(i).getEndPoint().getLocation();
				}
			}
		}
		
		return point;
	}
	
	public void resetIndex(){
		_id = 0;
	}
}
