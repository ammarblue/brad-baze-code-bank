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
//import straightedge.geom.vision.*;
//import straightedge.geom.util.CodeTimer;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import reuze.awt.ga_PolygonAwtShape;
import reuze.awt.ga_PolygonConverterJts;

import com.software.reuze.ga_Cell;
import com.software.reuze.ga_CellArray;
import com.software.reuze.ga_CellLinks;
import com.software.reuze.ga_CellRectangle;
import com.software.reuze.ga_OccluderImpl;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_VPOccluderOccluderIntersection;
import com.software.reuze.ga_Vector2;
import com.software.reuze.ga_VisionData;
import com.software.reuze.ga_VisionFinder;
import com.software.reuze.ga_i_Occluder;
import com.software.reuze.pt_TimeCounter;


import java.awt.geom.Ellipse2D;
import java.awt.image.*;

/**
 *
 * @author Keith
 */
public class demoPathJoin {
	JFrame frame;
	demoPathJoin.ViewPane view;
	volatile boolean keepRunning = true;
	pt_TimeCounter fpsCounter;

	final Object mutex = new Object();
	ArrayList<AWTEvent> events = new ArrayList<AWTEvent>();
	ArrayList<AWTEvent> eventsCopy = new ArrayList<AWTEvent>();
	ga_Vector2 lastMouseMovePoint = new ga_Vector2();

	ga_VisionFinder visionFinder;
	double smallAmount = 0.0001f;
	ga_VisionData performanceCache = null;
	ga_Polygon visiblePolygon = null;

	ga_PolygonConverterJts polygonConverter;
	ArrayList<ga_OccluderImpl> occluders;
	boolean renderSightField;

	ga_CellArray cellArray;

