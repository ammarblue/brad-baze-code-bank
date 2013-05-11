package com.software.reuze;
// Much of this is adapted from the beartronics FP lib
public class m_FixedPoint {
	public static final int ONE = 0x10000;
	public static final int ZERO = 0;
	public static final int MAX_VALUE = 0x7fffffff;
	public static final int MIN_VALUE = 0x1;
	public static final int PI = fixedValue((float)Math.PI);
	public static final int HALF_PI = fixedValue((float)(Math.PI/2));
	public static final int SIXTH_PI = fixedValue((float)(Math.PI/6));
	public static final int TWELFTH_PI = fixedValue((float)(Math.PI/12));
	public static final int THREE_HALVES_PI = fixedValue((float)(Math.PI*1.5));
	public static final int E = fixedValue((float)Math.E);
	public static final int ONE_OVER_E = fixedValue((float)(1/Math.E));
	/**
     * Degrees to radians conversion factor
     */
    public static final int DEG2RAD = fixedValue((float)(Math.PI/180));

    /**
     * Radians to degrees conversion factor
     */
    public static final int RAD2DEG = fixedValue((float)(180/Math.PI));
	
	/**
	 * Convert a float to  16.16 fixed-point representation
	 * @param val The value to convert
	 * @return The resulting fixed-point representation
	 */
	public static int fixedValue(float val) {
		return (int)(val * 65536F);
	}

	/**
	 * Convert an array of floats to 16.16 fixed-point
	 * @param arr The array
	 * @return A newly allocated array of fixed-point values.
	 */
	public static int[] fixedValue(float[] arr) {
		int[] res = new int[arr.length];
		fixedValue(arr, res);
		return res;
	}
	
	/**
	 * Convert an array of floats to 16.16 fixed-point
	 * @param arr The array of floats
	 * @param storage The array of ints, must already be allocated
	 */
	public static void fixedValue(float[] arr, int[] storage /*out*/)
	{
		for (int i=0;i<storage.length;i++) {
			storage[i] = fixedValue(arr[i]);
		}
	}
	
	/**
	 * Truncate a 16.16 fixed-point value to int
	 * @param val The fixed-point value
	 * @return The equivalent int value.
	 */
	public static int intValue(int val) {
		return val>>16;
	}
	
	/**
	 * Return a 16.16 fixed-point value's fractional part
	 * @param val The fixed-point value
	 * @return The equivalent int value.
	 */
	public static float fractionValue(int val) {
		return ((float)(val&0xffff))/65536;
	}
	
	/**
	 * Convert a 16.16 fixed-point value to floating point
	 * @param val The fixed-point value
	 * @return The equivalent floating-point value.
	 */
	public static float floatValue(int val) {
		return ((float)val)/65536.0f;
	}
	
	/**
	 * Convert an array of 16.16 fixed-point values to floating point
	 * @param arr The array to convert
	 * @return A newly allocated array of floats.
	 */
	public static float[] floatValue(int[] arr) {
		float[] res = new float[arr.length];
		floatValue(arr, res);
		return res;
	}
	
	/**
	 * Convert an array of 16.16 fixed-point values to floating point
	 * @param arr The array to convert
	 * @param storage Pre-allocate storage for the result.
	 */
	public static void floatValue(int[] arr, float[] storage)
	{
		for (int i=0;i<storage.length;i++) {
			storage[i] = floatValue(arr[i]);
		}
	}
	
	/**
	 * Multiply two fixed-point values. +, -, >=<, abs, max, min use int operators directly but only on 16.16 operands.
	 * @param x
	 * @param y
	 * @return
	 */
	public static int multiply (int x, int y) {
		long z = (long) x * (long) y;
		return ((int) (z >> 16));
	}

