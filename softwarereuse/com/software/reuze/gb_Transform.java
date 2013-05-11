package com.software.reuze;
//---------------------------------------------------------------------------
import java.lang.Math;
/* declarations for 3D rotation/translation class */
/*

            Absolute Moves
            --------------
View                             Object
----                             ------
_3DMove()                        _3DMoveObject


            Relative Moves
            --------------
View                             Object
----                             ------
 _3DMoveForward()                _3DMoveForwardObject()
 _3DMoveRight()                  _3DMoveRightObject()
 _3DMoveUp()                     _3DMoveUpObject()


            Absolute Rotations
            ------------------
View                             Object
----                             ------
_3DAxis/Angle()                  _3DAxis/AngleObject()


            Relative Rotations (roll, pitch, yaw)
            -------------------------------------
View                             Object
----                             ------
_3DRoll()                        _3DRollObject()
_3DRotateRight()                 _3DRotateRightObject()
_3DRotateUp()                    _3DRotateUpObject


            Absolute Move + Absolute Rotation using Euler Angles
            -----------------------------------------
View                             Object
----                             ------
_3DPOV()                         _3DSetObject()


            Absolute Move + Absolute Rotation using Look At vector
            -----------------------------------------
View                             Object
----                             ------
_3DLookAt()                       ?

*/


