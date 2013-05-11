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
//package straightedge.geom.vision;

//import straightedge.geom.*;

public class ga_VisionData {
	// The inputs to the VisionFinder.calc method
	public ga_Vector2 eye;
	public ga_Polygon boundaryPolygon;

	// The last calculated results of the VisionFinder.calc method
	public ArrayList<ga_VPVisiblePoint> visiblePoints;
	public ga_Polygon visiblePolygon;

	// These are some variables that can be re-used to prevent excess garbage for the garbage collector.
	// Note that these values can be re-used when the eye and the boundaryPolygon are
	// translated by the same coordinates.
	// Otherwise these variables need to be re-initialized by calling reset()
	// before this VisionData is passed to the VisibilityFinder.calc method.
	public int[] boundaryPolygonXIndicators;
	public int[] boundaryPolygonYIndicators;
	public float maxEyeToBoundaryPolygonPointDist = -1;
	public float minEyeToBoundaryPolygonPointDist = Float.MAX_VALUE;
	public float maxEyeToBoundaryPolygonPointDistSq = -1;
	public float minEyeToBoundaryPolygonPointDistSq = Float.MAX_VALUE;
	//public float[] boundaryPolygonPointAngles;

	protected ga_VisionData(){
	}

	public ga_VisionData(ga_Vector2 eye, ga_Polygon boundaryPolygon){
		reset(eye, boundaryPolygon);
	}
	public void reset(ga_Vector2 eye, ga_Polygon boundaryPolygon){
		this.eye = eye;
		this.boundaryPolygon = boundaryPolygon;
		visiblePoints = null;
		visiblePolygon = null;

		ArrayList<ga_Vector2> boundaryPolygonPoints = boundaryPolygon.getPoints();
//		if (boundaryPolygonPointAngles == null || boundaryPolygonPointAngles.length != boundaryPolygonPoints.size()){
//			boundaryPolygonPointAngles = new float[boundaryPolygonPoints.size()];
//		}
		//sightPolygonPointToEyeDists = new float[points.size()];
		for (int i = 0; i < boundaryPolygonPoints.size(); i++){
			int iPlus = (i+1 >= boundaryPolygonPoints.size() ? 0 : i+1);
			float distSq = eye.dst2(boundaryPolygonPoints.get(i));
			//sightPolygonPointToEyeDists[i] = dist;
			if (distSq > maxEyeToBoundaryPolygonPointDistSq){
				maxEyeToBoundaryPolygonPointDistSq = distSq;
			}
			float ptSegDistSq = eye.ptSegDistSq(boundaryPolygonPoints.get(i), boundaryPolygonPoints.get(iPlus));
			if (ptSegDistSq < minEyeToBoundaryPolygonPointDistSq){
				minEyeToBoundaryPolygonPointDistSq = ptSegDistSq;
			}
			//boundaryPolygonPointAngles[i] = eye.findAngle(boundaryPolygonPoints.get(i));
		}
		maxEyeToBoundaryPolygonPointDist = (float)Math.sqrt(maxEyeToBoundaryPolygonPointDistSq);
		minEyeToBoundaryPolygonPointDist = (float)Math.sqrt(minEyeToBoundaryPolygonPointDistSq);
		if (boundaryPolygonXIndicators == null || boundaryPolygonXIndicators.length != boundaryPolygonPoints.size()){
			boundaryPolygonXIndicators = new int[boundaryPolygonPoints.size()];
		}
		if (boundaryPolygonYIndicators == null || boundaryPolygonYIndicators.length != boundaryPolygonPoints.size()){
			boundaryPolygonYIndicators = new int[boundaryPolygonPoints.size()];
		}
	}

	public void reset(){
		reset(eye, boundaryPolygon);
	}

	public ga_Polygon getBoundaryPolygon() {
		return boundaryPolygon;
	}

	public ga_Vector2 getEye() {
		return eye;
	}

	public ArrayList<ga_VPVisiblePoint> getVisiblePoints() {
		return visiblePoints;
	}

	public ga_Polygon getVisiblePolygon() {
		return visiblePolygon;
	}

	public ArrayList<ga_i_Occluder> getVisibleOccluders(){
		// Add any occluders that are visible into a list by searching for them in the VisionData's VisiblePoints.
		ArrayList<ga_i_Occluder> visibleOccluders = new ArrayList<ga_i_Occluder>();
		for (int i = 0; i < visiblePoints.size(); i++){
			ga_VPVisiblePoint vp = visiblePoints.get(i);
			if (vp.getType() == ga_VPVisiblePoint.OCCLUDER){
				ga_VPOccluder vpo = (ga_VPOccluder)vp;
				ga_i_Occluder occluder = vpo.getOccluder();
				if (visibleOccluders.contains(occluder) == false){
					visibleOccluders.add(occluder);
				}
			}else if (vp.getType() == ga_VPVisiblePoint.OCCLUDER_OCCLUDER_INTERSECTION){
				ga_VPOccluderOccluderIntersection vpo = (ga_VPOccluderOccluderIntersection)vp;
				ga_i_Occluder occluder = vpo.getOccluder();
				if (visibleOccluders.contains(occluder) == false){
					visibleOccluders.add(occluder);
				}
				ga_i_Occluder occluder2 = vpo.getOccluder2();
				if (visibleOccluders.contains(occluder2) == false){
					visibleOccluders.add(occluder2);
				}
			}else if (vp.getType() == ga_VPVisiblePoint.OCCLUDER_BOUNDARY_INTERSECTION){
				ga_VPOccluderBoundaryIntersection vpo = (ga_VPOccluderBoundaryIntersection)vp;
				ga_i_Occluder occluder = vpo.getOccluder();
				if (visibleOccluders.contains(occluder) == false){
					visibleOccluders.add(occluder);
				}
			}else if (vp.getType() == ga_VPVisiblePoint.SHADOW_ON_OCCLUDER){
				ga_VPShadowOnOccluder vpo = (ga_VPShadowOnOccluder)vp;
				ga_i_Occluder occluder = vpo.getOccluder();
				if (visibleOccluders.contains(occluder) == false){
					visibleOccluders.add(occluder);
				}
			}
		}
		return visibleOccluders;
	}
}
