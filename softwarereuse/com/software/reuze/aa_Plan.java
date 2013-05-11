package com.software.reuze;

import java.util.LinkedList;

/**
 * Represents a solution plan for an AND-OR search; according to page 135
 * AIMA3e, the plan must be "a subtree that (1) has a goal node at every leaf,
 * (2) specifies one Object at each of its OR nodes, and (3) includes every
 * outcome branch at each of its AND nodes." As demonstrated on page 136, this
 * subtree is implemented as a linked list where every OR node is an Object--
 * satisfying (2)--and every AND node is an if-state-then-plan-else
 * chain--satisfying (3).
 * 
 * @author Andrew Brown
 */
public class aa_Plan extends LinkedList<Object> {

        private static final long serialVersionUID = 1L;

        /**
         * Each step is either an IfStateThenPlan, a Plan, or an Action.
         */
        LinkedList<Object> steps = new LinkedList<Object>();

        /**
         * Empty constructor
         */
        public aa_Plan() {
        }

        /**
         * Construct a plan based on a sequence of steps (IfStateThenPlan or a
         * Plan).
         * 
         * @param steps
         */
        public aa_Plan(Object... steps) {
                for (int i = 0; i < steps.length; i++) {
                        add(steps[i]);
                }
        }

        /**
         * Prepend an action to the plan and return itself.
         * 
         * @param action
         *            the action to be prepended to this plan.
         * @return this plan with action prepended to it.
         */
        public aa_Plan prepend(aa_i_Action action) {
                this.offerFirst(action);
                return this;
        }

        /**
         * Returns the string representation of this plan
         * 
         * @return
         */
        @Override
        public String toString() {
                StringBuilder s = new StringBuilder();
                s.append("[");
                int count = 0;
                int size = this.size();
                for (Object step : this) {
                        s.append(step);
                        if (count < size - 1) {
                                s.append(", ");
                        }
                        count++;
                }
                s.append("]");
                return s.toString();
        }
}