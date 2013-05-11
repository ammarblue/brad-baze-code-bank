package reuze.pending;
/*
 * Copyright (c) 2008, Keith Woodward
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Keith Woodward nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */


//import straightedge.geom.*;
//import straightedge.geom.path.*;
//import straightedge.geom.util.*;
import java.util.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

import reuze.awt.ga_PathBlockingObstacleImpl;
import reuze.awt.ga_PolygonAwtShape;

import com.software.reuze.ga_PathData;
import com.software.reuze.ga_PathFinder;
import com.software.reuze.ga_PathNodeConnector;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;


import java.awt.*;
import java.awt.image.*;
import java.awt.geom.Line2D;
/**
 *
 * @author Keith
 */
public class demoPathActiveRendering {
	JFrame frame;
	demoPathActiveRendering.ViewPane view;
	volatile boolean keepRunning = true;
	boolean pause = false;

	final Object mutex = new Object();
	ArrayList<AWTEvent> events = new ArrayList<AWTEvent>();
	ArrayList<AWTEvent> eventsCopy = new ArrayList<AWTEvent>();
	ga_Vector2 lastMouseMovePoint = new ga_Vector2();

	ArrayList<ga_PathBlockingObstacleImpl> stationaryObstacles;
	ga_PathNodeConnector nodeConnector;
	float maxConnectionDistanceBetweenObstacles;
	ga_PathFinder pathFinder;

	Player player;
	Random rand = new Random(11);

