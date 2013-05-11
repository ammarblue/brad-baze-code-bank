package reuze.pending;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.software.reuze.aa_EnvironmentMapScenario;
import com.software.reuze.d_i_Map;
import com.software.reuze.ga_Point;

/**
 * Extends the <code>MapAgentView</code> by visualization of
 * scenario, destination, and agent map information.
 * 
 * @author Ruediger Lunde
 */
public class ExtendedMapAgentView extends MapAgentView {
	
	private static final long serialVersionUID = 1L;
	/** A scenario. */
	protected aa_EnvironmentMapScenario scenario;
	/** A list of location names, possibly null. */
	protected List<String> destinations;
	/** Map which reflects the agent's knowledge about the environment. */
	protected d_i_Map agentMap;

	/** Sets data to be displayed. All values may be null. */
	public void setData(aa_EnvironmentMapScenario scenario, List<String> destinations, d_i_Map agentMap) {
		this.scenario = scenario;
		this.destinations = destinations;
		this.agentMap = agentMap;
		repaint();
	}
	
	/**
	 * Represents roads by lines and locations by name-labeled points.
	 */
	protected void paintMap(java.awt.Graphics2D g2) {
		d_i_Map envMap = getMapEnv().getMap();
		d_i_Map aMap = (agentMap != null) ? agentMap : envMap;
		List<Roadblock> roadblocks = new ArrayList<Roadblock>();
		for (String l1 : envMap.getLocations()) {
			ga_Point pt1 = envMap.getPosition(l1);
			List<String> linkedLocs = envMap.getLocationsLinkedTo(l1);
			for (String l2 : aMap.getLocationsLinkedTo(l1))
				if (!linkedLocs.contains(l2))
					linkedLocs.add(l2);
			for (String l2 : linkedLocs) {
				ga_Point pt2 = envMap.getPosition(l2);
				g2.setColor(Color.lightGray);
				g2.drawLine(x(pt1), y(pt1), x(pt2), y(pt2));
				boolean blockedInEnv = !envMap.getLocationsLinkedTo(l2)
						.contains(l1);
				boolean blockedInAgent = !aMap.getLocationsLinkedTo(l2)
						.contains(l1);
				roadblocks.add(new Roadblock(pt1, pt2, blockedInEnv,
						blockedInAgent));
				if (blockedInEnv && blockedInAgent) {
					boolean blockedInEnvOtherDir = !envMap
							.getLocationsLinkedTo(l1).contains(l2);
					boolean blockedInAgentOtherDir = !aMap
							.getLocationsLinkedTo(l1).contains(l2);
					roadblocks.add(new Roadblock(pt2, pt1,
							blockedInEnvOtherDir, blockedInAgentOtherDir));
				}
			}
		}
		for (Roadblock block : roadblocks)
			paintRoadblock(g2, block);
	}
	
	/** Displays a map location. */
	protected void paintLoc(Graphics2D g2, String loc) {
		d_i_Map map = getMapEnv().getMap();
		ga_Point pt = map.getPosition(loc);
		if (pt != null) {
			int x = x(pt);
			int y = y(pt);
			String info = "";
			List<String> track = new ArrayList<String>();
			if (!env.getAgents().isEmpty())
				// show details only for track of first agent...
				track = getTrack(env.getAgents().get(0));
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < track.size(); i++)
				if (track.get(i).equals(loc))
					list.add(i + 1);
			if (!list.isEmpty())
				info = list.toString();
//			if (getMapEnv().hasObjects(loc)) {
//				g2.setColor(Color.green);
//				g2.fillOval(x - 5, y - 5, 10, 10);
//			}
			if (scenario != null && scenario.getInitAgentLocation().equals(loc)) {
				g2.setColor(Color.red);
				g2.fillOval(x - 7, y - 7, 14, 14);
			}
			if (getAgentLocs().contains(loc)) {
				g2.setColor(Color.red);
				g2.fillOval(x - 4, y - 4, 8, 8);
			}
//			if (maModel.hasInfos(loc)) {
//				g2.setColor(Color.blue);
//				g2.drawString("i", x, y + 12);
//			}
			// if (model.isStart(loc))
			// g2.setColor(Color.red);
			// else
			if (destinations != null && destinations.contains(loc))
				g2.setColor(Color.green);
			else if (track.contains(loc))
				g2.setColor(Color.black);
			else
				g2.setColor(Color.gray);
			g2.drawString(loc + info, x, y);
		}
	}
	
	/**
	 * Blocked roads are represented by filled rectangles. Blue denotes, the
	 * agent doesn't know it, red denotes, the road is no blocked, but the
	 * agent thinks so.
	 */
	private void paintRoadblock(java.awt.Graphics2D g2, Roadblock block) {
		if (block.inEnvMap || block.inAgentMap) {
			int x = (int) (0.2 * x(block.pos1) + 0.8 * x(block.pos2) - 4);
			int y = (int) (0.2 * y(block.pos1) + 0.8 * y(block.pos2) - 4);
			if (!block.inAgentMap)
				g2.setColor(Color.blue); // agent doesn't know the road block
			else if (!block.inEnvMap)
				g2.setColor(Color.red); // agent doesn't know the way
			else
				g2.setColor(Color.lightGray);
			g2.fillRect(x, y, 9, 9);
		}
	}
	
	/**
	 * Stores road block information. Informations about obstacles are
	 * generally printed after the roads itself so that they always appear
	 * in front.
	 */
	private static class Roadblock {
		ga_Point pos1;
		ga_Point pos2;
		boolean inEnvMap;
		boolean inAgentMap;
	
		private Roadblock(ga_Point pos1, ga_Point pos2, boolean inEnvMap,
				boolean inAgentMap) {
			this.pos1 = pos1;
			this.pos2 = pos2;
			this.inEnvMap = inEnvMap;
			this.inAgentMap = inAgentMap;
		}
	}
}
