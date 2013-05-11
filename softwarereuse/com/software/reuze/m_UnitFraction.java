package com.software.reuze;
//package com.example.android.notepad2;
import java.util.Enumeration;

public class m_UnitFraction {

  double value;

  int exponents;

  m_UnitSystem us;

  boolean mode=false;



public m_UnitFraction(m_UnitSystem us) {

  exponents=0; value=0; this.us=us;

}

public m_UnitFraction(m_UnitFraction us) {

	  exponents=us.exponents; value=us.value; this.us=us.us; mode=us.mode;

}

public m_UnitFraction() {

  exponents=0; value=0; this.us=m_UnitFact.getDefaultSystem();

}



public m_UnitFraction(String s) {

  m_UnitFraction f=parse(m_UnitFact.getDefaultSystem(), s);
  if (f==null) f=new m_UnitFraction(Double.NaN);
  value=f.value;

  exponents=f.exponents;

  us=f.us;

}



public m_UnitFraction(m_UnitSystem u, String s) {

  m_UnitFraction f=parse(u, s);
  if (f==null) f=new m_UnitFraction(Double.NaN);
  value=f.value;

  exponents=f.exponents;

  us=u;

}



public m_UnitFraction(m_UnitSystem u, double v) {

  value=v;

  exponents=0;

  us=u;

}



public m_UnitFraction(double v) {

  value=v;

  exponents=0;

  us=m_UnitFact.getDefaultSystem();

}



public m_UnitFraction(m_UnitSystem u, double v, int e) {

  value=v;

  exponents=e;

  us=u;

}



public double doubleValue() {

  return value;

}



private static double power(double x, double e) {

    if (e==1.0) return x;

    if (e==2.0) return x*x;

    if (e==3.0) return x*x*x;

    if (e==0.5) return Math.sqrt(x);

    return Double.NaN;

}

public void setFormat(String s) {
  mode=s.equalsIgnoreCase("long");
}

public void setDefault(String sys) {
	us.setDefault(sys, m_UnitFact.getId(sys));
}

public boolean equals(int e, m_UnitSystem eus) {

  if (exponents!=e) return false;

  if (exponents==0) return true;

  return us==eus;

}



public m_UnitFraction emultiply(int e, int id) {

  exponents|=(e&0xf)<<(id*4);

  return this;

}



static class I {

