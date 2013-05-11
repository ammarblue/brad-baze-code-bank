package com.software.reuze;
import com.software.reuze.ga_Rectangle;
import com.software.reuze.ga_Vector2;
public class ga_RectangleRotated extends ga_Rectangle {
	  public float Rotation;
	  public ga_Vector2 Origin;  //relative to origin of rectangle

	  public ga_RectangleRotated() {
	    super();
	    Rotation=0;
	    Origin=new ga_Vector2(0, 0);
	  }
	  public ga_RectangleRotated(ga_Rectangle theRectangle, float theInitialRotation, ga_Vector2 origin) {
	    super(theRectangle);
	    Rotation = theInitialRotation;
	    //Calculate the Rectangle's origin. We assume the center of the Rectangle will
	    //be the point that we will be rotating around and we use that for the origin
	    Origin = origin;
	  }
	  public static ga_Vector2 aLowerRight=new ga_Vector2();
	  public static ga_Vector2 aLowerLeft=new ga_Vector2();
	  public static ga_Vector2 aUpperRight=new ga_Vector2();
	  public static ga_Vector2 aUpperLeft=new ga_Vector2();
	  private static double cos,sin;
	  private static ga_Vector2 aAxis = new ga_Vector2();
	  /// <summary>
	  /// Must be called prior to accessing corner methods
	  /// </summary>
	  public void calcVertices() {
		  cos=Math.cos(Rotation); sin=Math.sin(Rotation);
		  LowerRightCorner();
		  LowerLeftCorner();
		  UpperRightCorner();
		  UpperLeftCorner();
	  }
	  private void LowerRightCorner() {
	    aLowerRight.x=position.x+width; aLowerRight.y=position.y+height;
	    RotatePoint(aLowerRight, Origin.x, Origin.y, Rotation);
	  }
	  private void LowerLeftCorner() {
	    aLowerLeft.x=position.x; aLowerLeft.y=position.y+height;
	    RotatePoint(aLowerLeft, Origin.x, Origin.y, Rotation);
	  }
	  private void UpperRightCorner() {
	    aUpperRight.x=position.x+width; aUpperRight.y=position.y;
	    RotatePoint(aUpperRight, Origin.x, Origin.y, Rotation);
	  }
	  private void UpperLeftCorner() {
	    aUpperLeft.x = position.x; aUpperLeft.y=position.y;
	    RotatePoint(aUpperLeft, Origin.x, Origin.y, Rotation);
	  }

	  /// <summary>
	  /// Rotate a point from a given location and adjust using the Origin we are rotating around
	  /// </summary>
	  /// <param name="thePoint"></param>
	  /// <param name="theOrigin"></param>
	  /// <param name="theRotation"></param>
	  private void RotatePoint(ga_Vector2 thePoint/*inout*/, float theOriginX, float theOriginY, float theRotation) {
	    float x = (float)(theOriginX + (thePoint.x - theOriginX) * cos
	      - (thePoint.y - theOriginY) * sin);
	    thePoint.y = (float)(theOriginY + (thePoint.y - theOriginY) * cos
	      + (thePoint.x - theOriginX) * sin);
	    thePoint.x=x;
	  }

	  /// <summary>
	  /// Generates a scalar value that can be used to compare where corners of 
	  /// a rectangle have been projected onto a particular axis. 
	  /// </summary>
	  /// <param name="theRectangleCorner"></param>
	  /// <param name="theAxis"></param>
	  /// <returns></returns>
	  public int GenerateScalar(ga_Vector2 theRectangleCorner, ga_Vector2 theAxis) {
	    //Using the formula for Vector projection. Take the corner being passed in
	    //and project it onto the given Axis
	    float aNumerator = (theRectangleCorner.x * theAxis.x) + (theRectangleCorner.y * theAxis.y);
	    float aDenominator = (theAxis.x * theAxis.x) + (theAxis.y * theAxis.y);
	    float aDivisionResult = aNumerator / aDenominator;
	    //Now that we have our projected Vector, calculate a scalar of that projection
	    //that can be used to more easily do comparisons
	    float aScalar = (theAxis.x * aDivisionResult * theAxis.x) + (theAxis.y * aDivisionResult * theAxis.y);
	    return (int)aScalar;
	  }

