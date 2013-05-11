package com.software.reuze;
/**
  * Copyright John E. Lloyd, 2004. All rights reserved. Permission to use,
  * copy, modify and redistribute is granted, provided that this copyright
  * notice is retained and the author is given credit whenever appropriate.
  *
  * This  software is distributed "as is", without any warranty, including 
  * any implied warranty of merchantability or fitness for a particular
  * use. The author assumes no responsibility for, and shall not be liable
  * for, any special, indirect, or consequential damages, or any damages
  * whatsoever, arising out of or in connection with the use of this
  * software.
  */
import java.util.*;
import java.io.*;


/**
 * Testing class for QuickHull3D. Running the command
 * <pre>
 *   java quickhull3d.QuickHull3DTest
 * </pre>
 * will cause QuickHull3D to be tested on a number of randomly
 * choosen input sets, with degenerate points added near
 * the edges and vertics of the convex hull.
 *
 * <p>The command
 * <pre>
 *   java quickhull3d.QuickHull3DTest -timing
 * </pre>
 * will cause timing information to be produced instead.
 *
 * @author John E. Lloyd, Fall 2004
 */
public class gb_QuickHullTest
{
	static private final float DOUBLE_PREC = 0.1e-7f; //2.2204460492503131e-16;

	static boolean triangulate = false;
	static boolean doTesting = true;
	static boolean doTiming = false;

	static boolean debugEnable = false;

	static final int NO_DEGENERACY = 0;
	static final int EDGE_DEGENERACY = 1;
	static final int VERTEX_DEGENERACY = 2;

	Random rand; // random number generator

	static boolean testRotation = true;
	static int degeneracyTest = VERTEX_DEGENERACY;
	static float epsScale = 2.0f;

	/**
	 * Creates a testing object.
	 */
	public gb_QuickHullTest()
	 { 
	   rand = new Random();
	   rand.setSeed (0x1234);
	 }

	/**
	 * Returns true if two face index sets are equal,
	 * modulo a cyclical permuation.
	 *
	 * @param indices1 index set for first face
	 * @param indices2 index set for second face
	 * @return true if the index sets are equivalent
	 */
	public boolean faceIndicesEqual (int[] indices1, int[] indices2)
	 {
	   if (indices1.length != indices2.length)
	    { return false;
	    }
	   int len = indices1.length;
	   int j;
	   for (j=0; j<len; j++)
	    { if (indices1[0] == indices2[j])
	       { break; 
	       }
	    }
	   if (j==len)
	    { return false;
	    } 
	   for (int i=1; i<len; i++)
	    { if (indices1[i] != indices2[(j+i)%len])
	       { return false; 
	       }
	    }
	   return true;	   
	 }

	/**
	 * Returns the coordinates for <code>num</code> points whose x, y, and
	 * z values are randomly chosen within a given range.
	 *
	 * @param num number of points to produce
	 * @param range coordinate values will lie between -range and range
	 * @return array of coordinate values
	 */
	public float[] randomPoints (int num, float range)
	 {
	   float[] coords = new float[num*3];

	   for (int i=0; i<num; i++)
	    { for (int k=0; k<3; k++)
	       { coords[i*3+k] = (float) (2*range*(rand.nextDouble()-0.5));
	       }
	    }
	   return coords;
	 }
	
	private void randomlyPerturb (gb_Vector3 pnt, float tol)
	 {
	   pnt.x += tol*(rand.nextDouble()-0.5);
	   pnt.y += tol*(rand.nextDouble()-0.5);
	   pnt.z += tol*(rand.nextDouble()-0.5);
	 }

