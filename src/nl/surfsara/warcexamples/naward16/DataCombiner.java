package nl.surfsara.warcexamples.naward16;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

/*
 * Combines the input categories into a single comma separated file
 * @author Niek Tax
 */
public class DataCombiner {
	// Definieer hieronder de categorien
	static String[] categories = {"actors", "artists", "sportsmen", "politicians"};
	
	public static void main(String[] args){
		Multimap<String, String> entries = LinkedListMultimap.create();
		retrieveCategoryEntries(entries);
		try {
			createCSV(entries);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void retrieveCategoryEntries(Multimap<String, String> entries){
		BufferedReader br = null;
		
		for(String category: categories){
			try {
				br = new BufferedReader(new FileReader("data/"+category));
			} catch (FileNotFoundException e) {
				System.err.println("Warning: No inputfile found for category: "+category+", category was skipped");
				continue;
			}
			int lineNum = 1;
			String line;
			try {
				while((line = br.readLine()) != null){
					entries.put(line, category);
				}
			} catch (IOException e) {
				System.err.println("Warning: Unable to read line "+lineNum+" of "+category+" input file, line was skipped");	
			}
		}
	}
	
	private static void createCSV(Multimap<String, String> entries) throws IOException{
		CSVFormat format = CSVFormat.DEFAULT;
	    PrintWriter out = new PrintWriter(new FileWriter("data/combined.csv"));
	    CSVPrinter printer = new CSVPrinter(out, format);
		
		for(String key : entries.keySet()){
			for(String value: entries.get(key)){
				printer.printRecord(Arrays.asList(key, value));
			}
		}
		
		printer.close();
	    out.close();

	}
}
