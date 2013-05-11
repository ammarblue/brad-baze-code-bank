package com.software.reuze;
import com.software.reuze.m_RandomStd;

/*************************************************************************
 *  Compilation:  javac Topoological.java
 *  Dependencies: Digraph.java DepthFirstOrder.java DirectedCycle.java
 *                EdgeWeightedDigraph.java EdgeWeightedDirectedCycle.java
 *
 *  Compute topological ordering of a DAG.
 *  Runs in O(E + V) time.
 *
 *
 *************************************************************************/
public class dag_DigraphAcyclicSortTopological {
    private Iterable<Integer> order;    // topological order

    // topological sort in a digraph
    public dag_DigraphAcyclicSortTopological(dag_Digraph G) {
        dag_DigraphCycle finder = new dag_DigraphCycle(G);
        if (!finder.hasCycle()) {
            dag_DigraphVisitDepthFirst dfs = new dag_DigraphVisitDepthFirst(G);
            order = dfs.reversePostorder();
        }
    }

    // topological sort in an edge-weighted digraph
    public dag_DigraphAcyclicSortTopological(dag_DigraphEdgeWeighted G) {
        dag_DigraphEdgeWeightedCycle finder = new dag_DigraphEdgeWeightedCycle(G);
        if (!finder.hasCycle()) {
            dag_DigraphVisitDepthFirst dfs = new dag_DigraphVisitDepthFirst(G);
            order = dfs.reversePostorder();
        }
    }

    // return topological order if a DAG; null otherwise
    public Iterable<Integer> order() {
        return order;
    }

    // does digraph have a topological order?
    public boolean hasOrder() {
        return order != null;
    }



    public static void main(String[] args) {

        // create random DAG with V vertices and E edges
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        dag_Digraph G = new dag_Digraph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        m_RandomStd.shuffle(vertices);

        for (int i = 0; i < E; i++) {
            int v, w;
            do {
                v = m_RandomStd.uniform(V);
                w = m_RandomStd.uniform(V);
            } while (v >= w);
            G.addEdge(vertices[v], vertices[w]);
        }

        System.out.println("Acyclic graph G");
        System.out.println("---------------");
        System.out.println(G);

        System.out.println();
        System.out.println("Vertices in topological order by construction");
        System.out.println("---------------------------------------------");
        for (int i = 0; i < V; i++) 
            System.out.print(vertices[i] + " ");
        System.out.println();
        System.out.println();

        // compute its topological order
        // may not match above since there can be many topological orders
        System.out.println("Vertices in topological order by algorithm");
        System.out.println("---------------------------------------------");
        dag_DigraphAcyclicSortTopological topological = new dag_DigraphAcyclicSortTopological(G);
        for (int v : topological.order()) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

}
