package osu.cse2123;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
public class Project01 {

	public static void main(String[] args) {
		Scanner in = new Scanner (System.in);
		ArrayList<String> list = getList(in);
		System.out.println();
		ArrayList<String> titles = getTitle(list);
		ArrayList<String> quantities = getQuantity(list);
		ArrayList<String> prices = getPrice(list);
		ArrayList<String> types = getType(list);
		int largestQuantityIndex = getLargestQuantityIndex(quantities);
		int smallestQuantityIndex = getSmallestQuantityIndex(quantities);
		int highestTotalDollarItemIndex = getHighestTotalDollarItemIndex(quantities,prices);
		int lowestTotalDollarItemIndex = getLowestTotalDollarItemIndex(quantities,prices);
		printReport(titles,quantities,prices,types,largestQuantityIndex,smallestQuantityIndex,highestTotalDollarItemIndex,lowestTotalDollarItemIndex);
	}
	
	private static ArrayList<String> getList(Scanner inScanner) {
		ArrayList<String> list = new ArrayList<String>();
		System.out.print("Enter database filename: ");
		String inputFileName = inScanner.nextLine();
		try {
			File file = new File (inputFileName);
			Scanner inputFile = new Scanner (file);
			while (inputFile.hasNext()) {
				list.add(inputFile.nextLine());
				}
			inputFile.close();}
		catch (IOException e) {
			System.out.println("There was a problem reading from "+inputFileName);}
		return list;
	}
	
	private static ArrayList<String> getTitle(ArrayList<String> list){
		ArrayList<String> titles = new ArrayList<String>();
		for (int i=0;i<list.size();i=i+4){
			titles.add(list.get(i));
		}
		return titles;
	}
	
	private static ArrayList<String> getQuantity(ArrayList<String> list){
		ArrayList<String> quantities = new ArrayList<String>();
		for (int i=1;i<list.size();i=i+4){
			quantities.add(list.get(i));
		}
		return quantities;
	}
	
	private static ArrayList<String> getPrice(ArrayList<String> list){
		ArrayList<String> prices = new ArrayList<String>();
		for (int i=2;i<list.size();i=i+4){
			prices.add(list.get(i));
		}
		return prices;
	}
	
	private static ArrayList<String> getType(ArrayList<String> list){
		ArrayList<String> types = new ArrayList<String>();
		for (int i=3;i<list.size();i=i+4){
			types.add(list.get(i));
		}
		return types;
	}
	
	private static int getLargestQuantityIndex(ArrayList<String> quantities){
		int largestIndex=0;
		for (int i=1;i<quantities.size();i++){
			if (Integer.parseInt(quantities.get(i))>Integer.parseInt(quantities.get(largestIndex))){
				largestIndex=i;
			}
		}
		return largestIndex;
	}
	
	private static int getSmallestQuantityIndex(ArrayList<String> quantities){
		int smallestIndex=0;
		for (int i=1;i<quantities.size();i++){
			if (Integer.parseInt(quantities.get(i))<Integer.parseInt(quantities.get(smallestIndex))){
				smallestIndex=i;
			}
		}
		return smallestIndex;
	}
	
	private static int getHighestTotalDollarItemIndex(ArrayList<String> quantities, ArrayList<String> prices) {
		int highestTotalDollarItemIndex=0;
		for (int i=1;i<quantities.size();i++){
			if ((Double.parseDouble(quantities.get(i))*Double.parseDouble(prices.get(i)))>
			(Double.parseDouble(quantities.get(highestTotalDollarItemIndex))*Double.parseDouble(prices.get(highestTotalDollarItemIndex)))){
				highestTotalDollarItemIndex=i;
			}
		}
		return highestTotalDollarItemIndex;
	}
	
	private static int getLowestTotalDollarItemIndex(ArrayList<String> quantities, ArrayList<String> prices) {
		int lowestTotalDollarItemIndex=0;
		for (int i=1;i<quantities.size();i++){
			if ((Double.parseDouble(quantities.get(i))*Double.parseDouble(prices.get(i)))<
			(Double.parseDouble(quantities.get(lowestTotalDollarItemIndex))*Double.parseDouble(prices.get(lowestTotalDollarItemIndex)))){
				lowestTotalDollarItemIndex=i;
			}
		}
		return lowestTotalDollarItemIndex;
	}
	
	private static void printReport(ArrayList<String> titles, ArrayList<String> quantities,
			ArrayList<String> prices, ArrayList<String> types, int largestQuantityIndex, int smallestQuantityIndex,
			int highestTotalDollarItemIndex,int lowestTotalDollarItemIndex) {
		System.out.println("Product Summary Report");
		System.out.println("------------------------------------------------------------");
		int index=0;
		while (index<titles.size()) {
			System.out.println("Title: "+titles.get(index));
			System.out.println("	"+"Product Type: "+types.get(index));
			System.out.println("	"+"Price: "+prices.get(index));
			System.out.println("	"+"Quantity: "+quantities.get(index));
			System.out.println();
			index++;
		}
		System.out.println("-----------------------------------------------------------------");
		System.out.println("Total products in database: "+titles.size());
		System.out.println("Largest quantity item: "+titles.get(largestQuantityIndex)+" ("+types.get(largestQuantityIndex)+")");
		System.out.println("Highest total dollar item: "+titles.get(highestTotalDollarItemIndex)+" ($"+Double.parseDouble(prices.get(highestTotalDollarItemIndex))*
				Double.parseDouble(quantities.get(highestTotalDollarItemIndex))+")");
		System.out.println("Smallest quantity item: "+titles.get(smallestQuantityIndex)+" ("+types.get(smallestQuantityIndex)+")");
		System.out.println("Lowest total dollar item: "+titles.get(lowestTotalDollarItemIndex)+" ($"+Double.parseDouble(prices.get(lowestTotalDollarItemIndex))*
				Double.parseDouble(quantities.get(lowestTotalDollarItemIndex))+")");
		System.out.println("-----------------------------------------------------------------");
	}
	

}
