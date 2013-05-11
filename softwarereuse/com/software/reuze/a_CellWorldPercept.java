package com.software.reuze;

//package aima.core.learning.reinforcement.example;

//import aima.core.environment.cellworld.Cell;
//import aima.core.learning.reinforcement.PerceptStateReward;

/**
 * An implementation of the PerceptStateReward interface for the cell world
 * environment. Note: The getCell() and setCell() methods allow a single percept
 * to be instantiated per agent within the environment. However, if an agent
 * tracks its perceived percepts it will need to explicitly copy the relevant
 * information.
 * 
 * @author oreilly
 * 
 */
public class a_CellWorldPercept implements aa_i_PerceptRewardFromState<ga_CellPositionAndContent<Double>> {
	private ga_CellPositionAndContent<Double> cell = null;

	/**
	 * Constructor.
	 * 
	 * @param cell
	 *            the cell within the environment that the percept refers to.
	 */
	public a_CellWorldPercept(ga_CellPositionAndContent<Double> cell) {
		this.cell = cell;
	}

	/**
	 * 
	 * @return the cell within the environment that the percept refers to.
	 */
	public ga_CellPositionAndContent<Double> getCell() {
		return cell;
	}

	/**
	 * Set the cell within the environment that the percept refers to.
	 * 
	 * @param cell
	 *            the cell within the environment that the percept refers to.
	 */
	public void setCell(ga_CellPositionAndContent<Double> cell) {
		this.cell = cell;
	}

	//
	// START-PerceptStateReward

	public double reward() {
		return cell.getContent().doubleValue();
	}

	public ga_CellPositionAndContent<Double> state() {
		return cell;
	}

	// END-PerceptStateReward
	//
}