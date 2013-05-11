package com.software.reuze;

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
 * A double precision, general, dynamically-resizable, one-dimensional vector
 * class.
 */
public class d_VectorDouble implements java.io.Serializable, Cloneable {

    private int length;
    public double[] values;

    static final long serialVersionUID = 1L;

    /**
     * Constructs a new GVector of the specified length and initializes it by
     * copying the specified number of elements from the specified array. The
     * array must contain at least <code>length</code> elements (i.e.,
     * <code>vector.length</code> >= <code>length</code>. The length of this new
     * GVector is set to the specified length.
     * 
     * @param vector
     *            The array from which the values will be copied.
     * @param length
     *            The number of values copied from the array.
     */
    public d_VectorDouble(double vector[], int length) {
        this.length = length;
        values = new double[length];
        for (int i = 0; i < length; i++) {
            values[i] = vector[i];
        }
    }

    /**
     * Constructs a new GVector from the specified array elements. The length of
     * this GVector is set to the length of the specified array. The array
     * elements are copied into this new GVector.
     * 
     * @param vector
     *            the values for the new GVector.
     */
    public d_VectorDouble(double[] vector) {
        length = vector.length;
        values = new double[vector.length];
        for (int i = 0; i < length; i++) {
            values[i] = vector[i];
        }
    }

    /**
     * Constructs a new GVector from the specified vector. The vector elements
     * are copied into this new GVector.
     * 
     * @param vector
     *            the source GVector for this new GVector.
     */
    public d_VectorDouble(d_VectorDouble vector) {
        values = new double[vector.length];
        length = vector.length;
        for (int i = 0; i < length; i++) {
            values[i] = vector.values[i];
        }
    }

    /**
     * Constructs a new GVector of the specified length with all vector elements
     * initialized to 0.
     * 
     * @param length
     *            the number of elements in this GVector.
     */
    public d_VectorDouble(int length) {
        this.length = length;
        values = new double[length];
        for (int i = 0; i < length; i++) {
            values[i] = 0.0;
        }
    }

    /**
     * Constructs a new GVector and copies the initial values from the specified
     * tuple.
     * 
     * @param v
     *            the source for the new GVector's initial values
     */
    public d_VectorDouble(ga_Vector2 v) {
        values = new double[] {
                v.x, v.y
        };
        length = 2;
    }

    /**
     * Constructs a new GVector and copies the initial values from the specified
     * tuple.
     * 
     * @param v
     *            the source for the new GVector's initial values
     */
    public d_VectorDouble(gb_Vector3 v) {
        values = new double[] {
                v.x, v.y, v.z
        };
        length = 3;
    }

    /**
     * Constructs a new GVector and copies the initial values from the specified
     * tuple.
     * 
     * @param v
     *            the source for the new GVector's initial values
     */
    public d_VectorDouble(gc_Vector4 v) {
        values = new double[] {
                v.x(), v.y(), v.z(), v.w()
        };
        length = 4;
    }

    /**
     * Creates the vector sum of this vector and the given one (must be equal
     * sized). Returns result as new vector.
     * 
     * @param v
     * @return new vector
     */
    public final d_VectorDouble add(d_VectorDouble v) {
        if (length != v.length) {
            throw new le_ExceptionMatrixSize();
        }
        double[] tmp = new double[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = values[i] + v.values[i];
        }
        return new d_VectorDouble(tmp);
    }

    /**
     * Sets the value of this vector to sum of itself and the specified vector
     * 
     * @param vector
     *            the second vector
     * @return itself
     */
    public final d_VectorDouble addSelf(d_VectorDouble vector) {
        if (length != vector.length) {
            throw new le_ExceptionMatrixSize();
        }
        for (int i = 0; i < length; i++) {
            this.values[i] += vector.values[i];
        }
        return this;
    }

    /**
     * Returns the (n-space) angle in radians between this vector and the vector
     * parameter; the return value is constrained to the range [0,PI].
     * 
     * @param v
     *            The other vector
     * @return The angle in radians in the range [0,PI]
     */
    public final double angleBetween(d_VectorDouble v) {
        return (Math.acos(this.dot(v) / (this.magnitude() * v.magnitude())));
    }

