import java.awt.Point;
import java.util.Vector;
import java.lang.Math;
import java.util.ArrayList;

public class Board implements Comparable {
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
	public Board(Board parent, Point moveTo, String player) {
		this.state = new String[size][size];

		// copy over board state from parent/previous state of the game
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++)
				this.state[row][col] = parent.state[row][col];
		}

		// stores the old coordinates of player
		int[] old_coordinates = this.getPosition(player);

		this.updateState((int) moveTo.getX(), (int) moveTo.getY(), player);
		this.updateState(old_coordinates[0], old_coordinates[1], "#"); // mark old location with visited symbol

		if (player.equals("X"))
			this.turn = "O"; // switching turn to O player
		else
			this.turn = "X"; // switching turn to X player

		this.depth = parent.depth + 1;
		this.validMoves = this.updateValidMoves();
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
	public void updateState(String[][] otherState) {
		this.state = otherState;
	}

	// setter to update the state with parameters: int, int, and String
	public void updateState(int row, int col, String s) {
		this.state[row][col] = s;
	}

	// setter for the player turn
	public void setTurn(String t) {
		this.turn = t;
	}
	
	// clears all the validMoves belonging to the last calculations
	public void clearValidMoves() {
		this.validMoves = new Vector<Point>();
	}

	// establishes all the valid moves for current player
	public Vector<Point> updateValidMoves() {
		int limit; // to be used in diagonals
		Vector<Point> possibleMoves = new Vector<Point>(); // stores all the possible valid moves
		int[] coordinates = getPosition(this.turn); // stores current player's coordinates

		// cardinal direction - NORTH
		for (int index = coordinates[0] - 1; index >= 0; index--) {
			if (this.state[index][coordinates[1]].equals("-"))
				possibleMoves.add(new Point(index, coordinates[1]));
			else
				break;
		}
		// cardinal direction - EAST
		for (int index = coordinates[1] + 1; index < size; index++) {
			if (this.state[coordinates[0]][index].equals("-"))
				possibleMoves.add(new Point(coordinates[0], index));
			else
				break;
		}
		// cardinal direction - SOUTH
		for (int index = coordinates[0] + 1; index < size; index++) {
			if (this.state[index][coordinates[1]].equals("-"))
				possibleMoves.add(new Point(index, coordinates[1]));
			else
				break;
		}
		// cardinal direction - WEST
		for (int index = coordinates[1] - 1; index >= 0; index--) {
			if (this.state[coordinates[0]][index].equals("-"))
				possibleMoves.add(new Point(coordinates[0], index));
			else
				break;
		}
		// NORTHEAST diagonal
		if (coordinates[0] > size - coordinates[1] - 1)
			limit = size - coordinates[1] - 1;
		else
			limit = coordinates[0];
		for (int index = 1; index <= limit; index++) {
			if (this.state[coordinates[0] - index][coordinates[1] + index].equals("-")) {
				possibleMoves.add(new Point((coordinates[0] - index), (coordinates[1] + index)));
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
				possibleMoves.add(new Point((coordinates[0] + index), (coordinates[1] + index)));
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
				possibleMoves.add(new Point((coordinates[0] + index), (coordinates[1] - index)));
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
				possibleMoves.add(new Point((coordinates[0] - index), (coordinates[1] - index)));
			} else
				break;
		}

		return possibleMoves;
	}

	// calculates number of available moves that opponent has
	public int opponentValidMoves(String opponent) {
		int limit; // to be used in diagonals
		Vector<Point> oppValidMoves = new Vector<Point>(); // stores all valid moves for opponent
		int[] coordinates = getPosition(opponent); // stores opponent's current location
		
		// cardinal direction - NORTH
				for (int index = coordinates[0] - 1; index >= 0; index--) {
					if (this.state[index][coordinates[1]].equals("-"))
						oppValidMoves.add(new Point(index, coordinates[1]));
					else
						break;
				}
				// cardinal direction - EAST
				for (int index = coordinates[1] + 1; index < size; index++) {
					if (this.state[coordinates[0]][index].equals("-"))
						oppValidMoves.add(new Point(coordinates[0], index));
					else
						break;
				}
				// cardinal direction - SOUTH
				for (int index = coordinates[0] + 1; index < size; index++) {
					if (this.state[index][coordinates[1]].equals("-"))
						oppValidMoves.add(new Point(index, coordinates[1]));
					else
						break;
				}
				// cardinal direction - WEST
				for (int index = coordinates[1] - 1; index >= 0; index--) {
					if (this.state[coordinates[0]][index].equals("-"))
						oppValidMoves.add(new Point(coordinates[0], index));
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
						oppValidMoves.add(new Point((coordinates[0] - index), (coordinates[1] - index)));
					}
					else
						break;
				}
				// NORTHEAST diagonal
				if (coordinates[0] > size - coordinates[1] - 1)
					limit = size - coordinates[1] - 1;
				else
					limit = coordinates[0];
				for (int index = 1; index <= limit; index++) {
					if (this.state[coordinates[0] - index][coordinates[1] + index].equals("-")) {
						oppValidMoves.add(new Point((coordinates[0] - index), (coordinates[1] + index)));
					}
					else
						break;
				}

				// SOUTHWEST diagonal
				if (size - coordinates[0] - 1 < coordinates[1])
					limit = size - coordinates[0] - 1;
				else
					limit = coordinates[1];
				for (int index = 1; index <= limit; index++) {
					if (this.state[coordinates[0] + index][coordinates[1] - index].equals("-")) {
						oppValidMoves.add(new Point((coordinates[0] + index), (coordinates[1] - index)));
					}
					else
						break;
				}
				// SOUTHEAST diagonal
				if (size - coordinates[0] - 1 < size - coordinates[1] - 1)
					limit = size - coordinates[0] - 1;
				else
					limit = size - coordinates[1] - 1;
				for (int index = 1; index <= limit; index++) {
					if (this.state[coordinates[0] + index][coordinates[1] + index].equals("-")) {
						oppValidMoves.add(new Point((coordinates[0] + index), (coordinates[1] + index)));
					}
					else
						break;
				}
		return oppValidMoves.size();
	}

	// evaluation of utility function
	public int evaluate() {
		// all the multipliers to be used
		int X = 3;
		int Y = 1;
		int Z = 3;

		String otherPlayer = "X";
		if (this.turn.equals("X"))
			otherPlayer = "O";

		int thisRow = this.getPosition(this.turn)[0];
		int otherRow = this.getPosition(otherPlayer)[0];
		int thisCol = this.getPosition(this.turn)[1];
		int otherCol = this.getPosition(otherPlayer)[1];
		
		// start with X times the number of valid moves
		int score = X * this.validMoves.size();
		// subtract Y times the number of opponent's valid moves
		score -= Y * this.opponentValidMoves(otherPlayer);
		
		// subtract 10 for each wall it's next to
		if (thisRow == 0 || thisRow == size - 1)
			score -= 10;
		if (thisCol == 0 || thisCol == size - 1)
			score -= 10;
		
		for (int row = -1; row <= 1; row++) {
			for (int col = -1; col < 1; col++) {
				if (row + thisRow >= 0 && row + thisRow <= size - 1 && col + thisCol >= 0 && col + thisCol <= size - 1) {
					if (!this.state[thisRow + row][thisCol + col].equals("-"))
						score -= Z; // subtract Z for each surrounding cell that is filled
				}
				if (row + otherRow >= 0 && row + otherRow <= size - 1 && col + otherCol >= 0 && col + otherCol <= size - 1) {
					if (!this.state[otherRow + row][otherCol + col].equals("-"))
						score += Z; // add Z for each cell surrounding the opponent that is filled
				}

			}
		}

		this.value = score;
		return this.value;
	}

	// check if the move is valid for a player to make
	public boolean checkMove(String player, int newRow, int newCol) {
		if (newRow < 0 || newRow > 7)
			return false; // row is out of bounds
		if (newCol < 0 || newCol > 7)
			return false; // column is out of bounds

		if (getPosition(player)[0] == newRow && getPosition(player)[1] == newCol)
			return false; // player didn't move at all

		int vertDistance = Math.abs(getPosition(player)[0] - newRow); // distance between current row and new row of move
		int horizDistance = Math.abs(getPosition(player)[1] - newCol); // distance between current column and new col of move

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
		for (int row = -1; row < 2; row++) {
			for (int col = -1; col < 2; col++) {
				if (row != 0 || col != 0) {
					// check if row value would be out of bounds
					if (getPosition(player)[0] + row >= 0 && getPosition(player)[0] + row < 8) {
						// check if column value would be out of bounds
						if (getPosition(player)[1] + col >= 0 && getPosition(player)[1] + col < 8) {
							if (state[getPosition(player)[0] + row][getPosition(player)[1] + col].equals("-"))
								return false; // there is still an empty space for player to move to
						}
					}
				}
			}
		}

		return true; // player has been isolated by opponent
	}
	
	// implementing Comparable's compareTo() method to work with comparing Boards
	public int compareTo(Object other) {
		String[][] thisState = this.getState();
		String[][] thatState = ((Board) other).getState();
		int length = this.getState().length;
		for (int row = 0; row < length; row++) {
			for (int col = 0; col < length; col++) {
				if (!thisState[row][col].equals(thatState[row][col]))
					return 1;
			}
		}
		return 0;
	}

	// overrides equals() method
	public boolean equals(Object other) {
		if (this.compareTo(other) == 0)
			return true;
		else
			return false;
	}

	// prints out the current game board settings along with the past moves made by players
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
				}
				else { // O hasn't made a move
					System.out.println(xMoves.get(row));
				}
			}
			else { // X has not made a move
				if (row < oMoves.size()) { // O has made a move
					System.out.print(completeRow + "\t" + (row + 1) + ". ");
					System.out.println("    \t\t" + oMoves.get(row));
				}
				else // no one has made a move yet
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
				}
				else
					System.out.println("\t\t\t" + (index + 1) + ". " + xMoves.get(index));
			}
		}
		else if (oMoves.size() > size) // number of X moves exceed board size while number of X moves don't
			for (int index = size; index < oMoves.size(); index++)
				System.out.println("\t\t\t" + (index + 1) + ".     \t\t" + oMoves.get(index));
	}
}
