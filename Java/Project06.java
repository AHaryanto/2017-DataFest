/**
 * Project06
 * 
 *   This program will perform simple algebraic operations (+, -, *, /, %), and will be able to handle expressions of 
 *   any length properly by putting the expression into a binary expression tree and then evaluating the expression tree.
 *   These methods make use of the ExpressionTree<E> class.
 *   
 *   @author Alvin Haryanto
 *   @version 20161206
 */

import java.util.*;
import osu.cse2123.TreeNode;

public class Project06 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("No expression in memory");
		System.out.println();
		String choice = enterAChoice(in);
		TreeNode<String> expr = new TreeNode<String>("");
		int result = 0;
		while (!choice.equals("Q")) {
			if (choice.equals("E")) {
				String input = enterAnExpression(in);
				expr = ExpressionTree.buildTreeFromString(input);
				result = ExpressionTree.evaluate(expr);
				System.out.println(input + " = " + result);
				System.out.println();
			}
			if (choice.equals("S")) {
				setTheDisplayFormat(in, expr, result);
			}
			choice = enterAChoice(in);
		}
		System.out.println("Goodbye!");

	}

	public static void setTheDisplayFormat(Scanner in, TreeNode<String> expr, int result) {
		System.out.println("Enter your preferred output display:");
		System.out.println("[P]ostfix");
		System.out.println("[I]nfix");
		System.out.println("p[R]efix");
		System.out.print("> ");
		String input = in.nextLine();
		input = input.toUpperCase();
		while (!(input.equals("P") || input.equals("I") || input.equals("R"))) {
			System.out.println("ERROR!  You must enter one of [P], [I] or [R]!");
			System.out.println();
			System.out.println("Enter your preferred output display:");
			System.out.println("[P]ostfix");
			System.out.println("[I]nfix");
			System.out.println("p[R]efix");
			System.out.print("> ");
			input = in.nextLine();
			input = input.toUpperCase();
		}
		if (input.equals("P")) {
			System.out.println(ExpressionTree.toPostfixString(expr) + " = " + result);
		} else {
			if (input.equals("I")) {
				System.out.println(ExpressionTree.toInfixString(expr) + " = " + result);
			} else {
				if (input.equals("R")) {
					System.out.println(ExpressionTree.toPrefixString(expr) + " = " + result);
				}
			}
		}
		System.out.println();
	}

	public static String enterAnExpression(Scanner in) {
		System.out.print("Enter your expression in postfix notation: ");
		String input = in.nextLine();
		while (ExpressionTree.buildTreeFromString(input) == null) {
			System.out.println();
			System.out.println("ERROR! Expression not in postfix notation!");
			System.out.println();
			System.out.print("Enter your expression in postfix notation: ");
			input = in.nextLine();
		}
		return input;
	}

	public static String enterAChoice(Scanner in) {
		System.out.println("Enter your choice: ");
		System.out.println("[S]et the display format");
		System.out.println("[E]nter a new expression");
		System.out.println("[Q]uit");
		System.out.print("> ");
		String input = in.nextLine();
		input = input.toUpperCase();
		while (!(input.equals("S") || input.equals("E") || input.equals("Q"))) {
			System.out.println("ERROR!  You must enter one of [E], [S] or [Q]!");
			System.out.println();
			System.out.println("Enter your choice: ");
			System.out.println("[S]et the display format");
			System.out.println("[E]nter a new expression");
			System.out.println("[Q]uit");
			System.out.print("> ");
			input = in.nextLine();
			input = input.toUpperCase();
		}
		System.out.println();
		return input;
	}

}
