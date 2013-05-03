import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

public class TagSet {
	private String[] Tags;

	public TagSet(Reader reader) {
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(reader);
			String line = buffer.readLine();
			Tags = line.split("\\s+");
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

	public int Size() {
		return Tags.length;
	}

	public int GetIndexFromString(String tag) {
		for (int index = 0; index < Tags.length; ++index) {
			if (Tags[index].equals(tag)) {
				return index;
			}
		}

		return -1;
	}

	public long GetBitsFromString(String tag) {
		int index = GetIndexFromString(tag);
		if (index > -1) {
			return 1L << index;
		}
		return 0;
	}

	public int GetIndexFromBits(long bits) {
		if (bits < 0 || bits > 1L << Tags.length) {
			return -1;
		}

		int index = 0;

		while ((bits >>= 1) != 0) {
			++index;
		}

		return index;
	}

	public String GetStringFromBits(long bits) {
		int index = GetIndexFromBits(bits);
		return GetStringFromIndex(index);
	}

	public String GetAllStringsFromBitField(long bit_field) {
		boolean needs_a_space = false;
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < Tags.length; ++i) {
			if ((bit_field & 1L << i) > 0) {
				if (needs_a_space) {
					buffer.append(" ");
				}
				buffer.append(Tags[i]);
				needs_a_space = true;
			}
		}

		return buffer.toString();
	}

	public String GetStringFromIndex(int index) {
		if (index < 0 || index >= Tags.length) {
			return "???";
		}

		return Tags[index];
	}

	public int GetNextTagFromBitField(long tags, int current_tag) {
		for (int shift = current_tag + 1; shift < Tags.length; ++shift) {
			if ((tags & (1L << shift)) != 0L) {
				return shift;
			}
		}
		return 0;
	}
}
