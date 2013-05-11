package com.software.reuze;


public class vgu_PictureCircle implements vg_i_Renderer {

	protected z_Processing app;

	protected int fillCol = 255;
	protected int strokeCol = 0;
	protected float strokeWeight = 1.0f;
	
	protected boolean hintsOn;
	
	public vgu_PictureCircle(z_Processing papp){
		app = papp;
	}

	public vgu_PictureCircle(z_Processing papp, int fill, int stroke, float weight){
		app = papp;
		fillCol = fill;         //==0 means no fill
		strokeCol = stroke;
		strokeWeight = weight;  //==0 means no stroke
	}

	public void draw(float posX, float posY, float velX, float velY,
			float headX, float headY, dg_a_EntityBase owner) {
		app.pushStyle();
		app.pushMatrix();
		app.translate(posX, posY);
		
		if (strokeWeight!=0) {
			app.stroke(strokeCol);
			app.strokeWeight(strokeWeight);
		} else app.noStroke();
		if (fillCol!=0) app.fill(fillCol);	else app.noFill();
		app.ellipseMode(z_Processing.CENTER);
		
		double cr = owner.getColRadius();
		app.ellipse(0, 0, 2*(float)cr, 2*(float)cr);
		
		if(hintsOn)
			drawHints(velX, velY, headX, headY, owner);
		
		app.popMatrix();
		app.popStyle();		
	}

	private void drawHints(float velX, float velY, float headX, float headY, dg_a_EntityBase owner){
		app.stroke(128,0,0);
		app.noFill();
		app.ellipse(0, 0, 2 * (float)owner.getColRadius(), 2 * (float)owner.getColRadius());
	}
	
	public void setShowHints(boolean visible){
		this.hintsOn = visible;
	}



}
