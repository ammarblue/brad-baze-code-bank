package com.software.reuze;

public class gb_TerrainCamera 
	{
	  gb_Vector3 position;
	  gb_Vector3 lower_left_corner;
	  gb_Vector3 uvw_u;
	  gb_Vector3 uvw_v;
	  gb_Vector3 uvw_w;

	  public gb_TerrainCamera(gb_Vector3 eye, gb_Vector3 up, gb_Vector3 gaze,
			  float resX, float resY, float d)
	  {
	    position = eye;
	    uvw_v = up;
	    uvw_w = gaze;
	    uvw_u = uvw_v.cpy().crs(uvw_w);
	    uvw_u.nor();
	    
	    uvw_v.set(uvw_w.tmp().crs(uvw_u ));
	    uvw_v.nor();
	    
	    uvw_w.nor();
	    
	    float sX = -resX/2;
	    float sY = -resY/2;
	    //float eX = resX/2;
	    //float eY = resY/2;
	    
	    lower_left_corner = new gb_Vector3();
	    gb_Vector3 t1= new gb_Vector3();
	    gb_Vector3 t2= new gb_Vector3();
	    t1.set( uvw_w);
	    t1.mul(d);
	    t2.set(uvw_v);
	    t2.mul(sY);
	    lower_left_corner.set( uvw_u);
	    lower_left_corner.mul(sX);
	    lower_left_corner.add(t1);
	    lower_left_corner.add(t2);
	        
	    //uvw_u.mult(resX);
	    //uvw_v.mult( resY);
	  }

	  public gb_Ray generateRay( float screenCoordX, float screenCoordY )
	  {
	    gb_Vector3 origin =new gb_Vector3();
	    gb_Vector3 up =new gb_Vector3();
	    gb_Vector3 acc =new gb_Vector3();
	    gb_Vector3 direction = new gb_Vector3();
	    direction.set( lower_left_corner );
	    up.set( uvw_v);
	    up.mul( screenCoordY);
	    acc.set(uvw_u);
	    acc.mul( screenCoordX);
	    direction.add(up);
	    direction.add(acc);
	    direction.nor();
	    origin.set(position);
	    return new gb_Ray( origin, direction);
	  }
	}