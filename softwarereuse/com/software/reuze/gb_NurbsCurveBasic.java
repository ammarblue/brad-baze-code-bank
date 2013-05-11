package com.software.reuze;

/*
 * jgeom: Geometry Library for Java
 * 
 * Copyright (C) 2005  Samuel Gerber
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/**
 * A Basic implementation of a NurbsCurve.
 * 
 * @author sg
 * @version 1.0
 */
public class gb_NurbsCurveBasic implements gb_i_NurbsCurve, Cloneable {

    private gc_Vector4[] cpoly;
    private gb_NurbsKnotVector uKnots;

    /**
     * Create a Nurbs Curve from the given Controlpoints, Knots and degree.<br>
     * [TODO Validate Input, part of it is done by creating the KnotVector]
     * 
     * @param cps
     *            Array of Controlpoints
     * @param uK
     *            Knot values
     * @param degree
     *            Degree of the Nurbs Curve
     */
    public gb_NurbsCurveBasic(gc_Vector4[] cps, float[] uK, int degree) {
        this(cps, new gb_NurbsKnotVector(uK, degree));
    }

    /**
     * Generate a Nurbs from the given Controlpoints and the given Knotvector.<br>
     * [TODO validate input]
     * 
     * @param cps
     *            Array of Controlpoints
     * @param uKnots
     *            Knotvector of the Nurbs
     */
    public gb_NurbsCurveBasic(gc_Vector4[] cps, gb_NurbsKnotVector uKnots) {
        cpoly = cps;
        this.uKnots = uKnots;
        if (uKnots.length() != uKnots.getDegree() + cpoly.length + 1) {
            throw new IllegalArgumentException(
                    "Nurbs Curve has wrong knot number");
        }
    }

    public gc_Vector4[][] curveDeriveCpts(int d, int r1, int r2) {

    	gc_Vector4[][] result = new gc_Vector4[d + 1][r2 - r1 + 1];

        int degree = uKnots.getDegree();

        // k=0 => control points
        int r = r2 - r1;
        for (int i = 0; i <= r; i++) {
            result[0][i] = cpoly[i];
        }

        // k=1 => 1st derivative, k=2 => 2nd derivative, etc...
        for (int k = 1; k <= d; k++) {
            int tmp = degree - k + 1;
            for (int i = 0; i <= (r - k); i++) {
            	gc_Vector4 cw = new gc_Vector4(result[k - 1][i + 1]);
                cw.subSelf(result[k - 1][i]);
                cw.scaleSelf(tmp);
                cw.scaleSelf(1 / (uKnots.get(r1 + i + degree + 1) - uKnots
                        .get(r1 + i + k)));
                result[k][i] = cw;
            }
        }
        return result;
    }

    public gb_Vector3[] derivativesOnCurve(float u, int grade) {
        return derivativesOnCurve(u, grade, new gb_Vector3[grade + 1]);
    }

    public gb_Vector3[] derivativesOnCurve(float u, int grade, gb_Vector3[] derivs) {

        int span = uKnots.findSpan(u);
        int degree = uKnots.getDegree();

        // TODO: compute derivatives also for NURBS
        // currently supports only non-rational B-Splines
        float derivVals[][] = uKnots.deriveBasisFunctions(span, u, grade);

        // Zero values
        for (int k = (degree + 1); k <= grade; k++) {
            derivs[k] = new gb_Vector3();
        }

        for (int k = 0; k <= grade; k++) {
        	gb_Vector3 d = new gb_Vector3();
            for (int j = 0; j <= degree; j++) {
            	gc_Vector4 v = cpoly[(span - degree) + j];
                float s = derivVals[k][j];
                d.add(v.x * s, v.y * s, v.z * s);
            }
            derivs[k] = d;
        }
        return derivs;
    }

    public gc_Vector4[] getControlPoints() {
        return cpoly;
    }

    public int getDegree() {
        return uKnots.getDegree();
    }

    public float[] getKnots() {
        return uKnots.getArray();
    }

    public gb_NurbsKnotVector getKnotVector() {
        return uKnots;
    }

    public gb_Vector3 pointOnCurve(float u) {
        return pointOnCurve(u, new gb_Vector3());
    }

    public gb_Vector3 pointOnCurve(float u, gb_Vector3 out) {
        int span = uKnots.findSpan(u);
        int degree = uKnots.getDegree();

        // for periodic knot vectors the usable parameter range is
        // span >= degree and span <= no control points (n+1)
        if (span < degree) {
            return out;
        }

        if (span > uKnots.getN()) {
            return out;
        }

        double[] bf = uKnots.basisFunctions(span, u);
        gc_Vector4 cw = new gc_Vector4();
        for (int i = 0; i <= degree; i++) {
            cw.addSelf(cpoly[(span - degree) + i].getWeighted().scaleSelf(
                    (float) bf[i]));
        }
        return cw.unweightInto(out);
    }

    public ga_Polygon toPolygon(int res) {
        float delta = 1f / (res - 1);
        ga_Polygon poly = new ga_Polygon();
        for (int i = 0; i < res; i++) {
            poly.add(pointOnCurve(i * delta).to2DXY());
        }
        poly.calcCenter();
        return poly;
    }
}
