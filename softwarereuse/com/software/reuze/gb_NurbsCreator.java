package com.software.reuze;
import com.software.reuze.d_VectorDouble;
import com.software.reuze.gb_Axis;
import com.software.reuze.gb_Origin3D;
import com.software.reuze.le_ExceptionSingularMatrix;
import com.software.reuze.m_MatrixDouble;

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
 * This class offers some static methods to create NurbsSurfaces and NurbsCurves
 * from different inputs.
 * 
 * @author sg
 * @version 1.0
 */
public final class gb_NurbsCreator {

    private static gb_NurbsKnotVector averaging(final float[] uk, final int p) {
        int m = uk.length + p;
        int n = uk.length - 1;
        float ip = 1f / p;
        float[] u = new float[m + 1];
        for (int i = 0; i <= p; i++) {
            u[m - i] = 1;
        }
        for (int j = 1; j <= n - p; j++) {
            float sum = 0;
            for (int i = j; i <= j + p - 1; i++) {
                sum += uk[i];
            }
            u[j + p] = sum * ip;
        }
        return new gb_NurbsKnotVector(u, p);
    }

    private static float[] centripetal(gb_Vector3[] points) {
        int n = points.length - 1;
        float d = 0;
        float[] uk = new float[n + 1];
        uk[n] = 1;
        double[] tmp = new double[n];
        for (int k = 1; k <= n; k++) {
            tmp[k - 1] = Math.sqrt(points[k].dst(points[k - 1]));
            d += tmp[k - 1];
        }
        d = 1f / d;
        for (int i = 1; i < n; i++) {
            uk[i] = uk[i - 1] + (float) (tmp[i - 1] * d);
        }
        return uk;
    }

    /**
     * Create an Arc.
     * 
     * @param o
     *            Origin to create arc around
     * @param r
     *            Radius of the arc.
     * @param thetaStart
     *            Start angle of the arc in radians
     * @param thetaEnd
     *            End angle of the arc in radians. If end angle is smaller than
     *            start angle, the end angle is increased by 2*PI.
     * @return A NurbsCurve for the Arc.
     */
    public static gb_i_NurbsCurve createArc(gb_Origin3D o, float r, float thetaStart,
            float thetaEnd) {
        gb_Vector3 tmp = new gb_Vector3();

        if (thetaEnd < thetaStart) {
            thetaEnd += m_MathUtils.TWO_PI;
        }
        double theta = thetaEnd - thetaStart;

        int narcs = 4;
        if (theta <= m_MathUtils.HALF_PI) {
            narcs = 1;
        } else if (theta <= m_MathUtils.PI) {
            narcs = 2;
        } else if (theta <= m_MathUtils.THREE_HALVES_PI) {
            narcs = 3;
        }
        double dtheta = theta / narcs;
        int n = 2 * narcs;
        double w1 = Math.cos(dtheta / 2);

        final float sinStart = (float) Math.sin(thetaStart);
        final float cosStart = (float) Math.cos(thetaStart);
        tmp.set(o.xAxis).mul(r * cosStart);
        gb_Vector3 p0 = new gb_Vector3(o.origin).add(tmp);
        tmp.set(o.yAxis).mul(r * sinStart);
        p0.add(tmp);

        tmp.set(o.yAxis).mul(cosStart);
        gb_Vector3 t0 = new gb_Vector3(o.xAxis).mul(-sinStart).add(tmp);

        gc_Vector4[] cps = new gc_Vector4[n + 1];
        cps[0] = new gc_Vector4(p0, 1);
        int index = 0;
        double angle = thetaStart;

        gb_Vector3 p1 = new gb_Vector3();
        gb_Vector3 p2 = new gb_Vector3();
        gb_Vector3 t2 = new gb_Vector3();
        for (int i = 1; i <= narcs; i++) {
            angle += dtheta;
            final double sin = Math.sin(angle);
            final double cos = Math.cos(angle);

            tmp.set(o.xAxis).mul((float) (r * cos));
            p2.set(o.origin).add(tmp);
            tmp.set(o.yAxis).mul((float) (r * sin));
            p2.add(tmp);

            cps[index + 2] = new gc_Vector4(p2, 1);

            t2.set(o.xAxis).mul((float) -sin);
            tmp.set(o.yAxis).mul((float) cos);
            t2.add(tmp);

            lineIntersect3D(p0, t0, p2, t2, p1, p1);

            cps[index + 1] = new gc_Vector4(p1, (float) w1);
            index += 2;
            if (i < narcs) {
                p0.set(p2);
                t0.set(t2);
            }
        }
        int j = n + 1;
        float[] uKnot = new float[j + 3];
        for (int i = 0; i < 3; i++) {
            uKnot[i + j] = 1;
        }
        switch (narcs) {
            case 2:
                uKnot[3] = 0.5f;
                uKnot[4] = 0.5f;
                break;
            case 3:
                uKnot[3] = uKnot[4] = 1f/3f;
                uKnot[5] = uKnot[6] = 2f/3f;
                break;
            case 4:
                uKnot[3] = 0.25f;
                uKnot[4] = 0.25f;
                uKnot[5] = 0.5f;
                uKnot[6] = 0.5f;
                uKnot[7] = 0.75f;
                uKnot[8] = 0.75f;
                break;
        }

        return new gb_NurbsCurveBasic(cps, uKnot, 2);
    }