	/**
	 * Returns the coordinates for <code>num</code> randomly
	 * chosen points which are degenerate which respect
	 * to the specified dimensionality.
	 *
	 * @param num number of points to produce
	 * @param dimen dimensionality of degeneracy: 0 = coincident,
	 * 1 = colinear, 2 = coplaner.
	 * @return array of coordinate values
	 */
	public float[] randomDegeneratePoints (int num, int dimen)
	 { 
	   float[] coords= new float[num*3];
	   gb_Vector3 pnt = new gb_Vector3();

	   gb_Vector3 base = new gb_Vector3();
	   base.setRandom (-1, 1, rand);

	   float tol = DOUBLE_PREC;

	   if (dimen == 0)
	    { for (int i=0; i<num; i++)
	       { pnt.set (base);
		 randomlyPerturb (pnt, tol);
		 coords[i*3+0] = pnt.x;
	         coords[i*3+1] = pnt.y;
	         coords[i*3+2] = pnt.z;
	       }
	    }
	   else if (dimen == 1)
	    { gb_Vector3 u = new gb_Vector3();
	      u.setRandom (-1, 1, rand);
	      u.nor();
	      for (int i=0; i<num; i++)
	       { float a = (float) (2*(rand.nextDouble()-0.5));
		 pnt.set(u).mul(a);
		 pnt.add (base);
		 randomlyPerturb (pnt, tol);
		 coords[i*3+0] = pnt.x;
	         coords[i*3+1] = pnt.y;
	         coords[i*3+2] = pnt.z;
	       }
	    }
	   else // dimen == 2
	    { gb_Vector3 nrm = new gb_Vector3();
	      nrm.setRandom (-1, 1, rand);
	      nrm.nor();
	      for (int i=0; i<num; i++)
	       { // compute a random point and project it to the plane
		 gb_Vector3 perp = new gb_Vector3();
		 pnt.setRandom (-1, 1, rand);
		 perp.set(nrm).mul(pnt.dot(nrm));
		 pnt.sub (perp);
		 pnt.add (base);
		 randomlyPerturb (pnt, tol);
		 coords[i*3+0] = pnt.x;
	         coords[i*3+1] = pnt.y;
	         coords[i*3+2] = pnt.z;
	       }
	    }
	   return coords;
	 }

	/**
	 * Returns the coordinates for <code>num</code> points whose x, y, and
	 * z values are randomly chosen to lie within a sphere.
	 *
	 * @param num number of points to produce
	 * @param radius radius of the sphere
	 * @return array of coordinate values
	 */
	public float[] randomSphericalPoints (int num, float radius)
	 {
	   float[] coords = new float[num*3];
	   gb_Vector3 pnt = new gb_Vector3();

	   for (int i=0; i<num; )
	    { pnt.setRandom (-radius, radius, rand);
	      if (pnt.len() <= radius)
	       { coords[i*3+0] = pnt.x;
	         coords[i*3+1] = pnt.y;
	         coords[i*3+2] = pnt.z;
		 i++;
	       }
	    }
	   return coords;
	 }

	/**
	 * Returns the coordinates for <code>num</code> points whose x, y, and
	 * z values are each randomly chosen to lie within a specified
	 * range, and then clipped to a maximum absolute
	 * value. This means a large number of points
	 * may lie on the surface of cube, which is useful
	 * for creating degenerate convex hull situations.
	 *
	 * @param num number of points to produce
	 * @param range coordinate values will lie between -range and
	 * range, before clipping
	 * @param max maximum absolute value to which the coordinates
	 * are clipped
	 * @return array of coordinate values
	 */
	public float[] randomCubedPoints (int num, float range, float max)
	 {
	   float[] coords = new float[num*3];

	   for (int i=0; i<num; i++)
	    { for (int k=0; k<3; k++)
	       { float x = (float) (2*range*(rand.nextDouble()-0.5));
		 if (x > max)
		  { x = max;
		  }
		 else if (x < -max)
		  { x = -max; 
		  }
		 coords[i*3+k] = x;
	       }
	    }
	   return coords;
	 }

	private float[] shuffleCoords (float[] coords)
	 {
	   int num = coords.length/3;

	   for (int i=0; i<num; i++)
	    { int i1 = rand.nextInt (num); 
	      int i2 = rand.nextInt (num);
	      for (int k=0; k<3; k++)
	       { float tmp = coords[i1*3+k];
		 coords[i1*3+k] = coords[i2*3+k];
		 coords[i2*3+k] = tmp;
	       }
	    }
	   return coords;
	 }

	/**
	 * Returns randomly shuffled coordinates for points on a
	 * three-dimensional grid, with a presecribed width between each point.
	 *
	 * @param gridSize number of points in each direction,
	 * so that the total number of points produced is the cube of
	 * gridSize.
	 * @param width distance between each point along a particular
	 * direction
	 * @return array of coordinate values
	 */
	public float[] randomGridPoints (int gridSize, float width)
	 {
	   // gridSize gives the number of points across a given dimension
	   // any given coordinate indexed by i has value
	   // (i/(gridSize-1) - 0.5)*width

	   int num = gridSize*gridSize*gridSize;

	   float[] coords = new float[num*3];

	   int idx = 0;
	   for (int i=0; i<gridSize; i++)
	    { for (int j=0; j<gridSize; j++)
	       { for (int k=0; k<gridSize; k++)
		  { coords[idx*3+0] = (i/(float)(gridSize-1) - 0.5f)*width;
		    coords[idx*3+1] = (j/(float)(gridSize-1) - 0.5f)*width;
		    coords[idx*3+2] = (k/(float)(gridSize-1) - 0.5f)*width;
		    idx++;
		  }
	       }
	    }
	   shuffleCoords (coords);
	   return coords;
	 }

