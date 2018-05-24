import java.util.ArrayList;

public class Board {
	private String[][] board = new String[8][8];
	private ArrayList<String> xMoves = new ArrayList<String>();
	private ArrayList<String> oMoves = new ArrayList<String>();
	
	public Board() {
		initialize();
		printBoard();
	}
	
	public Board(Board copyBoard) {
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board.length; col++) {
				board[row][col] = copyBoard.boardValueAt(row, col);
			}
		}
	}
	
	public void initialize() {
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board.length; col++) {
				if(row == 0 && col == 0)
					board[row][col] = "X";
				else if(row == 7 && col == 7)
					board[row][col] = "O";
				else
					board[row][col] = "-";
			}
		}
	}
	
	public void makeMove(String player, String move) {
		int[] playerLoc = getPosition(player);
		
		if(checkMove(player, move)) {
			int newRow;
			if(move.startsWith("A"))
				newRow = 0;
			else if(move.startsWith("B"))
				newRow = 1;
			else if(move.startsWith("C"))
				newRow = 2;
			else if(move.startsWith("D"))
				newRow = 3;
			else if(move.startsWith("E"))
				newRow = 4;
			else if(move.startsWith("F"))
				newRow = 5;
			else if(move.startsWith("G"))
				newRow = 6;
			else
				newRow = 7;
			
			int newCol = Integer.parseInt(move.substring(1)) - 1;
			
			if(player.equals("X")) {
				board[newRow][newCol] = "X";
				xMoves.add(move);
			}
			else {
				board[newRow][newCol] = "O";
				oMoves.add(move);
			}
			
			board[playerLoc[0]][playerLoc[1]] = "#";
			
			printBoard();
		}
		else
			System.out.println("\nInvalid move. " + player + " needs to give another move.");
	}
	
	public String boardValueAt(int row, int col) {
		return board[row][col];
	}
	
	public int[] getPosition(String player) {
		int[] position = new int[2];
		
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board.length; col++) {
				if(board[row][col].equals(player)) {
					position[0] = row;
					position[1] = col;
				}
			}
		}
		
		return position;
	}
	
	public boolean checkMove(String player, String move) {
		
		int newRow;
		if(move.startsWith("A"))
			newRow = 0;
		else if(move.startsWith("B"))
			newRow = 1;
		else if(move.startsWith("C"))
			newRow = 2;
		else if(move.startsWith("D"))
			newRow = 3;
		else if(move.startsWith("E"))
			newRow = 4;
		else if(move.startsWith("F"))
			newRow = 5;
		else if(move.startsWith("G"))
			newRow = 6;
		else if(move.startsWith("H"))
			newRow = 7;
		else
			return false; // row is out of bounds
		
		int newCol = Integer.parseInt(move.substring(1)) - 1;
		if(newCol < 1 || newCol > 8)
			return false; // column is out of bounds
		
		if(getPosition(player)[0] == newRow && getPosition(player)[1] == newCol)
			return false; // player didn't move at all
		
		int vertDistance = Math.abs(getPosition(player)[0] - newRow);
		int horizDistance = Math.abs(getPosition(player)[1] - newCol);
		
		// check cardinal direction - NORTH
		if(newRow < getPosition(player)[0] && newCol == getPosition(player)[1]) {
			// check for obstructions in path
			for(int path = 0; path < vertDistance; path++) {
				int spotInPath = getPosition(player)[0] - path - 1;
				if(!board[spotInPath][newCol].equals("-"))
					return false; // there is an obstacle
			}
			
			return true; // there is no obstacle, player made valid move
		}
		
		// check cardinal direction - EAST
		if(newRow == getPosition(player)[0] && newCol > getPosition(player)[1]) {
			// check for obstructions in path
			for(int path = 0; path < horizDistance; path++) {
				int spotInPath = getPosition(player)[1] + path + 1;
				if(!board[newRow][spotInPath].equals("-"))
					return false; // there is an obstacle
			}
			
			return true; // there is no obstacle, player made valid move
		}
		
		// check cardinal direction - SOUTH
		if(newRow > getPosition(player)[0] && newCol == getPosition(player)[1]) {
			// check for obstructions in path
			for(int path = 0; path < vertDistance; path++) {
				int spotInPath = getPosition(player)[0] + path + 1;
				if(!board[spotInPath][newCol].equals("-"))
					return false; // there is an obstacle
			}
			
			return true; // there is no obstacle, player made valid move
		}
		
		// check cardinal direction - WEST
		if(newRow == getPosition(player)[0] && newCol < getPosition(player)[1]) {
			// check for obstructions in path
			for(int path = 0; path < horizDistance; path++) {
				int spotInPath = getPosition(player)[1] - path - 1;
				if(!board[newRow][spotInPath].equals("-"))
					return false; // there is an obstacle
			}
			
			return true; // there is no obstacle, player made valid move
		}
		
		// check diagonals
		if(vertDistance == horizDistance) {
			
			// NORTHEAST diagonal
			if(newRow < getPosition(player)[0] && newCol > getPosition(player)[1]) {
				// check for obstructions in path
				for(int path = 0; path < horizDistance; path++) {
					int tempRow = getPosition(player)[0] - path - 1;
					int tempCol = getPosition(player)[1] + path + 1;
					if(!board[tempRow][tempCol].equals("-"))
						return false; // there is an obstacle
				}
				
				return true; // there is no obstacle, player made valid move
			}
			
			// SOUTHEAST diagonal
			if(newRow > getPosition(player)[0] && newCol > getPosition(player)[1]) {
				// check for obstructions in path
				for(int path = 0; path < horizDistance; path++) {
					int tempRow = getPosition(player)[0] + path + 1;
					int tempCol = getPosition(player)[1] + path + 1;
					if(!board[tempRow][tempCol].equals("-"))
						return false; // there is an obstacle
				}
				
				return true; // there is no obstacle, player made valid move
			}
			
			// NORTHWEST diagonal
			if(newRow < getPosition(player)[0] && newCol < getPosition(player)[1]) {
				// check for obstructions in path
				for(int path = 0; path < horizDistance; path++) {
					int tempRow = getPosition(player)[0] - path - 1;
					int tempCol = getPosition(player)[1] - path - 1;
					if(!board[tempRow][tempCol].equals("-"))
						return false; // there is an obstacle
				}
				
				return true; // there is no obstacle, player made valid move
			}
			
			// SOUTHWEST diagonal
			if(newRow > getPosition(player)[0] && newCol < getPosition(player)[1]) {
				// check for obstructions in path
				for(int path = 0; path < horizDistance; path++) {
					int tempRow = getPosition(player)[0] + path + 1;
					int tempCol = getPosition(player)[1] - path - 1;
					if(!board[tempRow][tempCol].equals("-"))
						return false; // there is an obstacle
				}
				
				return true; // there is no obstacle, player made valid move
			}
		}
		
		return false; // the move isn't a valid queen move
	}
	
	public void printBoard() {
		String[] rowIndexes = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
		
		System.out.println("\n  1 2 3 4 5 6 7 8");
		for(int row = 0; row < board.length; row++) {
			String completeRow = rowIndexes[row] + " ";
			for(int col = 0; col < board.length; col++) {
				completeRow += board[row][col];
				completeRow += " ";
			}
			System.out.println(completeRow);
		}
	}
}
