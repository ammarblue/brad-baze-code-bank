import java.util.ArrayList;

public class Stats {
	Tagger t;
	String[] texts;
	int[] tags;
	int[] tagID;
	Word[] words;
	ArrayList<elms> wordList = new ArrayList<elms>();

	Stats() {
	}

	Stats(Tagger in) {
		t = in;
	}

	Stats(Tagger in, String[] a, int[] b, Word[] c) {
		texts = a;
		tags = b;
		t = in;
		words = c;
		tagID = new int[50];
	}

	public void calc() {//foundation calc
		for (int i = 0; i < texts.length; i++) {
			tagID[tags[i]]++;
			if (!dup(texts[i])) {
				wordList.add(new elms(texts[i], 1, t.Tags
						.GetStringFromIndex(tags[i])));
			}
		}
	}

	public boolean dup(String in) {//looks for doubles
		boolean ret = false;
		for (int i = 0; i < wordList.size(); i++) {
			if (in.compareTo(wordList.get(i).word) == 0) {
				ret = true;
				wordList.get(i).count++;
			} else {
				continue;
			}
		}
		return ret;
	}
	public String occur(int range, String type) {//counts number of times something happens 
		if(range>texts.length){
			return "ERROR range>word count";
		}
		boolean state = true;
		String ret = "";
		elms[] data = new elms[range];
		for (int i = 0; i < range; i++) {
			data[i] = new elms("", 0, "");
		}
		if (wordList == null) {
			System.err.println("Need to run cal first");
			return "";
		}
		for (int i = 0; i < wordList.size(); i++) {
			for (int j = 0; j < data.length; j++) {
				if (data[j].count < wordList.get(i).count
						&& data[j].word.compareTo(wordList.get(i).word) != 0
						&& wordList.get(i).type.contains(type)) {
					data[j] = wordList.get(i);
					i++;
				}
			}
		}
		for (int i = 0; i < data.length; i++) {
			ret = ret.concat(data[i].word+" "+data[i].count+"\n");
		}
		return ret;
	}

	class elms {//elements structure
		public elms(String string, int i, String t) {
			word = string;
			count = i;
			type = t;
		}
		int count;
		String word;
		String type;
	}
}
