package reuze.aifiles;

import java.util.Arrays;

import reuze.aifiles.dg_InfluenceMap.RegObject;

public class dg_InfluenceMapOccupance extends dg_InfluenceMap {
	public dg_InfluenceMapOccupance() {
		super(IM_OCCUPANCE);
	}
	//---------------------------------------------------------
	void Update(float dt)
	{
		//bail out if nobody to update
	    if(m_registeredObjects.size() == 0)
	        return;
	    
	    //stamp new locations
	    for(RegObject listObj:m_registeredObjects)
	    {
	    	if(listObj.m_stamped)
	            StampInfluenceShape(listObj.m_lastPosition,listObj.m_objSizeX,listObj.m_objSizeY, -1);
	    	StampInfluenceShape(listObj.m_pObject.m_position,listObj.m_objSizeX,listObj.m_objSizeY, 1);
	        listObj.m_stamped = true;
	        listObj.m_lastPosition.set(listObj.m_pObject.m_position);
	    }
	}

	//---------------------------------------------------------
	void RemoveGameObj(dg_GameObject object)
	{
	    if(m_registeredObjects.size() == 0)
	        return;
	    
	    for(RegObject listObj:m_registeredObjects)
	    {
	        if(listObj.m_pObject == object)
	        {
	            if(listObj.m_stamped)
	            	StampInfluenceShape(listObj.m_lastPosition,listObj.m_objSizeX,listObj.m_objSizeY, -1);
	            m_registeredObjects.remove(listObj);
	            return;
	        }
	    }
	    
	}

	//---------------------------------------------------------
	void RegisterGameObj(dg_GameObject object)
	{
	    int sizeX,sizeY;
	    if(object.m_size <4)  //TODO why not use dataSize directly, this doesn't seem accurate
	    {
	        sizeX = m_dataSizeX/16;
	        sizeY = m_dataSizeY/16;
	    }
	    else if(object.m_size<11)
	    {
	        sizeX = m_dataSizeX/10;
	        sizeY = m_dataSizeY/10;
	    }
	    else if(object.m_size<33)
	    {
	        sizeX = m_dataSizeX/8;
	        sizeY = m_dataSizeY/8;
	    }
	    else if(object.m_size <49)
	    {
	        sizeX = m_dataSizeX/5;
	        sizeY = m_dataSizeX/5;
	    }
	    else if(object.m_size <65)
	    {
	        sizeX = m_dataSizeX/4;
	        sizeY = m_dataSizeX/4;
	    }
	    else
	    {
	        sizeX = m_dataSizeX/3;
	        sizeY = m_dataSizeX/3;
	    }
	    
	    //set minimum size of 1 in each direction
	    sizeX = Math.max(1,sizeX);
	    sizeY = Math.max(1,sizeY);
	    
	    RegObject temp;
        temp = new RegObject();
        temp.m_pObject      = object;
        temp.m_objSizeX     = sizeX;
        temp.m_objSizeY     = sizeY;
        temp.m_lastPosition = object.m_position.cpy();
        temp.m_stamped      = false;
        m_registeredObjects.add(temp);
	}

}
