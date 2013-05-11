package com.software.reuze;

public final class ga_Geometry2D {

	private static final double 	ACCY 	= 1E-30;
	private static final double[] 	NONE 	= new double[0];

	public static final int ON_PLANE 		= 16;
	public static final int PLANE_INSIDE 	= 17;
	public static final int PLANE_OUTSIDE 	= 18;

	public static final int OUT_LEFT 		= 1;
	public static final int OUT_TOP 		= 2;
	public static final int OUT_RIGHT 		= 4;
	public static final int OUT_BOTTOM 		= 8;

	/**
	 * Sees if a line intersects with the circumference of a circle.
	 * 
	 * @param x0 
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param cx center of circle x position
	 * @param cy center of circle y position
	 * @param r radius of circle
	 * @return true if the line intersects the circle else false
	 */
	public static boolean line_circle(double x0, double y0, double x1, double y1, double cx, double cy, double r){
		double f = (x1 - x0);
		double g = (y1 - y0);
		double fSQ = f*f;
		double gSQ = g*g;
		double fgSQ = fSQ + gSQ;
		double rSQ = r*r;

		double xc0 = cx - x0;
		double yc0 = cy - y0;
		double xc1 = cx - x1;
		double yc1 = cy - y1;


		boolean lineInside = xc0*xc0 + yc0*yc0 < rSQ && xc1*xc1 + yc1*yc1 < rSQ;

		double fygx = f*yc0 - g*xc0;
		double root = r*r*fgSQ - fygx*fygx;

		if(root > ACCY && !lineInside){
			double fxgy = f*xc0 + g*yc0;
			double t = fxgy / fgSQ;
			if(t >= 0 && t <= 1)
				return true;
			// Circle intersects with one end then return true
			if( (xc0*xc0 + yc0*yc0 < rSQ) || (xc1*xc1 + yc1*yc1 < rSQ) )
				return true;
		}
		return false;
	}

	/**
	 * Calculate the points of intersection between a line and a circle. <br>
	 * An array is returned that contains the intersection points in x, y order.
	 * If the array is of length: <br>
	 * 0 then there is no intersection <br>
	 * 2 there is just one intersection (the line is a tangent to the circle) <br>
	 * 4 there are two intersections <br>
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param cx centre of circle x position
	 * @param cy centre of circle y position
	 * @param r radius of circle
	 * @return the intersection points as an array (2 elements per intersection)
	 */
	public static double[] line_circle_p(double x0, double y0, double x1, double y1, double cx, double cy, double r){
		double[] result = NONE;
		double f = (x1 - x0);
		double g = (y1 - y0);
		double fSQ = f*f;
		double gSQ = g*g;
		double fgSQ = fSQ + gSQ;

		double xc0 = cx - x0;
		double yc0 = cy - y0;

		double fygx = f*yc0 - g*xc0;
		double root = r*r*fgSQ - fygx*fygx;
		if(root > -ACCY){
			double[] temp = null;
			int np = 0;
			double fxgy = f*xc0 + g*yc0;
			if(root < ACCY){		// tangent so just one point
				double t = fxgy / fgSQ;
				if(t >= 0 && t <= 1)
					temp = new double[] { x0 + f*t, y0 + g*t};
				np = 2;
			}
			else {	// possibly two intersections
				temp = new double[4];
				root = (double) Math.sqrt(root);
				double t = (fxgy - root)/fgSQ;
				if(t >= 0 && t <= 1){
					temp[np++] = x0 + f*t;
					temp[np++] = y0 + g*t;
				}
				t = (fxgy + root)/fgSQ;
				if(t >= 0 && t <= 1){
					temp[np++] = x0 + f*t;
					temp[np++] = y0 + g*t;
				}
			}
			if(temp != null){
				result = new double[np];
				System.arraycopy(temp, 0, result, 0, np);
			}
		}
		return result;
	}

	/**
	 * See if two lines intersect
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return
	 */
	public static boolean line_line(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3){
		double f1 = (x1 - x0);
		double g1 = (y1 - y0);
		double f2 = (x3 - x2);
		double g2 = (y3 - y2);
		double f1g2 = f1 * g2;
		double f2g1 = f2 * g1;
		double det = f2g1 - f1g2;
		if(Math.abs(det) > ACCY){
			double s = (f2*(y2 - y0) - g2*(x2 - x0))/ det;
			double t = (f1*(y2 - y0) - g1*(x2 - x0))/ det;
			return (s >= 0 && s <= 1 && t >= 0 && t <= 1);
		}
		return false;
	}

