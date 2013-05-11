package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac DigraphPath.java
 *  Execution:    java DigraphPath V E
 *  Dependencies: DigraphEdgeWeighted.java DirectedEdge.java Topological.java
 *
 *  Computes shortest paths in an edge-weighted acyclic digraph.
 *
 *  Remark: should probably check that graph is a DAG before running
 *
 *************************************************************************/

import java.util.Stack;

import com.software.reuze.m_RandomStd;
public class dag_DigraphShortestPath {
    public double[] distTo;         // distTo[v] = distance  of shortest s->v path
    public dag_DigraphEdgeWithWeight[] edgeTo;   // edgeTo[v] = last edge on shortest s->v path
	public final boolean maximum;

    public dag_DigraphShortestPath(dag_DigraphEdgeWeighted G, int s, boolean maximum) {
        distTo = new double[G.V()];
        edgeTo = new dag_DigraphEdgeWithWeight[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = maximum?Double.NEGATIVE_INFINITY:Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;
		this.maximum=maximum;
        // visit vertices in toplogical order
        dag_DigraphAcyclicSortTopological topological = new dag_DigraphAcyclicSortTopological(G);
        for (int v : topological.order()) {
            for (dag_DigraphEdgeWithWeight e : G.adj(v))
                relax(e);
        }
    }

    // relax edge e
    private void relax(dag_DigraphEdgeWithWeight e) {
        int v = e.from(), w = e.to();
        if (maximum && distTo[w] < distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
			return;
        }
		if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        } 
    }

    // return length of the shortest path from s to v, infinity if no such path
    public double distTo(int v) {
        return distTo[v];
    }

    // is there a path from s to v?
    public boolean hasPathTo(int v) {
        return maximum?(distTo[v] > Double.NEGATIVE_INFINITY):(distTo[v] < Double.POSITIVE_INFINITY);
    }

    // return view of the shortest path from s to v, null if no such path
    public Iterable<dag_DigraphEdgeWithWeight> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<dag_DigraphEdgeWithWeight> path = new Stack<dag_DigraphEdgeWithWeight>();
        for (dag_DigraphEdgeWithWeight e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }



    public static void main(String[] args) {

        // create random DAG with V vertices and E edges
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
        

        // print graph
        System.out.println("Graph");
        System.out.println("--------------");
        System.out.println(G);


        // find shortest path from s to each other vertex in DAG
        int s = vertices[0];
        dag_DigraphShortestPath sp = new dag_DigraphShortestPath(G, s, true);
        System.out.println();

        System.out.println((sp.maximum?"Long":"Short")+"est paths from " + s);
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
