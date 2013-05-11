package reuze.pending;

import reuze.pending.lc_omp.Work;

public class demoOMPDijkstra {
	static final int NV=6;
	static final int i4_huge = 2147483647;
	static int[] connected, mind;
	public static void main(String args[]) {
		/******************************************************************************/
		/*
		  Purpose:

		    MAIN runs an example of Dijkstra's minimum distance algorithm.

		  Discussion:

		    Given the distance matrix that defines a graph, we seek a list
		    of the minimum distances between node 0 and all other nodes.

		    This program sets up a small example problem and solves it.

		    The correct minimum distances are:

		      0   35   15   45   49   41

		  Licensing:

		    This code is distributed under the GNU LGPL license. 

		  Modified:

		    01 July 2010

		  Author:

		    Original C version by Norm Matloff, CS Dept, UC Davis.
		    This C version by John Burkardt.
		 */
		int i;
		int i4_huge = 2147483647;
		int j;
		int ohd[][];
		ohd=new int[NV][];
		for (i=0; i<NV; i++) ohd[i]=new int[NV];
		System.out.printf ("\n" );
		System.out.printf ("DIJKSTRA_OPENMP\n" );
		System.out.printf ("  C version\n" );
		System.out.printf ("  Use Dijkstra's algorithm to determine the minimum\n" );
		System.out.printf ("  distance from node 0 to each node in a graph,\n" );
		System.out.printf ("  given the distances between each pair of nodes.\n" );
		System.out.printf ("\n" );
		System.out.printf ("  Although a very small example is considered, we\n" );
		System.out.printf ("  demonstrate the use of OpenMP directives for\n" );
		System.out.printf ("  parallel execution.\n" );
		/*
		  Initialize the problem data.
		 */
		init ( ohd );
		/*
		  Print the distance matrix.
		 */
		System.out.printf ("\n" );
		System.out.printf ("  Distance matrix:\n" );
		System.out.printf ("\n" );
		for ( i = 0; i < NV; i++ )
		{
			for ( j = 0; j < NV; j++ )
			{
				if ( ohd[i][j] == i4_huge )
				{
					System.out.printf ("  Inf" );
				}
				else
				{
					System.out.printf ("  %3d", ohd[i][j] );
				}
			}
			System.out.printf ("\n" );
		}
		/*
		  Carry out the algorithm.
		 */
		mind = dijkstra_distance2 ( ohd );  
		/*
		  Print the results.
		 */
		System.out.printf ("\n" );
		System.out.printf ("  Minimum distances from node 0:\n");
		System.out.printf ("\n" );
		for ( i = 0; i < NV; i++ )
		{
			System.out.printf ("  %2d  %2d\n", i, mind[i] );
		}
		/*
		  Terminate.
		 */
		System.out.printf ("\n" );
		System.out.printf ("DIJKSTRA_OPENMP\n" );
		System.out.printf ("  Normal end of execution.\n" );
		System.out.printf ("\n" );
	}

	/******************************************************************************/

	static void init ( int ohd[][] )

