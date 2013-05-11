package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac DepthFirstOrder.java
 *  Execution:    java DepthFirstOrder V E F
 *  Dependencies: Digraph.java EdgeWeightedDigraph.java DirectedEdge.java
 *                Queue.java Stack.java StdOut.java
 *
 *  Compute preorder and postorder for a digraph or edge-weighted digraph.
 *  Runs in O(E + V) time.
 *
 *
 *************************************************************************/

import java.util.Stack;

import com.software.reuze.d_Queue;
import com.software.reuze.m_RandomStd;
public class dag_DigraphVisitPreAndPostOrder {
    private boolean[] marked;          // marked[v] = has v been marked in dfs?
    private int[] pre;                 // pre[v]    = preorder  number of v
    private int[] post;                // post[v]   = postorder number of v
    private d_Queue<Integer> preorder;   // vertices in preorder
    private d_Queue<Integer> postorder;  // vertices in postorder
    private int preCounter;            // counter or preorder numbering
    private int postCounter;           // counter for postorder numbering

    // depth-first search preorder and postorder in a digraph
    public dag_DigraphVisitPreAndPostOrder(dag_Digraph G) {
        pre    = new int[G.V()];
        post   = new int[G.V()];
        postorder = new d_Queue<Integer>();
        preorder  = new d_Queue<Integer>();
        marked    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    // depth-first search preorder and postorder in an edge-weighted digraph
    public dag_DigraphVisitPreAndPostOrder(dag_DigraphEdgeWeighted G) {
        pre    = new int[G.V()];
        post   = new int[G.V()];
        postorder = new d_Queue<Integer>();
        preorder  = new d_Queue<Integer>();
        marked    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    // run DFS in digraph G from vertex v and compute preorder/postorder
    private void dfs(dag_Digraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preorder.enqueue(v);
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        postorder.enqueue(v);
        post[v] = postCounter++;
    }

    // run DFS in edge-weighted digraph G from vertex v and compute preorder/postorder
    private void dfs(dag_DigraphEdgeWeighted G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preorder.enqueue(v);
        for (dag_DigraphEdgeWithWeight e : G.adj(v)) {
            int w = e.to();
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        postorder.enqueue(v);
        post[v] = postCounter++;
    }

    public int pre(int v) {
        return pre[v];
    }

    public int post(int v) {
        return post[v];
    }

    // return vertices in postorder as an Iterable
    public Iterable<Integer> postorder() {
        return postorder;
    }

    // return vertices in preorder as an Iterable
    public Iterable<Integer> preorder() {
        return preorder;
    }

    // return vertices in reverse postorder as an Iterable
    public Iterable<Integer> reversePostorder() {
        Stack<Integer> reverse = new Stack<Integer>();
        for (int v : postorder)
            reverse.push(v);
        return reverse;
    }


    // certify that digraph is either acyclic or has a directed cycle
    private boolean check(dag_Digraph G) {

        // check that postorder() is consistent with rank()
        int r = 0;
        for (int v : postorder()) {
            if (post(v) != r) {
                System.err.println("postorder() and post() inconsistent");
                return false;
            }
            r++;
        }

        // check that preorder() is consistent with pre()
        r = 0;
        for (int v : preorder()) {
            if (pre(v) != r) {
                System.err.println("preorder() and pre() inconsistent");
                return false;
            }
            r++;
        }


        return true;
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
        dag_Digraph G = new dag_Digraph(V);
        for (int i = 0; i < E; i++) {
            int v = m_RandomStd.uniform(V);
            int w = m_RandomStd.uniform(V);
            G.addEdge(v, w);
        }

        System.out.println(G);

        dag_DigraphVisitPreAndPostOrder dfs = new dag_DigraphVisitPreAndPostOrder(G);
        System.out.println("   v  pre post");
        System.out.println("--------------");
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%4d %4d %4d\n", v, dfs.pre(v), dfs.post(v));
        }

        System.out.print("Postorder: ");
        for (int v : dfs.postorder()) {
            System.out.print(v + " ");
        }
        System.out.println();

        System.out.print("Preorder:  ");
        for (int v : dfs.preorder()) {
            System.out.print(v + " ");
        }
        System.out.println();


    }

}
