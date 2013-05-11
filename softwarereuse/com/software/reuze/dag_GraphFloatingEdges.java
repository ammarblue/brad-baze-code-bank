package com.software.reuze;
/**
 * 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
/**
 * 
 * Objects of this class represents graphs that can be used in games. <br>
 * 
 * The class maintains collections of nodes (vertices) and directed edges. <br> 
 * 
 * Each node should have a unique ID number, attempting to add a node which the 
 * same ID as a node already added to the graph will replace the existing node. <br>
 * 
 * An edge is specified by the id numbers to the 2 nodes that are to be joined. Each
 * edge is directed i.e. one-way, so to create a bidirectional (two-way) link between
 * the nodes requires two edges to be created. This does have the advantage that each
 * the cost of traveling between 2 nodes does not have to be the same in both 
 * directions. <br>
 * 
 * It is more efficient to add all the nodes first and then the edges but not essential. <br>
 * 
 * Attempting to add an edge where one or both of the connecting nodes do not yet exist
 * in the graph will be 'remembered' - this is called a floating edge. Once both nodes 
 * have been added to the graph then the floating edge will also be added to the graph. <br>
 * Floating edges are segregated from the graph edges to simply the graph searching 
 * algorithms. <br><br>
 * 
 * This arrangement is very flexible and can simplify the code needed to create the graph
 * at the expense of creating large numbers of floating edges that will never be added
 * to the graph. Once you have created the final graph it is recommended that the user
 * calls the compact method which simply deletes any floating edges and requests a 
 * garbage collection to release the memory. <br><br>
 * 
 * The classes
 * @see	    dag_GraphNode
 * @see		dag_GraphEdge
 * are the base classes used to store nodes and edges. These classes support
 * inheritance so you can provide more specialized classes for your graphs. <br>
 * 
 * The following classes can be used to search the graph.
 * 
 * @see	    dag_GraphSearch_DFS
 * @see		dag_GraphSearch_BFS
 * @see		dag_GraphSearch_Dijkstra
 * @see		dag_GraphSearch_Astar
 * 
 * <br>
 * 
 * @author Peter Lager
 */
public class dag_GraphFloatingEdges implements Iterable<dag_GraphNode> {

	// Data structures to hold nodes and edges
	protected HashMap<Integer, dag_GraphNode> nodes;
	protected HashMap<dag_GraphNode, LinkedList<dag_GraphEdge>> edgeLists;
	// Data structure to hold floating edges.
	protected HashMap<Integer, LinkedList<FloatingEdge>> nodesToBe;

	/**
	 * Create a graph with an initial capacity of 16 nodes.
	 * @param nbrNodes
	 */
	public dag_GraphFloatingEdges(){
		this(16);
	}

	/**
	 * Create a graph with an initial capacity based on an estimate of 
	 * the number of nodes to be added.
	 * @param nbrNodes
	 */
	public dag_GraphFloatingEdges(int nbrNodes){
		nodes = new HashMap<Integer, dag_GraphNode>(nbrNodes);
		edgeLists = new HashMap<dag_GraphNode, LinkedList<dag_GraphEdge>>(nbrNodes);
	}
	
	public Iterator<dag_GraphNode> iterator() {
		return nodes.values().iterator();
	}
	
	public Iterator<Integer> iteratorIds() {
		return nodes.keySet().iterator();
	}
	
	public Iterator<dag_GraphEdge> iteratorEdges() {
		return new EdgeIterator();
	}
	
	private class EdgeIterator implements Iterator<dag_GraphEdge> {
		Iterator<LinkedList<dag_GraphEdge>> it;
		LinkedList<dag_GraphEdge> ll;
		Iterator<dag_GraphEdge> gei;
		public EdgeIterator() {
			it=edgeLists.values().iterator();
		}
		
		public boolean hasNext() {
			if (ll==null) {
				if (!it.hasNext()) return false;
				ll=it.next(); gei=null;
			}
			if (gei==null) gei=ll.listIterator();
			if (!gei.hasNext()) {ll=null; return hasNext();}
			return true;
		}

