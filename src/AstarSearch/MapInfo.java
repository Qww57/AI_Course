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
	public List<Road> roads;
	
	private static int id;
	
	MapInfo(){
		roads = new ArrayList();
	}
	
	public void readFromFile(String path){
		
		roads = new ArrayList();
		id = 0;
		
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
				for (String part : line.split("\\s+")) 
				{
					// System.out.println("Part: " + part);
					
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
				Road road = new Road(id, name, startingPoint, endPoint);
				roads.add(road);
				id++;
			}
		} catch (IOException e) {
			System.out.println("Read: " + e.getMessage());
		}
	}
}
