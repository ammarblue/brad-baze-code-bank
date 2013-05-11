package com.software.reuze;

/*************************************************************************
 *  Compilation:  javac DirectedDFS.java
 *  Execution:    java DirectedDFS V E
 *  Dependencies: Digraph.java Bag.java In.java
 *  Data files:   http://www.cs.princeton.edu/algs4/42directed/tinyDG.txt
 *
 *  Determine single-source or multiple-source reachability in a digraph
 *  using depth first search.
 *  Runs in O(E + V) time.
 *
 *  % java DirectedDFS tinyDG.txt 1
 *  1
 *
 *  % java DirectedDFS tinyDG.txt 2
 *  0 1 2 3 4 5
 *
 *  % java DirectedDFS tinyDG.txt 1 2 6
 *  0 1 2 3 4 5 6 9 10 11 12 
 *
 *************************************************************************/

public class dag_DigraphIsReachable {
    private boolean[] marked;  // marked[v] = true if v is reachable
                               // from source (or sources)

    // single-source reachability
    public dag_DigraphIsReachable(dag_Digraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // multiple-source reachability
    public dag_DigraphIsReachable(dag_Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        for (int v : sources)
            dfs(G, v);
    }

    private void dfs(dag_Digraph G, int v) { 
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    // is there a directed path from the source (or sources) to v?
    public boolean marked(int v) {
        return marked[v];
    }

    // test client
    public static void main(String[] args) {

        // read in digraph from command-line argument
        dag_Digraph G = new dag_Digraph(15,8);
        System.out.println(G);
        // read in sources from command-line arguments
        d_Bag<Integer> sources = new d_Bag<Integer>();
        for (int i = 0; i < args.length; i++) {
            int s = Integer.parseInt(args[i]);
            sources.add(s);
        }

        // multiple-source reachability
        dag_DigraphIsReachable dfs = new dag_DigraphIsReachable(G, sources);

        // print out vertices reachable from sources
        for (int v = 0; v < G.V(); v++) {
            if (dfs.marked(v)) System.out.print(v + " ");
        }
        System.out.println();
    }

}
