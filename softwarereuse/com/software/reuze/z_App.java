package com.software.reuze;

import processing.core.PApplet;
import processing.core.PImage;

public class z_App {
	public static PApplet app;
	public static void size(int width, int height, String render, Object thiss) {
		app=(PApplet)thiss;
		app.size(width,height,render);
	}
    public static z_BufferedImage loadImage(String obj) {
    	PImage pi=app.loadImage(obj);
    	pi.loadPixels();
    	z_BufferedImage bi=new z_BufferedImage(pi.width,pi.height,z_BufferedImage.TYPE_INT_ARGB);
    		int[] rgbArray=pi.pixels;
    		for(int i=0;i<pi.width;i++){
    			for(int j=0;j<pi.height;j++){
    				bi.setRGB(j,i,rgbArray[i*pi.height+j]);
    			}
    		}
    		return bi;
    }
}
