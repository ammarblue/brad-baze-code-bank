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

public class gb_Vector3Id extends gb_Vector3 {

    public final gb_Vector3 normal = new gb_Vector3();

    public final int id;

    public gb_Vector3Id(gb_Vector3 v, int id) {
        super(v);
        this.id = id;
    }

    final void addFaceNormal(final gb_Vector3 n) {
        normal.add(n);
    }

    final void clearNormal() {
        normal.clr();
    }

    final void computeNormal() {
        normal.nor();
    }

    public String toString() {
        return id + ": p: " + super.toString() + " n:" + normal.toString();
    }
}