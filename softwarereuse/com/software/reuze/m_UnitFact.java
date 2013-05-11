package com.software.reuze;
//package com.example.android.notepad2;
import java.util.Vector;

public class m_UnitFact {

	byte id; String ab;

	String name;

        m_UnitFraction f;

        Vector toBase,fromBase;

        static boolean init=false;

        static m_UnitSystem defSystem;

public boolean check(m_UnitSystem s, String name, String abbreviation/*<=3*/, int position) {

        if ((position<0)||(position>8)) return false;

        if ((name==null)||(name.length()==0)) return false;

	if (!Character.isUpperCase(name.charAt(0))) return false;

	if ((abbreviation==null)||(abbreviation.length()==0)||!Character.isUpperCase(abbreviation.charAt(0))) return false;

	if ((name.length()>15)||(abbreviation.length()>3)) return false;

        this.name=name.substring(0,1)+name.substring(1).toLowerCase();

        ab=abbreviation.substring(0,1)+abbreviation.substring(1).toLowerCase();

        if (s.getTag(this.name)!=null) return false;

        if (s.getTag(ab)!=null) return false;

        return true;

}



public m_UnitFact(m_UnitSystem s, String name, String abbreviation/*<=3*/, m_UnitFraction f, int position) {

        id=98;

        if (!check(s,name,abbreviation,position)) return;

        if ((position==0)&&(f.value==0.0)) return;

	if ((position!=0)&&(f.value==0.0)&&(s.defaults[position-1]!=null)) return;

	this.f=f;

        s.addTag(position-1,f.value==0.0,this);

        if (position!=0) id=(byte)(position-1);

        else id=99;

}

/*

public UnitFact(UnitSystem s, String name, String abbreviation, Vector toBase, Vector fromBase, int position) {

       id=98;

       if (!check(s,name,abbreviation,position)) return;

       if (position==0) return;

       id=(byte)(position-1);

       this.f=null;

       this.toBase=(Vector)toBase.clone();

       this.fromBase=(Vector)fromBase.clone();

       s.addTag(position-1,false,this);

}

*/

public boolean isValid() {

  return id!=98;

}



public boolean equals(Object o) {

  String f=(String)o;

  return name.equalsIgnoreCase(f)||ab.equalsIgnoreCase(f);

}



public static m_UnitSystem getDefaultSystem() {

  if (init) return defSystem;

  init=true;

  defSystem=new m_UnitSystem();

  m_UnitFact t;

  t=new m_UnitFact(defSystem,"Feet","Ft",new m_UnitFraction(defSystem,0.3048),1);

  t=new m_UnitFact(defSystem,"Nanometers","Nm",new m_UnitFraction(defSystem,1.0e-9),1);

  t=new m_UnitFact(defSystem,"Micrometers","Um",new m_UnitFraction(defSystem,1.0e-6),1);

  t=new m_UnitFact(defSystem,"Millimeters","Mm",new m_UnitFraction(defSystem,0.001),1);

  t=new m_UnitFact(defSystem,"Centimeters","Cm",new m_UnitFraction(defSystem,0.01),1);

  t=new m_UnitFact(defSystem,"Meters","M",new m_UnitFraction(defSystem,0.0),1);

  t=new m_UnitFact(defSystem,"Kilometers","Km",new m_UnitFraction(defSystem,0.0),1);

  t=new m_UnitFact(defSystem,"Inches","In",new m_UnitFraction(defSystem,0.0254),1);

  t=new m_UnitFact(defSystem,"Yards","Yd",new m_UnitFraction(defSystem,0.9144),1);

  t=new m_UnitFact(defSystem,"Miles","Mi",new m_UnitFraction(defSystem,1609.344),1);

  t=new m_UnitFact(defSystem,"Fathoms","Fm",new m_UnitFraction(defSystem,1.8288),1);

  t=new m_UnitFact(defSystem,"NauticalMiles","Nmi",new m_UnitFraction(defSystem,1852.0),1);

  t=new m_UnitFact(defSystem,"Nanoseconds","Ns",new m_UnitFraction(defSystem,1.0e-6),4);

  t=new m_UnitFact(defSystem,"Microseconds","Us",new m_UnitFraction(defSystem,0.001),4);

  t=new m_UnitFact(defSystem,"Milliseconds","Ms",new m_UnitFraction(defSystem,0.0),4);

  t=new m_UnitFact(defSystem,"Seconds","S",new m_UnitFraction(defSystem,1000.0),4);

  t=new m_UnitFact(defSystem,"Minutes","Mn",new m_UnitFraction(defSystem,60000.0),4);

  t=new m_UnitFact(defSystem,"Hours","Hr",new m_UnitFraction(defSystem,60.0*60000.0),4);

  t=new m_UnitFact(defSystem,"Days","Dy",new m_UnitFraction(defSystem,24.0*60.0*60000.0),4);

  t=new m_UnitFact(defSystem,"Weeks","Wk",new m_UnitFraction(defSystem,7.0*24.0*60.0*60000.0),4);

  t=new m_UnitFact(defSystem,"Teaspoons","Tsp",new m_UnitFraction(defSystem,0.0),3);

  t=new m_UnitFact(defSystem,"Milliliters","Ml",new m_UnitFraction(defSystem,0.202884),3);

  t=new m_UnitFact(defSystem,"Tablespoons","Tbp",new m_UnitFraction(defSystem,3.0),3);

  t=new m_UnitFact(defSystem,"Cups","Cup",new m_UnitFraction(defSystem,48.0),3);

  t=new m_UnitFact(defSystem,"Pints","Pt",new m_UnitFraction(defSystem,95.9997728756778),3);

  t=new m_UnitFact(defSystem,"Quarts","Qt",new m_UnitFraction(defSystem,191.999545751356),3);

  t=new m_UnitFact(defSystem,"Gallons","Gal",new m_UnitFraction(defSystem,767.999636600397),3);

  t=new m_UnitFact(defSystem,"Liters","L",new m_UnitFraction(defSystem,202.884),3);

  t=new m_UnitFact(defSystem,"FluidOunces","Foz",new m_UnitFraction(defSystem,6),3);

  t=new m_UnitFact(defSystem,"Kilograms","Kg",new m_UnitFraction(defSystem,0.0),2);

  t=new m_UnitFact(defSystem,"Pounds","Lb",new m_UnitFraction(defSystem,0.453599081915458),2);

  t=new m_UnitFact(defSystem,"Ounces","Oz",new m_UnitFraction(defSystem,2.83499747685225e-02),2);

  t=new m_UnitFact(defSystem,"Milligrams","Mg",new m_UnitFraction(defSystem,1.0e-6),2);

  t=new m_UnitFact(defSystem,"Grams","Gm",new m_UnitFraction(defSystem,0.001),2);

  t=new m_UnitFact(defSystem,"Tons","Ton",new m_UnitFraction(defSystem,907.202278892125),2);

  return defSystem;

}



public boolean setStatus(String name, int delOnOff)

{m_UnitFact p;

  p=(m_UnitFact)f.us.getTag(name);

  if ((p==null)||(p.f.value==0.0)) return false;

  if (delOnOff==0) {

	  /*f.us.delete(this);*/

  } else if (p.id<99) return false;

  else if (delOnOff==1) p.id=99; //on

  else if (delOnOff==2) p.id=100; //off

  else p.id=(byte)(199-p.id);    //toggle

  return true;

}



public static int getId(String name) {

  m_UnitFact u=((m_UnitFact)defSystem.getTag(name));

  if (u==null) return 0;

  return u.id+1;

}

}