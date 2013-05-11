package reuze.pending;

import reuze.pending.lc_omp.Work;

public class demoOMPParticle {
	static int seed = 123456789;
	static final double PI2 = 3.141592653589793 / 2.0;
	/******************************************************************************/

	public static void main ( String args[] )

	/******************************************************************************/
	/*
	  Purpose:

	    MAIN is the main program for MD_OPENMP.

	  Discussion:

	    MD implements a simple molecular dynamics simulation.

	    The program uses Open MP directives to allow parallel computation.

	    The velocity Verlet time integration scheme is used. 

	    The particles interact with a central pair potential.

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    30 July 2009

	  Author:

	    Original FORTRAN77 version by Bill Magro.
	    C version by John Burkardt.

	  Parameters:

	    None
	 */
	{
		double []acc;
		double []box;
		double dt = 0.0001;
		double e0;
		double []force;
		int i;
		double mass = 1.0;
		int nd = 3;
		int np = 2000;
		double []pos;
		int step;
		int step_num = 400;
		int step_print;
		int step_print_index;
		int step_print_num;
		double []vel;
		double wtime;

		acc = new double[nd * np];
		box = new double[nd];
		force = new double[nd * np];
		pos = new double[nd * np];
		vel = new double[nd * np];

		System.out.printf ( "\n" );
		System.out.printf ( "MD_OPENMP\n" );
		System.out.printf ( "  C/OpenMP version\n" );
		System.out.printf ( "\n" );
		System.out.printf ( "  A molecular dynamics program.\n" );

		System.out.printf ( "\n" );
		System.out.printf ( "  NP, the number of particles in the simulation is %d\n", np );
		System.out.printf ( "  STEP_NUM, the number of time steps, is %d\n", step_num );
		System.out.printf ( "  DT, the size of each time step, is %f\n", dt );

		System.out.printf ( "\n" );
		System.out.printf ( "  Number of processors available = %d\n", lc_omp.omp_get_num_procs ( ) );
		System.out.printf ( "  Number of threads =              %d\n", lc_omp.omp_get_max_threads ( ) );
		/*
	  Set the dimensions of the box.
		 */
		for ( i = 0; i < nd; i++ )
		{
			box[i] = 10.0;
		}

		System.out.printf ( "\n" );
		System.out.printf ( "  Initializing positions, velocities, and accelerations.\n" );
		/*
	  Set initial positions, velocities, and accelerations.
		 */
		initialize ( np, nd, box, pos, vel, acc );
		/*
	  Compute the forces and energies.
		 */
		System.out.printf ( "\n" );
		System.out.printf ( "  Computing initial forces and energies.\n" );
		double poki[]=new double[2];
		compute ( np, nd, pos, vel, mass, force, poki );

		e0 = poki[0] + poki[1];
		/*
	  This is the main time stepping loop:
	    Compute forces and energies,
	    Update positions, velocities, accelerations.
		 */
		System.out.printf ( "\n" );
		System.out.printf ( "  At each step, we report the potential and kinetic energies.\n" );
		System.out.printf ( "  The sum of these energies should be a constant.\n" );
		System.out.printf ( "  As an accuracy check, we also print the relative error\n" );
		System.out.printf ( "  in the total energy.\n" );
		System.out.printf ( "\n" );
		System.out.printf ( "      Step      Potential       Kinetic        (P+K-E0)/E0\n" );
		System.out.printf ( "                Energy P        Energy K       Relative Energy Error\n" );
		System.out.printf ( "\n" );

		step_print = 0;
		step_print_index = 0;
		step_print_num = 10;

		step = 0;
		System.out.printf ( "  %8d  %14f  %14f  %14e\n",
				step, poki[0], poki[1], ( poki[0] + poki[1] - e0 ) / e0 );
		step_print_index = step_print_index + 1;
		step_print = ( step_print_index * step_num ) / step_print_num;

		wtime = lc_omp.omp_get_wtime ( );
		for ( step = 1; step <= step_num; step++ )
		{
			compute ( np, nd, pos, vel, mass, force, poki );

			if ( step == step_print )
			{
				System.out.printf ( "  %8d  %14f  %14f  %14e\n", step, poki[0], poki[1],
						( poki[0] + poki[1] - e0 ) / e0 );
				step_print_index = step_print_index + 1;
				step_print = ( step_print_index * step_num ) / step_print_num;
			}
			update ( np, nd, pos, vel, force, acc, mass, dt );
		}
		wtime = lc_omp.omp_get_wtime ( ) - wtime;

		System.out.printf ( "\n" );
		System.out.printf ( "  Elapsed time for main computation:\n" );
		System.out.printf ( "  %f seconds.\n", wtime );
		/*
	  Terminate.
		 */
		System.out.printf ( "\n" );
		System.out.printf ( "MD_OPENMP\n" );
		System.out.printf ( "  Normal end of execution.\n" );

		System.out.printf ( "\n" );
		lc_omp.finish();
	}
	/******************************************************************************/

