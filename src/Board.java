
public class Board {
	private String[][] board = new String[8][8];
	
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
	
	public void printBoard() {
		String[] rowIndexes = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
		
		System.out.println("  1 2 3 4 5 6 7 8");
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
