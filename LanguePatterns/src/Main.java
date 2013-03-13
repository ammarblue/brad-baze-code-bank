import java.util.*;
public class Main {
	String English;
	String German;
	public static void main(String args[]){
		Data E=new Data();
		E.parse();
		E.length();
		E.Num_of_letters();
		E.Ratio();
		E.printOut();
		E.RatePrint();
		E.WhatLangue();
		/*Data G=new Data();
		G.parse();
		G.length();
		G.Num_of_letters();
		G.Ratio();
		G.printOut();
		G.RatePrint();
		G.WhatLangue();*/
	}
}
