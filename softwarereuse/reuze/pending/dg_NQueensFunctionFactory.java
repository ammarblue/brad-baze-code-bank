package reuze.pending;
import java.util.LinkedHashSet;
import java.util.Set;

import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_ActionResultFunction;
import com.software.reuze.aa_i_ActionsFunction;
import com.software.reuze.dg_NQueensBoard;
import com.software.reuze.ga_XYLocation;


/**
 * Provides useful functions for two versions of the n-queens problem. The
 * incremental formulation and the complete-state formulation share the same
 * RESULT function but use different ACTIONS functions.
 * 
 * @author Ciaran O'Reilly
 * @author R. Lunde
 */
public class dg_NQueensFunctionFactory {
	private static aa_i_ActionsFunction _iActionsFunction = null;
	private static aa_i_ActionsFunction _cActionsFunction = null;
	private static aa_i_ActionResultFunction _resultFunction = null;

	/**
	 * Returns an ACTIONS function for the incremental formulation of the
	 * n-queens problem.
	 */
	public static aa_i_ActionsFunction getIActionsFunction() {
		if (null == _iActionsFunction) {
			_iActionsFunction = new NQIActionsFunction();
		}
		return _iActionsFunction;
	}

	/**
	 * Returns an ACTIONS function for the complete-state formulation of the
	 * n-queens problem.
	 */
	public static aa_i_ActionsFunction getCActionsFunction() {
		if (null == _cActionsFunction) {
			_cActionsFunction = new NQCActionsFunction();
		}
		return _cActionsFunction;
	}

	/**
	 * Returns a RESULT function for the n-queens problem.
	 */
	public static aa_i_ActionResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new NQResultFunction();
		}
		return _resultFunction;
	}

	/**
	 * Assumes that queens are placed column by column, starting with an empty
	 * board, and provides queen placing actions for all non-attacked positions
	 * of the first free column.
	 * 
	 * @author R. Lunde
	 */
	private static class NQIActionsFunction implements aa_i_ActionsFunction {
		public Set<aa_i_Action> actions(Object state) {
			dg_NQueensBoard board = (dg_NQueensBoard) state;

			Set<aa_i_Action> actions = new LinkedHashSet<aa_i_Action>();

			int numQueens = board.getNumberOfQueensOnBoard();
			int boardSize = board.getSize();
			for (int i = 0; i < boardSize; i++) {
				ga_XYLocation newLocation = new ga_XYLocation(numQueens, i);
				if (!(board.isSquareUnderAttack(newLocation))) {
					actions.add(new QueenAction(QueenAction.PLACE_QUEEN,
							newLocation));
				}
			}

			return actions;
		}
	}

	/**
	 * Assumes exactly one queen in each column and provides all possible queen
	 * movements in vertical direction as actions.
	 * 
	 * @author R. Lunde
	 */
	private static class NQCActionsFunction implements aa_i_ActionsFunction {

		public Set<aa_i_Action> actions(Object state) {
			Set<aa_i_Action> actions = new LinkedHashSet<aa_i_Action>();
			dg_NQueensBoard board = (dg_NQueensBoard) state;
			for (int i = 0; i < board.getSize(); i++)
				for (int j = 0; j < board.getSize(); j++) {
					ga_XYLocation loc = new ga_XYLocation(i, j);
					if (!board.queenExistsAt(loc))
						actions.add(new QueenAction(QueenAction.MOVE_QUEEN, loc));
				}
			return actions;
		}
	}

	/** Supports queen placing, queen removal, and queen movement actions. */
	private static class NQResultFunction implements aa_i_ActionResultFunction {
		public Object result(Object s, aa_i_Action a) {
			if (a instanceof QueenAction) {
				QueenAction qa = (QueenAction) a;
				dg_NQueensBoard board = (dg_NQueensBoard) s;
				dg_NQueensBoard newBoard = new dg_NQueensBoard(board.getSize());
				newBoard.setBoard(board.getQueenPositions());
				if (qa.getName() == QueenAction.PLACE_QUEEN)
					newBoard.addQueenAt(qa.getLocation());
				else if (qa.getName() == QueenAction.REMOVE_QUEEN)
					newBoard.removeQueenFrom(qa.getLocation());
				else if (qa.getName() == QueenAction.MOVE_QUEEN)
					newBoard.moveQueenTo(qa.getLocation());
				s = newBoard;
			}
			// if action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}