    /**
     * Create a full-circle NurbsCurve around the given Origin with radius r.
     * The NurbsCurve has control polygon which has 7 control points and the shape
     * of quadratic.
     * 
     * @param o
     *            Origin to create the full-circle around
     * @param r
     *            Radius of the full-circle
     * @return A NurbsCurve for a full-circle
     */
    public static gb_i_NurbsCurve createFullCircleQuad7(gb_Origin3D o, float r) {

        gc_Vector4[] cp = new gc_Vector4[7];
        cp[0] = new gc_Vector4(o.xAxis.tmp().mul(r), 1);
        cp[3] = cp[0].getInvertedXYZ();
        cp[6] = cp[0].copy();

        cp[1] = new gc_Vector4(o.yAxis.tmp().add(o.xAxis).mul(r), 0.5f);
        cp[4] = cp[1].getInvertedXYZ();

        cp[2] = new gc_Vector4(o.xAxis.tmp().inv().add(o.yAxis).mul(r),
                0.5f);
        cp[5] = cp[2].getInvertedXYZ();

        for (int i = 0; i < 7; i++) {
            cp[i].addXYZSelf(o.origin);
        }
        float[] u = {
                0, 0, 0, 0.25f, 0.5f, 0.5f, 0.75f, 1, 1, 1
        };
        return new gb_NurbsCurveBasic(cp, u, 2);
    }

    /**
     * Create a full-circle NurbsCurve around the given Origin with radius r.
     * The NurbsCurve has control polygon which has 9 control points and the shape
     * of quadratic.
     * 
     * @param o
     *            Origin to create the full-circle around
     * @param r
     *            Radius of the full-circle
     * @return A NurbsCurve for a full-circle
     */
    public static gb_i_NurbsCurve createFullCircleQuad9(gb_Origin3D o, float r) {
        final float w = m_MathUtils.SQRT2 / 2;

        gc_Vector4[] cp = new gc_Vector4[9];
        cp[0] = new gc_Vector4(o.xAxis.tmp().mul(r), 1);
        cp[4] = cp[0].getInvertedXYZ();
        cp[8] = cp[0].copy();

        cp[1] = new gc_Vector4(o.xAxis.tmp().add(o.yAxis).mul(r), w);
        cp[5] = cp[1].getInvertedXYZ();

        cp[2] = new gc_Vector4(o.yAxis.tmp().mul(r), 1);
        cp[6] = cp[2].getInvertedXYZ();

        cp[3] = new gc_Vector4(o.xAxis.tmp().inv().add(o.yAxis).mul(r),
                w);
        cp[7] = cp[3].getInvertedXYZ();

        for (int i = 0; i < 9; i++) {
            cp[i].addXYZSelf(o.origin);
        }
        float[] u = {
                0, 0, 0, 0.25f, 0.25f, 0.5f, 0.5f, 0.75f, 0.75f, 1, 1, 1
        };
        return new gb_NurbsCurveBasic(cp, u, 2);
    }

