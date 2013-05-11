package com.software.reuze;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;



/**
 * 
 * @author Peter Lager
 *
 */
public class dag_GraphSearch_Astar  implements dag_i_GraphSearch {

	protected dag_GraphFloatingEdges graph;


	/** 
	 * The settled nodes - the nodes whose shortest distances from 
	 * the source have been found. 
	 */
	protected HashSet<dag_GraphNode> settledNodes;

	/**
	 * The unsettled nodes
	 */
	protected PriorityQueue<GraphNodeCost> unsettledNodes;

	/**
	 * Indicates the predecessor node for a given node.
	 * This is used to store the shortest path.
	 * <node of interest, node where the edge originated>
	 */
	protected HashMap<dag_GraphNode, dag_GraphNode> parent;
	
	/**
	 * Stores the smallest path cost found so far for a given node
	 */
	protected HashMap<dag_GraphNode, Double> graphCostToNode;
	
	/**
	 * Stores the smallest full cost (path cost + heuristic cost)
	 * found so far for a given node.
	 */
	protected HashMap<dag_GraphNode, Double> fullCostAtNode;
	
	/**
	 * List for routes nodes in order of travel
	 */
	protected LinkedList<dag_GraphNode> route;
	
	/**
	 * The heuristic used to estimate cost to target.
	 */
	protected das_i_GraphAstar ash;
	
	/**
	 * Used to remember examined edges
	 */
	protected HashSet<dag_GraphEdge> examinedEdges;

	/**
	 * Create a search object that uses the A* algorithm for the given graph. <br>
	 * By default it uses the crow-flies heuristic. <br>
	 * @param graph the graph to use
	 */
	public dag_GraphSearch_Astar(dag_GraphFloatingEdges graph) {
		super();
		this.graph = graph;
		ash = new das_GraphAstarCrowFlight();
		makeDataStores(graph.getNbrNodes());	
	}

	/**
	 * Create a search object that uses the A* algorithm for the given graph 
	 * using the given heuristic. <br>
	 * @param graph the graph to use
	 * @param ash the heuristic to use
	 */
	public dag_GraphSearch_Astar(dag_GraphFloatingEdges graph, das_i_GraphAstar ash) {
		super();
		this.graph = graph;
		this.ash = ash;
		makeDataStores(graph.getNbrNodes());	
	}

	/**
	 * Create the data stores needed by A*
	 * @param nbrNodes number of nodes in the graph
	 */
	private void makeDataStores(int nbrNodes){
		// Create the data structures
		settledNodes = new HashSet<dag_GraphNode>(nbrNodes);
		parent = new HashMap<dag_GraphNode, dag_GraphNode>(nbrNodes);
		graphCostToNode = new HashMap<dag_GraphNode, Double>(nbrNodes);
		fullCostAtNode = new HashMap<dag_GraphNode, Double>(nbrNodes);
		unsettledNodes = new PriorityQueue<GraphNodeCost>(nbrNodes);		
		examinedEdges = new HashSet<dag_GraphEdge>();
		route = new LinkedList<dag_GraphNode>();
	}
	
	/**
	 * Clears all data related to a search so this object can be
	 * reused for another search
	 */
	private void clear(){
		graphCostToNode.clear();
		fullCostAtNode.clear();
		settledNodes.clear();
		parent.clear();
		unsettledNodes.clear();
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
		dag_GraphNode next, edgeTo;;
		GraphNodeCost pqNext;
		double gCost, hCost;
		
		dag_GraphNode start = graph.getNode(startID);
		dag_GraphNode target = graph.getNode(targetID);
		if(start == null || target == null)
			return null;

		hCost = ash.getCost(start, target);

		unsettledNodes.add(new GraphNodeCost(start, 0.0, hCost));
		graphCostToNode.put(start, 0.0);
		fullCostAtNode.put(start, hCost);

		while(!unsettledNodes.isEmpty()){
			pqNext = unsettledNodes.poll();
			next = pqNext.node;
			if(next == target){
				dag_GraphNode n = target;
				route.addFirst(n);
				while(n != start){
					n = parent.get(n);
					route.addFirst(n);
				}
				return route;
			}
			settledNodes.add(next);
			nextEdges = graph.getEdgeList(next.id()); //TODO doesn't work unless front and back edges between 2 nodes
			// Relax edges
			for(dag_GraphEdge edge : nextEdges){
				edgeTo = edge.to();
				// Calculate graph cost to edge end node
				gCost = getGraphCost(next) + edge.getCost();
				// Calculate heuristic cost for edge end node
				hCost = ash.getCost(edgeTo, target);
				
				if(!settledNodes.contains(edgeTo) && getFullCost(edgeTo) > gCost + hCost){
					graphCostToNode.put(edgeTo, gCost);
					fullCostAtNode.put(edgeTo, gCost + hCost);
					parent.put(edgeTo, next);
					if(remember)
						examinedEdges.add(edge);
					unsettledNodes.add(new GraphNodeCost(edgeTo, gCost, hCost));
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
	 * if the parameter is null otherwise it is T (where T is GraphNode
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

	/**
	 * Used during search.
	 * @param node
	 * @return
	 */
	protected double getGraphCost(dag_GraphNode node){
		Double c = graphCostToNode.get(node);
		if(c == null)
			return Double.MAX_VALUE;
		else
			return c; 
	}
	
	/**
	 * Used during search.
	 * @param node
	 * @return
	 */
	protected double getFullCost(dag_GraphNode node){
		Double fc = fullCostAtNode.get(node);
		if(fc == null)
			return Double.MAX_VALUE;
		else
			return fc; 
	}
	

	/**
	 * Objects of this class are created as and when required by the 
	 * path search algorithm
	 * 
	 * @author Peter Lager
	 *
	 */
	private class GraphNodeCost implements Comparable<Object>{
		private dag_GraphNode node;
		private Double fCost;

		public GraphNodeCost(dag_GraphNode node, Double gCost, Double hCost) {
			super();
			this.node = node;
			fCost = gCost + hCost;
		}

		public int compareTo(Object o) {
			GraphNodeCost gnc = (GraphNodeCost)o;
			if(fCost == gnc.fCost)
				return node.compareTo(gnc.node);
			else
				return fCost.compareTo(gnc.fCost);
		}

	}
}
