package reuze.pending;

import java.util.ArrayList;
import java.util.Collections;

import reuze.awt.ga_PathBlockingObstacleImpl;
import reuze.awt.ga_PolygonConverterJts;

import com.software.reuze.d_Bag;
import com.software.reuze.ga_ObstacleManager;
import com.software.reuze.ga_OccluderImpl;
import com.software.reuze.ga_PathNode;
import com.software.reuze.ga_PathNodeConnector;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_PolygonCollinearOverlapChecker;
import com.software.reuze.ga_TileArray;
import com.software.reuze.ga_TileArrayIntersections;
import com.software.reuze.ga_TileBag;
import com.software.reuze.ga_Vector2;
import com.software.reuze.pt_TimerCode;
import com.software.reuze.pt_TimerCode.Output;



/*
 * Copyright (c) 2008, Keith Woodward
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Keith Woodward nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */


/*import straightedge.geom.path.*;
import straightedge.geom.util.*;
import benchmark.event.*;
import straightedge.geom.*;
import straightedge.geom.vision.*;*/


/**
 * The model.
 * @author Keith Woodward
 */
public class GameWorld {
	long systemNanosAtStart;
	long nanosElapsed;
	static double NANOS_IN_A_SECOND = 1000000000;
	LoopGame loop;
	ArrayList<ga_Polygon> originalPolygons;
	ga_ObstacleManager<ga_PathBlockingObstacleImpl> obstacleManager;
	ga_TileArrayIntersections<ga_OccluderImpl> occluderTileArray;
	ArrayList<ga_OccluderImpl> movingOccluders;
	volatile boolean drawNodeConnections = false;
	volatile boolean drawGrid = false;
	ArrayList<IMonster> players;

	int numObstacles;
	int numNodes;
	int numPermanentNodeConnections;
	int numTemporaryNodeConnections;

	public GameWorld(){
	}

	pt_TimerCode codeTimer = new pt_TimerCode("GameWorld.init", pt_TimerCode.Output.Millis, pt_TimerCode.Output.Millis);
	public void init(float maxConnectionDist, float tileWidthAndHeight){
		originalPolygons = makePolygons();
		ga_PolygonCollinearOverlapChecker coc = new ga_PolygonCollinearOverlapChecker();
		coc.fixCollinearOverlaps(originalPolygons);
		codeTimer.click("init");
		occluderTileArray = new ga_TileArrayIntersections(new ga_Vector2(0, 0), new ga_Vector2(1000, 1000), tileWidthAndHeight);
		ga_TileArray<ga_PathBlockingObstacleImpl> tileArray = new ga_TileArray<ga_PathBlockingObstacleImpl>(new ga_Vector2(0, 0), new ga_Vector2(1000, 1000), tileWidthAndHeight);
		ga_TileBag tileBag = new ga_TileBag(tileArray, new d_Bag());
		obstacleManager = new ga_ObstacleManager<ga_PathBlockingObstacleImpl>(tileBag, maxConnectionDist);
		codeTimer.click("create and add");
		for (ga_Polygon poly : originalPolygons){
			{
//				PolygonConverter polygonConverter = new PolygonConverter();
//				com.vividsolutions.jts.geom.Polygon jtsPolygon = polygonConverter.makeJTSPolygonFrom(poly);
//				com.vividsolutions.jts.geom.Polygon shrunkJTSPolygon = null;
//				com.vividsolutions.jts.geom.Geometry bufferedGeometry = jtsPolygon.buffer(5, 1);
//				if (bufferedGeometry instanceof com.vividsolutions.jts.geom.Polygon){
//					shrunkJTSPolygon = (com.vividsolutions.jts.geom.Polygon)bufferedGeometry;
//				}else{
//					throw new RuntimeException("JTS didn't make a proper polygon, this might be because the outerPolygon is too small, so that after it's shrunk, it disappears or makes more than one Polygon.");
//				}
//				Polygon shrunkPolygon = polygonConverter.makePolygonFrom(shrunkJTSPolygon);
//				if (shrunkPolygon.isCounterClockWise() != poly.isCounterClockWise()){
//					shrunkPolygon.reversePointOrder();
//				}
//				UpdatableObstacle obst = new UpdatableObstacle(shrunkPolygon);
//				obstacleManager.addObstacle(obst);

				ga_PathBlockingObstacleImpl obst = ga_PathBlockingObstacleImpl.createObstacleFromOuterPolygon(poly.copy());
				if (obst == null){
					continue;
				}
				obstacleManager.addObstacle(obst);
			}
			{
				ga_PolygonConverterJts polygonConverter = new ga_PolygonConverterJts();
				com.vividsolutions.jts.geom.Polygon jtsPolygon = polygonConverter.makeJTSPolygonFrom(poly);
				com.vividsolutions.jts.geom.Polygon shrunkJTSPolygon = null;
				com.vividsolutions.jts.geom.Geometry bufferedGeometry = jtsPolygon.buffer(-0.01, 1);
				if (bufferedGeometry instanceof com.vividsolutions.jts.geom.Polygon){
					shrunkJTSPolygon = (com.vividsolutions.jts.geom.Polygon)bufferedGeometry;
				}else{
					throw new RuntimeException("JTS didn't make a proper polygon, this might be because the outerPolygon is too small, so that after it's shrunk, it disappears or makes more than one Polygon.");
				}
				ga_Polygon shrunkPolygon = polygonConverter.makePolygonFromExterior(shrunkJTSPolygon);
				if (shrunkPolygon.isCounterClockWise() != poly.isCounterClockWise()){
					shrunkPolygon.reversePointOrder();
				}
				occluderTileArray.add(new ga_OccluderImpl(shrunkPolygon));
			}
		}
		codeTimer.lastClick(true);
		movingOccluders = new ArrayList<ga_OccluderImpl>();

		players = new ArrayList<IMonster>();
	}
	