    /**
     * Create a revolved NurbsSurface from the given NurbsCurve around the given
     * axis with the angle theta.
     * 
     * @param a
     *            Axis to revolve around.
     * @param curve
     *            NurbsCurve to revolve
     * @param theta
     *            Angle to revolve
     * @return The revolved NurbsSurface
     */
    // TODO:call createRevolvedSurface(Axis3D a, NurbsCurve curve, double
    // thetaStart, double thetaEnd) as as it is tested
    public static gb_i_NurbsSurface createRevolvedSurface(gb_Axis a,
            gb_i_NurbsCurve curve, double theta) {
        int narcs = 4;
        if (theta <= m_MathUtils.HALF_PI) {
            narcs = 1;
        } else if (theta <= m_MathUtils.PI) {
            narcs = 2;
        } else if (theta <= m_MathUtils.THREE_HALVES_PI) {
            narcs = 3;
        }

        int j = 3 + 2 * (narcs - 1);
        final double dtheta = theta / narcs;
        final float[] uKnot = new float[j + 3];
        for (int i = 0; i < 3; i++) {
            uKnot[j + i] = 1;
        }
        switch (narcs) {
            case 2:
                uKnot[3] = 0.5f;
                uKnot[4] = 0.5f;
                break;
            case 3:
                uKnot[3] = uKnot[4] = 1f/3f;
                uKnot[5] = uKnot[6] = 2f/3f;
                break;
            case 4:
                uKnot[3] = 0.25f;
                uKnot[4] = 0.25f;
                uKnot[5] = 0.5f;
                uKnot[6] = 0.5f;
                uKnot[7] = 0.75f;
                uKnot[8] = 0.75f;
                break;
        }

        double angle = 0;
        final double[] cos = new double[narcs + 1];
        final double[] sin = new double[narcs + 1];
        for (int i = 0; i <= narcs; i++) {
            cos[i] = Math.cos(angle);
            sin[i] = Math.sin(angle);
            angle += dtheta;
        }

        gc_Vector4[] pj = curve.getControlPoints();
        gb_Vector3 P0 = new gb_Vector3();
        final gb_Vector3 P2 = new gb_Vector3();
        final gb_Vector3 O = new gb_Vector3();
        final gb_Vector3 T2 = new gb_Vector3();
        final gb_Vector3 T0 = new gb_Vector3();
        final gb_Vector3 tmp = new gb_Vector3();
        final gb_Vector3 X = new gb_Vector3();
        final gb_Vector3 Y = new gb_Vector3();
        final gc_Vector4[][] pij = new gc_Vector4[2 * narcs + 1][pj.length];
        final double wm = Math.cos(dtheta / 2);
        for (j = 0; j < pj.length; j++) {
            pointToLine3D(a.origin, a.dir, pj[j].to3D(), O);
            X.set(pj[j].to3D().sub(O));
            final double r = X.len();
            if (r == 0) {
                X.set(O);
            }
            X.nor();
            Y.set( a.dir.tmp().crs(X) );
            pij[0][j] = new gc_Vector4(pj[j]);
            P0 = pj[j].to3D();
            T0.set(Y);
            int index = 0;
            for (int i = 1; i <= narcs; i++) {
                tmp.set(X).mul((float) (r * cos[i]));
                P2.set(O).add(tmp);
                tmp.set(Y).mul((float) (r * sin[i]));
                P2.add(tmp);

                pij[index + 2][j] = new gc_Vector4(P2, pj[j].w);

                tmp.set(Y).mul((float) cos[i]);
                T2.set(X).mul((float) -sin[i]).add(tmp);

                lineIntersect3D(P0, T0, P2, T2, tmp, tmp);
                pij[index + 1][j] = new gc_Vector4(tmp, (float) (wm * pj[j].w));

                index += 2;
                if (i < narcs) {
                    P0.set(P2);
                    T0.set(T2);
                }

            }
        }
        gb_NurbsControlNet cnet = new gb_NurbsControlNet(pij);
        return new gb_NurbsSurfaceBasic(cnet, uKnot, curve.getKnots(), 2,
                curve.getDegree());
    }

