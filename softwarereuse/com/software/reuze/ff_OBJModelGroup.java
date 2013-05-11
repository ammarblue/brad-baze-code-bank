package com.software.reuze;



import java.util.ArrayList;
import java.util.List;


/**
 * A single group within the model
 * 
 * @author Kevin Glass
 * @author Marvin Froehlich (aka Qudus)
 */
public class ff_OBJModelGroup
{
    private String name;
    private boolean isTopGroup;
    private List<ff_OBJModelGroup> children;
    public d_ArrayListFloat vtn;     //x y z [ u v ]  [ nx ny nz ]
    public d_ArrayListInt vtnIndices; 
    public d_ArrayListInt faces;     //vertex index, vertex count, material index
    public int textureStart = -1;
    public int normalStart = -1;
    
    public void setName( String name )
    {
        this.name = name;
    }
    
    public String getName()
    {
        return ( name );
    }
    
    public boolean hasTextures()
    {
        return textureStart!=-1;
    }
    public boolean hasNormals()
    {
        return normalStart!=-1;
    }
    public boolean add(String name, vc_Material m) {
    	return false;
    }
    public boolean isTopGroup()
    {
        return ( isTopGroup );
    }
    
    public void addChild( ff_OBJModelGroup group )
    {
        if (children==null)  children = new ArrayList<ff_OBJModelGroup>();
        children.add( group );
    }
    
    public List<ff_OBJModelGroup> getChildren()
    {
        return ( children );
    }
    
    private ff_OBJModelGroup( String name )
    {        
        this.name = name;
        this.isTopGroup = false;
        vtn=new d_ArrayListFloat();
        vtnIndices=new d_ArrayListInt();
        faces=new d_ArrayListInt();
    }
    
    public ff_OBJModelGroup( )
    {
        this( "top" );
        this.isTopGroup = true;
    }

	public void addVertex(float... vert) {
		vtn.add(vert);
	}

	public void addTexture(float... texCoord) {
		int i=vtn.add(texCoord);
		if (textureStart<0) textureStart=i;
	}

	public void addNormal(float... normal) {
		int i=vtn.add(normal);
		if (normalStart<0) normalStart=i;
	}

}
