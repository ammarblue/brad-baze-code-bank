package reuze.pending;

import reuze.pending.lc_omp.Work;

public class demoOMPFFT {
	static double seed;
	/******************************************************************************/

	public static void main ( String args[] )

	/******************************************************************************/
	/* 
	  Purpose:

	    MAIN is the main program for FFT_OPENMP.

	  Discussion:

	    The "complex" vector A is actually stored as a double vector B.

	    The "complex" vector entry A[I] is stored as:

	      B[I*2+0], the real part,
	      B[I*2+1], the imaginary part.

	  Modified:

	    20 March 2009

	  Author:

	    Original C version by Wesley Petersen.
	    This C version by John Burkardt.

	  Reference:

	    Wesley Petersen, Peter Arbenz, 
	    Introduction to Parallel Computing - A practical guide with examples in C,
	    Oxford University Press,
	    ISBN: 0-19-851576-6,
	    LC: QA76.58.P47.
	*/
	{
	  double error;
	  int first;
	  double flops;
	  double fnm1;
	  int i;
	  int icase;
	  int it;
	  int ln2;
	  int ln2_max = 23;
	  double mflops;
	  int n;
	  int nits = 10000;
	  int proc_num;
	  double sgn;
	  int thread_num;
	  double w[];
	  double wtime, time;
	  double x[];
	  double y[];
	  double z[];
	  double z0;
	  double z1;

	  System.out.printf ( "\n" );
	  System.out.printf ( "FFT_OPENMP\n" );
	  System.out.printf ( "  C/OpenMP version\n" );
	  System.out.printf ( "\n" );
	  System.out.printf ( "  Demonstrate an implementation of the Fast Fourier Transform\n" );
	  System.out.printf ( "  of a complex data vector, using OpenMP for parallel execution.\n" );

	  System.out.printf ( "\n" );
	  System.out.printf ( "  Number of processors available = %d\n", lc_omp.omp_get_num_procs ( ) );
	  System.out.printf ( "  Number of threads =              %d\n", lc_omp.omp_get_max_threads ( ) );
	/*
	  Prepare for tests.
	*/
	  System.out.printf ( "\n" );
	  System.out.printf ( "  Accuracy check:\n" );
	  System.out.printf ( "\n" );
	  System.out.printf ( "    FFT ( FFT ( X(1:N) ) ) == N * X(1:N)\n" );
	  System.out.printf ( "\n" );
	  System.out.printf ( "             N      NITS    Error         Time          Time/Call     MFLOPS\n" );
	  System.out.printf ( "\n" );
	  time=lc_omp.omp_get_wtime();
	  seed  = 331.0;
	  n = 1;
	/*
	  LN2 is the log base 2 of N.  Each increase of LN2 doubles N.
	*/
	  for ( ln2 = 1; ln2 <= ln2_max; ln2++ )
	  {
	    n = 2 * n;
	/*
	  Allocate storage for the complex arrays W, X, Y, Z.  

	  We handle the complex arithmetic,
	  and store a complex number as a pair of doubles, a complex vector as a doubly
	  dimensioned array whose second dimension is 2. 
	*/
	    w = new double[n];
	    x = new double[2 * n];
	    y = new double[2 * n];
	    z = new double[2 * n];

	    first = 1;

	    for ( icase = 0; icase < 2; icase++ )
	    {
	      if ( first != 0 )
	      {
	        for ( i = 0; i < 2 * n; i = i + 2 )
	        {
	          z0 = ggl ();
	          z1 = ggl ();
	          x[i] = z0;
	          z[i] = z0;
	          x[i+1] = z1;
	          z[i+1] = z1;
	        }
	      } 
	      else
	      {
	//# pragma omp parallel shared ( n, x, z ) private ( i, z0, z1 )

	//# pragma omp for nowait

	        for ( i = 0; i < 2 * n; i = i + 2 )
	        {
	          z0 = 0.0;              /* real part of array */
	          z1 = 0.0;              /* imaginary part of array */
	          x[i] = z0;
	          z[i] = z0;           /* copy of initial real data */
	          x[i+1] = z1;
	          z[i+1] = z1;         /* copy of initial imag. data */
	        }
	      }
	/* 
	  Initialize the sine and cosine tables.
	*/
	      cffti2 ( n, w );
	/* 
	  Transform forward, back 
	*/
	      if ( first != 0 )
	      {
	        sgn = + 1.0;
	        cfft2 ( n, x, y, w, sgn );
	        sgn = - 1.0;
	        cfft2 ( n, y, x, w, sgn );
	/* 
	  Results should be same as the initial data multiplied by N.
	*/
	        fnm1 = 1.0 / ( double ) n;
	        error = 0.0;
	        for ( i = 0; i < 2 * n; i = i + 2 )
	        {
	          error = error 
	          + Math.pow ( z[i]   - fnm1 * x[i], 2 )
	          + Math.pow ( z[i+1] - fnm1 * x[i+1], 2 );
	        }
	        error = Math.sqrt ( fnm1 * error );
	        System.out.printf ( "  %12d  %8d  %12e", n, nits, error );
	        first = 0;
	      }
	      else
	      {
	        wtime = lc_omp.omp_get_wtime ( );
	        for ( it = 0; it < nits; it++ )
	        {
	          sgn = + 1.0;
	          cfft2 ( n, x, y, w, sgn );
	          sgn = - 1.0;
	          cfft2 ( n, y, x, w, sgn );
	        }
	        wtime = lc_omp.omp_get_wtime ( ) - wtime;

	        flops = 2.0 * ( double ) nits 
	          * ( 5.0 * ( double ) n * ( double ) ln2 );

	        mflops = flops / 1.0E+06 / wtime;

	        System.out.printf ( "  %12e  %12e  %12f\n", wtime, wtime / ( double ) ( 2 * nits ), mflops );
	      }
	    }
	    if ( ( ln2 % 4 ) == 0 ) 
	    {
	      nits = nits / 10;
	    }
	    if ( nits < 1 ) 
	    {
	      nits = 1;
	    }
	  }
	  System.out.printf("time=%g\n",lc_omp.omp_get_wtime()-time);
	/*
	  Terminate.
	*/
	  System.out.printf ( "\n" );
	  System.out.printf ( "FFT_OPENMP:\n" );
	  System.out.printf ( "  Normal end of execution.\n" );
	  System.out.printf ( "\n" );
	  lc_omp.finish();
	}
	/******************************************************************************/