    /**
     * LU Decomposition Back Solve; this method takes the LU matrix and the
     * permutation vector produced by the GMatrix method LUD and solves the
     * equation (LU)*x = b by placing the solution vector x into this vector.
     * This vector should be the same length or longer than b.
     * 
     * @param LU
     *            The matrix into which the lower and upper decompostions have
     *            been placed
     * @param b
     *            The b vector in the equation (LU)*x = b
     * @param permutation
     *            The row permuations that were necessary to produce the LU
     *            matrix parameter
     */
    public final void backSolveLUD(m_MatrixDouble LU, d_VectorDouble b, d_VectorDouble permutation) {
        int size = LU.nRow * LU.nCol;

        double[] temp = new double[size];
        double[] result = new double[size];
        int[] row_perm = new int[b.size()];
        int i, j;

        if (LU.nRow != b.size()) {
            throw new le_ExceptionMatrixSize();
        }

        if (LU.nRow != permutation.size()) {
            throw new le_ExceptionMatrixSize();
        }

        if (LU.nRow != LU.nCol) {
            throw new le_ExceptionMatrixSize();
        }

        for (i = 0; i < LU.nRow; i++) {
            for (j = 0; j < LU.nCol; j++) {
                temp[i * LU.nCol + j] = LU.values[i][j];
            }
        }

        for (i = 0; i < LU.nRow; i++) {
            result[i * LU.nCol] = b.values[i];
        }
        for (i = 0; i < LU.nCol; i++) {
            row_perm[i] = (int) permutation.values[i];
        }

        m_MatrixDouble.backSubstituteLU(LU.nRow, temp, row_perm, result);

        for (i = 0; i < LU.nRow; i++) {
            this.values[i] = result[i * LU.nCol];
        }
    }

    /**
     * Solves for x in Ax = b, where x is this vector (nx1), A is mxn, b is mx1,
     * and A = U*W*transpose(V); U,W,V must be precomputed and can be found by
     * taking the singular value decomposition (SVD) of A using the method SVD
     * found in the GMatrix class.
     * 
     * @param U
     *            The U matrix produced by the GMatrix method SVD
     * @param W
     *            The W matrix produced by the GMatrix method SVD
     * @param V
     *            The V matrix produced by the GMatrix method SVD
     * @param b
     *            The b vector in the linear equation Ax = b
     */
    public final void backSolveSVD(m_MatrixDouble U, m_MatrixDouble W, m_MatrixDouble V, d_VectorDouble b) {
        if (!(U.nRow == b.size() && U.nRow == U.nCol && U.nRow == W.nRow)) {
            throw new le_ExceptionMatrixSize();
        }
        if (!(W.nCol == values.length && W.nCol == V.nCol && W.nCol == V.nRow)) {
            throw new le_ExceptionMatrixSize();
        }
        m_MatrixDouble tmp = new m_MatrixDouble(U.nRow, W.nCol);
        tmp.mul(U, V);
        tmp.mulTransposeRight(U, W);
        tmp.invert();
        mul(tmp, b);
    }

