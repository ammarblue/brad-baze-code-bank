package com.software.reuze;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;


/**
 * Copyright (c) 2003-2007, Xith3D Project Group all rights reserved.
 * 
 * Portions based on the Java3D interface, Copyright by Sun Microsystems.
 * Many thanks to the developers of Java3D and Sun Microsystems for their
 * innovation and design.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of the 'Xith3D Project Group' nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
 * RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE
 */

/**
 * A 3D Flat Face.
 * 
 * @author Kevin Glass
 */
public class gb_FaceWithMaterial extends AbstractCollection<gb_Vertex3DIndex>
{
	public int faceNormal;
	public int vtnIndices, count; //assumes indices for a face are consecutive
	public int getVtnIndices() {
		return vtnIndices;
	}

	public void setVtnIndices(int vtnIndices) {
		this.vtnIndices = vtnIndices;
	}

	public vc_Material  mat;
    
    public vc_Material getMaterial()
    {
        return ( mat );
    }

    public int size()
    {
        return count;
    }
    
    public gb_FaceWithMaterial( int count, vc_Material mat )
    {        
        super();
        faceNormal=-1;
        this.mat = mat;
    }

	@Override
	public Iterator<gb_Vertex3DIndex> iterator() {
		return null;
	}
	public boolean add(gb_Vertex3DIndex v) {
		return false;
	}
	public String toString() {
		return null;
	}
	public gb_Vertex3DIndex[] toArray() {
		return null;
	}
	public gb_Vertex3DIndex[] toArray(gb_Vertex3DIndex[] va) {
		return null;
	}
	public void clear() {
		count=0;
        faceNormal=-1;
        vtnIndices=-1;
        mat=null;
	}
	public boolean contains(Object o) {
		return false;
	}
	public int getFaceNormal() {
		return faceNormal;
	}

	public void setFaceNormal(int faceNormal) {
		this.faceNormal = faceNormal;
	}

	public vc_Material getMat() {
		return mat;
	}

	public void setMat(vc_Material mat) {
		this.mat = mat;
	}

	public static void main(String args[]) {
		gb_FaceWithMaterial face=new gb_FaceWithMaterial(3, null);
		gb_Vertex3DIndex v=new gb_Vertex3DIndex(1,2,3);
		face.add(v);
		System.out.println(face.isEmpty());
		System.out.println(face.size());
		System.out.println(face);
		AbstractCollection<gb_Vertex3DIndex> c=(AbstractCollection<gb_Vertex3DIndex>)face;
		v.set(7,8,9);
		c.add(v);
		System.out.println(face);
		System.out.println(Arrays.toString(c.toArray()));
		System.out.println(c.contains(v));
	}
}
