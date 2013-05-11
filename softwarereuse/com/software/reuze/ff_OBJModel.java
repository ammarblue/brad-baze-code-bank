package com.software.reuze;



import java.util.HashMap;

/**
 * A prototype for OBJ model data.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class ff_OBJModel
{
    private HashMap<String, vc_Material> matMap;
    
    private ff_OBJModelGroup topGroup;
    
    public HashMap<String, vc_Material> getMaterialMap()
    {
        return ( matMap );
    }
    
    public ff_OBJModelGroup getTopGroup()
    {
        return ( topGroup );
    }
    
    public ff_OBJModel( HashMap<String, vc_Material> matMap, ff_OBJModelGroup topGroup )
    {
        this.matMap = matMap;
        this.topGroup = topGroup;
    }
}