	static void compute ( int np, int nd, double pos[], double vel[], 
			double mass, double f[], double potkin[] )

	/******************************************************************************/
	/*
	  Purpose:

	    COMPUTE computes the forces and energies.

	  Discussion:

	    The computation of forces and energies is fully parallel.

	    The potential function V(X) is a harmonic well which smoothly
	    saturates to a maximum value at PI/2:

	      v(x) = ( sin ( min ( x, PI2 ) ) )**2

	    The derivative of the potential is:

	      dv(x) = 2.0 * sin ( min ( x, PI2 ) ) * cos ( min ( x, PI2 ) )
	            = sin ( 2.0 * min ( x, PI2 ) )

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    21 November 2007

	  Author:

	    Original FORTRAN77 version by Bill Magro.
	    C version by John Burkardt.

	  Parameters:

	    Input, int NP, the number of particles.

	    Input, int ND, the number of spatial dimensions.

	    Input, double POS[ND*NP], the position of each particle.

	    Input, double VEL[ND*NP], the velocity of each particle.

	    Input, double MASS, the mass of each particle.

	    Output, double F[ND*NP], the forces.

	    Output, double *POT, the total potential energy.

	    Output, double *KIN, the total kinetic energy.
	 */
	{
		double d;
		double d2;
		int i;
		int j;
		int k;
		double ke;
		double pe;
		double rij[]=new double[3];
		pe = 0.0;
		ke = 0.0;
		Boo w=new Boo();
		w.nd=nd; w.np=np; w.pos=pos; w.vel=vel; w.f=f; 
		lc_omp.work(w, 0, np-1, 1);
		//# pragma omp parallel shared ( f, nd, np, pos, vel ) private ( i, j, k, rij, d, d2 )
		/*# pragma omp for reduction ( + : pe, ke )
		for ( k = 0; k < np; k++ )
		{
	  //Compute the potential energy and forces.
			for ( i = 0; i < nd; i++ )
			{
				f[i+k*nd] = 0.0;
			}

			for ( j = 0; j < np; j++ )
			{
				if ( k != j )
				{
					d = dist ( nd, pos, k*nd, j*nd, rij );
	  //Attribute half of the potential energy to particle J.
					if ( d < PI2 )
					{
						d2 = d;
					}
					else
					{
						d2 = PI2;
					}

					pe = pe + 0.5 * Math.pow ( Math.sin ( d2 ), 2 );

					for ( i = 0; i < nd; i++ )
					{
						f[i+k*nd] = f[i+k*nd] - rij[i] * Math.sin ( 2.0 * d2 ) / d;
					}
				}
			}
	  //Compute the kinetic energy.
			for ( i = 0; i < nd; i++ )
			{
				ke = ke + vel[i+k*nd] * vel[i+k*nd];
			}
		}*/
		ke=w.getKE(); pe=w.getPE();
		ke = ke * 0.5 * mass;

		potkin[0] = pe;
		potkin[1] = ke;
	}
	static class Boo implements lc_omp.IWork {
		int nd, np;
		double f[], pos[], vel[];
		double kee=0, pee=0;
		public double getPE() {return pee;}
		public double getKE() {return kee;}
		public boolean evaluate(Work work) {
			double pe = 0.0;
			double ke = 0.0;
			int i, j;
			double d, d2;
			double rij[]=new double[3];
			for (int k = work.start; k <= work.end; k++ ) {
				/*
				  Compute the potential energy and forces.
				 */
				for ( i = 0; i < nd; i++ )
				{
					f[i+k*nd] = 0.0;
				}

				for ( j = 0; j < np; j++ )
				{
					if ( k != j )
					{
						d = dist ( nd, pos, k*nd, j*nd, rij );
						/*
				  Attribute half of the potential energy to particle J.
						 */
						if ( d < PI2 )
						{
							d2 = d;
						}
						else
						{
							d2 = PI2;
						}

						pe = pe + 0.5 * Math.pow ( Math.sin ( d2 ), 2 );

						for ( i = 0; i < nd; i++ )
						{
							f[i+k*nd] = f[i+k*nd] - rij[i] * Math.sin ( 2.0 * d2 ) / d;
						}
					}
				}
				/*
				  Compute the kinetic energy.
				 */
				for ( i = 0; i < nd; i++ )
				{
					ke = ke + vel[i+k*nd] * vel[i+k*nd];
				}
			}
			synchronized(lc_omp.critical()) {
				pee+=pe;
				kee+=ke;
			}
			return true;
		}
	}
	/******************************************************************************/