	static void ccopy ( int n, double x[], double y[] )

	/******************************************************************************/
	/*
	  Purpose:

	    CCOPY copies a complex vector.

	  Discussion:

	    The "complex" vector A[N] is actually stored as a double vector B[2*N].

	    The "complex" vector entry A[I] is stored as:

	      B[I*2+0], the real part,
	      B[I*2+1], the imaginary part.

	  Modified:

	    20 March 2009

	  Author:

	    Original C version by Wesley Petersen.
	    This C version by John Burkardt.

	  Reference:

	    Wesley Petersen, Peter Arbenz, 
	    Introduction to Parallel Computing - A practical guide with examples in C,
	    Oxford University Press,
	    ISBN: 0-19-851576-6,
	    LC: QA76.58.P47.

	  Parameters:

	    Input, int N, the length of the vector.

	    Input, double X[2*N], the vector to be copied.

	    Output, double Y[2*N], a copy of X.
	*/
	{
	  int i;

	  for ( i = 0; i < n; i++ )
	  {
	    y[i*2+0] = x[i*2+0];
	    y[i*2+1] = x[i*2+1];
	   }
	  return;
	}
	/******************************************************************************/

	static void cfft2 ( int n, double x[], double y[], double w[], double sgn )

	/******************************************************************************/
	/*
	  Purpose:

	    CFFT2 performs a complex Fast Fourier Transform.

	  Modified:

	    20 March 2009

	  Author:

	    Original C version by Wesley Petersen.
	    This C version by John Burkardt.

	  Reference:

	    Wesley Petersen, Peter Arbenz, 
	    Introduction to Parallel Computing - A practical guide with examples in C,
	    Oxford University Press,
	    ISBN: 0-19-851576-6,
	    LC: QA76.58.P47.

	  Parameters:

	    Input, int N, the size of the array to be transformed.

	    Input/output, double X[2*N], the data to be transformed.  
	    On output, the contents of X have been overwritten by work information.

	    Output, double Y[2*N], the forward or backward FFT of X.

	    Input, double W[N], a table of sines and cosines.

	    Input, double SGN, is +1 for a "forward" FFT and -1 for a "backward" FFT.
	*/
	{
	  int j;
	  int m;
	  int mj;
	  int tgle;

	   m = ( int ) ( Math.log ( ( double ) n ) / Math.log ( 1.99 ) );
	   mj   = 1;
	/*
	  Toggling switch for work array.
	*/
	  tgle = 1;
	  step ( n, mj, x, x, (n/2)*2+0, y, y, mj*2+0, w, sgn );

	  if ( n == 2 )
	  {
	    return;
	  }

	  for ( j = 0; j < m - 2; j++ )
	  {
	    mj = mj * 2;
	    if ( tgle != 0 )
	    {
	      step ( n, mj, y, y, (n/2)*2+0, x, x, mj*2+0, w, sgn );
	      tgle = 0;
	    }
	    else
	    {
	      step ( n, mj, x, x, (n/2)*2+0, y, y, mj*2+0, w, sgn );
	      tgle = 1;
	    }
	  }
	/* 
	  Last pass through data: move Y to X if needed.
	*/
	  if ( tgle != 0 ) 
	  {
	    ccopy ( n, y, x );
	  }

	  mj = n / 2;
	  step ( n, mj, x, x, (n/2)*2+0, y, y, mj*2+0, w, sgn );

	  return;
	}
	/******************************************************************************/

