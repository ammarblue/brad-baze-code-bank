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

//import javax.xml.bind.annotation.XmlAttribute;


public class gc_Vector4 implements Cloneable {

    /** X coordinate */
    //@XmlAttribute(required = true)
    public float x;

    /** Y coordinate */
    //@XmlAttribute(required = true)
    public float y;

    /** Z coordinate */
    //@XmlAttribute(required = true)
    public float z;

    /** W coordinate (weight) */
    //@XmlAttribute(required = true)
    public float w;

    public gc_Vector4() {
    }

    public gc_Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public gc_Vector4(gb_Vector3 v, float w) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }

    public gc_Vector4(gc_Vector4 v) {
        set(v);
    }

    public gc_Vector4 abs() {
        x = m_MathUtils.abs(x);
        y = m_MathUtils.abs(y);
        z = m_MathUtils.abs(z);
        w = m_MathUtils.abs(w);
        return this;
    }

    public final gc_Vector4 add(gc_Vector4 v) {
        return new gc_Vector4(x + v.x(), y + v.y(), z + v.z(), w + v.w());
    }

    public final gc_Vector4 addScaled(gc_Vector4 t, float s) {
        return new gc_Vector4(s * t.x(), s * t.y(), s * t.z(), s * t.w());
    }

    public final gc_Vector4 addScaledSelf(gc_Vector4 t, float s) {
        x += s * t.x();
        y += s * t.y();
        z += s * t.z();
        w += s * t.w();
        return this;
    }

    public final gc_Vector4 addSelf(gc_Vector4 v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        this.w += v.w();
        return this;
    }

    public final gc_Vector4 addXYZ(float xx, float yy, float zz) {
        return new gc_Vector4(x + xx, y + yy, z + zz, w);
    }

    public final gc_Vector4 addXYZ(gb_Vector3 v) {
        return new gc_Vector4(x + v.x, y + v.y, z + v.z, w);
    }

    public final gc_Vector4 addXYZSelf(float xx, float yy, float zz) {
        x += xx;
        y += yy;
        z += zz;
        return this;
    }

    public final gc_Vector4 addXYZSelf(gb_Vector3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    /**
     * Returns the (4-space) angle in radians between this vector and the vector
     * parameter; the return value is constrained to the range [0,PI].
     * 
     * @param v
     *            the other vector
     * @return the angle in radians in the range [0,PI]
     */
    public final float angleBetween(gc_Vector4 v) {
        double vDot = dot(v) / (magnitude() * v.magnitude());
        if (vDot < -1.0) {
            vDot = -1.0;
        }
        if (vDot > 1.0) {
            vDot = 1.0;
        }
        return (float) (Math.acos(vDot));
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public int compareTo(gc_Vector4 v) {
        if (x == v.x() && y == v.y() && z == v.z() && w == v.w()) {
            return 0;
        }
        float a = magSquared();
        float b = v.magSquared();
        if (a < b) {
            return -1;
        }
        return +1;
    }

    public final gc_Vector4 copy() {
        return new gc_Vector4(this);
    }

    public final float distanceTo(gc_Vector4 v) {
        if (v != null) {
            final float dx = x - v.x();
            final float dy = y - v.y();
            final float dz = z - v.z();
            final float dw = w - v.z();
            return (float) Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
        } else {
            return Float.NaN;
        }
    }

    public final float distanceToSquared(gc_Vector4 v) {
        if (v != null) {
            final float dx = x - v.x();
            final float dy = y - v.y();
            final float dz = z - v.z();
            final float dw = w - v.z();
            return dx * dx + dy * dy + dz * dz + dw * dw;
        } else {
            return Float.NaN;
        }
    }

    public final float dot(gc_Vector4 v) {
        return (x * v.x() + y * v.y() + z * v.z() + w * v.w());
    }

    /**
     * Returns true if the Object v is of type ReadonlyVec4D and all of the data
     * members of v are equal to the corresponding data members in this vector.
     * 
     * @param v
     *            the Object with which the comparison is made
     * @return true or false
     */
    public boolean equals(Object v) {
        try {
        	gc_Vector4 vv = (gc_Vector4) v;
            return (x == vv.x() && y == vv.y() && z == vv.z() && w == vv.w());
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Returns true if the Object v is of type Vec4D and all of the data members
     * of v are equal to the corresponding data members in this vector.
     * 
     * @param v
     *            the vector with which the comparison is made
     * @return true or false
     */
    public boolean equals(gc_Vector4 v) {
        try {
            return (x == v.x() && y == v.y() && z == v.z() && w == v.w());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean equalsWithTolerance(gc_Vector4 v, float tolerance) {
        try {
            float diff = x - v.x();
            if (Float.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = y - v.y();
            if (Float.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = z - v.z();
            if (Float.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = w - v.w();
            if (Float.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public gc_Vector4 getAbs() {
        return copy().abs();
    }

    public final gc_Vector4 getInvertedXYZ() {
        return copy().invertXYZ();
    }

    public gc_Vector4 getMapped(m_ScaleMap map) {
        return new gc_Vector4((float) map.getClippedValueFor(x),
                (float) map.getClippedValueFor(y),
                (float) map.getClippedValueFor(z),
                (float) map.getClippedValueFor(w));
    }

    public gc_Vector4 getMappedXYZ(m_ScaleMap map) {
        return new gc_Vector4((float) map.getClippedValueFor(x),
                (float) map.getClippedValueFor(y),
                (float) map.getClippedValueFor(z), w);
    }

    public gc_Vector4 getNormalized() {
        return copy().normalize();
    }

    public gc_Vector4 getNormalizedTo(float len) {
        return copy().normalizeTo(len);
    }

    public gc_Vector4 getRotatedAroundAxis(gb_Vector3 axis, float theta) {
        return copy().rotateAroundAxis(axis, theta);
    }

    public gc_Vector4 getRotatedX(float theta) {
        return copy().rotateX(theta);
    }

    public gc_Vector4 getRotatedY(float theta) {
        return copy().rotateY(theta);
    }

    public gc_Vector4 getRotatedZ(float theta) {
        return copy().rotateZ(theta);
    }

    public gc_Vector4 getRoundedXYZTo(float prec) {
        return copy().roundXYZTo(prec);
    }

    public gc_Vector4 getUnweighted() {
        return copy().unweight();
    }

    public gc_Vector4 getWeighted() {
        return copy().weight();
    }

    /**
     * Returns a hash code value based on the data values in this object. Two
     * different Vec4D objects with identical data values (i.e., Vec4D.equals
     * returns true) will return the same hash code value. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the integer hash code value
     */
    public int hashCode() {
        long bits = 1L;
        bits = 31L * bits + Float.floatToIntBits(x);
        bits = 31L * bits + Float.floatToIntBits(y);
        bits = 31L * bits + Float.floatToIntBits(z);
        bits = 31L * bits + Float.floatToIntBits(w);
        return (int) (bits ^ (bits >> 32));
    }

    public final gc_Vector4 interpolateTo(gc_Vector4 v, float t) {
        return copy().interpolateToSelf(v, t);
    }

    public final gc_Vector4 interpolateTo(gc_Vector4 v, float f,
            m_i_InterpolateValue s) {
        return new gc_Vector4(s.interpolate(x, v.x(), f),
                s.interpolate(y, v.y(), f), s.interpolate(z, v.z(), f),
                s.interpolate(w, v.w(), f));
    }

    public final gc_Vector4 interpolateToSelf(gc_Vector4 v, float t) {
        this.x += (v.x() - x) * t;
        this.y += (v.y() - y) * t;
        this.z += (v.z() - z) * t;
        this.w += (v.w() - w) * t;
        return this;
    }

    public final gc_Vector4 interpolateToSelf(gc_Vector4 v, float f,
            m_i_InterpolateValue s) {
        x = s.interpolate(x, v.x(), f);
        y = s.interpolate(y, v.y(), f);
        z = s.interpolate(z, v.z(), f);
        w = s.interpolate(w, v.w(), f);
        return this;
    }

    public final gc_Vector4 invertXYZ() {
        this.x *= -1;
        this.y *= -1;
        this.z *= -1;
        return this;
    }

    public final boolean isZeroVector() {
        return m_MathUtils.abs(x) < m_MathUtils.EPS
                && m_MathUtils.abs(y) < m_MathUtils.EPS
                && m_MathUtils.abs(z) < m_MathUtils.EPS
                && m_MathUtils.abs(w) < m_MathUtils.EPS;
    }

    public final float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public final float magSquared() {
        return x * x + y * y + z * z + w * w;
    }

    /**
     * Normalizes the vector so that its magnitude = 1.
     * 
     * @return itself
     */
    public final gc_Vector4 normalize() {
        float mag = (float) Math.sqrt(x * x + y * y + z * z);
        if (mag > 0) {
            mag = 1f / mag;
            x *= mag;
            y *= mag;
            z *= mag;
            w *= mag;
        }
        return this;
    }

    /**
     * Normalizes the vector to the given length.
     * 
     * @param len
     *            desired length
     * @return itself
     */
    public final gc_Vector4 normalizeTo(float len) {
        float mag = (float) Math.sqrt(x * x + y * y + z * z);
        if (mag > 0) {
            mag = len / mag;
            x *= mag;
            y *= mag;
            z *= mag;
            w *= mag;
        }
        return this;
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
    public final gc_Vector4 rotateAroundAxis(gb_Vector3 axis, float theta) {
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
        float xx = (float) (ax * (ux + vy + wz)
                + (x * (ay * ay + az * az) - ax * (vy + wz)) * co + (-wy + vz)
                * si);
        float yy = (float) (ay * (ux + vy + wz)
                + (y * (ax * ax + az * az) - ay * (ux + wz)) * co + (wx - uz)
                * si);
        float zz = (float) (az * (ux + vy + wz)
                + (z * (ax * ax + ay * ay) - az * (ux + vy)) * co + (-vx + uy)
                * si);
        x = xx;
        y = yy;
        z = zz;
        return this;
    }

    /**
     * Rotates the vector by the given angle around the X axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return itself
     */
    public final gc_Vector4 rotateX(float theta) {
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
    public final gc_Vector4 rotateY(float theta) {
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
    public final gc_Vector4 rotateZ(float theta) {
        final float co = (float) Math.cos(theta);
        final float si = (float) Math.sin(theta);
        final float xx = co * x - si * y;
        y = si * x + co * y;
        x = xx;
        return this;
    }

    public gc_Vector4 roundXYZTo(float prec) {
        x = m_MathUtils.roundTo(x, prec);
        y = m_MathUtils.roundTo(y, prec);
        z = m_MathUtils.roundTo(z, prec);
        return this;
    }

    public final gc_Vector4 scale(float s) {
        return new gc_Vector4(x * s, y * s, z * s, w * s);
    }

    public final gc_Vector4 scale(float xx, float yy, float zz, float ww) {
        return new gc_Vector4(x * xx, y * yy, z * zz, w * ww);
    }

    public gc_Vector4 scale(gc_Vector4 s) {
        return copy().scaleSelf(s);
    }

    public final gc_Vector4 scaleSelf(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        this.w *= s;
        return this;
    }

    public gc_Vector4 scaleSelf(gc_Vector4 s) {
        this.x *= s.x();
        this.y *= s.y();
        this.z *= s.z();
        this.w *= s.w();
        return this;
    }

    public final gc_Vector4 scaleXYZSelf(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        return this;
    }

    public final gc_Vector4 scaleXYZSelf(float xscale, float yscale, float zscale) {
        this.x *= xscale;
        this.y *= yscale;
        this.z *= zscale;
        return this;
    }

    public final gc_Vector4 set(gc_Vector4 v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
        return this;
    }

    public gc_Vector4 setW(float w) {
        this.w = w;
        return this;
    }

    public gc_Vector4 setX(float x) {
        this.x = x;
        return this;
    }

    public final gc_Vector4 setXYZ(gb_Vector3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public gc_Vector4 setY(float y) {
        this.y = y;
        return this;
    }

    public gc_Vector4 setZ(float z) {
        this.z = z;
        return this;
    }

    public final gc_Vector4 sub(gc_Vector4 v) {
        return new gc_Vector4(x - v.x(), y - v.y(), z - v.z(), w - v.w());
    }

    public final gc_Vector4 subSelf(gc_Vector4 v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        this.w -= v.w();
        return this;
    }

    public final gc_Vector4 subXYZ(float xx, float yy, float zz) {
        return new gc_Vector4(x - xx, y - yy, z - zz, w);
    }

    public final gc_Vector4 subXYZ(gb_Vector3 v) {
        return new gc_Vector4(x - v.x, y - v.y, z - v.z, w);
    }

    public final gc_Vector4 subXYZSelf(float xx, float yy, float zz) {
        this.x -= xx;
        this.y -= yy;
        this.z -= zz;
        return this;
    }

    public final gc_Vector4 subXYZSelf(gb_Vector3 v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public final gb_Vector3 to3D() {
        return new gb_Vector3(x, y, z);
    }

    public float[] toArray() {
        return new float[] {
                x, y, z, w
        };
    }

    public String toString() {
        return "[x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
    }

    public final gc_Vector4 translate(float xx, float yy, float zz) {
        this.x += w * xx;
        this.y += w * yy;
        this.z += w * zz;
        return this;
    }

    /**
     * Divides each coordinate by the weight with and sets the coordinate to the
     * newly calculated ones.
     */
    public final gc_Vector4 unweight() {
        float iw = m_MathUtils.abs(w) > m_MathUtils.EPS ? 1f / w : 0;
        x *= iw;
        y *= iw;
        z *= iw;
        return this;
    }

    public final gb_Vector3 unweightInto(gb_Vector3 out) {
        float iw = m_MathUtils.abs(w) > m_MathUtils.EPS ? 1f / w : 0;
        out.x = x * iw;
        out.y = y * iw;
        out.z = z * iw;
        return out;
    }

    /**
     * @return the w
     */
    public final float w() {
        return w;
    }

    /**
     * Multiplies the weight with each coordinate and sets the coordinate to the
     * newly calculated ones.
     * 
     * @return itself
     */
    public final gc_Vector4 weight() {
        x *= w;
        y *= w;
        z *= w;
        return this;
    }

    public final gb_Vector3 weightInto(gb_Vector3 out) {
        out.x = x * w;
        out.y = y * w;
        out.z = z * w;
        return out;
    }

    /**
     * @return the x
     */
    public final float x() {
        return x;
    }

    /**
     * @return the y
     */
    public final float y() {
        return y;
    }

    /**
     * @return the z
     */
    public final float z() {
        return z;
    }
}
