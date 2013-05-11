package com.software.reuze;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class m_MathUtil2 {
	private static Random _r = new Random();
	public static double[] normalize(double[] probDist /*inout*/) {
		int len = probDist.length;
		double total = 0.0;
		for (double d : probDist) {
			total += d;
		}
		if (total != 0) {
			for (int i = 0; i < len; i++) {
				probDist[i] /= total;
			}
		}

		return probDist;
	}
	
	/**
	 * Get the first element from a list.
	 * 
	 * @param l
	 *            the list the first element is to be extracted from.
	 * @return the first element of the passed in list.
	 */
	public static <T> T first(List<T> l) {
		return l.get(0);
	}

	/**
	 * Get a sublist of all of the elements in the list except for first.
	 * 
	 * @param l
	 *            the list the rest of the elements are to be extracted from.
	 * @return a list of all of the elements in the passed in list except for
	 *         the first element.
	 */
	public static <T> List<T> rest(List<T> l) {
		return l.subList(1, l.size());
	}
	
	/**
	 * Create a Map<K, V> with the passed in keys having their values
	 * initialized to the passed in value.
	 * 
	 * @param keys
	 *            the keys for the newly constructed map.
	 * @param value
	 *            the value to be associated with each of the maps keys.
	 * @return a map with the passed in keys initialized to value.
	 */
	public static <K, V> Map<K, V> create(Collection<K> keys, V value) {
		Map<K, V> map = new LinkedHashMap<K, V>();

		for (K k : keys) {
			map.put(k, value);
		}

		return map;
	}
	
	/**
	 * Randomly select an element from a list.
	 * 
	 * @param <T>
	 *            the type of element to be returned from the list l.
	 * @param l
	 *            a list of type T from which an element is to be selected
	 *            randomly.
	 * @return a randomly selected element from l.
	 */
	public static <T> T selectRandomlyFromList(List<T> l) {
		return l.get(_r.nextInt(l.size()));
	}

	public static boolean randomBoolean() {
		int trueOrFalse = _r.nextInt(2);
		return (!(trueOrFalse == 0));
	}
	
	public static StringBuilder append(StringBuilder b, String s, int n) {
		for (int i = 0; i < n; i++) {
			b.append(s);
		}
		return b;
	}
	
	public static double log2(double d) {
		return Math.log(d) / m_MathUtils.LOG2;
	}
	
	public static double mean(List<Double> lst) {
		Double sum = 0.0;
		for (Double d : lst) {
			sum = sum + d.doubleValue();
		}
		return sum / lst.size();
	}

	public static double stdDev(List<Double> values, double mean) {

		int listSize = values.size();

		Double sumOfDiffSquared = 0.0;
		for (Double value : values) {
			double diffFromMean = value - mean;
			sumOfDiffSquared += ((diffFromMean * diffFromMean) / (listSize - 1));
			// division moved here to avoid sum becoming too big if this
			// doesn't work use incremental formulation

		}
		double variance = sumOfDiffSquared;
		// (listSize - 1);
		// assumes at least 2 members in list.
		return Math.sqrt(variance);
	}
	
	public static <T> T mode(List<T> l) {
		HashMap<T, Integer> hash = new HashMap<T, Integer>();
		for (T obj : l) {
			if (hash.containsKey(obj)) {
				hash.put(obj, hash.get(obj).intValue() + 1);
			} else {
				hash.put(obj, 1);
			}
		}

		T maxkey = hash.keySet().iterator().next();
		for (T key : hash.keySet()) {
			if (hash.get(key) > hash.get(maxkey)) {
				maxkey = key;
			}
		}
		return maxkey;
	}
}
