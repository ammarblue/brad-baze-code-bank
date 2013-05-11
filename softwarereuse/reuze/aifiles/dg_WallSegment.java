package reuze.aifiles;

import com.software.reuze.gb_Vector3;

public class dg_WallSegment {
	public dg_WallSegment(){}
	public dg_WallSegment(gb_Vector3 a, gb_Vector3 b){m_point1 = a; m_point2 = b;}
	void Set(gb_Vector3 a, gb_Vector3 b){m_point1 = a; m_point2 = b;}
	gb_Vector3 m_point1;
	gb_Vector3 m_point2;
}
