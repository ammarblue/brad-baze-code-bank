package reuze.awt;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;



public class ga_PolygonAwtShape extends ga_Polygon implements Shape {
	public ga_PolygonAwtShape(ga_Polygon p) {
		super(p);
	}
	public static ga_PolygonAwtShape getPolygon(ga_Polygon p) {
		ga_PolygonAwtShape pas=new ga_PolygonAwtShape(p);
		return pas;
	}

   	// Note: The following methods are needed to implement java.awt.geom.Shape.
	public boolean contains(double x, double y){
		return contains((float)x, (float)y);
	}
	public boolean contains(double x, double y, double w, double h) {
		return contains((float)x, y, w,h);
	}
	public boolean contains(Point2D p){
		return contains((float)p.getX(), (float)p.getY());
	}
	public boolean contains(Rectangle2D r){
		return this.contains((float)r.getX(), (float)r.getY(), (float)r.getWidth(), (float)r.getHeight());
	}
	public boolean intersects(double x, double y, double w, double h) {
		return intersects((float)x, (float)y, (float)w, (float)h);
	}
	public boolean intersects(Rectangle2D r){
		return this.intersects((float)r.getX(), (float)r.getY(), (float)r.getWidth(), (float)r.getHeight());
	}
	public java.awt.Rectangle getBounds(){
		float[] bounds = getBoundsArray();
		return new java.awt.Rectangle((int)(bounds[0]), (int)(bounds[1]), (int)Math.ceil(bounds[2]), (int)Math.ceil(bounds[3]));
	}
	public Rectangle2D getBounds2D(){
		float[] bounds = getBoundsArray();
		return new Rectangle2D.Double(bounds[0], bounds[1], bounds[2], bounds[3]);
	}
	public PathIterator getPathIterator(AffineTransform at){
		return new PolygonIterator(this, at);
	}
	public PathIterator getPathIterator(AffineTransform at, double flatness){
		return new PolygonIterator(this, at);
	}
	public class PolygonIterator implements PathIterator {
		int type = PathIterator.SEG_MOVETO;
		int index = 0;
		ga_Polygon polygon;
		ga_Vector2 currentPoint;
		AffineTransform affine;
		
		float[] singlePointSetDouble = new float[2];
		
		PolygonIterator(ga_Polygon kPolygon) {
			this(kPolygon, null);
		}
		
		PolygonIterator(ga_Polygon kPolygon, AffineTransform at) {
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
	
}