	void explicitFaceCheck (gb_QuickHull hull, int[][] checkFaces)
	   throws Exception
	 { 
	   int [][] faceIndices = hull.getFaces();
	   if (faceIndices.length != checkFaces.length)
	    { throw new Exception (
"Error: " + faceIndices.length + " faces vs. " + checkFaces.length);
	    }
	   // translate face indices back into original indices
	   gb_Vector3[] pnts = hull.getVertices();
	   int[] vtxIndices = hull.getVertexPointIndices();

	   for (int j=0; j<faceIndices.length; j++)
	    { int[] idxs = faceIndices[j];
	      for (int k=0; k<idxs.length; k++)
	       { idxs[k] = vtxIndices[idxs[k]];
	       }
	    }
	   for (int i=0; i<checkFaces.length; i++)
	    { int[] cf = checkFaces[i];
	      int j;
	      for (j=0; j<faceIndices.length; j++)
	       { if (faceIndices[j] != null)
		  { if (faceIndicesEqual (cf, faceIndices[j]))
		     { faceIndices[j] = null;
		       break;
		     }
		  }
	       }
	      if (j == faceIndices.length)
	       { String s = "";
		 for (int k=0; k<cf.length; k++)
		  { s += cf[k] + " ";
		  }
		 throw new Exception ("Error: face " + s + " not found");
	       }
	    }
	 }

	int cnt = 0;

	void singleTest (float[] coords, int[][] checkFaces)
	   throws Exception
	 {
	   gb_QuickHull hull = new gb_QuickHull ();
	   hull.setDebug (debugEnable);

	   hull.build (coords, coords.length/3);
	   if (triangulate)
	    { hull.triangulate();
	    }

	   if (!hull.check(System.out))
	    { (new Throwable()).printStackTrace();
	      System.exit(1); 
	    }
	   if (checkFaces != null)
	    { explicitFaceCheck (hull, checkFaces); 
	    }
	   if (degeneracyTest != NO_DEGENERACY)
	    { degenerateTest (hull, coords);
	    }
	 }

	float[] addDegeneracy (
	   int type, float[] coords, gb_QuickHull hull)
	 { 
	   int numv = coords.length/3;
	   int[][] faces = hull.getFaces();
	   float[] coordsx = new float[coords.length+faces.length*3];
	   for (int i=0; i<coords.length; i++)
	    { coordsx[i] = coords[i]; 
	    }

	   float[] lam = new float[3];
	   double eps = hull.getDistanceTolerance();

	   for (int i=0; i<faces.length; i++)
	    { 
	      // random point on an edge
	      lam[0] = rand.nextFloat();
	      lam[1] = 1-lam[0];
	      lam[2] = 0.0f;	      
	  
	      if (type == VERTEX_DEGENERACY && (i%2 == 0))
	       { lam[0] = 1.0f;
		 lam[1] = lam[2] = 0;
	       }

	      for (int j=0; j<3; j++)
	       { int vtxi = faces[i][j];
		 for (int k=0; k<3; k++)
		  { coordsx[numv*3+k] +=
		       lam[j]*coords[vtxi*3+k] +
		       epsScale*eps*(rand.nextDouble()-0.5);
		  }
	       }
	      numv++;
	    }
	   shuffleCoords (coordsx);
	   return coordsx;
	 }
	
	void degenerateTest (gb_QuickHull hull, float[] coords)
	   throws Exception
	 {
	   float[] coordsx = addDegeneracy (degeneracyTest, coords, hull);

	   gb_QuickHull xhull = new gb_QuickHull();
	   xhull.setDebug (debugEnable);

	   try
	    { xhull.build (coordsx, coordsx.length/3);
	      if (triangulate)
	       { xhull.triangulate();
	       }
	    }
	   catch (Exception e) 
	    { for (int i=0; i<coordsx.length/3; i++)
  	       { System.out.println (
  		    coordsx[i*3+0]+", "+
  		    coordsx[i*3+1]+", "+
  		    coordsx[i*3+2]+", ");
  	       } 
	    }

	   if (!xhull.check(System.out))
	    { (new Throwable()).printStackTrace();
	      //System.exit(1);
	    }
	 }

