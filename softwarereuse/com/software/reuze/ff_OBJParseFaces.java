package com.software.reuze;
import java.util.StringTokenizer;



public final class ff_OBJParseFaces {
    private final static int parseInt( String token )
    {
        if ( token.length() == 0 )
        {
            return ( -1 );
        }
        
        return ( Integer.parseInt( token ) );
    }
    
    private final static int parseVertIndex(String token)
    {
        if ( token.indexOf( "/" ) >= 0 )
        {
            return ( parseInt( token.substring( 0, token.indexOf( "/" ) ) ) );
        }
        
        return ( parseInt( token ) );
    }
    
    private final static int parseTextureIndex( String token )
    {        
        int slashPos = token.indexOf( "/" );
        
        if ( slashPos < 0 )
        {
            return ( -1 );
        }
        token = token.substring( slashPos + 1 );
        slashPos = token.indexOf( "/" );
        
        if ( slashPos >= 0 )
        {
            return parseInt( token.substring( 0, slashPos ) );
        }
        
        return parseInt( token );
    }
    
    private final static int parseNormalIndex( String token )
    {        
        if (token.indexOf( "/" ) < 0)
        {
            return ( -1 );
        }
        token = token.substring( token.indexOf( "/" ) + 1 );
        if ( token.indexOf( "/" ) < 0 )
        {
            return ( -1 );
        }
        token = token.substring( token.indexOf( "/" ) + 1 );
        
        if ( token.indexOf( "/" ) >= 0 )
        {
            return parseInt( token.substring( 0, token.indexOf( "/" ) ) );
        }
        
        return parseInt( token );
    }
    
    public final static int add( d_ArrayListInt faces, d_ArrayListInt faceIndices, String line, vc_Material mat )
    {
        StringTokenizer tokens = new StringTokenizer( line );
        
        int startFace=faces.size();
        int startIndex=faceIndices.size();
        int matIndex=(mat==null)?-1:mat.getIndex();
        faces.add(startIndex, 3, matIndex);
        tokens.nextToken();
        int i=0;
        while ( tokens.hasMoreTokens() )
        {
            String pt = tokens.nextToken();
            
            int vi = parseVertIndex( pt );
            int ti = parseTextureIndex( pt );
            int ni = parseNormalIndex( pt );
            i++;
            if (i<=3) {
              faceIndices.add(( vi > 0 ) ? vi - 1 : -1, ( ni > 0 ) ? ni - 1 : -1, ( ti > 0 ) ? ti - 1 : -1 );
            } else if (i==4) {
            	faces.add(startIndex+9*1, 3, matIndex);
            	faceIndices.add(faceIndices.get(startIndex+2*3),faceIndices.get(startIndex+2*3+1),faceIndices.get(startIndex+2*3+2));
            	faceIndices.add(( vi > 0 ) ? vi - 1 : -1, ( ni > 0 ) ? ni - 1 : -1, ( ti > 0 ) ? ti - 1 : -1 );
            	if  (!tokens.hasMoreTokens()) 
            		faceIndices.add(faceIndices.get(startIndex+0*3),faceIndices.get(startIndex+0*3+1),faceIndices.get(startIndex+0*3+2));
            } else if (i==5) {
                faceIndices.add(( vi > 0 ) ? vi - 1 : -1, ( ni > 0 ) ? ni - 1 : -1, ( ti > 0 ) ? ti - 1 : -1 );
                faces.add(startIndex+9*2, 3, matIndex);
            	faceIndices.add(( vi > 0 ) ? vi - 1 : -1, ( ni > 0 ) ? ni - 1 : -1, ( ti > 0 ) ? ti - 1 : -1 );
            	faceIndices.add(faceIndices.get(startIndex+0*3),faceIndices.get(startIndex+0*3+1),faceIndices.get(startIndex+0*3+2));
            	faceIndices.add(faceIndices.get(startIndex+2*3),faceIndices.get(startIndex+2*3+1),faceIndices.get(startIndex+2*3+2));
            } //if
        } //while
        return startFace;
    }
}
