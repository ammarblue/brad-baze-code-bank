package com.software.reuze;
/*
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

//package toxi.math;

import java.util.Random;

/**
 * Miscellaneous math utilities.
 */
public final class m_MathUtils {

    /**
     * Square root of 2
     */
    public static final float SQRT2 = (float) Math.sqrt(2);

    /**
     * Square root of 3
     */
    public static final float SQRT3 = (float) Math.sqrt(3);

    /**
     * Log(2)
     */
    public static final float LOG2 = (float) Math.log(2);

    /**
     * PI
     */
    public static final float PI = 3.14159265358979323846f;

    /**
     * The reciprocal of PI: (1/PI)
     */
    public static final float INV_PI = 1f / PI;

    /**
     * PI/2
     */
    public static final float HALF_PI = PI / 2;

    /**
     * PI/3
     */
    public static final float THIRD_PI = PI / 3f;

    /**
     * PI/4
     */
    public static final float QUARTER_PI = PI / 4f;

    /**
     * PI*2
     */
    public static final float TWO_PI = PI * 2f;

    /**
     * PI*1.5
     */
    public static final float THREE_HALVES_PI = TWO_PI - HALF_PI;

    /**
     * PI*PI
     */
    public static final float PI_SQUARED = PI * PI;

    /**
     * Epsilon value
     */
    public static final float EPS = 1.1920928955078125E-7f;

    /**
     * Degrees to radians conversion factor
     */
    public static final float DEG2RAD = PI / 180;

    /**
     * Radians to degrees conversion factor
     */
    public static final float RAD2DEG = 180 / PI;

	static private final int SIN_BITS = 13; // Adjust for accuracy.
	static private final int SIN_MASK = ~(-1 << SIN_BITS);
	static private final int SIN_COUNT = SIN_MASK + 1;

	static private final float radFull = PI * 2;
	static private final float degFull = 360;
	static private final float radToIndex = SIN_COUNT / radFull;
	static private final float degToIndex = SIN_COUNT / degFull;

	static public final float radiansToDegrees = 180f / PI;
	static public final float degreesToRadians = PI / 180;

    private static final float SHIFT23 = 1 << 23;
    private static final float INV_SHIFT23 = 1.0f / SHIFT23;

    private final static double SIN_A = -4d / (PI * PI);
    private final static double SIN_B = 4d / PI;
    private final static double SIN_P = 9d / 40;
	static private final int BIG_ENOUGH_INT = 16 * 1024;
	static private final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
	static private final double CEIL = 0.9999999;
	static private final double BIG_ENOUGH_CEIL = Double.longBitsToDouble(Double.doubleToLongBits(BIG_ENOUGH_INT + 1) - 1);
	static private final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5f;

    /**
     * Default random number generator used by random methods of this class
     * which don't use a passed in {@link Random} instance.
     */
    public static Random RND = new Random();

    /**
     * @param x
     * @return absolute value of x
     */
    public static final double abs(double x) {
        return x < 0 ? -x : x;
    }

    /**
     * @param x
     * @return absolute value of x
     */
    public static final float abs(float x) {
        return x < 0 ? -x : x;
    }

    /**
     * @param x
     * @return absolute value of x
     */
    public static final int abs(int x) {
        int y = x >> 31;
        return (x ^ y) - y;
    }
	/**
	 * Apply a bias to a number in the unit interval, moving numbers towards 0 or 1
	 * according to the bias parameter.
	 * @param a the number to bias
	 * @param b the bias parameter. 0.5 means no change, smaller values bias towards 0, larger towards 1.
	 * @return the output value
	 */
	public static float bias(float a, float b) {
//		return (float)Math.pow(a, Math.log(b) / Math.log(0.5));
		return a/((1.0f/b-2)*(1.0f-a)+1);
	}
	/**
	 * Returns the smallest integer greater than or equal to the specified float. This method will only properly ceil floats from
	 * -(2^14) to (Float.MAX_VALUE - 2^14).
	 */
	static public int ceil (float x) {
		return (int)(x + BIG_ENOUGH_CEIL) - BIG_ENOUGH_INT;
	}

