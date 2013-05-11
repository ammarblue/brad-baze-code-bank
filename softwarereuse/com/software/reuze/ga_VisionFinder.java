package com.software.reuze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.software.reuze.ga_TileArrayIntersections;
import com.software.reuze.ga_TileBagIntersections;


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
//import straightedge.geom.util.*;

/**
 *
 * @author Keith
 */
public class ga_VisionFinder {
	public ArrayList<ga_OccluderDistAndQuad> polygonAndDists = new ArrayList<ga_OccluderDistAndQuad>();
	public ArrayList<ga_VPOccluderOccluderIntersection> occluderIntersectionPoints = new ArrayList<ga_VPOccluderOccluderIntersection>();
	public ArrayList<ga_VPOccluderBoundaryIntersection> boundaryOccluderIntersectionPoints = new ArrayList<ga_VPOccluderBoundaryIntersection>();

	public ga_VisionData calc(ga_Vector2 eye, ga_Polygon boundaryPolygon, List<? extends ga_i_Occluder> allOccluders){
		return calc(eye, boundaryPolygon, new ArrayList<ga_i_Occluder>(0), new ArrayList<ga_VPOccluderOccluderIntersection>(0), allOccluders);
	}
	public ga_VisionData calc(ga_VisionData cache, List<? extends ga_i_Occluder> allOccluders){
		return calc(cache, new ArrayList<ga_i_Occluder>(0), new ArrayList<ga_VPOccluderOccluderIntersection>(0), allOccluders);
	}
	public ga_VisionData calc(ga_Vector2 eye, ga_Polygon boundaryPolygon, ga_TileArrayIntersections<? extends ga_i_Occluder> fixedOccludersTileArrayIntersections, ArrayList<? extends ga_i_Occluder> movingOccluders){
		List<? extends ga_i_Occluder> fixedOccluders = fixedOccludersTileArrayIntersections.getAllWithin(boundaryPolygon.getCenter(), boundaryPolygon.getRadius());
		List<ga_VPOccluderOccluderIntersection> fixedOccludersIntersectionPoints = fixedOccludersTileArrayIntersections.getIntersectionsWithinAtLeast(boundaryPolygon.getCenter(), boundaryPolygon.getRadius());
		return calc(eye, boundaryPolygon, fixedOccluders, fixedOccludersIntersectionPoints, movingOccluders);
	}
	public ga_VisionData calc(ga_Vector2 eye, ga_Polygon boundaryPolygon, ga_TileBagIntersections<? extends ga_i_Occluder> fixedOccludersTileBagIntersections, ArrayList<? extends ga_i_Occluder> movingOccluders){
		return calc(eye, boundaryPolygon, fixedOccludersTileBagIntersections.getTileArray(), movingOccluders);
	}
	public ga_VisionData calc(ga_Vector2 eye, ga_Polygon boundaryPolygon, ga_TileBagIntersections<? extends ga_i_Occluder> fixedOccludersTileBagIntersections){
		return calc(eye, boundaryPolygon, fixedOccludersTileBagIntersections.getTileArray(), new ArrayList<ga_i_Occluder>(0));
	}
	public ga_VisionData calc(ga_Vector2 eye, ga_Polygon boundaryPolygon, ga_TileArrayIntersections<? extends ga_i_Occluder> fixedOccludersTileArrayIntersections){
		return calc(eye, boundaryPolygon, fixedOccludersTileArrayIntersections, new ArrayList<ga_i_Occluder>(0));
	}
	public ga_VisionData calc(ga_VisionData cache, ga_TileArrayIntersections<? extends ga_i_Occluder> fixedOccludersTileArrayIntersections, List<? extends ga_i_Occluder> movingOccluders){
		ga_Polygon boundaryPolygon = cache.getBoundaryPolygon();
		ArrayList<? extends ga_i_Occluder> fixedOccluders = fixedOccludersTileArrayIntersections.getAllWithin(boundaryPolygon.getCenter(), boundaryPolygon.getRadius());
		ArrayList<ga_VPOccluderOccluderIntersection> fixedOccludersIntersectionPoints = fixedOccludersTileArrayIntersections.getIntersectionsWithinAtLeast(boundaryPolygon.getCenter(), boundaryPolygon.getRadius());
		return calc(cache, fixedOccluders, fixedOccludersIntersectionPoints, movingOccluders);
	}
	public ga_VisionData calc(ga_VisionData cache, ga_TileBagIntersections<? extends ga_i_Occluder> fixedOccludersTileBagIntersections, List<? extends ga_i_Occluder> movingOccluders){
		return calc(cache, fixedOccludersTileBagIntersections.getTileArray(), movingOccluders);
	}
	public ga_VisionData calc(ga_VisionData cache, ga_TileBagIntersections<? extends ga_i_Occluder> fixedOccludersTileBagIntersections){
		return calc(cache, fixedOccludersTileBagIntersections.getTileArray(), new ArrayList<ga_i_Occluder>(0));
	}
	public ga_VisionData calc(ga_VisionData cache, ga_TileArrayIntersections<? extends ga_i_Occluder> fixedOccludersTileArrayIntersections){
		return calc(cache, fixedOccludersTileArrayIntersections, new ArrayList<ga_i_Occluder>(0));
	}
	public ga_VisionData calc(ga_Vector2 eye, ga_Polygon boundaryPolygon, List<? extends ga_i_Occluder> fixedOccluders, List<ga_VPOccluderOccluderIntersection> fixedOccludersIntersectionPoints, List<? extends ga_i_Occluder> movingOccluders){
		ga_VisionData cache = new ga_VisionData(eye, boundaryPolygon);
		return calc(cache, fixedOccluders, fixedOccludersIntersectionPoints, movingOccluders);
	}
	
