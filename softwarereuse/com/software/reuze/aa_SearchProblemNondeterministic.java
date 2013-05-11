package com.software.reuze;

/**
 * Non-deterministic problems may have multiple results for a given state and
 * action; this class handles these results by mimicking Problem and replacing
 * ResultFunction (one result) with aa_i_ResultsFunction (a set of results).
 * 
 * @author Andrew Brown
 */
public class aa_SearchProblemNondeterministic {

        protected Object initialState;
        protected aa_i_ActionsFunction actionsFunction;
        protected a_i_GoalTest goalTest;
        protected aa_i_CostFunctionStep stepCostFunction;
        protected aa_i_ResultsFunction resultsFunction;

        /**
         * Constructor
         * 
         * @param initialState
         * @param actionsFunction
         * @param resultFunction
         * @param goalTest
         */
        public aa_SearchProblemNondeterministic(Object initialState,
                        aa_i_ActionsFunction actionsFunction, aa_i_ResultsFunction resultsFunction,
                        a_i_GoalTest goalTest) {
                this(initialState, actionsFunction, resultsFunction, goalTest,
                                new aa_CostFunctionStepDefault());
        }

        /**
         * Constructor
         * 
         * @param initialState
         * @param actionsFunction
         * @param resultsFunction
         * @param goalTest
         * @param stepCostFunction
         */
        public aa_SearchProblemNondeterministic(Object initialState,
                        aa_i_ActionsFunction actionsFunction, aa_i_ResultsFunction resultsFunction,
                        a_i_GoalTest goalTest, aa_i_CostFunctionStep stepCostFunction) {
                this.initialState = initialState;
                this.actionsFunction = actionsFunction;
                this.resultsFunction = resultsFunction;
                this.goalTest = goalTest;
                this.stepCostFunction = stepCostFunction;
        }

        /**
         * Returns the initial state of the agent.
         * 
         * @return the initial state of the agent.
         */
        public Object getInitialState() {
                return initialState;
        }

        /**
         * Returns <code>true</code> if the given state is a goal state.
         * 
         * @return <code>true</code> if the given state is a goal state.
         */
        public boolean isGoalState(Object state) {
                return goalTest.isGoalState(state);
        }

        /**
         * Returns the goal test.
         * 
         * @return the goal test.
         */
        public a_i_GoalTest getGoalTest() {
                return goalTest;
        }

        /**
         * Returns the description of the possible actions available to the agent.
         * 
         * @return the description of the possible actions available to the agent.
         */
        public aa_i_ActionsFunction getActionsFunction() {
                return actionsFunction;
        }

        /**
         * Return the description of what each action does.
         * 
         * @return the description of what each action does.
         */
        public aa_i_ResultsFunction getResultsFunction() {
                return this.resultsFunction;
        }

        /**
         * Returns the path cost function.
         * 
         * @return the path cost function.
         */
        public aa_i_CostFunctionStep getStepCostFunction() {
                return stepCostFunction;
        }
}