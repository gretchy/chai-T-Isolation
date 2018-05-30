import java.awt.Point;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class AlphaBetaPruning {
	static long start_time = 0; // starts timing for computer to make its move
	static long elapsed_time = 0; // stores the amount of time that's already gone by
	static long timeLimit; // stores actual time limit for computer to grab next move
	private int infinity = 9999; // positive infinity for beta
	private int neg_infinity = -9999; // negative infinity for alpha
	static Vector<Board> children = new Vector<Board>(); // stores successors of current board
	
	// empty constructor
	public AlphaBetaPruning() {}
	
	// alpha-beta pruning method, returns the best move for computer to make
	public int alphaBetaSearch(Board root, int depth_limit) {
		int score = maxValue(root, neg_infinity, infinity, depth_limit);
		return score;
	}
	
	// calculates best MAX value
	public int maxValue(Board node, int alpha, int beta, int depth_limit) {
		elapsed_time = (System.nanoTime() - start_time) / 1000000000;
		if (node.getDepth() >= depth_limit || node.getValidMoves().size() == 0 || elapsed_time >= .95 * timeLimit) {
			// best_move = node.findString(node.getTurn());
			return node.evaluate();
		}
		int value = neg_infinity;
		Iterator itr = node.getValidMoves().iterator();
		while (itr.hasNext()) {
			Point move = (Point) itr.next();
			Board child = new Board(node, move, node.getTurn());
			children.add(child);
			value = Math.max(value, minValue(child, alpha, beta, depth_limit));
			child.value = value;
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
		}
		itr = children.iterator();
		while (itr.hasNext()) {
			Board current = (Board) itr.next();

			int[] coord = current.getPosition(node.getTurn());
		}
		return value;
	}

	// calculates best MIN value
	public int minValue(Board node, int alpha, int beta, int depth_limit) {
		elapsed_time = (System.nanoTime() - start_time) / 1000000000;
		if (node.getDepth() >= depth_limit || node.getValidMoves().size() == 0 || elapsed_time > .95 * timeLimit) {
			return node.evaluate();
		}
		int value = infinity;
		Iterator itr = node.getValidMoves().iterator();
		while (itr.hasNext()) {
			Point move = (Point) itr.next();
			Board child = new Board(node, move, node.getTurn());
			value = Math.min(value, minValue(child, alpha, beta, depth_limit));
			child.value = value;
			if (value >= beta)
				return value;
			alpha = Math.min(beta, value);
		}
		return value;
	}
	
	// method returns the best move for computer to make
	// if there are multiple optimal moves, a random one is chosen
	public int[] getBestMove(Board board, int depth, long maxTime) {
		timeLimit = maxTime;
		start_time = System.nanoTime();
		
		children.clear();
		int score = alphaBetaSearch(board, depth);
		int[] bestMove = new int[2];
		
		Iterator itr = children.iterator();
		ArrayList<Board> sameScoreChildren = new ArrayList<Board>();
		
		while (itr.hasNext()) {
			Board child = (Board) itr.next();
			if (child.value == score)
				sameScoreChildren.add(child);
		}
		
		// there are multiple moves that are best options
		if(sameScoreChildren.size() > 1) {
			// randomly select move to make
			Random numGen = new Random();
			bestMove = sameScoreChildren.get(numGen.nextInt(sameScoreChildren.size())).getPosition("X");
		}
		
		return bestMove;
	}

}
