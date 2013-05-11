package com.software.reuze;

/*************************************************************************
 *  Compilation:  javac CPM.java
 *  Execution:    java CPM < input.txt
 *  Dependencies: EdgeWeightedDigraph.java AcyclicDigraphLP.java
 *
 *  CPM.
 *
 *  % java CPM < jobs10.txt
 *
 *************************************************************************/

public class m_JobShopCriticalPath {

    public static void main(String[] args) {

        // number of jobs
        int N = f_StdIn.readInt();

        // source and sink
        int source = 2*N;
        int sink   = 2*N + 1;

        // build network
        dag_DigraphEdgeWeighted G = new dag_DigraphEdgeWeighted(2*N + 2);
        for (int i = 0; i < N; i++) {
            double duration = f_StdIn.readDouble();
            G.addEdge(new dag_DigraphEdgeWithWeight(source, i, 0.0));
            G.addEdge(new dag_DigraphEdgeWithWeight(i+N, sink, 0.0));
            G.addEdge(new dag_DigraphEdgeWithWeight(i, i+N,    duration));

            // precedence constraints
            int M = f_StdIn.readInt();
            for (int j = 0; j < M; j++) {
                int precedent = f_StdIn.readInt();
                G.addEdge(new dag_DigraphEdgeWithWeight(N+i, precedent, 0.0));
            }
        }

        // compute longest path
        dag_DigraphAcyclicLongestPath lp = new dag_DigraphAcyclicLongestPath(G, source);

        // print results
        System.out.println(" job   start  finish");
        System.out.println("--------------------");
        for (int i = 0; i < N; i++) {
            System.out.printf("%4d %7.1f %7.1f\n", i, lp.distTo(i), lp.distTo(i+N));
        }
        System.out.printf("Finish time: %7.1f\n", lp.distTo(sink));
    }

}
