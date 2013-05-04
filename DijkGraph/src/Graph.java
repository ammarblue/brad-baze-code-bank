import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import processing.core.PVector;


class Graph {
	/**
	 * 
	 */
	private GraphWorkshop Graph;

	final int MAX_VERTICES = 100;

	Set<Vertex> vertexSet = new HashSet<Vertex>();

	Set<Edge> edgeSet = new HashSet<Edge>();

	boolean[][] adjacencyMatrix;

	int[][] weightedAdjacencyMatrix;

	Deque<String> vertexLabelDeque = new ArrayDeque<String>();

	Map<String, Integer> vertexColorMap = new HashMap<String, Integer>();

	Deque<Vertex> dfsStack = new ArrayDeque<Vertex>();

	Deque<Vertex> bfsQueue = new ArrayDeque<Vertex>();

	PriorityQueue<Vertex> dijkstraMinPriorityQueue = new PriorityQueue<Vertex>();

	List<Vertex> dijkstraSettledVertices = new ArrayList<Vertex>();

	List<Vertex> printableListOfVertices = new ArrayList<Vertex>();

	List<String> rowText = new ArrayList<String>();

	Graph(GraphWorkshop graphWorkshop) {
		Graph = graphWorkshop;
		int numberOfLettersInLabel = 1;
		int labelStringIndex = 0;
		String vertexLabelString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		for (int i = 0; i < MAX_VERTICES; i++) {
			if (labelStringIndex > 25) {
				labelStringIndex = 0;
				numberOfLettersInLabel++;
			}

			StringBuilder labelStringBuilder = new StringBuilder();
			String labelString = ""
					+ vertexLabelString.charAt(labelStringIndex);

			for (int j = 0; j < numberOfLettersInLabel; j++) {
				labelStringBuilder.append(labelString);
			}

			vertexLabelDeque.add(labelStringBuilder.toString());
			vertexColorMap.put(labelStringBuilder.toString(), new Integer(i
					- ((labelStringBuilder.length() - 1) * 26)));

			labelStringIndex++;
		}
	}

	/**
	 * Dijkstra's Algorithm
	 * 
	 * Dijkstra's algorithm is a graph search algorithm that solves the
	 * single-source shortest path problem for a graph with nonnegative edge
	 * path costs. For a given source vertex in the graph, the algorithm
	 * find the path with the lowest cost between that vertex and every
	 * other vertex.
	 * 
	 * Implemented with a minimum priority queue the worst case time
	 * complexity is O(|E| + |V| log |V|).
	 * 
	 * This is asymptotically the fastest known single-source shortest path
	 * algorithm for arbitrary directed graphs with unbounded nonnegative
	 * weights.
	 * 
	 */
	void dijkstra() {
		Vertex sourceVertex = getStartVertex();

		if (sourceVertex == null) {
			GraphWorkshop.println("Choose a source vertex");
		} else {
			sourceVertex.setMinimumDistanceFromSourceVertex(0);
			PriorityQueue<Vertex> minPriorityQueue = new PriorityQueue<Vertex>();
			minPriorityQueue.add(sourceVertex);

			while (minPriorityQueue.size() > 0) {
				Vertex u = minPriorityQueue.poll();

				// Visit each adjacent vertex
				for (Vertex v : u.getAdjacencyList()) {
					int edgeWeight = distance(u.getVertexLocation().x,
							u.getVertexLocation().y,
							v.getVertexLocation().x,
							v.getVertexLocation().y);

					int distanceThroughVertexU = u
							.getMinimumDistanceFromSourceVertex()
							+ edgeWeight;

					if (distanceThroughVertexU < v
							.getMinimumDistanceFromSourceVertex()) {
						minPriorityQueue.remove(v);
						v.setMinimumDistanceFromSourceVertex(distanceThroughVertexU);
						v.setPreviousVertexInDijkstraShortestPath(u);
						minPriorityQueue.add(v);
					}
				}
			}
		}
	}

	void startDijkstra() {
		Vertex sourceVertex = getStartVertex();

		if (sourceVertex == null) {
			GraphWorkshop.println("Choose a source vertex");
		} else {
			sourceVertex.setMinimumDistanceFromSourceVertex(0);
			dijkstraMinPriorityQueue = new PriorityQueue<Vertex>();
			dijkstraMinPriorityQueue.add(sourceVertex);

			// Print vertices
			StringBuffer row = new StringBuffer();
			for (Vertex vertex : printableListOfVertices) {
				GraphWorkshop.print(" " + vertex.toString() + "    ");
				row.append(" " + vertex.toString() + "    ");
			}
			GraphWorkshop.println();
			rowText.add(row.toString());

			printDijkstraDistanceFromSource();
		}

	}

