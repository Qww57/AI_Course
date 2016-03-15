package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import AstarSearch.*;

import org.junit.*;

public class MapTest {

	public MapInfo map;
	public List<Road> roads;
	
	@Before 
	public void before(){
		map = new MapInfo();
		String path = getPath();
		map.readFromFile(path);
		roads = map.getRoads();
		if (roads == null){
			fail("Should not be null");
		}
	}
	
	@Test
	public void testFindRoadByName() {
		String name = "Noerrevoldgade";
		
		List<Road> result = map.getRoadsByName(name);
		if (result == null){
			fail("Should not be null");
		}
	}
	
	private static String getPath(){
	    String path = new File("src/resources/PathFindingDataSet.txt").getAbsolutePath();
	    return path;
	}
}

