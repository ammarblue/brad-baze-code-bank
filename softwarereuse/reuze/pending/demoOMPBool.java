package reuze.pending;

import reuze.pending.lc_omp.Work;

public class demoOMPBool implements lc_omp.IWork {
	static int solution_num, ilo, ihi;
	static final int N=23;
	/******************************************************************************/

	public static void main ( String args[] )

	/******************************************************************************/
	/*
	  Purpose:

	    MAIN is the main program for SATISFY_OPENMP.

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    24 March 2009

	  Author:

	    John Burkardt

	  Reference:

	    Michael Quinn,
	    Parallel Programming in C with MPI and OpenMP,
	    McGraw-Hill, 2004,
	    ISBN13: 978-0071232654,
	    LC: QA76.73.C15.Q55.
	*/
	{
	  boolean bvec[]=new boolean[N];
	  int i;
	  int id;
	  int ihi2;
	  int ilo2;
	  int j;
	  int proc_num;
	  int solution_num_local;
	  int thread_num;
	  boolean value;
	  double wtime;

	  System.out.printf ( "\n" );
	  System.out.printf ( "\n" );
	  System.out.printf ( "SATISFY_OPENMP\n" );
	  System.out.printf ( "  C + OpenMP version\n" );
	  System.out.printf ( "  We have a logical function of N logical arguments.\n" );
	  System.out.printf ( "  We do an exhaustive search of all 2^N possibilities,\n" );
	  System.out.printf ( "  seeking those inputs that make the function TRUE.\n" );

	  System.out.printf ( "\n" );
	  System.out.printf ( "  Number of processors available = %d\n", lc_omp.omp_get_num_procs ( ) );
	  System.out.printf ( "  Number of threads =              %d\n", lc_omp.omp_get_max_threads ( ) );
	/*
	  Compute the number of binary vectors to check.
	*/
	  ilo = 0;
	  ihi = 1;
	  for ( i = 1; i <= N; i++ )
	  {
	    ihi = ihi * 2;
	  }
	  System.out.printf ( "\n" );
	  System.out.printf ( "  The number of logical variables is N = %d\n", N );
	  System.out.printf ( "  The number of input vectors to check is %d\n", ihi );
	  System.out.printf ( "\n" );
	  System.out.printf ( "   # Processor       Index    ---------Input Values------------------------\n" );
	  System.out.printf ( "\n" );
	/*
	  Processor ID takes the interval ILO2 <= I < IHI2.
	  Using the formulas below yields a set of non-intersecting intervals
	  which cover the original interval [ILO,IHI).
	*/
	  thread_num = 1;//lc_omp.omp_get_max_threads ( );

	  solution_num = 0;

	  wtime = lc_omp.omp_get_wtime ( );
	  demoOMPBool w=new demoOMPBool();
	  lc_omp.work(w, 1);
	  if (3>4) {
	//# pragma omp parallel shared ( ihi, ilo, n, thread_num ) private ( bvec, i, id, ihi2, ilo2, j, solution_num_local, value ) reduction ( + : solution_num )
	  {
	    id = 0;//lc_omp.omp_get_thread_num ( );

	    ilo2 = ( ( thread_num - id     ) * ilo   
	           + (              id     ) * ihi ) 
	           / ( thread_num          );

	    ihi2 = ( ( thread_num - id - 1 ) * ilo   
	           + (              id + 1 ) * ihi ) 
	           / ( thread_num          );

	    System.out.printf ( "\n" );
	    System.out.printf ( "  Processor %8d iterates from %8d <= I < %8d.\n", id, ilo2, ihi2 );
	    System.out.printf ( "\n" );
	/*
	  Check every possible input vector.
	*/
	    solution_num_local = 0;

	    for ( i = ilo2; i < ihi2; i++ )
	    {
	      i4_to_bvec ( i, N, bvec );

	      value = circuit_value ( N, bvec );

	      if ( value )
	      {
	        solution_num_local = solution_num_local + 1;
	  
	        System.out.printf ( "  %2d  %8d  %10d:  ", solution_num_local, id, i );
	        for ( j = 0; j < N; j++ )
	        {
	          System.out.print (bvec[j]?"1 ":"0 ");
	        }
	        System.out.printf ( "\n" );
	      }
	    }
	    synchronized(lc_omp.critical()) {
	      solution_num = solution_num + solution_num_local;
	    }
	  }
	  } //3>4
	  wtime = lc_omp.omp_get_wtime ( ) - wtime;
	  System.out.printf ( "\n" );
	  System.out.printf ( "  Number of solutions found was %d\n", solution_num );
	  System.out.printf ( "  Elapsed wall clock time (seconds) %f\n", wtime );
	/*
	  Terminate.
	*/
	  System.out.printf ( "\n" );
	  System.out.printf ( "SATISFY_OPENMP\n" );
	  System.out.printf ( "  Normal end of execution.\n" );
	  System.out.printf ( "\n" );
	  lc_omp.finish();
	}
	/******************************************************************************/

	static boolean circuit_value ( int n, boolean bvec[] )

