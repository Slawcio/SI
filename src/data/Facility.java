package data;
import java.util.ArrayList;

public class Facility {
	private ArrayList<ArrayList<Integer>> location;
	private ArrayList<ArrayList<Integer>> flow;
	private int number;
	
	public Facility(){
		location = new ArrayList<ArrayList<Integer>>();
		flow = new ArrayList<ArrayList<Integer>>();
	}
	
	public void addLocation(ArrayList<Integer> row){
		location.add(row);
	}
	
	public void addFlow(ArrayList<Integer> row) {
		flow.add(row);
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	
	public ArrayList<ArrayList <Integer>> getLocation(){
		return location;
	}
	
	public ArrayList<ArrayList <Integer>> getFlow(){
		return flow;
	}
	
	
}
