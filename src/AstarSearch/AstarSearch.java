package AstarSearch;

import java.awt.Point;
import java.util.*;

/**
 * A* algorithm based on the uniform cost search algorithm page 84 in the book
 * 
 * @author Quentin
 *
 */
@SuppressWarnings("unused")
public class AstarSearch {	
	
	//Global ones for the algorithm
	
	private int roadId;
	
	private PriorityQueue<RoadNode> frontier;
	
	private List<RoadNode> explored;
	
	private List<RoadNode> itinerary;
	
	public List<RoadNode> getItinerary(){
		return itinerary;
	}
	
	public AstarSearch(MapInfo map, Point startPoint, Point goal){		
		System.out.println("Start of A star");
		
		roadId = 0;
		explored = new ArrayList<RoadNode>();
		frontier = new PriorityQueue<>();
		
		RoadNode currentNode = createNewNode(startPoint, goal, null);		
		frontier.add(currentNode);
		
		/* Start loop */
		while (!frontier.isEmpty()){
			
			currentNode = frontier.poll();			
			
			System.out.println("Checking for node: " + currentNode.getId() + " - " + currentNode.getPosition().getLocation());	
			
			if (currentNode.getPosition().getLocation().equals(goal.getLocation())){
				System.out.println("A STAR SUCCESS");
				// Function to return the itinerary
			}
			
			explored.add(currentNode);
			
			// Get the children of the node
			findChildren(currentNode, map, goal);
			PriorityQueue<RoadNode> filteredChildren = filterDuplicates(currentNode.getChildren()); // Problem ?
			
			System.out.println("Children:" + currentNode.getChildren().size() 
						+ " After filter: " + filteredChildren.size());
			
			frontier.addAll(filteredChildren);		
		}
		
		System.out.println("Frontier is empty");
	}
	
	private PriorityQueue<RoadNode> filterDuplicates(PriorityQueue<RoadNode> children){
		PriorityQueue<RoadNode> result = new PriorityQueue<RoadNode>();
		Iterator<RoadNode> it = children.iterator();
		
		RoadNode child = null;
		
		while (it.hasNext()){
			child = it.next();
			
			if(!isInExplored(child) && !isInFrontier(child) && !child.equals(child.getParent())){
				result.add(child);
			}
		}
		return result;
	}
	
	private boolean isInExplored(RoadNode node){
		boolean result = false;
		for(int i = 0; i < explored.size(); i++){
			if(explored.get(i).equals(node)){
				result = true;
			}
		}
		return result;
	}
	
	private boolean isInFrontier(RoadNode node){
		boolean result = false;
		Iterator<RoadNode> it = frontier.iterator();
		while (it.hasNext() && result == false){
			if(it.next() == node){
				result = true;
			}
		}
		return result;
	}
	
	private RoadNode createNewNode(Point startPoint, Point goal, RoadNode parent){
		RoadNode currentNode = new RoadNode(startPoint, goal, roadId);
		currentNode.setParent(parent);
		roadId++;
		return currentNode;
	}
	
	private void findChildren(RoadNode roadNode, MapInfo map, Point goal){
		List<Road> roads =  map.getRoads();
		PriorityQueue<RoadNode> children = new PriorityQueue<RoadNode>();
	
		for (int i = 0; i < roads.size(); i++){
			if (roads.get(i).getStartingPoint().equals(roadNode.getPosition())){
				RoadNode newNode = createNewNode(roads.get(i).getEndPoint(), goal, roadNode);
				children.add(newNode);
			}
		}
		roadNode.setChildren(children);
	}
	
}
