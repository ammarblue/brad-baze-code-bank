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
public class vc_ColorNamesInt {

    /** Don't instantiate this, use only the static methods */
    protected vc_ColorNamesInt() {
    }

    /** map between color names and colors;
     * there are fields for every color. we use a map because it is a faster
     * way to get the color
     */
    protected static Map<String, Integer> name2color = new HashMap<String, Integer>();

    /** Initialize colors map */
    private static void initColorsMap() {
        Field[] fields = vc_ColorNamesInt.class.getFields();
        for (Field field : fields) {
            //if (field.getType().isAssignableFrom(Integer.class)) {
                addColor(field.getName());
            //}
        }
    }

    /** Used to initialize or to add to the map */
    public static void addColor(String colorName, Integer color) {
        name2color.put(colorName, color);
    }

    /** Used to initialize the map */
    private static void addColor(String colorName) {
        addColor(colorName, getColorFromField(colorName));
    }

    /** Used to initialize the map */
    private static void addColor(String colorName, int colorRGB) {
        addColor(colorName, colorRGB);
    }

    /** Returns a color with the specified case-insensitive name. */
    private static Integer getColorFromField(String name) {
        try {
            Field colorField = vc_ColorNamesInt.class.getField(name.toLowerCase());
            return (Integer) colorField.get(vc_ColorNamesInt.class);
        }
        catch (NoSuchFieldException exc) {
        }
        catch (SecurityException exc) {
        }
        catch (IllegalAccessException exc) {
        }
        catch (IllegalArgumentException exc) {
        }
        return null;
    }

    /** Returns a color with the specified case-insensitive name from ARGB.*/
    public static String getName(int color) {
		int i,c=color&0xffffff;
		i=(c&0xff00) | ((c&0xff)<<16) | ((c&0xff0000)>>16);
		for (Map.Entry m:name2color.entrySet()) {
			if (((Integer)m.getValue()).intValue()==i) {
				return (String)m.getKey();
			}
		}
        return null;
    }