	void nextStepDijkstra() {
		if (dijkstraMinPriorityQueue.size() > 0) {
			Vertex u = dijkstraMinPriorityQueue.poll();

			dijkstraSettledVertices.add(u);

			// Visit each adjacent vertex
			for (Vertex v : u.getAdjacencyList()) {
				int edgeWeight = distance(u.getVertexLocation().x,
						u.getVertexLocation().y, v.getVertexLocation().x,
						v.getVertexLocation().y);

				int distanceThroughVertexU = u
						.getMinimumDistanceFromSourceVertex() + edgeWeight;

				if (distanceThroughVertexU < v
						.getMinimumDistanceFromSourceVertex()) {
					dijkstraMinPriorityQueue.remove(v);
					v.setMinimumDistanceFromSourceVertex(distanceThroughVertexU);
					v.setPreviousVertexInDijkstraShortestPath(u);
					dijkstraMinPriorityQueue.add(v);
				}
			}

			Graph.graph.printDijkstraDistanceFromSource();
		} else {
			GraphWorkshop.println(getShortestPathToGoal());
		}
	}

	void printDijkstraDistanceFromSource() {
		StringBuffer row = new StringBuffer();
		for (Vertex vertex : printableListOfVertices) {
			if (dijkstraSettledVertices.contains(vertex)) {
				GraphWorkshop.print(" /    ");
				row.append(" /    ");
			} else {

				if (vertex.getMinimumDistanceFromSourceVertex().equals(
						Integer.MAX_VALUE)) {
					GraphWorkshop.print("Inf   ");
					row.append("Inf   ");
				} else {
					GraphWorkshop.print(String.format("%2d   ",
							vertex.getMinimumDistanceFromSourceVertex()));
					row.append(String.format("%2d   ",
							vertex.getMinimumDistanceFromSourceVertex()));
				}
			}
		}
		GraphWorkshop.println();
		rowText.add(row.toString());
	}

	List<Vertex> getShortestPathToGoal() {
		Vertex goalVertex = getGoalVertex();

		if (goalVertex == null) {
			GraphWorkshop.println("Choose a goal vertex");
		}

		List<Vertex> shortestPath = new ArrayList<Vertex>();

		for (Vertex vertex = goalVertex; vertex != null; vertex = vertex
				.getPreviousVertexInDijkstraShortestPath()) {
			shortestPath.add(vertex);
		}

		Collections.reverse(shortestPath);

		Vertex previousVertex = null;
		for (Vertex vertex : shortestPath) {
			if (previousVertex == null) {
				vertex.setVisited(true);
			} else {
				visit(previousVertex, vertex);
			}
			previousVertex = vertex;
		}

		return shortestPath;
	}

	void breadthFirstSearch() {
		Vertex startVertex = getStartVertex();

		if (startVertex == null) {
			GraphWorkshop.println("Choose a start vertex");
		} else {
			Deque<Vertex> queue = new ArrayDeque<Vertex>();
			queue.addLast(startVertex);
			GraphWorkshop.println(startVertex.getVertexLabel());
			startVertex.setVisited(true);

			while (queue.size() > 0) {
				Vertex vertex = queue.removeFirst();
				List<Vertex> adjacencyList = vertex.getAdjacencyList();
				for (Vertex adjacentVertex : adjacencyList) {
					if (!adjacentVertex.isVisited()) {
						GraphWorkshop.println(adjacentVertex.getVertexLabel());
						visit(vertex, adjacentVertex);
						queue.addLast(adjacentVertex);
					}
				}
			}
		}
	}

	void startNextStepBreadthFirstSearch() {
		Vertex startVertex = getStartVertex();

		if (startVertex == null) {
			GraphWorkshop.println("Choose a start vertex");
		} else {
			bfsQueue.addLast(startVertex);
			GraphWorkshop.println(startVertex.getVertexLabel());
			startVertex.setVisited(true);
		}
	}

	void nextStepBreadthFirstSearch() {
		if (bfsQueue.size() > 0) {
			Vertex vertex = bfsQueue.removeFirst();
			List<Vertex> adjacencyList = vertex.getAdjacencyList();
			for (Vertex adjacentVertex : adjacencyList) {
				if (!adjacentVertex.isVisited()) {
					GraphWorkshop.println(adjacentVertex.getVertexLabel());
					visit(vertex, adjacentVertex);
					bfsQueue.addLast(adjacentVertex);
				}
			}
		}
	}

