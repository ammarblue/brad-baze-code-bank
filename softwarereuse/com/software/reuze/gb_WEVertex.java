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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class gb_WEVertex extends gb_Vector3Id {

    public List<gb_WEWingedEdge> edges = new ArrayList<gb_WEWingedEdge>(4);

    public gb_WEVertex(gb_Vector3 v, int id) {
        super(v, id);
    }

    public void addEdge(gb_WEWingedEdge e) {
        edges.add(e);
    }

    public List<gb_WEWingedEdge> getEdges() {
        return edges;
    }

    public gb_WEVertex getNeighborInDirection(gb_Vector3 dir, float tolerance) {
        gb_WEVertex closest = null;
        float delta = 1 - tolerance;
        for (gb_WEVertex n : getNeighbors()) {
            float d = n.tmp().sub(this).nor().dot(dir);
            if (d > delta) {
                closest = n;
                delta = d;
            }
        }
        return closest;
    }

    public List<gb_WEVertex> getNeighbors() {
        List<gb_WEVertex> neighbors = new ArrayList<gb_WEVertex>(edges.size());
        for (gb_WEWingedEdge e : edges) {
            neighbors.add(e.getOtherEndFor(this));
        }
        return neighbors;
    }

    /**
     * Returns a list of all faces this vertex belongs to.
     * 
     * @return face list
     */
    public List<gb_WEFace> getRelatedFaces() {
        Set<gb_WEFace> faces = new HashSet<gb_WEFace>(edges.size() * 2);
        for (gb_WEWingedEdge e : edges) {
            faces.addAll(e.faces);
        }
        return new ArrayList<gb_WEFace>(faces);
    }

    public void removeEdge(gb_WEWingedEdge e) {
        edges.remove(e);
    }

    public String toString() {
        return id + " {" + x + "," + y + "," + z + "}";
    }
}