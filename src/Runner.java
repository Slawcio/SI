import CSP.HetmanProblem;
import data.Facility;
import evolution.GeneticAlgorithm;
import io.CSV;
import io.RandomRead;
import io.Reader;
import randomAndGreedy.GreedSearch;
import randomAndGreedy.RandomSearch;

public class Runner {
	
	final static String[] PATH = {"had12.txt", "had14.txt", "had16.txt", "had18.txt", "had20.txt"};
	final static String RESULTGREEDY = "greedy4.csv";
	final static String RESULTRANDOM = "random4.csv";
	final static String RESULTGAT = "gat4.csv";
	final static String RESULTGAR = "gar4.csv";
	
	public static void main(String[] args) {
		Reader reader = new Reader(PATH);
		Facility[] facilities = reader.aggregateFacility();
		
//		GreedRead greedRead = new GreedRead(facilities[0]);
		RandomRead read = new RandomRead();
		GreedSearch greedSearch = new GreedSearch();
		RandomSearch randomSearch = new RandomSearch();
		int i = 0;
		greedSearch.optGreed(facilities[i], read.assignRandomly(facilities[i].getNumber()), RESULTGREEDY);
		randomSearch.randomSearch(facilities[i], read.assignRandomly(facilities[i].getNumber()), RESULTRANDOM);
		GeneticAlgorithm GA = new GeneticAlgorithm();
		for(int j = 0; j < 10; j++) {
			GA.gaTour(facilities[i], read, RESULTGAT);
			GA.gaRoulette(facilities[i], read, RESULTGAR);
			}
		}
}
