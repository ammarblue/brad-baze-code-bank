package reuze.pending;

import reuze.pending.lc_omp.Work;

public class demoOMPPrime implements lc_omp.IWork {
	public static void main(String args[]) {
		demoOMPPrime c=new demoOMPPrime();
		if (args.length<=1) args=new String[]{"3","100000000"};//"533000389"};
		System.out.println("Range to check for Primes: "+args[0]+" - "+args[1]);
		double time=lc_omp.omp_get_wtime();
		//c.compute(args);
		lc_omp.work(c, 3, Integer.parseInt(args[1]), 2, 1, 1, lc_omp.DYNAMIC, 100);//4000);
		System.out.printf("time=%gs\n", lc_omp.omp_get_wtime()-time);
		System.out.printf("Program Done. %d primes found\n",c.number_of_primes);
		System.out.printf("Number of 4n+1 primes found: %d\n",c.number_of_41primes);
		System.out.printf("Number of 4n-1 primes found: %d\n",c.number_of_43primes);
		lc_omp.finish();
	}
	int start, end;          /* range of numbers to search */
	int number_of_primes=1;  /* number of primes found */
	int number_of_41primes=0;/* number of 4n+1 primes found */
	int number_of_43primes=0;/* number of 4n-1 primes found */
	int print_primes=0;      /* should each prime be printed? */

		public boolean evaluate(Work w) {
			int j;
			int limit;
			int prime;
			int number_of_primesX=0;  /* number of primes found */
			   int number_of_41primesX=0;/* number of 4n+1 primes found */
			   int number_of_43primesX=0;/* number of 4n-1 primes found */
			   //System.out.println(w.start+" "+w.end);
			for (int  i =w.start; i <=w.end; i += 2) {
		      limit = (int) Math.sqrt((float)i) + 1;
		      prime = 1; // assume number is prime 
		      j = 3;
		      while (prime!=0 && (j <= limit)) {
		         if (i%j == 0) prime = 0;
		         j += 2;
		      }
		      if (prime!=0) {
		        if (print_primes!=0) System.out.printf("%d ",i);
		        number_of_primesX++;
		        if (i%4 == 1) number_of_41primesX++;
		        if (i%4 == 3) number_of_43primesX++;
		      }
			} //for
			//System.out.println("***"+w.start+" "+w.end+" "+number_of_primesX+" "+number_of_41primesX+" "+number_of_43primesX);
			synchronized(lc_omp.critical()) {
				number_of_primes+=number_of_primesX;
				number_of_41primes+=number_of_41primesX;
				number_of_43primes+=number_of_43primesX;
			}
			return true;
		}
		public void compute(String args[]){
			int j, limit;
			int prime;               /* is the number prime? */
			start = Integer.parseInt(args[0]);   end = Integer.parseInt(args[1]);
			if ((start % 2)==0) start++;
			if (args.length == 3 && Integer.parseInt(args[2]) != 0) print_primes = 1;
			if (start==1 || start==2) {
				if (print_primes!=0) System.out.printf("2 ");
				number_of_primes++; start=3;
			}
			for (int i = start; i <= end; i += 2) {
				limit = (int) Math.sqrt((float)i) + 1;
				prime = 1; // assume number is prime 
				j = 3;
				while (prime!=0 && (j <= limit)) {
					if (i%j == 0) prime = 0;
					j += 2;
				}
				if (prime!=0) {
					if (print_primes!=0) System.out.printf("%d ",i);
					number_of_primes++;
					if (i%4 == 1) number_of_41primes++;
					if (i%4 == 3) number_of_43primes++;
				}
			}
		}
}
