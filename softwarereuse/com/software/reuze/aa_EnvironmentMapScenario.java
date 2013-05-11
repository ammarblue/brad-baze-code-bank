package com.software.reuze;

/**
 * A scenario specifies an environment, the agent's knowledge about the
 * environment, and the agents initial location. It can be used to specify
 * settings for route planning agent applications.
 * 
 * @author Ruediger Lunde
 */
public class aa_EnvironmentMapScenario {
	/**
	 * A map-based environment. Note that the contained map must be of type
	 * {@link d_MapExtendable}.
	 */
	private final aa_EnvironmentMap env;
	/** A map reflecting the knowledge of the agent about the environment. */
	private final d_i_Map agentMap;
	/** Initial location of the agent. */
	private final String initAgentLoc;

	/**
	 * Creates a scenario.
	 * 
	 * @param env
	 *            a map-based environment. Note that the contained map must be
	 *            of type {@link d_MapExtendable}
	 * @param agentMap
	 *            a map reflecting the knowledge of the agent about the
	 *            environment
	 * @param agentLoc
	 *            initial location of the agent
	 */
	public aa_EnvironmentMapScenario(aa_EnvironmentMap env, d_i_Map agentMap, String agentLoc) {
		this.agentMap = agentMap;
		this.env = env;
		this.initAgentLoc = agentLoc;
	}

	public aa_EnvironmentMap getEnv() {
		return env;
	}

	public d_i_Map getEnvMap() {
		return env.getMap();
	}

	public d_i_Map getAgentMap() {
		return agentMap;
	}

	public String getInitAgentLocation() {
		return initAgentLoc;
	}
}
