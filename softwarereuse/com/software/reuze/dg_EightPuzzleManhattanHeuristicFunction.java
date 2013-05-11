package com.software.reuze;

import com.software.reuze.ga_XYLocation;
import com.software.reuze.m_i_HeuristicFunction;

/**
 * @author Ravi Mohan
 * 
 */
public class dg_EightPuzzleManhattanHeuristicFunction implements m_i_HeuristicFunction {

	public double h(Object state) {
		dg_EightPuzzleBoard board = (dg_EightPuzzleBoard) state;
		int retVal = 0;
		for (int i = 1; i < 9; i++) {
			ga_XYLocation loc = board.getLocationOf(i);
			retVal += evaluateManhattanDistanceOf(i, loc);
		}
		return retVal;

	}

	public int evaluateManhattanDistanceOf(int i, ga_XYLocation loc) {
		int retVal = -1;
		int xpos = loc.getXCoOrdinate();
		int ypos = loc.getYCoOrdinate();
		switch (i) {

		case 1:
			retVal = Math.abs(xpos - 0) + Math.abs(ypos - 1);
			break;
		case 2:
			retVal = Math.abs(xpos - 0) + Math.abs(ypos - 2);
			break;
		case 3:
			retVal = Math.abs(xpos - 1) + Math.abs(ypos - 0);
			break;
		case 4:
			retVal = Math.abs(xpos - 1) + Math.abs(ypos - 1);
			break;
		case 5:
			retVal = Math.abs(xpos - 1) + Math.abs(ypos - 2);
			break;
		case 6:
			retVal = Math.abs(xpos - 2) + Math.abs(ypos - 0);
			break;
		case 7:
			retVal = Math.abs(xpos - 2) + Math.abs(ypos - 1);
			break;
		case 8:
			retVal = Math.abs(xpos - 2) + Math.abs(ypos - 2);
			break;

		}
		return retVal;
	}
}