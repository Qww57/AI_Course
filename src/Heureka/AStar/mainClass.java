package Heureka.AStar;
import java.awt.Point;
import java.io.File;
import java.util.List;

import Heureka.AStar.AstarSearch.Heuristic;
import Heureka.AStar.AstarSearch.Type;

public class mainClass {

	public static void main(String[] args) {
		System.out.println("--- PATHFINDING ---");
		System.out.println("");
		
		// Reading the map
		MapInfo map = new MapInfo();
		String path = getPath();
		System.out.println("Read: " + path);
		map.readFromFile(path);		
		System.out.println("Number of roads: " + map.getRoads().size());
		
		// Getting the start point and the goal point from the map
		Point start = map.getCrossingByStreetNames("SktPedersStraede", "Larsbjoernsstraede");	
		Point goal = map.getCrossingByStreetNames("Studiestraede", "Larsbjoernsstraede");
		System.out.println("Request from: " + start + " to " + goal);
			
		// Start the A star algorithm
		AstarSearch search = new AstarSearch(map, Heuristic.DISTANCE, Type.PATHFINDING);
		RoadNode startNode = new RoadNode(start);
		RoadNode goalNode = new RoadNode(goal);
		List<Node> results = search.startAstar(startNode, goalNode);
		
		map.printResults(results);	
		printCosts(results);
		
		System.out.println("");
		System.out.println("-- Second itinerary --");
		
		// Getting the start point and the goal point from the map
		start = map.getCrossingByStreetNames("Noerregade", "Noerrevoldgade");	
		goal = map.getCrossingByStreetNames("Studiestraede", "Vestervoldgade");
		System.out.println("Request from: " + start + " to " + goal);
			
		// Start the A star algorithm
		search = new AstarSearch(map, Heuristic.DISTANCE, Type.PATHFINDING);
		startNode = new RoadNode(start);
		goalNode = new RoadNode(goal);
		results = search.startAstar(startNode, goalNode);
		
		map.printResults(results);	
		printCosts(results);
	}
	
	private static void printCosts(List<Node> results){
		System.out.println("");
		for (int i = 0; i < results.size(); i++){
			System.out.println("Costs " + (i+1) 
					+ " - G: " + results.get(i).getGScore() 
					+ " - F: " + results.get(i).getFScore());
		}
	}
	
	private static String getPath(){
	    String path = new File("src/Heureka/resources/PathFindingDataSet.txt").getAbsolutePath();
	    return path;
	}
}