	static void cffti ( int n, double w[] )

	/******************************************************************************/
	/*
	  Purpose:

	    CFFTI sets up sine and cosine tables needed for the FFT calculation.

	  Modified:

	    20 March 2009

	  Author:

	    Original C version by Wesley Petersen.
	    This C version by John Burkardt.

	  Reference:

	    Wesley Petersen, Peter Arbenz, 
	    Introduction to Parallel Computing - A practical guide with examples in C,
	    Oxford University Press,
	    ISBN: 0-19-851576-6,
	    LC: QA76.58.P47.

	  Parameters:

	    Input, int N, the size of the array to be transformed.

	    Output, double W[N], a table of sines and cosines.
	*/
	{
	  double arg;
	  double aw;
	  int i;
	  int n2;

	  n2 = n / 2;
	  aw = 2.0 * Math.PI / ( ( double ) n );

	//# pragma omp parallel shared ( aw, n, w ) private ( arg, i )

	//# pragma omp for nowait

	  for ( i = 0; i < n2; i++ )
	  {
	    arg = aw * ( ( double ) i );
	    w[i*2+0] = Math.cos ( arg );
	    w[i*2+1] = Math.sin ( arg );
	  }
	  return;
	}
	static void cffti2 ( int n, double w[] ) {
		  int n2;
		  n2 = n / 2;
		  Foo f=new Foo();
		  f.w=w;
		  f.aw = 2.0 * Math.PI / ( ( double ) n );
		  lc_omp.work(f, 0, n2-1, 1, 1);
		
	}
	static class Foo implements lc_omp.IWork {
		double w[], aw;
		public boolean evaluate(Work work) {
			for (int i = work.start; i <= work.end; i++ )
			  {
			    double arg = aw * ( ( double ) i );
			    w[i*2+0] = Math.cos ( arg );
			    w[i*2+1] = Math.sin ( arg );
			  }
			return true;
		}
	}
	/******************************************************************************/

	static double ggl ()

	/******************************************************************************/
	/* 
	  Purpose:

	    GGL generates uniformly distributed pseudorandom real numbers in [0,1]. 

	  Modified:

	    20 March 2009

	  Author:

	    Original C version by Wesley Petersen, M Troyer, I Vattulainen.
	    This C version by John Burkardt.

	  Reference:

	    Wesley Petersen, Peter Arbenz, 
	    Introduction to Parallel Computing - A practical guide with examples in C,
	    Oxford University Press,
	    ISBN: 0-19-851576-6,
	    LC: QA76.58.P47.

	  Parameters:

	    Input/output, double *SEED, used as a seed for the sequence.

	    Output, double GGL, the next pseudo-random value.
	*/
	{
	  double d2 = 0.2147483647e10;
	  double t;
	  double value;

	  t = seed;
	  t = 16807.0 * t % d2;
	  seed =  t;
	  value = ( double ) ( ( t - 1.0 ) / ( d2 - 1.0 ) );

	  return value;
	}
	/******************************************************************************/

