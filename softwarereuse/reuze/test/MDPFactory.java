package reuze.test;
//package aima.core.probability.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.software.reuze.a_CellWorld;
import com.software.reuze.a_CellWorldAction;
import com.software.reuze.aa_i_ActionsFunction;
import com.software.reuze.ga_CellPositionAndContent;
import com.software.reuze.mp_MDP;
import com.software.reuze.mp_i_MDP;
import com.software.reuze.mp_i_MDPRewardFunction;
import com.software.reuze.mp_i_MDPTransitionProbabilityFunction;


/*import aima.core.environment.cellworld.CellContent;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.probability.mdp.ActionsFunction;
import aima.core.probability.mdp.MarkovDecisionProcess;
import aima.core.probability.mdp.RewardFunction;
import aima.core.probability.mdp.TransitionProbabilityFunction;
import aima.core.probability.mdp.impl.MDP;*/

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class MDPFactory {

	/**
	 * Constructs an MDP that can be used to generate the utility values
	 * detailed in Fig 17.3.
	 * 
	 * @param cw
	 *            the cell world from figure 17.1.
	 * @return an MDP that can be used to generate the utility values detailed
	 *         in Fig 17.3.
	 */
	public static mp_i_MDP<ga_CellPositionAndContent<Double>, a_CellWorldAction> createMDPForFigure17_3(
			final a_CellWorld<Double> cw) {

		return new mp_MDP<ga_CellPositionAndContent<Double>, a_CellWorldAction>(cw.getCells(),
				cw.getCellAt(1, 1), createActionsFunctionForFigure17_1(cw),
				createTransitionProbabilityFunctionForFigure17_1(cw),
				createRewardFunctionForFigure17_1());
	}

	/**
	 * Returns the allowed actions from a specified cell within the cell world
	 * described in Fig 17.1.
	 * 
	 * @param cw
	 *            the cell world from figure 17.1.
	 * @return the set of actions allowed at a particular cell. This set will be
	 *         empty if at a terminal state.
	 */
	public static aa_i_ActionsFunction<ga_CellPositionAndContent<Double>, a_CellWorldAction> createActionsFunctionForFigure17_1(
			final a_CellWorld<Double> cw) {
		final Set<ga_CellPositionAndContent<Double>> terminals = new HashSet<ga_CellPositionAndContent<Double>>();
		terminals.add(cw.getCellAt(4, 3));
		terminals.add(cw.getCellAt(4, 2));

		aa_i_ActionsFunction<ga_CellPositionAndContent<Double>, a_CellWorldAction> af = new aa_i_ActionsFunction<ga_CellPositionAndContent<Double>, a_CellWorldAction>() {

			public Set<a_CellWorldAction> actions(ga_CellPositionAndContent<Double> s) {
				// All actions can be performed in each cell
				// (except terminal states)
				if (terminals.contains(s)) {
					return Collections.emptySet();
				}
				return a_CellWorldAction.actions();
			}
		};
		return af;
	}

	/**
	 * Figure 17.1 (b) Illustration of the transition model of the environment:
	 * the 'intended' outcome occurs with probability 0.8, but with probability
	 * 0.2 the agent moves at right angles to the intended direction. A
	 * collision with a wall results in no movement.
	 * 
	 * @param cw
	 *            the cell world from figure 17.1.
	 * @return the transition probability function as described in figure 17.1.
	 */
	public static mp_i_MDPTransitionProbabilityFunction<ga_CellPositionAndContent<Double>, a_CellWorldAction> createTransitionProbabilityFunctionForFigure17_1(
			final a_CellWorld<Double> cw) {
		mp_i_MDPTransitionProbabilityFunction<ga_CellPositionAndContent<Double>, a_CellWorldAction> tf = new mp_i_MDPTransitionProbabilityFunction<ga_CellPositionAndContent<Double>, a_CellWorldAction>() {
			private double[] distribution = new double[] { 0.8, 0.1, 0.1 };

			public double probability(ga_CellPositionAndContent<Double> sDelta, ga_CellPositionAndContent<Double> s,
					a_CellWorldAction a) {
				double prob = 0;

				List<ga_CellPositionAndContent<Double>> outcomes = possibleOutcomes(s, a);
				for (int i = 0; i < outcomes.size(); i++) {
					if (sDelta.equals(outcomes.get(i))) {
						// Note: You have to sum the matches to
						// sDelta as the different actions
						// could have the same effect (i.e.
						// staying in place due to there being
						// no adjacent cells), which increases
						// the probability of the transition for
						// that state.
						prob += distribution[i];
					}
				}

				return prob;
			}
			
			private List<ga_CellPositionAndContent<Double>> possibleOutcomes(ga_CellPositionAndContent<Double> c,
					a_CellWorldAction a) {
				// There can be three possible outcomes for the planned action
				List<ga_CellPositionAndContent<Double>> outcomes = new ArrayList<ga_CellPositionAndContent<Double>>();

				outcomes.add(cw.result(c, a));
				outcomes.add(cw.result(c, a.getFirstRightAngledAction()));
				outcomes.add(cw.result(c, a.getSecondRightAngledAction()));

				return outcomes;
			}
		};

		return tf;
	}

	/**
	 * 
	 * @return the reward function which takes the content of the cell as being
	 *         the reward value.
	 */
	public static mp_i_MDPRewardFunction<ga_CellPositionAndContent<Double>> createRewardFunctionForFigure17_1() {
		mp_i_MDPRewardFunction<ga_CellPositionAndContent<Double>> rf = new mp_i_MDPRewardFunction<ga_CellPositionAndContent<Double>>() {
			public double reward(ga_CellPositionAndContent<Double> s) {
				return s.getContent();
			}
		};
		return rf;
	}
}
