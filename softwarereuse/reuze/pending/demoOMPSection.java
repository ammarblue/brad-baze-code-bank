package reuze.pending;

import reuze.pending.lc_omp.Work;

public class demoOMPSection implements lc_omp.IWork {
	static final int prime_num = 20000, sine_num = 20000;
	static int[] primes;
	static double[] sines;
	static double wtime1, wtime2;
	/******************************************************************************/

	public static void main ( String args[] )

	/******************************************************************************/
	/*
	  Purpose:

	    MAIN is the main program for MULTITASK_OPENMP.

	  Discussion:

	    This program demonstrates how OpenMP can be used for multitasking, that 
	    is, a simple kind of parallel processing in which a certain number of 
	    perhaps quite unrelated tasks must be done.

	    The OpenMP SECTIONS directive identifies the portion of the program where
	    the code for these tasks is given.

	    The OpenMP SECTION directive is used repeatedly to divide this area of
	    the program into independent tasks.

	    The code will get the benefit of parallel processing up to the point where
	    there are as many threads as there are tasks.

	    The code will get a substantial speedup if the tasks take roughly the
	    same amount of time.  However, if one task takes substantially more time
	    than the others, this results in a limit to the parallel speedup that is
	    possible.

	  Licensing:

	    This code is distributed under the GNU LGPL license.

	  Modified:

	    19 October 2011

	  Author:

	    John Burkardt

	*/
	{
	  double wtime;
	  System.out.printf ( "\n" );
	  System.out.printf ( "MULTITASK_OPENMP:\n" );
	  System.out.printf ( "  C/OpenMP version\n" );
	  System.out.printf ( "  Demonstrate how OpenMP can \"multitask\" by using the\n" );
	  System.out.printf ( "  SECTIONS directive to carry out several tasks in parallel.\n" );
	  demoOMPSection w=new demoOMPSection();
	  wtime = lc_omp.omp_get_wtime ( );
	/*# pragma omp parallel shared ( prime_num, primes, sine_num, sines )
	{
	  # pragma omp sections
	  {
	    # pragma omp section
	    {
	      wtime1 = lc_omp.omp_get_wtime ( );
	      primes = prime_table ( prime_num );
	      wtime1 = lc_omp.omp_get_wtime ( ) - wtime1;*/
	    /*}
	    # pragma omp section
	    {
	      wtime2 = lc_omp.omp_get_wtime ( );
	      sines = sine_table ( sine_num );
	      wtime2 = lc_omp.omp_get_wtime ( ) - wtime2;*/
	    /*}
	  }
	}*/
	  lc_omp.work(w, 1, 2);
	  wtime = lc_omp.omp_get_wtime ( ) - wtime;
	  lc_omp.finish();
	  System.out.printf ( "\n" );
	  System.out.printf ( "  Number of primes computed was %d\n", prime_num );
	  System.out.printf ( "  Last prime was %d\n", primes[prime_num-1] );
	  System.out.printf ( "  Number of sines computed was %d\n", sine_num );
	  System.out.printf ( "  Last sine computed was %g\n", sines[sine_num-1] );
	  System.out.printf ( "\n" );
	  System.out.printf ( "  Elapsed time = %g\n", wtime );
	  System.out.printf ( "  Task 1 time = %g\n", wtime1 );
	  System.out.printf ( "  Task 2 time = %g\n", wtime2 );

	/*
	  Terminate.
	*/
	  System.out.printf ( "\n" );
	  System.out.printf ( "MULTITASK_OPENMP:\n" );
	  System.out.printf ( "  Normal end of execution.\n" );
	  System.out.printf ( "\n" );
	}
	/******************************************************************************/

	static int[] prime_table ( int prime_num )

	/******************************************************************************/
	/*
	  Purpose:

	    PRIME_TABLE computes a table of the first PRIME_NUM prime numbers.

	  Licensing:

	    This code is distributed under the GNU LGPL license.

	  Modified:

	    19 October 2011

	  Author:

	    John Burkardt

	  Parameters:

	    Input, int PRIME_NUM, the number of primes to compute.

	    Output, int PRIME_TABLE[PRIME_NUM], the computed primes.
	*/
	{
	  int i;
	  int j;
	  int p;
	  int prime;
	  int[] primes;

	  primes = new int[prime_num];

	  i = 2;
	  p = 0;

	  while ( p < prime_num )
	  {
	    prime = 1;

	    for ( j = 2; j < i; j++ )
	    {
	      if ( ( i % j ) == 0 )
	      {
	        prime = 0;
	        break;
	      }
	    }
	      
	    if ( prime!=0 )
	    {
	      primes[p] = i;
	      p = p + 1;
	    }
	    i = i + 1;
	  }

	  return primes;
	}
	/******************************************************************************/

	static double[] sine_table ( int sine_num )

	/******************************************************************************/
	/*
	  Purpose:

	    SINE_TABLE computes a table of sines.

	  Licensing:

	    This code is distributed under the GNU LGPL license.

	  Modified:

	    19 October 2011

	  Author:

	    John Burkardt

	  Parameters:

	    Input, int SINE_NUM, the number of sines to compute.

	    Output, double SINE_TABLE[SINE_NUM], the sines.
	*/
	{
	  double a;
	  int i;
	  int j;
	  double pi = 3.141592653589793;
	  double sines[];

	  sines = new double[sine_num];

	  for ( i = 0; i < sine_num; i++ )
	  {
	    sines[i] = 0.0;
	    for ( j = 0; j <= i; j++ )
	    {
	      a = ( double ) ( j ) * pi / ( double ) ( sine_num - 1 );
	      sines[i] = sines[i] + Math.sin ( a );
	    }
	  }

	  return sines;
	}
	@Override
	public boolean evaluate(Work w) {
		switch (lc_omp.omp_get_thread_num()) {
			case 0:
			  wtime1 = lc_omp.omp_get_wtime ( );
		      primes = prime_table ( prime_num );
		      wtime1 = lc_omp.omp_get_wtime ( ) - wtime1;
		      break;
			case 1:
		      wtime2 = lc_omp.omp_get_wtime ( );
		      sines = sine_table ( sine_num );
		      wtime2 = lc_omp.omp_get_wtime ( ) - wtime2;
		      break;
		}
		return false;
	}
}
