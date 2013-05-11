package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac CC.java
 *  Dependencies: GraphIntegers.java 
 *
 *  Compute connected components using depth first search.
 *  Runs in O(E + V) time.
 *
 *************************************************************************/

public class dag_GraphConnectedComponents {
    private boolean[] marked;   // marked[v] = has vertex v been marked?
    private int[] id;           // id[v] = id of connected component containing v
    private int[] size;         // size[v] = number of vertices in component containing v
    private int count;          // number of connected components

    public dag_GraphConnectedComponents(dag_GraphIntegers G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    // depth first search
    private void dfs(dag_GraphIntegers G, int v) {
        marked[v] = true;
        id[v] = count;
        size[v]++;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // id of connected component containing v
    public int id(int v) {
        return id[v];
    }

    // size of connected component containing v
    public int size(int v) {
        return size[id[v]];
    }

    // number of connected components
    public int count() {
        return count;
    }

    // are v and w in the same connected component?
    public boolean areConnected(int v, int w) {
        return id(v) == id(w);
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
        dag_GraphIntegers G = new dag_GraphIntegers(V, E);
        System.out.println(G);
        dag_GraphConnectedComponents cc = new dag_GraphConnectedComponents(G);

        System.out.println("Number of connected components = " + cc.count());
        for (int v = 0; v < G.V(); v++) {
            System.out.println(v + ": " + cc.id(v));
        }
    }


}