	void depthFirstSearch() {
		Vertex startVertex = getStartVertex();

		if (startVertex == null) {
			GraphWorkshop.println("Choose a start vertex");
		} else {
			Deque<Vertex> stack = new ArrayDeque<Vertex>();
			stack.push(startVertex);
			GraphWorkshop.println(startVertex.getVertexLabel());
			startVertex.setVisited(true);

			while (stack.size() > 0) {
				Vertex vertex = stack.peek();
				Vertex unvisitedAdjacentVertex = vertex
						.getUnvisitedAdjacentVertex();

				if (unvisitedAdjacentVertex != null) {
					GraphWorkshop.println(unvisitedAdjacentVertex.getVertexLabel());
					visit(vertex, unvisitedAdjacentVertex);
					stack.push(unvisitedAdjacentVertex);
				} else {
					stack.pop();
				}
			}
		}
	}

	void startNextStepDepthFirstSearch() {
		Vertex startVertex = getStartVertex();

		if (startVertex == null) {
			GraphWorkshop.println("Choose a start vertex");
		} else {
			dfsStack.push(startVertex);
			GraphWorkshop.println(startVertex.getVertexLabel());
			startVertex.setVisited(true);
		}
	}

	void nextStepDepthFirstSearch() {
		if (dfsStack.size() > 0) {
			Vertex vertex = dfsStack.peek();
			Vertex unvisitedAdjacentVertex = vertex
					.getUnvisitedAdjacentVertex();

			if (unvisitedAdjacentVertex != null) {
				GraphWorkshop.println(unvisitedAdjacentVertex.getVertexLabel());
				visit(vertex, unvisitedAdjacentVertex);
				dfsStack.push(unvisitedAdjacentVertex);
			} else {
				dfsStack.pop();
			}
		}
	}

	void visit(Vertex vertex, Vertex unvisitedAdjacentVertex) {
		unvisitedAdjacentVertex.setVisited(true);
		for (Edge edge : edgeSet) {
			if (!Graph.toggleDigraph) {
				if (edge.getStartVertex().equals(vertex)
						&& edge.getEndVertex().equals(
								unvisitedAdjacentVertex)
						|| edge.getEndVertex().equals(vertex)
						&& edge.getStartVertex().equals(
								unvisitedAdjacentVertex)) {
					edge.setTraversed(true);
				}
			} else {
				if (edge.getStartVertex().equals(vertex)
						&& edge.getEndVertex().equals(
								unvisitedAdjacentVertex)) {
					edge.setTraversed(true);
				}
			}

		}
	}

	Vertex getStartVertex() {
		Vertex startVertex = null;
		for (Vertex vertex : vertexSet) {
			if (vertex.isStartVertex()) {
				startVertex = vertex;
			}
		}

		return startVertex;
	}

	Vertex getGoalVertex() {
		Vertex goalVertex = null;
		for (Vertex vertex : vertexSet) {
			if (vertex.isGoalVertex()) {
				goalVertex = vertex;
			}
		}

		return goalVertex;
	}

	void clearVisitedVertices() {
		for (Vertex vertex : vertexSet) {
			vertex.setVisited(false);
			vertex.setMinimumDistanceFromSourceVertex(Integer.MAX_VALUE);
			vertex.setPreviousVertexInDijkstraShortestPath(null);
		}
		dijkstraMinPriorityQueue = new PriorityQueue<Vertex>();
		dijkstraSettledVertices = new ArrayList<Vertex>();
		rowText = new ArrayList<String>();
	}

	void clearTraversedEdges() {
		for (Edge edge : edgeSet) {
			edge.setTraversed(false);
		}
	}

	Vertex addVertex(PVector pVector) {
		Vertex vertex = null;

		if (vertexSet.size() < MAX_VERTICES) {
			String vertexLabel = vertexLabelDeque.removeFirst();
			Graph.colorMode(GraphWorkshop.HSB, 25, 100, 100);
			vertex = new Vertex(Graph, vertexLabel, Graph.color(
					vertexColorMap.get(vertexLabel), 100, 100), pVector);
			vertexSet.add(vertex);
			printableListOfVertices.add(vertex);
		}

		updateGraph();

		return vertex;
	}

