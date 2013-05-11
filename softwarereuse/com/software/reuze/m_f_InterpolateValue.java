package com.software.reuze;

public final class m_f_InterpolateValue {
	public static m_i_InterpolateValue create(String propertyList) { //type=linear etc.
		char c = propertyList.charAt(5), d = propertyList.charAt(6), e = propertyList.charAt(7);
		m_i_InterpolateValue i=null;
		if (c=='l') return new m_InterpolateValueLinear(); //type=linear
		else if (c=='f') return new m_InterpolateValueFade(); //type=linear
		else if (c=='b' && d=='e') {
			i=new m_InterpolateValueBezier();
			if (!set(propertyList, i, "c1", "c2")) return null;
		} else if (c=='b' && d=='o') {
			i=new m_InterpolateValueBounce();
			if (!set(propertyList, i, "inout", "bounces")) return null;
		} else if (propertyList.compareTo("circle")==0) {
			i=new m_InterpolateValueCircle();
			if (!set(propertyList, i, "inout")) return null;
		} else if (c=='c' && d=='i') {
			i=new m_InterpolateValueCircular();
			if (!set(propertyList, i, "flip")) return null;
		} else if (c=='c' && d=='o') {
			i=new m_InterpolateValueCosine();
		} else if (c=='d') {
			int j=propertyList.indexOf("strategy");
			if (j<=0) {
				i=new m_InterpolateValueDecimated(0);
			} else {
				m_i_InterpolateValue k=create("type"+propertyList.substring(j+8));
				if (k==null) return null;
				i=new m_InterpolateValueDecimated(0,k);
			}
			if (!set(propertyList, i, "steps")) return null;
		} else if (c=='e' && d=='l') {
			i=new m_InterpolateValueElastic();
			if (!set(propertyList, i, "inout", "value", "power")) return null;
		} else if (c=='e' && d=='x') {
			i=new m_InterpolateValueExp();
			if (!set(propertyList, i, "inout", "value", "power")) return null;
		} else if (c=='p') {
			i=new m_InterpolateValuePow();
			if (!set(propertyList, i, "power", "inout")) return null;
		} else if (c=='s' && d=='i' && e=='g') {
			i=new m_InterpolateValueSigmoid();
			if (!set(propertyList, i, "sharpness")) return null;
		} else if (c=='s' && d=='i' && e=='n') {
			i=new m_InterpolateValueSine();
			if (!set(propertyList, i, "inout")) return null;
		} else if (c=='s' && d=='w') {
			i=new m_InterpolateValueSwing();
			if (!set(propertyList, i, "scale", "inout")) return null;
		} else if (c=='t') {
			i=new m_InterpolateValueThreshold();
			if (!set(propertyList, i, "fraction")) return null;
		} else if (c=='z') {
			i=new m_InterpolateValueZoomLens();
			if (!set(propertyList, i, "smooth", "position", "strength")) return null;
		}
		return i;
	}
	public static String tostring() {
		//inout=0 regular, 1 in, 2 out
		return "bezier[c1,c2],bounce[inout,bounces],circle[inout],circular[flip],cosine,decimated[steps,strategy[...]],elastic[value,power,inout],exp[value,power,inout],fade,linear,pow[power,inout],sigmoid[sharpness],sine[inout],swing[scale,inout],threshold[fraction],zoomlens[smooth,position,strength]";
	}
	private static boolean set(String propertyList, m_i_InterpolateValue i, String... names) {
		for (String n:names) {
			float f=d_PropertyStringReader.getFloat(propertyList, n);
			if (f==Float.NaN) return false;
			i.set(n, f);
		}
		return true;
	}
	public static void main(String args[]) {
		System.out.println(m_f_InterpolateValue.tostring());
		m_i_InterpolateValue i=m_f_InterpolateValue.create("type=linear");
		for (float j=0; j<=1.01; j+=0.1f) System.out.println(j+" "+i.interpolate(5, 25, j));

		i=m_f_InterpolateValue.create("type=bezier c1=9 c2=8");
		for (float j=0; j<=1.01; j+=0.1f) System.out.println(j+" "+i.interpolate(5, 25, j));
		
		i=m_f_InterpolateValue.create("type=circular flip=0");
		for (float j=0; j<=1.01; j+=0.1f) System.out.println(j+" "+i.interpolate(5, 25, j));
		
		i=m_f_InterpolateValue.create("type=cosine");
		for (float j=0; j<=1.01; j+=0.1f) System.out.println(j+" "+i.interpolate(5, 25, j));
		
		i=m_f_InterpolateValue.create("type=sigmoid sharpness=10");
		for (float j=0; j<=1.01; j+=0.1f) System.out.println(j+" "+i.interpolate(5, 25, j));
		
		i=m_f_InterpolateValue.create("type=threshold fraction=0.5");
		for (float j=0; j<=1.01; j+=0.1f) System.out.println(j+" "+i.interpolate(5, 25, j));
		
		i=m_f_InterpolateValue.create("type=zoomlens smooth=0.9 position=15 strength=0.6");
		for (float j=0; j<=1.01; j+=0.1f) System.out.println(j+" "+i.interpolate(5, 25, j));
		
		i=m_f_InterpolateValue.create("type=decimated steps=4 strategy=sigmoid sharpness=10");
		for (float j=0; j<=1.01; j+=0.1f) System.out.println(j+" "+i.interpolate(5, 25, j));
	}
}