    /**
     * Creates a new object of the same class as this object.
     * 
     * @return a clone of this instance.
     * @exception OutOfMemoryError
     *                if there is not enough memory.
     * @see java.lang.Cloneable
     * @since vecmath 1.3
     */
    public Object clone() {
        d_VectorDouble v = null;
        try {
            v = (d_VectorDouble) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        v.values = new double[length];
        for (int i = 0; i < length; i++) {
            v.values[i] = values[i];
        }
        return v;
    }

    /**
     * Returns the dot product of this vector and vector v.
     * 
     * @param v
     *            the other vector
     * @return the dot product of this and v
     */
    public final double dot(d_VectorDouble v) {
        if (length != v.length) {
            throw new le_ExceptionMatrixSize();
        }
        double result = 0.0;
        for (int i = 0; i < length; i++) {
            result += values[i] * v.values[i];
        }
        return result;
    }

    /**
     * Returns true if all of the data members of GVector vector1 are equal to
     * the corresponding data members in this GVector.
     * 
     * @param vector1
     *            The vector with which the comparison is made.
     * @return true or false
     */
    public boolean equals(d_VectorDouble vector1) {
        try {
            if (length != vector1.length) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (values[i] != vector1.values[i]) {
                    return false;
                }
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Returns true if the Object o1 is of type GMatrix and all of the data
     * members of o1 are equal to the corresponding data members in this
     * GMatrix.
     * 
     * @param o1
     *            The object with which the comparison is made.
     * @return true or false
     */
    public boolean equals(Object o1) {
        try {
            d_VectorDouble v2 = (d_VectorDouble) o1;
            if (length != v2.length) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (values[i] != v2.values[i]) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }

    }

    /**
     * Returns true if the L-infinite distance between this vector and vector v
     * is less than or equal to the tolerance parameter, otherwise returns
     * false. The L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2), .
     * . . ].
     * 
     * @param v
     *            The vector to be compared to this vector
     * @param tolerance
     *            the threshold value
     */
    public boolean equalsWithTolerance(d_VectorDouble v, double tolerance) {
        try {
            double diff;
            if (length != v.length) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                diff = values[i] - v.values[i];
                if ((diff < 0 ? -diff : diff) > tolerance) {
                    return false;
                }
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Retrieves the value at the specified index value of this vector.
     * 
     * @param index
     *            the index of the element to retrieve (zero indexed)
     * @return the value at the indexed element
     */
    public final double get(int index) {
        return values[index];
    }

    /**
     * Returns a hash code value based on the data values in this object. Two
     * different GVector objects with identical data values (i.e.,
     * GVector.equals returns true) will return the same hash number. Two
     * GVector objects with different data members may return the same hash
     * value, although this is not likely.
     * 
     * @return the integer hash code value
     */
    public int hashCode() {
        long bits = 1L;
        for (int i = 0; i < length; i++) {
            bits = 31L * bits + Double.doubleToLongBits(values[i]);
        }
        return (int) (bits ^ (bits >> 32));
    }

    /**
     * Linearly interpolates this vector to the target vector and places the
     * result into a new instance: result = this + (target-this)*alpha. The
     * target vector needs to be equal sized.
     * 
     * @param v
     *            the target vector
     * @param alpha
     *            the alpha interpolation parameter
     * @return result as new vector
     */
    public final d_VectorDouble interpolateTo(d_VectorDouble v, double alpha) {
        if (length != v.length) {
            throw new le_ExceptionMatrixSize();
        }
        return new d_VectorDouble(this).interpolateToSelf(v, alpha);
    }

    /**
     * Interpolates the vector towards the given target vector, using the given
     * {@link InterpolateStrategy}. The target vector needs to be equal sized.
     * 
     * @param v
     *            target vector
     * @param alpha
     *            interpolation factor (should be in the range 0..1)
     * @param strategy
     *            InterpolateStrategy instance
     * 
     * @return result as new vector
     */
    public final d_VectorDouble interpolateTo(d_VectorDouble v, double alpha,
            m_i_InterpolateValue strategy) {
        if (length != v.length) {
            throw new le_ExceptionMatrixSize();
        }
        return new d_VectorDouble(this).interpolateToSelf(v, alpha, strategy);
    }

    /**
     * Linearly interpolates this vector to the target vector and places result
     * in this vector. result = this + (target-this)*alpha. The target vector
     * needs to be equal sized.
     * 
     * @param v
     *            the target vector
     * @param alpha
     *            the alpha interpolation parameter
     */
    public final d_VectorDouble interpolateToSelf(d_VectorDouble v, double alpha) {
        if (v.length != length) {
            throw new le_ExceptionMatrixSize();
        }
        for (int i = 0; i < length; i++) {
            values[i] += (v.values[i] - values[i]) * alpha;
        }
        return this;
    }

    /**
     * Interpolates the vector towards the given target vector, using the given
     * {@link InterpolateStrategy}. The target vector needs to be equal sized.
     * 
     * @param v
     *            target vector
     * @param alpha
     *            interpolation factor (should be in the range 0..1)
     * @param strategy
     *            InterpolateStrategy instance
     * 
     * @return itself, result overrides current vector
     */
    public final d_VectorDouble interpolateToSelf(d_VectorDouble v, double alpha,
            m_i_InterpolateValue strategy) {
        if (v.length != length) {
            throw new le_ExceptionMatrixSize();
        }
        for (int i = 0; i < length; i++) {
            values[i] = strategy.interpolate((float)values[i], (float)v.values[i], (float)alpha);
        }
        return this;
    }

    /**
     * Negates the value of this vector: this = -this.
     */
    public final void invert() {
        for (int i = 0; i < length; i++) {
            this.values[i] *= -1.0;
        }
    }

    /**
     * Returns the square root of the sum of the squares of this vector (its
     * length in n-dimensional space).
     * 
     * @return length of this vector
     */

    public final double magnitude() {
        double sq = 0.0;
        for (int i = 0; i < length; i++) {
            sq += values[i] * values[i];
        }
        return Math.sqrt(sq);
    }

    /**
     * Returns the sum of the squares of this vector (its length squared in
     * n-dimensional space).
     * 
     * @return length squared of this vector
     */
    public final double magSquared() {
        double sq = 0.0;
        for (int i = 0; i < length; i++) {
            sq += values[i] * values[i];
        }
        return sq;
    }

    /**
     * Multiplies matrix m1 times Vector v1 and places the result into this
     * vector (this = m1*v1).
     * 
     * @param m1
     *            The matrix in the multiplication
     * @param v1
     *            The vector that is multiplied
     */
    public final void mul(m_MatrixDouble m1, d_VectorDouble v1) {
        if (m1.getNumCol() != v1.length) {
            throw new le_ExceptionMatrixSize();
        }

        if (length != m1.getNumRow()) {
            throw new le_ExceptionMatrixSize();
        }

        double v[];
        if (v1 != this) {
            v = v1.values;
        } else {
            v = values.clone();
        }

        for (int j = length - 1; j >= 0; j--) {
            values[j] = 0.0;
            for (int i = v1.length - 1; i >= 0; i--) {
                values[j] += m1.values[j][i] * v[i];
            }
        }
    }

    /**
     * Multiplies the transpose of vector v1 (ie, v1 becomes a row vector with
     * respect to the multiplication) times matrix m1 and places the result into
     * this vector (this = transpose(v1)*m1). The result is technically a row
     * vector, but the GVector class only knows about column vectors, and so the
     * result is stored as a column vector.
     * 
     * @param m1
     *            The matrix in the multiplication
     * @param v1
     *            The vector that is temporarily transposed
     */
    public final void mul(d_VectorDouble v1, m_MatrixDouble m1) {
        if (m1.getNumRow() != v1.length) {
            throw new le_ExceptionMatrixSize();
        }

        if (length != m1.getNumCol()) {
            throw new le_ExceptionMatrixSize();
        }

        double v[];
        if (v1 != this) {
            v = v1.values;
        } else {
            v = values.clone();
        }

        for (int j = length - 1; j >= 0; j--) {
            values[j] = 0.0;
            for (int i = v1.length - 1; i >= 0; i--) {
                values[j] += m1.values[i][j] * v[i];
            }
        }
    }

    /**
     * Normalizes this vector in place.
     */
    public final void normalize() {
        double mag = magnitude();
        if (mag > m_MathUtils.EPS) {
            double invMag = 1.0 / mag;
            for (int i = 0; i < length; i++) {
                values[i] = values[i] * invMag;
            }
        }
    }

    /**
     * Scales this vector by the scale factor s and returns result as new
     * vector.
     * 
     * @param s
     *            the scalar value
     * @return new vector
     */
    public final d_VectorDouble scale(double s) {
        double[] tmp = new double[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = values[i] * s;
        }
        return new d_VectorDouble(tmp);
    }

    /**
     * Scales the values of this vector with the values of the given vector
     * vector (this = this * vector). Returns result as new vector.
     * 
     * @param v
     *            scale vector
     * @return new vector
     */
    public final d_VectorDouble scale(d_VectorDouble v) {
        if (length != v.length) {
            throw new le_ExceptionMatrixSize();
        }
        double[] tmp = new double[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = values[i] * v.values[i];
        }
        return new d_VectorDouble(tmp);
    }

    /**
     * Scales this vector by the scale factor s.
     * 
     * @param s
     *            the scalar value
     * @return itself
     */
    public final d_VectorDouble scaleSelf(double s) {
        for (int i = 0; i < length; i++) {
            values[i] = values[i] * s;
        }
        return this;
    }

    /**
     * Scales the values of this vector with the values of the given vector
     * vector (this = this * vector).
     * 
     * @param v
     *            scale vector
     * @return itself
     */
    public final d_VectorDouble scaleSelf(d_VectorDouble v) {
        if (length != v.length) {
            throw new le_ExceptionMatrixSize();
        }
        for (int i = 0; i < length; i++) {
            this.values[i] *= v.values[i];
        }
        return this;
    }

    /**
     * Sets the values of this vector to the values found in the array
     * parameter. If the array is shorter than the number of values in this
     * vector the remaining values are zeroed. If the array is longer, only the
     * first values up to to the vector length are copied.
     * 
     * @param vector
     *            the source array
     */
    public final d_VectorDouble set(double[] vector) {
        int i;
        if (vector.length >= length) {
            for (i = 0; i < length; i++) {
                values[i] = vector[i];
            }
        } else {
            for (i = 0; i < vector.length; i++) {
                values[i] = vector[i];
            }
            for (i = vector.length; i < length; i++) {
                values[i] = 0.0;
            }
        }
        return this;
    }

    /**
     * Sets the value of this vector to the values found in vector vector.
     * 
     * @param vector
     *            the source vector
     */
    public final d_VectorDouble set(d_VectorDouble vector) {
        return set(vector.values);
    }

    /**
     * Sets the value of this vector to the values in tuple
     * 
     * @param tuple
     *            the source for the new GVector's new values
     */
    public final d_VectorDouble set(ga_Vector2 tuple) {
        return set(new double[] {
                tuple.x, tuple.y
        });
    }

    /**
     * Sets the value of this vector to the values in tuple
     * 
     * @param tuple
     *            the source for the new GVector's new values
     */
    public final d_VectorDouble set(gb_Vector3 tuple) {
        return set(new double[] {
                tuple.x, tuple.y, tuple.z
        });
    }

    /**
     * Sets the value of this vector to the values in tuple
     * 
     * @param tuple
     *            the source for the new GVector's new values
     * @return itself
     */
    public final d_VectorDouble set(gc_Vector4 tuple) {
        return set(new double[] {
                tuple.x(), tuple.y(), tuple.w()
        });
    }

    /**
     * Modifies the value at the specified index of this vector.
     * 
     * @param index
     *            the index if the element to modify (zero indexed)
     * @param value
     *            the new vector element value
     */
    public final d_VectorDouble setElement(int index, double value) {
        values[index] = value;
        return this;
    }

    /**
     * Changes the size of this vector dynamically. If the size is increased no
     * data values will be lost. If the size is decreased, only those data
     * values whose vector positions were eliminated will be lost.
     * 
     * @param length
     *            number of desired elements in this vector
     */
    public final d_VectorDouble setSize(int length) {
        double[] tmp = new double[length];
        int max;
        if (this.length < length) {
            max = this.length;
        } else {
            max = length;
        }
        for (int i = 0; i < max; i++) {
            tmp[i] = values[i];
        }
        this.length = length;
        values = tmp;
        return this;
    }

    /**
     * Returns the number of elements in this vector.
     * 
     * @return number of elements in this vector
     */
    public final int size() {
        return values.length;
    }

    /**
     * Creates the vector difference of this vector and the given one (must be
     * equal sized). Returns result as new vector.
     * 
     * @param v
     * @return new vector
     */
    public final d_VectorDouble sub(d_VectorDouble v) {
        if (length != v.length) {
            throw new le_ExceptionMatrixSize();
        }
        double[] tmp = new double[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = values[i] - v.values[i];
        }
        return new d_VectorDouble(tmp);
    }

    /**
     * Sets the value of this vector to the vector difference of itself and
     * vector (this = this - vector).
     * 
     * @param vector
     *            the other vector
     */
    public final d_VectorDouble subSelf(d_VectorDouble vector) {
        if (length != vector.length) {
            throw new le_ExceptionMatrixSize();
        }
        for (int i = 0; i < length; i++) {
            this.values[i] -= vector.values[i];
        }
        return this;
    }

    /**
     * Returns a string that contains the values of this GVector.
     * 
     * @return the String representation
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder(length * 8);
        for (int i = 0; i < length; i++) {
            buffer.append(values[i]).append(" ");
        }
        return buffer.toString();
    }

    /**
     * Sets all the values in this vector to zero.
     */
    public final d_VectorDouble zero() {
        for (int i = 0; i < length; i++) {
            this.values[i] = 0.0;
        }
        return this;
    }
}
