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


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.awt.Image;

import reuze.awt.ga_PathBlockingObstacleImpl;
import reuze.awt.ga_PolygonAwtShape;
import reuze.awt.ga_PolygonConverterAwt;
import reuze.awt.ga_PolygonConverterJts;
import reuze.awt.ga_PolygonMultiAwt;
import reuze.awt.ib_Accelerated;

import com.software.reuze.d_Bag;
import com.software.reuze.ga_AABB;
import com.software.reuze.ga_OccluderImpl;
import com.software.reuze.ga_PathFinder;
import com.software.reuze.ga_PathNode;
import com.software.reuze.ga_TileBag;
import com.software.reuze.ga_Vector2;
import com.software.reuze.ga_i_Occluder;



/*import straightedge.geom.AABB;
import straightedge.geom.KMultiPolygon;
import straightedge.geom.Vector2;
import straightedge.geom.PolygonConverter;
import straightedge.geom.path.KNode;
import straightedge.geom.path.PathBlockingObstacleImpl;
import straightedge.geom.path.PathFinder;
import straightedge.geom.util.Bag;
import straightedge.geom.util.TileBag;
import straightedge.geom.vision.Occluder;
import straightedge.geom.vision.OccluderImpl;*/

/**
 *
 * @author Keith
 */
public class ViewShooter {
	demoGameShooterMain main;
	ga_Vector2 viewCenterInScreenCoords = new ga_Vector2();
	boolean hasRendered = false;

	ga_Vector2 viewCenterInWorldCoords = new ga_Vector2();
	ga_AABB viewRectInWorldCoords = new ga_AABB();

	double scaleFactor = 1;

	protected AffineTransform originalAT;
	protected AffineTransform worldViewAT;
	protected AffineTransform worldViewATRounded;

	public boolean antialias = true;
	public boolean renderConnections = false;
	ib_Accelerated shadowImage;

	Color fogColor = null;
	{
		float gray = 0.25f;
		fogColor = new Color(gray,gray,gray,(1-gray));
	}
	Color backGroundColor = null;
	{
		float gray = 128/255f;
		backGroundColor = new Color(gray,gray,gray,1);
	}

	int WIRE_FRAME = 0;
	int ALL = 1;
	int CLIPPED = 2;
	int FOG_OF_WAR = 3;
	int FOG_OF_WAR_NO_CLIP = 4;
	int paintMode = ALL;

	public ViewShooter(demoGameShooterMain main) {
		this.main = main;
		hasRendered = false;
		scaleFactor = 1;

		//createCustomCursors();
		//main.viewPane.setCursor(blackCrosshairCursor);
	}

	public int getWidth(){
		return main.viewPane.getWidth();
	}
	public int getHeight(){
		return main.viewPane.getHeight();
	}

