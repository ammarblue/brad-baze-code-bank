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


public final class gb_WEFace extends gb_TriangleFace {

    public List<gb_WEWingedEdge> edges = new ArrayList<gb_WEWingedEdge>(3);

    public gb_WEFace(gb_WEVertex a, gb_WEVertex b, gb_WEVertex c) {
        super(a, b, c);
    }

    public gb_WEFace(gb_WEVertex a, gb_WEVertex b, gb_WEVertex c, ga_Vector2 uvA, ga_Vector2 uvB,
            ga_Vector2 uvC) {
        super(a, b, c, uvA, uvB, uvC);
    }

    public void addEdge(gb_WEWingedEdge e) {
        edges.add(e);
    }

    /**
     * @return the edges
     */
    public List<gb_WEWingedEdge> getEdges() {
        return edges;
    }

    public final gb_WEVertex[] getVertices(gb_WEVertex[] verts) {
        if (verts != null) {
            verts[0] = (gb_WEVertex) a;
            verts[1] = (gb_WEVertex) b;
            verts[2] = (gb_WEVertex) c;
        } else {
            verts = new gb_WEVertex[] { (gb_WEVertex) a, (gb_WEVertex) b, (gb_WEVertex) c };
        }
        return verts;
    }
}