package com.software.reuze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
//package straightedge.geom.path;

//import straightedge.geom.*;
//import straightedge.geom.util.*;

/**
 * Finds a path through PathBlockingObstacles.
 *
 * Some code concepts from: http://www.policyalmanac.org/games/aStarTutorial.htm
 * Thanks to Jono and Nate (Nathan Sweet) from JavaGaming.org for their clever help.
 *
 * @author Keith Woodward
 *
 * @version 20 December 2008
 */

public class ga_PathFinder{
	public ga_PathNode startNode;
	public ga_PathNode endNode;
	public d_BufferPriority openList;

	// Tracker is used in conjunction with the PathNodes to detect if the Nodes are in the open or closed state.
	da_Tracker tracker = new da_Tracker();

	// for debugging only:
	public boolean debug = false;
	public ga_Vector2 startPointDebug;
	public ga_Vector2 endPointDebug;
	public ArrayList<ga_PathNode> startNodeTempReachableNodesDebug = new ArrayList<ga_PathNode>();
	public ArrayList<ga_PathNode> endNodeTempReachableNodesDebug = new ArrayList<ga_PathNode>();
	

	public ga_PathFinder(){
		openList = new d_BufferPriority();
		startNode = new ga_PathNode();
		endNode = new ga_PathNode();
	}

	public ga_PathData calc(ga_Vector2 start, ga_Vector2 end, float maxTempNodeConnectionDist, ga_PathNodeConnector nodeConnector, List obstacles){
		return calc(start, end, maxTempNodeConnectionDist, Float.MAX_VALUE, nodeConnector, obstacles);
	}


