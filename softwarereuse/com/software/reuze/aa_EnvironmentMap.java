package com.software.reuze;
/** 
 * The contained <code>Map</code> interface provides
 * a simple but sufficient set of read methods for designing
 * environments, agents and viewers. <code>ExtendableMap</code>
 * implements this interface and adds functionality for
 * creation and modification. It maintains named locations with
 * coordinates and links between them.
 * The <code>MapEnvironment</code> enables a <code>MapAgent</code>
 * to travel through a world whose topology is described by a map.
 * Adaptable heuristic functions let the agent modify the
 * heuristic function with respect to the current goal. Standardized
 * dynamic attribute names in class <code>DynAttributeNames</code>
 * make information exchange between environment
 * and agent a bit safer without introducing direct dependencies.
 */
import com.software.reuze.aa_PerceptWithDynamicAttributes;
import com.software.reuze.aa_a_Environment;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Agent;
import com.software.reuze.aa_i_EnvironmentState;
import com.software.reuze.aa_i_Percept;

/**
 * Represents the environment a MapAgent can navigate.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class aa_EnvironmentMap extends aa_a_Environment {

	private d_i_Map map = null;
	private aa_EnvironmentStateMap state = new aa_EnvironmentStateMap();

	public aa_EnvironmentMap(d_i_Map map) {
		this.map = map;
	}

	public void addAgent(aa_i_Agent a, String startLocation) {
		// Ensure the agent state information is tracked before
		// adding to super, as super will notify the registered
		// EnvironmentViews that is was added.
		state.setAgentLocationAndTravelDistance(a, startLocation, 0.0);
		super.addAgent(a);
	}

	public String getAgentLocation(aa_i_Agent a) {
		return state.getAgentLocation(a);
	}

	public Double getAgentTravelDistance(aa_i_Agent a) {
		return state.getAgentTravelDistance(a);
	}

	@Override
	public aa_i_EnvironmentState getCurrentState() {
		return state;
	}

	@Override
	public aa_i_EnvironmentState executeAction(aa_i_Agent agent, aa_i_Action a) {

		if (!a.isNoOp()) {
			aa_ActionDynamicMoveTo act = (aa_ActionDynamicMoveTo) a;

			String currLoc = getAgentLocation(agent);
			Double distance = map.getDistance(currLoc, act.getToLocation());
			if (distance != null) {
				double currTD = getAgentTravelDistance(agent);
				state.setAgentLocationAndTravelDistance(agent,
						act.getToLocation(), currTD + distance);
			}
		}

		return state;
	}

	@Override
	public aa_i_Percept getPerceptSeenBy(aa_i_Agent anAgent) {
		return new aa_PerceptWithDynamicAttributes(aa_AttributeLocation.PERCEPT_IN,
				getAgentLocation(anAgent));
	}

	public d_i_Map getMap() {
		return map;
	}
}