	/**
	 * Divide two fixed-point values.
	 * @param x
	 * @param y
	 * @return
	 */
	public static int divide (int x, int y) {
		long z = (((long) x) << 32);
		return (int) ((z / y) >> 16);
	}
	public static int abs(int x) {return (x>=0)?x:-x;}
	public static int max(int x, int y) {return (x>=y)?x:y;}
	public static int min(int x, int y) {return (x>=y)?y:x;}
	/**
	 * Radian value from degrees.
	 * @param x
	 * @return
	 */
	public static int radianValue (int x) {
		return multiply(x, RAD2DEG);
	}
	/**
	 * Degree value from radians.
	 * @param x
	 * @return
	 */
	public static int degreeValue (int x) {
		return multiply(x, DEG2RAD);
	}
	/**
	 * Find the sqrt of a fixed-point value.
	 * @param n
	 * @return
	 */
	 public static int sqrt (int n) {
		if (n<0) throw new ArithmeticException("sqrt operand < 0");
		if (n==0) return 0;
		if (n>0x27100000) { //where accuracy falls off, >9000
			return fixedValue((float)Math.sqrt(floatValue(n)));			
		}
		int s = (n + 65536) >> 1;
		for (int i = 0; i < 8; i++) {
			//converge six times
			s = (s + divide(n, s)) >> 1;
		}
		return s;
	 }
	 
	 /**
		 * String float of a 16.16 fixed-point value.
		 * @param n
		 * @return
		 */
	 public static String toString (int n) {
		 return Float.toString(floatValue(n));
	 }
	 /** Round to nearest fixed point integer */
	    public static int round (int n) {
		if (n > 0) {
		    if ((n & 0x8000) != 0) {
			return (((n+0x10000)>>16)<<16);
		    } else {
			return (((n)>>16)<<16);
		    }
		} else {
		    int k;
		    n = -n;
		    if ((n & 0x8000) != 0) {
			k = (((n+0x10000)>>16)<<16);
		    } else {
			k = (((n)>>16)<<16);
		    }
		    return -k;
		}
	    }
	    private final static int SIN_A = fixedValue((float)(-4d / (Math.PI * Math.PI)));
	    private final static int SIN_B = fixedValue((float)(4d / Math.PI));
	    private final static int SIN_P = fixedValue(9f / 40f);	 
	    /**
	     * Fast sine approximation.
	     * 
	     * @param x
	     *            angle in -PI/2 .. +PI/2 radian interval
	     * @return sine
	     */
	    public static final int sin(int x) {
	        // float B = 4/pi;
	        // float C = -4/(pi*pi);
	        //
	        // float y = B * x + C * x * abs(x);
	        // y = P * (y * abs(y) - y) + y;
            int y=Math.abs(x);
            int z=multiply(y, x);
	        x = multiply(SIN_B, x) + multiply(SIN_A, z);
	        z=multiply(x, abs(x))-x;
	        return multiply(SIN_P, z) + x;
	    }
	    /**
	     * Fast cosine approximation.
	     * 
	     * @param x
	     *            angle in -PI/2 .. +PI/2 radian interval
	     * @return cosine
	     */
	    public static final int cos(int x) {
	        return sin(x + ((x > HALF_PI) ? -THREE_HALVES_PI : HALF_PI));
	    }
	    /**
	     * Fast tangent approximation.
	     * 
	     * @param x
	     *            angle in -PI/2 .. +PI/2 radian interval
	     * @return tangent
	     */
	    public static final int tan(int x) {
	        return divide(sin(x), cos(x));
	    }
	    private final static int POINT28=fixedValue(0.28f);
	    public static int atan2( int y, int x )
	    {
	    	if ( x == ZERO )
	    	{
	    		if ( y > ZERO ) return HALF_PI;
	    		if ( y == ZERO ) return ZERO;
	    		return -HALF_PI;
	    	}
	    	int atan;
	    	int z = divide(y, x);
	    	atan=multiply(z, z);
	    	if ( abs( z ) < ONE )
	    	{	    		
	    		atan = ONE + multiply(atan, POINT28);
	    		atan = divide(z, atan);
	    		if ( x < ZERO )
	    		{
	    			if ( y < ZERO ) atan -= PI;
	    			atan += PI;
	    		}
	    	}
	    	else
	    	{
	    		atan = HALF_PI - divide(z, atan+POINT28);
	    		if ( y < ZERO ) atan -= PI;
	    	}
	    	return atan;
	    }
	    private static final int C1=fixedValue(1.68676f);
	    private static final int C2=fixedValue(0.43785f);
	    private static final int C3=fixedValue(0.00457f);
	    private static final int C4=fixedValue(0.00914f);
	    /**
	     * Fast arc-tangent approximation.
	     * 
	     * @param x
	     *            angle in 0 .. +PI/12 radian interval
	     * @return arc-tangent
	     */
	    public static int atan12( int x )
	    {
	    	int x2=multiply(x, x);
	    	int y=multiply(x2, C2)+C1;
	    	y=multiply(x, y);
	    	return divide(y, C1+x2);
	    }
	    /**
	     * Fast arc-tangent approximation.
	     * 
	     * @param x
	     *            angle in -PI/2 .. +PI/2 radian interval
	     * @return arc-tangent
	     */
	    public static int atan( int x )
	    {
	    	boolean complement=false, region=false, sign=false;
	    	int y;
	    	if (x < ZERO) {x=-x; sign=true;}
	    	if (x>ONE) {x=divide(ONE, x); complement=true;}
	    	if (x>=ZERO && x<=TWELFTH_PI) ;
	    	else if (x>C3) {
	    		y=multiply(x, C4);
	    		x=divide(x-C4, y+ONE);
	    		region=true;
	    	}
	    	y=atan12(x);
	    	if (region) y+=SIXTH_PI;
	    	if (complement) y=HALF_PI-y;
	    	if (sign) y=-y;
	    	return y;
	    }
	    static final int AS1 = -1228;
	    static final int AS2 = 4866;
	    static final int AS3 = 13901;
	    static final int AS4 = 102939;
	    /** Compute ArcSin(f), 0 <= f <= 1 */
	    public static int asin (int f) {
		int fRoot = sqrt((1<<16)-f);
		int result = AS1;
		result = multiply(result, f);
		result += AS2;
		result = multiply(result, f);
		result -= AS3;
		result = multiply(result, f);
		result += AS4;
		result = HALF_PI - (multiply(fRoot,result));
		return result;
	    }
	    