	void removeVertex(Vertex selectedForRemoveVertex) {
		String selectedForRemoveVertexLabel = selectedForRemoveVertex
				.getVertexLabel();

		Set<Edge> edgeToRemoveSet = new HashSet<Edge>();

		for (Edge edge : edgeSet) {
			if (selectedForRemoveVertex == edge.getStartVertex()) {
				edge.getEndVertex().removeAdjacent(selectedForRemoveVertex);
				edgeToRemoveSet.add(edge);
			} else if (selectedForRemoveVertex == edge.getEndVertex()) {
				edge.getStartVertex().removeAdjacent(
						selectedForRemoveVertex);
				edgeToRemoveSet.add(edge);
			}
		}

		for (Edge edge : edgeToRemoveSet) {
			edgeSet.remove(edge);
		}

		vertexSet.remove(selectedForRemoveVertex);
		printableListOfVertices.remove(selectedForRemoveVertex);
		vertexLabelDeque.push(selectedForRemoveVertexLabel);
		updateGraph();
	}

	void addEdge(Vertex vertex1, Vertex vertex2, boolean directional) {
		int edgeWeight = distance(vertex1.getVertexLocation().x,
				vertex1.getVertexLocation().y,
				vertex2.getVertexLocation().x,
				vertex2.getVertexLocation().y);
		Edge edge = new Edge(Graph, vertex1, vertex2, edgeWeight, directional);
		edgeSet.add(edge);

		vertex1.addAdjacent(vertex2);

		if (!directional) {
			vertex2.addAdjacent(vertex1);
		}

		updateGraph();
	}