	/******************************************************************************/
	/*
	  Purpose:

	    INIT initializes the problem data.

	  Discussion:

	    The graph uses 6 nodes, and has the following diagram and
	    distance matrix:

	    N0--15--N2-100--N3           0   40   15  Inf  Inf  Inf
	      \      |     /            40    0   20   10   25    6
	       \     |    /             15   20    0  100  Inf  Inf
	        40  20  10             Inf   10  100    0  Inf  Inf
	          \  |  /              Inf   25  Inf  Inf    0    8
	           \ | /               Inf    6  Inf  Inf    8    0
	            N1
	            / \
	           /   \
	          6    25
	         /       \
	        /         \
	      N5----8-----N4

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    02 July 2010

	  Author:

	    Original C version by Norm Matloff, CS Dept, UC Davis.
	    This C version by John Burkardt.

	  Parameters:

	    Output, int OHD[NV][NV], the distance of the direct link between
	    nodes I and J.
	 */
	{
		int i;
		int i4_huge = 2147483647;
		int j;

		for ( i = 0; i < NV; i++ )  
		{
			for ( j = 0; j < NV; j++ )
			{
				if ( i == j ) 
				{
					ohd[i][i] = 0;
				}
				else
				{
					ohd[i][j] = i4_huge;
				}
			}
		}
		ohd[0][1] = ohd[1][0] = 40;
		ohd[0][2] = ohd[2][0] = 15;
		ohd[1][2] = ohd[2][1] = 20;
		ohd[1][3] = ohd[3][1] = 10;
		ohd[1][4] = ohd[4][1] = 25;
		ohd[2][3] = ohd[3][2] = 100;
		ohd[1][5] = ohd[5][1] = 6;
		ohd[4][5] = ohd[5][4] = 8;

		return;
	}
	static int[] dijkstra_distance2 ( int ohd[][]  ) {
		int i;
		int my_step;
		/*
	  Start out with only node 0 connected to the tree.
		 */
		connected = new int[NV];

		connected[0] = 1;
		for ( i = 1; i < NV; i++ )
		{
			connected[i] = 0;
		}
		/*
	  Initial estimate of minimum distance is the 1-step distance.
		 */
		mind = new int[NV];
		for ( i = 0; i < NV; i++ )
		{
			mind[i] = ohd[0][i];
		}
		Foo w=new Foo();
		w.ohd=ohd;
		for ( my_step = 1; my_step < NV; my_step++ ) {
			w.init();
			lc_omp.work(w, 1);
			int mv=w.getMV();
			if ( mv == - 1 ) {System.out.println("stop early at "+my_step); break;}
			connected[mv] = 1;
			System.out.printf ( " Connecting node %d.\n", mv );
			lc_omp.work(w, 2);
		}
		return mind;
	}
	static class Foo implements lc_omp.IWork {
		int md = i4_huge;
		int mv = -1;
		int ohd[][];
		public void init() {
			md = i4_huge;
			mv = -1;
		}
		public int getMV() {return mv;}
		public boolean evaluate(Work w) {
			int nth=lc_omp.omp_get_num_threads();
			int my_first =   (   lc_omp.omp_get_thread_num() * NV ) / nth;
			int my_last  =   ( ( lc_omp.omp_get_thread_num() + 1 ) * NV ) / nth - 1;
			switch (w.task){ 
			case 1:
				int index=find_nearest ( my_first, my_last, mind, connected);
				if (index<0) break;
				synchronized(lc_omp.critical()) {
					if (mind[index] < md) {
						md=mind[index];
						mv=index;
					}
				}
				break;
			case 2:
				update_mind ( my_first, my_last, mv, connected, ohd, mind );
				break;
			}
			return true;
		}
	}
	static int[] dijkstra_distance ( int ohd[][]  )

	/******************************************************************************/
	/*
	  Purpose:

	    DIJKSTRA_DISTANCE uses Dijkstra's minimum distance algorithm.

	  Discussion:

	    We essentially build a tree.  We start with only node 0 connected
	    to the tree, and this is indicated by setting CONNECTED[0] = 1.

	    We initialize MIND[I] to the one step distance from node 0 to node I.

	    Now we search among the unconnected nodes for the node MV whose minimum
	    distance is smallest, and connect it to the tree.  For each remaining
	    unconnected node I, we check to see whether the distance from 0 to MV
	    to I is less than that recorded in MIND[I], and if so, we can reduce
	    the distance.

	    After NV-1 steps, we have connected all the nodes to 0, and computed
	    the correct minimum distances.

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    02 July 2010

	  Author:

	    Original C version by Norm Matloff, CS Dept, UC Davis.
	    This C version by John Burkardt.

	  Parameters:

	    Input, int OHD[NV][NV], the distance of the direct link between
	    nodes I and J.

	    Output, int DIJKSTRA_DISTANCE[NV], the minimum distance from 
	    node 0 to each node.
	 */
	{
		int i;
		int i4_huge = 2147483647;
		int md;
		int mv;
		int my_step;
		int nth;
		/*
	  Start out with only node 0 connected to the tree.
		 */
		connected = new int[NV];

		connected[0] = 1;
		for ( i = 1; i < NV; i++ )
		{
			connected[i] = 0;
		}
		/*
	  Initial estimate of minimum distance is the 1-step distance.
		 */
		mind = new int[NV];
		for ( i = 0; i < NV; i++ )
		{
			mind[i] = ohd[0][i];
		}

		nth=1;   //no parallelism
		for ( my_step = 1; my_step < NV; my_step++ ) {
			md = i4_huge;
			mv = -1;
			for (int my_id=0; my_id<nth; my_id++) {
				int my_first =   (   my_id       * NV ) / nth;
				int my_last  =   ( ( my_id + 1 ) * NV ) / nth - 1;
				int index=find_nearest ( my_first, my_last, mind, connected);
				if (index>=0 && mind[index] < md) {
					md=mind[index];
					mv=index;
				}
			}
			if ( mv == - 1 ) {System.out.println("stop early at "+my_step); break;}
			connected[mv] = 1;
			System.out.printf ( " Connecting node %d.\n", mv );
			for (int my_id=0; my_id<nth; my_id++) {
				int my_first =   (   my_id       * NV ) / nth;
				int my_last  =   ( ( my_id + 1 ) * NV ) / nth - 1;
				update_mind ( my_first, my_last, mv, connected, ohd, mind );
			}
		}
		return mind;
	}

