package com.software.reuze;

import com.software.reuze.z_BufferedImage;
import java.util.ArrayList;

import com.software.reuze.z_GIFDecoder;



public class vgu_PictureImageAnimatedGif implements vg_i_Renderer {
	protected z_Processing app;
	protected ArrayList<Object> animation= new ArrayList<Object>();
	protected int frame=0;
	
	public vgu_PictureImageAnimatedGif(z_Processing papp) {
		app = papp;
	}

	public vgu_PictureImageAnimatedGif(z_Processing papp, double radius, String path) {
		app = papp;
		z_GIFDecoder d = new z_GIFDecoder();
		d.read(app.createInput(path));
		int n = d.getFrameCount();
		for (int i = 0; i < n; i++) {
			z_BufferedImage frame = d.getFrame(i);  // frame i
			int t = d.getDelay(i);  // display duration of frame in milliseconds
			System.out.println(i+" "+t);
			Object img=app.createImage(frame, (int)radius*2, (int)radius*2);
			animation.add(img);
		}
	}

	public void draw(float posX, float posY, float velX, float velY,
			float headX, float headY, dg_a_EntityBase owner) {
		float angle = (float) Math.atan2(headY, headX);
		app.pushStyle();
		app.pushMatrix();
		app.imageMode(z_Processing.CENTER);
		app.translate(posX, posY);
		app.rotate(angle);
		app.image(animation.get(frame),0,0);
		app.popMatrix();
		app.popStyle();
		frame=(frame+1)%animation.size();
	}
}