    /**
     * Create a revolved NurbsSurface from the given NurbsCurve around the given
     * axis with the angle theta.
     * 
     * @param a
     *            Axis to revolve around.
     * @param curve
     *            NurbsCurve to revolve
     * @param thetaStart
     *            Angle to start revolution
     * @param thetaEnd
     *            Angle to end revolution
     * @return The revolved NurbsSurface
     */
    public static gb_i_NurbsSurface createRevolvedSurface(gb_Axis a,
            gb_i_NurbsCurve curve, double thetaStart, double thetaEnd) {
        int narcs = 4;
        if (thetaStart > thetaEnd) {
            double tmp = thetaEnd;
            thetaEnd = thetaStart;
            thetaStart = tmp;
        }
        double theta = thetaEnd - thetaStart;
        if (theta <= m_MathUtils.HALF_PI) {
            narcs = 1;
        } else if (theta <= m_MathUtils.PI) {
            narcs = 2;
        } else if (theta <= m_MathUtils.THREE_HALVES_PI) {
            narcs = 3;
        }

        int j = 3 + 2 * (narcs - 1);
        final double dtheta = theta / narcs;
        final float[] uKnot = new float[j + 3];
        for (int i = 0; i < 3; i++) {
            uKnot[i] = 0;
            uKnot[j + i] = 1;
        }
        switch (narcs) {
            case 2:
                uKnot[3] = 0.5f;
                uKnot[4] = 0.5f;
                break;
            case 3:
                uKnot[3] = uKnot[4] = 1f/3f;
                uKnot[5] = uKnot[6] = 2f/3f;
                break;
            case 4:
                uKnot[3] = 0.25f;
                uKnot[4] = 0.25f;
                uKnot[5] = 0.5f;
                uKnot[6] = 0.5f;
                uKnot[7] = 0.75f;
                uKnot[8] = 0.75f;
                break;
        }

        double angle = thetaStart;
        final double[] cos = new double[narcs + 1];
        final double[] sin = new double[narcs + 1];
        for (int i = 0; i <= narcs; i++) {
            cos[i] = Math.cos(angle);
            sin[i] = Math.sin(angle);
            angle += dtheta;
        }

        final gc_Vector4[] pj = curve.getControlPoints();
        gb_Vector3 P0 = new gb_Vector3();
        final gb_Vector3 O = new gb_Vector3();
        final gb_Vector3 P2 = new gb_Vector3();
        final gb_Vector3 T2 = new gb_Vector3();
        final gb_Vector3 T0 = new gb_Vector3();
        final gb_Vector3 tmp = new gb_Vector3();
        final gb_Vector3 X = new gb_Vector3();
        final gb_Vector3 Y = new gb_Vector3();
        final gc_Vector4[][] pij = new gc_Vector4[2 * narcs + 1][pj.length];
        final double wm = Math.cos(dtheta / 2);
        for (j = 0; j < pj.length; j++) {
            pointToLine3D(a.origin, a.dir, pj[j].to3D(), O);
            X.set(pj[j].to3D().sub(O));
            final double r = X.len();
            if (r == 0) {
                X.set(O);
            }
            X.nor();
            Y.set( a.dir.tmp().crs(X) );
            pij[0][j] = new gc_Vector4(pj[j]);
            P0 = pj[j].to3D();
            T0.set(Y);
            int index = 0;
            for (int i = 1; i <= narcs; i++) {
                tmp.set(X).mul((float) (r * cos[i]));
                P2.set(O).add(tmp);
                tmp.set(Y).mul((float) (r * sin[i]));
                P2.add(tmp);

                pij[index + 2][j] = new gc_Vector4(P2, pj[j].w);

                tmp.set(Y).mul((float) cos[i]);
                T2.set(X).mul((float) -sin[i]).add(tmp);

                lineIntersect3D(P0, T0, P2, T2, tmp, tmp);
                pij[index + 1][j] = new gc_Vector4(tmp, (float) (wm * pj[j].w));

                index += 2;
                if (i < narcs) {
                    P0.set(P2);
                    T0.set(T2);
                }
            }
        }
        gb_NurbsControlNet cnet = new gb_NurbsControlNet(pij);
        return new gb_NurbsSurfaceBasic(cnet, uKnot, curve.getKnots(), 2,
                curve.getDegree());
    }

