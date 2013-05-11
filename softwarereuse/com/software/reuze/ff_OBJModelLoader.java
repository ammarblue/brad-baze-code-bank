package com.software.reuze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * A loader to create abstract java data from a Wavefront OBJ file.
 * 
 * @author Kevin Glass
 * @author <a href="http://www.CroftSoft.com/">David Wallace Croft</a>
 * @author Amos Wenger (aka BlueSky)
 * @author Marvin Froehlich (aka Qudus)
 */
public final class ff_OBJModelLoader
{
    
    private static float[] parseVector( String line, float[] vector )
    {
        StringTokenizer tokens = new StringTokenizer( line );
        
        tokens.nextToken();
        vector[ 0 ] = Float.parseFloat( tokens.nextToken() );
        vector[ 1 ] = Float.parseFloat( tokens.nextToken() );
        vector[ 2 ] = Float.parseFloat( tokens.nextToken() );
        
        return ( vector );
    }
    
    private static float[] parseTexCoord( String line, float[] texCoord )
    {
        StringTokenizer tokens = new StringTokenizer( line );
        
        tokens.nextToken();
        texCoord[ 0 ] = Float.parseFloat( tokens.nextToken() );
        texCoord[ 1 ] = Float.parseFloat( tokens.nextToken() );
        
        return ( texCoord );
    }
    
    private static float[] parseColor( String token )
    {
        StringTokenizer tokens = new StringTokenizer( token );
        
        float[] color = new float[ 3 ];
        
        color[ 0 ] = Float.parseFloat( tokens.nextToken() );
        color[ 1 ] = Float.parseFloat( tokens.nextToken() );
        color[ 2 ] = Float.parseFloat( tokens.nextToken() );
        
        return ( color );
    }
    

    private static List<vc_Material> parseMatLib( Scanner scan )
    {
        List<vc_Material> matList = new ArrayList<vc_Material>( 1 );
        vc_Material       mat     = null;
            
            String line;
            try {
				while ( ( line = scan.nextLine() ) != null )
				{
				   if ( line.startsWith( "newmtl" ) )
				    {
				        String matName = line.substring( line.indexOf( " " ) + 1 );
				        
				        mat = new vc_Material( matName );
				        
				        matList.add( mat );
				    }
				    else if ( line.startsWith( "Tf" ) )
				    {
				        float[] color = parseColor( line.substring( line.indexOf( " " ) + 1 ) );
				        
				        mat.setDiffuseColor( color );
				        mat.setAmbientColor( color );
				    }
				    else if ( line.startsWith( "Ka" ) )
				    {
				        float[] color = parseColor( line.substring( line.indexOf( " " ) + 1 ) );
				        
				        mat.setAmbientColor( color );
				    }
				    else if ( line.startsWith( "Kd" ) )
				    {
				        float[] color = parseColor( line.substring( line.indexOf( " " ) + 1 ) );
				        
				        mat.setColor( color );
				        mat.setDiffuseColor( color );
				    }
				    else if ( line.startsWith( "Ks" ) )
				    {
				        float[] color = parseColor( line.substring( line.indexOf( " " ) + 1 ) );
				        
				        mat.setSpecularColor( color );
				    }
				    else if ( line.startsWith( "Ns" ) )
				    {
				        mat.setShininess( Float.parseFloat( line.substring( line.indexOf( " " ) + 1 ) ) );
				    }
				    else if ( line.startsWith( "map_Kd" ) )
				    {
				        String texName = line.substring( line.indexOf( " " ) + 1 );
				        
				        mat.setTextureFileName( texName );
				    }
				}
			} catch (NumberFormatException e) {

				matList=null;
			} catch (NoSuchElementException e) {
			}
			scan.close();
        return ( matList );
    }
    
    public static ff_OBJModel load( Scanner scan ) throws IOException
    {                
        HashMap<String, vc_Material> matMap = new HashMap<String, vc_Material>();        
        ff_OBJModelGroup topGroup = new ff_OBJModelGroup( );
        try
        {
            vc_Material currentMat = null;
            
            ff_OBJModelGroup currentGroup = topGroup;
            
            String line = null;
            float f[]=new float[3];
            float t[]=new float[2];
            while ((line = scan.nextLine()) != null)
            {
                if (line.startsWith( "vn" ))
                {
                	currentGroup.addNormal( parseVector( line,f ) );
                }
                else if (line.startsWith( "vt" ))
                {
                	currentGroup.addTexture( parseTexCoord( line,t ) );
                }
                else if (line.startsWith( "v" ))
                {
                    currentGroup.addVertex( parseVector( line,f ) );
                }
                else if (line.startsWith( "f" ))
                {
                    ff_OBJParseFaces.add(currentGroup.faces, currentGroup.vtnIndices, line, currentMat );
                }
                else if (line.startsWith( "g" ))
                {
                    /*String name = (line.trim().length() >= 3) ? line.substring( 2 ) : null;
                    OBJGroup g = new OBJGroup( name, verts, normals, texs );
                    topGroup.addChild( g );
                    currentGroup = g;*/
                }
                else if (line.startsWith( "o" ))
                {
                    /*String name = (line.trim().length() >= 3) ? line.substring( 2 ) : null;
                    OBJGroup g = new OBJGroup( name, verts, normals, texs );
                    topGroup.addChild( g );
                    currentGroup = g;*/
                }
                else if (line.startsWith( "mtllib" ))
                {
                    StringTokenizer tokens = new StringTokenizer( line );
                    tokens.nextToken();
                    String name = tokens.nextToken();
                    List<vc_Material> matList;
                    try {
                    matList = parseMatLib( new Scanner( new File("./data/"+name) ) );
                    } catch (Exception e) {
                    	continue;
                    }
                    for (vc_Material mat: matList)
                    {
                        if (mat != null)
                            matMap.put( mat.getName(), mat );
                    }
                }
                else if (line.startsWith( "usemtl" ))
                {
                    String name = line.substring( line.indexOf( " " ) + 1 );
                    currentMat = matMap.get( name );
                }
            } //while
        } //try
        catch (NoSuchElementException e) { }
        finally
        {
            scan.close();
        }
        
        return ( new ff_OBJModel( matMap, topGroup ) );
    }
        
    private ff_OBJModelLoader()
    {
    }
    public static void main(String args[]) {
    	try {
			ff_OBJModel m=load(new Scanner(new File("./data/cube.obj")));
			System.out.println(m);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