/* -------------------------------------------------------------------------- 
by Diana Gruber

This is the code I wrote to illustrate some of the ideas I had about
3D rotation. This is NOT THE SOURCE CODE TO FASTGRAPH. Fastgraph is
written in assembly language. It is much faster than this C++ code.

This code illustrates the concepts in my paper "The Mathematics of the 
3D Rotation Matrix" presented at XGDC on October 1, 2000. Hopefully
you will find it easy to follow and mildly interesting. If you are
looking at this in hard copy and you want to download it, try
http://www.makegames.com/3Drotation or email me at dgruber@fastgraph.com.

-------------------------------------------------------------------------- */
public class gb_Transform {
	double WorldUp[];
        double ViewMoveMatrix[];
        double ViewRotationMatrix[];
        double ObjectMoveMatrix[];
        double ObjectRotationMatrix[];
        double WorldTransform[];
        double ObjectTransform[];
        double CombinedTransform[];
//---------------------------------------------------------------------------
public gb_Transform() {
   /* Constructor -- initialize */

   // initialize WorldUp
   WorldUp=new double[3];
   ViewMoveMatrix=new double[12];
   ViewRotationMatrix=new double[12];
   ObjectMoveMatrix=new double[12];
   ObjectRotationMatrix=new double[12];
   WorldTransform=new double[12];
   ObjectTransform=new double[12];
   CombinedTransform=new double[12];
   WorldUp[0] = 0;
   WorldUp[1] = 1.0;
   WorldUp[2] = 0;

   // initialize the World Transform
   POV(0,0,0,0,0,0);

   // initialize the Object Transform
   SetObject(0,0,0,0,0,0);

}
	//---------------------------------------------------------------------------
	public boolean AxisAngle (double x, double y, double z, double theta) {
		/* This function performs an axis/angle rotation. (x,y,z) is any 
		 vector on the axis. For greater speed, always use a unit vector 
		 (length = 1). In this version, we will assume an arbitrary 
		 length and normalize. */
		
		double length;
		double c,s,t;
		
		// normalize
		length = Math.sqrt(x*x + y*y + z*z);
		
		// too close to 0, can't make a normalized vector
		if (length < .000001)
			return false;
		
		x /= length;
		y /= length;
		z /= length;
		
		// do the trig
		c = Math.cos(theta);
		s = Math.sin(theta);
		t = 1-c;   
		
		// build the rotation matrix
		ViewRotationMatrix[0] = t*x*x + c;
		ViewRotationMatrix[1] = t*x*y - s*z;
		ViewRotationMatrix[2] = t*x*z + s*y;
		ViewRotationMatrix[3] = 0;
		
		ViewRotationMatrix[4] = t*x*y + s*z;
		ViewRotationMatrix[5] = t*y*y + c;
		ViewRotationMatrix[6] = t*y*z - s*x;
		ViewRotationMatrix[7] = 0;
		
		ViewRotationMatrix[8] = t*x*z - s*y;
		ViewRotationMatrix[9] = t*y*z + s*x;
		ViewRotationMatrix[10] = t*z*z + c;
		ViewRotationMatrix[11] = 0;
		
		// build the transform
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
		return true;
	}
	//---------------------------------------------------------------------------
	public boolean AxisAngleObject (double x, double y, double z, double theta) {
		/* This function performs an axis/angle rotation. (x,y,z) is any 
		 vector on the axis. For greater speed, always use a unit vector 
		 (length = 1). In this version, we will assume an arbitrary 
		 length and normalize. */
		
		double length;
		double c,s,t;
		
		// normalize
		length = Math.sqrt(x*x + y*y + z*z);
		
		// too close to 0, can't make a normalized vector
		if (length < .000001)
			return false;
		
		x /= length;
		y /= length;
		z /= length;
		
		// do the trig
		c = Math.cos(theta);
		s = Math.sin(theta);
		t = 1-c;   
		
		// build the rotation matrix
		ObjectRotationMatrix[0] = t*x*x + c;
		ObjectRotationMatrix[1] = t*x*y - s*z;
		ObjectRotationMatrix[2] = t*x*z + s*y;
		ObjectRotationMatrix[3] = 0;
		
		ObjectRotationMatrix[4] = t*x*y + s*z;
		ObjectRotationMatrix[5] = t*y*y + c;
		ObjectRotationMatrix[6] = t*y*z - s*x;
		ObjectRotationMatrix[7] = 0;
		
		ObjectRotationMatrix[8] = t*x*z - s*y;
		ObjectRotationMatrix[9] = t*y*z + s*x;
		ObjectRotationMatrix[10] = t*z*z + c;
		ObjectRotationMatrix[11] = 0;
		
		// build the transform
		MatrixMultiply(ObjectMoveMatrix,ObjectRotationMatrix,ObjectTransform);
		MatrixMultiply(WorldTransform,ObjectTransform,CombinedTransform);
		return true;
	}
	double temp[]=new double[6];
	//---------------------------------------------------------------------------
	public double[] GetView ()
	{
		/* Returns the current position and a unit vector representing
		 the direction of the rotation */
		
		// the current position is the 4th column of the translation matrix
		temp[0] = -ViewMoveMatrix[3];
		temp[1] = -ViewMoveMatrix[7];
		temp[2] = -ViewMoveMatrix[11];
		
		// the "out" unit vector is the 3rd row of the rotation matrix
		temp[3] = ViewRotationMatrix[8];
		temp[4] = ViewRotationMatrix[9];
		temp[5] = ViewRotationMatrix[10];
		return temp;
	}
	double ViewOut[]=new double[3];      // the View or "new Z" vector
	double ViewUp[]=new double[3];       // the Up or "new Y" vector
	double ViewRight[]=new double[3];    // the Right or "new X" vector
	//---------------------------------------------------------------------------
	public boolean LookAt (double x1, double y1, double z1, double x2, double y2, double z2)
	{
		/* Build a transform as if you were at a point (x1,y1,z1), and
		 looking at a point (x2,y2,z2) */
		
		double ViewMagnitude;   // for normalizing the View vector
		double UpMagnitude;     // for normalizing the Up vector
		double UpProjection;    // magnitude of projection of View Vector on World UP
		
		// first, calculate and normalize the view vector
		ViewOut[0] = x2-x1;
		ViewOut[1] = y2-y1;
		ViewOut[2] = z2-z1;
		ViewMagnitude = Math.sqrt(ViewOut[0]*ViewOut[0] + ViewOut[1]*ViewOut[1]+
							 ViewOut[2]*ViewOut[2]);
		
		// invalid points (not far enough apart)
		if (ViewMagnitude < .000001)
			return false;
		
		// normalize. This is the unit vector in the "new Z" direction
		ViewOut[0] = ViewOut[0]/ViewMagnitude;
		ViewOut[1] = ViewOut[1]/ViewMagnitude;
		ViewOut[2] = ViewOut[2]/ViewMagnitude;
		
		// Now the hard part: The ViewUp or "new Y" vector
		
		// dot product of ViewOut vector and World Up vector gives projection of
		// of ViewOut on WorldUp
		UpProjection = ViewOut[0]*WorldUp[0] + ViewOut[1]*WorldUp[1]+
		ViewOut[2]*WorldUp[2];
		
		// first try at making a View Up vector: use World Up
		ViewUp[0] = WorldUp[0] - UpProjection*ViewOut[0];
		ViewUp[1] = WorldUp[1] - UpProjection*ViewOut[1];
		ViewUp[2] = WorldUp[2] - UpProjection*ViewOut[2];
		
		// Check for validity:
		UpMagnitude = ViewUp[0]*ViewUp[0] + ViewUp[1]*ViewUp[1] + ViewUp[2]*ViewUp[2];
		
		if (UpMagnitude < .0000001)
		{
			//Second try at making a View Up vector: Use Y axis default  (0,1,0)
			ViewUp[0] = -ViewOut[1]*ViewOut[0];
			ViewUp[1] = 1-ViewOut[1]*ViewOut[1];
			ViewUp[2] = -ViewOut[1]*ViewOut[2];
			
			// Check for validity:
			UpMagnitude = ViewUp[0]*ViewUp[0] + ViewUp[1]*ViewUp[1] + ViewUp[2]*ViewUp[2];
			
			if (UpMagnitude < .0000001)
			{
				//Final try at making a View Up vector: Use Z axis default  (0,0,1)
				ViewUp[0] = -ViewOut[2]*ViewOut[0];
				ViewUp[1] = -ViewOut[2]*ViewOut[1];
				ViewUp[2] = 1-ViewOut[2]*ViewOut[2];
				
				// Check for validity:
				UpMagnitude = ViewUp[0]*ViewUp[0] + ViewUp[1]*ViewUp[1] + ViewUp[2]*ViewUp[2];
				
				if (UpMagnitude < .0000001)
					return false;
			}
		}
		
		// normalize the Up Vector
		UpMagnitude = Math.sqrt(UpMagnitude);
		ViewUp[0] = ViewUp[0]/UpMagnitude;
		ViewUp[1] = ViewUp[1]/UpMagnitude;
		ViewUp[2] = ViewUp[2]/UpMagnitude;
		
		// Calculate the Right Vector. Use cross product of Out and Up.
		ViewRight[0] = -ViewOut[1]*ViewUp[2] + ViewOut[2]*ViewUp[1];
		ViewRight[1] = -ViewOut[2]*ViewUp[0] + ViewOut[0]*ViewUp[2];
		ViewRight[2] = -ViewOut[0]*ViewUp[1] + ViewOut[1]*ViewUp[0];
		
		// Plug values into rotation matrix R
		ViewRotationMatrix[0]=ViewRight[0];
		ViewRotationMatrix[1]=ViewRight[1];
		ViewRotationMatrix[2]=ViewRight[2];
		ViewRotationMatrix[3]=0;
		
		ViewRotationMatrix[4]=ViewUp[0];
		ViewRotationMatrix[5]=ViewUp[1];
		ViewRotationMatrix[6]=ViewUp[2];
		ViewRotationMatrix[7]=0;
		
		ViewRotationMatrix[8]=ViewOut[0];
		ViewRotationMatrix[9]=ViewOut[1];
		ViewRotationMatrix[10]=ViewOut[2];
		ViewRotationMatrix[11]=0;
		
		// Plug values into translation matrix T
		MoveFill(ViewMoveMatrix,-x1,-y1,-z1);
		
		// build the World Transform
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
		
		return true;
	}
	//---------------------------------------------------------------------------
	public void Move(double x, double y, double z)
	{
		/* Absolute move, without changing the rotation */
		
		ViewMoveMatrix[3] = -x;
		ViewMoveMatrix[7] = -y;
		ViewMoveMatrix[11]= -z;
		
		// build the transform
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
	}
	
