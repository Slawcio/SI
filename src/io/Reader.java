package io;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Facility;

public class Reader {
	
	  private static final Logger LOGGER = Logger.getLogger( Reader.class.getName() );

	String[] PATH;
	
	public Reader(String[] path){
		this.PATH = path;
	}
	
	public Facility[] aggregateFacility() {
		Facility[] facilities = new Facility[PATH.length];
		for(int i = 0; i < PATH.length; i++) {
			facilities[i] = getFacility(PATH[i]);
		}
		return facilities;
	}

	private Facility getFacility(String argPATH) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(argPATH));
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		int counter = 0;
		Facility result = new Facility();
		result.setNumber(scanner.nextInt());
		
		while(scanner.hasNextLine()) {
			String line;
			Scanner colReader = new Scanner(line = scanner.nextLine());
			if(line.equals("") || line.equals("   ")) { 
				counter++;}
			else {
				ArrayList<Integer> row = new ArrayList<Integer>();
				while(colReader.hasNextInt()) {
					row.add(colReader.nextInt());
				}
				if(counter>2) {
					result.addFlow(row);
				} else {
					result.addLocation(row);
				}
			} // if(line.equals(""))
			colReader.close();
		}
		scanner.close();
		return result;
	}
}