	public demoPathActiveRendering(){
		frame = new JFrame(this.getClass().getSimpleName());
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		view = new demoPathActiveRendering.ViewPane();
		frame.add(view);

		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				keepRunning = false;
				System.exit(0);
			}
		});
		frame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				synchronized (mutex){
					events.add(e);
				}
			}
		});
		frame.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e){ synchronized (mutex){ events.add(e); } }
			public void keyReleased(KeyEvent e){ synchronized (mutex){ events.add(e); } }
			public void keyTyped(KeyEvent e){ synchronized (mutex){ events.add(e); } }
		});
		view.addMouseListener(new MouseListener(){
			public void mousePressed(MouseEvent e){ synchronized (mutex){ events.add(e); } }
			public void mouseReleased(MouseEvent e){ synchronized (mutex){ events.add(e); } }
			public void mouseClicked(MouseEvent e){ synchronized (mutex){ events.add(e); } }
			public void mouseEntered(MouseEvent e){ synchronized (mutex){ events.add(e); } }
			public void mouseExited(MouseEvent e){ synchronized (mutex){ events.add(e); } }
		});
		view.addMouseMotionListener(new MouseMotionListener(){
			public void mouseMoved(MouseEvent e){ synchronized (mutex){ events.add(e); } }
			public void mouseDragged(MouseEvent e){ synchronized (mutex){ events.add(e); } }
		});

		init();

		frame.setVisible(true);

		Thread gameLoopThread = new Thread("GameLoop"){
			public void run(){
				long lastUpdateNanos = System.nanoTime();
				while(keepRunning){
					long currentNanos = System.nanoTime();
					float seconds = (currentNanos - lastUpdateNanos)/1000000000f;
					processEvents();
					if (pause != true){
						update(seconds);
					}
					view.render();
					try{ Thread.sleep(1);}catch(Exception e){}
					lastUpdateNanos = currentNanos;
				}
			}
		};
		gameLoopThread.setDaemon(false);
		gameLoopThread.start();
	}

	public void init(){
		ArrayList<ga_Polygon> polygons = makePolygons();
		stationaryObstacles = new ArrayList<ga_PathBlockingObstacleImpl>();
//		// As an alternative, you can also replace the stationaryObstacles ArrayList
//		// with a TileBag which may increase performance for big worlds.
//		AABB aabb = TileArray.getAABB(polygons.toArray());
//		TileArray tileArray = new TileArray(aabb.getBotLeft(), aabb.getTopRight(), 50);
//		stationaryObstacles = new TileBag(tileArray, new Bag());
		
		maxConnectionDistanceBetweenObstacles = 1000f;
		nodeConnector = new ga_PathNodeConnector();

		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon poly = polygons.get(i);
			ga_PathBlockingObstacleImpl obst = ga_PathBlockingObstacleImpl.createObstacleFromInnerPolygon(poly.copy());
			if (obst == null){
				continue;
			}
			stationaryObstacles.add(obst);
			nodeConnector.addObstacle(obst, stationaryObstacles, maxConnectionDistanceBetweenObstacles);
		}

		pathFinder = new ga_PathFinder();

		player = new Player();
		player.pos = new ga_Vector2(100, 100);
		player.target = player.pos.copy();
	}

	public ArrayList<ga_Polygon> makePolygons(){
		ArrayList<ga_Polygon> polygons = new ArrayList<ga_Polygon>();

		// make some rectangles
		for (int i = 0; i < 4; i++){
			ga_Vector2 p = new ga_Vector2((float)rand.nextFloat()*frame.getWidth(), (float)rand.nextFloat()*frame.getHeight());
			ga_Vector2 p2 = new ga_Vector2((float)rand.nextFloat()*frame.getWidth(), (float)rand.nextFloat()*frame.getHeight());
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
			poly.translate(20 + (float)rand.nextFloat()*frame.getWidth(), 20 + (float)rand.nextFloat()*frame.getHeight());
			polygons.add(poly);
		}
		return polygons;
	}

	public void processEvents(){
		synchronized(mutex){
			if (events.size() > 0){
				eventsCopy.addAll(events);
				events.clear();
			}
		}
		if (eventsCopy.size() > 0){
			for (int i = 0; i < eventsCopy.size(); i++){
				AWTEvent awtEvent = eventsCopy.get(i);
				if (awtEvent instanceof MouseEvent){
					MouseEvent e = (MouseEvent)awtEvent;
					if (e.getID() == MouseEvent.MOUSE_MOVED){
						lastMouseMovePoint.x = e.getX();
						lastMouseMovePoint.y = e.getY();
					}else if (e.getID() == MouseEvent.MOUSE_PRESSED){
						lastMouseMovePoint.x = e.getX();
						lastMouseMovePoint.y = e.getY();
						player.target.x = lastMouseMovePoint.x;
						player.target.y = lastMouseMovePoint.y;
					}else if (e.getID() == MouseEvent.MOUSE_DRAGGED){
						lastMouseMovePoint.x = e.getX();
						lastMouseMovePoint.y = e.getY();
						player.target.x = lastMouseMovePoint.x;
						player.target.y = lastMouseMovePoint.y;
					}
				}else if (awtEvent instanceof java.awt.event.KeyEvent){
					KeyEvent e = (KeyEvent)awtEvent;
					if (e.getID() == KeyEvent.KEY_PRESSED){
						if (e.getKeyCode() == KeyEvent.VK_R){
							this.init();
						}
						if (e.getKeyCode() == KeyEvent.VK_P){
							if (pause == true){
								pause = false;
							}else{
								pause = true;
							}
						}
					}
				}else if (awtEvent instanceof ComponentEvent){
					ComponentEvent e = (ComponentEvent)awtEvent;
					if (e.getID() == ComponentEvent.COMPONENT_RESIZED){
						this.init();
					}
				}
			}
			eventsCopy.clear();
		}
	}

	double totalSeconds = 0;
	public void update(float seconds){
		player.update(seconds);
		totalSeconds += seconds;
	}

	public class Player{
		ga_Vector2 pos;
		ga_Vector2 target;
		ga_Vector2 targetAdjusted;
		float maxConnectionDistanceFromPlayerToObstacles;
		ga_PathData pathData;
		float speed;
		float speedX;
		float speedY;
		float moveAngle;
		ga_Vector2 currentTargetPoint = null;

		public Player(){
			maxConnectionDistanceFromPlayerToObstacles = demoPathActiveRendering.this.maxConnectionDistanceBetweenObstacles;
			pathData = new ga_PathData();
			speed = 200;
		}
		public void update(double seconds){
			System.out.println(pos+" "+seconds);
			pos = getNearestPointOutsideOfObstacles(pos);
			targetAdjusted = getNearestPointOutsideOfObstacles(target);
			//pathFinder.calc(pos, targetAdjusted, maxConnectionDistanceFromPlayerToObstacles, nodeConnector, stationaryObstacles.getTileArray(), pathPoints);
			pathData = pathFinder.calc(pos, targetAdjusted, maxConnectionDistanceFromPlayerToObstacles, nodeConnector, stationaryObstacles);

			if (speed == 0){
				return;
			}
			// update the player's position as it travels from point to point along the path.
			double secondsLeft = seconds;
			for (int i = 0; i < pathData.points.size(); i++){
				currentTargetPoint = pathData.points.get(i);
				ga_Vector2 oldPos = new ga_Vector2();
				oldPos.x = pos.x;
				oldPos.y = pos.y;
				//System.out.println(this.getClass().getSimpleName()+": targetX == "+targetX+", x == "+x+", targetY == "+targetY+", y == "+y);
				double distUntilTargetReached = ga_Vector2.dst(currentTargetPoint.x, currentTargetPoint.y, pos.x, pos.y);
				double timeUntilTargetReached = distUntilTargetReached/speed;
				assert timeUntilTargetReached >= 0 : timeUntilTargetReached;
				double xCoordToWorkOutAngle = currentTargetPoint.x - pos.x;
				double yCoordToWorkOutAngle = currentTargetPoint.y - pos.y;
				if (xCoordToWorkOutAngle != 0 || yCoordToWorkOutAngle != 0) {
					double dirAngle = ga_Vector2.findAngle(0f, 0f, (float)xCoordToWorkOutAngle, (float)yCoordToWorkOutAngle);//(float)Math.atan(yCoordToWorkOutAngle/xCoordToWorkOutAngle);
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
					pathData.points.remove(i);
					i--;
				}else{
					//s = t(u + v)/2
					pos.x = (float)(oldPos.x + secondsLeft * speedX);
					pos.y = (float)(oldPos.y + secondsLeft * speedY);
					secondsLeft = 0;
					break;
				}
			}
		}

		public ga_Vector2 getNearestPointOutsideOfObstacles(ga_Vector2 point){
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
	}

	public class ViewPane extends JComponent {
		Image backImage;
		Graphics2D backImageGraphics2D;

		public void render() {
			if (getWidth() <= 0 || getHeight() <= 0) {
				System.out.println(this.getClass().getSimpleName() + ": width &/or height <= 0!!!");
				return;
			}
			if (backImage == null || getWidth() != backImage.getWidth(null) || getHeight() != backImage.getHeight(null)) {
				backImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TRANSLUCENT);
			}
			backImageGraphics2D = (Graphics2D)backImage.getGraphics();
			renderWorld();
			// It's always best to dispose of your Graphics objects.
			backImageGraphics2D.dispose();
			if (getGraphics() != null) {
				getGraphics().drawImage(backImage, 0, 0, null);
				Toolkit.getDefaultToolkit().sync(); // to flush the graphics commands to the graphics card.  see http://www.javagaming.org/forums/index.php?topic=15000.msg119601;topicseen#msg119601
			}
		}

		protected void renderWorld() {
			Graphics2D g = backImageGraphics2D;

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			float backGroundGrey = 77f/255f;
			g.setColor(new Color(backGroundGrey, backGroundGrey, backGroundGrey));
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			float g4 = 0.1f;
			g.setColor(new Color(g4, g4, g4));
			for (int i = 0; i < stationaryObstacles.size(); i++) {
				g.fill(ga_PolygonAwtShape.getPolygon(stationaryObstacles.get(i).getPolygon()));
			}

			g.setColor(Color.LIGHT_GRAY);
			if (player.pathData.points.size() > 0) {
				ga_Vector2 currentPoint = player.pos;
				for (int j = 0; j < player.pathData.points.size(); j++) {
					ga_Vector2 nextPoint = player.pathData.points.get(j);
					g.draw(new Line2D.Double(currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y));
					float d = 5f;
					g.fill(new Ellipse2D.Double(nextPoint.x - d / 2f, nextPoint.y - d / 2f, d, d));
					currentPoint = nextPoint;
				}
			}

			g.setColor(Color.RED);
			double r = 5;
			g.fill(new Ellipse2D.Double(player.pos.x - r, player.pos.y - r, 2*r, 2*r));
		}
	}

	public static void main(String[] args){
		new demoPathActiveRendering();
	}
}
