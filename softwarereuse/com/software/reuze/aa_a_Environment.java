package com.software.reuze;
//package aima.core.agent.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentObject;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.agent.EnvironmentViewNotifier;
import aima.core.agent.Percept;*/

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public abstract class aa_a_Environment implements aa_i_Environment,
		aa_i_EnvironmentViewNotifier {

	// Note: Use LinkedHashSet's in order to ensure order is respected as
	// provide
	// access to these elements via List interface.
	protected Set<aa_i_EnvironmentObject> envObjects = new LinkedHashSet<aa_i_EnvironmentObject>();

	protected Set<aa_i_Agent> agents = new LinkedHashSet<aa_i_Agent>();

	protected Set<aa_i_EnvironmentView> views = new LinkedHashSet<aa_i_EnvironmentView>();

	protected Map<aa_i_Agent, Double> performanceMeasures = new LinkedHashMap<aa_i_Agent, Double>();

	//
	// PUBLIC METHODS
	//

	//
	// Methods to be implemented by subclasses.
	public abstract aa_i_EnvironmentState getCurrentState();

	public abstract aa_i_EnvironmentState executeAction(aa_i_Agent agent, aa_i_Action action);

	public abstract aa_i_Percept getPerceptSeenBy(aa_i_Agent anAgent);

	/**
	 * Method for implementing dynamic environments in which not all changes are
	 * directly caused by agent action execution. The default implementation
	 * does nothing.
	 */
	public void createExogenousChange() {
	}

	//
	// START-Environment
	public List<aa_i_Agent> getAgents() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<aa_i_Agent>(agents);
	}

	public void addAgent(aa_i_Agent a) {
		addEnvironmentObject(a);
	}

	public void removeAgent(aa_i_Agent a) {
		removeEnvironmentObject(a);
	}

	public List<aa_i_EnvironmentObject> getEnvironmentObjects() {
		// Return as a List but also ensures the caller cannot modify
		return new ArrayList<aa_i_EnvironmentObject>(envObjects);
	}

	public void addEnvironmentObject(aa_i_EnvironmentObject eo) {
		envObjects.add(eo);
		if (eo instanceof aa_i_Agent) {
			aa_i_Agent a = (aa_i_Agent) eo;
			if (!agents.contains(a)) {
				agents.add(a);
				this.updateEnvironmentViewsAgentAdded(a);
			}
		}
	}

	public void removeEnvironmentObject(aa_i_EnvironmentObject eo) {
		envObjects.remove(eo);
		agents.remove(eo);
	}

	/**
	 * Central template method for controlling agent simulation. The concrete
	 * behavior is determined by the primitive operations
	 * {@link #getPerceptSeenBy(aa_i_Agent)}, {@link #executeAction(aa_i_Agent, aa_i_Action)},
	 * and {@link #createExogenousChange()}.
	 */
	public void step() {
		for (aa_i_Agent agent : agents) {
			if (agent.isAlive()) {
				aa_i_Action anAction = agent.execute(getPerceptSeenBy(agent));
				aa_i_EnvironmentState es = executeAction(agent, anAction);
				updateEnvironmentViewsAgentActed(agent, anAction, es);
			}
		}
		createExogenousChange();
	}

	public void step(int n) {
		for (int i = 0; i < n; i++) {
			step();
		}
	}

	public void stepUntilDone() {
		while (!isDone()) {
			step();
		}
	}

	public boolean isDone() {
		for (aa_i_Agent agent : agents) {
			if (agent.isAlive()) {
				return false;
			}
		}
		return true;
	}

	public double getPerformanceMeasure(aa_i_Agent forAgent) {
		Double pm = performanceMeasures.get(forAgent);
		if (null == pm) {
			pm = new Double(0);
			performanceMeasures.put(forAgent, pm);
		}

		return pm;
	}

	public void addEnvironmentView(aa_i_EnvironmentView ev) {
		views.add(ev);
	}

	public void removeEnvironmentView(aa_i_EnvironmentView ev) {
		views.remove(ev);
	}

	public void notifyViews(String msg) {
		for (aa_i_EnvironmentView ev : views) {
			ev.notify(msg);
		}
	}

	// END-Environment
	//

	//
	// PROTECTED METHODS
	//

	protected void updatePerformanceMeasure(aa_i_Agent forAgent, double addTo) {
		performanceMeasures.put(forAgent, getPerformanceMeasure(forAgent)
				+ addTo);
	}

	protected void updateEnvironmentViewsAgentAdded(aa_i_Agent agent) {
		for (aa_i_EnvironmentView view : views) {
			view.agentAdded(agent, getCurrentState());
		}
	}

	protected void updateEnvironmentViewsAgentActed(aa_i_Agent agent, aa_i_Action action,
			aa_i_EnvironmentState state) {
		for (aa_i_EnvironmentView view : views) {
			view.agentActed(agent, action, state);
		}
	}
}