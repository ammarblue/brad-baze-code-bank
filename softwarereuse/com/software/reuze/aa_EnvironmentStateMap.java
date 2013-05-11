package com.software.reuze;

import java.util.HashMap;

import com.software.reuze.aa_i_Agent;
import com.software.reuze.aa_i_EnvironmentState;
import com.software.reuze.d_Pair;


/**
 * @author Ciaran O'Reilly
 * 
 */
public class aa_EnvironmentStateMap implements aa_i_EnvironmentState {
	private java.util.Map<aa_i_Agent, d_Pair<String, Double>> agentLocationAndTravelDistance = new HashMap<aa_i_Agent, d_Pair<String, Double>>();

	public aa_EnvironmentStateMap() {

	}

	public String getAgentLocation(aa_i_Agent a) {
		d_Pair<String, Double> locAndTDistance = agentLocationAndTravelDistance
				.get(a);
		if (null == locAndTDistance) {
			return null;
		}
		return locAndTDistance.getFirst();
	}

	public Double getAgentTravelDistance(aa_i_Agent a) {
		d_Pair<String, Double> locAndTDistance = agentLocationAndTravelDistance
				.get(a);
		if (null == locAndTDistance) {
			return null;
		}
		return locAndTDistance.getSecond();
	}

	public void setAgentLocationAndTravelDistance(aa_i_Agent a, String location,
			Double travelDistance) {
		agentLocationAndTravelDistance.put(a, new d_Pair<String, Double>(
				location, travelDistance));
	}
}
