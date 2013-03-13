import java.math.BigDecimal;

import mpi.*;

public class test {

	public static void test1(String args[]) {
	      MPI.Init(args);
	      System.out.println("computer="+MPI.Get_processor_name());
	      System.out.println("time="+MPI.Wtime());
	      System.out.println("tick="+MPI.Wtick());
	      System.out.println("cpus"+MPI.NUM_OF_PROCESSORS);
	      System.out.println("rank="+MPI.COMM_WORLD.Rank());
	      System.out.println("size="+MPI.COMM_WORLD.Size());
	      Datatype d=MPI.FLOAT;
	      System.out.println("float size="+d.Size());
	      System.out.println("float extent="+d.Extent());
	      System.out.println("float lb="+d.Lb());
	      System.out.println("float ub="+d.Ub());
	      System.out.println("float toString="+d);
	      double c_rbuf[]=new double[20];
	      double c_sbuf[]=new double[20];
	      BigDecimal bd_sbuf[]=new BigDecimal[2];
	      BigDecimal bd_rbuf[]=new BigDecimal[2];
	      bd_sbuf[1]=new BigDecimal("123456789123456789123456789");
	      double dt;
	      int i;
	      c_sbuf[0]=56.75;
	      if (MPI.COMM_WORLD.Rank()==0) { 
	          MPI.COMM_WORLD.Send (c_sbuf, 0,1, MPI.DOUBLE, 1, 55);
	          MPI.COMM_WORLD.Send (bd_sbuf, 1,1, MPI.OBJECT, 1, 55);
	          c_sbuf[0]=-98.125;
	          MPI.COMM_WORLD.Bcast(c_sbuf, 0,1, MPI.DOUBLE, 0);
	          c_sbuf[0]=33;
	          MPI.COMM_WORLD.Isend(c_sbuf, 0,1, MPI.DOUBLE, 1, 66);
	          c_sbuf[1]=34;
	          MPI.COMM_WORLD.Isend(c_sbuf, 1,1, MPI.DOUBLE, 1, 77);
	          dt=MPI.Wtime();
	          for (i=0; i<10000; i++) MPI.COMM_WORLD.Send (c_sbuf, 0,1, MPI.DOUBLE, 1, 88);
	          System.out.println("10000 Send time="+(MPI.Wtime()-dt));
	          dt=MPI.Wtime();
	          for (i=0; i<10000; i++) MPI.COMM_WORLD.Ssend (c_sbuf, 0,1, MPI.DOUBLE, 1, 88);
	          System.out.println("10000 SSend time="+(MPI.Wtime()-dt));
	          for (i=0; i<c_sbuf.length; i++) c_sbuf[i]=i+1;
	          MPI.COMM_WORLD.Bcast(c_sbuf, 0, c_sbuf.length, MPI.DOUBLE, 0);
	          MPI.COMM_WORLD.Reduce(c_sbuf, 0, c_rbuf, 0, c_sbuf.length, MPI.DOUBLE, MPI.SUM, 1);
	          MPI.COMM_WORLD.Scan(c_sbuf, 0, c_rbuf, 0, c_sbuf.length, MPI.DOUBLE, MPI.SUM);
	          dt=0;
	          for (i=0; i<c_rbuf.length; i++) dt+=c_rbuf[i];
	          System.out.println(dt==(c_rbuf.length*(c_rbuf.length+1)/2)?"scan 0 + ok":"scan 0 + not ok");
	          for (i=0; i<c_sbuf.length; i++) c_sbuf[i]=i+1;
	          MPI.COMM_WORLD.Gather(c_sbuf, 10, 10, MPI.DOUBLE, c_rbuf, 0, 10, MPI.DOUBLE, 0);
	          dt=0;
	          for (i=0; i<c_rbuf.length; i++) dt+=c_rbuf[i];
	          if (c_rbuf[c_rbuf.length/2]!=1) System.out.println("gather 0b not ok");
	          System.out.println(dt==(c_rbuf.length*(c_rbuf.length+1)/2)?"gather 0 + ok":"gather 0 + not ok");
	          MPI.COMM_WORLD.Scatter(c_sbuf, 0, 1, MPI.DOUBLE, c_rbuf, 0, 1, MPI.DOUBLE, 1);
	          System.out.println(c_rbuf[0]!=33?"scatter 0 not ok":"scatter 0 ok");
	      } else {
	    	  MPI.COMM_WORLD.Recv (c_rbuf, 0,1, MPI.DOUBLE, 0, 55);
	    	  System.out.println(c_rbuf[0]==56.75?"S/R ok":"S/R failed");
	    	  Status s=MPI.COMM_WORLD.Recv (bd_rbuf, 0,2, MPI.OBJECT, 0, 55);
	    	  System.out.println(s.numEls);
	    	  System.out.println(bd_rbuf[0].compareTo(bd_sbuf[1])==0?"object S/R ok":"object S/R failed");
	    	  MPI.COMM_WORLD.Bcast(c_rbuf, 0,1, MPI.DOUBLE, 0);
	    	  System.out.println(c_rbuf[0]==-98.125?"bcast ok":"bcast failed");
	    	  MPI.COMM_WORLD.Recv (c_rbuf, 0,1, MPI.DOUBLE, 0, 77);
	    	  System.out.println(c_rbuf[0]==34?"out of order S/R ok":"out of order S/R failed");
	    	  MPI.COMM_WORLD.Recv (c_rbuf, 1,1, MPI.DOUBLE, 0, 66);
	    	  System.out.println(c_rbuf[1]==33?"IS/R ok":"IS/R failed");
	    	  MPI.COMM_WORLD.Recv (c_rbuf, 1,1, MPI.DOUBLE, 0, 88);
	    	  dt=MPI.Wtime();
	    	  for (i=0; i<9999; i++) MPI.COMM_WORLD.Recv (c_rbuf, 1,1, MPI.DOUBLE, 0, 88);
	    	  System.out.println("9999 receive time="+(MPI.Wtime()-dt));
	    	  MPI.COMM_WORLD.Recv (c_rbuf, 1,1, MPI.DOUBLE, 0, 88);
	    	  dt=MPI.Wtime();
	    	  for (i=0; i<9999; i++) MPI.COMM_WORLD.Recv (c_rbuf, 1,1, MPI.DOUBLE, 0, 88);
	    	  System.out.println("9999 receive time="+(MPI.Wtime()-dt));
	          MPI.COMM_WORLD.Bcast(c_rbuf, 0, c_rbuf.length, MPI.DOUBLE, 0);
	          for (i=0; i<c_rbuf.length; i++) if (c_rbuf[i]!=(i+1)) {System.out.println("bcast failed at "+i); break;}
	          if (i>=c_rbuf.length) System.out.println("bcast ok");
	          for (i=0; i<c_sbuf.length; i++) c_sbuf[i]=i+1;
	          MPI.COMM_WORLD.Reduce(c_sbuf, 0, c_rbuf, 0, c_rbuf.length, MPI.DOUBLE, MPI.SUM, 1);
	          dt=0;
	          for (i=0; i<c_rbuf.length; i++) dt+=c_rbuf[i];
	          System.out.println(dt==(c_rbuf.length*(c_rbuf.length+1))?"reduce + ok":"reduce + not ok");
	          MPI.COMM_WORLD.Scan(c_sbuf, 0, c_rbuf, 0, c_sbuf.length, MPI.DOUBLE, MPI.SUM);
	          dt=0;
	          for (i=0; i<c_rbuf.length; i++) dt+=c_rbuf[i];
	          System.out.println(dt==(c_rbuf.length*(c_rbuf.length+1))?"scan 1 + ok":"scan 1 + not ok");
	          for (i=0; i<c_sbuf.length; i++) {c_sbuf[i]=i+1; c_rbuf[i]=0;}
	          MPI.COMM_WORLD.Gather(c_sbuf, 0, 10, MPI.DOUBLE, c_rbuf, 0, 10, MPI.DOUBLE, 0);
	          dt=0;
	          for (i=0; i<c_rbuf.length; i++) dt+=c_rbuf[i];
	          System.out.println(dt==0?"gather 1 ok":"gather 1 not ok");
	          c_sbuf[0]=33;
	          c_rbuf[0]=0; c_rbuf[1]=0;
	          MPI.COMM_WORLD.Scatter(c_sbuf, 0, 1, MPI.DOUBLE, c_rbuf, 1, 1, MPI.DOUBLE, 1);
	          for (i=0; i<5; i++) System.out.println(c_rbuf[i]);
	          if ((c_rbuf[0]!=0)||(c_rbuf[1]!=2)) System.out.println("scatter 1 not ok");
	      }
	      MPI.Finalize();
	}

	public static void main(String args[]) {
		test1(args);
	}
}