	//---------------------------------------------------------------------------
	public void MoveObject(double x, double y, double z)
	{
		/* Absolute move, without changing the rotation */
		
		ObjectMoveMatrix[3] = x;
		ObjectMoveMatrix[7] = y;
		ObjectMoveMatrix[11]= z;
		
		// build the transform
		MatrixMultiply(ObjectMoveMatrix,ObjectRotationMatrix,ObjectTransform);
		MatrixMultiply(WorldTransform,ObjectTransform,CombinedTransform);
	}
	
	//---------------------------------------------------------------------------
	public void MoveForward(double n)
	{
		/* Take the 3rd row of the rotation matrix, multiply by a constant
		 and add to the translation vector */
		
		ViewMoveMatrix[3] = ViewMoveMatrix[3] - n*ViewRotationMatrix[8];
		ViewMoveMatrix[7] = ViewMoveMatrix[7] - n*ViewRotationMatrix[9];
		ViewMoveMatrix[11]= ViewMoveMatrix[11]- n*ViewRotationMatrix[10];
		
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
	}
	
	//---------------------------------------------------------------------------
	public void MoveForwardObject(double n)
	{
		/* Take the 3rd row of the rotation matrix, multiply by a constant
		 and add to the translation vector */
		
		ObjectMoveMatrix[3] = ObjectMoveMatrix[3] - n*ObjectRotationMatrix[8];
		ObjectMoveMatrix[7] = ObjectMoveMatrix[7] - n*ObjectRotationMatrix[9];
		ObjectMoveMatrix[11]= ObjectMoveMatrix[11]- n*ObjectRotationMatrix[10];
		
		// build the transform
		MatrixMultiply(ObjectMoveMatrix,ObjectRotationMatrix,ObjectTransform);
		MatrixMultiply(WorldTransform,ObjectTransform,CombinedTransform);
	}
	
