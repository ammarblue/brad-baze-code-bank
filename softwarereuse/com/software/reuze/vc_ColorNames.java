package com.software.reuze;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.StringTokenizer;
import java.lang.reflect.Field;


/**
 * HTML color names. It's intended use is for parsing a name and return the
 * corresponding color or return a name for a given color.
 * @author Adrian Ber
 * @author from package com.greef.ui project jcalendar @koders.com
 * @author modified by R. Cook for libgdx
 */
public class vc_ColorNames extends vc_ColorNamesInt {

    /** Don't instantiate this, use only the static methods */
    private vc_ColorNames() {
    }

    /** Returns a color with the specified case-insensitive name.*/
    public static String getName(z_Colors color) {
		int i,c=color.toARGB()&0xffffff;
		i=(c&0xff00) | ((c&0xff)<<16) | ((c&0xff0000)>>16);
		for (Map.Entry m:name2color.entrySet()) {
			if (((Integer)m.getValue()).intValue()==i) {
				return (String)m.getKey();
			}
		}
        return null;
    }

    /** Returns a color with the specified case-insensitive name.*/
    public static z_Colors getColor(String name) {
        return getColor(name, 1);
    }
	public static z_Colors getColor(String name, float alpha) {
		if ((name==null)||(name.length()==0)) return z_Colors.black;
		char z=name.charAt(0);
		int i=0;
		if (z=='#') {
			if (name.length()==4) {
				int rx=Integer.valueOf(name.substring(1,2), 16);
				int gx=Integer.valueOf(name.substring(2,3), 16);
				int bx=Integer.valueOf(name.substring(3,4), 16);
				return new z_Colors(rx*0x11,gx*0x11,bx*0x11,(int)(alpha*255f));
			}
			i = Integer.valueOf(name.substring(1), 16);
		} else if (isHex(name)) i = Integer.valueOf(name, 16);
		else if ((z=='r')&&(name.charAt(1)=='g')) {
			float rr,gg,bb;
			boolean pct=name.indexOf('%')>0;
			StringTokenizer st=new StringTokenizer(name,",)%",false);
			rr=Float.valueOf(st.nextToken().substring(4));
			gg=Float.valueOf(st.nextToken());
			bb=Float.valueOf(st.nextToken());
			if (!pct) return new z_Colors(rr/255f, gg/255f, bb/255f, alpha);
			return new z_Colors(rr/100.0f,gg/100.0f,bb/100.0f,alpha);
		} else
			i=name2color.get(name.toLowerCase()).intValue();
        return new z_Colors(
						 ((i>>16)&0xff)/255f,
						 ((i>>8)&0xff)/255f,
						 (i&0xff)/255f, alpha
						 );
    }

}

