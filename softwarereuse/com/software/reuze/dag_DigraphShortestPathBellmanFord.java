package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac BellmanFordSP.java
 *  Execution:    java BellmanFordSP V E
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge.java Queue.java
 *                EdgeWeightedDirectedCycle.java
 *
 *  Bellman-Ford shortest path algorithm. Computes the shortest path tree in
 *  edge-weighted digraph G from vertex s, or finds a negative
 *  cost cycle reachable from s.
 * 
 *  % java BellmanFordSP 100 500
 *
 *************************************************************************/

import java.util.Stack;

import com.software.reuze.d_Queue;
public class dag_DigraphShortestPathBellmanFord {
    private double[] distTo;               // distTo[v] = distance  of shortest s->v path
    private dag_DigraphEdgeWithWeight[] edgeTo;         // edgeTo[v] = last edge on shortest s->v path
    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
    private d_Queue<Integer> queue;          // queue of vertices to relax
    private int cost;                      // number of calls to relax()
    private Iterable<dag_DigraphEdgeWithWeight> cycle;  // negative cycle (or null if no such cycle)

    public dag_DigraphShortestPathBellmanFord(dag_DigraphEdgeWeighted G, int s) {
        distTo  = new double[G.V()];
        edgeTo  = new dag_DigraphEdgeWithWeight[G.V()];
        onQueue = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // Bellman-Ford algorithm
        queue = new d_Queue<Integer>();
        queue.enqueue(s);
        onQueue[s] = true;
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.dequeue();
            onQueue[v] = false;
            relax(G, v);
        }

        assert check(G, s);
    }

    // relax vertex v and put other endpoints on queue if changed
    private void relax(dag_DigraphEdgeWeighted G, int v) {
        for (dag_DigraphEdgeWithWeight e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (!onQueue[w]) {
                    queue.enqueue(w);
                    onQueue[w] = true;
                }
            }
            if (cost++ % G.V() == 0)
                findNegativeCycle();
        }
    }


    // is there a negative cycle reachable from s?
    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    // return a negative cycle; null if no such cycle
    public Iterable<dag_DigraphEdgeWithWeight> negativeCycle() {
        return cycle;
    }

    // by finding a cycle in predecessor graph
    private void findNegativeCycle() {
        int V = edgeTo.length;
        dag_DigraphEdgeWeighted spt = new dag_DigraphEdgeWeighted(V);
        for (int v = 0; v < V; v++)
            if (edgeTo[v] != null)
                spt.addEdge(edgeTo[v]);

        dag_DigraphEdgeWeightedCycle finder = new dag_DigraphEdgeWeightedCycle(spt);
        cycle = finder.cycle();
    }

    // is there a path from s to v?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }


    // return length of shortest path from s to v
    public double distTo(int v) {
        return distTo[v];
    }

    // return view of shortest path from s to v, null if no such path
    public Iterable<dag_DigraphEdgeWithWeight> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<dag_DigraphEdgeWithWeight> path = new Stack<dag_DigraphEdgeWithWeight>();
        for (dag_DigraphEdgeWithWeight e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    // check optimality conditions: either 
    // (i) there exists a negative cycle reacheable from s
    //     or 
    // (ii)  for all edges e = v->w:            distTo[w] <= distTo[v] + e.weight()
    // (ii') for all edges e = v->w on the SPT: distTo[w] == distTo[v] + e.weight()
    private boolean check(dag_DigraphEdgeWeighted G, int s) {

        // has a negative cycle
        if (hasNegativeCycle()) {
            double weight = 0.0;
            for (dag_DigraphEdgeWithWeight e : negativeCycle()) {
                weight += e.weight();
            }
            if (weight >= 0.0) {
                System.err.println("error: weight of negative cycle = " + weight);
                return false;
            }
        }

        // no negative cycle reachable from source
        else {

            // check that distTo[v] and edgeTo[v] are consistent
            if (distTo[s] != 0.0 || edgeTo[s] != null) {
                System.err.println("distanceTo[s] and edgeTo[s] inconsistent");
                return false;
            }
            for (int v = 0; v < G.V(); v++) {
                if (v == s) continue;
                if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                    System.err.println("distTo[] and edgeTo[] inconsistent");
                    return false;
                }
            }

            // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
            for (int v = 0; v < G.V(); v++) {
                for (dag_DigraphEdgeWithWeight e : G.adj(v)) {
                    int w = e.to();
                    if (distTo[v] + e.weight() < distTo[w]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
            }

            // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
            for (int w = 0; w < G.V(); w++) {
                if (edgeTo[w] == null) continue;
                dag_DigraphEdgeWithWeight e = edgeTo[w];
                int v = e.from();
                if (w != e.to()) return false;
                if (distTo[v] + e.weight() != distTo[w]) {
                    System.err.println("edge " + e + " on shortest path not tight");
                    return false;
                }
            }
        }

        System.out.println("Satisfies optimality conditions");
        System.out.println();
        return true;
    }



    public static void main(String[] args) {

        // random graph with V vertices and E edges, parallel edges allowed
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        dag_DigraphEdgeWeighted G = new dag_DigraphEdgeWeighted(V);
        for (int i = 0; i < E; i++) {
            int v = (int) (V * Math.random());
            int w = (int) (V * Math.random());
            double weight = Math.round(100 * (Math.random() - 0.15)) / 100.0;
            if (v == w) G.addEdge(new dag_DigraphEdgeWithWeight(v, w, Math.abs(weight)));
            else        G.addEdge(new dag_DigraphEdgeWithWeight(v, w, weight));
        }

        System.out.println(G);

        // run Bellman-Ford algorithm from vertex 0
        int s = 0;
        dag_DigraphShortestPathBellmanFord sp = new dag_DigraphShortestPathBellmanFord(G, s);

        // print negative cycle
        if (sp.hasNegativeCycle()) {
            System.out.println("Negative cycle:");
            for (dag_DigraphEdgeWithWeight e : sp.negativeCycle())
                System.out.println(e);
        }

        // print shortest paths
        else {
            System.out.println("Shortest paths from " + s);
            System.out.println("------------------------");
            for (int v = 0; v < G.V(); v++) {
                if (sp.hasPathTo(v)) {
                    System.out.printf("%d to %d (%5.2f)  ", s, v, sp.distTo(v));
                    for (dag_DigraphEdgeWithWeight e : sp.pathTo(v)) {
                        System.out.print(e + "   ");
                    }
                    System.out.println();
                }
                else {
                    System.out.printf("%d to %d           no path\n", s, v);
                }
            }
        }

    }

}
