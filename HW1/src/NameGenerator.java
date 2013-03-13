/* Ashleigh Amrine
 * CSCI 1302A
 * 9/20/2012
 * 
 * Generates random String
 * 
 * This code retrieved from: http://www.osintegrators.com/node/71
 */

import java.util.Random;

public class NameGenerator {

	public final static short TYPE_MIXED_CASE = 0;
	public final static short TYPE_UPPER_ONLY = 1;
	public final static short TYPE_LOWER_ONLY = 2;
	public final static Random rnd = new Random();
	
	static final char[] alphas = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			         			  'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

	public static String generateRandomString(short type,int length, boolean initialCaps){
		
		int min = type == TYPE_LOWER_ONLY ? 26 : 0;
		int max = type == TYPE_UPPER_ONLY ? 26 : alphas.length;
		String generated = "";
		
		for (int i = 0; i < length; i++){
			int random = rnd.nextInt(max - min) + min;
			generated += alphas[random];
		}
		
		generated = initialCaps ? (""+generated.charAt(0)).toUpperCase() + generated.substring(1) : generated;
		return generated;
	}

	public static String generateRandomString(short type,int length) {
		return generateRandomString(type, length, false);
	}
}
