import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

public class IsolationMain {
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("********************************");
		System.out.println("          ISOLATION");
		System.out.println(" Gretchen Lai - Nandita Chauhan");
		System.out.println("      CS 420 - Spring 2018");
		System.out.println("********************************");
		
		//System.out.print("\nTime Limit per move: ");
		//int timeLimit = keyboard.nextInt(); // add check to make sure user input is a valid number
		System.out.print("First player (C or O)? ");
		String player = keyboard.nextLine().toUpperCase();
		
		// check to make sure user input is valid string
		while(!player.equals("C") && !player.equals("O")) {
			System.out.println("Invalid option given...");
			System.out.print("First player (C or O)? ");
			player = keyboard.nextLine().toUpperCase();
		}
		
		if(player.equals("C")) {
			Board board = new Board();
			
			//board.makeMove("O", "G7");
			//board.makeMove("X", "B2");
			
			while(!board.isIsolated("X") && !board.isIsolated("O")) {
				String compMove = "F6";
				board.makeMove("X", compMove);
				board.printBoard();
				System.out.println("\nComputer's move is: " + compMove);
				System.out.print("Enter opponent's move: ");
				String oppMove = keyboard.nextLine();
				
				// check if opponent entered a valid move
				while(!board.checkMove("O", oppMove)) {
					System.out.println("Invalid move.");
					System.out.print("Enter opponent's move: ");
					oppMove = keyboard.nextLine();
				}
				
				board.makeMove("O", oppMove);
			}
		}
		else {
			Board board = new Board();
		}
		
		keyboard.close();
	}

	
	public boolean isValidMove(Board board, String player, String move) {
		boolean isValid = true;
		
		
		
		return isValid;
	}
	
	
	
	static int board_index = 1; // how the user refers to to the first row/column
    static int infinity = 9999;
    static int neg_infinity = -9999; // to be used in alpha-beta
    
	public static int alphaBetaSearch(Board root, int depth_limit){
        int score = maxValue(root, neg_infinity, infinity, depth_limit);
        return score;
    }
	public static int maxValue(Board node, int alpha, int beta, int depth_limit){
        if (node.getDepth() >= depth_limit || node.getValidMoves().size() == 0){
            return node.evaluate();
        }
        int value = neg_infinity;
        Iterator itr = node.getValidMoves().iterator();
        Vector<Board> children = new Vector<Board>(); 
        while(itr.hasNext()){
            Point move = (Point)itr.next();
            Board child = new Board();
            children.add(child);
            value = Math.max(value, minValue(child, alpha, beta, depth_limit));
            child.value = value;
            if (value >= beta){
                return value;
            }
            alpha = Math.max(alpha, value);
        }
        itr = children.iterator();
        while(itr.hasNext()){
            Board current = (Board)itr.next();

            int[] coord = current.findChar(node.getTurn());
        }
        return value;
	}


	private static int minValue(Board child, int alpha, int beta, int depth_limit) {
		// TODO Auto-generated method stub
		return 0;
	}
}
