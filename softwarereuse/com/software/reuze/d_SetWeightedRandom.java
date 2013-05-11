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

package com.software.reuze;

import java.util.ArrayList;
import java.util.List;

import com.software.reuze.m_MathUtils;


/**
 * This class provides a generic random-weight distribution of arbitrary objects.
 * Add elements with their weight to the set and then use the
 * {@link #getRandom()} method to retrieve objects. The frequency of returned
 * elements is based on their relative weight. This makes it easy to provide
 * biased preferences.
 * 
 * http://www.electricmonk.nl/log/2009/12/23/weighted-random-distribution/
 */
public class d_SetWeightedRandom<T> {

    protected List<d_SetEntryInt<T>> elements = new ArrayList<d_SetEntryInt<T>>();

    protected int totalWeight;

    /**
     * Add a new element of type T to the set.
     * 
     * @param item
     * @param weight
     * @return itself
     */
    public d_SetWeightedRandom<T> add(T item, int weight) {
        d_SetEntryInt<T> e = new d_SetEntryInt<T>(item, weight);
        int num = elements.size();
        boolean isInserted = false;
        if (num > 0) {
            for (int i = 0; i < num; i++) {
                if (weight < elements.get(i).weight) {
                    elements.add(i, e);
                    isInserted = true;
                    break;
                }
            }
        }
        if (!isInserted) {
            elements.add(e);
        }
        totalWeight += weight;
        return this;
    }

    /**
     * @return the elements
     */
    public List<d_SetEntryInt<T>> getElements() {
        return elements;
    }

    /**
     * Returns a randomly picked element from the set. The frequency of
     * occurrence depends on the relative weight of each item.
     * 
     * @return picked element
     */
    public T getRandom() {
        int rnd = m_MathUtils.random(totalWeight);
        T choice = null;
        int sum = totalWeight;
        for (d_SetEntryInt<T> e : elements) {
            sum -= e.weight;
            if (sum <= rnd) {
                choice = e.item;
                break;
            }
        }
        return choice;
    }

    /**
     * Removes the given item from the set.
     * 
     * @param item
     */
    public void remove(T item) {
        for (d_SetEntryInt<T> e : elements) {
            if (e.item.equals(item)) {
                elements.remove(e);
                totalWeight -= e.weight;
                return;
            }
        }
    }
}
