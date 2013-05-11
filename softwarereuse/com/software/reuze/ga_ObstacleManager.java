package com.software.reuze;
import java.util.ArrayList;
import java.util.Collection;


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


//import straightedge.geom.util.*;
//import straightedge.geom.*;

/**
 *
 * @author Keith
 */
public class ga_ObstacleManager<T extends ga_i_PathBlockingObstacle>{
	public ga_TileBag<T> tileBag;
	public ga_PathNodeConnector<T> nodeConnector;
	public float maxConnectionDistance;

	public ga_ObstacleManager(ga_TileBag<T> tileBag, float maxConnectionDistance){
		this.tileBag = tileBag;
		this.maxConnectionDistance = maxConnectionDistance;
		nodeConnector = new ga_PathNodeConnector<T>();
	}

	public void addObstacles(Collection<T> newObstacles){
		// This method re-adds the obstacleList one by one which can be faster
		// since nodes are only checked against each other once rather than twice.
		for (T obst : newObstacles){
			this.addObstacle(obst);
		}
	}
	

	public void addObstacle(T obst){
		tileBag.add(obst);
		nodeConnector.addObstacle(obst, tileBag, maxConnectionDistance);
	}

	public void removeObstacle(T obst){
		long startTime = System.nanoTime();
		nodeConnector.clearConnectionsToRemovedObstacleNodes(obst);
		tileBag.remove(obst);
		ga_Polygon poly = obst.getInnerPolygon();
		ArrayList<T> nearByObstacles = tileBag.getAllWithin(poly.getCenter(), poly.getRadius() + maxConnectionDistance);
		// Any nodes that may have been contained but now aren't need to be marked as so.
		for (T nearByObstacle : nearByObstacles){
			for (ga_PathNodeOfObstacle node : nearByObstacle.getNodes()){
				if (poly.getCenter().dst(node.getPoint()) <= poly.getRadius()){
					node.resetContainedToUnknown();
				}
			}
		}

		ArrayList<ga_PathNodeOfObstacle> nodesToBeReconnected = new ArrayList<ga_PathNodeOfObstacle>();
		for (T nearByObstacle : nearByObstacles){
			for (ga_PathNodeOfObstacle node : nearByObstacle.getNodes()){
				if (node.getPoint().dst(poly.getCenter()) < maxConnectionDistance + poly.getRadius()){
					nodesToBeReconnected.add(node);
				}
			}
		}
		nodeConnector.reConnectNodesAroundRemovedObstacle(obst, nodesToBeReconnected, nearByObstacles, maxConnectionDistance);

		long endTime = System.nanoTime();
		System.out.println(this.getClass().getSimpleName()+".removeObstacle running time = "+((endTime - startTime)/1000000000f));
	}

	public void remakeConnectionsBetweenAllObstacles(float maxConnectionDistance){
		this.maxConnectionDistance = maxConnectionDistance;
		// This method re-adds the obstacleList one by one which can be faster
		// since nodes are only checked against each other once rather than twice.
		d_Bag<T> copyOfObstacles = new d_Bag<T>(tileBag.size());
		copyOfObstacles.addAll(tileBag);
		tileBag.clear();

		long startTime = System.nanoTime();
		for (T obst : copyOfObstacles){
			this.addObstacle(obst);
		}
		long endTime = System.nanoTime();
		System.out.println(this.getClass().getSimpleName()+".remakeConnectionsBetweenAllObstacles addObstacle running time = "+((endTime - startTime)/1000000000f));
	}

	public ga_TileBag<T> getTileBag() {
		return tileBag;
	}

	public double getMaxConnectionDistance() {
		return maxConnectionDistance;
	}

	public ga_PathNodeConnector<T> getNodeConnector() {
		return nodeConnector;
	}


}
