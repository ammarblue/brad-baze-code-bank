package reuze.awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.TexturePaint;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.software.reuze.ff_SVGImage;
import com.software.reuze.ff_SVGTextStyle;
import com.software.reuze.ga_CircleProperties;
import com.software.reuze.ga_EllipseProperties;
import com.software.reuze.ga_LineProperties;
import com.software.reuze.ga_RectangleProperties;
import com.software.reuze.vc_ColorNames;
import com.software.reuze.vc_LinearGradientProperties;
import com.software.reuze.vc_RadialGradientProperties;
import com.software.reuze.vg_PathProperties;
import com.software.reuze.vg_PatternProperties;
import com.software.reuze.vg_StyleProperties;

public class ff_SVGDrawAWT {
	  public static ArrayList<Object> objects=null;
	  public static float width, height;
	  
	  public static final void setObjects(ArrayList<Object> ob, float w, float h) {
		  objects=ob; width=w; height=h;
	  }
	  
	  public static final void drawCircle(Graphics2D g2, ga_CircleProperties cr) {
		  if (cr==null || !cr.isValid(0)) return;
		  Ellipse2D.Float crr=new Ellipse2D.Float(cr.cx-cr.r, cr.cy-cr.r, cr.r*2, cr.r*2);
		  Color c;
		  if (crr!=null) {
			  if (cr.draw.getFill()!=null) {
				  c=vc_ColorNames.getColor(cr.draw.getFill(), (cr.draw.opacity<1)?cr.draw.opacity:cr.draw.fillOpacity);
				  g2.setPaint(c);
				  g2.fill(crr);
				  if (cr.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(cr.draw.getStroke(), (cr.draw.opacity<1)?cr.draw.opacity:cr.draw.strokeOpacity);
					  g2.setPaint(c);
					  g2.setStroke(new BasicStroke(cr.draw.strokeWidth));
					  g2.draw(crr);
				  }
			  }
			  else {
				  if (cr.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(cr.draw.getStroke(), (cr.draw.opacity<1)?cr.draw.opacity:cr.draw.strokeOpacity);
					  g2.setPaint(c);
				  } else g2.setPaint(Color.black);
				  g2.setStroke(new BasicStroke(cr.draw.strokeWidth));
				  g2.draw(crr);
			  }
		  }
	  }
	  
	  public static final void drawEllipse(Graphics2D g2, ga_EllipseProperties er) {
		  Color c=null;
		  if (er==null || !er.isValid(0)) return;
		  Ellipse2D.Float err=new Ellipse2D.Float(er.cx-er.rx, er.cy-er.ry, er.rx*2, er.ry*2);
		     if (er.draw.fill!=null) {
		  		  setFill(g2, er.draw, er.cx-er.rx, er.cy-er.ry, er.rx*2, er.ry*2);
				  g2.fill(err);
				  if (er.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(er.draw.getStroke(), (er.draw.opacity<1)?er.draw.opacity:er.draw.strokeOpacity);
					  g2.setPaint(c);
					  g2.setStroke(new BasicStroke(er.draw.strokeWidth));
					  g2.draw(err);
				  }
			  }
			  else {
				  if (er.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(er.draw.getStroke(), (er.draw.opacity<1)?er.draw.opacity:er.draw.strokeOpacity);
					  g2.setPaint(c);
				  } else g2.setPaint(Color.black);
				  g2.setStroke(new BasicStroke(er.draw.strokeWidth));
				  g2.draw(err);
			  }
	  }
	  
	  public static final void drawImage(Graphics2D g2, ff_SVGImage ig) {
		  if (ig==null || !ig.isValid(0)) return;
		  BufferedImage big=null,bg=null;
		  try {
				big=ImageIO.read(new File(ig.image));
			} catch (IOException e) {
				big=null;
			}
			if (big!=null) {
				g2.drawImage(big, null, (int)ig.x, (int)ig.y);
			}
	  }
	  