	/**
	 * Find the point of intersection between two lines. <br>
	 * This method uses Vector2D objects to represent the line end points.
	 * @param v0
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return a Vector2D object holding the intersection coordinates else null if no intersection 
	 */
	public static ga_Vector2D line_line_p(ga_Vector2D v0, ga_Vector2D v1, ga_Vector2D v2, ga_Vector2D v3){
		ga_Vector2D intercept = null;

		double f1 = (v1.x - v0.x);
		double g1 = (v1.y - v0.y);
		double f2 = (v3.x - v2.x);
		double g2 = (v3.y - v2.y);
		double f1g2 = f1 * g2;
		double f2g1 = f2 * g1;
		double det = f2g1 - f1g2;

		if(Math.abs(det) > ACCY){
			double s = (f2*(v2.y - v0.y) - g2*(v2.x - v0.x))/ det;
			double t = (f1*(v2.y - v0.y) - g1*(v2.x - v0.x))/ det;
			if(s >= 0 && s <= 1 && t >= 0 && t <= 1)
				intercept = new  ga_Vector2D(v0.x + f1 * s, v0.y + g1 * s);
		}
		return intercept;
	}

	/**
	 * Find the point of intersection between two lines. <br>
	 * An array is returned that contains the intersection points in x, y order.
	 * If the array is of length: <br>
	 * 0 then there is no intersection <br>
	 * 2 these are the x/y coordinates of the intersection point. <br>
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return
	 */
	public static double[] line_line_p(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3){
		double[] result = NONE;
		double f1 = (x1 - x0);
		double g1 = (y1 - y0);
		double f2 = (x3 - x2);
		double g2 = (y3 - y2);
		double f1g2 = f1 * g2;
		double f2g1 = f2 * g1;
		double det = f2g1 - f1g2;

		if(Math.abs(det) > ACCY){
			double s = (f2*(y2 - y0) - g2*(x2 - x0))/ det;
			double t = (f1*(y2 - y0) - g1*(x2 - x0))/ det;
			if(s >= 0 && s <= 1 && t >= 0 && t <= 1)
				result = new double[] { x0 + f1 * s, y0 + g1 * s };
		}
		return result;
	}

	/**
	 * Calculate the intersection points between a line and a collection of lines. <br>
	 * This will calculate all the intersection points between a given line
	 * and the lines formed from the points in the array xy. <br>
	 * If the parameter continuous = true the points form a continuous line so the <br>
	 * <pre>
	 * line 1 is from xy[0],xy[1] to xy[2],xy[3] and
	 * line 2 is from xy[2],xy[3] to xy[4],xy[5] and so on
	 * </pre>
	 * and if continuous is false then each set of four array elements form their 
	 * own line <br>
	 * <pre>
	 * line 1 is from xy[0],xy[1] to xy[2],xy[3] and
	 * line 2 is from xy[4],xy[5] to xy[6],xy[7] and so on
	 * </pre>
	 * 
	 * @param x0 x position of the line start
	 * @param y0 y position of the line start
	 * @param x1 x position of the line end
	 * @param y1 y position of the line end
	 * @param xy array of x/y coordinates
	 * @param continuous if true the points makes a continuous line
	 * @return an array with all the intersection coordinates
	 */
	public static double[] line_lines_p(double x0, double y0, double x1, double y1, double[] xy, boolean continuous){
		double[] result = NONE;
		int stride = continuous ? 2 : 4;
		int np = 0;
		double[] temp = new double[xy.length];
		double f2, g2, f1g2, f2g1, det;
		double f1 = (x1 - x0);
		double g1 = (y1 - y0);
		for(int i = 0; i < xy.length - stride; i += stride){
			f2 = (xy[i+2] - xy[i]);
			g2 = (xy[i+3] - xy[i+1]);
			f1g2 = f1 * g2;
			f2g1 = f2 * g1;
			det = f2g1 - f1g2;
			if(Math.abs(det) > ACCY){
				double s = (f2*(xy[i+1] - y0) - g2*(xy[i] - x0))/ det;
				double t = (f1*(xy[i+1] - y0) - g1*(xy[i] - x0))/ det;
				if(s >= 0 && s <= 1 && t >= 0 && t <= 1){
					temp[np++] = x0 + f1 * s;
					temp[np++] = y0 + g1 * s;
				}
			}
		}
		if(np > 0){
			result = new double[np];
			System.arraycopy(temp, 0, result, 0, np);
		}
		return result;
	}

