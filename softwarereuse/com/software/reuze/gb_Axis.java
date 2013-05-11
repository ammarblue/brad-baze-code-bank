package com.software.reuze;

import java.util.Arrays;
import java.util.Comparator;
/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
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

/**
 * An immutable origin + axis in 3D-Space.
 */
public class gb_Axis {

    /**
     * Creates a new x-Axis3D object from the world origin.
     */
    public static final gb_Axis xAxis() {
        return new gb_Axis(gb_Vector3.X);
    }

    /**
     * Creates a new y-Axis3D object from the world origin.
     */
    public static final gb_Axis yAxis() {
        return new gb_Axis(gb_Vector3.Y);
    }

    /**
     * Creates a new z-Axis3D object from the world origin.
     */
    public static final gb_Axis zAxis() {
        return new gb_Axis(gb_Vector3.Z);
    }

    public final gb_Vector3 origin;
    public final gb_Vector3 dir;

    /**
     * Creates a new z-Axis3D object from the world origin.
     */
    public gb_Axis() {
        this(gb_Vector3.Z);
    }

    public gb_Axis(float x, float y, float z) {
        this(new gb_Vector3(x, y, z));
    }

    public gb_Axis(gb_Ray ray) {
        this(ray.origin, ray.getDirection());
    }

    /**
     * Creates a new Axis3D from the world origin in the given direction.
     * 
     * @param dir
     *            direction vector
     */
    public gb_Axis(gb_Vector3 dir) {
        this(new gb_Vector3(), dir);
    }

    /**
     * Creates a new Axis3D from the given origin and direction.
     * 
     * @param o
     *            origin
     * @param dir
     *            direction
     */
    public gb_Axis(gb_Vector3 o, gb_Vector3 dir) {
        this.origin = o;
        this.dir = new gb_Vector3(dir).nor();
    }
    
    /**
     * Returns an array of normalized vectors defining the three principal axes of the x-, y-, and z-coordinates from
     * the specified points Iterable, sorted from the most prominent axis to the least prominent. This returns null if
     * the points Iterable is empty, or if all of the points are null. The returned array contains three normalized
     * orthogonal vectors defining a coordinate system which best fits the distribution of the points Iterable about its
     * arithmetic mean.
     *
     * @param points the Iterable of points for which to compute the principal axes.
     *
     * @return the normalized principal axes of the points Iterable, sorted from the most prominent axis to the least
     *         prominent.
     *
     * @throws IllegalArgumentException if the points Iterable is null.
     */
    public static gb_Vector3[] computePrincipalAxes(Iterable<? extends gb_Vector3> points)
    {
        if (points == null)
        {
            String message = "nullValue.IterableIsNull";
            throw new IllegalArgumentException(message);
        }

        // Compute the covariance matrix of the specified points Iterable. Note that Matrix.fromCovarianceOfVertices
        // returns null if the points Iterable is empty, or if all of the points are null.
        m_Matrix4 covariance = fromCovarianceOfVertices(points);
        if (covariance == null)
            return null;

        // Compute the eigenvalues and eigenvectors of the covariance matrix. Since the covariance matrix is symmetric
        // by definition, we can safely use the method Matrix.computeEigensystemFromSymmetricMatrix3().
        final double[] eigenValues = new double[3];
        final gb_Vector3[] eigenVectors = new gb_Vector3[3];
        computeEigensystemFromSymmetricMatrix3(covariance, eigenValues, eigenVectors);

        // Compute an index array who's entries define the order in which the eigenValues array can be sorted in
        // ascending order.
        Integer[] indexArray = {0, 1, 2};
        Arrays.sort(indexArray, new Comparator<Integer>()
        {
            public int compare(Integer a, Integer b)
            {
                return Double.compare(eigenValues[a], eigenValues[b]);
            }
        });

        // Return the normalized eigenvectors in order of decreasing eigenvalue. This has the effect of returning three
        // normalized orthognal vectors defining a coordinate system, which are sorted from the most prominent axis to
        // the least prominent.
        return new gb_Vector3[]
            {
                eigenVectors[indexArray[2]].nor(),
                eigenVectors[indexArray[1]].nor(),
                eigenVectors[indexArray[0]].nor()
            };
    }
    
