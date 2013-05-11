package com.software.reuze;
import java.lang.Math.*;
import java.util.Stack;
public class dag_GraphHamiltonCircuit {
/*all bits on mean each vertex visited at least once*/
private static boolean bitOn(int x[], int i) {
  int j=i/32; i=1<<(i%32);
  if ((x[j]&i) != 0) return false;
  x[j] |=i;
  return true;
}
private static void bitOff(int x[], int i) {
  int j=i/32; i=1<<(i%32);
  assert (x[j]&i) != 0 :  "bitOff:bit not on";
  x[j] ^=i;
}

static  dag_GraphEdgeWeighted G;
static  int bits[]=new int[100];
static  Stack<Integer> stk=new Stack<Integer>();
static double sum=0;
/*v vertex to visit; nv decremented at each step*/
private static void TSP(int v, int nv) {
  stk.push(v);
  nv--;
  if (!bitOn(bits, v)) { //already visited; 2 stops not allowed
    if (nv<=0 && v==0) System.out.println(stk+" "+sum);
    stk.pop();
    return;
  }
  if (nv>0) //only V steps are allowed
  for (dag_GraphEdgeWithWeight e:G.adj(v)) {
    int i=e.either();
    if (i==v) i=e.other(i);
    if (i==v) continue;
    sum+=e.weight();
    TSP(i, nv);
    sum-=e.weight();
  }
  bitOff(bits, v); //undo effect of this visit
  stk.pop();
}
    public static void main(String[] args) {

            // random graph with V vertices and E edges, parallel edges allowed
            int V;
            int E;
            if (args.length<2) {
            	V=5;  E=V*3/2;
            } else {
            	V = Integer.parseInt(args[0]);
            	E  = Math.max(Integer.parseInt(args[1]), V*3/2);
            }
            G = new dag_GraphEdgeWeighted(V);
            for (int i=0; i<V; i++) { //connect each v to next v+1
              double weight=Math.round(100*Math.random())/100.0;
              dag_GraphEdgeWithWeight e= new dag_GraphEdgeWithWeight(i, (i+1)%V, weight);
              G.addEdge(e);
            }
            for (int i=0; i<E-V; i++) { //add a few more random edges
              double weight=Math.round(100*Math.random())/100.0;
              int j=(int)(1000*Math.random())%V, k;
              do {
                k=(int)(1000*Math.random())%V;
              } while ((j==k) || (Math.abs(j-k)==1));
              dag_GraphEdgeWithWeight e= new dag_GraphEdgeWithWeight(j, k, weight);
              G.addEdge(e);
            }

        System.out.println(G);
        TSP(0, V+1);
    }
}
