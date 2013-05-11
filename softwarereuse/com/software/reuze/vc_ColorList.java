package com.software.reuze;
/*
 * Some classes in this package have been partly inspired by & bits ported from
 * Python code written by Tom De Smedt & Frederik De Bleser for the "colors" library
 * of Nodebox.net.
 * 
 * http://nodebox.net/code/index.php/Colors
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */




import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

//import javax.xml.bind.annotation.XmlElement;


/**
 * A container class of concrete colors. ColorLists can be built manually and
 * are also created when working with {@link vc_ColorRange}s. The class has various
 * methods to manipulate all colors in the list in parallel, as well as sort
 * them by various criteria.
 * 
 * @see vc_ColorRange
 * @see vc_a_CriteriaAccess
 */
public class vc_ColorList implements Iterable<z_Colors> {

    /**
     * Factory method. Creates a new ColorList of colors sampled from the given
     * ARGB image array. If the number of samples equals or exceeds the number
     * of pixels in the image and no unique colors are required, the function
     * will simply return the same as {@link #ColorList(int[])}.
     * 
     * @param pixels
     *            int array of ARGB pixels
     * @param num
     *            number of colors samples (clipped automatically to number of
     *            pixels in the image)
     * @param uniqueOnly
     *            flag if only unique samples are to be taken (doesn't guarantee
     *            unique colors though)
     * @return new color list
     */
    public static final vc_ColorList createFromARGBArray(int[] pixels, int num,
            boolean uniqueOnly) {
        return createFromARGBArray(pixels, num, uniqueOnly, 100);
    }

    /**
     * Factory method. Creates a new ColorList of colors randomly sampled from
     * the given ARGB image array. If the number of samples equals or exceeds
     * the number of pixels in the source image and no unique colors are
     * required, the function will simply return the same as
     * {@link #ColorList(int[])}.
     * 
     * @param pixels
     *            int array of ARGB pixels
     * @param num
     *            number of colors samples (clipped automatically to number of
     *            pixels in the image)
     * @param uniqueOnly
     *            flag if only unique samples are to be taken (doesn't guarantee
     *            unique colors though)
     * @param maxIterations
     *            max number of attempts to find a unique color. If no more
     *            unique colors can be found the search is terminated.
     * @return new color list of samples
     */
    public static final vc_ColorList createFromARGBArray(int[] pixels, int num,
            boolean uniqueOnly, int maxIterations) {
        num = m_MathUtils.min(num, pixels.length);
        if (!uniqueOnly && num == pixels.length) {
            return new vc_ColorList(pixels);
        }
        List<z_Colors> colors = new ArrayList<z_Colors>();
        z_Colors temp = z_Colors.black.copy();
        for (int i = 0; i < num; i++) {
            int idx;
            if (uniqueOnly) {
                boolean isUnique = true;
                int numTries = 0;
                do {
                    idx = m_MathUtils.random(pixels.length-1);
                    temp=new z_Colors(pixels[idx]);
                    isUnique = !colors.contains(temp);
                } while (!isUnique && ++numTries < maxIterations);
                if (numTries < maxIterations) {
                    colors.add(temp.copy());
                } else {
                    break;
                }
            } else {
                idx = m_MathUtils.random(pixels.length-1);
                colors.add(new z_Colors(pixels[idx]));
            }
        }
        return new vc_ColorList(colors);
    }

    /**
     * Factory method. Creates a new ColorList based on the given
     * {@link vc_i_ColorTheoryStrategy} instance and the given source color. The
     * number of colors returned will vary with the strategy chosen.
     * 
     * @param strategy
     * @param c
     * @return new list
     */
    public static final vc_ColorList createUsingStrategy(
            vc_i_ColorTheoryStrategy strategy, z_Colors c) {
        return strategy.createListFromColor(c);
    }

    /**
     * Factory method. Creates a ColorList based on the name of a
     * {@link vc_i_ColorTheoryStrategy} and the given source color.
     * 
     * @param name
     *            strategy name
     * @param c
     * @return new color list or null, if the supplied strategy name is not
     *         mapped to a registered implementation.
     */
    /*public static final ColorList createUsingStrategy(String name, Colors c) {
        ColorTheoryStrategy strategy = ColorTheoryRegistry
                .getStrategyForName(name);
        ColorList list = null;
        if (strategy != null) {
            list = strategy.createListFromColor(c);
        }
        return list;
    }*/

