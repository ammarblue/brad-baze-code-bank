package com.software.reuze;
/*
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




import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.software.reuze.m_PolynomialBernsteins;


/*import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;*/

/**
 * <p>
 * This is a generic 2D B-Spline class for curves of arbitrary length, control
 * handles and patches are created and joined automatically as described here:
 * <a
 * href="http://www.ibiblio.org/e-notes/Splines/Bint.htm">ibiblio.org/e-notes/
 * Splines/Bint.htm</a>
 * </p>
 * 
 * <p>
 * Thanks to a bug report by Aaron Meyers (http://universaloscillation.com) the
 * {@linkplain #computeVertices(int)} method has a slightly changed behavior
 * from version 0014 onwards. In earlier versions erroneous duplicate points
 * would be added near each given control point, which lead to various weird
 * results.
 * </p>
 * 
 * <p>
 * The new behaviour of the curve interpolation/computation is described in the
 * docs for the {@linkplain #computeVertices(int)} method below.
 * </p>
 * 
 * @version 0014 Added user adjustable curve tightness control
 * @version 0015 Added JAXB annotations and List support for dynamic building of
 *          spline
 */
//@XmlAccessorType(XmlAccessType.FIELD)
public class ga_SplineB {

    public static final float DEFAULT_TIGHTNESS = 0.25f;

    public static final int DEFAULT_RES = 16;

    //@XmlElement
    protected ga_Vector2[] points;

    //@XmlElement(name = "p")
    public List<ga_Vector2> pointList = new ArrayList<ga_Vector2>();

    //@XmlTransient
    public List<ga_Vector2> vertices;

    //@XmlTransient
    public m_PolynomialBernsteins bernstein;

    //@XmlTransient
    public ga_Vector2[] delta;

    //@XmlTransient
    public ga_Vector2[] coeffA;

    //@XmlTransient
    public float[] bi;

    //@XmlAttribute
    protected float tightness;

    //@XmlTransient
    protected float invTightness;

    //@XmlTransient
    protected int numP;

    //@XmlTransient
    private float[] arcLenIndex;

    /**
     * Constructs an empty spline container with default curve tightness. You
     * need to populate the spline manually by using {@link #add(ReadonlyVec2D)}
     * .
     */
    public ga_SplineB() {
        setTightness(DEFAULT_TIGHTNESS);
    }

    /**
     * @param rawPoints
     *            list of control point vectors
     */
    public ga_SplineB(List<ga_Vector2> rawPoints) {
        this(rawPoints, null, DEFAULT_TIGHTNESS);
    }

    /**
     * @param rawPoints
     *            list of control point vectors
     * @param b
     *            predefined Bernstein polynomial (good for reusing)
     * @param tightness
     *            default curve tightness used for the interpolated vertices
     *            {@linkplain #setTightness(float)}
     */
    public ga_SplineB(List<ga_Vector2> rawPoints, m_PolynomialBernsteins b,
            float tightness) {
        pointList.addAll(rawPoints);
        bernstein = b;
        setTightness(tightness);
    }

    /**
     * @param pointArray
     *            array of control point vectors
     */
    public ga_SplineB(ga_Vector2[] pointArray) {
        this(pointArray, null, DEFAULT_TIGHTNESS);
    }

    /**
     * @param pointArray
     *            array of control point vectors
     * @param b
     *            predefined Bernstein polynomial (good for reusing)
     * @param tightness
     *            default curve tightness used for the interpolated vertices
     *            {@linkplain #setTightness(float)}
     */
    public ga_SplineB(ga_Vector2[] pointArray, m_PolynomialBernsteins b, float tightness) {
        this(Arrays.asList(pointArray), b, tightness);
    }

    public ga_SplineB add(float x, float y) {
        return add(new ga_Vector2(x, y));
    }

    /**
     * Adds the given point to the list of control points.
     * 
     * @param p
     * @return itself
     */
    public ga_SplineB add(final ga_Vector2 p) {
        pointList.add(p.copy());
        return this;
    }

    /**
     * <p>
     * Computes all curve vertices based on the resolution/number of
     * subdivisions requested. The higher, the more vertices are computed:
     * </p>
     * <p>
     * <strong>(number of control points - 1) * resolution + 1</strong>
     * </p>
     * <p>
     * Since version 0014 the automatic placement of the curve handles can also
     * be manipulated via the {@linkplain #setTightness(float)} method.
     * </p>
     * 
     * @param res
     *            the number of vertices to be computed per segment between
     *            original control points (incl. control point always at the
     *            start of each segment)
     * @return list of Vector2 vertices along the curve
     */
    public List<ga_Vector2> computeVertices(int res) {
        updateCoefficients();
        if (res < 1) {
            res = 1;
        }
        res++;
        if (bernstein == null || bernstein.resolution != res) {
            bernstein = new m_PolynomialBernsteins(res);
        }
        if (vertices == null) {
            vertices = new ArrayList<ga_Vector2>();
        } else {
            vertices.clear();
        }
        findCPoints();
        ga_Vector2 deltaP = new ga_Vector2();
        ga_Vector2 deltaQ = new ga_Vector2();
        res--;
        for (int i = 0; i < numP - 1; i++) {
            ga_Vector2 p = points[i];
            ga_Vector2 q = points[i + 1];
            deltaP.set(delta[i]).add(p);
            deltaQ.set(q).sub(delta[i + 1]);
            for (int k = 0; k < res; k++) {
                float x =
                        p.x * bernstein.b0[k] + deltaP.x * bernstein.b1[k]
                                + deltaQ.x * bernstein.b2[k] + q.x
                                * bernstein.b3[k];
                float y =
                        p.y * bernstein.b0[k] + deltaP.y * bernstein.b1[k]
                                + deltaQ.y * bernstein.b2[k] + q.y
                                * bernstein.b3[k];
                vertices.add(new ga_Vector2(x, y));
            }
        }
        vertices.add(points[points.length - 1]);
        return vertices;
    }

