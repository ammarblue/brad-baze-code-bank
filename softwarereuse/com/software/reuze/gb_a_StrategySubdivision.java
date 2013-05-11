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

import java.util.Comparator;
import java.util.List;


/**
 * This is the abstract parent class for all subdivision strategies. Each of its
 * implementations defines a concrete solution to split a single edge of a
 * {@link gb_WETriangleMesh}.
 */
public abstract class gb_a_StrategySubdivision {

    public static final Comparator<? super gb_WEWingedEdge> DEFAULT_ORDERING = new gb_EdgeLengthComparator();

    protected Comparator<? super gb_WEWingedEdge> order = DEFAULT_ORDERING;

    /**
     * Computes a number of points on (or near) the given edge which are used
     * for splitting the edge in smaller segments.
     * 
     * @param edge
     *            edge to split
     * @return list of split points
     */
    public abstract List<gb_Vector3> computeSplitPoints(gb_WEWingedEdge edge);

    /**
     * Returns the {@link Comparator} used to sort a mesh's edge list based on a
     * certain criteria. By default the {@link gb_EdgeLengthComparator} is used.
     * 
     * @return edge comparator
     */
    public Comparator<? super gb_WEWingedEdge> getEdgeOrdering() {
        return order;
    }

    /**
     * Sets the given edge list {@link Comparator} for a strategy
     * implementation.
     * 
     * @param order
     */
    public void setEdgeOrdering(Comparator<? super gb_WEWingedEdge> order) {
        this.order = order;
    }
}