     void rotateCoords (float[] res, float[] xyz,
			      float roll, float pitch, float yaw)
	 {
	   float sroll = (float) Math.sin (roll);
	   float croll = (float) Math.cos (roll);
	   float spitch = (float) Math.sin (pitch);
	   float cpitch = (float) Math.cos (pitch);
	   float syaw = (float) Math.sin (yaw);
	   float cyaw = (float) Math.cos (yaw);

	   float m00 = croll * cpitch;
	   float m10 = sroll * cpitch;
	   float m20 = - spitch;

	   float m01 = croll * spitch * syaw - sroll * cyaw;
	   float m11 = sroll * spitch * syaw + croll * cyaw;
	   float m21 = cpitch * syaw;

	   float m02 = croll * spitch * cyaw + sroll * syaw;
	   float m12 = sroll * spitch * cyaw - croll * syaw;
	   float m22 = cpitch * cyaw;

	   //float x, y, z;

	   for (int i=0; i<xyz.length-2; i+=3)
	    {
	      res[i+0] = m00*xyz[i+0] + m01*xyz[i+1] + m02*xyz[i+2];
	      res[i+1] = m10*xyz[i+0] + m11*xyz[i+1] + m12*xyz[i+2];
	      res[i+2] = m20*xyz[i+0] + m21*xyz[i+1] + m22*xyz[i+2];
	    }
	 }

	void printCoords (float[] coords)
	 {
	   int nump = coords.length/3;
	   for (int i=0; i<nump; i++)
	    { System.out.println (
		 coords[i*3+0]+", "+
		 coords[i*3+1]+", "+
		 coords[i*3+2]+", ");
	    } 
	 }

	void testException (float[] coords, String msg)
	 {
	   gb_QuickHull hull = new gb_QuickHull();
	   Exception ex = null;
	   try
	    { hull.build(coords); 
	    }
	   catch (Exception e)
	    { ex = e; 
	    }
	   if (ex == null)
	    { System.out.println ("Expected exception " + msg);
	      System.out.println ("Got no exception");
	      System.out.println ("Input pnts:");
	      printCoords (coords);
	      System.exit(1); 
	    }
	   else if (ex.getMessage() == null ||
		    !ex.getMessage().equals(msg))
	    { System.out.println ("Expected exception " + msg);
	      System.out.println ("Got exception " + ex.getMessage());
	      System.out.println ("Input pnts:");
	      printCoords (coords);
	      System.exit(1);
	    }
	 }

	void test (float[] coords, int[][] checkFaces)
	   throws Exception
	 { 
	   float[][] rpyList = new float[][]
	    { 
	      {  0,  0,  0},
	      { 10, 20, 30},
	      { -45, 60, 91},
	      { 125, 67, 81}
	    };
	   float[] xcoords = new float[coords.length];

	   singleTest (coords, checkFaces);
	   if (testRotation)
	    { 
	      for (int i=0; i<rpyList.length; i++)
	       { float[] rpy = rpyList[i]; 
		 rotateCoords (xcoords, coords,
			       (float)Math.toRadians(rpy[0]),
			       (float)Math.toRadians(rpy[1]),
			       (float)Math.toRadians(rpy[2]));
		 singleTest (xcoords, checkFaces);
	       }
	    }
	 }

