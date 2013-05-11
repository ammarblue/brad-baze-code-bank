package com.software.reuze;


public class vgu_PictureArrow implements vg_i_Renderer
{

	protected z_Processing app;

	protected int fillCol = 255;
	protected int strokeCol = 0;
	protected float strokeWeight = 1.0f;

	protected boolean hintsOn = false;

	public vgu_PictureArrow(z_Processing papp){
		app = papp;
	}

	public vgu_PictureArrow(z_Processing papp, int fill, int stroke, float weight){
		app = papp;
		fillCol = fill;
		strokeCol = stroke;
		strokeWeight = weight;
	}

	public void draw(float posX, float posY, float velX, float velY,
			float headX, float headY, dg_a_EntityBase owner) {
			float angle = (float) Math.atan2(headY, headX);

			app.pushStyle();
			app.pushMatrix();
			app.translate(posX, posY);

			// Draw arrow head
			app.rotate(angle);

			if(hintsOn){
				drawHints(velX, velY, headX, headY, owner);
			}
			app.strokeWeight(1.3f);
			app.stroke(strokeCol);
			app.strokeWeight(strokeWeight);
			app.fill(fillCol);

			app.beginShape(z_Processing.TRIANGLES);
			float r=(float)owner.colRadius;
			app.vertex(r,0f);
			app.vertex(-0.8f*r,0.6f*r);
			app.vertex(-0.8f*r,-0.6f*r);
			app.endShape(z_Processing.CLOSE);

			app.popMatrix();
			if(hintsOn){
				drawWhiskers(velX, velY, headX, headY, owner);
			}
			app.popStyle();		
	}

	
	
	public void setFill(int col){
		fillCol = col;
	}


	public void setShowHints(boolean visible){
		this.hintsOn = visible;
	}

	private void drawWhiskers(float velX, float velY, float headX, float headY, dg_a_EntityBase owner){
		dg_EntityVehicle m = (dg_EntityVehicle)owner;
		ga_Vector2D p = m.getPos();
		if(m.getSB() != null && m.getSB().isBehaviourOn(dg_i_SteeringConstants.WALL_AVOID)){
			ga_Vector2D[] feelers = m.getSB().getFeelers(m);
			// draw whiskers
			app.stroke(0,192,192);
			app.strokeWeight(1);
			for(int f = 0; f < feelers.length; f++){
				app.line((float)p.x,(float)p.y, (float)feelers[f].x, (float)feelers[f].y);
			}	
		}
	}

	private void drawHints(float velX, float velY, float headX, float headY,dg_a_EntityBase owner){
		dg_EntityVehicle v = (dg_EntityVehicle)owner;
		ga_Vector2D head = v.getHeading();
		ga_Vector2D vel = v.getVelocity();
		if(v.getSB() != null && v.getSB().isBehaviourOn(dg_i_SteeringConstants.WALL_AVOID) == false){
			// draw heading vector
			app.stroke(64, 64, 64, 64);
			app.strokeWeight(6);
			app.line(0, 0, 100, 0);
			// Draw velocity
			float angle = (float) vel.angleBetween(head);
			int dir = vel.sign(head);
			app.pushMatrix();
			app.rotate(dir * angle);
			app.stroke(64,0,0, 128);
			app.strokeWeight(2);
			app.line(0, 0, 80, 0);
			app.popMatrix();
		}
		// Obstacle avoidance box
		if(v.getSB() != null && v.getSB().isBehaviourOn(dg_i_SteeringConstants.OBSTACLE_AVOID)){
			float dblength = (float) (v.getSB().getDetectBoxLength() * (1 + v.getSpeed() / v.getMaxSpeed()));
			float dbwidth = (float) v.getColRadius();
			app.fill(64,64,0, 16);
			app.stroke(0);
			app.strokeWeight(1.0f);
			app.rectMode(z_Processing.CORNER);
			app.rect(0, - dbwidth/2, dblength, dbwidth);
		}
	}

	public void fill(int col){
		fillCol = col;
	}


}
