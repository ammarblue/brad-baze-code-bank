package com.software.reuze;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.software.reuze.aa_PerceptWithDynamicAttributes;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_ActionResultFunction;
import com.software.reuze.aa_i_ActionsFunction;
import com.software.reuze.aa_i_Percept;
import com.software.reuze.aa_i_PerceptToStateFunction;


/**
 * @author Ciaran O'Reilly
 * 
 */
public class aa_ActionsFunctionMap {
	private static aa_i_ActionResultFunction _resultFunction = null;
	private static aa_i_PerceptToStateFunction _perceptToStateFunction = null;

	public static aa_i_ActionsFunction getActionsFunction(d_i_Map map) {
		return new MapActionsFunction(map);
	}

	public static aa_i_ActionResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new MapResultFunction();
		}
		return _resultFunction;
	}

	private static class MapActionsFunction implements aa_i_ActionsFunction {
		private d_i_Map map = null;

		public MapActionsFunction(d_i_Map map) {
			this.map = map;
		}

		public Set<aa_i_Action> actions(Object state) {
			Set<aa_i_Action> actions = new LinkedHashSet<aa_i_Action>();
			String location = state.toString();

			List<String> linkedLocations = map.getLocationsLinkedTo(location);
			for (String linkLoc : linkedLocations) {
				actions.add(new aa_ActionDynamicMoveTo(linkLoc));
			}

			return actions;
		}
	}

	public static aa_i_PerceptToStateFunction getPerceptToStateFunction() {
		if (null == _perceptToStateFunction) {
			_perceptToStateFunction = new MapPerceptToStateFunction();
		}
		return _perceptToStateFunction;
	}

	private static class MapResultFunction implements aa_i_ActionResultFunction {
		public MapResultFunction() {
		}

		public Object result(Object s, aa_i_Action a) {

			if (a instanceof aa_ActionDynamicMoveTo) {
				aa_ActionDynamicMoveTo mta = (aa_ActionDynamicMoveTo) a;

				return mta.getToLocation();
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}

	private static class MapPerceptToStateFunction implements
			aa_i_PerceptToStateFunction {
		public Object getState(aa_i_Percept p) {
			return ((aa_PerceptWithDynamicAttributes) p)
					.getAttribute(aa_AttributeLocation.PERCEPT_IN);
		}
	}
}