    /**
     * Computes a symmetric covariance Matrix from the x, y, z coordinates of the specified points Iterable. This
     * returns null if the points Iterable is empty, or if all of the points are null.
     * <p/>
     * The returned covariance matrix represents the correlation between each pair of x-, y-, and z-coordinates as
     * they're distributed about the point Iterable's arithmetic mean. Its layout is as follows:
     * <p/>
     * <code> C(x, x)  C(x, y)  C(x, z) <br/> C(x, y)  C(y, y)  C(y, z) <br/> C(x, z)  C(y, z)  C(z, z) </code>
     * <p/>
     * C(i, j) is the covariance of coordinates i and j, where i or j are a coordinate's dispersion about its mean
     * value. If any entry is zero, then there's no correlation between the two coordinates defining that entry. If the
     * returned matrix is diagonal, then all three coordinates are uncorrelated, and the specified point Iterable is
     * distributed evenly about its mean point.
     *
     * @param points the Iterable of points for which to compute a Covariance matrix.
     *
     * @return the covariance matrix for the iterable of 3D points.
     *
     * @throws IllegalArgumentException if the points Iterable is null.
     */
    public static m_Matrix4 fromCovarianceOfVertices(Iterable<? extends gb_Vector3> points)
    {
        if (points == null)
        {
            String msg = "nullValue.IterableIsNull";
            throw new IllegalArgumentException(msg);
        }

        gb_Vector3 mean = gb_Vector3.computeAveragePoint(points);
        if (mean == null)
            return null;

        int count = 0;
        double c11 = 0d;
        double c22 = 0d;
        double c33 = 0d;
        double c12 = 0d;
        double c13 = 0d;
        double c23 = 0d;

        for (gb_Vector3 vec : points)
        {
            if (vec == null)
                continue;

            count++;
            c11 += (vec.x - mean.x) * (vec.x - mean.x);
            c22 += (vec.y - mean.y) * (vec.y - mean.y);
            c33 += (vec.z - mean.z) * (vec.z - mean.z);
            c12 += (vec.x - mean.x) * (vec.y - mean.y); // c12 = c21
            c13 += (vec.x - mean.x) * (vec.z - mean.z); // c13 = c31
            c23 += (vec.y - mean.y) * (vec.z - mean.z); // c23 = c32
        }

        if (count == 0)
            return null;

        return new m_Matrix4(
            c11 / (double) count, c12 / (double) count, c13 / (double) count, 0d,
            c12 / (double) count, c22 / (double) count, c23 / (double) count, 0d,
            c13 / (double) count, c23 / (double) count, c33 / (double) count, 0d,
            0d, 0d, 0d, 0d);
    }