	// over ride this method to add obstacles etc
	protected ArrayList<ga_Polygon> makePolygons(){
		return new ArrayList<ga_Polygon>();
	}
	
	public void addPlayer(Player player){
		this.players.add(player);
		player.setWorld(this);
	}
	
	public void update(long nanos){
		double seconds = nanos/NANOS_IN_A_SECOND;
		double startTime = nanosElapsed/NANOS_IN_A_SECOND;
		//System.out.println(this.getClass().getSimpleName() + ": update, startTime == "+startTime+", seconds == "+seconds);
		assert seconds >= 0 : seconds;
		ArrayList<PlayerEvent> events = new ArrayList<PlayerEvent>();
		for (int i = 0; i < players.size(); i++){
			players.get(i).fillEventsList(events);
		}
		Collections.sort(events);

		if (events.size() > 0){
			int currentEventIndex = 0;
			double nextTimeStop;
			double endTime = startTime + seconds;
			//System.out.println(this.getClass().getSimpleName() + ": startTime == "+startTime+", endTime == "+endTime+", seconds == "+seconds);
			while (true) {
				nextTimeStop = events.get(currentEventIndex).getTimeStamp();
				//System.out.println(this.getClass().getSimpleName() + ": nextTimeStop == "+nextTimeStop);
				if (nextTimeStop > endTime){
					nextTimeStop = endTime;
				}
				double reducedSeconds = nextTimeStop - startTime;
				if (reducedSeconds < 0){
					reducedSeconds = 0;
				}
				if (reducedSeconds != 0){
					//System.out.println(this.getClass().getSimpleName() + ": doMove2(reducedSeconds, timeAtStartOfMoveSeconds), reducedSeconds == "+reducedSeconds+", timeAtStartOfMoveSeconds == "+timeAtStartOfMoveSeconds);
					doMove(reducedSeconds, startTime);
				}
				PlayerEvent ev = events.get(currentEventIndex);
				ev.getPlayer().processEvent(ev);
				startTime = nextTimeStop;
				if (endTime == nextTimeStop){
					seconds = 0;
				}else{
					seconds -= reducedSeconds;
				}
				currentEventIndex++;
				if (currentEventIndex >= events.size()){
					break;
				}
			}
		}
		for (int i = 0; i < players.size(); i++){
			players.get(i).beforeLastUpdate();
		}
		if (seconds > 0){
			//System.out.println(this.getClass().getSimpleName() + ": doMove2(seconds, timeAtStartOfMoveSeconds), seconds == "+seconds+", timeAtStartOfMoveSeconds == "+timeAtStartOfMoveSeconds);
			doMove(seconds, startTime);
		}
		for (int i = 0; i < players.size(); i++){
			players.get(i).afterLastUpdate();
		}
		nanosElapsed += nanos;
	}

