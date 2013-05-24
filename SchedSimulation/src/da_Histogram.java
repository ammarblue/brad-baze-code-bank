import java.util.ArrayList;

public class da_Histogram {
	static ArrayList<da_Histogram> list = new ArrayList<da_Histogram>();
	String name;
	int numIntervals;
	long numEntries;
	double sumEntries;
	double sumSquared;
	int lower;
	static int step;
	long hist[];
	static ArrayList<Integer> Gdata = new ArrayList<Integer>();

	public da_Histogram(String name, int lowerbound, int stepamt,
			int numIntervals) {
		this.name = name;
		lower = lowerbound;
		step = stepamt;
		this.numIntervals = numIntervals;
		hist = new long[numIntervals + 2];
		if (stepamt <= 0 || numIntervals <= 0)
			throw new RuntimeException("histogram arguments <=0");
		list.add(this);

	}

	/* Tallies x into the table data */
	public void Tabulate(int x) {
		Tabulate((double) x);
	}

	public void Tabulate(long x) {
		Tabulate((double) x);
	}

	public void Tabulate(float x) {
		Tabulate((double) x);
	}

	public void Tabulate(double x) {
		numEntries++;
		sumEntries += x;
		sumSquared += x * x;
		int slot = 0;
		if (x < lower) {
		} else if (x >= (lower + step * numIntervals + step)) {
			slot = numIntervals + 1;
		} else {
			slot = (int) ((x - lower) / step);
		}
		hist[slot]++;
	}

	/* Prints out all associated data with tables and clears them */
	public static void Print() {

		if (list.isEmpty())
			return;
		System.out.println("Histogram\t\tnEntries\tMean\tStdDev");
		for (da_Histogram s : list) {
			double d;
			if (s.numEntries == 0) {
				System.out.println(s.name + "\t\t   NO ENTRIES");
				continue;
			}
			if (s.numEntries == 1) {
				System.out.println(s.name + "\t\t   ONE ENTRY=" + s.sumEntries);
				continue;
			}
			d = s.sumEntries * s.sumEntries / s.numEntries;
			d = s.sumSquared - d;
			d = d / (s.numEntries - 1.0);
			d = Math.sqrt(d);
			System.out.printf("%s%.3f\t%.3f\n", s.name + "\t\t  "
					+ s.numEntries + "\t\t", s.sumEntries / s.numEntries, d);
			System.out
					.println("UpperBound\tnEntries\tPercent\tCumulativePercent");

			int i = s.lower, j = 0, k = s.numIntervals + 2;

			for (long l : s.hist) {
				j += l;
				k--;
				if (k <= 0)
					System.out.print(">=");
				System.out.printf("%s\t\t%.2f\t     %.2f\n", "   " + i
						+ "\t\t    " + l, ((float) l / s.numEntries),
						((float) j / s.numEntries));
				Gdata.add((int) (((float) j / s.numEntries) * 100));
				i += s.step;
			}
		}
	}

	public static int[] getData() {
		int[] temp = new int[Gdata.size() - 1];
		for (int i = 0; i < Gdata.size() - 1; i++) {
			temp[i] = Gdata.get(i).intValue();
		}
		return temp;
	}

	public static int getSteps() {
		return step;
	}
}