	/**
	 * @param start
	 * @param end
	 * @param maxTempNodeConnectionDist Maximum connection distance from start to obstacles and end to obstacles. The smaller the distance, the faster the algorithm.
	 * @param maxSearchDistStartToEnd Maximum distance from start to end. Any paths with a longer distance won't be returned. The smaller the value the faster the algorithm.
	 * @param nodeConnector
	 * @param obstacles
	 * @param pointList Path points from start to end. If there was no path found, or no path found with a distance less than maxSearchDistStartToEnd, an empty (zero-size) list will be returned.
	 * @return
	 */
	public ga_PathData calc(ga_Vector2 start, ga_Vector2 end, float maxTempNodeConnectionDist, float maxSearchDistStartToEnd, ga_PathNodeConnector nodeConnector, List obstacles){
		assert tempReachableNodesExist(obstacles) == false;
		float startToEndDist = start.dst(end);
		if (startToEndDist > maxSearchDistStartToEnd){
			// no point doing anything since startToEndDist is greater than maxSearchDistStartToEnd.
			ga_PathData pathData = new ga_PathData(ga_PathData.Result.ERROR1);
			return pathData;
		}
//		Vector2 midPoint = start.midPoint(end);
//		ArrayList obstacles = obstaclesTileArray.getAllWithin(midPoint, startToEndDist/2f);
//		assert tempReachableNodesExist(obstacles) == false;

		startNode.clearForReuse();
		startNode.setPoint(start);
		// Set startNode gCost to zero
		startNode.calcGCost();
		ga_PathNode currentNode = startNode;
		endNode.clearForReuse();
		endNode.setPoint(end);

		// Check for straight line path between start and end.
		// Note that this assumes start and end are not both contained in the same polygon.
		boolean intersection = false;
		ObstacleLoop:
		for (int i = 0; i < obstacles.size(); i++){
			ga_Polygon innerPolygon = ((ga_i_PathBlockingObstacle)obstacles.get(i)).getInnerPolygon();
			// Test if polygon intersects the line from start to end
			if (innerPolygon.intersectionPossible(start, end) && innerPolygon.intersectsLine(start, end)){
				intersection = true;
				break ObstacleLoop;
			}
		}
		if (intersection == false){
			// No intersections, so the straight-line path is fine!
			endNode.setParent(currentNode);
			ga_PathData pathData = this.makePathData();
			clearTempReachableNodes();
			tracker.incrementCounter();
			return pathData;
		}
		{
			// Connect the startNode to its reachable nodes and vice versa
			ArrayList<ga_PathNode> reachableNodes = nodeConnector.makeReachableNodesFor(startNode, maxTempNodeConnectionDist, obstacles);
			if (reachableNodes.size() == 0){
				// path from start node is not possible since there are no connections to it.
				ga_PathData pathData = new ga_PathData(ga_PathData.Result.ERROR2);
				clearTempReachableNodes();
				tracker.incrementCounter();
				return pathData;
			}
			startNode.getTempConnectedNodes().addAll(reachableNodes);
			for (int i = 0; i < reachableNodes.size(); i++){
				ga_PathNode node = reachableNodes.get(i);
				node.getTempConnectedNodes().add(startNode);
			}

			// Connect the endNode to its reachable nodes and vice versa
			reachableNodes = nodeConnector.makeReachableNodesFor(endNode, maxTempNodeConnectionDist, obstacles);
			if (reachableNodes.size() == 0){
				// path to end node is not possible since there are no connections to it.
				ga_PathData pathData = new ga_PathData(ga_PathData.Result.ERROR3);
				clearTempReachableNodes();
				tracker.incrementCounter();
				return pathData;
			}
			endNode.getTempConnectedNodes().addAll(reachableNodes);
			for (int i = 0; i < reachableNodes.size(); i++){
				ga_PathNode node = reachableNodes.get(i);
				node.getTempConnectedNodes().add(endNode);
			}
		}

		// Here we start the A* algorithm!
		openList.clear();
		while (true){
			// put the current node in the closedSet and take it out of the openList.
			currentNode.setPathFinderStatus(ga_PathNode.CLOSED, tracker);
			if (openList.size() != 0){
				openList.remove();
			}
			// add reachable nodes to the openList if they're not already there.
			ArrayList<ga_PathNode> reachableNodes = currentNode.getConnectedNodes();
			for (int i = 0; i < reachableNodes.size(); i++){
				ga_PathNode reachableNode = reachableNodes.get(i);
				if (reachableNode.getPathFinderStatus(tracker) == ga_PathNode.UNPROCESSED){
					reachableNode.setParent(currentNode);
					reachableNode.calcHCost(endNode);
					reachableNode.calcGCost();
					reachableNode.calcFCost();
					if (reachableNode.getFCost() <= maxSearchDistStartToEnd){
						openList.add(reachableNode);
						reachableNode.setPathFinderStatus(ga_PathNode.OPEN, tracker);
					}
				}else if (reachableNode.getPathFinderStatus(tracker) == ga_PathNode.OPEN){
					assert reachableNode.getGCost() != reachableNode.G_COST_NOT_CALCULATED_FLAG;
					float currentGCost = reachableNode.getGCost();
					float newGCost = currentNode.getGCost() + currentNode.getPoint().dst(reachableNode.getPoint());
					if (newGCost < currentGCost){
						reachableNode.setParent(currentNode);
						reachableNode.setGCost(newGCost);	//reachableNode.calcGCost();
						reachableNode.calcFCost();
						// Since the g-cost of the node has changed,
						// must re-sort the list to reflect this.
						openList.percolateUpMinHeap(openList.indexOf(reachableNode));
					}
				}
			}
			ArrayList<ga_PathNode> tempReachableNodes = currentNode.getTempConnectedNodes();
			for (int i = 0; i < tempReachableNodes.size(); i++){
				ga_PathNode reachableNode = tempReachableNodes.get(i);
				if (reachableNode.getPathFinderStatus(tracker) == ga_PathNode.UNPROCESSED){
					reachableNode.setParent(currentNode);
					reachableNode.calcHCost(endNode);
					reachableNode.calcGCost();
					reachableNode.calcFCost();
					if (reachableNode.getFCost() <= maxSearchDistStartToEnd){
						openList.add(reachableNode);
						reachableNode.setPathFinderStatus(ga_PathNode.OPEN, tracker);
					}
				}else if (reachableNode.getPathFinderStatus(tracker) == ga_PathNode.OPEN){
					assert reachableNode.getGCost() != reachableNode.G_COST_NOT_CALCULATED_FLAG;
					float currentGCost = reachableNode.getGCost();
					float newGCost = currentNode.getGCost() + currentNode.getPoint().dst(reachableNode.getPoint());
					if (newGCost < currentGCost){
						reachableNode.setParent(currentNode);
						reachableNode.setGCost(newGCost);	//reachableNode.calcGCost();
						reachableNode.calcFCost();
						// Since the g-cost of the node has changed,
						// must re-sort the list to reflect this.
						openList.percolateUpMinHeap(openList.indexOf(reachableNode));
					}
				}
			}
			if (openList.size() == 0){
				//System.out.println(this.getClass().getSimpleName()+": openList.size() == 0, returning");
				ga_PathData pathData = new ga_PathData(ga_PathData.Result.ERROR4);
				clearTempReachableNodes();
				tracker.incrementCounter();
				return pathData;
			}

			currentNode = (ga_PathNode)openList.get();
			if (currentNode == endNode){
				//System.out.println(this.getClass().getSimpleName()+": currentNode == endNode, returning");
				break;
			}
		}
		ga_PathData pathData = makePathData();
		clearTempReachableNodes();
		tracker.incrementCounter();
		return pathData;
	}


