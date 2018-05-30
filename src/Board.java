import java.awt.Point;
import java.util.Vector;
import java.util.ArrayList;

public class Board implements Comparable<Board> {
	public static int size = 8; // width of board
	private String[][] state; // current state of the game
	private String turn; // stores which player's turn it is
	private int depth; // depth to reach when searching for best move to make
	public Vector<Point> validMoves; // all the legal moves that a player can make
	public int value; // used for utility function

	// basic constructor
	public Board() {
		this.state = new String[size][size];
		this.turn = "X";
		this.depth = 0;
		this.validMoves = new Vector<Point>();
		this.value = 0;
	}

	// constructor with parameters: Board, Point, and String
	public Board(Board parent, Point move_to, String player) {
		// this.state = parent.state;
		this.state = new String[size][size];

		// copy over board state from parent/previous state of the game
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++)
				this.state[i][j] = parent.state[i][j];
		}

		// stores the old coordinates of player
		int[] old_coordinates = this.getPosition(player);

		this.setState((int) move_to.getX(), (int) move_to.getY(), player);
		this.setState(old_coordinates[0], old_coordinates[1], "#"); // mark old location with visited symbol

		if (player.equals("X"))
			this.turn = "O"; // switching turn to O
		else
			this.turn = "X"; // switching turn to X

		this.depth = parent.depth + 1;
		this.validMoves = this.setValidMoves();
		this.value = 0;
	}

	// getter for the current state
	public String[][] getState() {
		return this.state;
	}

	// getter for the turn variable
	public String getTurn() {
		return this.turn;
	}

	// getter for the depth
	public int getDepth() {
		return this.depth;
	}

	// getter for all valid moves
	public Vector<Point> getValidMoves() {
		return this.validMoves;
	}

	// setter to update the state with a String parameter
	public void setState(String[][] that_state) {
		this.state = that_state;
	}

	// setter to update the state with parameters: int, int, and String
	public void setState(int i, int j, String val) {
		this.state[i][j] = val;
	}

	// setter for the player turn
	public void setTurn(String t) {
		this.turn = t;
	}

	/*
	 * setValidMoves(String current_String) sets the validMoves Vector with
	 * appropriate values; returns: NA args: NA
	 */
	public Vector<Point> setValidMoves() {
		int limit; // to be used in diagonals
		Vector<Point> valid_moves = new Vector<Point>(); // stores all the possible valid moves
		int[] coordinates = getPosition(this.turn); // stores current player's coordinates

		// cardinal direction - NORTH
		for (int index = coordinates[0] - 1; index >= 0; index--) {
			if (this.state[index][coordinates[1]].equals("-"))
				valid_moves.add(new Point(index, coordinates[1]));
			else
				break;
		}
		// cardinal direction - EAST
		for (int index = coordinates[1] + 1; index < size; index++) {
			if (this.state[coordinates[0]][index].equals("-"))
				valid_moves.add(new Point(coordinates[0], index));
			else
				break;
		}
		// cardinal direction - SOUTH
		for (int index = coordinates[0] + 1; index < size; index++) {
			if (this.state[index][coordinates[1]].equals("-"))
				valid_moves.add(new Point(index, coordinates[1]));
			else
				break;
		}
		// cardinal direction - WEST
		for (int index = coordinates[1] - 1; index >= 0; index--) {
			if (this.state[coordinates[0]][index].equals("-"))
				valid_moves.add(new Point(coordinates[0], index));
			else
				break;
		}

		// NORHTEAST diagonal
		if (coordinates[0] > size - coordinates[1] - 1)
			limit = size - coordinates[1] - 1;
		else
			limit = coordinates[0];
		for (int index = 1; index <= limit; index++) {
			if (this.state[coordinates[0] - index][coordinates[1] + index].equals("-")) {
				valid_moves.add(new Point((coordinates[0] - index), (coordinates[1] + index)));
			} else
				break;
		}
		// SOUTHEAST diagonal
		if (size - coordinates[0] - 1 < size - coordinates[1] - 1)
			limit = size - coordinates[0] - 1;
		else
			limit = size - coordinates[1] - 1;
		for (int index = 1; index <= limit; index++) {
			if (this.state[coordinates[0] + index][coordinates[1] + index].equals("-")) {
				valid_moves.add(new Point((coordinates[0] + index), (coordinates[1] + index)));
			} else
				break;
		}
		// SOUTHWEST diagonal
		if (size - coordinates[0] - 1 < coordinates[1])
			limit = size - coordinates[0] - 1;
		else
			limit = coordinates[1];
		for (int index = 1; index <= limit; index++) {
			if (this.state[coordinates[0] + index][coordinates[1] - index].equals("-")) {
				valid_moves.add(new Point((coordinates[0] + index), (coordinates[1] - index)));
			} else
				break;
		}
		// NORTHWEST diagonal
		if (coordinates[0] > coordinates[1])
			limit = coordinates[1];
		else
			limit = coordinates[0];
		for (int index = 1; index <= limit; index++) {
			if (this.state[coordinates[0] - index][coordinates[1] - index].equals("-")) {
				valid_moves.add(new Point((coordinates[0] - index), (coordinates[1] - index)));
			} else
				break;
		}

		return valid_moves;
	}

	// calculates number of available moves that opponent has
	public int opponentValidMoves(String opponent) {
		int limit; // to be used in diagonals
		Vector<Point> valid_moves = new Vector<Point>();
		int[] coordinates = getPosition(opponent); // stores opponent's current location

		// cardinal direction - NORTH
		for (int index = coordinates[0] - 1; index >= 0; index--) {
			if (this.state[index][coordinates[1]].equals("-"))
				valid_moves.add(new Point(index, coordinates[1]));
			else
				break;
		}
		// cardinal direction - EAST
		for (int index = coordinates[1] + 1; index < size; index++) {
			if (this.state[coordinates[0]][index].equals("-"))
				valid_moves.add(new Point(coordinates[0], index));
			else
				break;
		}
		// cardinal direction - SOUTH
		for (int index = coordinates[0] + 1; index < size; index++) {
			if (this.state[index][coordinates[1]].equals("-"))
				valid_moves.add(new Point(index, coordinates[1]));
			else
				break;
		}
		// cardinal direction - WEST
		for (int index = coordinates[1] - 1; index >= 0; index--) {
			if (this.state[coordinates[0]][index].equals("-"))
				valid_moves.add(new Point(coordinates[0], index));
			else
				break;
		}
		// NORTHWEST diagonal
		if (coordinates[0] > coordinates[1])
			limit = coordinates[1];
		else
			limit = coordinates[0];
		for (int index = 1; index <= limit; index++) {
			if (this.state[coordinates[0] - index][coordinates[1] - index].equals("-")) {
				valid_moves.add(new Point((coordinates[0] - index), (coordinates[1] - index)));
			} else
				break;
		}
		// NORTHEAST diagonal
		if (coordinates[0] > size - coordinates[1] - 1)
			limit = size - coordinates[1] - 1;
		else
			limit = coordinates[0];
		for (int index = 1; index <= limit; index++) {
			if (this.state[coordinates[0] - index][coordinates[1] + index].equals("-")) {
				valid_moves.add(new Point((coordinates[0] - index), (coordinates[1] + index)));
			} else
				break;
		}

		// SOUTHWEST diagonal
		if (size - coordinates[0] - 1 < coordinates[1])
			limit = size - coordinates[0] - 1;
		else
			limit = coordinates[1];
		for (int index = 1; index <= limit; index++) {
			if (this.state[coordinates[0] + index][coordinates[1] - index].equals("-")) {
				valid_moves.add(new Point((coordinates[0] + index), (coordinates[1] - index)));
			} else
				break;
		}
		// SOUTHEAST diagonal
		if (size - coordinates[0] - 1 < size - coordinates[1] - 1)
			limit = size - coordinates[0] - 1;
		else
			limit = size - coordinates[1] - 1;
		for (int index = 1; index <= limit; index++) {
			if (this.state[coordinates[0] + index][coordinates[1] + index].equals("-")) {
				valid_moves.add(new Point((coordinates[0] + index), (coordinates[1] + index)));
			} else
				break;
		}
		return valid_moves.size();
	}

	// evaluation of utility function
	public int evaluate() {
		/* multipliers */
		int X = 3;
		int Y = 1;
		int Z = 3;
		int W = 2;

		String that_String = "X";
		if (this.turn.equals("X"))
			that_String = "O";
		int this_row = this.getPosition(this.turn)[0];
		int other_row = this.getPosition(that_String)[0];
		int this_col = this.getPosition(this.turn)[1];
		int other_col = this.getPosition(that_String)[1];
		// start with X times the number of valid moves
		int score = X * this.validMoves.size();
		// subtract Y times the number of opponent's valid moves
		score -= Y * this.opponentValidMoves(that_String);
		// subtract 10 for each wall it's next to
		if (this_row == 0 || this_row == size - 1)
			score -= 10;
		if (this_col == 0 || this_col == size - 1)
			score -= 10; // score = score = 10;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j < 1; j++) {
				if (i + this_row >= 0 && i + this_row <= size - 1 && j + this_col >= 0 && j + this_col <= size - 1) {
					if (!this.state[this_row + i][this_col + j].equals("-"))
						score -= Z; // subtract Z for each surrounding cell that is filled
				}
				if (i + other_row >= 0 && i + other_row <= size - 1 && j + other_col >= 0
						&& j + other_col <= size - 1) {
					if (!this.state[other_row + i][other_col + j].equals("-"))
						score += Z; // add Z for each cell surrounding the opponent that is filled
				}
			}
		}

		this.value = score;
		return this.value;
	}

	// check if the move is valid for a player to make
	public boolean checkMove(String player, String move) {
		// converting the row value from A-F into integers
		int newRow;
		if (move.startsWith("A") || move.startsWith("a"))
			newRow = 0;
		else if (move.startsWith("B") || move.startsWith("b"))
			newRow = 1;
		else if (move.startsWith("C") || move.startsWith("c"))
			newRow = 2;
		else if (move.startsWith("D") || move.startsWith("d"))
			newRow = 3;
		else if (move.startsWith("E") || move.startsWith("e"))
			newRow = 4;
		else if (move.startsWith("F") || move.startsWith("f"))
			newRow = 5;
		else if (move.startsWith("G") || move.startsWith("g"))
			newRow = 6;
		else if (move.startsWith("H") || move.startsWith("h"))
			newRow = 7;
		else
			return false; // row is out of bounds

		int newCol = Integer.parseInt(move.substring(1)) - 1;
		if (newCol < 0 || newCol > 7)
			return false; // column is out of bounds

		if (getPosition(player)[0] == newRow && getPosition(player)[1] == newCol)
			return false; // player didn't move at all

		int vertDistance = Math.abs(getPosition(player)[0] - newRow); // distance between current row and new row of
																		// move
		int horizDistance = Math.abs(getPosition(player)[1] - newCol); // distance between current column and new col of
																		// move

		// check cardinal direction - NORTH
		if (newRow < getPosition(player)[0] && newCol == getPosition(player)[1]) {
			// check for obstructions in path
			for (int path = 0; path < vertDistance; path++) {
				int spotInPath = getPosition(player)[0] - path - 1;
				if (!state[spotInPath][newCol].equals("-"))
					return false; // there is an obstacle
			}

			return true; // there is no obstacle, player made valid move
		}

		// check cardinal direction - EAST
		if (newRow == getPosition(player)[0] && newCol > getPosition(player)[1]) {
			// check for obstructions in path
			for (int path = 0; path < horizDistance; path++) {
				int spotInPath = getPosition(player)[1] + path + 1;
				if (!state[newRow][spotInPath].equals("-"))
					return false; // there is an obstacle
			}

			return true; // there is no obstacle, player made valid move
		}

		// check cardinal direction - SOUTH
		if (newRow > getPosition(player)[0] && newCol == getPosition(player)[1]) {
			// check for obstructions in path
			for (int path = 0; path < vertDistance; path++) {
				int spotInPath = getPosition(player)[0] + path + 1;
				if (!state[spotInPath][newCol].equals("-"))
					return false; // there is an obstacle
			}

			return true; // there is no obstacle, player made valid move
		}

		// check cardinal direction - WEST
		if (newRow == getPosition(player)[0] && newCol < getPosition(player)[1]) {
			// check for obstructions in path
			for (int path = 0; path < horizDistance; path++) {
				int spotInPath = getPosition(player)[1] - path - 1;
				if (!state[newRow][spotInPath].equals("-"))
					return false; // there is an obstacle
			}

			return true; // there is no obstacle, player made valid move
		}

		// check diagonals
		if (vertDistance == horizDistance) {

			// NORTHEAST diagonal
			if (newRow < getPosition(player)[0] && newCol > getPosition(player)[1]) {
				// check for obstructions in path
				for (int path = 0; path < horizDistance; path++) {
					int tempRow = getPosition(player)[0] - path - 1;
					int tempCol = getPosition(player)[1] + path + 1;
					if (!state[tempRow][tempCol].equals("-"))
						return false; // there is an obstacle
				}

				return true; // there is no obstacle, player made valid move
			}

			// SOUTHEAST diagonal
			if (newRow > getPosition(player)[0] && newCol > getPosition(player)[1]) {
				// check for obstructions in path
				for (int path = 0; path < horizDistance; path++) {
					int tempRow = getPosition(player)[0] + path + 1;
					int tempCol = getPosition(player)[1] + path + 1;
					if (!state[tempRow][tempCol].equals("-"))
						return false; // there is an obstacle
				}

				return true; // there is no obstacle, player made valid move
			}

			// NORTHWEST diagonal
			if (newRow < getPosition(player)[0] && newCol < getPosition(player)[1]) {
				// check for obstructions in path
				for (int path = 0; path < horizDistance; path++) {
					int tempRow = getPosition(player)[0] - path - 1;
					int tempCol = getPosition(player)[1] - path - 1;
					if (!state[tempRow][tempCol].equals("-"))
						return false; // there is an obstacle
				}

				return true; // there is no obstacle, player made valid move
			}

			// SOUTHWEST diagonal
			if (newRow > getPosition(player)[0] && newCol < getPosition(player)[1]) {
				// check for obstructions in path
				for (int path = 0; path < horizDistance; path++) {
					int tempRow = getPosition(player)[0] + path + 1;
					int tempCol = getPosition(player)[1] - path - 1;
					if (!state[tempRow][tempCol].equals("-"))
						return false; // there is an obstacle
				}

				return true; // there is no obstacle, player made valid move
			}
		}

		return false; // the move isn't a valid queen move
	}

	// gets the position of a specified player
	public int[] getPosition(String player) {
		int[] position = new int[2];

		for (int row = 0; row < state.length; row++) {
			for (int col = 0; col < state.length; col++) {
				if (state[row][col].equals(player)) {
					position[0] = row;
					position[1] = col;
				}
			}
		}

		return position;
	}

	// checks if a player has been isolated by the opponent
	public boolean isIsolated(String player) {
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (x != 0 || y != 0) {
					// check if row value would be out of bounds
					if (getPosition(player)[0] + x >= 0 && getPosition(player)[0] + x <= 8) {
						// check if column value would be out of bounds
						if (getPosition(player)[1] + y >= 0 && getPosition(player)[1] + y <= 8) {
							if (state[getPosition(player)[0] + x][getPosition(player)[1] + y].equals("-"))
								return false; // there is still an empty space for player to move to
						}
					}
				}
			}
		}

		return true; // player has been isolated by opponent
	}

	// implementing Comparable's compareTo() method to work with comparing Boards
	public int compareTo(Board other) {
		String[][] thisState = this.getState();
		String[][] otherState = other.getState();
		int length = this.getState().length;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (!thisState[i][j].equals(otherState[i][j]))
					return 1;
			}
		}
		return 0;
	}

	// overrides equals() method
	public boolean equals(Board other) {
		if (this.compareTo(other) == 0)
			return true;
		else
			return false;
	}

	// prints out the current game board settings along with the past moves made by
	// players
	public void printBoard(ArrayList<String> xMoves, ArrayList<String> oMoves) {
		String[] rowIndexes = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };

		System.out.println("\n  1 2 3 4 5 6 7 8\tComputer vs. Oponent");

		// printing actual playing board portion
		for (int row = 0; row < size; row++) {
			String completeRow = rowIndexes[row] + " ";
			for (int col = 0; col < size; col++) {
				completeRow += String.valueOf(this.state[row][col]);
				completeRow += " ";
			}

			// there exists moves made by X
			if (row < xMoves.size()) {
				System.out.print(completeRow + "\t" + (row + 1) + ". ");

				// there exists moves made by O
				if (row < oMoves.size()) {
					System.out.print(xMoves.get(row));
					System.out.println("    \t" + oMoves.get(row));
				} else { // O hasn't made a move
					System.out.println(xMoves.get(row));
				}
			} else { // X has not made a move
				if (row < oMoves.size()) { // O has made a move
					System.out.print(completeRow + "\t" + (row + 1) + ". ");
					System.out.println("    \t\t" + oMoves.get(row));
				} else // no one has made a move yet
					System.out.println(completeRow);
			}
		}

		// when players have made more moves than the number of rows
		// only need to worry about printing out the moves rather than including a row
		// of game board
		if (xMoves.size() > size) { // number of X moves exceed board size
			for (int index = size; index < xMoves.size(); index++) {
				if (oMoves.size() > index) { // number of O moves also exceed board size
					System.out.print("\t\t\t" + (index + 1) + ". " + xMoves.get(index));
					System.out.println("    \t" + oMoves.get(index));
				} else
					System.out.println("\t\t\t" + (index + 1) + ". " + xMoves.get(index));
			}
		} else if (oMoves.size() > size) // number of X moves exceed board size while number of X moves don't
			for (int index = size; index < oMoves.size(); index++)
				System.out.println("\t\t\t" + (index + 1) + ".     \t\t" + oMoves.get(index));
	}
}
