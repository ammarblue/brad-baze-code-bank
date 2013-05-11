package reuze.pending;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.LinkedList;

import com.software.reuze.gb_Vector3;

import processing.core.PApplet;
import processing.core.PImage;
/**
 * This class is used to load MD2 models.
 * 
 * This functionality has been split from the MD2_Model class to aid
 * future development.
 * The following feature(s) of the MD2 model format have not been 
 * implement yet:
 *    multiple skin files
 * 
 * @author Peter Lager
 *
 */
public class MD2_Loader {
	// Used to hold the file contents while it is being used to 
	// create the model, after the model is created it is dumped.
	private ByteBuffer buf;

	private String ident;		// identifies as MD2 file "IDP2"
	private int version;	 	// mine is 8
	private int skinwidth;		// width of texture
	private int skinheight;		// height of texture
	private int framesize;		// number of bytes per frame
	private int numSkins;		// number of textures
	private int numXYZ;			// number of points
	private int numST;			// number of texture
	private int numTris;		// number of triangles
	private int numGLcmds;		// number of gl commands and vetices
	private int numFrames;		// total number of frames
	private int offsetSkins;	// offset to skin names (64 bytes each)
	private int offsetST;		// offset of texture s-t values
	private int offsetTris;		// offset of triangle mesh
	private int offsetFrames;	// offset of frame data (points)
	private int offsetGLcmds;	// type of OpenGL commands to use
	private int offsetEnd;		// end of file

	/*
	 * Each frame in the file has a name which is used to identify the
	 * different model animation sets.
	 */
	private String[] frameNames;
	private MD2_ModelState[] states;

	// All the XYZ points that make up the model
	private gb_Vector3[] points;

	
	private gb_Vector3 modOffset;   // values to subtract to center model
	private gb_Vector3 modSize;     // size of model in 3 dimensions

	private int[] glCommands;      // Type (FAN or STRIP and number of vertices to render
	private GL_Vertex[] glVertex;  // vertices to render in FAN or STRIP

	private PImage tex;          // the texture to be used

	private PApplet app;

	public MD2_Loader(PApplet papplet){
		app = papplet;
	}

	/**
	 * This method performs 3 main tasks
	 * 1) Create the model texture
	 * 2) Load the model data from file
	 * 3) Create a MD2_Model object
	 * 
	 * @param modelFilename
	 * @param texFilename
	 * @return a MD2_Model object or null if unsuccessful
	 */
	public MD2_Model loadModel(String modelFilename, String texFilename){
		MD2_Model model = null;
		tex = app.loadImage(texFilename);
		if(createBuffer(modelFilename) == 0){
			loadHeader();
			getPointList();
			getModelStates();
			getGLcommands();
			// release buffer memory ASAP
			buf = null;
			System.gc();

			model = new MD2_Model(app, numFrames, numXYZ, framesize, 
					points, modOffset, modSize, glCommands,glVertex, tex, states);
		}
		else {
			System.out.println("Failed to open buffer");
		}
		return model;
	}

	/**
	 * Create a ByteBuffer object that holds the file contents
	 * 
	 * @param mdlFile
	 * @return 0 - successful : >0 failed
	 */
	private int createBuffer(String mdlFile){
		int error = 0;
                try{
 		InputStream is = app.createInput(mdlFile);
                byte[] b = PApplet.loadBytes(is);
 		buf = ByteBuffer.wrap(b);      // BIG_ENDIAN is the default - might be for Java
		// but the file was created with a C++ program
		buf.order(ByteOrder.LITTLE_ENDIAN);
		//buf.order(ByteOrder.BIG_ENDIAN);
		buf.rewind();
                }
                catch (Exception e){
                  error = 1;
                }
                return error;              
	}

	/**
	 * Load all MD2 header information even if we don't get to use it
	 */
	private void loadHeader(){
		ident = readString(4);
		version = buf.getInt();
		skinwidth = buf.getInt();
		skinheight = buf.getInt();
		framesize = buf.getInt();
		numSkins = buf.getInt();
		numXYZ = buf.getInt();
		numST = buf.getInt();      // number of texture
		numTris = buf.getInt();      // number of triangles
		numGLcmds = buf.getInt();
		numFrames = buf.getInt();    // total number of frames
		offsetSkins = buf.getInt();  // offset to skin names (64 bytes each)
		offsetST = buf.getInt();    // offset of texture s-t values
		offsetTris = buf.getInt();   // offset of triangle mesh
		offsetFrames = buf.getInt(); // offset of frame data (points)
		offsetGLcmds = buf.getInt(); // type of OpenGL commands to use
		offsetEnd = buf.getInt();    // end of file
	}