	static double dist ( int nd, double r1[], int index1, int index2, double dr[] )

	/******************************************************************************/
	/*
	  Purpose:

	    DIST computes the displacement (and its norm) between two particles.

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    21 November 2007

	  Author:

	    Original FORTRAN77 version by Bill Magro.
	    C version by John Burkardt.

	  Parameters:

	    Input, int ND, the number of spatial dimensions.

	    Input, double R1[ND], R2[ND], the positions of the particles.

	    Output, double DR[ND], the displacement vector.

	    Output, double D, the Euclidean norm of the displacement.
	 */
	{
		double d;
		int i;

		d = 0.0;
		for ( i = 0; i < nd; i++ )
		{
			dr[i] = r1[i+index1] - r1[i+index2];
			d = d + dr[i] * dr[i];
		}
		d = Math.sqrt ( d );

		return d;
	}
	/******************************************************************************/

	static void initialize ( int np, int nd, double box[], double pos[], 
			double vel[], double acc[] )

	/******************************************************************************/
	/*
	  Purpose:

	    INITIALIZE initializes the positions, velocities, and accelerations.

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    21 November 2007

	  Author:

	    Original FORTRAN77 version by Bill Magro.
	    C version by John Burkardt.

	  Parameters:

	    Input, int NP, the number of particles.

	    Input, int ND, the number of spatial dimensions.

	    Input, double BOX[ND], specifies the maximum position
	    of particles in each dimension.

	    Input, int *SEED, a seed for the random number generator.

	    Output, double POS[ND*NP], the position of each particle.

	    Output, double VEL[ND*NP], the velocity of each particle.

	    Output, double ACC[ND*NP], the acceleration of each particle.
	 */
	{
		int i;
		int j;
		/*
	  Give the particles random positions within the box.
		 */
		for ( i = 0; i < nd; i++ )
		{
			for ( j = 0; j < np; j++ )
			{
				pos[i+j*nd] = box[i] * r8_uniform_01 ();
			}
		}

		for ( j = 0; j < np; j++ )
		{
			for ( i = 0; i < nd; i++ )
			{
				vel[i+j*nd] = 0.0;
			}
		}
		for ( j = 0; j < np; j++ )
		{
			for ( i = 0; i < nd; i++ )
			{
				acc[i+j*nd] = 0.0;
			}
		}
		return;
	}
	/******************************************************************************/

	static double r8_uniform_01 ()

