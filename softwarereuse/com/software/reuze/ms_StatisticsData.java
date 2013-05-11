package com.software.reuze;
//package com.example.android.notepad2;

import java.util.Vector;


public class ms_StatisticsData { 
   public static double max=-Double.MAX_VALUE,min=Double.MAX_VALUE;
   public static ms_StatisticsFrequency frequency(Vector v) {
	   return new ms_StatisticsFrequency(v);
   }
   public static m_Regression regression(Vector v, Vector z) {
	   return new m_Regression(v,z);
   }
    /**
     * The sum of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.</p>
     * 
     * @param values the input array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the sum of the values or Double.NaN if length = 0
     * @throws IllegalArgumentException if the array is null or the array index
     *  parameters are not valid
     */
    private static double[] get(Vector<m_i_Number> v) {
    	int t=v.size();
    	double xf[]=new double[t];
		while (--t>=0) {
			xf[t]=v.elementAt(t).doubleValue();
		}
		return xf;
    }
    public static double sum(Vector v) {
    	final double[] values=get(v);
        max=-Double.MAX_VALUE;
        min=Double.MAX_VALUE;
        double sum = 0.0;
        int n=values.length;
            for (int i = 0; i < n; i++) {
                sum += values[i];
                if (values[i]>max) max=values[i];
                if (values[i]<min) min=values[i];
            }
        return sum;
    }
    
    public static double lnsum(Vector v) {
    	final double[] values=get(v);
        double sum = 0.0;
        int n=values.length;
            for (int i = 0; i < n; i++) {
                sum += Math.log(values[i]);
            }
        return sum;
    }

    public static double max(Vector values) {
      if (max==-Double.MAX_VALUE) sum(values);
      return max;
    }

    public static double min(Vector values) {
      if (min==Double.MAX_VALUE) sum(values);
      return min;
    }
    /**
     * Returns the arithmetic mean of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.</p>
     * <p>
     * See {@link Mean} for details on the computing algorithm.</p>
     * 
     * @param values the input array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the mean of the values or Double.NaN if length = 0
     * @throws IllegalArgumentException if the array is null or the array index
     *  parameters are not valid
     */
    public static double mean(Vector v) {
    	    final double[] values=get(v);
            int n=values.length;
            double sampleSize = n;
       if (n!=0) {     
            // Compute initial estimate using definitional formula
            double xbar = sum(v) / sampleSize;
            
            // Compute correction factor in second pass
            double correction = 0;
            for (int i = 0; i < n; i++) {
                correction += (values[i] - xbar);
            }
            return xbar + (correction/sampleSize);
        }
        return Double.NaN;
    }
    public static double geomean(Vector v) { //values must be positive
        int n=v.size();
        double sampleSize = n;
        if (n!=0) {     
        	return Math.exp(lnsum(v) / sampleSize);
        }
        return Double.NaN;
    }
    /**
     * Returns the variance of the entries in the specified portion of
     * the input array, using the precomputed mean value.  Returns 
     * <code>Double.NaN</code> if the designated subarray is empty.
     * <p>
     * See {@link Variance} for details on the computing algorithm.</p>
     * <p>
     * The formula used assumes that the supplied mean value is the arithmetic
     * mean of the sample data, not a known population parameter.  This method
     * is supplied only to save computation when the mean has already been
     * computed.</p>
     * <p>
     * Returns 0 for a single-value (i.e. length = 1) sample.</p>
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.</p>
     * <p>
     * Does not change the internal state of the statistic.</p>
     * 
     * @param values the input array
     * @param mean the precomputed mean value
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the variance of the values or Double.NaN if length = 0
     * @throws IllegalArgumentException if the array is null or the array index
     *  parameters are not valid
     */
    public static double variance(Vector v, final double mean) {
    	final double[] values=get(v);
        double var = Double.NaN;
        int length=values.length;
            if (length == 1) {
                var = 0.0;
            } else if (length > 1) {
                double accum = 0.0;
                double dev = 0.0;
                double accum2 = 0.0;
                for (int i = 0; i < length; i++) {
                    dev = values[i] - mean;
                    accum += dev * dev;
                    accum2 += dev;
                }
                double len = length;            
                if (false/*isBiasCorrected*/) {
                    var = (accum - (accum2 * accum2 / len)) / (len - 1.0);
                } else {
                    var = (accum - (accum2 * accum2 / len)) / len;
                }
            }
        return var;
    }

    /**
     * Returns the Standard Deviation of the entries in the input array, or 
     * <code>Double.NaN</code> if the array is empty.
     * <p>
     * Returns 0 for a single-value (i.e. length = 1) sample.</p>
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.</p>
     * <p>
     * Does not change the internal state of the statistic.</p>
     * 
     * @param values the input array
     * @return the standard deviation of the values or Double.NaN if length = 0
     * @throws IllegalArgumentException if the array is null
     */  
    public static double std(Vector v, final double mean)  {
        return Math.sqrt(variance(v,mean));
    }