		public dag_GraphEdge next() {
			if (gei==null) return null;
			return gei.next();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	/**
	 * Add a node to the list. The user must ensure that the node id is unique.
	 * @param node
	 */
	public void addNode(dag_GraphNode node){
		nodes.put(node.id(), node);
		if(nodesToBe != null)
			resolveFloatEdges(node);
	}

	/**
	 * This method is called every time a node is added to the graph. It will 
	 * update all floating edges and where possible add the resolved floating 
	 * edge to the graph. 
	 * @param node a node to be added to the graph (must not be null)
	 */
	protected void resolveFloatEdges(dag_GraphNode node){
		int nodeID = node.id();
		LinkedList<FloatingEdge> elist = nodesToBe.get(nodeID);
		if(elist != null){
			Iterator<FloatingEdge> iter = elist.iterator();
			while(iter.hasNext()){
				FloatingEdge edge = iter.next();
				if(edge.fromID == nodeID)
					edge.from = node;
				else if(edge.toID == nodeID)
					edge.to = node;
				if(edge.from != null && edge.to != null){
					addValidEdge(new dag_GraphEdge(edge.from, edge.to, edge.cost));
					iter.remove();
				}
			}
			// See if we have emptied the edgelist for this node id
			if(elist.isEmpty()){
				nodesToBe.remove(nodeID);
				// Edge list has been removed so see if there are
				// any more 'nodes to be' if not dump nodesToBe 
				if(nodesToBe.isEmpty())
					nodesToBe = null;
			}
		}
	}

	/**
	 * If the node exists remove it and all edges that start
	 * or end at this node.
	 * @param nodeID id of the node to remove
	 * @return true if the node was removed else false
	 */
	public boolean removeNode(int nodeID){
		dag_GraphNode node = nodes.get(nodeID);
		if(node == null)
			return false;
		edgeLists.remove(node);	// remove edges from this node
		nodes.remove(nodeID);	// remove node
		// get a list of all edges that go to the node we just removed
		dag_GraphEdge[] edges = toArrayEdges();
		ArrayList<dag_GraphEdge> edgesToRemove = new ArrayList<dag_GraphEdge>();
		for(int i = 0; i < edges.length; i++){
			if(edges[i].to().id() == nodeID)
				edgesToRemove.add(edges[i]);
		}
		// Now remove these edges.
		for(dag_GraphEdge edge : edgesToRemove)
			edgeLists.get(edge.from).remove(edge);
		return true;
	}

	/**
	 * Get a node with a given id.
	 * 
	 * @param id
	 * @return the node if it exists else null
	 */
	public dag_GraphNode getNode(int id){
		return nodes.get(id);
	}

	/**
	 * Does a node with a given id exist?
	 * @param id
	 * @return true if the node exists else false
	 */
	public boolean hasNode(int id){
		return nodes.get(id) != null;		
	}
	
	/**
	 * Locate and return the first node encountered that is within a
	 * stated distance of a position at [x,y,z]
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param maxDistance only consider a node that is with this distance of [x,y,z]
	 * @return the node if it meets the distance criteria else null
	 */
	public dag_GraphNode getNodeNear(double x, double y, double z, double maxDistance){
		double d2 = maxDistance * maxDistance, dx2, dy2, dz2;
		Collection<dag_GraphNode> c = nodes.values();
		for(dag_GraphNode node : c){
			dx2 = (node.x() - x)*(node.x() - x);
			dy2 = (node.y() - y)*(node.y() - y);
			dz2 = (node.z() - z)*(node.z() - z);
			if(dx2+dy2+dz2 < d2)
				return node;
		}	
		return null;
	}

	/**
	 * Locate and return the node nearest a given position irrespective of the distance apart.
	 * @param x
	 * @param y
	 * @param z
	 * @return the nearest node
	 */
	public dag_GraphNode getNodeNearest(double x, double y, double z){
		double d2 = Double.MAX_VALUE, dist;
		dag_GraphNode nearest = null;
		Collection<dag_GraphNode> c = nodes.values();
		for(dag_GraphNode node : c){
			dist = (node.x() - x)*(node.x() - x) +
					(node.y() - y)*(node.y() - y) +
					(node.z() - z)*(node.z() - z);
			if(dist < d2){
				d2 = dist;
				nearest = node;				
			}
		}	
		return nearest;
	}

	
	/**
	 * get the number of nodes in the graph
	 * @return
	 */
	public int getNbrNodes(){
		return nodes.size();
	}

	/**
	 * Add a unidirectional edge to the graph.
	 * 
	 * @param fromID the ID number of the from node
	 * @param toID the ID number of the to node
	 * @param cost cost from > to
	 * @return true if the edge was added else false
	 */
	public boolean addEdge(int fromID, int toID, double cost){
		dag_GraphNode fromNode = nodes.get(fromID);
		dag_GraphNode toNode = nodes.get(toID);
		dag_GraphEdge ge;
		if(fromNode != null && toNode != null){
			ge = new dag_GraphEdge(fromNode, toNode ,cost);
			addValidEdge(ge);
			return true;
		}
		FloatingEdge floatEdge = new FloatingEdge(fromID, toID, fromNode, toNode, cost);
		if(fromNode == null)
			rememberFloatingEdge(fromID, floatEdge);
		if(toNode == null)
			rememberFloatingEdge(toID, floatEdge);
		return false;
	}

	/**
	 * Add bidirectional link with the costs indicated.
	 * 
	 * @param fromID the ID number of the from node
	 * @param toID the ID number of the to node
	 * @param costOutward cost from > to
	 * @param costInward cost to > from
	 * @return true if the edge was added else false
	 */
	public boolean addEdge(int fromID, int toID, double costOutward, double costInward){
		boolean added = false;
		added = addEdge(fromID, toID, costOutward);
		added &= addEdge(toID, fromID, costInward);
		return added;
	}

	/**
	 * This method is called to add a validated edge to the graph.
	 * @param edge the validated edge to add.
	 */
	protected void addValidEdge(dag_GraphEdge edge){
		dag_GraphNode fromNode = edge.from();
		LinkedList<dag_GraphEdge> geList = edgeLists.get(fromNode);
		if(geList == null){
			geList = new LinkedList<dag_GraphEdge>();
			edgeLists.put(fromNode, geList);
		}
		geList.add(edge);
	}

	/**
	 * This method is used to remember floating edges.
	 * @param id
	 * @param floatEdge
	 */
	protected void rememberFloatingEdge(int id, FloatingEdge floatEdge){
		if(nodesToBe == null)
			nodesToBe = new HashMap<Integer, LinkedList<FloatingEdge>>();
		if(!nodesToBe.containsKey(id))
			nodesToBe.put(id, new LinkedList<FloatingEdge>());
		nodesToBe.get(id).add(floatEdge);
	}

	//	public void unusedFloatingEdges(){
	//		System.out.println("Unresolved floating edges");
	//		int count = 0;
	//		if(nodesToBe != null){
	//			Collection<LinkedList<FloatingEdge>> c = nodesToBe.values();
	//			for(LinkedList<FloatingEdge> list : c){
	//				for(FloatingEdge fedge : list){
	//					System.out.println(fedge);
	//					count++;
	//				}
	//			}
	//		}
	//		System.out.println("======  " + count +"  ============================");
	//	}

	/**
	 * Clear out all remaining floating edges.
	 */
	public void compact(){
		if(nodesToBe != null){
			Collection<LinkedList<FloatingEdge>> c = nodesToBe.values();
			for(LinkedList<FloatingEdge> list : c)
				list.clear();
			nodesToBe.clear();
			nodesToBe = null;
			System.gc();	// request garbage collection
		}
	}

	/**
	 * Get the edge between 2 nodes. <br>
	 * If either node does not exist or there is no edge
	 * exists between them then the method returns null.
	 * @param fromID ID for the from node
	 * @param toID ID for the to node
	 * @return the edge or null if it doesn't exist
	 */
	public dag_GraphEdge getEdge(int fromID, int toID){
		dag_GraphNode fromNode = nodes.get(fromID);
		dag_GraphNode toNode = nodes.get(toID);
		if(fromNode == null || toNode == null)
			return null;
		LinkedList<dag_GraphEdge> edgeList = edgeLists.get(fromNode);
		for(dag_GraphEdge ge : edgeList){
			if(ge.to() == toNode)
				return ge;
		}
		return null;		
	}

	/**
	 * Get the cost of traversing an edge between 2 nodes. <br>
	 * If either node does not exist or there is no edge
	 * exists between them then the method returns a value <0.
	 * @param fromID ID for the from node
	 * @param toID ID for the to node
	 * @return the edge or null if it doesn't exist
	 */
	public double getEdgeCost(int fromID, int toID){
		dag_GraphNode fromNode = nodes.get(fromID);
		dag_GraphNode toNode = nodes.get(toID);
		if(fromNode == null || toNode == null)
			return -1;
		LinkedList<dag_GraphEdge> edgeList = edgeLists.get(fromNode);
		for(dag_GraphEdge ge : edgeList){
			if(ge.to() == toNode)
				return ge.getCost();
		}
		return -1;		
	}

	/**
	 * Remove an edge between 2 nodes. <br>
	 * This will delete the edge from one node to another
	 * but does not remove any return edge. <br>
	 * To remove a 'bidirectional route' between nodes
	 * 22 and 33 then you must call this method twice e.g.
	 * <code>
	 * graph.removeEdge(22, 33);
	 * graph.removeEdge(33, 22);
	 * </code>
	 * @param fromID ID for the from node
	 * @param toID ID for the to node
	 * @return true if an edge has been removed
	 */
	public boolean removeEdge(int fromID, int toID){
		dag_GraphEdge ge = getEdge(fromID, toID);
		if(ge != null){
			dag_GraphNode fromNode = nodes.get(fromID);
			edgeLists.get(fromNode).remove(ge);
			return true;
		}
		return false;
	}

	/**
	 * Sees whether the graph has this edge
	 * @param from node id of from-node
	 * @param to node if of to-node
	 * @return true if the graph has this node else false
	 */
	public boolean hasEdge(int from, int to){
		dag_GraphNode fromNode = nodes.get(from);
		dag_GraphNode toNode = nodes.get(to);
		if(fromNode != null && toNode != null){
			LinkedList<dag_GraphEdge> geList = edgeLists.get(fromNode);
			Iterator<dag_GraphEdge> iter = geList.iterator();
			while(iter.hasNext()){
				if(iter.next().to() == toNode)
					return true;
			}
		}
		return false;
	}

	/**
	 * Gets a list of GraphEdges from this node. <br>
	 * Used by graph search classes.
	 * @param nodeID id of the node where the edges start from
	 * @return
	 */
	public LinkedList<dag_GraphEdge> getEdgeList(int nodeID){
		return edgeLists.get(nodes.get(nodeID));
	}

	/**
	 * Gets a list of GraphEdges from this node. <br>
	 * Used by graph search classes.
	 * @param nodeID the node where the edges start from
	 * @return
	 */
	public LinkedList<dag_GraphEdge> getEdgeList(dag_GraphNode node){
		return edgeLists.get(node);
	}

	/**
	 * Will return an array of all the GraphEdges in the graph. <br>
	 * The type of each element in the array will be of type GraphEdge
	 *  
	 * @return
	 */
	public dag_GraphEdge[] toArrayEdges(){
		return toArrayEdges(new dag_GraphEdge[0]);
	}

	/**
	 * Will return an array of all the GraphEdges in the graph. <br>
	 * The type of each element in the array will be of type Object 
	 * if the parameter is null otherwise it is T (where T is GraphEdge
	 * or any class derived from GraphEdge.
	 * 
	 * @param <T>
	 * @param from the node where the edges start from
	 * @param array a zero length array of GraphNode or any derived class.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends dag_GraphEdge> T[] toArrayEdges(T[] array){
		if(array == null)
			array = (T[]) new Object[0];
		LinkedList<dag_GraphEdge> edges = new LinkedList<dag_GraphEdge>(); 
		Collection<LinkedList<dag_GraphEdge>> c = edgeLists.values();
		for (LinkedList<dag_GraphEdge> geList : c)
			edges.addAll(geList);
		return edges.toArray(array);
	}

	public int getNbrEdges(){
		int count = 0;
		Collection<LinkedList<dag_GraphEdge>> c = edgeLists.values();
		for(LinkedList<dag_GraphEdge> geList : c)
			count += geList.size();
		return count;
	}
	
	/**
	 * Will return an array of all the GraphEdges that start from the node. <br>
	 * The type of each element in the array will be of type GraphEdge
	 * 
	 * @param <T>
	 * @param from the node where the edges start from
	 * @return
	 */
	public dag_GraphEdge[] toArrayEdges(int from){
		return toArrayEdges(from, new dag_GraphEdge[0]);
	}

	/**
	 * Will return an array of all the GraphEdges that start from the node. <br>
	 * The type of each element in the array will be of type Object
	 * if the parameter is null otherwise it is T (where T is GrahEdge
	 * or any class that extends GrahEdge.
	 * 
	 * @param <T>
	 * @param from the node where the edges start from
	 * @param array a zero length array of GraphNode or any derived class.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends dag_GraphEdge> T[] toArrayEdges(int from, T[] array){
		if(array == null)
			array = (T[]) new Object[0];
		LinkedList<dag_GraphEdge> edges = getEdgeList(from);
		return edges.toArray(array);
	}

	/**
	 * Will return an array of all the GraphNodes in the graph. <br>
	 * The type of each element in the array will be of type GraphNode
	 * 
	 * @param array a zero length array of GraphNode or any derived class.
	 * @return
	 */
	public dag_GraphNode[] toArray() {
		return toArray(new dag_GraphNode[0]);
	}

	/**
	 * Will return an array of all the GraphNodes in the graph. <br>
	 * The type of each element in the array will be of type Object
	 * if the parameter is null otherwise it is T (where T is GraphNode
	 * or any class that extends GraphNode.
	 * 
	 * @param <T>
	 * @param array a zero length array of GraphNode or any derived class.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends dag_GraphNode> T[] toArray(T[] array) {
		if(array == null)
			array = (T[]) new Object[0];
		Collection<dag_GraphNode> c = nodes.values();
		return c.toArray(array);
	}

	/**
	 * Used for debugging only.
	 */
	public String toString(){
		LinkedList<dag_GraphEdge> edgeList;
		StringBuilder s = new StringBuilder("========= Graph ==============");
		LinkedList<dag_GraphNode> n = new LinkedList<dag_GraphNode>();
		n.addAll(nodes.values());
		Collections.sort(n);
		//Collection<Integer> k = nodes.keySet();
		for(dag_GraphNode node : n){
			s.append("\n\t" + node);
			edgeList = edgeLists.get(node);
			if(edgeList != null) {
				for(dag_GraphEdge edge : edgeList)
					s.append("\n\t\t" + edge);
			}
		}
		s.append("\n===================================\n");
		return new String(s);
	}

	/**
	 * Inner class to represent floating edges.
	 * 
	 * @author Peter Lager
	 *
	 */
	private class FloatingEdge {

		public int fromID = -1;
		public int toID = -1;
		public dag_GraphNode from;
		public dag_GraphNode to;
		public double cost = 1.0;

		/**
		 * @param fromID
		 * @param toID
		 * @param from
		 * @param to
		 * @param cost
		 */
		public FloatingEdge(int fromID, int toID, dag_GraphNode from, dag_GraphNode to,
				double cost) {
			super();
			this.fromID = fromID;
			this.toID = toID;
			this.from = from;
			this.to = to;
			this.cost = cost;
		}

	}

}