	/**
	 * Determine if two circles overlap
	 * @param cx centre of first circle x position
	 * @param cy centre of first circle y position
	 * @param r radius of first circle
	 * @param cx centre of second circle x position
	 * @param cy centre of second circle y position
	 * @param r radius of second circle
	 * @return true if the circles overlap else false
	 */
	public static boolean circle_circle(double cx0, double cy0, double r0, double cx1, double cy1, double r1){
		double dxSQ = (cx1 - cx0)*(cx1 - cx0);
		double dySQ = (cy1 - cy0)*(cy1 - cy0);
		double rSQ = (r0 + r1)*(r0 + r1);
		double drSQ = (r0 - r1)*(r0 - r1);
		return (dxSQ + dySQ <= rSQ && dxSQ + dySQ >= drSQ);
	}

	/**
	 * Calculate the intersection points between two circles. <br>
	 * If the array is of length: <br>
	 * 0 then there is no intersection <br>
	 * 2 there is just one intersection (the circles are touching) <br>
	 * 4 there are two intersections <br>
	 * 
	 * @param cx centre of first circle x position
	 * @param cy centre of first circle y position
	 * @param r radius of first circle
	 * @param cx centre of second circle x position
	 * @param cy centre of second circle y position
	 * @param r radius of second circle
	 * @return an array with the intersection points
	 */
	public static double[] circle_circle_p(double cx0, double cy0, double r0, double cx1, double cy1, double r1){
		double[] result = NONE;
		double dx = cx1 - cx0;
		double dy = cy1 - cy0;
		double distSQ = dx*dx + dy*dy;

		if(distSQ > ACCY){
			double r0SQ = r0 * r0;
			double r1SQ = r1 * r1;
			double diffRSQ = (r1SQ - r0SQ);
			double root = 2 * (r1SQ + r0SQ) * distSQ - distSQ * distSQ - diffRSQ * diffRSQ;
			if(root > -ACCY){
				double distINV = 0.5f/ distSQ;
				double scl = 0.5f - diffRSQ * distINV;
				double x = dx * scl + cx0;
				double y = dy * scl + cy0;
				if(root < ACCY){
					result = new double[] { x, y };
				}
				else {
					root = (double) (distINV * Math.sqrt(root));
					double xfac = dx * root;
					double yfac = dy * root;
					result = new double[] { x-yfac, y+xfac,  x+yfac, y-xfac };
				}
			}
		}
		return result;
	}

	/**
	 * Calculate the tangents from a point. <br>
	 * If the array is of length: <br>
	 * 0 then there is no tangent the point is inside the circle <br>
	 * 2 there is just one intersection (the point is on the circumference) <br>
	 * 4  there are two points.
	 * 
	 * @param x
	 * @param y
	 * @param cx centre of circle x position
	 * @param cy centre of circle y position
	 * @param r radius of circle
	 * @return
	 */
	public static double[] tangents_to_circle(double x, double y, double cx, double cy, double r){
		double[] result = NONE;
		double dx = cx - x;
		double dy = cy - y;
		double dxSQ = dx * dx;
		double dySQ = dy * dy;
		double denom = dxSQ + dySQ;

		double root = denom - r*r;

		if(root > -ACCY){
			double denomINV = 1.0f/denom;
			double A, B;

			if(root < ACCY){ // point is on circle
				A = -r*dx*denomINV;
				B = -r*dy*denomINV;
				result = new double[] { cx + A*r, cy + B*r };
			}
			else {
				root = (double) Math.sqrt(root);
				result = new double[4];
				A = (-dy*root - r*dx)*denomINV;
				B = (dx*root - r*dy)*denomINV;
				result[0] = cx +A*r;
				result[1] = cy + B*r;
				A = (dy*root - r*dx)*denomINV;
				B = (-dx*root - r*dy)*denomINV;
				result[2] = cx +A*r;
				result[3] = cy + B*r;	
			}
		}
		return result;
	}