	//---------------------------------------------------------------------------
	public void MoveRight(double n)
	{
		/* Take the 1st row of the rotation matrix, multiply by a constant
		 and add to the translation vector */
		
		ViewMoveMatrix[3] = ViewMoveMatrix[3] - n*ViewRotationMatrix[0];
		ViewMoveMatrix[7] = ViewMoveMatrix[7] - n*ViewRotationMatrix[1];
		ViewMoveMatrix[11]= ViewMoveMatrix[11]- n*ViewRotationMatrix[2];
		
		// build the transform
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
	}
	
	//---------------------------------------------------------------------------
	public void MoveRightObject(double n)
	{
		/* Take the 1st row of the rotation matrix, multiply by a constant
		 and add to the translation vector */
		
		ObjectMoveMatrix[3] = ObjectMoveMatrix[3] - n*ObjectRotationMatrix[0];
		ObjectMoveMatrix[7] = ObjectMoveMatrix[7] - n*ObjectRotationMatrix[1];
		ObjectMoveMatrix[11]= ObjectMoveMatrix[11]- n*ObjectRotationMatrix[2];
		
		// build the transform
		MatrixMultiply(ObjectMoveMatrix,ObjectRotationMatrix,ObjectTransform);
		MatrixMultiply(WorldTransform,ObjectTransform,CombinedTransform);
	}
	
	//---------------------------------------------------------------------------
	public void MoveUp(double n)
	{
		/* Take the 2nd row of the rotation matrix, multiply by a 
		 constant and add to the translation vector */
		
		ViewMoveMatrix[3] = ViewMoveMatrix[3] - n*ViewRotationMatrix[4];
		ViewMoveMatrix[7] = ViewMoveMatrix[7] - n*ViewRotationMatrix[5];
		ViewMoveMatrix[11]= ViewMoveMatrix[11]- n*ViewRotationMatrix[6];
		
		// build the transform
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
	}
	
