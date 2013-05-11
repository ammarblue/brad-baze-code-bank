package com.software.reuze;
import java.util.ArrayList;
import java.util.StringTokenizer;

//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class vg_StyleProperties implements da_i_Validate {
	public String fill, stroke, markerEnd;
	public float strokeWidth, opacity=1, fillOpacity=1, strokeOpacity=1;
	public String strokeLinecap, strokeLinejoin, strokeDasharray;
	public String fontFamily;
	public String fontStyle;
	public String fontWeight;
	public int fontSize=10;
	public float strokeMiterlimit=4, strokeDashoffset;
	public float dashArray[], stopOpacity;
	public String textDecoration, stopColor;
	public int fontweight=400;
	public static vg_StyleProperties create(String svgString) {
		return null;
		//return SVGJAXBreader.getStyle(svgString);
	}
	public String getFontWeight() {
		return fontWeight;
	}
//	@XmlAttribute(name="font-weight")
	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}
	public String getTextDecoration() {
		return textDecoration;
	}
	public float getStopOpacity() {
		return stopOpacity;
	}
//	@XmlAttribute(name="stop-opacity")
	public void setStopOpacity(float stopOpacity) {
		this.stopOpacity = stopOpacity;
	}
	public String getStopColor() {
		return stopColor;
	}
//	@XmlAttribute(name="stop-color")
	public void setStopColor(String stopColor) {
		this.stopColor = stopColor;
	}
//	@XmlAttribute(name="text-decoration")
	public void setTextDecoration(String textDecoration) {
		this.textDecoration = textDecoration;
	}
	public String getFontFamily() {
		return fontFamily;
	}
//	@XmlAttribute(name="font-family")
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}
	public String getFontStyle() {
		return fontStyle;
	}
//	@XmlAttribute(name="font-style")
	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}
	public int getFontSize() {
		return fontSize;
	}
//	@XmlAttribute(name="font-size")
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public String getStrokeDasharray() {
		return strokeDasharray;
	}
//	@XmlAttribute(name="stroke-dasharray")
	public void setStrokeDasharray(String strokeDasharray) {
		this.strokeDasharray = strokeDasharray;
	}
	public float getStrokeDashoffset() {
		return strokeDashoffset;
	}
//	@XmlAttribute(name="stroke-dashoffset")
	public void setStrokeDashoffset(float strokeDashoffset) {
		this.strokeDashoffset = strokeDashoffset;
	}
	public float getStrokeMiterlimit() {
		return strokeMiterlimit;
	}
//	@XmlAttribute(name="stroke-miterlimit")
	public void setStrokeMiterlimit(float strokeMiterlimit) {
		this.strokeMiterlimit = strokeMiterlimit;
	}
	public String getStrokeLinecap() {
		return strokeLinecap;
	}
//	@XmlAttribute(name="stroke-linecap")
	public void setStrokeLinecap(String strokeLinecap) {
		this.strokeLinecap = strokeLinecap;
	}
	public String getStrokeLinejoin() {
		return strokeLinejoin;
	}
//	@XmlAttribute(name="stroke-linejoin")
	public void setStrokeLinejoin(String strokeLinejoin) {
		this.strokeLinejoin = strokeLinejoin;
	}
	public vg_StyleProperties() {};
	public vg_StyleProperties(String fill, String stroke, float width) {
		this.fill=fill; this.stroke=stroke; strokeWidth=width;
	}
	public float getOpacity() {
		return opacity;
	}
//	@XmlAttribute
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	public float getFillOpacity() {
		return fillOpacity;
	}
//	@XmlAttribute(name="fill-opacity")
	public void setFillOpacity(float fillOpacity) {
		this.fillOpacity = fillOpacity;
	}
	public float getStrokeOpacity() {
		return strokeOpacity;
	}
//	@XmlAttribute(name="stroke-opacity")
	public void setStrokeOpacity(float strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	}
	@Override
	public String toString() {
		return "style [fill=" + fill + ", stroke=" + stroke + ", strokeWidth="
				+ strokeWidth + ", opacity=" + opacity + ", fillOpacity="
				+ fillOpacity + ", strokeOpacity=" + strokeOpacity + "]";
	}
	public String getFill() {
		return fill;
	}
//	@XmlAttribute
	public void setFill(String fill) {
		if (fill!=null && fill.compareToIgnoreCase("none")==0) fill=null;
		else this.fill = fill;
	}
	public String getStroke() {
		return stroke;
	}
//	@XmlAttribute
	public void setStroke(String stroke) {
		this.stroke = stroke;
	}
	public float getStrokeWidth() {
		return strokeWidth;
	}
//	@XmlAttribute(name="stroke-width")
	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	public boolean isValid(int extra) {
		if (fill!=null && fill.compareTo("none")==0) fill=null;
		if (stroke==null) {
			if (fill==null) fill="black";
		} else if (stroke.compareTo("none")==0) {
			if (fill==null) fill="black";
			stroke=null;
		}
		if (strokeLinecap!=null) {
			if (strokeLinecap.compareTo("butt")==0) ;
			else if (strokeLinecap.compareTo("round")==0) ;
			else if (strokeLinecap.compareTo("square")==0) ;
			else strokeLinecap=null;
		}
		if (strokeLinejoin!=null) {
			if (strokeLinejoin.compareTo("miter")==0) ;
			else if (strokeLinejoin.compareTo("round")==0) ;
			else if (strokeLinejoin.compareTo("bevel")==0) ;
			else strokeLinejoin=null;
		}
		if (fontWeight!=null) {
			if (fontWeight.compareTo("bold")==0) fontweight=700;
			else {
				if (Character.isDigit(fontWeight.charAt(0))) {
					fontweight=Integer.valueOf(fontWeight);
					if (fontweight<100) fontweight=100;
					if (fontweight>900) fontweight=900;
				}
			}
		}
		if (textDecoration!=null) {
			if (textDecoration.compareTo("underline")!=0 && textDecoration.compareTo("line-through")!=0) textDecoration=null;
		}
		if (strokeDasharray!=null) {
			ArrayList<Float> ai=new ArrayList<Float>();
			   if (strokeDasharray.length()!=0) {
				   StringTokenizer s=new StringTokenizer(strokeDasharray, " ,");
				   while (s.hasMoreTokens()) {
					   String nt=s.nextToken();
					   float f=Float.valueOf(nt);
					   if (f<0) {System.out.println("dash width negative"); f=0;}
					   ai.add(f);
				   }
				   dashArray=new float[ai.size()];
				   int i=0;
				   for (float f:ai) dashArray[i++]=f;
				   ai=null;
				   strokeDasharray=null;
			   }
		}
		return true;
	}
}
