package com.software.reuze;

/*************************************************************************
 *  Compilation:  javac Digraph.java
 *  Execution:    java Digraph V E
 *  Dependencies: Bag.java
 *
 *  A graph, implemented using an array of lists.
 *  Parallel edges and self-loops are permitted.
 *  
 *************************************************************************/



/**
 *  The <tt>Digraph</tt> class represents an directed graph of vertices
 *  named 0 through V-1.
 *  It supports the following operations: add an edge to the graph,
 *  iterate over all of the neighbors incident to a vertex.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/52directed">Section 5.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */

public class dag_Digraph {
    private final int V;
    private int E;
    private d_Bag<Integer>[] adj;
    
   /**
     * Create an empty digraph with V vertices.
     */
    public dag_Digraph(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (d_Bag<Integer>[]) new d_Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new d_Bag<Integer>();
        }
    }

   /**
     * Create a random digraph with V vertices and E edges.
     */
    public dag_Digraph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            addEdge(v, w);
        }
    }

   /**
     * Create a digraph from input stream.
     */  
/*
    public Digraph(In in) {
        this(in.readInt()); 
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            addEdge(v, w); 
        }
    }
*/
        


   /**
     * Return the number of vertices in the digraph.
     */
    public int V() {
        return V;
    }

   /**
     * Return the number of edges in the digraph.
     */
    public int E() {
        return E;
    }

   /**
     * Add the directed edge v-w to the digraph.
     */
    public void addEdge(int v, int w) {
        adj[v].add(w);
        E++;
    }

   /**
     * Return the list of neighbors of vertex v as in Iterable.
     */
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

   /**
     * Return the reverse of the digraph.
     */
    public dag_Digraph reverse() {
        dag_Digraph R = new dag_Digraph(V);
        for (int v = 0; v < V; v++) {
            for (int w : adj(v)) {
                R.addEdge(w, v);
            }
        }
        return R;
    }

   /**
     * Return a string representation of the digraph.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }


   /**
     * A test client.
     */
    public static void main(String[] args) {
    	int V;
        int E;
        if (args.length<2) {
        	V=5;  E=9;
        } else {
        	V = Integer.parseInt(args[0]);
        	E  = Integer.parseInt(args[1]);
        }
        dag_Digraph G = new dag_Digraph(V, E);
        System.out.println("G");
        System.out.println("------------");
        System.out.println(G);
        System.out.println();
        System.out.println("Reverse of G");
        System.out.println("------------");
        System.out.println(G.reverse());
    }

}
