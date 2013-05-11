package reuze.pending;
import java.util.ArrayList;

import com.software.reuze.aa_EnvironmentMap;
import com.software.reuze.aa_EnvironmentMapScenario;
import com.software.reuze.aa_SimpleProblemSolvingMap;
import com.software.reuze.apm_a_HeuristicAdaptable;
import com.software.reuze.d_MapAustraliaRoadsSimplified;
import com.software.reuze.d_MapExtendable;
import com.software.reuze.d_MapRomaniaRoadsSimplified;
import com.software.reuze.ga_Point;


/**
 * Demo example of a route finding agent application with GUI. The main method
 * starts a map agent frame and supports runtime experiments. This
 * implementation is based on the {@link aa_SimpleProblemSolvingMap.core.environment.map.MapAgent} and
 * the {@link aima.core.environment.map.aa_EnvironmentMap}. It can be used as a
 * code template for creating new applications with different specialized kinds
 * of agents and environments.
 * 
 * @author Ruediger Lunde
 */
public class demoRouteFindingAgent extends SimpleAgentApp {

	/** Creates a <code>MapAgentView</code>. */
	public AgentAppEnvironmentView createEnvironmentView() {
		return new ExtendedMapAgentView();
	}
	
	/** Creates and configures a <code>RouteFindingAgentFrame</code>. */
	@Override
	public AgentAppFrame createFrame() {
		return new RouteFindingAgentFrame();
	}

	/** Creates a <code>RouteFindingAgentController</code>. */
	@Override
	public AgentAppController createController() {
		return new RouteFindingAgentController();
	}

	// //////////////////////////////////////////////////////////
	// local classes

	/** Frame for a graphical route finding agent application. */
	protected static class RouteFindingAgentFrame extends MapAgentFrame {
		private static final long serialVersionUID = 1L;

		public static enum MapType {
			ROMANIA, AUSTRALIA
		};

		private MapType usedMap = null;
		private static String[] ROMANIA_DESTS = new String[] {
				"to Bucharest", "to Eforie", "to Neamt",
				"to Random" };
		private static String[] AUSTRALIA_DESTS = new String[] {
				"to Port Hedland", "to Albany", "to Melbourne",
				"to Random" };

		/** Creates a new frame. */
		public RouteFindingAgentFrame() {
			setTitle("RFA - the Route Finding Agent");
			setSelectorItems(SCENARIO_SEL, new String[] {
					"Romania, from Arad", "Romania, from Lugoj",
					"Romania, from Fagaras",
					"Australia, from Sydney",
					"Australia, from Random" }, 0);
			setSelectorItems(SEARCH_MODE_SEL, SearchFactory.getInstance()
					.getSearchModeNames(), 1); // change the default!
			setSelectorItems(HEURISTIC_SEL, new String[] { "=0", "SLD" }, 1);
		}

		/**
		 * Changes the destination selector items depending on the scenario
		 * selection if necessary, and calls the super class implementation
		 * afterwards.
		 */
		@Override
		protected void selectionChanged(String changedSelector) {
			SelectionState state = getSelection();
			int scenarioIdx = state.getValue(MapAgentFrame.SCENARIO_SEL);
			RouteFindingAgentFrame.MapType mtype = (scenarioIdx < 3) ? MapType.ROMANIA
					: MapType.AUSTRALIA;
			if (mtype != usedMap) {
				usedMap = mtype;
				String[] items = null;
				switch (mtype) {
				case ROMANIA:
					items = ROMANIA_DESTS;
					break;
				case AUSTRALIA:
					items = AUSTRALIA_DESTS;
					break;
				}
				setSelectorItems(DESTINATION_SEL, items, 0);
			}
			super.selectionChanged(changedSelector);
		}
	}

