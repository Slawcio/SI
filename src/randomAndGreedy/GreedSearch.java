package randomAndGreedy;
import java.util.ArrayList;

import data.Facility;
import io.CSV;
import utils.Utils;

public class GreedSearch extends Utils{
	
	private static final int REPEAT = 10000;
	private static final int BORDER = REPEAT/5;
	
	public int[] optGreed(Facility facility, int[] locations, String fileName){
		boolean ifChanged = false;
		int[] result = new int[REPEAT];
		long startTime = System.currentTimeMillis();
		int cost = 0;
		cost = judge(locations, facility);
		int[] save = new int[locations.length];
		int waypoint = 0;
		int tempCost = 0;
		int counter = 0;
		ArrayList<Integer> generations = new ArrayList<Integer>();
		generations.add(cost * 2);
		for(int i = 0; i < REPEAT && counter < BORDER; i++) {
			for(int j = 0; j < locations.length; j++) {
				if(waypoint != j) {
					assigment(save, locations);
					permutation(waypoint, j, locations, facility);
					tempCost = judge(locations, facility);
					if(cost <= tempCost){
						assigment(locations, save);
					} else {
						if(waypoint != locations.length - 1 )
							waypoint++;
						else
							waypoint = 0;
						cost = tempCost;
						j = 0;
						ifChanged = true;
					}
				}
			}
			if(generations.size() > 2 && generations.get(i) == generations.get(i-1)) 
				counter++;
			else
				counter = 0;
			generations.add(cost * 2);
			if(ifChanged) 
				ifChanged = false;
			else
				if(waypoint == locations.length - 1) waypoint = 0;
				else
					waypoint++;
		}
		long endTime = System.currentTimeMillis();
		CSV.writeFileSimple(generations, fileName, endTime - startTime);
		return locations;
	}

}
