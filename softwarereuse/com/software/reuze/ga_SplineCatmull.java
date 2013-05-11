package com.software.reuze;
//A Catmull spline passes through all points given from first to last.
//The code normalizes travel to time values of 0.0 to 1.0
//Initialize can be called with a time array to require arrival times at each point
//Call evaluate(time) to get the position on the curve at that instant
//The distance traveled and instantaneous velocity/acceleration may also be accessed.
public class ga_SplineCatmull {

	   static final float smX[]=new float[]{0.0000000000f, 0.5384693101f, -0.5384693101f, 0.9061798459f, -0.9061798459f};
	   static final float smC[]=new float[]{0.5688888889f, 0.4786286705f, 0.4786286705f, 0.2369268850f, 0.2369268850f};  

	   /// The time to arrive at each point.
	   float mTimes[];

	   /// the length of each curve segment.
	   float mLengths[];

	   /// The total length of curve.
	   float mTotalLength;

	   /// The number of points and times.
	   int mCount;

	   /// The sample points.
	   ga_Vector2 mPositions[]; 


	public ga_SplineCatmull() {   
	}

	public void clear() {
	   mPositions = null;
	   mTimes=null;
	   mLengths=null;
	   mTotalLength=0;
	   mCount=0;
	}
	public static boolean isZero(float x) {
		return Math.abs(x)<1.0e-7f;
	}
	public void initialize( final ga_Vector2 positions[], final float times[] ) {
	   int count=positions.length;
	   if (positions==null) throw new RuntimeException("CatmullRom::initialize - Got null position!" );
	   if ( count < 3 ) throw new RuntimeException("CatmullRom::initialize - Must have more than 2 points!" );

	   // Clean up any previous state.
	   clear();

	   // copy the points.
	   mPositions = new ga_Vector2[count];
	   for ( int i = 0; i < count; ++i )
	      mPositions[i] = new ga_Vector2(positions[i]);
	   
	// set up arrays
	   mTimes = new float[count];
	   mCount = count;
	   
	// set up curve segment lengths
	   mLengths = new float[count-1];
	   mTotalLength = 0.0f;
	   for ( int i = 0; i < count-1; ++i )
	   {
	      mLengths[i] = segmentArcLength(i, 0.0f, 1.0f);
	      mTotalLength += mLengths[i];
	   }
	   
	// copy the times if we have them.
	//TODO doesn't check times <0 or >1 or that they are in increasing order
	   float l = 0.0f;
	   for ( int i = 0; i < count; ++i )
	   {
	      if ( times != null )
	         mTimes[i] = times[i];
	      else
	      {
	         if ( isZero( mTotalLength ) )
	            mTimes[i] = 0.0f;
	         else
	            mTimes[i] = l / mTotalLength;
	         if ( i < count-1 )
	            l += mLengths[i];
	      }
	   }
	}

	public ga_Vector2 evaluate( float t ) {
	   if (mCount < 2) throw new RuntimeException("CatmullRom::evaluate - Not initialized!" );

	   // handle boundary conditions
	   if ( t <= mTimes[0] )
	      return mPositions[0];
	   else if ( t >= mTimes[mCount-1] )
	      return mPositions[mCount-1];

	   // find segment and parameter
	   int i;  // segment #
	   for ( i = 0; i < mCount-1; ++i )
	   {
	      if ( t <= mTimes[i+1] )
	      {
	         break;
	      }
	   }

	   if ( !(i < mCount) )  throw new RuntimeException("CatmullRom::evaluate - Got bad index!" );

	   float t0 = mTimes[i];
	   float t1 = mTimes[i+1];
	   float u = (t - t0)/(t1 - t0);
	      
	   int idx0, idx1, idx2, idx3;
	   idx0 = i - 1;
	   idx1 = i;
	   idx2 = i + 1;
	   idx3 = i + 2;

	   if ( idx0 < 0 )
	      idx0 = 0;
	   if ( idx3 >= mCount )
	      idx3 = mCount - 1;
	   ga_Vector2 x = mPositions[idx2].tmp2().mul(3f);
	   ga_Vector2 A = mPositions[idx1].tmp().mul(3f).sub(mPositions[idx0]).add(mPositions[idx3]).sub(x);
	   x=mPositions[idx1].tmp2().mul(5f);
	   ga_Vector2 B = mPositions[idx0].copy().mul(2f).sub(mPositions[idx3]).sub(x);
	   B.add(mPositions[idx2].tmp2().mul(4f));

	   ga_Vector2 C = mPositions[idx2].copy().sub(mPositions[idx0]);
       A.mul(u).add(B).mul(u*u*0.5f);
	   x=C.copy().mul(0.5f*u).add(mPositions[i]).add(A);
	   return x;
	}

