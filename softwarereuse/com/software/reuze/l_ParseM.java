package com.software.reuze;
import java.util.Vector;
import java.lang.Double;
import java.lang.reflect.*;
import java.util.Enumeration;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

import com.software.reuze.l_ParseM.Str;

/*ANDROID
import android.content.Intent;
public static Intent result=null;
ANDROID*/

public final class l_ParseM {

public static final int M_FAIL=-1; //anything <0 is an error

public static final int M_NOERR=0;

/*ANDROID
public static class Cmplx extends M {
	 public Complex x;
	 public Cmplx(double r, double i) {x=new Complex(r,i);}
	 public String toString() {return x.toString(); }
	 public void copy(M y) {((MTYPE)y).x=new Cmplx(x.x,x.y);}
	 public int intValue() {throw new 
		 java.lang.ClassCastException();}
	 public double realValue() {throw new 
		 java.lang.ClassCastException();}
	 public int invert() {x=new Complex(-x.x,-x.y); return M_NOERR;}
	 public int multiply(M y) { x=x.multiply( ((Cmplx)((MTYPE)y).x).x); return M_NOERR;}
	 public int divide(M y) {x=x.divide( ((Cmplx)((MTYPE)y).x).x); return M_NOERR;}
	 //public int mod(M y) {return M_FAIL;}
	 public int pow(M y) {x=x.pow( ((Cmplx)((MTYPE)y).x).x); return M_NOERR;}
	 public int add(M y) {
		 x=x.add( ((Cmplx)((MTYPE)y).x).x); return M_NOERR;
		}
	 public int subtract(M y) { x=x.subtract( ((Cmplx)((MTYPE)y).x).x); return M_NOERR;}
	 public boolean isTrue() {return x.x!=0.0||x.y!=0.0;}
	 public int compare(M y) {
	    return (x.compareTo( ((Cmplx)((MTYPE)y).x).x));
	 }
	}


public static class Unit extends M {

 public UnitFraction x;

 public Unit(UnitFraction y) {x=new UnitFraction(y);}

 public Unit(String y) {x=new UnitFraction(y);}

 public String toString() {return x.toString(); }

 public void copy(M y) {((MTYPE)y).x=new Unit(x);}

 public int invert() {x.value=-x.value; return M_NOERR;}

 public int multiply(M y) {

     if ((((MTYPE)y).x instanceof Int)||(((MTYPE)y).x instanceof Real)) x.value*=y.realValue();

     else x.multiply( ((Unit)((MTYPE)y).x).x); return M_NOERR;}

 public int divide(M y) {

     if ((((MTYPE)y).x instanceof Int)||(((MTYPE)y).x instanceof Real)) x.value/=y.realValue();

     else x.divide( ((Unit)((MTYPE)y).x).x); return M_NOERR;}
 public int pow(M y) {
	 x=x.pow(new UnitFraction(y.realValue()));
	 return M_NOERR;
 }
 public int add(M y) {

     if ((((MTYPE)y).x instanceof Int)||(((MTYPE)y).x instanceof Real)) x.value+=y.realValue();

     else x.add( ((Unit)((MTYPE)y).x).x); return M_NOERR;}

 public int subtract(M y) {

     if ((((MTYPE)y).x instanceof Int)||(((MTYPE)y).x instanceof Real)) x.value-=y.realValue();

     else x.subtract( ((Unit)((MTYPE)y).x).x); return M_NOERR;}

 public boolean isTrue() {return x.value!=0.0;}

 public int compare(M y) {

    int i=x.compareTo( ((Unit)((MTYPE)y).x).x);

    if (i==0) return 0;

    if (i>0) return 1;

    return 2;

 }

}
ANDROID*/

public static int strpbrk(StringBuffer s, int ii, String set)

{int i=0,j=s.length(),k,m=set.length();

  if (m>0) 

  while (ii<j) {

    k=0;

    while ((k<m)&&(set.charAt(k)!=s.charAt(ii))) k++;

    if (k>=m) break;

    ii++; i++;

  }

  return i;

}



public static class M extends Object {

  public void clear() {}

  public boolean equals(Object obj) {return compare((M)obj)==0;}

  public String toString() {return "null";}

  public void copy(M y) {}

  public void move(M y) {}

  public int intValue() {return 0;}

  public double realValue() {return Double.NaN;}

  public int invert() {return M_FAIL;}

  public int multiply(M y){return M_FAIL;}

  public int divide(M y) {return M_FAIL;}
  public int pow(M y) {return M_FAIL;}
  public int mod(M y) {return M_FAIL;}

  public int add(M y) {return M_FAIL;}

  public int subtract(M y) {return M_FAIL;}

  public int and(M y) {return M_FAIL;}

  public int or(M y) {return M_FAIL;}

  public int shift(M y,int right) {return M_FAIL;}

  public boolean isTrue() {return false;}

  public int compare(M y) {return M_FAIL;}

  public Object index(M y) {return null;}

  public int fromString(StringBuffer s, Int ii) {return M_FAIL;}

  public int length() {return -1;}

}



public static class MTYPE extends M implements Comparable {

  public M x;
  public MTYPE() {
  }
  public MTYPE(M m) {
	  x=m;
  }
  public void clear() {x=null;}

  public String toString() {

    if (x==null) return "null";

    return x.toString();

  }
  public int compareTo(Object y) {
	  int i=x.compare((MTYPE)y);
	  if (i==2) return -1;
	  return i;
  }
  public boolean isNull() {return x==null;}

  public void copy(M y) {if (x==null) ((MTYPE)y).x=null; else x.copy(y);}

  public void move(M y) {((MTYPE)y).x=x; x=null;}

  public int intValue() {return x.intValue();}

  public double realValue() {return x.realValue();}

  public int invert() {if (x==null) return M_FAIL; return x.invert();}

  public int multiply(M y){if (x==null) return M_FAIL; return x.multiply(y);}

  public int divide(M y) {if (x==null) return M_FAIL; return x.divide(y);}
  public int pow(M y) {if (x==null) return M_FAIL; return x.pow(y);}
  public int mod(M y) {if (x==null) return M_FAIL; return x.mod(y);}

  public int add(M y) {if (x==null) return M_FAIL; return x.add(y);}

  public int subtract(M y) {if (x==null) return M_FAIL; return x.subtract(y);}

  public int and(M y) {if (x==null) return M_FAIL; return x.and(y);}

  public int or(M y) {if (x==null) return M_FAIL; return x.or(y);}

  public int shift(M y, int right) {if (x==null) return M_FAIL; return x.shift(y,right);}

  public boolean isTrue() {if (x==null) return false; return x.isTrue();}

  public int compare(M y) {if (x==null) return M_FAIL; return x.compare(y);}

  public Object index(M y) {

    if (x==null) return null;

    if (x instanceof Array) return x.index(y);
    else if (x instanceof Obj) return x.index(y);
    else return null;

  }

  public int length() {

    if (x==null) return -1;

    return x.length();

  }

