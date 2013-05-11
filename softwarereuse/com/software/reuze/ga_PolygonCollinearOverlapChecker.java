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


//import straightedge.geom.*;

/**
 *
 * @author Keith
 */
public class ga_PolygonCollinearOverlapChecker {
	// Since floating point calculations of distances are never exact,
	// if the difference between two distances are within this minDistDifference they are considered equal.
	public double minDistDifference = 0.0001;
	
	// Distance to move polygon point in method fixCollinearOverlaps.
	public float pointMoveDist = 0.001f;

	public int numRandomTries = 20;

	public ga_PolygonCollinearOverlapChecker(){

	}

	/**
	 * Returns a list of CollinearOverlaps. Any points in the polygons list that
	 * lie between two consecutive points on the polygon (so the points are
	 * collinear and they overlap) can cause erroneous calculations in the method
	 * SightField.intersectSightPolygon. This method lists all collinear overlaps
	 * so you can deal with the problem yourself by moving the polygons.
	 * 
	 * @param polygon
	 * @param polygons
	 * @return
	 */
	public ArrayList<ga_PolygonsThatCollinearOverlap> getCollinearOverlaps(ga_Polygon polygon, ArrayList<ga_Polygon> polygons){
		ArrayList<ga_PolygonsThatCollinearOverlap> collinearOverlaps = new ArrayList<ga_PolygonsThatCollinearOverlap>();
		ArrayList<ga_Vector2> points = polygon.getPoints();
		for (int i = 0; i < points.size(); i++){
			ga_Vector2 p = points.get(i);
			int iPlus = (i+1 >= points.size()-1 ? 0 : i+1);
			ga_Vector2 pPlus = points.get(iPlus);
			double pToPPlusDist = p.dst(pPlus);
			for (int j = 0; j < polygons.size(); j++){
				ga_Polygon polygon2 = polygons.get(j);
				ArrayList<ga_Vector2> points2 = polygon2.getPoints();
				for (int k = 0; k < points2.size(); k++){
					ga_Vector2 p2 = points2.get(k);
					double pToP2Dist = p.dst(p2);
					if (pToP2Dist < pToPPlusDist){
						// It's possible that the points overlap.
						double pPlusToP2Dist = pPlus.dst(p2);
						double sumDist = pPlusToP2Dist + pToP2Dist;
						if (sumDist < pToPPlusDist + minDistDifference && sumDist > pToPPlusDist - minDistDifference){
							// the distances are effectively equal... so the points are collinear and overlapping.
							//System.out.println(this.getClass().getSimpleName()+": collinear! polygon == "+polygon+", pIndex == "+i);
							ga_PolygonsThatCollinearOverlap collinearOverlap = new ga_PolygonsThatCollinearOverlap(polygon, i, iPlus, polygon2, k);
							collinearOverlaps.add(collinearOverlap);
						}
					}

				}
			}
		}
		return collinearOverlaps;
	}
	/**
	 * * Calls getCollinearOverlaps() on each polygon in allPolygons with all other polygons.
	 * Randomly moves the offending polygon points by pointMoveDist so that there
	 * are no collinear overlaps. Note that this will change the center,
	 * circularBound and area of any polygons which have their points moved.
	 *
	 * @param allPolygons
	 */
	public void fixCollinearOverlaps(ArrayList<ga_Polygon> allPolygons){
		ga_PolygonPointShuffler pointShuffler = new ga_PolygonPointShuffler();
		for (int g = 0; g < allPolygons.size(); g++){
			ArrayList<ga_Polygon> otherPolys = new ArrayList<ga_Polygon>();
			for (int h = 0; h < allPolygons.size(); h++){
				if (h == g){
					continue;
				}
				otherPolys.add(allPolygons.get(h));
			}
			ga_PolygonCollinearOverlapChecker coc = new ga_PolygonCollinearOverlapChecker();
			ArrayList<ga_PolygonsThatCollinearOverlap> collinearOverlaps = coc.getCollinearOverlaps(allPolygons.get(g), otherPolys);
			for (int i = 0; i < collinearOverlaps.size(); i++){
				ga_PolygonsThatCollinearOverlap co = collinearOverlaps.get(i);
				ga_Polygon poly = co.getPolygon();
				assert poly.isValidNoLineIntersections() == true;
				pointShuffler.shufflePoint(co.getPolygon(), co.getPointIndex());
				assert poly.isValidNoLineIntersections() == true;
			}
		}
	}

	public double getMinDistDifference() {
		return minDistDifference;
	}

	public void setMinDistDifference(double minDistDifference) {
		this.minDistDifference = minDistDifference;
	}

	public float getPointMoveDist() {
		return pointMoveDist;
	}

	public void setPointMoveDist(float pointMoveDist) {
		this.pointMoveDist = pointMoveDist;
	}

	
}
