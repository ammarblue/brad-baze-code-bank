package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac DepthFirstSearch.java
 *  Execution:    java DepthFirstSearch filename s
 *  Dependencies: Graph.java
 *
 *  Run depth first search on an undirected graph.
 *  Runs in O(E + V) time.
 *
 *  % java DepthFirstSearch tinyG.txt 0
 *  0 1 2 3 4 5 6 
 *  not connected
 *
 *  % java DepthFirstSearch tinyG.txt 9
 *  9 10 11 12 
 *  not connected
 *
 *************************************************************************/

public class dag_GraphVisitDepthFirst {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int count;           // number of vertices connected to s

    public dag_GraphVisitDepthFirst(dag_GraphIntegers G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // depth first search from v
    private void dfs(dag_GraphIntegers G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // is there an s-v path?
    public boolean marked(int v) {
        return marked[v];
    }

    // number of vertices connected to s
    public int count() {
        return count;
    }

    // test client
    public static void main(String[] args) {
        dag_GraphIntegers G = new dag_GraphIntegers(15,8);
        System.out.println(G);
        int s;
        if (args.length==1) s= Integer.parseInt(args[0]); else s=3;
        dag_GraphVisitDepthFirst search = new dag_GraphVisitDepthFirst(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (search.marked(v))
                System.out.print(v + " ");
        }

        System.out.println();
        if (search.count() != G.V()) System.out.println("not connected");
        else                         System.out.println("connected");
    }
}

