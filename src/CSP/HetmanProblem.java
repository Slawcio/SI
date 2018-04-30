package CSP;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HetmanProblem {
	
	int[][] chessboard;
	boolean[] row;
	boolean[] up;
	boolean[] down;
	long startTime;
	long endTime;
	int[] solution;
	int n;
	LinkedList<Integer> domain;
	public int[][] solve(int size){
		n = size;
		chessboard = new int[n][n];
		row = new boolean[n];
		up = new boolean[2*n - 1];
		down = new boolean[2*n - 1];
		solution = new int[n];
		startTime = System.currentTimeMillis();
		fillDomain();
		drawHetman();
		//int[] test = {1, 3, 0, 2};
		while(!test2(solution)) {
			clear();
			fillDomain();
			drawHetman();
		}
		endTime = System.currentTimeMillis();
		for(int i = 0; i < chessboard.length; i++) {
			for(int j = 0; j < chessboard.length; j++) {
				chessboard[i][j] = 0;
			}
		}
		print();
		
		return chessboard;
	}
	
	private void printSolution() {
		for(Integer i : solution)
			System.out.print(i + " ");
		System.out.println("");
	}
	
	private void fillDomain() {
		domain = new LinkedList<Integer>();
		for(int i = 0; i < n; i++) {
			domain.add(i);
		}
	}
	
	private void drawHetman() {
		for(int i = 0; i < n; i++) {
			int random = drawSolution(domain.size());
			solution[i] = domain.get(random);
			domain.remove(random);
		}
	}
	
	boolean test2(int[] r) {
		for(int i = 0; i < r.length; i++) {
			if(possible(i, r[i]))
			put(i, r[i]);
				else
			{
					return false;
					}
		}
		return true;
	}
	
	private void clear() {
		for(int i = 0; i < up.length; i++) {
			up[i] = false;
			down[i] = false;
		}
		for(int i = 0; i < row.length; i++) {
			row[i] = false;
		}
		for(int i = 0; i < solution.length; i++) {
			solution[i] = 0;
		}
	}
	
	void random(int c) {
		while(c != n) {
			
		}
	}
	
	private int drawSolution(int size) {
		if(size > 1)
		return ThreadLocalRandom.current().nextInt(0, size);
		else
			return 0;
	}
	
	boolean possible(int r, int c) {
		try {
			return !row[r] && !up[r+c+1] && !down[r-c+n];
		}
		catch (
			ArrayIndexOutOfBoundsException e) {
				return false;
			}
		
	}
	
	void put(int r, int c) {
		row[r] = up[r+c+1] = down[r-c+n] = true;
	}
	
	void remove(int r, int c) {
		row[r] = up[r+c-1] = down[r-c+n] = false;
	}
	
	boolean test(int c) {
		for(int r = 1; r < n; r++) {
			if(possible(r, c)) {
				put(r, c);
			}
			if(c==n) {
				print();
				return true;
			} else {
				test(c + 1);
			} remove(r, c);
		}
		return false;
	}
	
	void print() {
		for(int i = 0; i < n; i++) {
			chessboard[i][solution[i]] = 1;
		}
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				System.out.print(chessboard[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("\n time: " + (endTime-startTime) + " ms \n");
		System.out.println("");
	}
	
	void print(int[] test) {
		for(int i = 0; i < n; i++) {
			chessboard[i][test[i]] = 1;
		}
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				System.out.print(chessboard[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("\n time: " + (endTime-startTime) + " ms \n");
		System.out.println("");
	}
	
}