	private void getPointList(){
		buf.position(offsetFrames);
		points = new gb_Vector3[numFrames * numXYZ];
		int index = 0;
		float x,y,z;
		float minX, minY, minZ, maxX, maxY, maxZ;
		minX = minY = minZ = Float.MAX_VALUE;
		maxX = maxY = maxZ = -Float.MAX_VALUE;

		int normIndex;
		float sx,sy,sz,tx,ty,tz;
		frameNames = new String[numFrames];
		for(int j = 0 ; j < numFrames ; j++){
			buf.position(offsetFrames + framesize * j);
			sx = buf.getFloat();
			sy = buf.getFloat();
			sz = buf.getFloat();
			tx = buf.getFloat();
			ty = buf.getFloat();
			tz = buf.getFloat();
			frameNames[j] = readString(16);
			for(int i = 0; i < numXYZ; i++){
				x = sx * getUByte() + tx;
				y = sy * getUByte() + ty;
				z = sz * getUByte() + tz;
				if(x < minX) minX = x;
				if(y < minY) minY = y;
				if(z < minZ) minZ = z;
				if(x > maxX) maxX = x;
				if(y > maxY) maxY = y;
				if(z > maxZ) maxZ = z;
				normIndex = buf.get(); // not used in this program
				points[index] = new gb_Vector3(x, y, z);
				index++;	
			}
		}
		modOffset = new gb_Vector3((minX + maxX)/2, (minY + maxY)/2, (minZ + maxZ)/2);
		modSize = new gb_Vector3(maxX-minX, maxY-minY, maxZ-minZ);
	}

	/**
	 * 	 * Get the next 2 bytes from the buffer and interpret as
	 * 	 * a signed integer (twos complement) 
	 * 	 * @return
	 */
	private int getShort(){
		int ch = buf.getChar();
		ch = (ch > 32767) ? 65536 - ch : ch;
		return ch;
	}

	/**
	 * 	 * Get the next 2 bytes from the buffer and interpret as
	 * 	 * an unsigned integer 
	 * 	 * @return
	 */
	private int getUShort(){
		int ch = buf.getChar();
		ch = (ch > 32767) ? -(65536 - ch) : ch;
		return ch;
	}

	/**
	 * 	 * Get the next byte from the buffer and interpret as
	 * 	 * an unsigned integer (0 - 255)
	 * 	 * @return
	 */
	private int getUByte(){
		int ch = buf.get();
		ch = (ch < 0) ? ch + 256 : ch;
		return ch;
	}

	/**
	 * Extract a string from the buffer until a zero byte or
	 * nbytes have been read.
	 * The buffer pointer is advanced by nbytes irrespective of 
	 * string length.
	 *  
	 * @param nbytes
	 * @return
	 */
	private String readString(int nbytes){
		return readString(nbytes, '\0');
	}

	/**
	 * Extract a string from the buffer until the stopAt character
	 * is reached (string does not include stopAt char) or until
	 * nbytes have been read.
	 * The buffer pointer is advanced by nbytes irrespective of 
	 * string length.
	 *
	 * @param nbytes
	 * @return
	 */
	private String readString(int nbytes, char stopAt){
		char ch;
		StringBuilder s = new StringBuilder("");
		for(int i = 0; i < nbytes; i++){
			ch = (char)buf.get();
			if(ch != stopAt)
				s.append(ch);
		}
		return new String(s);
	}

	/**
	 * Create a list of model states based on frame names with ant
	 * trailing digits removed e.e. crouch001, crouch002 both brcome 
	 * crouch.
	 */
	private void getModelStates(){
		LinkedList<MD2_ModelState> list = new LinkedList<MD2_ModelState>();
		String nextFrameName;

		// Load first state so we have something to compare when
		// we loop through rest of frames
		String thisFrameName = stripTrailingNumber(frameNames[0]);
		MD2_ModelState state = new MD2_ModelState(thisFrameName,0,0);

		for(int i = 1; i < numFrames; i++){
			nextFrameName = stripTrailingNumber(frameNames[i]);
			if(state.name.equals(nextFrameName)){
				state.endFrame = i;
			}
			else {
				list.add(state);
				state = new MD2_ModelState(nextFrameName, i,i);
			}
		}
		list.add(state);
		// Convert linked list to an array of correct size
		states = (MD2_ModelState[])list.toArray(new MD2_ModelState[list.size()]);
	}

	/**
	 * Remove trailing numbers from a string used to get state names
	 * @param s
	 * @return
	 */
	private String stripTrailingNumber(String s){
		String result = "";
		if( s != null && s.length() > 0){
			int len = s.length();
			char[] c = new char[len]; 
			s.getChars(0, len, c, 0);
			int pos = len -1;
			while(pos > 0 && c[pos] >= '0' && c[pos] <= '9')
				pos--;
			if(pos > 0)
				result = s.substring(0,pos+1);
		}
		return result;
	}

