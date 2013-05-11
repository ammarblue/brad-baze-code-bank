package reuze.pending;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.software.reuze.aa_EnvironmentMap;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Agent;
import com.software.reuze.aa_i_EnvironmentState;
import com.software.reuze.d_i_Map;
import com.software.reuze.ga_Point;


/**
 * General graphical environment view implementation for map agent
 * applications. This view requires the used environment
 * to be of type {@link aa_EnvironmentMap}. All agents are tracked but
 * only the track of the first agent is shown.
 * 
 * @author Ruediger Lunde
 */
public class MapAgentView extends EmptyEnvironmentView {

	private static final long serialVersionUID = 1L;
	/** Stores for each agent the locations, it has already visited. */
	private final Hashtable<aa_i_Agent,List<String>> agentTracks = new Hashtable<aa_i_Agent,List<String>>();

	protected aa_EnvironmentMap getMapEnv() {
		return (aa_EnvironmentMap) env;
	}
	
	/** Returns a list of all already visited agent locations. */
	public List<String> getTrack(aa_i_Agent agent) {
		return agentTracks.get(agent);
	}

	/** Clears the list of already visited locations. */
	public void clearTracks() {
		agentTracks.clear();
	}
	
	
	/**
	 * Reacts on environment changes and updates the agent tracks.
	 */
	@Override
	public void agentAdded(aa_i_Agent agent, aa_i_EnvironmentState resultingState) {
		updateTracks();
		super.agentAdded(agent, resultingState);
	}

	/**
	 * Reacts on environment changes and updates the agent tracks. The command
	 * is always send to the message logger as string.
	 */
	@Override
	public void agentActed(aa_i_Agent agent, aa_i_Action command, aa_i_EnvironmentState state) {
		aa_EnvironmentMap mEnv = getMapEnv();
		String msg = "";
		if (mEnv.getAgents().size() > 1)
			msg = "A" + mEnv.getAgents().indexOf(agent) + ": ";
		notify(msg + command.toString());
		updateTracks();
		repaint();
	}

	/**
	 * Clears the panel, displays the map, the current agent locations,
	 * and the track of the first agent.
	 */
	@Override
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		if (env != null) {
			d_i_Map map = getMapEnv().getMap();
			if (!map.getLocations().isEmpty()) {
				updateTracks(); // safety first!
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				adjustTransformation();
				paintMap(g2);
				for (aa_i_Agent a : env.getAgents())
					paintTrack(g2, a);
				for (String loc : map.getLocations())
					paintLoc(g2, loc);
			}
		}
	}

	/** Updates all tracks with respect to the current agent locations. */
	protected void updateTracks() {
		aa_EnvironmentMap mEnv = getMapEnv();
		if (mEnv != null)
			for (aa_i_Agent a : mEnv.getAgents()) {
				List<String> aTrack = getTrack(a);
				String aLoc = mEnv.getAgentLocation(a);
				if (aTrack == null) {
					aTrack = new ArrayList<String>();
					agentTracks.put(a, aTrack);
				}
				if (aTrack.isEmpty() || !aTrack.get(aTrack.size()-1).equals(aLoc))
					aTrack.add(aLoc);
			}
	}
	
	/** Returns the locations of all agents. */
	protected List<String> getAgentLocs() {
		List<String> result = new ArrayList<String>();
		aa_EnvironmentMap mEnv = getMapEnv();
		for (aa_i_Agent a : mEnv.getAgents())
			result.add(mEnv.getAgentLocation(a));
		return result;
	}
	
	/**
	 * Adjusts offsets and scale so that the whole map fits on the view
	 * without scrolling.
	 */
	private void adjustTransformation() {
		d_i_Map map = getMapEnv().getMap();
		List<String> locs = map.getLocations();
		// adjust coordinates relative to the left upper corner of the graph
		// area
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		for (String loc : locs) {
			ga_Point xy = map.getPosition(loc);
			if (xy.getX() < minX)
				minX = xy.getX();
			if (xy.getY() < minY)
				minY = xy.getY();
			if (xy.getX() > maxX)
				maxX = xy.getX();
			if (xy.getY() > maxY)
				maxY = xy.getY();
		}
		this.setBorder(20, 20, 20, 100);
		adjustTransformation(minX, minY, maxX, maxY);
	}

	/**
	 * Represents roads by lines and locations by name-labeled points.
	 */
	protected void paintMap(java.awt.Graphics2D g2) {
		d_i_Map envMap = getMapEnv().getMap();
		for (String l1 : envMap.getLocations()) {
			ga_Point pt1 = envMap.getPosition(l1);
			List<String> linkedLocs = envMap.getLocationsLinkedTo(l1);
			for (String l2 : linkedLocs) {
				ga_Point pt2 = envMap.getPosition(l2);
				g2.setColor(Color.lightGray);
				g2.drawLine(x(pt1), y(pt1), x(pt2), y(pt2));
			}
		}
	}

	/** The track of the agent is visualized with red lines. */
	private void paintTrack(java.awt.Graphics2D g2, aa_i_Agent a) {
		d_i_Map map = getMapEnv().getMap();
		ga_Point lastPt = null;
		g2.setColor(Color.red);
		for (String loc : getTrack(a)) {
			ga_Point pt = map.getPosition(loc);
			if (pt != null && lastPt != null) {
				g2.drawLine(x(pt), y(pt), x(lastPt), y(lastPt));
			}
			lastPt = pt;
		}
	}

	protected void paintLoc(java.awt.Graphics2D g2, String loc) {
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
			if (getAgentLocs().contains(loc)) {
				g2.setColor(Color.red);
				g2.fillOval(x - 4, y - 4, 8, 8);
			}
			if (track.contains(loc))
				g2.setColor(Color.black);
			else
				g2.setColor(Color.gray);
			g2.drawString(loc + info, x, y);
		}
	}
}