  int x;

public I(int y) {x=y;};

}



public static int eparse(String s, I x) {

    int i=x.x;

    int j=s.length();

    if (i>=j) return 1;

    if (s.charAt(i)!='^') return 99;

    i++;

    if (i>=j) return 99;

    int k=i;

    if (s.charAt(k)=='+') {i++; k++;} //due to stupid parseInt

    else if (s.charAt(k)=='-') k++;

    if (k>=j) return 99;

    if (!Character.isDigit(s.charAt(k))) return 99;

    if ((k<j)&&Character.isDigit(s.charAt(k))) k++;

    int e=Integer.parseInt(s.substring(i,k));  

    if ((e<-8)||(e>7)) return 99;

    x.x=k;

    return e;

}



private int subsumes(int i) {

  int e=exponents,m,j,n=0,s,t;

  for (j=0; j<8;j++) {

    m=0xf<<(j*4);

    if ((e&m)==0)continue;

    if ((i&m)==0) return 0;

    s=e&m;  t=i&m;

    if (n==0) {

      if (s==t) n=1;

      else {

        s=s>>(j*4);  t=t>>(j*4);

        if (s>7) s-=16;

        if (t>7) t-=16;

        if ((t%s)!=0) return 0;

        n=t/s;

        if (n<=1) return 0;

      }

    } else {

      if ( ((s*n)&m)!=((t*n)&m) ) return 0;

    }

  }

  return n;

}



public String toString() {

      if (exponents==0L) return String.valueOf(value);
      if (exponents<0L) return "###";

      String s="";

      double v=value;

      int j=exponents,m;

      boolean b=true;

      while ((j!=0)&&b) {

      Enumeration e=us.elements();

      b=false;

      while (e.hasMoreElements()) {

        m_UnitFact p=(m_UnitFact)e.nextElement();

    if (p.id==99) { //must be derived

          m=p.f.subsumes(j);

          if (m==0) continue;

      v/=p.f.value;

      if (mode) s+=p.name;

          else s+=p.ab;

          System.out.println("m="+m+" j="+j);

          if (m!=1) {

            s+='^'+m;

          }

          while (m>0) {m--; j-=p.f.exponents;};

          b=true;

      break;

    }

      }

      } //while b

      int i,k;

      for (i=0; (j!=0)&&(i<8); i++) {

          k=j&0xf; j=(j>>4)&0xfffffff;

          if (k==0) continue;

          if (k>7) k=k-16;

          if (us.defaults[i]!=null) {

                    if (mode) s+=((m_UnitFact)us.defaults[i]).name;

                    else s+=((m_UnitFact)us.defaults[i]).ab;

          } else s+="###";

          if (k!=1) {

              s+="^"+String.valueOf(k);

          }

                  m_UnitFact uf=((m_UnitFact)us.defaults[i]);

                  double df;

          if (uf!=null) {

                    if (uf.f !=null) {df=uf.f.value;

                      if (df!=0.0) v/=power(df,(double)k);

                    } else {/*UnitScript sc=new UnitScript(us);

                      Stack sk=new Stack(); sk.push(new UnitFraction(v));

                      sc.execute(sk,uf.fromBase);

                      v=((UnitFraction)sk.pop()).value;*/

                    }

                  }

      };

      return String.valueOf(v)+s;

}



public static m_UnitFraction parse(m_UnitSystem us, String s) {

int i,j,k,m,n=0; int e; m_UnitFact p; String t; m_UnitFraction f=new m_UnitFraction(us);

  t=s.trim();

  if (s==null) return null;

  i=t.length();

  if (i==0) return null;

  while (i>0) {char c;

    i--;

    c=t.charAt(i);

    if ((c>='0')&&(c<='9')&&!((i>0)&&(t.charAt(i-1)=='^')||

       ((i>1)&&(t.charAt(i-1)=='-')&&(t.charAt(i-2)=='^'))||

       ((i>1)&&(t.charAt(i-1)=='+')&&(t.charAt(i-2)=='^')) )) break;

    if (i==0) return null;

  };

  i++;

  f.value=Double.parseDouble(t.substring(0,i));

  j=t.length();

  for (;i<j;) {

    if (!Character.isUpperCase(t.charAt(i))) return null;

    k=i; i++;

    while((i<j)&&Character.isLowerCase(t.charAt(i))) i++;

    e=1;

    p = (m_UnitFact)us.getTag(t.substring(k,i));

    if ((i<j)&&(t.charAt(i)=='^')) {I x=new I(i);

      e=eparse(t,x);

      if (e==99) return null;

      i=x.x;

    };

    if (p==null) return null;

    if (p.f==null) {/*UnitScript sc=new UnitScript(f.us);

      Stack sk=new Stack(); sk.push(f);

      sc.execute(sk,p.toBase);

      f=(UnitFraction)sk.pop();*/

    } else if (p.f.exponents!=0) {

      if ((e!=1)||(p.id!=99)) return null;

      f.multiply(p.f);

      continue;

    };

    if (e!=0) {

      if (p.f==null) {

        if (e!=1) return null;

      } else if (p.f.value!=0.0) f.value *= power(p.f.value,(double)e);

      if ((n&(1<<p.id))!=0) return null;

      n|=(1<<p.id);

      f.emultiply(e,p.id);

    };

  }; //for

  return f;

}



public boolean isValid() {

  return exponents!=-1;

}



public m_UnitFraction add(m_UnitFraction right) {

        if (exponents!=right.exponents) value=Double.NaN;

        else value+=right.value;

        return this;

}



public m_UnitFraction subtract(m_UnitFraction right) {

        if (exponents!=right.exponents) value=Double.NaN;

        else value-=right.value;

        return this;

}



public m_UnitFraction root(m_UnitFraction right) {

        int op=(int)right.value;

        if ((right.exponents!=0)||(op<=0)) {value=Double.NaN; return this;}

        if (op==1) return this;

        value=power(value,1.0/(double)op);

        {int i,k, n=exponents; exponents=0;

        for (i=0;(n!=0)&&(i<8);i++) {

                k=n&0xf; n=n>>4;

                if (k!=0) {

                    if (k>7) k=k-16;

                    if ((k%op)!=0) return this;

                    exponents|=((k/op)&0xf)<<(i*4);

                };

        };

        };

        return this;

}



public m_UnitFraction divideC(double numerator) {

        m_UnitFraction f=new m_UnitFraction(us,numerator); 

                f.divide(this);

                this.value=f.value;  this.exponents=f.exponents;

        return this;

}

/*

public UnitFraction fmod(UnitFraction right) {

        if (exponents!=right.exponents) exponents=-1;

        else value=Math.IEEEremainder(value,right.value);

                return this;

}

*/

public m_UnitFraction pow(m_UnitFraction right) {
        if (right.exponents!=0) {value=Double.NaN; return this;}
        double val=right.value; 
        if (val<0) {val=-val; this.divideC(1.0);}
        int op=(int)val;
        if (op==0) {
        	if (val>0) {
        		op=(int)(1.0/val);  this.root(new m_UnitFraction(op)); return this;
        	}
            exponents=0; value=1.0; return this;
        }
        m_UnitFraction old=new m_UnitFraction(us,value,exponents);
        while (op>1) {
            this.multiply(old);
            op--;
        }
        return this;
}



public m_UnitFraction multiply(m_UnitFraction right) {

        value*=right.value;

        if (exponents==0) {exponents=right.exponents;

        } else if (right.exponents!=0) {int i; int j,k,m,n,p;

            m=exponents;  n=right.exponents; exponents=0;

            for (i=0;((m!=0)||(n!=0))&&(i<8);i++) {

                j=(m&0xf); 

                k=(n&0xf);

                if ((j!=0)||(k!=0)) {

                    if (j>7) j=j-16;

                    if (k>7) k=k-16;

                    p=j+k;

                    if ((p<-8)||(p>7)) {exponents=-1; return this;}

                    exponents|=(p&0xf)<<(i*4);

                };

                m=m>>4; n=n>>4;

            };

        };

                return this;

}



public m_UnitFraction divide(m_UnitFraction right) {

        value/=right.value;

        if (exponents==0) {int i; int k,n;

            n=right.exponents;

            for (i=0;(n!=0)&&(i<8);i++) {

                k=(n&0xf); n=n>>4;

                if (k!=0) {

                    if (k>7) k=k-16;

                    exponents|=((-k)&0xf)<<(i*4);

                };

            };

        } else if (right.exponents!=0) {int i; int j,k,m,n,p;

            m=exponents;  n=right.exponents; exponents=0;

            for (i=0;((m!=0)||(n!=0))&&(i<8);i++) {

                j=(m&0xf);              

                k=(n&0xf);

                if ((j!=0)||(k!=0)) {

                    if (j>7) j=j-16;

                    if (k>7) k=k-16;

                    p=j-k;

                    if ((p<-8)||(p>7)) {value=Double.NaN; return this;}

                    exponents|=(p&0xf)<<(i*4);

                };

                m=m>>4; n=n>>4;

            };

        };

                return this;

}



public boolean setUnitDerived(String name, String abbreviation)

{

    if ((value==0.0)||(exponents==0)) return false;

    m_UnitFact f=new m_UnitFact(us,name,abbreviation,this,0);

    return f!=null;

}

/*

public int hashCode() {

  return (int) ((int)value^Double.doubleToRawLongBits(value)^exponents);

}

*/

public int compareTo(Object o) {

  if (!(o instanceof m_UnitFraction)) return -2;

  if (!isValid()) return -3;

  if (!((m_UnitFraction)o).isValid()) return -4;

  if (((m_UnitFraction)o).us!=this.us) return -5;

  if (((m_UnitFraction)o).exponents!=this.exponents) {

    return -6;

  };

  if (((m_UnitFraction)o).value==this.value) return 0;

  if (((m_UnitFraction)o).value<this.value) return +1;

  return -1;

}



public int compareTo(m_UnitFraction f) {

  return compareTo((Object)f);

}



public boolean equals(Object o) {

  if (!(o instanceof m_UnitFraction)) return false;

  if (!isValid()) return false;

  if (!((m_UnitFraction)o).isValid()) return false;

  if (((m_UnitFraction)o)==this) return true;

  if (((m_UnitFraction)o).us!=this.us) return false;

  if (((m_UnitFraction)o).exponents!=this.exponents) return false;

  return ((m_UnitFraction)o).value==this.value;

}



}