package reuze.pending;
import java.util.List;

import com.software.reuze.dg_i_Game;
import com.software.reuze.ga_XYLocation;


/**
 * Provides an implementation of the Tic-tac-toe game which can be used for
 * experiments with the Minimax algorithm.
 * 
 * @author Ruediger Lunde
 * 
 */
public class TicTacToeGame implements dg_i_Game<TicTacToeState, ga_XYLocation, String> {

	TicTacToeState initialState = new TicTacToeState();

	public TicTacToeState getInitialState() {
		return initialState;
	}

	public String[] getPlayers() {
		return new String[] { TicTacToeState.X, TicTacToeState.O };
	}

	public String getPlayer(TicTacToeState state) {
		return state.getPlayerToMove();
	}

	public List<ga_XYLocation> getActions(TicTacToeState state) {
		return state.getUnMarkedPositions();
	}

	public TicTacToeState getResult(TicTacToeState state, ga_XYLocation action) {
		TicTacToeState result = state.clone();
		result.mark(action);
		return result;
	}

	public boolean isTerminal(TicTacToeState state) {
		return state.getUtility() != -1;
	}

	public double getUtility(TicTacToeState state, String player) {
		double result = state.getUtility();
		if (result != -1) {
			if (player == TicTacToeState.O)
				result = 1 - result;
		} else {
			throw new IllegalArgumentException("State is not terminal.");
		}
		return result;
	}
}
