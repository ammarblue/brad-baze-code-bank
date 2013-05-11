package com.software.reuze;
/*
 * @(#)Matrix3D.java	1.4 98/03/18
 *
 * Copyright (c) 1995-1997 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 *
 * Modified by hqm@ai.mit.edu for IAppli fixed point math lib
 */

/** A fairly conventional 3D matrix object that can transform sets of
    3D points and perform a variety of manipulations on the transform */


public class m_MatrixFixedPoint {
    int xx, xy, xz, xo;
    int yx, yy, yz, yo;
    int zx, zy, zz, zo;

    /** Create a new unit matrix */
    public m_MatrixFixedPoint () {
	xx = m_FixedPoint.ONE;
	yy = m_FixedPoint.ONE;
	zz = m_FixedPoint.ONE;
    }
    public void set(float[] in) {
    	xx = m_FixedPoint.fixedValue(in[0]);
    	xy = m_FixedPoint.fixedValue(in[1]);
    	xz = m_FixedPoint.fixedValue(in[2]);
    	xo = m_FixedPoint.fixedValue(in[3]);
    	yx = m_FixedPoint.fixedValue(in[4]);
    	yy = m_FixedPoint.fixedValue(in[5]);
    	yz = m_FixedPoint.fixedValue(in[6]);
    	yo = m_FixedPoint.fixedValue(in[7]);
    	zx = m_FixedPoint.fixedValue(in[8]);
    	zy = m_FixedPoint.fixedValue(in[9]);
    	zz = m_FixedPoint.fixedValue(in[10]);
    	zo = m_FixedPoint.fixedValue(in[11]);
    }
    public m_MatrixFixedPoint (float[] in) {
    	this.set(in);
    }
    public m_MatrixFixedPoint(m_MatrixFixedPoint f) {
    	this.set(f);
    }
    public void set(m_MatrixFixedPoint f) {
    	xx = f.xx;
    	xy = f.xy;
    	xz = f.xz;
    	xo = f.xo;
    	yx = f.yx;
    	yy = f.yy;
    	yz = f.yz;
    	yo = f.yo;
    	zx = f.zx;
    	zy = f.zy;
    	zz = f.zz;
    	zo = f.zo;
    }
    /** Scale by f in all dimensions */
    public void scale(int f) {
	xx = m_FixedPoint.multiply(xx, f);
	xy = m_FixedPoint.multiply(xy, f);
	xz = m_FixedPoint.multiply(xz, f);
	xo = m_FixedPoint.multiply(xo, f);
	yx = m_FixedPoint.multiply(yx, f);
	yy = m_FixedPoint.multiply(yy, f);
	yz = m_FixedPoint.multiply(yz, f);
	yo = m_FixedPoint.multiply(yo, f);
	zx = m_FixedPoint.multiply(zx, f);
	zy = m_FixedPoint.multiply(zy, f);
	zz = m_FixedPoint.multiply(zz, f);
	zo = m_FixedPoint.multiply(zo, f);
    }
    public void scale(float f) {
    	scale(m_FixedPoint.fixedValue(f));
    }
    /** Scale along each axis independently */
    public void scale(int xf, int yf, int zf) {
	xx = m_FixedPoint.multiply(xx, xf);
	xy = m_FixedPoint.multiply(xy, xf);
	xz = m_FixedPoint.multiply(xz, xf);
	xo = m_FixedPoint.multiply(xo, xf);
	yx = m_FixedPoint.multiply(yx, yf);
	yy = m_FixedPoint.multiply(yy, yf);
	yz = m_FixedPoint.multiply(yz, yf);
	yo = m_FixedPoint.multiply(yo, yf);
	zx = m_FixedPoint.multiply(zx, zf);
	zy = m_FixedPoint.multiply(zy, zf);
	zz = m_FixedPoint.multiply(zz, zf);
	zo = m_FixedPoint.multiply(zo, zf);
    }
    public void scale(float xf, float yf, float zf) {
    	scale(m_FixedPoint.fixedValue(xf), m_FixedPoint.fixedValue(yf), m_FixedPoint.fixedValue(zf));
    }
    /** Translate the origin */
    public void translate(int xf, int yf, int zf) {
	xo += xf;
	yo += yf;
	zo += zf;
    }
    public void translate(float xf, float yf, float zf) {
    	translate(m_FixedPoint.fixedValue(xf), m_FixedPoint.fixedValue(yf), m_FixedPoint.fixedValue(zf));
    }
    /** rotate theta degrees about the y axis */
    public void yrot(int theta) {
	theta = m_FixedPoint.multiply(m_FixedPoint.fixedValue(theta), m_FixedPoint.DEG2RAD);
	int ct = m_FixedPoint.cos(theta);
	int st = m_FixedPoint.sin(theta);

	int Nxx =  (m_FixedPoint.multiply(xx, ct) + m_FixedPoint.multiply(zx, st));
	int Nxy =  (m_FixedPoint.multiply(xy, ct) + m_FixedPoint.multiply(zy, st));
	int Nxz =  (m_FixedPoint.multiply(xz, ct) + m_FixedPoint.multiply(zz, st));
	int Nxo =  (m_FixedPoint.multiply(xo, ct) + m_FixedPoint.multiply(zo, st));
	
	int Nzx =  (m_FixedPoint.multiply(zx, ct) - m_FixedPoint.multiply(xx, st));
	int Nzy =  (m_FixedPoint.multiply(zy, ct) - m_FixedPoint.multiply(xy, st));
	int Nzz =  (m_FixedPoint.multiply(zz, ct) - m_FixedPoint.multiply(xz, st));
	int Nzo =  (m_FixedPoint.multiply(zo, ct) - m_FixedPoint.multiply(xo, st));
	
	xo = Nxo;
	xx = Nxx;
	xy = Nxy;
	xz = Nxz;
	zo = Nzo;
	zx = Nzx;
	zy = Nzy;
	zz = Nzz;
    }
    /** rotate theta degrees about the x axis */
    public void xrot(int theta) {
	theta = m_FixedPoint.multiply(m_FixedPoint.fixedValue(theta), m_FixedPoint.DEG2RAD);
	int ct = m_FixedPoint.cos(theta);
	int st = m_FixedPoint.sin(theta);

	int Nyx = (int) (m_FixedPoint.multiply(yx, ct) + m_FixedPoint.multiply(zx, st));
	int Nyy = (int) (m_FixedPoint.multiply(yy, ct) + m_FixedPoint.multiply(zy, st));
	int Nyz = (int) (m_FixedPoint.multiply(yz, ct) + m_FixedPoint.multiply(zz, st));
	int Nyo = (int) (m_FixedPoint.multiply(yo, ct) + m_FixedPoint.multiply(zo, st));
	
	int Nzx = (int) (m_FixedPoint.multiply(zx, ct) - m_FixedPoint.multiply(yx, st));
	int Nzy = (int) (m_FixedPoint.multiply(zy, ct) - m_FixedPoint.multiply(yy, st));
	int Nzz = (int) (m_FixedPoint.multiply(zz, ct) - m_FixedPoint.multiply(yz, st));
	int Nzo = (int) (m_FixedPoint.multiply(zo, ct) - m_FixedPoint.multiply(yo, st));
	
	yo = Nyo;
	yx = Nyx;
	yy = Nyy;
	yz = Nyz;
	zo = Nzo;
	zx = Nzx;
	zy = Nzy;
	zz = Nzz;
    }
    /** rotate theta degrees about the z axis */
    public void zrot(int theta) {
	theta = m_FixedPoint.multiply(m_FixedPoint.fixedValue(theta), m_FixedPoint.DEG2RAD);
	int ct = m_FixedPoint.cos(theta);
	int st = m_FixedPoint.sin(theta);

	int Nyx = (int) (m_FixedPoint.multiply(yx, ct) + m_FixedPoint.multiply(xx, st));
	int Nyy = (int) (m_FixedPoint.multiply(yy, ct) + m_FixedPoint.multiply(xy, st));
	int Nyz = (int) (m_FixedPoint.multiply(yz, ct) + m_FixedPoint.multiply(xz, st));
	int Nyo = (int) (m_FixedPoint.multiply(yo, ct) + m_FixedPoint.multiply(xo, st));
	
	int Nxx = (int) (m_FixedPoint.multiply(xx, ct) - m_FixedPoint.multiply(yx, st));
	int Nxy = (int) (m_FixedPoint.multiply(xy, ct) - m_FixedPoint.multiply(yy, st));
	int Nxz = (int) (m_FixedPoint.multiply(xz, ct) - m_FixedPoint.multiply(yz, st));
	int Nxo = (int) (m_FixedPoint.multiply(xo, ct) - m_FixedPoint.multiply(yo, st));
	
	yo = Nyo;
	yx = Nyx;
	yy = Nyy;
	yz = Nyz;
	xo = Nxo;
	xx = Nxx;
	xy = Nxy;
	xz = Nxz;
    }
    public void xrot(float theta) {
    	xrot(m_FixedPoint.fixedValue(theta));
    }
    public void yrot(float theta) {
    	yrot(m_FixedPoint.fixedValue(theta));
    }
    public void zrot(float theta) {
    	zrot(m_FixedPoint.fixedValue(theta));
    }
    /** Multiply this matrix by a second: M = M*R */
    public void mult(m_MatrixFixedPoint rhs) {
	int lxx = m_FixedPoint.multiply(xx, rhs.xx) + m_FixedPoint.multiply(yx, rhs.xy) + m_FixedPoint.multiply(zx, rhs.xz);
	int lxy = m_FixedPoint.multiply(xy, rhs.xx) + m_FixedPoint.multiply(yy, rhs.xy) + m_FixedPoint.multiply(zy, rhs.xz);
	int lxz = m_FixedPoint.multiply(xz, rhs.xx) + m_FixedPoint.multiply(yz, rhs.xy) + m_FixedPoint.multiply(zz, rhs.xz);
	int lxo = m_FixedPoint.multiply(xo, rhs.xx) + m_FixedPoint.multiply(yo, rhs.xy) + m_FixedPoint.multiply(zo, rhs.xz) + rhs.xo;

	int lyx = m_FixedPoint.multiply(xx, rhs.yx) + m_FixedPoint.multiply(yx, rhs.yy) + m_FixedPoint.multiply(zx, rhs.yz);
	int lyy = m_FixedPoint.multiply(xy, rhs.yx) + m_FixedPoint.multiply(yy, rhs.yy) + m_FixedPoint.multiply(zy, rhs.yz);
	int lyz = m_FixedPoint.multiply(xz, rhs.yx) + m_FixedPoint.multiply(yz, rhs.yy) + m_FixedPoint.multiply(zz, rhs.yz);
	int lyo = m_FixedPoint.multiply(xo, rhs.yx) + m_FixedPoint.multiply(yo, rhs.yy) + m_FixedPoint.multiply(zo, rhs.yz) + rhs.yo;

	int lzx = m_FixedPoint.multiply(xx, rhs.zx) + m_FixedPoint.multiply(yx, rhs.zy) + m_FixedPoint.multiply(zx, rhs.zz);
	int lzy = m_FixedPoint.multiply(xy, rhs.zx) + m_FixedPoint.multiply(yy, rhs.zy) + m_FixedPoint.multiply(zy, rhs.zz);
	int lzz = m_FixedPoint.multiply(xz, rhs.zx) + m_FixedPoint.multiply(yz, rhs.zy) + m_FixedPoint.multiply(zz, rhs.zz);
	int lzo = m_FixedPoint.multiply(xo, rhs.zx) + m_FixedPoint.multiply(yo, rhs.zy) + m_FixedPoint.multiply(zo, rhs.zz) + rhs.zo;

	xx = lxx;
	xy = lxy;
	xz = lxz;
	xo = lxo;

	yx = lyx;
	yy = lyy;
	yz = lyz;
	yo = lyo;

	zx = lzx;
	zy = lzy;
	zz = lzz;
	zo = lzo;
    }

