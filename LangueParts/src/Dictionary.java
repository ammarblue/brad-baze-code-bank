import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Dictionary {
	private TagSet Tags;
	private ArrayList<Word> Words;
	private boolean IsSorted;

	public Dictionary(TagSet tags) {
		Tags = tags;
		Words = new ArrayList<Word>();
		IsSorted = false;
	}

	public Word GetWord(String word) {
		if (IsSorted) {
			int index = Collections.binarySearch(Words, new Word(word));

			if (index < 0) {
				return null;
			}

			return Words.get(index);
		} else {
			for (int i = 0; i < Words.size(); ++i) {
				if (Words.get(i).Text.equals(word)) {
					return Words.get(i);
				}
			}
			return null;
		}
	}

	public Word GetWord(int index) {
		return Words.get(index);
	}

	public void Sort() {
		Collections.sort(Words);
		IsSorted = true;
	}

	public void Sort(Comparator<Word> comparator) {
		Collections.sort(Words, comparator);
		IsSorted = false;
	}

	public int NumWords() {
		return Words.size();
	}

	public Word AddWord(String text) {
		Word word = GetWord(text);
		if (word == null) {
			word = new Word(text);
			Words.add(word);
			IsSorted = false;
		}
		return word;
	}

	public void Load(Reader reader) {
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(reader);

			String line = null;
			while ((line = buffer.readLine()) != null) {
				String[] tokens = line.split("\\s+");

				// Ignore empty lines and comments
				if (tokens.length == 0 || tokens[0].startsWith("#")
						|| tokens[0].startsWith("//")) {
					continue;
				}

				Word current_word = AddWord(tokens[0]);
				for (int i = 1; i < tokens.length; ++i) {
					int tag_index = Tags.GetIndexFromString(tokens[i]);
					if (tag_index > -1) // This token is a new tag:
					{
						current_word.AddTag(1L << tag_index);
					}
				}
			}
			buffer.close();
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException();
		} finally {
			if (buffer != null)
				try {
					buffer.close();
				} catch (IOException ignored) {
				}
		}
	}

	public void Save(String file_name) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file_name));

			for (int i = 0; i < Words.size(); ++i) {
				Word word = Words.get(i);
				out.println(word.Text + " "
						+ Tags.GetAllStringsFromBitField(word.Tags));
			}
			out.close();
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException();
		}
	}
}
