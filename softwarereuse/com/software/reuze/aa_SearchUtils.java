package com.software.reuze;

import java.util.ArrayList;
import java.util.List;

import com.software.reuze.aa_ActionDynamicNoOp;
import com.software.reuze.aa_i_Action;


/**
 * @author Ravi Mohan
 * 
 */
public class aa_SearchUtils {

	public static List<aa_i_Action> actionsFromNodes(List<aa_TreeSearchNode> nodeList) {
		List<aa_i_Action> actions = new ArrayList<aa_i_Action>();
		if (nodeList.size() == 1) {
			// I'm at the root node, this indicates I started at the
			// Goal node, therefore just return a NoOp
			actions.add(aa_ActionDynamicNoOp.NO_OP);
		} else {
			// ignore the root node this has no action
			// hence index starts from 1 not zero
			for (int i = 1; i < nodeList.size(); i++) {
				aa_TreeSearchNode node = nodeList.get(i);
				actions.add(node.getAction());
			}
		}
		return actions;
	}

	public static boolean isGoalState(a_Problem p, aa_TreeSearchNode n) {
		boolean isGoal = false;
		a_i_GoalTest gt = p.getGoalTest();
		if (gt.isGoalState(n.getState())) {
			if (gt instanceof aa_SolutionChecker) {
				isGoal = ((aa_SolutionChecker) gt).isAcceptableSolution(
						aa_SearchUtils.actionsFromNodes(n.getPathFromRoot()),
						n.getState());
			} else {
				isGoal = true;
			}
		}
		return isGoal;
	}
}