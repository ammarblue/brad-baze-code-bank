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
//import straightedge.geom.KMultiPolygon;
//import straightedge.geom.Vector2;
//import straightedge.geom.Polygon;



/**
 *
 * @author Keith
 */
public class WorldSpiral extends World{
	public WorldSpiral(demoGameShooterMain main){
		super(main);
	}
	public void fillMultiPolygonsList(){
		Container cont = main.getParentFrameOrApplet();
		double contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		double contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);

		ArrayList<ga_Polygon> allPolys = new ArrayList<ga_Polygon>();


		// constant-spaced spiral
		{
			float pillarH = 6;
			float pillarW = 2;
			ga_Vector2 centerOfSpiral = new ga_Vector2(200, 250);
			int numPointsPerRevolution = 40;//250;
			int numRevolutions = 5;
			double spacing = 20;
			float rSmall = 20;
			float rBig = 200;
			double currentAngle = 0;
			double totalNumPoints = numPointsPerRevolution*numRevolutions;
//			double rIncrement = (rBig-rSmall)/totalNumPoints;
			double maxAngle = numRevolutions*Math.PI*2;
			double r = rSmall;
			for (int j = 0; j < numRevolutions; j++){
				for (int k = 0; k < numPointsPerRevolution; k++){
					double x = r*Math.cos(currentAngle);
					double y = r*Math.sin(currentAngle);
					ga_Vector2 center = new ga_Vector2((float)x, (float)y);
					ga_Polygon poly = ga_Polygon.createRectOblique(0,0, pillarH,0, pillarW);
					poly.rotate((float)currentAngle);
					poly.translateTo(center);
					poly.translate(centerOfSpiral);
					allPolys.add(poly);
//					currentAngle += angleIncrement;
					double newR = rSmall + rBig*currentAngle/maxAngle;
					double avCircumference = 2*Math.PI*(r + newR/2f);
					currentAngle += spacing/(avCircumference)*2*Math.PI;
					r = newR;
				}
			}
		}


//		// constant-angle spiral
//		{
//			float pillarH = 6;
//			float pillarW = 2;
//			Vector2 centerOfSpiral = new Vector2(200, 250);
//			int numPointsPerRevolution = 40;//250;
//			int numRevolutions = 5;
//			double angleIncrement = Math.PI*2f/(numPointsPerRevolution);
//			float rSmall = 20;
//			float rBig = 200;
//			double currentAngle = 0;
//			double totalNumPoints = numPointsPerRevolution*numRevolutions;
//			double rIncrement = (rBig-rSmall)/totalNumPoints;
//			double r = rSmall;
//			for (int j = 0; j < numRevolutions; j++){
//				for (int k = 0; k < numPointsPerRevolution; k++){
//					double x = r*Math.cos(currentAngle);
//					double y = r*Math.sin(currentAngle);
//					Vector2 center = new Vector2((float)x, (float)y);
//					Polygon poly = Polygon.createRectOblique(0,0, pillarH,0, pillarW);
//					poly.rotate((float)currentAngle);
//					poly.translateTo(center);
//					poly.translate(centerOfSpiral);
//					allPolys.add(poly);
//					currentAngle += angleIncrement;
//					r += rIncrement;
//				}
//			}
//		}
//
//		// circle
//		{
//			float pillarH = 5;
//			float pillarW = 1;
//			Vector2 centerOfSpiral = new Vector2(400, 350);
//			int numPoints = 60;//200;
//			double angleIncrement = Math.PI*2f/(numPoints);
//			float rBig = 50;
//			double currentAngle = 0;
//			for (int k = 0; k < numPoints; k++){
//				double x = rBig*Math.cos(currentAngle);
//				double y = rBig*Math.sin(currentAngle);
//				Vector2 center = new Vector2((float)x, (float)y);
//				Polygon poly = Polygon.createRectOblique(0,0, pillarH,0, pillarW);
//				poly.rotate((float)currentAngle);
//				poly.translateTo(center);
//				poly.translate(centerOfSpiral);
//				allPolys.add(poly);
//				currentAngle += angleIncrement;
//			}
//		}
//
//		// wall
//		{
//			float pillarH = 10;
//			float pillarW = 2;
//			Vector2 wallStart = new Vector2(50, 50);
//			Vector2 wallEnd = new Vector2(550, 50);
//			float wallGap = 15;
//			float currentDist = 0;
//			while(currentDist < wallStart.distance(wallEnd)){
//				Vector2 center = wallStart.createPointToward(wallEnd, currentDist);
//				Polygon poly = Polygon.createRectOblique(0,0, pillarH,0, pillarW);
//				poly.rotate((float)(wallStart.findAngle(wallEnd)+Math.PI/2f));
//				poly.translateTo(center);
//				allPolys.add(poly);
//				currentDist += wallGap;
//			}
//		}
//
//		// wall
//		{
//			float pillarH = 10;
//			float pillarW = 2;
//			Vector2 wallStart = new Vector2(50, 80);
//			Vector2 wallEnd = new Vector2(550, 80);
//			float wallGap = 15;
//			float currentDist = 0;
//			while(currentDist < wallStart.distance(wallEnd)){
//				Vector2 center = wallStart.createPointToward(wallEnd, currentDist);
//				Polygon poly = Polygon.createRectOblique(0,0, pillarH,0, pillarW);
//				poly.rotate((float)(wallStart.findAngle(wallEnd)+Math.PI/2f));
//				poly.translateTo(center);
//				allPolys.add(poly);
//				currentDist += wallGap;
//			}
//		}
		for (int i = 0; i < allPolys.size(); i++){
			ga_PolygonMultiAwt multiPolygon = new ga_PolygonMultiAwt(allPolys.get(i).getPolygon().copy());
			allMultiPolygons.add(multiPolygon);
		}
	}
}