	/******************************************************************************/
	/*
	  Purpose:

	    R8_UNIFORM_01 is a unit pseudo-random R8.

	  Discussion:

	    This routine implements the recursion

	      seed = 16807 * seed mod ( 2**31 - 1 )
	      unif = seed / ( 2**31 - 1 )

	    The integer arithmetic never requires more than 32 bits,
	    including a sign bit.

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    11 August 2004

	  Author:

	    John Burkardt

	  Reference:

	    Paul Bratley, Bennett Fox, Linus Schrage,
	    A Guide to Simulation,
	    Springer Verlag, pages 201-202, 1983.

	    Bennett Fox,
	    Algorithm 647:
	    Implementation and Relative Efficiency of Quasirandom
	    Sequence Generators,
	    ACM Transactions on Mathematical Software,
	    Volume 12, Number 4, pages 362-376, 1986.

	  Parameters:

	    Input/output, int *SEED, a seed for the random number generator.

	    Output, double R8_UNIFORM_01, a new pseudo-random variate, strictly between
	    0 and 1.
	 */
	{
		int k;
		double r;

		k = seed / 127773;

		seed = 16807 * ( seed - k * 127773 ) - k * 2836;

		if ( seed < 0 )
		{
			seed = seed + 2147483647;
		}

		r = ( double ) ( seed ) * 4.656612875E-10;

		return r;
	}
	/******************************************************************************/

	static void update ( int np, int nd, double pos[], double vel[], double f[], 
			double acc[], double mass, double dt )

	/******************************************************************************/
	/*
	  Purpose:

	    UPDATE updates positions, velocities and accelerations.

	  Discussion:

	    The time integration is fully parallel.

	    A velocity Verlet algorithm is used for the updating.

	    x(t+dt) = x(t) + v(t) * dt + 0.5 * a(t) * dt * dt
	    v(t+dt) = v(t) + 0.5 * ( a(t) + a(t+dt) ) * dt
	    a(t+dt) = f(t) / m

	  Licensing:

	    This code is distributed under the GNU LGPL license. 

	  Modified:

	    17 April 2009

	  Author:

	    Original FORTRAN77 version by Bill Magro.
	    C version by John Burkardt.

	  Parameters:

	    Input, int NP, the number of particles.

	    Input, int ND, the number of spatial dimensions.

	    Input/output, double POS[ND*NP], the position of each particle.

	    Input/output, double VEL[ND*NP], the velocity of each particle.

	    Input, double F[ND*NP], the force on each particle.

	    Input/output, double ACC[ND*NP], the acceleration of each particle.

	    Input, double MASS, the mass of each particle.

	    Input, double DT, the time step.
	 */
	{
		int i;
		int j;
		double rmass;

		rmass = 1.0 / mass;
		Foo w=new Foo();
		w.nd=nd; w.pos=pos; w.vel=vel; w.f=f; w.acc=acc; w.rmass=1/mass; w.dt=dt;
		lc_omp.work(w, 0, np-1, 1);
		//# pragma omp parallel shared ( acc, dt, f, nd, np, pos, rmass, vel ) private ( i, j )
		/*# pragma omp for
	  for ( j = 0; j < np; j++ )
	  {
	    for ( i = 0; i < nd; i++ )
	    {
	      pos[i+j*nd] = pos[i+j*nd] + vel[i+j*nd] * dt + 0.5 * acc[i+j*nd] * dt * dt;
	      vel[i+j*nd] = vel[i+j*nd] + 0.5 * dt * ( f[i+j*nd] * rmass + acc[i+j*nd] );
	      acc[i+j*nd] = f[i+j*nd] * rmass;
	    }
	  }*/
	}
	static class Foo implements lc_omp.IWork {
		int nd; double pos[]; double vel[]; double f[]; 
		double acc[]; double rmass; double dt;
		public boolean evaluate(Work work) {
			for (int j = work.start; j <= work.end; j++ )
			{
				for (int i = 0; i < nd; i++ )
				{
					pos[i+j*nd] = pos[i+j*nd] + vel[i+j*nd] * dt + 0.5 * acc[i+j*nd] * dt * dt;
					vel[i+j*nd] = vel[i+j*nd] + 0.5 * dt * ( f[i+j*nd] * rmass + acc[i+j*nd] );
					acc[i+j*nd] = f[i+j*nd] * rmass;
				}
			}
			return true;
		}
	}
}
