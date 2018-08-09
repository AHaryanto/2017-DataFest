
/*
 * Project04
 * 
 * This program takes orders for products and ship them to customers based on what they have in inventory.
 * This program reads an order from a text file, turns that order into a list of products, determines which products can 
 * be shipped now and which must be delayed until later, and then produce two reports – one showing what will be shipped now,
 * and one showing what portions of the order remain “on hold” for a later date.
 * 
 * 	@author Alvin Haryanto
 *  @version 20161019
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Project04 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Queue<SimpleProduct> myQueue = new LinkedList<SimpleProduct>();
		Stack<SimpleProduct> myStack = new Stack<SimpleProduct>();
		Customer myCustomer = new Customer();
		String inputFileName = getFileAndName(in, myQueue, myStack, myCustomer);
		printReport(in, myQueue, myStack, myCustomer);
	}

	private static String getFileAndName(Scanner in, Queue<SimpleProduct> myQueue, Stack<SimpleProduct> myStack,
			Customer myCustomer) {
		SimpleProduct product = new SimpleProduct();
		System.out.print("Enter database filename: ");
		String inputFileName = in.nextLine();
		try {
			File file = new File(inputFileName);
			Scanner inputFile = new Scanner(file);
			myCustomer.setLastName(inputFile.nextLine());
			myCustomer.setFirstName(inputFile.nextLine());
			myCustomer.setAddress(inputFile.nextLine());
			myCustomer.setCity(inputFile.nextLine());
			myCustomer.setState(inputFile.nextLine());
			myCustomer.setZip(inputFile.nextLine());
			myCustomer.setTax(Double.parseDouble(inputFile.nextLine()), in);
			while (product.readNextProduct(inputFile)) {
				if (product.getInStock() == true) {
					myQueue.add(product);
				} else {
					myStack.push(product);
				}
				product = new SimpleProduct();
			}
			inputFile.close();
		} catch (IOException e) {
			System.out.println("There was a problem reading from " + inputFileName);
		}
		return inputFileName;
	}

	private static void printReport(Scanner in, Queue<SimpleProduct> myQueue, Stack<SimpleProduct> myStack,
			Customer myCustomer) {
		System.out.println();
		System.out.println("Shipping To:");
		System.out.format("%7s %s", " ", myCustomer.getFirstName() + " " + myCustomer.getLastName());
		System.out.println();
		System.out.format("%7s %s", " ", myCustomer.getAddress());
		System.out.println();
		System.out.format("%7s %s", " ",
				myCustomer.getCity() + " " + myCustomer.getState() + " " + myCustomer.getZip());
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		double subtotal = 0;
		while (!myQueue.isEmpty()) {
			SimpleProduct head = myQueue.remove();
			double price = head.getQuantity() * head.getPrice();
			subtotal = subtotal + price;
			System.out.format("%1s %1s %1s %-30.30s %-10s %20s", " ", head.getQuantity(), "x", head.getName(),
					"(" + head.getType() + ")", price);
			System.out.println();
		}
		System.out.format("%1s %s", " ", "---------------------------------------------------------------------------");
		System.out.println();
		System.out.format("%1s %-60s %.2f", " ", "Subtotal:", subtotal);
		System.out.println();
		double tax = myCustomer.getTax() * subtotal;
		System.out.format("%1s %-61s %.2f", " ", "Sales Tax:  (" + myCustomer.getTax() + ")", tax);
		System.out.println();
		double shipping;
		if (subtotal >= 25) {
			shipping = 0;
		} else {
			if (subtotal >= 10) {
				shipping = subtotal * 1.05;
			} else {
				shipping = subtotal * 1.15;
			}
		}
		System.out.format("%1s %-61s %.2f", " ", "Shipping:", shipping);
		System.out.println();
		System.out.format("%1s %s", " ", "---------------------------------------------------------------------------");
		double total = subtotal + tax + shipping;
		System.out.println();
		System.out.format("%1s %-60s %.2f", " ", "Total:", total);
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("Orders Outstanding For: ");
		System.out.format("%7s %s", " ", myCustomer.getFirstName() + " " + myCustomer.getLastName());
		System.out.println();
		System.out.format("%7s %s", " ", myCustomer.getAddress());
		System.out.println();
		System.out.format("%7s %s", " ",
				myCustomer.getCity() + " " + myCustomer.getState() + " " + myCustomer.getZip());
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		double outstandingBalance = 0;
		while (!myStack.isEmpty()) {
			SimpleProduct head = myStack.pop();
			double price = head.getQuantity() * head.getPrice();
			outstandingBalance = outstandingBalance + price;
			System.out.format("%1s %1s %1s %-30.30s %-10s %20s", " ", head.getQuantity(), "x", head.getName(),
					"(" + head.getType() + ")", price);
			System.out.println();
		}
		System.out.format("%1s %s", " ", "---------------------------------------------------------------------------");
		System.out.println();
		System.out.format("%1s %-59s %.2f", " ", "Outstanding Balance:", outstandingBalance);
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
	}

}
