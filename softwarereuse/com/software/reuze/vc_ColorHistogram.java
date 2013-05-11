package com.software.reuze;
/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * This class can be used to compute the distribution frequency of colors within
 * a given {@link vc_ColorList}. In other words, it will provide some statistics of
 * how many times each color occurs within the list. In most cases the source
 * for this ColorList will be the pixel buffer of an image. The calculation of
 * the histogram works with tolerances, allowing similar colors to be grouped
 * (and blended) and so can be used to produce results of varying precision.
 * 
 */
public class vc_ColorHistogram implements Iterable<vc_ColorHistogramEntry> {

    /**
     * Creates a new histogram of random color samples taken from the given ARGB
     * array in order to identify the most frequently used colors in the image.
     * This is a convenience factory method which internally uses
     * {@link vc_ColorList#createFromARGBArray(int[], int, boolean, int)} to sample
     * the image before computing the histogram. Supports blending of grouped
     * result colors. If enabled, the colors returned in the histogram will be
     * averaged for each group based on the given tolerance. If disabled, only
     * the first color found for each tolerance group is used.
     * 
     * @see #compute(float, boolean)
     * 
     * @param pixels
     *            source pixel buffer (in ARGB format), alpha is ignored though
     * @param numSamples
     *            number of random samples. If the number of samples equals or
     *            exceeds the number of pixels, NO RANDOM samples are used.
     * @param tolerance
     *            normalized grouping tolerance, e.g. 0.2 = 20%
     * @param blendCols
     *            flag to enable automatic blending of result colors.
     * @return histogram instance
     */
    public static vc_ColorHistogram newFromARGBArray(int[] pixels, int numSamples,
            float tolerance, boolean blendCols) {
        vc_ColorHistogram h = new vc_ColorHistogram(vc_ColorList.createFromARGBArray(pixels,
                numSamples, false));
        h.compute(tolerance, blendCols);
        return h;
    }

    protected vc_ColorList palette;

    protected ArrayList<vc_ColorHistogramEntry> entries;

    /**
     * Constructs a new instance for the given {@link vc_ColorList}.
     * 
     * @param palette
     *            list of source colors
     */
    public vc_ColorHistogram(vc_ColorList palette) {
        this.palette = palette;
    }

    /**
     * Computes the histogram for the given {@link vc_ColorList}. Supports blending
     * of grouped result colors. If enabled, the colors returned in the
     * histogram will be averaged for each group based on the given tolerance
     * (using RGB distances). If disabled, only the first color found for each
     * tolerance group is used.
     * 
     * @see TColor#distanceToRGB(ReadonlyTColor)
     * 
     * @param tolerance
     *            normalized grouping tolerance, 0.0 .. 1.0 interval, e.g. 0.2 =
     *            20%
     * @param blendCols
     *            flag to enable automatic blending of result colors.
     * @return sorted histogram as List of HistEntry
     */
    public List<vc_ColorHistogramEntry> compute(float tolerance, boolean blendCols) {
        entries = new ArrayList<vc_ColorHistogramEntry>(palette.size() / 4);
        float maxFreq = 1;
        tolerance /= m_MathUtils.SQRT3;
        for (z_Colors c : palette) {
            vc_ColorHistogramEntry existing = null;
            for (vc_ColorHistogramEntry e : entries) {
                if (e.col.distanceToRGB(c) < tolerance) {
                    if (blendCols) {
                        e.col.blend(c, 1f / (e.freq + 1));
                    }
                    existing = e;
                    break;
                }
            }
            if (existing != null) {
                existing.freq++;
                if (existing.freq > maxFreq) {
                    maxFreq = existing.freq;
                }
            } else {
                entries.add(new vc_ColorHistogramEntry(c));
            }
        }
        Collections.sort(entries);
        maxFreq = 1f / palette.size();
        for (vc_ColorHistogramEntry e : entries) {
            e.freq *= maxFreq;
        }
        return entries;
    }

    /**
     * Returns the list of {@link vc_ColorHistogramEntry} elements produced by the
     * {@link #compute(float, boolean)} method.
     * 
     * @return the histogram entries
     */
    public List<vc_ColorHistogramEntry> getEntries() {
        return entries;
    }

    /**
     * Returns the list of source colors
     * 
     * @return the palette
     */
    public vc_ColorList getPalette() {
        return palette;
    }

    /**
     * Returns an iterator of the underlying list of {@link vc_ColorHistogramEntry} elements.
     * That way, the Histogram instance itself can be directly used in for()
     * loops processing the entries.
     */
    public Iterator<vc_ColorHistogramEntry> iterator() {
        return entries.iterator();
    }

    /**
     * Configures the histogram instance to use a new list of source colors.
     * 
     * @param palette
     *            the palette to set
     */
    public void setPalette(vc_ColorList palette) {
        this.palette = palette;
    }
}