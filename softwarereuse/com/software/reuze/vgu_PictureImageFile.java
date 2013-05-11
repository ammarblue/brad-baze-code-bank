package com.software.reuze;



public class vgu_PictureImageFile implements vg_i_Renderer {

	protected z_Processing app;
	protected Object image;
	
	public vgu_PictureImageFile(z_Processing papp) {
		app = papp;
	}

	public vgu_PictureImageFile(z_Processing papp, String path) {
		app = papp;
		image=app.loadImage(path);
	}

	public vgu_PictureImageFile(z_Processing app2, String path, int x,
			int y, int w, int h) {
		app = app2;
		image=app.loadImage(path,x,y,w,h);
	}

	public void draw(float posX, float posY, float velX, float velY,
			float headX, float headY, dg_a_EntityBase owner) {
		float angle = (float) Math.atan2(headY, headX);
		app.pushStyle();
		app.pushMatrix();
		app.imageMode(z_Processing.CENTER);
		app.translate(posX, posY);
		app.rotate(angle);
		float radius=(float) owner.getColRadius()*2;
		app.image(image,0,0,radius,radius);
		app.popMatrix();
		app.popStyle();
	}

}
