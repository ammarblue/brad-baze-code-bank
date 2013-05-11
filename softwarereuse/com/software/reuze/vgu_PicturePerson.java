package com.software.reuze;


public class vgu_PicturePerson implements vg_i_Renderer {

	protected z_Processing app;

	protected static final float[] x = new float[] {-0.5f, 0.2f,  0.2f, -0.5f};
	protected static final float[] y = new float[] {0.6f,  1.0f, -1.0f, -0.6f};

	protected int bodyFillCol = 255, headFillCol = 255;;
	protected int strokeCol = 0;
	protected float strokeWeight = 1.0f;

	protected boolean hintsOn;

	public vgu_PicturePerson(z_Processing papp){
		app = papp;
	}

	public vgu_PicturePerson(z_Processing papp, int fill, int hairFill, int stroke, float weight) {
		app = papp;
		bodyFillCol = fill;
		headFillCol = hairFill;
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
		float r=(float)owner.colRadius;
		fillTriangle(0,1,r);
		fillTriangle(1,2,r);
		fillTriangle(2,3,r);
		fillTriangle(3,0,r);
		app.noStroke();
		app.fill(bodyFillCol);

		app.ellipseMode(z_Processing.CENTER);
		app.fill(headFillCol);
		app.ellipse(0,0,r,r);

		app.popMatrix();
		if(hintsOn){
			drawWhiskers(velX, velY, headX, headY, owner);
		}
		app.popStyle();		
	}

	private void fillTriangle(int p1, int p2, float r){
		app.noStroke();
		app.fill(bodyFillCol);
		app.beginShape(z_Processing.TRIANGLES);
		app.vertex(0,0);
		app.vertex(x[p1]*r, y[p1]*r);
		app.vertex(x[p2]*r, y[p2]*r);
		app.endShape(z_Processing.CLOSE);

		app.stroke(strokeCol);
		app.strokeWeight(strokeWeight);
		app.line(x[p1]*r, y[p1]*r, x[p2]*r, y[p2]*r);
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

	private void drawHints(float velX, float velY, float headX, float headY, dg_a_EntityBase owner){
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
			app.rect(0, - dbwidth, dblength, 2 * dbwidth);
		}
	}

	public void setFill(int col){
		bodyFillCol = col;
	}

	public void setShowHints(boolean visible){
		this.hintsOn = visible;
	}



}