    public static double median(Vector v) {
      double[] values=get(v);
      java.util.Arrays.sort(values);
      int i=values.length;
      if ((i%2)==0) return (values[i/2]+values[i/2-1])/2.0;
      else return values[i/2];
    }

	/**
     * <p>
     * Gets the greatest common divisor of the absolute value of two numbers,
     * using the "binary gcd" method which avoids division and modulo
     * operations. See Knuth 4.5.2 algorithm B. This algorithm is due to Josef
     * Stein (1961).
     * </p>
     * Special cases:
     * <ul>
     * <li>The invocations
     * <code>gcd(Integer.MIN_VALUE, Integer.MIN_VALUE)</code>,
     * <code>gcd(Integer.MIN_VALUE, 0)</code> and
     * <code>gcd(0, Integer.MIN_VALUE)</code> throw an
     * <code>ArithmeticException</code>, because the result would be 2^31, which
     * is too large for an int value.</li>
     * <li>The result of <code>gcd(x, x)</code>, <code>gcd(0, x)</code> and
     * <code>gcd(x, 0)</code> is the absolute value of <code>x</code>, except
     * for the special cases above.
     * <li>The invocation <code>gcd(0, 0)</code> is the only one which returns
     * <code>0</code>.</li>
     * </ul>
     * 
     * @param p any number
     * @param q any number
     * @return the greatest common divisor, never negative
     * @throws ArithmeticException
     *             if the result cannot be represented as a nonnegative int
     *             value
     * @since 1.1
     */
    public static int gcd(final int p, final int q) {
        int u = p;
        int v = q;
        if ((u == 0) || (v == 0)) {
            if ((u == Integer.MIN_VALUE) || (v == Integer.MIN_VALUE)) {
                return 0;
            }
            return (Math.abs(u) + Math.abs(v));
        }
        // keep u and v negative, as negative integers range down to
        // -2^31, while positive numbers can only be as large as 2^31-1
        // (i.e. we can't necessarily negate a negative number without
        // overflow)
        /* assert u!=0 && v!=0; */
        if (u > 0) {
            u = -u;
        } // make u negative
        if (v > 0) {
            v = -v;
        } // make v negative
        // B1. [Find power of 2]
        int k = 0;
        while ((u & 1) == 0 && (v & 1) == 0 && k < 31) { // while u and v are
			// both even...
            u /= 2;
            v /= 2;
            k++; // cast out twos.
        }
        if (k == 31) {
            return 0;
        }
        // B2. Initialize: u and v have been divided by 2^k and at least
        // one is odd.
        int t = ((u & 1) == 1) ? v : -(u / 2)/* B3 */;
        // t negative: u was odd, v may be even (t replaces v)
        // t positive: u was even, v is odd (t replaces u)
        do {
            /* assert u<0 && v<0; */
            // B4/B3: cast out twos from t.
            while ((t & 1) == 0) { // while t is even..
                t /= 2; // cast out twos
            }
            // B5 [reset max(u,v)]
            if (t > 0) {
                u = -t;
            } else {
                v = t;
            }
            // B6/B3. at this point both u and v should be odd.
            t = (v - u) / 2;
            // |u| larger: t positive (replace u)
            // |v| larger: t negative (replace v)
        } while (t != 0);
        return -u * (1 << k); // gcd is u*2^k
    }

	/**
     * <p>
     * Returns the least common multiple of the absolute value of two numbers,
     * using the formula <code>lcm(a,b) = (a / gcd(a,b)) * b</code>.
     * </p>
     * Special cases:
     * <ul>
     * <li>The invocations <code>lcm(Integer.MIN_VALUE, n)</code> and
     * <code>lcm(n, Integer.MIN_VALUE)</code>, where <code>abs(n)</code> is a
     * power of 2, throw an <code>ArithmeticException</code>, because the result
     * would be 2^31, which is too large for an int value.</li>
     * <li>The result of <code>lcm(0, x)</code> and <code>lcm(x, 0)</code> is
     * <code>0</code> for any <code>x</code>.
     * </ul>
     * 
     * @param a any number
     * @param b any number
     * @return the least common multiple, never negative
     * @throws ArithmeticException
     *             if the result cannot be represented as a nonnegative int
     *             value
     * @since 1.1
     */
    public static int lcm(int a, int b) {
        if (a==0 || b==0){
            return 0;
        }
        int lcm = Math.abs(mulAndCheck(a / gcd(a, b), b));
        //if (lcm == Integer.MIN_VALUE){return 0;}
        return lcm;
    }

	/**
     * Multiply two integers, checking for overflow.
     * 
     * @param x a factor
     * @param y a factor
     * @return the product <code>x*y</code>
     * @throws ArithmeticException if the result can not be represented as an
     *         int
     * @since 1.1
     */
    private static int mulAndCheck(int x, int y) {
        long m = ((long)x) * ((long)y);
        if (m < Integer.MIN_VALUE || m > Integer.MAX_VALUE) {
            return 0;
        }
        return (int)m;
    }

