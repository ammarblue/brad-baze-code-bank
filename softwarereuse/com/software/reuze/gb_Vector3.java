package com.software.reuze;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//package com.badlogic.gdx.math;


import java.io.Serializable;
import java.util.Random;

import com.software.reuze.m_Matrix4;
//import toxi.geom.Vec3D;
//import com.badlogic.gdx.utils.NumberUtils;
/**
 * Encapsulates a 3D vector. Allows chaining operations by returning a reference to it self in all modification methods.
 * 
 * @author badlogicgames@gmail.com
 * 
 */
public class gb_Vector3 implements Serializable {
	private static final long serialVersionUID = 3840054589595372522L;
	/** the x-component of this vector **/
	public float x;
	/** the x-component of this vector **/
	public float y;
	/** the x-component of this vector **/
	public float z;

	private static gb_Vector3 tmp = new gb_Vector3();
	private static gb_Vector3 tmp2 = new gb_Vector3();
	private static gb_Vector3 tmp3 = new gb_Vector3();
	public static final gb_Vector3 X = new gb_Vector3(1, 0, 0);
	public static final gb_Vector3 Y = new gb_Vector3(0, 1, 0);
	public static final gb_Vector3 Z = new gb_Vector3(0, 0, 1);
	/** Defines the zero vector. */
	public static final gb_Vector3 ZERO = new gb_Vector3();;

	/**
	 * Defines vector with all coords set to Float.MIN_VALUE. Useful for
	 * bounding box operations.
	 */
	public static final gb_Vector3 MIN_VALUE = new gb_Vector3(Float.MIN_VALUE,
			Float.MIN_VALUE, Float.MIN_VALUE);

	/**
	 * Defines vector with all coords set to Float.MAX_VALUE. Useful for
	 * bounding box operations.
	 */
	public static final gb_Vector3 MAX_VALUE = new gb_Vector3(Float.MAX_VALUE,
			Float.MAX_VALUE, Float.MAX_VALUE);

	/**
	 * Constructs a vector at (0,0,0)
	 */
	public gb_Vector3 () {
	}

	/**
	 * Creates a vector with the given components
	 * 
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 */
	public gb_Vector3 (float x, float y, float z) {
		this.set(x, y, z);
	}
	public gb_Vector3(float angle) {
		this.set((float)Math.cos(Math.PI*angle/180.0),(float)Math.sin(Math.PI*angle/180.0),0f);
	}
	public gb_Vector3 (double x, double y, double z) {
		this.set((float)x, (float)y, (float)z);
	}
	/**
	 * Creates a vector from the given vector
	 * 
	 * @param vector The vector
	 */
	public gb_Vector3 (final gb_Vector3 vector) {
		this.set(vector);
	}

	/**
	 * Creates a vector from the given array. The array must have at least 3 elements.
	 * 
	 * @param values The array
	 */
	public gb_Vector3 (float[] values) {
		this.set(values[0], values[1], values[2]);
	}

