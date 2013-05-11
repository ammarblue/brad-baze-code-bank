package com.software.reuze;

/*************************************************************************
 *  Compilation:  javac EdgeWeightedGraph.java
 *  Execution:    java EdgeWeightedGraph V E
 *  Dependencies: Bag.java GraphEdgeWithWeight.java
 *
 *  An edge-weighted undirected graph, implemented using adjacency lists.
 *  Parallel edges and self-loops are permitted.
 *
 *************************************************************************/

/**
 *  The <tt>EdgeWeightedGraph</tt> class represents an undirected graph of vertices
 *  named 0 through V-1, where each edge has a real-valued weight.
 *  It supports the following operations: add an edge to the graph,
 *  in the graph, iterate over all of the neighbors incident to a vertex.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */


public class dag_GraphEdgeWeighted {
    private final int V;
    private int E;
    private d_Bag<dag_GraphEdgeWithWeight>[] adj;
    
   /**
     * Create an empty edge-weighted graph with V vertices.
     */
    public dag_GraphEdgeWeighted(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (d_Bag<dag_GraphEdgeWithWeight>[]) new d_Bag[V];
        for (int v = 0; v < V; v++) adj[v] = new d_Bag<dag_GraphEdgeWithWeight>();
    }

   /**
     * Create a random edge-weighted graph with V vertices and E edges.
     * The expected running time is proportional to V + E.
     */
    public dag_GraphEdgeWeighted(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.round(100 * Math.random()) / 100.0;
            dag_GraphEdgeWithWeight e = new dag_GraphEdgeWithWeight(v, w, weight);
            addEdge(e);
        }
    }

   /**
     * Create a weighted graph from input stream.
     */
/*
    public EdgeWeightedGraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double weight = in.readDouble();
            GraphEdgeWithWeight e = new GraphEdgeWithWeight(v, w, weight);
            addEdge(e);
        }
    }
*/

   /**
     * Return the number of vertices in this graph.
     */
    public int V() {
        return V;
    }

   /**
     * Return the number of edges in this graph.
     */
    public int E() {
        return E;
    }


   /**
     * Add the edge e to this graph.
     */
    public void addEdge(dag_GraphEdgeWithWeight e) {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }


   /**
     * Return the edges incident to vertex v as an Iterable.
     * To iterate over the edges incident to vertex v, use foreach notation:
     * <tt>for (GraphEdgeWithWeight e : graph.adj(v))</tt>.
     */
    public Iterable<dag_GraphEdgeWithWeight> adj(int v) {
        return adj[v];
    }

   /**
     * Return all edges in this graph as an Iterable.
     * To iterate over the edges, use foreach notation:
     * <tt>for (GraphEdgeWithWeight e : graph.edges())</tt>.
     */
    public Iterable<dag_GraphEdgeWithWeight> edges() {
        d_Bag<dag_GraphEdgeWithWeight> list = new d_Bag<dag_GraphEdgeWithWeight>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (dag_GraphEdgeWithWeight e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // only add one copy of each self loop
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
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
            for (dag_GraphEdgeWithWeight e : adj[v]) {
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
        dag_GraphEdgeWeighted G;

            // random graph with V vertices and E edges, parallel edges allowed
        int V;
        int E;
        if (args.length<2) {
        	V=5;  E=9;
        } else {
        	V = Integer.parseInt(args[0]);
        	E  = Integer.parseInt(args[1]);
        }
            G = new dag_GraphEdgeWeighted(V, E);

        System.out.println(G);
    }

}
