package reuze.awt;
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

import java.util.ArrayList;


import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_PolygonMulti;
import com.software.reuze.ga_Vector2;
import com.vividsolutions.jts.geom.*;
	
/**
 *
 * @author Keith Woodward
 */
public class ga_PolygonConverterJts {
	public GeometryFactory geometryFactory = new GeometryFactory();

	public ga_PolygonConverterJts(){
	}
	
	public com.vividsolutions.jts.geom.Polygon makeJTSPolygonFrom(ga_Polygon polygon){
		com.vividsolutions.jts.geom.Polygon jtsPolygon;
		Coordinate[] coordinateArray = new Coordinate[polygon.getPoints().size() + 1];
		for (int i = 0; i < polygon.getPoints().size(); i++){
			ga_Vector2 p = polygon.getPoints().get(i);
			coordinateArray[i] = new Coordinate(p.x, p.y);
		}
		// link the first and last points
		coordinateArray[polygon.getPoints().size()] = new Coordinate(coordinateArray[0].x, coordinateArray[0].y);
		LinearRing linearRing = geometryFactory.createLinearRing(coordinateArray);
		jtsPolygon = new com.vividsolutions.jts.geom.Polygon(linearRing, null, geometryFactory);
		return jtsPolygon;
	}

	public com.vividsolutions.jts.geom.Polygon makeJTSPolygonFrom(ga_PolygonMulti polygon){
		com.vividsolutions.jts.geom.Polygon jtsPolygon;
		ga_Polygon exteriorPolygon = polygon.getExteriorPolygon();
		Coordinate[] coordinateArray = new Coordinate[exteriorPolygon.points.size() + 1];
		for (int i = 0; i < exteriorPolygon.getPoints().size(); i++){
			ga_Vector2 p = exteriorPolygon.getPoints().get(i);
			coordinateArray[i] = new Coordinate(p.x, p.y);
		}
		// link the first and last points
		coordinateArray[exteriorPolygon.getPoints().size()] = new Coordinate(coordinateArray[0].x, coordinateArray[0].y);
		LinearRing exteriorLinearRing = geometryFactory.createLinearRing(coordinateArray);

		
		ArrayList<ga_Polygon> interiorPolygons = polygon.getInteriorPolygonsCopy();
		ArrayList<LinearRing> interiorLinearRings = new ArrayList<LinearRing>(interiorPolygons.size());
		for (int i = 0; i < interiorPolygons.size(); i++){
			ga_Polygon interiorPolygon = interiorPolygons.get(i);
			coordinateArray = new Coordinate[interiorPolygon.points.size() + 1];
			for (int j = 0; j < interiorPolygon.getPoints().size(); j++){
				ga_Vector2 p = interiorPolygon.getPoints().get(j);
				coordinateArray[j] = new Coordinate(p.x, p.y);
			}
			// link the first and last points
			coordinateArray[interiorPolygon.getPoints().size()] = new Coordinate(coordinateArray[0].x, coordinateArray[0].y);
			LinearRing interiorLinearRing = geometryFactory.createLinearRing(coordinateArray);
			interiorLinearRings.add(interiorLinearRing);
		}

		LinearRing[] interiorLinearRingArray = interiorLinearRings.toArray(new LinearRing[interiorLinearRings.size()]);
		jtsPolygon = new com.vividsolutions.jts.geom.Polygon(exteriorLinearRing, interiorLinearRingArray, geometryFactory);
		return jtsPolygon;
	}

