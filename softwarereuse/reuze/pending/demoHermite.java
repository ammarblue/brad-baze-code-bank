package reuze.pending;

/*******************************************************************************
*
* File    :    HermiteDemo.java
*
* Author  :    Joseph R. Weber
*
* Purpose :    Performs some simple tests on the Hermite class.
*
******************************************************************************/

//package org.proteinshader.math;

import java.text.NumberFormat;
import java.text.DecimalFormat;

import com.software.reuze.gb_Vector3;

import processing.core.PApplet;


//All subsequent comments beginning with "/**" and ending with "*/"
//are intended to be used by JavaDoc to generate an API in html form.

/*******************************************************************************
Performs some simple tests on the Hermite class.
*******************************************************************************/
public class demoHermite extends PApplet {
   /** The number of segments to divide the curve into is 10,
       so the total number of points printed will be 11,
       including the points for t = 0.0 and t = 1.0. */
   public static final int SEGMENTS = 20;

   /***************************************************************************
   Creates a start and end point (each with a tangent vector)
   to plug into a Hermite object.  A helper method is then used to
   increment parameter t from 0.0 to 1.0 while printing the
   points (and tangents) in between the start and end points.
   When graphed, these point should form a curve.
   ***************************************************************************/
   public void setup() {
	   size(400,400,P3D);
       // Announce purpose of tests.
       System.out.println( "\nHERMITE INTERPOLATION TESTS:\n\n"
         + "The constructor for class Hermite expects a start point,"
         + "\nan end point, a start tangent, and an end tangent.\n"
         + "Coefficients for a cubic equation are than calculated\n"
         + "by the constructor.  The calculatePoint(t) method\n"
         + "expects a value of t between 0.0 and 1.0, and it will\n"
         + "return an interpolated point on the curve between\n"
         + "the start and end points.  A calculateTangent(t)\n"
         + "method is also provided.  This demo class tests these\n"
         + "methods.\n\n"
         + "Points and tangents are given in the form (x, y, z).");

       // Create a start and end point in the xy-plane.
       gb_Vector3 p1 = new gb_Vector3( 50, 100, 0 ),
               p2 = new gb_Vector3( 200, 300, 0 );
       gb_Vector3 tan1 = new gb_Vector3( -360, 400, 0 ),
             tan2 = new gb_Vector3( 0, -400, 0 );

       // Print the start point, end point, and Hermite curve.
       printStartAndEndPoints( p1, p2, tan1, tan2 );
       h1=new Hermite( p1, p2, tan1, tan2 );
       printCurve( h1 );
   }
   Hermite h1;
   public void draw() {
	   background(-1);
	   lights();
	   drawCurve(h1);
   }

   /***************************************************************************
   Prints the start and end points along with their tangents.

   @param p1    the start point
   @param p2    the end point
   @param tan1  the tangent of the start point.
   @param tan2  the tangent of the end point.
   ***************************************************************************/
   public static void printStartAndEndPoints( gb_Vector3 p1, gb_Vector3 p2,
                                              gb_Vector3 tan1, gb_Vector3 tan2)
   {
       System.out.println(
           "\nStart Point: " + p1 + " with tangent " + tan1 + "\n"
         + "End Point:   " + p2 + " with tangent " + tan2 + "\n" );
   }

   /***************************************************************************
   Uses the Hermite object to obtain and print interpolated points
   from parameter t = 0.0 to t = 1.0.

   <br/><br/>
   The total number of points is SEGMENTS + 1, and each point is
   printed along with its tangent.

   @param hermite  a Hermite object with a start and end point.
   ***************************************************************************/
   public static void printCurve( Hermite hermite )
   {
       NumberFormat formatter = new DecimalFormat( "0.00" );

       // Print points from t = 0.0 to t = 1.0.
       for( int i = 0; i <= SEGMENTS; ++i ) {
           double t = i / (double)SEGMENTS;

           System.out.println( "t = " + formatter.format( t )
               + ": " + hermite.calculatePoint( t )
               + " with tangent " + hermite.calculateTangent( t ) );
       }
   }
   public void drawCurve( Hermite hermite )
   {
       stroke(255,0,0);
       beginShape();
	   // draw points from t = 0.0 to t = 1.0.
       for( int i = 0; i <= SEGMENTS; ++i ) {
           double t = i / (double)SEGMENTS;
           gb_Vector3 v=hermite.calculatePoint(t);
           vertex(v.x,v.y,v.z);
       }
       endShape();
   }
}