	/** Controller for a graphical route finding agent application. */
	protected static class RouteFindingAgentController extends
			AbstractMapAgentController {
		/**
		 * Configures a scenario and a list of destinations. Note that for route
		 * finding problems, the size of the list needs to be 1.
		 */
		@Override
		protected void selectScenarioAndDest(int scenarioIdx, int destIdx) {
			d_MapExtendable map = new d_MapExtendable();
			aa_EnvironmentMap env = new aa_EnvironmentMap(map);
			String agentLoc = null;
			switch (scenarioIdx) {
			case 0:
				d_MapRomaniaRoadsSimplified.initMap(map);
				agentLoc = d_MapRomaniaRoadsSimplified.ARAD;
				break;
			case 1:
				d_MapRomaniaRoadsSimplified.initMap(map);
				agentLoc = d_MapRomaniaRoadsSimplified.LUGOJ;
				break;
			case 2:
				d_MapRomaniaRoadsSimplified.initMap(map);
				agentLoc = d_MapRomaniaRoadsSimplified.FAGARAS;
				break;
			case 3:
				d_MapAustraliaRoadsSimplified.initMap(map);
				agentLoc = d_MapAustraliaRoadsSimplified.SYDNEY;
				break;
			case 4:
				d_MapAustraliaRoadsSimplified.initMap(map);
				agentLoc = map.randomlyGenerateDestination();
				break;
			}
			scenario = new aa_EnvironmentMapScenario(env, map, agentLoc);

			destinations = new ArrayList<String>();
			if (scenarioIdx < 3) {
				switch (destIdx) {
				case 0:
					destinations
							.add(d_MapRomaniaRoadsSimplified.BUCHAREST);
					break;
				case 1:
					destinations.add(d_MapRomaniaRoadsSimplified.EFORIE);
					break;
				case 2:
					destinations.add(d_MapRomaniaRoadsSimplified.NEAMT);
					break;
				case 3:
					destinations.add(map.randomlyGenerateDestination());
					break;
				}
			} else {
				switch (destIdx) {
				case 0:
					destinations.add(d_MapAustraliaRoadsSimplified.PORT_HEDLAND);
					break;
				case 1:
					destinations.add(d_MapAustraliaRoadsSimplified.ALBANY);
					break;
				case 2:
					destinations.add(d_MapAustraliaRoadsSimplified.MELBOURNE);
					break;
				case 3:
					destinations.add(map.randomlyGenerateDestination());
					break;
				}
			}
		}

		/**
		 * Prepares the view for the previously specified scenario and
		 * destinations.
		 */
		@Override
		protected void prepareView() {
			ExtendedMapAgentView mEnv = (ExtendedMapAgentView) frame.getEnvView();
			mEnv.setData(scenario, destinations, null);
			mEnv.setEnvironment(scenario.getEnv());
		}

		/**
		 * Returns the trivial zero function or a simple heuristic which is
		 * based on straight-line distance computation.
		 */
		@Override
		protected apm_a_HeuristicAdaptable createHeuristic(int heuIdx) {
			apm_a_HeuristicAdaptable ahf = null;
			switch (heuIdx) {
			case 0:
				ahf = new H1();
				break;
			default:
				ahf = new H2();
			}
			return ahf.adaptToGoal(destinations.get(0), scenario
					.getAgentMap());
		}

		/**
		 * Creates a new agent and adds it to the scenario's environment.
		 */
		@Override
		public void initAgents(MessageLogger logger) {
			if (destinations.size() != 1) {
				logger.log("Error: This agent requires exact one destination.");
				return;
			}
			aa_EnvironmentMap env = scenario.getEnv();
			String goal = destinations.get(0);
			aa_SimpleProblemSolvingMap agent = new aa_SimpleProblemSolvingMap(env.getMap(), env, search, new String[] { goal });
			env.addAgent(agent, scenario.getInitAgentLocation());
		}
	}

	/**
	 * Returns always the heuristic value 0.
	 */
	static class H1 extends apm_a_HeuristicAdaptable {

		public double h(Object state) {
			return 0.0;
		}
	}

	/**
	 * A simple heuristic which interprets <code>state</code> and {@link #goal}
	 * as location names and uses the straight-line distance between them as
	 * heuristic value.
	 */
	static class H2 extends apm_a_HeuristicAdaptable {

		public double h(Object state) {
			double result = 0.0;
			ga_Point pt1 = map.getPosition((String) state);
			ga_Point pt2 = map.getPosition((String) goal);
			if (pt1 != null && pt2 != null)
				result = pt1.distance(pt2);
			return result;
		}
	}

	// //////////////////////////////////////////////////////////
	// starter method

	/** Application starter. */
	public static void main(String args[]) {
		new demoRouteFindingAgent().startApplication();
	}
}
