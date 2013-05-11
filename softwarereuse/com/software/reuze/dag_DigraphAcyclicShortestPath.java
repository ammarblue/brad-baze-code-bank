package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac AcyclicSP.java
 *  Execution:    java AcyclicSP V E
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge.java Topological.java
 *
 *  Computes shortest paths in an edge-weighted acyclic, connected digraph.
 *
 *  Remark: should probably check that graph is a DAG before running
 *
 *************************************************************************/

import java.util.Stack;
import java.util.Arrays;

import com.software.reuze.f_StdIn;
import com.software.reuze.m_RandomStd;
public class dag_DigraphAcyclicShortestPath {
    private double[] distTo;         // distTo[v] = distance  of shortest s->v path
    private dag_DigraphEdgeWithWeight[] edgeTo;   // edgeTo[v] = last edge on shortest s->v path

    public dag_DigraphAcyclicShortestPath(dag_DigraphEdgeWeighted G, int s) {
        distTo = new double[G.V()];
        edgeTo = new dag_DigraphEdgeWithWeight[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // visit vertices in toplogical order
        dag_DigraphAcyclicSortTopological topological = new dag_DigraphAcyclicSortTopological(G);
        for (int v : topological.order()) {
            for (dag_DigraphEdgeWithWeight e : G.adj(v))
                relax(e);
        }
		System.out.println(Arrays.toString(distTo));
    }

    // relax edge e
    private void relax(dag_DigraphEdgeWithWeight e) {
        int v = e.from(), w = e.to(); System.out.println(v+"-"+w);
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
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // return view of the shortest path from s to v, null if no such path
    public Iterable<dag_DigraphEdgeWithWeight> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<dag_DigraphEdgeWithWeight> path = new Stack<dag_DigraphEdgeWithWeight>(); System.out.print("*"+v);
        for (dag_DigraphEdgeWithWeight e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e); System.out.print(" "+e);
        } System.out.println("*");
        return path;
    }



    public static void main(String[] args) {
    	dag_DigraphEdgeWeighted G;
    	int V, E, s;
    	if (args.length==1) {
    		s = Integer.parseInt(args[0]);
    		V=f_StdIn.readInt();
    		G = new dag_DigraphEdgeWeighted(V);
    		E = f_StdIn.readInt();
    		for (int i = 0; i < E; i++) {
    			int vv = f_StdIn.readInt();
    			int w = f_StdIn.readInt();
    			double d = f_StdIn.readDouble();
    			G.addEdge(new dag_DigraphEdgeWithWeight(vv, w, d)); 
    		}
    	} else {
    		// create random DAG with V vertices and E edges
    		if (args.length==2) {
    			V = Integer.parseInt(args[0]);
    			E = Integer.parseInt(args[1]);
    		} else { V=5; E=9; }
    		G = new dag_DigraphEdgeWeighted(V);

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
            s = vertices[0];
    	}

        // print graph
        System.out.println("Graph");
        System.out.println("--------------");
        System.out.println(G);


        // find shortest path from s to each other vertex in DAG
        dag_DigraphAcyclicShortestPath sp = new dag_DigraphAcyclicShortestPath(G, s);
        System.out.println();

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
