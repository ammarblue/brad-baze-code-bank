package com.software.reuze;
import java.text.MessageFormat;
import java.util.Locale;

public class d_MessageString {

	
	public static String build(String pattern, Object ... arguments){
		return (pattern == null) ? "" : new MessageFormat(pattern, Locale.UK).format(arguments);        
	}
	
	public static String println(String pattern, Object ... arguments){
		String s = build(pattern, arguments);
		System.out.println(s);
		return s;
	}
}
