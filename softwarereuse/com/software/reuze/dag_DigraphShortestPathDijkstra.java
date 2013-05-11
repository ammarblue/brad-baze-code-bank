package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac DijkstraSP.java
 *  Execution:    java DijkstraSP V E
 *  Dependencies: DigraphEdgeWeighted.java IndexMinPQ.java Stack.java DigraphEdge.java
 *
 *  Dijkstra's algorithm. Computes the shortest path tree.
 *  Assumes all weights are nonnegative.
 *
 *************************************************************************/

import java.util.Stack;

import com.software.reuze.da_QueuePriorityMinIndex;
public class dag_DigraphShortestPathDijkstra {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private dag_DigraphEdgeWithWeight[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private da_QueuePriorityMinIndex<Double> pq;    // priority queue of vertices

    public dag_DigraphShortestPathDijkstra(dag_DigraphEdgeWeighted G, int s) {
        distTo = new double[G.V()];
        edgeTo = new dag_DigraphEdgeWithWeight[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new da_QueuePriorityMinIndex<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (dag_DigraphEdgeWithWeight e : G.adj(v))
                relax(e);
        }

        // check optimality conditions
        assert check(G, s);
    }

    // relax edge e and update pq if changed
    private void relax(dag_DigraphEdgeWithWeight e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.change(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    // length of shortest path from s to v
    public double distTo(int v) {
        return distTo[v];
    }

    // is there a path from s to v?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // shortest path from s to v as an Iterable, null if no such path
    public Iterable<dag_DigraphEdgeWithWeight> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<dag_DigraphEdgeWithWeight> path = new Stack<dag_DigraphEdgeWithWeight>();
        for (dag_DigraphEdgeWithWeight e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(dag_DigraphEdgeWeighted G, int s) {

        // check that edge weights are nonnegative
        for (dag_DigraphEdgeWithWeight e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
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
        return true;
    }


    public static void main(String[] args) {
        dag_DigraphEdgeWeighted G;

            // random digraph with V vertices and E edges, parallel edges allowed
        int V;
        int E;
        if (args.length<2) {
        	V=5;  E=9;
        } else {
        	V = Integer.parseInt(args[0]);
        	E  = Integer.parseInt(args[1]);
        }
        G = new dag_DigraphEdgeWeighted(V, E);

        // print graph
        System.out.println("Graph");
        System.out.println("--------------");
        System.out.println(G);


        // run Dijksra's algorithm from vertex 0
        int s = 0;
        dag_DigraphShortestPathDijkstra sp = new dag_DigraphShortestPathDijkstra(G, s);
        System.out.println();


        // print shortest path
        System.out.println("Shortest paths from " + s);
        System.out.println("------------------------");
        for (int v = 0; v < G.V(); v++) {
            if (sp.hasPathTo(v)) {
                System.out.printf("%d to %d (%.2f)  ", s, v, sp.distTo(v));
                for (dag_DigraphEdgeWithWeight e : sp.pathTo(v)) {
                    System.out.print(e + "   ");
                }
                System.out.println();
            }
            else {
                System.out.printf("%d to %d         no path\n", s, v);
            }
        }
    }

}
