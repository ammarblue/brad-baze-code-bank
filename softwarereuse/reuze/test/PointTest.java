/*  Course:  CSCI-7130-01F
 *  Student:  Steven Benson
 *  Assignment:  P1
 *  Date:  8/25/2012
 *  
 *  Description:  Create a unit test class for a class that does not currently have one.
 */

package reuze.test;

import org.junit.Test;

import com.software.reuze.ga_Point;

import junit.framework.TestCase;

public class PointTest extends TestCase {

		public void testParameterizedConstructor() {
			ga_Point localPoint = new ga_Point(99.2121d,46.6532d);
			
			assertEquals(localPoint.x,99.2121d);
			assertEquals(localPoint.y,46.6532d);	
		}

		//may not be necessary, but I am including this test anyway for complete coverage
		public void testGetters() {
			ga_Point localPoint = new ga_Point(87.521d,78.254d);
			
			assertEquals(localPoint.getX(),87.521d);
			assertEquals(localPoint.getY(),78.254d);			
		}
		
		public void testSet() {
			ga_Point localPoint = new ga_Point();
			
			double xCoordinateAsDouble = -1.54f; //will actually be stored as -1.5399999618530273
			double yCoordinateAsDouble = 3.7f;  //will actually be stored as 3.700000047683716
			
			localPoint.set(-1.54f, 3.7f);
			
			assertEquals(localPoint.x, xCoordinateAsDouble);
			assertEquals(localPoint.y, yCoordinateAsDouble);
		}
		
		private double calculateEuclidianDistance(double startX, double startY, double endX, double endY)
		{		
			double result = (endX - startX) * (endX - startX);
			result += ((endY - startY) * (endY - startY));
			
			return Math.sqrt(result);
		}
		
		
		public void testDistance() {
			ga_Point startPoint = new ga_Point(2.5f,7.8f);
			ga_Point endPoint = new ga_Point(-14.2f, -32.7f);
			
			double distanceFromMethod = calculateEuclidianDistance(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
			double distanceFromClass = startPoint.distance(endPoint);
					
			assertEquals(distanceFromMethod, distanceFromClass);
		}
}