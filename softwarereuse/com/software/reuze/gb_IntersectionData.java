package com.software.reuze;

/*
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



public class gb_IntersectionData {

    public boolean isIntersection;
    public float dist;
    public gb_Vector3 pos;
    public gb_Vector3 dir;
    public gb_Vector3 normal;

    public gb_IntersectionData() {

    }

    public gb_IntersectionData(gb_IntersectionData isec) {
        isIntersection = isec.isIntersection;
        dist = isec.dist;
        pos = isec.pos.cpy();
        dir = isec.dir.cpy();
        normal = isec.normal.cpy();
    }

    public void clear() {
        isIntersection = false;
        dist = 0;
        pos = new gb_Vector3();
        dir = new gb_Vector3();
        normal = new gb_Vector3();
    }

    public String toString() {
        String s = "isec: " + isIntersection;
        if (isIntersection) {
            s += " at:" + pos + " dist:" + dist + "normal:" + normal;
        }
        return s;
    }
}