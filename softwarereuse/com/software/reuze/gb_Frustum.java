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
//package com.badlogic.gdx.math;

/*import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.math.Plane.PlaneSide;
import com.badlogic.gdx.math.collision.BoundingBox;*/

public class gb_Frustum {
	protected static final gb_Vector3[] clipSpacePlanePoints = { new gb_Vector3(-1, -1, -1), new gb_Vector3(1, -1, -1), new gb_Vector3(1, 1, -1), new gb_Vector3(-1, 1, -1), // near clip
		   												   new gb_Vector3(-1, -1, 1), new gb_Vector3(1, -1, 1), new gb_Vector3(1, 1, 1), new gb_Vector3(-1, 1, 1) }; // far clip
	protected static final float[] clipSpacePlanePointsArray = new float[8 * 3];	
	
	static {
		int j = 0;
		for(gb_Vector3 v: clipSpacePlanePoints) {
			clipSpacePlanePointsArray[j++] = v.x;
			clipSpacePlanePointsArray[j++] = v.y;
			clipSpacePlanePointsArray[j++] = v.z;
		}
	}
	
	/** the six clipping planes, near, far, left, right, top, bottom **/
	public final gb_Plane[] planes = new gb_Plane[6];	
	
	/** eight points making up the near and far clipping "rectangles". order is counter clockwise, starting at bottom left **/
	public final gb_Vector3[] planePoints = { new gb_Vector3(), new gb_Vector3(), new gb_Vector3(), new gb_Vector3(), 
			new gb_Vector3(), new gb_Vector3(), new gb_Vector3(), new gb_Vector3() 
	};	
	protected final float[] planePointsArray = new float[8 *  3];
		
	public gb_Frustum() {
		for(int i = 0; i < 6; i++) {
			planes[i] = new gb_Plane(new gb_Vector3(), 0);
		}
	}
	public gb_Plane getNear() {return planes[0];}
	public gb_Plane getFar() {return planes[1];}
	public gb_Plane getLeft() {return planes[2];}
	public gb_Plane getRight() {return planes[3];}
	public gb_Plane getTop() {return planes[4];}
	public gb_Plane getBottom() {return planes[5];}
		
	/**
	 * Updates the clipping plane's based on the given inverse combined projection and view
	 * matrix, e.g. from an {@link OrthographicCamera} or {@link PerspectiveCamera}.
	 * @param inverseProjectionView the combined projection and view matrices.
	 */
	public void update(m_Matrix4 inverseProjectionView) {						
		System.arraycopy(clipSpacePlanePointsArray, 0, planePointsArray, 0, clipSpacePlanePointsArray.length);
		assert false; //Matrix4.prj(inverseProjectionView.val, planePointsArray, 0, 8, 3);
		for(int i = 0, j=0; i < 8; i++) {
			gb_Vector3 v = planePoints[i];
			v.x = planePointsArray[j++];
			v.y = planePointsArray[j++];
			v.z = planePointsArray[j++];
		}			
		
		planes[0].set(planePoints[1], planePoints[0], planePoints[2]);
		planes[1].set(planePoints[4], planePoints[5], planePoints[7]);
		planes[2].set(planePoints[0], planePoints[4], planePoints[3]);
		planes[3].set(planePoints[5], planePoints[1], planePoints[6]);
		planes[4].set(planePoints[2], planePoints[3], planePoints[6]);
		planes[5].set(planePoints[4], planePoints[0], planePoints[1]);
	}	
	
	/**
	 * Returns whether the point is in the frustum.
	 * 
	 * @param point The point
	 * @return Whether the point is in the frustum.
	 */
	public boolean pointInFrustum (gb_Vector3 point) {
		for (int i = 0; i < planes.length; i++) {
			gb_Plane.PlaneSide result = planes[i].testPoint(point);
			if (result == gb_Plane.PlaneSide.Back) return false;
		}

		return true;
	}

	/**
	 * Returns whether the given sphere is in the frustum.
	 * 
	 * @param center The center of the sphere
	 * @param radius The radius of the sphere
	 * @return Whether the sphere is in the frustum
	 */
	public boolean sphereInFrustum (gb_Vector3 center, float radius) {
		for (int i = 0; i < planes.length; i++)
			if (planes[i].distance(center) < -radius) return false;

		return true;
	}

	/**
	 * Returns whether the given sphere is in the frustum not checking whether it is behind the near and far clipping plane.
	 * 
	 * @param center The center of the sphere
	 * @param radius The radius of the sphere
	 * @return Whether the sphere is in the frustum
	 */
	public boolean sphereInFrustumWithoutNearFar (gb_Vector3 center, float radius) {
		for (int i = 0; i < planes.length; i++)
			if (planes[i].distance(center) < -radius) return false;

		return true;
	}

	/**
	 * Returns whether the given {@link gb_BoundingBox} is in the frustum.
	 * 
	 * @param bounds The bounding box
	 * @return Whether the bounding box is in the frustum
	 */
	public boolean boundsInFrustum (gb_BoundingBox bounds) {
		gb_Vector3[] corners = bounds.getCorners();
		int len = corners.length;

		for (int i = 0, len2 = planes.length; i < len2; i++) {
			int out = 0;

			for (int j = 0; j < len; j++)
				if (planes[i].testPoint(corners[j]) == gb_Plane.PlaneSide.Back) out++;

			if (out == 8) return false;
		}

		return true;
	}	
	
//	/**
//	 * Calculates the pick ray for the given window coordinates. Assumes the window coordinate system has it's y downwards. The
//	 * returned Ray is a member of this instance so don't reuse it outside this class.
//	 * 
//	 * @param screen_width The window width in pixels
//	 * @param screen_height The window height in pixels
//	 * @param mouse_x The window x-coordinate
//	 * @param mouse_y The window y-coordinate
//	 * @param pos The camera position
//	 * @param dir The camera direction, having unit length
//	 * @param up The camera up vector, having unit length
//	 * @return the picking ray.
//	 */
//	public Ray calculatePickRay (float screen_width, float screen_height, float mouse_x, float mouse_y, Vector3 pos, Vector3 dir,
//		Vector3 up) {
//		float n_x = mouse_x - screen_width / 2.0f;
//		float n_y = mouse_y - screen_height / 2.0f;
//		n_x /= screen_width / 2.0f;
//		n_y /= screen_height / 2.0f;
//
//		Z.set(dir.tmp().mul(-1)).nor();
//		X.set(up.tmp().crs(Z)).nor();
//		Y.set(Z.tmp().crs(X)).nor();
//		near_center.set(pos.tmp3().sub(Z.tmp2().mul(near)));
//		Vector3 near_point = X.tmp3().mul(near_width).mul(n_x).add(Y.tmp2().mul(near_height).mul(n_y));
//		near_point.add(near_center);
//
//		return ray.set(near_point.tmp(), near_point.sub(pos).nor());
//	}
	
//	public static void main(String[] argv) {
//		PerspectiveCamera camera = new PerspectiveCamera(45, 2, 2);
////		camera.rotate(90, 0, 1, 0);
//		camera.update();
//		System.out.println(camera.direction);
//		System.out.println(Arrays.toString(camera.frustum.planes));
//		
//		OrthographicCamera camOrtho = new OrthographicCamera(2, 2);
//		camOrtho.near = 1;
////		camOrtho.rotate(90, 1, 0, 0);
//		camOrtho.update();
//		System.out.println(camOrtho.direction);
//		System.out.println(Arrays.toString(camOrtho.frustum.planes));
//	}
}
