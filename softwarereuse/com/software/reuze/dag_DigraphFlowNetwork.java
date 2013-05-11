package com.software.reuze;
import com.software.reuze.m_RandomStd;

/*************************************************************************
 *  Compilation:  javac FlowNetwork.java
 *  Execution:    java FlowNetwork V E
 *  Dependencies: Bag.java FlowEdge.java
 *
 *  A capacitated flow network, implemented using adjacency lists.
 *
 *************************************************************************/

public class dag_DigraphFlowNetwork {
    private final int V;
    private int E;
    private d_Bag<dag_DigraphEdgeWithFlow>[] adj;
    
    // empty graph with V vertices
    public dag_DigraphFlowNetwork(int V) {
        this.V = V;
        this.E = 0;
        adj = (d_Bag<dag_DigraphEdgeWithFlow>[]) new d_Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new d_Bag<dag_DigraphEdgeWithFlow>();
    }

    // random graph with V vertices and E edges
    public dag_DigraphFlowNetwork(int V, int E) {
        this(V);
        for (int i = 0; i < E; i++) {
            int v = m_RandomStd.uniform(V);
            int w = m_RandomStd.uniform(V);
            double capacity = m_RandomStd.uniform(100);
            addEdge(new dag_DigraphEdgeWithFlow(v, w, capacity));
        }
    }

    // graph, read from input stream
/*
    public FlowNetwork(In in) {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double capacity = in.readDouble();
            addEdge(new FlowEdge(v, w, capacity));
        }
    }
*/


    // number of vertices and edges
    public int V() { return V; }
    public int E() { return E; }

    // add edge e in both v's and w's adjacency lists
    public void addEdge(dag_DigraphEdgeWithFlow e) {
        E++;
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        adj[w].add(e);
    }

    // return list of edges incident to  v
    public Iterable<dag_DigraphEdgeWithFlow> adj(int v) {
        return adj[v];
    }

    // return list of all edges
    public Iterable<dag_DigraphEdgeWithFlow> edges() {
        d_Bag<dag_DigraphEdgeWithFlow> list = new d_Bag<dag_DigraphEdgeWithFlow>();
        for (int v = 0; v < V; v++)
            for (dag_DigraphEdgeWithFlow e : adj(v))
                list.add(e);
        return list;
    }


    // string representation of Graph - takes quadratic time
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ":  ");
            for (dag_DigraphEdgeWithFlow e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    // test client
    public static void main(String[] args) {
    	int V;
        int E;
        if (args.length<2) {
        	V=5;  E=9;
        } else {
        	V = Integer.parseInt(args[0]);
        	E  = Integer.parseInt(args[1]);
        }
        dag_DigraphFlowNetwork G = new dag_DigraphFlowNetwork(V, E);

        System.out.println(G);
    }

}