	    /** Compute ArcCos(f), 0 <= f <= 1 */
	    public static int acos (int f) {
		int fRoot = sqrt((1<<16)-f);
		int result = AS1;
		result = multiply(result, f);
		result += AS2;
		result = multiply(result, f);
		result -= AS3;
		result = multiply(result, f);
		result += AS4;
		result = multiply(fRoot,result);
		return result;
	    }
	    public static int pow(int base, int INTEGER) {
	    	if (INTEGER==0) return ONE;
	    	boolean neg=false;
	    	if (INTEGER<0) {neg=true; INTEGER=-INTEGER;}
	    	int result=base;
	    	for (int i=1; i<INTEGER; i++) result=multiply(result, base);
	    	return neg?divide(ONE,result):result;
	    }
	    /** Exponential

		e^x = 1 + x/1! + x^2/2! + x^3/3!

	    */

	    static final int fpfact[] = { 1<<16,
			     1<<16,
			     2<<16,
			     6<<16,
			     24<<16,
			     120<<16,
			     720<<16,
			     5040<<16
	    };
        /* valid between -3<=x<=+3 */
	    public static int exp (int x) {
	    boolean neg=false;
	    if (x<0) {neg=true; x=-x;}
	    if ((x&0xffff)==0) return neg?pow(ONE_OVER_E,x>>16):pow(E, x>>16);
		int x2 = multiply(x,x);
		int x3 = multiply(x2,x);
		int x4 = multiply(x2,x2);
		int x5 = multiply(x4,x);
		int x6 = multiply(x4,x2);
		int x7 = multiply(x6,x);
		int result = ONE + x 
		    + divide(x2,fpfact[2]) 
		    + divide(x3,fpfact[3]) 
		    + divide(x4,fpfact[4])
		    + divide(x5,fpfact[5]) 
		    + divide(x6,fpfact[6]) 
		    + divide(x7,fpfact[7]);
		return neg?divide(ONE,result):result;
	    }
	    /** Logarithms: 
	     * 
	     * (2) Knuth, Donald E., "The Art of Computer Programming Vol 1",
	     * Addison-Wesley Publishing Company, ISBN 0-201-03822-6 ( this
	     * comes from Knuth (2), section 1.2.3, exercise 25).
	     *
	     * http://www.dattalo.com/technical/theory/logs.html
	     *

	    */