	  public static final void drawLine(Graphics2D g2, ga_LineProperties l) {
		  if (l==null || !l.isValid(0)) return;
		  Line2D.Float ll=new Line2D.Float(l.x1, l.y1, l.x2, l.y2);
		  Color c;
		  if (ll!=null) {
			  if (l.draw.getStroke()!=null) {
				  c=vc_ColorNames.getColor(l.draw.getStroke(), (l.draw.opacity<1)?l.draw.opacity:l.draw.strokeOpacity);
				  g2.setPaint(c);
			  } else g2.setPaint(Color.black);
			  char x='b', y='m';
			  if (l.draw.strokeLinecap!=null) x=l.draw.strokeLinecap.charAt(0);
			  if (l.draw.strokeLinejoin!=null) y=l.draw.strokeLinejoin.charAt(0);
			  if (x!='b' || y!='m')  { 
				  g2.setStroke(new BasicStroke(l.draw.strokeWidth, 
						  (x=='r')?BasicStroke.CAP_ROUND: (x=='s')?BasicStroke.CAP_SQUARE:BasicStroke.CAP_BUTT, 
								  (x=='r')?BasicStroke.JOIN_ROUND: (x=='b')?BasicStroke.JOIN_BEVEL:BasicStroke.JOIN_MITER,
										  l.draw.strokeMiterlimit));
			  } else if (l.draw.dashArray!=null) g2.setStroke(new BasicStroke(l.draw.strokeWidth, BasicStroke.CAP_BUTT,
					  BasicStroke.JOIN_MITER, l.draw.strokeMiterlimit, l.draw.dashArray, l.draw.strokeDashoffset));
			  else g2.setStroke(new BasicStroke(l.draw.strokeWidth));
			  g2.draw(ll);
			  if (l.draw.markerEnd!=null) {
				  Object url=getName(l.draw.markerEnd.substring(5,l.draw.markerEnd.length()-1));
				  if (url!=null)
					  if (url instanceof vg_PatternProperties) {
						  vg_PatternProperties p=(vg_PatternProperties)url;
						  if (p.marker) drawMarker(g2,l.x1, l.y1, l.x2, l.y2, l.draw.strokeWidth, p);
					  }
			  }
		  }
	  }
	  
	  public static final void drawMarker(Graphics2D g, float x1, float y1, float x2, float y2, float lineWidth, vg_PatternProperties p) {
	      double dx = x2 - x1, dy = y2 - y1;
	      double angle = Math.atan2(dy, dx);
	      int len = (int) Math.sqrt(dx*dx + dy*dy);
	      AffineTransform at = AffineTransform.getTranslateInstance(x2+p.width-1, y2-(p.height*lineWidth)/2);
	      at.concatenate(AffineTransform.getRotateInstance(angle));
	      at.concatenate(AffineTransform.getScaleInstance(p.width,p.height));
	      AffineTransform at2=g.getTransform();
	      g.setTransform(at);
	      paintObjects(g, p.objects);
	      g.setTransform(at2);
	  }
	  
