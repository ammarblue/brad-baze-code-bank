package com.software.reuze;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;


/**
 * This class keeps track of a 3ds model
 */
public class ff_3DSModel {
    // constants
    //>------ Primary Chunk, at the beginning of each file
    private static final int PRIMARY        = 0x4D4D;

    //>------ Main Chunks
    private static final int OBJECTINFO     = 0x3D3D; // This gives the version of the mesh and is found right before the material and object information
    private static final int VERSION        = 0x0002; // This gives the version of the .3ds file
    private static final int EDITKEYFRAME   = 0xB000; // This is the header for all of the key frame info

    //>------ sub defines of OBJECTINFO
    private static final int MATERIAL	= 0xAFFF; // This stored the texture info
    private static final int OBJECT	        = 0x4000; // This stores the faces, vertices, etc...

    //>------ sub defines of MATERIAL
    private static final int MATNAME        = 0xA000; // This holds the material name
    private static final int MATDIFFUSE     = 0xA020; // This holds the color of the object/material
    private static final int MATMAP         = 0xA200; // This is a header for a new material
    private static final int MATMAPFILE     = 0xA300; // This holds the file name of the texture

    private static final int OBJECT_MESH    = 0x4100; // This lets us know that we are reading a new object

    //>------ sub defines of OBJECT_MESH
    private static final int OBJECT_VERTICES = 0x4110; // The objects vertices
    private static final int OBJECT_FACES	 = 0x4120; // The objects faces
    private static final int OBJECT_MATERIAL = 0x4130; // This is found if the object has a material, either texture map or color
    private static final int OBJECT_UV       = 0x4140; // The UV texture coordinates

    // data typ length to load C++ files
    private static final int WORD = 2;
    private static final int DWORD = 4;

    /** logging */
    private static final Logger LOG = Logger.getLogger(ff_3DSModel.class.getName());

    private String meshName;

    private int fileVersion;       // This will hold the file version
    public List<MaterialInfo3DS> materials;   // The list of material information (Textures and colors)
    public List<Object3DS> objects;     // The object list for our model


    private f_LittleEndianDataInputStream winDataInputStream;
    private Chunk currentChunk;
    private Chunk tempChunk;

    // jogl
//    private GL gl;

    /**
     * Creates a JD3dsModel by loading it from the given mesh file.
     *
     * @param meshFile The mesh data of the 3ds model.
     */
    public ff_3DSModel(File meshFile) {
        LOG.fine("JD3dsModel(GLDrawable, File)");
//        gl = JDEngine.getInstance().getGL();

        materials = new LinkedList<MaterialInfo3DS>();
        objects = new LinkedList<Object3DS>();

        meshName = meshFile.getName().substring(0, meshFile.getName().length() - 4);
        LOG.fine("meshName: " + meshName);

        currentChunk = new Chunk(); // Initialize and allocate our current chunk
        tempChunk = new Chunk();    // Initialize and allocate a temporary chunk

        // load file in buffer
        FileInputStream fileInputStream;
        byte[] buffer = null;

        // read the file in byte buffer and close it
        try {
            fileInputStream = new FileInputStream(meshFile);
            buffer = new byte[(int)meshFile.length()];
            fileInputStream.read(buffer, 0, (int)meshFile.length());
            fileInputStream.close();

            winDataInputStream = new f_LittleEndianDataInputStream(new ByteArrayInputStream(buffer));
        }
        catch (IOException ioException) {
            LOG.severe("*** Can't read model file ***");
            LOG.severe(ioException.getMessage());
            return;
        }



        // Once we have the file open, we need to read the very first data chunk
        // to see if it's a 3DS file.  That way we don't read an invalid file.
        // If it is a 3DS file, then the first chunk ID will be equal to PRIMARY (some hex num)

        // Read the first chuck of the file to see if it's a 3DS file
        readChunk(currentChunk);

        // Make sure this is a 3DS file
        if (currentChunk.ID != PRIMARY) {
            LOG.severe("Unable to load PRIMARY chuck from file: " + getName());
            return;
        }

        // Now we actually start reading in the data.  ProcessNextChunk() is recursive
        // Begin loading objects, by calling this recursive function
        processNextChunk(currentChunk);

        // After we have read the whole 3DS file, we want to calculate our own vertex normals.
        computeNormals();

        // Clean up after everything
        currentChunk = null; // Free the current chunk
        tempChunk = null;	 // Free our temporary chunk

    } // END constructor JD3dsModel

    /**
     * The model data is already in memory, so this constructor uses references to
     * this data and doesn't load the model again.
     *
     * @param model The model to reuse the data of.
     *
     */
    public ff_3DSModel(ff_3DSModel model) {
//        gl = JDEngine.getInstance().getGL();

        meshName = model.getName();

        materials = model.materials;
        objects = model.objects;
    } // END constructor JD3dsModel(GLDrawable drawable, JDmd2Model model)

