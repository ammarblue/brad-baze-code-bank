package reuze.pending;

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



import com.software.reuze.ga_AABB;
import com.software.reuze.ga_OccluderImpl;
import com.software.reuze.ga_PathNode;
import com.software.reuze.ga_PathNodeConnector;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_TileBag;
import com.software.reuze.ga_TileBagIntersections;
import com.software.reuze.ga_Vector2;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Random;
/*import straightedge.geom.AABB;
import straightedge.geom.PolygonMulti;
import straightedge.geom.Vector2;
import straightedge.geom.Polygon;
import straightedge.geom.PolygonConverter;
import straightedge.geom.path.NodeConnector;
import straightedge.geom.path.PathBlockingObstacleImpl;
import straightedge.geom.util.TileBag;
import straightedge.geom.vision.OccluderImpl;*/
/*import straightedge.geom.path.KNode;
import straightedge.geom.util.TileBagIntersections;*/
import java.util.Collections;

import reuze.awt.ga_PathBlockingObstacleImpl;
import reuze.awt.ga_PolygonConverterJts;
import reuze.awt.ga_PolygonMultiAwt;



/**
 *
 * @author Keith
 */
public class World {

	long systemNanosAtStart;
	long nanosElapsed;
	static double NANOS_IN_A_SECOND = 1000000000;
	boolean pause = false;

	public void setSystemNanosAtStart(long systemNanosAtStart){
		this.systemNanosAtStart = systemNanosAtStart;
	}
	public long getSystemNanosAtStart(){
		return systemNanosAtStart;
	}

	public void update(long nanos){
		double seconds = nanos/NANOS_IN_A_SECOND;
		double startTime = nanosElapsed/NANOS_IN_A_SECOND;
		assert seconds >= 0 : seconds;
		main.getEventHandler().eventCache.clearAndFillCache();
		ArrayList<AWTEventWrapper> events = main.getEventHandler().eventCache.getEventsList();
		Collections.sort(events);
		for (int i = 0; i < events.size(); i++){
			AWTEventWrapper ev = events.get(i);
			main.getEventHandler().processEvent(ev);
		}
		if (pause == false){
			doMove(seconds, startTime);
			nowAtTimeStop(startTime + seconds);
			nanosElapsed += nanos;
		}else{
			doMove(0, startTime);
			nowAtTimeStop(startTime + 0);
		}

	}


	public Random random;
	public ArrayList<ga_PolygonMultiAwt> allMultiPolygons;
	public ga_TileBagIntersections<ga_OccluderImpl> allOccluders;

	public ga_TileBag<ga_PathBlockingObstacleImpl> allObstacles;
	public ga_AABB originalScreenAABB;
	public ga_AABB innerAABB;
	public ga_AABB obstaclesAABB;
	public ga_AABB enemySpawnAABB;
	public ga_AABB playerSpawnAABB;

	public ga_PathNodeConnector nodeConnector;
	public float maxConnectionDistance;

	public PlayerTarget player;
	public ArrayList<PlayerTarget> enemies;
	public ArrayList<Bullet> bullets;

	public boolean makeFromOuterPolygon = false;
	public double obstacleBufferAmount = 5;
	public int numPointsPerQuadrant = 1;

	public demoGameShooterMain main;

	public World(demoGameShooterMain main){
		this.main = main;
	}

	// this method should be over-ridden by sub-classes
	public void fillMultiPolygonsList(){
		Container cont = main.getParentFrameOrApplet();
		double contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		double contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);
		ArrayList<ga_Polygon> polygons = new ArrayList<ga_Polygon>();
		
