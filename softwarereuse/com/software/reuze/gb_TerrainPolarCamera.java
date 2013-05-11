package com.software.reuze;

public class gb_TerrainPolarCamera
	{
	  public gb_Vector3  m_camPos = new gb_Vector3(0.0f,2.4f,0.0f);
	  public float   m_CamAngleX = 0.f;
	  public float   m_CamAngleY = -0.3f;
	  public gb_Vector3  m_camDir=new gb_Vector3();
	  
	  public gb_TerrainPolarCamera(gb_Vector3 cpos, float angX, float angY )
	  {
	    m_camPos = cpos;
	    m_CamAngleX = angX;
	    m_CamAngleY = angY;
	    update(0,0);
	  }
	  public gb_TerrainPolarCamera()
	  {
	    update(0,0);
	  }
	  public void update(float dy, float dz)
	  {
	     m_CamAngleX += dy;
	     m_CamAngleY += -dz;
	     float cy=(float)Math.cos(m_CamAngleY);
	     m_camDir.set((float)Math.sin(m_CamAngleX)*cy,
	               (float)Math.sin(m_CamAngleY),(float)Math.cos(m_CamAngleX)*cy);
	  }
	  public void forward(float v )
	  {
	    m_camPos.add(m_camDir.tmp().mul(v*0.25f));
	  }
	  public void side(float v )
	  {
	    gb_Vector3 f = m_camDir.tmp().crs(new gb_Vector3(0,1,0));
	    f.nor();
	    f.mul(v*0.25f);
	    m_camPos.add(f);
	  }
	}