    /**
     * This function computes the normals and vertex normals of the objects
     */
    private void computeNormals() {
        Vertex3D vector1, vector2, normal;
        Vertex3D[] poly = new Vertex3D[3];
        poly[0]=new Vertex3D(); poly[1]=new Vertex3D(); poly[2]=new Vertex3D();
        // If there are no objects, we can skip this part
        if(objects.size() == 0) return;

        // What are vertex normals?  And how are they different from other normals?
        // Well, if you find the normal to a triangle, you are finding a "Face Normal".
        // If you give OpenGL a face normal for lighting, it will make your object look
        // really flat and not very round.  If we find the normal for each vertex, it makes
        // the smooth lighting look.  This also covers up blocky looking objects and they appear
        // to have more polygons than they do.    Basically, what you do is first
        // calculate the face normals, then you take the average of all the normals around each
        // vertex.  It's just averaging.  That way you get a better approximation for that vertex.

        // Go through each of the objects to calculate their normals
        for(int index = 0; index < objects.size(); index++) {
            // Get the current object
            Object3DS object = objects.get(index);

            // Here we allocate all the memory we need to calculate the normals
            Vertex3D[] normals = new Vertex3D[object.numberOfFaces];
            for (int i = 0; i < object.numberOfFaces; i++) normals[i] = new Vertex3D();

            Vertex3D[] tempNormals = new Vertex3D[object.numberOfFaces];
            for (int i = 0; i < object.numberOfFaces; i++) tempNormals[i] = new Vertex3D();

            object.normals = new Vertex3D[object.numberOfVertices];
            for (int i = 0; i < object.numberOfVertices; i++) object.normals[i] = new Vertex3D();

            // Go though all of the faces of this object
            for(int i = 0; i < object.numberOfFaces; i++) {
                // To cut down LARGE code, we extract the 3 points of this face
                poly[0].set(object.vertices[object.faces[i].vertexIndex[0]]);
                poly[1].set(object.vertices[object.faces[i].vertexIndex[1]]);
                poly[2].set(object.vertices[object.faces[i].vertexIndex[2]]);

                // Now let's calculate the face normals (Get 2 vectors and find the cross product of those 2)

                vector1 = poly[0].sub(poly[0], poly[2]); // Get the vector of the polygon (we just need 2 sides for the normal)
                vector2 = poly[0].sub(poly[2], poly[1]); // Get a second vector of the polygon

                normal = vector1.cross(vector1, vector2); // Return the cross product of the 2 vectors (normalize vector, but not a unit vector)
                tempNormals[i] = new Vertex3D(normal); // Save the un-normalized normal for the vertex normals
                normal.normalize(); // Normalize the cross product to give us the polygons normal
                normals[i] = normal; // Assign the normal to the list of normals
            }

            //////////////// Now Get The Vertex Normals /////////////////
            Vertex3D vSum = new Vertex3D(0.0f, 0.0f, 0.0f);
            int shared=0;

            // Go through all of the vertices
            for (int i = 0; i < object.numberOfVertices; i++) {
                // Go through all of the triangles
                for (int j = 0; j < object.numberOfFaces; j++) {
                    // Check if the vertex is shared by another face
                    if (object.faces[j].vertexIndex[0] == i || object.faces[j].vertexIndex[1] == i || object.faces[j].vertexIndex[2] == i) {
                        // Add the un-normalized normal of the shared face
                        //vSum = JDVertex3D.add(vSum, tempNormals[j]);
                        vSum.add(tempNormals[j]);
                        // Increase the number of shared triangles
                        shared++;
                    }
                }

                // Get the normal by dividing the sum by the shared.  We negate the shared so it has the normals pointing out.
                //object.normals[i] = JDVertex3D.divideByScalar(vSum, (float)-shared);
                vSum.divideByScalar(-shared);
                object.normals[i] = vSum;

                // Normalize the normal for the final vertex normal
                object.normals[i].normalize();

                vSum = new Vertex3D(0.0f, 0.0f, 0.0f);    // Reset the sum
                shared = 0;	                            // Reset the shared
            } // END for (i = 0; i < pObject->numOfVerts; i++) {

            // Free our memory and start over on the next object
            tempNormals = null;
            normals = null;
        } // END for(int index = 0; index < model.numberOfObjects; index++)
    } // END computeNormals

    /**
     * This method is responsible for drawing this model on screen.
     * You must create the body of this method for your model to show.
     *
     * @param percent The percentage between two key frames.
     */
    public void render(float percent) {
        // Since we know how many objects our model has, go through each of them.
        for(Iterator iterator = objects.iterator(); iterator.hasNext(); ) {

            // Get the current object that we are displaying
            Object3DS object = (Object3DS)iterator.next();
            // LOG.fine("object.name: " + object.name);

//            gl.glEnable(GL.GL_TEXTURE_2D);
            // Go through all of the faces (polygons) of the object and draw them
            int materialID = -1;
            for (int j = 0; j < object.numberOfFaces; j++) {
                // check if we use the right material
                if (object.faces[j].materialID != materialID) {
                    // this face has a differen material,
                    // thus we must set the correct material/texture
                    materialID = object.faces[j].materialID;
//                    JDTexture texture = materials.get(materialID).texture;
//                    if (texture != null) {
//                        texture.bind(gl);
//                    }

                }
                // Go through each corner of the triangle and draw it.
//                gl.glBegin(GL.GL_TRIANGLES);
                for (int whichVertex = 0; whichVertex < 3; whichVertex++) {
                    // Get the index for each point of the face
                    int index = object.faces[j].vertexIndex[whichVertex];
                    // Give OpenGL the normal for this vertex.
//                    gl.glNormal3f(object.normals[index].x, object.normals[index].y, object.normals[index].z);
                    // set texture coordinates
//                    gl.glTexCoord2f(object.vertices[index].u, object.vertices[index].v);
                    // Pass in the current vertex of the object (Corner of current face)
//                    gl.glVertex3f(object.vertices[index].x, object.vertices[index].y, object.vertices[index].z);
                } // END for(int whichVertex = 0; whichVertex < 3; whichVertex++)
//                gl.glEnd(); // End the drawing
            } // END for (int j = 0; j < object.numberOfFaces; j++)
        } // END for(int i = 0; i < g_3DModel.numOfObjects; i++)
    } // END drawModel