	  /// <summary>
	  /// Determines if a collision has occurred on an Axis of one of the
	  /// planes parallel to the Rectangle. must call calcVertices on both arguments.
	  /// </summary>
	  /// <param name="theRectangle"></param>
	  /// <param name="aAxis"></param>
	  /// <returns></returns>
	  public boolean IsAxisCollision(ga_Vector2[] UL_UR_LL_LR, ga_Vector2 aAxis) {
	    //Project the corners of the Rectangle we are checking onto the Axis and
	    //get a scalar value of that projection we can then use for comparison
	    int amin=999999, amax=-999999, t;
	    t=GenerateScalar(UL_UR_LL_LR[0], aAxis);
	    if (t<amin) amin=t;  
	    if (t>amax) amax=t;
	    t=GenerateScalar(UL_UR_LL_LR[1], aAxis);
	    if (t<amin) amin=t;  
	    if (t>amax) amax=t;
	    t=GenerateScalar(UL_UR_LL_LR[2], aAxis);
	    if (t<amin) amin=t;  
	    if (t>amax) amax=t;
	    t=GenerateScalar(UL_UR_LL_LR[3], aAxis);
	    if (t<amin) amin=t;  
	    if (t>amax) amax=t;
	    //Project the corners of the current Rectangle onto the Axis and
	    //get a scalar value of that projection we can then use for comparison
	    int bmin=999999, bmax=-999999;
	    t=GenerateScalar(aUpperLeft, aAxis);
	    if (t<bmin) bmin=t;  
	    if (t>bmax) bmax=t;
	    t=GenerateScalar(aUpperRight, aAxis);
	    if (t<bmin) bmin=t;  
	    if (t>bmax) bmax=t;
	    t=GenerateScalar(aLowerLeft, aAxis);
	    if (t<bmin) bmin=t;  
	    if (t>bmax) bmax=t;
	    t=GenerateScalar(aLowerRight, aAxis);
	    if (t<bmin) bmin=t;  
	    if (t>bmax) bmax=t;
	    //If we have overlaps between the Rectangles (i.e. Min of B is less than Max of A)
	    //then we are detecting a collision between the rectangles on this Axis
	    if (bmin <= amax && bmax >= amax)
	    {
	      return true;
	    }
	    else if (amin <= bmax && amax >= bmax)
	    {
	      return true;
	    }
	    return false;
	  }

	  /// <summary>
	  /// The intersects method can be used to check a standard Rectangle
	  /// object and see if it collides with a Rotated Rectangle object
	  /// checks only if standard rect contains rotated but not if rotated contains rect
	  /// </summary>
	  /// <param name="theRectangle"></param>
	  /// <returns></returns>
	  public boolean Intersects(ga_Rectangle theRectangle)
	  {
	    //return Intersects(new RotatedRectangle(theRectangle, 0.0f));
		calcVertices();
		ga_Vector2 tl=aUpperLeft, tr=aUpperRight;
		if (ga_Geometry2D.line_box_xywh(tl.x, tl.y, tr.x, tr.y, theRectangle.position.x, theRectangle.position.y, theRectangle.width, theRectangle.height)) return true;
		ga_Vector2 bl=aLowerLeft;
		if (ga_Geometry2D.line_box_xywh(tl.x, tl.y, bl.x, bl.y, theRectangle.position.x, theRectangle.position.y, theRectangle.width, theRectangle.height)) return true;
		ga_Vector2 br=aLowerRight;
		if (ga_Geometry2D.line_box_xywh(br.x, br.y, bl.x, bl.y, theRectangle.position.x, theRectangle.position.y, theRectangle.width, theRectangle.height)) return true;
		if (ga_Geometry2D.line_box_xywh(br.x, br.y, tr.x, tr.y, theRectangle.position.x, theRectangle.position.y, theRectangle.width, theRectangle.height)) return true;
		return false;
	  }

	  /// <summary>
	  /// Check to see if two Rotated Rectangles have collided or one contains the other
	  /// </summary>
	  /// <param name="theRectangle"></param>
	  /// <returns></returns>
	  public boolean Intersects(ga_RectangleRotated theRectangle) {
		  theRectangle.calcVertices();
		  ga_Vector2 a[]=new ga_Vector2[]{aUpperLeft.copy(), aUpperRight.copy(), aLowerLeft.copy(), aLowerRight.copy()};
		  calcVertices();
	    //Calculate the Axis we will use to determine if a collision has occurred
	    //Since the objects are rectangles, we only have to generate 4 Axes (2 for
	    //each rectangle) since we know the other 2 on a rectangle are parallel.
	    aAxis.set(aUpperRight).sub(aUpperLeft);
	    if (!IsAxisCollision(a, aAxis)) return false;
	    aAxis.set(aUpperRight).sub(aLowerRight);
	    if (!IsAxisCollision(a, aAxis)) return false;
	    aAxis.set(a[0]).sub(a[2]);  //UL-LL
	    if (!IsAxisCollision(a, aAxis)) return false;
	    aAxis.set(a[0]).sub(a[1]);  //UL-UR
	    if (!IsAxisCollision(a, aAxis)) return false;
	    //Cycle through all of the Axes we need to check. If a collision does not occur
	    //on ALL of the Axes, then a collision is NOT occurring. We can then exit 
	    //immediately and notify the calling function that no collision was detected. If
	    //a collision DOES occur on ALL of the Axes, then there is a collision
	    //between the rotated rectangles. We know this to be true by the Separating Axis Theorem.
	    return true;
	  }
	} //class