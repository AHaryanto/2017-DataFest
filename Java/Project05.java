/**
 * Project05
 * 
 *   The program performs the following actions:
 *   Ask the user to enter the name of the file that contains the paths.  Verify that the user has actually entered a value and not an empty line.
 *   Load that file into an adjacency list.
 *   Display the adjacency list to the console in a nicely formatted report.
 *   Prompt the user for the name of a start city.
 *   Find the shortest paths from that start city to all destinations and display that as a nicely formatted report.
 *   Continue asking for start cities until the user enters a blank line.
 *   
 *   @author Alvin Haryanto
 *   @version 20161114
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Project05 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a filename with paths: ");
		String fname = in.nextLine();
		while (fname.equals("")) {
			System.out.println("Filename can't be empty");
			System.out.print("Enter a filename with paths: ");
			fname = in.nextLine();
		}
		Map<String, List<Path>> adj_list = readPaths(fname);
		displayAdjacencyList(adj_list);
		System.out.println();
		System.out.print("Enter a start city (empty line to quit): ");
		String start = in.nextLine();
		while (!start.equals("")) {
			Map<String, Double> distances = new HashMap<String, Double>();
			distances = findDistances(start, adj_list);
			displayShortest(start, distances);
			System.out.print("Enter a start city (empty line to quit): ");
			start = in.nextLine();
			adj_list = readPaths(fname);
		}
		System.out.println("Goodbye!");
	}

	public static Map<String, List<Path>> readPaths(String fname) {
		Map<String, List<Path>> listOfPaths = new HashMap<String, List<Path>>();
		try {
			Scanner inFile = new Scanner(new File(fname));
			while (inFile.hasNext()) {
				String line = inFile.nextLine();
				String[] tokens = line.split(",");
				String node1 = tokens[0];
				String node2 = tokens[1];
				double distance = Double.parseDouble(tokens[2]);
				Path path1 = new Path(node2, distance);
				Path path2 = new Path(node1, distance);
				if (listOfPaths.containsKey(node1)) {
					listOfPaths.get(node1).add(path1);
				}
				if (listOfPaths.containsKey(node2)) {
					listOfPaths.get(node2).add(path2);
				}
				if (!listOfPaths.containsKey(node1)) {
					List<Path> list = new LinkedList<Path>();
					list.add(path1);
					listOfPaths.put(node1, list);
				}
				if (!listOfPaths.containsKey(node2)) {
					List<Path> list = new LinkedList<Path>();
					list.add(path2);
					listOfPaths.put(node2, list);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("There was a problem reading from " + fname);
		}
		return listOfPaths;
	}

	public static void displayAdjacencyList(Map<String, List<Path>> map) {
		System.out.println();
		System.out.printf("%-14s Paths", "Start City");
		System.out.println();
		System.out.println("-------------- ------------------------------");
		Set<String> keys = map.keySet();
		Iterator<String> keyIter = keys.iterator();
		while (keyIter.hasNext()) {
			String currentKey = keyIter.next();
			List currentListOfPaths = map.get(currentKey);
			System.out.printf("%-14s ", currentKey);
			for (int i = 0; i < currentListOfPaths.size() - 1; i++) {
				System.out.print(currentListOfPaths.get(i) + ", ");
			}
			System.out.print(currentListOfPaths.get(currentListOfPaths.size() - 1));
			System.out.println();
		}
	}

	public static Map<String, Double> findDistances(String start, Map<String, List<Path>> adj_list) {
		Map<String, Double> shortestDistances = new HashMap<String, Double>();
		PriorityQueue<Path> queue = new PriorityQueue<Path>();
		Path toItself = new Path(start, 0.0);
		queue.add(toItself);
		while (!queue.isEmpty()) {
			Path current = queue.remove();
			String destination = current.getEndpoint();
			if (!shortestDistances.containsKey(destination)) {
				double d = current.getCost();
				String dest = current.getEndpoint();
				shortestDistances.put(dest, d);
				List<Path> adjacencyList = new LinkedList<Path>();
				adjacencyList = adj_list.get(destination);
				while (!adjacencyList.isEmpty()) {
					Path head = adjacencyList.remove(0);
					String headDest = head.getEndpoint();
					double headCost = head.getCost() + d;
					Path newHead = new Path(headDest, headCost);
					queue.add(newHead);
				}
			}
		}
		return shortestDistances;
	}

	public static void displayShortest(String start, Map<String, Double> shortest) {
		System.out.println();
		System.out.println("Distances from " + start + " to each city:");
		System.out.printf("%-14s Distance", "Dest. City");
		System.out.println();
		System.out.println("-------------- --------");
		Set<String> keys = shortest.keySet();
		Iterator<String> keyIter = keys.iterator();
		while (keyIter.hasNext()) {
			String currentKey = keyIter.next();
			System.out.printf("%-14s %1s %6.2f", currentKey, " ", shortest.get(currentKey));
			System.out.println();
		}
		System.out.println();
	}

}