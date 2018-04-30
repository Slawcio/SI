package io;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class RandomRead {
	
	public int[] assignRandomly(int n) {
		int[] result = new int[n];
		LinkedList<Integer> domain = new LinkedList<Integer>();
		for(int i = 0; i < result.length; i++) {
			domain.add(i);
		}
		for(int i = 0; i < result.length; i++) {
			int domainIndex = randInt(0, domain.size());
			result[i] = domain.get(domainIndex);
			domain.remove(domainIndex);
		}
		return result;
	}
	
	private int randInt(int min, int max) {
		if(max>min)
			return ThreadLocalRandom.current().nextInt(min, max);
				else
			return 0;
	}
	
}