	public ga_PathData calc(ga_Vector2 start, ga_Vector2 end, float maxTempNodeConnectionDist, ga_PathNodeConnector nodeConnector, ga_TileBag obstaclesTileBag){
		return calc(start, end, maxTempNodeConnectionDist, Float.MAX_VALUE, nodeConnector, obstaclesTileBag.getTileArray());
	}

	public ga_PathData calc(ga_Vector2 start, ga_Vector2 end, float maxTempNodeConnectionDist, float maxSearchDistStartToEnd, ga_PathNodeConnector nodeConnector, ga_TileBag obstaclesTileBag){
		return calc(start, end, maxTempNodeConnectionDist, maxSearchDistStartToEnd, nodeConnector, obstaclesTileBag.getTileArray());
	}

	public ga_PathData calc(ga_Vector2 start, ga_Vector2 end, float maxTempNodeConnectionDist, ga_PathNodeConnector nodeConnector, ga_TileArray obstaclesTileArray){
		return calc(start, end, maxTempNodeConnectionDist, Float.MAX_VALUE, nodeConnector, obstaclesTileArray);
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @param maxTempNodeConnectionDist Maximum connection distance from start to obstacles and end to obstacles. The smaller the distance, the faster the algorithm.
	 * @param maxSearchDistStartToEnd Maximum distance from start to end. Any paths with a longer distance won't be returned. The smaller the value the faster the algorithm. Use Double.MAX_VALUE to have a very large search dist.
	 * @param nodeConnector
	 * @param obstaclesTileArray
	 * @param pointList Path points from start to end. If there was no path found, or no path found with a distance less than maxSearchDistStartToEnd, an empty (zero-size) list will be returned.
	 * @return
	 */
	public ga_PathData calc(ga_Vector2 start, ga_Vector2 end, float maxTempNodeConnectionDist, float maxSearchDistStartToEnd, ga_PathNodeConnector nodeConnector, ga_TileArray obstaclesTileArray){
		float startToEndDist = start.dst(end);
		if (startToEndDist > maxSearchDistStartToEnd){
			// no point doing anything since startToEndDist is greater than maxSearchDistStartToEnd.
			ga_PathData pathData = new ga_PathData(ga_PathData.Result.ERROR1);
			return pathData;
		}
		ga_Vector2 midPoint = start.midPoint(end);
		ArrayList obstacles = obstaclesTileArray.getAllWithin(midPoint, startToEndDist/2f);
		assert tempReachableNodesExist(obstacles) == false;

		startNode.clearForReuse();
		startNode.setPoint(start);
		// Set startNode gCost to zero
		startNode.calcGCost();
		ga_PathNode currentNode = startNode;
		endNode.clearForReuse();
		endNode.setPoint(end);

		// Check for straight line path between start and end.
		// Note that this assumes start and end are not both contained in the same polygon.
		boolean intersection = false;
		ObstacleLoop:
		for (int i = 0; i < obstacles.size(); i++){
			ga_Polygon innerPolygon = ((ga_i_PathBlockingObstacle)obstacles.get(i)).getInnerPolygon();
			// Test if polygon intersects the line from start to end
			if (innerPolygon.intersectionPossible(start, end) && innerPolygon.intersectsLine(start, end)){
				intersection = true;
				break ObstacleLoop;
			}
		}
		if (intersection == false){
			// No intersections, so the straight-line path is fine!
			endNode.setParent(currentNode);
			ga_PathData pathData = this.makePathData();
			clearTempReachableNodes();
			tracker.incrementCounter();
			return pathData;
		}
		{
			// Connect the startNode to its reachable nodes and vice versa
			ArrayList<ga_PathNode> reachableNodes = nodeConnector.makeReachableNodesFor(startNode, maxTempNodeConnectionDist, obstaclesTileArray);
			if (reachableNodes.size() == 0){
				// path from start node is not possible since there are no connections to it.
				ga_PathData pathData = new ga_PathData(ga_PathData.Result.ERROR2);
				clearTempReachableNodes();
				tracker.incrementCounter();
				return pathData;
			}
			startNode.getTempConnectedNodes().addAll(reachableNodes);
			for (int i = 0; i < reachableNodes.size(); i++){
				ga_PathNode node = reachableNodes.get(i);
				node.getTempConnectedNodes().add(startNode);
			}

			// Connect the endNode to its reachable nodes and vice versa
			reachableNodes = nodeConnector.makeReachableNodesFor(endNode, maxTempNodeConnectionDist, obstaclesTileArray);
			if (reachableNodes.size() == 0){
				// path to end node is not possible since there are no connections to it.
				ga_PathData pathData = new ga_PathData(ga_PathData.Result.ERROR3);
				clearTempReachableNodes();
				tracker.incrementCounter();
				return pathData;
			}
			endNode.getTempConnectedNodes().addAll(reachableNodes);
			for (int i = 0; i < reachableNodes.size(); i++){
				ga_PathNode node = reachableNodes.get(i);
				node.getTempConnectedNodes().add(endNode);
			}
		}

		// Here we start the A* algorithm!
		openList.clear();
		while (true){
			// put the current node in the closedSet and take it out of the openList.
			currentNode.setPathFinderStatus(ga_PathNode.CLOSED, tracker);
			if (openList.size() != 0){
				openList.remove();
			}
			// add reachable nodes to the openList if they're not already there.
			ArrayList<ga_PathNode> reachableNodes = currentNode.getConnectedNodes();
			for (int i = 0; i < reachableNodes.size(); i++){
				ga_PathNode reachableNode = reachableNodes.get(i);
				if (reachableNode.getPathFinderStatus(tracker) == ga_PathNode.UNPROCESSED){
					reachableNode.setParent(currentNode);
					reachableNode.calcHCost(endNode);
					reachableNode.calcGCost();
					reachableNode.calcFCost();
					if (reachableNode.getFCost() <= maxSearchDistStartToEnd){
						openList.add(reachableNode);
						reachableNode.setPathFinderStatus(ga_PathNode.OPEN, tracker);
					}
				}else if (reachableNode.getPathFinderStatus(tracker) == ga_PathNode.OPEN){
					assert reachableNode.getGCost() != ga_PathNode.G_COST_NOT_CALCULATED_FLAG;
					float currentGCost = reachableNode.getGCost();
					float newGCost = currentNode.getGCost() + currentNode.getPoint().dst(reachableNode.getPoint());
					if (newGCost < currentGCost){
						reachableNode.setParent(currentNode);
						reachableNode.setGCost(newGCost);	//reachableNode.calcGCost();
						reachableNode.calcFCost();
						// Since the g-cost of the node has changed,
						// must re-sort the list to reflect this.
						openList.percolateUpMinHeap(openList.indexOf(reachableNode));
					}
				}
			}
			ArrayList<ga_PathNode> tempReachableNodes = currentNode.getTempConnectedNodes();
			for (int i = 0; i < tempReachableNodes.size(); i++){
				ga_PathNode reachableNode = tempReachableNodes.get(i);
				if (reachableNode.getPathFinderStatus(tracker) == ga_PathNode.UNPROCESSED){
					reachableNode.setParent(currentNode);
					reachableNode.calcHCost(endNode);
					reachableNode.calcGCost();
					reachableNode.calcFCost();
					if (reachableNode.getFCost() <= maxSearchDistStartToEnd){
						openList.add(reachableNode);
						reachableNode.setPathFinderStatus(ga_PathNode.OPEN, tracker);
					}
				}else if (reachableNode.getPathFinderStatus(tracker) == ga_PathNode.OPEN){
					assert reachableNode.getGCost() != ga_PathNode.G_COST_NOT_CALCULATED_FLAG;
					float currentGCost = reachableNode.getGCost();
					float newGCost = currentNode.getGCost() + currentNode.getPoint().dst(reachableNode.getPoint());
					if (newGCost < currentGCost){
						reachableNode.setParent(currentNode);
						reachableNode.setGCost(newGCost);	//reachableNode.calcGCost();
						reachableNode.calcFCost();
						// Since the g-cost of the node has changed,
						// must re-sort the list to reflect this.
						openList.percolateUpMinHeap(openList.indexOf(reachableNode));
					}
				}
			}
			if (openList.size() == 0){
				//System.out.println(this.getClass().getSimpleName()+": openList.size() == 0, returning");
				ga_PathData pathData = new ga_PathData(ga_PathData.Result.ERROR4);
				clearTempReachableNodes();
				tracker.incrementCounter();
				return pathData;
			}

			currentNode = (ga_PathNode)openList.get();
			if (currentNode == endNode){
				//System.out.println(this.getClass().getSimpleName()+": currentNode == endNode, returning");
				break;
			}
		}
		ga_PathData pathData = makePathData();
		clearTempReachableNodes();
		tracker.incrementCounter();
		return pathData;
	}

	protected void clearTempReachableNodes(){
		if (debug){
			startPointDebug = startNode.getPoint().copy();
			endPointDebug = endNode.getPoint().copy();
			startNodeTempReachableNodesDebug.clear();
			endNodeTempReachableNodesDebug.clear();
			startNodeTempReachableNodesDebug.addAll(startNode.getTempConnectedNodes());
			endNodeTempReachableNodesDebug.addAll(endNode.getTempConnectedNodes());
		}

		// Erase all nodes' tempConnectedNodes
		if (startNode != null){
			startNode.clearTempConnectedNodes();
		}
		if (endNode != null){
			endNode.clearTempConnectedNodes();
		}
	}

	ArrayList<ga_PathNode> nodes = new ArrayList<ga_PathNode>();
	ArrayList<ga_Vector2> points = new ArrayList<ga_Vector2>();
	protected ga_PathData makePathData(){
		ga_PathNode currentNode = getEndNode();
		while (true){
			nodes.add(currentNode);
			points.add(currentNode.getPoint());
			ga_PathNode parentNode = currentNode.getParent();
			if (parentNode == null){
				break;
			}
			currentNode = parentNode;
		}
		Collections.reverse(nodes);
		Collections.reverse(points);
		ga_PathData pathData = new ga_PathData(points, nodes);
		nodes.clear();
		points.clear();
		return pathData;
	}

	public boolean pathExists(){
		if (getEndNode() != null && getEndNode().getParent() != null){
			return true;
		}
		return false;
	}

	public ga_PathNode getEndNode() {
		return endNode;
	}

	public ga_PathNode getStartNode() {
		return startNode;
	}

	// used only for assertion checks
	protected boolean tempReachableNodesExist(List obstacles){
		for (int i = 0; i < obstacles.size(); i++){
			ga_i_PathBlockingObstacle obst = (ga_i_PathBlockingObstacle)obstacles.get(i);
			for (int j = 0; j < obst.getNodes().size(); j++){
				ga_PathNodeOfObstacle node = obst.getNodes().get(j);
				if (node.getTempConnectedNodes().size() > 0){
					return true;
				}
			}
		}
		return false;
	}
}