	//---------------------------------------------------------------------------
	public void MoveUpObject(double n)
	{
		/* Take the 2nd row of the rotation matrix, multiply by a 
		 constant and add to the translation vector */
		
		ObjectMoveMatrix[3] = ObjectMoveMatrix[3] - n*ObjectRotationMatrix[4];
		ObjectMoveMatrix[7] = ObjectMoveMatrix[7] - n*ObjectRotationMatrix[5];
		ObjectMoveMatrix[11]= ObjectMoveMatrix[11]- n*ObjectRotationMatrix[6];
		
		// build the transform
		MatrixMultiply(ObjectMoveMatrix,ObjectRotationMatrix,ObjectTransform);
		MatrixMultiply(WorldTransform,ObjectTransform,CombinedTransform);
	}
	//---------------------------------------------------------------------------
	public void Roll(double theta)
	{
		/* Multiply the rotation matrix with a rotation around Out
		 to get a new rotation matrix. */

		double c,s;
		
		c = Math.cos(-theta);
		s = Math.sin(-theta);
		
		x[0] = c;    x[1] = -s;   x[2] = 0;   x[3] = 0;
		x[4] = s;    x[5] = c;    x[6] = 0;   x[7] = 0;
		x[8] = 0;    x[9] = 0;    x[10]= 1;   x[11]= 0;
		
		MatrixMultiply(ViewRotationMatrix,x);
		
		// build the transform
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
	}
	
	//---------------------------------------------------------------------------
	public void RollObject(double theta)
	{
		/* Multiply the rotation matrix with a rotation around Out
		 to get a new rotation matrix. */

		double c,s;
		
		c = Math.cos(-theta);
		s = Math.sin(-theta);
		
		x[0] = c;    x[1] = -s;   x[2] = 0;   x[3] = 0;
		x[4] = s;    x[5] = c;    x[6] = 0;   x[7] = 0;
		x[8] = 0;    x[9] = 0;    x[10]= 1;   x[11]= 0;
		
		MatrixMultiply(ObjectRotationMatrix,x);
		
		// build the transform
		MatrixMultiply(ObjectMoveMatrix,ObjectRotationMatrix,ObjectTransform);
		MatrixMultiply(WorldTransform,ObjectTransform,CombinedTransform);
	}
	
	//---------------------------------------------------------------------------
	public void RotateRight(double theta)
	{
		/* Multiply the rotation matrix with a rotation around Up
		 to get a new rotation matrix. */

		double c,s;
		
		c = Math.cos(-theta);
		s = Math.sin(-theta);
		
		x[0] = c;   x[1] = 0;   x[2] = s;   x[3] = 0;
		x[4] = 0;   x[5] = 1;   x[6] = 0;   x[7] = 0;
		x[8] = -s;  x[9] = 0;   x[10]= c;   x[11]= 0;
		
		MatrixMultiply(ViewRotationMatrix,x);
		
		// build the transform
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
	}
	
	//---------------------------------------------------------------------------
	public void RotateRightObject(double theta)
	{
		/* Multiply the rotation matrix with a rotation around Up
		 to get a new rotation matrix. */

		double c,s;
		
		c = Math.cos(-theta);
		s = Math.sin(-theta);
		
		x[0] = c;   x[1] = 0;   x[2] = s;   x[3] = 0;
		x[4] = 0;   x[5] = 1;   x[6] = 0;   x[7] = 0;
		x[8] = -s;  x[9] = 0;   x[10]= c;   x[11]= 0;
		
		MatrixMultiply(ObjectRotationMatrix,x);
		
		// build the transform
		MatrixMultiply(ObjectMoveMatrix,ObjectRotationMatrix,ObjectTransform);
		MatrixMultiply(WorldTransform,ObjectTransform,CombinedTransform);
	}
	
