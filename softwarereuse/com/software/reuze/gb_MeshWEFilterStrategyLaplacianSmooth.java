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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Applies a laplacian smooth function to all vertices in the mesh
 * 
 */
public class gb_MeshWEFilterStrategyLaplacianSmooth implements gb_i_MeshWEFilterStrategy {

    public void filter(gb_a_Vector3IdSelector selector, int numIterations) {
        final Collection<gb_Vector3Id> selection = selector.getSelection();
        if (!(selector.getMesh() instanceof gb_WETriangleMesh)) {
            throw new IllegalArgumentException(
                    "This filter requires a WETriangleMesh");
        }
        final gb_WETriangleMesh mesh = (gb_WETriangleMesh) selector.getMesh();
        final HashMap<gb_Vector3Id, gb_Vector3> filtered = new HashMap<gb_Vector3Id, gb_Vector3>(
                selection.size());
        for (int i = 0; i < numIterations; i++) {
            filtered.clear();
            for (gb_Vector3Id v : selection) {
                final gb_Vector3 laplacian = new gb_Vector3();
                final List<gb_WEVertex> neighbours = ((gb_WEVertex) v).getNeighbors();
                for (gb_WEVertex n : neighbours) {
                    laplacian.add(n);
                }
                laplacian.mul(1f / neighbours.size());
                filtered.put(v, laplacian);
            }
            for (gb_Vector3Id v : filtered.keySet()) {
                mesh.vertices.get(v).set(filtered.get(v));
            }
            mesh.rebuildIndex();
        }
        mesh.computeFaceNormals();
        mesh.computeVertexNormals();
    }

    public void filter(gb_WETriangleMesh mesh, int numIterations) {
        filter(new gb_Vector3IdSelectorDefault(mesh).selectVertices(), numIterations);
    }
}
