package com.software.reuze;

/*************************************************************************
 *  Compilation:  javac EdgeWeightedDigraph.java
 *  Execution:    java EdgeWeightedDigraph V E
 *  Dependencies: Bag.java DirectedEdge.java
 *
 *  An edge-weighted digraph, implemented using adjacency lists.
 *
 *************************************************************************/

/**
 *  The <tt>EdgeWeightedDigraph</tt> class represents an directed graph of vertices
 *  named 0 through V-1, where each edge has a real-valued weight.
 *  It supports the following operations: add an edge to the graph,
 *  iterate over all of edges leaving a vertex.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */



public class dag_DigraphEdgeWeighted {
    private final int V;
    private int E;
    private d_Bag<dag_DigraphEdgeWithWeight>[] adj;
    
    /**
     * Create an empty edge-weighted digraph with V vertices.
     */
    public dag_DigraphEdgeWeighted(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (d_Bag<dag_DigraphEdgeWithWeight>[]) new d_Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new d_Bag<dag_DigraphEdgeWithWeight>();
    }

   /**
     * Create a edge-weighted digraph with V vertices and E edges.
     */
    public dag_DigraphEdgeWeighted(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.round(100 * Math.random()) / 100.0;
            dag_DigraphEdgeWithWeight e = new dag_DigraphEdgeWithWeight(v, w, weight);
            addEdge(e);
        }
    }

    /**
     * Create an edge-weighted digraph from input stream.
     */
/*
    public EdgeWeightedDigraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double weight = in.readDouble();
            addEdge(new DirectedEdge(v, w, weight));
        }
    }
*/


   /**
     * Return the number of vertices in this digraph.
     */
    public int V() {
        return V;
    }

   /**
     * Return the number of edges in this digraph.
     */
    public int E() {
        return E;
    }


   /**
     * Add the edge e to this digraph.
     */
    public void addEdge(dag_DigraphEdgeWithWeight e) {
        int v = e.from();
        adj[v].add(e);
        E++;
    }


   /**
     * Return the edges leaving vertex v as an Iterable.
     * To iterate over the edges leaving vertex v, use foreach notation:
     * <tt>for (DirectedEdge e : graph.adj(v))</tt>.
     */
    public Iterable<dag_DigraphEdgeWithWeight> adj(int v) {
        return adj[v];
    }

   /**
     * Return all edges in this graph as an Iterable.
     * To iterate over the edges, use foreach notation:
     * <tt>for (DirectedEdge e : graph.edges())</tt>.
     */
    public Iterable<dag_DigraphEdgeWithWeight> edges() {
        d_Bag<dag_DigraphEdgeWithWeight> list = new d_Bag<dag_DigraphEdgeWithWeight>();
        for (int v = 0; v < V; v++) {
            for (dag_DigraphEdgeWithWeight e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

   /**
     * Return number of edges leaving v.
     */
    public int outdegree(int v) {
        return adj[v].size();
    }



   /**
     * Return a string representation of this graph.
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (dag_DigraphEdgeWithWeight e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Test client.
     */
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        dag_DigraphEdgeWeighted G = new dag_DigraphEdgeWeighted(V, E);
        System.out.println(G);
    }

}