    /**
     * This method returns the maximum number of states the JD3dsModelLoader supports.
     * Because it doesn't support keyframe animation it only has one state.
     *
     * @return number of supportes states (1).
     * @see jd.engine.model.JDModel#getMaximumNumberOfStates()
     */
    public int getMaximumNumberOfStates() {
        return 1;
    } // END getMaximumNumberOfStates

    /**
     * @see jd.engine.model.JDModel#getName()
     */
    public String getName() {
        return meshName;
    } // END getName

    /**
     * @see jd.engine.model.JDModel#getNextFrame()
     */
    public int getNextFrame() {
        return 0;
    } // END getNextFrame

    /**
     * @see jd.engine.model.JDModel#getState()
     */
    public int getState() {
        // This method is needed to implement the JDModel interface. Because the
        // 3dsModelLoader doesn't support keyframes, it always is in state 0.
        return 0;
    } // END getState

    /**
     * @see jd.engine.model.JDModel#getStateEndFrame()
     */
    public int getStateEndFrame() {
        return 0;
    } // END getStateEndFrame

    /**
     * Tells if animation sequence completed once.
     *
     * @return - true animation sequence completed.
     * @see jd.engine.model.JDModel#isAnimationCompleted()
     */
    public boolean isAnimationCompleted() {
        return true;
    } // END isAnimationCompleted

