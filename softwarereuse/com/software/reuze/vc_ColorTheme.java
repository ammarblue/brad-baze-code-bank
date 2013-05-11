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
import java.util.StringTokenizer;



/**
 * A ColorTheme is a weighted collection of {@link vc_ColorRange}s used to define
 * custom palettes with a certain balance between individual colors/shades. New
 * theme parts can be added via textual descriptors referring to one of the
 * preset {@link vc_ColorRange}s and/or {@link NamedColor}s: e.g.
 * "warm springgreen". For each theme part a weight has to be specified. The
 * magnitude of the weight value is irrelevant and is only important in relation
 * to the weights of other theme parts. For example: Theme part A with a weight
 * of 0.5 will only have 1/20 of the weight of theme part B with a weight of
 * 5.0...
 */
public class vc_ColorTheme {

    static class ThemePart {

        vc_ColorRange range;
        z_Colors col;
        float weight;

        ThemePart(vc_ColorRange range, z_Colors col, float weight) {
            this.range = range;
            this.col = col;
            this.weight = weight;
        }

        public z_Colors getColor() {
            return range.getColor(col, vc_ColorRange.DEFAULT_VARIANCE);
        }
    }

    protected String name;
    protected ArrayList<ThemePart> parts = new ArrayList<ThemePart>();

    protected float weightedSum;

    public vc_ColorTheme(String name) {
        this.name = name;
    }

    public vc_ColorTheme addRange(vc_ColorRange range, z_Colors col,
            float weight) {
        parts.add(new ThemePart(range, col, weight));
        weightedSum += weight;
        return this;
    }

    public vc_ColorTheme addRange(String descriptor, float weight) {
        StringTokenizer st = new StringTokenizer(descriptor, " ,");
        z_Colors col = null;
        vc_ColorRange range = vc_ColorRange.NEUTRAL;
        while (st.hasMoreTokens()) {
            String item = st.nextToken();
            if (vc_ColorRange.getPresetForName(item) != null) {
                range = vc_ColorRange.getPresetForName(item);
            } else if (z_Colors.getColor(item) != null) {
                col = z_Colors.getColor(item);
            }
        }
        if (range != null) {
            addRange(range, col, weight);
        }
        return this;
    }

    public z_Colors getColor() {
        float rnd = m_MathUtils.random(1f);
        for (ThemePart t : parts) {
            float currWeight = t.weight / weightedSum;
            if (currWeight >= rnd) {
                return t.getColor();
            }
            rnd -= currWeight;
        }
        return null;
    }

    /**
     * Creates a {@link vc_ColorList} of {@link z_Colors}s based on the theme's
     * ranges and balance defined by their weights
     * 
     * @param num
     *            number of colors to create
     * @return new list
     */
    public vc_ColorList getColors(int num) {
        vc_ColorList list = new vc_ColorList();
        for (int i = 0; i < num; i++) {
            list.add(getColor());
        }
        return list;
    }

    /**
     * @return the theme's name
     */
    public String getName() {
        return name;
    }
}
