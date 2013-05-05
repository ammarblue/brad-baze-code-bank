import java.util.*;

public class Main {
	public static void main(String[] args) {
		Selection test = new Selection();
	}
}

class Selection {
	int prevW = 0;
	int side = 0;
	long time = 0;
	long time2 = 0;

	Selection() {
		int arr[] = new int[100000];
		for (int i = 0; i < 100000; i++) {
			arr[i] = (int) (Math.random() * 10000);
		}
		System.out.println("------------------------------------------------------------------");
		System.out.println("Integrate f(x)=1-x^2:");
		System.out.println("Number of steps 100            f(x):" + Carlo(100)
				+ "      time of:" + time + "(ms)");
		System.out.println("Number of steps 1000           f(x):" + Carlo(1000)
				+ "     time of:" + time + "(ms)");
		System.out.println("Number of steps 10000          f(x):" + Carlo(10000)
				+ "    time of:" + time + "(ms)");
		System.out.println("Number of steps 100000         f(x):" + Carlo(100000)
				+ "   time of:" + time + "(ms)");
		System.out.println("Number of steps 1000000        f(x):" + Carlo(1000000)
				+ "  time of:" + time + "(ms)");
		System.out.println("Number of steps 10000000       f(x):" + Carlo(10000000)
				+ " time of:" + time + "(ms)");
		System.out.println("------------------------------------------------------------------");
		//System.out.println("Random Select:");
		//System.out.println("Array of 100000 elms to find 300th statistic:");
		long start = System.nanoTime();
		//System.out.println("Value is:" + random_select(arr, 1, arr.length, 300));
		long finish = System.nanoTime();
		time2 = ((finish - start) / 1000);
		//System.out.println("Time to finish: " + time2);
	}

	public int partition(int A[], int f, int l) {// use random partition
		int pindex = (int) (Math.random() * A.length);
		int pivot = A[pindex];
		while (f < l) {
			while (A[f] < pivot)
				f++;
			while (A[l] > pivot)
				l--;
			swap(A, f, l);
		}
		return f;
	}

	public int random_select(int A[], int p, int q, int k) {// select value k
															// from A via
															// partition of
															// quick sort
		if (p == q) {
			return A[p];
		}
		int r = partition(A, p, q);
		int n = r - p + q;
		if (k == n) {
			return A[r];
		}
		if (k < n) {
			return random_select(A, p, r - 1, k);
		} else {
			return random_select(A, r + 1, q, k - n);
		}
	}

	public static void swap(int A[], int x, int y) {// swap values
		int temp = A[x];
		A[x] = A[y];
		A[y] = temp;
	}

	public double Carlo(double in) {// find 1-x^2
		int i;
		double dart = 0;
		double darthit = 0;
		double x,y;
		long start = System.nanoTime();
		for (i = 0; i < in; i++) {// run in times
			x = Math.random(); // Throw a dart
			y = Math.random();
			dart++;
			if (x<=Math.pow(y, y)) {// is it in the area ?
				darthit++;
			}
		}
		long finish = System.nanoTime();
		time = (finish - start) / 1000;
		return (darthit / dart);// return value
	}
}