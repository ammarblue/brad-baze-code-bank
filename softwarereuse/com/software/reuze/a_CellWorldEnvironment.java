package com.software.reuze;
//package aima.core.learning.reinforcement.example;

/*import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.environment.cellworld.CellContent;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.probability.mdp.TransitionProbabilityFunction;
import aima.core.util.Randomizer;*/
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Implementation of the CellContent World Environment, supporting the execution of
 * trials for reinforcement learning agents.
 *
 * @author Ciaran O'Reilly
 */
public class a_CellWorldEnvironment extends aa_a_Environment {

    private ga_CellPositionAndContent<Double> startingCell = null;
    private Set<ga_CellPositionAndContent<Double>> allStates = new LinkedHashSet<ga_CellPositionAndContent<Double>>();
    private mp_i_MDPTransitionProbabilityFunction<ga_CellPositionAndContent<Double>, a_CellWorldAction> tpf;
    private m_i_Random r = null;
    private a_CellWorldEnvironmentState currentState = new a_CellWorldEnvironmentState();

    /**
     * Constructor.
     *
     * @param startingCell the cell that agent(s) are to start from at the
     * beginning of each trial within the environment.
     * @param allStates all the possible states in this environment.
     * @param tpf the transition probability function that simulates how the
     * environment is meant to behave in response to an agent action.
     * @param r a Randomizer used to sample actions that are actually to be
     * executed based on the transition probabilities for actions.
     */
    public a_CellWorldEnvironment(ga_CellPositionAndContent<Double> startingCell,
            Set<ga_CellPositionAndContent<Double>> allStates,
            mp_i_MDPTransitionProbabilityFunction<ga_CellPositionAndContent<Double>, a_CellWorldAction> tpf,
            m_i_Random r) {
        this.startingCell = startingCell;
        this.allStates.addAll(allStates);
        this.tpf = tpf;
        this.r = r;
    }

    /**
     * Execute N trials.
     *
     * @param n the number of trials to execute.
     */
    public void executeTrials(int n) {
        for (int i = 0; i < n; i++) {
            executeTrial();
        }
    }

    /**
     * Execute a single trial.
     */
    public void executeTrial() {
        currentState.reset();
        for (aa_i_Agent a : agents) {
            a.setAlive(true);
            currentState.setAgentLocation(a, startingCell);
        }
        stepUntilDone();
    }

    /**
     * Return current state
     *
     * @return
     */
    @Override
    public aa_i_EnvironmentState getCurrentState() {
        return currentState;
    }

    /**
     * Execute an action, returning a new state
     *
     * @param agent
     * @param action
     * @return
     */
    @Override
    public aa_i_EnvironmentState executeAction(aa_i_Agent agent, aa_i_Action action) {
        if (!action.isNoOp()) {
            ga_CellPositionAndContent<Double> s = currentState.getAgentLocation(agent);
            double probabilityChoice = r.nextDouble();
            double total = 0;
            boolean set = false;
            for (ga_CellPositionAndContent<Double> sDelta : allStates) {
                total += tpf.probability(sDelta, s, (a_CellWorldAction) action);
                if (total > 1.0) {
                    throw new IllegalStateException("Bad probability calculation.");
                }
                if (total > probabilityChoice) {
                    currentState.setAgentLocation(agent, sDelta);
                    set = true;
                    break;
                }
            }
            if (!set) {
                throw new IllegalStateException("Failed to simulate the action=" + action + " correctly from s=" + s);
            }
        }

        return currentState;
    }

    /**
     * Collect percept
     *
     * @param anAgent
     * @return
     */
    @Override
    public aa_i_Percept getPerceptSeenBy(aa_i_Agent anAgent) {
        return currentState.getPerceptFor(anAgent);
    }
}