	public ga_Vector2 velocity( float t ) {
	   if ( mCount < 2) throw new RuntimeException("CatmullRom::velocity - Not initialized!" );

	   // handle boundary conditions
	   if ( t <= mTimes[0] )
	      t = 0.0f;
	   else if ( t > mTimes[mCount-1] )
	      t = mTimes[mCount-1];

	   // find segment and parameter
	   int i;
	   for ( i = 0; i < mCount-1; ++i )
	   {
	      if ( t <= mTimes[i+1] )
	      {
	         break;
	      }
	   }
	   float t0 = mTimes[i];
	   float t1 = mTimes[i+1];
	   float u = (t - t0)/(t1 - t0);

	   int idx0, idx1, idx2, idx3;
	   idx0 = i - 1;
	   idx1 = i;
	   idx2 = i + 1;
	   idx3 = i + 2;

	   if ( idx0 < 0 )
	      idx0 = 0;
	   if ( idx3 >= mCount )
	      idx3 = mCount - 1;
	   
	   ga_Vector2 x = mPositions[idx2].tmp2().mul(3f);
	   ga_Vector2 A = mPositions[idx1].tmp().mul(3f).sub(mPositions[idx0]).add(mPositions[idx3]).sub(x);
	   
	   x=mPositions[idx1].tmp2().mul(5f);
	   ga_Vector2 B = mPositions[idx0].copy().mul(2f).sub(mPositions[idx3]).sub(x);
	   B.add(mPositions[idx2].tmp2().mul(4f));

	   ga_Vector2 C = mPositions[idx2].copy().sub(mPositions[idx0]);
	   
       A.mul(u*1.5f).add(B).mul(u);
	   x=C.copy().mul(0.5f).add(A);
	   return x;
	}

	public ga_Vector2 acceleration( float t ) {
	   if ( mCount < 2) throw new RuntimeException("CatmullRom::acceleration - Not initialized!" );

	   // handle boundary conditions
	   if ( t <= mTimes[0] )
	      t = 0.0f;
	   else if ( t > mTimes[mCount-1] )
	      t = mTimes[mCount-1];

	   // find segment and parameter
	   int i;
	   for ( i = 0; i < mCount-1; ++i )
	   {
	      if ( t <= mTimes[i+1] )
	      {
	         break;
	      }
	   }
	   float t0 = mTimes[i];
	   float t1 = mTimes[i+1];
	   float u = (t - t0)/(t1 - t0);

	   int idx0, idx1, idx2, idx3;
	   idx0 = i - 1;
	   idx1 = i;
	   idx2 = i + 1;
	   idx3 = i + 2;

	   if ( idx0 < 0 )
	      idx0 = 0;
	   if ( idx3 >= mCount )
	      idx3 = mCount - 1;

	   ga_Vector2 x = mPositions[idx2].tmp2().mul(3f);
	   ga_Vector2 A = mPositions[idx1].tmp().mul(3f).sub(mPositions[idx0]).add(mPositions[idx3]).sub(x);
	   
	   x=mPositions[idx1].tmp2().mul(5f);
	   ga_Vector2 B = mPositions[idx0].copy().mul(2f).sub(mPositions[idx3]).sub(x);
	   B.add(mPositions[idx2].tmp2().mul(4f));

	   ga_Vector2 C = mPositions[idx2].copy().sub(mPositions[idx0]);
	   
       A.mul(u*3f);
	   return B.copy().add(A);
	}

	public ga_Vector2 getPosition( int idx ) {
	   if ( idx < 0 || idx >= mCount-1) throw new RuntimeException("CatmullRom<>::getPosition - Got bad index!" );
	   return mPositions[idx];
	}
	
	public float getSegmentLength( int idx ) {
		   if ( idx < 0 || idx >= mCount-1) throw new RuntimeException("CatmullRom<>::getSegmentLength - Got bad index!" );
		   return mLengths[idx];
		}

