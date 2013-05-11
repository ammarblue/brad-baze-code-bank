package reuze.pending;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.software.reuze.ml_DPLL;
import com.software.reuze.ml_Model;
import com.software.reuze.ml_Parser;
import com.software.reuze.ml_SentenceAtomicSymbol;
import com.software.reuze.ml_a_ParseTreeSentence;

/*
 * Author: Cory Nance
 * Date:   October 9 2012
 */

public class demoLogic {

	private static ml_DPLL dpll;
	private static ml_Parser parser;

	public static void main(String[] args) {
		dpll = new ml_DPLL();
		parser = new ml_Parser();

		ml_Model model = new ml_Model();

		model = model.extend(new ml_SentenceAtomicSymbol("A"), true);
		model = model.extend(new ml_SentenceAtomicSymbol("B"), false);
		model = model.extend(new ml_SentenceAtomicSymbol("C"), false);
		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(is);
		String proposition;

		System.out
				.println("Please enter a propositional sentence.  You may use any letter other then T and F.");
		System.out.print("Sentence: ");
		try {
			proposition = in.readLine();
			if (proposition.isEmpty())
				throw new Exception("Empty String!!");
		} catch (Exception e) {
			System.out.println("Bad input: " + e.toString());
			System.out.println("I'll make a proposition for you!");
			System.out.println("((A OR B) OR (A => (B AND C)))");
			proposition = "((A OR B) OR (A => (B AND C)))";
		}
		proposition = proposition.toUpperCase();
		
		ArrayList<String> possibleSymbols = new ArrayList<String>();
		possibleSymbols.add("A");
		possibleSymbols.add("B");
		possibleSymbols.add("C");
		possibleSymbols.add("D");
		possibleSymbols.add("E");
		//possibleSymbols.add("F");
		possibleSymbols.add("G");
		possibleSymbols.add("H");
		possibleSymbols.add("I");
		possibleSymbols.add("J");
		possibleSymbols.add("K");
		possibleSymbols.add("L");
		possibleSymbols.add("M");
		possibleSymbols.add("N");
		possibleSymbols.add("O");
		possibleSymbols.add("P");
		possibleSymbols.add("Q");
		possibleSymbols.add("R");
		possibleSymbols.add("S");
		//possibleSymbols.add("T");
		possibleSymbols.add("U");
		possibleSymbols.add("V");
		possibleSymbols.add("W");
		possibleSymbols.add("X");
		possibleSymbols.add("Y");
		possibleSymbols.add("Z");

		for (String p : possibleSymbols) {
			if (proposition.replace("AND", "").replace("OR", "").replace("NOT","").contains(p)) {
				System.out.print("Enter a boolean value for " + p + ": ");
				boolean a;
				try {
					String input = in.readLine().trim();
					if (input.equalsIgnoreCase("T")) input = "True";
					if (input.equalsIgnoreCase("1")) input = "True";
					if (input.equalsIgnoreCase("F")) input = "False";
					if (input.equalsIgnoreCase("0")) input = "False";
					a = Boolean.parseBoolean(input);
				} catch (Exception e) {
					System.out.println("Bad input: " + e.toString());
					System.out.println("I'll set " + p + " = True for you.");
					a = true;
				}
				System.out.println("Setting " + p + " = " + a);
				model = model.extend(new ml_SentenceAtomicSymbol(p), a);
			}
		}

		ml_a_ParseTreeSentence sentence = (ml_a_ParseTreeSentence) parser.parse(proposition);

		boolean result = dpll.dpllSatisfiable(sentence, model);

		System.out.println("Result: " + result);

	}

}