	/**
	 * Returns a Polygon of this polygon, or returns null if there was a problem.
	 * For example, if there are less than 3 points, returns null.
	 * If there are more than 3 points but consecutive points have the same coordinates,
	 * and when the duplicates are deleted then there are less than 3 points.
	 * @param jtsPolygon
	 * @return
	 */
	public static ga_Polygon makePolygonFromExterior(com.vividsolutions.jts.geom.Polygon jtsPolygon){
		LineString exteriorRingLineString = jtsPolygon.getExteriorRing();
		ga_Polygon polygon = makePolygonFrom(exteriorRingLineString);
		return polygon;
	}
	public static ga_PolygonMulti makeMultiPolygonFrom(com.vividsolutions.jts.geom.Polygon jtsPolygon){
		LineString exteriorRingLineString = jtsPolygon.getExteriorRing();
		ga_Polygon polygon = makePolygonFrom(exteriorRingLineString);
		if (polygon == null){
			return null;
		}
		ga_PolygonMulti multiPolygon = new ga_PolygonMulti(polygon);
		for (int i = 0; i < jtsPolygon.getNumInteriorRing(); i++){
			LineString interiorRingLineString = jtsPolygon.getInteriorRingN(i);
			ga_Polygon internalPolygon = makePolygonFrom(interiorRingLineString);
			if (internalPolygon == null){
				continue;
			}
			multiPolygon.getPolygons().add(internalPolygon);
		}
		return multiPolygon;
	}
	public static ga_Polygon makePolygonFrom(com.vividsolutions.jts.geom.LineString lineString){
		CoordinateSequence coordinateSequence = lineString.getCoordinateSequence();
		ArrayList<ga_Vector2> points = new ArrayList<ga_Vector2>();
		// The loop stops at the second-last coord since the last coord will be
		// the same as the start coord.
		ga_Vector2 lastAddedPoint = null;
		for (int i = 0; i < coordinateSequence.size()-1; i++){
			Coordinate coord = coordinateSequence.getCoordinate(i);
			ga_Vector2 p = new ga_Vector2(coord.x, coord.y);
			if (lastAddedPoint != null && p.x == lastAddedPoint.x && p.y == lastAddedPoint.y){
				// Don't add the point since it's the same as the last one
//				System.out.println(this.getClass().getSimpleName()+": skipping p == "+p+", lastAddedPoint == "+lastAddedPoint+", i == "+i+", coordinateSequence.size()-1 == "+(coordinateSequence.size()-1));
				continue;
			}else{
				points.add(p);
				lastAddedPoint = p;
			}
		}
		if (points.size() < 3){
			return null;
		}
		ga_Polygon polygon = new ga_Polygon(points);
		return polygon;
	}
	

//	public ArrayList<Polygon> makePolygonsFrom(com.vividsolutions.jts.geom.MultiPolygon jtsMultiPolygon){
//		ArrayList<Polygon> polygons = new ArrayList<Polygon>();
//		int numGeomtries = jtsMultiPolygon.getNumGeometries();
//		for (int i = 0; i < numGeomtries; i++){
//			Geometry geom = jtsMultiPolygon.getGeometryN(i);
//			if (geom instanceof Polygon){
//				Polygon jtsPoly = (Polygon)geom;
//				LineString lineString = jtsPoly.getExteriorRing();
//				CoordinateSequence coordinateSequence = lineString.getCoordinateSequence();
//				ArrayList<Vector2> points = new ArrayList<Vector2>(coordinateSequence.size());
//				// The loop stops at the second-last coord since the last coord will be
//				// the same as the start coord.
//				for (int j = 0; j < coordinateSequence.size()-1; j++){
//					Coordinate coord = coordinateSequence.getCoordinate(j);
//					points.add(new Vector2(coord.x, coord.y));
//				}
//				Polygon poly = new Polygon(points);
//				polygons.add(poly);
//
//			}else{
//				System.out.println(this.getClass().getSimpleName()+": geom.getClass() == "+geom.getClass());
//				System.out.println(this.getClass().getSimpleName()+": numGeomtries == "+numGeomtries);
//				if (geom instanceof LineString){
//					LineString lineString = (LineString)geom;
//					System.out.println(this.getClass().getSimpleName()+": lineString.isRing() == "+lineString.isRing());
//				}
//				//throw new RuntimeException("geom is not a polygon, geom == "+geom);
//			}
//		}
//		return polygons;
//	}

