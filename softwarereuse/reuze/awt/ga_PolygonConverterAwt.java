package reuze.awt;
import java.util.ArrayList;

import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_PolygonMulti;
import com.software.reuze.ga_Vector2;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;



public class ga_PolygonConverterAwt {
	
	public static Path2D.Double makePath2DFrom(Geometry geometry){
		Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD);
		addGeometryToPath2D(geometry, path);
		return path;
	}

	public static void addGeometryToPath2D(Geometry geometry, Path2D.Double path){
		if (geometry instanceof com.vividsolutions.jts.geom.Polygon){
			com.vividsolutions.jts.geom.Polygon polygon = (com.vividsolutions.jts.geom.Polygon)geometry;
			addPolygonToPath2D(polygon, path);
		}else if (geometry instanceof com.vividsolutions.jts.geom.GeometryCollection){
			// GeometryCollection is the super-class of MultiPolygon and
			// MultiLineString and MultiPoint so these ones are taken care of in this code block.
			com.vividsolutions.jts.geom.GeometryCollection geometryCollection = (com.vividsolutions.jts.geom.GeometryCollection)geometry;
			for (int i = 0; i < geometryCollection.getNumGeometries(); i++){
				Geometry internalGeometry = geometryCollection.getGeometryN(i);
				addGeometryToPath2D(internalGeometry, path);
			}
		}else{
			// Sometimes these are found:
			//com.vividsolutions.jts.geom.Point
			//com.vividsolutions.jts.geom.LineString
			//System.out.println(this.getClass().getSimpleName()+": geometry.getClass() == "+geometry.getClass());
		}
	}

	public static void addPolygonToPath2D(com.vividsolutions.jts.geom.Polygon jtsPolygon, Path2D path){
		LineString lineString = jtsPolygon.getExteriorRing();
		addLineStringToPath2D(lineString, path);
		for (int i = 0; i < jtsPolygon.getNumInteriorRing(); i++){
			lineString = jtsPolygon.getInteriorRingN(i);
			addLineStringToPath2D(lineString, path);
		}
	}

	public static void addLineStringToPath2D(LineString lineString, Path2D path){
		CoordinateSequence coordinateSequence = lineString.getCoordinateSequence();
		if (coordinateSequence.size() == 0){
			// sometimes JTS gives an empty LineString
			return;
		}
		// add the first coord to the path
		Coordinate coord = coordinateSequence.getCoordinate(0);
		path.moveTo(coord.x, coord.y);
		// The loop stops at the second-last coord since the last coord will be
		// the same as the start coord.
		for (int i = 1; i < coordinateSequence.size()-1; i++){
			coord = coordinateSequence.getCoordinate(i);
			path.lineTo(coord.x, coord.y);
		}
		path.closePath();
	}
	
	public static ArrayList<ga_PolygonMultiAwt> makeMultiPolygonListFrom(Shape shape, float flatness){
		ArrayList<ga_Polygon> list = makePolygonListFrom(shape, flatness);
		ArrayList<ga_PolygonMultiAwt> multiPolygon = makeMultiPolygonListFrom(list);
		return multiPolygon;
	}
	
	public static ArrayList<ga_PolygonMultiAwt> makeMultiPolygonListFrom(ArrayList<ga_Polygon> polygons){
		ArrayList<ga_PolygonMultiAwt> multiPolygons = new ArrayList<ga_PolygonMultiAwt>();
		OuterLoop:
		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon polygon = polygons.get(i).copy();
			ga_PolygonMultiAwt mpoly = new ga_PolygonMultiAwt(polygon);
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

	public static ArrayList<ga_Polygon> makePolygonListFrom(Shape shape, float flatness){
		ArrayList<ga_Polygon> list = new ArrayList<ga_Polygon>();
		PathIterator pathIterator = shape.getPathIterator(null, flatness);
		float[] coords = new float[6];
		while (pathIterator.isDone() == false){
			int type = pathIterator.currentSegment(coords);
			if (type == PathIterator.SEG_MOVETO){
				ArrayList<ga_Vector2> points = new ArrayList<ga_Vector2>();
				addPathIteratorPointsToList(pathIterator, points);
				// remove last point if it's the same as the first one.
				while (points.size() >= 2){
					ga_Vector2 firstPoint = points.get(0);
					ga_Vector2 lastPoint = points.get(points.size()-1);
					if (firstPoint.equals(lastPoint)){
						points.remove(points.size()-1);
					}else{
						break;
					}
				}
				ga_Polygon polygon = new ga_Polygon(points, true);
				list.add(polygon);
			}
		}
		return list;
	}

	public static void addPathIteratorPointsToList(PathIterator pathIterator, ArrayList<ga_Vector2> points){
		float[] coords = new float[6];
		while (pathIterator.isDone() == false){
			int type = pathIterator.currentSegment(coords);
			if (type == PathIterator.SEG_MOVETO){
				points.add(new ga_Vector2(coords[0], coords[1]));
				pathIterator.next();
			}else if (type == PathIterator.SEG_LINETO){
				points.add(new ga_Vector2(coords[0], coords[1]));
				pathIterator.next();
			}else if (type == PathIterator.SEG_CLOSE){
				pathIterator.next();
				return;
			}
		}
	}

	public static Path2D.Double makePath2DFrom(ArrayList<ga_PolygonMulti> multiPolygons){
		Path2D.Double path = new Path2D.Double(PathIterator.WIND_EVEN_ODD);
		for (int h = 0; h < multiPolygons.size(); h++){
			ga_PolygonMulti multiPolygon = multiPolygons.get(h);
			ArrayList<ga_Polygon> polygons = multiPolygon.getPolygons();
			for (int i = 0; i < polygons.size(); i++){
				ga_Polygon polygon = polygons.get(i);
				ga_Vector2 p = polygon.getPoint(0);
				path.moveTo(p.x, p.y);
				for (int j = 1; j < polygon.getPoints().size(); j++){
					p = polygon.getPoint(j);
					path.lineTo(p.x, p.y);
				}
				path.closePath();
			}
		}
		return path;
	}
	public static Path2D.Double makePath2DFrom(ga_PolygonMulti multiPolygon){
		Path2D.Double path = new Path2D.Double(PathIterator.WIND_EVEN_ODD);
		ArrayList<ga_Polygon> polygons = multiPolygon.getPolygons();
		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon polygon = polygons.get(i);
			ga_Vector2 p = polygon.getPoint(0);
			path.moveTo(p.x, p.y);
			for (int j = 1; j < polygon.getPoints().size(); j++){
				p = polygon.getPoint(j);
				path.lineTo(p.x, p.y);
			}
			path.closePath();
		}
		return path;
	}
	public static Path2D.Double makePath2DFrom(ga_Polygon polygon){
		Path2D.Double path = new Path2D.Double(PathIterator.WIND_EVEN_ODD);
		ga_Vector2 p = polygon.getPoint(0);
		path.moveTo(p.x, p.y);
		for (int j = 1; j < polygon.getPoints().size(); j++){
			p = polygon.getPoint(j);
			path.lineTo(p.x, p.y);
		}
		path.closePath();
		return path;
	}

}
