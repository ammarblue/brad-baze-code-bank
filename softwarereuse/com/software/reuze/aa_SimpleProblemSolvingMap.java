package com.software.reuze;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.software.reuze.a_Problem;
import com.software.reuze.aa_PerceptWithDynamicAttributes;
import com.software.reuze.aa_StateWithDynamicAttributes;
import com.software.reuze.aa_a_SimpleProblemSolving;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_EnvironmentViewNotifier;
import com.software.reuze.aa_i_Percept;
import com.software.reuze.aa_i_State;
import com.software.reuze.das_i_SearchProblem;


/**
 * @author Ciaran O'Reilly
 * 
 */
public class aa_SimpleProblemSolvingMap extends aa_a_SimpleProblemSolving {
	private d_i_Map map = null;

	private aa_i_EnvironmentViewNotifier notifier = null;

	private aa_StateWithDynamicAttributes state = new aa_StateWithDynamicAttributes();

	private das_i_SearchProblem search = null;

	private String[] goalTests = null;

	private int goalTestPos = 0;

	public aa_SimpleProblemSolvingMap(d_i_Map map, aa_i_EnvironmentViewNotifier notifier, das_i_SearchProblem search) {
		this.map = map;
		this.notifier = notifier;
		this.search = search;
	}

	public aa_SimpleProblemSolvingMap(d_i_Map map, aa_i_EnvironmentViewNotifier notifier, das_i_SearchProblem search,
			int maxGoalsToFormulate) {
		super(maxGoalsToFormulate);
		this.map = map;
		this.notifier = notifier;
		this.search = search;
	}

	public aa_SimpleProblemSolvingMap(d_i_Map map, aa_i_EnvironmentViewNotifier notifier, das_i_SearchProblem search,
			String[] goalTests) {
		super(goalTests.length);
		this.map = map;
		this.notifier = notifier;
		this.search = search;
		this.goalTests = new String[goalTests.length];
		System.arraycopy(goalTests, 0, this.goalTests, 0, goalTests.length);
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected aa_i_State updateState(aa_i_Percept p) {
		aa_PerceptWithDynamicAttributes dp = (aa_PerceptWithDynamicAttributes) p;

		state.setAttribute(aa_AttributeLocation.AGENT_LOCATION,
				dp.getAttribute(aa_AttributeLocation.PERCEPT_IN));

		return state;
	}

	@Override
	protected Object formulateGoal() {
		Object goal = null;
		if (null == goalTests) {
			goal = map.randomlyGenerateDestination();
		} else {
			goal = goalTests[goalTestPos];
			goalTestPos++;
		}
		notifier.notifyViews("CurrentLocation=In("
				+ state.getAttribute(aa_AttributeLocation.AGENT_LOCATION)
				+ "), Goal=In(" + goal + ")");

		return goal;
	}

	@Override
	protected a_Problem formulateProblem(Object goal) {
		return new apm_ProblemBidirectional(map,
				(String) state.getAttribute(aa_AttributeLocation.AGENT_LOCATION),
				(String) goal);
	}

	@Override
	protected List<aa_i_Action> search(a_Problem problem) {
		List<aa_i_Action> actions = new ArrayList<aa_i_Action>();
		try {
			List<aa_i_Action> sactions = search.search(problem);
			for (aa_i_Action action : sactions) {
				actions.add(action);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return actions;
	}

	@Override
	protected void notifyViewOfMetrics() {
		Set<String> keys = search.getMetrics().keySet();
		for (String key : keys) {
			notifier.notifyViews("METRIC[" + key + "]="
					+ search.getMetrics().get(key));
		}
	}
}
