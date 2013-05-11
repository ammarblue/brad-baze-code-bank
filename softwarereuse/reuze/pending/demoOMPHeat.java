package reuze.pending;

import reuze.pending.lc_omp.Work;

public class demoOMPHeat implements lc_omp.IWork {
	static final int M=500, N=600;
	static double u[][]=new double[M][];
	static double w[][]=new double[M][];
	static double diff, mean;
	public static void main ( String args[] )

	/******************************************************************************/
	/*
	  Purpose:

	    MAIN is the main program for HEATED_PLATE_OPENMP.

	  Discussion:

	    This code solves the steady state heat equation on a rectangular region.

	    The sequential version of this program needs approximately
	    18/epsilon iterations to complete. 


	    The physical region, and the boundary conditions, are suggested
	    by this diagram;

	                   W = 0
	             +------------------+
	             |                  |
	    W = 100  |                  | W = 100
	             |                  |
	             +------------------+
	                   W = 100

	    The region is covered with a grid of M by N nodes, and an N by N
	    array W is used to record the temperature.  The correspondence between
	    array indices and locations in the region is suggested by giving the
	    indices of the four corners:

	                  I = 0
	          [0][0]-------------[0][N-1]
	             |                  |
	      J = 0  |                  |  J = N-1
	             |                  |
	        [M-1][0]-----------[M-1][N-1]
	                  I = M-1

	    The steady state solution to the discrete heat equation satisfies the
	    following condition at an interior grid point:

	      W[Central] = (1/4) * ( W[North] + W[South] + W[East] + W[West] )

	    where "Central" is the index of the grid point, "North" is the index
	    of its immediate neighbor to the "north", and so on.
	   
	    Given an approximate solution of the steady state heat equation, a
	    "better" solution is given by replacing each interior point by the
	    average of its 4 neighbors - in other words, by using the condition
	    as an ASSIGNMENT statement:

	      W[Central]  <=  (1/4) * ( W[North] + W[South] + W[East] + W[West] )

	    If this process is repeated often enough, the difference between successive 
	    estimates of the solution will go to zero.

	    This program carries out such an iteration, using a tolerance specified by
	    the user, and writes the final estimate of the solution to a file that can
	    be used for graphic processing.

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    18 October 2011

	  Author:

	    Original C version by Michael Quinn.
	    This C version by John Burkardt.

	  Reference:

	    Michael Quinn,
	    Parallel Programming in C with MPI and OpenMP,
	    McGraw-Hill, 2004,
	    ISBN13: 978-0071232654,
	    LC: QA76.73.C15.Q55.

	  Local parameters:

	    Local, double DIFF, the norm of the change in the solution from one iteration
	    to the next.

	    Local, double MEAN, the average of the boundary values, used to initialize
	    the values of the solution in the interior.

	    Local, double U[M][N], the solution at the previous iteration.

	    Local, double W[M][N], the solution computed at the latest iteration.
	*/
	{
	  double epsilon = 0.001;
	  int i;
	  int iterations;
	  int iterations_print;
	  int j;
	  double my_diff;
	  double wtime;

	  System.out.printf ( "\n" );
	  System.out.printf ( "HEATED_PLATE_OPENMP\n" );
	  System.out.printf ( "  C/OpenMP version\n" );
	  System.out.printf ( "  A program to solve for the steady state temperature distribution\n" );
	  System.out.printf ( "  over a rectangular plate.\n" );
	  System.out.printf ( "\n" );
	  System.out.printf ( "  Spatial grid of %d by %d points.\n", M, N );
	  System.out.printf ( "  The iteration will be repeated until the change is <= %e\n", epsilon ); 
	  System.out.printf ( "  Number of processors available = %d\n", lc_omp.omp_get_num_procs ( ) );
	  System.out.printf ( "  Number of threads =              %d\n", lc_omp.omp_get_max_threads ( ) );
	/*
	  Set the boundary values, which don't change. 
	*/
	  demoOMPHeat work=new demoOMPHeat();
	  mean = 0.0;
      u[0]=new double[N];
      w[0]=new double[N];
      u[M-1]=new double[N];
      w[M-1]=new double[N];
	//#pragma omp parallel shared ( w ) private ( i, j )
	  {
	//#pragma omp for
	    for ( i = 1; i < M - 1; i++ )
	    {
	    	u[i]=new double[N];
	        w[i]=new double[N];
	      w[i][0] = 100.0;
	    /*}
	#pragma omp for
	    for ( i = 1; i < M - 1; i++ )
	    {*/
	      w[i][N-1] = 100.0;
	    }
	/*#pragma omp for
	    for ( j = 0; j < N; j++ )
	    {
	      w[M-1][j] = 100.0;
	    }
	#pragma omp for
	    for ( j = 0; j < N; j++ )
	    {
	      w[0][j] = 0.0;
	    }*/
	    for ( j = 0; j < N; j++ ) { w[M-1][j] = 100.0; w[0][j] = 0.0;}
	    //Arrays.fill seems to be slower??
	/*
	  Average the boundary values, to come up with a reasonable
	  initial value for the interior.
	*/
	//#pragma omp for reduction ( + : mean )
	    for ( i = 1; i < M - 1; i++ )
	    {
	      mean = mean + w[i][0] + w[i][N-1];
	    }
	//#pragma omp for reduction ( + : mean )
	    for ( j = 0; j < N; j++ )
	    {
	      mean = mean + w[M-1][j] + w[0][j];
	    }
	  }
	/*
	  OpenMP note:
	  You cannot normalize MEAN inside the parallel region.  It
	  only gets its correct value once you leave the parallel region.
	  So we interrupt the parallel region, set MEAN, and go back in.
	*/
	  mean = mean / ( double ) ( 2 * M + 2 * N - 4 );
	  System.out.printf ( "\n" );
	  System.out.printf ( "  MEAN = %f\n", mean );
	/* 
	  Initialize the interior solution to the mean value.
	*/
	//#pragma omp parallel shared ( mean, w ) private ( i, j )
	  {
	  lc_omp.work(work, 1, M-2, 3);
	/*#pragma omp for
	    for ( i = 1; i < M - 1; i++ )
	    {
	      for ( j = 1; j < N - 1; j++ )
	      {
	        w[i][j] = mean;
	      }
	    }*/
	  }
	/*
	  iterate until the  new solution W differs from the old solution U
	  by no more than EPSILON.
	*/
	  iterations = 0;
	  iterations_print = 1;
	  System.out.printf ( "\n" );
	  System.out.printf ( " Iteration  Change\n" );
	  System.out.printf ( "\n" );
	  wtime = lc_omp.omp_get_wtime ( );

	  diff = epsilon;

	  while ( epsilon <= diff )
	  {
	//# pragma omp parallel shared ( u, w ) private ( i, j )
	    {
	/*
	  Save the old solution in U.
	*/
	lc_omp.work(work, 0, M-1, 1);
	/*# pragma omp for
	      for ( i = 0; i < M; i++ ) 
	      {
	        for ( j = 0; j < N; j++ )
	        {
	          u[i][j] = w[i][j];
	        }
	      }
	      */ //Arrays.copyOf is slower??
	/*
	  Determine the new estimate of the solution at the interior points.
	  The new solution W is the average of north, south, east and west neighbors.
	*/
	my_diff=0;
	diff=0;
	lc_omp.work(work, 1, M-2, 2);
	/*# pragma omp for
	      for ( i = 1; i < M - 1; i++ )
	      {
	        for ( j = 1; j < N - 1; j++ )
	        {
	          w[i][j] = ( u[i-1][j] + u[i+1][j] + u[i][j-1] + u[i][j+1] ) / 4.0;
	          double x=Math.abs ( w[i][j] - u[i][j] );
	          if ( my_diff <  x )
	          {
	            my_diff = x;
	          }
	        }
	      }
	      diff=my_diff;*/
	    }
	/*
	  C and C++ cannot compute a maximum as a reduction operation.

	  Therefore, we define a private variable MY_DIFF for each thread.
	  Once they have all computed their values, we use a CRITICAL section
	  to update DIFF.
	*/
	    /*diff = 0.0;
	//# pragma omp parallel shared ( diff, u, w ) private ( i, j, my_diff )
	    {
	      my_diff = 0.0;
	//# pragma omp for
	      for ( i = 1; i < M - 1; i++ )
	      {
	        for ( j = 1; j < N - 1; j++ )
	        {
	          if ( my_diff < Math.abs ( w[i][j] - u[i][j] ) )
	          {
	            my_diff = Math.abs ( w[i][j] - u[i][j] );
	          }
	        }
	      }
	//# pragma omp critical
	      {
	        if ( diff < my_diff )
	        {
	          diff = my_diff;
	        }
	      }
	    }*/

	    iterations++;
	    if ( iterations == iterations_print )
	    {
	      System.out.printf ( "  %8d  %f\n", iterations, diff );
	      iterations_print = 2 * iterations_print;
	    }
	  } 
	  wtime = lc_omp.omp_get_wtime ( ) - wtime;

	  System.out.printf ( "\n" );
	  System.out.printf ( "  %8d  %f\n", iterations, diff );
	  System.out.printf ( "\n" );
	  System.out.printf ( "  Error tolerance achieved.\n" );
	  System.out.printf ( "  Wallclock time = %f\n", wtime );
	/*
	  Terminate.
	*/
	  System.out.printf ( "\n" );
	  System.out.printf ( "HEATED_PLATE_OPENMP:\n" );
	  System.out.printf ( "  Normal end of execution.\n" );
	  lc_omp.finish();
	}
	@Override
	public boolean evaluate(Work work) {
		switch (work.task) {
		case 1:
			for (int i = work.start; i <= work.end; i++ ) 
		      {
		        for (int j = 0; j < N; j++ )
		        {
		          u[i][j] = w[i][j];
		        }
		      }
			break;
		case 2:
			double my_diff=0;
			for (int i = work.start; i <= work.end; i++ )
		      {
		        for (int j = 1; j < N - 1; j++ )
		        {
		          w[i][j] = ( u[i-1][j] + u[i+1][j] + u[i][j-1] + u[i][j+1] ) / 4.0;
		          double x=Math.abs ( w[i][j] - u[i][j] );
		          if ( my_diff <  x )
		          {
		            my_diff = x;
		          }
		        }
		      }
			synchronized(lc_omp.critical()) {
				if ( diff < my_diff )
		        {
		          diff = my_diff;
		        }
			}
			break;
		case 3:
			for (int i = work.start; i <= work.end; i++ )
		    {
		      for (int j = 1; j < N - 1; j++ )
		      {
		        w[i][j] = mean;
		      }
		    }
			break;
		}
		return true;
	}

}