    /**
     * Create a semi-circle NurbsCurve around the given Origin with radius r.
     * 
     * @param o
     *            Origin to create semi-circle around.
     * @param r
     *            Radius of the semi-circle
     * @return A NurbsCurve for a semi-circle
     */
    public static gb_i_NurbsCurve createSemiCircle(gb_Origin3D o, float r) {
        gc_Vector4[] cp = new gc_Vector4[4];
        cp[0] = new gc_Vector4(o.xAxis.tmp().mul(r), 1);
        cp[3] = cp[0].getInvertedXYZ();
        cp[0].addXYZSelf(o.origin);
        cp[3].addXYZSelf(o.origin);
        cp[1] = new gc_Vector4(o.xAxis.tmp().add(o.yAxis).mul(r).add(o.origin),
                0.5f);
        cp[2] = new gc_Vector4(o.xAxis.tmp().inv().add(o.yAxis).mul(r)
                .add(o.origin), 0.5f);

        float[] u = {
                0, 0, 0, 0.5f, 1, 1, 1
        };
        return new gb_NurbsCurveBasic(cp, u, 2);
    }

    /**
     * Creates a {@link gb_i_NurbsSurface} by swinging a profile {@link gb_i_NurbsCurve}
     * in the XZ plane around a trajectory curve in the XY plane. Both curves
     * MUST be offset from the major axes (i.e. their control points should have
     * non-zero coordinates for the Y coordinates of the profile curve and the Z
     * coordinates of the trajectory).
     * 
     * @param proj
     *            profile curve in XZ
     * @param traj
     *            trajectory curve in XY
     * @param alpha
     *            scale factor
     * @return 3D NURBS surface
     */
    public static gb_i_NurbsSurface createSwungSurface(gb_i_NurbsCurve proj,
            gb_i_NurbsCurve traj, float alpha) {
        gc_Vector4[] cpProj = proj.getControlPoints();
        gc_Vector4[] cpTraj = traj.getControlPoints();

        // The NURBS Book, Piegl, p.455,456
        // http://books.google.co.uk/books?id=7dqY5dyAwWkC&pg=PA455&lpg=PA455
        // fixed Z handling (was wrong in original jgeom version)
        gc_Vector4[][] cps = new gc_Vector4[cpProj.length][cpTraj.length];
        for (int i = 0; i < cpProj.length; i++) {
            for (int j = 0; j < cpTraj.length; j++) {
                gc_Vector4 cp = new gc_Vector4();
                cp.x = cpProj[i].x * cpTraj[j].x * alpha;
                cp.y = cpProj[i].y * cpTraj[j].y * alpha;
                cp.z = (cpProj[i].z + cpTraj[j].z) * alpha;
                cp.w = cpProj[i].w * cpTraj[j].w;
                cps[i][j] = cp;
            }
        }
        return new gb_NurbsSurfaceBasic(new gb_NurbsControlNet(cps), proj.getKnots(),
                traj.getKnots(), proj.getDegree(), traj.getDegree());

    }

    /**
     * Perform a linear extrusion of the given {@link gb_i_NurbsCurve} along the
     * supplied vector to produce a new {@link gb_i_NurbsSurface}. The extrusion
     * length is the length of the vector given.
     * 
     * @param curve
     *            NURBS curve instance
     * @param extrude
     *            a extrusion vector
     * @return a NurbsSurface.
     */
    public static gb_i_NurbsSurface extrudeCurve(gb_i_NurbsCurve curve, gb_Vector3 extrude) {

        // Curve and Surface Construction using Rational B-splines
        // Piegl and Tiller CAD Vol 19 #9 November 1987 pp 485-498
        gb_NurbsKnotVector vKnot = new gb_NurbsKnotVector(new float[] {
                0f, 0f, 1f, 1f
        }, 1);

        gc_Vector4[][] cpoints = new gc_Vector4[curve.getControlPoints().length][2];
        gc_Vector4[] curvePoints = curve.getControlPoints();
        for (int i = 0; i < cpoints.length; i++) {
            for (int j = 0; j < 2; j++) {
                /*
                 * Change added 11/02/90 Steve Larkin : Have multiplied the term
                 * wcoord to the extrusion vector before adding to the curve
                 * coordinates. Not really sure this is the correct fix, but it
                 * works !
                 */
                gc_Vector4 cp = new gc_Vector4();
                cp.x = curvePoints[i].x + j * extrude.x;
                cp.y = curvePoints[i].y + j * extrude.y;
                cp.z = curvePoints[i].z + j * extrude.z;
                cp.w = curvePoints[i].w;
                cpoints[i][j] = cp;
            }
        }
        gb_NurbsControlNet cnet = new gb_NurbsControlNet(cpoints);
        return new gb_NurbsSurfaceBasic(cnet, curve.getKnots(), vKnot.getArray(),
                curve.getDegree(), vKnot.getDegree());
    }

