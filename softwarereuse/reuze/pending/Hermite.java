package reuze.pending;
import com.software.reuze.gb_Vector3;

/******************************************************************************
*
* Copyright 2007 Joseph Robert Weber, joe.weber@alumni.duke.edu
*
* This file is part of the ProteinShader program.
*
* The ProteinShader is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License as
* published by the Free Software Foundation; either version 3 of the
* License, or (at your option) any later version.
*
* The ProteinShader is distributed in the hope that it will be
* useful, but WITHOUT ANY WARRANTY; without even the implied
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General
* Public License along with this program.  If not, see
* <http://www.gnu.org/licenses/>.
*
******************************************************************************/

//package org.proteinshader.math;

//All subsequent comments beginning with "/**" and ending with "*/"
//are intended to be used by JavaDoc to generate an API in html form.

/*******************************************************************************
Calculates a cubic equation between two control points so that points
on the curve in between can be interpolated.

<br/><br/>
To use Hermite interpolation, the xyz-coordinate and tangent must be
known for both the start point and the end point.  Three separate
cubic equations will be determined: one for the x dimension, one for
the y dimension, and one for the z dimension.  The free variable in
each cubic equation is the parameter t.  The parameter t is 0.0 for
the start point and 1.0 for the end point.  Once the constants for the
cubic equations have been determined, a parameter t between 0.0 and
1.0 can be plugged in to get the xyz-coordinate and tangent of any
point on the curve between the start and end points.

<br/><br/>
The equations shown below are for calculating the y dimension, but the
exact same math applies to the x and z dimensions.  These equations
can be found on pages 643-645 of 'Computer Graphics Using OpenGL'
by F.S. Hill, 2nd Edition (2001).

<br/><br/>
Cubic equation for y: y(t) = Ay*t^3 + By*t^2 + Cy*t + Dy  <br/>

<br/><br/>
First derivative for y: y(t) = 3Ay*t^2 + 2By*t + Cy       <br/>

<br/><br/>
Input values for Hermite interpolation:                   <br/>
p1.y   - the y-value at the start point                   <br/>
p2.y   - the y-value at the end point                     <br/>
tan1.y - the y-value of the tangent at the start point    <br/>
tan2.y - the y-value of the tangent at the end point      <br/>

<br/><br/>
Solutions for the coefficients of the cubic equation:     <br/>
Ay = tan2.y + tan1.y - 2(p2.y - p1.y)                     <br/>
By = 3(p2.y - p1.y) - 2tan1.y - tan2.y                    <br/>
Cy = tan1.y                                               <br/>
Dy = p1.y                                                 <br/>
*******************************************************************************/
public class Hermite
{
   private final double m_Ax, m_Bx, m_Cx, m_Dx,  // x(t) coefficients
                        m_Ay, m_By, m_Cy, m_Dy,  // y(t) coefficients
                        m_Az, m_Bz, m_Cz, m_Dz;  // z(t) coefficients

   /***************************************************************************
   Constructs a Hermite object by using the input points and vectors
   to calculate and store the coefficients needed for the cubic
   equations for x(t), y(t), and z(t).

   <br/><br/>
   The input points and vectors are not modified in any way and
   references to them are not kept.

   @param p1    the start point of the cubic equation.
   @param p2    the end point of the cubic equation.
   @param tan1  the tangent vector for the start point.
   @param tan2  the tangent vector for the end point.
   ***************************************************************************/
   public Hermite( gb_Vector3 p1, gb_Vector3 p2, gb_Vector3 tan1, gb_Vector3 tan2 )
   {
       double p2x_minus_p1x = (p2.x - p1.x),
              p2y_minus_p1y = (p2.y - p1.y),
              p2z_minus_p1z = (p2.z - p1.z);

       // Calculate coefficient A for x(t), y(t), and z(t).
       m_Ax = tan2.x + tan1.x - 2*p2x_minus_p1x;
       m_Ay = tan2.y + tan1.y - 2*p2y_minus_p1y;
       m_Az = tan2.z + tan1.z - 2*p2z_minus_p1z;

       // Calculate coefficient B for x(t), y(t), and z(t).
       m_Bx = 3*p2x_minus_p1x - 2*tan1.x - tan2.x;
       m_By = 3*p2y_minus_p1y - 2*tan1.y - tan2.y;
       m_Bz = 3*p2z_minus_p1z - 2*tan1.z - tan2.z;

       // Calculate coefficient C for x(t), y(t), and z(t).
       m_Cx = tan1.x;
       m_Cy = tan1.y;
       m_Cz = tan1.z;

       // Calculate coefficient D for x(t), y(t), and z(t).
       m_Dx = p1.x;
       m_Dy = p1.y;
       m_Dz = p1.z;
   }