	/**
	 * Will calculate the contact points for both outer and inner tangents. <br>
	 * There are no tangents if one circle is completely inside the other.
	 * If the circles interact only the outer tangents exist. When the circles
	 * do not intersect there will be 4 tangents (outer and inner), the array
	 * has the outer pair first.
	 * 
	 * @param cx0 x position for the first circle
	 * @param cy0 y position for the first circle
	 * @param r0 radius of the first circle
	 * @param cx1 x position for the second circle
	 * @param cy1 y position for the second circle
	 * @param r1 radius of the second circle
	 * @return
	 */
	public static double[] tangents_between_circles(double cx0, double cy0, double r0, double cx1, double cy1, double r1) {
		double[] result = NONE;
		double dxySQ = (cx0 - cx1) * (cx0 - cx1) + (cy0 - cy1) * (cy0 - cy1);

		if (dxySQ <= (r0-r1)*(r0-r1)) return result;

		double d = (double) Math.sqrt(dxySQ);
		double vx = (cx1 - cx0) / d;
		double vy = (cy1 - cy0) / d;

		double[] temp = new double[16];
		int np = 0;
		double c, h, nx, ny;
		for (int sign1 = +1; sign1 >= -1; sign1 -= 2) {
			c = (r0 - sign1 * r1) / d;
			if (c*c > 1) continue;

			h = (double) Math.sqrt(Math.max(0.0, 1.0 - c*c));

			for (int sign2 = +1; sign2 >= -1; sign2 -= 2) {
				nx = vx * c - sign2 * h * vy;
				ny = vy * c + sign2 * h * vx;

				temp[np++] = cx0 + r0 * nx;
				temp[np++] = cy0 + r0 * ny;
				temp[np++] = cx1 + sign1 * r1 * nx;
				temp[np++] = cy1 + sign1 * r1 * ny;
			}
		}
		if(np > 0){
			result = new double[np];
			System.arraycopy(temp, 0, result, 0, np);
		}

		return result;
	}

	/**
	 * Outside is in the same direction of the plane normal. <br>
	 * The first four parameters represent the start and end position
	 * for a line segment (finite plane).  
	 * 
	 * @param x0 x start of the line
	 * @param y0 y start of the line
	 * @param x1 x end of the line
	 * @param y1 y end of the line
	 * @param px x position of the point to test
	 * @param py y position of the point to test
	 * @return
	 */
	public static int which_side_pp(double x0, double y0, double x1, double y1, double px, double py){
		int side;
		double dot = (y0 - y1)*(px - x0) + (x1 - x0)*(py - y0);

		if(dot < -ACCY)
			side = PLANE_INSIDE;
		else if(dot > ACCY)
			side = PLANE_OUTSIDE;
		else 
			side = ON_PLANE;
		return side;
	}

	/**
	 * Outside is in the same direction of the plane normal. <br>
	 * This version requires a single point on the plane and the normal 
	 * direction. Useful for an infinite plane or for testing many
	 * points against a single plane when the plane normal does not have
	 * to be calculated each time.
	 * 
	 * @param x0 x position point of a point on the plane
	 * @param y0 y position point of a point on the plane
	 * @param nx x value of normal vector
	 * @param ny y value of normal vector
	 * @param px x position of the point to test
	 * @param py y position of the point to test
	 * @return
	 */
	public static int which_side_pn(double x0, double y0, double nx, double ny, double px, double py){
		int side;
		double dot = nx*(px - x0) + ny*(py - y0);

		if(dot < -ACCY)
			side = PLANE_INSIDE;
		else if(dot > ACCY)
			side = PLANE_OUTSIDE;
		else 
			side = ON_PLANE;
		return side;
	}

	/**
	 * Code copied from {@link java.awt.geom.Rectangle2D#intersectsLine(double, double, double, double)}
	 */
	private static int outcode(double pX, double pY, double rectX, double rectY, double rectWidth, double rectHeight) {
		int out = 0;
		if (rectWidth <= 0) {
			out |= OUT_LEFT | OUT_RIGHT;
		} else if (pX < rectX) {
			out |= OUT_LEFT;
		} else if (pX > rectX + rectWidth) {
			out |= OUT_RIGHT;
		}
		if (rectHeight <= 0) {
			out |= OUT_TOP | OUT_BOTTOM;
		} else if (pY < rectY) {
			out |= OUT_TOP;
		} else if (pY > rectY + rectHeight) {
			out |= OUT_BOTTOM;
		}
		return out;
	}

