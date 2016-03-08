package AstarSearch;

import java.awt.Point;
import java.io.File;

public class mainClass {

	public static void main(String[] args) {
		MapInfo map = new MapInfo();
		String path = getPath();
		map.readFromFile(path);
		
		System.out.println("Number of roads: " + map.roads.size());
		
		// TODO: compute starting and goal points from names
		
		// SktPedersStraede & Larsbjoernsstraede
		Point startingPoint = new Point(10,70);
		
		// Studiestraede & Larsbjoernsstraede.
		Point goal = new Point(45,70);
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