    /**
     * This function reads the main sections of the .3DS file, then dives deeper with recursion.
     *
     * @param previousChunk The previous chung of the 3DS file.
     */
    private void processNextChunk(Chunk previousChunk) {
        LOG.fine("--- processNextChunk");

        currentChunk = new Chunk();	// Allocate a new chunk

        // Below we check our chunk ID each time we read a new chunk.  Then, if
        // we want to extract the information from that chunk, we do so.
        // If we don't want a chunk, we just read past it.

        // Continue to read the sub chunks until we have reached the length.
        // After we read ANYTHING we add the bytes read to the chunk and then check
        // check against the length.
        while (previousChunk.bytesRead < previousChunk.length) {
            // Read next Chunk
            readChunk(currentChunk);

            // Check the chunk ID
            switch (currentChunk.ID) {
                case VERSION: // This holds the version of the file
                    LOG.fine("VERSION");
                    // This chunk has an unsigned short that holds the file version.
                    // Since there might be new additions to the 3DS file format in 4.0,
                    // we give a warning to that problem.

                    // Read the file version and add the bytes read to our bytesRead variable
                    try {
                        fileVersion = winDataInputStream.readInt();
                        LOG.fine("fileVersion: " + fileVersion);
                        currentChunk.bytesRead += DWORD;
                    }
                    catch (Exception e) {
                        LOG.severe(e.getMessage());
                        //e.printStackTrace();
                    }

                    // If the file version is over 3, give a warning that there could be a problem
                    if (fileVersion > 0x03)
                        LOG.severe("This 3DS file is over version 3 so it may load incorrectly");
                break;

                case OBJECTINFO: // This holds the version of the mesh
                    LOG.fine("OBJECTINFO");
                    // This chunk holds the version of the mesh.  It is also the head of the MATERIAL
                    // and OBJECT chunks.  From here on we start reading in the material and object info.

                    // Read the next chunk
                    readChunk(tempChunk);

                    // Get the version of the mesh
                    try {
                        int meshVersion = winDataInputStream.readInt();
                        LOG.fine("meshVersion: " + meshVersion);
                        tempChunk.bytesRead += DWORD;
                    }
                    catch (Exception e) {
                        LOG.severe(e.getMessage());
                    }

                    // Increase the bytesRead by the bytes read from the last chunk
                    currentChunk.bytesRead += tempChunk.bytesRead;

                    // Go to the next chunk, which is the object has a texture, it should be MATERIAL, then OBJECT.
                    processNextChunk(currentChunk);
                break;

                case MATERIAL: // This holds the material information
                    LOG.fine("MATERIAL");
                    // This chunk is the header for the material info chunks
                    MaterialInfo3DS newTexture = new MaterialInfo3DS();    // This is used to add to our material list

                    // Add an empty texture structure to our texture list.
                    // If you are unfamiliar with STL's "vector" class, all push_back()
                    // does is add a new node onto the list.  I used the vector class
                    // so I didn't need to write my own link list functions.
                    materials.add(newTexture);

                    // Proceed to the material loading function
                    processNextMaterialChunk(currentChunk);
                    break;

                case OBJECT: // This holds the name of the object being read
                    LOG.fine("OBJECT");
                    // This chunk is the header for the object info chunks.
                    // It also holds the name of the object.
                    Object3DS newObject = new Object3DS();  // This is used to add to our object list

                    // Add a new tObject node to our list of objects (like a link list)
                    objects.add(newObject);

                    // Initialize the object and all it's data members
                    // memset(&(pModel->pObject[pModel->numOfObjects - 1]), 0, sizeof(t3DObject));

                    // Get the name of the object and store it, then add the read bytes to our byte counter.
                    try {
                        newObject.name = winDataInputStream.readCString(64, false);
                        LOG.fine("newObject.name: " + newObject.name);
                        currentChunk.bytesRead += newObject.name.length() + 1;
                    }
                    catch (Exception e) {
                        LOG.severe(e.getMessage());
                        //e.printStackTrace();
                    }
                    // Now proceed to read in the rest of the object information
                    processNextObjectChunk(newObject, currentChunk);
                break;

                case EDITKEYFRAME:
                    LOG.fine("EDITKEYFRAME");
                    // Because I wanted to make this a SIMPLE tutorial as possible, I did not include
                    // the key frame information.  This chunk is the header for all the animation info.
                    // In a later tutorial this will be the subject and explained thoroughly.

                    // ProcessNextKeyFrameChunk(pModel, m_CurrentChunk);

                    // Read past this chunk and add the bytes read to the byte counter
                    // If we didn't care about a chunk, then we get here.  We still need
                    // to read past the unknown or ignored chunk and add the bytes read to the byte counter.
                    try {
                        winDataInputStream.skipBytes(currentChunk.length - currentChunk.bytesRead);
                        currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                    }
                    catch (Exception e) {
                        LOG.severe(e.getMessage());
                        //e.printStackTrace();
                    }
                break;

                default:
                    // If we didn't care about a chunk, then we get here.  We still need
                    // to read past the unknown or ignored chunk and add the bytes read to the byte counter.
                    LOG.fine("processNextChunk default");
                    try {
                        winDataInputStream.skipBytes(currentChunk.length - currentChunk.bytesRead);
                        currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                    }
                    catch (Exception e) {
                        LOG.severe(e.getMessage());
                        //e.printStackTrace();
                    }
                break;
            } // END switch (currentChunk.ID)
            // Add the bytes read from the last chunk to the previous chunk passed in.
            previousChunk.bytesRead += currentChunk.bytesRead;
        } // END while (previousChunk.bytesRead < previousChunk.length)
        // Free the current chunk and set it back to the previous chunk (since it started that way)
        currentChunk = previousChunk;
    } // END readNextChunk

    /**
     * This function handles all the information about the material (Texture).
     *
     * @param previousChunk The previous chung.
     */
    private void processNextMaterialChunk(Chunk previousChunk) {
        LOG.fine("------ processNextMaterialChunk");
        // Allocate a new chunk to work with
        currentChunk = new Chunk();

        // Continue to read these chunks until we read the end of this sub chunk
        while (previousChunk.bytesRead < previousChunk.length) {
            // Read the next chunk
            readChunk(currentChunk);

            // Check which chunk we just read in
            switch (currentChunk.ID) {
                case MATNAME: // This chunk holds the name of the material
                    LOG.fine("MATNAME");
                    // Here we read in the material name
                    try {
                        materials.get(materials.size() - 1).name = winDataInputStream.readCString(currentChunk.length - currentChunk.bytesRead, false);
                        LOG.fine("Material name: " + materials.get(materials.size() - 1).name);
                        currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                    }
                    catch (Exception e) {
                        LOG.severe(e.getMessage());
                    }
                break;

                case MATDIFFUSE: // This holds the R G B color of our object
                    LOG.fine("MATDIFFUSE");
                    readColorChunk(materials.get(materials.size() - 1).color, currentChunk);
                break;
                /*case MATSPECULAR:
        			readColorChunk(materials.get(materials.size() - 1).color, currentChunk);
        			break;
        		
        		case MATSHININESS:
        			readColorChunk(mCurrentChunk, (float *) mBuffer);
        			break;*/

                case MATMAP: // This is the header for the texture info
                    LOG.fine("MATMAP");
                    // Proceed to read in the material information
                    processNextMaterialChunk(currentChunk);
                break;

                case MATMAPFILE: // This stores the file name of the material
                    LOG.fine("MATMAPFILE");
                    // Here we read in the material's file name
                    try {
                        String materialFileName = winDataInputStream.readCString(currentChunk.length - currentChunk.bytesRead, false);
                        // turn to lower case
                        materialFileName = materialFileName.toLowerCase();
                        materials.get(materials.size() - 1).file = materialFileName;
                        LOG.fine("materialFileName: " + materialFileName);
                        currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                    }
                    catch (Exception e) {
                        LOG.severe(e.getMessage());
                    }
                break;

                default:
                    // Read past the ignored or unknown chunks
                    try {
                        winDataInputStream.skipBytes(currentChunk.length - currentChunk.bytesRead);
                        currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                    }
                    catch (Exception e) {
                        LOG.severe(e.getMessage());
                    }
                break;
            }

            // Add the bytes read from the last chunk to the previous chunk passed in.
            previousChunk.bytesRead += currentChunk.bytesRead;
        } // END while (previousChunk.bytesRead < previousChunk.length)

        // Free the current chunk and set it back to the previous chunk (since it started that way)
        currentChunk = previousChunk;
    } // END processNextMaterialChunk

