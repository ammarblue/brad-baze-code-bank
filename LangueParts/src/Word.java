public class Word implements Comparable<Word> {
	public String Text;
	public long Tags;

	public Word(String text) {
		this(text, 0L);
	}

	public Word(String text, long tags) {
		Text = text;
		Tags = tags;
	}

	public int compareTo(Word other) {
		return Text.compareTo(other.Text);
	}

	public boolean equals(Object other) {
		if (other instanceof Word) {
			return Text.equals(((Word) other).Text);
		}
		if (other instanceof Long) {
			return Tags == ((Long) other).longValue();
		}
		return false;
	}

	public int hashCode() {
		return Text.hashCode();
	}

	public void AddTag(long tags) {
		Tags |= tags;
	}

	public boolean HasTag(long tag) {
		return (Tags & tag) != 0;
	}

	public String toString() {
		return Text;
	}
}
