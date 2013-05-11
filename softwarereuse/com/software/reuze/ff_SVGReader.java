package com.software.reuze;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ff_SVGReader {
	  public static final Pattern p=Pattern.compile(
			  "(<rect\\s)|(<circle\\s)|(<image\\s)|(<line\\s)|(<text\\s)|(<ellipse\\s)|(<polygon\\s)|(<polyline\\s)|(<linearGradient\\s)|(<radialGradient\\s)|(<path\\s)|(<image\\s)|(<pattern\\s)|(<marker\\s)|(<!--)", 0);
	  public static final Pattern pcomend=Pattern.compile(
			  "-->", 0);
	  public static final ArrayList<Object> parse(Scanner s) {
		  if (s==null) return null;
		  ArrayList<Object> ob=new ArrayList<Object>();
		  //long time=System.currentTimeMillis();
		  for (;;) {
			  String str=s.findWithinHorizon(p, 0);
			  if (str==null) break;
			  System.out.println(str);
			  if (str.charAt(1)=='c') ob.add(ff_SVGReader.circles(s));
			  else if (str.charAt(1)=='p' && str.charAt(4)=='h') ob.add(ff_SVGReader.paths(s));
			  else if (str.charAt(1)=='r' && str.charAt(2)=='e') ob.add(ff_SVGReader.rects(s));
			  else if (str.charAt(1)=='e') ob.add(ff_SVGReader.ellipses(s));
			  else if (str.charAt(1)=='l' && str.charAt(5)==' ') ob.add(ff_SVGReader.lines(s));
			  else if (str.charAt(1)=='i') ob.add(ff_SVGReader.images(s));
			  else if (str.charAt(1)=='t') ob.add(ff_SVGReader.texts(s));
			  else if (str.charAt(1)=='p' && str.charAt(5)=='g') ob.add(ff_SVGReader.polygons(s));
			  else if (str.charAt(1)=='p' && str.charAt(5)=='l') ob.add(ff_SVGReader.polygons(s));
			  else if (str.charAt(1)=='l' && str.charAt(7)=='G') ob.add(ff_SVGReader.linearGradients(s));
			  else if (str.charAt(1)=='r' && str.charAt(7)=='G') ob.add(ff_SVGReader.radialGradients(s));
			  else if (str.charAt(1)=='p' && str.charAt(4)=='t') ob.add(ff_SVGReader.patterns(s));
			  else if (str.charAt(1)=='m') ob.add(ff_SVGReader.markers(s));
			  else if (str.charAt(1)=='!') {
				  str=s.findWithinHorizon(pcomend, 0);
				  if (str==null) break;
			  }
		  }
		  s.close();
		  //System.out.println((float)(System.currentTimeMillis()-time)/1000f);
		  return ob;
	  }
	public static final Pattern pradialgradient=Pattern.compile(
			"(gradientTransform=\")|(cx\\s*=\\s*\"\\d*.??\\d+%\")|(cy\\s*=\\s*\"\\d*.??\\d+%\")|(fx\\s*=\\s*\"\\d*.??\\d+%\")|(fy\\s*=\\s*\"\\d*.??\\d+%\")|" +
	"(r\\s*=\\s*\"\\d*.??\\d+%\")|(>)|(id=\"\\w+\")|(gradientUnits=\"userSpaceOnUse\")|(spreadMethod=\"[a-z]+\")");
	public static final Pattern pradialgradient2=Pattern.compile(
			"(<stop)|(offset\\s*=\\s*\"\\d*.??\\d+%\")|(stop-opacity\\s*=\\s*\"\\d*.??\\d+\")|" +
	"(stop-color\\s*=\\s*\"[a-z0-9%. #(),]+\")|(/>)|(id=\"\\w+\")|(</radialGradient>)");
	public static Object radialGradients(Scanner s) {
		vc_RadialGradientProperties rg=new vc_RadialGradientProperties();
		for (;;) {
			String str=s.findWithinHorizon(pradialgradient, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='>') break;
			if (str.charAt(0)=='c' && str.charAt(1)=='x') rg.cx=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='c' && str.charAt(1)=='y') rg.cy=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='f' && str.charAt(1)=='x') rg.fx=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='f' && str.charAt(1)=='y') rg.fy=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='r') rg.r=str.substring(3,str.length()-1);
			else if (str.charAt(0)=='i') rg.id=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='s') rg.spreadMethod=str.substring(13,str.length()-1);
		}
		rg.stopList = new ArrayList<vc_GradientStopProperties>();
		vc_GradientStopProperties st=null;
		for (;;) {
			String str=s.findWithinHorizon(pradialgradient2, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='/') {
				rg.stopList.add(st);
				continue;
			}
			if (str.charAt(0)=='<' && str.charAt(1)=='/') break;
			if (str.charAt(0)=='<' && str.charAt(1)=='s') {
				st=new vc_GradientStopProperties();
			}
			else if (str.charAt(0)=='o') st.offset=str.substring(8,str.length()-1);
			else if (str.charAt(6)=='p') st.opacity=getFloat(str.substring(13));
			else if (str.charAt(5)=='c') st.color=str.substring(12,str.length()-1);
		}
		rg.isValid(0);
		return rg;
	}

	public static final Pattern plineargradient=Pattern.compile(
			"(gradientTransform=\")|(x1\\s*=\\s*\"\\d*.??\\d+%\")|(y1\\s*=\\s*\"\\d*.??\\d+%\")|(x2\\s*=\\s*\"\\d*.??\\d+%\")|(y2\\s*=\\s*\"\\d*.??\\d+%\")|" +
	"(>)|(id=\"\\w+\")|(gradientUnits=\"userSpaceOnUse\")|(spreadMethod=\"[a-z]+\")");
	public static final Pattern plineargradient2=Pattern.compile(
			"(<stop)|(offset\\s*=\\s*\"\\d*.??\\d+%\")|(stop-opacity\\s*=\\s*\"\\d*.??\\d+\")|" +
	"(stop-color\\s*=\\s*\"[a-z0-9%. #(),]+\")|(/>)|(id=\"\\w+\")|(</linearGradient>)");
	public static Object linearGradients(Scanner s) {
		vc_LinearGradientProperties lg=new vc_LinearGradientProperties();
		for (;;) {
			String str=s.findWithinHorizon(plineargradient, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='>') break;
			if (str.charAt(0)=='x' && str.charAt(1)=='1') lg.x1=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='x' && str.charAt(1)=='2') lg.x2=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='y' && str.charAt(1)=='1') lg.y1=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='y' && str.charAt(1)=='2') lg.y2=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='i') lg.id=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='s') lg.spreadMethod=str.substring(13,str.length()-1);
		}
		lg.stopList = new ArrayList<vc_GradientStopProperties>();
		vc_GradientStopProperties st=null;
		for (;;) {
			String str=s.findWithinHorizon(plineargradient2, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='/') {
				lg.stopList.add(st);
				continue;
			}
			if (str.charAt(0)=='<' && str.charAt(1)=='/') break;
			if (str.charAt(0)=='<' && str.charAt(1)=='s') {
				st=new vc_GradientStopProperties();
			}
			else if (str.charAt(0)=='o') st.offset=str.substring(8,str.length()-1);
			else if (str.charAt(6)=='p') st.opacity=getFloat(str.substring(13));
			else if (str.charAt(5)=='c') st.color=str.substring(12,str.length()-1);
		}
		return lg;
	}

	public static final Pattern pimage=Pattern.compile(
			"(transform=\")|(x\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(y\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|" +
			"(width\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(height\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|" +
	"(xlink:href\\s*=\\s*\"[a-zA-Z0-9$/_.-]+\")|(</image>)|(id=\"\\w+\")");
	public static Object images(Scanner s) {
		ff_SVGImage i=new ff_SVGImage();
		for (;;) {
			String str=s.findWithinHorizon(pimage, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='<') break;
			if (str.charAt(0)=='x' && str.charAt(1)!='l') i.x=getFloat(str);
			else if (str.charAt(0)=='y') i.y=getFloat(str);
			else if (str.charAt(0)=='w') i.width=getFloat(str);
			else if (str.charAt(0)=='h') i.height=getFloat(str);
			else if (str.charAt(0)=='i') i.id=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='x' && str.charAt(1)=='l') i.image=str.substring(12,str.length()-1);
		}
		return i;
	}

	public static final Pattern pstyle=Pattern.compile(
			"(font-family:\\s*\\w+;)|(font-style:\\s*[a-z]+;)|(font-weight:\\s*[a-z0-9]+;)|(font-size:\\s*[0-9]+;)|(text-decoration:\\s*[a-z-]+;)|" +
			"(stroke-dasharray:\\s*[\\d*.?\\d+,?]+;)|(stroke-linejoin:\\s*[a-z]+;)|(stroke-dashoffset:\\s*\\d*.?\\d+;)|" +
			"(stroke-linecap:\\s*[a-z]+;)|(stroke-opacity:\\s*\\d*.?\\d+;)|(fill-opacity:\\s*\\d*.?\\d+;)|" +
			"(opacity:\\s*\\d*.?\\d+;)|(stroke-width:\\s*\\d*.?\\d+;)|(stroke-miterlimit:\\s*\\d*.?\\d+;)|" +
	"(marker-end:\\s*[A-Za-z0-9#()]+;)|(fill:\\s*[A-Za-z0-9%. #(),]+;)|(stroke:\\s*[a-z0-9%. #(),]+;)|(stop-color:\\s*[a-z0-9%. #(),]+;)|(\")");
	public static final vg_StyleProperties styles(Scanner s) {
		vg_StyleProperties st=new vg_StyleProperties();
		int i;
		for (;;) {
			String str=s.findWithinHorizon(pstyle, 0);
			if (str==null) break;
			if (str.charAt(0)=='"') break;
			System.out.println(str);
			if (str.charAt(0)=='f' && str.charAt(4)==':') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.fill=str.substring(5,str.length()-1);
				else st.setFill(str.substring(i+1,str.length()-1));
			} else if (str.charAt(2)=='r' && str.charAt(6)==':') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.stroke=str.substring(7,str.length()-1);
				else st.stroke=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='s' && str.charAt(5)=='c') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.stopColor=str.substring(11,str.length()-1);
				else st.stopColor=str.substring(i+1,str.length()-1);
			} else if (str.charAt(2)=='r' && str.charAt(7)=='w') {
				st.strokeWidth=getFloat(str.substring(11));
			} else if (str.charAt(0)=='o') {
				st.opacity=getFloat(str);
			} else if (str.charAt(0)=='f' && str.charAt(5)=='o') {
				st.fillOpacity=getFloat(str.substring(10));
			} else if (str.charAt(0)=='s' && str.charAt(7)=='o') {
				st.strokeOpacity=getFloat(str.substring(13));
			} else if (str.charAt(0)=='s' && str.charAt(7)=='l' && str.charAt(11)=='c') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.strokeLinecap=str.substring(15,str.length()-1);
				else st.strokeLinecap=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='s' && str.charAt(7)=='l' && str.charAt(11)=='j') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.strokeLinejoin=str.substring(16,str.length()-1);
				else st.strokeLinejoin=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='s' && str.charAt(7)=='d') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.strokeDasharray=str.substring(17,str.length()-1);
				else st.strokeDasharray=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='s' && str.charAt(7)=='m') {
				st.strokeMiterlimit=getFloat(str.substring(16));
			} else if (str.charAt(0)=='s' && str.charAt(7)=='d' && str.charAt(11)=='o') {
				st.strokeDashoffset=getFloat(str.substring(16));
			} else if (str.charAt(0)=='f' && str.charAt(5)=='f') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.fontFamily=str.substring(12,str.length()-1);
				else st.fontFamily=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='f' && str.charAt(6)=='t') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.fontStyle=str.substring(11,str.length()-1);
				else st.fontStyle=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='f' && str.charAt(6)=='i') {
				st.fontSize=(int)getFloat(str.substring(8));
			} else if (str.charAt(0)=='f' && str.charAt(5)=='w') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.fontWeight=str.substring(12,str.length()-1);
				else st.fontWeight=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='t' && str.charAt(2)=='x') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.textDecoration=str.substring(16,str.length()-1);
				else st.textDecoration=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='m' && str.charAt(7)=='e') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.markerEnd=str.substring(11,str.length()-1);
				else st.markerEnd=str.substring(i+1,str.length()-1);
			}
		}
		System.out.println(st);
		if (!st.isValid(0)) st=ga_LineProperties.defaults;
		return st;
	}
	public static final Pattern pstyle2=Pattern.compile(
			"|(stroke-dasharray=\\s*\"[\\d*.?\\d+,?]+\")|(stroke-linejoin=\\s*\"[a-z]+\")|(stroke-dashoffset=\\s*\"\\d*.?\\d+\")|" +
			"(stroke-linecap=\\s*\"[a-z]+\")|(stroke-opacity=\\s*\"\\d*.?\\d+\")|(fill-opacity=\\s*\"\\d*.?\\d+\")|" +
			"(opacity=\\s*\"\\d*.?\\d+\")|(stroke-width=\\s*\"\\d*.?\\d+\")|(stroke-miterlimit=\\s*\"\\d*.?\\d+\")|" +
			"(fill=\\s*\"[A-Za-z0-9%. #(),]+\")|(stroke=\\s*\"[a-z0-9%. #(),]+\")");
	public static final Pattern ppath=Pattern.compile("(style=\")|(transform=\")|(id=\"[\\sa-zA-Z0-9-]+\")|(d\\s*=\\s*\"[\\sa-zA-Z0-9,.+-]+\")|(/>)"+pstyle2);
	public static Object paths(Scanner s) {
		vg_PathProperties p=new vg_PathProperties();
		int i;
		for (;;) {
			String str=s.findWithinHorizon(ppath, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='/') break;
			if (str.charAt(0)=='d') {
				i=str.indexOf("\"");
				p.d=str.substring(i+1,str.length()-1);
			}
			else if (str.charAt(0)=='i') p.id=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='s' && str.charAt(2)=='y') p.draw=styles(s);
			else {
				if (p.draw==null) p.draw=new vg_StyleProperties();
				if (!checkStyle(str, p.draw)) break;
			}
		}
		return p;
	}
	
	private static boolean checkStyle(String str, vg_StyleProperties st) {
		int i;
			if (str==null) return false;
			if (st==null) return false;
			if (str.charAt(0)=='f' && str.charAt(4)=='=') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.setFill(str.substring(6,str.length()-1));
				else st.setFill(str.substring(i+1,str.length()-1));
			} else if (str.charAt(2)=='r' && str.charAt(6)=='=') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.stroke=str.substring(8,str.length()-1);
				else st.stroke=str.substring(i+1,str.length()-1);
			} else if (str.charAt(2)=='r' && str.charAt(7)=='w') {
				st.strokeWidth=getFloat(str.substring(13));
			} else if (str.charAt(0)=='o') {
				st.opacity=getFloat(str);
			} else if (str.charAt(0)=='f' && str.charAt(5)=='o') {
				st.fillOpacity=getFloat(str.substring(10));
			} else if (str.charAt(0)=='s' && str.charAt(7)=='o') {
				st.strokeOpacity=getFloat(str.substring(13));
			} else if (str.charAt(0)=='s' && str.charAt(7)=='l' && str.charAt(11)=='c') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.strokeLinecap=str.substring(15,str.length()-1);
				else st.strokeLinecap=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='s' && str.charAt(7)=='l' && str.charAt(11)=='j') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.strokeLinejoin=str.substring(16,str.length()-1);
				else st.strokeLinejoin=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='s' && str.charAt(7)=='d') {
				i=str.lastIndexOf(" ");
				if (i<=0) st.strokeDasharray=str.substring(17,str.length()-1);
				else st.strokeDasharray=str.substring(i+1,str.length()-1);
			} else if (str.charAt(0)=='s' && str.charAt(7)=='m') {
				st.strokeMiterlimit=getFloat(str.substring(16));
			} else if (str.charAt(0)=='s' && str.charAt(7)=='d' && str.charAt(11)=='o') {
				st.strokeDashoffset=getFloat(str.substring(16));
			}
		System.out.println(st);
		return true;
	}
	public static final Pattern pline=Pattern.compile(
	"(style=\")|(transform=\")|(x1\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(y1\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(x2\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(y2\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(/>)|(id=\"\\w+\")");
	public static Object lines(Scanner s) {
		ga_LineProperties l=new ga_LineProperties();
		for (;;) {
			String str=s.findWithinHorizon(pline, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='/') break;
			if (str.charAt(0)=='x' && str.charAt(1)=='1') l.x1=getFloat(str.substring(2));
			else if (str.charAt(0)=='x' && str.charAt(1)=='2') l.x2=getFloat(str.substring(2));
			else if (str.charAt(0)=='y' && str.charAt(1)=='1') l.y1=getFloat(str.substring(2));
			else if (str.charAt(0)=='y' && str.charAt(1)=='2') l.y2=getFloat(str.substring(2));
			else if (str.charAt(0)=='i') l.id=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='s') l.draw=styles(s);
		}
		return l;
	}
	public static final Pattern pcircle=Pattern.compile(
	"(style=\")|(transform=\")|(cx\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(cy\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(r\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(id=\"\\w+\")|(fill\\s*=\\s*\"[a-z0-9%. #(),]+\")|(/>)"+pstyle2);
	public static Object circles(Scanner s) {
		ga_CircleProperties c=new ga_CircleProperties();
		for (;;) {
			String str=s.findWithinHorizon(pcircle, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='/') break;
			if (str.charAt(0)=='r') c.r=getFloat(str);
			else if (str.charAt(1)=='x') c.cx=getFloat(str);
			else if (str.charAt(1)=='y') c.cy=getFloat(str);
			else if (str.charAt(0)=='i') c.id=str.substring(4,str.length()-1);
			else if (str.charAt(2)=='y') c.draw=styles(s);
			else {
				if (c.draw==null) c.draw=new vg_StyleProperties();
				if (!checkStyle(str, c.draw)) break;
			}
		}
		return c;
	}
	public static final Pattern pellipse=Pattern.compile(
	"(style=\")|(transform=\")|(cx\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(cy\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(rx\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(ry\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(id=\"\\w+\")|(fill\\s*=\\s*\"[a-z0-9%. #(),]+\")|(/>)"+pstyle2);
	public static Object ellipses(Scanner s) {
		ga_EllipseProperties e=new ga_EllipseProperties();
		for (;;) {
			String str=s.findWithinHorizon(pellipse, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='/') break;
			if (str.charAt(0)=='r' && str.charAt(1)=='x') e.rx=getFloat(str);
			else if (str.charAt(0)=='r' && str.charAt(1)=='y') e.ry=getFloat(str);
			else if (str.charAt(1)=='x') e.cx=getFloat(str);
			else if (str.charAt(1)=='y') e.cy=getFloat(str);
			else if (str.charAt(0)=='i') e.id=str.substring(4,str.length()-1);
			else if (str.charAt(2)=='y') e.draw=styles(s);
			else {
				if (e.draw==null) e.draw=new vg_StyleProperties();
				if (!checkStyle(str, e.draw)) break;
			}
		}
		return e;
	}
	public static final Pattern prect=Pattern.compile(
			"(style=\")|(transform=\")|(x\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(y\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|" +
			"(width\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(height\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|" +
	"(rx\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(ry\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(id=\"\\w+\")|(/>)"+pstyle2);
	public static Object rects(Scanner s) {
		ga_RectangleProperties r=new ga_RectangleProperties();
		for (;;) {
			String str=s.findWithinHorizon(prect, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='/') break;
			if (str.charAt(0)=='r' && str.charAt(1)=='x') r.rx=getFloat(str);
			else if (str.charAt(0)=='r' && str.charAt(1)=='y') r.ry=getFloat(str);
			else if (str.charAt(0)=='x') r.x=getFloat(str);
			else if (str.charAt(0)=='y') r.y=getFloat(str);
			else if (str.charAt(0)=='w') r.width=getFloat(str);
			else if (str.charAt(0)=='h') r.height=getFloat(str);
			else if (str.charAt(0)=='i') r.id=str.substring(4,str.length()-1);
			else if (str.charAt(2)=='y') r.draw=styles(s);
			else {
				if (r.draw==null) r.draw=new vg_StyleProperties();
				if (!checkStyle(str, r.draw)) break;
			}
		}
		return r;
	}

	public static final Pattern ptext=Pattern.compile(
			"(transform=\")|(x\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|(y\\s*=\\s*\"-??\\+??\\d*.??\\d+\")|" +
	"(>.+</text>)|(style=\")|(id=\"\\w+\")");
	public static Object texts(Scanner s) {
		ff_SVGText t=new ff_SVGText();
		vg_StyleProperties st=null;
		for (;;) {
			String str=s.findWithinHorizon(ptext, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='x') t.x=getFloat(str);
			else if (str.charAt(0)=='y') t.y=getFloat(str);
			else if (str.charAt(0)=='i') t.id=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='s') st=styles(s);
			else if (str.charAt(0)=='>') {
				t.content=str.substring(1,str.length()-7);
				break;
			}
		}
		ff_SVGTextStyle tt=new ff_SVGTextStyle(t);
		tt.draw=st;
		return tt;
	}
	public static final Pattern ppolygon=Pattern.compile(
			"(transform=\")|(points\\s*=\\s*\"[\\s0-9,.+-]+\")|" +
	"(style\\s*=\\s*\")|(id=\"\\w+\")|(fill\\s*=\\s*\"[a-z0-9%. #(),]+\")|(/>)"+pstyle2);
	public static Object polygons(Scanner s) {
		ff_SVGPolygon pn=new ff_SVGPolygon();
		vg_StyleProperties sty=null;
		for (;;) {
			String str=s.findWithinHorizon(ppolygon, 0);
			if (str==null) break;
			System.out.println(str);
			if (str.charAt(0)=='/') break;
			else if (str.charAt(0)=='i') pn.id=str.substring(4,str.length()-1);
			else if (str.charAt(2)=='y') sty=styles(s);
			else if (str.charAt(0)=='p') pn.points=str.substring(8,str.length()-1);
			else {
				if (sty==null) sty=new vg_StyleProperties();
				if (!checkStyle(str, sty)) break;
			}
		}
		vg_PathProperties p=pn.getPath();
		p.draw=sty;
		return p;
	}


	public static final Pattern ppattern=Pattern.compile(
			"(patternTransform=\")|(x\\s*=\\s*\"[\\s0-9.+-]+\")|(y\\s*=\\s*\"[\\s0-9.+-]+\")|" +
	"(viewBox\\s*=\\s*\"[\\s0-9]+\")|(height\\s*=\\s*\"[\\s0-9.+-]+\")|(width\\s*=\\s*\"[\\s0-9.+-]+\")|(>)|(id=\"\\w+\")");
	public static final Pattern ppattern2=Pattern.compile(
			"(<rect )|(</pattern>)"
	);
	public static Object patterns(Scanner s) {
		vg_PatternProperties pn=new vg_PatternProperties();
		for (;;) {
			String str=s.findWithinHorizon(ppattern, 0);
			if (str==null) return null;
			System.out.println(str);
			if (str.charAt(0)=='>') break;
			else if (str.charAt(0)=='i') pn.id=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='x') pn.x=getFloat(str);
			else if (str.charAt(0)=='y') pn.y=getFloat(str);
			else if (str.charAt(0)=='w') pn.width=getFloat(str);
			else if (str.charAt(0)=='h') pn.height=getFloat(str);
		}
		ArrayList<Object> ob=new ArrayList<Object>();
		for (;;) {
			String str=s.findWithinHorizon(ppattern2, 0);
			if (str==null) return null;
			System.out.println(str);
			if (str.charAt(1)=='/') break;
			if (str.charAt(1)=='r' && str.charAt(2)=='e') ob.add(rects(s));
		}
		if (ob.size()!=0) pn.objects=ob;
		ob=null;
		return pn;
	}
	public static final Pattern pmarker=Pattern.compile(
			"(refX\\s*=\\s*\"[\\s0-9.+-]+\")|(refY\\s*=\\s*\"[\\s0-9.+-]+\")|" +
	"(viewBox\\s*=\\s*\"[\\s0-9]+\")|(markerHeight\\s*=\\s*\"[\\s0-9.+-]+\")|(markerWidth\\s*=\\s*\"[\\s0-9.+-]+\")|(>)|(id=\"\\w+\")");
	public static final Pattern pmarker2=Pattern.compile(
			"(<rect )|(</marker>)"
	);
	public static Object markers(Scanner s) {
		vg_PatternProperties pn=new vg_PatternProperties();
		for (;;) {
			String str=s.findWithinHorizon(pmarker, 0);
			if (str==null) return null;
			System.out.println(str);
			if (str.charAt(0)=='>') break;
			else if (str.charAt(0)=='i') pn.id=str.substring(4,str.length()-1);
			else if (str.charAt(0)=='r' && str.charAt(3)=='X') pn.x=getFloat(str);
			else if (str.charAt(0)=='r' && str.charAt(3)=='Y') pn.y=getFloat(str);
			else if (str.charAt(0)=='m' && str.charAt(6)=='W') pn.width=getFloat(str);
			else if (str.charAt(0)=='m' && str.charAt(6)=='H') pn.height=getFloat(str);
		}
		ArrayList<Object> ob=new ArrayList<Object>();
		for (;;) {
			String str=s.findWithinHorizon(pmarker2, 0);
			if (str==null) return null;
			System.out.println(str);
			if (str.charAt(1)=='/') break;
			if (str.charAt(1)=='r' && str.charAt(2)=='e') ob.add(rects(s));
		}
		pn.marker=true;
		if (ob.size()!=0) pn.objects=ob;
		ob=null;
		return pn;
	}

	/* scans to first +/-/./digit, assumes last character, such as ; or ", is ignored */
	public static float getFloat(String str) {
		for (int i=0; i<str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)) && str.charAt(i)!='.' && str.charAt(i)!='+' && str.charAt(i)!='-') continue;
			return Float.valueOf(str.substring(i,str.length()-1));
		}
		return Float.NaN;
	}
}