    /**
     * This function handles all the information about the objects in the file
     *
     * @param object The Object3DS.
     * @param previousChunk The previous chunk.
     */
    private void processNextObjectChunk(Object3DS object, Chunk previousChunk) {
        LOG.fine("------ processNextObjectChunk");
        // Allocate a new chunk to work with
        currentChunk = new Chunk();

        // Continue to read these chunks until we read the end of this sub chunk
        while (previousChunk.bytesRead < previousChunk.length) {
            // Read the next chunk
            readChunk(currentChunk);
System.out.println(Integer.toHexString(currentChunk.ID));
            // Check which chunk we just read
            switch (currentChunk.ID) {
                case OBJECT_MESH:	// This lets us know that we are reading a new object
                    LOG.fine("OBJECT_MESH");
                    // We found a new object, so let's read in it's info using recursion
                    processNextObjectChunk(object, currentChunk);
                break;

                case OBJECT_VERTICES: // This is the objects vertices
                    LOG.fine("OBJECT_VERTICES");
                    readVertices(object, currentChunk);
                break;

                case OBJECT_FACES: // This is the objects face information
                    LOG.fine("OBJECT_FACES");
                    readVertexIndices(object, currentChunk);
                break;

                case OBJECT_MATERIAL: // This holds the material name that the object has
                    LOG.fine("OBJECT_MATERIAL");
                    // This chunk holds the name of the material that the object has assigned to it.
                    // This could either be just a color or a texture map.  This chunk also holds
                    // the faces that the texture is assigned to (In the case that there is multiple
                    // textures assigned to one object, or it just has a texture on a part of the object.
                    // Since most of my game objects just have the texture around the whole object, and
                    // they aren't multitextured, I just want the material name.

                    // We now will read the name of the material assigned to this object
                    readObjectMaterial(object, currentChunk);
                break;

                case OBJECT_UV: // This holds the UV texture coordinates for the object
                    LOG.fine("OBJECT_UV");
                    // This chunk holds all of the UV coordinates for our object.  Let's read them in.
                    readUVCoordinates(object, currentChunk);
                break;

                default:
                    LOG.fine("processNextObjectChunk default");
                    System.out.println("--> currentChunk.ID: " + currentChunk.ID);
                    // Read past the ignored or unknown chunks
                    try {
                        winDataInputStream.skipBytes(currentChunk.length - currentChunk.bytesRead);
                        currentChunk.bytesRead += currentChunk.length - currentChunk.bytesRead;
                    }
                    catch (Exception e) {
                        LOG.severe(e.getMessage());
                        //e.printStackTrace();
                    }
                break;
            } // END switch (currentChunk.ID)

            // Add the bytes read from the last chunk to the previous chunk passed in.
            previousChunk.bytesRead += currentChunk.bytesRead;
        } // END while (previousChunk.bytesRead < previousChunk.length)

        // Free the current chunk and set it back to the previous chunk (since it started that way)
        currentChunk = previousChunk;
    } // END readNextObjectChunk

    /**
     * This function reads in a chunk ID and it's length in bytes.
     *
     * @param chunk The chunk to read.
     */
    private void readChunk(Chunk chunk) {
        try {
            // This reads the chunk ID which is 2 bytes.
            // The chunk ID is like OBJECT or MATERIAL.  It tells what data is
            // able to be read in within the chunks section.
            chunk.ID = winDataInputStream.readUnsignedShort();
            chunk.bytesRead = WORD;

            // Then, we read the length of the chunk which is 4 bytes.
            // This is how we know how much to read in, or read past.
            chunk.length = winDataInputStream.readInt();
            chunk.bytesRead += DWORD;
        }
        catch (Exception e) {
            LOG.severe(e.getMessage());
        }
    } // END readChunk

    /**
     * This function reads in the RGB color data.
     *
     * @param material The material.
     * @param chunk The chunk.
     */
    private void readColorChunk(int[] color/*out*/, Chunk chunk) {
        // Read the color chunk info
        readChunk(tempChunk);

        // Read in the R G B color (3 bytes - 0 through 255)
        try {
            // tempChunk.bytesRead += winDataInputStream.read(material.color);
            color[0] = winDataInputStream.readUnsignedByte();
            color[1] = winDataInputStream.readUnsignedByte();
            color[2] = winDataInputStream.readUnsignedByte();
            tempChunk.bytesRead += 3;
        }
        catch (Exception e) {
            LOG.severe(e.getMessage());
            //e.printStackTrace();
        }
        // Add the bytes read to our chunk
        chunk.bytesRead += tempChunk.bytesRead;
    } // END readColorChunk

