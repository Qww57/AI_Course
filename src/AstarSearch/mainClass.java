package AstarSearch;

import java.awt.Point;
import java.io.File;

public class mainClass {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		MapInfo map = new MapInfo();
		String path = getPath();
		map.readFromFile(path);
		
		System.out.println("Number of roads: " + map.getRoads().size());
		
		Point startingPoint = map.getCrossingByStreetNames("SktPedersStraede", "Larsbjoernsstraede");
		System.out.println("Start: " + startingPoint);
		if (startingPoint == null || startingPoint == new Point()){
			startingPoint = new Point(35,80);
			System.out.println("StartingPoint fail");
		}
		
		Point goal = map.getCrossingByStreetNames("Studiestraede", "Larsbjoernsstraede");
		System.out.println("Goal: " + goal);
		if (goal == null || goal == new Point()){
			goal = new Point(45,70);
			System.out.println("Goal fail");
		}
		
		AstarSearch search = new AstarSearch(map, startingPoint, goal);
	}
	
	/**
	 * 
	 * @return path to resources
	 */
	private static String getPath(){
	    String path = new File("src/resources/PathFindingDataSet.txt").getAbsolutePath();
	    return path;
	}
}
