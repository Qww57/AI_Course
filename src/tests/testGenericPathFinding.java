package tests;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import DirectInferenceEngine.Clause;
import DirectInferenceEngine.ClauseEvent;
import DirectInferenceEngine.Event;
import GenericAStar.AbstractNode;
import GenericAStar.ClauseNode;
import GenericAStar.DirectInferenceEngine;
import GenericAStar.MapInfo;
import GenericAStar.PathFinding;
import GenericAStar.RoadNode;

/**
 * Unit test class for the methods creating the input from the resource text file.
 * 
 * @author Quentin
 *
 */
public class testGenericPathFinding {

	private static MapInfo map;
	
	@BeforeClass
	public static void loadMap(){
		// Reading the map
		map = new MapInfo();
		String path = getPath();
		System.out.println("Read: " + path);
		map.readFromFile(path);		
		System.out.println("Number of roads: " + map.getRoads().size());
	}
	
	@Before
	public void beforeTest(){
		System.out.println("");
		System.out.println("");
	}
	
	@Test
	public void testPathFinding1(){
		System.out.println("--- PATHFINDING 1 ---");
		System.out.println("");
		
		// Getting the start point and the goal point from the map
		Point start = map.getCrossingByStreetNames("SktPedersStraede", "Larsbjoernsstraede");	
		Point goal = map.getCrossingByStreetNames("Studiestraede", "Larsbjoernsstraede");
		System.out.println("Request from: " + start + " to " + goal);
			
		// Start the A star algorithm
		RoadNode startNode = new RoadNode(start);
		RoadNode goalNode = new RoadNode(goal);
		
		PathFinding search = new PathFinding(map);
		List<AbstractNode> results = search.start(startNode, goalNode);
		
		map.printResults(results);	
		printCosts(results);
	}
		
	@Test
	public void testPathFinding2(){
		System.out.println("--- PATHFINDING 2 ---");
		System.out.println("");
		
		// Getting the start point and the goal point from the map
		Point start = map.getCrossingByStreetNames("Noerregade", "Noerrevoldgade");	
		Point goal = map.getCrossingByStreetNames("Studiestraede", "Vestervoldgade");
		System.out.println("Request from: " + start + " to " + goal);
			
		// Start the A star algorithm
		RoadNode startNode = new RoadNode(start);
		RoadNode goalNode = new RoadNode(goal);
		
		PathFinding search = new PathFinding(map);
		List<AbstractNode> results = search.start(startNode, goalNode);
		
		// map.printResults(results);	
		printCosts(results);
	}
	
	private static void printCosts(List<AbstractNode> results){
		System.out.println("");
		for (int i = 0; i < results.size(); i++){
			System.out.println("Costs " + (i+1) 
					+ " - G: " + results.get(i).getGScore() 
					+ " - F: " + results.get(i).getFScore());
		}
	}
	
	private static String getPath(){
	    String path = new File("src/resources/PathFindingDataSet.txt").getAbsolutePath();
	    return path;
	}
}