  public int fromString(StringBuffer s, Int ii) {

    int j=ii.intValue();

    int i,k=0;

    x=null;

    i=strpbrk(s,j,"\n\r\t ");

    if (i>0) {j+=i; ii.set(j);} 

    if (s.charAt(j)=='[') {MTYPE y;

      x=new Array(); ii.inc();

      if (s.charAt(j+1)==']') {ii.inc(); return M_NOERR;}

      for (;;) {

        y=new MTYPE(); i=y.fromString(s,ii);

        if (i!=M_NOERR) {x.clear(); x=null; return i;}

        ((Array)x).extend(y);

        if (s.charAt(ii.intValue())==']') {ii.inc(); return M_NOERR;}

        if (s.charAt(ii.intValue())!=',') {x.clear(); x=null; return M_FAIL;}

        ii.inc();

      }

    }

    if ((s.charAt(j)=='"')||(s.charAt(j)=='\'')) {int b=1; char sch=s.charAt(j);

      k=s.length(); i=j+1;

      while (i<k) {
    	  if (s.charAt(i-1)=='\\' && s.charAt(i)=='\\') {
    		  i++;
    		  if (s.charAt(i)==sch) break;
    		  b=0; i++;
    		  continue;
    	  }
    	  if (((s.charAt(i-1)=='\\')&&(s.charAt(i)==sch))||(s.charAt(i)!=sch)) {
    		  if ((b==1)&&(s.charAt(i)=='\\')) b=0;
    		  i++;
    	  } else break;
      }

      if (i>=k) return M_FAIL;

      String st=s.toString().substring(j+1,i);

      if (b==1) x=new Str(st);

      else {

        String ss;

        j=0;  k=st.length(); ss="";

        while (j<k) {

          b=st.indexOf("\\",j);

          if (b<0) {ss+=st.substring(j); break;}

          ss+=st.substring(j,b);

          j=b+1;

          if (st.charAt(j)=='n') {ss+='\n';}
          else if (st.charAt(j)=='t') {ss+='\t';}
          else if (st.charAt(j)=='r') {ss+='\r';}
          else if (st.charAt(j)=='\'') {ss+='\'';}
          else if (st.charAt(j)=='"') {ss+='"';}
          else if (st.charAt(j)=='\\') {ss+='\\';}
        else ss+=st.charAt(j);

        j++;

        }

      x=new Str(ss);

      }

      ii.set(i+1);

      return M_NOERR;

    }

    if ((s.charAt(j)=='+')||(s.charAt(j)=='-')) k=1;

    i=strpbrk(s,j+k,"01232456789");

    if (i==0) return M_FAIL;

    int base=10;

    i+=k;

    if (s.charAt(j+k)=='0') {

        base=8;

        if ((s.charAt(j+k+1)=='x')||(s.charAt(j+k+1)=='x')) {

            j+=k+2;

            i=strpbrk(s,j,"01232456789abcdefABCDEF");

            if (i==0) return M_FAIL;

            x=new Int(Integer.parseInt(s.toString().substring(j,j+i),16));  ii.set(j+i); 

            return M_NOERR;

        }

    }

    if (s.charAt(j+i)=='.') {

      i++;

      k=strpbrk(s,j+i,"01232456789");

      if (k==0) return M_FAIL;

      i+=k; k=0;

      if (s.charAt(j+i)=='e') {

        i++;

        if ((s.charAt(j+i)=='+')||(s.charAt(j+i)=='-')) i++;

        k=strpbrk(s,j+i,"01232456789");

        if (k==0) return M_FAIL;

      }
/*ANDROID
      if (Character.isUpperCase(s.charAt(j+i+k))) {

          int m=strpbrk(s,j+i+k,"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ^23");

          if (m<=0) return M_FAIL;

          x=new Unit(s.toString().substring(j,j+i+k+m)); j+=m;

      } else if (s.charAt(j+i+k)=='r') {
	 	  int m=s.indexOf("i",j+i+k);
    	  if (m<0) return M_FAIL;
    	  Complex z=Complex.parseComplex(s.toString().substring(j,m+1));  j=m; k=1; i=0;
    	  x=new Cmplx(z.x,z.y);
      } else if (s.charAt(j+i+k)=='i') {
    	  Complex z=Complex.parseComplex(s.toString().substring(j,j+i+k+1));  j++;
    	  x=new Cmplx(0.0,z.y);
      } else
ANDROID*/
    	  x=new Real(Double.parseDouble(s.toString().substring(j,j+i+k)));
      ii.set(j+i+k);
      return M_NOERR;

    }
/*ANDROID
    if (Character.isUpperCase(s.charAt(j+i))) {

          int m=strpbrk(s,j+i,"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ^23");

          if (m<=0) return M_FAIL;

          x=new Unit(s.toString().substring(j,j+i+k+m)); j+=m; base=10;

    } else
    if (s.charAt(j+i)=='r') {
      int m=s.indexOf("i",j+i);
  	  if (m<0) return M_FAIL;
  	  Complex z=Complex.parseComplex(s.toString().substring(j,m+1));  j=m+1; i=0;
	  x=new Cmplx(z.x,z.y);
    } else if (s.charAt(j+i)=='i') {
  	  Complex z=Complex.parseComplex(s.toString().substring(j,j+i+1));  j++;
  	  x=new Cmplx(0.0,z.y);
    } else 
ANDROID*/
          x=new Int(Integer.parseInt(s.toString().substring(j,j+i),base));
    ii.set(j+i); 
    return M_NOERR;

  }

}



public static class Array extends M {

public Vector x;

public Array() {x=new Vector();}

public void clear() {x.removeAllElements();}

public int add(M y) { MTYPE z;

    if (((MTYPE)y).x instanceof Array) {

        int i=((Array)( ((MTYPE)y) .x)).x.size(),j;

        for (j=0; j<i; j++) {Array a=(Array)((MTYPE)y).x;

            z=new MTYPE(); MTYPE o=(MTYPE)a.x.elementAt(j); o.copy(z);

            x.addElement(z);

        }

    } else { z=new MTYPE();

        y.move(z);

        x.addElement(z);

    }

    return M_NOERR;

}

public void copy(M y) {Array a;

  a=new Array(); ((MTYPE)y).x=a;

  int i=x.size(),j; 

  for (j=0;j<i;j++) {MTYPE m;

    m=new MTYPE(); ((MTYPE)x.elementAt(j)).copy(m);

    a.extend(m);

  }

}

public void extend(Object m) {x.addElement(m);}

public String toString() {return x.toString(); }

public Object index(M indices) {

  if (((MTYPE)indices).x instanceof Int) {

    Int y=(Int)(((MTYPE)indices).x);

    int i=y.x;

    if ((i>=0)&&(i<x.size())) {

      return x.elementAt(i);

    }

  }

  return null;

}

public int set(Object indices, Object value) {return M_FAIL;}

public int length() {return x.size();}

}



public static class Int extends M {

 public int x;

 public Int(int y) {x=y;}

 public Int(long y) {x=(int)y;}

 public Int(boolean b) {x=b?1:0;}

 public void set(int y) {x=y;}

 public int intValue() {return x;}

 public double realValue() {return (double)x;}

 public void inc() {x++;}

 public String toString() {return String.valueOf(x); }

 public void copy(M y) {((MTYPE)y).x=new Int(x);}

 public int invert() {x=-x; return M_NOERR;}

 public int multiply(M y) { x*=y.intValue(); return M_NOERR;}

 public int divide(M y) {x/=y.intValue(); return M_NOERR;}
 public int pow(M y) {x=(int)java.lang.Math.pow((double)x,y.realValue()); return M_NOERR;}
  public int mod(M y) {x%=y.intValue(); return M_NOERR;}

  public int add(M y) {x+=y.intValue(); return M_NOERR;}

  public int subtract(M y) {x-=y.intValue(); return M_NOERR;}

  public int and(M y) {x&=y.intValue(); return M_NOERR;}

  public int or(M y) {x|=y.intValue(); return M_NOERR;}

  public int shift(M y, int right) {

      if (right!=0) x>>=y.intValue(); else x<<=y.intValue();

      return M_NOERR;

  }

  public int compare(M y) {int j=y.intValue();

    if (x==j) return 0;

    if (x>j) return 1;

    return 2;

  }

  public boolean isTrue() {return x!=0;}

}



public static class Obj extends M {

