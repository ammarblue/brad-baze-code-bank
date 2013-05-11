package reuze.pending;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.awt.Container;
import java.awt.Font;
//import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;

import reuze.awt.ga_PolygonConverterAwt;
import reuze.awt.ga_PolygonMultiAwt;
import reuze.awt.ib_Accelerated;

import com.software.reuze.ga_AABB;
import com.software.reuze.ga_Polygon;



/**
 *
 * @author Keith
 */
public class WorldLetters extends World{
	public WorldLetters(demoGameShooterMain main){
		super(main);
	}

	public void fillMultiPolygonsList(){
		Container cont = main.getParentFrameOrApplet();
		double contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		double contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);

		// Add some words as shapes
		Graphics2D g2d = (Graphics2D)ib_Accelerated.createTransparentBufferedImage(1, 1).getGraphics();
		FontRenderContext frc = g2d.getFontRenderContext();
		Font font = g2d.getFont().deriveFont(130.0f);
//			Polygon border = Polygon.createRectOblique(0, 0, frameW, 0, 1);
//			Polygon border2 = Polygon.createRectOblique(frameW, 0, frameW, frameH, 1);
//			Polygon border3 = Polygon.createRectOblique(frameW, frameH, 0, frameH, 1);
//			Polygon border4 = Polygon.createRectOblique(0, frameH, 0, 0, 1);
//			allOccluders.add(new OccluderImpl(border));
//			allOccluders.add(new OccluderImpl(border2));
//			allOccluders.add(new OccluderImpl(border3));
//			allOccluders.add(new OccluderImpl(border4));
//			allMultiPolygons.add(new KMultiPolygon(border));
//			allMultiPolygons.add(new KMultiPolygon(border2));
//			allMultiPolygons.add(new KMultiPolygon(border3));
//			allMultiPolygons.add(new KMultiPolygon(border4));

		{
			String text = "Straight";
			TextLayout tl = new TextLayout(text, font, frc);
			//FontMetrics fm = cont.getFontMetrics(font);
			//double height = fm.getHeight();
			//double textW = fm.stringWidth(text);
			Shape textShape = tl.getOutline(null);
			ArrayList<ga_Polygon> polygons = ga_PolygonConverterAwt.makePolygonListFrom(textShape, 1);
			ArrayList<ga_PolygonMultiAwt> multiPolygons = ga_PolygonConverterAwt.makeMultiPolygonListFrom(polygons);
			ga_AABB bigAABB = new ga_AABB();
			for (int i = 0; i < polygons.size(); i++){
				bigAABB = bigAABB.union(polygons.get(i).getAABB());
			}
			double translateX = -bigAABB.w()/2f + contW/2f;
			double translateY = -bigAABB.h()/2f + contH/2f;
			for (int i = 0; i < multiPolygons.size(); i++){
				ga_PolygonMultiAwt polygon = multiPolygons.get(i);
				polygon.translate((float)translateX, (float)translateY);
			}
			allMultiPolygons.addAll(multiPolygons);
		}
		{
			String text = "Edge";
			TextLayout tl = new TextLayout(text, font, frc);
			//FontMetrics fm = cont.getFontMetrics(font);
			//double height = fm.getHeight();
			//double textW = fm.stringWidth(text);
			Shape textShape = tl.getOutline(null);
			ArrayList<ga_Polygon> polygons = ga_PolygonConverterAwt.makePolygonListFrom(textShape, 1);
			ArrayList<ga_PolygonMultiAwt> multiPolygons = ga_PolygonConverterAwt.makeMultiPolygonListFrom(polygons);
			ga_AABB bigAABB = new ga_AABB();
			for (int i = 0; i < polygons.size(); i++){
				bigAABB = bigAABB.union(polygons.get(i).getAABB());
			}
			double translateX = -bigAABB.w()/2f + contW/2f;
			double translateY = +bigAABB.h()/2f + bigAABB.h()/2f + contH/2f;
			for (int i = 0; i < multiPolygons.size(); i++){
				ga_PolygonMultiAwt polygon = multiPolygons.get(i);
				polygon.translate((float)translateX, (float)translateY);
			}
			allMultiPolygons.addAll(multiPolygons);
		}
	}
}
