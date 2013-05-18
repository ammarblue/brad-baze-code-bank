
import java.util.Random;


public class ms_StatisticsDistributions {
	Random r;
	public ms_StatisticsDistributions() {
		r=new Random();
	}
	public ms_StatisticsDistributions(long seed) {
		r=new Random(seed);
	}
	/*repeated calls with variable r as a randomStream input will produce 
    an exponential distribution with mean as the user specifies.
    A single random number is produced per call.  */
	public static double Exponential(double mean, Random r){
		double rr = r.nextDouble();
		if (rr == 0.0) rr=0.0000001;
		return -mean * Math.log(rr);
	}	  
	/* Given a probability of Success and a random stream to produce random 
	     numbers this procedure returns TRUE for success in a Bernoulli trial */
	public static boolean Flip(double probabilityOfSuccess, Random r){
		return r.nextDouble() < probabilityOfSuccess;
	}
	  /*Given a probability of Success and a random stream to produce random
	    numbers this procedure returns the number of steps before a success*/
	public static int Geometric(double probabilityOfSuccess, Random r){
	  int n=0;
	  if (probabilityOfSuccess > 0.1) {
	    do { n++; } while (r.nextDouble() >= probabilityOfSuccess);
	    return n;
	  }
	  n = (int) (Math.log(r.nextDouble())/Math.log(1.0-probabilityOfSuccess));
	  return n;
	}
	/*Input a random stream to produce random numbers and this procedure produces
    a normal distribution with the user specified mean and standard deviation 
    when called repeatedly.  A single random number is produced per call.*/
	public static double Normal(double mean, double std, Random r){
	  double rr = r.nextDouble();
	  if (rr == 0.0) rr=0.0000001;
	  return mean + std*Math.sqrt(Math.log(rr)*(-2))*Math.sin(r.nextDouble()*Math.PI*2);
	}
	/*With a random stream to produce random numbers this procedure returns the 
    number of arrivals for a Poisson process with the user specified arrival 
    rate and time interval*/
	public static int Poisson(double arrivalRate, double timeInterval, Random r){
	  int n=0;
	  double t=1.0/Math.exp(arrivalRate*timeInterval);
	  double z=r.nextDouble();
	  while (z >= t) {
	    n++;
	    z=r.nextDouble()*z;
	}
	  return n;
	}
	
	/*With an input of a random stream to produce random numbers and a limit
    of type int, this procedure will produce a uniform distribution over
    the interval (0..limit) when called repeatedly.  One random number per
    call is produced.*/
	public static int Interval(int limit, Random r){
		return (int) (r.nextDouble()*(((double)limit)-0e-32));
	}
	/*With an input of a random stream to produce random numbers and a limit
    of type long, this procedure will produce a uniform distribution over
    the interval (0..limit) when called repeatedly.  One random number per
    call is produced.*/ 
	public static long Interval(long limit, Random r) {
		return (long) (r.nextDouble()*(((double)limit)-0e-32));
	}
	public static float Interval(float limit, Random r) {
		return (float) (r.nextDouble()*(((double)limit)-0e-32));
	}
	public static double Interval(double limit, Random r) {
		return r.nextDouble()*(((double)limit)-0e-32);
	}
	public double Exponential(double mean){
		return Exponential(mean, r);
	}
	public boolean Flip(double probabilityOfSuccess){
		return Flip(probabilityOfSuccess, r);
	}
	public int Geometric(double probabilityOfSuccess){
		return Geometric(probabilityOfSuccess, r);
	}
	double Normal(double mean, double std){
		return Normal(mean, std, r);
	}
	public int Poisson(double arrivalRate, double timeInterval){
		return Poisson(arrivalRate, timeInterval, r);
	} 
	public int Interval(int limit){
		return Interval(limit, r);
	} 
	public long Interval(long limit) {
		return Interval(limit, r);
	}
	public float Interval(float limit) {
		return Interval(limit, r);
	}
	public double Interval(double limit) {
		return Interval(limit, r);
	} 
}
