package com.software.reuze;
/**
 * 
 */
/**
 * This class is used to represent a directed edge between 2 nodes and the cost 
 * of traversing this edge.
 * 
 * @author Peter Lager
 *
 */
public class dag_GraphEdge implements Comparable<Object>{

	protected dag_GraphNode from;
	protected dag_GraphNode to;	
	protected double cost = 1.0;
	
	/** 
	 * Make protected to prevent its use outside the class.
	 */
	protected dag_GraphEdge(){
		from = to = null;
	}

	/**
	 * Create an edge of cost 1.0f
	 * @param from 'from' node
	 * @param to 'to' node
	 */
	public dag_GraphEdge(dag_GraphNode from, dag_GraphNode to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param edge
	 */
	public dag_GraphEdge(dag_GraphEdge edge){
		from = edge.from;
		to = edge.to;
		cost = edge.cost;
	}
	

	/**
	 * Create an edge from 2 existing nodes. If the cost is 0.0
	 * then calculate the cost based on the physical distance
	 * between the nodes. <br>
	 * 
	 * @param from 'from' node
	 * @param to 'to' node
	 * @param cost traversal cost
	 */
	public dag_GraphEdge(dag_GraphNode from, dag_GraphNode to, double cost) {
		this.from = from;
		this.to = to;
		if(cost == 0.0f){
			cost = Math.sqrt( (to.x() - from.x()) * (to.x() - from.x()) 
					+ (to.y() - from.y()) * (to.y() - from.y()) 
					+ (to.z() - from.z()) * (to.z() - from.z()) );		
		}
		this.cost = cost;
	}

	/**
	 * This constructor is used to create new edges for use with the 
	 * path finding algorithms Dijkstra and A*  <br>
	 * <b>It should not be used directly. </b>
	 * @param edge the existing edge
	 * @param costSoFar the cost to the destination node so far.
	 */
	public dag_GraphEdge(dag_GraphEdge edge, double costSoFar){
		from = edge.from;
		to = edge.to;
		this.cost = costSoFar;
	}
	
	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Change the traversal cost.
	 * @param cost the new traversal cost.
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @return the 'from' node
	 */
	public dag_GraphNode from() {
		return from;
	}

	/**
	 * @return the 'to' node
	 */
	public dag_GraphNode to() {
		return to;
	}
	
	/**
	 * Compare two graph edges.
	 */
	public int compareTo(Object o) {
		dag_GraphEdge ge = (dag_GraphEdge)o;
		if(from.compareTo(ge.from) == 0 && to.compareTo(ge.to) == 0)
			return 0;
		if(from.compareTo(ge.from) == 0)
			return  to.compareTo(ge.to);
		else
			return from.compareTo(ge.from);
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder("Edge from: " + from.id() + " to: " + to.id());
		s.append("   \t cost: " + cost);
		return new String(s);
	}
}