    /**
     * This function reads in the material name assigned to the object and sets the materialID
     *
     * @param object The Object3DS
     * @param previousChunk The previous chunk.
     */
    private void readObjectMaterial(Object3DS object, Chunk previousChunk) {
        LOG.fine("readObjectMaterial");

        String materialName = null; // This is used to hold the objects material name
        // int buffer[50000] = {0};	 // This is used to read past unwanted data

        // *What is a material?*  - A material is either the color or the texture map of the object.
        // It can also hold other information like the brightness, shine, etc... Stuff we don't
        // really care about.  We just want the color, or the texture map file name really.

        // Here we read the material name that is assigned to the current object.
        // strMaterial should now have a string of the material name, like "Material #2" etc..
        try {
            materialName = winDataInputStream.readCString(64, false);
            previousChunk.bytesRead += materialName.length() + 1;
            LOG.fine("materialName: " + materialName);
        }
        catch (Exception e) {
            LOG.severe(e.getMessage());
            //e.printStackTrace();
        }

        // Now that we have a material name, we need to go through all of the materials
        // and check the name against each material.  When we find a material in our material
        // list that matches this name we just read in, then we assign the materialID
        // of the object to that material index.  You will notice that we passed in the
        // model to this function.  This is because we need the number of textures.
        // Yes though, we could have just passed in the model and not the object too.

        // Go through all of the textures
        int materialID = 0;
        for (Iterator iterator = materials.iterator(); iterator.hasNext(); materialID++) {
            MaterialInfo3DS material = (MaterialInfo3DS)iterator.next();
            // If the material we just read in matches the current texture name
            if(materialName.equals(material.name)) {
                // Set the material ID to the current index 'i' and stop checking
                LOG.fine("materialID: " + materialID);

                // Now that we found the material, check if it's a texture map.
                // If the strFile has a string length of 1 and over it's a texture
                if (material.file != null) {
                    // Set the object's flag to say it has a texture map to bind.
                    object.hasTexture = true;
                }
                break;
            } // END if(strcmp(strMaterial, pModel->pMaterials[i].strName) == 0)
        } // END for (int i = 0; i < model.numberOfMaterials; i++)

        // Read past the rest of the chunk since we don't care about shared vertices
        // You will notice we subtract the bytes already read in this chunk from the total length.
        try {
            // read number of faces using this material
            int numberOfFaces = winDataInputStream.readUnsignedShort();
            previousChunk.bytesRead += WORD;
            LOG.fine("numberOfFaces: " + numberOfFaces);
            for (int i = 0; i < numberOfFaces; i++) {
                int faceIndex = winDataInputStream.readUnsignedShort();
                previousChunk.bytesRead += WORD;
                object.faces[faceIndex].materialID = materialID;
                LOG.fine("faceIndex: " + faceIndex);
            }
            /*
            winDataInputStream.skipBytes(previousChunk.length - previousChunk.bytesRead);
            previousChunk.bytesRead += previousChunk.length - previousChunk.bytesRead;
            */
        }
        catch (Exception e) {
            LOG.severe(e.getMessage());
            //e.printStackTrace();
        }
    } // END readObjectMaterial

    /**
     * This function reads in the UV coordinates for the object.
     *
     * @param object The Object3DS.
     * @param previousChunk The previous chunk.
     */
    private void readUVCoordinates(Object3DS object, Chunk previousChunk) {
        // In order to read in the UV indices for the object, we need to first
        // read in the amount there are, then read them in.

        // Read in the number of UV coordinates there are (int)
        try {
            object.numberTextureVertices = winDataInputStream.readUnsignedShort();
            previousChunk.bytesRead += 2;
            LOG.fine("object.numberTextureVertices: " + object.numberTextureVertices);
        }
        catch (Exception e) {
            LOG.severe(e.getMessage());
            //e.printStackTrace();
        }

        // Allocate memory to hold the UV coordinates
        // pObject->pTexVerts = new tVector2 [pObject->numTexVertex];

        // Read in the texture coordinates (an array 2 float)
        for (int i = 0; i < object.numberTextureVertices; i++) {
            try {
                object.vertices[i].u = winDataInputStream.readFloat();
                object.vertices[i].v = winDataInputStream.readFloat();
            }
            catch (Exception e) {
                LOG.severe(e.getMessage());
                //e.printStackTrace();
            }

        }
        previousChunk.bytesRead += previousChunk.length - previousChunk.bytesRead;
        LOG.fine("bytesRead: " + previousChunk.bytesRead);
    } // END readUVCoordinates

