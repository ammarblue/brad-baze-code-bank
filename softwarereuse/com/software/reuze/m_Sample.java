package com.software.reuze;
  /* Floyd's Algorithm CACM 9(Sept. 1987) 754-757. */

final class m_Sample {

  public static boolean tsample(int M, int N, int result[])
  {
    int i, j, T;
    double RN;
    if ((M>N)||(M<=0)||(N<=0)) return false;
    RN = ((double)(N-M+1))/100000.0;
    result[0] = (int)(Math.random()*RN*100000.0)+1; 
    for (j=1; j<=M-1; j++) {
      RN = RN + 0.00001;
      T = (int)(Math.random()*RN*100000.0)+1; 
      i=0;
      for (;;) {
        if (result[i] == T) {T = N-M+j+1; break; };
        i = i+1;
        if (i >= j) break;
      } 
      result[j] = T;
    }
    return true;
  }
public static int[] tsample(int M, int N)
  {
    int i, j, T;
    double RN;
    if ((M>N)||(M<=0)||(N<=0)) return null;
    int result[]=new int[M];
    RN = ((double)(N-M+1))/100000.0;
    result[0] = (int)(Math.random()*RN*100000.0)+1; 
    for (j=1; j<=M-1; j++) {
      RN += 0.00001;
      T = (int)(Math.random()*RN*100000.0)+1; 
      i=0;
      for (;;) {
        if (result[i] == T) {T = N-M+j+1; break; };
        i++;
        if (i >= j) break;
      } 
      result[j] = T;
    }
    return result; 
  } 

  public static int[] permute(int M, int N)
  { if ((M>N)||(M<=0)||(N<=0)) return null;
    int result[]=new int[M];
    int k=0;
    for (int j=N-M+1; j<=N; j++) {
    	int T=(int)(Math.random()*(double)j)+1;
    	for (int m=0; m<k; m++)
    		if (result[m]==T) {result[m]=j; break;}
    	result[k++]=result[0];  result[0]=T;
    	
    }
    return result;
  }
  
  public static boolean permute(int M, int N, int[] result)
  { if ((M>N)||(M<=0)||(N<=0)) return false;
    int k=0;
    for (int j=N-M+1; j<=N; j++) {
    	int T=(int)(Math.random()*(double)j)+1;
    	for (int m=0; m<k; m++)
    		if (result[m]==T) {result[m]=j; break;}
    	result[k++]=result[0];  result[0]=T;    	
    }
    return true;
  }

  public static void main(String args[]) {
    int x[]=new int[5];
    int i;
    tsample(5,20,x);
    for (i=0;i<x.length;i++)System.out.print(x[i]+" ");
    System.out.println(" "); tsample(5,20,x);
    for (i=0;i<x.length;i++)System.out.print(x[i]+" ");
    System.out.println(" "); tsample(5,20,x);
    for (i=0;i<x.length;i++)System.out.print(x[i]+" ");
    System.out.println(" "); tsample(5,20,x);
    for (i=0;i<x.length;i++)System.out.print(x[i]+" ");
    System.out.println(" ");
    for (int k=0; k<20; k++) {
    x=tsample(5,10); for (i=0;i<x.length;i++) System.out.print(x[i]+" "); System.out.println(); }
  }
}