	    /** This table is created using base of e. 
		
		(defun fixedpoint (z) 
		  (round (* z (lsh 1 16))))

		(loop for k from 0 to 16 do
		      (setq z (log (+ 1 (expt 2.0 (- (+ k 1)))))) 
		      (insert (format "%d\n"  (fixedpoint z))))


	    */
	    static int log2arr[] = {
		26573,
		14624,
		7719,
		3973,
		2017,
		1016,
		510,
		256,
		128,
		64,
		32,
		16,
		8,
		4,
		2,
		1,
		0,
		0,
		0
	    };

	    /*
	      Binary Logarithm:

	      case is very similar to the previous one. The only difference is
	      in how the input is factored. Like before we are given:

	      Input: 16 bit unsigned integer x; 0 < x < 65536

	      (or 8 bit unsigned integer...)
	      
	      Output: g, lg(x) the logarithm of x with respect to base 2.
	      
	      3(b).i) Create a table of logarithms of the following constants:
	      log2arr[i] = lg(1 + 2^(-(i+1))) 
	      
	      i = 0..M, M == desired size of the table.
	      
	      The first few values of the array are
	      
	      lg(3/2), lg(5/4), lg(9/8), lg(17/16),...
	      
	      Recall that in the previous case the factors were
	      
	      lg(2/1), lg(4/3), lg(8/7), lg(16/15),...
	      
	      Again, if you wish to compute logarithms to a different base,
	      then substitute the lg() function with the appropriate based
	      logarithm function.
	      
	      3(b).ii)Scale y to a value between 1 and 2.
	      This is identical to 3(a).ii.
	      
	      3(b).iii) Changing Perspective
	      Again, this is identical to 3(a).iii.
	      
	      3(b).iv) Factor y.
	      
	      This is very similar to step (iv) above. However, we now have
	      different factors. Using the same example, x = 1.9, we can find
	      the factors for this case.
	      
	      a) 1.9 > 1.5,
	      x = x/1.5 ==> 1.266666
	      b) 1.26 >  1.25
	      x ==> 1.0133333
	      c) 1.0133 < 1.125 so don't divide
	      d) 1.0133 < 1.0625  "     "
	      e) etc.
	      
	      
	      So, x ~= 1.5 * 1.25 * etc.
	      
	      
	      Like the previouse case, these factors are not perfect. Also,
	      they're somewhat redundant in the sense that
	      1.5*1.25*1.125*1.0625*... spans a range that is larger than 2 (
	      ~2.38423 for i<=22). So unlike the previous factoring method,
	      this one will not have repeated factors. Here's some psuedo
	      code:
	      
	      for(i=1,d=0.5; i<M; i++, d/=2)
	      if( x > 1+d)
	      {
	      x /= (1+d);
	      g += log2arr[i-1];   // log2arr[i-1] = log2(1+d);
	      }
	      
	      
	      Here, d takes on the values of 0.5, 0.25, 0.125, ... , 2^(-i). Then
	      1+d is the trial factor at each step. If x is greater than this trial
	      factor, then we divide the trial factor out and add to g (ultimately
	      the logarithm of x) the partial logarithm of the factor.
	      
	    */

