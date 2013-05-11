package com.software.reuze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
//package straightedge.geom;

import com.software.reuze.da_Tracker;
import com.software.reuze.ga_AABB;
import com.software.reuze.ga_Line2D;
import com.software.reuze.ga_Rectangle;
import com.software.reuze.ga_i_PolygonHolder;

//import straightedge.geom.util.Tracker;

/**
 * A cool polygon class that's got some pretty useful geometry methods.
 * Note that the polygon can be convex or concave but it should not have intersecting sides.
 *
 * Some code is from here:
 * http://www.cs.princeton.edu/introcs/35purple/Polygon.java.html
 *
 * and
 * Joseph O'Rourke:
 * http://exaflop.org/docs/cgafaq/cga2.html
 *
 * Another good source
 * Paul Bourke:
 * http://local.wasp.uwa.edu.au/~pbourke/geometry/
 *
 * @author Keith Woodward
 */
public class ga_Polygon implements ga_i_PolygonHolder/*, Shape*/ {
	public ArrayList<ga_Vector2> points;
	public ga_Vector2 center;
	public float area;
	public float radius;
	public float radiusSq;
	public boolean counterClockWise;
	
	public Object userObject;
	// for use with straightedge.geom.util.Tracker and straightedge.geom.util.TileArray
	public int trackerID = -1;
	public long trackerCounter = -1;
	public boolean trackerAddedStatus = false;

	public ga_Polygon() {
		this.points = new ArrayList<ga_Vector2>(10);
	}

	public ga_Polygon(ArrayList<ga_Vector2> pointsList, boolean copyPoints){
		if (pointsList.size() < 3){
			throw new RuntimeException("Minimum of 3 points needed. pointsList.size() == "+pointsList.size());
		}
		this.points = new ArrayList<ga_Vector2>(pointsList.size());
		for (int i = 0; i < pointsList.size(); i++){
			ga_Vector2 existingPoint = pointsList.get(i);
			if (copyPoints){
				points.add(new ga_Vector2(existingPoint));
			}else{
				points.add(existingPoint);
			}
		}
		calcAll();
	}
	public ga_Polygon(ArrayList<ga_Vector2> pointsList){
		this(pointsList, true);
	}

	public ga_Polygon(ga_Vector2[] pointsArray, boolean copyPoints){
		if (pointsArray.length < 3){
			throw new RuntimeException("Minimum of 3 points needed. pointsArray.length == "+pointsArray.length);
		}
		this.points = new ArrayList<ga_Vector2>(pointsArray.length);
		for (int i = 0; i < pointsArray.length; i++){
			ga_Vector2 existingPoint = pointsArray[i];
			if (copyPoints){
				points.add(new ga_Vector2(existingPoint));
			}else{
				points.add(existingPoint);
			}
		}
		calcAll();
	}
	public ga_Polygon(ga_Vector2... pointsArray){
		this(pointsArray, true);
	}
	public void add(ga_Vector2 point){
		points.add(point);
	}

	public ga_Polygon(ga_Polygon polygon){
		points = new ArrayList<ga_Vector2>(polygon.size());
		for (int i = 0; i < polygon.size() ;i++){
			ga_Vector2 existingPoint = polygon.points.get(i);
			points.add(new ga_Vector2(existingPoint));
		}
		area = polygon.getArea();
		counterClockWise = polygon.isCounterClockWise();
		radius = polygon.getRadius();
		radiusSq = polygon.getRadiusSq();
		center = new ga_Vector2(polygon.getCenter());
	}
	public int size() {return points.size();}

	public static ga_Polygon createRect(float x, float y, float x2, float y2){
		// make x and y the bottom left point.
		if (x2 < x) {
			float t = x;
			x = x2;
			x2 = t;
		}
		// make x2 and y2 the top right point.
		if (y2 < y) {
			float t = y;
			y = y2;
			y2 = t;
		}
		ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
		pointList.add(new ga_Vector2(x, y));
		pointList.add(new ga_Vector2(x2, y));
		pointList.add(new ga_Vector2(x2, y2));
		pointList.add(new ga_Vector2(x, y2));
		return new ga_Polygon(pointList, false);
	}
	public static ga_Polygon createRect(ga_AABB aabb){
		ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
		pointList.add(new ga_Vector2(aabb.p.x,  aabb.p.y));
		pointList.add(new ga_Vector2(aabb.p2.x, aabb.p.y));
		pointList.add(new ga_Vector2(aabb.p2.x, aabb.p2.y));
		pointList.add(new ga_Vector2(aabb.p.x,  aabb.p2.y));
		return new ga_Polygon(pointList, false);
	}
	public static ga_Polygon createRect(ga_Vector2 p, ga_Vector2 p2){
		return createRect(p.x, p.y, p2.x, p2.y);
	}
	public static ga_Polygon createRect(ga_Vector2 botLeftPoint, float width, float height){
		return createRect(botLeftPoint.x, botLeftPoint.y, botLeftPoint.x + width, botLeftPoint.y + height);
	}

