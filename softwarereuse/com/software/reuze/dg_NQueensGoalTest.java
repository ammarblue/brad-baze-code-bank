package com.software.reuze;

/**
 * @author R. Lunde
 */
public class dg_NQueensGoalTest implements a_i_GoalTest {

	public boolean isGoalState(Object state) {
		dg_NQueensBoard board = (dg_NQueensBoard) state;
		return board.getNumberOfQueensOnBoard() == board.getSize()
				&& board.getNumberOfAttackingPairs() == 0;
	}
}