package reuze.awt;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_PolygonMulti;
import com.software.reuze.ga_Vector2;


public class ga_PolygonMultiAwt extends ga_PolygonMulti implements Shape {
	public ga_PolygonMultiAwt(){
		super();
	}
	public ga_PolygonMultiAwt(ga_Polygon polygon){
		super(polygon);
	}
	public ga_PolygonMultiAwt(ArrayList<ga_Polygon> polygons){
		super(polygons);
	}

	public ga_PolygonMultiAwt(ga_Polygon... polygonArray){
		super(polygonArray);
	}

	public ga_PolygonMulti copy(){
		ArrayList<ga_Polygon> newPolygons = new ArrayList<ga_Polygon>();
		for (int i = 0; i < polygons.size(); i++){
			newPolygons.add(polygons.get(i).copy());
		}
		return new ga_PolygonMulti(newPolygons);
	}
	// Note: The following methods are needed to implement java.awt.geom.Shape.
	public Rectangle2D.Double getBounds2D(){
		float[] bounds = getExteriorPolygon().getBoundsArray();
		return new Rectangle2D.Double(bounds[0], bounds[1], bounds[2], bounds[3]);
	}
	public java.awt.Rectangle getBounds(){
		float[] bounds = getExteriorPolygon().getBoundsArray();
		return new java.awt.Rectangle((int)bounds[0], (int)bounds[1], (int)bounds[2], (int)bounds[3]);
	}
	public boolean intersects(Rectangle2D r){
		return this.intersects((float)r.getX(), (float)r.getY(), (float)r.getWidth(), (float)r.getHeight());
	}
	public boolean contains(Point2D p){
		return contains((float)p.getX(), (float)p.getY());
	}
	public boolean contains(Rectangle2D r){
		return this.contains((float)r.getX(), (float)r.getY(), (float)r.getWidth(), (float)r.getHeight());
	}
	public PathIterator getPathIterator(AffineTransform at){
		return new PolygonMultiIterator(this, at);
	}
	public PathIterator getPathIterator(AffineTransform at, double flatness){
		return new PolygonMultiIterator(this, at);
	}
	public class PolygonMultiIterator implements PathIterator {
		int type = PathIterator.SEG_MOVETO;
		int index = 0;
		int polygonNum = 0;
		ga_Polygon currentPolygon;
		ga_Vector2 currentPoint;
		ga_PolygonMulti multiPolygon;
		AffineTransform affine;

		float[] singlePointSetDouble = new float[2];

		PolygonMultiIterator(ga_PolygonMulti kPolygon) {
			this(kPolygon, null);
		}

		PolygonMultiIterator(ga_PolygonMulti kPolygon, AffineTransform at) {
			this.multiPolygon = kPolygon;
			this.affine = at;
			currentPolygon = multiPolygon.getPolygon(polygonNum);
			currentPoint = currentPolygon.getPoint(0);
		}

		public int getWindingRule() {
			return PathIterator.WIND_EVEN_ODD;
		}
		public boolean isDone() {
			if (polygonNum >= polygons.size()){
				return true;
			}
			return false;
		}

		public void next() {
			index++;
			if (index == currentPolygon.points.size() + 1){
				polygonNum++;
				index = 0;
			}
		}

		public void assignPointAndType(){
			currentPolygon = multiPolygon.getPolygon(polygonNum);
			if (index == 0){
				currentPoint = currentPolygon.getPoint(0);
				type = PathIterator.SEG_MOVETO;
			} else if (index == currentPolygon.points.size()){
				type = PathIterator.SEG_CLOSE;
			} else{
				currentPoint = currentPolygon.getPoint(index);
				type = PathIterator.SEG_LINETO;
			}
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

		public int currentSegment(double[] coords){
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

	public static void main(String[] args){
		ga_Polygon poly1 = ga_Polygon.createRegularPolygon(10, 100);
		ga_Polygon poly2 = ga_Polygon.createRegularPolygon(10, 50);
		poly1.translate(10, 10);
		poly2.translate(10, 10);
		ga_PolygonMultiAwt multiPoly = new ga_PolygonMultiAwt(poly1, poly2);
		Shape shape = ga_PolygonConverterAwt.makePath2DFrom(multiPoly);
		{
			System.out.println("For shape:");
			PathIterator pathIterator = shape.getPathIterator(null);
			float[] coords = new float[6];
			while (pathIterator.isDone() == false){
				int type = pathIterator.currentSegment(coords);
				if (type == PathIterator.SEG_MOVETO){
					System.out.println(": SEG_MOVETO, "+coords[0]+", "+coords[1]);
					pathIterator.next();
				}else if (type == PathIterator.SEG_LINETO){
					System.out.println(": SEG_LINETO, "+coords[0]+", "+coords[1]);
					pathIterator.next();
				}else if (type == PathIterator.SEG_CLOSE){
					System.out.println(": SEG_CLOSE, "+coords[0]+", "+coords[1]);
					pathIterator.next();
				}
			}
		}
		{
			System.out.println("For multiPoly:");
			PathIterator pathIterator = multiPoly.getPathIterator(null);
			float[] coords = new float[6];
			while (pathIterator.isDone() == false){
				int type = pathIterator.currentSegment(coords);
				if (type == PathIterator.SEG_MOVETO){
					System.out.println(": SEG_MOVETO, "+coords[0]+", "+coords[1]);
					pathIterator.next();
				}else if (type == PathIterator.SEG_LINETO){
					System.out.println(": SEG_LINETO, "+coords[0]+", "+coords[1]);
					pathIterator.next();
				}else if (type == PathIterator.SEG_CLOSE){
					System.out.println(": SEG_CLOSE, "+coords[0]+", "+coords[1]);
					pathIterator.next();
				}
			}
		}
	}
}
