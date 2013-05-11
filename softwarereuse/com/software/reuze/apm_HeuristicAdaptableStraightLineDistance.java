package com.software.reuze;

import com.software.reuze.ga_Point;

/**
 * @author Ruediger Lunde
 */
public class apm_HeuristicAdaptableStraightLineDistance extends
		apm_a_HeuristicAdaptable {

	public apm_HeuristicAdaptableStraightLineDistance(Object goal, d_i_Map map) {
		this.goal = goal;
		this.map = map;
	}

	public double h(Object state) {
		double result = 0.0;
		ga_Point pt1 = map.getPosition((String) state);
		ga_Point pt2 = map.getPosition((String) goal);
		if (pt1 != null && pt2 != null) {
			result = pt1.distance(pt2);
		}
		return result;
	}
}