   /***************************************************************************
   Uses Hermite interpolation to calculate a point on the curve
   between the start and end points given the constructor.

   <br/><br/>
   The parameter t must be between 0.0 to 1.0 for interpolation.  If
   t is less than zero or greater than 1, then the result is an
   extrapolation.

   @param t  a parameter from 0.0 to 1.0.
   @return  An interpolated point.
   ***************************************************************************/
   public gb_Vector3 calculatePoint( double t )
   {
       gb_Vector3 p = new gb_Vector3();

       // Save t^2 and t^3 for reused.
       double tSquared = t * t,
              tCubed   = t * tSquared;

       // Use cubic equations to calculate x(t), y(t), and z(t).
       p.x = (float) (m_Ax*tCubed + m_Bx*tSquared + m_Cx*t + m_Dx);
       p.y = (float) (m_Ay*tCubed + m_By*tSquared + m_Cy*t + m_Dy);
       p.z = (float) (m_Az*tCubed + m_Bz*tSquared + m_Cz*t + m_Dz);

       return p;
   }

   /***************************************************************************
   If the xyz-point obtained by calculatePoint() is going to be
   used to translate an object from the origin to the point, then it
   is convenient for a vector to be returned instead of a point.

   <br/><br/>
   The vector can be thought of as having its tail at the origin,
   (0, 0, 0), and its tip at a point on the curved line defined by
   this Hermite object.

   <br/><br/>
   The parameter t must be between 0.0 to 1.0.  If it is outside of
   that range, a vector with all zeros is returned.

   @param t  a parameter from 0.0 to 1.0.
   @return  A vector from the origin to a point on the spline.
   ***************************************************************************/
   public gb_Vector3 calculateTranslation( double t )
   {
       gb_Vector3 p = calculatePoint( t );

       return new gb_Vector3( p.x, p.y, p.z );
   }

   /***************************************************************************
   Uses Hermite interpolation to calculate the tangent of a point on
   the cubic equation between the start and end points given the
   constructor.  The first derivative of the cubic equation is used
   to calculate the tangent, and the tangent vector is normalized
   before it is returned.

   <br/><br/>
   The parameter t must be between 0.0 to 1.0 for interpolation.  If
   t is less than zero or greater than 1, then the result is an
   extrapolation.

   @param t  a parameter from 0.0 to 1.0.
   @return  The tangent vector of an interpolated point.
   ***************************************************************************/
   public gb_Vector3 calculateTangent( double t )
   {
       gb_Vector3 vec = new gb_Vector3();

       double tSquared = t * t;

       // Use the first derivative of each cubic
       // equation to calculate x, y, and z.
       vec.x = (float) (3*m_Ax*tSquared + 2*m_Bx*t + m_Cx);
       vec.y = (float) (3*m_Ay*tSquared + 2*m_By*t + m_Cy);
       vec.z = (float) (3*m_Az*tSquared + 2*m_Bz*t + m_Cz);
       vec.nor();

       return vec;
   }

   /***************************************************************************
   Gets the tangent from the calculateTangent() method and reverses
   its direction.

   <br/><br/>
   The parameter t must be between 0.0 to 1.0 for interpolation.  If
   t is less than zero or greater than 1, then the result is an
   extrapolation.

   @param t  a parameter from 0.0 to 1.0.
   @return  The reverse of the tangent vector at an interpolated
            point.
   ***************************************************************************/
   public gb_Vector3 calculateReverseTangent( double t )
   {
       gb_Vector3 vec = calculateTangent( t );
       vec.inv();
       return vec;
   }

   /***************************************************************************
   Creates a clone of the calling Hermite object and returns it.

   <br/><br/>
   The clone is a deep clone (in the sense of being completely
   independent of the original).

   @return  A clone of the calling Hermite object.
   ***************************************************************************/
   public Hermite clone()
   {
       try {
           // The attributes are all numbers,
           // so super.clone() is adequate.
           return (Hermite)super.clone();
       }
       catch( CloneNotSupportedException e ) {
           return null;
       }
   }
}