    //@XmlElement(name = "col")
    //@XmlJavaTypeAdapter(TColorAdapter.class)
    protected List<z_Colors> colors = new ArrayList<z_Colors>();

    /**
     * Creates an empty list.
     */
    public vc_ColorList() {

    }

    /**
     * Creates a ColorList by wrapping the given ArrayList of colors. No copies
     * of the given colors are created (shallow copy only).
     * 
     * @param colors
     */
    public vc_ColorList(Collection<z_Colors> colors) {
        this.colors.addAll(colors);
    }

    /**
     * Creates a deep copy of the given ColorList. Manipulating the new list or
     * its color entries does NOT change the colors of the original.
     * 
     * @param list
     *            source list to copy
     */
    public vc_ColorList(vc_ColorList list) {
        for (z_Colors c : list) {
            this.colors.add(c.copy());
        }
    }

    /**
     * Creates a new color list from the array of ARGB int values. In most cases
     * this will be the pixel buffer of an image.
     * 
     * @param argbArray
     */
    public vc_ColorList(int[] argbArray) {
        for (int c : argbArray) {
            colors.add(new z_Colors(c));
        }
    }

    /**
     * Creates new ColorList from the given colors. Copies of the given colors
     * are created. This is a varargs constructor allowing these two parameter
     * formats:
     * 
     * <pre>
     * // individual parameters
     * ColorList cols=new ColorList(Colors.BLACK,Colors.WHITE,Colors.newRGB(1,0,0));
     * 
     * // or array of colors
     * Colors[] colArray=new Colors[] {
     *   Colors.BLACK,Colors.WHITE,Colors.newRGB(1,0,0);
     * };
     * ColorList cols=new ColorList(colArray);
     * </pre>
     * 
     * @param colorArray
     */
    public vc_ColorList(z_Colors... colorArray) {
        for (z_Colors c : colorArray) {
            colors.add(c.copy());
        }
    }

    /**
     * Adds a copy of the given color to the list
     * 
     * @param c
     * @return itself
     */
    public vc_ColorList add(z_Colors c) {
        colors.add(c.copy());
        return this;
    }

    /**
     * Adds all entries of the Colors collection to the list (shallow copy only,
     * manipulating the new list will modify the original colors).
     * 
     * @param collection
     * @return itself
     */
    public vc_ColorList addAll(Collection<z_Colors> collection) {
        colors.addAll(collection);
        return this;
    }

    /**
     * Adjusts the brightness component of all list colors by the given amount.
     * 
     * @param step
     *            adjustment value
     * @return itself
     */
    public vc_ColorList adjustBrightness(float step) {
        for (z_Colors c : colors) {
            c.lighten(step);
        }
        return this;
    }

    /**
     * Adjusts the saturation component of all list colors by the given amount.
     * 
     * @param step
     *            adjustment value
     * @return itself
     */
    public vc_ColorList adjustSaturation(float step) {
        for (z_Colors c : colors) {
            c.saturate(step);
        }
        return this;
    }

    /**
     * Sorts the list based on two criteria to create clusters/segments within
     * the list.
     * 
     * @param clusterCriteria
     *            main sort criteria
     * @param subClusterCriteria
     *            secondary sort criteria
     * @param numClusters
     *            number of clusters
     * @param isReversed
     *            true, if reversed sort
     * @return itself
     */
    public vc_ColorList clusterSort(vc_a_CriteriaAccess clusterCriteria,
            vc_a_CriteriaAccess subClusterCriteria, int numClusters,
            boolean isReversed) {
        ArrayList<z_Colors> sorted = new ArrayList<z_Colors>(colors);
        Collections.sort(sorted, clusterCriteria);
        Collections.reverse(sorted);
        ArrayList<z_Colors> clusters = new ArrayList<z_Colors>();

        float d = 1;
        int i = 0;
        int num = sorted.size();
        for (int j = 0; j < num; j++) {
            z_Colors c = sorted.get(j);
            if (c.getComponentValue(clusterCriteria) < d) {
                ArrayList<z_Colors> slice = new ArrayList<z_Colors>();
                slice.addAll(sorted.subList(i, j));
                Collections.sort(slice, subClusterCriteria);
                clusters.addAll(slice);
                d -= 1.0f / numClusters;
                i = j;
            }
        }
        ArrayList<z_Colors> slice = new ArrayList<z_Colors>();
        slice.addAll(sorted.subList(i, sorted.size()));
        Collections.sort(slice, subClusterCriteria);
        clusters.addAll(slice);
        if (isReversed) {
            Collections.reverse(clusters);
        }
        colors = clusters;
        return this;
    }