    /**
     * Interpolates a NurbCurve form the given Points using a global
     * interpolation technique.
     * 
     * @param points
     *            Points to interpolate
     * @param degree
     *            degree of the interpolated NurbsCurve
     * @return A NurbsCurve interpolating the given Points
     * @throws le_ExceptionInterpolation
     *             thrown if interpolation failed or is not possible.
     */
    public static gb_i_NurbsCurve globalCurveInterpolation(gb_Vector3[] points, int degree)
            throws le_ExceptionInterpolation {
        try {
            final int n = points.length;
            final double[] A = new double[n * n];

            final float[] uk = centripetal(points);
            gb_NurbsKnotVector uKnots = averaging(uk, degree);
            for (int i = 0; i < n; i++) {
                int span = uKnots.findSpan(uk[i]);
                double[] tmp = uKnots.basisFunctions(span, uk[i]);
                System.arraycopy(tmp, 0, A, i * n + span - degree, tmp.length);
            }
            final m_MatrixDouble a = new m_MatrixDouble(n, n, A);
            final d_VectorDouble perm = new d_VectorDouble(n);
            final m_MatrixDouble lu = new m_MatrixDouble(n, n);
            a.computeLUD(lu, perm);

            final gc_Vector4[] cps = new gc_Vector4[n];
            for (int i = 0; i < cps.length; i++) {
                cps[i] = new gc_Vector4(0, 0, 0, 1);
            }

            // x-ccordinate
            final d_VectorDouble b = new d_VectorDouble(n);
            for (int j = 0; j < n; j++) {
                b.setElement(j, points[j].x);
            }
            final d_VectorDouble sol = new d_VectorDouble(n);
            sol.backSolveLUD(lu, b, perm);
            for (int j = 0; j < n; j++) {
                cps[j].x = (float) sol.get(j);
            }

            // y-ccordinate
            for (int j = 0; j < n; j++) {
                b.setElement(j, points[j].y);
            }
            sol.zero();
            sol.backSolveLUD(lu, b, perm);
            for (int j = 0; j < n; j++) {
                cps[j].y = (float) sol.get(j);
            }

            // z-ccordinate
            for (int j = 0; j < n; j++) {
                b.setElement(j, points[j].z);
            }
            sol.zero();
            sol.backSolveLUD(lu, b, perm);
            for (int j = 0; j < n; j++) {
                cps[j].z = (float) sol.get(j);
            }
            return new gb_NurbsCurveBasic(cps, uKnots);
        } catch (le_ExceptionSingularMatrix ex) {
            throw new le_ExceptionInterpolation(ex);
        }

    }

