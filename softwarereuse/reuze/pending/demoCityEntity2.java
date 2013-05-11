package reuze.pending;


import java.awt.Point;
import java.awt.Rectangle;

import com.software.reuze.dag_GraphEdge;
import com.software.reuze.dag_GraphFloatingEdges;
import com.software.reuze.dag_GraphNode;
import com.software.reuze.dg_EntityDomainRectangular;
import com.software.reuze.dg_EntityObstacle;
import com.software.reuze.dg_EntityVehicle;
import com.software.reuze.dg_GameXMLInput;
import com.software.reuze.dg_Steering;
import com.software.reuze.ga_Vector2;
import com.software.reuze.ga_Vector2D;
import com.software.reuze.pt_StopWatch;
import com.software.reuze.z_Processing;
import com.software.reuze.vgu_PicturePerson;

import processing.core.PApplet;


public class demoCityEntity2 extends PApplet {
	dg_EntityObstacle[] stalls;
	dg_EntityVehicle[] tourists, patrolLeader;

	dag_GraphFloatingEdges routes;
	dag_GraphNode[] nodes;
	dag_GraphNode dest = null;
	dag_GraphEdge[] edges;

	pt_StopWatch watch;

	int nbrTourists = 1000;
	int nbrPatrols = 9;
	int followPatrol = -1;
	int displayBorderX = 80, displayBorderY = 80;
	Rectangle displayArea;
	int count = 0;
	float HALF_CITY_SIZE = 4000;
	boolean draggable = true;
	dg_GameXMLInput gm;

	public void setup() {
		z_Processing app=new z_Processing(this);
		gm=new dg_GameXMLInput();
		gm.loadXML(app, "game2.xml", gm);
		size(gm.width, gm.height);
		if (gm.cursor>=0) cursor(gm.cursor);
		displayArea = new Rectangle(0, 0, 480, 480);

		gm.world.panPixelX(width/2);
		gm.world.panPixelY(height/2);
		gm.world.setScale(0.9f);

		gm.domain = new dg_EntityDomainRectangular(-HALF_CITY_SIZE-80, -HALF_CITY_SIZE-80, HALF_CITY_SIZE+80, HALF_CITY_SIZE+80);
		// Get the navigation map
		routes = gm.routes;
		nodes = routes.toArray();
		edges = routes.toArrayEdges();

		// Create all the moving objects - start with the patrol
		dg_Steering sb = new dg_Steering();	// steering behavior used as base for others
		sb.setWallAvoidDetails(3, 2.8, 20);
		sb.enableBehaviours(dg_Steering.WALL_AVOID);
		// Create the patrols
		patrolLeader = new dg_EntityVehicle[nbrPatrols];
		for (int i = 0; i < patrolLeader.length; i++) {
			makePatrol(i);
		}
		for (dg_EntityVehicle ve:gm.vehicles) gm.world.removeMover(ve);
		gm.vehicles.clear();
		// Now create the tourists
		sb.setWanderDetails(12, 10, 100);
		dg_Steering touristSB = (dg_Steering) sb.clone();
		touristSB.enableBehaviours(dg_Steering.WANDER | dg_Steering.WALL_AVOID);
		touristSB.setWanderDetails(300, 20, 80);
		touristSB.setWallAvoidDetails(3, 2.8, 20);
		tourists = new dg_EntityVehicle[nbrTourists];
		vgu_PicturePerson apic = new vgu_PicturePerson(app, color(255, 160, 255), color(145, 64, 47), color(0), 1f);
		for (int i = 0; i < tourists.length; i++) {
			tourists[i] = new dg_EntityVehicle( new ga_Vector2D(random(-HALF_CITY_SIZE, HALF_CITY_SIZE), random(-HALF_CITY_SIZE, HALF_CITY_SIZE)), 8, 
					new ga_Vector2D(40 * (random(1.0f) - 0.5f), 40 * (random(1.0f) - 0.5)), 40 + 28 * random(1.0f), 
					new ga_Vector2D(random(1.0f) - 0.5f, random(1.0f) - 0.5f), 1, 1, 700);
			tourists[i].setSB((dg_Steering) touristSB.clone());
			tourists[i].setRenderer(apic);
			tourists[i].setWorldDomain(gm.domain);
			gm.world.addMover(tourists[i]);
		}
		frameRate(100);
		watch = new pt_StopWatch(); // last thing to be done in setup
	}

