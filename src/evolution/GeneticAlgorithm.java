package evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import data.Facility;
import io.CSV;
import io.RandomRead;
import utils.Utils;

public class GeneticAlgorithm extends Utils{
	
	private final int POPULATION = 200;
	private final int GENERATION = 40000;
	private final double PX = 0.8;
	private final double MUTATION = 0.1;
	private final int TOUR = 10;
	private final int ROULETTE = 10;
	private final int BORDER = GENERATION/6;
	
	
	public class Generation{
		
		private int generation, best, worst;
		private double avg;
		
		Generation(int generation, int best, double avg, int worst){
			this.generation = generation;
			this.best = best * 2;
			this.avg = avg * 2;
			this.worst = worst * 2;
		}
		
		public int getBest() {
			return best;
		}
		
		public double getAvg() {
			return avg;
		}
		
		public int getWorst() {
			return worst;
		}
	}

	private class Solution implements Comparable<Object>{
		
		private int evaluation = 0;
		private int[] solution;
		private boolean changed = false;
		
		Solution(int[] solution){
			this.solution = solution;
		}
		
		Solution(Solution s) {
			int[] temp = s.getSolution();
			int[] copy = new int [temp.length];
			for(int i = 0; i < temp.length; i++){
				copy[i] = temp[i];
			}
			this.solution = copy;
			this.evaluation = s.getEvaluation();
		}
		
		public void setChanged() {
			if(changed)
				changed = false;
			else
				changed = true;
		}
		
		public boolean getChanged() {
			return changed;
		}
		
		public void setEvaluation(int evaluation) {
			this.evaluation = evaluation;
		}
		
		public int getEvaluation() {
			return evaluation;
		}
		
		public int[] getSolution() {
			return solution;
		}

		@Override
		public int compareTo(Object o) {
			Solution s = (Solution) o;
			if(s.getEvaluation()>this.evaluation)
				return -1;
			else if(s.getEvaluation() < this.evaluation)
				return 1;
			else
				return 0;
		}
	}

	/**
	 * 
	 * @param facility - ref to object with location & flow matrixes
	 * @param read - ref to object that alows assign possible solutions
	 * @return best solution found in GA way
	 */
	public ArrayList<Generation> gaTour(Facility facility, RandomRead read, String fileName) {
		long startTime = System.currentTimeMillis();
		ArrayList<Solution> population = setPopulation(facility, read);									//creating array for current pop
		ArrayList<Generation> generations = new ArrayList<Generation>();								//creating array for consecutive generations
		LinkedList<Integer> domain = new LinkedList<Integer>();											//creating object domain for drawing
		ArrayList<Solution> nextPopulation;
		Solution solution;//creating 
		fillDomain(domain, population.get(0).solution.length);											//filling domain with elements
		int[] indexes = {0, 1};																			//indexes for drawing in domain
		int counter = 0;																				//counter for checking faster result
		evaluateFirstTime(population, facility);
		for(int i = 0; i < GENERATION && counter < BORDER; i++) {
			nextPopulation = new ArrayList<Solution>();													//next generation of population 
			generations.add(getGeneration(population, i));												//passing data about current generation
			if(generations.size() > 2 && generations.get(i).getBest() == generations.get(i-1).getBest()) 
				counter++;
			else
				counter = 0;
			tournament(population, nextPopulation);														//tournament selection
			for(int j = 0; j < POPULATION; j++) {
				solution = nextPopulation.get(j);
				if(drawPropabilityForChange() < PX*1000) {												//checking if cross happen
					cross(solution.getSolution());											//crossing
					solution.setChanged();
				}
				if(drawPropabilityForChange() < MUTATION*1000) {										//checking if mutation happen
					draw(domain, indexes);																//drawing indexes for mutation
					permutation(domain.get(indexes[0]), domain.get(indexes[1]), solution.solution, facility);//permutation with indexes drawed
					if(!solution.getChanged()) {
						solution.setChanged();
					}
				}
			}
			evaluate(nextPopulation, facility);															//evaluation value of solutions from next pop
			population = nextPopulation;																//assigning new pop to regular pop 
		}
		long endTime = System.currentTimeMillis();														//receiving end of time
		long time = (endTime - startTime);
		print(generations, time, fileName);
		CSV.writeFile(generations, fileName, time);														//writing results to CSV
		return generations;
	}
	