	public static ArrayList<ga_PolygonMulti> makeMultiPolygonListFrom(Geometry geometry){
		ArrayList<ga_PolygonMulti> list = new ArrayList<ga_PolygonMulti>();
		if (geometry instanceof com.vividsolutions.jts.geom.Polygon){
			com.vividsolutions.jts.geom.Polygon jtsIntersectionPoly = (com.vividsolutions.jts.geom.Polygon)geometry;
			ga_PolygonMulti path2D = makeMultiPolygonFrom(jtsIntersectionPoly);
			if (path2D != null){
				list.add(path2D);
			}
		}else if (geometry instanceof com.vividsolutions.jts.geom.MultiPolygon){
			com.vividsolutions.jts.geom.MultiPolygon multiPolygon = (com.vividsolutions.jts.geom.MultiPolygon)geometry;
			addGeometryToMultiPolygonList(multiPolygon, list);
		}else if (geometry instanceof com.vividsolutions.jts.geom.GeometryCollection){
			/* Even though MultiPolygon extends GeometryCollection, sometimes a
			 * buffer or intersection operation will result in random points, in
			 * addition to Polygons and MultiPolygons. For this reason we should
			 * still deal with GeometryCollections, and simply ignore the random points.
			 */
			com.vividsolutions.jts.geom.GeometryCollection geometryCollection = (com.vividsolutions.jts.geom.GeometryCollection)geometry;
			addGeometryToMultiPolygonList(geometryCollection, list);
		}else{
			// Sometimes these are found:
			//com.vividsolutions.jts.geom.Point
			//com.vividsolutions.jts.geom.LineString
		}
		return list;
	}
	public static void addGeometryToMultiPolygonList(Geometry geometry, ArrayList<ga_PolygonMulti> list){
		if (geometry instanceof com.vividsolutions.jts.geom.Polygon){
			com.vividsolutions.jts.geom.Polygon jtsIntersectionPoly = (com.vividsolutions.jts.geom.Polygon)geometry;
			ga_PolygonMulti path2D = makeMultiPolygonFrom(jtsIntersectionPoly);
			if (path2D != null){
				list.add(path2D);
			}
		}else if (geometry instanceof com.vividsolutions.jts.geom.GeometryCollection){
			// GeometryCollection is the super-class of MultiPolygon and
			// MultiLineString and MultiPoint so these ones are taken care of in this code block.
			com.vividsolutions.jts.geom.GeometryCollection geometryCollection = (com.vividsolutions.jts.geom.GeometryCollection)geometry;
			for (int i = 0; i < geometryCollection.getNumGeometries(); i++){
				Geometry internalGeometry = geometryCollection.getGeometryN(i);
				addGeometryToMultiPolygonList(internalGeometry, list);
			}
		}else{
			// Sometimes these are found:
			//com.vividsolutions.jts.geom.Point
			//com.vividsolutions.jts.geom.LineString
			//System.out.println(this.getClass().getSimpleName()+": geometry.getClass() == "+geometry.getClass());
		}
	}

	public GeometryFactory getGeometryFactory() {
		return geometryFactory;
	}
	
	public static ArrayList<ga_PolygonMulti> makeMultiPolygonListFrom(ArrayList<ga_Polygon> polygons){
		ArrayList<ga_PolygonMulti> multiPolygons = new ArrayList<ga_PolygonMulti>();
		OuterLoop:
		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon polygon = polygons.get(i).copy();
			ga_PolygonMulti mpoly = new ga_PolygonMulti(polygon);
			for (int j = 0; j < multiPolygons.size(); j++){
				ga_PolygonMulti mpoly2 = multiPolygons.get(j);
				if (mpoly2.getExteriorPolygon().contains(polygon.getPoint(0))){
					mpoly2.getPolygons().add(polygon);
					continue OuterLoop;
				}else if (polygon.contains(mpoly2.getExteriorPolygon().getPoint(0))){
					multiPolygons.remove(j);
					j--;
					mpoly.getPolygons().add(mpoly2.getExteriorPolygon());
					mpoly.getPolygons().addAll(mpoly2.getInteriorPolygonsCopy());
					// Note that we might need to swallow up other m-polygons too, so keep going.
					continue;
				}
			}
			multiPolygons.add(mpoly);
		}
		return multiPolygons;
	}

	public void printGeometry(Geometry geometry){
		if (geometry instanceof com.vividsolutions.jts.geom.Polygon){
			//com.vividsolutions.jts.geom.Polygon polygon = (com.vividsolutions.jts.geom.Polygon)geometry;
			System.out.println(this.getClass().getSimpleName()+": geometry.getClass() == "+geometry.getClass());
			Coordinate[] coords = geometry.getCoordinates();
			for (int i = 0; i < coords.length; i++){
				System.out.println(this.getClass().getSimpleName()+": coords["+i+"] == "+coords[i].x+", "+coords[i].y);
			}
		}else if (geometry instanceof com.vividsolutions.jts.geom.GeometryCollection){
			// GeometryCollection is the super-class of MultiPolygon and
			// MultiLineString and MultiPoint so these ones are taken care of in this code block.
			com.vividsolutions.jts.geom.GeometryCollection geometryCollection = (com.vividsolutions.jts.geom.GeometryCollection)geometry;
			System.out.println(this.getClass().getSimpleName()+": geometryCollection.getClass() == "+geometryCollection.getClass());
			for (int i = 0; i < geometryCollection.getNumGeometries(); i++){
				Geometry internalGeometry = geometryCollection.getGeometryN(i);
				printGeometry(internalGeometry);
			}
		}else{
			// Sometimes these are found:
			//com.vividsolutions.jts.geom.Point
			//com.vividsolutions.jts.geom.LineString
			System.out.println(this.getClass().getSimpleName()+": geometry.getClass() == "+geometry.getClass());
		}
	}
}
