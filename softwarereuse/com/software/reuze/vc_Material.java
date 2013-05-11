package com.software.reuze;
import java.io.Serializable;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2007-2011, JAGaToo Project Group all rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of the 'Xith3D Project Group' nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
 * RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE
 */
/**
 * Copyright (c) 2003-2007, Xith3D Project Group all rights reserved.
 * 
 * Portions based on the Java3D interface, Copyright by Sun Microsystems.
 * Many thanks to the developers of Java3D and Sun Microsystems for their
 * innovation and design.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of the 'Xith3D Project Group' nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
 * RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE
 */


/**
 * Abstractly stores an OBJ loaded material.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class vc_Material implements Comparable<vc_Material>, Serializable
{
    private String   name = null;
    private static int counter=0;
    public static final char INPUT_DELIMITER='[';
    private float[]  color;
    private float[]  ambientColor;
    private float[]  diffuseColor; //only diffuse and material have alpha
    private float[]  specularColor;
    private float    shininess;
    private int index;
    
    private String   textureFileName = null;
    
    /**
     * @return this Material's name
     */
    public String getName()
    {
        return ( name );
    }
    public void set(vc_Material m) {
    	name=m.name;
    	textureFileName=m.textureFileName;
    	shininess=m.shininess;
    	setColor(m.color);
    	setDiffuseColor(m.diffuseColor);
    	setAmbientColor(m.ambientColor);
    	setSpecularColor(m.specularColor);
    }
    /**
     * Sets this material's color.
     * 
     * @param color
     */
    public void setColor( float... color )
    {
        if ( color == null || color.length<3) {
            this.color = null;
            return;
        }
        float transparency=(color.length>=4)?color[3]:1;
        if (this.color != null) {
            this.color[ 0 ] = color[ 0 ];
            this.color[ 1 ] = color[ 1 ];
            this.color[ 2 ] = color[ 2 ];
            this.color[ 3 ] = transparency;
        }
        else {
            this.color = new float[] { color[ 0 ], color[ 1 ], color[ 2 ], transparency };
        }
    }
    
    /**
     * @return this material's color.
     */
    public float[] getColor()
    {
        return ( color );
    }
    
    /**
     * Sets this material's ambient color.
     * 
     * @param color
     */
    public void setAmbientColor( float... color )
    {
        if ( color == null || color.length<3 )
        {
            this.ambientColor = null;
            return;
        }
        if (this.ambientColor != null)
        {
            this.ambientColor[ 0 ] = color[ 0 ];
            this.ambientColor[ 1 ] = color[ 1 ];
            this.ambientColor[ 2 ] = color[ 2 ];
        }
        else
        {
            this.ambientColor = new float[] { color[ 0 ], color[ 1 ], color[ 2 ] };
        }
    }
    
    /**
     * @return this material's color.
     */
    public float[] getAmbientColor()
    {
        return ( ambientColor );
    }
    
    /**
     * Sets this material's diffuse color.
     * 
     * @param color
     */
    public void setDiffuseColor( float... color )
    {
        if ( color == null || color.length<3)
        {
            this.diffuseColor = null;
            return;
        }
        float transparency=(color.length>=4)?color[3]:1;
        if (this.diffuseColor != null)
        {
            this.diffuseColor[ 0 ] = color[ 0 ];
            this.diffuseColor[ 1 ] = color[ 1 ];
            this.diffuseColor[ 2 ] = color[ 2 ];
            this.diffuseColor[ 3 ] = transparency;
        }
        else
        {
            this.diffuseColor = new float[] { color[ 0 ], color[ 1 ], color[ 2 ], transparency };
        }
    }
    
    /**
     * @return this material's diffuse color.
     */
    public float[] getDiffuseColor()
    {
        return ( diffuseColor );
    }
    
    /**
     * Sets this material's specular color, () sets to null.
     * 
     * @param color
     */
    public void setSpecularColor( float... color )
    {
        if ( color == null  || color.length<3)
        {
            this.specularColor = null;
            return;
        }
        if (this.specularColor != null)
        {
            this.specularColor[ 0 ] = color[ 0 ];
            this.specularColor[ 1 ] = color[ 1 ];
            this.specularColor[ 2 ] = color[ 2 ];
        }
        else
        {
            this.specularColor = new float[] { color[ 0 ], color[ 1 ], color[ 2 ] };
        }
    }
    
    /**
     * @return this material's specular color.
     */
    public float[] getSpecularColor()
    {
        return ( specularColor );
    }
    
    /**
     * Sets this Material's shininess.
     * 
     * @param shininess
     */
    public void setShininess( float shininess )
    {
        this.shininess = shininess;
    }
    
    /**
     * @return this Material's shininess.
     */
    public float getShininess()
    {
        return ( shininess );
    }
    
    /**
     * Sets this Material's texture name.
     * 
     * @param textureName
     */
    public void setTextureFileName( String name )
    {
    	assert name.charAt(INPUT_DELIMITER)<0;
    	this.textureFileName = name;
    }
    
    /**
     * @return this Material's texture name.
     */
    public String getTextureFileName()
    {
        return ( textureFileName );
    }
    
    public vc_Material( String name, float... color )
    {
        if (name!=null) assert name.charAt(INPUT_DELIMITER)<0;
        this.name = name;
        this.color=null;
        diffuseColor=null;
        if (color!=null) {
        	setColor(color);
        	setDiffuseColor(color);
        }
        ambientColor=null;
        specularColor=null;
        shininess=64;
        index=++counter;
    }
    public vc_Material(vc_Material m) {
    	this.set(m);
    	index=++counter;
    }
    public vc_Material() {index=++counter;}
    public int getIndex() {return index;}
    public void setName( String name )
    {
        assert name.charAt(INPUT_DELIMITER)<0;
        this.name = name;
    }
	public int compareTo(vc_Material arg0) {
		if (arg0==null) return 1;
		if (name!=null) return name.compareTo(arg0.name);
		if (color==null) return -1;
		if (arg0.color==null) return 1;
		for (int i=0; i<4; i++)
			if (Math.abs(arg0.color[i]-color[i])>1.0e-6)
				return (arg0.color[i]<color[i])?1:-1;
		return 0;
	}
	public boolean equals(Object o) {
		if (o==null) return false;
		return name.compareTo(((vc_Material)o).name)==0;
	}
	public int hashCode() {
	    if (name==null) return 0;
	    return name.hashCode();
	  }
	private static StringBuilder sb=new StringBuilder(100);
	public String toString() {
		sb.delete(0,1000);
		sb.append('[');
		sb.append(name); sb.append(',');
		sb.append(textureFileName); sb.append(',');
		if (color==null) {
			sb.append("null,");
		} else
		for (float f:color) {
			sb.append(f); sb.append(',');
		}
		if (diffuseColor==null) {
			sb.append("null,");
		} else
		for (float f:diffuseColor) {
			sb.append(f); sb.append(',');
		}
		if (ambientColor==null) {
			sb.append("null,");
		} else
		for (float f:ambientColor) {
			sb.append(f); sb.append(',');
		}
		if (specularColor==null) {
			sb.append("null,");
		} else
		for (float f:specularColor) {
			sb.append(f); sb.append(',');
		}
		sb.append(shininess);
		sb.append(']');
		return sb.toString();
	}
	private static float tmp[]=new float[4];
	public static vc_Material constant(String material) {
		String[] values=material.split("[,\\[\\]]");
		vc_Material m=new vc_Material();
		String s;
		int i=0, j=0,k=1;
		if (values[0].length()>0) k=0; //accept string without enclosing []
		while (++i<values.length) {
			s=(values[k].compareTo("null")==0)?null:values[k++];
			switch(i) {
			case 1: m.setName(s); break;
			case 2: m.textureFileName=s; break;
			case 3: case 4: case 5: case 6:
				if (s==null) {m.setColor(null); i=6; break;}
			case 7: case 8: case 9: case 10:
				if (s==null) {m.setDiffuseColor(null); i=10; break;}
			case 11: case 12: case 13: case 14:
				if (s==null) {m.setAmbientColor(null); i=14; break;}
			case 15: case 16: case 17: case 18:
				if (s==null) {m.setSpecularColor(null); i=18; break;}
			case 19:
				tmp[j++]=Float.parseFloat(s);
				if (i==6) {m.setColor(tmp); j=0;}
				if (i==10) {m.setDiffuseColor(tmp); j=0;}
				if (i==14) {m.setAmbientColor(tmp); j=0;}
				if (i==18) {m.setSpecularColor(tmp); j=0;}
				if (i==19) {m.setShininess(tmp[0]); return m;}
				break;
			}
		}
		return m;
	}
	public vc_Material parse(Scanner s) {
		Pattern p=s.delimiter();
		s.useDelimiter("[\\[\\]]");
		String in=s.next();
		this.set(constant(in));
		s.useDelimiter(p);
		return this;
	}
	public static void main(String args[]) {
		vc_Material m=new vc_Material("mat name",0.7f,0.8f,0.9f);
		m.setTextureFileName("file");
		m.setDiffuseColor(0.1f,0.2f,0.3f,0.4f); //diffuse rgba
		System.out.println(m);
		m=constant("[mat name,file,0.7,0.8,0.9,1.0,0.1,0.2,0.3,0.4,null,null,64.0]");
		System.out.println(m);
		vc_Material x=new vc_Material();
		x.parse(new Scanner("[mat name,file,0.7,0.8,0.9,1.0,0.1,0.2,0.3,0.4,null,null,64.0]"));
		System.out.println(x);
	}
}
