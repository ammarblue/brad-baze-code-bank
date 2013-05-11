package com.software.reuze;
//package com.example.android.notepad2;
import java.util.Vector;

import java.util.Enumeration;

public class m_UnitSystem {

	Vector v=new Vector();

        Object p; int n=0;

	Object defaults[]=new Object[8];



public Object getTag(String name) {

	Enumeration e=v.elements();

        while (e.hasMoreElements()) {

          Object uf=e.nextElement();

          if (uf.equals(name)) return uf;

        };

        return null;

}



public Enumeration elements() {

  return v.elements();

}



public void addTag(int pos, boolean defaultt, Object fact) {

  v.addElement(fact);

  if (defaultt) {

    n++;

    defaults[pos]=fact;

  }

}

/*

public void delete(Object o) {

        Enumeration e=v.elements();

        while (e.hasMoreElements()) {

          Object uf=e.nextElement();

          if (e==o) {v.remove(e); break;};

        };

}

*/

public Object setDefault(String name, int id)

{Object p,t;

        p=getTag(name);

	if (p==null) return null;

	t=defaults[id-1];

	defaults[id-1]=p;

	return t;

}

}