	static void step ( int n, int mj, double a[], double b[], int bindex, double c[],
	  double d[], int dindex, double w[], double sgn )

	/******************************************************************************/
	/*
	  Purpose:

	    STEP carries out one step of the workspace version of CFFT2.

	  Modified:

	    20 March 2009

	  Author:

	    Original C version by Wesley Petersen.
	    This C version by John Burkardt.

	  Reference:

	    Wesley Petersen, Peter Arbenz, 
	    Introduction to Parallel Computing - A practical guide with examples in C,
	    Oxford University Press,
	    ISBN: 0-19-851576-6,
	    LC: QA76.58.P47.

	  Parameters:

	*/
	{

	  int lj;
	  int mj2;

	  mj2 = 2 * mj;
	  lj  = n / mj2;

	//# pragma omp parallel shared ( a, b, c, d, lj, mj, mj2, sgn, w ) private ( ambr, ambu, j, ja, jb, jc, jd, jw, k, wjw )

	//# pragma omp for nowait
	  Moo f=new Moo();
	  f.a=a; f.b=b; f.c=c; f.d=d; f.bindex=bindex; f.dindex=dindex; f.mj=mj; f.mj2=mj2; f.w=w; f.sgn=sgn; f.lj=lj;
      lc_omp.work(f, 0, lj-1, 1);
	  /*double ambr;
	  double ambu;
	  int j;
	  int ja;
	  int jb;
	  int jc;
	  int jd;
	  int jw;
	  int k;
	  double wjw[]=new double[2];
	  for (j = 0; j <lj; j++ )
	  {
	    jw = j * mj;
	    ja  = jw;
	    jb  = ja;
	    jc  = j * mj2;
	    jd  = jc;

	    wjw[0] = w[jw*2+0]; 
	    wjw[1] = w[jw*2+1];

	    if ( sgn < 0.0 ) 
	    {
	      wjw[1] = - wjw[1];
	    }

	    for ( k = 0; k < mj; k++ )
	    {
	      c[(jc+k)*2+0] = a[(ja+k)*2+0] + b[(jb+k)*2+0+bindex];
	      c[(jc+k)*2+1] = a[(ja+k)*2+1] + b[(jb+k)*2+1+bindex];

	      ambr = a[(ja+k)*2+0] - b[(jb+k)*2+0+bindex];
	      ambu = a[(ja+k)*2+1] - b[(jb+k)*2+1+bindex];

	      d[(jd+k)*2+0+dindex] = wjw[0] * ambr - wjw[1] * ambu;
	      d[(jd+k)*2+1+dindex] = wjw[1] * ambr + wjw[0] * ambu;
	    }
	  }*/
	}
	static class Moo implements lc_omp.IWork {
		double a[], b[], c[], d[], sgn, w[];
		int lj, mj, mj2, bindex, dindex;
		public boolean evaluate(Work work) {
			  double ambr;
			  double ambu;
			  int j;
			  int ja;
			  int jb;
			  int jc;
			  int jd;
			  int jw;
			  int k;
			  double wjw[]=new double[2];
			  for (j = work.start; j <= work.end; j++ )
			  {
			    jw = j * mj;
			    ja  = jw;
			    jb  = ja;
			    jc  = j * mj2;
			    jd  = jc;

			    wjw[0] = w[jw*2+0]; 
			    wjw[1] = w[jw*2+1];

			    if ( sgn < 0.0 ) 
			    {
			      wjw[1] = - wjw[1];
			    }

			    for ( k = 0; k < mj; k++ )
			    {
			      c[(jc+k)*2+0] = a[(ja+k)*2+0] + b[(jb+k)*2+0+bindex];
			      c[(jc+k)*2+1] = a[(ja+k)*2+1] + b[(jb+k)*2+1+bindex];

			      ambr = a[(ja+k)*2+0] - b[(jb+k)*2+0+bindex];
			      ambu = a[(ja+k)*2+1] - b[(jb+k)*2+1+bindex];

			      d[(jd+k)*2+0+dindex] = wjw[0] * ambr - wjw[1] * ambu;
			      d[(jd+k)*2+1+dindex] = wjw[1] * ambr + wjw[0] * ambu;
			    }
			  }
			return true;
		}
	}
}
