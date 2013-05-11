package com.software.reuze;


public class vgu_PicturePolygon implements vg_i_Renderer {

	protected z_Processing app;
	protected int fillCol = 255;
	protected int strokeCol = 0;
	protected float strokeWeight = 1.0f;
	
	public vgu_PicturePolygon(z_Processing papp){
		app = papp;
	}

	public vgu_PicturePolygon(z_Processing papp, int fill, int stroke, float weight){
		app = papp;
		fillCol = fill;
		strokeCol = stroke;
		strokeWeight = weight;
	}

	public void setFill(int col){
		fillCol = col;
	}
	
	public void draw(float posX, float posY, float velX, float velY,
			float headX, float headY, dg_a_EntityBase owner) {
		dg_EntityBuilding b = (dg_EntityBuilding)owner;
		ga_Vector2D[] contour = b.getContour();
		if(contour == null) return;
		Integer[] triangle = b.getTriangle();
		app.pushStyle();
		app.pushMatrix();
		app.translate(posX, posY);
		// Draw colored center
		if(fillCol!=0 && triangle != null){
			app.noStroke();
			app.fill(fillCol);
			for(int i = 0 ; i < triangle.length; i+= 3){
				app.beginShape(z_Processing.TRIANGLES);
				app.vertex((float)contour[triangle[i+2]].x, (float)contour[triangle[i+2]].y);
				app.vertex((float)contour[triangle[i+1]].x, (float)contour[triangle[i+1]].y);
				app.vertex((float)contour[triangle[i+0]].x, (float)contour[triangle[i+0]].y);
				app.endShape(z_Processing.CLOSE);
			}
		} // end of fill
		// Draw contour
		if (strokeWeight!=0) {
			app.noFill();
			app.stroke(strokeCol);
			app.strokeWeight(strokeWeight);
			app.beginShape();
			for(int i = 0; i < contour.length; i++){
				app.vertex((float)contour[i].x, (float)contour[i].y);
			}
			app.endShape(z_Processing.CLOSE);
		}
		app.popMatrix();
		app.popStyle();
	}

}
