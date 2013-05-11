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
 * A Basic NurbsSurface implementation.
 * 
 * @author sg
 * @version 1.2
 */
public class gb_NurbsSurfaceBasic implements gb_i_NurbsSurface {

    private gb_NurbsKnotVector uKnots;
    private gb_NurbsKnotVector vKnots;
    private gb_NurbsControlNet cpnet;

    // private List<TrimCurve> trimms = new LinkedList<TrimCurve>();

    /**
     * Create a Nurbs Surface from the given {@link ControlNet} and the knot
     * values of degree p in u direction and of degree q in v direction.
     * 
     * @param cps
     *            ControlNet of the Nurbs
     * @param uK
     *            Knot values in u direction
     * @param vK
     *            Knot values in v direction
     * @param p
     *            degree in u direction
     * @param q
     *            degree in v direction
     */
    public gb_NurbsSurfaceBasic(gb_NurbsControlNet cps, float[] uK, float[] vK, int p,
            int q) throws IllegalArgumentException {
        this(cps, new gb_NurbsKnotVector(uK, p), new gb_NurbsKnotVector(vK, q));
    }

    /**
     * Create a Nurbs form the Controlnet and the two Knot vectors.
     * 
     * @param net
     *            Control net of Nurbs
     * @param u
     *            KnotVector in u direction
     * @param v
     *            KnotVector in v direction
     */
    public gb_NurbsSurfaceBasic(gb_NurbsControlNet net, gb_NurbsKnotVector u, gb_NurbsKnotVector v)
            throws IllegalArgumentException {
        cpnet = net;
        uKnots = u;
        vKnots = v;
        validate();
    }

    // public void addTrimCurve(TrimCurve tc) {
    // trimms.add(tc);
    // }

    public gb_NurbsControlNet getControlNet() {
        return cpnet;
    }

    // public List<TrimCurve> getTrimCurves() {
    // return trimms;
    // }

    public int getUDegree() {
        return uKnots.getDegree();
    }

    public float[] getUKnots() {
        return uKnots.getArray();
    }

    public gb_NurbsKnotVector getUKnotVector() {
        return uKnots;
    }

    public int getVDegree() {
        return vKnots.getDegree();
    }

    public float[] getVKnots() {
        return vKnots.getArray();
    }

    public gb_NurbsKnotVector getVKnotVector() {
        return vKnots;
    }

    public gb_Vector3 pointOnSurface(double u, double v) {
        return pointOnSurface((float) u, (float) v, new gb_Vector3());
    }

    public gb_Vector3 pointOnSurface(float u, float v) {
        return pointOnSurface(u, v, new gb_Vector3());
    }

    public gb_Vector3 pointOnSurface(float u, float v, gb_Vector3 out) {

        // Piegl -> Algorithm A4.3 -> page 134

        int uspan = uKnots.findSpan(u);
        double[] bfu = uKnots.basisFunctions(uspan, u);

        int vspan = vKnots.findSpan(v);
        double[] bfv = vKnots.basisFunctions(vspan, v);

        int p = uKnots.getDegree();
        int q = vKnots.getDegree();
        gc_Vector4[] tmp = new gc_Vector4[q + 1];
        for (int l = 0; l <= q; l++) {
            gc_Vector4 pw = new gc_Vector4();
            for (int k = 0; k <= p; k++) {
                pw.addSelf(cpnet.get(uspan - p + k, vspan - q + l)
                        .getWeighted().scaleSelf((float) bfu[k]));
            }
            tmp[l] = pw;
        }
        gc_Vector4 sw = new gc_Vector4();
        for (int l = 0; l <= q; l++) {
            sw.addSelf(tmp[l].scaleSelf((float) bfv[l]));
        }
        return sw.unweightInto(out);
    }

    public gc_Vector4[][][][] surfaceDeriveCpts(int d, int r1, int r2, int s1, int s2) {

        gc_Vector4[][][][] result = new gc_Vector4[d + 1][d + 1][r2 - r1 + 1][s2 - s1 + 1];
        int degreeU = uKnots.getDegree();
        int degreeV = vKnots.getDegree();

        int du = d < degreeU ? d : degreeU;
        int dv = d < degreeV ? d : degreeV;
        int r = r2 - r1;
        int s = s2 - s1;
        gc_Vector4[][] cps = cpnet.getControlPoints();
        gc_Vector4[] ucps = new gc_Vector4[cpnet.uLength()];
        for (int j = s1; j <= s2; j++) {
            for (int idxu = 0; idxu < cpnet.uLength(); idxu++) {
                ucps[idxu] = cps[idxu][j];
            }
            gc_Vector4[][] tmp = new gb_NurbsCurveBasic(ucps, uKnots).curveDeriveCpts(
                    du, r1, r2);
            for (int k = 0; k <= du; k++) {
                final gc_Vector4[][] resk0 = result[k][0];
                for (int i = 0; i <= (r - k); i++) {
                    resk0[i][j - s1] = tmp[k][i];
                }
            }
        }

        for (int k = 0; k <= du; k++) {
            final gc_Vector4[][] resk0 = result[k][0];
            for (int i = 0; i <= (r - k); i++) {
                final int length = resk0[i].length;
                final gc_Vector4[] resk0i = resk0[i];
                gc_Vector4[] vcps = new gc_Vector4[length];
                for (int idx = 0; idx < length; idx++) {
                    vcps[idx] = resk0i[idx];
                }
                final int dd = (d - k) < dv ? (d - k) : dv;
                gc_Vector4[][] tmp = new gb_NurbsCurveBasic(vcps, vKnots)
                        .curveDeriveCpts(dd, 0, s);
                for (int l = 1; l <= dd; l++) {
                    final gc_Vector4[] reskli = result[k][l][i];
                    final gc_Vector4[] tmpL = tmp[l];
                    for (int j = 0; j <= (s - 1); j++) {
                        reskli[j] = tmpL[j];
                    }
                }
            }
        }
        return result;
    }

    private void validate() {
        if (uKnots.length() != uKnots.getDegree() + cpnet.uLength() + 1) {
            throw new IllegalArgumentException(
                    "Nurbs Surface has wrong Knot number in u Direction");
        }
        if (vKnots.length() != vKnots.getDegree() + cpnet.vLength() + 1) {
            throw new IllegalArgumentException(
                    "Nurbs Surface has wrong Knot number in v Direction");
        }

    }
}
