package reuze.pending;

import java.util.concurrent.atomic.AtomicIntegerArray;

import reuze.pending.lc_omp.Work;

public class demoOMPSort {
	static final int N=1<<22;
	static int x[]=new int[N];
	//selection sort
	static void selection(int x[], int low, int high) {
		int i,j,temp;
		for (i=low; i<high; i++) {
			for (j=i+1; j<=high; j++) {
				if (x[i] > x[j]) {
					temp=x[i]; x[i]=x[j]; x[j]=temp;
				}
			}
		}
	}
	/* array A[] has the items to merge sort; array B[] is a work array */
	static void BottomUpSort(int A[])
	{
		int width, n=A.length;
		int B[]=new int[n];
		/* each 1-element run in A is already "sorted". */
		int[] AA=A;
		/* Make successively longer sorted runs of length 2, 4, 8, 16... until whole array is sorted*/
		for (width = 1; width < n; width = 2 * width)
		{
			int i;
			int k=n/width/2;
			/* array A is full of runs of length width */
			for (int m = 0; m < k; m++)
			{ i=m*width*2;
			/* merge two runs: A[i:i+width-1] and A[i+width:i+2*width-1] to B[] */
			/*  or copy A[i:n-1] to B[] ( if(i+width >= n) ) */
			BottomUpMerge(AA, i, Math.min(i+width, n), Math.min(i+2*width, n), B);
			}

			/* now work array B is full of runs of length 2*width */
			/* copy array B to array A for next iteration */
			/*   a more efficient implementation would swap the roles of A and B */
			//System.arraycopy(B, 0, A, 0, n);
			int[] C=AA;  AA=B;  B=C;
			/* now array A is full of runs of length 2*width */
		}
		if (AA!=A) System.arraycopy(AA, 0, A, 0, n);
	}
	//parallel merge sort
	static void bottomUpSort(int A[])
	{
		int width, n=A.length;
		int B[]=new int[n];
		Foo f=new Foo();
		f.A=A;  f.B=B;
		/* each 1-element run in A is already "sorted". */	 
		/* Make successively longer sorted runs of length 2, 4, 8, 16... until whole array is sorted*/
		for (width = 1; width < n; width = 2 * width)
		{
			int k=n/width/2;
			f.width=width;
			/* array A is full of runs of length width */
			lc_omp.work(f, 0, k-1, 1, 1, 0, lc_omp.STATIC, 0);	 
			/* now work array B is full of runs of length 2*width */
			/* copy array B to array A for next iteration */
			/*   a more efficient implementation would swap the roles of A and B */
			//System.arraycopy(B, 0, A, 0, n);
			B=f.A;  f.A=f.B;  f.B=B;
			/* now array A is full of runs of length 2*width */
		}
		if (f.A!=A) System.arraycopy(f.A, 0, A, 0, n);
	}
	static void BottomUpMerge(int A[], int iLeft, int iRight, int iEnd, int B[])
	{
		int i0 = iLeft;
		int i1 = iRight;
		int j;

		/* while there are elements in the left or right lists */
		for (j = iLeft; j < iEnd; j++)
		{
			/* if left list head exists and is <= existing right list head */
			if (i0 < iRight && (i1 >= iEnd || A[i0] <= A[i1]))
			{
				B[j] = A[i0];
				i0 = i0 + 1;
			}
			else
			{
				B[j] = A[i1];
				i1 = i1 + 1;
			}
		}
	}
	static class Foo implements lc_omp.IWork {
		int width;
		int[] A, B;
		public boolean evaluate(Work w) {
			switch (w.task){ 
			case 1:
				int n=A.length;
				int i;
				int width=((Foo)w.where).width;
				for (int m = w.start; m <= w.end; m ++)
				{ i=m*width*2;
				/* merge two runs: A[i:i+width-1] and A[i+width:i+2*width-1] to B[] */
				/*  or copy A[i:n-1] to B[] ( if(i+width >= n) ) */
				BottomUpMerge(A, i, Math.min(i+width, n), Math.min(i+2*width, n), B);
				}
				break;
			case 2:
				for (int m = w.start; m <= w.end; m+=w.step)
					selection(A, m, m+w.step-1);
				break;
			}
			return true;
		}
	}
	static void mergeSort(int x[], int temp[], int low, int high) {
		if (high-low < 100) {
			selection(x, low, high);
			return;
		}
		int middle=(high+low)/2;
		mergeSort(x, temp, low, middle);
		mergeSort(x, temp, middle+1, high);
		int i = low;
		int j = middle+1;
		int k = low;
		while ((i<=middle) && (j<=high)) {
			if (x[i] <= x[j]) {
				temp[k] = x[i];
				i++;
			} else {
				temp[k] = x[j];
				j++;
			}
			k++;
		}
		if (j > high) {
			if (i <= middle) {
				System.arraycopy(x, i, temp, k, middle-i+1);
			}
		} else if (i > middle) {
			if (j <= high) {
				System.arraycopy(x, j, temp, k, high-j+1);
			}
		}
		System.arraycopy(temp, low, x, low, high-low+1);
	}
	//queues threads, not work, so runs out of threads quickly and deadlocks
	/*static final class MyPromise extends lc_Promise { //TODO try with FutureTask
		public MyPromise(Object...x) {
			super(x);
		}
		public final boolean server(Object ...o) {
			int[] x=(int[])o[0];
			int[] temp=(int[])o[1];
			int low=(int)(Integer)o[2];
			int high=(int)(Integer)o[3];
			if (high-low < 100) {
				selection(x, low, high);
				return true;
			}
			int middle=(high+low)/2;
			lc_Promise p1=amergeSort(x, temp, low, middle);
			lc_Promise p2=amergeSort(x, temp, middle+1, high);
			p2.await();  p1.await();
			int i = low;
			int j = middle+1;
			int k = low;
			while ((i<=middle) && (j<=high)) {
				if (x[i] <= x[j]) {
					temp[k] = x[i];
					i++;
				} else {
					temp[k] = x[j];
					j++;
				}
				k++;
			}
			if (j > high) {
				if (i <= middle) {
					System.arraycopy(x, i, temp, k, middle-i+1);
				}
			} else if (i > middle) {
				if (j <= high) {
					System.arraycopy(x, j, temp, k, high-j+1);
				}
			}
			System.arraycopy(temp, low, x, low, high-low+1);
			return true;
		}
	}
	static lc_Promise amergeSort(int x[], int temp[], int low, int high) {
		return new MyPromise(x, temp, low, high);
	}*/
	static int z;
	static void merge(int[] x,int[] temp,int start,int count) {
		int middle=start+count;
		//if (x[middle]>=x[middle-1]) {z++; return;}  //TODO works great if mostly sorted
		int i = start;
		int high=start+count+count-1;
		int j = middle;
		int k = start;
		while ((i<middle) && (j<=high)) {
			if (x[i] <= x[j]) {
				temp[k] = x[i];
				i++;
			} else {
				temp[k] = x[j];
				j++;
			}
			k++;
		}
		if (j > high) {
			if (i < middle) {
				System.arraycopy(x, i, temp, k, middle-i);
			}
		} else if (i >= middle) {
			if (j <= high) {
				System.arraycopy(x, j, temp, k, high-j+1);
			}
		}
		System.arraycopy(temp, start, x, start, high-start+1);
	}
	static class Moo implements lc_omp.IWork {
		AtomicIntegerArray ia;
		int[] A, temp;  int n;
		public Moo(int[] x, int N) {
			A=x; temp=new int[x.length];
			ia=new AtomicIntegerArray((1<<N)-1);  //4 15 nodes
			n=N;   //0 1 2 3 levels
		}
		public boolean evaluate(Work w) {
				for (int m = w.start; m <= w.end; m+=w.step) {
					selection(A, m, m+w.step-1);
					int x=(1<<n);   //16
					int y=w.step+w.step;
					int z;
					do {
						x>>=1;
						z=m/y;
						if (ia.incrementAndGet(x-1+z)==2) {
							merge(A,temp,m/y*y,y/2);
						} else break;
						y+=y;
					} while (x>1);
				}
			return true;
		}
	}
	static void mergingSort(int x[]) {
		Moo f=new Moo(x, 16);
		lc_omp.work(f, 0, x.length-1, 64, 2, 0, lc_omp.STATIC, 0);
		return;
	}
	static boolean validate(int[] x, int start, int count) {
		int end=start+count-1;
		for (int i=start+1; i<=end; i++) 
			if (x[i]<x[i-1]) 
				return false;
		return true;
	}
	public static void main(String args[]) {
		for (int i=0; i<N; i++) x[i]=i;
		for (int i=0; i<N; i++) {
			int j=(int)(Math.random()*N);
			int k=(int)(Math.random()*N);
			int m=x[j];
			x[j]=x[k];   x[k]=m;
		}
		long t=System.nanoTime();
		//selection(x,0,x.length-1);
		//BottomUpSort(x);
		//bottomUpSort(x);
		//int B[]=new int[N]; lc_Promise p=amergeSort(x, B, 0,N-1); p.await();
		//mergingSort(x);       //TODO try starting multiple sorts at once
		System.out.println(N+" "+(System.nanoTime()-t));
		lc_omp.finish();
		for (int i=0; i<N; i++) 
			if (x[i]!=i) {
				System.out.println("err at "+i+" "+x[i]);
				break;
			}
		System.out.println(z);
	}
}
