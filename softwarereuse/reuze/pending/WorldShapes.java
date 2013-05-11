package reuze.pending;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.awt.Container;
import java.util.ArrayList;

import reuze.awt.ga_PolygonMultiAwt;

import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;
/*import straightedge.geom.KMultiPolygon;
import straightedge.geom.Vector2;
import straightedge.geom.Polygon;
import straightedge.geom.vision.OccluderImpl;*/



/**
 *
 * @author Keith
 */
public class WorldShapes extends World{
	public WorldShapes(demoGameShooterMain main){
		super(main);
	}
	public void fillMultiPolygonsList(){
		Container cont = main.getParentFrameOrApplet();
		double contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		double contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);
		ArrayList<ga_Polygon> polygons = new ArrayList<ga_Polygon>();
		// make some rectangles
		for (int i = 0; i < 2; i++){
			ga_Vector2 p = new ga_Vector2(innerAABB.p.x + random.nextFloat()*innerAABB.getWidth(), innerAABB.p.y + random.nextFloat()*innerAABB.getHeight());
			ga_Vector2 p2 = new ga_Vector2(innerAABB.p.x + random.nextFloat()*innerAABB.getWidth(), innerAABB.p.y + random.nextFloat()*innerAABB.getHeight());
			float width = 20 + 20*random.nextFloat();
			ga_Polygon rect = ga_Polygon.createRectOblique(p, p2, width);
			polygons.add(rect);
		}

		// make some rectangles
		for (int i = 0; i < 2; i++){
			ga_Vector2 p = new ga_Vector2(innerAABB.p.x + random.nextFloat()*innerAABB.getWidth(), innerAABB.p.y + random.nextFloat()*innerAABB.getHeight());
			ga_Vector2 p2 = new ga_Vector2(innerAABB.p.x + random.nextFloat()*innerAABB.getWidth(), innerAABB.p.y + random.nextFloat()*innerAABB.getHeight());
			float width = 20 + 20*random.nextFloat();
			ga_Polygon rect = ga_Polygon.createRectOblique(p, p2, width);
			polygons.add(rect);
		}
		// make a cross
		polygons.add(ga_Polygon.createRectOblique(40, 70, 100, 70, 20));
		polygons.add(ga_Polygon.createRectOblique(70, 40, 70, 100, 20));
		// make stars
		for (int i = 0; i < 2; i++){
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			int numPoints = 4 + random.nextInt(4)*2;
			double angleIncrement = Math.PI*2f/(numPoints*2);
			float rBig = 40 + random.nextFloat()*90;
			float rSmall = 20 + random.nextFloat()*70;
			double currentAngle = 0;
			for (int k = 0; k < numPoints; k++){
				double x = rBig*Math.cos(currentAngle);
				double y = rBig*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
				x = rSmall*Math.cos(currentAngle);
				y = rSmall*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
			}
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			//poly.translate(20 + (float)random.nextFloat()*aabb.getWidth(), 20 + (float)random.nextFloat()*aabb.getHeight());
			ga_Vector2 p = new ga_Vector2(innerAABB.p.x + random.nextFloat()*innerAABB.getWidth(), innerAABB.p.y + random.nextFloat()*innerAABB.getHeight());
			poly.translateTo(p);
			polygons.add(poly);
		}
		for (int i = 0; i < polygons.size(); i++){
			ga_PolygonMultiAwt multiPolygon = new ga_PolygonMultiAwt(polygons.get(i).getPolygon().copy());
			allMultiPolygons.add(multiPolygon);
		}
	}
}
