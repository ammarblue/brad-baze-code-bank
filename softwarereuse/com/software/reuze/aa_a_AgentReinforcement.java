package com.software.reuze;
//package aima.core.learning.reinforcement.agent;

/*import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.learning.reinforcement.PerceptStateReward;*/
import java.util.Map;


/**
 * An abstract base class for creating reinforcement based agents.
 *
 * @param <S> the state type.
 * @param <A> the action type.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public abstract class aa_a_AgentReinforcement<S, A extends aa_i_Action> extends aa_a_Agent {

    /**
     * Default Constructor.
     */
    public aa_a_AgentReinforcement() {
    }

    /**
     * Map the given percept to an Agent action.
     *
     * @param percept a percept indicating the current state s' and reward
     * signal r'
     * @return the action to take.
     */
    public abstract A execute(aa_i_PerceptRewardFromState<S> percept);

    /**
     * Get a vector of the currently calculated utilities for states of type S
     * in the world.
     *
     * @return a Map of the currently learned utility values for the states in
     * the environment (Note: this map may not contain all of the states in the
     * environment, i.e. the agent has not seen them yet).
     */
    public abstract Map<S, Double> getUtility();

    /**
     * Reset the agent back to its initial state before it has learned anything
     * about its environment.
     */
    public abstract void reset();

    /**
     * Return the action to execute given the current percept
     *
     * @param p the current percept
     * @return the action to execute
     */
    @SuppressWarnings("unchecked")
    @Override
    public aa_i_Action execute(aa_i_Percept p) {
        if (p instanceof aa_i_PerceptRewardFromState<?>) {
            aa_i_Action a = execute((aa_i_PerceptRewardFromState<S>) p);
            if (null == a) {
                a = aa_ActionDynamicNoOp.NO_OP;
                setAlive(false);
            }
            return a;
        }
        throw new IllegalArgumentException(
                "Percept passed in must be a PerceptStateReward");
    }
}
