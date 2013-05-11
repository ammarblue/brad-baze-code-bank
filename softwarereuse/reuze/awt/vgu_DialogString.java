package reuze.awt;
//import org.mozilla.javascript.Context;
//import org.mozilla.javascript.Scriptable;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
/*
 * A#  multi-line text box with horizontal and vertical scroll bars, may need to precede with 2Q#
 * B#  multi-line text box with only horizontal scroll bar, may need to precede with 2Q#
 * nameC#nameC#... multi-selection checkbox list   call getResult(0)+getResult(1)... to get 0000100 string
 * G#nameD#nameD#... radio button list   call getResult(0)+getResult(1)... to get 0000100 string
 * colorNameE# color=white,red,blue,green,black,lightGray,gray,darkGray,pink,orange,yellow,magenta,cyan,brown
 * fontName-styleNumber-SizeNumberF# font=...
 * colorNameH# fore=...
 * J# font=default font
 * name,name,...,name,K#   list box with scroll bar, name chosen is returned by getResult
 * stringL# back=color fore=fore font=font
 * M# color=background color
 * P# add a new Panel to contain following items
 * digitQ# sets grid layout with digit rows (1-9) and 1 column
 * R# allow the dialog to be resized by the user
 * name,name,...,name,S#   drop down list box, name chosen is returned by getResult
 * numberT# text entry, single line, entry is returned by getResult
 * digitU# file selection dialog, digit=0 load, digit=1 save, getResult returns the absolute path
 * letterV# display a font drop-down list of font names beginning with letter or higher
 * stringW# add a button, getResult will return 0,1,.. based on selection, default button is OK
 * number-stringZ# define number x number image using letters a-z to select colors, draws as background
 */

public class vgu_DialogString extends Dialog implements /*AdjustmentListener,*/ActionListener,ComponentListener,FocusListener,MouseListener,MouseMotionListener
{
	private Button but[]=new Button[20]; int bux=0;
	private boolean which,sizable=false;
	private Color color,fore;
	private Font font;
	private String pm=null,result=null;
	private TextField[] tf=null; int tfx=0;
	private TextArea ta[]=null; int tax=0;
	private Checkbox[] cb=null; int cbx=0;
	private CheckboxGroup[] cbg=null; int cbgx=0;
	private Choice[] ch=null; int chx=0;
	private List[] li=null; int lix=0;
	private Panel pa[]=new Panel[3]; int pax=0;
	private Scrollbar sb[]=null; int sbx=0;
	private int mouseX,mouseY;
	private Graphics gg=null;
	private boolean selection=false;
	private Image smiley[]=null; int imx=0;
	private Component smileyo[]=null;
	private Component lastContainer=null;
	private static String[] fontNames=null;
	Frame f=null;
	//private Context cx=null;
	//private Scriptable scope=null;
	//private String sss=null;
	//public void setJava(String s) {
	//  sss=s;
	//}
	/*public void adjustmentValueChanged(AdjustmentEvent ae) {
      try {
        if (cx==null) {
          cx = Context.enter();
          scope = cx.initStandardObjects(null);
        }
      } catch (Exception e) {cx=null; return;}
      try {
        if ((cx!=null)&&(sss!=null))
          cx.evaluateString(scope, sss, "<cmd>", 1, null);
      } catch(Exception ee) {System.out.println(ee);}
    }*/ 
	public void componentHidden(ComponentEvent e) {
	}
	public void componentShown(ComponentEvent e) {
		if (getSize().height<80) setSize(230,260);
		repaint();
	}
	public void componentMoved(ComponentEvent e) {
		repaint();
	}
	int width=230,height=260;
	public void setSize(int w, int h) {
		width=w; height=h;
		super.setSize(w, h);
	}
	public void componentResized(ComponentEvent e) { 
		if (getSize().height<80) setSize(230,260);
		else if (!sizable) {
			if (getSize().height<230) setSize(230,260);
			else setSize(width,height);
		}
		repaint();
	}
	public void focusGained(java.awt.event.FocusEvent e){
		((TextField)(e.getComponent())).selectAll();
	}
	public void focusLost(java.awt.event.FocusEvent e){}

	public void clearSelection() {
		selection=false;
	}