    /** Reinitialize to the unit matrix */
    public void unit() {
	xo = 0;
	xx = m_FixedPoint.ONE;
	xy = 0;
	xz = 0;
	yo = 0;
	yx = 0;
	yy = m_FixedPoint.ONE;
	yz = 0;
	zo = 0;
	zx = 0;
	zy = 0;
	zz = m_FixedPoint.ONE;
    }
    /** Transform n-vert points from v into tv.  v contains the input
        coordinates in inting point.  Three successive entries in
	the array constitute a point.  tv ends up holding the transformed
	points as integers; three successive entries per point */
    public void transform(int v[], int tv[], int nvert) {
	int lxx = xx, lxy = xy, lxz = xz, lxo = xo;
	int lyx = yx, lyy = yy, lyz = yz, lyo = yo;
	int lzx = zx, lzy = zy, lzz = zz, lzo = zo;
	for (int i = nvert * 3; (i -= 3) >= 0;) {
	    int x = v[i];
	    int y = v[i + 1];
	    int z = v[i + 2];
	    tv[i    ] = (int) (m_FixedPoint.multiply(x, lxx) + m_FixedPoint.multiply(y, lxy) + m_FixedPoint.multiply(z, lxz) + lxo);
	    tv[i + 1] = (int) (m_FixedPoint.multiply(x, lyx) + m_FixedPoint.multiply(y, lyy) + m_FixedPoint.multiply(z, lyz) + lyo);
	    tv[i + 2] = (int) (m_FixedPoint.multiply(x, lzx) + m_FixedPoint.multiply(y, lzy) + m_FixedPoint.multiply(z, lzz) + lzo);
	}
    }

