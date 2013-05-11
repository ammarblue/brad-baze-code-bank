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
public class WorldVogelSpiral extends World{
	public WorldVogelSpiral(demoGameShooterMain main){
		super(main);
	}
	public void fillMultiPolygonsList(){
		Container cont = main.getParentFrameOrApplet();
		double contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		double contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);

		ArrayList<ga_Polygon> allPolys = new ArrayList<ga_Polygon>();

		// vogel spiral (137.5 degrees)
		{
			ga_Vector2 centerOfSpiral = new ga_Vector2(contW/2f, contH/2f);
			double maxN = 100;
			double r = 0;
            double angle = 0;
			double angleConstant = Math.toRadians(137.5);
			for (int n = 0; n < maxN; n++){
				angle = n*angleConstant;
				r = 22*Math.sqrt(n);
				double x = r*Math.cos(angle);
				double y = r*Math.sin(angle);
				ga_Vector2 center = new ga_Vector2((float)x, (float)y);
				ga_Polygon poly = ga_Polygon.createRegularPolygon(12, 8);
				poly.rotate((float)angle);
				poly.translateTo(center);
				poly.translate(centerOfSpiral);
				allPolys.add(poly);
			}
		}
		

		for (int i = 0; i < allPolys.size(); i++){
			ga_PolygonMultiAwt multiPolygon = new ga_PolygonMultiAwt(allPolys.get(i).getPolygon().copy());
			allMultiPolygons.add(multiPolygon);
		}
	}
}