	/** All long-representable factorials */
    private static final long[] factorials = new long[] 
	{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800,
	479001600, 6227020800l, 87178291200l, 1307674368000l, 20922789888000l,
	355687428096000l, 6402373705728000l, 121645100408832000l,
	2432902008176640000l};
	
    /**
     * Returns n!. Shorthand for <code>n</code> <a
     * href="http://mathworld.wolfram.com/Factorial.html"> Factorial</a>, the
     * product of the numbers <code>1,...,n</code>.
     * <p>
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>n >= 0</code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * <li> The result is small enough to fit into a <code>long</code>. The
     * largest value of <code>n</code> for which <code>n!</code> <
     * Long.MAX_VALUE</code> is 20. If the computed value exceeds <code>Long.MAX_VALUE</code>
     * an <code>ArithMeticException </code> is thrown.</li>
     * </ul>
     * </p>
     * 
     * @param n argument
     * @return <code>n!</code>
     * @throws ArithmeticException if the result is too large to be represented
     *         by a long integer.
     * @throws IllegalArgumentException if n < 0
     */
    public static int factorial(final int n) {
        if (n < 0) {
            return 0;
        }
        if (n > 12) {
            return 0;
        }
        return (int)factorials[n];
    }
	
    /**
     * Returns n!. Shorthand for <code>n</code> <a
     * href="http://mathworld.wolfram.com/Factorial.html"> Factorial</a>, the
     * product of the numbers <code>1,...,n</code> as a <code>double</code>.
     * <p>
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>n >= 0</code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * <li> The result is small enough to fit into a <code>double</code>. The
     * largest value of <code>n</code> for which <code>n!</code> <
     * Double.MAX_VALUE</code> is 170. If the computed value exceeds
     * Double.MAX_VALUE, Double.POSITIVE_INFINITY is returned</li>
     * </ul>
     * </p>
     * 
     * @param n argument
     * @return <code>n!</code>
     * @throws IllegalArgumentException if n < 0
     */
    public static double dfactorial(final int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 21) {
            return factorials[n];
        }
        return Math.floor(Math.exp(factorialLog(n)) + 0.5);
    }

	/**
     * Returns the natural logarithm of n!.
     * <p>
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>n >= 0</code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * </ul></p>
     * 
     * @param n argument
     * @return <code>n!</code>
     * @throws IllegalArgumentException if preconditions are not met.
     */
    public static double factorialLog(final int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 21) {
            return Math.log(factorials[n]);
        }
        double logSum = 0;
        for (int i = 2; i <= n; i++) {
            logSum += Math.log(i);
        }
        return logSum;
    }

	/**
     * Computes the covariance between the two arrays.
     * 
     * <p>Array lengths must match and the common length must be at least 2.</p>
     *
     * @param xArray first data array
     * @param yArray second data array
     * @param biasCorrected if true, returned value will be bias-corrected 
     * @return returns the covariance for the two arrays 
     * @throws  IllegalArgumentException if the arrays lengths do not match or
     * there is insufficient data
     */
    public static double covariance(Vector x, Vector y/*, boolean biasCorrected*/) {
    	final double[] xArray=get(x);
    	final double[] yArray=get(y);
        double result = 0d;
        int length = xArray.length;
        if (length == yArray.length && length > 1) {
            double xMean = mean(x);
            double yMean = mean(y);
            for (int i = 0; i < length; i++) {
                double xDev = xArray[i] - xMean;
                double yDev = yArray[i] - yMean;
                result += (xDev * yDev - result) / (i + 1);
            }
            return /*biasCorrected ? result * ((double) length / (double)(length - 1)) :*/ result;
        }
        return Double.NaN;
    }
    
    public static double pearsons(Vector x, Vector y/*, boolean biasCorrected*/) {
    	return covariance(x,y)/std(x,mean(x))/std(y,mean(y));
    }
	
/*    public static void main(String[] args) { 
      double t[]={5,3,1,2,4};
      double x=mean(t);
      System.out.println("mean "+x);
      System.out.println("max "+max);
      System.out.println("min "+min);
      System.out.println("median "+median(t));
      System.out.println("variance "+variance(t,x));
      System.out.println("std "+std(t,x));
      Frequency f=new Frequency();
      f.addValue(0.2+0.1);
      f.addValue(0.3);
      f.addValue(5.001);
      f.addValue(4.3);
      System.out.println(f);
      System.out.println(lcm(9*13,15*13));
      System.out.println(gcd(9*13,15*13));
	  Regression r=new Regression();
	  r.addValue(1,9);  //3x+6
		r.addValue(3,15);
		r.addValue(5,21);
		r.addValue(9,33);
	  System.out.println(r.getSlope()+"x"+r.getIntercept());
    }
 */
}

