package com.software.reuze;

import java.util.Arrays;

public final class m_PolynomialSolver {
	public static boolean isZero(float x) {
		return Math.abs(x)<1.0e-7f;
	}
	public static float mCbrt(float val)
	{
	   if(val < 0.f)
	      return (float) (-Math.pow(-val, (1.f/3.f)));
	   else
	      return (float) (Math.pow(val, (1.f/3.f)));
	}
	public static float[] solve(float... x) {
		if (x!=null) switch (x.length) {
		case 2:
			if (isZero(x[0])) return new float[]{0};
			   return new float[]{ -x[1]/x[0] };
		case 3:
			// really linear?
			   if(isZero(x[0]))
			      return solve(x[1],x[2]);

			   // get the discriminant:   (b^2 - 4ac)
			   float desc = (x[1] * x[1]) - (4.f * x[0] * x[2]);

			   // solutions:
			   // desc < 0:   two imaginary solutions
			   // desc > 0:   two real solutions (b +- sqrt(desc)) / 2a
			   // desc = 0:   one real solution (-b / 2a)
			   if(isZero(desc)) return new float[]{ -x[1] / (2.f * x[0]) };
			   else if(desc > 0.f)
			   {
			      float sqrdesc = (float) Math.sqrt(desc);
			      float den = (2.f * x[0]);
			      return new float[] { (-x[1] + sqrdesc) / den,
			                           (-x[1] - sqrdesc) / den};
			   }
			   else return null;
		case 4: //// from Graphics Gems I: pp 738-742
			if(isZero(x[0]))
			      return solve(x[1], x[2], x[3]);

			   // normal form: x^3 + Ax^2 + BX + C = 0
			   float A = x[1] / x[0];
			   float B = x[2] / x[0];
			   float C = x[3] / x[0];

			   // substitute x = y - A/3 to eliminate quadric term and depress
			   // the cubic equation to (x^3 + px + q = 0)
			   float A2 = A * A;
			   float A3 = A2 * A;

			   float p = (1.f/3.f) * (((-1.f/3.f) * A2) + B);
			   float q = (1.f/2.f) * (((2.f/27.f) * A3) - ((1.f/3.f) * A * B) + C);

			   // use Cardano's formula to solve the depressed cubic
			   float p3 = p * p * p;
			   float q2 = q * q;

			   float D = q2 + p3;
			   // resubstitute
			   float sub = (1.f/3.f) * A;
			   if(isZero(D))          // 1 or 2 solutions
			   {
			      if(isZero(q)) // 1 triple solution
			      {
			         return new float[]{ -sub };
			      }
			      else // 1 single and 1 double
			      {
			         float u = mCbrt(-q);
			         return new float[] { 2.f * u-sub, -u-sub };
			      }
			   }
			   else if(D < 0.f)        // 3 solutions: casus irreducibilis
			   {
			      float phi = (float) ((1.f/3.f) * Math.acos(-q / Math.sqrt(-p3)));
			      float t = (float) (2.f * Math.sqrt(-p));
			      return new float[] { (float) (t * Math.cos(phi)-sub),
			                           (float) (-t * Math.cos(phi + (Math.PI / 3.f))-sub),
			                           (float) (-t * Math.cos(phi - (Math.PI / 3.f))-sub) };
			   }
			   else                    // 1 solution
			   {
			      float sqrtD = (float) Math.sqrt(D);
			      return new float[] { mCbrt(sqrtD - q)-
			                           mCbrt(sqrtD + q)-sub };
			   }
		case 5:
			if(isZero(x[0]))
			      return solve(x[1], x[2], x[3], x[4]);

			   // normal form: x^4 + ax^3 + bx^2 + cx + d = 0
			   float AA = x[1] / x[0];
			   float BB = x[2] / x[0];
			   float CC = x[3] / x[0];
			   float DD = x[4] / x[0];

			   // substitute x = y - A/4 to eliminate cubic term:
			   // x^4 + px^2 + qx + r = 0
			   float AA2 = AA * AA;
			   float AA3 = AA2 * AA;
			   float AA4 = AA2 * AA2;

			   float pp = ((-3.f/8.f) * AA2) + BB;
			   float qq = ((1.f/8.f) * AA3) - ((1.f/2.f) * AA * BB) + CC;
			   float rr = ((-3.f/256.f) * AA4) + ((1.f/16.f) * AA2 * BB) - ((1.f/4.f) * AA * CC) + DD;
			   float xx[]=null, yy[]=null;
			   int num=0;
			   if(isZero(rr)) // no absolute term: y(y^3 + py + q) = 0
			   {
			      xx=solve(1.f, 0.f, pp, qq);
			      if (xx==null) return null;
			      num=xx.length;
			      yy=new float[num+1];
			      System.arraycopy(xx, 0, yy, 0, num);
			      yy[num++] = 0.f;
			   }
			   else
			   {
			      // solve the resolvent cubic
			      float qq2 = qq * qq;   //TODO won't work if 2 results and xx[0] is expected smallest
			      xx=solve(1.f, (-1.f/2.f) * pp, -rr, ((1.f/2.f) * rr * pp) - ((1.f/8.f) * qq2));
			      if (xx==null) return null;
			      float zz = xx[0];

			      // build 2 quadratic equations from the one solution
			      float uu = (zz * zz) - rr;
			      float vv = (2.f * zz) - pp;

			      if (isZero(uu))
			         uu = 0.f;
			      else if (uu > 0.f)
			         uu = (float) Math.sqrt(uu);
			      else return null;

			      if (isZero(vv))
			         vv = 0.f;
			      else if (vv > 0.f)
			         vv = (float) Math.sqrt(vv);
			      else return null;

			      // solve the two quadratics
			      xx=solve(1.f, vv, zz-uu);
			      float zzz[]=solve(1.f, -vv, zz+uu);
			      if (zzz==null) yy=xx;
			      else {
			    	  yy=new float[xx.length+zzz.length];
			    	  System.arraycopy(xx,0,yy,0,xx.length);
			    	  System.arraycopy(zzz, 0, yy, xx.length, zzz.length);
			      }
			   }
			// resubstitute
			float subb = (1.f/4.f) * AA;
			for (int i=0; i<yy.length; i++) yy[i] -= subb;
			return yy;
		} //switch
		return null;
	}
	public static float[] derivative(float... x) {
		if (x==null||x.length==1) return null;
		float[] y=new float[x.length-1];
		for (int i=0,j=x.length-1; i<y.length; i++) y[i]=x[i]*j--;
		return y;
	}
	/*public static void main(String args[]) {
		System.out.println("5x-32=0, x="+Arrays.toString(solve(5,-32)));
		System.out.println("0x^2+5x-32=0, x="+Arrays.toString(solve(0,5,-32)));
		System.out.println("x^2+2x+1=0, x="+Arrays.toString(solve(1,2,1)));
		System.out.println("2x^2+8x-3=0, x="+Arrays.toString(solve(2,8,-3)));
		System.out.println("x^3-6x^2+12x-8=0, x="+Arrays.toString(solve(1,-6,12,-8)));
		System.out.println("x^3-9x^2+26x-24=0, x="+Arrays.toString(solve(1,-9,26,-24)));
		System.out.println("x^3-7x^2+16x-12=0, x="+Arrays.toString(solve(1,-7,16,-12)));
		System.out.println("x^4+x^3+x^2+x-4=0, x="+Arrays.toString(solve(1,1,1,1,-4)));
		System.out.println("2x^4+6.7x^3+1.7x^2-5.5x-1.3=0, x="+Arrays.toString(solve(2,6.7f,1.7f,-5.5f,-1.3f)));
		System.out.println("2x^4+6.6x^3+1.4x^2-2.8x+3.7=0, x="+Arrays.toString(solve(2,6.6f,1.4f,-2.8f,3.7f)));
		System.out.println("8x^3+19.8x^2+2.8x-2.8=0, x="+Arrays.toString(solve(8,19.8f,2.8f,-2.8f))); //1st derivative
		System.out.println("f'(2x^4+6.6x^3+1.4x^2-2.8x+3.7)="+Arrays.toString(derivative(2,6.6f,1.4f,-2.8f,3.7f)));
	}*/
}