  public Object x;

  public String name;

  public Obj(Object o, String nm) {x=o; name=nm;}

  public String toString() {return x.toString(); }

  public void copy(M y) {((MTYPE)y).x=new Obj(x,name);}
  public Object index(M indices) {
	  MTYPE o=new MTYPE();  o.x=null;
	  if (((MTYPE)indices).x instanceof Int) {
	  Class c=x.getClass();
	  if (c.isArray()) {
		  int i=java.lang.reflect.Array.getLength((Object)x);
		  int j=indices.intValue();
		  if (j<0||j>=i) return o;
		  Object obj=java.lang.reflect.Array.get((Object)x,j);
		  if (obj == null) ;
          else if (obj instanceof Double) o.x=new Real((Double) obj);

          else if (obj instanceof Integer) o.x=new Int((Integer) obj);

          else if (obj instanceof String) o.x=new Str((String) obj);

          else o.x=new Obj(obj,obj.getClass().getName());
	  }
	  }
	  return o;
  }
  public boolean isTrue() {if (x instanceof Boolean) return ((Boolean)x).booleanValue(); return false;}

}



public static class Real extends M {

 public double x;

 public Real(double y) {x=y;}

 public String toString() {return Double.toString(x); }

 public void copy(M y) {((MTYPE)y).x=new Real(x);}

 public int intValue() {return (int)x;}

 public double realValue() {return x;}

 public int invert() {x=-x; return M_NOERR;}

 public int multiply(M y) { x*=y.realValue(); return M_NOERR;}

 public int divide(M y) {x/=y.realValue(); return M_NOERR;}
 public int mod(M y) {x=java.lang.Math.IEEEremainder(x, y.realValue()); return M_NOERR;}
 public int pow(M y) {x=java.lang.Math.pow(x, y.realValue()); return M_NOERR;}
 public int add(M y) {x+=y.realValue(); return M_NOERR;}
 public int subtract(M y) {x-=y.realValue(); return M_NOERR;}

 public boolean isTrue() {return x!=0.0;}

 public int compare(M y) {double j=y.realValue();

    if (Math.abs(x-j)<=1.0e-12) return 0;

    if (x>j) return 1;

    return 2;

 }

}


public static class Str extends M {

 public String x;

 public Str(String y) {x=y;}

 public String toString() {return x; }

 public void copy(M y) {((MTYPE)y).x=new Str(new String(x));}

 public int add(M y) {x+=y.toString(); return M_NOERR;} //((Str)((MTYPE)y).x).x

 public int compare(M y) {

    int i=x.compareTo( ((Str)((MTYPE)y).x).x);

    return (i==0)?0:((i>0)?1:2);

 }

