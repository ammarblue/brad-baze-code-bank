package reuze.aifiles;

import java.util.Arrays;

public class dg_InfluenceMapControl extends dg_InfluenceMap {
	public dg_InfluenceMapControl() {
		super(IM_CONTROL);
	}
	void Update(float dt)
	{
	    //bail out if nobody to update
	    if(m_registeredObjects.size() == 0)
	        return;
	    
	    //clear out map
	    Arrays.fill(m_map,0);
	    
	    //stamp obj locations
	    for(RegObject listObj:m_registeredObjects)
	    {
	        //only care about "control" objects, not miscellaneous
	        if(listObj.m_objType == OT_MISC) continue;
	        if(listObj.m_objType == OT_FRIENDLY)
	            StampInfluenceGradient(listObj.m_pObject.m_position, 16);
	        else if(listObj.m_objType == OT_BULLET)
	            StampInfluenceGradient(listObj.m_pObject.m_position, 8);
	        else //ENEMY
	            StampInfluenceGradient(listObj.m_pObject.m_position, (int)-((listObj.m_pObject.m_size)/3));
	        listObj.m_lastPosition.set(listObj.m_pObject.m_position);
	    }
	}

	//---------------------------------------------------------
	void RegisterGameObj(dg_GameObject object)
	{
	    int sizeX,sizeY;
	    sizeX = sizeY = 1;
	    
	    RegObject temp;
	    temp = new RegObject();
	    temp.m_pObject      = object;
	    temp.m_objSizeX     = sizeX;
	    temp.m_objSizeY     = sizeY;
	    temp.m_lastPosition = object.m_position;
	    temp.m_stamped      = false;
	    temp.m_objType = z_app.game.getInfluenceType(object);
	    m_registeredObjects.add(temp);
	}

	//---------------------------------------------------------
	void DrawTheInfluence()
	{
	    super.DrawTheInfluence();
	    /*glPushMatrix();
	    glDisable(GL_LIGHTING);
	    glTranslatef(0,0,0);
	    glEnable(GL_BLEND);                                 
	    glBlendFunc(GL_ONE, GL_ONE);                    
	    for(int i=0;i<m_numCels;i++)
	    {
	        if(m_map[i])
	        {
	            int y = i / m_dataSizeY;
	            int x = i - y*m_dataSizeY;
	            float color = m_map[i]/16.0f;
	            if(color > 0)
	                glColor3f(0,0,color);
	            else
	                glColor3f(-color,0,0);
	            glBegin(GL_POLYGON);
	            glVertex3f(x*m_celResX,          y*m_celResY,          0);
	            glVertex3f(x*m_celResX,          y*m_celResY+m_celResY,0);
	            glVertex3f(x*m_celResX+m_celResX,y*m_celResY+m_celResY,0);
	            glVertex3f(x*m_celResX+m_celResX,y*m_celResY,          0);
	            glEnd();
	        }
	    }
	    glDisable(GL_BLEND);    
	    glEnable(GL_LIGHTING);
	    glPopMatrix();*/
	}
}