	    /*
		(loop for k from 0 to 16 do
		(setq z (log (expt 2 k)))
		(insert (format "%d,\n" (fixedpoint z))))
	    */
	    
	    static int lnscale[] = {
		0,
		45426,
		90852,
		136278,
		181704,
		227130,
		272557,
		317983,
		363409,
		408835,
		454261,
		499687,
		545113,
		590539,
		635965,
		681391,
		726817
	    };
        /* only valid for x>=1 because prescale only reduces*/
	    public static int log (int x) {
		// prescale so x is between 1 and 2
		int shift = 0;

		while (x > 1<<17) {
		    shift++;
		    x >>= 1;
		}

		int g = 0;
		int d = 0x8000;  // 1/2
		for (int i = 1; i < 16; i++) {
		    if (x > (ONE + d)) {
			x = divide(x, ( ONE + d));
			g += log2arr[i-1];   // log2arr[i-1] = log2(1+d);
		    }
		    d >>= 1;
		}
		return g + lnscale[shift];
	    }
        private static int temp[]=new int[2];
        /**
         * Does line segment A intersection line segment B?
         *
         * Assumes 16 bit fixed point numbers with 16 bits of fraction.
         *
         * For debugging, side effect xint, yint, the intersection point.
         *
         * <pre>
         * Algorithm 
         * 
         * As an example of algorithm development, consider the intersection of
         * two line segments.  Given line segment A goes from point XA1 and YA1
         * to point XA2 and YA2 and given line segment B goes from point XB1 and
         * YB1 to point XB2 and YB2.  Find whether there is zero, one, or an
         * infinite number of points of intersection (the line segments overlap)
         * and the values of the points of intersection.  Assume all numbers are
         * double.
         * 
         * For case 1 where line segment A is not vertical, line segment B is not
         * vertical, and line segment A is not parallel to line segment B, the
         * equations for line segment A and B are:
         * 
         * 
         * XMA = (YA2-YA1)/(XA2-XA1) = slope of line segment A
         * XBA = YA1 - XA1*XMA = Y-intercept for line segment A
         * YA = XMA*XA + XBA
         * 
         * XMB = (YB2-YB1)/(XB2-XB1) = slope of line segment B
         * XBB = YB1 - XB1*XMB = Y-intercept for line segment B
         * YB = XMB*XB + XBB
         * 
         * At the intersection of line segment A and B, XA=XB=XINT and YA=YB=YINT.
         * YINT = XMA*XINT + XBA
         * YINT = XMB*XINT + XBB
         * XMA*XINT + XBA = XMB*XINT + XBB
         * XMA*XINT - XMB*XINT = XBB - XBA
         * XINT*(XMA-XMB) = XBB - XBA
         * XINT = (XBB-XBA)/(XMA-XMB)
         * YINT = XMA*XINT + XBA
         * There is one point of intersection.
         * 
         * For case 2 where line segment A is vertical (XA1 is close to XA2) and
         * line segment B is not vertical, the equations for line segment A and B
         * are:
         * 
         * XA = 0.5*(XA1+XA2)
         * 
         * XMB = (YB2-YB1)/(XB2-XB1) = slope of line segment B
         * XBB = YB1 - XB1*XMB = Y-intercept for line segment B
         * YB = XMB*XB + XBB
         * 
         * At the intersection of line segment A and B, XA=XB=XINT and YA=YB=YINT.
         * XINT = XA
         * YINT = XMB*XINT + XBB
         * There is one point of intersection.
         * 
         * For case 3 where line segment A is not vertical and line segment B is
         * vertical (XB1 is close to XB2), the equations for line segment A and B
         * are:
         * 
         * XMA = (YA2-YA1)/(XA2-XA1) = slope of line segment A
         * XBA = YA1 - XA1*XMA = Y-intercept for line segment A
         * YA = XMA*XA + XBA
         * 
         * XB= 0.5*(XB1+XB2)
         * 
         * At the intersection of line segment A and B, XA=XB=XINT and YA=YB=YINT.
         * XINT = XB
         * YINT = XMA*XINT + XBA
         * There is one point of intersection.
         * 
         * For case 4 where line segment A is vertical (XA1 is close to XA2) and
         * line segment B is vertical (XB1 is close to XB2), the distance between
         * the parallel line segments is:
         * 
         * DIST = ABS ( 0.5*(XA1+XA2) - 0.5*(XB1+XB2) )
         * 
         * If DIST is close to zero, then:
         * 
         * XINT1 = 0.5*(0.5*(XA1+XA2)+0.5*(XB1+XB2))
         * YINT1 = MAX(MIN(YA1,YA2),MIN(YB1,YB2))
         * XINT2 = XINT1
         * YINT2 = MIN(MAX(YA1,YA2),MAX(YB1,YB2))
         * There are two points of intersection.
         * 
         * For case 5 where line segment A is not vertical, line segment B is not
         * vertical, and line segment A is parallel to line segment B (XMA is
         * close to XMB), the equations for line segment A and B are:
         * 
         * XMA = (YA2-YA1)/(XA2-XA1) = slope of line segment A
         * XBA = YA1 - XA1*XMA = Y-intercept for line segment A
         * YA = XMA*XA + XBA
         * 
         * XMB = (YB2-YB1)/(XB2-XB1) = slope of line segment B
         * XBB = YB1 - XB1*XMB = Y-intercept for line segment B
         * YB = XMB*XB + XBB
         * 
         * The distance between the parallel line segments is:
         * 
         * DIST = ABS(XBA-XBB)*COS(ATAN(0.5*(XMA+XMB)))
         * 
         * If DIST is close to zero, then:
         * 
         * XINT1 = MAX(MIN(XA1,XA2),MIN(XB1,XB2))
         * YINT1 = MAX(MIN(YA1,YA2),MIN(YB1,YB2))
         * XINT2 = MIN(MAX(XA1,XA2),MAX(XB1,XB2))
         * YINT2 = MIN(MAX(YA1,YA2),MAX(YB1,YB2))
         * There are two points of intersection.
         * 
         * After the point or points of intersection are calculated, each
         * solution must be checked to ensure that the point of intersection lies
         * on line segment A and B by checking if XINT >= MIN(XA1,XA2) and XINT
         * <= MAX(XA1,XA2) and YINT >= MIN(YA1,YA2) and YINT <= MAX(YA1,YA2) and
         * checking if XINT >= MIN(XB1,XB2) and XINT <= MAX(XB1,XB2) and YINT >=
         * MIN(XB1,XB2) and YINT <= MAX(YB1,YB2).
         * 
         * Note that case 2, 3, 4, and 5 are all special instances of case 1
         * where a division by zero would have caused the creation of an infinite
         * number and thus a program error.
         * 
         * </pre>
         */
        public static int[] intersects (int ax0, int ay0, int ax1, int ay1,
    			int bx0, int by0, int bx1, int by1) {
    	
    	ax0 <<= 16;
    	ay0 <<= 16;
    	ax1 <<= 16;
    	ay1 <<= 16;
    	
    	bx0 <<= 16;
    	by0 <<= 16;
    	bx1 <<= 16;
    	by1 <<= 16;
    	
    	int adx = (ax1 - ax0);
    	int ady = (ay1 - ay0);
    	int bdx = (bx1 - bx0);
    	int bdy = (by1 - by0);

    	int xma;
    	int xba;
    	int xIntersect=ax0, yIntersect=ay0;
    	int xmb;
    	int xbb;	
    	int TWO = (2 << 16);

    	if ((adx == 0) && (bdx == 0)) { // both vertical lines
    	    int dist = abs(divide((ax0+ax1)-(bx0+bx1), TWO));
    	    assert false; //TODO return (dist == 0);
    	} else if (adx == 0) { // A  vertical
    	    int xa = divide((ax0 + ax1), TWO);
    	    xmb = divide(bdy,bdx);           // slope segment B
    	    xbb = by0 - multiply(bx0, xmb); // y intercept of segment B
    	    xIntersect = xa;
    	    yIntersect = (multiply(xmb,xIntersect)) + xbb;
    	} else if ( bdx == 0) { // B vertical
    	    int xb = divide((bx0+bx1), TWO);
    	    xma = divide(ady,adx);           // slope segment A
    	    xba = ay0 - (multiply(ax0,xma)); // y intercept of segment A
    	    xIntersect = xb;
    	    yIntersect = (multiply(xma,xIntersect)) + xba;
    	} else {
    	     xma = divide(ady,adx);           // slope segment A
    	     xba = ay0 - (multiply(ax0, xma)); // y intercept of segment A

    	     xmb = divide(bdy,bdx);           // slope segment B
    	     xbb = by0 - (multiply(bx0,xmb)); // y intercept of segment B
    	
    	     // parallel lines? 
    	     if (xma == xmb) {
    		 // Need trig functions
    		 int dist = abs(multiply((xba-xbb),
    					   (cos(atan(divide((xma+xmb), TWO))))));
    		 if (dist < (1<<16) ) {
    			 temp[0]=xIntersect; temp[1]=yIntersect;
    		     return temp;
    		 } else {
    		     return null;
    		 }
    	     } else {
    		 // Calculate points of intersection
    		 // At the intersection of line segment A and B, XA=XB=XINT and YA=YB=YINT
    		 if ((xma-xmb) == 0) {
    		     return null;
    		 }
    		 xIntersect = divide((xbb-xba),(xma-xmb));
    		 yIntersect = (multiply(xma,xIntersect)) + xba;
    	     }
    	}

    	// After the point or points of intersection are calculated, each
    	// solution must be checked to ensure that the point of intersection lies
    	// on line segment A and B.
    	
    	int minxa = min(ax0, ax1);
    	int maxxa = max(ax0, ax1);

    	int minya = min(ay0, ay1);
    	int maxya = max(ay0, ay1);

    	int minxb = min(bx0, bx1);
    	int maxxb = max(bx0, bx1);

    	int minyb = min(by0, by1);
    	int maxyb = max(by0, by1);

    	if ((xIntersect >= minxa) && (xIntersect <= maxxa) && (yIntersect >= minya) && (yIntersect <= maxya) 
    		&& 
    		(xIntersect >= minxb) && (xIntersect <= maxxb) && (yIntersect >= minyb) && (yIntersect <= maxyb)) {
    		temp[0]=xIntersect; temp[1]=yIntersect;
    		return temp;
    	}
    	return null;
        }

	 public static void main(String args[]) {
		 int x=fixedValue(10000);
		 System.out.println(Integer.toHexString(x));
		 System.out.println(fractionValue(x));
		 System.out.println(floatValue(MAX_VALUE));
		 System.out.println(floatValue(MIN_VALUE));
		 System.out.println(floatValue(sqrt(MIN_VALUE)));
		 for (float f=-3.14f/2f; f<=3.14f/2f; f+=0.05f) System.out.println(f+" "+floatValue(sin(fixedValue(f)))+" "+Math.sin(f));
		 //for (float f=0; f<=4; f+=.2f) System.out.println(f+" "+floatValue(log(fixedValue(f)))+" "+Math.log(f));
		 int sum=0;
		 long start=System.nanoTime();
		 for (float f=-3.14f/2f; f<=3.14f/2f; f+=0.001f) sum+=sin(fixedValue(f));
		 System.out.println(System.nanoTime()-start);
		 System.out.println(sum);
		 float sumf=0;
		 start=System.nanoTime();
		 for (float f=-3.14f/2f; f<=3.14f/2f; f+=0.001f) sumf+=Math.sin(f); //5 times faster
		 System.out.println(System.nanoTime()-start);
		 System.out.println(sumf);
	 }
}