	/**
	 * Determine whether a line intersects with a box. <br>
	 * The box is represented by the top-left and bottom-right corner coordinates. 
	 * @param lx0  line
	 * @param ly0
	 * @param lx1
	 * @param ly1
	 * @param rx0  box
	 * @param ry0
	 * @param rx1
	 * @param ry1
	 * @return true if they intersect else false
	 */
	public static boolean line_box_xyxy(double lx0, double ly0, double lx1, double ly1, double rx0, double ry0, double rx1, double ry1) {
		int out1, out2;
		double rectWidth = rx1 - rx0;
		double rectHeight = ry1 - ry0;
		
		if ((out2 = outcode(lx1, ly1, rx0, ry0, rectWidth, rectHeight)) == 0) {
			return true;
		}
		while ((out1 = outcode(lx0, ly0, rx0, ry0, rectWidth, rectHeight)) != 0) {
			if ((out1 & out2) != 0) {
				return false;
			}
			if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
				double x = rx0;
				if ((out1 & OUT_RIGHT) != 0) {
					x += rectWidth;
				}
				ly0 = ly0 + (x - lx0) * (ly1 - ly0) / (lx1 - lx0);
				lx0 = x;
			} else {
				double y = ry0;
				if ((out1 & OUT_BOTTOM) != 0) {
					y += rectHeight;
				}
				lx0 = lx0 + (y - ly0) * (lx1 - lx0) / (ly1 - ly0);
				ly0 = y;
			}
		}
		return true;
	}

	/**
	 * Determine whether a line intersects with a box. <br>
	 * The box is represented by the top-left corner coordinates and the box width and height. 
	 * @param lx0
	 * @param ly0
	 * @param lx1
	 * @param ly1
	 * @param rx0
	 * @param ry0
	 * @param rWidth
	 * @param rHeight
	 * @return true if they intersect else false
	 */
	public static boolean line_box_xywh(double lx0, double ly0, double lx1, double ly1, double rx0, double ry0, double rWidth, double rHeight) {
		int out1, out2;
		if ((out2 = outcode(lx1, ly1, rx0, ry0, rWidth, rHeight)) == 0) {
			return true;
		}
		while ((out1 = outcode(lx0, ly0, rx0, ry0, rWidth, rHeight)) != 0) {
			if ((out1 & out2) != 0) {
				return false;
			}
			if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
				double x = rx0;
				if ((out1 & OUT_RIGHT) != 0) {
					x += rWidth;
				}
				ly0 = ly0 + (x - lx0) * (ly1 - ly0) / (lx1 - lx0);
				lx0 = x;
			} else {
				double y = ry0;
				if ((out1 & OUT_BOTTOM) != 0) {
					y += rHeight;
				}
				lx0 = lx0 + (y - ly0) * (lx1 - lx0) / (ly1 - ly0);
				ly0 = y;
			}
		}
		return true;
	}

	/**
	 * Determine whether two boxes intersect. <br>
	 * The boxes are represented by the top-left and bottom-right corner coordinates. 
	 * 
	 * @param ax0
	 * @param ay0
	 * @param ax1
	 * @param ay1
	 * @param bx0
	 * @param by0
	 * @param bx1
	 * @param by1
	 * @return
	 */
	public static boolean box_box(double ax0, double ay0, double ax1, double ay1, double bx0, double by0, double bx1, double by1){
		double topA = m_MathFast.min(ay0, ay1);
		double botA = m_MathFast.max(ay0, ay1);
		double leftA = m_MathFast.min(ax0, ax1);
		double rightA = m_MathFast.max(ax0, ax1);
		double topB = m_MathFast.min(by0, by1);
		double botB = m_MathFast.max(by0, by1);
		double leftB = m_MathFast.min(bx0, bx1);
		double rightB = m_MathFast.max(bx0, bx1);

		if(botA <= topB  || botB <= topA || rightA <= leftB || rightB <= leftA)
			return false;

		return true;		
	}
	
	/**
	 * If two boxes overlap then the overlap region is another box. This method is used to 
	 * calculate the coordinates of the overlap. <br>
	 * The boxes are represented by the top-left and bottom-right corner coordinates. 
	 * If the returned array has a length:
	 * 0 then they do not overlap <br>
	 * 4 then these are the coordinates of the top-left and bottom-right corners of the overlap region.
	 *  
	 * @param ax0
	 * @param ay0
	 * @param ax1
	 * @param ay1
	 * @param bx0
	 * @param by0
	 * @param bx1
	 * @param by1
	 * @return an array with the overlap box coordinates (if any)
	 */
	public static double[] box_box_p(double ax0, double ay0, double ax1, double ay1, double bx0, double by0, double bx1, double by1){
		double[] result = NONE;
		double topA = m_MathFast.min(ay0, ay1);
		double botA = m_MathFast.max(ay0, ay1);
		double leftA = m_MathFast.min(ax0, ax1);
		double rightA = m_MathFast.max(ax0, ax1);
		double topB = m_MathFast.min(by0, by1);
		double botB = m_MathFast.max(by0, by1);
		double leftB = m_MathFast.min(bx0, bx1);
		double rightB = m_MathFast.max(bx0, bx1);
		
		if(botA <= topB  || botB <= topA || rightA <= leftB || rightB <= leftA)
			return result;

		double leftO = (leftA < leftB) ? leftB : leftA;
		double rightO = (rightA > rightB) ? rightB : rightA;
		double botO = (botA > botB) ? botB : botA;
		double topO = (topA < topB) ? topB : topA;
		result =  new double[] {leftO, topO, rightO, botO};
		return result;
	}
	
	/**
	 * Determine if the point pX/pY is inside triangle defined by triangle ABC
	 * @param aX
	 * @param aY
	 * @param bX
	 * @param bY
	 * @param cX
	 * @param cY
	 * @param pX
	 * @param pY
	 * @return
	 */
	public static boolean isInsideTriangle(double aX, double aY,
			double bX, double bY,
			double cX, double cY,
			double pX, double pY){
		boolean b1, b2, b3;
		  b1 = sign(pX,pY, aX,aY, bX,bY) < 0.0;
		  b2 = sign(pX,pY, bX,bY, cX,cY) < 0.0;
		  if (b1!=b2) return false;
		  b3 = sign(pX,pY, cX,cY, aX,aY) < 0.0;
		  return (b2 == b3);
	}
	public static double sign(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y)
	{
	  return (p1x - p3x) * (p2y - p3y) - (p2x - p3x) * (p1y - p3y);
	}
	public static double sign(ga_Vector2D p1, ga_Vector2D p2, ga_Vector2D p3)
	{
	  return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}

	/**
	 * Determine if the point (p) is inside triangle defined by triangle ABC
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param p
	 * @return
	 */
	public static boolean isInsideTriangle(ga_Vector2D a, ga_Vector2D b, ga_Vector2D c, ga_Vector2D p){
		boolean b1, b2, b3;
		  b1 = sign(p, a, b) < 0.0;
		  b2 = sign(p, b, c) < 0.0;
		  if (b1!=b2) return false;
		  b3 = sign(p, c, a) < 0.0;
		  return (b2 == b3);
	}
	
	/**
	 * Determine if the point pX/pY is inside triangle defined by triangle ABC
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param pX
	 * @param pY
	 * @return
	 */
	public static boolean isInsideTriangle(ga_Vector2D a, ga_Vector2D b, ga_Vector2D c, double pX, double pY){
		return isInsideTriangle(a.x, a.y, b.x, b.y, c.x, c.y, pX, pY);
	}
	
	
	
	public static boolean isInsideRectangle_xyxy(double x0, double y0, double x1, double y1,
			double pX, double pY){
		return (pX >= x0 && pY >= y0 && pX <= x1 && pY <= y1);
	}

	public static boolean isInsideRectangle_xyxy(ga_Vector2D v0, ga_Vector2D v1, ga_Vector2D p){
		return isInsideRectangle_xyxy(v0.x, v0.y, v1.x, v1.y, p.x, p.y);
	}

	public static boolean isInsideRectangle_xywh(double x0, double y0, double width, double height,
			double pX, double pY){
		return (pX >= x0 && pY >= y0 && pX <= x0 + width && pY <= y0 + height);
	}
	
	public static boolean isInsideRectangle_xywh(ga_Vector2D v0, double width, double height, ga_Vector2D p){
		return isInsideRectangle_xyxy(v0.x, v0.y, v0.x + width, v0.y + height, p.x, p.y);
	}


	public static double distance_sq(double x0, double y0, double x1, double y1){
		return (x1-x0)*(x1-x0) + (y1-y0)*(y1-y0);
	}
	
	public static double distance(double x0, double y0, double x1, double y1){
		return m_MathFast.sqrt((x1-x0)*(x1-x0) + (y1-y0)*(y1-y0));
	}
	
}