	public demoPathJoin(){
		frame = new JFrame("FogOfWarTest");
		frame.setSize(700, 700);
		//frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		view = new demoPathJoin.ViewPane();
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
		view.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent e){
				synchronized (mutex){
					events.add(e);
				}
			}
		});
		{
			int numPoints = 4;
			float radius = 10;
			ga_Polygon boundaryPolygon = ga_Polygon.createRegularPolygon(numPoints, radius);
			// By making the eye (or light source) slightly offset from (0,0), it will prevent problems caused by collinearity.
			ga_Vector2 eye = new ga_Vector2(smallAmount, smallAmount);
			performanceCache = new ga_VisionData(eye, boundaryPolygon);
			visionFinder = new ga_VisionFinder();
		}
		polygonConverter = new ga_PolygonConverterJts();

		remakeOccluders();

		fpsCounter = new pt_TimeCounter();

		frame.setVisible(true);

		Thread gameLoopThread = new Thread("Game loop thread"){
//			CodeTimer codeTimer = new CodeTimer("GameLoop", CodeTimer.Output.Millis, CodeTimer.Output.Millis);
			public void run(){
				long lastUpdateNanos = System.nanoTime();
				while(keepRunning){
//					codeTimer.click("update");
					long currentNanos = System.nanoTime();
					float seconds = (currentNanos - lastUpdateNanos)/1000000000f;
					update(seconds);
//					codeTimer.click("render");
					fpsCounter.update();
					view.render();
//					codeTimer.click("yield");
					Thread.yield();
//					codeTimer.lastClick();
					lastUpdateNanos = currentNanos;
				}
			}
		};
		gameLoopThread.setDaemon(true);
		gameLoopThread.start();
	}

	public void remakeOccluders(){
		// make the occluders
		// make random rectangles
		Random rand = new Random();
		occluders = new ArrayList<ga_OccluderImpl>();
//		for (int i = 0; i < 4; i++){
//			Vector2 p = new Vector2((float)rand.nextFloat()*frame.getWidth(), (float)rand.nextFloat()*frame.getHeight());
//			Vector2 p2 = new Vector2((float)rand.nextFloat()*frame.getWidth(), (float)rand.nextFloat()*frame.getHeight());
//			float width = 10 + 30*rand.nextFloat();
//			Polygon rect = Polygon.createRectangle(p, p2, width);
//			occluders.add(new OccluderImpl(rect));
//		}
//		// make a cross
//		occluders.add(new OccluderImpl(Polygon.createRectangle(40, 70, 100, 70, 20)));
//		occluders.add(new OccluderImpl(Polygon.createRectangle(70, 40, 70, 100, 20)));
//		// make a star
//		for (int i = 0; i < 4; i++){
//			ArrayList<Vector2> pointList = new ArrayList<Vector2>();
//			int numPoints = 4 + rand.nextInt(4)*2;
//			double angleIncrement = Math.PI*2f/(numPoints*2);
//			float rBig = 40 + rand.nextFloat()*90;
//			float rSmall = 20 + rand.nextFloat()*70;
//			double currentAngle = 0;
//			for (int k = 0; k < numPoints; k++){
//				double x = rBig*Math.cos(currentAngle);
//				double y = rBig*Math.sin(currentAngle);
//				pointList.add(new Vector2((float)x, (float)y));
//				currentAngle += angleIncrement;
//				x = rSmall*Math.cos(currentAngle);
//				y = rSmall*Math.sin(currentAngle);
//				pointList.add(new Vector2((float)x, (float)y));
//				currentAngle += angleIncrement;
//			}
//			Polygon poly = new Polygon(pointList);
//			assert poly.isCounterClockWise();
//			poly.translate(20 + (float)rand.nextFloat()*frame.getWidth(), 20 + (float)rand.nextFloat()*frame.getHeight());
//			occluders.add(new OccluderImpl(poly));
//		}
	}


	public void update(float seconds){
		// copy the events list:
		synchronized(mutex){
			if (events.size() > 0){
				eventsCopy.addAll(events);
				events.clear();
			}
		}
		// process the events
		if (eventsCopy.size() > 0){
			for (int i = 0; i < eventsCopy.size(); i++){
				AWTEvent awtEvent = eventsCopy.get(i);
				if (awtEvent instanceof MouseEvent){
					MouseEvent e = (MouseEvent)awtEvent;
					if (e.getID() == MouseEvent.MOUSE_MOVED){
						lastMouseMovePoint.x = e.getX();
						lastMouseMovePoint.y = e.getY();
					}
				}else if (awtEvent instanceof ComponentEvent){
					ComponentEvent e = (ComponentEvent)awtEvent;
					if (e.getID() == ComponentEvent.COMPONENT_RESIZED){
						this.remakeOccluders();
					}
				}
			}
			eventsCopy.clear();
		}

		// rotate every 2nd occluder
		float rotateSpeed = (float)(Math.PI*2f/16f);
		for (int i = 0; i < occluders.size(); i+=2){
			ga_OccluderImpl occluder = occluders.get(i);
			occluder.getPolygon().rotate(rotateSpeed*seconds);
		}

		// Move the eye and boundaryPolygon to wherever they need to be.
		// By making the eye slightly offset from its integer coordinate by smallAmount,
		// it will prevent problems caused by collinearity.
		performanceCache.eye.set(lastMouseMovePoint.x + smallAmount, lastMouseMovePoint.y + smallAmount);
		performanceCache.boundaryPolygon.translateTo(performanceCache.eye);
		visionFinder.calc(performanceCache, new ArrayList<ga_i_Occluder>(0), new ArrayList<ga_VPOccluderOccluderIntersection>(0), occluders);
		visiblePolygon = performanceCache.visiblePolygon;
		/* Note that the above is a slow way to process shadows - every occluder is
		 * intersected with every other occluder, which is not necessary if some of the
		 * occluders are stationary.
		 */

		// If the viewPane has a non-zero size, create the cell array if it's still null.
		if ((cellArray == null || view.getWidth() != cellArray.getWidth() || view.getHeight() != cellArray.getHeight()) &&
				(view.getWidth() != 0 && view.getHeight() != 0)){
			cellArray = new ga_CellArray(view.getWidth(), view.getHeight());
		}
		if (cellArray != null && performanceCache.visiblePolygon != null){
			cellArray.explore(performanceCache.visiblePolygon);
		}

	}


	public class ViewPane extends JComponent {
		VolatileImage backImage;
		Graphics2D backImageGraphics2D;

		public ViewPane() {
		}

		protected void renderWorld() {
			Graphics2D g = backImageGraphics2D;

			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.GRAY);
			for (int i = 0; i < occluders.size(); i++) {
				g.fill(ga_PolygonAwtShape.getPolygon(occluders.get(i).getPolygon()));
			}
			g.setColor(Color.BLUE);
			for (int i = 0; i < occluders.size(); i++) {
				g.draw(ga_PolygonAwtShape.getPolygon(occluders.get(i).getPolygon()));
			}
			if (performanceCache.visiblePolygon != null){
				g.setColor(Color.WHITE);
				g.fill(ga_PolygonAwtShape.getPolygon(performanceCache.visiblePolygon));
				g.setColor(Color.BLACK);
				g.draw(ga_PolygonAwtShape.getPolygon(performanceCache.visiblePolygon));
			}
//			if (path2D != null){
//				g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.6f));
//				g.fill(path2D);
//			}
			ga_Cell[][] cells = cellArray.getCells();
			if (cells != null){
				for (int i = 0; i < cellArray.getNumRows(); i++){
					ga_Cell[] row = cells[i];
					for (int j = 0; j < cellArray.getNumCols(); j++){
						ga_Cell cell = row[j];
						float r = cellArray.getCellWidthAndHeight()/3f;
						if (cell.isDiscovered()){
							continue;
						}
//						Vector2 p = cell.getPolygon().getCenter();
//						g.fill(new Ellipse2D.Float(p.x - r, p.y - r, 2*r, 2*r));

//						g.draw(cell.getPolygon());

						ga_CellLinks[] points = new ga_CellLinks[4];
						points[0] = cell.getBotLeft();
						points[1] = cell.getBotRight();
						points[2] = cell.getTopRight();
						points[3] = cell.getTopLeft();
						for (int k = 0; k < points.length; k++){
							ga_CellLinks point = points[k];

							ga_CellRectangle link = point.getDownLink();
							if (link != null){
								if (link.isBorder()){
									g.setColor(Color.BLUE);
									ga_Vector2 p = link.getPoint().getPoint();
									ga_Vector2 p2 = link.getPoint2().getPoint();
									//g.draw(new Line2D.Float(p.x, p.y, p2.x, p2.y));
									g.drawLine((int)p.x, (int)p.y, (int)p2.x, (int)p2.y);
								}
							}
							link = point.getUpLink();
							if (link != null){
								if (link.isBorder()){
									g.setColor(Color.BLUE);
									ga_Vector2 p = link.getPoint().getPoint();
									ga_Vector2 p2 = link.getPoint2().getPoint();
									g.drawLine((int)p.x, (int)p.y, (int)p2.x, (int)p2.y);
								}
							}
							link = point.getRightLink();
							if (link != null){
								if (link.isBorder()){
									g.setColor(Color.BLUE);
									ga_Vector2 p = link.getPoint().getPoint();
									ga_Vector2 p2 = link.getPoint2().getPoint();
									g.drawLine((int)p.x, (int)p.y, (int)p2.x, (int)p2.y);
								}
							}
							link = point.getLeftLink();
							if (link != null){
								if (link.isBorder()){
									g.setColor(Color.BLUE);
									ga_Vector2 p = link.getPoint().getPoint();
									ga_Vector2 p2 = link.getPoint2().getPoint();
									g.drawLine((int)p.x, (int)p.y, (int)p2.x, (int)p2.y);
								}
							}
						}
					}
				}
			}

			g.setColor(Color.MAGENTA);
			float r = 1f;
			g.fill(new Ellipse2D.Double(lastMouseMovePoint.x - r, lastMouseMovePoint.y - r, 2*r, 2*r));

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 80, 30);
			g.setColor(Color.WHITE);
			int stringX = 10;
			int stringY = 20;
			int yInc = 15;
			g.drawString("FPS: " + fpsCounter.getFPSRounded(), stringX, stringY);
		}

		protected VolatileImage createVolatileImage() {
			return createVolatileImage(getWidth(), getHeight(), Transparency.OPAQUE);
		}

		protected VolatileImage createVolatileImage(int width, int height, int transparency) {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
			VolatileImage image = null;

			image = gc.createCompatibleVolatileImage(width, height, transparency);

			int valid = image.validate(gc);

			if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
				image = this.createVolatileImage(width, height, transparency);
			}
			//System.out.println(this.getClass().getSimpleName() + ": initiated VolatileImage backImage for quick rendering");
			return image;
		}

		public void render() {
			if (getWidth() <= 0 || getHeight() <= 0) {
				System.out.println(this.getClass().getSimpleName() + ": width &/or height <= 0!!!");
				return;
			}
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
			if (backImage == null || getWidth() != backImage.getWidth() || getHeight() != backImage.getHeight() || backImage.validate(gc) != VolatileImage.IMAGE_OK) {
				backImage = createVolatileImage();
			}
			do {
				int valid = backImage.validate(gc);
				if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
					backImage = createVolatileImage();
				}
				backImageGraphics2D = backImage.createGraphics();
				renderWorld();
				// It's always best to dispose of your Graphics objects.
				backImageGraphics2D.dispose();
			} while (backImage.contentsLost());
			if (getGraphics() != null) {
				getGraphics().drawImage(backImage, 0, 0, null);
				Toolkit.getDefaultToolkit().sync(); // to flush the graphics commands to the graphics card.  see http://www.javagaming.org/forums/index.php?topic=15000.msg119601;topicseen#msg119601
			}
		}



		public Graphics2D getBackImageGraphics2D() {
			return backImageGraphics2D;
		}
	}
	
	public static void main(String[] args){
		new demoPathJoin();
	}


}
