import java.util.*;
import java.util.regex.*;

public class Tokeniser {
	public static String[] Tokenise(String sentence) {
		// This aims to be compatible with the Penn Treebank tokenizer.

		// Input should be split into sentences, and periods will be assumed
		// to be part of a word unless they are at the end of the string.

		// This inserts a space before the "n't" part of a word.
		String nt_string = "[nN]'[tT]";
		String quote = "\"";
		sentence = sentence.replaceAll(nt_string, " $0");

		// Everything else is tokenised with one giant regular expression.
		String word_characters = "\\'?[\\w\\-]+";
		String parentheses = "[\\(\\[\\{\\<\\)\\]\\}\\>]";
		String sentence_terminators = "[\\?\\!]|\\.+$";
		String other_symbols = "[\\,\\;\\:\\@\\#\\$\\%\\&\\*\\']+";
		String number = "[0-9\\,\\.\\-\\\"]+";

		ArrayList<String> token_list = new ArrayList<String>();
		Pattern pattern = Pattern.compile("(" + nt_string + "|" + quote + "|"
				+ number + "|" + word_characters + "|" + parentheses + "|"
				+ other_symbols + "|" + sentence_terminators + ")",
				Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(sentence);
		while (matcher.find()) {
			token_list.add(matcher.group(1));
		}

		String[] tokens = new String[token_list.size()];
		tokens = token_list.toArray(tokens);
		return tokens;
	}
}
