package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac LazyPrimMST.java
 *  Execution:    java LazyPrimMST V E
 *  Dependencies: GraphEdgeWeighted.java GraphEdgeWithWeight.java Queue.java MinPQ.java
 *                UF.java
 *
 *  Prim's algorithm to compute a minimum spanning forest.
 *
 *************************************************************************/

public class dag_GraphSpanningTreePrim {
    private double weight;       // total weight of MST
    private d_Queue<dag_GraphEdgeWithWeight> mst;     // edges in the MST
    private boolean[] marked;    // marked[v] = true if v on tree
    private d_QueuePriorityGeneric<dag_GraphEdgeWithWeight> pq;      // edges with one endpoint in tree

    // compute minimum spanning forest of G
    public dag_GraphSpanningTreePrim(dag_GraphEdgeWeighted G) {
        mst = new d_Queue<dag_GraphEdgeWithWeight>();
        pq = new d_QueuePriorityGeneric<dag_GraphEdgeWithWeight>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)     // run Prim from all vertices to
            if (!marked[v]) prim(G, v);     // get a minimum spanning forest

        // check optimality conditions
        assert check(G);
    }

    // run Prim's algorithm
    private void prim(dag_GraphEdgeWeighted G, int s) {
        scan(G, s);
        while (!pq.isEmpty()) {                        // better to stop when mst has V-1 edges
            dag_GraphEdgeWithWeight e = pq.delMin();                      // smallest edge on pq
            int v = e.either(), w = e.other(v);        // two endpoints
            assert marked[v] || marked[w];
            if (marked[v] && marked[w]) continue;      // lazy, both v and w already scanned
            mst.enqueue(e);                            // add e to MST
            weight += e.weight();
            if (!marked[v]) scan(G, v);               // v becomes part of tree
            if (!marked[w]) scan(G, w);               // w becomes part of tree
        }
    }

    // add all edges e incident to v onto pq if the other endpoint has not yet been scanned
    private void scan(dag_GraphEdgeWeighted G, int v) {
        assert !marked[v];
        marked[v] = true;
        for (dag_GraphEdgeWithWeight e : G.adj(v))
            if (!marked[e.other(v)]) pq.insert(e);
    }
        
    // return edges in MST as an Iterable
    public Iterable<dag_GraphEdgeWithWeight> edges() {
        return mst;
    }

    // return weight of MST
    public double weight() {
        return weight;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(dag_GraphEdgeWeighted G) {

        // check weight
        double weight = 0.0;
        for (dag_GraphEdgeWithWeight e : edges()) {
            weight += e.weight();
        }
        double EPSILON = 1E-12;
        if (Math.abs(weight - weight()) > EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", weight, weight());
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
        

        // print graph
        if (G.V() <= 10) System.out.println(G);

        // compute and print MST
        dag_GraphSpanningTreePrim mst = new dag_GraphSpanningTreePrim(G);
        System.out.println("total cost = " + mst.weight());
        for (dag_GraphEdgeWithWeight e : mst.edges())
            System.out.println(e);
    }

}