	/**
	 * Returns the smallest integer greater than or equal to the specified float. This method will only properly ceil floats that
	 * are positive.
	 */
	static public int ceilPositive (float x) {
		return (int)(x + CEIL);
	}
    /**
     * Rounds up the value to the nearest higher power^2 value.
     * 
     * @param x
     * @return power^2 value
     */
    public static final int ceilPowerOf2(int x) {
        int pow2 = 1;
        while (pow2 < x) {
            pow2 <<= 1;
        }
        return pow2;
    }
    /**
	 * A "circle up" function. Returns y on a unit circle given 1-x. Useful for forming bevels.
	 * @param x the input parameter in the range 0..1
	 * @return the output value
	 */
	public static float circleUp(float x) {
		x = 1-x;
		return (float)Math.sqrt(1-x*x);
	}

	/**
	 * A "circle down" function. Returns 1-y on a unit circle given x. Useful for forming bevels.
	 * @param x the input parameter in the range 0..1
	 * @return the output value
	 */
	public static float circleDown(float x) {
		return 1.0f-(float)Math.sqrt(1-x*x);
	}

	/**
	 * Clamp a value to an interval.
	 * @param a the lower clamp threshold
	 * @param b the upper clamp threshold
	 * @param x the input parameter
	 * @return the clamped value
	 */
	public static float clamp(float x, float a, float b) {
		return (x < a) ? a : (x > b) ? b : x;
	}

	/**
	 * Clamp a value to an interval.
	 * @param a the lower clamp threshold
	 * @param b the upper clamp threshold
	 * @param x the input parameter
	 * @return the clamped value
	 */
	public static int clamp(int x, int a, int b) {
		return (x < a) ? a : (x > b) ? b : x;
	}
	public static float clamp180(float dir) {
		float outDirAbs = Math.abs(dir);
        float outDir;

        if( outDirAbs > 360.0f )
        {
                if( outDirAbs > 720.0f )
                {
                        outDir = dir % 360.0f;
                }
                else
                {
                        outDir = ( dir > 0.0f ) ? dir - 360.0f : dir + 360.0f;
                }
                outDirAbs = Math.abs(outDir);
        }
        else
        {
                outDir = dir;
        }

        if(outDirAbs > 180.0f)
        {
                if(outDir > 0.0f)
                {
                        outDir -= 360.0f;
                }
                else
                {
                        outDir += 360.0f;
                }
        }
        return outDir;
	}
	public static float clamp360(float dir) {
        float outDirAbs = Math.abs(dir);
        float outDir;

        if( outDirAbs > 360.0f )
        {
                if( outDirAbs > 720.0f )
                {
                        outDir = dir % 360.0f;
                }
                else
                {
                        outDir = ( dir > 0.0f ) ? dir - 360.0f : dir + 360.0f;
                }
                outDirAbs = Math.abs(outDir);
        }
        else
        {
                if(dir < 0.0f)
                        outDir = dir + 360.0f;
                else
                        outDir = dir;
        }
        return outDir;
}

	/**
	 * Clamp a value to the range 0..255
	 */
	public static int clampToByte(int c) {
		if (c < 0)
			return 0;
		if (c > 255)
			return 255;
		return c;
	}
    public static final double clip(double a, double min, double max) {
        return a < min ? min : (a > max ? max : a);
    }

    public static final float clip(float a, float min, float max) {
        return a < min ? min : (a > max ? max : a);
    }

    public static final int clip(int a, int min, int max) {
        return a < min ? min : (a > max ? max : a);
    }

    /**
     * Clips the value to the 0.0 .. 1.0 interval.
     * 
     * @param a
     * @return clipped value
     * @since 0012
     */
    public static final float clipNormalized(float a) {
        if (a < 0) {
            return 0;
        } else if (a > 1) {
            return 1;
        }
        return a;
    }

    public static final double cos(final double theta) {
        return sin(theta + HALF_PI);
    }
    