 public int length() {return x.length();}

}



private static boolean sequals(StringBuffer s, int offset, String ss) {

  int i,j,k;  i=s.length(); j=ss.length();

  if ((offset+j)>i) return false;

  for (k=0;j>0;offset++,k++,j--) {

    if (s.charAt(offset)!=ss.charAt(k)) return false;

  }

  return true;

}



private static int convert(Object o, MTYPE x) {

          if (o==null) return M_FAIL;

          if (o instanceof Double)

            x.x=new Real(((Double)o).doubleValue());

          else if (o instanceof Integer)

            x.x=new Int(((Integer)o).intValue());

          else if (o instanceof String)

            x.x=new Str((String)o);

          else return M_FAIL;

          return M_NOERR;

}



public static M[] args(MTYPE x[], StringBuffer s, Int ii) {

int m,failure; M n[]=new M[10];

          if (s.charAt(ii.intValue())==')') {

            m=-1;  ii.inc();

          } else

          for (m=0;;m++) {

            failure = expr (x,s,ii);

            if ( failure!=M_NOERR ) return null;

            n[m]=x[0].x;

            if (s.charAt(ii.intValue())==',') ii.inc();
            else if (s.charAt(ii.intValue())==' ') ii.inc();
            else if (s.charAt(ii.intValue())=='\t') ii.inc();
            else if (s.charAt(ii.intValue())=='\n') ii.inc();
            else if (s.charAt(ii.intValue())=='\r') ii.inc();
            else if (s.charAt(ii.intValue())!=')') return null;
            else {ii.inc(); break;}
          }

          n[9]=new Int(m+1);

          return n;

}



public static int factor(MTYPE x[], StringBuffer s, Int ii) 

{   int failure=M_FAIL;  int k,j=0,m=0,r,i=ii.intValue(); MTYPE y;
    if ( (s.charAt(i)=='+')||(s.charAt(i)=='-')) {
      if ((s.charAt(i+1)>='0') && (s.charAt(i+1) <= '9')) failure=x[0].fromString(s, ii);
      else failure=expr(x,s,ii);
    } else if ( (s.charAt(i)=='"')||
         (s.charAt(i)=='\'')||((s.charAt(i)>='0') && (s.charAt(i) <= '9')) ) {
      failure=x[0].fromString(s, ii);

    } else if (s.charAt(i)=='[') {

        ii.inc(); y=new MTYPE(); y.x=new Array();

        if (s.charAt(i+1)==']') {ii.inc(); x[0]=y; return M_NOERR;}

        for (;;) {

          x[0]=new MTYPE();

          failure = expr (x,s,ii);

          if ( failure!=M_NOERR ) return failure;

          ((Array)y.x).extend(x[0]);

          if (s.charAt(ii.intValue())==',') ii.inc();
          else if (s.charAt(ii.intValue())==' ') ii.inc();
          else if (s.charAt(ii.intValue())=='\t') ii.inc();
          else if (s.charAt(ii.intValue())=='\n') ii.inc();
          else if (s.charAt(ii.intValue())=='\r') ii.inc();
          else if (s.charAt(ii.intValue())==']') {

            ii.inc();

            x[0]=y;

            return M_NOERR;

          }

        }

    } else if ( ( (s.charAt(i)>='a') && (s.charAt(i) <= 'z') )|| 

        ( (s.charAt(i)>='A') && (s.charAt(i) <= 'Z') && (s.charAt(i+1)=='.')) ) {

      if ((s.charAt(i)=='n')&&(s.charAt(i+1)=='u')&&(s.charAt(i+2)=='l')&&(s.charAt(i+3)=='l')) { /*null*/

          ii.set(i+4);

          x[0].x=null;

          return M_NOERR;

      }

      r=Character.toLowerCase(s.charAt(i))-'a'+1;

      MTYPE z;//=new MTYPE();

      z=x[r]; //x[r].copy(z);

      String ss=null;

    if (s.charAt(i+1)=='.') { 

        if (Character.isUpperCase(s.charAt(i))) {

            if (!(z.x instanceof Str)) return M_FAIL; 

            ss=((Str)z.x).x;

        }

    }

    ii.inc();

    i=ii.intValue();

    for(;;) {

      if (s.charAt(i)=='.') { M n[]=null;

        ii.inc();

        j=strpbrk(s,i+1,"abcdefghijklmnopqrstuvwxyz_0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        if (j<=0) return M_FAIL;

        Object obj=null;
        k=i+1;  i+=j+1; ii.set(i);
        String methodName = s.toString().substring(k,k+j);
        if (ss==null)

       	    if (z.x instanceof Obj) {

                ss=((Obj)z.x).name;

                obj=((Obj)z.x).x;

            } else {

                if (z.x instanceof Str) {ss="java.lang.String"; obj=((Str)z.x).x;}

                else if (z.x instanceof Array) {
                	ss="java.util.Vector"; obj=((Array)z.x).x;
                	if (s.charAt(i)=='(') {
                        ii.set(i+1);
                        n=args(x,s,ii);
                        if (n==null) return M_FAIL;
                        int xx=((Int)n[9]).x;
                        int xy=((Array)z.x).x.size();
                        if (methodName.equals("size")&&(xx==0)) {
                        	x[0].x=new Int(xy);  return M_NOERR;
                        }
                        if (methodName.equals("setSize")&&(xx==1)) {
                        	int xz=((Int)n[0]).x;
                        	((Array)z.x).x.setSize(xz);
                        	for(;xy<xz;xy++) {
                        		MTYPE p=new MTYPE();
                        		p.x=null;
                        		((Array)z.x).x.setElementAt(p,xy);
                        	}
                        	x[0].x=new Int(0);  return M_NOERR;
                        }
                	}
            	}
                else if (z.x instanceof Int) {ss="java.lang.Integer"; obj=((Int)z.x).x;}
                else if (z.x instanceof Real) {ss="java.lang.Double"; obj=((Real)z.x).x;}
/*ANDROID
                else if (z.x instanceof Unit) {ss="com.example.android.notepad2.UnitFraction"; obj=((Unit)z.x).x;}
                else if (z.x instanceof Cmplx) {ss="com.example.android.notepad2.Complex"; obj=((Cmplx)z.x).x;}
ANDROID*/
                else return M_FAIL;

            }
        try {
        Class  c = Class.forName(ss);

        if (s.charAt(i)=='(') {

          ii.set(i+1);

          n=args(x,s,ii);

          if (n==null) return M_FAIL;

        } else {
          if (methodName.compareTo("class")==0) {
        	  x[0].x=new Str(ss);
        	  return M_NOERR;
          }
          Field f=c.getDeclaredField(methodName);

          Object oo=f.get(obj);

          if (oo instanceof Double) x[0].x=new Real((Double) oo);

          else if (oo instanceof Integer) x[0].x=new Int((Integer) oo);

          else if (oo instanceof String) x[0].x=new Str((String) oo);

          else x[0].x=new Obj(oo,oo.getClass().getName());

          return M_NOERR;

        } 

            int xx=((Int)n[9]).x;

            Class [] c1=new Class[xx];

            Object [] ret=new Object[xx];

            Method me;
//TODO null as an argument does not work
//TODO implement .object to cast variable to Object
//TODO add stop statement to exit programs
            for (int u=0; u<xx; u++) {
                if (n[u] instanceof Real) {
                	/*if (ss.charAt(0)=='a') {c1[u]=float.class;ret[u]=(float)((Real)n[u]).x;} //most android interfaces are float
                	else*/ {c1[u]=double.class;ret[u]=((Real)n[u]).x;}
                }
                else if (n[u] instanceof Str) {c1[u]=String.class;ret[u]=((Str)n[u]).x;}
                else if (n[u] instanceof Int) {c1[u]=int.class;ret[u]=((Int)n[u]).x;}
/*ANDROID
                else if (n[u] instanceof Cmplx) {c1[u]=Complex.class;ret[u]=((Cmplx)n[u]).x;}
ANDROID*/
                else if (n[u] instanceof Array) {c1[u]=Vector.class;ret[u]=((Array)n[u]).x;}
                else if (n[u] instanceof Obj) {
                	ret[u]=((Obj)n[u]).x;c1[u]=Class.forName(((Obj)n[u]).name);
                	if (((Obj)n[u]).name.compareTo("java.lang.Long")==0) c1[u]=long.class;
                	else if (((Obj)n[u]).name.compareTo("java.lang.Float")==0) c1[u]=float.class;
                	} //otherwise cast doesn't work
                else return M_FAIL;

            }

            if (methodName.equals("new")) {
/*ANDROID
              if ((xx==3)&&(ss.compareToIgnoreCase("com.example.android.notepad2.Matrix")==0)) {
            	  if (ret[0] instanceof Vector) {
            		  x[0].x=new Obj(new Matrix((Vector)ret[0],(Integer)ret[1],(Integer)ret[2]),ss);
            		  return M_NOERR;
            	  }
              }
ANDROID*/
              Constructor c2=c.getConstructor(c1);
              x[0].x = new Obj(c2.newInstance(ret),ss);
              return M_NOERR;
            }
            if (methodName.equals("cast")) {
            	String s1=(String)ret[0];
                if (s1.charAt(0)=='[') {
                	if (obj instanceof Vector) {
                		int t=((Vector)obj).size();
                		if (s1.compareTo("[]float")==0) {
                			float xf[]=new float[t];
                			while (--t>=0) {
                				xf[t]=(float)((M)((Vector)obj).elementAt(t)).realValue();
                			}
                			x[0].x=new Obj(xf,xf.getClass().getName());
            			} else return M_FAIL;
                		return M_NOERR;
                	}
                	return M_FAIL;
                }
                c1[0]=Class.forName((String) ret[0]);
                obj=c1[0].cast(obj);
                x[0].x=new Obj(obj,(String) ret[0]);
                return M_NOERR;
            }
            if (methodName.equals("isa")) {

                c1[0]=Class.forName((String) ret[0]);
                if (c1[0].isInstance(obj)) x[0].x=new Int(1);
                else x[0].x=new Int(0);
                return M_NOERR;
            }
            me=c.getMethod(methodName, c1);

            switch (xx) {

            case 0:
            	obj = me.invoke(obj);
            	break;
            case 1:
            	obj = me.invoke(obj,ret[0]);
            	break;
            case 2:
            	obj = me.invoke(obj,ret[0],ret[1]);
            	break;
            case 3:
            	obj = me.invoke(obj,ret[0],ret[1],ret[2]);
            	break;
            case 4:
            	obj = me.invoke(obj,ret[0],ret[1],ret[2],ret[3]);
            	break;
            case 5:
            	obj = me.invoke(obj,ret[0],ret[1],ret[2],ret[3],ret[4]);
            	break;
            case 6:
            	obj = me.invoke(obj,ret[0],ret[1],ret[2],ret[3],ret[4],ret[5]);
            	break;
            case 7:
            	obj = me.invoke(obj,ret[0],ret[1],ret[2],ret[3],ret[4],ret[5],ret[6]);
            	break;
            default:
            	return M_FAIL;
            }
            failure=M_NOERR;
            if (obj == null) x[0].x=null;
            else if (obj instanceof Integer) x[0].x=new Int((Integer) obj);
            else if (obj instanceof Double) x[0].x=new Real((Double) obj);
            else if (obj instanceof String) x[0].x=new Str((String) obj);
            else if (obj instanceof Boolean) x[0].x=new Int( ((Boolean)obj).booleanValue()?1:0 );
/*ANDROID
            else if (obj instanceof Complex) x[0].x=new Cmplx( ((Complex) obj).x,((Complex) obj).y);
ANDROID*/
            else x[0].x=new Obj(obj,obj.getClass().getName());

        } catch (ClassNotFoundException e) {
            ss += " class not found";
            System.out.println(ss);

        } catch (NoSuchFieldException e) {
            ss = "Field "+methodName+" not found";
        	System.out.println(ss);

        } catch (NoSuchMethodException e) {
        	ss = "Method "+methodName+" not matched";
        	System.out.println(ss);

        } catch (IllegalAccessException e) {
        	ss = "Method "+methodName+" no permission";
        	System.out.println(ss);

        } catch (InstantiationException e) {
            ss += " class constructor error";
        	System.out.println(ss);

        } catch (InvocationTargetException e) {
            ss=methodName+" Method threw: " + e.getTargetException();
        	System.out.println(ss);

        }
//RPC        failure=invoke.invoke(x[0],n,ss,z,s.toString().substring(k,k+j));

        if ( failure!=M_NOERR ) return M_FAIL;

      } else if (s.charAt(i)!='[') {

        z.copy(x[0]); return M_NOERR;

      } else {

        ii.inc(); y=z;

        for (;;) {

          failure = expr (x,s,ii);

          if ( failure!=M_NOERR ) break;

          y=(MTYPE)(y.index(x[0]));

          if (y==null) return M_FAIL;   

          if (s.charAt(ii.intValue())==',') ii.inc();

          else if (s.charAt(ii.intValue())==']') {

            ii.inc(); 

            y.copy(x[0]); break;

          }

        } /*for*/

      }

      i=ii.intValue();

      if ((s.charAt(i)!='[')&&(s.charAt(i)!='.')) return M_NOERR;

      ss=null;  z=x[0]; x[0]=new MTYPE();

     }  //for

    } else if ( s.charAt(i) == '(' ) {

      ii.inc();

      failure = expr (x,s,ii);

      if ( failure==M_NOERR ) {

        if ( s.charAt(ii.intValue()) == ')' ) {

          ii.inc();

        } else {

          failure = M_FAIL;

        }

      }

    }

  return failure;

}



public static int term(MTYPE x[], StringBuffer s, Int ii)

{   int failure;

MTYPE y=null;

failure = factor (x,s,ii); 

for (;;) {

	if ( failure!=M_NOERR ) { break; }

	if (y==null) y=new MTYPE();

	if ( s.charAt(ii.intValue()) == '*' ) { 

		x[0].move(y);

		ii.inc();
		if ( s.charAt(ii.intValue()) == '*' ) { 
			ii.inc(); failure = factor (x,s,ii);
			if (failure==M_NOERR) failure=y.pow(x[0]);
		} else {
			failure = factor (x,s,ii);
			if (failure==M_NOERR) failure=y.multiply(x[0]);
		}
		y.move(x[0]);

	} else if ( s.charAt(ii.intValue()) == '/' ) {

		x[0].move(y);

		ii.inc(); failure = factor (x,s,ii);

		if (failure==M_NOERR) failure=y.divide(x[0]); y.move(x[0]);

	} else if ( s.charAt(ii.intValue()) == '%' ) { 
		x[0].move(y);
		ii.inc(); failure = factor (x,s,ii);
		if (failure==M_NOERR) failure=y.mod(x[0]); y.move(x[0]);
	} else if ( s.charAt(ii.intValue()) == '^' ) { 
		failure = M_FAIL;
	} else {

		break;

	}

}

return failure;

}



public static int expr(MTYPE x[], StringBuffer s, Int ii)

{ int failure; MTYPE y=null;

      if ( s.charAt(ii.intValue()) == '+' ) {

        ii.inc(); failure = term (x,s,ii);

      } else if (( s.charAt(ii.intValue()) == '-' )||( s.charAt(ii.intValue()) == '!' )) {
        if ( ( s.charAt(ii.intValue()+1) >= '0' )&&( s.charAt(ii.intValue()+1) <= '9' )) failure=factor(x,s,ii);
        else {
        	ii.inc(); failure = term (x,s,ii);
        	if (failure==M_NOERR) failure=x[0].invert();
        }
      } else {

        failure = term (x,s,ii);

      }

    for (;;) {

      if ( failure!=M_NOERR ) { break; }

      if (y==null) y=new MTYPE();

      switch ( s.charAt(ii.intValue() )) {

      case '+' :

        ii.inc(); x[0].move(y);

        failure = term (x,s,ii);

    if (failure==M_NOERR) failure=y.add(x[0]); y.move(x[0]);

        y.clear();

        break;

      case '-' :

        ii.inc(); x[0].move(y);

        failure = term (x,s,ii);

    if (failure==M_NOERR) failure=y.subtract(x[0]); y.move(x[0]);

        y.clear();

        break;

      case '&' :

        ii.inc(); x[0].move(y);

        if ( s.charAt(ii.intValue())=='&' ) ii.inc();

        failure = term (x,s,ii);

    if (failure==M_NOERR) failure=x[0].and(y);

        y.clear();

        break;

      case '|' :

        ii.inc(); x[0].move(y);

        if ( s.charAt(ii.intValue())=='|' ) ii.inc();

        failure = term (x,s,ii);

    if (failure==M_NOERR) failure=x[0].or(y);

        y.clear();

        break;

      case '=' : ii.inc();

        if ( s.charAt(ii.intValue())=='=' ) {

          ii.inc(); x[0].move(y);

          failure = term (x,s,ii);

          if (failure==M_NOERR) {

            if (y.x==null) {

                x[0].x=new Int(x[0].x==null?1:0); failure=M_NOERR;

            } else if (x[0].x==null) {

                x[0].x=new Int(0); failure=M_NOERR;

            } else {

                failure=y.compare(x[0]);

                if (failure==0) {x[0].x=new Int(1); failure=M_NOERR;}

                else if (failure>0) {x[0].x=new Int(0); failure=M_NOERR;}

            }

          }

          y.clear();

        } else failure=M_FAIL;

        break;

      case '!' : ii.inc();

        if ( s.charAt(ii.intValue())=='=' ) {

          ii.inc(); x[0].move(y);

          failure = term (x,s,ii);

          if (failure==M_NOERR) {

            failure=y.compare(x[0]);

            if (failure>0) {x[0].x=new Int(1); failure=M_NOERR;}

            else if (failure==0) {x[0].x=new Int(0); failure=M_NOERR;}

          }

          y.clear();

        } else failure=M_FAIL;

        break;

      case '>' : int b=0; ii.inc();

        if ( s.charAt(ii.intValue())=='=' ) {b=1; ii.inc(); }

        if ( s.charAt(ii.intValue())=='>' ) {b=3; ii.inc(); }

          x[0].move(y);

          failure = term (x,s,ii);

          if (failure==M_NOERR) {

            if (b==3) {failure=y.shift(x[0],1); y.move(x[0]);

            } else {

                failure=y.compare(x[0]);

                if (failure==1) {x[0].x=new Int(1); failure=M_NOERR;}

                else if (failure>=0) {x[0].x=new Int(((failure+b)==1)?1:0); failure=M_NOERR;}

                else failure=M_FAIL;

            }

          }

          y.clear();      

        break;

      case '<' : b=0; ii.inc();

          if ( s.charAt(ii.intValue())=='=' ) {b=2; ii.inc(); }

          if ( s.charAt(ii.intValue())=='<' ) {b=3; ii.inc(); }

          x[0].move(y);

          failure = term (x,s,ii);

          if (failure==M_NOERR) {

            if (b==3) {failure=y.shift(x[0],0); y.move(x[0]);

            } else {

                failure=y.compare(x[0]);

                if (failure==2) {x[0].x=new Int(1); failure=M_NOERR;}

                else if (failure>=0) {x[0].x=new Int(((failure+b)==2)?1:0); failure=M_NOERR;}

                else failure=M_FAIL;

            }

          }

          y.clear();

        break;

      default:

        return failure;

      }

    }

    return failure;

}

private static class Context {

int code,codeIndexStart,variable,codeIndexStop;

int onetrue,target;

MTYPE p[]; StringBuffer s;

public Context(int targt, MTYPE x[], StringBuffer ps,int pcodeIndexStart) {

p=x; s=ps; code=2; codeIndexStart=pcodeIndexStart; target=targt;

}

public Context(int pcodeIndexStart, int pvariable) {

code=0; /*for*/ codeIndexStart=pcodeIndexStart; variable=pvariable;

codeIndexStop=-1;

}

public Context(StringBuffer s, int pcodeIndexStart, int pvariable,int pcode) {

code=1; /*if*/ codeIndexStart=pcodeIndexStart; variable=pvariable; onetrue=0;

/*delete toString if v>1.4*/

codeIndexStop=s.toString().indexOf("end"+(char)(variable-1+'a')+";",pcodeIndexStart);

if (codeIndexStop>0) codeIndexStop+=5;

}

public int setout(int i,int j) {

  if (variable!=j) return M_FAIL;

  codeIndexStop=i; return M_NOERR;

}

public int docase(MTYPE x[], StringBuffer s, Int ii, int pvariable) {int i;

  if ((code!=1)||(variable!=pvariable)) return 2;
  if (codeIndexStop<=0) return 2;
  if (onetrue==1) {ii.set(codeIndexStop); return 1;}
  if (x[0].isTrue()) {onetrue=1; return 0;}
  String ss=s.toString().substring(ii.intValue(),codeIndexStop);
  i=ss.toString().indexOf("case"+(char)(variable-1+'a'));

    if (i<=0) {ii.set(codeIndexStop); return 1;}

    ii.set(ii.intValue()+i);

    return 0;

}



public int breaker(MTYPE x[], StringBuffer ss, Int ii, boolean eof) {int i;

  if (code==2) {

    if (target!=0) p[target]=x[0];

    ii.set(codeIndexStart);

    return M_NOERR;

  } else if (eof) return M_FAIL;

  if (codeIndexStop>0) {ii.set(codeIndexStop); return M_NOERR;}

  i=ss.toString().indexOf("end"+(char)(variable-1+'a')+";",ii.intValue());

  if (i>0) {

    ii.set(i+5);

    return M_NOERR;

  }

  return M_FAIL;

}

public int continueit(MTYPE x[], StringBuffer s, Int ii) {

  int i;

  if (code==1) {
    ii.set(codeIndexStart);
    onetrue=0;
    return -1;

  }

  if (((MTYPE)x[variable]).x instanceof Int) {

    i=((Int)((MTYPE)x[variable]).x).x-1;

    if (i>=0) {

      ((Int)((MTYPE)x[variable]).x).x=i; ii.set(codeIndexStart);

      return 0;

    }

  } else if (((MTYPE)x[variable]).x instanceof Real) {

    double r=((Real)((MTYPE)x[variable]).x).x-1.0;

    if (r>=0.0) {

      ((Real)((MTYPE)x[variable]).x).x=r; ii.set(codeIndexStart);

      return 0;

    }

  } else return M_FAIL;

  if (codeIndexStop>0) {ii.set(codeIndexStop); return 1;}

  i=s.toString().indexOf("end"+(char)(variable-1+'a')+";",ii.intValue());

  if (i>0) {

      ii.set(i+5);

      return 1;

  }

  return M_FAIL;

}

}



public static MTYPE [] calls(int k, Vector loops, int j, int t, MTYPE x[], StringBuffer s, Int ii) {

      int i;

      MTYPE y=new MTYPE(),temp; x[j].copy(y);

          if (!(y.x instanceof Str)) return null;

          M z[]=args(x,s,ii);

          if (z==null) return null;

          loops.addElement(new Context(k,x,s,ii.intValue()));

          ii.set(0);

	      temp=x[t];

          x=new MTYPE[27];

          for (i=0;i<27;i++) x[i]=new MTYPE();

          x[1].x=new Array();

	      if (t!=0) x[9].x=temp.x; //shares prop array but not variable i; otherwise overwrites i

          y.copy(x[0]);

          i=((Int)z[9]).x;

          for (j=0;j<i;j++) {

              y=new MTYPE();

              y.x=(M)z[j];

              ((Array)x[1].x).extend(y);

          }

          return x;

}



public static int stat(Vector loops, MTYPE x[], StringBuffer s, Int ii, StringBuffer output)

{

  int failure,j,k,i=ii.intValue(),ls;

  loops.clear();

try {

    x[0].clear();

    ls=s.length();

    for (;;) {

start:

      while ((i<ls)&&(( s.charAt(i)==';')||(s.charAt(i)=='\n')||(s.charAt(i)=='\r')||(s.charAt(i)=='\t')||(s.charAt(i)==' '))) i++;

      if ((i<ls)&&(s.charAt(i)=='/')&&(s.charAt(i+1)=='/')) {

        while ((i<ls)&&(s.charAt(i)!='\n')&&(s.charAt(i)!='\r')) i++;

        continue;

      }

      if ((i<ls)&&(s.charAt(i)=='/')&&(s.charAt(i+1)=='*')) {

        while ((i<ls)&&((s.charAt(i)!='*')||(s.charAt(i+1)!='/'))) i++;

        i+=2;

        continue;

      }

      if (i>=ls) {

        if (loops.isEmpty()) return M_NOERR;

        Context cc=((Context)loops.lastElement());

        k=cc.breaker(x,s,ii,true);

        if (k==M_FAIL) throw new Exception("EOF error");

        x=cc.p;  s=cc.s;

        i=ii.intValue();  ls=s.length();

        loops.removeElementAt(loops.size()-1);

        continue;

      }

	  boolean bA=(s.charAt(i)>='a')&&(s.charAt(i)<='z');

      if (bA&&(s.charAt(i+1)==';')) {

        j=s.charAt(i)-'a'+1;  i+=2;

        x[0]=x[j];

        continue;

      }

      if (bA&&(s.charAt(i+1)=='=')) {

        j=s.charAt(i)-'a'+1;  i+=2;

        if ((s.charAt(i)>='a')&&(s.charAt(i)<='z')) {boolean b=false; int m,n;

		  k=s.charAt(i)-'a'+1;  n=0;

          for (;;) {

          if (s.charAt(i+1)!='(') {

              if (s.charAt(i+1)!='.') break;

              m=strpbrk(s,i+2,"abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ");

              if (m<=0) break;

              String ss=s.toString().substring(i+2,m+i+2);

              if (! (((MTYPE)x[k]).x instanceof Array)) break;

			  n=k;

              Vector v=((Array)((MTYPE)x[k]).x).x;

              MTYPE o=null;

              for (Enumeration e=v.elements();e.hasMoreElements();) {

                  o=(MTYPE)e.nextElement();

                  if (! (o.x instanceof Array)) break;

                  Vector vv=((Array)(o.x)).x;

                  if (vv.size()!=2) break;

                  o=(MTYPE)vv.firstElement();

                  if (!(o.x instanceof Str)||!((Str)(o.x)).x.equals(ss)) continue;

                  i+=m+2; b=true;

                  o=(MTYPE)vv.elementAt(1);

                  break;

              } //for

              if (!b) break;

              if (s.charAt(i)!='(') {

                  o.copy(x[j]); break;

              }

              o.copy(x[0]); k=0; i--;

          } //if ! (

          ii.set(i+2);

          x=calls(j,loops,k,n,x,s,ii);

          if (x==null) throw new Exception("call error");

          s=new StringBuffer(((Str)((MTYPE)x[0]).x).x);

          i=0;  ls=s.length();

          b=true;  break;

          } /*for*/

          if (b) continue;

        }

        ii.set(i);

        failure=expr(x,s,ii); i=ii.intValue();

        if (failure!=M_NOERR) break;

        x[0].move(x[j]);

        continue;

      }

    if ((bA&&(s.charAt(i+1)=='.'))||

        ((s.charAt(i)>='A')&&(s.charAt(i)<='Z')&&(s.charAt(i+1)=='.')) ) {

		if (bA) {

			k=s.charAt(i)-'a'+1;

			if ( (((MTYPE)x[k]).x instanceof Array)) {

				Vector v=((Array)((MTYPE)x[k]).x).x;

				MTYPE o=null;

				boolean b=false;

				int m;

				m=strpbrk(s,i+2,"abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ");

				if ((m>0)&&(s.charAt(m+i+2)=='=')) {

				String ss=s.toString().substring(i+2,m+i+2);

				for (Enumeration e=v.elements();e.hasMoreElements();) {

					o=(MTYPE)e.nextElement();

					if (! (o.x instanceof Array)) break;

					Vector vv=((Array)(o.x)).x;

					if (vv.size()!=2) break;

					o=(MTYPE)vv.firstElement();

					if (!(o.x instanceof Str)||!((Str)(o.x)).x.equals(ss)) continue;

					i+=m+3; b=true;

					o=(MTYPE)vv.elementAt(1);

					break;

				} //for

				if (b) {

					ii.set(i);

					failure=expr(x,s,ii); i=ii.intValue();

					if (failure!=M_NOERR) break;

					x[0].move(o);

					continue;

				}

				} //a.name=

			}

		}

        ii.set(i);

        failure=factor(x,s,ii); i=ii.intValue();

        if (failure!=M_NOERR) break;

        continue;

    }

      if (bA&&(s.charAt(i+1)=='(')) {

        j=s.charAt(i)-'a'+1;  i+=2;

        ii.set(i);

        x=calls(0,loops,j,0,x,s,ii);

        s=new StringBuffer(((Str)((MTYPE)x[0]).x).x);

        i=0;  ls=s.length();

        continue;

      }

      if (bA&&(s.charAt(i+1)=='[')) {

        j=s.charAt(i)-'a'+1;

        ii.set(i+2); MTYPE y=x[j];

        for (;;) {

          failure = expr (x,s,ii);

          if ( failure!=M_NOERR ) throw new Exception("subscript expression error");

          Object z=y.index(x[0]); 

          if (z==null) throw new Exception("subscript error");

          y=(MTYPE)z;

          if (s.charAt(ii.intValue())==',') ii.inc();

          else if (s.charAt(ii.intValue())==']') {

            ii.inc(); 

            if (s.charAt(ii.intValue())!='=') throw new Exception("misplaced , or ] error");

            ii.inc();

            break;

          }

        } /*for*/

        failure = expr (x,s,ii);

        i=ii.intValue();

        if (failure!=M_NOERR) break;

        x[0].move(y);

        continue;

      }

      if (sequals(s,i,"for")) {

        if ((s.charAt(i+3)<'a')||(s.charAt(i+3)>'z')) {failure=M_FAIL; break;}

        j=s.charAt(i+3)-'a'+1; i+=4; ii.set(i);

        k=loops.size()-1;

        while (k>=0) {if (((Context)loops.elementAt(k)).variable==j) {failure=M_FAIL; break;} k--;}

        loops.addElement(new Context(i,j));  

        k=((Context)loops.lastElement()).continueit(x,s,ii); i=ii.intValue();

        if (k==0) continue;

        if (k==1) {loops.removeElementAt(loops.size()-1); continue;}

        failure=M_FAIL; break;

      }

      if (sequals(s,i,"if")) {

        if ((s.charAt(i+2)<'a')||(s.charAt(i+2)>'z')) {failure=M_FAIL; break;}

        j=s.charAt(i+2)-'a'+1; i+=3; ii.set(i);

        k=loops.size()-1;

        while (k>=0) {if (((Context)loops.elementAt(k)).variable==j) {failure=M_FAIL; break;} k--;}

        loops.addElement(new Context(s,i,j,1));  

        k=((Context)loops.lastElement()).continueit(x,s,ii); i=ii.intValue();

        if (k<=0) continue;

        failure=M_FAIL; break;

      }

      if (sequals(s,i,"case")) {

        if ((s.charAt(i+4)<'a')||(s.charAt(i+4)>'z')) {failure=M_FAIL; break;}

        j=s.charAt(i+4)-'a'+1; i+=6; ii.set(i);

        failure = factor (x,s,ii); i=ii.intValue();

        if (s.charAt(i)!=':') {failure=M_FAIL; break;}

        ii.inc();

        k=loops.size()-1;

        if (k<0) {failure=M_FAIL; break;} 

        k=((Context)loops.lastElement()).docase(x,s,ii,j); i=ii.intValue();

        if (k==0) continue;

        if (k==1) {loops.removeElementAt(loops.size()-1); continue;}

        failure=M_FAIL; break;

      }

      if (sequals(s,i,"end")) {

        if ((s.charAt(i+3)<'a')||(s.charAt(i+3)>'z')) {failure=M_FAIL; break;}

        j=s.charAt(i+3)-'a'+1; i+=4;

        if (loops.size()<=0) {failure=M_FAIL; break;}

        k=((Context)loops.lastElement()).setout(i,j);
        ii.set(i);
        if (k!=M_NOERR) {failure=k; break;}

        k=((Context)loops.lastElement()).continueit(x,s,ii);

        if (k==0) {i=ii.intValue(); continue;}

        if ((k==1)||(k<0)) {loops.removeElementAt(loops.size()-1); continue;}

        failure=M_FAIL; break;

      }

      if (sequals(s,i,"continue")) {

        j=s.charAt(i+8)-'a'+1;

        k=loops.size()-1;

        while (k>=0) {if (((Context)loops.elementAt(k)).variable!=j) {loops.removeElementAt(k);k--;} else break;}

        if (k<0) {i+=9;failure=M_FAIL; break;}

        k=((Context)loops.lastElement()).continueit(x,s,ii); i=ii.intValue();

        if (k<=0) continue;

        if (k==1) {loops.removeElementAt(loops.size()-1); continue;}

        failure=M_FAIL; break;

      }

      if (sequals(s,i,"break")) {

        j=s.charAt(i+5)-'a'+1;

        k=loops.size()-1;

        while (k>=0) {if (((Context)loops.elementAt(k)).variable!=j) {loops.removeElementAt(k);k--;} else break;}

        if (k<0) {i+=6;failure=M_FAIL; break;}

        failure=((Context)loops.lastElement()).breaker(x,s,ii,false); i=ii.intValue();

        if (failure==M_NOERR) {loops.removeElementAt(loops.size()-1); continue;}

        break;

      }

      if (sequals(s,i,"return")) {

		k=loops.size()-1;

		while (k>=0) {if (((Context)loops.elementAt(k)).code!=2) {loops.removeElementAt(k);k--;} else break;}

		//if (k<0) {failure=M_FAIL; break;}

	    i=ls; continue;

	  }
/*ANDROID
      if (sequals(s,i,"show(")) {
    	  ii.set(i+5); failure = expr (x,s,ii); i=ii.intValue();
    	  if ((failure==M_NOERR)&&(s.charAt(i)==')')) {
    		  i++;
    		  android.widget.Toast.makeText(NoteEditor.act,x[0].toString(),1).show();
    		  continue;
    	  }
      }
ANDROID*/
      if (sequals(s,i,"print(")||sequals(s,i,"println(")) {boolean b=false;     
        if (s.charAt(i+5)=='l') {i+=2; b=true;}
        i+=6;
        
		for (;; ) {

			ii.set(i);

			failure = expr (x,s,ii); i=ii.intValue();

			if (failure!=M_NOERR) break;

			output.append(x[0].toString());
            output.append(b?'\n':' ');
			//System.out.println(x[0].toString());

			if (s.charAt(i)==')') {i++; break;}

		    if (s.charAt(i)==',') i++; else {failure=M_FAIL; break;}

		}

		if (failure==M_NOERR) continue;

      }

      failure=M_FAIL; break;

  }

ii.set(i);

} catch (Exception e) {

    output.append("\n"+"EX: "+e.toString());

    failure=M_FAIL;

}

if (failure==M_FAIL) {

  for (;;) {

  if (s!=null) output.append("\n"+"ERR: <<"+s.toString().substring(Math.max(i-60,0),Math.max(10,i))+">>\n");

  if (loops.isEmpty()) return M_FAIL;

  Context cc=((Context)loops.lastElement());

  k=cc.breaker(x,s,ii,true);

  s=cc.s;  i=ii.intValue();

  loops.removeElementAt(loops.size()-1);

  }

}

return failure;

}

 

public static int Expr(MTYPE p[], String ss, StringBuffer output) {
	Int i=new Int(0); StringBuffer s=new StringBuffer(ss);
    Vector v=new Vector();
    return stat(v,p,s,i,output);
}
public static final class ExecutionContext {
	public MTYPE p[];
	StringBuffer output=new StringBuffer();
	Vector v=new Vector();
	Int i=new Int(0);
	public ExecutionContext() {
		p=new MTYPE[27];
		for (int i=0;i<27;i++) p[i]=new MTYPE();
		p[1].x=new Array();
		p[13].x=new Str("java.lang.Math"); //M
	}
	public ExecutionContext(Object... objects) {
		p=new MTYPE[27];
		for (int i=0;i<27;i++) p[i]=new MTYPE();
		Array a=new Array();
		for (Object o:objects) {
			a.extend(new MTYPE(new Obj(o, o.getClass().getName())));
		}
		p[1].x=a;
		p[13].x=new Str("java.lang.Math"); //M
	}
	public int execute(String program) {
		output=new StringBuffer();
		v.clear();
		i.x=0;
		StringBuffer s=new StringBuffer(program);
		return stat(v,p,s,i,output);
	}
	public String output() {
		return this.output.toString();
	}
	public void setArg(int j, M arg) {
		((MTYPE)p[1].x.index(new MTYPE(new Int(j)))).x=arg;
	}
}


public static String parse(String program, String args[]) {

MTYPE p[]=new MTYPE[27];  StringBuffer output;

int i;

for (i=0;i<27;i++) p[i]=new MTYPE();

p[1].x=new Array();

if (args!=null) {

    for (i=0; i<args.length; i++) {MTYPE x=new MTYPE(); x.x=new Str(args[i]); p[1].x.add(x);}

}
/*ANDROID
p[3].x=new Obj(NoteEditor.act,"android.app.Activity");
p[4].x=new Str("com.example.android.notepad2.Data"); //D
ANDROID*/
p[13].x=new Str("java.lang.Math"); //M
/*ANDROID
p[14].x=new Str("com.example.android.notepad2.Matrix"); //N
if (result==null) p[18].x=null;
else p[18].x=new Obj(result,"android.content.Intent");
result=null;
ANDROID*/
output=new StringBuffer("");

Expr(p,program,output);

return output.toString();

}

}
/*
VariableName a to z
        there is a limit of 26 one-letter names total.
//ignore characters to end of line    /* ignore characters until AsteriskSlash 
;	\n	\r	\t
V=Expression
	an expression can contain ( ) + - * / % & |
		** (exponentiation)  << (left shift) >> (right shift)
                >  <  >=  <=  ==  !=
	an E can contain Variables and constants

	java.lang.Integer
	integers 3  67 -892 036 (leading zero means octal) 0xa9 (hex)
	java.lang.Double
	double-precision real numbers 6.8 0.1e-9
	use V.intValue() .floatValue() .doubleValue() for conversions
		to convert a string to a number just add 0, a+0
                or Integer.valueOf() etc.

	java.lang.String
	strings  'abc' or "abcd"   + can be used for string concatenation
		' " \ must be escaped " \\ \n \r \t \' \" "
			              ' \\ \n \r \t \' \" '
		strings can span multiple lines
	java.util.Vector
	arrays [ E [,E] ... ]   [3,4,["hi",5.6]]
	null   an empty object constant

print(E,E,E,E)  print comma-separated list of expressions
println(E,E,E,E)  same as "print" but output newline after each E

ARRAYS
V=V[E [,E]...]		array elements accessed by zero-origin subscripts
			print(a[12]);  a[i*2+1]=93; a[3]="hi";
V[E[,E]...]=E           assignment to multi-dimensional array
V.size()		refers to number of elements
V.setSize(E)		extends or shrinks array, new elements are null

OBJECTS
all Java classes can be access but must use fully qualified class name
a="java.util.Date"  b=87654321; e=A.new(b.longValue()) print(e)
	a capital letter in a qualified name indicates a class name
	a lower case letter indicates a class instance
	WARNING: ARGUMENT TYPES MUST MATCH EXACTLY!!
		for example, int != long, String != Object
V.isa(E)		E must evaluate to a string
			a=45; print(a.isa("java.lang.Integer")) is true
V.class			accesses the string value of the class name
	ASSIGNMENT and PARAMETER PASSING IS BY REFERENCE

PROPERTY LISTS
simple structured data is provided through property lists
//property list-array of name/value arrays  [['dog',45],['cat',3+4]] 
a=[["cat",56],["rat",34]];         property list  print(a.cat);  a.cat=52;
V.property=E
V.property[E[,E]É]=E		   property values can be arrays   
//accessing a property with more than one value returns the whole array

REPETITION
there is only one looping construct, which counts down from N-1 to 0
a=10 fora print(a) enda;
9 8 7 6 5 4 3 2 1 0
breaka (to exit loop a) and continuea (skip to next iteration of fora) are implemented
	WARNING: the "end" statements must include a ;

CONDITIONAL
there is only one "if" statement
a=10; fora ifb caseb (a!=5): print(a) endb; enda;
9 8 7 6 4 3 2 1 0
breakb (to exit "if" statement b) is implemented
	there can be multiple "case"s in an "if" statement
	WARNING: caseb etc must be followed by at least one blank
	WARNING: the "end" statements must include a ;

PROCEDURES
the return; statement is implemented
the return value is set by typing V; as the last statement executed
the argument list to a method is collected as an array and set in variable 'a'
	the same convention is implemented for programs
	allarguments are passed by value (copied)
a="print(a)" b=a(3,4)
[3, 4] 
M procedures can only be invoked as subroutines or assignments
a(3,4)
b=a(3,4)
a="print(a) b=a[0]+a[1] b;" b=a(3,4) print(b)
[3, 4] 7

INTERFACE math         		use M.sqrt(6.7) to access methods
				only abs/min/max take integer arguments
abs(number)			absolute value
acos(number)			arc cosine of angle in radians
asin(number)			arc sine of angle in radians
atan(number)			arc tangent of angle in radians
atan2(number,number)		arc cosine of angle in radians
cbrt(number)			cube root
ceil(number)			ceiling
cos(number)			cosine of angle in radians
cosh(number)			hyperbolic cosine of angle in radians
exp(number)			raises E to the number power
floor(number)			floor
hypot(number,number)		Pythagorean Theorem, length of hypotenuse
				 of right triangle given two side lengths
IEEEremainder(number1,number2)	remainder on division of number1 by number2
log(number)			natural logarithm
log1p(number)			natural logarithm of number+1
log10(number)			base 10 logarithm
max(number,number)		maximum
min(number,number)		minimum
pow(number1,number2)		raises number1 to the number2 power
random()			0 to less than 1.0
rint(number)			round to closest integer
sin(number)			sine of angle in radians
sinh(number)			hyperbolic sine of angle in radians
sqrt(number)			square root
tan(number)			tangent of angle in radians
tanh(number)			hyperbolic tangent of angle in radians
toDegrees(number)		from radians
toRadians(number)		from degrees
PI 3.14.....
E  2.78.....

*/
