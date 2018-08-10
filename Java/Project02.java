/*
 * Project02.java
 * 
 *   This program delivers an inventory summary report. This program will read data about various products in a store's inventory from a file into
 *   a single Product object and maintain an ArrayList of Product objects. This program will display the report in the format of a table. 
 *   This program will also calculate the highest average user rating in stock, the lowest average user rating in stock,
 *   the largest total dollar amount in inventory, and the smallest total dollar amount in inventory.
 *   
 *   @author Alvin Haryanto
 *   @version 20160919
 */
package osu.cse2123;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Project02 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		ArrayList<Product> products = new ArrayList<Product>();
		getFile(in, products);
		printReport(products);
	}

	private static void getFile(Scanner in, ArrayList<Product> products) {
		System.out.print("Enter an inventory filename: ");
		String inputFileName = in.nextLine();
		try {
			File file = new File(inputFileName);
			Scanner inputFile = new Scanner(file);
			while (inputFile.hasNext()) {
				addAnItem(inputFile, products);
			}
			inputFile.close();
		} catch (IOException e) {
			System.out.println("There was a problem reading from " + inputFileName);
		}
	}

	private static void addAnItem(Scanner inputFile, ArrayList<Product> products) {
		boolean stop;
		Product product = new Product();
		product.setName(inputFile.nextLine());
		product.setInventoryCode(inputFile.nextLine());
		product.setQuantity(inputFile.nextInt());
		product.setPrice(inputFile.nextDouble());
		inputFile.nextLine();
		product.setType(inputFile.nextLine());
		stop = false;
		while (!stop) {
			int i = inputFile.nextInt();
			if (i >= 0) {
				product.addUserRating(i);
			} else {
				stop = true;
			}
		}
		if (inputFile.hasNext()) {
			inputFile.nextLine();
		}
		products.add(product);
	}

	private static void printReport(ArrayList<Product> products) {
		System.out.println("Product Inventory Summary Report");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println();
		String productName = "Product Name";
		String iCode = "I Code";
		String type = "Type";
		String rating = "Rating";
		String count = "# Rat.";
		String quantity = "Quant.";
		String price = "Price";
		System.out.format("%-25.25s %-9.9s %-5.5s %-6.6s %-6.6s %-6.6s %-6.6s", productName, iCode, type, rating, count,
				quantity, price);
		System.out.println();
		String dash = "---------------------------";
		System.out.format("%-25.25s %-9.9s %-5.5s %-6.6s %-6.6s %-6.6s %-6.6s", dash, dash, dash, dash, dash, dash,
				dash);
		System.out.println();
		for (int i = 0; i < products.size(); i++) {
			System.out.format("%-25.25s %-9.9s %-5.5s %-6.6s %6d %6d %6.2f", products.get(i).getName(),
					products.get(i).getInventoryCode(), products.get(i).getType(), stars(i, products),
					products.get(i).getUserRatingCount(), products.get(i).getQuantity(), products.get(i).getPrice());
			System.out.println();
		}
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("Total products in database: " + products.size());
		System.out.println("Highest Average Ranked item: " + products.get(highestRanked(products)).getName() + " ("
				+ stars(highestRanked(products), products) + ")");
		System.out.println("Lowest Average Ranked item: " + products.get(lowestRanked(products)).getName() + " ("
				+ stars(lowestRanked(products), products) + ")");
		System.out.println("Highest total dollar item: " + products.get(highestTotalDollar(products)).getName() + " ($"
				+ products.get(highestTotalDollar(products)).getPrice()
						* products.get(highestTotalDollar(products)).getQuantity()
				+ ")");
		System.out.println("Lowest total dollar item: " + products.get(lowestTotalDollar(products)).getName() + " ($"
				+ products.get(lowestTotalDollar(products)).getPrice()
						* products.get(lowestTotalDollar(products)).getQuantity()
				+ ")");
		System.out.println("---------------------------------------------------------------------------");
	}

	private static String stars(int index, ArrayList<Product> products) {
		String stars = "";
		for (int i = 0; i < products.get(index).getAvgUserRating(); i++) {
			stars = stars + "*";
		}
		return stars;
	}

	private static int highestRanked(ArrayList<Product> products) {
		int highestRanked = 0;
		for (int i = 1; i < products.size(); i++) {
			if (products.get(i).getAvgUserRating() > products.get(highestRanked).getAvgUserRating()) {
				highestRanked = i;
			}
		}
		return highestRanked;
	}

	private static int lowestRanked(ArrayList<Product> products) {
		int lowestRanked = 0;
		for (int i = 1; i < products.size(); i++) {
			if (products.get(i).getAvgUserRating() < products.get(lowestRanked).getAvgUserRating()) {
				lowestRanked = i;
			}
		}
		return lowestRanked;
	}

	private static int highestTotalDollar(ArrayList<Product> products) {
		int highestTotalDollar = 0;
		for (int i = 1; i < products.size(); i++) {
			if (products.get(i).getPrice() * products.get(i).getQuantity() > products.get(highestTotalDollar).getPrice()
					* products.get(highestTotalDollar).getQuantity()) {
				highestTotalDollar = i;
			}
		}
		return highestTotalDollar;
	}

	private static int lowestTotalDollar(ArrayList<Product> products) {
		int lowestTotalDollar = 0;
		for (int i = 1; i < products.size(); i++) {
			if (products.get(i).getPrice() * products.get(i).getQuantity() < products.get(lowestTotalDollar).getPrice()
					* products.get(lowestTotalDollar).getQuantity()) {
				lowestTotalDollar = i;
			}
		}
		return lowestTotalDollar;
	}

}
