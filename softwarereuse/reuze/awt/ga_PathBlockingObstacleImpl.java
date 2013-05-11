package reuze.awt;
import java.util.ArrayList;

import com.software.reuze.ga_PathNodeOfObstacle;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;
import com.software.reuze.ga_i_PathBlockingObstacle;



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

//import straightedge.geom.PolygonBufferer;
//import straightedge.geom.*;

/**
 *
 * @author Keith Woodward
 */
public class ga_PathBlockingObstacleImpl implements ga_i_PathBlockingObstacle{
	public static float BUFFER_AMOUNT = 0.01f;
	public static int NUM_POINTS_IN_A_QUADRANT = 1;

	public ga_Polygon outerPolygon;
	public ga_Polygon innerPolygon;
	public ArrayList<ga_PathNodeOfObstacle> nodes;
	
	public ga_PathBlockingObstacleImpl(){
	}
	
	public ga_PathBlockingObstacleImpl(ga_Polygon outerPolygon, ga_Polygon innerPolygon){
		this.outerPolygon = outerPolygon;
		this.innerPolygon = innerPolygon;
		resetNodes();
		assert outerPolygon.intersectsPerimeter(innerPolygon) == false : "\n"+outerPolygon.toString()+"\n"+innerPolygon.toString();
		
	}
	public void resetNodes(){
		if (nodes == null){
			nodes = new ArrayList<ga_PathNodeOfObstacle>();
			for (int i = 0; i < this.outerPolygon.getPoints().size(); i++){
				nodes.add(new ga_PathNodeOfObstacle(this, i));
			}
		}else if (nodes.size() != getOuterPolygon().getPoints().size()){
			nodes.clear();
			for (int i = 0; i < this.outerPolygon.getPoints().size(); i++){
				nodes.add(new ga_PathNodeOfObstacle(this, i));
			}
		}else{
			for (int j = 0; j < nodes.size(); j++){
				ga_PathNodeOfObstacle node = nodes.get(j);
				ga_Vector2 outerPolygonPoint = getOuterPolygon().getPoint(j);
				node.getPoint().x = outerPolygonPoint.x;
				node.getPoint().y = outerPolygonPoint.y;
			}
		}
	}

	public static ga_PathBlockingObstacleImpl createObstacleFromOuterPolygon(ga_Polygon outerPolygon){
		ga_PolygonBufferer polygonBufferer = new ga_PolygonBufferer();
		ga_Polygon innerPolygon = polygonBufferer.buffer(outerPolygon, -1*BUFFER_AMOUNT, NUM_POINTS_IN_A_QUADRANT);
		if (innerPolygon == null){
			// there was an error so return null;
			return null;
		}
		ga_PathBlockingObstacleImpl pathBlockingObstacleImpl = new ga_PathBlockingObstacleImpl(outerPolygon, innerPolygon);
		return pathBlockingObstacleImpl;
	}

	public static ga_PathBlockingObstacleImpl createObstacleFromInnerPolygon(ga_Polygon innerPolygon){
		ga_PolygonBufferer polygonBufferer = new ga_PolygonBufferer();
		ga_Polygon outerPolygon = polygonBufferer.buffer(innerPolygon, BUFFER_AMOUNT, NUM_POINTS_IN_A_QUADRANT);
		if (outerPolygon == null){
			// there was an error so return null;
			return null;
		}
		ga_PathBlockingObstacleImpl pathBlockingObstacleImpl = new ga_PathBlockingObstacleImpl(outerPolygon, innerPolygon);
		return pathBlockingObstacleImpl;
	}
	
	public ArrayList<ga_PathNodeOfObstacle> getNodes() {
		return nodes;
	}

	public ga_Polygon getOuterPolygon() {
		return outerPolygon;
	}

	public ga_Polygon getInnerPolygon() {
		return innerPolygon;
	}

	public ga_Polygon getPolygon(){
		return this.getInnerPolygon();
	}

	public void setOuterPolygon(ga_Polygon outerPolygon){
		this.outerPolygon = outerPolygon;
	}

	public void setInnerPolygon(ga_Polygon innerPolygon){
		this.innerPolygon = innerPolygon;
	}
}
