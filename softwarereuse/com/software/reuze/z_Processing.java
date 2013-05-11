package com.software.reuze;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;

public class z_Processing {
    public PApplet processing;
    public static z_Processing app;
    public static s_i_SoundCallback isound;
    public static final int TRIANGLES = PApplet.TRIANGLES;
    public static final int CLOSE = PApplet.CLOSE;
    public static final int CENTER = PApplet.CENTER;
	public static final int CORNER = PApplet.CORNER;
	public static final int CROSS = PApplet.CROSS;
    public z_Processing(Object o) {
    	processing=(PApplet)o;
    	app=this;
    	if (o instanceof s_i_SoundCallback) isound=(s_i_SoundCallback)o;
    }
	public InputStream createInput(String path) {
		return processing.createInput(path);
	}
	
	public void pushMatrix() {
		processing.pushMatrix();
	}
	public void pushStyle() {
		processing.pushStyle();
	}
	public void popMatrix() {
		processing.popMatrix();
	}
	public void popStyle() {
		processing.popStyle();
	}
	public void noStroke() {
		processing.noStroke();
	}
	public void noFill() {
		processing.noFill();
	}
	public void beginShape() {
		processing.beginShape();
	}
	public void beginShape(int type) {
		processing.beginShape(type);
	}
	public void endShape(int how) {
		processing.endShape(how);
	}
	public void vertex(float x, float y) {
		processing.vertex(x, y);
	}
	public void fill(int fillColor) {
		processing.fill(fillColor);
	}
	public void stroke(int strokeColor) {
		processing.stroke(strokeColor);
	}
	public void strokeWeight(float strokeWeight) {
		processing.strokeWeight(strokeWeight);
	}
	public void translate(float posX, float posY) {
		processing.translate(posX, posY);
	}
	public void rotate(float radians) {
		processing.rotate(radians);
	}
	public void stroke(float rgb, float or, float hsb) {
		processing.stroke(rgb, or, hsb);
	}
	public void ellipse(float x, float y, float width, float height) {
		processing.ellipse(x, y, width, height);
	}
	public float random(float low, float high) {
		return processing.random(low, high);
	}
	public void ellipseMode(int type) {
		processing.ellipseMode(type);
	}
	public void line(float x1, float y1, float x2, float y2) {
		processing.line(x1, y1, x2, y2);
	}
	public void rectMode(int type) {
		processing.rectMode(type);
	}
	public void stroke(float r, float g, float b, float a) {
		processing.stroke(r, g, b, a);
	}
	public void fill(float r, float g, float b, float a) {
		processing.fill(r, g, b, a);
	}
	public void rect(float x, float y, float width, float height) {
		processing.rect(x, y, width, height);
	}
	public void arc(float x, float y, float width, float height, float start, float stop) {
		processing.arc(x, y, width, height, start, stop);
	}
	public void fill(float r, float g, float b) {
		processing.fill(r, g, b);
	}
	public void fill(float i, float j) {
		processing.fill(i, j);
	}
	public Object loadImage(String path) {
		if (path.endsWith(".svg")) return processing.loadShape(path);
		else return processing.loadImage(path);
	}
	public Object loadImage(String path, int x, int y, int w, int h) {
		PImage im=processing.loadImage(path);
		return im.get(x, y, w, h);
	}
	public void image(Object image, float posX, float posY) {
		if (image instanceof PImage) processing.image((PImage)image, posX, posY);
		else processing.shape((PShape)image, posX, posY);
	}
	public void imageMode(int type) {
		processing.imageMode(type);
		processing.shapeMode(type);
	}
	public void image(Object image, float x, float y, float width, float height) {
		if (image instanceof PImage) processing.image((PImage)image, x,y,width,height);
		else processing.shape((PShape)image, x,y,width,height);
	}
	public Object createImage(z_BufferedImage frame, int width, int height) {
		PImage img=new PImage(frame.getWidth(),frame.getHeight(),PConstants.ARGB);
		frame.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
		img.updatePixels();
		img.resize(width,height);
		return img;
	}
	public Integer color(int r, int g, int b, int a) {
		return processing.color(r,g,b,a);
	}
	public void endShape() {
		processing.endShape();
	}
	public void stroke(float f, float g) {
		processing.stroke(f, g);
	}
	public String dataPath(String fileLocation) {
		return processing.sketchPath("data/"+fileLocation);
	}
}
