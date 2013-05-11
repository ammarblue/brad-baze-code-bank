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
import java.util.List;


/**
 * This subdivision strategy splits an edge in three equal parts using two split
 * points at 33% and 66% of the edge.
 */
public class gb_StrategySubdivisionDual extends gb_a_StrategySubdivision {

    public List<gb_Vector3> computeSplitPoints(gb_WEWingedEdge edge) {
        List<gb_Vector3> mid = new ArrayList<gb_Vector3>(2);
        mid.add(edge.a.tmp().interpolate(edge.b, 0.3333f));
        mid.add(edge.a.tmp().interpolate(edge.b, 0.6666f));
        return mid;
    }
}