	public void render(){
		Image image = main.viewPane.getBackImage();
		if (image == null){
			return;
		}
		World world = main.world;
		EventHandler eventHandler = main.eventHandler;
		Loop loop = main.loop;
		d_Bag<ga_OccluderImpl> allOccluders = world.allOccluders.bag;
		ArrayList<ga_PolygonMultiAwt> allMultiPolygons = world.allMultiPolygons;
		ga_TileBag<ga_PathBlockingObstacleImpl> allObstacles = world.allObstacles;
		PlayerTarget player = world.player;
		ArrayList<PlayerTarget> enemies = world.enemies;
		ArrayList<Bullet> bullets = world.bullets;

		Graphics2D g = (Graphics2D)image.getGraphics();

		viewCenterInScreenCoords.x = (getWidth() / 2.0f);
		viewCenterInScreenCoords.y = (getHeight() / 2.0f);
		double scaledW = getWidth() / scaleFactor;
		double scaledH = getHeight() / scaleFactor;

		// for the stationary world view
		if (hasRendered == false){
			hasRendered = true;
			viewCenterInWorldCoords.x = viewCenterInScreenCoords.x;
			viewCenterInWorldCoords.y = viewCenterInScreenCoords.y;
		}
		double viewRectInWorldCoordsX = viewCenterInWorldCoords.x - scaledW / 2.0;
		double viewRectInWorldCoordsY = viewCenterInWorldCoords.y - scaledH / 2.0;
		double viewRectInWorldCoordsW = scaledW;
		double viewRectInWorldCoordsH = scaledH;
		viewRectInWorldCoords.setFromXYWH((float)viewRectInWorldCoordsX, (float)viewRectInWorldCoordsY, (float)viewRectInWorldCoordsW, (float)viewRectInWorldCoordsH);

//		// for the player-centered view.
//		//Note that if you use this, the mouse movement requires changes to work properly...
//		double playerViewX = player.getPos().getX();
//		double playerViewY = player.getPos().getY();
//		double xOffset = 0;
//		double yOffset = 0;
//		viewCenterInWorldCoords.x = playerViewX - (xOffset / scaleFactor);
//		viewCenterInWorldCoords.y = playerViewY - (yOffset / scaleFactor);
//		double viewRectInWorldCoordsX = playerViewX - (scaledW/2f) - (xOffset/scaleFactor);
//		double viewRectInWorldCoordsY = playerViewY + (scaledH/2f) - (yOffset/scaleFactor);
//		double viewRectInWorldCoordsW = scaledW;
//		double viewRectInWorldCoordsH = scaledH;
//		viewRectInWorldCoords.setFromXYWH(viewRectInWorldCoordsX, viewRectInWorldCoordsY, viewRectInWorldCoordsW, viewRectInWorldCoordsH);


		originalAT = g.getTransform();
		// Here we make some commonly used AffineTransforms.
		// Note that for graphical performance on non-accelerated graphics computers, it's
		// better to not do anything tricky with transforms, such as scaling, rotating,
		// or translating by non-integer amounts...

		{
			// Make the worldViewAT
			worldViewAT = new AffineTransform(originalAT);
			worldViewAT.translate(viewCenterInScreenCoords.x, viewCenterInScreenCoords.y);
			if (scaleFactor != 1){
				worldViewAT.scale(scaleFactor, scaleFactor);
			}
			worldViewAT.translate(-viewCenterInWorldCoords.x, -viewCenterInWorldCoords.y);

			// Make the worldViewATRounded
			worldViewATRounded = new AffineTransform(originalAT);
			worldViewATRounded.translate(viewCenterInScreenCoords.x, viewCenterInScreenCoords.y);
			if (scaleFactor != 1){
				worldViewATRounded.scale(scaleFactor, scaleFactor);
			}
			worldViewATRounded.translate(-viewCenterInWorldCoords.x, -viewCenterInWorldCoords.y);
			// This does the rounding:
			double[] roundedMatrix = new double[6];
			worldViewATRounded.getMatrix(roundedMatrix);
			roundedMatrix[4] = Math.round(roundedMatrix[4]);
			roundedMatrix[5] = Math.round(roundedMatrix[5]);
			worldViewATRounded.setTransform(roundedMatrix[0], roundedMatrix[1], roundedMatrix[2], roundedMatrix[3], roundedMatrix[4], roundedMatrix[5]);
		}

		if (paintMode == WIRE_FRAME){
			g.setTransform(originalAT);
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());

			doAntiAlias(g);

			g.setTransform(worldViewAT);
			Stroke oldStroke = g.getStroke();
			if (scaleFactor != 1){
				g.setStroke(new BasicStroke((float)(1/scaleFactor)));
			}

			for (int i = 0; i < allOccluders.size(); i++) {
				ga_i_Occluder occluder = allOccluders.get(i);
				g.setColor(Color.GRAY);
				g.fill(ga_PolygonAwtShape.getPolygon(occluder.getPolygon()));
//				KPolygon poly = occluder.getPolygon();
//				for (int j = 0; j < poly.getPoints().size(); j++){
//					Vector2 p = poly.getPoints().get(j);
//					double r = 1;
//					g.setColor(Color.GRAY.darker());
//					g.fill(new Ellipse2D.Double(p.x - r, p.y - r, 2*r, 2*r));
////					g.drawString(""+j, (float)p.x, (float)p.y);
//				}
			}

			if (player.cache.getVisiblePolygon() != null){
				g.setColor(Color.WHITE);
				g.fill(ga_PolygonAwtShape.getPolygon(player.cache.getVisiblePolygon()));
			}
			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget enemy = enemies.get(i);
				if (enemy.cache.getVisiblePolygon() != null){
					g.setColor(Color.WHITE);
					g.fill(ga_PolygonAwtShape.getPolygon(enemy.cache.getVisiblePolygon()));
				}
			}
			if (player.cache.getVisiblePolygon() != null){
				g.setColor(Color.BLACK);
				g.draw(ga_PolygonAwtShape.getPolygon(player.cache.getVisiblePolygon()));
			}
			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget enemy = enemies.get(i);
				if (enemy.cache.getVisiblePolygon() != null){
					g.setColor(Color.BLACK);
					g.draw(ga_PolygonAwtShape.getPolygon(enemy.cache.getVisiblePolygon()));
				}
			}
			Font oldFont = g.getFont();
			g.setFont(g.getFont().deriveFont((float)(g.getFont().getSize()/scaleFactor)));
			g.setColor(Color.RED);
			for (int i = 0; i < allObstacles.size(); i++) {
				g.draw(ga_PolygonAwtShape.getPolygon(allObstacles.get(i).getInnerPolygon()));
			}
			g.setColor(Color.MAGENTA);
			for (int i = 0; i < allObstacles.size(); i++) {
				g.draw(ga_PolygonAwtShape.getPolygon(allObstacles.get(i).getOuterPolygon()));
			}
			g.setFont(oldFont);

			if (renderConnections){
				ga_PathFinder pathFinder = player.targetFinder.pathFinder;
				pathFinder.debug = true;
				// render connections between reachableNodes
				g.setColor(Color.DARK_GRAY);
				for (int i = 0; i < allObstacles.size(); i++) {
					for (int j = 0; j < allObstacles.get(i).getNodes().size(); j++) {
						ga_PathNode currentNode = allObstacles.get(i).getNodes().get(j);
						for (ga_PathNode n : currentNode.getConnectedNodes()) {
							g.draw(new Line2D.Double(currentNode.getPoint().x, currentNode.getPoint().y, n.getPoint().x, n.getPoint().y));
						}
					}
				}
				// render connections between tempReachableNodes
				g.setColor(Color.BLUE);
				ga_Vector2 startPoint = pathFinder.startPointDebug;
				for (ga_PathNode n : pathFinder.startNodeTempReachableNodesDebug) {
					g.draw(new Line2D.Double(startPoint.x, startPoint.y, n.getPoint().x, n.getPoint().y));
				}
				ga_Vector2 endPoint = pathFinder.endPointDebug;
				for (ga_PathNode n : pathFinder.endNodeTempReachableNodesDebug) {
					g.draw(new Line2D.Double(endPoint.x, endPoint.y, n.getPoint().x, n.getPoint().y));
				}
			}else{
				player.targetFinder.pathFinder.debug = false;
			}

			g.setColor(Color.MAGENTA);
			float r = (float)(1/scaleFactor);
			g.fill(new Ellipse2D.Double(eventHandler.lastMousePointInWorldCoords.x - r, eventHandler.lastMousePointInWorldCoords.y - r, 2*r, 2*r));

			{
				PlayerTarget p = player;
				g.setColor(Color.MAGENTA.darker());
				ArrayList<ga_Vector2> points = p.targetFinder.pathData.points;
				if (points.size() > 0) {
					ga_Vector2 currentPoint = p.getPos();
					for (int j = 0; j < points.size(); j++) {
						ga_Vector2 nextPoint = points.get(j);
						g.draw(new Line2D.Double(currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y));
						float d = 5f;
						g.draw(new Ellipse2D.Double(nextPoint.x - d / 2f, nextPoint.y - d / 2f, d, d));
						currentPoint = nextPoint;
					}
				}
				ga_Vector2 targetPoint = p.targetFinder.getAbsoluteTarget();
				float d = 7f;
				g.draw(new Ellipse2D.Double(targetPoint.x - d / 2f, targetPoint.y - d / 2f, d, d));
			}
			g.setColor(Color.PINK.darker());
			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget p = enemies.get(i);
				ArrayList<ga_Vector2> points = p.targetFinder.pathData.points;
				if (points.size() > 0) {
					ga_Vector2 currentPoint = p.getPos();
					for (int j = 0; j < points.size(); j++) {
						ga_Vector2 nextPoint = points.get(j);
						g.draw(new Line2D.Double(currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y));
						float d = 5f;
						g.fill(new Ellipse2D.Double(nextPoint.x - d / 2f, nextPoint.y - d / 2f, d, d));
						currentPoint = nextPoint;
					}
				}
				ga_Vector2 targetPoint = p.targetFinder.getAbsoluteTarget();
				float d = 7f;
				g.draw(new Ellipse2D.Double(targetPoint.x - d / 2f, targetPoint.y - d / 2f, d, d));
			}

			{
				PlayerTarget p = player;
				g.setColor(Color.BLUE);
				r = 3;
				g.draw(ga_PolygonAwtShape.getPolygon(player.polygon));
				g.setColor(Color.BLACK);
				g.draw(new Line2D.Double(p.pos.x, p.pos.y, p.pos.x+Math.cos(p.gun.angle)*p.gun.length, p.pos.y+Math.sin(p.gun.angle)*p.gun.length));
			}
			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget p = enemies.get(i);
				g.setColor(Color.RED);
				g.draw(ga_PolygonAwtShape.getPolygon(p.polygon));
				g.setColor(Color.BLACK);
				g.draw(new Line2D.Double(p.pos.x, p.pos.y, p.pos.x+Math.cos(p.gun.angle)*p.gun.length, p.pos.y+Math.sin(p.gun.angle)*p.gun.length));
				//g.fill(new Ellipse2D.Double(enemy.getPos().x - r, enemy.getPos().y - r, 2*r, 2*r));
			}

			g.setColor(Color.BLACK);
			for (int i = 0; i < bullets.size(); i++){
				Bullet b = bullets.get(i);
				g.fill(new Ellipse2D.Double(b.x - b.radius, b.y - b.radius, b.radius*2, b.radius*2));
			}
			g.setStroke(oldStroke);
		}else if (paintMode == CLIPPED){
			g.setTransform(originalAT);
			g.setColor(backGroundColor);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setTransform(worldViewAT);
			doAntiAlias(g);

			Shape oldClip = g.getClip();

			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget p = enemies.get(i);
				if (p.cache.getVisiblePolygon() != null &&
						player.cache.visiblePolygon.intersectionPossible(p.cache.visiblePolygon) &&
						player.cache.visiblePolygon.intersects(p.cache.visiblePolygon)){
					ga_PolygonConverterJts polygonConverter = new ga_PolygonConverterJts();
					com.vividsolutions.jts.geom.Polygon jtsPoly = polygonConverter.makeJTSPolygonFrom(p.cache.getVisiblePolygon());
					com.vividsolutions.jts.geom.Polygon jtsPoly2 = polygonConverter.makeJTSPolygonFrom(player.cache.getVisiblePolygon());
					com.vividsolutions.jts.geom.Geometry jtsIntersection = jtsPoly.intersection(jtsPoly2);
					Path2D.Double intersectionPath2D = ga_PolygonConverterAwt.makePath2DFrom(jtsIntersection);
					if (intersectionPath2D == null){
						//System.out.println(this.getClass().getSimpleName()+": intersectionPath2D == null");
						continue;
					}
//						g.setColor(Color.red);
//						g.draw(intersectionPath2D);
					g.setClip(intersectionPath2D);

					AffineTransform oldAT = g.getTransform();
					g.translate(p.cache.getEye().x, p.cache.getEye().y);
					g.rotate(p.cache.getBoundaryPolygonRotationAroundEye());
					g.translate(p.originalBoundaryPolygonAABB.getX() - p.cache.getOriginalEye().x, p.originalBoundaryPolygonAABB.getY() - p.cache.getOriginalEye().y);
					Image vi = p.ai.getImage();
					g.drawImage(vi, 0, 0, null);
					g.setTransform(oldAT);
					g.setColor(Color.BLACK);
					g.draw(new Line2D.Double(p.pos.x, p.pos.y, p.pos.x+Math.cos(p.gun.angle)*p.gun.length, p.pos.y+Math.sin(p.gun.angle)*p.gun.length));
					g.setClip(oldClip);
				}
			}

			float g4 = 0.1f;
			g.setColor(new Color(g4, g4, g4));
			for (int i = 0; i < allMultiPolygons.size(); i++) {
				g.fill(allMultiPolygons.get(i));
			}

			{
				PlayerTarget p = player;
				if (p.cache.getVisiblePolygon() != null){
					g.setClip(ga_PolygonAwtShape.getPolygon(p.cache.visiblePolygon));
				}
				AffineTransform oldAT = g.getTransform();
				g.translate(p.cache.getEye().x, p.cache.getEye().y);
				g.rotate(p.cache.getBoundaryPolygonRotationAroundEye());
				g.translate(p.originalBoundaryPolygonAABB.getX() - p.cache.getOriginalEye().x, p.originalBoundaryPolygonAABB.getY() - p.cache.getOriginalEye().y);
				Image vi = p.ai.getImage();
				g.drawImage(vi, 0, 0, null);
				g.setTransform(oldAT);
				g.setClip(oldClip);
				g.setColor(Color.BLACK);
				g.draw(new Line2D.Double(p.pos.x, p.pos.y, p.pos.x+Math.cos(p.gun.angle)*p.gun.length, p.pos.y+Math.sin(p.gun.angle)*p.gun.length));
			}

			g.setClip(ga_PolygonAwtShape.getPolygon(player.cache.visiblePolygon));
			if (player.dead){
				g.setColor(Color.BLUE.darker().darker());
			}else{
				g.setColor(Color.BLUE);
			}
			g.fill(ga_PolygonAwtShape.getPolygon(player.polygon));
			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget enemy = enemies.get(i);
				if (enemy.dead){
					g.setColor(Color.RED.darker().darker());
				}else{
					g.setColor(Color.RED);
				}
				g.fill(ga_PolygonAwtShape.getPolygon(enemy.polygon));
			}

			g.setColor(Color.BLACK);
			for (int i = 0; i < bullets.size(); i++){
				Bullet b = bullets.get(i);
				g.fill(new Ellipse2D.Double(b.x - b.radius, b.y - b.radius, b.radius*2, b.radius*2));
			}
			g.setClip(oldClip);

		}else if (paintMode == ALL){
			g.setTransform(originalAT);
			g.setColor(backGroundColor);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setTransform(worldViewAT);
			//g.setTransform(playerCenteredAT);
			doAntiAlias(g);

			ArrayList<PlayerTarget> allPlayers = new ArrayList<PlayerTarget>();
			allPlayers.addAll(enemies);
			allPlayers.add(player);
			for (int i = 0; i < allPlayers.size(); i++){
				PlayerTarget p = allPlayers.get(i);
				Shape oldClip = g.getClip();
				if (p.cache.getVisiblePolygon() != null){
					g.setClip(ga_PolygonAwtShape.getPolygon(p.cache.getVisiblePolygon()));
				}
				AffineTransform oldAT = g.getTransform();
				g.translate(p.cache.getEye().x, p.cache.getEye().y);
				g.rotate(p.cache.getBoundaryPolygonRotationAroundEye());
				g.translate(p.originalBoundaryPolygonAABB.getX() - p.cache.getOriginalEye().x, p.originalBoundaryPolygonAABB.getY() - p.cache.getOriginalEye().y);
				Image vi = p.ai.getImage();
				g.drawImage(vi, 0, 0, null);
				g.setTransform(oldAT);
				g.setClip(oldClip);
				g.setColor(Color.BLACK);
				g.draw(new Line2D.Double(p.pos.x, p.pos.y, p.pos.x+Math.cos(p.gun.angle)*p.gun.length, p.pos.y+Math.sin(p.gun.angle)*p.gun.length));
			}

			float g4 = 0.1f;
			g.setColor(new Color(g4, g4, g4));
			for (int i = 0; i < allMultiPolygons.size(); i++) {
				g.fill(allMultiPolygons.get(i));
			}


			if (player.dead){
				g.setColor(Color.BLUE.darker().darker());
			}else{
				g.setColor(Color.BLUE);
			}
			g.fill(ga_PolygonAwtShape.getPolygon(player.polygon));
			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget enemy = enemies.get(i);
				if (enemy.dead){
					g.setColor(Color.RED.darker().darker());
				}else{
					g.setColor(Color.RED);
				}
				g.fill(ga_PolygonAwtShape.getPolygon(enemy.polygon));
			}

			g.setColor(Color.BLACK);
			for (int i = 0; i < bullets.size(); i++){
				Bullet b = bullets.get(i);
				g.fill(new Ellipse2D.Double(b.x - b.radius, b.y - b.radius, b.radius*2, b.radius*2));
			}
		}else if (paintMode == FOG_OF_WAR){
			g.setTransform(originalAT);
			g.setColor(backGroundColor);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setTransform(worldViewAT);
			doAntiAlias(g);

			Shape oldClip = g.getClip();

			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget p = enemies.get(i);
				if (p.cache.getVisiblePolygon() != null &&
						player.cache.visiblePolygon.intersectionPossible(p.cache.visiblePolygon) &&
						player.cache.visiblePolygon.intersects(p.cache.visiblePolygon)){
					ga_PolygonConverterJts polygonConverter = new ga_PolygonConverterJts();
					com.vividsolutions.jts.geom.Polygon jtsPoly = polygonConverter.makeJTSPolygonFrom(p.cache.getVisiblePolygon());
					com.vividsolutions.jts.geom.Polygon jtsPoly2 = polygonConverter.makeJTSPolygonFrom(player.cache.getVisiblePolygon());
					com.vividsolutions.jts.geom.Geometry jtsIntersection = jtsPoly.intersection(jtsPoly2);
					Path2D.Double intersectionPath2D = ga_PolygonConverterAwt.makePath2DFrom(jtsIntersection);
					if (intersectionPath2D == null){
						System.out.println(this.getClass().getSimpleName()+": intersectionPath2D == null");
						continue;
					}
					g.setClip(intersectionPath2D);

					AffineTransform oldAT = g.getTransform();
					g.translate(p.cache.getEye().x, p.cache.getEye().y);
					g.rotate(p.cache.getBoundaryPolygonRotationAroundEye());
					g.translate(p.originalBoundaryPolygonAABB.getX() - p.cache.getOriginalEye().x, p.originalBoundaryPolygonAABB.getY() - p.cache.getOriginalEye().y);
					Image vi = p.ai.getImage();
					g.drawImage(vi, 0, 0, null);
					g.setTransform(oldAT);
					g.setColor(Color.BLACK);
					g.draw(new Line2D.Double(p.pos.x, p.pos.y, p.pos.x+Math.cos(p.gun.angle)*p.gun.length, p.pos.y+Math.sin(p.gun.angle)*p.gun.length));
					g.setClip(oldClip);
				}
			}

			float g4 = 0.1f;
			g.setColor(new Color(g4, g4, g4));
			for (int i = 0; i < allMultiPolygons.size(); i++) {
				g.fill(allMultiPolygons.get(i));
			}

			if (shadowImage == null || getWidth() != shadowImage.getWidth() || getHeight() != shadowImage.getHeight()){
				shadowImage = new ib_Accelerated(getWidth(), getHeight());
			}
			Graphics2D imgGraphics = (Graphics2D)shadowImage.getImage().getGraphics();
			Composite oldComposite = imgGraphics.getComposite();
			imgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
			imgGraphics.setColor(fogColor);
			imgGraphics.fillRect(0,0,shadowImage.width,shadowImage.height);
			imgGraphics.setTransform(worldViewAT);

			Graphics2D graphics = imgGraphics;
			{
				PlayerTarget p = player;
				if (p.cache.getVisiblePolygon() != null){
					graphics.setClip(ga_PolygonAwtShape.getPolygon(p.cache.visiblePolygon));
				}
				AffineTransform oldAT = graphics.getTransform();
				graphics.translate(p.cache.getEye().x, p.cache.getEye().y);
				graphics.rotate(p.cache.getBoundaryPolygonRotationAroundEye());
				graphics.translate(p.originalBoundaryPolygonAABB.getX() - p.cache.getOriginalEye().x, p.originalBoundaryPolygonAABB.getY() - p.cache.getOriginalEye().y);
				Image vi = p.ai2.getImage();
				graphics.drawImage(vi, 0, 0, null);
				graphics.setTransform(oldAT);
				graphics.setClip(oldClip);
				graphics.setColor(Color.BLACK);
				graphics.draw(new Line2D.Double(p.pos.x, p.pos.y, p.pos.x+Math.cos(p.gun.angle)*p.gun.length, p.pos.y+Math.sin(p.gun.angle)*p.gun.length));
			}
			g.setTransform(originalAT);
			g.drawImage(shadowImage.getImage(),0,0,null);
			g.setTransform(worldViewAT);

			g.setClip(ga_PolygonAwtShape.getPolygon(player.cache.visiblePolygon));

			if (player.dead){
				g.setColor(Color.BLUE.darker().darker());
			}else{
				g.setColor(Color.BLUE);
			}
			g.fill(ga_PolygonAwtShape.getPolygon(player.polygon));
			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget enemy = enemies.get(i);
				if (enemy.dead){
					g.setColor(Color.RED.darker().darker());
				}else{
					g.setColor(Color.RED);
				}
				g.fill(ga_PolygonAwtShape.getPolygon(enemy.polygon));
			}

			g.setColor(Color.BLACK);
			for (int i = 0; i < bullets.size(); i++){
				Bullet b = bullets.get(i);
				g.fill(new Ellipse2D.Double(b.x - b.radius, b.y - b.radius, b.radius*2, b.radius*2));
			}
			g.setClip(oldClip);

		}else if (paintMode == FOG_OF_WAR_NO_CLIP){
			g.setTransform(originalAT);
			g.setColor(backGroundColor);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setTransform(worldViewAT);
			doAntiAlias(g);

			Shape oldClip = g.getClip();

			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget p = enemies.get(i);
				g.setClip(ga_PolygonAwtShape.getPolygon(p.cache.visiblePolygon));

				AffineTransform oldAT = g.getTransform();
				g.translate(p.cache.getEye().x, p.cache.getEye().y);
				g.rotate(p.cache.getBoundaryPolygonRotationAroundEye());
				g.translate(p.originalBoundaryPolygonAABB.getX() - p.cache.getOriginalEye().x, p.originalBoundaryPolygonAABB.getY() - p.cache.getOriginalEye().y);
				Image vi = p.ai.getImage();
				g.drawImage(vi, 0, 0, null);
				g.setTransform(oldAT);
				g.setColor(Color.BLACK);
				g.draw(new Line2D.Double(p.pos.x, p.pos.y, p.pos.x+Math.cos(p.gun.angle)*p.gun.length, p.pos.y+Math.sin(p.gun.angle)*p.gun.length));
				g.setClip(oldClip);
			}

			float g4 = 0.1f;
			g.setColor(new Color(g4, g4, g4));
			for (int i = 0; i < allMultiPolygons.size(); i++) {
				g.fill(allMultiPolygons.get(i));
			}

			if (shadowImage == null || getWidth() != shadowImage.getWidth() || getHeight() != shadowImage.getHeight()){
				shadowImage = new ib_Accelerated(getWidth(), getHeight());
			}
			Graphics2D imgGraphics = (Graphics2D)shadowImage.getImage().getGraphics();
			Composite oldComposite = imgGraphics.getComposite();
			imgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
			imgGraphics.setColor(fogColor);
			imgGraphics.fillRect(0,0,shadowImage.width,shadowImage.height);
			imgGraphics.setTransform(worldViewAT);

			Graphics2D graphics = imgGraphics;
			{
				PlayerTarget p = player;
				if (p.cache.getVisiblePolygon() != null){
					graphics.setClip(ga_PolygonAwtShape.getPolygon(p.cache.visiblePolygon));
				}
				AffineTransform oldAT = graphics.getTransform();
				graphics.translate(p.cache.getEye().x, p.cache.getEye().y);
				graphics.rotate(p.cache.getBoundaryPolygonRotationAroundEye());
				graphics.translate(p.originalBoundaryPolygonAABB.getX() - p.cache.getOriginalEye().x, p.originalBoundaryPolygonAABB.getY() - p.cache.getOriginalEye().y);
				Image vi = p.ai2.getImage();
				graphics.drawImage(vi, 0, 0, null);
				graphics.setTransform(oldAT);
				graphics.setClip(oldClip);
				graphics.setColor(Color.BLACK);
				graphics.draw(new Line2D.Double(p.pos.x, p.pos.y, p.pos.x+Math.cos(p.gun.angle)*p.gun.length, p.pos.y+Math.sin(p.gun.angle)*p.gun.length));
			}
			g.setTransform(originalAT);
			g.drawImage(shadowImage.getImage(),0,0,null);
			g.setTransform(worldViewAT);

			if (player.dead){
				g.setColor(Color.BLUE.darker().darker());
			}else{
				g.setColor(Color.BLUE);
			}
			g.fill(ga_PolygonAwtShape.getPolygon(player.polygon));
			for (int i = 0; i < enemies.size(); i++){
				PlayerTarget enemy = enemies.get(i);
				if (enemy.dead){
					g.setColor(Color.RED.darker().darker());
				}else{
					g.setColor(Color.RED);
				}
				g.fill(ga_PolygonAwtShape.getPolygon(enemy.polygon));
			}

			g.setColor(Color.BLACK);
			for (int i = 0; i < bullets.size(); i++){
				Bullet b = bullets.get(i);
				g.fill(new Ellipse2D.Double(b.x - b.radius, b.y - b.radius, b.radius*2, b.radius*2));
			}
			g.setClip(oldClip);

		}

		g.setTransform(originalAT);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 80, 30);
		g.setColor(Color.WHITE);
		int stringX = 10;
		int stringY = 20;
		int yInc = 15;
		g.drawString("FPS: " + loop.fpsCounter.getFPSRounded(), stringX, stringY);
		stringY += 20;
		g.setColor(Color.BLACK);
		g.fillRect(0, 30, 80, 20);
		g.setColor(Color.WHITE);
		g.drawString("Millis: " + loop.fpsCounter.getAvTimeBetweenUpdatesMillis(), stringX, stringY);
		stringY += 20;

		main.viewPane.displayBackImage();
	}


	protected void doAntiAlias(Graphics2D g){
		if (antialias){
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}else{
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}


	public double getWorldCoordFromViewPaneCoordX(double x){
		PlayerTarget player = main.world.player;
		double mx = ((x - viewCenterInScreenCoords.x) / scaleFactor + player.getPos().x);
		return mx;
	}
	public double getWorldCoordFromViewPaneCoordY(double y){
		PlayerTarget player = main.world.player;
		double my = ((y - viewCenterInScreenCoords.y) / scaleFactor + player.getPos().y);
		return my;
	}
	public ga_Vector2 getWorldPointFromViewPanePoint(ga_Vector2 viewPanePoint){
		return new ga_Vector2(getWorldCoordFromViewPaneCoordX(viewPanePoint.x), getWorldCoordFromViewPaneCoordY(viewPanePoint.y));
	}

	public double getViewPaneCoordFromWorldCoordX(double x){
		PlayerTarget player = main.world.player;
		double mx = (x - player.getPos().x)*scaleFactor + viewCenterInScreenCoords.x;
		return mx;
	}
	public double getViewPaneCoordFromWorldCoordY(double y){
		PlayerTarget player = main.world.player;
		double my = (y - player.getPos().y)*scaleFactor + viewCenterInScreenCoords.y;
		return my;
	}
	public ga_Vector2 getViewPanePointFromWorldPoint(ga_Vector2 viewPanePoint){
		return new ga_Vector2(getViewPaneCoordFromWorldCoordX(viewPanePoint.x), getViewPaneCoordFromWorldCoordY(viewPanePoint.y));
	}


//	Cursor whiteCrosshairCursor = null;
//	Cursor blackCrosshairCursor = null;
//	public void createCustomCursors(){
//		// whiteCrosshairCursor:
//		Toolkit toolkit = Toolkit.getDefaultToolkit();
//		int preferredWidthAndHeight = 18;
//		Dimension dimension = toolkit.getBestCursorSize(preferredWidthAndHeight, preferredWidthAndHeight);
//		{
//			BufferedImage img = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
//			Graphics2D g2D = (Graphics2D)img.getGraphics();
//			g2D.setColor(new Color(0,0,0,0));
//			g2D.fillRect(0,0,img.getWidth(),img.getHeight());
//			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//
//			int width, height, halfWidth, halfHeight;
//			if (img.getWidth() > preferredWidthAndHeight || img.getHeight() > preferredWidthAndHeight){
//				width = preferredWidthAndHeight;
//				height = preferredWidthAndHeight;
//				halfWidth = (int)(width/2);
//				halfHeight = (int)(height/2);
//			}else{
//				width = img.getWidth();
//				height = img.getHeight();
//				halfWidth = (int)(width/2);
//				halfHeight = (int)(height/2);
//			}
//			g2D.setColor(Color.white);
//			g2D.setStroke(new BasicStroke(1));
//			int f = 0;
//			int div = 3;
//			int c = 1;
//			g2D.drawOval((int)(width/4), (int)(height/4), width-(int)(width/2)+c, height-(int)(height/2)+c);
//			g2D.drawLine(halfWidth, halfHeight, halfWidth, halfHeight);
//
//			g2D.drawLine(0, halfHeight, halfWidth - (int)(width/div), halfHeight);
//			g2D.drawLine(halfWidth + (int)(width/div), halfHeight, width-f, halfHeight);
//			g2D.drawLine(halfWidth, 0, halfWidth, halfHeight - (int)(height/div));
//			g2D.drawLine(halfWidth, halfHeight + (int)(height/div), halfWidth, height-f);
//			/*
//			g2D.setStroke(new BasicStroke(1));
//			int n = 3;
//			g2D.drawLine(0, 0, n, n);
//			g2D.drawLine(0, height, n, height-n);
//			g2D.drawLine(width, 0, width-n, n);
//			g2D.drawLine(width, height, width-n, height-n);
//			*/
//			whiteCrosshairCursor = toolkit.createCustomCursor(img, new Point(halfWidth,halfHeight), "whiteCrosshair");//(int)(img.getWidth()/2),(int)(img.getHeight()/2)), "crosshair");
//		}
//		{
//			BufferedImage img = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
//			Graphics2D g2D = (Graphics2D)img.getGraphics();
//			g2D.setColor(new Color(0,0,0,0));
//			g2D.fillRect(0,0,img.getWidth(),img.getHeight());
//			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//
//			int width, height, halfWidth, halfHeight;
//			if (img.getWidth() > preferredWidthAndHeight || img.getHeight() > preferredWidthAndHeight){
//				width = preferredWidthAndHeight;
//				height = preferredWidthAndHeight;
//				halfWidth = (int)(width/2);
//				halfHeight = (int)(height/2);
//			}else{
//				width = img.getWidth();
//				height = img.getHeight();
//				halfWidth = (int)(width/2);
//				halfHeight = (int)(height/2);
//			}
//			g2D.setColor(Color.BLACK);
//			g2D.setStroke(new BasicStroke(1));
//			int f = 0;
//			int div = 3;
//			int c = 1;
//			g2D.drawOval((int)(width/4), (int)(height/4), width-(int)(width/2)+c, height-(int)(height/2)+c);
//			g2D.drawLine(halfWidth, halfHeight, halfWidth, halfHeight);
//
//			g2D.drawLine(0, halfHeight, halfWidth - (int)(width/div), halfHeight);
//			g2D.drawLine(halfWidth + (int)(width/div), halfHeight, width-f, halfHeight);
//			g2D.drawLine(halfWidth, 0, halfWidth, halfHeight - (int)(height/div));
//			g2D.drawLine(halfWidth, halfHeight + (int)(height/div), halfWidth, height-f);
//			/*
//			g2D.setStroke(new BasicStroke(1));
//			int n = 3;
//			g2D.drawLine(0, 0, n, n);
//			g2D.drawLine(0, height, n, height-n);
//			g2D.drawLine(width, 0, width-n, n);
//			g2D.drawLine(width, height, width-n, height-n);
//			*/
//			blackCrosshairCursor = toolkit.createCustomCursor(img, new Point(halfWidth,halfHeight), "whiteCrosshair");//(int)(img.getWidth()/2),(int)(img.getHeight()/2)), "crosshair");
//		}
//	}
}