	int distance(float x1, float y1, float x2, float y2) {
		return GraphWorkshop.round(GraphWorkshop.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) / 10);
	}

	void updateAdjacencyMatrix() {
		adjacencyMatrix = new boolean[vertexSet.size()][vertexSet.size()];

		int i = 0;
		for (Vertex vertexI : vertexSet) {
			int j = 0;
			for (Vertex vertexJ : vertexSet) {
				adjacencyMatrix[i][j] = vertexI.isAdjacent(vertexJ);
				adjacencyMatrix[j][i] = vertexJ.isAdjacent(vertexI);
				j++;
			}
			i++;
		}
	}

	void updateWeightedAdjacencyMatrix() {
		weightedAdjacencyMatrix = new int[vertexSet.size()][vertexSet
				.size()];

		int i = 0;
		for (Vertex vertexI : vertexSet) {
			int j = 0;
			for (Vertex vertexJ : vertexSet) {
				if (vertexI.isAdjacent(vertexJ)) {
					int edgeWeight = distance(
							vertexI.getVertexLocation().x,
							vertexI.getVertexLocation().y,
							vertexJ.getVertexLocation().x,
							vertexJ.getVertexLocation().y);

					weightedAdjacencyMatrix[i][j] = edgeWeight;
				} else {
					weightedAdjacencyMatrix[i][j] = Integer.MAX_VALUE;
				}

				if (vertexJ.isAdjacent(vertexI)) {
					int edgeWeight = distance(
							vertexJ.getVertexLocation().x,
							vertexJ.getVertexLocation().y,
							vertexI.getVertexLocation().x,
							vertexI.getVertexLocation().y);

					weightedAdjacencyMatrix[j][i] = edgeWeight;
				} else {
					weightedAdjacencyMatrix[j][i] = Integer.MAX_VALUE;
				}

				j++;
			}
			i++;
		}
	}

	void updateGraph() {
		updateAdjacencyMatrix();
		updateWeightedAdjacencyMatrix();
	}

	void show(boolean showVertexLabel, boolean showEdgeLabel) {
		Graph.textSize(18);

		for (Edge edge : edgeSet) {
			edge.show(showEdgeLabel);
		}

		for (Vertex vertex : vertexSet) {
			vertex.show(showVertexLabel);
		}

		boolean firstRow = true;
		int textHeight = 55;
		for (String row : rowText) {
			if (firstRow) {
				Graph.textSize(22);
				firstRow = false;
			} else {
				Graph.fill(0, 102, 153);
			}
			Graph.text(row, 20, textHeight);
			Graph.fill(0);
			textHeight += 20;
		}

		if (Graph.toggleDijkstra) {
			Graph.stroke(Graph.EDGE_COLOR);
			Graph.strokeWeight(Graph.EDGE_STROKE_WEIGHT);
			Graph.textSize(22);
			Graph.text("Distance from source vertex:", 20, 30);
			Graph.line(15, 35, 300, 35);
			Graph.text("Settled vertices:", 20, 400);
			Graph.textSize(18);
			Graph.line(15, 405, 180, 405);
			textHeight = 425;
			for (Vertex vertex : dijkstraSettledVertices) {
				Graph.fill(0, 102, 153);
				Graph.text(vertex.toString() + "("
						+ vertex.getMinimumDistanceFromSourceVertex() + ")",
						20, textHeight);
				textHeight += 20;
			}
			Graph.fill(0);
		}
	}

	Vertex getNearestVertex(float x, float y) {
		Vertex nearestVertex = null;
		float nearestDistance = 0;
		boolean firstVertexChecked = true;

		for (Vertex vertex : vertexSet) {
			PVector location = vertex.getVertexLocation();
			float distance = GraphWorkshop.dist(x, y, location.x, location.y);

			if (firstVertexChecked) {
				nearestDistance = GraphWorkshop.dist(x, y, location.x, location.y);
				nearestVertex = vertex;
				firstVertexChecked = false;
			} else if (distance < nearestDistance) {
				nearestDistance = distance;
				nearestVertex = vertex;
			}
		}

		return nearestVertex;
	}

	void setStartVertex(Vertex startVertex) {
		if (!startVertex.isStartVertex()) {
			for (Vertex vertex : vertexSet) {
				if (vertex.isStartVertex()) {
					vertex.setStartVertex(false);
				}
			}
			startVertex.setStartVertex(true);
			startVertex.setGoalVertex(false);
		} else {
			startVertex.setStartVertex(false);
		}
	}

	void setGoalVertex(Vertex goalVertex) {
		if (!goalVertex.isGoalVertex()) {
			for (Vertex vertex : vertexSet) {
				if (vertex.isGoalVertex()) {
					vertex.setGoalVertex(false);
				}
			}
			goalVertex.setGoalVertex(true);
			goalVertex.setStartVertex(false);
		} else {
			goalVertex.setGoalVertex(false);
		}
	}

	void findEulerianPath() {
		Vertex startVertex = null;

		for (Vertex vertex : vertexSet) {
			if (vertex.isStartVertex()) {
				startVertex = vertex;
			}
		}

		if (startVertex != null) {
			List<Vertex> previousStartVertexList = new ArrayList<Vertex>();
			int previousStartVertexListIndex = 0;

			while (true) {
				Edge edgeToRemove = null;

				for (Edge edge : edgeSet) {
					if (startVertex == edge.getStartVertex()) {
						edgeToRemove = edge;
						previousStartVertexList.add(startVertex);
						startVertex = edge.getEndVertex();
						break;
					} else if (startVertex == edge.getEndVertex()) {
						edgeToRemove = edge;
						previousStartVertexList.add(startVertex);
						startVertex = edge.getStartVertex();
						break;
					}
				}

				if (edgeToRemove != null) {
					GraphWorkshop.println("Remove: " + edgeToRemove.getEdgeLabel());
					edgeSet.remove(edgeToRemove);
				} else if (edgeSet.isEmpty()) {
					GraphWorkshop.println("EMPTY Edge Set");
					break;
				} else {
					startVertex = previousStartVertexList
							.get(previousStartVertexListIndex);
					previousStartVertexListIndex++;
				}
			}
		}
	}

	boolean isEdge(Vertex startVertex, Vertex endVertex) {
		boolean isEdge = false;

		for (Edge edge : edgeSet) {
			if (edge.getStartVertex().equals(startVertex)
					&& edge.getEndVertex().equals(endVertex)) {
				isEdge = true;
				break;
			}
		}

		return isEdge;
	}

	void printGraphInformation() {
		GraphWorkshop.println("Number of Edges = " + edgeSet.size());
		GraphWorkshop.println("Total number of Degrees = " + (edgeSet.size() * 2));
		GraphWorkshop.println("Number of Vertices = " + vertexSet.size());

		for (Edge edge : edgeSet) {
			edge.showPrint();
		}

		for (Vertex vertex : vertexSet) {
			vertex.showPrint();
		}

		GraphWorkshop.println();
		GraphWorkshop.print("Edges:  (");
		for (Edge edge : edgeSet) {
			GraphWorkshop.print(" " + edge.getEdgeLabel());
		}
		GraphWorkshop.print(" )\n");

		GraphWorkshop.print("Vertices:  (");
		for (Vertex vertex : vertexSet) {
			GraphWorkshop.print(" " + vertex.getVertexLabel());
		}
		GraphWorkshop.print(" )\n");
	}

	void printAdjacencyMatrix() {
		GraphWorkshop.println("Adjacency Matrix:");
		for (int index = 0; index < vertexSet.size(); index++) {
			GraphWorkshop.println(Arrays.toString(adjacencyMatrix[index]));
		}
		GraphWorkshop.println();
	}

	void printWeightedAdjacencyMatrix() {
		GraphWorkshop.println("Weighted Adjacency Matrix:");
		for (int index = 0; index < vertexSet.size(); index++) {
			GraphWorkshop.println(Arrays.toString(weightedAdjacencyMatrix[index]));
		}
		GraphWorkshop.println();
	}
}