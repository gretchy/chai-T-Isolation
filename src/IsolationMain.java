import java.util.Scanner;
import java.lang.String;
import java.util.ArrayList;
import java.io.*;

public class IsolationMain {
	public static Board root = new Board(); // root node for each search
	public static String computer = "X"; // symbol for computer player
	public static String opponent = "O"; // symbol for the human player
	public static String[][] board = new String[Board.size][Board.size]; // current board
	public static long timeLimit; // time limit for computer's calculations
	static ArrayList<String> xMoves = new ArrayList<String>(); // keeps track of moves that X makes
	static ArrayList<String> oMoves = new ArrayList<String>(); // keeps track of moves that O makes
	static int depth = 6; // the maximum depth limit that will be searched
	static int round = 1; // indicates how many rounds of turns have processed

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);

		System.out.println("********************************");
		System.out.println("          ISOLATION");
		System.out.println(" Gretchen Lai - Nandita Chauhan");
		System.out.println("      CS 420 - Spring 2018");
		System.out.println("********************************");

		System.out.print("\nTime Limit per move: ");
		String choice = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			choice = br.readLine();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		timeLimit = Long.parseLong(choice);

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

		boolean computerTurn; // indicates if it is the computer's turn to move
		if (player.equals("C"))
			computerTurn = true; // computer was chosen to start
		else
			computerTurn = false; // opponent was chosen to start

		root.printBoard(xMoves, oMoves); // print initial board (all blank w/ X & O at starting positions)
		
		while (true) {
			// increase the depth of search every 5 turns
			if (round / 5 == 2) {
				depth++;
				round = 1;
			}
			// computer's turn to make move
			if (computerTurn) {
				root.setTurn(computer);
				if (!root.isIsolated(computer)) {
					root.validMoves = root.setValidMoves();
					computerMove();
					root.setState(board);
					root.printBoard(xMoves, oMoves);
					System.out.println("\nComputer's move is: " + xMoves.get(xMoves.size() - 1));
					round++;
				}
				else {
					System.out.println("\nX has been isolated. Opponent Wins!");
					break;
				}
			}
			// opponent's turn to make move
			else {
				root.setTurn(opponent);
				if (!root.isIsolated(opponent)) {
					opponentMove();
					root.printBoard(xMoves, oMoves);
					round++;
				}
				else {
					System.out.println("\nO has been isolated. Computer Wins!");
					break;
				}
			}
			
			// switches to other player's turn
			computerTurn = !computerTurn;
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

	// edits the game board to reflect new valid move made by computer
	public static void computerMove() {
		AlphaBetaPruning instance = new AlphaBetaPruning(); // activate Alpha-Beta Pruning object
		
		int[] old_coord = root.getPosition(computer); // stores where X is currently located
		int[] moveToCoords = instance.getBestMove(root, depth, timeLimit); // stores the coordinates that X is to move to
		board[moveToCoords[0]][moveToCoords[1]] = computer;

		// convert row value from an integer to an A-H character
		String move;
		if (moveToCoords[0] == 0)
			move = "A";
		else if (moveToCoords[0] == 1)
			move = "B";
		else if (moveToCoords[0] == 2)
			move = "C";
		else if (moveToCoords[0] == 3)
			move = "D";
		else if (moveToCoords[0] == 4)
			move = "E";
		else if (moveToCoords[0] == 5)
			move = "F";
		else if (moveToCoords[0] == 6)
			move = "G";
		else
			move = "H";
		
		move += moveToCoords[1] + 1; // attached the column value

		xMoves.add(move); // computer makes move, add to ArrayList for printBoard() to use
		board[old_coord[0]][old_coord[1]] = "#"; // old position is marked with a used/visited symbol
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
		} catch (Exception e) {
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