	//---------------------------------------------------------------------------
	public void RotateUp(double theta)
	{
		/* Multiply the rotation matrix with a rotation around Right
		 to get a new rotation matrix. */

		double c,s;
		
		c = Math.cos(theta);
		s = Math.sin(theta);
		
		x[0] = 1;   x[1] = 0;   x[2] = 0;   x[3] = 0;
		x[4] = 0;   x[5] = c;   x[6] = -s;  x[7] = 0;
		x[8] = 0;   x[9] = s;   x[10]= c;   x[11]= 0;
		
		MatrixMultiply(ViewRotationMatrix,x);
		
		// build the transform
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
	}
	
	//---------------------------------------------------------------------------
	public void RotateUpObject(double theta)
	{
		/* Multiply the rotation matrix with a rotation around Right
		 to get a new rotation matrix. */

		double c,s;
		
		c = Math.cos(theta);
		s = Math.sin(theta);
		
		x[0] = 1;   x[1] = 0;   x[2] = 0;   x[3] = 0;
		x[4] = 0;   x[5] = c;   x[6] = -s;  x[7] = 0;
		x[8] = 0;   x[9] = s;   x[10]= c;   x[11]= 0;
		
		MatrixMultiply(ObjectRotationMatrix,x);
		
		// build the transform
		MatrixMultiply(ObjectMoveMatrix,ObjectRotationMatrix,ObjectTransform);
		MatrixMultiply(WorldTransform,ObjectTransform,CombinedTransform);
	}
	//---------------------------------------------------------------------------
	public void POV(double Cx, double Cy, double Cz, double xAngle, double yAngle, double zAngle) {
		/* Set the Point of View (World Transform) using Euler Angles.
		 This is the simplest but least useful way to do a 3D rotation,
		 and is subject to gimbal lock. */
		
		// Fill translation matrix
		MoveFill(ViewMoveMatrix,-Cx,-Cy,-Cz);
		
		// Fill rotation matrix
		RotateFill(ViewRotationMatrix,xAngle,yAngle,zAngle);
		
		// build the transform
		MatrixMultiply(ViewRotationMatrix,ViewMoveMatrix,WorldTransform);
	}
	//---------------------------------------------------------------------------
	public void SetObject(double Ox, double Oy, double Oz, int xAngle, int yAngle, int zAngle) {
		/* The object transform is used to move and rotate objects. It
		 uses Euler angles. */
		
		// set item translation matrix
		MoveFill(ObjectMoveMatrix,Ox,Oy,Oz);
		
		// set item rotation matrix
		RotateFill(ObjectRotationMatrix,xAngle,yAngle,zAngle);
		
		// note order is OPPOSITE from world transform: move first!
		MatrixMultiply(ObjectMoveMatrix,ObjectRotationMatrix,ObjectTransform);
		
		// concatenate with the world transform
		MatrixMultiply(WorldTransform,ObjectTransform,CombinedTransform);
	}
	//---------------------------------------------------------------------------
	public void UpVector(double x, double y, double z)
	{
		/* Change the World Up vector (the default is (0,1,0))
		 For greater speed you can normalize and verify here 
		 instead of in _3DLookAt() */
		
		WorldUp[0] = x;
		WorldUp[1] = y;
		WorldUp[2] = z;
	}
	//---------------------------------------------------------------------------
	/* The following are the "helper" functions */
	//---------------------------------------------------------------------------
	public final void MatrixMultiply(double A[], double B[], double C[]/*out*/)
	{
		/* A matrix multiplication (dot product) of two 4x4 matrices.
		 Actually, we are only using matrices with 3 rows and 4 columns. */
		
		C[0] = A[0]*B[0] + A[1]*B[4] + A[2]*B[8];
		C[1] = A[0]*B[1] + A[1]*B[5] + A[2]*B[9];
		C[2] = A[0]*B[2] + A[1]*B[6] + A[2]*B[10];
		C[3] = A[0]*B[3] + A[1]*B[7] + A[2]*B[11] + A[3];
		
		C[4] = A[4]*B[0] + A[5]*B[4] + A[6]*B[8];
		C[5] = A[4]*B[1] + A[5]*B[5] + A[6]*B[9];
		C[6] = A[4]*B[2] + A[5]*B[6] + A[6]*B[10];
		C[7] = A[4]*B[3] + A[5]*B[7] + A[6]*B[11] + A[7];
		
		C[8] = A[8]*B[0] + A[9]*B[4] + A[10]*B[8];
		C[9] = A[8]*B[1] + A[9]*B[5] + A[10]*B[9];
		C[10] = A[8]*B[2] + A[9]*B[6] + A[10]*B[10];
		C[11] = A[8]*B[3] + A[9]*B[7] + A[10]*B[11] + A[11];
	}
	public final void MatrixMultiply(double A[]/*out*/, double B[])
	{
		/* A matrix multiplication (dot product) of two 4x4 matrices.
		 Actually, we are only using matrices with 3 rows and 4 columns. */
		double x=A[0], y=A[1], z=A[2];
		A[0] = x*B[0] + y*B[4] + z*B[8];
		A[1] = x*B[1] + y*B[5] + z*B[9];
		A[2] = x*B[2] + y*B[6] + z*B[10];
		A[3] += x*B[3] + y*B[7] + z*B[11];
		x=A[4]; y=A[5]; z=A[6];
		A[4] = x*B[0] + y*B[4] + z*B[8];
		A[5] = x*B[1] + y*B[5] + z*B[9];
		A[6] = x*B[2] + y*B[6] + z*B[10];
		A[7] += x*B[3] + y*B[7] + z*B[11];
		x=A[8]; y=A[9]; z=A[10];
		A[8] = x*B[0] + y*B[4] + z*B[8];
		A[9] = x*B[1] + y*B[5] + z*B[9];
		A[10] = x*B[2] + y*B[6] + z*B[10];
		A[11] += x*B[3] + y*B[7] + z*B[11];
	}
	//---------------------------------------------------------------------------
		double x[]=new double[12];
		double y[]=new double[12];
		double z[]=new double[12];
		double tempArray[]=new double[12];
	public final void RotateFill(double A[], double xAngle, double yAngle, double zAngle)
	{
		/* Fill the rotation matrix, using Euler angles */
		
		double cx,cy,cz;
		double sx,sy,sz;
		
		cx = Math.cos(xAngle);
		cy = Math.cos(yAngle);
		cz = Math.cos(zAngle);
		
		sx = Math.sin(xAngle);
		sy = Math.sin(yAngle);
		sz = Math.sin(zAngle);
		
		x[0]=1;     x[1]=0;     x[2] =0;     x[3] =0;
		x[4]=0;     x[5]=cx;    x[6] =-sx;   x[7] =0;
		x[8]=0;     x[9]=sx;    x[10]=cx;    x[11]=0;
		
		y[0]=cy;    y[1]=0;     y[2] =sy;    y[3] =0;
		y[4]=0;     y[5]=1;     y[6] =0;     y[7] =0;
		y[8]=-sy;   y[9]=0;     y[10]=cy;    y[11]=0;
		
		z[0]=cz;    z[1]=-sz;   z[2] =0;     z[3] =0;
		z[4]=sz;    z[5]=cz;    z[6] =0;     z[7] =0;
		z[8]=0;     z[9]=0;     z[10]=1;     z[11]=0;
		
		/* Note we are multiplying x*y*z. You can change the order,
		 but you will get different results. */
		
		MatrixMultiply(z,y,tempArray);   // multiply 2 matrices
		MatrixMultiply(tempArray,x,A);   // multiply result by 3rd matrix
	}
	//---------------------------------------------------------------------------
	public final void MoveFill(double A[], double Cx, double Cy, double Cz)
	{
		/* Fill the translation matrix */
		
		A[0] = 1;   A[1] = 0;   A[2] = 0;   A[3] = Cx;
		A[4] = 0;   A[5] = 1;   A[6] = 0;   A[7] = Cy;
		A[8] = 0;   A[9] = 0;   A[10]= 1;   A[11]= Cz;
	}
}
