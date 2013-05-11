package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac EdgeWeightedDirectedCycle.java
 *  Execution:    java EdgeWeightedDirectedCycle V E F
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge Stack.java
 *
 *  Finds a directed cycle in an edge-weighted digraph.
 *  Runs in O(E + V) time.
 *
 *
 *************************************************************************/

import java.util.Stack;

import com.software.reuze.m_RandomStd;
public class dag_DigraphEdgeWeightedCycle {
    private boolean[] marked;             // marked[v] = has vertex v been marked?
    private dag_DigraphEdgeWithWeight[] edgeTo;        // edgeTo[v] = previous edge on path to v
    private boolean[] onStack;            // onStack[v] = is vertex on the stack?
    private Stack<dag_DigraphEdgeWithWeight> cycle;    // directed cycle (or null if no such cycle)

    public dag_DigraphEdgeWeightedCycle(dag_DigraphEdgeWeighted G) {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new dag_DigraphEdgeWithWeight[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);

        // check that digraph has a cycle
        assert check(G);
    }


    // check that algorithm computes either the topological order or finds a directed cycle
    private void dfs(dag_DigraphEdgeWeighted G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (dag_DigraphEdgeWithWeight e : G.adj(v)) {
            int w = e.to();

            // short circuit if directed cycle found
            if (cycle != null) return;

            //found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = e;
                dfs(G, w);
            }


            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new Stack<dag_DigraphEdgeWithWeight>();
                while (e.from() != w) {
                    cycle.push(e);
                    e = edgeTo[e.from()];
                }
                cycle.push(e);
            }
        }

        onStack[v] = false;
    }

    public boolean hasCycle()             { return cycle != null;   }
    public Iterable<dag_DigraphEdgeWithWeight> cycle() { return cycle;           }


    // certify that digraph is either acyclic or has a directed cycle
    private boolean check(dag_DigraphEdgeWeighted G) {

        // edge-weighted digraph is cyclic
        if (hasCycle()) {
            // verify cycle
        	dag_DigraphEdgeWithWeight first = null, last = null;
            for (dag_DigraphEdgeWithWeight e : cycle()) {
                if (first == null) first = e;
                if (last != null) {
                    if (last.to() != e.from()) {
                        System.err.printf("cycle edges %s and %s not incident\n", last, e);
                        return false;
                    }
                }
                last = e;
            }

            if (last.to() != first.from()) {
                System.err.printf("cycle edges %s and %s not incident\n", last, first);
                return false;
            }
        }


        return true;
    }

    public static void main(String[] args) {

        // create random DAG with V vertices and E edges; then add F random edges
    	int V;
        int E;
        int F;
        if (args.length<2) {
        	V=5;  E=9;  F=3;
        } else {
        	V = Integer.parseInt(args[0]);
        	E  = Integer.parseInt(args[1]);
        	F = Integer.parseInt(args[2]);
        } 
        dag_DigraphEdgeWeighted G = new dag_DigraphEdgeWeighted(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        m_RandomStd.shuffle(vertices);
        for (int i = 0; i < E; i++) {
            int v, w;
            do {
                v = m_RandomStd.uniform(V);
                w = m_RandomStd.uniform(V);
            } while (v >= w);
            double weight = Math.random();
            G.addEdge(new dag_DigraphEdgeWithWeight(v, w, weight));
        }

        // add F extra edges
        for (int i = 0; i < F; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.random();
            G.addEdge(new dag_DigraphEdgeWithWeight(v, w, weight));
        }

        System.out.println(G);

        // find a directed cycle
        dag_DigraphEdgeWeightedCycle finder = new dag_DigraphEdgeWeightedCycle(G);
        if (finder.hasCycle()) {
            System.out.print("Cycle: ");
            for (dag_DigraphEdgeWithWeight e : finder.cycle()) {
                System.out.print(e + " ");
            }
            System.out.println();
        }

        // or give topologial sort
        else {
            System.out.println("No directed cycle");
        }
    }

}
