import java.util.ArrayList;

public class Board {
	private String[][] board = new String[8][8];
	private ArrayList<String> xMoves = new ArrayList<String>();
	private ArrayList<String> oMoves = new ArrayList<String>();
	
	public Board() {
		initialize();
		printBoard();
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
