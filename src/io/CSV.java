package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import evolution.GeneticAlgorithm.Generation;;

public class CSV {

	public static void writeFile(ArrayList<Generation> array, String name, long time) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(name));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##");
		sb.append("best");
        sb.append('-');
        sb.append("avg");
        sb.append('-');
        sb.append("worst");
        sb.append('\n');
        sb.append("time:" + time + " ms");
        sb.append('\n');
		for(Generation g : array) {
			sb.append(g.getBest());
			sb.append('-');
			sb.append(df.format(g.getAvg()));
			sb.append('-');
			sb.append(g.getWorst());
	        sb.append('\n');
		}
		pw.write(sb.toString());
	    pw.close();
	}
	
	public static void writeFileSimple(ArrayList<Integer> array, String name, long time) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(name));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("name");
        sb.append('\n');
        sb.append("time:" + time + " ms");
        sb.append('\n');
		for(int g : array) {
			sb.append(g);
	        sb.append('\n');
		}
		pw.flush();
		pw.write(sb.toString());
	    pw.close();
	}
}
