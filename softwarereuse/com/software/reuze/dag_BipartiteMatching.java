package com.software.reuze;
import com.software.reuze.dag_DigraphEdgeWithFlow;
import com.software.reuze.dag_DigraphFlowNetwork;
import com.software.reuze.dag_DigraphFlowNetworkFordFulkerson;
import com.software.reuze.m_RandomStd;

/*************************************************************************
 *  Compilation:  javac BipartiteMatching.java
 *  Execution:    java BipartiteMatching V E
 *  Dependencies: FordFulkerson.java FlowNetwork.java FlowEdge.java 
 *
 *  Find a maximum matching in a bipartite graph. Solve by reducing
 *  to maximum flow.
 *
 *********************************************************************/

public class dag_BipartiteMatching {

    public static void main(String[] args) {

        // read in bipartite network with 2N vertices and E edges
        // we assume the vertices on one side of the bipartition
        // are named 0 to N-1 and on the other side are N to 2N-1.
        int N = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int s = 2*N, t = 2*N + 1;
        dag_DigraphFlowNetwork G = new dag_DigraphFlowNetwork(2*N + 2);
        for (int i = 0; i < E; i++) {
            int v = m_RandomStd.uniform(N);
            int w = m_RandomStd.uniform(N) + N;
            G.addEdge(new dag_DigraphEdgeWithFlow(v, w, Double.POSITIVE_INFINITY));
            System.out.println(v + "-" + w);
        }
        for (int i = 0; i < N; i++) {
            G.addEdge(new dag_DigraphEdgeWithFlow(s,     i, 1.0));
            G.addEdge(new dag_DigraphEdgeWithFlow(i + N, t, 1.0));
        }


        // compute maximum flow and minimum cut
        dag_DigraphFlowNetworkFordFulkerson maxflow = new dag_DigraphFlowNetworkFordFulkerson(G, s, t);
        System.out.println();
        System.out.println("Size of maximum matching = " + (int) maxflow.value());
        for (int v = 0; v < N; v++) {
            for (dag_DigraphEdgeWithFlow e : G.adj(v)) {
                if (e.from() == v && e.flow() > 0)
                    System.out.println(e.from() + "-" + e.to());
            }
        }
    }

}

