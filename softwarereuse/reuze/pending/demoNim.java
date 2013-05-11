package reuze.pending;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.software.reuze.dasg_SearchAlphaBeta;
import com.software.reuze.dasg_i_SearchAdversarial;
import com.software.reuze.dg_i_Game;

/* Craig Porter for AI @ georgiasouthern.edu 2012
 * In Nim, take turns removing sticks from different piles
 * loser is the one that removes the last stick.
 */
//TODO many games have states that are repeated during AB evaluation, develop a caching strategy
//TODO change implementation to learn and remember from game to game
public class demoNim {
	NimGame game;
	NimState currentState;
	//d_Metrics searchMetrics;
	
	public demoNim() {
		game = new NimGame();
		currentState = game.getInitialState();
	}
	
	public void start() {
		Scanner scanner;		
		int pile;
		int removalAmount;
		
		while (!game.isTerminal(currentState)) {
			System.out.println("Board:");
			System.out.println(currentState.toString());
			System.out.println(currentState.getPlayerToMove() + "'s move.");
			if (currentState.getPlayerToMove().equals(NimState.PLAYER1)) {
				System.out.println("Enter an integer for the stack to remove from and for the number to remove. (Ex. '2 4' Remove 4 from pile 2)");
				scanner = new Scanner(System.in);
				pile = scanner.nextInt();
				if (pile < 1 || pile > currentState.board.length) {
					System.out.println("pile must be 1,2,3,"+currentState.board.length);
					continue;
				}
				removalAmount = scanner.nextInt();
				if (removalAmount<=0) {
					System.out.println("amount must be positive.");
					continue;
				}
				if (removalAmount > currentState.board[pile-1] ) {
					System.out.println("amount exceeds sticks.");
					continue;
				}
				NimAction action = new NimAction(pile - 1, removalAmount);
				currentState = game.getResult(currentState, action);
					
			}
			else if (currentState.getPlayerToMove().equals(NimState.PLAYER2)) {
				//searchMetrics = null; // reset search metrics
				proposeMove();
			}
		}
		if (game.getUtility(currentState, NimState.PLAYER1) == 1)
			System.out.println(NimState.PLAYER1 + " has won. :-)");
		else if (game.getUtility(currentState, NimState.PLAYER2) == 1)
			System.out.println(NimState.PLAYER2 + " has won. :-(");
		else
			System.out.println("Something went wrong. I guess there is no winner. O.o");
	}
	
	public static void main(String[] args) {
		demoNim prog = new demoNim();
		prog.start();
	}
	
	/* Uses adversarial search for selecting the next action. */
	private void proposeMove() {
		dasg_i_SearchAdversarial<NimState, NimAction> search;
		search = dasg_SearchAlphaBeta.createFor(game);

		NimAction action = search.makeDecision(currentState);
		//searchMetrics = search.getMetrics();
		currentState = game.getResult(currentState, action);
	}
	public class NimState implements Cloneable, Iterable<NimAction> {
		public final static String PLAYER1 = "Human";
		public final static String PLAYER2 = "Computer";
		
		private int[] board = new int[] {3,5,7,1,2}; // For now, it is a static board
		
		private String playerToMove = PLAYER1;
		private double utility = -1; // 0: win for P1, 1: win for P2
		
		public String getPlayerToMove() {
			return playerToMove;
		}
		
		public boolean isEmpty(int col, int pile) {
			return board[pile] == 0;
		}
		
		public int getValue(int col, int pile) {
			return board[pile];
		}
		
		public double getUtility() {
			return utility;
		}
		
		public void takeTurn(NimAction action) {
			if (utility == -1 && getValue(0, action._pile) > 0) {
				board[action._pile] -= action._removalAmount;
				analyzeUtility();
				playerToMove = (playerToMove == PLAYER1 ? PLAYER2 : PLAYER1);
			}
		}
		
		private void analyzeUtility() {
			if (gameOver())
				utility = (playerToMove == PLAYER1 ? 0 : 1); // If the current player
		}
		
		private boolean gameOver() {
			for (int pile = 0; pile < board.length; pile++)
				if (board[pile] > 0) return false;
			return true;
		}
		
		@Override
		public NimState clone() {
			NimState copy = null;
			try {
				copy = (NimState) super.clone();
				copy.board = Arrays.copyOf(board, board.length);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace(); // should never happen...
			}
			return copy;
		}

		@Override
		public boolean equals(Object anObj) {
			if (anObj != null && anObj.getClass() == getClass()) {
				NimState anotherState = (NimState) anObj;
				for (int i = 0; i < board.length; i++)
					if (board[i] != anotherState.board[i])
						return false;
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return Arrays.toString(board);
		}
		public Iterator<NimAction> iterator() {
			return new internal();
		}
		public class internal implements Iterator<NimAction> {
			int pile;
			int removalPossibility;
			public internal() {
				pile=-1;
				removalPossibility = 0;
			}
			public boolean hasNext() {
				if (removalPossibility > 1) {
					removalPossibility--;
					return true;
				}
				while (++pile<board.length) {
					if (board[pile]==0) continue;
					removalPossibility = board[pile];
					return true;
				}
				return false;
			}
			public NimAction next() {
				return new NimAction(pile, removalPossibility);
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
	}
///////////////////////////////////////////////////////////////////////////////////////
	public class NimGame implements dg_i_Game<NimState, NimAction, String> {

		NimState initialState = new NimState();
		
		public NimState getInitialState() {
			return initialState;
		}

		public String[] getPlayers() {
			return new String[] {NimState.PLAYER1, NimState.PLAYER2};
		}

		public String getPlayer(NimState state) {
			return state.getPlayerToMove();
		}
		//depending on game, this may return List<NimAction> or an online iterator
		public List<NimAction> getActions(NimState state) {
			List<NimAction> l=new ArrayList<NimAction>();
			for (NimAction a:state) l.add(a);
			return l;
		}

		public NimState getResult(NimState state, NimAction action) {
			NimState result = state.clone();
			result.takeTurn(action);
			return result;
		}

		public boolean isTerminal(NimState state) {
			return state.getUtility() != -1;
		}

		public double getUtility(NimState state, String player) {
			double result = state.getUtility();
			if (result != -1) {
				if (player == NimState.PLAYER2)
					result = 1 - result;
			} else {
				throw new IllegalArgumentException("State is not terminal.");
			}
			return result;
		}
	}
///////////////////////////////////////////////////////////////////////
	public final static class NimAction {
		public int _pile;
		public int _removalAmount;
		
		public NimAction(int location, int removalAmount) {
			_pile = location;
			_removalAmount = removalAmount;
		}
		@Override
		public String toString() {
			return _pile+"/"+_removalAmount;
		}
	}
}