	public static ga_Polygon createRectOblique(float x, float y, float x2, float y2, float width){
		ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
		float r = width/2f;
		float xOffset = 0;
		float yOffset = 0;
		float xDiff = x2 - x;
		float yDiff = y2 - y;
		if (xDiff == 0){
			xOffset = r;
			yOffset = 0;
		}else if (yDiff == 0){
			xOffset = 0;
			yOffset = r;
		}else{
			float gradient = (yDiff)/(xDiff);
			xOffset = (r*gradient/(float)(Math.sqrt(1 + gradient*gradient)));
			yOffset = -xOffset/gradient;
		}
		//System.out.println(this.getClass().getSimpleName() + ": xOffset == "+xOffset+", yOffset == "+yOffset);
		pointList.add(new ga_Vector2(x-xOffset, y-yOffset));
		pointList.add(new ga_Vector2(x+xOffset, y+yOffset));
		pointList.add(new ga_Vector2(x2+xOffset, y2+yOffset));
		pointList.add(new ga_Vector2(x2-xOffset, y2-yOffset));
		return new ga_Polygon(pointList, false);
	}
	public static ga_Polygon createRectOblique(ga_Vector2 p1, ga_Vector2 p2, float width){
		return createRectOblique(p1.x, p1.y, p2.x, p2.y, width);
	}

	public static ga_Polygon createRegularPolygon(int numPoints, float distFromCenterToPoints){
		if (numPoints < 3){
			throw new IllegalArgumentException("numPoints must be 3 or more, it can not be "+numPoints+".");
		}
		ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
		float angleIncrement = (float)Math.PI*2f/(numPoints);
		float radius = distFromCenterToPoints;
		float currentAngle = 0;
		for (int k = 0; k < numPoints; k++){
			float x = radius*(float)Math.cos(currentAngle);
			float y = radius*(float)Math.sin(currentAngle);
			pointList.add(new ga_Vector2(x, y));
			currentAngle += angleIncrement;
		}
		ga_Polygon createdPolygon = new ga_Polygon(pointList, false);
		return createdPolygon;
	}

	public ArrayList<ga_Vector2> getPoints() {
		return points;
	}

	// Gives point of intersection with line specified, where intersection point
	// returned is the one closest to (x1, y1).
	// null is returned if there is no intersection.
	public ga_Vector2 getClosestIntersectionToFirstFromSecond(float x1, float y1, float x2, float y2){
		ga_Vector2 closestIntersectionPoint = null;
		float closestIntersectionDistanceSq = Float.MAX_VALUE;
		int nextI;
		for (int i = 0; i < points.size(); i++){
			nextI = (i+1 == points.size() ? 0 : i+1);
			if (ga_Vector2.linesIntersect(x1,y1,x2,y2,points.get(i).x,points.get(i).y,points.get(nextI).x,points.get(nextI).y)){
				ga_Vector2 currentIntersectionPoint = ga_Vector2.getLineLineIntersection(x1,y1,x2,y2,points.get(i).x,points.get(i).y,points.get(nextI).x,points.get(nextI).y);
				if (currentIntersectionPoint == null){
					continue;
				}
				float currentIntersectionDistanceSq = currentIntersectionPoint.dst2(x1, y1);
				if (currentIntersectionDistanceSq < closestIntersectionDistanceSq){
					closestIntersectionPoint = currentIntersectionPoint;
					closestIntersectionDistanceSq = currentIntersectionDistanceSq;
				}
			}
		}
		return closestIntersectionPoint;
	}
	public ga_Vector2 getClosestIntersectionToFirstFromSecond(ga_Vector2 first, ga_Vector2 second){
		return getClosestIntersectionToFirstFromSecond(first.x, first.y, second.x, second.y);
	}

	public ga_Vector2 getBoundaryPointClosestTo(ga_Vector2 p){
		return getBoundaryPointClosestTo(p.x, p.y);
	}
	public ga_Vector2 getBoundaryPointClosestTo(float x, float y){
		float closestDistanceSq = Float.MAX_VALUE;
		int closestIndex = -1;
		int closestNextIndex = -1;

		int nextI;
		for (int i = 0; i < points.size(); i++){
			nextI = (i+1 == points.size() ? 0 : i+1);
			ga_Vector2 p = this.getPoints().get(i);
			ga_Vector2 pNext = this.getPoints().get(nextI);
			float ptSegDistSq = ga_Vector2.ptSegDistSq(p.x, p.y, pNext.x, pNext.y, x, y);
			if (ptSegDistSq < closestDistanceSq){
				closestDistanceSq = ptSegDistSq;
				closestIndex = i;
				closestNextIndex = nextI;
			}
		}
		ga_Vector2 p = this.getPoints().get(closestIndex);
		ga_Vector2 pNext = this.getPoints().get(closestNextIndex);
		return ga_Vector2.getClosestPointOnSegment(p.x, p.y, pNext.x, pNext.y, x, y);
	}


	public boolean contains(ga_Polygon foreign){
		if (intersectsPerimeter(foreign)){
			return false;
		}
		if (contains(foreign.getPoints().get(0)) == false){
			return false;
		}
		return true;
	}
	
	public boolean contains(ga_Vector2 p){
		return contains(p.x, p.y);
	}

    // Source code from: http://exaflop.org/docs/cgafaq/cga2.html
	//Subject 2.03: How do I find if a point lies within a polygon?
	//The definitive reference is "Point in Polygon Strategies" by Eric Haines [Gems IV] pp. 24-46. The code in the Sedgewick book Algorithms (2nd Edition, p.354) is incorrect.
	//The essence of the ray-crossing method is as follows. Think of standing inside a field with a fence representing the polygon. Then walk north. If you have to jump the fence you know you are now outside the poly. If you have to cross again you know you are now inside again; i.e., if you were inside the field to start with, the total number of fence jumps you would make will be odd, whereas if you were ouside the jumps will be even.
	//The code below is from Wm. Randolph Franklin <wrf@ecse.rpi.edu> with some minor modifications for speed. It returns 1 for strictly interior points, 0 for strictly exterior, and 0 or 1 for points on the boundary. The boundary behavior is complex but determined; | in particular, for a partition of a region into polygons, each point | is "in" exactly one polygon. See the references below for more detail
	//The code may be further accelerated, at some loss in clarity, by avoiding the central computation when the inequality can be deduced, and by replacing the division by a multiplication for those processors with slow divides.
	//References:
	//[Gems IV] pp. 24-46
	//[O'Rourke] pp. 233-238
	//[Glassner:RayTracing]
	/*public boolean contains(float x, float y) {
		Vector2 pointIBefore = (points.size() != 0 ? points.get(points.size() - 1) : null);
		int crossings = 0;
		for (int i = 0; i < points.size(); i++) {
			Vector2 pointI = points.get(i);
			if (((pointIBefore.y <= y && y < pointI.y)
					|| (pointI.y <= y && y < pointIBefore.y))
					&& x < ((pointI.x - pointIBefore.x)/(pointI.y - pointIBefore.y)*(y - pointIBefore.y) + pointIBefore.x)) {
				crossings++;
			}
			pointIBefore = pointI;
		}
		return (crossings % 2 != 0);
	}*/
	
