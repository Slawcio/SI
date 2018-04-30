package utils;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import data.Facility;

public abstract class Utils {

	protected int judge(int[] locations, Facility facility) {
		int localCost = 0;
		for(int i = 0; i < locations.length; i++) {
			for(int j = i; j < locations.length; j++) {
				localCost = localCost + 
						facility.getLocation().get(i).get(j) * 
						facility.getFlow().get(locations[i]).get(locations[j]);
			}				
		}
		return localCost;
	}
	
	protected void draw(LinkedList<Integer> domain, int[] indexes) {
		indexes[0] = domain.get(ThreadLocalRandom.current().nextInt(0, domain.size()));
		int temp = domain.get(indexes[0]);
		domain.remove(indexes[0]);
		indexes[1] = domain.get(ThreadLocalRandom.current().nextInt(0, domain.size()));
		domain.add(temp);
	}

	protected void fillDomain(LinkedList<Integer> domain, int size) {
		for(int i = 0; i < size; i++) {
			domain.add(i);
		}
	}
	
	protected void permutation(int firstIndex, int secondIndex, int[] locations, Facility facility) {
		int temp = locations[firstIndex];
		if(firstIndex!=locations[secondIndex]) {
			if(temp != secondIndex) {
			locations[firstIndex] = locations[secondIndex];
			locations[secondIndex] = temp;
			}
		}
	}	
	
	protected int[] assigment(int[] result, int[] source) {
		if(source.length != result.length) {
			return null;
		} else {
			for(int i = 0; i < source.length; i++) {
				result[i] = source[i];
			}
			return result;
		}
	}
	
	protected void print(int cost, String name) {
		System.out.print("Cost: " + cost + " ");
	}
}