	/**
	 * Sets the vector to the given components
	 * 
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 * @return this vector for chaining
	 */
	public gb_Vector3 set (float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/**
	 * Sets the components of the given vector
	 * 
	 * @param vector The vector
	 * @return This vector for chaining
	 */
	public gb_Vector3 set (final gb_Vector3 vector) {
		return this.set(vector.x, vector.y, vector.z);
	}

	/**
	 * Sets the components from the array. The array must have at least 3 elements
	 * 
	 * @param values The array
	 * @return this vector for chaining
	 */
	public gb_Vector3 set (float[] values) {
		return this.set(values[0], values[1], values[2]);
	}

	/**
	 * @return a copy of this vector
	 */
	public gb_Vector3 cpy () {
		return new gb_Vector3(this.x,this.y,this.z);
	}

	/**
	 * NEVER EVER SAVE THIS REFERENCE!
	 * 
	 * @return a temporary copy of this vector
	 */
	public gb_Vector3 tmp () {
		return tmp.set(this);
	}

	/**
	 * NEVER EVER SAVE THIS REFERENCE!
	 * 
	 * @return a temporary copy of this vector
	 */
	public gb_Vector3 tmp2 () {
		return tmp2.set(this);
	}

	/**
	 * NEVER EVER SAVE THIS REFERENCE!
	 * 
	 * @return a temporary copy of this vector
	 */
	public gb_Vector3 tmp3 () {
		return tmp3.set(this);
	}
	/**
	 * Clears the vector
	 * 
	 * @return This vector for chaining
	 */
	public gb_Vector3 clr () {
		x=0f; y=0f; z=0f;
		return this;
	}
	/**
	 * Adds the given vector to this vector
	 * 
	 * @param vector The other vector
	 * @return This vector for chaining
	 */
	public gb_Vector3 add (final gb_Vector3 vector) {
		return this.add(vector.x, vector.y, vector.z);
	}

	/**
	 * Adds the given vector to this component
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return This vector for chaining.
	 */
	public gb_Vector3 add (float x, float y, float z) {
		return this.set(this.x + x, this.y + y, this.z + z);
	}

	/**
	 * Adds the given value to all three components of the vector.
	 * 
	 * @param values The value
	 * @return This vector for chaining
	 */
	public gb_Vector3 add (float values) {
		return this.set(this.x + values, this.y + values, this.z + values);
	}

	/**
	 * Subtracts the given vector from this vector
	 * @param a_vec The other vector
	 * @return This vector for chaining
	 */
	public gb_Vector3 sub (final gb_Vector3 a_vec) {
		return this.sub(a_vec.x, a_vec.y, a_vec.z);
	}

	/**
	 * Subtracts the other vector from this vector.
	 * 
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return This vector for chaining
	 */
	public gb_Vector3 sub (float x, float y, float z) {
		return this.set(this.x - x, this.y - y, this.z - z);
	}

	/**
	 * Subtracts the given value from all components of this vector
	 * 
	 * @param value The value
	 * @return This vector for chaining
	 */
	public gb_Vector3 sub (float value) {
		return this.set(this.x - value, this.y - value, this.z - value);
	}

	/**
	 * Multiplies all components of this vector by the given value
	 * 
	 * @param value The value
	 * @return This vector for chaining
	 */
	public gb_Vector3 mul (float value) {
		return this.set(this.x * value, this.y * value, this.z * value);
	}

	/**
	 * Divides all components of this vector by the given value
	 * 
	 * @param value The value
	 * @return This vector for chaining
	 */
	public gb_Vector3 div (float value) {
		float d = 1f / value;
		return this.set(this.x * d, this.y * d, this.z * d);
	}

	/**
	 * @return The euclidian length
	 */
	public float len () {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * @return The squared euclidian length
	 */
	public float len2 () {
		return x * x + y * y + z * z;
	}

	/**
	 * @param vector The other vector
	 * @return Whether this and the other vector are equal
	 */
	public boolean idt (final gb_Vector3 vector) {
		return x == vector.x && y == vector.y && z == vector.z;
	}

	/**
	 * @param vector The other vector
	 * @return The euclidian distance between this and the other vector
	 */
	public float dst (final gb_Vector3 vector) {
		float a = vector.x - x;
		float b = vector.y - y;
		float c = vector.z - z;

		a *= a;
		b *= b;
		c *= c;

		return (float)Math.sqrt(a + b + c);
	}

	/**
	 * Normalizes this vector to unit length
	 * 
	 * @return This vector for chaining
	 */
	public gb_Vector3 nor () {
		float len = this.len();
		if(len == 0) {
			return this;
		} else {
			return this.div(len);
		}
	}

	/**
	 * @param vector The other vector
	 * @return The dot product between this and the other vector
	 */
	public float dot (final gb_Vector3 vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	/**
	 * Sets this vector to the cross product between it and the other vector.
	 * @param vector The other vector
	 * @return This vector for chaining
	 */
	public gb_Vector3 crs (final gb_Vector3 vector) {
		return this.set(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
	}
	public void crs( final gb_Vector3 v1, final gb_Vector3 v2 ) {
		x = v1.y * v2.z - v1.z * v2.y;
		y = v1.z * v2.x - v1.x * v2.z;
		z = v1.x * v2.y - v1.y * v2.x;
	}

	/**
	 * Sets this vector to the cross product between it and the other vector.
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return This vector for chaining
	 */
	public gb_Vector3 crs (float x, float y, float z) {
		return this.set(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
	}

	/**
	 * Multiplies the vector by the given matrix.
	 * @param matrix The matrix
	 * @return This vector for chaining
	 */
	public gb_Vector3 mul (m_Matrix4 matrix) {
		float l_mat[] = matrix.val;
		return this.set(x * l_mat[m_Matrix4.M00] + y * l_mat[m_Matrix4.M01] + z * l_mat[m_Matrix4.M02] + l_mat[m_Matrix4.M03], x
				* l_mat[m_Matrix4.M10] + y * l_mat[m_Matrix4.M11] + z * l_mat[m_Matrix4.M12] + l_mat[m_Matrix4.M13], x * l_mat[m_Matrix4.M20] + y
				* l_mat[m_Matrix4.M21] + z * l_mat[m_Matrix4.M22] + l_mat[m_Matrix4.M23]);
	}

	/**
	 * Multiplies this vector by the given matrix dividing by w. This is mostly used to project/unproject vectors via a perspective
	 * projection matrix.
	 * 
	 * @param matrix The matrix.
	 * @return This vector for chaining
	 */
	public gb_Vector3 prj (m_Matrix4 matrix) {
		float l_mat[] = matrix.val;
		float l_w = x * l_mat[m_Matrix4.M30] + y * l_mat[m_Matrix4.M31] + z * l_mat[m_Matrix4.M32] + l_mat[m_Matrix4.M33];
		return this.set((x * l_mat[m_Matrix4.M00] + y * l_mat[m_Matrix4.M01] + z * l_mat[m_Matrix4.M02] + l_mat[m_Matrix4.M03]) / l_w, (x
				* l_mat[m_Matrix4.M10] + y * l_mat[m_Matrix4.M11] + z * l_mat[m_Matrix4.M12] + l_mat[m_Matrix4.M13])
				/ l_w, (x * l_mat[m_Matrix4.M20] + y * l_mat[m_Matrix4.M21] + z * l_mat[m_Matrix4.M22] + l_mat[m_Matrix4.M23]) / l_w);
	}

	/**
	 * Multiplies this vector by the first three columns of the matrix, essentially only applying rotation and scaling.
	 * 
	 * @param matrix The matrix
	 * @return This vector for chaining
	 */
	public gb_Vector3 rot (m_Matrix4 matrix) {
		float l_mat[] = matrix.val;
		return this.set(x * l_mat[m_Matrix4.M00] + y * l_mat[m_Matrix4.M01] + z * l_mat[m_Matrix4.M02], x * l_mat[m_Matrix4.M10] + y
				* l_mat[m_Matrix4.M11] + z * l_mat[m_Matrix4.M12], x * l_mat[m_Matrix4.M20] + y * l_mat[m_Matrix4.M21] + z * l_mat[m_Matrix4.M22]);
	}

	/**
	 * @return Whether this vector is a unit length vector
	 */
	public boolean isUnit () {
		return this.len() == 1;
	}

	/**
	 * @return Whether this vector is a zero vector
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.ReadonlyVec3D#isZeroVector()
	 */
	public boolean isZero() {
		return m_MathUtils.abs(x) < m_MathUtils.EPS
		&& m_MathUtils.abs(y) < m_MathUtils.EPS
		&& m_MathUtils.abs(z) < m_MathUtils.EPS;
	}

	/**
	 * Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is stored
	 * in this vector.
	 * 
	 * @param target The target vector
	 * @param alpha The interpolation coefficient
	 * @return This vector for chaining.
	 */
	public gb_Vector3 lerp (final gb_Vector3 target, float alpha) {
		mul(1.0f - alpha);
		add(target.tmp().mul(alpha));
		return this;
	}

	/**
	 * Spherically interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is
	 * stored in this vector.
	 * 
	 * @param target The target vector
	 * @param alpha The interpolation coefficient
	 * @return This vector for chaining.
	 */
	public gb_Vector3 slerp (final gb_Vector3 target, float alpha) {
		float dot = dot(target);
		if (dot > 0.99995 || dot < 0.9995) {
			this.add(target.tmp().sub(this).mul(alpha));
			this.nor();
			return this;
		}

		if (dot > 1) dot = 1;
		if (dot < -1) dot = -1;

		float theta0 = (float)Math.acos(dot);
		float theta = theta0 * alpha;
		gb_Vector3 v2 = target.tmp().sub(x * dot, y * dot, z * dot);
		v2.nor();
		return this.mul((float)Math.cos(theta)).add(v2.mul((float)Math.sin(theta))).nor();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString () {
		return x + "," + y + "," + z;
	}

	/**
	 * Returns the dot product between this and the given vector.
	 * 
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return The dot product
	 */
	public float dot (float x, float y, float z) {
		return this.x * x + this.y * y + this.z * z;
	}

	/**
	 * Returns the squared distance between this point and the given point
	 * 
	 * @param point The other point
	 * @return The squared distance
	 */
	public float dst2 (final gb_Vector3 point) {

		float a = point.x - x;
		float b = point.y - y;
		float c = point.z - z;
		return a*a + b*b + c*c;
	}

	/**
	 * Returns the squared distance between this point and the given point
	 * 
	 * @param x The x-component of the other point
	 * @param y The y-component of the other point
	 * @param z The z-component of the other point
	 * @return The squared distance
	 */
	public float dst2 (float x, float y, float z) {
		float a = x - this.x;
		float b = y - this.y;
		float c = z - this.z;

		a *= a;
		b *= b;
		c *= c;

		return a + b + c;
	}

	public float dst (float x, float y, float z) {
		return (float)Math.sqrt(dst2(x, y, z));
	}

	public final float angleBetween(final gb_Vector3 v) {
		return (float) Math.acos(dot(v));
	}

	public final float angleBetween(final gb_Vector3 v, boolean forceNormalize) {
		if (!forceNormalize) return angleBetween(v);
		float theta = this.tmp().nor().dot(v.tmp2().nor());
		return (float) Math.acos(theta);
	}
	/**
	 * {@inheritDoc}
	 */
	 @Override public int hashCode () {
		 final int prime = 31;
		 int result = 1;
		 result = prime * result + Float.floatToIntBits(x);
		 result = prime * result + Float.floatToIntBits(y);
		 result = prime * result + Float.floatToIntBits(z);
		 return result;
	 }

	 /**
	  * {@inheritDoc}
	  */
	 @Override
	 public boolean equals(Object obj) {
		 if (obj==null) return false;
		 if (obj instanceof gb_Vector3) {
			 return equalsWithTolerance((gb_Vector3)obj, 0.0005f);
		 }
		 return false;
	 }

	 /**
	  * Scales the vector components by the given scalars.
	  * 
	  * @param scalarX
	  * @param scalarY
	  * @param scalarZ
	  */
	 public gb_Vector3 mul (float scalarX, float scalarY, float scalarZ) {
		 x *= scalarX;
		 y *= scalarY;
		 z *= scalarZ;
		 return this;
	 }

	 public gb_Vector3 mul(gb_Vector3 s) {
		 x *= s.x; y *= s.y; z *= s.z;
		 return this;
	 }
	 /**
	  * Scales vector uniformly by factor -1 ( v = -v ), overrides coordinates
	  * with result.
	  * 
	  * @return itself
	  */
	 public final gb_Vector3 inv() {
		 x = -x;
		 y = -y;
		 z = -z;
		 return this;
	 }
	 /**
	  * Sets the vector to the largest components of both
	  * vectors.
	  * 
	  * @param b
	  *            the b 
	  * @return result as new vector
	  */
	 public gb_Vector3 max(final gb_Vector3 b) {
		 x=m_MathUtils.max(x, b.x);
		 y=m_MathUtils.max(y, b.y);
		 z=m_MathUtils.max(z, b.z);
		 return this;
	 }

	 /**
	  * Sets the vector to the smallest components of both
	  * vectors.
	  * 
	  * @param b
	  *            comparing vector 
	  * @return result as new vector
	  */
	 public gb_Vector3 min(final gb_Vector3 b) {
		 x=m_MathUtils.min(x, b.x);
		 y=m_MathUtils.min(y, b.y);
		 z=m_MathUtils.min(z, b.z);
		 return this;
	 }
	 /**
	  * Interpolates the vector towards the given target vector, using linear
	  * interpolation.
	  * 
	  * @param v
	  *            target vector
	  * @param f
	  *            interpolation factor (should be in the range 0..1)
	  * 
	  * @return itself, result overrides current vector
	  */
	 public final gb_Vector3 interpolate(final gb_Vector3 v, float f) {
		 x += (v.x - x) * f;
		 y += (v.y - y) * f;
		 z += (v.z - z) * f;
		 return this;
	 }

	 /**
	  * Abs.
	  * 
	  * @return the vec3 d
	  */
	 public final gb_Vector3 abs() {
		 x = m_MathUtils.abs(x);
		 y = m_MathUtils.abs(y);
		 z = m_MathUtils.abs(z);
		 return this;
	 }
	 /**
	  * Replaces all vector components with the signum of their original values.
	  * In other words if a components value was negative its new value will be
	  * -1, if zero => 0, if positive => +1
	  * 
	  * @return itself
	  */
	 public gb_Vector3 signum() {
		 x = (x < 0 ? -1 : x == 0 ? 0 : 1);
		 y = (y < 0 ? -1 : y == 0 ? 0 : 1);
		 z = (z < 0 ? -1 : z == 0 ? 0 : 1);
		 return this;
	 }
	 /**
	  * Replaces the vector components with their multiplicative inverse.
	  * 
	  * @return itself
	  */
	 public gb_Vector3 reciprocal() {
		 x = 1f / x;
		 y = 1f / y;
		 z = 1f / z;
		 return this;
	 }

	 public gb_Vector3 reflect(final gb_Vector3 normal) {
		 return set(normal.tmp().mul(this.dot(normal) * 2f).sub(this));
	 }
	 /**
	  * Normalizes the vector to the given length.
	  * 
	  * @param len
	  *            desired length
	  * @return itself
	  */
	 public final gb_Vector3 norTo(float len) {
		 float mag = (float) Math.sqrt(x * x + y * y + z * z);
		 if (mag > 0) {
			 mag = len / mag;
			 x *= mag;
			 y *= mag;
			 z *= mag;
		 }
		 return this;
	 }
	 /**
	  * Add random jitter to the vector in the range -j ... +j using the default
	  * {@link Random} generator of {@link m_MathUtils}.
	  * 
	  * @param j
	  *            the j
	  * 
	  * @return the vec3 d
	  */
	 public final gb_Vector3 jitter(float j) {
		 return jitter(j, j, j);
	 }

	 /**
	  * Adds random jitter to the vector in the range -j ... +j using the default
	  * {@link Random} generator of {@link m_MathUtils}.
	  * 
	  * @param jx
	  *            maximum x jitter
	  * @param jy
	  *            maximum y jitter
	  * @param jz
	  *            maximum z jitter
	  * 
	  * @return itself
	  */
	 public final gb_Vector3 jitter(float jx, float jy, float jz) {
		 x += m_MathUtils.normalizedRandom() * jx;
		 y += m_MathUtils.normalizedRandom() * jy;
		 z += m_MathUtils.normalizedRandom() * jz;
		 return this;
	 }

	 public final gb_Vector3 jitter(Random rnd, float j) {
		 return jitter(rnd, j, j, j);
	 }

	 public final gb_Vector3 jitter(Random rnd, float jx, float jy, float jz) {
		 x += m_MathUtils.normalizedRandom(rnd) * jx;
		 y += m_MathUtils.normalizedRandom(rnd) * jy;
		 z += m_MathUtils.normalizedRandom(rnd) * jz;
		 return this;
	 }

	 public final gb_Vector3 jitter(Random rnd, gb_Vector3 jitterVec) {
		 return jitter(rnd, jitterVec.x, jitterVec.y, jitterVec.z);
	 }

	 /**
	  * Adds random jitter to the vector in the range defined by the given vector
	  * components and using the default {@link Random} generator of
	  * {@link m_MathUtils}.
	  * 
	  * @param jitterVec
	  *            the jitter vec
	  * 
	  * @return itself
	  */
	 public final gb_Vector3 jitter(gb_Vector3 jitterVec) {
		 return jitter(jitterVec.x, jitterVec.y, jitterVec.z);
	 }

	 /**
	  * Limits the vector's magnitude to the length given.
	  * 
	  * @param lim
	  *            new maximum magnitude
	  * 
	  * @return itself
	  */
	 public final gb_Vector3 limit(float lim) {
		 if (len2() > lim * lim) {
			 return nor().mul(lim);
		 }
		 return this;
	 }
	 public final gb_Vector3 toSpherical() {
		 final float xx = Math.abs(x) <= m_MathUtils.EPS ? m_MathUtils.EPS : x;
		 final float zz = z;

		 final float radius = (float) Math.sqrt((xx * xx) + (y * y) + (zz * zz));
		 z = (float) Math.asin(y / radius);
		 y = (float) Math.atan(zz / xx) + (xx < 0.0 ? m_MathUtils.PI : 0);
		 x = radius;
		 return this;
	 }
	 public final gb_Vector3 toCartesian() {
		 final float a = (float) (x * Math.cos(z));
		 final float xx = (float) (a * Math.cos(y));
		 final float yy = (float) (x * Math.sin(z));
		 final float zz = (float) (a * Math.sin(y));
		 x = xx;
		 y = yy;
		 z = zz;
		 return this;
	 }
	 public final ga_Vector2 to2DXY() {
		 return new ga_Vector2(x, y);
	 }

	 public final ga_Vector2 to2DXZ() {
		 return new ga_Vector2(x, z);
	 }

	 public final ga_Vector2 to2DYZ() {
		 return new ga_Vector2(y, z);
	 }
	 public static final gb_Vector3 to3DXY(ga_Vector2 v) {
		 return new gb_Vector3(v.x, v.y, 0);
	 }

	 public static final gb_Vector3 to3DXZ(ga_Vector2 v) {
		 return new gb_Vector3(v.x, 0, v.y);
	 }

	 public static final gb_Vector3 to3DYZ(ga_Vector2 v) {
		 return new gb_Vector3(0, v.x, v.y);
	 }

	 public gc_Vector4 to4D() {
		 return new gc_Vector4(x, y, z, 1);
	 }

	 public gc_Vector4 to4D(float w) {
		 return new gc_Vector4(x, y, z, w);
	 }

	 public float[] toArray() {
		 return new float[] {
				 x, y, z
		 };
	 }

	 public float[] toArray4(float w) {
		 return new float[] {
				 x, y, z, w
		 };
	 }
	 /**
	  * Calculate the angle of rotation for the 2D component of the vector
	  * @return the angle of rotation
	  */
	  public float heading2D() {
		  float angle = (float) Math.atan2(-y, x);
		  return -1*angle;
	  }
	  public boolean equalsWithTolerance(final gb_Vector3 v, float tolerance) {
		  if (v==null || tolerance<=0.0f) return false;
		  if (m_MathUtils.abs(x - v.x) < tolerance) {
			  if (m_MathUtils.abs(y - v.y) < tolerance) {
				  if (m_MathUtils.abs(z - v.z) < tolerance) {
					  return true;
				  }
			  }
		  }
		  return false;
	  }

	  public gb_Vector3 bisect(final ga_Line2D l) {
		  ga_Vector2 diff = l.a.tmp().sub(l.b);
		  ga_Vector2 sum = l.a.tmp2().add(l.b);
		  float dot = diff.dot(sum);
		  return new gb_Vector3(diff.x, diff.y, -dot / 2);
	  }
	  public static gb_Vector3 bisect(final ga_Vector2 a, final ga_Vector2 b) {
		  ga_Vector2 diff = a.tmp().sub(b);
		  ga_Vector2 sum = a.tmp2().add(b);
		  float dot = diff.dot(sum);
		  return new gb_Vector3(diff.x, diff.y, -dot / 2);
	  }
	  /**
	   * Rotates the vector around the giving axis.
	   * 
	   * @param axis
	   *            rotation axis vector
	   * @param theta
	   *            rotation angle (in radians)
	   * 
	   * @return itself
	   */
	  public final gb_Vector3 rotateAroundAxis(gb_Vector3 axis, float theta) {
		  final float ax = axis.x;
		  final float ay = axis.y;
		  final float az = axis.z;
		  final float ux = ax * x;
		  final float uy = ax * y;
		  final float uz = ax * z;
		  final float vx = ay * x;
		  final float vy = ay * y;
		  final float vz = ay * z;
		  final float wx = az * x;
		  final float wy = az * y;
		  final float wz = az * z;
		  final double si = Math.sin(theta);
		  final double co = Math.cos(theta);
		  float xx =
			  (float) (ax * (ux + vy + wz)
					  + (x * (ay * ay + az * az) - ax * (vy + wz)) * co + (-wy + vz)
					  * si);
		  float yy =
			  (float) (ay * (ux + vy + wz)
					  + (y * (ax * ax + az * az) - ay * (ux + wz)) * co + (wx - uz)
					  * si);
		  float zz =
			  (float) (az * (ux + vy + wz)
					  + (z * (ax * ax + ay * ay) - az * (ux + vy)) * co + (-vx + uy)
					  * si);
		  x = xx;
		  y = yy;
		  z = zz;
		  return this;
	  }
	  /**
	   * Identifies the closest cartesian axis to this vector. If at least two
	   * vector components are equal, no unique decision can be made and the
	   * method returns null.
	   * 
	   * @return Vector3 or null
	   */
	  public final gb_Vector3 getClosestAxis() {
		  float ax = m_MathUtils.abs(x);
		  float ay = m_MathUtils.abs(y);
		  float az = m_MathUtils.abs(z);
		  if (ax > ay && ax > az) {
			  return gb_Vector3.X;
		  }
		  if (ay > ax && ay > az) {
			  return gb_Vector3.Y;
		  }
		  if (az > ax && az > ay) {
			  return gb_Vector3.Z;
		  }
		  return null;
	  }

	  /**
	   * Forcefully fits the vector in the given AABB specified by the 2 given
	   * points.
	   * 
	   * @param min
	   * @param max
	   * @return itself
	   */
	  public gb_Vector3 constrain(gb_Vector3 min, gb_Vector3 max) {
		  x = m_MathUtils.clip(x, min.x, max.x);
		  y = m_MathUtils.clip(y, min.y, max.y);
		  z = m_MathUtils.clip(z, min.z, max.z);
		  return this;
	  }
	  /*
	   * (non-Javadoc)
	   * 
	   * @see toxi.geom.ReadonlyVec3D#getRotatedAroundAxis(toxi.geom.Vec3D, float)
	   */
	  public final gb_Vector3 getRotatedAroundAxis(gb_Vector3 axis, float theta) {
		  return new gb_Vector3(this).rotateAroundAxis(axis, theta);
	  }

	  /*
	   * (non-Javadoc)
	   * 
	   * @see toxi.geom.ReadonlyVec3D#getRotatedX(float)
	   */
	  public final gb_Vector3 getRotatedX(float theta) {
		  return new gb_Vector3(this).rotateX(theta);
	  }

	  /*
	   * (non-Javadoc)
	   * 
	   * @see toxi.geom.ReadonlyVec3D#getRotatedY(float)
	   */
	  public final gb_Vector3 getRotatedY(float theta) {
		  return new gb_Vector3(this).rotateY(theta);
	  }

	  /*
	   * (non-Javadoc)
	   * 
	   * @see toxi.geom.ReadonlyVec3D#getRotatedZ(float)
	   */
	  public final gb_Vector3 getRotatedZ(float theta) {
		  return new gb_Vector3(this).rotateZ(theta);
	  }
	  /**
	   * Rotates the vector by the given angle around the X axis.
	   * 
	   * @param theta
	   *            the theta
	   * 
	   * @return itself
	   */
	  public final gb_Vector3 rotateX(float theta) {
		  final float co = (float) Math.cos(theta);
		  final float si = (float) Math.sin(theta);
		  final float zz = co * z - si * y;
		  y = si * z + co * y;
		  z = zz;
		  return this;
	  }

	  /**
	   * Rotates the vector by the given angle around the Y axis.
	   * 
	   * @param theta
	   *            the theta
	   * 
	   * @return itself
	   */
	  public final gb_Vector3 rotateY(float theta) {
		  final float co = (float) Math.cos(theta);
		  final float si = (float) Math.sin(theta);
		  final float xx = co * x - si * z;
		  z = si * x + co * z;
		  x = xx;
		  return this;
	  }

	  /**
	   * Rotates the vector by the given angle around the Z axis.
	   * 
	   * @param theta
	   *            the theta
	   * 
	   * @return itself
	   */
	  public final gb_Vector3 rotateZ(float theta) {
		  final float co = (float) Math.cos(theta);
		  final float si = (float) Math.sin(theta);
		  final float xx = co * x - si * y;
		  y = si * x + co * y;
		  x = xx;
		  return this;
	  }

	  /**
	   * Creates a new vector from the given angle in the XY plane. The Z
	   * component of the vector will be zero.
	   * 
	   * The resulting vector for theta=0 is equal to the positive X axis.
	   * 
	   * @param theta
	   *            the theta
	   * 
	   * @return new vector in the XY plane
	   */
	  public static final gb_Vector3 fromXYTheta(float theta) {
		  return new gb_Vector3((float) Math.cos(theta), (float) Math.sin(theta), 0);
	  }

	  /**
	   * Creates a new vector from the given angle in the XZ plane. The Y
	   * component of the vector will be zero.
	   * 
	   * The resulting vector for theta=0 is equal to the positive X axis.
	   * 
	   * @param theta
	   *            the theta
	   * 
	   * @return new vector in the XZ plane
	   */
	  public static final gb_Vector3 fromXZTheta(float theta) {
		  return new gb_Vector3((float) Math.cos(theta), 0, (float) Math.sin(theta));
	  }

	  /**
	   * Creates a new vector from the given angle in the YZ plane. The X
	   * component of the vector will be zero.
	   * 
	   * The resulting vector for theta=0 is equal to the positive Y axis.
	   * 
	   * @param theta
	   *            the theta
	   * 
	   * @return new vector in the YZ plane
	   */
	  public static final gb_Vector3 fromYZTheta(float theta) {
		  return new gb_Vector3(0, (float) Math.cos(theta), (float) Math.sin(theta));
	  }
	  /**
	   * Sets the elements of this vector to uniformly distributed
	   * random values in a specified range, using a supplied
	   * random number generator.
	   *
	   * @param lower lower random value (inclusive)
	   * @param upper upper random value (exclusive)
	   * @param generator random number generator
	   */
	  protected gb_Vector3 setRandom (float lower, float upper, Random generator)
	  {
		  float range = upper-lower;

		  x = generator.nextFloat()*range + lower;
		  y = generator.nextFloat()*range + lower;
		  z = generator.nextFloat()*range + lower;
		  return this;
	  }

	  /**
	   * Returns the arithmetic mean of the x, y, z, and w coordinates of the specified points Iterable. This returns null
	   * if the Iterable contains no points, or if all of the points are null.
	   *
	   * @param points the Iterable of points which define the returned arithmetic mean.
	   *
	   * @return the arithmetic mean point of the specified points Iterable, or null if the Iterable is empty or contains
	   *         only null points.
	   *
	   * @throws IllegalArgumentException if the Iterable is null.
	   */
	  public static gb_Vector3 computeAveragePoint(Iterable<? extends gb_Vector3> points)
	  {
		  if (points == null)
		  {
			  String msg = "nullValue.PointListIsNull";
			  throw new IllegalArgumentException(msg);
		  }

		  double count = 0;
		  double x = 0d;
		  double y = 0d;
		  double z = 0d;

		  for (gb_Vector3 vec : points)
		  {
			  if (vec == null)
				  continue;

			  count++;
			  x += vec.x;
			  y += vec.y;
			  z += vec.z;
		  }

		  if (count == 0)
			  return null;

		  return new gb_Vector3(x / count, y / count, z / count);
	  }

	  public static void clamp(gb_Vector3 value, float floor,float ceiling)
	  {//ensure length of vector is between floor and ceiling
		  float vecLength = value.len();
		  float adjust = -1.0f;
		  if(vecLength < floor)
			  adjust = floor;
		  else if(vecLength > ceiling)
			  adjust = ceiling;

		  if(adjust != -1.0f)
		  {
			  value.nor();
			  value.mul(adjust);
		  }
	  }
	  
	  public gb_Vector3 rotate(float angle) {
		  	float angleR = (float) Math.toRadians(angle);
		  	float sinAngle;
		  	float cosAngle;
		  	
		  	sinAngle = (float) Math.sin( angleR );
		  	cosAngle = (float) Math.cos( angleR );
		  	float xx = x;
		  	x = ( cosAngle * x ) - ( sinAngle * y );
		  	y = ( sinAngle * xx ) + ( cosAngle * y );		  	
		  	return this;
	  }
}