	protected void doMove(double seconds, double startTime){
		float rotationSpeed = (float)(0.1*2*Math.PI);
		for (int i = 0; i < movingOccluders.size(); i++){
			movingOccluders.get(i).getPolygon().rotate((float)seconds*rotationSpeed);
		}
		for (int i = 0; i < players.size(); i++){
			players.get(i).update(seconds, startTime);
		}

		numObstacles = 0;
		numNodes = 0;
		numPermanentNodeConnections = 0;
		numTemporaryNodeConnections = 0;
		d_Bag<ga_PathBlockingObstacleImpl> obstacles = obstacleManager.getTileBag().getBag();
		numObstacles = obstacles.size();
		for (int i = 0; i < obstacles.size(); i++) {
			ga_PathBlockingObstacleImpl obst = obstacles.get(i);
			numNodes += obst.getNodes().size();
			for (int j = 0; j < obst.getNodes().size(); j++) {
				ga_PathNode node = obst.getNodes().get(j);
				numPermanentNodeConnections += node.getConnectedNodes().size();
				numTemporaryNodeConnections += node.getTempConnectedNodes().size();
			}
		}
		// divide numPermanentNodeConnections by 2 since connections are double-counted.
		numPermanentNodeConnections /= 2;
		// numTemporaryNodeConnections are not double-counted since the pathFinder's start and end nodes were not included in the count.

	}
	
	public d_Bag<ga_PathBlockingObstacleImpl> getObstacles() {
		return obstacleManager.getTileBag().getBag();
	}

	public double getTimeStampForEventNow() {
		double timeStamp = (System.nanoTime() - getSystemNanosAtStart()) / NANOS_IN_A_SECOND;
		return timeStamp;
	}

	public void setSystemNanosAtStart(long systemNanosAtStart){
		this.systemNanosAtStart = systemNanosAtStart;
	}
	public long getSystemNanosAtStart(){
		return systemNanosAtStart;
	}

	public LoopGame getLoop() {
		return loop;
	}

	public void setLoop(LoopGame loop) {
		if (this.loop != loop){
			this.loop = loop;
			loop.setWorld(this);
		}
	}

	public ga_ObstacleManager getObstacleManager() {
		return obstacleManager;
	}

	public ga_PathNodeConnector getNodeConnector() {
		return getObstacleManager().getNodeConnector();
	}

	public boolean isDrawNodeConnections() {
		return drawNodeConnections;
	}

	public void setDrawNodeConnections(boolean drawNodeConnections) {
		this.drawNodeConnections = drawNodeConnections;
	}

	public boolean isDrawGrid() {
		return drawGrid;
	}

	public void setDrawGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
	}

	public ArrayList<IMonster> getPlayers() {
		return players;
	}

	public int getNumNodes() {
		return numNodes;
	}

	public int getNumObstacles() {
		return numObstacles;
	}

	public int getNumPermanentNodeConnections() {
		return numPermanentNodeConnections;
	}

	public int getNumTemporaryNodeConnections() {
		return numTemporaryNodeConnections;
	}

	public ga_TileArray<ga_PathBlockingObstacleImpl> getObstacleTileArray() {
		return getObstacleManager().getTileBag().getTileArray();
	}
	
	public ga_TileArrayIntersections<ga_OccluderImpl> getOccluderTileArray() {
		return occluderTileArray;
	}

	public ArrayList<ga_Polygon> getOriginalPolygons() {
		return originalPolygons;
	}

	public ArrayList<ga_OccluderImpl> getMovingOccluders() {
		return movingOccluders;
	}

}
