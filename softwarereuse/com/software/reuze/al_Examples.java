package com.software.reuze;
//package aima.core.learning.framework;

//import aima.core.learning.data.DataResource;
//import aima.core.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Provides methods for representing a set of learning examples
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class al_Examples implements Iterable<al_Example>, Cloneable {

    public ArrayList<al_Example> examples;

    /**
     * Constructor
     */
    public al_Examples() {
        examples = new ArrayList<al_Example>();
    }

    /**
     * Adds an example to the set
     *
     * @param e
     */
    public void add(al_Example e) {
        examples.add(e);
    }

    /**
     * Returns the size of the set
     *
     * @return
     */
    public int size() {
        return examples.size();
    }

    /**
     * Returns the example given an index
     *
     * @param number
     * @return
     */
    public al_Example getExample(int index) {
        return examples.get(index);
    }

    /**
     * Removes the example given from the data set
     *
     * @param e
     * @return
     */
    public al_Examples remove(al_Example e) {
        al_Examples ds = new al_Examples();
        for (al_Example eg : examples) {
            if (!(e.equals(eg))) {
                ds.add(eg);
            }
        }
        return ds;
    }

    /**
     * Find the examples that match the given attribute name/value pair
     *
     * @param attributeName
     * @param attributeValue
     * @return
     */
    public al_Examples find(String attributeName, Object attributeValue) {
        al_Examples ds = new al_Examples();
        for (al_Example e : examples) {
            if (e.get(attributeName).getValue().equals(attributeValue)) {
                ds.add(e);
            }
        }
        return ds;
    }

    /**
     * Return all possible attributes (a name/value pair) in the given example
     * set; by using a HashSet, no duplicates should exist.
     *
     * @return
     */
    public HashSet<d_PropertyListPair> getPossibleAttributes() {
        HashSet<d_PropertyListPair> possibleAttributes = new HashSet<d_PropertyListPair>();
        for (al_Example e : this) {
            for (d_PropertyListPair a : e.getAttributes()) {
                possibleAttributes.add(a);
            }
        }
        return possibleAttributes;
    }

    /**
     * Split the set by attribute; return a map from attribute value to example
     * set.
     *
     * @param attributeName
     * @return
     */
    public <T> HashMap<T, al_Examples> splitBy(String attributeName) {
        HashMap<T, al_Examples> results = new HashMap<T, al_Examples>();
        for (al_Example e : this.examples) {
            T attributeValue = (T) e.get(attributeName).getValue();
            if (results.containsKey(attributeValue)) {
                results.get(attributeValue).add(e);
            } else {
                al_Examples ds = new al_Examples();
                ds.add(e);
                results.put(attributeValue, ds);
            }
        }
        return results;
    }

    /**
     * Return each distinct value for this attribute in the example set
     *
     * @param attributeName
     * @return
     */
    public <T> HashSet<T> getValuesOf(String attributeName) {
        HashSet<T> values = new HashSet<T>();
        for (al_Example e : this.examples) {
            values.add((T) e.get(attributeName).getValue());
        }
        return values;
    }

    /**
     * Calculate entropy of an attribute in the example set; see description of
     * process on page 704, AIMAv3. Used by DecisionTreeLearner. Does not use
     * Util.information() as this method is vaguely named and does not fully
     * conform to formula on page 704.
     *
     * @return
     */
    public double getEntropyOf(String attributeName) {
        HashMap<Object, Integer> distribution = new HashMap<Object, Integer>();
        // count number of values v_k for variable V, page 704
        for (al_Example e : examples) {
            Object value = e.get(attributeName).getValue();
            if (distribution.containsKey(value)) {
                distribution.put(value, distribution.get(value) + 1);
            } else {
                distribution.put(value, 1);
            }
        }
        // normalize probability distribution, see page 493
        double[] normalizedDistribution = new double[distribution.keySet().size()];
        Iterator<Integer> iter = distribution.values().iterator();
        for (int i = 0; i < normalizedDistribution.length; i++) {
            normalizedDistribution[i] = iter.next();
        }
        normalizedDistribution = m_MathUtil2.normalize(normalizedDistribution);
        // calculate entropy H(V), page 704		
        double total = 0.0;
        for (double d : normalizedDistribution) {
            total += d * m_MathUtil2.log2(d);
        }
        return -1.0 * total;
    }

    /**
     * Calculate entropy of output values in the example set; see 
     * getEntropyOf() for details.
     *
     * @return
     */
    public double getEntropyOfOutput(){
        HashMap<Object, Integer> distribution = new HashMap<Object, Integer>();
        // count number of values v_k for variable V, page 704
        for (al_Example e : examples) {
            Object value = e.getOutput();
            if (distribution.containsKey(value)) {
                distribution.put(value, distribution.get(value) + 1);
            } else {
                distribution.put(value, 1);
            }
        }
        // normalize probability distribution, see page 493
        double[] normalizedDistribution = new double[distribution.keySet().size()];
        Iterator<Integer> iter = distribution.values().iterator();
        for (int i = 0; i < normalizedDistribution.length; i++) {
            normalizedDistribution[i] = iter.next();
        }
        normalizedDistribution = m_MathUtil2.normalize(normalizedDistribution);
        // calculate entropy H(V), page 704		
        double total = 0.0;
        for (double d : normalizedDistribution) {
            total += d * m_MathUtil2.log2(d);
        }
        return -1.0 * total;
    }

    /**
     * Calculate information gain--the expected reduction in entropy--after
     * testing the given attribute; see page 704, AIMAv3. Used in
     * DecisionTreeLearner.
     *
     * @param attributeName
     * @return
     */
    public double getInformationGainOf(String attributeName) {
        HashMap<Object, al_Examples> attributeValueMap = this.splitBy(attributeName);
        double totalSize = this.examples.size();
        double remainder = 0.0;
        for (Object attributeValue : attributeValueMap.keySet()) {
            // this may not look exactly like page 704, but is equivalent:
            double matchingValueSize = attributeValueMap.get(attributeValue).size();
            double outputEntropy = attributeValueMap.get(attributeValue).getEntropyOfOutput();
            remainder += (matchingValueSize / totalSize) * outputEntropy;
        }
        return this.getEntropyOfOutput() - remainder;
    }

    /**
     * Returns a set of examples from a given CSV file and a sample Example; CSV
     * file should be located in the "aima/core/learning/data" directory.
     *
     * @param filename
     * @param sample
     * @param separator
     * @return
     * @throws Exception
     */
    public static al_Examples loadFrom(URL url, String separator, al_Example sample) throws IOException {
        al_Examples ds = new al_Examples();
        // check cache
        InputStream stream;
        if( f_TmpDirIO.isCached(url.getFile()) ){
            stream = f_TmpDirIO.get(url.getFile());
        }
        else{
            stream = url.openStream();
        }
        // loop through stream lines
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            ds.add(al_Examples.loadLine(line, separator, sample));
        }
        reader.close();
        // save to cache if necessary
        if( !f_TmpDirIO.isCached(url.getFile()) ){
            f_TmpDirIO.put(url.getFile(), url.openStream());
        }
        // return
        return ds;
    }

    
    
    /**
     * Returns an Example from a CSV line
     *
     * @param line
     * @param separator
     * @param sample
     * @return
     */
    private static <T> al_Example<T> loadLine(String line, String separator, al_Example<T> sample) {
        // set attributes
        String[] parts = line.split(separator);
        al_Example<T> new_example = sample.clone();
        for (int i = 0; i < sample.inputAttributes.size(); i++) {
            if (i < parts.length) {
                d_PropertyListPair<?> old_attribute = (d_PropertyListPair) sample.inputAttributes.get(i);
                d_PropertyListPair<?> new_attribute = old_attribute.clone();
                new_attribute.setValue(parts[i].trim());
                new_example.inputAttributes.remove(old_attribute);
                new_example.add(new_attribute);
            }
        }
        // set output value; uses reflection to match the generic outputValue type
        try {
            String last_field = parts[parts.length - 1].trim();
            T value = (T) sample.outputValue.getClass().getDeclaredConstructor(String.class).newInstance(last_field);
            new_example.setOutput(value);
        } catch (Exception e) {
            new_example.outputValue = null;
        }
        // return
        return new_example;
    }

    /**
     * Overrides equals() to compare with uncast Object
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        al_Examples other = (al_Examples) o;
        return examples.equals(other.examples);
    }

    /**
     * For use in equals()
     *
     * @return
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Iterator
     *
     * @return
     */
    public Iterator<al_Example> iterator() {
        return examples.iterator();
    }

    /**
     * Clone
     *
     * @return
     */
    @Override
    public al_Examples clone() {
        al_Examples copy = new al_Examples();
        for (al_Example e : this) {
            copy.add(e.clone());
        }
        return copy;
    }

    /**
     * Return string representation
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("DataSet:[\n");
        for (al_Example e : this.examples) {
            s.append(" ");
            s.append(e);
            s.append(",\n");
        }
        s.append("]");
        return s.toString();
    }
}