	  public static final void drawPath(Graphics2D g2, vg_PathProperties pr) {
		  Color c;
		  if (pr!=null && pr.isValid(0) && pr.points!=null) {
			  if (pr.draw.getFill()!=null) {
				  c=vc_ColorNames.getColor(pr.draw.getFill(), (pr.draw.opacity<1)?pr.draw.opacity:pr.draw.fillOpacity);
				  g2.setPaint(c);
				  g2.fill(pr.points.path);
				  if (pr.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(pr.draw.getStroke(), (pr.draw.opacity<1)?pr.draw.opacity:pr.draw.strokeOpacity);
					  g2.setPaint(c);
					  g2.setStroke(new BasicStroke(pr.draw.strokeWidth));
					  g2.draw(pr.points.path);
				  }
			  }
			  else {
				  if (pr.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(pr.draw.getStroke(), (pr.draw.opacity<1)?pr.draw.opacity:pr.draw.strokeOpacity);
					  g2.setPaint(c);
				  } else g2.setPaint(Color.black);
				  g2.setStroke(new BasicStroke(pr.draw.strokeWidth));
				  g2.draw(pr.points.path);
			  }
		  }
	  }
	  public static final void setStroke(Graphics2D g2, vg_StyleProperties st) {
		  char x='b', y='m';
		  if (st.strokeLinecap!=null) x=st.strokeLinecap.charAt(0);
		  if (st.strokeLinejoin!=null) y=st.strokeLinejoin.charAt(0);
		  if (x!='b' || y!='m')  {
			  g2.setStroke(new BasicStroke(st.strokeWidth, 
					  (x=='r')?BasicStroke.CAP_ROUND: (x=='s')?BasicStroke.CAP_SQUARE:BasicStroke.CAP_BUTT, 
							  (y=='r')?BasicStroke.JOIN_ROUND: (y=='b')?BasicStroke.JOIN_BEVEL:BasicStroke.JOIN_MITER,
									  st.strokeMiterlimit));
		  } else if (st.dashArray!=null) g2.setStroke(new BasicStroke(st.strokeWidth, BasicStroke.CAP_BUTT,
				  BasicStroke.JOIN_MITER, st.strokeMiterlimit, st.dashArray, st.strokeDashoffset));
		  else g2.setStroke(new BasicStroke(st.strokeWidth));
	  }
	  public static final void drawRect(Graphics2D g2, ga_RectangleProperties r) {
		  if (r==null || !r.isValid(0)) return;
		  RectangularShape rr=null;
		  Color c;
		  if (r.rx>0) rr=new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, r.rx, r.ry);
		  else rr=new Rectangle2D.Float(r.x, r.y, r.width, r.height);
		  if (rr!=null) {
			  if (r.draw.getFill()!=null) {
				  c=vc_ColorNames.getColor(r.draw.getFill(), (r.draw.opacity<1)?r.draw.opacity:r.draw.fillOpacity);
				  g2.setPaint(c);
				  g2.fill(rr);
				  if (r.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(r.draw.getStroke(), (r.draw.opacity<1)?r.draw.opacity:r.draw.strokeOpacity);
					  g2.setPaint(c);
					  setStroke(g2, r.draw);
					  g2.draw(rr);
				  }
			  }
			  else {
				  if (r.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(r.draw.getStroke(), (r.draw.opacity<1)?r.draw.opacity:r.draw.strokeOpacity);
					  g2.setPaint(c);
				  } else g2.setPaint(Color.black);
				  setStroke(g2, r.draw);
				  g2.draw(rr);
			  }
		  }
	  }
	  
	  public static final void drawText(Graphics2D g2, ff_SVGTextStyle tr) {
		  if (tr==null || !tr.isValid(0)) return;
			  AttributedString as=new AttributedString(tr.content);
			  if (tr.draw.fontFamily!=null || tr.draw.fontStyle!=null || tr.draw.fontSize!=10) {
				  int st=Font.PLAIN;
				  if (tr.draw.fontStyle!=null && tr.draw.fontStyle.charAt(0)=='i') st=Font.ITALIC;
				  g2.setFont(new Font(tr.draw.fontFamily, st, tr.draw.fontSize));
			  }
			  as.addAttribute(TextAttribute.FONT, g2.getFont());
			  if (tr.draw.textDecoration!=null) {
				  if (tr.draw.textDecoration.charAt(0)=='u') 
					  as.addAttribute(TextAttribute.UNDERLINE,  //STRIKETHROUGH,
				        TextAttribute.UNDERLINE_ON,0,tr.content.length());     //STRIKETHROUGH_ON);
				  else
					  as.addAttribute(TextAttribute.STRIKETHROUGH,
						        TextAttribute.STRIKETHROUGH_ON);
			  }
			  Color c;
			  if (tr.draw.getFill()!=null) {
				  if (tr.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(tr.draw.getStroke(), (tr.draw.opacity<1)?tr.draw.opacity:tr.draw.strokeOpacity);
					  g2.setColor(c);
					  g2.drawString(tr.content, tr.x-1, tr.y-1);
					  g2.drawString(tr.content, tr.x-1, tr.y+1);
					  g2.drawString(tr.content, tr.x+1, tr.y-1);
					  g2.drawString(tr.content, tr.x+1, tr.y+1);
				  }
				  c=vc_ColorNames.getColor(tr.draw.getFill(), (tr.draw.opacity<1)?tr.draw.opacity:tr.draw.fillOpacity);
				  g2.setColor(c);
				  g2.drawString(as.getIterator(), tr.x, tr.y);
			  }
			  else {			  
				  if (tr.draw.getStroke()!=null) {
					  c=vc_ColorNames.getColor(tr.draw.getStroke(), (tr.draw.opacity<1)?tr.draw.opacity:tr.draw.strokeOpacity);
					  g2.setColor(c);
				  } else g2.setColor(Color.black);
				  g2.drawString(tr.content, tr.x-1, tr.y-1);  //TODO x,y are too far left and down by 1 pixel
				  g2.drawString(tr.content, tr.x-1, tr.y+1);
				  g2.drawString(tr.content, tr.x+1, tr.y-1);
				  g2.drawString(as.getIterator(), tr.x+1, tr.y+1);
				  g2.setColor(g2.getBackground());
				  g2.drawString(tr.content, tr.x, tr.y);
			  }
	  }
	  
	  public static final void setFill(Graphics2D g2, vg_StyleProperties st, float x, float y, float width, float height) {
		  Color c;
		  if (st.fill==null) return;
			  if (st.fill.charAt(0)!='u') {
				  c=vc_ColorNames.getColor(st.fill, (st.opacity<1)?st.opacity:st.fillOpacity);
				  g2.setPaint(c);
			  } else {
				  Object url=getName(st.fill.substring(5,st.fill.length()-1));
				  if (url!=null) {
					  if (url instanceof vc_LinearGradientProperties) {
						  vc_LinearGradientProperties lg=(vc_LinearGradientProperties)url;
						  if (!lg.isValid(0)) return;
						  float startx, starty,endx,endy;
						  if (lg.gradientUnits==null) {
							  startx=x+width*lg.X1;  endx=x+width*lg.X2;
							  starty=y+height*lg.Y1;  endy=y+height*lg.Y2;
						  } else {
							  startx=width*lg.X1;  endx=width*lg.X2;
							  starty=height*lg.Y1;  endy=height*lg.Y2;
						  }
						  LinearGradientPaint lgp=new LinearGradientPaint(startx, starty, endx,endy,lg.f,lg.c,(lg.spreadMethod.charAt(0)=='p')?MultipleGradientPaint.CycleMethod.NO_CYCLE:
							  (lg.spreadMethod.compareTo("repeat")==0)?MultipleGradientPaint.CycleMethod.REPEAT:MultipleGradientPaint.CycleMethod.REFLECT);
						  g2.setPaint(lgp);
					  }
					  if (url instanceof vc_RadialGradientProperties) {
						  vc_RadialGradientProperties rg=(vc_RadialGradientProperties)url;
						  RadialGradientPaint rgp=new RadialGradientPaint(x,y,width,rg.f,rg.c,(rg.spreadMethod.charAt(0)=='p')?MultipleGradientPaint.CycleMethod.NO_CYCLE:
							  (rg.spreadMethod.compareTo("repeat")==0)?MultipleGradientPaint.CycleMethod.REPEAT:MultipleGradientPaint.CycleMethod.REFLECT);
						  g2.setPaint(rgp);
					  }
					  if (url instanceof vg_PatternProperties) {
						  vg_PatternProperties pn=(vg_PatternProperties)url;
						  BufferedImage bufferedImage =
						        new BufferedImage((int)pn.width, (int)pn.height, BufferedImage.TYPE_INT_ARGB);
						Graphics2D g = bufferedImage.createGraphics();
						paintObjects(g, pn.objects);
						Rectangle2D rect = new Rectangle2D.Double(0, 0, pn.width, pn.height);
						g2.setPaint(new TexturePaint(bufferedImage, rect));
					  }
				  }
			  }
	  }
	  
	  public static final Object getName(String name) {
		  for (Object ob:objects) {
			  if (ob instanceof vc_LinearGradientProperties) {
				  if (((vc_LinearGradientProperties)ob).id.compareTo(name)==0) return ob;
			  }
			  if (ob instanceof vc_RadialGradientProperties) {
				  if (((vc_RadialGradientProperties)ob).id.compareTo(name)==0) return ob;
			  }
			  if (ob instanceof vg_PatternProperties) {
				  if (((vg_PatternProperties)ob).id.compareTo(name)==0) return ob;
			  }
		  }
		  return null;
	  }
	  
	  public static final void paintObjects(Graphics2D g2, ArrayList<Object> objects) {
		  if (objects!=null) {
			  ff_SVGDrawAWT.objects=objects;
		  for (Object ob:objects) {
			  if (ob instanceof vg_PathProperties) ff_SVGDrawAWT.drawPath(g2, (vg_PathProperties)ob);
			  else if (ob instanceof ga_RectangleProperties) ff_SVGDrawAWT.drawRect(g2, (ga_RectangleProperties)ob);
			  else if (ob instanceof ga_EllipseProperties) ff_SVGDrawAWT.drawEllipse(g2, (ga_EllipseProperties)ob);
			  else if (ob instanceof ga_LineProperties) ff_SVGDrawAWT.drawLine(g2, (ga_LineProperties)ob);
			  else if (ob instanceof ff_SVGImage) ff_SVGDrawAWT.drawImage(g2, (ff_SVGImage)ob);
			  else if (ob instanceof ff_SVGTextStyle) ff_SVGDrawAWT.drawText(g2, (ff_SVGTextStyle)ob);
			  else if (ob instanceof ga_CircleProperties) ff_SVGDrawAWT.drawCircle(g2, (ga_CircleProperties)ob);
			  } //for pr
		  }
	  }
}