		// make stars
		for (int i = 0; i < 5; i++){
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			int numPoints = 4 + random.nextInt(4)*2;
			double angleIncrement = Math.PI*2f/(numPoints*2);
			float rBig = 40 + random.nextFloat()*90;
			float rSmall = 20 + random.nextFloat()*70;
			double currentAngle = 0;
			for (int k = 0; k < numPoints; k++){
				double x = rBig*Math.cos(currentAngle);
				double y = rBig*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
				x = rSmall*Math.cos(currentAngle);
				y = rSmall*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
			}
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			//poly.translate(20 + (float)random.nextFloat()*aabb.getWidth(), 20 + (float)random.nextFloat()*aabb.getHeight());
			ga_Vector2 p = new ga_Vector2(innerAABB.p.x + random.nextFloat()*innerAABB.getWidth(), innerAABB.p.y + random.nextFloat()*innerAABB.getHeight());
			poly.translateTo(p);
			polygons.add(poly);
		}
		for (int i = 0; i < polygons.size(); i++){
			ga_PolygonMultiAwt multiPolygon = new ga_PolygonMultiAwt(polygons.get(i).getPolygon().copy());
			allMultiPolygons.add(multiPolygon);
		}
	}

	public void init(){
		Container cont = main.getParentFrameOrApplet();
		
		random = new Random();
		allMultiPolygons = new ArrayList<ga_PolygonMultiAwt>();
		
		int insets = 20;
		float contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		float contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);
		originalScreenAABB = ga_AABB.createFromDiagonal(0f,0f,contW,contH);
		innerAABB = ga_AABB.createFromDiagonal(insets, insets, contW - 2*insets, contH - 2*insets);
		float spawnWH = 150;
		enemySpawnAABB = new ga_AABB(insets, insets, spawnWH+insets, spawnWH+insets);
		playerSpawnAABB = new ga_AABB(contW - (spawnWH+insets), contH - (spawnWH+insets), contW - insets, contH - insets);

		fillMultiPolygonsList();

		ArrayList<ga_OccluderImpl> tempOccludersList = new ArrayList<ga_OccluderImpl>();
		for (int i = 0; i < allMultiPolygons.size(); i++){
			ga_Polygon poly = allMultiPolygons.get(i).getExteriorPolygon().copy();
			ga_OccluderImpl occluder = new ga_OccluderImpl(poly);
			tempOccludersList.add(occluder);
		}
		allOccluders = new ga_TileBagIntersections(tempOccludersList, 50);
		allOccluders.addAll(tempOccludersList);

		ArrayList<ga_PathBlockingObstacleImpl> tempStationaryObstacles = new ArrayList<ga_PathBlockingObstacleImpl>();
		ga_PolygonConverterJts pc = new ga_PolygonConverterJts();
		for (int i = 0; i < allMultiPolygons.size(); i++){
			ga_Polygon poly = allMultiPolygons.get(i).getExteriorPolygon().copy();
			com.vividsolutions.jts.geom.Polygon jtsPolygon = pc.makeJTSPolygonFrom(poly);
			Geometry bufferedJTSPolygon = jtsPolygon.buffer(obstacleBufferAmount, numPointsPerQuadrant);
			ga_Polygon bufferedPoly = pc.makeMultiPolygonListFrom(bufferedJTSPolygon).get(0).getExteriorPolygon();
			ga_PathBlockingObstacleImpl obst = ga_PathBlockingObstacleImpl.createObstacleFromInnerPolygon(bufferedPoly);
			if (obst == null){
				continue;
			}
			tempStationaryObstacles.add(obst);
		}
