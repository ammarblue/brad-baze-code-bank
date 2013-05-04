import processing.core.PApplet;
import processing.core.PVector;


public class Edge extends PApplet{
	/**
	 * 
	 */
	private GraphWorkshop Edge;

	Vertex startVertex;

	Vertex endVertex;

	boolean directional;

	int edgeWeight;

	String edgeLabel;

	boolean traversed;

	Edge(GraphWorkshop graphWorkshop, Vertex startVertex, Vertex endVertex, int edgeWeight,
			boolean directional) {
		Edge = graphWorkshop;
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.directional = directional;
		this.edgeLabel = startVertex.getVertexLabel()
				+ endVertex.getVertexLabel();
		this.edgeWeight = edgeWeight;
	}

	public void show(boolean showEdgeLabel) {
		PVector startLocation = startVertex.getVertexLocation();
		PVector endLocation = endVertex.getVertexLocation();

		Edge.stroke(Edge.EDGE_COLOR);
		Edge.strokeWeight(Edge.EDGE_STROKE_WEIGHT);

		if (traversed) {
			Edge.stroke(Edge.TRAVERSED_EDGE_COLOR);
			Edge.strokeWeight(Edge.TRAVERSED_EDGE_STROKE_WEIGHT);
		}

		Edge.line(startLocation.x, startLocation.y, endLocation.x, endLocation.y);

		if (Edge.toggleDigraph) {
			float lineAngle = GraphWorkshop.atan2(startLocation.y - endLocation.y,
					startLocation.x - endLocation.x);
			float arrowAngle = GraphWorkshop.PI / 12;
			float arrowSize = 9;
			Edge.fill(Edge.EDGE_COLOR);
			Edge.triangle(GraphWorkshop.lerp(startLocation.x, endLocation.x, 0.5f),
					GraphWorkshop.lerp(startLocation.y, endLocation.y, 0.5f),
					GraphWorkshop.lerp(startLocation.x, endLocation.x, 0.5f)
							+ (arrowSize * GraphWorkshop.cos(lineAngle + arrowAngle)),
					GraphWorkshop.lerp(startLocation.y, endLocation.y, 0.5f)
							+ (arrowSize * GraphWorkshop.sin(lineAngle + arrowAngle)),
					GraphWorkshop.lerp(startLocation.x, endLocation.x, 0.5f)
							+ (arrowSize * GraphWorkshop.cos(lineAngle - arrowAngle)),
					GraphWorkshop.lerp(startLocation.y, endLocation.y, 0.5f)
							+ (arrowSize * GraphWorkshop.sin(lineAngle - arrowAngle)));
		}

		if (showEdgeLabel) {
			Edge.fill(Edge.EDGE_COLOR);
			Edge.text(edgeWeight, GraphWorkshop.lerp(startLocation.x, endLocation.x, 0.5f) + 5,
					GraphWorkshop.lerp(startLocation.y, endLocation.y, 0.5f) - 5);
		}
	}

	void showPrint() {
		GraphWorkshop.print("Edge:  " + edgeLabel + "  (" + edgeWeight + ")");

		if (directional) {
			GraphWorkshop.print("  *directional");
		}

		GraphWorkshop.println();
	}

	boolean isDirectional() {
		return directional;
	}

	Vertex getStartVertex() {
		return startVertex;
	}

	Vertex getEndVertex() {
		return endVertex;
	}

	int getEdgeWeight() {
		return edgeWeight;
	}

	String getEdgeLabel() {
		return edgeLabel;
	}

	boolean isTraversed() {
		return traversed;
	}

	void setTraversed(boolean traversed) {
		this.traversed = traversed;
	}
}