
import processing.core.PApplet;
import processing.core.PVector;

/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/65275*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */
// Graph Workshop
// David Balash 2012  

// Press "v" to toggle the addition of a vertex, use the mouse to add a new vertex.
// Press "e" to toggle the addition of an edge, use the mouse to add a new edge.
// Press "r" to toggle the removal of a vertex, use the mouse to select a vertex to remove.
// Press "s" to toggle the selection of a start vertex, use the mouse to select a vertex. 
// Press "g" to toggle the selection of a goal or end vertex, use the mouse to select a vertex.
// Press "i" to toggle directional graph (Digraph).
// Press "j" to run Dijkstra's Algorithm (Single Source Shortest Path). 
// Press "b" to run Breadth First Search (BFS). 
// Press "d" to run Depth First Search (DFS). 
// Press "f" to run Find Eulerian Path.
// Press "n" to go to the next step of a search algorithm. 
// Press "c" to clear visited vertices and traversed edges. 
// Press "m" to print adjacency matrix and weighted adjacency matrix.
// Press "p" to print graph information.
// Press "l" to toggle vertex labels.  
// Press "k" to toggle edge labels.  
public class GraphWorkshop extends PApplet {
	final int STANDARD_WIDTH = 720;
	final int STANDARD_HEIGHT = 720;
	final int BACKGROUND_COLOR = 0xFFFFF9F9;
	final int EDGE_COLOR = 0xFF000000;
	final float EDGE_STROKE_WEIGHT = 1.5f;
	final int TRAVERSED_EDGE_COLOR = 0xFFCFB53B;
	final float TRAVERSED_EDGE_STROKE_WEIGHT = 3.5f;
	final int VISITED_VERTEX_STROKE_COLOR = 0xFFCFB53B;
	final int VISITED_VERTEX_STROKE_WEIGHT = 8;
	final int VERTEX_DIAMETER = 50;

	Graph graph = new Graph(this);
	Vertex selectedVertex;
	boolean vertexSelected = false;
	boolean toggleAddVertex = true;
	boolean toggleAddEdge = false;
	boolean toggleRemoveVertex = false;
	boolean toggleSelectStartVertex = false;
	boolean toggleSelectGoalVertex = false;
	boolean toggleDigraph = true;
	boolean toggleShowVertexLabel = true;
	boolean toggleShowEdgeLabel = true;
	boolean toggleDFS = false;
	boolean toggleBFS = false;
	boolean toggleDijkstra = false;

	public void setup() {
		size(STANDARD_WIDTH, STANDARD_HEIGHT);
		background(BACKGROUND_COLOR);
		smooth();
	}

	public void draw() {
		background(BACKGROUND_COLOR);

		graph.show(toggleShowVertexLabel, toggleShowEdgeLabel);

		if (vertexSelected) {
			PVector vertexLocation = selectedVertex.getVertexLocation();
			stroke(0);
			line(mouseX, mouseY, vertexLocation.x, vertexLocation.y);
		}
	}

	public void keyPressed() {
		if (key == 'm') {
			graph.printAdjacencyMatrix();
			graph.printWeightedAdjacencyMatrix();
		}
		if (key == 'p') {
			graph.printGraphInformation();
		}
		if (key == 'c') {
			graph.clearVisitedVertices();
			graph.clearTraversedEdges();
		} else if (key == 'b') {
			toggleBFS = true;
			toggleDFS = false;
			toggleDijkstra = false;
			graph.startNextStepBreadthFirstSearch();
		} else if (key == 'd') {
			toggleDFS = true;
			toggleBFS = false;
			toggleDijkstra = false;
			graph.startNextStepDepthFirstSearch();
		} else if (key == 'j') {
			toggleDijkstra = true;
			toggleBFS = false;
			toggleDFS = false;
			graph.startDijkstra();
		} else if (key == 'n') {
			if (toggleDFS) {
				graph.nextStepDepthFirstSearch();
			}

			if (toggleBFS) {
				graph.nextStepBreadthFirstSearch();
			}

			if (toggleDijkstra) {
				graph.nextStepDijkstra();
			}
		} else if (key == 'f') {
			graph.findEulerianPath();
		} else if (key == 'l') {
			if (toggleShowVertexLabel) {
				toggleShowVertexLabel = false;
			} else {
				toggleShowVertexLabel = true;
			}
		} else if (key == 'k') {
			if (toggleShowEdgeLabel) {
				toggleShowEdgeLabel = false;
			} else {
				toggleShowEdgeLabel = true;
			}
		} else if (key == 'i') {
			if (toggleDigraph) {
				toggleDigraph = false;
			} else {
				toggleDigraph = true;
			}
		} else if (key == 'r') {
			if (toggleRemoveVertex) {
				toggleRemoveVertex = false;
			} else {
				toggleRemoveVertex = true;
				toggleAddEdge = false;
				toggleAddVertex = false;
				toggleSelectStartVertex = false;
				toggleSelectGoalVertex = false;
				vertexSelected = false;
			}
		} else if (key == 's') {
			if (toggleSelectStartVertex) {
				toggleSelectStartVertex = false;
			} else {
				toggleSelectStartVertex = true;
				toggleAddEdge = false;
				toggleAddVertex = false;
				toggleSelectGoalVertex = false;
				toggleRemoveVertex = false;
				vertexSelected = false;
			}
		} else if (key == 'g') {
			if (toggleSelectGoalVertex) {
				toggleSelectGoalVertex = false;
			} else {
				toggleSelectGoalVertex = true;
				toggleAddEdge = false;
				toggleAddVertex = false;
				toggleSelectStartVertex = false;
				toggleRemoveVertex = false;
				vertexSelected = false;
			}
		} else if (key == 'v') {
			if (toggleAddVertex) {
				toggleAddVertex = false;
			} else {
				toggleAddVertex = true;
				toggleAddEdge = false;
				toggleSelectStartVertex = false;
				toggleSelectGoalVertex = false;
				toggleRemoveVertex = false;
				vertexSelected = false;
			}
		} else if (key == 'e') {
			if (toggleAddEdge) {
				toggleAddEdge = false;
			} else {
				toggleAddEdge = true;
				toggleAddVertex = false;
				toggleSelectStartVertex = false;
				toggleSelectGoalVertex = false;
				toggleRemoveVertex = false;
			}
		}
	}

	public void mousePressed() {
		if (toggleAddVertex) {
			graph.addVertex(new PVector(mouseX, mouseY));
		} else if (toggleAddEdge) {
			if (vertexSelected) {
				Vertex connectedVertex = graph.getNearestVertex(mouseX, mouseY);
				if (connectedVertex != selectedVertex) {
					graph.addEdge(selectedVertex, connectedVertex,
							toggleDigraph);
				}
				vertexSelected = false;
			} else {
				selectedVertex = graph.getNearestVertex(mouseX, mouseY);
				if (selectedVertex != null) {
					vertexSelected = true;
				}
			}
		} else if (toggleRemoveVertex) {
			Vertex selectedForRemoveVertex = graph.getNearestVertex(mouseX,
					mouseY);
			if (selectedForRemoveVertex != null) {
				graph.removeVertex(selectedForRemoveVertex);
			}
		} else if (toggleSelectStartVertex) {
			Vertex startVertex = graph.getNearestVertex(mouseX, mouseY);
			graph.setStartVertex(startVertex);
		} else if (toggleSelectGoalVertex) {
			Vertex goalVertex = graph.getNearestVertex(mouseX, mouseY);
			graph.setGoalVertex(goalVertex);
		}
	}
}