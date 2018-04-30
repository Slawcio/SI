package randomAndGreedy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import data.Facility;
import io.CSV;
import utils.Utils;

public class RandomSearch extends Utils {

	private final int REPEAT = 10000;
	private final int BORDER = REPEAT/5;
	
	public int[] randomSearch(Facility facility, int[] locations, String fileName) {
		int cost = 0, firstIndex = 0, secondIndex = 1;
		cost = judge(locations, facility);
		LinkedList<Integer> domain = new LinkedList<Integer>();
		int[] indexes = {0, 1};
		int tempCost;
		int counter = 0;
		fillDomain(domain, locations.length);
		int[] save = new int[locations.length];
		ArrayList<Integer> generations = new ArrayList<Integer>();
		long startTime = System.currentTimeMillis();
		generations.add(cost*2);
		for(int i = 0; i < REPEAT && counter < BORDER; i++) {
					assigment(save, locations);
					draw(domain, indexes);
					permutation(domain.get(indexes[0]), domain.get(indexes[1]), locations, facility);
					tempCost = judge(locations, facility);
					if(cost <= tempCost){
						assigment(locations, save);
					} else {
						cost = tempCost;
					}
					generations.add(cost*2);
					if(generations.size() > 2 && generations.get(i) == generations.get(i-1)) 
						counter++;
					else
						counter = 0;
		}
		long endTime = System.currentTimeMillis();
		CSV.writeFileSimple(generations, fileName, endTime - startTime);
		return locations;
	}
	
}
