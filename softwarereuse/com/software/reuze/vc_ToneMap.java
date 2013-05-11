package com.software.reuze;
import com.software.reuze.m_ScaleMap;
import com.software.reuze.m_i_InterpolateValue;

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
public class vc_ToneMap {

    public m_ScaleMap map;
    public vc_ColorList colors;

    public vc_ToneMap(float min, float max, vc_ColorGradient gradient) {
        this(min, max, gradient.calcGradient());
    }

    public vc_ToneMap(float min, float max, vc_ColorList c) {
        map = new m_ScaleMap(min, max, 0, c.size() - 1);
        colors = c;
    }

    public vc_ToneMap(float a, float b, z_Colors colA, z_Colors colB) {
        this(a, b, new vc_ColorList(colA, colB));
    }

    public vc_ToneMap(float min, float max, z_Colors colA,
            z_Colors colB, int res) {
        vc_ColorGradient g = new vc_ColorGradient();
        g.addColorAt(0, colA);
        g.addColorAt(res - 1, colB);
        colors = g.calcGradient(0, res);
        map = new m_ScaleMap(min, max, 0, colors.size() - 1);
    }

    public final int getARGBToneFor(float t) {
        return getToneFor(t).toARGB();
    }

    public final z_Colors getToneFor(float t) {
        int idx;
        if (colors.size() > 2) {
            idx = (int) (map.getClippedValueFor(t) + 0.5);
        } else {
            idx = (t >= map.getInputMedian() ? 1 : 0);
        }
        return colors.get(idx);
    }

    /**
     * Applies the tonemap to all elements in the given source array of single
     * precision values and places the resulting ARGB color in the corresponding
     * index of the target pixel buffer. If the target buffer is null, a new one
     * will be created automatically.
     * 
     * @param src
     *            source array of values to be tone mapped
     * @param pixels
     *            target pixel buffer
     * @return pixel array
     */
    public int[] getToneMappedArray(float[] src, int[] pixels) {
        if (pixels == null) {
            pixels = new int[src.length];
        } else if (src.length != pixels.length) {
            throw new IllegalArgumentException(
                    "pixel array need to be the same size as source array");
        }
        for (int i = 0; i < src.length; i++) {
            pixels[i] = getToneFor(src[i]).toARGB();
        }
        return pixels;
    }

    public int[] getToneMappedArray(float[] src, int[] pixels, int offset) {
        if (offset < 0 || offset + src.length > pixels.length) {
            throw new IllegalArgumentException(
                    "offset into target pixel buffer is negative or too large");
        }
        for (int i = 0; i < src.length; i++) {
            pixels[offset++] = getToneFor(src[i]).toARGB();
        }
        return pixels;
    }

    /**
     * Applies the tonemap to all elements in the given source array of integers
     * and places the resulting ARGB color in the corresponding index of the
     * target pixel buffer. If the target buffer is null, a new one will be
     * created automatically.
     * 
     * @param src
     *            source array of values to be tone mapped
     * @param pixels
     *            target pixel buffer
     * @return pixel array
     */
    public int[] getToneMappedArray(int[] src, int[] pixels) {
        if (pixels == null) {
            pixels = new int[src.length];
        } else if (src.length != pixels.length) {
            throw new IllegalArgumentException(
                    "pixel array need to be the same size as source array");
        }
        for (int i = 0; i < src.length; i++) {
            pixels[i] = getToneFor(src[i]).toARGB();
        }
        return pixels;
    }

    public int[] getToneMappedArray(int[] src, int[] pixels, int offset) {
        if (offset < 0 || offset + src.length > pixels.length) {
            throw new IllegalArgumentException(
                    "offset into target pixel buffer is negative or too large");
        }
        for (int i = 0; i < src.length; i++) {
            pixels[offset++] = getToneFor(src[i]).toARGB();
        }
        return pixels;
    }

    /**
     * Sets the interpolation function for the underlying {@link m_ScaleMap}
     * instance of this {@link vc_ToneMap}.
     * 
     * @param func
     * @see m_ScaleMap#setMapFunction(InterpolateStrategy)
     */
    public void setMapFunction(m_i_InterpolateValue func) {
        map.setMapFunction(func);
    }
}