    /**
     * Switches all list colors to their complementary color.
     * 
     * @return itself
     */
    public vc_ColorList complement() {
        for (z_Colors c : colors) {
            c.complement();
        }
        return this;
    }

    /**
     * Checks if the given color is part of the list. Check is done by value,
     * not instance.
     * 
     * @param color
     * @return true, if the color is present.
     */
    public boolean contains(z_Colors color) {
        for (z_Colors c : colors) {
            if (c.equals(color)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the color at the given index. This function follows Python
     * convention, in that if the index is negative, it is considered relative
     * to the list end. Therefore the color at index -1 is the last color in the
     * list.
     * 
     * @param i
     *            index
     * @return color
     */
    public z_Colors get(int i) {
        if (i < 0) {
            i += colors.size();
        }
        return colors.get(i);
    }

    /**
     * Calculates and returns the average color of the list.
     * 
     * @return average color or null, if there're no entries yet.
     */
    private static float temp[]=new float[4];
    public z_Colors getAverage() {
        float r = 0;
        float g = 0;
        float b = 0;
        float a = 0;
        for (z_Colors c : colors) {
        	c.getComponents(temp); //r,g,b,a
            r += temp[0];
            g += temp[1];
            b += temp[2];
            a += temp[3];
        }
        int num = colors.size();
        if (num > 0) {
            return new z_Colors(r / num, g / num, b / num, a / num);
        } else {
            return null;
        }
    }

    /**
     * Creates a new ColorList by blending all colors in the list with each
     * other (successive indices only)
     * 
     * @param amount
     *            blend amount
     * @return new color list
     */
    public vc_ColorList getBlended(float amount) {
        z_Colors[] clrs = new z_Colors[colors.size()];
        for (int i = 0; i < clrs.length; i++) {
            z_Colors c = colors.get(i > 0 ? i - 1 : clrs.length - 1);
            clrs[i] = colors.get(i).blend(c, amount);
        }
        return new vc_ColorList(clrs);
    }

    /**
     * Finds and returns the darkest color of the list.
     * 
     * @return darkest color or null if there're no entries yet.
     */
    public z_Colors getDarkest() {
        z_Colors darkest = null;
        float minBrightness = Float.MAX_VALUE;
        for (z_Colors c : colors) {
            float luma = c.luminance();
            if (luma < minBrightness) {
                darkest = c;
                minBrightness = luma;
            }
        }
        return darkest;
    }

    /**
     * Finds and returns the lightest (luminance) color of the list.
     * 
     * @return lightest color or null, if there're no entries yet.
     */
    public z_Colors getLightest() {
        z_Colors lightest = null;
        float maxBrightness = Float.MIN_VALUE;
        for (z_Colors c : colors) {
            float luma = c.luminance();
            if (luma > maxBrightness) {
                lightest = c;
                maxBrightness = luma;
            }
        }
        return lightest;
    }

    public z_Colors getRandom() {
        return colors.get(m_MathUtils.random(colors.size()));
    }

    /**
     * Returns a reversed copy of the current list.
     * 
     * @return reversed copy of the list
     */
    public vc_ColorList getReverse() {
        return new vc_ColorList(colors).reverse();
    }

    /**
     * Inverts all colors in the list.
     * 
     * @return itself
     */
    public vc_ColorList invert() {
        for (z_Colors c : colors) {
            c.invert();
        }
        return this;
    }

    /**
     * Returns an iterator over the internal list. This means the list can be
     * accessed via standard Iterator loops.
     * 
     * @return list iterator
     */
    public Iterator<z_Colors> iterator() {
        return colors.iterator();
    }

    /**
     * Reverses the current order of the list.
     * 
     * @return itself
     */
    public vc_ColorList reverse() {
        Collections.reverse(colors);
        return this;
    }

    /**
     * Rotates the hues of all colors in the list by the given amount.
     * 
     * @param theta
     *            rotation angle in radians
     * @return itself
     */
    public vc_ColorList rotateRYB(float theta) {
        return rotateRYB(m_MathUtils.degrees(theta));
    }

    /**
     * Rotates the hues of all colors in the list by the given amount.
     * 
     * @param angle
     *            rotation angle in degrees
     * @return itself
     */
    public vc_ColorList rotateRYB(int angle) {
        for (int i=0; i<colors.size(); i++) {
            colors.set(i, colors.get(i).rotateRYB(angle));
        }
        return this;
    }

    /**
     * @return the number of colors in the list
     */
    public int size() {
        return colors.size();
    }

    /**
     * Convenience method. Sorts the list by hue.
     * 
     * @return itself
     */
    public vc_ColorList sort() {
        return sortByCriteria(vc_a_CriteriaAccess.HUE, false);
    }

    /**
     * Sorts the list using the given comparator.
     * 
     * @param comp
     *            comparator
     * @param isReversed
     *            true, if reversed sort
     * @return itself
     */
    public vc_ColorList sortByComparator(Comparator<z_Colors> comp,
            boolean isReversed) {
        Collections.sort(colors, comp);
        if (isReversed) {
            Collections.reverse(colors);
        }
        return this;
    }

    /**
     * Sorts the list using the given {@link vc_a_CriteriaAccess}.
     * 
     * @param criteria
     *            sort criteria
     * @param isReversed
     *            true, if reversed sort
     * @return itself
     */
    public vc_ColorList sortByCriteria(vc_a_CriteriaAccess criteria, boolean isReversed) {
        return sortByComparator(criteria, isReversed);
    }

    /**
     * Sorts the list by relative distance to each predecessor, starting with
     * the darkest color in the list.
     * 
     * @param isReversed
     *            true, if list is to be sorted in reverse.
     * @return itself
     */
    public vc_ColorList sortByDistance(boolean isReversed) {
        return sortByDistance(new vc_DistanceHSV(), isReversed);
    }

    /**
     * Sorts the list by relative distance to each predecessor, starting with
     * the darkest color in the list.
     * 
     * @param isReversed
     *            true, if list is to be sorted in reverse.
     * @return itself
     */
    public vc_ColorList sortByDistance(vc_i_Distance proxy, boolean isReversed) {
        if (colors.size() == 0) {
            return this;
        }

        z_Colors root = getDarkest();

        // Remove the darkest color from the stack,
        // put it in the sorted list as starting element.
        ArrayList<z_Colors> stack = new ArrayList<z_Colors>(colors);
        stack.remove(root);
        ArrayList<z_Colors> sorted = new ArrayList<z_Colors>(colors.size());
        sorted.add(root);

        // Now find the color in the stack closest to that color.
        // Take this color from the stack and add it to the sorted list.
        // Now find the color closest to that color, etc.
        int sortedCount = 0;
        while (stack.size() > 1) {
            z_Colors closest = stack.get(0);
            z_Colors lastSorted = sorted.get(sortedCount);
            float distance = proxy.distanceBetween(closest, lastSorted);
            for (int i = stack.size() - 1; i >= 0; i--) {
                z_Colors c = stack.get(i);
                float d = proxy.distanceBetween(c, lastSorted);
                if (d < distance) {
                    closest = c;
                    distance = d;
                }
            }
            stack.remove(closest);
            sorted.add(closest);
            sortedCount++;
        }
        sorted.add(stack.get(0));
        if (isReversed) {
            Collections.reverse(sorted);
        }
        colors = sorted;
        return this;
    }

    /**
     * Sorts the list by proximity to the given target color (using RGB distance
     * metrics).
     * 
     * @see #sortByProximityTo(z_Colors, vc_i_Distance, boolean)
     * @param target
     *            color
     * @param isReversed
     *            true, if reverse sorted
     * @return sorted list
     */
    public vc_ColorList sortByProximityTo(z_Colors target, boolean isReversed) {
        return sortByProximityTo(target, new vc_DistanceRGB(), isReversed);
    }

    /**
     * Sorts the list by proximity to the given target color using the given
     * {@link vc_i_Distance} implementation.
     * 
     * @param target
     *            color
     * @param proxy
     *            distance metrics
     * @param isReversed
     *            true, if reverse sorted
     * @return sorted list
     */
    public vc_ColorList sortByProximityTo(z_Colors target,
            vc_i_Distance proxy, boolean isReversed) {
        return sortByComparator(new vc_ComparatorProximity(target, proxy),
                isReversed);
    }

    /**
     * Creates an ARGB integer array of the list items.
     * 
     * @return all list colors as ARGB values
     */
    public int[] toARGBArray() {
        int[] array = new int[colors.size()];
        int i = 0;
        for (z_Colors c : colors) {
            array[i++] = c.toARGB();
        }
        return array;
    }
}