	public ArrayList<Generation> gaRoulette(Facility facility, RandomRead read, String fileName) {
		long startTime = System.currentTimeMillis();
		ArrayList<Solution> population = setPopulation(facility, read);
		ArrayList<Generation> generations = new ArrayList<Generation>();
		LinkedList<Integer> domain = new LinkedList<Integer>();
		ArrayList<Solution> nextPopulation;
		fillDomain(domain, population.get(0).solution.length);
		int[] indexes = {0, 1};
		int counter = 0;
		evaluateFirstTime(population, facility);
		for(int i = 0; i < GENERATION && counter < BORDER; i++) {
			nextPopulation = new ArrayList<Solution>();
			generations.add(getGeneration(population, i));
			if(generations.size() > 2 && generations.get(i).getBest() == generations.get(i-1).getBest()) 
				counter++;
			else
				counter = 0;
			roulette(population, nextPopulation);
			for(int j = 0; j < POPULATION; j++) {
				if(drawPropabilityForChange() < PX*1000) {
					cross(nextPopulation.get(j).getSolution());
				}
				if(drawPropabilityForChange() < MUTATION*1000) {
					draw(domain, indexes);
					permutation(domain.get(indexes[0]), domain.get(indexes[1]), nextPopulation.get(j).solution, facility);
				}
			}
			evaluate(nextPopulation, facility);
			population = nextPopulation;
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		print(generations, time, fileName);
		CSV.writeFile(generations, fileName, time);
		return generations;
	}
	/**
	 * @return evaluation matrix of solution
	 * @param population
	 * @param facility
	 */
	private void evaluate(ArrayList<Solution> population, Facility facility) {
		for(int i = 0; i < population.size(); i++) {
			if(population.get(i).getChanged()) {
				population.get(i).setEvaluation(judge(population.get(i).solution, facility));
				population.get(i).setChanged();
			}
		}
	}
	
	private void evaluateFirstTime(ArrayList<Solution> population, Facility facility) {
		for(int i = 0; i < population.size(); i++) {
				population.get(i).setEvaluation(judge(population.get(i).solution, facility));
			}
	}
	
	private void print(ArrayList<Generation> generation, long time){
		for(Generation g : generation) {
			System.out.println("i: " + g.generation + " best: " + g.best + " avg: " + g.avg + " worst: " + g.worst);
		}
		System.out.print("\n" + "\n" + "time: " + time);
	}
	
	private void print(ArrayList<Generation> generation, long time, String file) {
		Generation last = generation.get(generation.size()-1);
		System.out.println(file + " " + "i: " + last.generation + " best: " + last.best + " avg: " + last.avg + " worst: " + last.worst + " time: " + time);
	}
	
	private void repair(int[] repair, LinkedList<Integer> missing) {
		boolean isFound = false;
		for(int i = 0; i < repair.length - 1 && !missing.isEmpty(); i++) {
			for(int j = i + 1; j < repair.length && !isFound; j++) {
				if(	repair[i] == repair[j]) {
					isFound = true;
					repair[i] = missing.getFirst();
					missing.removeFirst();
				}
			}
			isFound = false;
		}
	}
	
	private boolean check(int[] check) {
		boolean isMissing = true;
		LinkedList<Integer> missing = new LinkedList<Integer>();
		for(int i = 0; i < check.length; i++) {
			for(int j = 0; j < check.length && isMissing; j++) {
				if(i == check[j]) 
					isMissing = false;
			}
			if(isMissing) 
				missing.add(i);
			 else
				isMissing = true;
			}
		if(!missing.isEmpty()) {
			repair(check, missing);
			return false;
		} else
			return true;
	}
	
/**
 * 
 * @param facility - 
 * @param read
 * @return ArrayList of Solutions
 */
	private ArrayList<Solution> setPopulation(Facility facility, RandomRead read){
		int size = facility.getNumber();
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for(int i = 0; i < POPULATION; i++) {
			solutions.add(new Solution(read.assignRandomly(size)));
		}
		return solutions;
	}
	
	private int drawPropabilityForChange() {
			return ThreadLocalRandom.current().nextInt(0, 1000);
	}
	
	private int drawForCross(int n) {
		return ThreadLocalRandom.current().nextInt(1, n);
	}
	
	private int drawSolution() {
		return ThreadLocalRandom.current().nextInt(0, POPULATION);
	}
	
	private int drawForRoulette(double n) {
		if( 1 < n )
		return ThreadLocalRandom.current().nextInt(0, (int) n );
		else
			return 0;
	}
	
	private void cross(int[] parent1) {
		int crossPoint = drawForCross(parent1.length - 2);
		int[] temp = new int[crossPoint];
		for(int i = 0; i < temp.length; i++) {
			temp[i] = parent1[i];
		}
		for(int i = 0; i < parent1.length - temp.length; i++) {
			parent1[i] = parent1[temp.length + i];
		}
		for(int i = parent1.length - temp.length, j = 0; j < temp.length; j++, i++) {
			parent1[i] = temp[j];
		}
	//	check(parent1);
	}
	
	private void tournament(ArrayList<Solution> population, ArrayList<Solution> nextPopulation) {
		TreeSet<Solution> tournament = new TreeSet<Solution>();
		for(int i = 0; i < population.size(); i++) {
			int checker = 0;
			tournament.clear();
			while(tournament.size() != TOUR && checker < population.size()) {
				tournament.add(population.get(drawSolution()));
				checker++;
			}
			nextPopulation.add((new Solution(tournament.first())));
		}
	}
	
	private void rouletteOld(ArrayList<Solution> population, ArrayList<Solution> nextPopulation) {
		ArrayList<Solution> toRoulette = new ArrayList<Solution>();
		boolean isFound = false;
		int drawTheBest;
		int[] propabilities = new int[ROULETTE];
		for(int j = 0; j < POPULATION; j++) {	
			for(int i = 0; i < ROULETTE; i++) {
				toRoulette.add(population.get(drawSolution()));
			}
			propabilities[0] = toRoulette.get(0).getEvaluation();
			for(int i = 1; i < propabilities.length; i++) {
				propabilities[i] = propabilities[i - 1] + toRoulette.get(i).getEvaluation();
			}
			drawTheBest = drawForRoulette(propabilities[propabilities.length-1]);
			for(int i = 0; i < propabilities.length && !isFound; i++) {
				if(drawTheBest < propabilities[i]) {
					nextPopulation.add(new Solution(toRoulette.get(i)));
					isFound = true;
				}
			}
			toRoulette.clear();
			isFound = false;
		}
	}
	
	private void roulette(ArrayList<Solution> population, ArrayList<Solution> nextPopulation) {
		TreeSet<Solution> toRoulette = new TreeSet<Solution>();
		int drawTheBest;
		int fitness = 0;
		int forTreeMap = 0;
		double sum = 0;
		ArrayList<Double> propabilities = new ArrayList<Double>();
		for(int j = 0; j < POPULATION; j++) {	
			fitness = 2;
			sum = 0;
			for(int i = 0; i < ROULETTE; i++) {
				Solution s = population.get(drawSolution());
				sum = sum + s.getEvaluation();
				toRoulette.add(s);
			}
			for(Solution s : toRoulette) {
				propabilities.add( s.getEvaluation() * 100 /sum);
			}
			for(int i = propabilities.size() - 1; i > -1 ;i--) {
				propabilities.set(i, propabilities.get(i)*fitness);
				fitness = fitness * 2;
			}
			for(int i = 1; i < propabilities.size() ; i++) {
				propabilities.set(i, propabilities.get(i) + propabilities.get(i - 1));
			}
			drawTheBest = drawForRoulette(propabilities.get(propabilities.size() - 1));
			forTreeMap = 0;
			for(Solution s : toRoulette) {
				if(drawTheBest < propabilities.get(forTreeMap)) {
					nextPopulation.add(new Solution(s));
					break;
				}
				forTreeMap++;
			}
			propabilities.clear();
			toRoulette.clear();
		}
	}
	
	private Generation getGeneration(ArrayList<Solution> population, int iteration) {
		int best = Integer.MAX_VALUE;
		int worst = Integer.MIN_VALUE;
		double avg = 0;
		for(Solution s : population) {
			if(s.getEvaluation() < best) 
				best = s.getEvaluation();
			if(s.getEvaluation() > worst)
				worst = s.getEvaluation();
			avg = avg + s.getEvaluation();
		}
		avg = avg/population.size();
		return new Generation(iteration, best, avg, worst);
	}
}
