package com.software.reuze;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Breadth First Search
 * @author Peter
 *
 */
public class dag_GraphSearch_BFS  implements dag_i_GraphSearch {

	protected dag_GraphFloatingEdges graph;
	
	/**
	 * Used to remember which nodes had been visited
	 */
	protected HashSet<Integer> visited;

	/**
	 * Remember the route found
	 * 				 to       from
	 */ 
	protected HashMap<Integer, Integer> settledNodes;

	/**
	 * Queue of graph edges to consider
	 */
	LinkedList<dag_GraphEdge> queue;

	/**
	 * Used to remember examined edges
	 */
	protected HashSet<dag_GraphEdge> examinedEdges;

	/**
	 * List for routes nodes in order of travel
	 */
	protected LinkedList<dag_GraphNode> route;

	/**
	 * Prevent creating an object without a graph
	 */
	protected dag_GraphSearch_BFS(){ }

	/**
	 * Create a search object that uses breadth first search algorithm
	 * for the given graph.
	 * @param graph the graph to use
	 */
	public dag_GraphSearch_BFS(dag_GraphFloatingEdges graph) {
		super();
		this.graph = graph;
		int nbrNodes = graph.getNbrNodes();
		visited = new HashSet<Integer>(nbrNodes);
		settledNodes = new HashMap<Integer, Integer>(nbrNodes);
		queue = new LinkedList<dag_GraphEdge>();
		examinedEdges = new HashSet<dag_GraphEdge>();
		route = new LinkedList<dag_GraphNode>();
	}

	/**
	 * Clears all data related to a search so this object can be
	 * reused for another search
	 */
	public void clear(){
		queue.clear();
		settledNodes.clear();
		visited.clear();
		examinedEdges.clear();
		route.clear();
	}

	/**
	 * Search for a route from node startID and ends at targetID. <br>
	 * This will return a linkedlist of the nodes that make up the route
	 * from start to end order. <br>
	 * If either the start or target node does not exist or if a route 
	 * can't be found the returned list is empty.
	 * 
	 * @param startID id of the start node
	 * @param targetID id of the target node
	 * @return the route as a list of nodes
	 */
	public LinkedList<dag_GraphNode> search(int startID, int targetID){
		return search(startID, targetID, false);
	}

	/**
	 * Search for a route from node startID and ends at targetID. <br>
	 * This will return a linkedlist of the nodes that make up the route
	 * from start to end order. <br>
	 * If either the start or target node does not exist or if a route 
	 * can't be found the returned list is empty.
	 * 
	 * @param startID id of the start node
	 * @param targetID id of the target node
	 * @param remember whether to remember the examined edges.
	 * @return the route as a list of nodes
	 */
	public LinkedList<dag_GraphNode> search(int startID, int targetID, boolean remember){
		clear();
		LinkedList<dag_GraphEdge> nextEdges;

		dag_GraphEdge next;
		dag_GraphNode start = graph.getNode(startID);
		dag_GraphNode target = graph.getNode(targetID);
		if(start == null || target == null)
			return null;

		dag_GraphEdge dummy = new dag_GraphEdge(start, start, 0);
		queue.addLast(dummy);

		while(!queue.isEmpty()){
			next = queue.removeFirst();
			settledNodes.put(next.to().id(), next.from().id());
			visited.add(next.to().id());
			if(next.to().id() == targetID){
				int parent = target.id();
				route.addFirst(target);
				do {
					parent = settledNodes.get(parent);
					route.addFirst(graph.getNode(parent));

				} while (parent != startID);
				return route;
			}
			nextEdges = graph.getEdgeList(next.to().id());
			for(dag_GraphEdge ge : nextEdges){
				if(!visited.contains(ge.to().id())){
					queue.addLast(ge);
					visited.add(ge.to().id());
					// Edges visited collection update
					if(remember)
						examinedEdges.add(ge);
				}
			}
		}
		System.out.println("No route found");
		return null;
	}
	
	/**
	 * Get all the edges examined during the search. <br>
	 * 
	 * @return edges examined or array size 0 if none found
	 */
	public dag_GraphEdge[] getExaminedEdges(){
		return getExaminedEdges(new dag_GraphEdge[0]);
	}
	
	/**
	 * Get all the edges examined during the search. <br>
	 * The type of each element in the array will be of type Object
	 * if the parameter is null otherwise it is T (where T is GraphEdge
	 * or any class that extends GraphEdge.
	 * 
	 * @param the array to populate
	 * @return edges examined or array size 0 if none found
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getExaminedEdges(T[] array){
		if(array == null)
			return (T[]) examinedEdges.toArray(new Object[0]);
		else
			return examinedEdges.toArray(array);
	}
	
	/**
	 * Get the path found as an array of GraphNode(s) in start->end
	 * order <br>
	 * @return path found or array size 0 if none found
	 */
	public dag_GraphNode[] getRoute(){
		return route.toArray(new dag_GraphNode[route.size()]);
	}
	
	/**
	 * Get the path found as an array of T(s) in start->end
	 * order. <br>
	 * The type of each element in the array will be of type Object
	 * if the parameter is null, otherwise it is T (where T is GraphNode
	 * or any class that extends GraphNode.
	 * @param the array to populate
	 * @return path found or array size 0 if none found
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getRoute(T[] array){
		if(array == null)
			return (T[]) route.toArray(new Object[0]);
		else
			return route.toArray(array);
	}
	
}
