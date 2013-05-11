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
public class WorldStarField extends World{
	public WorldStarField(demoGameShooterMain main){
		super(main);
	}
	public void fillMultiPolygonsList(){
		Container cont = main.getParentFrameOrApplet();
		double contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		double contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);


		ArrayList<ga_Polygon> allPolys = new ArrayList<ga_Polygon>();
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++){
				ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
				pointList.add(new ga_Vector2(0, 0));
				pointList.add(new ga_Vector2(15, 5));
				pointList.add(new ga_Vector2(30, 0));
				pointList.add(new ga_Vector2(30, 30));
				pointList.add(new ga_Vector2(0, 30));
				ga_Polygon poly = new ga_Polygon(pointList);
				assert poly.isCounterClockWise();
				poly.translate(60 + 45*i, 50 + 45*j);
				//poly.rotate(i+j);
				allPolys.add(poly);
			}
		}

		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++){
				ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
				int numPoints = 6;
				double angleIncrement = Math.PI*2f/(numPoints*2);
				float rBig = 22;
				float rSmall = 8;
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
				poly.rotate(i+j);
				poly.translate(300 + 45*i, 60 + 45*j);
				allPolys.add(poly);
			}
		}

		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			pointList.add(new ga_Vector2(5, 25));
			pointList.add(new ga_Vector2(25, 25));
			pointList.add(new ga_Vector2(25, 5));
			pointList.add(new ga_Vector2(5, 0));
			pointList.add(new ga_Vector2(30, 0));
			pointList.add(new ga_Vector2(30, 30));
			pointList.add(new ga_Vector2(0, 30));
			pointList.add(new ga_Vector2(0, 0));
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.translate(100, 350);
			poly.scale(3);
			allPolys.add(poly);
		}
		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();

			pointList.add(new ga_Vector2(5, 25));
			pointList.add(new ga_Vector2(25, 25));
			pointList.add(new ga_Vector2(25, 5));
			pointList.add(new ga_Vector2(5, 0));
			pointList.add(new ga_Vector2(30, 0));
			pointList.add(new ga_Vector2(30, 30));
			pointList.add(new ga_Vector2(0, 30));
			pointList.add(new ga_Vector2(0, 25));
			pointList.add(new ga_Vector2(10, 5));
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.translate(100, 550);
			poly.scale(3);
			allPolys.add(poly);
		}
		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			pointList.add(new ga_Vector2(0, 0));
			pointList.add(new ga_Vector2(15, 5));
			pointList.add(new ga_Vector2(30, 0));
			pointList.add(new ga_Vector2(30, 30));
			pointList.add(new ga_Vector2(0, 30));
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.scale(3);
			poly.translate(200, 350);
			allPolys.add(poly);
		}
		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			pointList.add(new ga_Vector2(0, 0));
			pointList.add(new ga_Vector2(15, 5));
			pointList.add(new ga_Vector2(30, 0));
			pointList.add(new ga_Vector2(30, 30));
			pointList.add(new ga_Vector2(0, 30));
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.scale(3);
			poly.rotate(1);
			poly.translate(270, 350);
			allPolys.add(poly);
		}
		{
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			int numPoints = 6;
			double angleIncrement = Math.PI*2f/(numPoints*2);
			float rBig = 22;
			float rSmall = 8;
			double currentAngle = 0;
			for (int i = 0; i < numPoints; i++){
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
			poly.scale(3.5f);
			poly.rotate(2.5f);
			poly.translate(400, 350);
			allPolys.add(poly);
		}
		for (int i = 0; i < allPolys.size(); i++){
			ga_PolygonMultiAwt multiPolygon = new ga_PolygonMultiAwt(allPolys.get(i).getPolygon().copy());
			allMultiPolygons.add(multiPolygon);
		}
	}
}
