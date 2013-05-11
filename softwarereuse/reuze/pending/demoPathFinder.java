package reuze.pending;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import reuze.awt.ga_PathBlockingObstacleImpl;

import com.software.reuze.ga_PathData;
import com.software.reuze.ga_PathFinder;
import com.software.reuze.ga_PathNodeConnector;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;



public class demoPathFinder {
	static ArrayList<ga_PathBlockingObstacleImpl> stationaryObstacles;
	static ga_PathNodeConnector nodeConnector;
	static float maxConnectionDistanceBetweenObstacles;
	static ga_PathFinder pathFinder;
	
	static ga_Vector2 pos;
	static ga_Vector2 target;
	static ga_Vector2 targetAdjusted;
	static float maxConnectionDistanceFromPlayerToObstacles;
	static ga_PathData pathData;
	static float speed;
	static float speedX;
	static float speedY;
	static float moveAngle;
	static ga_Vector2 currentTargetPoint = null;
	static Random rand = new Random(11);
	static float frameWidth=500;
	static float frameHeight=500;
	
	public static void main(String args[]) {
		ArrayList<ga_Polygon> polygons = makePolygons();
		// Connect the obstacles' nodes so that the PathFinder can do its work:
		maxConnectionDistanceBetweenObstacles = 1000f;
		nodeConnector = new ga_PathNodeConnector();
		// Make the obstacles:
		stationaryObstacles = new ArrayList<ga_PathBlockingObstacleImpl>();
		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon poly = polygons.get(i);
			ga_PathBlockingObstacleImpl obst = ga_PathBlockingObstacleImpl.createObstacleFromInnerPolygon(poly.copy());
			if (obst == null){
				continue;
			}
			stationaryObstacles.add(obst);
			nodeConnector.addObstacle(obst, stationaryObstacles, maxConnectionDistanceBetweenObstacles);
		}

		// Initialise the PathFinder
		pathFinder = new ga_PathFinder();
		
		maxConnectionDistanceFromPlayerToObstacles = maxConnectionDistanceBetweenObstacles;
		pathData = new ga_PathData();
		speed = 200;
		pos=new ga_Vector2(100,100);
		target=new ga_Vector2(478,275);
		for (double i=0; i<1.1; i+=0.01) {
			update(i);
			if (pos.x>500)
				System.out.println(pos);
		}

	}
	
	public static ArrayList<ga_Polygon> makePolygons(){
		ArrayList<ga_Polygon> polygons = new ArrayList<ga_Polygon>();

		// make some rectangles
		for (int i = 0; i < 4; i++){
			ga_Vector2 p = new ga_Vector2((float)rand.nextFloat()*frameWidth, (float)rand.nextFloat()*frameHeight);
			ga_Vector2 p2 = new ga_Vector2((float)rand.nextFloat()*frameWidth, (float)rand.nextFloat()*frameHeight);
			float width = 10 + 30*rand.nextFloat();
			ga_Polygon rect = ga_Polygon.createRectOblique(p, p2, width);
			polygons.add(rect);
		}
		// make a cross
		polygons.add(ga_Polygon.createRectOblique(40, 70, 100, 70, 20));
		polygons.add(ga_Polygon.createRectOblique(70, 40, 70, 100, 20));
		// make some stars
		for (int i = 0; i < 4; i++){
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			int numPoints = 4 + rand.nextInt(4)*2;
			double angleIncrement = Math.PI*2f/(numPoints*2);
			float rBig = 40 + rand.nextFloat()*90;
			float rSmall = 20 + rand.nextFloat()*70;
			double currentAngle = 0;
			for (int k = 0; k < numPoints; k++){
				double x = rBig*Math.cos(currentAngle);
				double y = rBig*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
				x = rSmall*Math.cos(currentAngle);
				y = rSmall*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
			}
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.translate(20 + (float)rand.nextFloat()*frameWidth, 20 + (float)rand.nextFloat()*frameHeight);
			polygons.add(poly);
		}
		return polygons;
	}
	
	static 	public ga_Vector2 getNearestPointOutsideOfObstacles(ga_Vector2 point){
		// check that the target point isn't inside any obstacles.
		// if so, move it.
		ga_Vector2 movedPoint = point.copy();
		boolean targetIsInsideObstacle = false;
		int count = 0;
		while (true){
			for (ga_PathBlockingObstacleImpl obst : stationaryObstacles){
				if (obst.getOuterPolygon().contains(movedPoint)){
					targetIsInsideObstacle = true;
					ga_Polygon poly = obst.getOuterPolygon();
					ga_Vector2 p = poly.getBoundaryPointClosestTo(movedPoint);
					if (p != null){
						movedPoint.x = p.x;
						movedPoint.y = p.y;
					}
					assert point != null;
				}
			}
			count++;
			if (targetIsInsideObstacle == false || count >= 3){
				break;
			}
		}
		return movedPoint;
	}
	
	static 	public void update(double seconds){
		pos = getNearestPointOutsideOfObstacles(pos);
		targetAdjusted = getNearestPointOutsideOfObstacles(target);
		// This is where the PathFinder does its work:
		pathData = pathFinder.calc(pos, targetAdjusted, maxConnectionDistanceFromPlayerToObstacles, nodeConnector, stationaryObstacles);

		if (speed == 0){
			return;
		}
		// Update the player's position as it travels from point to point along the path.
		double secondsLeft = seconds;
		ArrayList<ga_Vector2> pathPoints = pathData.points;
		for (int i = 0; i < pathPoints.size(); i++){
			currentTargetPoint = pathPoints.get(i);
			ga_Vector2 oldPos = new ga_Vector2();
			oldPos.x = pos.x;
			oldPos.y = pos.y;
			double distUntilTargetReached = ga_Vector2.dst(currentTargetPoint.x, currentTargetPoint.y, pos.x, pos.y);
			double timeUntilTargetReached = distUntilTargetReached/speed;
			assert timeUntilTargetReached >= 0 : timeUntilTargetReached;
			double xCoordToWorkOutAngle = currentTargetPoint.x - pos.x;
			double yCoordToWorkOutAngle = currentTargetPoint.y - pos.y;
			System.out.println(pos+" "+currentTargetPoint+" "+xCoordToWorkOutAngle+" "+yCoordToWorkOutAngle+" "+secondsLeft);
			if (xCoordToWorkOutAngle != 0 || yCoordToWorkOutAngle != 0) {
				double dirAngle = ga_Vector2.findAngle(0f, 0f, (float)xCoordToWorkOutAngle, (float)yCoordToWorkOutAngle);//(float)Math.atan(yCoordToWorkOutAngle/xCoordToWorkOutAngle);
				System.out.println("angle="+dirAngle);
				moveAngle = (float)dirAngle;
				speedX = (float)Math.cos(moveAngle) * speed;
				speedY = (float)Math.sin(moveAngle) * speed;
			}else{
				speedX = 0f;
				speedY = 0f;
			}
			if (secondsLeft >= timeUntilTargetReached){
				pos.x = currentTargetPoint.x;
				pos.y = currentTargetPoint.y;
				speedX = 0f;
				speedY = 0f;
				secondsLeft -= timeUntilTargetReached;
				assert i == 0 : "i == "+i;
				// remove the current node from the pathNodes list since we've now reached it
				pathPoints.remove(i);
				i--;
			}else{
				pos.x = (float)(oldPos.x + secondsLeft * speedX);
				pos.y = (float)(oldPos.y + secondsLeft * speedY);
				secondsLeft = 0;
				break;
			}
		}
	}
}

