package com.software.reuze;
import java.util.List;

import com.software.reuze.m_Matrix2D;


public class ga_Transformations {


	//--------------------------- WorldTransform -----------------------------
	//
	//given a List of 2D vectors, a position and  orientation
	//this function transforms the 2D vectors into the object's world space
	//------------------------------------------------------------------------

	public static List<ga_Vector2D> worldTransform(List<ga_Vector2D> points,
			ga_Vector2D   pos,
			ga_Vector2D   forward,
			ga_Vector2D   side,
			ga_Vector2D   scale){

		//create a transformation matrix
		m_Matrix2D matTransform = new m_Matrix2D();

//		forward.normalize();
//		side.normalize();

		//scale
		if ( (scale.x != 1.0) || (scale.y != 1.0) )	
			matTransform.scale(scale.x, scale.y);
		//rotate
		matTransform.rotate(forward, side);
		//and translate
		matTransform.translate(pos.x, pos.y);

		//now transform the object's vertices
		return matTransform.transformVector2D(points);
	}

	public static List<ga_Vector2D> worldTransform(List<ga_Vector2D> points,
			ga_Vector2D   pos,
			ga_Vector2D   forward,
			ga_Vector2D   side){
		return worldTransform(points, pos, forward, side, ga_Vector2D.ONE);
	}


	//--------------------- PointToWorldSpace --------------------------------
	//
	//Transforms a point from the agent's local space into world space
	//------------------------------------------------------------------------
	public static ga_Vector2D pointToWorldSpace(ga_Vector2D point,
			ga_Vector2D AgentHeading,
			ga_Vector2D AgentSide,
			ga_Vector2D AgentPosition)
	{
		
//		AgentHeading.normalize();
//		AgentSide.normalize();
		//create a transformation matrix
		m_Matrix2D matTransform = new m_Matrix2D();

		//rotate
		matTransform.rotate(AgentHeading, AgentSide);
		//matTransform.rotate(Vector2D.normalize(AgentHeading), Vector2D.normalize(AgentSide));

		//and translate
		matTransform.translate(AgentPosition.x, AgentPosition.y);

		//now transform the vertices
		return matTransform.transformVector2D(point);
	}


	//--------------------- VectorToWorldSpace --------------------------------
	//
	//Transforms a vector from the agent's local space into world space
	//------------------------------------------------------------------------
	public static ga_Vector2D vectorToWorldSpace(ga_Vector2D vec,	ga_Vector2D AgentHeading,
			ga_Vector2D AgentSide) {
//		AgentHeading.normalize();
//		AgentSide.normalize();
		
		//create a transformation matrix
		m_Matrix2D matTransform = new m_Matrix2D();

		//rotate
		matTransform.rotate(AgentHeading, AgentSide);

		//now transform and return the vertices
		return matTransform.transformVector2D(vec);
	}


	//--------------------- PointToLocalSpace --------------------------------
	//
	//------------------------------------------------------------------------
	public static ga_Vector2D pointToLocalSpace( ga_Vector2D point,
			ga_Vector2D agentHeading,
			ga_Vector2D agentSide,
			ga_Vector2D agentPosition)
	{
		//create a transformation matrix
		m_Matrix2D matTransform = new m_Matrix2D();

//		agentHeading.normalize();
//		agentSide.normalize();

		double tx = -agentPosition.dot(agentHeading);
		double ty = -agentPosition.dot(agentSide);

		//create the transformation matrix
		matTransform._11(agentHeading.x); 	matTransform._12(agentSide.x);
		matTransform._21(agentHeading.y); 	matTransform._22(agentSide.y);
		matTransform._31(tx);           	matTransform._32(ty);

		//now transform the vertices
		return matTransform.transformVector2D(point);
	}

	//--------------------- VectorToLocalSpace --------------------------------
	//
	//------------------------------------------------------------------------
	public static ga_Vector2D vectorToLocalSpace(ga_Vector2D vec,
			ga_Vector2D AgentHeading,
			ga_Vector2D AgentSide)
	{ 
//		AgentHeading.normalize();
//		AgentSide.normalize();

		//create a transformation matrix
		m_Matrix2D matTransform = new m_Matrix2D();

		//create the transformation matrix
		matTransform._11(AgentHeading.x); matTransform._12(AgentSide.x);
		matTransform._21(AgentHeading.y); matTransform._22(AgentSide.y);

		//now transform the vertices
		return matTransform.transformVector2D(vec);
	}

	//-------------------------- Vec2DRotateAroundOrigin --------------------------
	//
	//rotates a vector ang rads around the origin
	//-----------------------------------------------------------------------------
	public static ga_Vector2D vec2DRotateAroundOrigin(ga_Vector2D v, double ang)
	{
		//create a transformation matrix
		m_Matrix2D mat = new m_Matrix2D();;

		//rotate
		mat.rotate(ang);

		//now transform the object's vertices
		return mat.transformVector2D(v);
	}




	//------------------------ CreateWhiskers ------------------------------------
	//
	//given an origin, a facing direction, a 'field of view' describing the 
	//limit of the outer whiskers, a whisker length and the number of whiskers
	//this method returns a vector containing the end positions of a series
	//of whiskers radiating away from the origin and with equal distance between
	//them. (like the spokes of a wheel clipped to a specific segment size)
	//----------------------------------------------------------------------------
	public static ga_Vector2D[] createWhiskers(int  nbrWhiskers,
			double        whiskerLength,
			double        fov,
			ga_Vector2D      facing,
			ga_Vector2D      origin){
		//this is the magnitude of the angle separating each whisker
		double SectorSize = fov/(double)(nbrWhiskers-1);

		ga_Vector2D[] whiskers = new ga_Vector2D[nbrWhiskers];
		ga_Vector2D temp;
		double angle = -fov*0.5; 

		for (int w=0; w<nbrWhiskers; ++w) {			
			temp = vec2DRotateAroundOrigin(facing, angle);
			//temp.mult(whiskerLength);
			temp.mult(whiskerLength * (0.75 + 0.25 * m_MathFast.abs(m_MathFast.cos(angle))) );
			temp.add(origin);
			whiskers[w] = temp;
			angle+=SectorSize;
		}
		return whiskers;
	}


}
