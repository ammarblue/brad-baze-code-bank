package com.software.reuze;

import java.util.LinkedHashSet;
import java.util.Set;

import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_ActionResultFunction;
import com.software.reuze.aa_i_ActionsFunction;


/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class dg_EightPuzzleFunctionFactory {
	private static aa_i_ActionsFunction _actionsFunction = null;
	private static aa_i_ActionResultFunction _resultFunction = null;

	public static aa_i_ActionsFunction getActionsFunction() {
		if (null == _actionsFunction) {
			_actionsFunction = new EPActionsFunction();
		}
		return _actionsFunction;
	}

	public static aa_i_ActionResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new EPResultFunction();
		}
		return _resultFunction;
	}

	private static class EPActionsFunction implements aa_i_ActionsFunction {
		public Set<aa_i_Action> actions(Object state) {
			dg_EightPuzzleBoard board = (dg_EightPuzzleBoard) state;

			Set<aa_i_Action> actions = new LinkedHashSet<aa_i_Action>();

			if (board.canMoveGap(dg_EightPuzzleBoard.UP)) {
				actions.add(dg_EightPuzzleBoard.UP);
			}
			if (board.canMoveGap(dg_EightPuzzleBoard.DOWN)) {
				actions.add(dg_EightPuzzleBoard.DOWN);
			}
			if (board.canMoveGap(dg_EightPuzzleBoard.LEFT)) {
				actions.add(dg_EightPuzzleBoard.LEFT);
			}
			if (board.canMoveGap(dg_EightPuzzleBoard.RIGHT)) {
				actions.add(dg_EightPuzzleBoard.RIGHT);
			}

			return actions;
		}
	}

	private static class EPResultFunction implements aa_i_ActionResultFunction {
		public Object result(Object s, aa_i_Action a) {
			dg_EightPuzzleBoard board = (dg_EightPuzzleBoard) s;

			if (dg_EightPuzzleBoard.UP.equals(a)
					&& board.canMoveGap(dg_EightPuzzleBoard.UP)) {
				dg_EightPuzzleBoard newBoard = new dg_EightPuzzleBoard(board);
				newBoard.moveGapUp();
				return newBoard;
			} else if (dg_EightPuzzleBoard.DOWN.equals(a)
					&& board.canMoveGap(dg_EightPuzzleBoard.DOWN)) {
				dg_EightPuzzleBoard newBoard = new dg_EightPuzzleBoard(board);
				newBoard.moveGapDown();
				return newBoard;
			} else if (dg_EightPuzzleBoard.LEFT.equals(a)
					&& board.canMoveGap(dg_EightPuzzleBoard.LEFT)) {
				dg_EightPuzzleBoard newBoard = new dg_EightPuzzleBoard(board);
				newBoard.moveGapLeft();
				return newBoard;
			} else if (dg_EightPuzzleBoard.RIGHT.equals(a)
					&& board.canMoveGap(dg_EightPuzzleBoard.RIGHT)) {
				dg_EightPuzzleBoard newBoard = new dg_EightPuzzleBoard(board);
				newBoard.moveGapRight();
				return newBoard;
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}