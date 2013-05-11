package com.software.reuze;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/** Represents an MD5 (Doom 3) skinned model. Note: The normal interpolation implementation is experimental. Using it will incur a
 * greater CPU overhead, and correct normals for dynamically lit models are not guaranteed at this time. Expert contribution for
 * this code is encouraged, please email Dave if you're interested in helping.
 * @author Mario Zechner <contact@badlogicgames.com>, Nathan Sweet <admin@esotericsoftware.com>, Dave Clayton
 *         <contact@redskyforge.com> */
public class ff_MD5Model {
	public int numJoints;
	public ff_MD5Joints baseSkeleton;
	public ff_MD5Mesh[] meshes;

	public int getNumVertices () {
		int numVertices = 0;

		for (int i = 0; i < meshes.length; i++)
			numVertices += meshes[i].numVertices;

		return numVertices;
	}

	public int getNumTriangles () {
		int numTriangles = 0;

		for (int i = 0; i < meshes.length; i++)
			numTriangles += meshes[i].numTriangles;

		return numTriangles;
	}

	public void read (DataInputStream in) throws IOException {
		numJoints = in.readInt();
		baseSkeleton = new ff_MD5Joints();
		baseSkeleton.read(in);
		int numMeshes = in.readInt();
		meshes = new ff_MD5Mesh[numMeshes];
		for (int i = 0; i < numMeshes; i++) {
			meshes[i] = new ff_MD5Mesh();
			meshes[i].read(in);
		}
	}

	public void write (DataOutputStream out) throws IOException {
		out.writeInt(numJoints);
		baseSkeleton.write(out);
		out.writeInt(meshes.length);
		for (int i = 0; i < meshes.length; i++) {
			meshes[i].write(out);
		}
	}
	
	public void calculateNormals (float[][] vertices /*out*/) {
		for (int i = 0; i < meshes.length; i++) {
			ff_MD5Mesh mesh = meshes[i];
			mesh.calculateNormalsBind(baseSkeleton, vertices[i]);
		}
	}
	
	public void setSkeleton (gb_BoundingBox mBBox, ff_MD5Joints skeleton, float[][] vertices /*out*/, boolean useNormals) {
		mBBox.clr();
		for (int i = 0; i < meshes.length; i++) {
			ff_MD5Mesh mesh = meshes[i];
			if (useNormals) {
				mesh.calculateVerticesN(skeleton, vertices[i], mBBox);
			} else {
				mesh.calculateVertices(skeleton, vertices[i], mBBox);
			}
		}
	}
}