	//CodeTimer codeTimer = new CodeTimer("calc", CodeTimer.Output.Millis, CodeTimer.Output.Millis);
//	{
//		codeTimer.setEnabled(true);
//	}
	public ga_VisionData calc(ga_VisionData cache, List<? extends ga_i_Occluder> fixedOccluders, List<ga_VPOccluderOccluderIntersection> fixedOccludersIntersectionPoints, List<? extends ga_i_Occluder> movingOccluders){
		ga_Vector2 eye = cache.eye;
		ga_Polygon boundaryPolygon = cache.boundaryPolygon;
		cache.visiblePoints = null;
		cache.visiblePolygon = null;
		int[] boundaryPolygonXIndicators = cache.boundaryPolygonXIndicators;
		int[] boundaryPolygonYIndicators = cache.boundaryPolygonYIndicators;
		float maxEyeToBoundaryPolygonPointDist = cache.maxEyeToBoundaryPolygonPointDist;
		float minEyeToBoundaryPolygonPointDist = cache.minEyeToBoundaryPolygonPointDist;
		float maxEyeToBoundaryPolygonPointDistSq = cache.maxEyeToBoundaryPolygonPointDistSq;
		float minEyeToBoundaryPolygonPointDistSq = cache.minEyeToBoundaryPolygonPointDistSq;
		//float[] boundaryPolygonPointAngles = cache.boundaryPolygonPointAngles;
		//float boundaryPolygonRotationAroundEye = cache.getBoundaryPolygonRotationAroundEye();

		ArrayList<ga_Vector2> boundaryPolygonPoints = boundaryPolygon.getPoints();
		ArrayList<ga_VPVisiblePoint> visiblePoints = new ArrayList<ga_VPVisiblePoint>(boundaryPolygonPoints.size());	// size is likely to be boundaryPolygon.size() or more.
//		codeTimer.click("polygonAndDists clear");
		polygonAndDists.clear();
//		codeTimer.click("polygonAndDists");
		// add the fixedPolygons to polygonAndDists
		for (int n = 0; n < fixedOccluders.size(); n++){
			ga_i_Occluder occluder = fixedOccluders.get(n);
			ga_Polygon poly = occluder.getPolygon();
			float distCenterToEyeLessCircBound = eye.dst(poly.getCenter()) - poly.getRadius();
			float distCenterToEyeLessCircBoundSq = distCenterToEyeLessCircBound*distCenterToEyeLessCircBound;
			if (distCenterToEyeLessCircBound < 0){
				distCenterToEyeLessCircBoundSq *= -1;
			}
			int xIndicator = getXIndicator(poly, eye);
			int yIndicator = getYIndicator(poly, eye);
			ga_OccluderDistAndQuad polygonAndDist = new ga_OccluderDistAndQuad(occluder, distCenterToEyeLessCircBound, distCenterToEyeLessCircBoundSq, xIndicator, yIndicator);
			polygonAndDists.add(polygonAndDist);
		}
		// add the movingPolygons to polygonAndDists
		for (int n = 0; n < movingOccluders.size(); n++){
			ga_i_Occluder occluder = movingOccluders.get(n);
			ga_Polygon poly = occluder.getPolygon();
			float distCenterToEyeLessCircBound = eye.dst(poly.getCenter()) - poly.getRadius();
			float distCenterToEyeLessCircBoundSq = distCenterToEyeLessCircBound*distCenterToEyeLessCircBound;
			if (distCenterToEyeLessCircBound < 0){
				distCenterToEyeLessCircBoundSq *= -1;
			}
			int xIndicator = getXIndicator(poly, eye);
			int yIndicator = getYIndicator(poly, eye);
			ga_OccluderDistAndQuad polygonAndDist = new ga_OccluderDistAndQuad(occluder, distCenterToEyeLessCircBound, distCenterToEyeLessCircBoundSq, xIndicator, yIndicator);
			polygonAndDists.add(polygonAndDist);
		}
//		codeTimer.click("sort");
		// Sort the list.
		Collections.sort(polygonAndDists);
		//codeTimer.click();

//		codeTimer.click("visiblePoints clear");
		visiblePoints.clear();

//		codeTimer.click("Add occluder points");
		// Add occluder points
		for (int i = 0; i < polygonAndDists.size(); i++){
			ga_OccluderDistAndQuad polygonAndDist = polygonAndDists.get(i);
			// check if it's possible for occluder to be inside the boundaryPolygon
			if (polygonAndDist.getDistEyeToCenterLessRadius() > maxEyeToBoundaryPolygonPointDist){
				continue;
			}
			ga_Polygon polygon = polygonAndDist.getPolygon();
			ArrayList<ga_Vector2> points = polygon.getPoints();
			boolean allPointsInsideBoundaryPolygon = (polygonAndDist.getDistEyeToCenterLessRadius() + 2*polygon.getRadius()) < minEyeToBoundaryPolygonPointDist;
			PointLoop:
			for (int j = 0; j < points.size(); j++){
				ga_Vector2 p = points.get(j);
				float eyeToPDistSq = eye.dst2(p);
				// Only add occluder points if they're inside the boundaryPolygon and they're unobstructed.
				if (allPointsInsideBoundaryPolygon == false){
					// The polygon points may not all be inside the boundaryPolygon so need to check that this point is using the contains method.
					if (eyeToPDistSq > maxEyeToBoundaryPolygonPointDistSq){
						continue;
					}else if (eyeToPDistSq >= minEyeToBoundaryPolygonPointDistSq){
						if (boundaryPolygon.contains(p) == false){
							continue;
						}
					}
				}

				// check if the line from the eye to the polygon point intersects other lines that make up the polygon
				for (int m = 0; m < points.size(); m++){
					int nextM = (m+1 >= points.size() ? 0 : m+1);
					if (j == m || j == nextM){
						continue;
					}
					if (ga_Vector2.linesIntersect(p, eye, points.get(m), points.get(nextM))){
						continue PointLoop;
					}
				}

				// check if the line from the eye to the polygon point intersects other polygons
				for (int k = 0; k < polygonAndDists.size(); k++){
					ga_OccluderDistAndQuad polygonAndDist2 = polygonAndDists.get(k);
					if (polygonAndDist == polygonAndDist2){
						// check if the line from the eye to the polygon point intersects other lines that make up the polygon
//						for (int m = 0; m < points.size(); m++){
//							int nextM = (m+1 >= points.size() ? 0 : m+1);
//							if (j == m || j == nextM){
//								continue;
//							}
//							if (Polygon.isValidNoLineIntersections(p, eye, points.get(m), points.get(nextM))){
//								continue PointLoop;
//							}
//						}
						continue;
					}
					if (polygonAndDist2.getDistEyeToCenterLessRadiusSqSigned() > eyeToPDistSq){
						break;
					}
					if (polygonAndDist.getXIndicator()*polygonAndDist2.getXIndicator() == -1 || polygonAndDist.getYIndicator()*polygonAndDist2.getYIndicator() == -1){
						continue;
					}
					ga_Polygon polygon2 = polygonAndDist2.getPolygon();
					if (polygon2.intersectionPossible(p, eye) && polygon2.intersectsLine(p, eye)){
						continue PointLoop;
					}
				}
				//float angleRelativeToEye = eye.findAngle(p);
				//VisiblePoint vp = new VPOccluder(p, polygonAndDist.getOccluder(), j, angleRelativeToEye);
				ga_VPVisiblePoint vp = new ga_VPOccluder(p, polygonAndDist.getOccluder(), j);
				visiblePoints.add(vp);
			}
		}
//		codeTimer.click("Find intersection between occluders and the boundaryPolygon");
		// Add all points of intersection between occluders and the boundaryPolygon
		boundaryOccluderIntersectionPoints.clear();
		{
			for (int j = 0; j < boundaryPolygonPoints.size(); j++){
				ga_Vector2 p = boundaryPolygonPoints.get(j);
				int jPlus = (j+1 >= boundaryPolygonPoints.size() ? 0 : j+1);
				ga_Vector2 p2 = boundaryPolygonPoints.get(jPlus);

				int xIndicator = getXIndicator(p, p2, eye);
				int yIndicator = getYIndicator(p, p2, eye);
				boundaryPolygonXIndicators[j] = xIndicator;
				boundaryPolygonYIndicators[j] = yIndicator;
				for (int k = 0; k < polygonAndDists.size(); k++){
					ga_OccluderDistAndQuad polygonAndDist = polygonAndDists.get(k);
					ga_Polygon polygon = polygonAndDist.getPolygon();
					if ((xIndicator*polygonAndDist.getXIndicator() == -1 || yIndicator*polygonAndDist.getYIndicator() == -1) == true){
						continue;
					}
					if (polygon.intersectionPossible(p, p2) == false){
						// intersection is not possible, so skip to next occluder.
						continue;
					}
					ArrayList<ga_Vector2> points = polygon.getPoints();
					for (int i = 0; i < points.size(); i++){
						int nextI = (i+1 >= points.size() ? 0 : i+1);
						ga_Vector2 p3 = points.get(i);
						ga_Vector2 p4 = points.get(nextI);
						if (ga_Vector2.linesIntersect(p, p2, p3, p4)){
							ga_Vector2 intersection = ga_Vector2.getLineLineIntersection(p, p2, p3, p4);
							if (intersection != null){
								boundaryOccluderIntersectionPoints.add(new ga_VPOccluderBoundaryIntersection(intersection, polygonAndDist.getOccluder(), i));
							}
						}
					}
				}
			}
		}
//		codeTimer.click("Only add boundary-occluder intersection points that are unoccluded");
		// Only add boundary-occluder intersection points that are unoccluded
		OuterLoop:
		for (int j = 0; j < boundaryOccluderIntersectionPoints.size(); j++){
			ga_VPOccluderBoundaryIntersection visiblePoint = boundaryOccluderIntersectionPoints.get(j);
			ga_Vector2 p = visiblePoint.getPoint();
			// see if the occluder that makes this visiblePoint intersection occludes this point from the eye.
			ArrayList<ga_Vector2> points = visiblePoint.getPolygon().getPoints();
			for (int m = 0; m < points.size(); m++){
				int nextM = (m+1 >= points.size() ? 0 : m+1);
				if (visiblePoint.getPolygonPointNum() == m){
					continue;
				}
				if (ga_Vector2.linesIntersect(p, eye, points.get(m), points.get(nextM))){
					continue OuterLoop;
				}
			}
			int xIndicator = getXIndicator(p, eye);
			int yIndicator = getYIndicator(p, eye);
			// see if any other occluders cause an obstruction
			float eyeToPDistSq = eye.dst2(p);
			for (int k = 0; k < polygonAndDists.size(); k++){
				ga_OccluderDistAndQuad polygonAndDist2 = polygonAndDists.get(k);
				ga_Polygon polygon2 = polygonAndDists.get(k).getPolygon();
				if (visiblePoint.getPolygon() == polygon2){
					continue;
				}
				if (polygonAndDists.get(k).getDistEyeToCenterLessRadiusSqSigned() > eyeToPDistSq){
					break;
				}
				if (xIndicator*polygonAndDist2.getXIndicator() == -1 || yIndicator*polygonAndDist2.getYIndicator() == -1){
					continue;
				}
				if (polygon2.intersectionPossible(p, eye) && polygon2.intersectsLine(p, eye)){
					continue OuterLoop;
				}
			}
			visiblePoints.add(visiblePoint);
		}
//		codeTimer.click("movingOccluders intersections");
		boundaryOccluderIntersectionPoints.clear();
		// Add all points of intersection between movingOccluders and fixedOccluders and other movingOccluders
		occluderIntersectionPoints.clear();
		occluderIntersectionPoints.addAll(fixedOccludersIntersectionPoints);
		for (int i = 0; i < movingOccluders.size(); i++){
			ga_i_Occluder occluder = movingOccluders.get(i);
			ga_Polygon polygon = occluder.getPolygon();
			if (boundaryPolygon.getCenter().dst(polygon.getCenter()) > boundaryPolygon.getRadius() + polygon.getRadius()){
				continue;
			}
			for (int j = 0; j < polygon.getPoints().size(); j++){
				ga_Vector2 p = polygon.getPoints().get(j);
				int jPlus = (j+1 >= polygon.getPoints().size() ? 0 : j+1);
				ga_Vector2 p2 = polygon.getPoints().get(jPlus);
				// first intersect with other movingPolygons
				for (int k = i+1; k < movingOccluders.size(); k++){
					ga_i_Occluder occluder2 = movingOccluders.get(k);
					ga_Polygon polygon2 = occluder2.getPolygon();
					if (polygon2.intersectionPossible(p, p2) == false){
						// intersection is not possible, so skip to next occluder.
						continue;
					}
					ArrayList<ga_Vector2> points = polygon2.getPoints();
					for (int m = 0; m < points.size(); m++){
						int nextM = (m+1 >= points.size() ? 0 : m+1);
						if (ga_Vector2.linesIntersect(p, p2, points.get(m),points.get(nextM))){
							ga_Vector2 intersection = ga_Vector2.getLineLineIntersection(p, p2, points.get(m), points.get(nextM));
							if (intersection != null){
								occluderIntersectionPoints.add(new ga_VPOccluderOccluderIntersection(intersection, occluder, j, occluder2, m));
							}
						}
					}
				}
				// intersect with fixedPolygons
				for (int k = 0; k < fixedOccluders.size(); k++){
					ga_i_Occluder occluder2 = fixedOccluders.get(k);
					ga_Polygon polygon2 = occluder2.getPolygon();
					if (polygon2.intersectionPossible(p, p2) == false){
						// intersection is not possible, so skip to next occluder.
						continue;
					}
					ArrayList<ga_Vector2> points = polygon2.getPoints();
					for (int m = 0; m < points.size(); m++){
						int nextM = (m+1 >= points.size() ? 0 : m+1);
						if (ga_Vector2.linesIntersect(p, p2, points.get(m),points.get(nextM))){
							ga_Vector2 intersection = ga_Vector2.getLineLineIntersection(p, p2, points.get(m), points.get(nextM));
							if (intersection != null){
								occluderIntersectionPoints.add(new ga_VPOccluderOccluderIntersection(intersection, occluder, j, occluder2, m));
							}
						}
					}
				}
			}
		}
//		codeTimer.click("take out occluder points unobstructed");
		// only add occluder intersection points that are unobstructed
		OuterLoop:
		for (int j = 0; j < occluderIntersectionPoints.size(); j++){
			ga_VPOccluderOccluderIntersection visiblePoint = occluderIntersectionPoints.get(j);
			ga_Vector2 p = visiblePoint.getPoint();
			// it's not guaranteed that the occluder intersection points are actually inside the boundaryPolygon so must check this.
			float eyeToPDistSq = eye.dst2(p);
			if (eyeToPDistSq > maxEyeToBoundaryPolygonPointDistSq){
				continue;
			}else if (eyeToPDistSq >= minEyeToBoundaryPolygonPointDistSq){
				if (boundaryPolygon.contains(p) == false){
					continue;
				}
			}
			int xIndicator = getXIndicator(p, eye);
			int yIndicator = getYIndicator(p, eye);
			for (int k = 0; k < polygonAndDists.size(); k++){
				ga_OccluderDistAndQuad polygonAndDist = polygonAndDists.get(k);
				ga_Polygon polygon = polygonAndDist.getPolygon();
				if (visiblePoint.getPolygon() == polygon){
					ArrayList<ga_Vector2> points = polygon.getPoints();
					for (int m = 0; m < points.size(); m++){
						int nextM = (m+1 >= points.size() ? 0 : m+1);
						if (visiblePoint.getPolygonPointNum() == m){
							continue;
						}
						if (ga_Vector2.linesIntersect(p, eye, points.get(m), points.get(nextM))){
							continue OuterLoop;
						}
					}
				}else if (visiblePoint.getPolygon2() == polygon){
					ArrayList<ga_Vector2> points = polygon.getPoints();
					for (int m = 0; m < points.size(); m++){
						int nextM = (m+1 >= points.size() ? 0 : m+1);
						if (visiblePoint.getPolygonPointNum2() == m){
							continue;
						}
						if (ga_Vector2.linesIntersect(p, eye, points.get(m), points.get(nextM))){
							continue OuterLoop;
						}
					}
				}else{
					if (polygonAndDist.getDistEyeToCenterLessRadiusSqSigned() > eyeToPDistSq){
						break;
					}
					if (xIndicator*polygonAndDist.getXIndicator() == -1 || yIndicator*polygonAndDist.getYIndicator() == -1){
						continue;
					}
					if (polygon.intersectionPossible(p, eye) && polygon.intersectsLine(p, eye)){
						continue OuterLoop;
					}
				}
			}
			ga_VPOccluderOccluderIntersection occluderIntersectionVisiblePoint = occluderIntersectionPoints.get(j);
			visiblePoints.add(occluderIntersectionVisiblePoint);
		}
		occluderIntersectionPoints.clear();
//		codeTimer.click("add visible boundaryPolygon points");
		// Add all points on the boundaryPolygon if they're unobstructed.
		OuterLoop:
		for (int j = 0; j < boundaryPolygonPoints.size(); j++){
			ga_Vector2 p = boundaryPolygonPoints.get(j);
			int xIndicator = getXIndicator(p, eye);
			int yIndicator = getYIndicator(p, eye);
			for (int k = 0; k < polygonAndDists.size(); k++){
				ga_OccluderDistAndQuad polygonAndDist = polygonAndDists.get(k);
				ga_Polygon polygon = polygonAndDists.get(k).getPolygon();
				if (xIndicator*polygonAndDist.getXIndicator() == -1 || yIndicator*polygonAndDist.getYIndicator() == -1){
					continue;
				}
				if (polygon.intersectionPossible(p, eye) && polygon.intersectsLine(p, eye)){
					continue OuterLoop;
				}
			}
			ga_VPBoundary vp = new ga_VPBoundary(p);
			visiblePoints.add(vp);
		}
//		codeTimer.click("sort visiblePoints");
		for (int i = 0; i < visiblePoints.size(); i++){
			visiblePoints.get(i).preSortCalcs(eye);
		}
		Collections.sort(visiblePoints);
//		codeTimer.click("add shadow points");
		// Make new points by casting a ray from the eye thru each occluder end point and finding the closest intersection.
		for (int j = 0; j < visiblePoints.size(); j++){
			int jPlus = (j+1 >= visiblePoints.size() ? 0 : j+1);
			if (visiblePoints.get(j).getType() == ga_VPVisiblePoint.OCCLUDER){
				// see if the the points on the polygon on either side of this one are on the same side so a shadow can be cast
				ga_VPOccluder sp = (ga_VPOccluder)visiblePoints.get(j);
				ga_Vector2 p = sp.getPoint();
				ga_Polygon polygon = sp.getPolygon();
				int pNum = sp.getPolygonPointNum();
				int pNumPlus = (pNum+1 >= polygon.getPoints().size() ? 0 : pNum+1);
				ga_Vector2 pPlus = polygon.getPoints().get(pNumPlus);
				int pNumMinus = (pNum-1 < 0 ? polygon.getPoints().size()-1 : pNum-1);
				ga_Vector2 pMinus = polygon.getPoints().get(pNumMinus);
				int pPlusRCCW = pPlus.relCCW(eye, p);
				int pMinusRCCW = pMinus.relCCW(eye, p);
				if (pPlusRCCW == pMinusRCCW){
					float pToEyeDist = p.dst(eye);
					ga_Vector2 endOfRayPoint = eye.createPointToward(p, pToEyeDist + boundaryPolygon.getRadius()*2);	//p.createPointFromAngle(angleRelativeToEye, getOriginalSightPolygon().getRadius()*2);
					ga_Vector2 closestIntersectionPoint = null;
					float closestDist = Float.MAX_VALUE;
					ga_i_Occluder closestOccluder = null;
					int closestObstPolygonEdgeIndex = -1;
					boolean obstCloser = false;
					int xIndicator = getXIndicator(p, eye);
					int yIndicator = getYIndicator(p, eye);
					// cast a ray from the shadow point and find the closest intersection with the occluders
					for (int k = 0; k < polygonAndDists.size(); k++){
						ga_OccluderDistAndQuad polygonAndDist = polygonAndDists.get(k);
						ga_Polygon polygon2 = polygonAndDist.getPolygon();
						if (pToEyeDist > polygonAndDist.getDistEyeToCenterLessRadius() + polygon2.getRadius()*2){
							// intersection not possible since the polygon is even closer than the ray start point.
							continue;
						}
						if (closestDist < polygonAndDist.getDistEyeToCenterLessRadius()){
							// break since this polygon is further away than the existing closestIntersection.
							break;
						}
						if (xIndicator*polygonAndDist.getXIndicator() == -1 || yIndicator*polygonAndDist.getYIndicator() == -1){
							// intersection is not possible since the point and
							// the occluders are in different quadrants, so skip to next occluder.
							continue;
						}
						if (polygon2.intersectionPossible(p, endOfRayPoint) == false){
							// intersection is not possible, so skip to next occluder.
							continue;
						}
						ArrayList<ga_Vector2> points = polygon2.getPoints();
						for (int m = 0; m < points.size(); m++){
							int mPlus = (m+1 >= points.size() ? 0 : m+1);
							if (polygon == polygon2 && (pNum == m || pNum == mPlus)){
								continue;
							}

							if (ga_Vector2.linesIntersect(p, endOfRayPoint, points.get(m), points.get(mPlus))){
								ga_Vector2 intersection = ga_Vector2.getLineLineIntersection(p, endOfRayPoint, points.get(m), points.get(mPlus));
								if (intersection != null){
									float dist = eye.dst(intersection);
									if (dist < closestDist){
										closestDist = dist;
										closestIntersectionPoint = intersection;
										closestOccluder = polygonAndDist.getOccluder();
										closestObstPolygonEdgeIndex = m;
										obstCloser = true;
									}
								}
							}
						}
					}
					int closestBoundaryPolygonEdgeIndex = -1;
					// also see if the closest intersection is with the boundaryPolygon
					if (closestIntersectionPoint == null || closestDist > minEyeToBoundaryPolygonPointDist){
						ArrayList<ga_Vector2> points = boundaryPolygon.getPoints();
						for (int m = 0; m < points.size(); m++){
							if (xIndicator*boundaryPolygonXIndicators[m] == -1 || yIndicator*boundaryPolygonYIndicators[m] == -1){
								// intersection is not possible since the boundaryPolygon points and
								// the endOfRayPoint are in different quadrants, so skip to next occluder.
								continue;
							}
							int mPlus = (m+1 >= points.size() ? 0 : m+1);
							if (ga_Vector2.linesIntersect(p, endOfRayPoint, points.get(m), points.get(mPlus))){
//								atLeastOneIntersection = true;
								ga_Vector2 intersection = ga_Vector2.getLineLineIntersection(p, endOfRayPoint, points.get(m), points.get(mPlus));
								if (intersection != null){
									float dist = eye.dst(intersection);
									if (dist < closestDist){
										closestDist = dist;
										closestIntersectionPoint = intersection;
										closestBoundaryPolygonEdgeIndex = m;
										obstCloser = false;
										// There should only be one intersection with the boundaryPolygon, so we can break.
										break;
									}
								}
							}
						}


						// for debugging:
//						if (atLeastOneIntersection == false || closestIntersectionPoint != null && closestDist > maxEyeToBoundaryPolygonPointDist){
//							System.out.println(this.getClass().getSimpleName()+": atLeastOneIntersection == "+atLeastOneIntersection+", closestIntersectionPoint != null, closestDist == "+closestDist+", maxEyeToBoundaryPolygonPointDist == "+maxEyeToBoundaryPolygonPointDist+", minEyeToBoundaryPolygonPointDist == "+minEyeToBoundaryPolygonPointDist+", boundaryPolygon.contains(p) == "+boundaryPolygon.contains(p)+", eye.distance(p) == "+eye.dst(p));
//						}
					}
					if (closestIntersectionPoint != null){
						ga_VPVisiblePoint newSightPoint = null;
						if (obstCloser){
							newSightPoint = new ga_VPShadowOnOccluder(closestIntersectionPoint, closestOccluder, closestObstPolygonEdgeIndex, sp);
//							newSightPoint.quadrant = sp.quadrant;
//							newSightPoint.xOnY = sp.xOnY;
						}else{
							newSightPoint = new ga_VPShadowOnBoundary(closestIntersectionPoint, closestBoundaryPolygonEdgeIndex, sp);
//							newSightPoint.quadrant = sp.quadrant;
//							newSightPoint.xOnY = sp.xOnY;
						}
						if (pPlusRCCW == -1 && pMinusRCCW == -1){
							visiblePoints.add(jPlus, newSightPoint);
							j++;
							continue;
						}else if (pPlusRCCW == 1 && pMinusRCCW == 1){
							visiblePoints.add(j, newSightPoint);
							j++;
							continue;
						}
//						if (pPlusRCCW == -1 && pMinusRCCW == -1){
//							shadowPointsToBeAdded.add(newSightPoint);
//							shadowPointsToBeAddedIndexes.add(jPlus);
//							continue;
//						}else if (pPlusRCCW == 1 && pMinusRCCW == 1){
//							shadowPointsToBeAdded.add(newSightPoint);
//							shadowPointsToBeAddedIndexes.add(j);
//							continue;
//						}
					}
				}
			}
		}

//		int nextInsert = 0;
//		int lastInsert = 0;
//		for (int i = 0; i < shadowPointsToBeAddedIndexes.size(); i++){
//			nextInsert = shadowPointsToBeAddedIndexes.get(i);
//			if (nextInsert == 0 && lastInsert > nextInsert){
//				// nextInsert == jPlus == 0 because j == sightPoints.size()-1,
//				// so the point can be inserted on the end or the beginning so
//				// add it on the end so that this simple loop logic works.
//				nextInsert = sightPoints.size();
//			}
//			for (int j = lastInsert; j < nextInsert; j++){
//				sightPoints2.add(sightPoints.get(j));
//			}
//			sightPoints2.add(shadowPointsToBeAdded.get(i));
//			lastInsert = nextInsert;
//		}
//		for (int j = lastInsert; j < sightPoints.size(); j++){
//			sightPoints2.add(sightPoints.get(j));
//		}
//		sightPoints.clear();
//		//sightPoints.addAll(sightPoints2);
//		//sightPoints2.clear();
//		shadowPointsToBeAdded.clear();
//		shadowPointsToBeAddedIndexes.clear();


//		codeTimer.click("make polygon");
		polygonAndDists.clear();
		cache.visiblePoints = visiblePoints;
		cache.visiblePolygon = createPolygonFromVisiblePoints(visiblePoints);
//		codeTimer.lastClick();
		return cache;
		
	}

