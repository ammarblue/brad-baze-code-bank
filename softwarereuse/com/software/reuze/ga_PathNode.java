package com.software.reuze;
import java.util.ArrayList;


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

//import straightedge.geom.util.Tracker;
//import straightedge.geom.*;


/**
 *
 * @author Keith Woodward
 */
public class ga_PathNode implements Comparable{
	public ga_Vector2 point;
	public ArrayList<ga_PathNode> connectedNodes;
	public ga_PathNode parent;
	public float distToParent;
	public final static float G_COST_NOT_CALCULATED_FLAG = -Float.MAX_VALUE;
	public float gCost;		// distance to startPoint, via the parent nodes.
	public float hCost;		// distance straight to endPoint.
	public float fCost;		// gCost+hCost. This is what the the A* algorithm uses to sort the openList in PathFinder.
	public ArrayList<ga_PathNode> tempConnectedNodes;

	public int trackerID = -1;
	public long trackerCounter = -1;
	public int trackerStatus = UNPROCESSED;
	public static int UNPROCESSED = 100;
	public static int OPEN = 101;
	public static int CLOSED = 102;

	public ga_PathNode(){
		connectedNodes = new ArrayList<ga_PathNode>();
		tempConnectedNodes = new ArrayList<ga_PathNode>();
		gCost = G_COST_NOT_CALCULATED_FLAG;
	}

	public ga_PathNode(ga_Vector2 point){
		this();
		this.point = point;
	}

	public void clearForReuse(){
		clearConnectedNodes();
		clearTempConnectedNodes();
		gCost = G_COST_NOT_CALCULATED_FLAG;
		hCost = 0;
		fCost = 0;
		distToParent = 0;
		parent = null;
	}

	public ga_Vector2 getPoint() {
		return point;
	}

	public void setPoint(ga_Vector2 p) {
		this.point = p;
	}

	public ArrayList<ga_PathNode> getConnectedNodes() {
		return connectedNodes;
	}

	public void setConnectedNodes(ArrayList<ga_PathNode> connectedNodes) {
		this.connectedNodes = connectedNodes;
	}

	public void calcHCost(ga_PathNode endNode){
		hCost = point.dst(endNode.getPoint());
	}
	public float getHCost(){
		return hCost;
	}
	public void calcGCost(){
		if (parent == null){
			gCost = 0;
		}else{
			gCost = (this.getDistToParent() + getParent().getGCost());
		}
	}
	public void setGCost(float gCost){
		this.gCost = gCost;
	}
	public float getGCost(){
		return gCost;
	}

	public float getFCost() {
		assert parent != null;
		return fCost;
	}
	public void calcFCost(){
		fCost = getGCost() + getHCost();
	}

	ga_PathNode getParent() {
		return parent;
	}

	void setParent(ga_PathNode parent) {
		this.parent = parent;
		this.distToParent = this.getDistToParent();
	}

	float getDistToParent(){
		return point.dst(getParent().getPoint());
	}

	public ArrayList<ga_PathNode> getTempConnectedNodes() {
		return tempConnectedNodes;
	}
	public void clearConnectedNodes(){
		for (int k = connectedNodes.size()-1; k >= 0; k--){
			ArrayList<ga_PathNode> otherConnectedNodes = connectedNodes.get(k).getConnectedNodes();
			int index = otherConnectedNodes.indexOf(this);
			assert index != -1;
			otherConnectedNodes.remove(index);
		}
		connectedNodes.clear();
	}
	public void clearTempConnectedNodes(){
		for (int k = tempConnectedNodes.size()-1; k >= 0; k--){
			ArrayList<ga_PathNode> otherConnectedNodes = tempConnectedNodes.get(k).getTempConnectedNodes();
			boolean nodeRemoved = otherConnectedNodes.remove(this);
			assert nodeRemoved == true;
		}
		tempConnectedNodes.clear();
	}

	public int compareTo(Object node){
		assert node instanceof ga_PathNode : node;
		float otherNodeFCost = ((ga_PathNode)node).getFCost();
		if (fCost > otherNodeFCost){
			return 1;
		}else if (fCost < otherNodeFCost){
			return -1;
		}else{
			return 0;
		}
	}

	public void setPathFinderStatus(int trackerStatus, da_Tracker tracker){
		this.trackerStatus = trackerStatus;
		this.trackerCounter = tracker.getCounter();
		this.trackerID = tracker.getID();
	}

	public int getPathFinderStatus(da_Tracker tracker){
		if (this.trackerCounter == tracker.getCounter() && this.trackerID == tracker.getID()){
			return trackerStatus;
		}else{
			return UNPROCESSED;
		}
	}

}
