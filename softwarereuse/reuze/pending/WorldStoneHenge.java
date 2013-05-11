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
public class WorldStoneHenge extends World{
	public WorldStoneHenge(demoGameShooterMain main){
		super(main);
	}
	public void fillMultiPolygonsList(){
		Container cont = main.getParentFrameOrApplet();
		double contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		double contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);

		ArrayList<ga_Polygon> allPolys = new ArrayList<ga_Polygon>();
		// circle
		{
			float pillarH = 10;
			float pillarW = 2;
			ga_Vector2 centerOfSpiral = new ga_Vector2(200, 250);
			int numPoints = 40;//250;
			double angleIncrement = Math.PI*2f/(numPoints);
			float rBig = 100;
			double currentAngle = 0;
			for (int k = 0; k < numPoints; k++){
				double x = rBig*Math.cos(currentAngle);
				double y = rBig*Math.sin(currentAngle);
				ga_Vector2 center = new ga_Vector2((float)x, (float)y);
				ga_Polygon poly = ga_Polygon.createRectOblique(0,0, pillarH,0, pillarW);
				poly.rotate((float)currentAngle);
				poly.translateTo(center);
				poly.translate(centerOfSpiral);
				allPolys.add(poly);
				currentAngle += angleIncrement;
			}
		}

		// circle
		{
			float pillarH = 5;
			float pillarW = 1;
			ga_Vector2 centerOfSpiral = new ga_Vector2(400, 350);
			int numPoints = 60;//200;
			double angleIncrement = Math.PI*2f/(numPoints);
			float rBig = 50;
			double currentAngle = 0;
			for (int k = 0; k < numPoints; k++){
				double x = rBig*Math.cos(currentAngle);
				double y = rBig*Math.sin(currentAngle);
				ga_Vector2 center = new ga_Vector2((float)x, (float)y);
				ga_Polygon poly = ga_Polygon.createRectOblique(0,0, pillarH,0, pillarW);
				poly.rotate((float)currentAngle);
				poly.translateTo(center);
				poly.translate(centerOfSpiral);
				allPolys.add(poly);
				currentAngle += angleIncrement;
			}
		}

		// wall
		{
			float pillarH = 10;
			float pillarW = 2;
			ga_Vector2 wallStart = new ga_Vector2(50, 50);
			ga_Vector2 wallEnd = new ga_Vector2(550, 50);
			float wallGap = 15;
			float currentDist = 0;
			while(currentDist < wallStart.dst(wallEnd)){
				ga_Vector2 center = wallStart.createPointToward(wallEnd, currentDist);
				ga_Polygon poly = ga_Polygon.createRectOblique(0,0, pillarH,0, pillarW);
				poly.rotate((float)(wallStart.findAngle(wallEnd)+Math.PI/2f));
				poly.translateTo(center);
				allPolys.add(poly);
				currentDist += wallGap;
			}
		}

		// wall
		{
			float pillarH = 10;
			float pillarW = 2;
			ga_Vector2 wallStart = new ga_Vector2(50, 80);
			ga_Vector2 wallEnd = new ga_Vector2(550, 80);
			float wallGap = 15;
			float currentDist = 0;
			while(currentDist < wallStart.dst(wallEnd)){
				ga_Vector2 center = wallStart.createPointToward(wallEnd, currentDist);
				ga_Polygon poly = ga_Polygon.createRectOblique(0,0, pillarH,0, pillarW);
				poly.rotate((float)(wallStart.findAngle(wallEnd)+Math.PI/2f));
				poly.translateTo(center);
				allPolys.add(poly);
				currentDist += wallGap;
			}
		}
		for (int i = 0; i < allPolys.size(); i++){
			ga_PolygonMultiAwt multiPolygon = new ga_PolygonMultiAwt(allPolys.get(i).getPolygon().copy());
			allMultiPolygons.add(multiPolygon);
		}
	}
}
