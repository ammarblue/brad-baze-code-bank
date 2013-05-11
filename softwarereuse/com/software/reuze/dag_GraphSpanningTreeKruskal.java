package com.software.reuze;
/*************************************************************************
 * Compilation:  javac KruskalMST.java
 * Execution:  java KruskalMST V E
 * Dependencies: GraphEdgeWeighted.java GraphEdgeWithWeight.java Queue.java UF.java
 *
 * Kruskal's algorithm to compute a minimum spanning forest.
 *
 * % java KruskalMST < graph8.txt
 *
 *************************************************************************/

public class dag_GraphSpanningTreeKruskal {
    private double weight;  // weight of MST
    private d_Queue<dag_GraphEdgeWithWeight> mst = new d_Queue<dag_GraphEdgeWithWeight>();  // edges in MST

    // Kruskal's algorithm
    public dag_GraphSpanningTreeKruskal(dag_GraphEdgeWeighted G) {
        // more efficient to build heap by passing array of edges
    	d_QueuePriorityGeneric<dag_GraphEdgeWithWeight> pq = new d_QueuePriorityGeneric<dag_GraphEdgeWithWeight>();
        for (dag_GraphEdgeWithWeight e : G.edges()) {
            pq.insert(e);
        }

        // run greedy algorithm
        da_SetsUnionFind uf = new da_SetsUnionFind(G.V());
        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            dag_GraphEdgeWithWeight e = pq.delMin();
            int v = e.either();
            int w = e.other(v);
            if (!uf.connected(v, w)) { // v-w does not create a cycle
                uf.union(v, w);  // merge v and w components
                mst.enqueue(e);  // add edge e to mst
                weight += e.weight();
            }
        }

        // check optimality conditions
        assert check(G);
    }

    // edges in minimum spanning forest as an Iterable
    public Iterable<dag_GraphEdgeWithWeight> edges() {
        return mst;
    }

    // weight of minimum spanning forest
    public double weight() {
        return weight;
    }
    
    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(dag_GraphEdgeWeighted G) {

        // check total weight
        double total = 0.0;
        for (dag_GraphEdgeWithWeight e : edges()) {
            total += e.weight();
        }
        double EPSILON = 1E-12;
        if (Math.abs(total - weight()) > EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        // check that it is acyclic
        da_SetsUnionFind uf = new da_SetsUnionFind(G.V());
        for (dag_GraphEdgeWithWeight e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (dag_GraphEdgeWithWeight e : edges()) {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (dag_GraphEdgeWithWeight e : edges()) {
            int v = e.either(), w = e.other(v);

            // all edges in MST except e
            uf = new da_SetsUnionFind(G.V());
            for (dag_GraphEdgeWithWeight f : mst) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }
            
            // check that e is min weight edge in crossing cut
            for (dag_GraphEdgeWithWeight f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("GraphEdgeWithWeight " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }

    public static void main(String[] args) {
        dag_GraphEdgeWeighted G;

            // random graph with V vertices and E edges
        int V;
        int E;
        if (args.length<2) {
        	V=5;  E=9;
        } else {
        	V = Integer.parseInt(args[0]);
        	E  = Integer.parseInt(args[1]);
        }
        G = new dag_GraphEdgeWeighted(V, E);

        // print G
        if (G.V() <= 10) System.out.println(G);

        // compute and print MST
        dag_GraphSpanningTreeKruskal mst = new dag_GraphSpanningTreeKruskal(G);
        System.out.println("total weight = " + mst.weight());
        for (dag_GraphEdgeWithWeight e : mst.edges())
            System.out.println(e);
    }

}
