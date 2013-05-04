import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;


public class Vertex extends PApplet implements Comparable<Vertex> {
	/**
	 * 
	 */
	private GraphWorkshop Vertex;

	String vertexLabel;

	int vertexColor;

	PVector vertexLocation;

	// Used for graph applications like DFS, BFS
	boolean startVertex;

	// Used for graph applications.
	boolean goalVertex;

	boolean visited;

	List<Vertex> adjacencyList;

	// Used for Dijkstra
	Integer minimumDistanceFromSourceVertex = Integer.MAX_VALUE;

	Vertex previousVertexInDijkstraShortestPath;

	Vertex(GraphWorkshop graphWorkshop, String vertexLabel, int vertexColor, PVector vertexLocation) {
		Vertex = graphWorkshop;
		this.vertexLabel = vertexLabel;
		this.vertexColor = vertexColor;
		this.vertexLocation = vertexLocation;
		this.startVertex = false;
		this.goalVertex = false;
		this.visited = false;
		adjacencyList = new ArrayList<Vertex>();
	}

	public void show(boolean showVertexLabel) {
		if (visited) {
			Vertex.stroke(Vertex.VISITED_VERTEX_STROKE_COLOR);
			Vertex.strokeWeight(Vertex.VISITED_VERTEX_STROKE_WEIGHT);
		} else {
			Vertex.noStroke();
		}

		Vertex.fill(vertexColor);
		Vertex.ellipse(vertexLocation.x, vertexLocation.y, Vertex.VERTEX_DIAMETER,
				Vertex.VERTEX_DIAMETER);

		if (showVertexLabel) {
			Vertex.fill(0);
			if (Vertex.toggleDijkstra) {
				if (minimumDistanceFromSourceVertex
						.equals(Integer.MAX_VALUE)) {
					Vertex.text(vertexLabel + " (Infinity)", vertexLocation.x
							+ (Vertex.VERTEX_DIAMETER / 2.5f), vertexLocation.y
							- (Vertex.VERTEX_DIAMETER / 2.5f));
				} else {
					Vertex.text(vertexLabel + " ("
							+ minimumDistanceFromSourceVertex.toString()
							+ ")", vertexLocation.x
							+ (Vertex.VERTEX_DIAMETER / 2.5f), vertexLocation.y
							- (Vertex.VERTEX_DIAMETER / 2.5f));
				}
			} else {
				Vertex.text(vertexLabel + " (" + adjacencyList.size() + ")",
						vertexLocation.x + (Vertex.VERTEX_DIAMETER / 2.5f),
						vertexLocation.y - (Vertex.VERTEX_DIAMETER / 2.5f));
			}
		}

		if (startVertex) {
			Vertex.fill(0);
			Vertex.textAlign(GraphWorkshop.CENTER);
			Vertex.text("Start", vertexLocation.x, vertexLocation.y);
			Vertex.textAlign(GraphWorkshop.LEFT);
		} else if (goalVertex) {
			Vertex.fill(0);
			Vertex.textAlign(GraphWorkshop.CENTER);
			Vertex.text("Goal", vertexLocation.x, vertexLocation.y);
			Vertex.textAlign(GraphWorkshop.LEFT);
		}
	}

	void showPrint() {
		GraphWorkshop.print("Vertex:  " + vertexLabel + "  (" + adjacencyList.size()
				+ ")");

		if (startVertex) {
			GraphWorkshop.print("  *Start");
		} else if (goalVertex) {
			GraphWorkshop.print("  *Goal");
		}

		GraphWorkshop.print("\n");
	}

	PVector getVertexLocation() {
		return vertexLocation;
	}

	String getVertexLabel() {
		return vertexLabel;
	}

	int getVertexColor() {
		return vertexColor;
	}

	boolean isStartVertex() {
		return startVertex;
	}

	void setStartVertex(boolean startVertex) {
		this.startVertex = startVertex;
	}

	boolean isGoalVertex() {
		return goalVertex;
	}

	void setGoalVertex(boolean goalVertex) {
		this.goalVertex = goalVertex;
	}

	void addAdjacent(Vertex adjacentVertex) {
		adjacencyList.add(adjacentVertex);
	}

	void removeAdjacent(Vertex adjacentVertex) {
		adjacencyList.remove(adjacentVertex);
	}

	Vertex getUnvisitedAdjacentVertex() {
		Vertex unvisitedAdjacentVertex = null;

		for (Vertex adjacentVertex : adjacencyList) {
			if (!adjacentVertex.isVisited()) {
				unvisitedAdjacentVertex = adjacentVertex;
				break;
			}
		}

		return unvisitedAdjacentVertex;
	}

	List<Vertex> getAdjacencyList() {
		return adjacencyList;
	}

	boolean isAdjacent(Vertex vertex) {
		return adjacencyList.contains(vertex);
	}

	int getDegree() {
		return adjacencyList.size();
	}

	boolean isVisited() {
		return visited;
	}

	void setVisited(boolean visited) {
		this.visited = visited;
	}

	Integer getMinimumDistanceFromSourceVertex() {
		return minimumDistanceFromSourceVertex;
	}

	void setMinimumDistanceFromSourceVertex(
			Integer minimumDistanceFromSourceVertex) {
		this.minimumDistanceFromSourceVertex = minimumDistanceFromSourceVertex;
	}

	Vertex getPreviousVertexInDijkstraShortestPath() {
		return previousVertexInDijkstraShortestPath;
	}

	void setPreviousVertexInDijkstraShortestPath(
			Vertex previousVertexInDijkstraShortestPath) {
		this.previousVertexInDijkstraShortestPath = previousVertexInDijkstraShortestPath;
	}

	public int compareTo(Vertex vertex) {
		return minimumDistanceFromSourceVertex.compareTo(vertex
				.getMinimumDistanceFromSourceVertex());
	}

	public String toString() {
		return vertexLabel;
	}

	boolean equals(Vertex vertex) {
		return vertex.getVertexLabel().equals(vertexLabel);
	}
}