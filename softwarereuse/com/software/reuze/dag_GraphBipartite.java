package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac Bipartite.java
 *  Dependencies: Graph.java 
 *
 *  Given a graph, find either (i) a bipartition or (ii) an odd-length cycle.
 *  Runs in O(E + V) time.
 *
 *
 *************************************************************************/
import java.util.Stack;

public class dag_GraphBipartite {
    private boolean isBipartite;   // is the graph bipartite?
    private boolean[] color;       // color[v] gives vertices on one side of bipartition
    private boolean[] marked;      // marked[v] = true if v has been visited in DFS
    private int[] edgeTo;          // edgeTo[v] = last edge on path to v
    private Stack<Integer> cycle;  // odd-length cycle

    public dag_GraphBipartite(dag_GraphIntegers G) {
        isBipartite = true;
        color  = new boolean[G.V()];
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
//                color[v] = false;
                dfs(G, v);
            }
        }
        assert check(G);
    }

    private void dfs(dag_GraphIntegers G, int v) { 
        marked[v] = true;
        for (int w : G.adj(v)) {

            // short circuit if odd-length cycle found
            if (cycle != null) return;

            // found uncolored vertex, so recur
            if (!marked[w]) {
                edgeTo[w] = v;
                color[w] = !color[v];
                dfs(G, w);
            } 

            // if v-w create an odd-length cycle, find it
            else if (color[w] == color[v]) {
                isBipartite = false;
                cycle = new Stack<Integer>();
                cycle.push(w);  // don't need this unless you want to include start vertex twice
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
            }
        }
    }

    boolean isBipartite()            { return isBipartite; }
    boolean color(int v)             { return color[v];    }
    public Iterable<Integer> cycle() { return cycle;       }

    private boolean check(dag_GraphIntegers G) {
        // graph is bipartite
        if (isBipartite) {
            for (int v = 0; v < G.V(); v++) {
                for (int w : G.adj(v)) {
                    if (color[v] == color[w]) {
                        System.err.printf("edge %d-%d with %d and %d in same side of bipartition\n", v, w, v, w);
                        return false;
                    }
                }
            }
        }

        // graph has an odd-length cycle
        else {
            // verify cycle
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // create random bipartite graph with V vertices and E edges; then add F random edges
        int V;
        int E, F;
        if (args.length<3) {
        	V=5;  E=9;  F=3;
        } else {
        	V = Integer.parseInt(args[0]);
        	E  = Integer.parseInt(args[1]);
        	F = Integer.parseInt(args[2]);
        }
        dag_GraphIntegers G = new dag_GraphIntegers(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        m_RandomStd.shuffle(vertices);
        for (int i = 0; i < E; i++) {
            int v = m_RandomStd.uniform(V/2);
            int w = m_RandomStd.uniform(V/2);
            G.addEdge(vertices[v], vertices[V/2 + w]);
        }

        // add F extra edges
        for (int i = 0; i < F; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            G.addEdge(v, w);
        }

        System.out.println(G);


        dag_GraphBipartite b = new dag_GraphBipartite(G);
        if (b.isBipartite()) {
            System.out.println("Graph is bipartite");
            for (int v = 0; v < G.V(); v++) {
                System.out.println(v + ": " + b.color(v));
            }
        }
        else {
            System.out.print("Graph has an odd-length cycle: ");
            for (int x : b.cycle()) {
                System.out.print(x + " ");
            }
            System.out.println();
        }
    }


}