    protected void findCPoints() {
        bi[1] = -tightness;
        coeffA[1].set((points[2].x - points[0].x - delta[0].x) * tightness,
                (points[2].y - points[0].y - delta[0].y) * tightness);
        for (int i = 2; i < numP - 1; i++) {
            bi[i] = -1 / (invTightness + bi[i - 1]);
            coeffA[i].set(
                    -(points[i + 1].x - points[i - 1].x - coeffA[i - 1].x)
                            * bi[i],
                    -(points[i + 1].y - points[i - 1].y - coeffA[i - 1].y)
                            * bi[i]);
        }
        for (int i = numP - 2; i > 0; i--) {
            delta[i].set(coeffA[i].x + delta[i + 1].x * bi[i], coeffA[i].y
                    + delta[i + 1].y * bi[i]);
        }
    }

    /**
     * Computes a list of points along the spline which are uniformly separated
     * by the given step distance.
     * 
     * @param step
     * @return point list
     */
    public List<ga_Vector2> getDecimatedVertices(float step) {
        return getDecimatedVertices(step, true);
    }

    /**
     * Computes a list of points along the spline which are uniformly separated
     * by the given step distance.
     * 
     * @param step
     * @param doAddFinalVertex
     *            true, if the last vertex computed by
     *            {@link #computeVertices(int)} should be added regardless of
     *            its distance.
     * @return point list
     */
    public List<ga_Vector2> getDecimatedVertices(float step, boolean doAddFinalVertex) {
        if (vertices == null || vertices.size() < 2) {
            computeVertices(DEFAULT_RES);
        }
        float arcLen = getEstimatedArcLength();
        ArrayList<ga_Vector2> uniform = new ArrayList<ga_Vector2>();
        double delta = step / arcLen;
        int currIdx = 0;
        for (double t = 0; t < 1.0; t += delta) {
            double currT = t * arcLen;
            while (currT >= arcLenIndex[currIdx]) {
                currIdx++;
            }
            ga_Vector2 p = vertices.get(currIdx - 1);
            ga_Vector2 q = vertices.get(currIdx);
            float frac =
                    (float) ((currT - arcLenIndex[currIdx - 1]) / (arcLenIndex[currIdx] - arcLenIndex[currIdx - 1]));
            ga_Vector2 i = p.interpolateTo(q, frac);
            uniform.add(i);
        }
        if (doAddFinalVertex) {
            uniform.add(vertices.get(vertices.size() - 1));
        }
        return uniform;
    }

    /**
     * @return estimated arc length based on summing the individual lengths of
     *         the line segments computed with {@link #computeVertices(int)}.
     */
    public float getEstimatedArcLength() {
        if (arcLenIndex == null
                || (arcLenIndex != null && arcLenIndex.length != vertices
                        .size())) {
            arcLenIndex = new float[vertices.size()];
        }
        float arcLen = 0;
        for (int i = 1; i < arcLenIndex.length; i++) {
        	ga_Vector2 p = vertices.get(i - 1);
            ga_Vector2 q = vertices.get(i);
            arcLen += p.dst(q);
            arcLenIndex[i] = arcLen;
        }
        return arcLen;
    }

    /**
     * Returns the number of key points.
     * 
     * @return the numP
     */
    public int getNumPoints() {
        return numP;
    }

    /**
     * @return the pointList
     */
    public List<ga_Vector2> getPointList() {
        return pointList;
    }

    /**
     * @see #setTightness(float)
     * @return the spline3d tightness
     * @since 0014 (rev.216)
     */
    public float getTightness() {
        return tightness;
    }

    /**
     * Overrides the current control points with the given list.
     * 
     * @param plist
     * @return itself
     */
    public ga_SplineB setPointList(List<ga_Vector2> plist) {
        pointList.clear();
        for (ga_Vector2 p : plist) {
            add(p);
        }
        return this;
    }

    /**
     * Sets the tightness for future curve interpolation calls. Default value is
     * 0.25. A value of 0.0 equals linear interpolation between the given curve
     * input points. Curve behaviour for values outside the 0.0 .. 0.5 interval
     * is unspecified and becomes increasingly less intuitive. Negative values
     * are possible too and create interesting results (in some cases).
     * 
     * @param tightness
     *            the tightness value used for the next call to
     *            {@link #computeVertices(int)}
     * @since 0014 (rev. 216)
     */
    public ga_SplineB setTightness(float tightness) {
        this.tightness = tightness;
        this.invTightness = 1f / tightness;
        return this;
    }

    public void updateCoefficients() {
        numP = pointList.size();
        if (points == null || (points != null && points.length != numP)) {
            coeffA = new ga_Vector2[numP];
            delta = new ga_Vector2[numP];
            bi = new float[numP];
            for (int i = 0; i < numP; i++) {
                coeffA[i] = new ga_Vector2();
                delta[i] = new ga_Vector2();
            }
            setTightness(tightness);
        }
        points = pointList.toArray(new ga_Vector2[0]);
    }
}