//		// Here, we combine all allObstacles that are touching. Note that this leads
//		// to sub-optimal performance when you've got just one massive obstacle
//		// because then the obstacle-sorting optimizations in the path finding code don't work.
//		// This affects the WorldMaze in particular...
//		ArrayList<com.vividsolutions.jts.geom.Polygon> allJTSPolygons = new ArrayList<com.vividsolutions.jts.geom.Polygon>();
//		PolygonConverter pc = new PolygonConverter();
//		for (int i = 0; i < allMultiPolygons.size(); i++){
//			Polygon poly = allMultiPolygons.get(i).getExteriorPolygon().copy();
//			com.vividsolutions.jts.geom.Polygon jtsPolygon = pc.makeJTSPolygonFrom(poly);
//			allJTSPolygons.add(jtsPolygon);
//		}
//		com.vividsolutions.jts.geom.Polygon[] jtsPolygonArray = allJTSPolygons.toArray(new com.vividsolutions.jts.geom.Polygon[allJTSPolygons.size()]);
//		com.vividsolutions.jts.geom.MultiPolygon jtsMultiPolygon = new com.vividsolutions.jts.geom.MultiPolygon(jtsPolygonArray, new com.vividsolutions.jts.geom.GeometryFactory());
//		com.vividsolutions.jts.geom.Geometry bufferedGeometry = jtsMultiPolygon.buffer(obstacleBufferAmount, numPointsPerQuadrant);
//		ArrayList<PolygonMulti> multiPolygons = pc.makePolygonMultiListFrom(bufferedGeometry);
//		ArrayList<Polygon> bufferedPolygons = new ArrayList<Polygon>();
//		for (int i = 0; i < multiPolygons.size(); i++){
//			PolygonMulti mpoly = multiPolygons.get(i);
//			bufferedPolygons.add(mpoly.getExteriorPolygon());
//		}
//		ArrayList<PathBlockingObstacleImpl> tempStationaryObstacles = new ArrayList<PathBlockingObstacleImpl>();
//		for (int i = 0; i < bufferedPolygons.size(); i++){
//			Polygon poly = bufferedPolygons.get(i);
//			PathBlockingObstacleImpl obst = PathBlockingObstacleImpl.createObstacleFromInnerPolygon(poly);
//			if (obst == null){
//				continue;
//			}
//			tempStationaryObstacles.add(obst);
//		}

		maxConnectionDistance = 700f;
		nodeConnector = new ga_PathNodeConnector();
		obstaclesAABB = ga_AABB.getAABBEnclosingCenterAndRadius(tempStationaryObstacles.toArray());
		allObstacles = new ga_TileBag(obstaclesAABB, 50);

		for (int i = 0; i < tempStationaryObstacles.size(); i++){
			ga_PathBlockingObstacleImpl obst = tempStationaryObstacles.get(i);
			allObstacles.add(obst);
			nodeConnector.addObstacle(obst, allObstacles, maxConnectionDistance);
		}
		
		ga_Vector2 spawnPoint = getNearestPointOutsideOfObstacles(makeRandomPointWithin(playerSpawnAABB));
		player = new PlayerTarget(this, spawnPoint);
		player.cache.originalBoundaryPolygon.scale(3, player.cache.originalEye);
		player.cache.reset();
		player.makeImage();
		player.makeImage2();

		enemies = new ArrayList<PlayerTarget>();
		changeNumEnemies(3);
		bullets = new ArrayList<Bullet>();
		
		
	}

	public void changeNumEnemies(int numEnemies){
		if (enemies.size() == numEnemies){
			return;
		}
		while (enemies.size() > numEnemies){
			enemies.remove(enemies.size()-1);
		}
		while (enemies.size() < numEnemies){
			ga_Vector2 spawnPoint = getNearestPointOutsideOfObstacles(makeRandomPointWithin(enemySpawnAABB));
			PlayerTarget enemy = new PlayerTarget(this, spawnPoint);
			enemy.cache.originalBoundaryPolygon.scale(0.75f + random.nextFloat()*0.5f, 0.75f + random.nextFloat()*0.5f, enemy.cache.originalEye);
			enemy.cache.reset();
			enemy.makeImage();
			enemy.gun.rotationSpeed *= 0.5;
			enemies.add(enemy);
		}
	}

	public void doMove(double seconds, double startTime){
		ga_Vector2 botLeft = this.obstaclesAABB.getBotLeft();
		ga_Vector2 topRight = this.obstaclesAABB.getTopRight();
		float worldEdgeDistance = 1000;
		ga_AABB worldBounds = ga_AABB.createFromDiagonal(botLeft.x - worldEdgeDistance,
											botLeft.y - worldEdgeDistance,
											topRight.x + worldEdgeDistance,
											topRight.y + worldEdgeDistance);
		for (int i = 0; i < bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			bullet.doMove(seconds, startTime);
			// if the bullet is outside of the world's bounds, remove it.
			if (worldBounds.contains((float)bullet.x, (float)bullet.y) == false){
				bullets.remove(i);
				i--;
			}
		}
		
		for (int i = 0; i < enemies.size(); i++){
			PlayerTarget enemy = enemies.get(i);
			enemy.doMove(seconds, startTime);
		}
		player.doMove(seconds, startTime);

		// Note that dead bullets need to be eliminated after player.doMove method
		// since when a bullet is fired by the player, bullet.doMove is called.
		for (int j = 0; j < bullets.size(); j++) {
			if (bullets.get(j).dead == true) {
				bullets.remove(j);
				j--;
			}
		}

		//countStats();
	}

	public int numObstacles;
	public int numNodes;
	public int numPermanentNodeConnections;
	public int numTemporaryNodeConnections;
	public void countStats(){
		// Counts number of obstacles, nodes, connections
		numObstacles = 0;
		numNodes = 0;
		numPermanentNodeConnections = 0;
		numTemporaryNodeConnections = 0;
		numObstacles = allObstacles.size();
		for (int i = 0; i < allObstacles.size(); i++) {
			ga_PathBlockingObstacleImpl obst = allObstacles.get(i);
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
		System.out.println(this.getClass().getSimpleName()+": numObstacles == "+numObstacles);
		System.out.println(this.getClass().getSimpleName()+": numNodes == "+numNodes);
		System.out.println(this.getClass().getSimpleName()+": numTemporaryNodeConnections == "+numTemporaryNodeConnections);
		System.out.println(this.getClass().getSimpleName()+": numPermanentNodeConnections == "+numPermanentNodeConnections);
	}

	protected void nowAtTimeStop(double timeNow) {
		player.nowAtTimeStop(timeNow);
		for (int i = 0; i < enemies.size(); i++){
			PlayerTarget enemy = enemies.get(i);
			enemy.nowAtTimeStop(timeNow);
		}
	}

	public ga_Vector2 makeRandomPointWithin(ga_AABB aabb){
		return new ga_Vector2(aabb.getX() + random.nextDouble()*aabb.w(), aabb.getY() + random.nextDouble()*aabb.h());
	}
	public ga_Vector2 getNearestPointOutsideOfObstacles(ga_Vector2 point){
		// check that the target point isn't inside any allObstacles.
		// if so, move it.
		ga_Vector2 movedPoint = point.copy();
		boolean targetIsInsideObstacle = false;
		int count = 0;
		while (true){
			for (ga_PathBlockingObstacleImpl obst : allObstacles.getBag()){
				if (obst.getOuterPolygon().contains(movedPoint)){
					targetIsInsideObstacle = true;
					ga_Polygon poly = obst.getOuterPolygon();
					ga_Vector2 p = poly.getBoundaryPointClosestTo(movedPoint);
					if (p != null){
						movedPoint.x = p.x;
						movedPoint.y = p.y;
					}
					assert point != null;
				}
			}
			count++;
			if (targetIsInsideObstacle == false || count >= 3){
				break;
			}
		}
		return movedPoint;
	}
}