    /** Returns an int color with the specified case-insensitive name.*/
    public static int getARGB(String name) {
        return getARGB(name, 1);
    }
    /** Returns an int color with the specified case-insensitive name.*/
    public static float get(String name) {/*RPC0812*/
    	// This mask avoids using bits in the NaN range. See Float.intBitsToFloat javadocs.
		// This unfortunately means we don't get the full range of alpha.
		return Float.intBitsToFloat(getARGB(name, 1) & 0xfeffffff);
    }
    public static int toARGB(int r, int g, int b, int a) {
    	return (a&0xff)<<24 | (r&0xff)<<16 | (g&0xff)<<8 | (b&0xff);
    }
    public static int toARGB(float r, float g, float b, float a) {
    	int aa=(int) (a*255);
    	int rr=(int) (r*255);
    	int gg=(int) (g*255);
    	int bb=(int) (b*255);
    	return toARGB(rr,gg,bb,aa);
    }
	public static int getARGB(String name, float alpha) {
		if ((name==null)||(name.length()==0)) return black;
		char z=name.charAt(0);
		int i=0;
		if (z=='#') {
			if (name.length()==4) {
				int rx=Integer.valueOf(name.substring(1,2), 16);
				int gx=Integer.valueOf(name.substring(2,3), 16);
				int bx=Integer.valueOf(name.substring(3,4), 16);
				return toARGB(rx*0x11,gx*0x11,bx*0x11,(int)(alpha*255f));
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
			if (!pct) return toARGB(rr/255f, gg/255f, bb/255f, alpha);
			return toARGB(rr/100.0f,gg/100.0f,bb/100.0f,alpha);
		} else i=name2color.get(name.toLowerCase()).intValue();
        return toARGB(
						 ((i>>16)&0xff)/255f,
						 ((i>>8)&0xff)/255f,
						 (i&0xff)/255f, alpha
						 );
    }
    protected static boolean isHex(String s) {
		for (int i=0; i<s.length(); i++) {
			char c=s.charAt(i);
		    if (Character.isDigit(c)) continue;
		    if ((c>='a' && c<='f') || (c>='A' && c<='F')) continue;
		    return false;
		}
		return true;
	}
	/** Returns a color with the specified case-insensitive name.*/
    /*public static float getFloat(String name) {
        int i=name2color.get(name.toLowerCase()).intValue();
        return Color.toFloatBits(
						 (i>>16)&0xff,
						 (i>>8)&0xff,
						 i&0xff, 255
						 );
    }
	public static float getFloat(String name, float alpha) {
        int i=name2color.get(name.toLowerCase()).intValue();
        return Color.toFloatBits(
						 (i>>16)&0xff,
						 (i>>8)&0xff,
						 i&0xff, (int)(alpha*255f)
						 );
    }*/

    /** Returns a collection of all color names */
    public static Collection<String> colors() {
        return name2color.keySet();
    }

    public static final int aliceblue = 0xf0f8ff;
    public static final int antiquewhite = 0xfaebd7;
    public static final int aqua = 0x00ffff;
    public static final int aquamarine = 0x7fffd4;
    public static final int azure = 0xf0ffff;
    public static final int beige = 0xf5f5dc;
    public static final int bisque = 0xffe4c4;
    public static final int black = 0x000000;
    public static final int blanchedalmond = 0xffebcd;
    public static final int blue = 0x0000ff;
    public static final int blueviolet = 0x8a2be2;
    public static final int brown = 0xa52a2a;
    public static final int burlywood = 0xdeb887;
    public static final int cadetblue = 0x5f9ea0;
    public static final int chartreuse = 0x7fff00;
    public static final int chocolate = 0xd2691e;
    public static final int coral = 0xff7f50;
    public static final int cornflowerblue = 0x6495ed;
    public static final int cornsilk = 0xfff8dc;
    public static final int crimson = 0xdc143c;
    public static final int cyan = 0x00ffff;
    public static final int darkblue = 0x00008b;
    public static final int darkcyan = 0x008b8b;
    public static final int darkgoldenrod = 0xb8860b;
    public static final int darkgray = 0xa9a9a9;
    public static final int darkgreen = 0x006400;
    public static final int darkkhaki = 0xbdb76b;
    public static final int darkmagenta = 0x8b008b;
    public static final int darkolivegreen = 0x556b2f;
    public static final int darkorange = 0xff8c00;
    public static final int darkorchid = 0x9932cc;
    public static final int darkred = 0x8b0000;
    public static final int darksalmon = 0xe9967a;
    public static final int darkseagreen = 0x8fbc8f;
    public static final int darkslateblue = 0x483d8b;
    public static final int darkslategray = 0x2f4f4f;
    public static final int darkturquoise = 0x00ced1;
    public static final int darkviolet = 0x9400d3;
    public static final int deeppink = 0xff1493;
    public static final int deepskyblue = 0x00bfff;
    public static final int dimgray = 0x696969;
    public static final int dodgerblue = 0x1e90ff;
    public static final int firebrick = 0xb22222;
    public static final int floralwhite = 0xfffaf0;
    public static final int forestgreen = 0x228b22;
    public static final int fuchsia = 0xff00ff;
    public static final int gainsboro = 0xdcdcdc;
    public static final int ghostwhite = 0xf8f8ff;
    public static final int gold = 0xffd700;
    public static final int goldenrod = 0xdaa520;
    public static final int gray = 0x808080;
    public static final int green = 0x008000;
    public static final int greenyellow = 0xadff2f;
    public static final int honeydew = 0xf0fff0;
    public static final int hotpink = 0xff69b4;
    public static final int indianred = 0xcd5c5c;
    public static final int indigo = 0x4b0082;
    public static final int ivory = 0xfffff0;
    public static final int khaki = 0xf0e68c;
    public static final int lavender = 0xe6e6fa;
    public static final int lavenderblush = 0xfff0f5;
    public static final int lawngreen = 0x7cfc00;
    public static final int lemonchiffon = 0xfffacd;
    public static final int lightblue = 0xadd8e6;
    public static final int lightcoral = 0xf08080;
    public static final int lightcyan = 0xe0ffff;
    public static final int lightgoldenrodyellow = 0xfafad2;
    public static final int lightgreen = 0x90ee90;
    public static final int lightgrey = 0xd3d3d3;
    public static final int lightpink = 0xffb6c1;
    public static final int lightsalmon = 0xffa07a;
    public static final int lightseagreen = 0x20b2aa;
    public static final int lightskyblue = 0x87cefa;
    public static final int lightslategray = 0x778899;
    public static final int lightsteelblue = 0xb0c4de;
    public static final int lightyellow = 0xffffe0;
    public static final int lime = 0x00ff00;
    public static final int limegreen = 0x32cd32;
    public static final int linen = 0xfaf0e6;
    public static final int magenta = 0xff00ff;
    public static final int maroon = 0x800000;
    public static final int mediumaquamarine = 0x66cdaa;
    public static final int mediumblue = 0x0000cd;
    public static final int mediumorchid = 0xba55d3;
    public static final int mediumpurple = 0x9370db;
    public static final int mediumseagreen = 0x3cb371;
    public static final int mediumslateblue = 0x7b68ee;
    public static final int mediumspringgreen = 0x00fa9a;
    public static final int mediumturquoise = 0x48d1cc;
    public static final int mediumvioletred = 0xc71585;
    public static final int midnightblue = 0x191970;
    public static final int mintcream = 0xf5fffa;
    public static final int mistyrose = 0xffe4e1;
    public static final int moccasin = 0xffe4b5;
    public static final int navajowhite = 0xffdead;
    public static final int navy = 0x000080;
    public static final int oldlace = 0xfdf5e6;
    public static final int olive = 0x808000;
    public static final int olivedrab = 0x6b8e23;
    public static final int orange = 0xffa500;
    public static final int orangered = 0xff4500;
    public static final int orchid = 0xda70d6;
    public static final int palegoldenrod = 0xeee8aa;
    public static final int palegreen = 0x98fb98;
    public static final int paleturquoise = 0xafeeee;
    public static final int palevioletred = 0xdb7093;
    public static final int papayawhip = 0xffefd5;
    public static final int peachpuff = 0xffdab9;
    public static final int peru = 0xcd853f;
    public static final int pink = 0xffc0cb;
    public static final int plum = 0xdda0dd;
    public static final int powderblue = 0xb0e0e6;
    public static final int purple = 0x800080;
    public static final int red = 0xff0000;
    public static final int rosybrown = 0xbc8f8f;
    public static final int royalblue = 0x4169e1;
    public static final int saddlebrown = 0x8b4513;
    public static final int salmon = 0xfa8072;
    public static final int sandybrown = 0xf4a460;
    public static final int seagreen = 0x2e8b57;
    public static final int seashell = 0xfff5ee;
    public static final int sienna = 0xa0522d;
    public static final int silver = 0xc0c0c0;
    public static final int skyblue = 0x87ceeb;
    public static final int slateblue = 0x6a5acd;
    public static final int slategray = 0x708090;
    public static final int snow = 0xfffafa;
    public static final int springgreen = 0x00ff7f;
    public static final int steelblue = 0x4682b4;
    public static final int tan = 0xd2b48c;
    public static final int teal = 0x008080;
    public static final int thistle = 0xd8bfd8;
    public static final int tomato = 0xff6347;
    public static final int turquoise = 0x40e0d0;
    public static final int violet = 0xee82ee;
    public static final int wheat = 0xf5deb3;
    public static final int white = 0xffffff;
    public static final int whitesmoke = 0xf5f5f5;
    public static final int yellow = 0xffff00;
    public static final int yellowgreen = 0x9acd32;

    static {
        initColorsMap();
    }
public static void main(String args[]) {
  System.out.println(name2color.get("aqua"));
  System.out.println(getColorFromField("aqua"));
  System.out.println(getName(0xff0000));
  System.out.println(getName(0xfff5f5f5));
}

}
/*RPC0812 added an option to retrieve float colors*/