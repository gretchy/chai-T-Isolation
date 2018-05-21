import java.util.Scanner;

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
		String player = keyboard.nextLine(); // add check to make sure user input is valid string
		
		if(player.equals("C")) {
			Board board = new Board();
			board.makeMove("X", "B2");
			board.makeMove("O", "G7");
		}
		else {
			
		}
		
		keyboard.close();
	}
	
	public boolean isValidMove(Board board, String player, String move) {
		boolean isValid = true;
		
		
		
		return isValid;
	}
}
