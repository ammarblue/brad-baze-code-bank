package com.software.reuze;
import java.util.Iterator;

import com.software.reuze.dag_GraphFloatingEdges;



public class vgu_GraphDraw {
	public static void drawArrow(dag_GraphNode fromNode, dag_GraphNode toNode, float nodeRadius, float arrowSize) {
		float fx, fy, tx, ty;
		float ax, ay, sx, sy, ex, ey;
		float awidthx, awidthy;

		fx = fromNode.xf();
		fy = fromNode.yf();

		tx = toNode.xf();
		ty = toNode.yf();

		float deltaX = tx - fx;
		float deltaY = (ty - fy);
		float d = (float)Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		sx = fx + (nodeRadius * deltaX / d);
		sy = fy + (nodeRadius * deltaY / d);

		ex = tx - (nodeRadius * deltaX / d);
		ey = ty - (nodeRadius * deltaY / d);

		ax = tx - (nodeRadius + arrowSize) * deltaX / d;
		ay = ty - (nodeRadius + arrowSize) * deltaY / d;

		awidthx = - (ey - ay);
		awidthy = ex - ax;
		final z_Processing a=z_Processing.app;
		a.noFill();
		a.strokeWeight(4.0f);
		a.stroke(160f, 128f);
		a.line(sx, sy, ax, ay);

		a.noStroke();
		a.fill(48, 128);
		a.beginShape(a.TRIANGLES);
		a.vertex(ex, ey);
		a.vertex(ax - awidthx, ay - awidthy);
		a.vertex(ax + awidthx, ay + awidthy);
		a.endShape();
	}

	public static void drawNodes(dag_GraphNode[] r, float nodeSize, int lineColor, float sWeight) {
		if (r.length >= 2) {
			final z_Processing a=z_Processing.app;
			a.pushStyle();
			a.stroke(lineColor);
			a.strokeWeight(sWeight);
			a.noFill();
			for (int i = 1; i < r.length; i++)
				a.line(r[i-1].xf(), r[i-1].yf(), r[i].xf(), r[i].yf());
			// Route start node
			a.strokeWeight(2.0f);
			a.stroke(0, 0, 160);
			a.fill(0, 255, 0);
			a.ellipseMode(a.CENTER);
			a.ellipse(r[0].xf(), r[0].yf(), nodeSize, nodeSize);
			// Route end node
			a.stroke(160, 0, 0);
			a.fill(255, 0, 0);
			a.ellipse(r[r.length-1].xf(), r[r.length-1].yf(), nodeSize, nodeSize); 
			a.popStyle();
		}
	}

	public static void drawEdges(dag_GraphEdge[] edges, float nodeSize, int lineColor, float sWeight, boolean arrow) {
		if (edges != null) {
			final z_Processing a=z_Processing.app;
			a.pushStyle();
			a.noFill();
			a.stroke(lineColor);
			a.strokeWeight(sWeight);
			for (dag_GraphEdge ge : edges) {
				if (arrow)
					vgu_GraphDraw.drawArrow(ge.from(), ge.to(), nodeSize / 2.0f, 6);
				else {
					a.line(ge.from().xf(), ge.from().yf(), ge.to().xf(), ge.to().yf());
				}
			}
			a.popStyle();
		}
	}

	public static void drawEdges(dag_GraphFloatingEdges g, float nodeSize, int lineColor, float sWeight, boolean arrow) {
		final z_Processing a=z_Processing.app;
		a.pushStyle();
		a.noFill();
		a.stroke(lineColor);
		a.strokeWeight(sWeight);
		Iterator<dag_GraphEdge> it=g.iteratorEdges();
		while (it.hasNext()) {
			dag_GraphEdge ge=it.next();
			if (arrow)
				vgu_GraphDraw.drawArrow(ge.from(), ge.to(), nodeSize / 2.0f, 6);
			else {
				a.line(ge.from().xf(), ge.from().yf(), ge.to().xf(), ge.to().yf());
			}
		}
		a.popStyle();
	}
	
	public static void drawNodes(dag_GraphFloatingEdges g, float nodeSize, int c) {
		final z_Processing a=z_Processing.app;
		a.pushStyle();
		a.noStroke();
		a.fill(c);
		a.ellipseMode(a.CENTER);
		for (dag_GraphNode node : g)
			a.ellipse(node.xf(), node.yf(), nodeSize, nodeSize);
		a.popStyle();
	}
}