	public void setSelection() {
		selection=true;
	}

	public void deleteSelection() {
		gg.clearRect(0,0,1024,1024);
		System.gc();
		f.repaint();
		selection=false;
	}

	private void checkPoint(int x,int y) {
	}

	public void mouseClicked(java.awt.event.MouseEvent evt) {}
	public void mouseExited(java.awt.event.MouseEvent evt) {}
	public void mouseEntered(java.awt.event.MouseEvent evt) {}
	//public boolean mouseDown(java.awt.Event evt, int x, int y) {
	public void mousePressed(java.awt.event.MouseEvent evt) {
		int x=evt.getX()-4; int y=evt.getY()-3;
		mouseX=x;  mouseY=y;
	}

	//public boolean mouseDrag(java.awt.Event evt, int x, int y) {
	public void mouseMoved(java.awt.event.MouseEvent evt) {}
	public void mouseDragged(java.awt.event.MouseEvent evt) {
		int x=evt.getX()-4; int y=evt.getY()-3;
		if (selection) {
			checkPoint(x,y);
		}
		mouseX=x;  mouseY=y;
	}

	//public boolean mouseUp(java.awt.Event evt, int x, int y) {
	public void mouseReleased(java.awt.event.MouseEvent evt) {
	}

	public void clearv() {
		selection=false;
		f.repaint();
	}