    /// Count the number of 1-bits in a byte.
    public static final int countOnes( byte n )
	{
	// There are faster ways to do this, all the way up to looking
	// up bytes in a 256-element table.  But this is not too bad.
	int count = 0;
	while ( n != 0 )
	    {
	    count += n&1;
	    n >>>= 1;
	    }
	return count;
	}

    public static final float degrees(float radians) {
        return radians * RAD2DEG;
    }
    // 0 to 180 counter-clockwise for positive Y, 0 to 180 clockwise for negative Y
	public static float directionXangle(gb_Vector3 vect) {
		// Zero degree angle along the +X axis
    	float result = m_MathUtils.atan2(vect.y,vect.x);
    	result = m_MathUtils.clamp180( (float) Math.toDegrees(result) );
    	return result;
	}
    public static double dualSign(double a, double b) {
        double x = (a >= 0 ? a : -a);
        return (b >= 0 ? x : -x);
    }
	  /// Test is a number is even.
    public static final boolean even( long n )
	{
	return ( n & 1 ) == 0;
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
    public static long factorial(final int n) {
        if (n < 0) {
            return 0;
        }
        if (n > 20) {
            return 0;
        }
        return factorials[n];
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
            return factorial(n);
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
            return Math.log(factorial(n));
        }
        double logSum = 0;
        for (int i = 2; i <= n; i++) {
            logSum += Math.log(i);
        }
        return logSum;
    }
    /**
     * Fast cosine approximation.
     * 
     * @param x
     *            angle in -PI/2 .. +PI/2 interval
     * @return cosine
     */
    public static final double fastCos(final double x) {
        return fastSin(x + ((x > HALF_PI) ? -THREE_HALVES_PI : HALF_PI));
    }

    public static final float fastInverseSqrt(float x) {
        float half = 0.5F * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f375a86 - (i >> 1);
        x = Float.intBitsToFloat(i);
        return x * (1.5F - half * x * x);
    }

    /**
     * Computes a fast approximation to <code>Math.pow(a, b)</code>. Adapted
     * from http://www.dctsystems.co.uk/Software/power.html.
     * 
     * @param a
     *            a positive number
     * @param b
     *            a number
     * @return a^b
     * 
     */
    public static final float fastPow(float a, float b) {
        float x = Float.floatToRawIntBits(a);
        x *= INV_SHIFT23;
        x -= 127;
        float y = x - (x >= 0 ? (int) x : (int) x - 1);
        b *= x + (y - y * y) * 0.346607f;
        y = b - (b >= 0 ? (int) b : (int) b - 1);
        y = (y - y * y) * 0.33971f;
        return Float.intBitsToFloat((int) ((b + 127 - y) * SHIFT23));
    }

    /**
     * Fast sine approximation.
     * 
     * @param x
     *            angle in -PI/2 .. +PI/2 interval
     * @return sine
     */
    public static final double fastSin(double x) {
        // float B = 4/pi;
        // float C = -4/(pi*pi);
        //
        // float y = B * x + C * x * abs(x);
        // y = P * (y * abs(y) - y) + y;

        x = SIN_B * x + SIN_A * x * abs(x);
        return SIN_P * (x * abs(x) - x) + x;
    }

    public static final boolean flipCoin() {
        return Math.random() < 0.5;
    }

    public static final boolean flipCoin(Random rnd) {
        return rnd.nextBoolean();
    }

    public static final int floor(double x) {
        int y = (int) x;
        if (x < 0 && x != y) {
            y--;
        }
        return y;
    }

