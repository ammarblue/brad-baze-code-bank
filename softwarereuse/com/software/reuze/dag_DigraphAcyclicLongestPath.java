package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac AcyclicLP.java
 *  Execution:    java AcyclicP V E
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge.java Topological.java
 *
 *  Computes longest paths in an edge-weighted acyclic digraph.
 *
 *  Remark: should probably check that graph is a DAG before running
 *
 *************************************************************************/

import java.util.Stack;

import com.software.reuze.m_RandomStd;
public class dag_DigraphAcyclicLongestPath {
    private double[] distTo;          // distTo[v] = distance  of longest s->v path
    private dag_DigraphEdgeWithWeight[] edgeTo;    // edgeTo[v] = last edge on longest s->v path

    public dag_DigraphAcyclicLongestPath(dag_DigraphEdgeWeighted G, int s) {
        distTo = new double[G.V()];
        edgeTo = new dag_DigraphEdgeWithWeight[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = Double.NEGATIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in toplogical order
        dag_DigraphAcyclicSortTopological topological = new dag_DigraphAcyclicSortTopological(G);
        for (int v : topological.order()) {
            for (dag_DigraphEdgeWithWeight e : G.adj(v))
                relax(e);
        }
    }

    // relax edge e, but update if you find a *longer* path
    private void relax(dag_DigraphEdgeWithWeight e) {
        int v = e.from(), w = e.to();
        if (distTo[w] < distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }       
    }

    // return length of the longest path from s to v, -infinity if no such path
    public double distTo(int v) {
        return distTo[v];
    }

    //  is there a  path from s to v?
    public boolean hasPathTo(int v) {
        return distTo[v] > Double.NEGATIVE_INFINITY;
    }

    // return view of longest path from s to v, null if no such path
    public Iterable<dag_DigraphEdgeWithWeight> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<dag_DigraphEdgeWithWeight> path = new Stack<dag_DigraphEdgeWithWeight>();
        for (dag_DigraphEdgeWithWeight e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }



    public static void main(String[] args) {

        // create random DG with V vertices and E edges
    	int V;
        int E;
        if (args.length<2) {
        	V=5;  E=9;
        } else {
        	V = Integer.parseInt(args[0]);
        	E  = Integer.parseInt(args[1]);
        }
        dag_DigraphEdgeWeighted G = new dag_DigraphEdgeWeighted(V);

        // create random permutation
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) {
            vertices[i] = i;
        }
        m_RandomStd.shuffle(vertices);

        // add E random edges that respect topological order
        while (G.E() < E) {   
            int v, w;
            do {
                v = m_RandomStd.uniform(V);
                w = m_RandomStd.uniform(V);
            } while (v >= w);
            double weight = (int) (100 * Math.random()) / 100.0;
            G.addEdge(new dag_DigraphEdgeWithWeight(vertices[v], vertices[w], weight));
        }
        

        System.out.println(G);


        // find longest path from 0 to each other vertex in DAG
        int s = vertices[0];
        dag_DigraphAcyclicLongestPath lp = new dag_DigraphAcyclicLongestPath(G, s);
        System.out.println();


        System.out.println("Longest paths from " + s);
        System.out.println("------------------------");
        for (int v = 0; v < G.V(); v++) {
            if (lp.hasPathTo(v)) {
                System.out.printf("%d to %d (%.2f)  ", s, v, lp.distTo(v));
                for (dag_DigraphEdgeWithWeight e : lp.pathTo(v)) {
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