	static int find_nearest ( int s, int e, int mind[], int connected[] )

	/******************************************************************************/
	/*
			  Purpose:

			    FIND_NEAREST finds the nearest unconnected node.

			  Licensing:

			    This code is distributed under the GNU LGPL license. 

			  Modified:

			    02 July 2010

			  Author:

			    Original C version by Norm Matloff, CS Dept, UC Davis.
			    This C version by John Burkardt.

			  Parameters:

			    Input, int S, E, the first and last nodes that are to be checked.

			    Input, int MIND[NV], the currently computed minimum distance from
			    node 0 to each node.

			    Input, int CONNECTED[NV], is 1 for each connected node, whose 
			    minimum distance to node 0 has been determined.

			    D, the distance from node 0 to the nearest unconnected 
			    node in the range S to E.

			    Output, int V, the index of the nearest unconnected node in the range
			    S to E.
	 */
	{
		int i;
		int i4_huge = 2147483647;

		int d = i4_huge;
		int v = -1;

		for ( i = s; i <= e; i++ )
		{
			if ( connected[i]==0 &&  mind[i]<d )
			{
				d = mind[i];
				v = i;
			}
		}
		return v;
	}

	static void update_mind ( int s, int e, int mv, int connected[], int ohd[][], int mind[] )

	/******************************************************************************/
	/*
			  Purpose:

			    UPDATE_MIND updates the minimum distance vector.

			  Discussion:

			    We've just determined the minimum distance to node MV.

			    For each unconnected node I in the range S to E,
			    check whether the route from node 0 to MV to I is shorter
			    than the currently known minimum distance.

			  Licensing:

			    This code is distributed under the GNU LGPL license. 

			  Modified:

			    02 July 2010

			  Author:

			    Original C version by Norm Matloff, CS Dept, UC Davis.
			    This C version by John Burkardt.

			  Parameters:

			    Input, int S, E, the first and last nodes that are to be checked.

			    Input, int MV, the node whose minimum distance to node 0
			    has just been determined.

			    Input, int CONNECTED[NV], is 1 for each connected node, whose 
			    minimum distance to node 0 has been determined.

			    Input, int OHD[NV][NV], the distance of the direct link between
			    nodes I and J.

			    Input/output, int MIND[NV], the currently computed minimum distances
			    from node 0 to each node.  On output, the values for nodes S through
			    E have been updated.
	 */
	{
		int i;
		int i4_huge = 2147483647;

		for ( i = s; i <= e; i++ )
		{
			if ( connected[i]==0 )
			{
				if ( ohd[mv][i] < i4_huge )
				{
					if ( mind[mv] + ohd[mv][i] < mind[i] )  
					{
						mind[i] = mind[mv] + ohd[mv][i];
					}
				}
			}
		}
		return;
	}
}