	public float segmentArcLength( int i, float u1, float u2 )
	{
	   if ( i < 0 || i >= mCount-1) throw new RuntimeException("CatmullRom<>::getPosition - Got bad index!" );

	   if ( u2 <= u1 )
	      return 0.0f;

	   if ( u1 < 0.0f )
	      u1 = 0.0f;

	   if ( u2 > 1.0f )
	      u2 = 1.0f;

	   int idx0, idx1, idx2, idx3;
	   idx0 = i - 1;
	   idx1 = i;
	   idx2 = i + 1;
	   idx3 = i + 2;

	   if ( idx0 < 0 )
	      idx0 = 0;
	   if ( idx3 >= mCount )
	      idx3 = mCount - 1;

	   ga_Vector2 x = mPositions[idx2].tmp2().mul(3f);
	   ga_Vector2 A = mPositions[idx1].copy().mul(3f).sub(mPositions[idx0]).add(mPositions[idx3]).sub(x);
	   
	   x=mPositions[idx1].tmp2().mul(5f);
	   ga_Vector2 B = mPositions[idx0].copy().mul(2f).sub(mPositions[idx3]).sub(x);
	   B.add(mPositions[idx2].tmp2().mul(4f));

	   ga_Vector2 C = mPositions[idx2].copy().sub(mPositions[idx0]);

	   float sum = 0.0f;

	   for ( int j = 0; j < 5; ++j )
	   {
	      float u = 0.5f*((u2 - u1)*smX[j] + u2 + u1);
	      ga_Vector2 derivative;
	      if ( i == 0 || i >= mCount-2)
	         derivative = B.tmp().mul(0.5f).add(A.tmp2().mul(u));
	      else {
	    	 derivative=B.tmp2().mul(u);
	    	 derivative.add(A.tmp().mul(u*u*1.5f));
	         derivative.add(C.tmp().mul(0.5f));
	      }
	      sum += smC[j]*derivative.len();
	   }
	   sum *= 0.5f*(u2-u1);

	   return sum;
	}
	
	public float arcLength( float t1, float t2 )
	{
		   if ( t2 <= t1 )
		      return 0.0f;

		   if ( t1 < mTimes[0] )
		      t1 = mTimes[0];

		   if ( t2 > mTimes[mCount-1] )
		      t2 = mTimes[mCount-1];

		   // find segment and parameter
		   int seg1;
		   for ( seg1 = 0; seg1 < mCount-1; ++seg1 )
		   {
		      if ( t1 <= mTimes[seg1+1] )
		      {
		         break;
		      }
		   }
		   float u1 = (t1 - mTimes[seg1])/(mTimes[seg1+1] - mTimes[seg1]);

		   // find segment and parameter
		   int seg2;
		   for ( seg2 = 0; seg2 < mCount-1; ++seg2 )
		   {
		      if ( t2 <= mTimes[seg2+1] )
		      {
		         break;
		      }
		   }
		   float u2 = (t2 - mTimes[seg2])/(mTimes[seg2+1] - mTimes[seg2]);

		   float result;
		   // both parameters lie in one segment
		   if ( seg1 == seg2 )
		   {
		      result = segmentArcLength( seg1, u1, u2 );
		   }
		   // parameters cross segments
		   else
		   {
		      result = segmentArcLength( seg1, u1, 1.0f );
		      for ( int i = seg1+1; i < seg2; ++i )
		         result += mLengths[i];
		      result += segmentArcLength( seg2, 0.0f, u2 );
		   }

		   return result;
		}
	
	public int getPrevNode( float t )
	{
		   if ( mCount < 2 ) throw new RuntimeException("CatmullRomBase::getPrevNode - Bad point count!" );

		   // handle boundary conditions
		   if ( t <= mTimes[0] )
		      return 0;
		   else if ( t >= mTimes[mCount-1] )
		      return mCount-1;

		   // find segment and parameter
		   int i;  // segment #
		   for ( i = 0; i < mCount-1; ++i )
		   {
		      if ( t <= mTimes[i+1] )
		         break;
		   }

		   if ( i >= 0 && i < mCount) throw new RuntimeException("CatmullRomBase::getPrevNode - Got bad output index!" );

		   return i;   
		}

	public float getTime( int idx )
	{
	   if ( idx < 0 && idx >= mCount) throw new RuntimeException("CatmullRomBase::getTime - Got bad index!" );
	   return mTimes[idx];
	}
	
	/// Get the total length of the curve.
	   public float getLength() { return mTotalLength; }
}
//-----------------------------------------------------------------------------
//Copyright (c) 2012 GarageGames, LLC
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to
//deal in the Software without restriction, including without limitation the
//rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
//sell copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
//IN THE SOFTWARE.
//-----------------------------------------------------------------------------