    /**
     * Interpolates a NurbsSurface from the given points using a global
     * interpolation technique.
     * 
     * @param points
     *            Points arranged in a net (matrix) to interpolate
     * @param uDegrees
     *            degree in u direction
     * @param vDegrees
     *            degree in v direction
     * @return A NurbsSurface interpolating the given points.
     * @throws le_ExceptionInterpolation
     *             thrown if interpolation failed or is not possible.
     */
    public static gb_i_NurbsSurface globalSurfaceInterpolation(gb_Vector3[][] points,
            int uDegrees, int vDegrees) throws le_ExceptionInterpolation {
        final int n = points.length;
        final int m = points[0].length;
        float[][] uv = surfaceMeshParameters(points, n - 1, m - 1);
        gb_NurbsKnotVector u = averaging(uv[0], uDegrees);
        gb_NurbsKnotVector v = averaging(uv[1], vDegrees);

        gc_Vector4[][] r = new gc_Vector4[m][n];
        gb_Vector3[] tmp = new gb_Vector3[n];
        for (int l = 0; l < m; l++) {
            for (int i = 0; i < n; i++) {
                tmp[i] = points[i][l];
            }
            try {
                gb_i_NurbsCurve curve = globalCurveInterpolation(tmp, uDegrees);
                r[l] = curve.getControlPoints();
            } catch (le_ExceptionInterpolation ex) {
                for (int i = 0; i < tmp.length; i++) {
                    r[l][i] = new gc_Vector4(tmp[i], 1);
                }
            }

        }

        gc_Vector4[][] cp = new gc_Vector4[n][m];
        tmp = new gb_Vector3[m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tmp[j] = r[j][i].to3D();
            }
            try {
                gb_i_NurbsCurve curve = globalCurveInterpolation(tmp, vDegrees);
                cp[i] = curve.getControlPoints();
            } catch (le_ExceptionInterpolation ex) {
                for (int j = 0; j < tmp.length; j++) {
                    cp[i][j] = new gc_Vector4(tmp[j], 1);
                }
            }
        }

        return new gb_NurbsSurfaceBasic(new gb_NurbsControlNet(cp), u, v);
    }

    private static void lineIntersect3D(gb_Vector3 p0, gb_Vector3 t0, gb_Vector3 p2, gb_Vector3 t2,
            gb_Vector3 out0, gb_Vector3 out2) {
        gb_Vector3 v02 = p0.sub(p2);

        double a = t0.dot(t0);
        double b = t0.dot(t2);
        double c = t2.dot(t2);
        double d = t0.dot(v02);
        double e = t2.dot(v02);
        double denom = a * c - b * b;

        double mu0, mu2;

        if (denom < m_MathUtils.EPS) {
            mu0 = 0;
            mu2 = b > c ? d / b : e / c;
        } else {
            mu0 = (b * e - c * d) / denom;
            mu2 = (a * e - b * d) / denom;
        }

        out0.set(t0.tmp().mul((float) mu0).add(p0));
        out2.set(t2.tmp().mul((float) mu2).add(p2));
    }

    private static void pointToLine3D(gb_Vector3 p, gb_Vector3 t,
            gb_Vector3 top, gb_Vector3 out) {
        gb_Vector3 dir = top.tmp().sub(p);
        float hyp = dir.len();
        out.set(p).add( t.tmp().mul( t.dot(dir.nor()) * hyp) );
    }

    private static float[][] surfaceMeshParameters(gb_Vector3 points[][], int n,
            int m) {
        final float[][] res = new float[2][];
        int num = m + 1;
        final float[] cds = new float[(n + 1) * (m + 1)];
        final float[] uk = new float[n + 1];
        uk[n] = 1;
        for (int l = 0; l <= m; l++) {
            float total = 0;
            for (int k = 1; k <= n; k++) {
                cds[k] = points[k][l].dst(points[k - 1][l]);
                total += cds[k];
            }
            if (total == 0) {
                num = num - 1;
            } else {
                float d = 0;
                total = 1f / total;
                for (int k = 1; k <= n; k++) {
                    d += cds[k];
                    uk[k] += d * total;
                }
            }
        }
        if (num == 0) {
            return null;
        }
        float inum = 1f / num;
        for (int k = 1; k < n; k++) {
            uk[k] *= inum;
        }

        num = n + 1;
        final float[] vk = new float[m + 1];
        vk[m] = 1;
        for (int l = 0; l <= n; l++) {
            float total = 0;
            gb_Vector3[] pl = points[l];
            for (int k = 1; k <= m; k++) {
                cds[k] = pl[k].dst(pl[k - 1]);
                total += cds[k];
            }
            if (total == 0) {
                num = num - 1;
            } else {
                float d = 0;
                total = 1f / total;
                for (int k = 1; k <= m; k++) {
                    d += cds[k];
                    vk[k] += d * total;
                }
            }
        }
        if (num == 0) {
            return null;
        }
        inum = 1f / num;
        for (int k = 1; k < m; k++) {
            vk[k] *= inum;
        }
        res[0] = uk;
        res[1] = vk;
        return res;
    }

    private gb_NurbsCreator() {
    }
}