    /**
     * This method is a *lot* faster than using (int)Math.floor(x).
     * 
     * @param x
     *            value to be floored
     * @return floored value as integer
     * @since 0012
     */
    public static final int floor(float x) {
        int y = (int) x;
        if (x < 0 && x != y) {
            y--;
        }
        return y;
    }
    /**
	 * Returns the largest integer less than or equal to the specified float. This method will only properly floor floats from
	 * -(2^14) to (Float.MAX_VALUE - 2^14).
	 */
	static public int fastFloor (float x) {
		return (int)(x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
	}

	/**
	 * Returns the largest integer less than or equal to the specified float. This method will only properly floor floats that are
	 * positive. Note this method simply casts the float to int.
	 */
	static public int floorPositive (float x) {
		return (int)x;
	}
    /**
     * Rounds down the value to the nearest lower power^2 value.
     * 
     * @param x
     * @return power^2 value
     */
    public static final int floorPowerOf2(int x) {
        return (int) Math.pow(2, (int) (Math.log(x) / LOG2));
    }
	/**
	 * A variant of the gamma function.
	 * @param a the number to apply gain to
	 * @param b the gain parameter. 0.5 means no change, smaller values reduce gain, larger values increase gain.
	 * @return the output value
	 */
	public static float gain(float a, float b) {
/*
		float p = (float)Math.log(1.0 - b) / (float)Math.log(0.5);

		if (a < .001)
			return 0.0f;
		else if (a > .999)
			return 1.0f;
		if (a < 0.5)
			return (float)Math.pow(2 * a, p) / 2;
		else
			return 1.0f - (float)Math.pow(2 * (1. - a), p) / 2;
*/
		float c = (1.0f/b-2.0f) * (1.0f-2.0f*a);
		if (a < 0.5)
			return a/(c+1.0f);
		else
			return (c-a)/(c-1.0f);
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
    static public boolean isPowerOfTwo (int value) {
		return value != 0 && (value & value - 1) == 0;
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
    public static double mapInterval(double x, double minIn, double maxIn,
            double minOut, double maxOut) {
        return minOut + (maxOut - minOut) * (x - minIn) / (maxIn - minIn);
    }

    public static float mapInterval(float x, float minIn, float maxIn,
            float minOut, float maxOut) {
        return minOut + (maxOut - minOut) * (x - minIn) / (maxIn - minIn);
    }
    public static final double max(double a, double b) {
        return a > b ? a : b;
    }

    public static final double max(double a, double b, double c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }

    public static final float max(float a, float b) {
        return a > b ? a : b;
    }

    /**
     * Returns the maximum value of three floats.
     * 
     * @param a
     * @param b
     * @param c
     * @return max val
     */
    public static final float max(float a, float b, float c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }

    public static final int max(int a, int b) {
        return a > b ? a : b;
    }

    /**
     * Returns the maximum value of three ints.
     * 
     * @param a
     * @param b
     * @param c
     * @return max val
     */
    public static final int max(int a, int b, int c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }

    public static final double min(double a, double b) {
        return a < b ? a : b;
    }

    public static final double min(double a, double b, double c) {
        return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
    }

    public static final float min(float a, float b) {
        return a < b ? a : b;
    }

    /**
     * Returns the minimum value of three floats.
     * 
     * @param a
     * @param b
     * @param c
     * @return min val
     */
    public static final float min(float a, float b, float c) {
        return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
    }

    public static final int min(int a, int b) {
        return a < b ? a : b;
    }

    /**
     * Returns the minimum value of three ints.
     * 
     * @param a
     * @param b
     * @param c
     * @return min val
     */
    public static final int min(int a, int b, int c) {
        return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
    }
    /**
	 * Return a mod b. This differs from the % operator with respect to negative numbers.
	 * @param a the dividend
	 * @param b the divisor
	 * @return a mod b
	 */
	public static double mod(double a, double b) {
		int n = (int)(a/b);
		
		a -= n*b;
		if (a < 0)
			return a + b;
		return a;
	}

	/**
	 * Return a mod b. This differs from the % operator with respect to negative numbers.
	 * @param a the dividend
	 * @param b the divisor
	 * @return a mod b
	 */
	public static float mod(float a, float b) {
		int n = (int)(a/b);
		
		a -= n*b;
		if (a < 0)
			return a + b;
		return a;
	}

	/**
	 * Return a mod b. This differs from the % operator with respect to negative numbers.
	 * @param a the dividend
	 * @param b the divisor
	 * @return a mod b
	 */
	public static int mod(int a, int b) {
		int n = a/b;
		
		a -= n*b;
		if (a < 0)
			return a + b;
		return a;
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
    public static int mulAndCheck(int x, int y) {
        long m = ((long)x) * ((long)y);
        if (m < Integer.MIN_VALUE || m > Integer.MAX_VALUE) {
            return 0;
        }
        return (int)m;
    }
	static public int nextPowerOfTwo (int value) {
		if (value == 0) return 1;
		value--;
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;
		return value + 1;
	}
	/**
	   * Normalize a value to exist between 0 and 1 (inclusive).
	   * Mathematically the opposite of lerp(), figures out what proportion
	   * a particular value is relative to start and stop coordinates.
	   */
	  static public final float norm(float value, float start, float stop) {
	    return (value - start) / (stop - start);
	  }
    /**
     * Returns a random number in the interval -1 .. +1.
     * 
     * @return random float
     */
    public static final float normalizedRandom() {
        return RND.nextFloat() * 2 - 1;
    }

    /**
     * Returns a random number in the interval -1 .. +1 using the {@link Random}
     * instance provided.
     * 
     * @return random float
     */
    public static final float normalizedRandom(Random rnd) {
        return rnd.nextFloat() * 2 - 1;
    }
    /// Test is a number is odd.
    public static final boolean odd( long n )
	{
	return ( n & 1 ) != 0;
	}
    public static final float pow4(float a)
	{
	  a = a*a;
	  return a*a;  
	}
    public static final float pow16(float a)
	{
	  a =pow4(a);
	  return pow4(a);
	}
    /**
	 * The pulse function. Returns 1 between two thresholds, 0 outside.
	 * @param a the lower threshold position
	 * @param b the upper threshold position
	 * @param x the input parameter
	 * @return the output value - 0 or 1
	 */
	public static float pulse(float a, float b, float x) {
		return (x < a || x >= b) ? 0.0f : 1.0f;
	}
    public static final float radians(float degrees) {
        return degrees * DEG2RAD;
    }
	/**
	 * Returns a random number between 0 (inclusive) and the specified value (inclusive).
	 */
	static public final int random (int range) {
		return RND.nextInt(range + 1);
	}

	static public final boolean randomBoolean () {
		return RND.nextBoolean();
	}

	static public final float random () {
		return RND.nextFloat();
	}
	
    public static final float random(float max) {
        return RND.nextFloat() * max;
    }

    public static final float random(float min, float max) {
        return RND.nextFloat() * (max - min) + min;
    }

    public static final int random(int min, int max) {
        return (int) (RND.nextFloat() * (max - min)) + min;
    }

    public static final double random(Random rnd, double max) {
        return rnd.nextDouble() * max;
    }

    public static final double random(Random rnd, double min, double max) {
        return rnd.nextDouble() * (max - min) + min;
    }

    public static final float random(Random rnd, float max) {
        return rnd.nextFloat() * max;
    }

    public static final float random(Random rnd, float min, float max) {
        return rnd.nextFloat() * (max - min) + min;
    }

    public static final int random(Random rnd, int max) {
        return (int) (rnd.nextDouble() * max);
    }

    public static final int random(Random rnd, int min, int max) {
        return (int) (rnd.nextDouble() * (max - min)) + min;
    }

    public static final double reduceAngle(double theta) {
        theta %= TWO_PI;
        if (abs(theta) > PI) {
            theta = theta - TWO_PI;
        }
        if (abs(theta) > HALF_PI) {
            theta = PI - theta;
        }
        return theta;
    }

    /**
     * Reduces the given angle into the -PI/4 ... PI/4 interval for faster
     * computation of sin/cos. This method is used by {@link #sin(float)} &
     * {@link #cos(float)}.
     * 
     * @param theta
     *            angle in radians
     * @return reduced angle
     * @see #sin(float)
     * @see #cos(float)
     */
    public static final float reduceAngle(float theta) {
        theta %= TWO_PI;
        if (abs(theta) > PI) {
            theta = theta - TWO_PI;
        }
        if (abs(theta) > HALF_PI) {
            theta = PI - theta;
        }
        return theta;
    }
	/**
	 * Returns the closest integer to the specified float. This method will only properly round floats from -(2^14) to
	 * (Float.MAX_VALUE - 2^14).
	 */
	static public int round (float x) {
		return (int)(x + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
	}

	/**
	 * Returns the closest integer to the specified float. This method will only properly round floats that are positive.
	 */
	static public int roundPositive (float x) {
		return (int)(x + 0.5f);
	}
    /**
     * Rounds a double precision value to the given precision.
     * 
     * @param val
     * @param prec
     * @return rounded value
     */
    public static final double roundTo(double val, double prec) {
        return floor(val / prec + 0.5) * prec;
    }

    /**
     * Rounds a single precision value to the given precision.
     * 
     * @param val
     * @param prec
     * @return rounded value
     */
    public static final float roundTo(float val, float prec) {
        return floor(val / prec + 0.5f) * prec;
    }

    /**
     * Rounds an integer value to the given precision.
     * 
     * @param val
     * @param prec
     * @return rounded value
     */
    public static final int roundTo(int val, int prec) {
        return floor((float) val / prec + 0.5f) * prec;
    }
    /**
     * Sets the default Random number generator for this class. This generator
     * is being reused by all future calls to random() method versions which
     * don't explicitly ask for a {@link Random} instance to be used.
     * 
     * @param rnd
     */
    public static void setDefaultRandomGenerator(Random rnd) {
        RND = rnd;
    }

    public static int sign(double x) {
        return x < 0 ? -1 : (x > 0 ? 1 : 0);
    }

    public static int sign(float x) {
        return x < 0 ? -1 : (x > 0 ? 1 : 0);
    }

    public static int sign(int x) {
        return x < 0 ? -1 : (x > 0 ? 1 : 0);
    }

    public static final double sin(double theta) {
        theta = reduceAngle(theta);
        if (abs(theta) <= QUARTER_PI) {
            return (float) fastSin(theta);
        }
        return (float) fastCos(HALF_PI - theta);
    }

    /**
     * Returns a fast sine approximation of a value. Note: code from <a
     * href="http://wiki.java.net/bin/view/Games/JeffGems">wiki posting on
     * java.net by jeffpk</a>
     * 
     * @param theta
     *            angle in radians.
     * @return sine of theta.
     */
    public static final float sin2(float theta) {
        theta = reduceAngle(theta);
        if (abs(theta) <= QUARTER_PI) {
            return (float) fastSin(theta);
        }
        return (float) fastCos(HALF_PI - theta);
    }
    /**
	 * A smoothed pulse function. A cubic function is used to smooth the step between two thresholds.
	 * @param a1 the lower threshold position for the start of the pulse
	 * @param a2 the upper threshold position for the start of the pulse
	 * @param b1 the lower threshold position for the end of the pulse
	 * @param b2 the upper threshold position for the end of the pulse
	 * @param x the input parameter
	 * @return the output value
	 */
	public static float smoothPulse(float a1, float a2, float b1, float b2, float x) {
		if (x < a1 || x >= b2)
			return 0;
		if (x >= a2) {
			if (x < b1)
				return 1.0f;
			x = (x - b1) / (b2 - b1);
			return 1.0f - (x*x * (3.0f - 2.0f*x));
		}
		x = (x - a1) / (a2 - a1);
		return x*x * (3.0f - 2.0f*x);
	}

	/**
	 * A smoothed step function. A cubic function is used to smooth the step between two thresholds.
	 * @param a the lower threshold position
	 * @param b the upper threshold position
	 * @param x the input parameter
	 * @return the output value
	 */
	public static float smoothStep(float a, float b, float x) {
		if (x < a)
			return 0;
		if (x >= b)
			return 1;
		x = (x - a) / (b - a);
		return x*x * (3 - 2*x);
	}
    public static final float sqrt2(float x) {
        x = fastInverseSqrt(x);
        if (x > 0) {
            return 1.0f / x;
        } else {
            return 0;
        }
    }
    static public float sqr(float x) {return x*x;}
	static public int sqr(int x) {return x*x;}
	static public double sqr(double x) {return x*x;}
    /**
	 * The step function. Returns 0 below a threshold, 1 above.
	 * @param a the threshold position
	 * @param x the input parameter
	 * @return the output value - 0 or 1
	 */
	public static float step(float a, float x) {
		return (x < a) ? 0.0f : 1.0f;
	}
    /**
     * The sum of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.</p>
     * 
     * @param values, the input array
     * @return the sum of the values, out[0] min index [1] max
     */
    public static double sumMinMax(double[] values, int out[]) {
        int max=0;
        int min=0, i;
        double sum = values[0];
        int n=values.length;
            for (i = 1; i < n; i++) {
                sum += values[i];
                if (values[i]>values[max]) max=i;
                if (values[i]<values[min]) min=i;
            }
        out[0]=min; out[1]=max;
        return sum;
    }

    /**
     * Utility and fast math functions.<br>
     * <br>
     * Thanks to Riven on JavaGaming.org for sin/cos/atan2/floor/ceil.<br>
     */

    	static public final float[] sin = new float[SIN_COUNT];
    	static public final float[] cos = new float[SIN_COUNT];

    	static {
    		for (int i = 0; i < SIN_COUNT; i++) {
    			float a = (i + 0.5f) / SIN_COUNT * radFull;
    			sin[i] = (float)Math.sin(a);
    			cos[i] = (float)Math.cos(a);
    		}
    		for (int i = 0; i < 360; i += 90) {
    			sin[(int)(i * degToIndex) & SIN_MASK] = (float)Math.sin(i * degreesToRadians);
    			cos[(int)(i * degToIndex) & SIN_MASK] = (float)Math.cos(i * degreesToRadians);
    		}
    	}

    	static public final float sin (float rad) {
    		return sin[(int)(rad * radToIndex) & SIN_MASK];
    	}

    	static public final float cos (float rad) {
    		return cos[(int)(rad * radToIndex) & SIN_MASK];
    	}

    	static public final float sinDeg (float deg) {
    		return sin[(int)(deg * degToIndex) & SIN_MASK];
    	}

    	static public final float cosDeg (float deg) {
    		return cos[(int)(deg * degToIndex) & SIN_MASK];
    	}

    	// ---

    	static private final int ATAN2_BITS = 7; // Adjust for accuracy.
    	static private final int ATAN2_BITS2 = ATAN2_BITS << 1;
    	static private final int ATAN2_MASK = ~(-1 << ATAN2_BITS2);
    	static private final int ATAN2_COUNT = ATAN2_MASK + 1;
    	static private final int ATAN2_DIM = (int)Math.sqrt(ATAN2_COUNT);
    	static private final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (ATAN2_DIM - 1);
    	static private final float[] atan2 = new float[ATAN2_COUNT];
    	static {
    		for (int i = 0; i < ATAN2_DIM; i++) {
    			for (int j = 0; j < ATAN2_DIM; j++) {
    				float x0 = (float)i / ATAN2_DIM;
    				float y0 = (float)j / ATAN2_DIM;
    				atan2[j * ATAN2_DIM + i] = (float)Math.atan2(y0, x0);
    			}
    		}
    	}

    	static public final float atan2 (float y, float x) {
    		float add, mul;
    		if (x < 0) {
    			if (y < 0) {
    				y = -y;
    				mul = 1;
    			} else
    				mul = -1;
    			x = -x;
    			add = -3.141592653f;
    		} else {
    			if (y < 0) {
    				y = -y;
    				mul = -1;
    			} else
    				mul = 1;
    			add = 0;
    		}
    		float invDiv = 1 / ((x < y ? y : x) * INV_ATAN2_DIM_MINUS_1);
    		int xi = (int)(x * invDiv);
    		int yi = (int)(y * invDiv);
    		return (atan2[yi * ATAN2_DIM + xi] + add) * mul;
    	}

    	//TODO move to correct locations, possibly rename

    	public static boolean isEqual(float a, float b){
    		return (Math.abs(a-b) < 1.0E-12f) ? true : false;
    	}
    	
    	public static boolean isEqual(float a, float b, float err){
    		return (Math.abs(a-b) < err) ? true : false;
    	}
    	
    	public static float RandomClamped(){
    		return (float) (Math.random() - Math.random());
    	}

}

