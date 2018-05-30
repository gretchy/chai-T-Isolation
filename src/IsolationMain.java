import java.awt.Point;
import java.util.Vector;
import java.util.Scanner;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.io.*;

public class IsolationMain {
	public static Board root = new Board(); // root node for each search
	public static String computer = "X"; // symbol for computer player
	public static String opponent = "O"; // symbol for the opponent player
	public static String[][] board = new String[Board.size][Board.size]; // current board
	public static long timeLimit; // time limit for computer's calculations
	static long startTime = 0; // starts timing for computer to make its move
	static long elapsedTime = 0; // stores the amount of time that's already gone by
	static Vector<Board> children = new Vector<Board>(); // stores successors of current board
	static ArrayList<String> xMoves = new ArrayList<String>(); // keeps track of moves that X makes
	static ArrayList<String> oMoves = new ArrayList<String>(); // keeps track of moves that O makes
	static int depth = 6; // the maximum depth limit that will be searched
	static int round = 1; // indicates how many rounds of turns have processed
	static int infinity = 9999; // positive infinity for beta
	static int neg_infinity = -9999; // negative infinity for alpha

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);

		System.out.println("********************************");
		System.out.println("          ISOLATION");
		System.out.println(" Gretchen Lai - Nandita Chauhan");
		System.out.println("      CS 420 - Spring 2018");
		System.out.println("********************************");

		System.out.print("\nTime Limit per move: ");
		String input = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			input = br.readLine();
		}
		catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		timeLimit = Long.parseLong(input);

		System.out.print("First player (C or O)? ");
		String player = keyboard.nextLine().toUpperCase();

		// check to make sure user input is valid string
		while (!player.equals("C") && !player.equals("O")) {
			System.out.println("Invalid option given...");
			System.out.print("First player (C or O)? ");
			player = keyboard.nextLine().toUpperCase();
		}
		System.out.println("");

		initialize(); // set up the initial game board
		root.setState(board);

		boolean computer_turn; // indicates if it is the computer's turn to move
		if (player.equals("C"))
			computer_turn = true; // computer was chosen to start
		else
			computer_turn = false; // opponent was chosen to start

		root.printBoard(xMoves, oMoves); // print initial board (all blank w/ X & O at starting positions)

		while (true) {
			// increase the depth of search every 5 turns
			if (round / 5 == 2) {
				depth++;
				round = 1;
			}
			root.clearValidMoves();

			// computer's turn to make move
			if (computer_turn) {
				root.setTurn(computer);
				if (!root.isIsolated(computer)) { // computer still has room to move
					root.validMoves = root.setValidMoves();
					computerMove(); // computer makes move
					root.setState(board); // updates root with new settings
					root.printBoard(xMoves, oMoves); // print out board to reflect move
					System.out.println("\nComputer's move is: " + xMoves.get(xMoves.size() - 1));
					round++;
				}
				else { // no more moves possible
					System.out.println("\nX has been isolated. Opponent Wins!");
					break;
				}
			}
			// opponent's turn to make move
			else {
				root.setTurn(opponent);
				if (!root.isIsolated(opponent)) { // opponent still has room to move
					//root.validMoves = root.setValidMoves();
					opponentMove(); // opponent makes move
					//root.printBoard(xMoves, oMoves); // print out board to reflect move
					round++;
				}
				else { // no more moves possible
					System.out.println("\nO has been isolated. Computer Wins!");
					break;
				}
			}
			// switches to other player's turn
			computer_turn = !computer_turn;
		}

		System.out.println();
		keyboard.close();
	}

	// setting the game board to blanks "-" & starting positions of X and O
	public static void initialize() {
		for (int row = 0; row < Board.size; row++) {
			for (int col = 0; col < Board.size; col++) {
				if (row == 0 && col == 0)
					board[row][col] = "X"; // X starting position
				else if (row == 7 && col == 7)
					board[row][col] = "O"; // O starting position
				else
					board[row][col] = "-"; // represents empty, unvisited spot
			}
		}
	}

	// alpha-beta pruning method, returns the best move for computer to make
	public static int alphaBetaSearch(Board root, int depth_limit) {
		int score = maxValue(root, neg_infinity, infinity, depth_limit);
		return score;
	}

	// calculates best MAX value
	public static int maxValue(Board node, int alpha, int beta, int depth_limit) {
		elapsedTime = (System.nanoTime() - startTime) / 1000000000; // calculates how much time has already gone by

		// return utility of node because the depth has been exceeded, there are no
		// valid moves, or too much time has passed
		if (node.getDepth() >= depth_limit || node.getValidMoves().size() == 0 || elapsedTime >= .95 * timeLimit) {
			return node.evaluate();
		}

		int value = neg_infinity;
		Iterator itr = node.getValidMoves().iterator();

		// iterating through all the valid moves
		while (itr.hasNext()) {
			Point move = (Point) itr.next();
			Board child = new Board(node, move, node.getTurn());
			children.add(child);

			// grab the largest number between the already existing value and the minimum
			value = Math.max(value, minValue(child, alpha, beta, depth_limit));
			child.value = value;

			// if alpha >= beta, prune
			if (value >= beta)
				return value;

			alpha = Math.max(alpha, value);
		}

		return value;
	}

	// calculates best MIN value
	public static int minValue(Board node, int alpha, int beta, int depth_limit) {
		elapsedTime = (System.nanoTime() - startTime) / 1000000000; // calculates how much time has already gone by

		// return utility of node because the depth has been exceeded, there are no
		// valid moves, or too much time has passed
		if (node.getDepth() >= depth_limit || node.getValidMoves().size() == 0 || elapsedTime > .95 * timeLimit) {
			// best_move = node.findChar(node.getTurn());
			return node.evaluate();
		}

		int value = infinity;
		Iterator itr = node.getValidMoves().iterator();

		// iterating through all the valid moves
		while (itr.hasNext()) {
			Point move = (Point) itr.next();
			Board child = new Board(node, move, node.getTurn());

			// grab the smallest number between the already existing value and the minimum
			value = Math.min(value, minValue(child, alpha, beta, depth_limit));
			child.value = value;

			// if alpha >= beta, prune
			if (value >= beta)
				return value;

			alpha = Math.min(beta, value);
		}
		return value;
	}

	// edits the game board to reflect new valid move made by computer
	public static void computerMove() {
		startTime = System.nanoTime(); // starts timer for computer to find best move based on current situation
		children.clear(); // clearing children from previous calculations

		int score = alphaBetaSearch(root, depth);
		Iterator itr = children.iterator();

		// prepared in case there's multiple optimal children Boards to pick from
		ArrayList<Board> sameScoreChildren = new ArrayList<Board>();

		// iterate through all the children to find best one
		while (itr.hasNext()) {
			Board child = (Board) itr.next();
			if (child.value == score) {
				sameScoreChildren.add(child);
			}
		}

		int[] bestMove = new int[2]; // will hold the best move for the computer

		// among all children, randomly pick one
		if (sameScoreChildren.size() > 0) {
			// Random integer generator used for fairness
			Random numGen = new Random();
			bestMove = sameScoreChildren.get(numGen.nextInt(sameScoreChildren.size())).getPosition(computer);
		}

		int[] old_coord = root.getPosition(computer); // stores where X is currently located
		board[bestMove[0]][bestMove[1]] = computer; // stores the coordinates that X is to move to
		board[old_coord[0]][old_coord[1]] = "#"; // old position is marked with a used/visited symbol

		// convert row value from an integer to an A-H character
		String move;
		if (bestMove[0] == 0)
			move = "A";
		else if (bestMove[0] == 1)
			move = "B";
		else if (bestMove[0] == 2)
			move = "C";
		else if (bestMove[0] == 3)
			move = "D";
		else if (bestMove[0] == 4)
			move = "E";
		else if (bestMove[0] == 5)
			move = "F";
		else if (bestMove[0] == 6)
			move = "G";
		else
			move = "H";

		move += bestMove[1] + 1; // attached the column value
		xMoves.add(move); // computer makes move, add to ArrayList for printBoard() to use
	}

	// edits the game board to reflect new valid move made by opponent
	public static void opponentMove() {
		int[] coordinates = root.getPosition(opponent); // stored current coordinates of O

		// grabbing opponent's new location
		System.out.print("\nEnter oponent's move: ");
		String move = "";

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			move = br.readLine();
		}
		catch (Exception e) {
			System.out.println("Exception: " + e);
		}

		// converting the row value from A-H into corresponding integer
		int rowValue;
		if (move.startsWith("A") || move.startsWith("a"))
			rowValue = 0;
		else if (move.startsWith("B") || move.startsWith("b"))
			rowValue = 1;
		else if (move.startsWith("C") || move.startsWith("c"))
			rowValue = 2;
		else if (move.startsWith("D") || move.startsWith("d"))
			rowValue = 3;
		else if (move.startsWith("E") || move.startsWith("e"))
			rowValue = 4;
		else if (move.startsWith("F") || move.startsWith("f"))
			rowValue = 5;
		else if (move.startsWith("G") || move.startsWith("g"))
			rowValue = 6;
		else if (move.startsWith("H") || move.startsWith("h"))
			rowValue = 7;
		else
			rowValue = 10;

		// grabbing the column value
		int colValue = Integer.parseInt(move.substring(1)) - 1;

		// checks to see if move is valid
		if (root.checkMove(opponent, rowValue, colValue)) {
			oMoves.add(move); // opponent makes move, add to ArrayList for printBoard() to use
			root.setState(rowValue, colValue, opponent); // O moves to new spot
			root.setState(coordinates[0], coordinates[1], "#"); // old position is marked with a # symbol
		}
		else { // keep asking until opponent enters valid move
			System.out.println("\nInvalid move. Please enter another move.");
			opponentMove();
		}
	}
}
