package com.software.reuze;
import com.software.reuze.z_Arc2DFloat;
import com.software.reuze.z_GeneralPath;
import java.util.StringTokenizer;


//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class vg_PathProperties implements da_i_Validate {
	public vg_StyleProperties draw;
	public String id, transform, style, d;
	private static final vg_StyleProperties defaults=new vg_StyleProperties("black",null,1);
	public z_GeneralPath points;
	//	@XmlAttribute
	public void setD(String points) {
		this.d = points;
	}
	public String getD() {
		return d;
	}
	public vg_StyleProperties getDraw() {
		return draw;
	}
	public void setDraw(vg_StyleProperties draw) {
		this.draw = draw;
	}
	public String getId() {
		return id;
	}
	//	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}
	public String getTransform() {
		return transform;
	}
	//	@XmlAttribute
	public void setTransform(String transform) {
		this.transform = transform;
	}
	public String getStyle() {
		return style;
	}
	//	@XmlAttribute
	public void setStyle(String style) {
		this.style = style;
	}
	private static char getPathFloat(StringTokenizer t, d_RefFloat f) {
		if (!t.hasMoreElements()) return '!';
		String tempBuffer = t.nextToken();
		if (tempBuffer.equals("-")) {
			f.set( -Float.valueOf(t.nextToken()) );
			return ' ';
		}
		while (tempBuffer.equals(",") || tempBuffer.equals(" ") || tempBuffer.equals("\n")){
			tempBuffer = t.nextToken();
		}
		if (tempBuffer.equals("-")) {
			f.set( -Float.valueOf(t.nextToken()) );
			return ' ';
		}
		if (Character.isLetter(tempBuffer.charAt(0))) return tempBuffer.charAt(0);
		f.set( Float.valueOf(tempBuffer) );
		return ' ';
	}
	public static z_GeneralPath checkPoints(String points) {
		StringTokenizer t = new StringTokenizer(points," ,MmLlCczZAarSsHhVvDdEeFfGgJjQqTt-",true);
		z_GeneralPath ids=new z_GeneralPath();
		float fx=0, fy=0, startx=Float.NaN, starty=0, oldfx=0, oldfy=0, x, y, x2=0, y2=0;
		d_RefFloat temp=new d_RefFloat();
		while (t.hasMoreElements()) {
			points = t.nextToken();
			char c=points.charAt(0);
			for (;;) {
				switch (c) {
				case 'M': //Move To (x y)+
				for (;;) {
					c = getPathFloat(t, temp);
					if (c!=' ') break;
					oldfx=fx=temp.get();
					getPathFloat(t, temp);
					oldfy=fy=temp.get();
					ids.moveTo(fx, fy);
					if (startx==Float.NaN) {startx=fx; starty=fy;}
				}
				continue;
				case 'm':
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						oldfx=fx=temp.get()+oldfx;
						getPathFloat(t, temp);
						oldfy=fy=temp.get()+oldfy;
						ids.moveTo(fx, fy);
						if (startx==Float.NaN) {startx=fx; starty=fy;}
					}
					continue;
				case 'L': // Line to (x y)+
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						oldfx=fx=temp.get();
						getPathFloat(t, temp);
						oldfy=fy=temp.get();
						ids.lineTo(fx, fy);
					}
					continue;
				case 'l':
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						oldfx=fx=temp.get()+oldfx;
						getPathFloat(t, temp);
						oldfy=fy=temp.get()+oldfy;
						ids.lineTo(fx, fy);
					}
					continue;
				case 'a': //arc to (rx ry xrot largearc sweep x y)+
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						x2=temp.get();
						getPathFloat(t, temp);
						y2=temp.get();
						getPathFloat(t, temp);
						float angle=temp.get();
						getPathFloat(t, temp);
						boolean largeArc=temp.get()!=0;
						getPathFloat(t, temp);
						boolean sweep=temp.get()!=0;
						getPathFloat(t, temp);
						oldfx=fx=temp.get()+oldfx;
						getPathFloat(t, temp);
						oldfy=fy=temp.get()+oldfy;
						arcTo(ids, x2, y2, angle, largeArc, sweep, fx, fy);
					}
					continue;
				case 'C': //cubic bezier Curve to (x1 y1 x2 y2 x y)+
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						fx=temp.get();
						getPathFloat(t, temp);
						fy=temp.get();
						getPathFloat(t, temp);
						x2=temp.get();
						getPathFloat(t, temp);
						y2=temp.get();
						getPathFloat(t, temp);
						x=temp.get();
						getPathFloat(t, temp);
						y=temp.get();
						ids.curveTo(fx, fy, x2, y2, x, y);
						oldfx=x;  oldfy=y;
					}
					continue;
				case 'c':
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						fx=temp.get()+oldfx;
						getPathFloat(t, temp);
						fy=temp.get()+oldfy;
						getPathFloat(t, temp);
						x2=temp.get()+oldfx;
						getPathFloat(t, temp);
						y2=temp.get()+oldfy;
						getPathFloat(t, temp);
						x=temp.get()+oldfx;
						getPathFloat(t, temp);
						y=temp.get()+oldfy;
						ids.curveTo(fx, fy, x2, y2, x, y);
						oldfx=x;  oldfy=y;
					}
					continue;
				case 'S': //cubic bezier shorthand smooth Curve to (x2 y2 x y)+
					for (;;) { /*TODO doesn't implement case where it's not preceded by C/S*/
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						x2=oldfx+oldfx-x2;  y2=oldfy+oldfy-y2;
						fx=temp.get();
						getPathFloat(t, temp);
						fy=temp.get();
						getPathFloat(t, temp);
						x=temp.get();
						getPathFloat(t, temp);
						y=temp.get();
						ids.curveTo(x2, y2, fx, fy, x, y);
						x2=fx; y2=fy; oldfx=x; oldfy=y;
					}
					continue;
				case 's':
					for (;;) { /*TODO doesn't implement case where it's not preceded by C/S*/
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						x2=oldfx+oldfx-x2;  y2=oldfy+oldfy-y2;
						fx=temp.get()+oldfx;
						getPathFloat(t, temp);
						fy=temp.get()+oldfy;
						getPathFloat(t, temp);
						x=temp.get()+oldfx;
						getPathFloat(t, temp);
						y=temp.get()+oldfy;
						ids.curveTo(x2, y2, fx, fy, x, y);
						x2=fx; y2=fy; oldfx=x; oldfy=y;
					}
					continue;
				case 'Q': //quadratic bezier Curve to (x1 y1 x y)+
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						fx=temp.get();
						getPathFloat(t, temp);
						fy=temp.get();
						getPathFloat(t, temp);
						x=temp.get();
						getPathFloat(t, temp);
						y=temp.get();
						ids.quadTo(fx, fy, x, y);
						oldfx=x;  oldfy=y;
					}
					continue;
				case 'q':
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						fx=temp.get()+oldfx;
						getPathFloat(t, temp);
						fy=temp.get()+oldfy;
						getPathFloat(t, temp);
						x=temp.get()+oldfx;
						getPathFloat(t, temp);
						y=temp.get()+oldfy;
						ids.quadTo(fx, fy, x, y);
						oldfx=x;  oldfy=y;
					}
					continue;
				case 'T': //quadratic bezier shorthand smooth Curve to (x y)+
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						fx=oldfx+oldfx-fx;  fy=oldfy+oldfy-fy;
						x=temp.get();
						getPathFloat(t, temp);
						y=temp.get();
						ids.quadTo(fx, fy, x, y);
						oldfx=x;  oldfy=y;
					}
					continue;
				case 't':
					for (;;) {
						c = getPathFloat(t, temp);
						if (c!=' ') break;
						fx=oldfx+oldfx-fx;  fy=oldfy+oldfy-fy;
						x=temp.get()+oldfx;
						getPathFloat(t, temp);
						y=temp.get()+oldfy;
						ids.quadTo(fx, fy, x, y);
						oldfx=x;  oldfy=y;
					}
					continue;
				case 'Z': //close path back to the origin
				case 'z':
					ids.closePath();
					oldfx=startx; oldfy=starty;
					break;
				case 'H': //absolute horizontal Line to (x)
					getPathFloat(t, temp);
					oldfx=fx=temp.get();
					ids.lineTo(fx, oldfy);
					break;
				case 'h': //relative horizontal Line to (x)
					getPathFloat(t, temp);
					oldfx=fx=temp.get()+oldfx;
					ids.lineTo(fx, oldfy);
					break;
				case 'V': //absolute vertical Line to (y)
					getPathFloat(t, temp);
					oldfy=fy=temp.get();
					ids.lineTo(oldfx, fy);
					break;
				case 'v': //relative vertical Line to (y)
					fy = getPathFloat(t, temp);
					oldfy=fy=temp.get()+oldfy;
					ids.lineTo(oldfx, fy);
					break;
				case '-':
					System.out.println("Negative value");
					break;
				case '0':
				case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
					System.out.println("Misplaced digits");
					break;
				} //switch
				break;
			} //for
		}
		return ids;
	}
	public boolean isValid(int extra) {
		if (style!=null) {
			draw=draw.create(style);
			style=null;
			if (!draw.isValid(0)) return false;
		} else if (draw==null) {
			draw=defaults;
		}
		if (points==null) points=checkPoints(d);
		return points!=null;
	}
	public static final void arcTo(z_GeneralPath path, float rx, float ry, float theta, boolean largeArcFlag, boolean sweepFlag, float x, float y) {
		// Ensure radii are valid
		if (rx == 0 || ry == 0) {
			path.lineTo(x, y);
			return;
		}
		// Get the current (x, y) coordinates of the path
		z_Point2D p2d = path.getCurrentPoint(null);
		float x0 = (float) p2d.x;
		float y0 = (float) p2d.y;
		// Compute the half distance between the current and the final point
		float dx2 = (x0 - x) / 2.0f;
		float dy2 = (y0 - y) / 2.0f;
		// Convert theta from degrees to radians
		theta = (float) Math.toRadians(theta % 360f);

		//
		// Step 1 : Compute (x1, y1)
		//
		float x1 = (float) (Math.cos(theta) * (double) dx2 + Math.sin(theta)
				* (double) dy2);
		float y1 = (float) (-Math.sin(theta) * (double) dx2 + Math.cos(theta)
				* (double) dy2);
		// Ensure radii are large enough
		rx = Math.abs(rx);
		ry = Math.abs(ry);
		float Prx = rx * rx;
		float Pry = ry * ry;
		float Px1 = x1 * x1;
		float Py1 = y1 * y1;
		double d = Px1 / Prx + Py1 / Pry;
		if (d > 1) {
			rx = Math.abs((float) (Math.sqrt(d) * (double) rx));
			ry = Math.abs((float) (Math.sqrt(d) * (double) ry));
			Prx = rx * rx;
			Pry = ry * ry;
		}

		//
		// Step 2 : Compute (cx1, cy1)
		//
		double sign = (largeArcFlag == sweepFlag) ? -1d : 1d;
		float coef = (float) (sign * Math
				.sqrt(((Prx * Pry) - (Prx * Py1) - (Pry * Px1))
						/ ((Prx * Py1) + (Pry * Px1))));
		float cx1 = coef * ((rx * y1) / ry);
		float cy1 = coef * -((ry * x1) / rx);

		//
		// Step 3 : Compute (cx, cy) from (cx1, cy1)
		//
		float sx2 = (x0 + x) / 2.0f;
		float sy2 = (y0 + y) / 2.0f;
		float cx = sx2
		+ (float) (Math.cos(theta) * (double) cx1 - Math.sin(theta)
				* (double) cy1);
		float cy = sy2
		+ (float) (Math.sin(theta) * (double) cx1 + Math.cos(theta)
				* (double) cy1);

		//
		// Step 4 : Compute the angleStart (theta1) and the angleExtent (dtheta)
		//
		float ux = (x1 - cx1) / rx;
		float uy = (y1 - cy1) / ry;
		float vx = (-x1 - cx1) / rx;
		float vy = (-y1 - cy1) / ry;
		float p, n;
		// Compute the angle start
		n = (float) Math.sqrt((ux * ux) + (uy * uy));
		p = ux; // (1 * ux) + (0 * uy)
		sign = (uy < 0) ? -1d : 1d;
		float angleStart = (float) Math.toDegrees(sign * Math.acos(p / n));
		// Compute the angle extent
		n = (float) Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
		p = ux * vx + uy * vy;
		sign = (ux * vy - uy * vx < 0) ? -1d : 1d;
		float angleExtent = (float) Math.toDegrees(sign * Math.acos(p / n));
		if (!sweepFlag && angleExtent > 0) {
			angleExtent -= 360f;
		} else if (sweepFlag && angleExtent < 0) {
			angleExtent += 360f;
		}
		angleExtent %= 360f;
		angleStart %= 360f;

		z_Arc2DFloat arc = new z_Arc2DFloat();
		arc.x = cx - rx;
		arc.y = cy - ry;
		arc.width = rx * 2.0f;
		arc.height = ry * 2.0f;
		arc.start = -angleStart;
		arc.extent = -angleExtent;
		path.append(arc, true);
	}
}
