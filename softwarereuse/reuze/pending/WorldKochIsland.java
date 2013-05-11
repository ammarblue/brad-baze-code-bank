package reuze.pending;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.awt.Container;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import reuze.awt.ga_PolygonMultiAwt;

import com.software.reuze.ga_AABB;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_PolygonCollinearOverlapChecker;
import com.software.reuze.ga_Vector2;
/*import straightedge.geom.AABB;
import straightedge.geom.KMultiPolygon;
import straightedge.geom.Vector2;
import straightedge.geom.Polygon;
import straightedge.geom.vision.CollinearOverlapChecker;*/



/**
 * See book 'The Algorithmic Beauty of Plants' (http://algorithmicbotany.org/papers/#abop)
 * @author Keith
 */
public class WorldKochIsland extends World{
	public WorldKochIsland(demoGameShooterMain main){
		super(main);
	}
	public void fillMultiPolygonsList(){
		Container cont = main.getParentFrameOrApplet();
		double contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		double contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);
		ga_Vector2 center = new ga_Vector2(contW/2f, contH/2f);
		ga_Polygon screenPoly = ga_Polygon.createRect(0f,0f, (float)contW, (float)contH);
		
		ArrayList<ga_Polygon> finalPolygons = new ArrayList<ga_Polygon>();
		ArrayList<ga_Polygon> allPolygons = new ArrayList<ga_Polygon>();
		// From book 'The Algorithmic Beauty of Plants' (http://algorithmicbotany.org/papers/#abop)
		
		// koch island
		String initiator = "F+F+F+F";
		String regex = "F";
		String replacer = "F-F+F+FF-F-F+F";
		int numIterations = 2;
		double dist = 18;
		double width = 5;
		double angleIncrement = Math.PI/2f;
		
		// snow flake
//		String initiator = "F--F--F";
//		String regex = "F";
//		String replacer = "F+F--F+F";
//		int numIterations = 3;
//		double dist = 15;
//		double width = 2;
//		double angleIncrement = Math.PI/3f;
		
		// not good for a map
//		String initiator = "F-F-F-F";
//		String regex = "F";
//		String replacer = "F+F-FF-F-F+F+FF-F-F+F+FF+FF-FF";
//		int numIterations = 2;
//		double dist = 5;
//		double width = 2;
//		double angleIncrement = Math.PI/2f;
		
		// not good for a map
//		String initiator = "F-F-F-F";
//		String regex = "F";
//		String replacer = "FF-F-F-F-F-F+F";
//		int numIterations = 3;
//		double dist = 8;
//		double width = 3;
//		double angleIncrement = Math.PI/2f;
		
//		// Enclosed Koch Curve
//		String initiator = "F-F-F-F";
//		String regex = "F";
//		String replacer = "FF-F--F-F";
//		int numIterations = 3;
//		double dist = 17;
//		double width = 4;
//		double angleIncrement = Math.PI/2f;
		
		String instr = initiator;
		for (int i = 0; i < numIterations; i++){
			instr = instr.replaceAll(regex, replacer);
		}
		// take off the last forward move to make a hole in the boundary.
		instr = instr.substring(0, instr.lastIndexOf(regex)); 
		
//		// Hexagonal Gosper Curve
//		String initiator = "L";
//		char regex = 'L';
//		String replacer = "L+R++R-L--LL-R+";
//		char regex2 = 'R';
//		String replacer2 = "-L+RR++R+L--L-R";
//		int numIterations = 2;
//		double dist = 25;
//		double width = 5;
//		double angleIncrement = Math.PI/3f;
		
//		// Quadratic Gosper Curve
//		String initiator = "L";
//		char regex = 'L';
//		String replacer = "LL-R-R+L+L-R-RL+R+LLR-L+R+LL+R-LR-R-L+L+RR-";
//		char regex2 = 'R';
//		String replacer2 = "+LL-R-R+L+LR+L-RR-L-R+LRR-L-RL+L+R-R-L+L+RR";
//		int numIterations = 2;
//		double dist = 25;
//		double width = 5;
//		double angleIncrement = Math.PI/2f;
		
//		// Sierpinski Gasket
//		String initiator = "R";
//		char regex = 'L';
//		String replacer = "R+L+R";
//		char regex2 = 'R';
//		String replacer2 = "L-R-L";
//		int numIterations = 5;
//		double dist = 14;
//		double width = 5;
//		double angleIncrement = Math.PI/3f;
		
//		// Dragon Curve. not good for maps since there are cells to get stuck in.
//		String initiator = "L";
//		char regex = 'L';
//		String replacer = "L+R+";
//		char regex2 = 'R';
//		String replacer2 = "-L-R";
//		int numIterations = 7;
//		double dist = 25;
//		double width = 5;
//		double angleIncrement = Math.PI/2f;
		
//		StringBuilder instrBuf = new StringBuilder(initiator);
//		for (int i = 0; i < numIterations; i++){
//			for (int j = 0; j < instrBuf.length(); j++){
//				if (instrBuf.charAt(j) == regex){
//					instrBuf.replace(j,j+1,replacer);
//					j += replacer.length()-1;
//				}else if (instrBuf.charAt(j) == regex2){
//					instrBuf.replace(j,j+1,replacer2);
//					j += replacer2.length()-1;
//				}
//			}
//		}
//		String instr = instrBuf.toString();
		
		
		
		double angle = Math.PI/2f;	// direction starts facing up.
		ga_Vector2 p = new ga_Vector2(0,0);
		ga_Vector2 oldP = p.copy();
		for (int i = 0; i < instr.length(); i++){
			char c = instr.charAt(i);
			if (c == ('F') || c == ('L') || c == ('R')){
				p.x += Math.cos(angle)*dist;
				p.y += Math.sin(angle)*dist;
//				System.out.println(this.getClass().getSimpleName()+": p == "+p);
				allPolygons.add(ga_Polygon.createRectOblique(p, oldP, (float)width));
				oldP = p.copy();
			}else if (c == ('+')){
				angle += angleIncrement;
			}else if (c == ('-')){
				angle -= angleIncrement;
			}
		}
		
		// Move the polygons into the middle of the screen, 
		// and if there are to many then chop off the excess ones.
		ga_AABB bounds = ga_AABB.getAABBEnclosingCenterAndRadius(allPolygons.toArray());
		ga_Vector2 centerBounds = bounds.getCenter();
		for (int i = 0; i < allPolygons.size(); i++){
			ga_Polygon poly = allPolygons.get(i);
			poly.translate(center.x-centerBounds.x, center.y-centerBounds.y);
			if (screenPoly.contains(poly)){
				finalPolygons.add(poly);
			}
		}
		
		System.out.println(this.getClass().getSimpleName()+": finalPolygons.size() == "+finalPolygons.size());
		
		ga_PolygonCollinearOverlapChecker coc = new ga_PolygonCollinearOverlapChecker();
		coc.fixCollinearOverlaps(finalPolygons);
		
		for (int i = 0; i < finalPolygons.size(); i++){
			ga_PolygonMultiAwt multiPolygon = new ga_PolygonMultiAwt(finalPolygons.get(i).getPolygon().copy());
			allMultiPolygons.add(multiPolygon);
		}
	}
}
