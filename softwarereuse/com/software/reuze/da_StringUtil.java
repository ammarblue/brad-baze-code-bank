package com.software.reuze;

public final class da_StringUtil {
	public static String replace(String target, String sub, String rep, boolean nocase) {
	      int i,prev=0,j=sub.length();
	      if (nocase) {target=target.toLowerCase(); sub=sub.toLowerCase();};
	      for (;;) {
	        i=target.indexOf(sub,prev);
	        if (i<0) break;
	        target=target.substring(0,i)+rep+target.substring(i+j);
	        prev=i+rep.length();
	      };
	      return target;
	    }
}