	/******************************************************************************/
	/*
	  Purpose:

	    CIRCUIT_VALUE returns the value of a circuit for a given input set.

	  Licensing:

	    This code is distributed under the GNU LGPL license.

	  Modified:

	    20 March 2009

	  Author:

	    John Burkardt

	  Reference:

	    Michael Quinn,
	    Parallel Programming in C with MPI and OpenMP,
	    McGraw-Hill, 2004,
	    ISBN13: 978-0071232654,
	    LC: QA76.73.C15.Q55.

	  Parameters:

	    Input, int N, the length of the input vector.

	    Input, int BVEC[N], the binary inputs.

	    Output, int CIRCUIT_VALUE, the output of the circuit.
	*/
	{
	  boolean value;

	  value = 
	       (  bvec[0]  ||  bvec[1]  )
	    && ( !bvec[1]  || !bvec[3]  )
	    && (  bvec[2]  ||  bvec[3]  )
	    && ( !bvec[3]  || !bvec[4]  )
	    && (  bvec[4]  || !bvec[5]  )
	    && (  bvec[5]  || !bvec[6]  )
	    && (  bvec[5]  ||  bvec[6]  )
	    && (  bvec[6]  || !bvec[15] )
	    && (  bvec[7]  || !bvec[8]  )
	    && ( !bvec[7]  || !bvec[13] )
	    && (  bvec[8]  ||  bvec[9]  )
	    && (  bvec[8]  || !bvec[9]  )
	    && ( !bvec[9]  || !bvec[10] )
	    && (  bvec[9]  ||  bvec[11] )
	    && (  bvec[10] ||  bvec[11] )
	    && (  bvec[12] ||  bvec[13] )
	    && (  bvec[13] || !bvec[14] )
	    && (  bvec[14] ||  bvec[15] )
	    && (  bvec[14] ||  bvec[16] )
	    && (  bvec[17] ||  bvec[1]  )
	    && (  bvec[18] || !bvec[0]  )
	    && (  bvec[19] ||  bvec[1]  )
	    && (  bvec[19] || !bvec[18] )
	    && ( !bvec[19] || !bvec[9]  )
	    && (  bvec[0]  ||  bvec[17] )
	    && ( !bvec[1]  ||  bvec[20] )
	    && ( !bvec[21] ||  bvec[20] )
	    && ( !bvec[22] ||  bvec[20] )
	    && ( !bvec[21] || !bvec[20] )
	    && (  bvec[22] || !bvec[20] );

	  return value;
	}
	/******************************************************************************/

	static void i4_to_bvec ( int i4, int n, boolean bvec[] )

	/******************************************************************************/
	/*
	  Purpose:

	    I4_TO_BVEC converts an integer into a binary vector.

	  Licensing:

	    This code is distributed under the GNU LGPL license.

	  Modified:

	    20 March 2009

	  Author:

	    John Burkardt

	  Parameters:

	    Input, int I4, the integer.

	    Input, int N, the dimension of the vector.

	    Output, int BVEC[N], the vector of binary remainders.
	*/
	{
	  int i;

	  for ( i = n - 1; 0 <= i; i-- )
	  {
	    bvec[i] = (i4 & 1)!=0;
	    i4 = i4 / 2;
	  }

	}
	@Override
	public boolean evaluate(Work w) {
		int thread_num, id, ilo2, ihi2, solution_num_local;
		boolean bvec[]=new boolean[N];
		boolean value;
		/*
		  Processor ID takes the interval ILO2 <= I < IHI2.
		  Using the formulas below yields a set of non-intersecting intervals
		  which cover the original interval [ILO,IHI).
		*/
		  thread_num = lc_omp.omp_get_num_threads ( );

		//# pragma omp parallel shared ( ihi, ilo, n, thread_num ) private ( bvec, i, id, ihi2, ilo2, j, solution_num_local, value ) reduction ( + : solution_num )
		  {
		    id = lc_omp.omp_get_thread_num ( );

		    ilo2 = ( ( thread_num - id     ) * ilo   
		           + (              id     ) * ihi ) 
		           / ( thread_num          );

		    ihi2 = ( ( thread_num - id - 1 ) * ilo   
		           + (              id + 1 ) * ihi ) 
		           / ( thread_num          );

		    System.out.printf ( "\n" );
		    System.out.printf ( "  Processor %8d iterates from %8d <= I < %8d.\n", id, ilo2, ihi2 );
		    System.out.printf ( "\n" );
		  //Check every possible input vector.
		    solution_num_local = 0;

		    for (int i = ilo2; i < ihi2; i++ )
		    {
		      i4_to_bvec ( i, N, bvec );

		      value = circuit_value ( N, bvec );

		      if ( value )
		      {
		        solution_num_local = solution_num_local + 1;
		  
		        System.out.printf ( "  %2d  %8d  %10d:  ", solution_num_local, id, i );
		        for (int j = 0; j < N; j++ )
		        {
		          System.out.print (bvec[j]?"1 ":"0 ");
		        }
		        System.out.printf ( "\n" );
		      }
		    }
		    solution_num = solution_num + solution_num_local;
		  }
		return true;
	}
}
