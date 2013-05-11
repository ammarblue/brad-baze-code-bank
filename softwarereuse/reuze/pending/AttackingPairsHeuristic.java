package reuze.pending;
import com.software.reuze.dg_NQueensBoard;
import com.software.reuze.m_i_HeuristicFunction;

/**
 * Estimates the distance to goal by the number of attacking pairs of queens on
 * the board.
 * 
 * @author R. Lunde
 */
public class AttackingPairsHeuristic implements m_i_HeuristicFunction {

	public double h(Object state) {
		dg_NQueensBoard board = (dg_NQueensBoard) state;
		return board.getNumberOfAttackingPairs();
	}
}