	/**
	 * 	 * Get the OpenGL commands to be used (GL_TRIANGLE_STRIP or 
	 * 	 * GL_TRIANGLE_FAN) and the vertices (index to points array
	 * 	 * and texture coordinates)
	 */
	public void getGLcommands(){
		buf.position(offsetGLcmds);
		int cmd;
		float s,t;
		int idx;
		LinkedList<Integer> cmds = new LinkedList<Integer>();
		LinkedList<GL_Vertex> verts = new LinkedList<GL_Vertex>();
		GL_Vertex v;

		while(buf.position() < offsetEnd){
			// get GL command type and num vertices
			cmd = buf.getInt();
			cmds.addLast(cmd);
			if(cmd < 0)	cmd = -cmd;

			for(int n = 0; n < cmd; n++){
				s = buf.getFloat();
				t = buf.getFloat();
				idx = buf.getInt();
				v = new GL_Vertex(s,t,idx);
				verts.addLast(v);
			}
		}
		/* 
		 * Convert LinkedList<Integer> to int array of the correct size
		 * This will unbox the integers just once so should make the
		 * rendering quicker.
		 */
		glCommands = new int[cmds.size()];
		Iterator<Integer> iter = cmds.iterator();
		int pos = 0;
		while(iter.hasNext()){
			glCommands[pos] = iter.next();
			pos++;
		}
		/*
		 * convert LinkedList<Vertex> to Vertex array of correct size.
		 * Faster to traverse array than list when we come to render
		 */
		glVertex = (GL_Vertex[])verts.toArray(new GL_Vertex[verts.size()]);
	}

	/*
	 * The remaining methods in this class were used to confirm that
	 * the MD2 file was read correctly and are not needed in the
	 * distribution version
	 */
	public void displayGLcommands(){
		int cmd;
		int vpos = 0;

		for(int i = 0; i < glCommands.length; i++){
			cmd = glCommands[i];
			if(cmd < 0){
				cmd = -cmd;
				System.out.println("GL_TRIANGLE_FAN    ( " + cmd + " vertices )");
			}
			else if(cmd > 0) {
				System.out.println("GL_TRIANGLE_STRIP  ( " + cmd + " vertices )");
			}
			for(int j = 0; j < cmd; j++){
				System.out.println(glVertex[vpos++]);
			}
		}
		System.out.println("Number of fans & strips " + glCommands.length);
		System.out.println("Number of verts to process " + glVertex.length);
	}


	public void displayModelStates(){
		for(int i =0; i < states.length; i++){
			System.out.println(states[i]);
		}
	}

	public void displayFrameNames(){
		for(int i = 0 ; i < frameNames.length; i++){
			System.out.println(i + "\t"+ stripTrailingNumber(frameNames[i]));
		}
	}

	public void displayHeader(){
		System.out.println("Ident " + ident+ " ");		 // identifies as MD2 file "IDP2"
		System.out.println("Version " + version + " ");	 // mine is 8
		System.out.println("Skin size " + skinwidth + " x " + skinheight);    // width of texture
		System.out.println("Frame size " + framesize + " bytes");    // number of bytes per frame
		System.out.println("Number of skins " + numSkins + " listed");     // number of textures
		System.out.println("Number of vertices " + numXYZ + " per frame");       // number of pos
		System.out.println("Number of textute coords " + numST + " ");        // number of texture
		System.out.println("Number of triangles " + numTris + " ");      // number of triangles
		System.out.println("Number of GL commands " + numGLcmds + " ");
		System.out.println("Number of frames " + numFrames + " ");    // total number of frames
		System.out.println(">> Offset to skins       " + offsetSkins+ " ");  // offset to skin names (64 bytes each)
		System.out.println(">> Offset to tex coords  " + offsetST + " ");     // offset of texture s-t values
		System.out.println(">> Offset to triangles   " + offsetTris + " ");   // offset of triangle mesh
		System.out.println(">> Offset to frame data  " + offsetFrames + " "); // offset of frame data (pos)
		System.out.println(">> Offset to GL commands " + offsetGLcmds + " "); // type of OpenGL commands to use
		System.out.println(">> Offset end of file    " + offsetEnd + " ");    // end of file
	}
	
	/**
	 * For each point in the model we store the texture coordinates and an 
	 * index value to an array of Vector that hold the XYZ coordinates.
	 *
	 * @author Peter Lager
	 *
	 */
	public static class GL_Vertex {
		public float s;
		public float t;
		public int idx;
		
		/**
		 * Parameter ctor
		 * @param s
		 * @param t
		 * @param idx
		 */
		public GL_Vertex(float s, float t, int idx) {
			this.s = s;
			this.t = t;
			this.idx = idx;
		}
		
		public String toString(){
			return (""+idx+"\t"+s+"\t"+t);
		}
	}

}