    /**
     * This function reads in the indices for the vertex array.
     *
     * @param object The Object3DS.
     * @param previousChunk The previous chunk.
     */
    private void readVertexIndices(Object3DS object, Chunk previousChunk) {

        // In order to read in the vertex indices for the object, we need to first
        // read in the number of them, then read them in.  Remember,
        // we only want 3 of the 4 values read in for each face.  The fourth is
        // a visibility flag for 3D Studio Max that doesn't mean anything to us.

        // Read in the number of faces that are in this object (int)
        try {
            object.numberOfFaces = winDataInputStream.readUnsignedShort();
            previousChunk.bytesRead += 2;
            LOG.fine("NumberOfFaces: " + object.numberOfFaces);
        }
        catch (Exception e) {
            LOG.severe(e.getMessage());
            //e.printStackTrace();
        }

        // Alloc enough memory for the faces and initialize the structure
        object.faces = new Face3DS[object.numberOfFaces];
        for (int i = 0; i < object.numberOfFaces; i++)
            object.faces[i] = new Face3DS();

        // Go through all of the faces in this object
        for(int i = 0; i < object.numberOfFaces; i++) {
            // Next, we read in the A then B then C index for the face, but ignore the 4th value.
            // The fourth value is a visibility flag for 3D Studio Max, we don't care about this.
            try {
                object.faces[i].vertexIndex[0] = winDataInputStream.readUnsignedShort();
                object.faces[i].vertexIndex[1] = winDataInputStream.readUnsignedShort();
                object.faces[i].vertexIndex[2] = winDataInputStream.readUnsignedShort();
                winDataInputStream.skipBytes(2);
                previousChunk.bytesRead += 8;
            }
            catch (Exception e) {
                LOG.severe(e.getMessage());
                //e.printStackTrace();
            }
        } // END for(int i = 0; i < object.numberOfFaces; i++)
        LOG.fine("bytesRead: " + previousChunk.bytesRead);
        LOG.fine("last Face: " + object.faces[object.numberOfFaces - 1].vertexIndex[0] + "  " + object.faces[object.numberOfFaces - 1].vertexIndex[1] + "  " + object.faces[object.numberOfFaces - 1].vertexIndex[2]);
    } // END readVertexIndices

    /**
     * This function reads in the vertices for the object
     *
     * @param object The Object3DS.
     * @param previousChunk The previous chunk.
     */
    private void readVertices(Object3DS object, Chunk previousChunk) {
        LOG.fine("readVertices");
        // Like most chunks, before we read in the actual vertices, we need
        // to find out how many there are to read in.  Once we have that number
        // we then fread() them into our vertice array.

        // Read in the number of vertices (int)
        try {
            object.numberOfVertices = winDataInputStream.readUnsignedShort();
            previousChunk.bytesRead += 2;
            // System.out.println("NumberOfVertices: " + object.numberOfVertices);
        }
        catch (Exception e) {
            LOG.severe(e.getMessage());
            //e.printStackTrace();
        }
        // Allocate the memory for the verts and initialize the structure
        object.vertices = new Vertex3DTex[object.numberOfVertices];
        for (int i = 0; i < object.numberOfVertices; i++)
            object.vertices[i] = new Vertex3DTex();

        // Read in the array of vertices (an array of 3 floats)
        for (int i = 0; i < object.numberOfVertices; i++) {
            try {
                object.vertices[i].x = winDataInputStream.readFloat();
                object.vertices[i].y = winDataInputStream.readFloat();
                object.vertices[i].z = winDataInputStream.readFloat();
            }
            catch (Exception e) {
                LOG.severe(e.getMessage());
                //e.printStackTrace();
            }
        }
        previousChunk.bytesRead += previousChunk.length - previousChunk.bytesRead;
        LOG.fine("bytesRead: " + previousChunk.bytesRead);
        LOG.fine("last Vertex: " + object.vertices[object.numberOfVertices - 1].x + "  " + object.vertices[object.numberOfVertices - 1].y + "  " + object.vertices[object.numberOfVertices - 1].z);
    } // END readVertices

    /**
     * @see jd.engine.model.JDModel#setState(int)
     */
    public void setState(int modelState) {
        /* This method is needed to support the JDModel interface. Because the JD3dsModelLoader,
           doesn't support keyframe animation sofar, the method is empty. */
    } // END setState

    /**
     * @see jd.engine.model.JDModel#getNumberOfPolygons()
     */
    public int getNumberOfPolygons() {
        return 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "\nmodelName: " + meshName +
               "\nobjects.size(): " + objects.size() +
               "\nobjects" + objects +
               "\nmaterials.size(): " + materials.size() +
               "\nmaterials: " + materials +
               "\nfileVersion: " + fileVersion;
    }

    //	Inner classes
    //	Inner classes
    //	Inner classes
    //	Inner classes

    /**
     * This holds the chunk info
     */
    private class Chunk {

        /** The chunk's ID */
        public int ID;

        /** The length of the chunk */
        public int length;

        /** The amount of bytes read within that chunk */
        public int bytesRead;
    } // END class Chunk

    /**
     * This is our face structure.  This is is used for indexing into the vertex
     * and texture coordinate arrays.  From this information we know which vertices
     * from our vertex array go to which face, along with the correct texture coordinates.
     */
    public class Face3DS {

        /** indicies for the verts that make up this triangle */
        public int[] vertexIndex;

        /** indicies for the tex coords to texture this face */
        public int[] coordIndex;

