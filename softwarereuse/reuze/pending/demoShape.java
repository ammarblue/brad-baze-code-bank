package reuze.pending;

import com.software.reuze.ga_ShapeCircle;
import com.software.reuze.ga_ShapeCurve;
import com.software.reuze.ga_ShapeEllipse;
import com.software.reuze.ga_ShapeLine;
import com.software.reuze.ga_ShapePolygon;
import com.software.reuze.ga_ShapeRectangle;
import com.software.reuze.ga_ShapeRectangleRounded;
import com.software.reuze.ga_Vector2;
import com.software.reuze.ga_a_Shape;

import processing.core.PApplet;
import processing.core.PImage;

public class demoShape extends PApplet {
	ga_ShapeLine line;
	ga_ShapeRectangle rect;
	ga_ShapeRectangleRounded rrect;
	ga_ShapeCircle circle;
	ga_ShapeEllipse elly;
	ga_ShapePolygon poly;
	ga_ShapeCurve curve;
	PImage img;
	public void setup() {
		size(500,500,P3D);
		line = new ga_ShapeLine(50,50,300,180);
		rect =new ga_ShapeRectangle(100,100,80,200);
		rrect =new ga_ShapeRectangleRounded(225,100,80,200,18f);
		circle =new ga_ShapeCircle(350,50,40);
		poly =new ga_ShapePolygon(10,10,80,80,200,10,60,150);
		elly =new ga_ShapeEllipse(350,350,40,10);
		img=loadImage("pencils.jpg");
		curve = new ga_ShapeCurve(new ga_Vector2(20,300),new ga_Vector2(220,80),new ga_Vector2(240,520),new ga_Vector2(380,390));
	}
	ga_a_Shape shape;
	public void draw() {
		background(0,255,0);
		/*beginShape();
		texture(img);
		vertex(10, 220, 0, 0);
		vertex(80, 205, 100, 0);
		vertex(95, 290, 100, 100);
		vertex(40, 295, 0, 100);
		endShape(CLOSE);
		stroke(255,0,0);
		noFill();
		draw(line,null);
		draw(circle,null);
		fill(0,255,0);
		draw(poly,null);
		draw(elly,null);
		draw(curve,null);*/
		noFill();
		circle.setLocation(mouseX, mouseY);
		if (circle.intersects(rect)) {         //TODO both ops have bugs
			draw(circle.union(rect)[0],null);
		}/* if (circle.intersects(rrect)) {
			draw(rrect.subtract(circle)[0],null);
		}*/ else {
			draw(rrect,null);
			draw(rect,null);
			draw(circle,null);
		}
	}
	/**
     * Draw the outline of the given shape.  Only the vertices are set.  
     * The color has to be set independently of this method.
     * 
     * @param shape The shape to draw.
     */
    public final void draw(ga_a_Shape shape) {        
        float points[] = shape.getPoints();        
        for(int i=0;i<points.length;i+=2) {
        	point(points[i], points[i + 1]);
        }        
        if (shape.closed()) {
        	point(points[0], points[1]);
        }
    }
    /**
     * Draw the outline of the given shape.  Only the vertices are set.  
     * The color has to be set independently of this method.
     * 
     * @param shape The shape to draw.
     * @param fill The fill to apply
     */
    public final void draw(ga_a_Shape shape, /*ShapeFill*/Object fill) {
        float points[] = shape.getPoints();

        //float center[] = shape.getCenter();
        beginShape();
        for(int i=0;i<points.length;i+=2) {
//            fill.colorAt(shape, points[i], points[i + 1]).bind();
//            ga_Vector2f offset = fill.getOffsetAt(shape, points[i], points[i + 1]);
//            GL.glVertex2f(points[i] + offset.x, points[i + 1] + offset.y);
        	/*if (i>0 && (i&3) == 0) {
        		vertex(points[i-2], points[i-1]);
        		vertex(points[i], points[i + 1]);
        	}*/
        	vertex(points[i], points[i + 1]);
        }
        
        if (shape.closed()) endShape(CLOSE);
        else endShape();
//	        fill.colorAt(shape, points[0], points[1]).bind();
//	        Vector2f offset = fill.getOffsetAt(shape, points[0], points[1]);
//	        GL.glVertex2f(points[0] + offset.x, points[1] + offset.y);
        	//vertex(points[points.length-2], points[points.length-1]);
        	//vertex(points[0], points[1]);
    }
}