	void makePatrol(int patrolNumber) {
		// Get start and end nodes
		float x0, x1, y0, y1, dx, dy;
		dag_GraphNode start, dest;
		dg_EntityVehicle[] troop=new dg_EntityVehicle[gm.vehicles.size()];
		do {
			x0 = random(-HALF_CITY_SIZE, HALF_CITY_SIZE);
			y0 = random(-HALF_CITY_SIZE, HALF_CITY_SIZE);
			x1 = random(-HALF_CITY_SIZE, HALF_CITY_SIZE);
			y1 = random(-HALF_CITY_SIZE, HALF_CITY_SIZE);
		} 
		while (abs (x1-x0) < 500 && abs(y1-y0) < 500);
		start = routes.getNodeNearest(x0, y0, 0);
		dest = routes.getNodeNearest(x1, y1, 0);

		dg_Steering sb = new dg_Steering();	// steering behavior used as base for others
		sb.setWallAvoidDetails(5, 2.8, 20);
		sb.enableBehaviours(dg_Steering.WALL_AVOID);
		int i=0;
		for (dg_EntityVehicle ve:gm.vehicles) {
			troop[i]=new dg_EntityVehicle(ve);
			troop[i].incPos(start.xf(), start.yf());
			dg_Steering patrolSB = (dg_Steering) sb.clone();
			if (i == 0) {
				patrolSB.enableBehaviours(dg_Steering.PATH);
				patrolSB.setWeight(dg_Steering.ARRIVE, 900);
			}
			else {
				patrolSB.enableBehaviours(dg_Steering.OFFSET_PURSUIT);
				patrolSB.setWeight(dg_Steering.OFFSET_PURSUIT, 30);
				patrolSB.setPursuitOffset(ga_Vector2D.sub(troop[i].getPos(), troop[0].getPos()));
				patrolSB.setAgent(dg_Steering.AGENT_TO_PURSUE, troop[0]);
			}
			troop[i].setSB(patrolSB);
			troop[i].setWorldDomain(gm.domain);
			gm.world.addMover(troop[i]);
			i++;
		}
		troop[0].getSB().addToPath(routes, dest);
		patrolLeader[patrolNumber] = troop[0];
	}

	public void draw() {
		float deltaTime = (float) watch.getElapsedTime();
		updatePatrolRoutes();
		if (followPatrol >= 0) {
			ga_Vector2 p = gm.world.world2pixel(patrolLeader[followPatrol].getPos(), null);
			int dx = 0, dy = 0;
			if (p.x < displayBorderX)
				dx = (int) (displayBorderX - p.x);
			else if (p.x > displayArea.width - displayBorderX)
				dx = (int) (displayArea.width - displayBorderX - p.x);		
			if (p.y < displayBorderY)
				dy = (int) (displayBorderY - p.y);
			else if (p.y > height - displayBorderY)
				dy = (int) (height - displayBorderY - p.y);
			gm.world.panPixelXY(dx, dy);
		}
		background(250);
		pushMatrix();
		translate((float)gm.world.getXoffset(), (float)(gm.world.getYoffset()));
		scale((float) gm.world.getViewScale());
		gm.world.update(deltaTime);
		gm.world.draw();
		popMatrix();
		fill(255, 220, 255);
		noStroke();
		rect(displayArea.width, 0, width - displayArea.width, height);
	}

	void updatePatrolRoutes() {
		dag_GraphNode dest;
		for (int i = 0; i < patrolLeader.length; i++) {
			if (patrolLeader[i].getSB().getPath().size() < 3) {
				float x = random(-HALF_CITY_SIZE, HALF_CITY_SIZE);
				float y = random(-HALF_CITY_SIZE, HALF_CITY_SIZE);
				dest = routes.getNodeNearest(x, y, 0);
				patrolLeader[i].getSB().addToPath(routes, dest);
			}
		}
	}
	public void mousePressed() {
		draggable = (mouseX >= displayArea.x 
				&& mouseY >= displayArea.y 
				&& mouseX < displayArea.x + displayArea.width 
				&& mouseY <= displayArea.y + displayArea.height);
	}

	public void mouseReleased() {
		draggable = false;
	}

	public void mouseDragged() {
		if (draggable && followPatrol < 0)
			gm.world.panPixelXY(mouseX - pmouseX, mouseY - pmouseY);
	}
	double scale=900;
	public void keyTyped() {
		if (Character.isDigit(key)) followPatrol=key=='0'?-1:key-'1';
		else if (key=='u' || key=='d') {
			if (key=='u') scale=Math.min(1800, scale+100); else scale=Math.max(100, scale-100);
			gm.world.setScale(scale/1000);
		}
	}
}