        /** The id of the material */
        public int materialID;

        /**
         * Constructs a Face3DS.
         */
        public Face3DS() {
            vertexIndex = new int[3];
            coordIndex = new int[3];
        } // END constructor Face3DS
    } // END class Face3DS

    /**
     * This holds the information for a material.  It may be a texture map of a color.
     * Some of these are not used, but I left them because you will want to eventually
     * read in the UV tile ratio and the UV tile offset for some models.
     */
    public static class MaterialInfo3DS {

        /** The texture name */
        public String name;

        /** The texture file name (If this is set it's a texture map) */
        public String file;

        /** The color of the object (R, G, B) */
        public int[] color;

        /** The texture */
        public Object texture;   //set by renderer

        /** u tiling of texture (Currently not used) */
        public float uTile;

        /** v tiling of texture (Currently not used) */
        public float vTile;

        /** u offset of texture (Currently not used) */
        public float uOffset;

        /** v offset of texture (Currently not used) */
        public float vOffset;

        /**
         * Constructs MaterialInfo3DS.
         */
        public MaterialInfo3DS(){
            color = new int[3];
        } // END constructor MaterialInfo3DS

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return "\nname: " + name +
                   "\nfile: " + file +
                   "\ncolor: " + Arrays.toString(color);
        }
    } // END class MaterialInfo3DS
    public class Vertex3DTex {
    	public float x, y, z, u, v;
    	public Vertex3DTex(float ax, float by, float cz) {
    		x=ax; y=by; z=cz; u=v=0;
    	}
    	public Vertex3DTex() {x=y=z=u=v=0;}
    	public Vertex3DTex(Vertex3DTex in) {x=in.x; y=in.y; z=in.z; u=in.u; v=in.v;}
    }
    public class Vertex3D {
    	public float x, y, z;
    	public Vertex3D(float ax, float by, float cz) {
    		x=ax; y=by; z=cz;
    	}
    	public void set(Vertex3DTex v) {
			x=v.x; y=v.y; z=v.z;
		}
		public Vertex3D() {x=y=z=0;}
    	public Vertex3D(Vertex3D in) {x=in.x; y=in.y; z=in.z;}
    	public final Vertex3D sub(Vertex3D v1, Vertex3D v2) {
            Vertex3D result = new Vertex3D();
            result.x = v1.x - v2.x;
            result.y = v1.y - v2.y;
            result.z = v1.z - v2.z;
            return result;
        } // END sub
    	public final Vertex3D cross(Vertex3D v1, Vertex3D v2) {
            Vertex3D result = new Vertex3D();
            result.x = (v1.y * v2.z) - (v1.z * v2.y);
            result.y = (v1.z * v2.x) - (v1.x * v2.z);
            result.z = (v1.x * v2.y) - (v1.y * v2.x);
            return result;
        } // END cross
    	public void add(Vertex3D v2){
            this.x += v2.x;
            this.y += v2.y;
            this.z += v2.z;
        } // END add
    	public void normalize() {
            float length = (float)Math.sqrt(x * x + y * y + z * z);

            // avoid division by zero
            if (length == 0) length=0.0001f;
            x = x / length;
            y = y / length;
            z = z / length;
        } // END normalize
    	public void divideByScalar(float scalar){
            x = x / scalar;
            y = y / scalar;
            z = z / scalar;
        } // END divideByScalar
    }
    /**
     * This holds all the information for our model/scene.
     * You should eventually turn into a robust class that
     * has loading/drawing/querying functions like:
     * LoadModel(...); DrawObject(...); DrawModel(...); DestroyModel(...);
     */
    public class Object3DS {

        /** The name of the object */
        public String name;

        /** The number of verts in the model */
        public int  numberOfVertices;

        /** The number of faces in the model*/
        public int  numberOfFaces;

        /** The number of texture coordinates */
        public int  numberTextureVertices;

        /** This is TRUE if there is a texture map for this object */
        public boolean hasTexture;

        /** The object's vertices */
        public Vertex3DTex[] vertices;

        /** The object's normals */
        public Vertex3D[] normals;
        // public tVector2  *pTexVerts; // The texture's UV coordinates

        /** The faces information of the object */
        public Face3DS[] faces;

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            String output = "\nname: " + name +                            "\nnumberOfVertices: " + numberOfVertices +
                            "\nnumberOfFaces: " + numberOfFaces +
                            "\nnumberTextureVertices: " + numberTextureVertices +
                            "\nhasTexture: " + hasTexture +
                            "\ncoords:";
            /*for (int i = 0; i < vertices.length; i++) {
                output += "\n[" + vertices[i].x + " / " + vertices[i].y + " / " + vertices[i].z + "] [" + vertices[i].u + " / " + vertices[i].v + "]";
            }*/
            return output;
        }
    } // END class Object3DS

    //	END Inner classes
    //	END Inner classes
    //	END Inner classes
    //	END Inner classes
    public static void main(String args[]) {
    	ff_3DSModel m=new ff_3DSModel(new File("data/pottery.3ds"));
    	System.out.println(m);
    }
} // END JD3dsModel