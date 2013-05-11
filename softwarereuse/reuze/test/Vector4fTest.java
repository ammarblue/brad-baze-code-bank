package reuze.test;

import com.software.reuze.gc_Vector4f;

import junit.framework.TestCase;

public class Vector4fTest extends TestCase {

	float[] vectorFloat = {1, 2, 3, 4};
	
	public void testVecInit() {
		// Test initialization of vector object from
		// a normal array
		gc_Vector4f vectorObj = new gc_Vector4f(vectorFloat);

		assertEquals(vectorObj.x, vectorFloat[0]);
	}
	
	public void testVecNegAbs() {
		// Test the negation and absolute-value operations
		float[] vectorFloatNeg = {-1, -2, -3, -4};
		gc_Vector4f vectorObj1 = new gc_Vector4f(vectorFloat);
		gc_Vector4f vectorObj2 = new gc_Vector4f(vectorFloat);

		vectorObj1.negate();
		assertEquals(vectorObj1.x, vectorFloatNeg[0]);

		vectorObj1.absolute();
		assertEquals(vectorObj1.x, vectorObj2.x);
	}

	public void testVecScale() {
		// Test the scaling operator
		float[] vectorFloatScale = {2, 4, 6, 8};
		gc_Vector4f vectorObj = new gc_Vector4f(vectorFloat);

		vectorObj.scale(2);
		assertEquals(vectorObj.x, vectorFloatScale[0]);
	}

	public void testVecAddSub() {
		// Test the addition and subtraction operators
		float[] vectorFloatAdd = {2, 3, 4, 5};
		float[] vectorFloatDiff = {1, 1, 1, 1};
		gc_Vector4f vectorObj = new gc_Vector4f(vectorFloat);
		gc_Vector4f vectorDiff = new gc_Vector4f(vectorFloatDiff);

		vectorObj.add(vectorDiff);
		assertEquals(vectorObj.x, vectorFloatAdd[0]);

		vectorObj.sub(vectorDiff);
		assertEquals(vectorObj.x, vectorFloat[0]);
	}
	
	public void testVecSet() {
		// Test the set method
		gc_Vector4f vectorObj = new gc_Vector4f(vectorFloat);
		vectorObj.set(vectorFloat);
		assertEquals(vectorObj.w, vectorFloat[3]);
	}
	
}