	public ga_Polygon createPolygonFromVisiblePoints(ArrayList<ga_VPVisiblePoint> visiblePoints){
		ga_Polygon visiblePolygon;
		ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>(visiblePoints.size());
		for (int i = 0; i < visiblePoints.size(); i++){
			pointList.add(visiblePoints.get(i).getPoint().copy());
		}
		if (pointList.size() >= 3){
			// should check that points do not make intersecting lines
			//Polygon.isValidNoLineIntersections(pointList);
			visiblePolygon = new ga_Polygon(pointList);
		}else{
			visiblePolygon = null;//visiblePolygon.copy();
		}
		return visiblePolygon;
	}

	protected int getXIndicator(ga_Polygon poly, ga_Vector2 p2){
		int xIndicator;
		float relX = poly.getCenter().x - p2.x;
		if (relX - poly.getRadius() > 0){
			xIndicator = 1;
		}else if (relX + poly.getRadius() < 0){
			xIndicator = -1;
		}else{
			xIndicator = 0;
		}
		return xIndicator;
	}

	protected int getYIndicator(ga_Polygon poly, ga_Vector2 p2){
		int yIndicator;
		float relY = poly.getCenter().y - p2.y;
		if (relY - poly.getRadius() > 0){
			yIndicator = 1;
		}else if (relY + poly.getRadius() < 0){
			yIndicator = -1;
		}else{
			yIndicator = 0;
		}
		return yIndicator;
	}
	// p2 is the eye
	protected int getXIndicator(ga_Vector2 p0, ga_Vector2 p1, ga_Vector2 p2){
		int xIndicator;
		float relX0 = p0.x - p2.x;
		float relX1 = p1.x - p2.x;
		if (relX0 < 0 && relX1 < 0){
			xIndicator = -1;
		}else if (relX0 > 0 && relX1 > 0){
			xIndicator = 1;
		}else{
			xIndicator = 0;
		}
		return xIndicator;
	}
	protected int getYIndicator(ga_Vector2 p0, ga_Vector2 p1, ga_Vector2 p2){
		int yIndicator;
		float relY0 = p0.y - p2.y;
		float relY1 = p1.y - p2.y;
		if (relY0 < 0 && relY1 < 0){
			yIndicator = -1;
		}else if (relY0 > 0 && relY1 > 0){
			yIndicator = 1;
		}else{
			yIndicator = 0;
		}
		return yIndicator;
	}
	protected int getXIndicator(ga_Vector2 p, ga_Vector2 p2){
		int xIndicator;
		float relX = p.x - p2.x;
		if (relX > 0){
			xIndicator = 1;
		}else if (relX < 0){
			xIndicator = -1;
		}else{
			xIndicator = 0;
		}
		return xIndicator;
	}

	protected int getYIndicator(ga_Vector2 p, ga_Vector2 p2){
		int yIndicator;
		float relY = p.y - p2.y;
		if (relY > 0){
			yIndicator = 1;
		}else if (relY < 0){
			yIndicator = -1;
		}else{
			yIndicator = 0;
		}
		return yIndicator;
	}


}