	public static Color ctran[]={Color.white,Color.red,     Color.blue,Color.green, Color.black, Color.lightGray,
		                  Color.gray, Color.darkGray,Color.pink,Color.orange,Color.yellow,Color.magenta,
		                  Color.cyan,new Color(132,92,79),new Color(128,0,0),new Color(0,128,128),new Color(0,128,0),new Color(0,0,139),
		                  new Color(128,0,128),new Color(128,128,0),new Color(255,127,80),new Color(160,82,45),new Color(255,215,0),new Color(0,191,255),
		                  new Color(255,69,0),new Color(255,228,196)};
	static String cntran=
"00white01red02blue03green04black05lightGray06gray07darkGray08pink09orange10yellow11magenta12cyan13brown14darkRed15teal16darkGreen17darkBlue18violet19darkYellow20coral21sienna22gold23skyBlue24orangeRed25bisque";
	//                   a b c d e f g h i j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z
	static int cltran[]={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
	private Color color(String s) {
		int i;
		i=cntran.indexOf(s);
		if (i<=0) return Color.white;
		i=Integer.parseInt(cntran.substring(i-2,i));
		return ctran[i];	
	}
	public Object getHandle(String type, int index) {
		if (type.equalsIgnoreCase("textarea")) return ta[index];
		if (type.equalsIgnoreCase("button")) return but[index];
		if (type.equalsIgnoreCase("window")) return this;
		if (type.equalsIgnoreCase("scroll")) return sb[index];
		if (type.equalsIgnoreCase("text")) return tf[index];
		return null;
	}
	static Frame x;
	static Frame y() {if (x==null) x=new Frame(); return x;}
	public vgu_DialogString(String title, String params) {
		super(y(),title,true);
		FlowLayout fl=new FlowLayout(FlowLayout.CENTER);
		fl.setHgap(0);
		init((Frame)(this.getOwner()),title,params,new Panel(fl));
	}
	vgu_DialogString(Frame dw, String title, String params,Panel p) {
		super(dw, title, true);
		init(dw,title,params,p);
	}
	vgu_DialogString(Frame dw, String title, String params) {
		super(dw, title, true);
		FlowLayout fl=new FlowLayout(FlowLayout.CENTER);
		fl.setHgap(0);
		init(dw,title,params,new Panel(fl));
	}
	private FileDialog df=null;
	private static String currentDir=null,currentFile=null;

	private void init(Frame dw, String title, String params, Panel p) { 
		int i; pm=params; f=dw; Color bg; Font oldFont;
		color=dw.getBackground();  bg=color;
		font=dw.getFont(); oldFont=font;
		fore=dw.getForeground();
		p.setBackground(color);
		lastContainer=p; 
		while (params.length()>0) {
			f.setFont(oldFont);
			i=params.indexOf("#"); int j,k,prev;
			if ((i>0)&&(params.charAt(i-1)=='L')) {
				Label l=new Label(params.substring(0,i-1));
				l.setBackground(color);
				l.setForeground(fore);
				l.setFont(font);
				p.add(l);
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='U')) {
				if (params.charAt(i-2)=='1') df = new FileDialog(dw,"Select file:",FileDialog.SAVE);
				else df = new FileDialog(dw,"Select file:",FileDialog.LOAD);
				if (currentDir!=null) df.setDirectory(currentDir);
				if (currentFile!=null) df.setFile(currentFile);
				return;
			/*} else if ((i>0)&&(params.charAt(i-1)=='N')) {
				if (sb==null) sb=new Scrollbar[3];
				if (params.charAt(i-2)=='1') sb[sbx] = new Scrollbar(Scrollbar.VERTICAL,50,16,0,1000);
				else sb[sbx] = new Scrollbar(Scrollbar.HORIZONTAL,50,16,0,1000);
				sb[sbx].setBackground(color);
				sb[sbx].setForeground(fore);
				sb[sbx].setSize(32, getWidth());
				//sb[sbx].addAdjustmentListener(this);
				p.add(sb[sbx]);
				sbx++; params=params.substring(i+1);*/
			} else if ((i>0)&&(params.charAt(i-1)=='W')) {
				but[bux] = new Button(params.substring(0,i-1));
				but[bux].setBackground(color);
				but[bux].setFont(font);
				but[bux].setForeground(fore);
				but[bux].addActionListener(this);
				lastContainer=but[bux];
				bux++; params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='R')) {
				sizable=true;
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='V')) {
				if (ch==null) ch=new Choice[3];
				if (fontNames==null) {
					fontNames = Toolkit.getDefaultToolkit().getFontList();
				}
				char c=params.charAt(i-2);
				c=Character.toUpperCase(c);
				Choice tt=new Choice();
				tt.setBackground(color);
				tt.setForeground(fore);
				tt.setFont(font);
				ch[chx]=tt; chx++;
				int x=0;
				for (j=0;j<fontNames.length;j++) {
					if (fontNames[j].charAt(0)>=c) {
					  tt.add(fontNames[j]);
					  x++;
					  if (x>50) break;
					}
				}
				p.add(tt);
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='F')) {
				String s=params.substring(0,i-1);
				j=s.indexOf("-");
				prev=s.indexOf("-",j+1);
				try {
					k=Integer.parseInt(s.substring(j+1,prev));
				} catch (NumberFormatException e) {
					k=0;
				}
				try {
					prev=Integer.parseInt(s.substring(prev+1));
				} catch (NumberFormatException e) {
					prev=12;
				}
				font=new Font(s.substring(0,j),k,prev); 
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='P')) {
				add(p); p=new Panel();
				p.setBackground(color);
				lastContainer=p; 
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='Q')) {
				add(p); p=new Panel(new GridLayout(Character.digit(params.charAt(i-2),10),1));
				p.setBackground(color); 
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='J')) {
				font=oldFont;
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='C')) {
				if (cb==null) cb=new Checkbox[23];
				Checkbox t=new Checkbox(params.substring(0,i-1));
				t.setBackground(color);
				t.setForeground(fore);
				t.setFont(font); 
				cb[cbx]=t;   cbx++;
				p.add(t);
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='D')&&(cbgx>0)) {
				if (cb==null) cb=new Checkbox[23];
				Checkbox t=new Checkbox(params.substring(0,i-1),cbg[cbgx-1],false);
				t.setBackground(color);
				t.setForeground(fore);
				t.setFont(font); 
				cb[cbx]=t;   cbx++;
				p.add("Center",t);
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='G')) {
				if (cbg==null) cbg=new CheckboxGroup[3];
				cbg[cbgx]=new CheckboxGroup();   cbgx++;
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='Z')) {
				String s=params.substring(0,i-1);
				j=s.indexOf("-");
				try {
					k=Integer.parseInt(s.substring(0,j));
				} catch (NumberFormatException e) {
					k=0;
				} System.out.println(k);
				int imageData[]=new int[k*k];
				int m=k*k,n=0;
				for (prev=j+1;m>0; m--,prev++,n++) imageData[n]=ctran[cltran[s.charAt(prev)-'a']].getRGB();
				if (smiley==null) {
					smiley=new Image[16];
					smileyo=new Component[16];
				}
				smiley[imx] = createImage(
						new MemoryImageSource(k, k, imageData, 0, k));
				smileyo[imx++]=lastContainer;
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='S')) {
				if (ch==null) ch=new Choice[3];
				Choice t=new Choice();
				t.setBackground(color);
				t.setForeground(fore);
				t.setFont(font);
				ch[chx]=t; chx++;
				String s=params.substring(0,i-1); prev=0;
				for (;;) {
					j=s.indexOf(',',prev);
					if (j<=0) break;
					t.add(s.substring(prev,j));
					prev=j+1;
				};
				p.add(t);
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='K')) {
				if (li==null) li=new List[3];
				List t=new List(4);
				t.setBackground(color);
				t.setForeground(fore);
				t.setFont(font);
				li[lix]=t; lix++;
				String s=params.substring(0,i-1); prev=0;
				for (;;) {
					j=s.indexOf(',',prev);
					if (j<=0) break;
					t.add(s.substring(prev,j));
					prev=j+1;
				};
				p.add(t);
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='E')) {
				color=color(params.substring(0,i-1));
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='M')) {
				color=bg;
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='H')) {
				fore=color(params.substring(0,i-1));
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='T')) {
				if (tf==null) tf=new TextField[15];
				if (i>1&&params.charAt(i-2)=='*') j=i-2; else j=i-1;
				String s=params.substring(0,j);
				try {
					j=Integer.parseInt(s);
					s="";
				} catch (NumberFormatException e) {
					j=s.length();
				};
				TextField t=new TextField(s,Math.min(j,18));
				if (i>1&&params.charAt(i-2)=='*') t.setEchoChar('*');
				t.setBackground(color);
				t.setForeground(fore);
				t.setFont(font);
				tf[tfx]=t; tfx++;
				p.add(t);
				t.addFocusListener(this);
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='A')) {
				if (ta==null) ta=new TextArea[3];
				ta[tax]=new TextArea(params.substring(0,i-1),8,28,TextArea.SCROLLBARS_BOTH);
				ta[tax].setBackground(color);
				ta[tax].setForeground(fore);
				ta[tax].setFont(font); 
				p.add(ta[tax]); tax++;
				params=params.substring(i+1);
			} else if ((i>0)&&(params.charAt(i-1)=='B')) {
				if (ta==null) ta=new TextArea[3];
				ta[tax]=new TextArea(params.substring(0,i-1),2,29,TextArea.SCROLLBARS_HORIZONTAL_ONLY);
				ta[tax].setBackground(color);
				ta[tax].setForeground(fore);
				ta[tax].setFont(font); 
				p.add(ta[tax]); tax++;
				params=params.substring(i+1);
			};
		};
		add("Center",p);
		//FlowLayout fl=new FlowLayout(FlowLayout.CENTER);
		//fl.setHgap(0);
		//Panel panel = new Panel(fl);
		//panel.setBackground(color);
		addComponentListener(this);
		p.addMouseListener(this);
		p.addMouseMotionListener(this);
		if (bux==0) {
			but[0] = new Button("OK");
			but[0].addActionListener(this); bux=1;
		}
		but[0].setBackground(color);
		but[0].setForeground(fore);
		but[0].setFont(font);
		for (i=0; i<bux; i++) p/*anel*/.add("Center",but[i]);
		//add("South",panel);
		addNotify();
	}

	public void setVisible(boolean b) {
		if (df!=null) {
			df.setVisible(true);
			if ((df.getFile() != null) && (df.getFile().length()>0)) {
				currentDir=df.getDirectory(); currentFile=df.getFile(); 
				result=(df.getDirectory()+df.getFile()).replace('#','\b')+'#';
				which=true;
			} else result=null;
			df=null;
			return;
		}
		if (tfx>0) tf[0].selectAll();
		super.setVisible(true);
	}

	public void paint(java.awt.Graphics g) {
		if (smiley!=null) {int i;
		for (i=0; i<imx; i++) {
			Graphics gg=smileyo[i].getGraphics();
			gg.drawImage(smiley[i], 0, 0, smileyo[i].getSize().width, smileyo[i].getSize().height, this);
		}
		}
		/*if (pax>0) {Graphics gg=((Panel)pa[0]).getGraphics();
        int i,j=gv.size();
        boolean b=j>0 && gv.elementAt(0) instanceof Polygon;
        for (i=0; i<j; i++) {
          Object o=gv.elementAt(i);
          if (b) {
            if (bbd!=null&&bbd.get(i)) gg.setColor(Color.blue); else gg.setColor(Color.black);
            gg.drawPolyline(((Polygon)o).xpoints,((Polygon)o).ypoints,((Polygon)o).npoints);
          } else {
            if (o instanceof Polygon) {gg.drawPolygon((Polygon)o);}
            else if (o instanceof Color) gg.setColor((Color)o);
            else if (o instanceof Font) gg.setFont((Font)o);
            //else if (o instanceof rpcFilledRectangle) ((rpcFilledRectangle)o).paint(gg);
            //else if (o instanceof rpcPolyline) ((rpcPolyline)o).paint(gg);
            //else if (o instanceof rpcFilledPolygon) ((rpcFilledPolygon)o).paint(gg);
            //else if (o instanceof rpcText) ((rpcText)o).paint(gg);
          }
        }
      }*/
	}

	public void actionPerformed(ActionEvent e) {
		int j,butt=0; String s;
		for (j=0; j<bux; j++) {
			if (e.getSource()==but[j]) butt=j+1;
		}
		if (bux>1) {
			s=but[1].getLabel();
			if (s!=null && s.equalsIgnoreCase("cancel")&&butt==2) butt=0;
		}
		if (butt>0) {
			result="";   which=true; int sbi=0,tfi=0,cbi=0,chi=0,bui=0,lii=0;
			String m=pm;
			while (m.length()>0) {
				int i=m.indexOf("#");
				if (i>=0){
					if (m.charAt(i-1)=='S' || m.charAt(i-1)=='V') { 
						j=ch[chi].getSelectedIndex();
						if (j>=0) result+=ch[chi].getItem(j); chi++;
						result+="#";
					} else if (m.charAt(i-1)=='K') {
						j=li[lii].getSelectedIndex();
						if (j>=0) result+=li[lii].getItem(j); lii++;
						result+="#";
					} else if (m.charAt(i-1)=='N') { 
						result+=sb[sbi].getValue()+"#"; sbi++;
					} else if (m.charAt(i-1)=='W') { 
						result+=((butt==++bui)?"1":"0")+"#";
					} else if (m.charAt(i-1)=='T') {
						s=tf[tfi++].getText().replace('#','\b');
						result+=s+"#";
					} else if ((m.charAt(i-1)=='A')||(m.charAt(i-1)=='B')) {
						s=ta[0].getText().replace('#','\b'); 
						result+=s+"#";
					} else if ((m.charAt(i-1)=='C')||(m.charAt(i-1)=='D') ) 
						result+=(cb[cbi++].getState()?"1":"0")+"#"; 
					m=m.substring(i+1);
				} else break;
			}
		} else {
			which=false;
		}
		dispose();
	}

	public String getResult(int i) {
		if (which) {
			if (result.length()==0) return result;
			else {int j=0,k,prev=0;
			for (;;j++) {
				k=result.indexOf("#",prev);
				if (k<0) return null;
				if (j>=i) break;
				prev=k+1;
			};
			return result.substring(prev,k).replace('\b','#');
			}
		}
		return null;
	}
	public String getState() {
		if (which) {
			if (result.length()==0) return "[]";
			String r="[";
			for (int i=0;;i++) {int j=0,k,prev=0; 
			for (;;j++) {
				k=result.indexOf("#",prev);
				if (k<0)  return r+"]";
				if (j>=i) break;
				prev=k+1;
			}
			r = r+",\""+result.substring(prev,k).replace('\b','#')+"\"";
			}
		}
		return "[]";
	}
	public Object getResult(int i, int j) {
		if (!which) return null;
		if (j==0) return getResult(i);
		return null;
	}  
}
