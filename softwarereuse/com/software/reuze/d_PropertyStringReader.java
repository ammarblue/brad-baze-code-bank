package com.software.reuze;

public class d_PropertyStringReader {

	public static float getFloat(String source, String name) {
		int j,i=source.indexOf(name);
		if (i<0) return Float.NaN;
		j=source.indexOf(' ',i+3);
		if (j<0) j=source.length();
		return Float.valueOf(source.substring(name.length()+i+1,j));
	}
	public static String getString(String source, String name) {
		int j,i=source.indexOf(name);
		if (i<0) return null;
		j=source.indexOf(' ',i+3);
		if (j<0) j=source.length();
		return source.substring(name.length()+i+1,j);
	}
}