    /**
     * Computes the eigensystem of the specified symmetric Matrix's upper 3x3 matrix. If the Matrix's upper 3x3 matrix
     * is not symmetric, this throws an IllegalArgumentException. This writes the eigensystem parameters to the
     * specified arrays <code>outEigenValues</code> and <code>outEigenVectors</code>, placing the eigenvalues in the
     * entries of array <code>outEigenValues</code>, and the corresponding eigenvectors in the entires of array
     * <code>outEigenVectors</code>. These arrays must be non-null, and have length three or greater.
     *
     * @param matrix          the symmetric Matrix for which to compute an eigensystem.
     * @param outEigenvalues  the array which receives the three output eigenvalues.
     * @param outEigenvectors the array which receives the three output eigenvectors.
     *
     * @throws IllegalArgumentException if the Matrix is null or is not symmetric, if the output eigenvalue array is
     *                                  null or has length less than 3, or if the output eigenvector is null or has
     *                                  length less than 3.
     */
    public static void computeEigensystemFromSymmetricMatrix3(m_Matrix4 matrix, double[] outEigenvalues,
    		gb_Vector3[] outEigenvectors)
    {
        if (matrix == null)
        {
            String msg = "nullValue.MatrixIsNull";
            throw new IllegalArgumentException(msg);
        }

        if (!matrix.isSymmetric())
        {
            String msg = "generic.MatrixNotSymmetric "+ matrix;
            throw new IllegalArgumentException(msg);
        }

        // Take from "Mathematics for 3D Game Programming and Computer Graphics, Second Edition" by Eric Lengyel,
        // Listing 14.6 (pages 441-444).

        final double EPSILON = 1.0e-10;
        final int MAX_SWEEPS = 32;

        // Since the Matrix is symmetric, m12=m21, m13=m31, and m23=m32. Therefore we can ignore the values m21, m31,
        // and m32.
        double m11 = matrix.val[m_Matrix4.M11];
        double m12 = matrix.val[m_Matrix4.M12];
        double m13 = matrix.val[m_Matrix4.M13];
        double m22 = matrix.val[m_Matrix4.M22];
        double m23 = matrix.val[m_Matrix4.M23];
        double m33 = matrix.val[m_Matrix4.M33];

        double[][] r = new double[3][3];
        r[0][0] = r[1][1] = r[2][2] = 1d;

        for (int a = 0; a < MAX_SWEEPS; a++)
        {
            // Exit if off-diagonal entries small enough
            if ((Math.abs(m12) < EPSILON) && (Math.abs(m13) < EPSILON) && (Math.abs(m23) < EPSILON))
                break;

            // Annihilate (1,2) entry
            if (m12 != 0d)
            {
                double u = (m22 - m11) * 0.5 / m12;
                double u2 = u * u;
                double u2p1 = u2 + 1d;
                double t = (u2p1 != u2) ?
                    ((u < 0d) ? -1d : 1d) * (Math.sqrt(u2p1) - Math.abs(u))
                    : 0.5 / u;
                double c = 1d / Math.sqrt(t * t + 1d);
                double s = c * t;

                m11 -= t * m12;
                m22 += t * m12;
                m12 = 0d;

                double temp = c * m13 - s * m23;
                m23 = s * m13 + c * m23;
                m13 = temp;

                for (int i = 0; i < 3; i++)
                {
                    temp = c * r[i][0] - s * r[i][1];
                    r[i][1] = s * r[i][0] + c * r[i][1];
                    r[i][0] = temp;
                }
            }

            // Annihilate (1,3) entry
            if (m13 != 0d)
            {
                double u = (m33 - m11) * 0.5 / m13;
                double u2 = u * u;
                double u2p1 = u2 + 1d;
                double t = (u2p1 != u2) ?
                    ((u < 0d) ? -1d : 1d) * (Math.sqrt(u2p1) - Math.abs(u))
                    : 0.5 / u;
                double c = 1d / Math.sqrt(t * t + 1d);
                double s = c * t;

                m11 -= t * m13;
                m33 += t * m13;
                m13 = 0d;

                double temp = c * m12 - s * m23;
                m23 = s * m12 + c * m23;
                m12 = temp;

                for (int i = 0; i < 3; i++)
                {
                    temp = c * r[i][0] - s * r[i][2];
                    r[i][2] = s * r[i][0] + c * r[i][2];
                    r[i][0] = temp;
                }
            }

            // Annihilate (2,3) entry
            if (m23 != 0d)
            {
                double u = (m33 - m22) * 0.5 / m23;
                double u2 = u * u;
                double u2p1 = u2 + 1d;
                double t = (u2p1 != u2) ?
                    ((u < 0d) ? -1d : 1d) * (Math.sqrt(u2p1) - Math.abs(u))
                    : 0.5 / u;
                double c = 1d / Math.sqrt(t * t + 1d);
                double s = c * t;

                m22 -= t * m23;
                m33 += t * m23;
                m23 = 0d;

                double temp = c * m12 - s * m13;
                m13 = s * m12 + c * m13;
                m12 = temp;

                for (int i = 0; i < 3; i++)
                {
                    temp = c * r[i][1] - s * r[i][2];
                    r[i][2] = s * r[i][1] + c * r[i][2];
                    r[i][1] = temp;
                }
            }
        }

        outEigenvalues[0] = m11;
        outEigenvalues[1] = m22;
        outEigenvalues[2] = m33;

        outEigenvectors[0] = new gb_Vector3(r[0][0], r[1][0], r[2][0]);
        outEigenvectors[1] = new gb_Vector3(r[0][1], r[1][1], r[2][1]);
        outEigenvectors[2] = new gb_Vector3(r[0][2], r[1][2], r[2][2]);
    }
}