	/**
	 * Runs a set of explicit and random tests on QuickHull3D,
	 * and prints <code>Passed</code> to System.out if all is well.
	 */
	public void explicitAndRandomTests()
	 { 
	   try
	    { 
	      float[] coords = null;

	      System.out.println (
"Testing degenerate input ...");
	      for (int dimen=0; dimen<3; dimen++)
	       { for (int i=0; i<10; i++)
		  { coords = randomDegeneratePoints (10, dimen);
		    if (dimen == 0)
		     { testException (
			  coords, "Input points appear to be coincident");
		     }
		    else if (dimen == 1)
		     { testException (
			  coords, "Input points appear to be colinear"); 
		     }
		    else if (dimen == 2)
		     { testException (
			  coords, "Input points appear to be coplanar");  
		     }
		  }
	       }

	      System.out.println (
"Explicit tests ...");

	      // test cases furnished by Mariano Zelke, Berlin
	      coords = new float[]
		 { 21, 0, 0,
		   0, 21, 0,
		   0, 0, 0,
		   18, 2, 6,
		   1, 18, 5,
		   2, 1, 3,
		   14, 3, 10,
		   4, 14, 14,
		   3, 4, 10,
		   10, 6, 12,
		   5, 10, 15,
		 };
	      test (coords, null);

	      coords = new float[]
		 {
		   0.0f , 0.0f , 0.0f,
		   21.0f, 0.0f, 0.0f,
		   0.0f, 21.0f, 0.0f,
		   2.0f, 1.0f, 2.0f,
		   17.0f, 2.0f, 3.0f,
		   1.0f, 19.0f, 6.0f,
		   4.0f, 3.0f, 5.0f,
		   13.0f, 4.0f, 5.0f,
		   3.0f, 15.0f, 8.0f,
		   6.0f, 5.0f, 6.0f,
		   9.0f, 6.0f, 11.0f,
		 };
	      test (coords, null);
	   
	      System.out.println (
"Testing 20 to 200 random points ...");
	      for (int n=20; n<200; n+=10)
	       { System.out.println (n);
		 for (int i=0; i<10; i++)
		  { coords = randomPoints (n, 1.0f);
		    test (coords, null);
		  }
	       }

	      System.out.println (
"Testing 20 to 200 random points in a sphere ...");
	      for (int n=20; n<200; n+=10)
	       { System.out.println (n);
		 for (int i=0; i<10; i++)
		  { coords = randomSphericalPoints (n, 1.0f);
		    test (coords, null);
		  }
	       }

	      System.out.println (
"Testing 20 to 200 random points clipped to a cube ...");
	      for (int n=20; n<200; n+=10)
	       { System.out.println (n);
		 for (int i=0; i<10; i++)
		  { coords = randomCubedPoints (n, 1.0f, 0.5f);
		    test (coords, null);
		  }
	       }

	      System.out.println (
"Testing 8 to 1000 randomly shuffled points on a grid ...");
	      for (int n=2; n<=10; n++)
	       { // System.out.println (n*n*n);
		 for (int i=0; i<10; i++)
		  { coords = randomGridPoints (n, 4.0f);
		    test (coords, null);
		  }
	       }

	    }
	   catch (Exception e) 
	    { e.printStackTrace();
	      System.exit(1); 
	    }

	   System.out.println ("\nPassed\n");
	 }

	/**
	 * Runs timing tests on QuickHull3D, and prints
	 * the results to System.out.
	 */
	public void timingTests()
	 { 
	   long t0, t1;
	   int n = 10;
	   gb_QuickHull hull = new gb_QuickHull ();
	   System.out.println ("warming up ... ");
	   for (int i=0; i<2; i++)
	    { float[] coords = randomSphericalPoints (10000, 1.0f);
	      hull.build (coords); 
	    }
	   int cnt = 10;
	   for (int i=0; i<4; i++)
	    { n *= 10;
	      float[] coords = randomSphericalPoints (n, 1.0f);
	      t0 = System.currentTimeMillis();
	      for (int k=0; k<cnt; k++)
	       { hull.build (coords);
	       }
	      t1 = System.currentTimeMillis();
	      System.out.println (n + " points: " + (t1-t0)/(float)cnt +
				  " msec");
	    }
	 }

	/**
	 * Runs a set of tests on the QuickHull3D class, and
	 * prints <code>Passed</code> if all is well.
	 * Otherwise, an error message and stack trace
	 * are printed.
	 *
	 * <p>If the option <code>-timing</code> is supplied,
	 * then timing information is produced instead.
	 */
	public static void main (String[] args) 
	 {
	   gb_QuickHullTest tester = new gb_QuickHullTest();

	   for (int i=0; i<args.length; i++)
	    { if (args[i].equals ("-timing"))
	       { doTiming = true;
		 doTesting = false;
	       }
	      else
	       { System.out.println (
"Usage: java quickhull3d.QuickHull3DTest [-timing]");
		 System.exit(1);
	       }
	    }
	   if (doTesting)
	    { tester.explicitAndRandomTests();
	    }

	   if (doTiming)
	    { tester.timingTests();
	    }
	 }
}