	//TODO quad case optimization is check two triangles
	public boolean contains(float px, float py) {
        int num = points.size();
        int i, j = num - 1;
        boolean oddNodes = false;
        for (i = 0; i < num; i++) {
        	ga_Vector2 vi = points.get(i);
        	ga_Vector2 vj = points.get(j);
            if (vi.y < py && vj.y >= py || vj.y < py && vi.y >= py) {
                if (vi.x + (py - vi.y) / (vj.y - vi.y) * (vj.x - vi.x) < px) {
                    oddNodes = !oddNodes;
                }
            }
            j = i;
        }
        return oddNodes;
    }

	public ga_Vector2 getPoint(int i){
		return getPoints().get(i);
	}

	public void calcArea(){
		float signedArea = getAndCalcSignedArea();
		if (signedArea < 0){
			counterClockWise = false;
		}else{
			counterClockWise = true;
		}
		area = Math.abs(signedArea);
	}
	public float getAndCalcSignedArea(){
		float totalArea = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			totalArea += ((points.get(i).x - points.get(i+1).x)*(points.get(i+1).y + (points.get(i).y - points.get(i+1).y)/2));
		}
		// need to do points[point.length-1] and points[0].
		totalArea += ((points.get(points.size()-1).x - points.get(0).x)*(points.get(0).y + (points.get(points.size()-1).y - points.get(0).y)/2));
		return totalArea;
	}

	public float[] getBoundsArray(){
		return getBoundsArray(new float[4]);
	}
	public float[] getBoundsArray(float[] bounds){
		float leftX = Float.MAX_VALUE;
		float botY = Float.MAX_VALUE;
		float rightX = -Float.MAX_VALUE;
		float topY = -Float.MAX_VALUE;

		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).x < leftX) {
				leftX = points.get(i).x;
			}
			if (points.get(i).x > rightX) {
				rightX = points.get(i).x;
			}
			if (points.get(i).y < botY) {
				botY = points.get(i).y;
			}
			if (points.get(i).y > topY) {
				topY = points.get(i).y;
			}
		}
		bounds[0] = leftX;
		bounds[1] = botY;
		bounds[2] = rightX;
		bounds[3] = topY;
		return bounds;
	}

	public ga_AABB getAABB(){
		float leftX = Float.MAX_VALUE;
		float botY = Float.MAX_VALUE;
		float rightX = -Float.MAX_VALUE;
		float topY = -Float.MAX_VALUE;

		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).x < leftX) {
				leftX = points.get(i).x;
			}
			if (points.get(i).x > rightX) {
				rightX = points.get(i).x;
			}
			if (points.get(i).y < botY) {
				botY = points.get(i).y;
			}
			if (points.get(i).y > topY) {
				topY = points.get(i).y;
			}
		}
		ga_AABB aabb = new ga_AABB(leftX, botY, rightX, topY);
		return aabb;
	}

	public boolean intersectsPerimeter(ga_Polygon foreign){
		ga_Vector2 pointIBefore = (points.size() != 0 ? points.get(points.size()-1) : null);
		ga_Vector2 pointJBefore = (foreign.points.size() != 0 ? foreign.points.get(foreign.points.size()-1) : null);
		for (int i = 0; i < points.size(); i++){
			ga_Vector2 pointI = points.get(i);
			//int nextI = (i+1 >= points.size() ? 0 : i+1);
			for (int j = 0; j < foreign.points.size(); j++){
				//int nextJ = (j+1 >= foreign.points.size() ? 0 : j+1);
				ga_Vector2 pointJ = foreign.points.get(j);
				//if (Vector2.linesIntersect(points.get(i).x, points.get(i).y, points.get(nextI).x, points.get(nextI).y, foreign.points.get(j).x, foreign.points.get(j).y, foreign.points.get(nextJ).x, foreign.points.get(nextJ).y)){
				// The below linesIntersect could be sped up slightly since many things are recalc'ed over and over again.
				if (ga_Vector2.linesIntersect(pointI, pointIBefore, pointJ, pointJBefore)){
					return true;
				}
				pointJBefore = pointJ;
			}
			pointIBefore = pointI;
		}
		return false;
	}

	public boolean intersects(ga_Polygon foreign){
		if (intersectsPerimeter(foreign)){
			return true;
		}
		if (contains(foreign.getPoint(0)) || foreign.contains(getPoint(0))){
			return true;
		}
		return false;
	}
	public boolean intersectionPossible(ga_Polygon poly){
		return intersectionPossible(this, poly);
	}
	public static boolean intersectionPossible(ga_Polygon poly, ga_Polygon poly2){
		float sumRadiusSq = poly.getRadius() + poly2.getRadius();
		sumRadiusSq *= sumRadiusSq;
		if (poly.getCenter().dst2(poly2.getCenter()) > sumRadiusSq){
		//if (center.dst(foreign.getCenter()) > radius + foreign.getRadius()){
			return false;
		}
		return true;
	}
	public boolean intersectionPossible(ga_Vector2 p1, ga_Vector2 p2){
		return intersectionPossible(p1.x, p1.y, p2.x, p2.y);
	}
	public boolean intersectionPossible(float x1, float y1, float x2, float y2){
		if (center.ptSegDistSq(x1, y1, x2, y2) > radiusSq){
			return false;
		}
		return true;
	}
	
	public boolean intersectsLine(ga_Vector2 p1, ga_Vector2 p2){
		return intersectsLine(p1.x, p1.y, p2.x, p2.y);
	}
	public boolean intersectsLine(float x1, float y1, float x2, float y2){
//		// pretty much just does the following, but with some optimizations by
//		// caching some values normally recalculated in the Vector2.linesIntersect method:
//		Vector2 pointIBefore = points.get(points.size()-1);
//		for (int i = 0; i < points.size(); i++){
//			Vector2 pointI = points.get(i);
//			if (Vector2.linesIntersect(x1, y1, x2, y2, pointIBefore.x, pointIBefore.y, pointI.x, pointI.y)){
//				return true;
//			}
//			pointIBefore = pointI;
//		}
//		return false;

		// Sometimes this method fails if the 'lines'
		// start and end on the same point, so here we check for that.
		if (x1 == x2 && y1 == y2){
			return false;
		}
		float ax = x2-x1;
		float ay = y2-y1;
		ga_Vector2 pointIBefore = points.get(points.size()-1);
		for (int i = 0; i < points.size(); i++){
			ga_Vector2 pointI = points.get(i);
			float x3 = pointIBefore.x;
			float y3 = pointIBefore.y;
			float x4 = pointI.x;
			float y4 = pointI.y;

			float bx = x3-x4;
			float by = y3-y4;
			float cx = x1-x3;
			float cy = y1-y3;

			float alphaNumerator = by*cx - bx*cy;
			float commonDenominator = ay*bx - ax*by;
			if (commonDenominator > 0){
				if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
					pointIBefore = pointI;
					continue;
				}
			}else if (commonDenominator < 0){
				if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
					pointIBefore = pointI;
					continue;
				}
			}
			float betaNumerator = ax*cy - ay*cx;
			if (commonDenominator > 0){
				if (betaNumerator < 0 || betaNumerator > commonDenominator){
					pointIBefore = pointI;
					continue;
				}
			}else if (commonDenominator < 0){
				if (betaNumerator > 0 || betaNumerator < commonDenominator){
					pointIBefore = pointI;
					continue;
				}
			}
			if (commonDenominator == 0){
				// This code wasn't in Franklin Antonio's method. It was added by Keith Woodward.
				// The lines are parallel.
				// Check if they're collinear.
				float collinearityTestForP3 = x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2);	// see http://mathworld.wolfram.com/Collinear.html
				// If p3 is collinear with p1 and p2 then p4 will also be collinear, since p1-p2 is parallel with p3-p4
				if (collinearityTestForP3 == 0){
					// The lines are collinear. Now check if they overlap.
					if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 ||
							x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4 ||
							x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2){
						if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 ||
								y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4 ||
								y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2){
							return true;
						}
					}
				}
				pointIBefore = pointI;
				continue;
			}
			return true;
		}
		return false;
	}

	public void calcCenter() {
		if (center == null){
			center = new ga_Vector2();
		}
		if (getArea() == 0){
			center.x = points.get(0).x;
			center.y = points.get(0).y;
			return;
		}
        float cx = 0.0f;
		float cy = 0.0f;
		ga_Vector2 pointIBefore = (points.size() != 0 ? points.get(points.size()-1) : null);
        for (int i = 0; i < points.size(); i++) {
			//int nextI = (i+1 >= points.size() ? 0 : i+1);
//			float multiplier = (points.get(i).y * points.get(nextI).x - points.get(i).x * points.get(nextI).y);
//			cx += (points.get(i).x + points.get(nextI).x) * multiplier;
//			cy += (points.get(i).y + points.get(nextI).y) * multiplier;
			ga_Vector2 pointI = points.get(i);
			float multiplier = (pointIBefore.y * pointI.x - pointIBefore.x * pointI.y);
			cx += (pointIBefore.x + pointI.x) * multiplier;
			cy += (pointIBefore.y + pointI.y) * multiplier;
			pointIBefore = pointI;
        }
		cx /= (6 * getArea());
        cy /= (6 * getArea());
		if (counterClockWise == true){
			cx *= -1;
			cy *= -1;
		}
        center.x = cx;
		center.y = cy;
    }
	public void calcRadius() {
		if (center == null){
			calcCenter();
		}
		float maxRadiusSq = -1;
		int furthestPointIndex = 0;
		for (int i = 0; i < points.size(); i++) {
			float currentRadiusSq = (center.dst2(points.get(i)));
			if (currentRadiusSq > maxRadiusSq) {
				maxRadiusSq = currentRadiusSq;
				furthestPointIndex = i;
			}
		}
		radius = (center.dst(points.get(furthestPointIndex)));
		radiusSq = radius*radius;
	}

	public void calcAll(){
		this.calcArea();
		this.calcCenter();
		this.calcRadius();
	}

	public float getArea() {
		return area;
	}

	public ga_Vector2 getCenter() {
		return center;
	}

	public float getRadius() {
		return radius;
	}
	public float getRadiusSq() {
		return radiusSq;
	}

	public float getPerimeter() {
		float perimeter = 0;
		for (int i = 0; i < points.size()-1; i++) {
			perimeter += points.get(i).dst(points.get(i+1));
		}
		perimeter += points.get(points.size()-1).dst(points.get(0));
		return perimeter;
	}

	public ga_Polygon rotate(float angle) {
		rotate(angle, center.x, center.y);
		return this;
	}
	public ga_Polygon rotate(float angle, ga_Vector2 axle) {
		rotate(angle, axle.x, axle.y);
		return this;
	}
	public ga_Polygon rotate(float angle, float x, float y) {
		for (int i = 0; i < points.size(); i++) {
			ga_Vector2 p = points.get(i);
			p.rotate(angle, x, y);
		}
		// rotate the center if it's not equal to the axle.
		if (x != center.x || y != center.y){
			center.rotate(angle, x, y);
		}
		return this;
	}

	public ga_Polygon translate(float x, float y) {
		for (int i = 0; i < points.size(); i++) {
			points.get(i).x += x;
			points.get(i).y += y;
		}
		center.x += x;
		center.y += y;
		return this;
	}
	public ga_Polygon translate(ga_Vector2 translation){
		return translate(translation.x, translation.y);
	}

	public ga_Polygon translateTo(float x, float y){
		float xIncrement = x - center.x;
		float yIncrement = y - center.y;
		center.x = x;
		center.y = y;
		for (int i = 0; i < points.size(); i++){
			points.get(i).x += xIncrement;
			points.get(i).y += yIncrement;
		}
		return this;
	}
	public ga_Polygon translateTo(ga_Vector2 newCentre){
		return translateTo(newCentre.x, newCentre.y);
	}
	public ga_Polygon translateToOrigin(){
		return translateTo(0, 0);
	}

	public ga_Polygon scale(float xMultiplier, float yMultiplier, float x, float y){
		float incX;
		float incY;
		for (int i = 0; i < points.size(); i++){
			incX = points.get(i).x - x;
			incY = points.get(i).y - y;
			incX *= xMultiplier;
			incY *= yMultiplier;
			points.get(i).x = x + incX;
			points.get(i).y = y + incY;
		}
		incX = center.x - x;
		incY = center.y - y;
		incX *= xMultiplier;
		incY *= yMultiplier;
		center.x = x + incX;
		center.y = y + incY;
		this.calcArea();
		this.calcRadius();
		return this;
	}
	public ga_Polygon scale(float multiplierX, float multiplierY){
		return scale(multiplierX, multiplierY, getCenter().x, getCenter().y);
	}
	public ga_Polygon scale(float multiplierX, float multiplierY, ga_Vector2 p){
		return scale(multiplierX, multiplierY, p.x, p.y);
	}
	public ga_Polygon scale(float multiplier){
		return scale(multiplier, multiplier, getCenter().x, getCenter().y);
	}
	public ga_Polygon scale(double multiplier){
		return scale((float)multiplier, (float)multiplier, getCenter().x, getCenter().y);
	}
	public ga_Polygon scale(float multiplier, ga_Vector2 p){
		return scale(multiplier, multiplier, p.x, p.y);
	}

	public ga_Vector2 getBoundaryPointFromCenterToward(ga_Vector2 endPoint){
		float distToExtendOutTo = 3*getRadius();
		float xCoord = getCenter().x;
		float yCoord = getCenter().y;
		float xDiff = endPoint.x - getCenter().x;
		float yDiff = endPoint.y - getCenter().y;
		if (xDiff == 0 && yDiff == 0){
			yCoord += distToExtendOutTo;
		}else if (xDiff == 0){
			yCoord += distToExtendOutTo * Math.signum(yDiff);
		}else if (yDiff == 0){
			xCoord += distToExtendOutTo * Math.signum(xDiff);
		}else{
			xCoord += distToExtendOutTo * Math.abs(xDiff/(xDiff + yDiff)) * Math.signum(xDiff);
			yCoord += distToExtendOutTo * Math.abs(yDiff/(xDiff + yDiff)) * Math.signum(yDiff);
		}
		ga_Vector2 boundaryPoint = getClosestIntersectionToFirstFromSecond(getCenter().x, getCenter().y, xCoord, yCoord);
		return boundaryPoint;
	}

	public boolean isCounterClockWise() {
		return counterClockWise;
	}

	public ga_Polygon reversePointOrder(){
		counterClockWise = !counterClockWise;
		ArrayList<ga_Vector2> tempPoints = new ArrayList<ga_Vector2>(points.size());
		for (int i = points.size()-1; i >= 0; i--){
			tempPoints.add(points.get(i));
		}
		points.clear();
		points.addAll(tempPoints);
		return this;
	}

	public boolean isValidNoLineIntersections() {
		return isValidNoLineIntersections(points);
	}
	public static boolean isValidNoLineIntersections(ArrayList<ga_Vector2> points) {
		for (int i = 0; i < points.size(); i++){
			int iPlus = (i+1 >= points.size() ? 0 : i+1);
			for (int j = i+2; j < points.size(); j++){
				int jPlus = (j+1 >= points.size() ? 0 : j+1);
				if (i == jPlus){
					continue;
				}
				if (ga_Vector2.linesIntersect(points.get(i), points.get(iPlus), points.get(j), points.get(jPlus))){
					return false;
				}
			}
		}
		return true;
	}

	public boolean isValidNoConsecutiveEqualPoints() {
		return isValidNoConsecutiveEqualPoints(points);
	}
	public static boolean isValidNoConsecutiveEqualPoints(ArrayList<ga_Vector2> points) {
		ga_Vector2 pointIBefore = (points.size() != 0 ? points.get(points.size()-1) : null);
		for (int i = 0; i < points.size(); i++){
			ga_Vector2 pointI = points.get(i);
			if (pointI.x == pointIBefore.x && pointI.y == pointIBefore.y){
				return false;
			}
		}
		return true;
	}
	public boolean isValidNoEqualPoints() {
		return isValidNoEqualPoints(points);
	}
	public static boolean isValidNoEqualPoints(ArrayList<ga_Vector2> points) {
		for (int i = 0; i < points.size(); i++){
			ga_Vector2 pointI = points.get(i);
			for (int j = i+1; j < points.size(); j++){
				ga_Vector2 pointJ = points.get(j);
				if (pointI.x == pointJ.x && pointI.y == pointJ.y){
					return false;
				}
			}
		}
		return true;
	}
	public static boolean printOffendingIntersectingLines(ArrayList<ga_Vector2> points) {
		boolean linesIntersect = false;
		for (int i = 0; i < points.size(); i++){
			int iPlus = (i+1 >= points.size() ? 0 : i+1);
			for (int j = i+2; j < points.size(); j++){
				int jPlus = (j+1 >= points.size() ? 0 : j+1);
				if (i == jPlus){
					continue;
				}
				if (ga_Vector2.linesIntersect(points.get(i), points.get(iPlus), points.get(j), points.get(jPlus))){
					System.out.println(ga_Polygon.class.getSimpleName()+": the line between points.get("+i+") & points.get("+iPlus+") intersects with the line between points.get("+j+") & points.get("+jPlus+")");
					System.out.println(ga_Polygon.class.getSimpleName()+": the line between points.get("+i+") == "+points.get(i));
					System.out.println(ga_Polygon.class.getSimpleName()+": the line between points.get("+iPlus+") == "+points.get(iPlus));
					System.out.println(ga_Polygon.class.getSimpleName()+": the line between points.get("+j+") == "+points.get(j));
					System.out.println(ga_Polygon.class.getSimpleName()+": the line between points.get("+jPlus+") == "+points.get(jPlus));
					linesIntersect = true;
				}
			}
		}
		return linesIntersect;
	}

	public ga_Polygon copy(){
		ga_Polygon polygon = new ga_Polygon(this);
		return polygon;
	}
    /**
     * Reduces the number of vertices in the polygon based on the given minimum
     * edge length. Only vertices with at least this distance between them will
     * be kept.
     * 
     * @param minEdgeLen
     * @return itself
     */
    public ga_Polygon reduceVertices(float minEdgeLen) {
        minEdgeLen *= minEdgeLen;
        ArrayList<ga_Vector2> reduced = new ArrayList<ga_Vector2>();
        ga_Vector2 prev = points.get(0);
        reduced.add(prev);
        int num = points.size() - 1;
        for (int i = 1; i < num; i++) {
        	ga_Vector2 v = points.get(i);
            if (prev.dst2(v) >= minEdgeLen) {
                reduced.add(v);
                prev = v;
            }
        }
        if (points.get(0).dst2(points.get(num)) >= minEdgeLen) {
            reduced.add(points.get(num));
        }
        points = reduced;
        return this;
    }
    
    /**
     * Repeatedly inserts vertices as mid points of the longest edges until the
     * new vertex count is reached.
     * 
     * @param count
     *            new vertex count
     * @return itself
     */
    public ga_Polygon increaseVertexCount(int count) {
        int num = points.size();
        while (num < count) {
            // find longest edge
            int longestID = 0;
            float maxD = 0;
            for (int i = 0; i < num; i++) {
                float d = points.get(i).dst2(
                        points.get((i + 1) % num));
                if (d > maxD) {
                    longestID = i;
                    maxD = d;
                }
            }
            // insert mid point of longest segment in vertex list
            ga_Vector2 m = points.get(longestID).copy()
                    .add(points.get((longestID + 1) % num)).mul(0.5f);
            points.add(longestID + 1, m);
            num++;
        }
        return this;
    }
	/**
	 * Needed by PolygonHolder.
	 * @return This Polygon.
	 */
	public ga_Polygon getPolygon(){
		return this;
	}
	
	
	public void setTileArraySearchStatus(boolean trackerAddedStatus, da_Tracker tracker){
		this.trackerAddedStatus = trackerAddedStatus;
		this.trackerCounter = tracker.getCounter();
		this.trackerID = tracker.getID();
	}

	public boolean isTileArraySearchStatusAdded(da_Tracker tracker){
		if (this.trackerCounter == tracker.getCounter() && this.trackerID == tracker.getID()){
			return trackerAddedStatus;
		}else{
			return false;
		}
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	public int getNextIndex(int i){
		int iPlus = i+1;
		return (iPlus >= points.size() ? 0 : iPlus);
	}
	public int getPrevIndex(int i){
		int iMinus = i-1;
		return (iMinus < 0 ? points.size()-1 : iMinus);
	}
	public ga_Vector2 getNextPoint(int i){
		return points.get(getNextIndex(i));
	}
	public ga_Vector2 getPrevPoint(int i){
		return points.get(getPrevIndex(i));
	}

	public String toString() {
		String str = getClass().getName() + "@" + Integer.toHexString(hashCode());
		if (getCenter() != null) {
			str += ", center == " + getCenter().toString();
		}
		str += ", area == " + area;
		str += ", radius == " + radius;
		if (points != null) {
			//str += ", points == " + points.toString();
			str += ", points.size() == "+points.size()+":\n";
			for (int i = 0; i < points.size(); i++){
				ga_Vector2 p = points.get(i);
				str += "  i == "+i+", "+p+"\n";
			}
		}
		return str;
	}

	public static ga_Rectangle getBounds(ga_Polygon p) { //cannot be parameterless since cannot be overridden w a different return type
		float[] bounds = p.getBoundsArray();
		return new ga_Rectangle((int)(bounds[0]), (int)(bounds[1]), (int)Math.ceil(bounds[2]), (int)Math.ceil(bounds[3]));
	}
	/**
	 * Unlike Shape.intersects, this method is exact. Note that this method should
	 * really be called overlaps(x,y,w,h) since it doesn't just test for line-line intersection.
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return Returns true if the given rectangle overlaps this polygon.
	 */
	public boolean intersects(float x, float y, float w, float h){
		if (x + w < center.x - radius ||
				x > center.x + radius ||
				y + h < center.y - radius ||
				y > center.y + radius){
			return false;
		}
		for (int i = 0; i < points.size(); i++){
			int nextI = (i+1 >= points.size() ? 0 : i+1);
			if (ga_Vector2.linesIntersect(x, y, x + w, y, points.get(i).x, points.get(i).y, points.get(nextI).x, points.get(nextI).y) ||
					ga_Vector2.linesIntersect(x, y, x, y+h, points.get(i).x, points.get(i).y, points.get(nextI).x, points.get(nextI).y) ||
					ga_Vector2.linesIntersect(x, y+h, x+w, y+h, points.get(i).x, points.get(i).y, points.get(nextI).x, points.get(nextI).y) ||
					ga_Vector2.linesIntersect(x+w, y, x+w, y+h, points.get(i).x, points.get(i).y, points.get(nextI).x, points.get(nextI).y)){
				return true;
			}
		}
		float px = points.get(0).x;
		float py = points.get(0).y;
		if (px > x && px < x + w && py > y && py < y + h){
			return true;
		}
		if (contains(x, y) == true){
			return true;
		}
		return false;
	}

	/**
	 * Unlike Shape.contains, this method is exact.
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return Returns true if the given rectangle wholly fits inside this polygon with no perimeter intersections.
	 */
	public boolean contains(float x, float y, float w, float h){
		if (x + w < center.x - radius ||
				x > center.x + radius ||
				y + h < center.y - radius ||
				y > center.y + radius){
			return false;
		}
		for (int i = 0; i < points.size(); i++){
			int nextI = (i+1 >= points.size() ? 0 : i+1);
			if (ga_Vector2.linesIntersect(x, y, x + w, y, points.get(i).x, points.get(i).y, points.get(nextI).x, points.get(nextI).y) ||
					ga_Vector2.linesIntersect(x, y, x, y+h, points.get(i).x, points.get(i).y, points.get(nextI).x, points.get(nextI).y) ||
					ga_Vector2.linesIntersect(x, y+h, x+w, y+h, points.get(i).x, points.get(i).y, points.get(nextI).x, points.get(nextI).y) ||
					ga_Vector2.linesIntersect(x+w, y, x+w, y+h, points.get(i).x, points.get(i).y, points.get(nextI).x, points.get(nextI).y)){
				return false;
			}
		}
		float px = points.get(0).x;
		float py = points.get(0).y;
		if (px > x && px < x + w && py > y && py < y + h){
			return false;
		}
		if (contains(x, y) == true){
			return true;
		}
		return false;
	}
    /**
     * Flips the ordering of the polygon's vertices.
     * 
     * @return itself
     */
    public ga_Polygon flipVertexOrder() {
        Collections.reverse(points);
        return this;
    }

    /**
     * Computes the polygon's center of mass. Code ported from:
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     * 
     * @return centroid point
     */
    public ga_Vector2 getCentroid() {
    	ga_Vector2 res = new ga_Vector2();
        int numPoints = points.size();
        for (int i = 0; i < numPoints; i++) {
        	ga_Vector2 a = points.get(i);
        	ga_Vector2 b = points.get((i + 1) % numPoints);
            float factor = a.x * b.y - b.x * a.y;
            res.x += (a.x + b.x) * factor;
            res.y += (a.y + b.y) * factor;
        }
        return res.mul(1f / (getArea() * 6));
    }
    /**
     * Constrains the target vector to the perimeter of this polygon. Unlike the
     * {@link #constrain(ga_Rectangle)} version of this method, this version DOES NOT
     * check containment automatically. If you want to only constrain a point if
     * its (for example) outside the polygon, then check containment with
     * {@link ga_Polygon#contains(ga_Vector2)} first before calling this
     * method.
     * 
     * @param target
     * @return target, possibly modified
     */
    public ga_Vector2 constrain(ga_Vector2 target) {
        float minD = Float.MAX_VALUE;
        ga_Vector2 q = null;
        ga_Line2D l=null;
        for (ga_Vector2 p : this.points) {
        	if (l==null) {
        		l = new ga_Line2D(this.points.get(this.points.size()-1), this.points.get(0));
        	} else {l.a=l.b; l.b=p;}
        	ga_Vector2 c = l.closestPointTo(target);
            float d = c.dst(target);
            if (d < minD) {
                q = c;
                minD = d;
            }
        }
        target.x = q.x;
        target.y = q.y;
        return target;
    }
    /**
     * Removes duplicate vertices from the polygon. Only successive points are
     * recognized as duplicates.
     * 
     * @param tolerance
     *            snap distance for finding duplicates
     * @return itself
     */
    public ga_Polygon removeDuplicates(float tolerance) {
        ga_Vector2 prev = null;
        for (Iterator<ga_Vector2> iv = points.iterator(); iv.hasNext();) {
        	ga_Vector2 p = iv.next();
            if (p.equalsWithTolerance(prev, tolerance)) {
                iv.remove();
            } else {
                prev = p;
            }
        }
        int num = points.size();
        if (num > 0) {
        	ga_Vector2 last = points.get(num - 1);
            if (last.equalsWithTolerance(points.get(0), tolerance)) {
                points.remove(last);
            }
        }
        return this;
    }
    /**
     * Applies a laplacian-style smooth operation to all polygon vertices,
     * causing sharp corners/angles to widen and results in a general smoother
     * shape. Let the current vertex be A and its neighbors P and Q, then A
     * will be moved by a specified amount into the direction given by
     * (P-A)+(Q-A). Additionally, and to avoid shrinking of the shape through
     * repeated iteration of this procedure, the vector A - C (Polygon centroid)
     * is added as counter force and a weight for its impact can be specified.
     * To keep the average size of the polygon stable, this weight value should
     * be ~1/2 of the smooth amount.
     * TODO repeated application causes a slow drift to the left
     * @param amount
     *            smooth amount (between 0 < x < 0.5)
     * @param baseWeight
     *            counter weight (0 <= x < 1/2 * smooth amount)
     * @return itself
     */
    public ga_Polygon smooth(float amount, float baseWeight) {
        ga_Vector2 centroid = getCentroid();
        int num = points.size();
        List<ga_Vector2> filtered = new ArrayList<ga_Vector2>(num);
        for (int i = 0, j = num - 1, k = 1; i < num; i++) {
            ga_Vector2 a = points.get(i);
            ga_Vector2 dir = points.get(j).copy().sub(a).add(points.get(k).tmp().sub(a))
                    .add(a.tmp2().sub(centroid).mul(baseWeight));
            filtered.add(a.copy().add(dir.mul(amount)));
            j++;
            if (j == num) {
                j = 0;
            }
            k++;
            if (k == num) {
                k = 0;
            }
        }
        points.clear();
        points.addAll(filtered);
        return this;
    }
/*********************
    public boolean contains(Rectangle2D r){
		return this.contains((float)r.getX(), (float)r.getY(), (float)r.getWidth(), (float)r.getHeight());
	}
	public boolean intersects(Rectangle2D r){
		return this.intersects((float)r.getX(), (float)r.getY(), (float)r.getWidth(), (float)r.getHeight());
	}
	public boolean contains(Point2D p){
		return contains((float)p.getX(), (float)p.getY());
	}
   	// Note: The following methods are needed to implement java.awt.geom.Shape.
	public Rectangle2D.Float getBounds2D(){
		float[] bounds = getBoundsArray();
		return new Rectangle2D.Float(bounds[0], bounds[1], bounds[2], bounds[3]);
	}
	public PathIterator getPathIterator(AffineTransform at){
		return new PolygonIterator(this, at);
	}
	public PathIterator getPathIterator(AffineTransform at, float flatness){
		return new PolygonIterator(this, at);
	}
	public class PolygonIterator implements PathIterator {
		int type = PathIterator.SEG_MOVETO;
		int index = 0;
		Polygon polygon;
		Vector2 currentPoint;
		AffineTransform affine;

		float[] singlePointSetDouble = new float[2];

		PolygonIterator(Polygon kPolygon) {
			this(kPolygon, null);
		}

		PolygonIterator(Polygon kPolygon, AffineTransform at) {
			this.polygon = kPolygon;
			this.affine = at;
			currentPoint = polygon.getPoint(0);
		}

		public int getWindingRule() {
			return PathIterator.WIND_EVEN_ODD;
		}

		public boolean isDone() {
			if (index == polygon.points.size() + 1){
				return true;
			}
			return false;
		}

		public void next() {
			index++;
		}

		public void assignPointAndType(){
			if (index == 0){
				currentPoint = polygon.getPoint(0);
				type = PathIterator.SEG_MOVETO;
			} else if (index == polygon.points.size()){
				type = PathIterator.SEG_CLOSE;
			} else{
				currentPoint = polygon.getPoint(index);
				type = PathIterator.SEG_LINETO;
			}
//			if (index == 0){
//				currentPoint = polygon.getPoint(0);
//				type = PathIterator.SEG_MOVETO;
//			} else if (index == polygon.points.size()+1){
//				type = PathIterator.SEG_CLOSE;
//			} else if (index == polygon.points.size()){
//				currentPoint = polygon.getPoint(0);
//				type = PathIterator.SEG_LINETO;
//			} else{
//				currentPoint = polygon.getPoint(index);
//				type = PathIterator.SEG_LINETO;
//			}
		}

		public int currentSegment(float[] coords){
			assignPointAndType();
			if (type != PathIterator.SEG_CLOSE){
				if (affine != null){
					float[] singlePointSetFloat = new float[2];
					singlePointSetFloat[0] = (float)currentPoint.x;
					singlePointSetFloat[1] = (float)currentPoint.y;
					affine.transform(singlePointSetFloat, 0, coords, 0, 1);
				} else{
					coords[0] = (float)currentPoint.x;
					coords[1] = (float)currentPoint.y;
				}
			}
			return type;
		}

		public int currentSegment(float[] coords){
			assignPointAndType();
			if (type != PathIterator.SEG_CLOSE){
				if (affine != null){
					singlePointSetDouble[0] = currentPoint.x;
					singlePointSetDouble[1] = currentPoint.y;
					affine.transform(singlePointSetDouble, 0, coords, 0, 1);
				} else{
					coords[0] = currentPoint.x;
					coords[1] = currentPoint.y;
				}
			}
			return type;
		}
	}
****************************/




}