    public String toString() {
	return ("[" + m_FixedPoint.toString(xx) + "\t" + m_FixedPoint.toString(xy) + "\t" + m_FixedPoint.toString(xz) + "\t" + m_FixedPoint.toString(xo) + "\n"
			+ m_FixedPoint.toString(yx) + "\t" + m_FixedPoint.toString(yy) + "\t" + m_FixedPoint.toString(yz) + "\t" + m_FixedPoint.toString(yo) + "\n"
			+ m_FixedPoint.toString(zx) + "\t" + m_FixedPoint.toString(zy) + "\t" + m_FixedPoint.toString(zz) + "\t" + m_FixedPoint.toString(zo)
			+ "]");
    }
    private static float temp[]=new float[12];
    public float[] floatValue() {
    	temp[0]=m_FixedPoint.floatValue(xx);
    	temp[1]=m_FixedPoint.floatValue(xy);
    	temp[2]=m_FixedPoint.floatValue(xz);
    	temp[3]=m_FixedPoint.floatValue(xo);
    	temp[4]=m_FixedPoint.floatValue(yx);
    	temp[5]=m_FixedPoint.floatValue(yy);
    	temp[6]=m_FixedPoint.floatValue(yz);
    	temp[7]=m_FixedPoint.floatValue(yo);
    	temp[8]=m_FixedPoint.floatValue(zx);
    	temp[9]=m_FixedPoint.floatValue(zy);
    	temp[10]=m_FixedPoint.floatValue(zz);
    	temp[11]=m_FixedPoint.floatValue(zo);
    	return temp;
    }
    private static int temp1[]=new int[12];
    public float[] arrayValue() {
    	temp1[0]=xx;
    	temp1[1]=xy;
    	temp1[2]=xz;
    	temp1[3]=xo;
    	temp1[4]=yx;
    	temp1[5]=yy;
    	temp1[6]=yz;
    	temp1[7]=yo;
    	temp1[8]=zx;
    	temp1[9]=zy;
    	temp1[10]=zz;
    	temp1[11]=zo;
    	return temp;
    }
    public static void main(String args[]) {
    	m_MatrixFixedPoint m=new m_MatrixFixedPoint();
    	System.out.println(m);
    }
}
