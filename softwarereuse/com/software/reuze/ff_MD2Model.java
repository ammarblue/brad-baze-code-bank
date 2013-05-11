package com.software.reuze;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.logging.Logger;




/**
 * This class keeps track of a md2 model (current frame, current state) etc. While
 * the model data itself is stored in the JDmd2ModelLoader.
 */
public class ff_MD2Model  {
    /*
       Frame#  Action
       ----------------
       0-39    stand
       40-45   run
       46-53   attack
       54-57   pain1
       58-61   pain2
       62-65   pain3
       66-71   jump
       72-83   flip
       84-94   salute
       95-111  taunt
       112-122 wave
       123-134 point
       135-153 creep stand
       154-159 creep walk
       160-168 creep attack
       169-172 creep pain
       173-177 creep death
       178-183 death1
       184-189 death2
       190-197 death3
    */
    /** Maximum number of states of an md2 model */
    public static final int MAX_NUMBER_OF_STATES = 22;

    /** Model does nothing (no animation) */
    public static final int STATE_NOTHING = 0;

    /** Standing */
    public static final int STATE_STAND = 1;

    /** Running */
    public static final int STATE_RUN = 2;

    /** Attacking */
    public static final int STATE_ATTACK = 3;

    /** Having pain first animation */
    public static final int STATE_PAIN_1 = 4;

    /** Having pain second animation */
    public static final int STATE_PAIN_2 = 5;

    /** Having pain third animation */
    public static final int STATE_PAIN_3 = 6;

    /** Jumping */
    public static final int STATE_JUMP = 7;

    /** Flipping */
    public static final int STATE_FLIP = 8;

    /** Saluting */
    public static final int STATE_SALUTE = 9;

    /** Taunting */
    public static final int STATE_TAUNTE = 10;

    /** Waving */
    public static final int STATE_WAVE = 11;

    /** Pointing */
    public static final int STATE_POINT = 12;

    /** Stand while creeping */
    public static final int STATE_CREEP_STAND = 13;

    /** Walk while creeping */
    public static final int STATE_CREEP_WALK = 14;

    /** Attack while creeping */
    public static final int STATE_CREEP_ATTACK = 15;

    /** Pain while creeping */
    public static final int STATE_CREEP_PAIN = 16;

    /** Dying while creeping */
    public static final int STATE_CREEP_DEATH = 17;

    /** Dying first animation */
    public static final int STATE_DEATH_1 = 18;

    /** Dying second animation */
    public static final int STATE_DEATH_2 = 19;

    /** Dying third animation */
    public static final int STATE_DEATH_3 = 20;

    /** Dead (no animation) */
    public static final int STATE_DEAD = 21;

    /** logging */
    private static final Logger LOG = Logger.getLogger(ff_MD2Model.class.getName());

    public int numFrames;                      // number of frames
    public int numPoints;                      // number of vertices
    public int numTriangles;                   // number of triangles
    public int numST;                          // number of texture coordinates
    public int frameSize;                      // size of each frame in bytes
    public int currentFrame;                   // current frame number in animation
    public int nextFrame;                      // next frame number in animation
    public float interpolation;                // percent through current frame
    public Mesh[] triIndex;                    // triangle list
    public TextureCoordinate[] textureCoordinates; // texture coordinate list
    public float[] pointList;               // vertex list
    //public md2Texture modelTexture;               // texture data
    public float[] vertex;                // 3 xyz vertices for calculating normal

    private ByteBuffer byteBuffer;
    public FloatBuffer normalBuffer;
    public FloatBuffer texCoordBuffer;
    public FloatBuffer vertexBuffer;

    private String meshName;
    private int textureID=-1;
    public int getTextureID() {
		return textureID;
	}
    public final static int X=0, Y=1, Z=2;
    public float getXYZ(float[] array, int index, int XYZ) { //array of XYZ
    	return array[index*3+XYZ];
    }
    public void setXYZ(float[] array, int index, int XYZ, float value) { //array of XYZ
    	array[index*3+XYZ]=value;
    }
	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}

	private int modelState;
    private int stateStart;
    private int stateEnd;

    private boolean animationCompleted;

    /**
     * This constructor really loads the model in memory
     *
     * @param meshFile The file containing the meshes.
     * @param skinFile The file containing the skin texture.
     */
    public ff_MD2Model(File meshFile, File skinFile) {

        vertex = new float[3*3];

        meshName = meshFile.getName().substring(0, meshFile.getName().length() - 4);
        LOG.fine("meshName: " + meshName);

        // construct textureFile
        LOG.fine("meshFile: " + meshFile);
        LOG.fine("skinFile: " + skinFile);

        // load model file in buffer
        FileInputStream fileInputStream;
        byte[] buffer = null;  // holds whole model file

        // MD2ModelHeader
        f_LittleEndianDataInputStream bufferWinDataInputStream;
        MD2ModelHeader modelHeader = null;

        // fill pointList
        int frameOffset;
        Frame frame;
        byte[] frameHeaderBuffer;
        byte[] frameDataBuffer;
        int pointListPointer;
        f_LittleEndianDataInputStream frameDataWinDataInputStream;

        // fill texture coordinates
        int textureCoordinatesOffset;
        byte[] textureCoordinatesDataBuffer;
        f_LittleEndianDataInputStream textureCoordinatesWinDataInputStream;

        // fill triIndex
        int triOffset;
        byte[] triDataBuffer;
        f_LittleEndianDataInputStream triDataWinDataInputStream;

        // read the file in byte buffer and close it
        try {
            fileInputStream = new FileInputStream(meshFile);
            buffer = new byte[(int)meshFile.length()];
            fileInputStream.read(buffer, 0, (int)meshFile.length());
            fileInputStream.close();
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }

        // read model header
        try {
            bufferWinDataInputStream = new f_LittleEndianDataInputStream(new ByteArrayInputStream(buffer));

            modelHeader = new MD2ModelHeader();
            modelHeader.loadMD2ModelHeader(bufferWinDataInputStream);
            // modelHeader.printMD2ModelHeader();
        }
        catch (Exception e) {
            LOG.severe(e.getMessage());
        }

        // store model header data in model
        numPoints = modelHeader.getNumberOfPoints();
        numFrames = modelHeader.getNumberOfFrames();
        numTriangles = modelHeader.getNumberOfTriangles();
        frameSize = modelHeader.getFrameSize();
        numST = modelHeader.getNumberOfTextureCoordinates();

        // filling the point list
        // allocate memory fo all vertices used in model, including animations
        pointList = new float[numPoints * numFrames*3];

        // temp frame to hold frame header information
        frame = new Frame();
        // buffer for holding frame header infomation
        frameHeaderBuffer = new byte[40]; // 40 is the size of the structure frame_t
        // buffer for holding frame data
        frameDataBuffer = new byte[numPoints * 4];
        float textureWidth=64f, textureHeight=128f;
/*        if (skinFile!=null)
        try {
            modelTexture = JDTextureManager.getTexture(skinFile);
            textureHeight=(float)modelTexture.getHeight();
            textureWidth=(float)modelTexture.getWidth();
        }
        catch (JDTextureFormatException jdTextureFormatException) {
            LOG.severe("*** Unknown texture format");
            LOG.severe(jdTextureFormatException.getMessage());
        }
        catch (IOException ioException) {
            LOG.severe("*** Can't access texture file");
            LOG.severe(ioException.getMessage());
        }
        LOG.fine("modelTexture: " + modelTexture);*/
        // loop number of frames in model file
        for (int j = 0; j < numFrames; j++) {
            // calculate frame offset
            frameOffset = modelHeader.getOffsetFrames() + (frameSize * j);
            // copy frame header from buffer in single frame buffer
            System.arraycopy(buffer, frameOffset, frameHeaderBuffer, 0, 40);
            // copy frame data from buffer to frame point buffer
            System.arraycopy(buffer, frameOffset + 40, frameDataBuffer, 0, numPoints * 4);
            // creating WinDataInputStream for reading in frame points
            frameDataWinDataInputStream = new f_LittleEndianDataInputStream(new ByteArrayInputStream(frameDataBuffer));
            // loading frame header information
            frame.loadFrameHeader(new f_LittleEndianDataInputStream(new ByteArrayInputStream(frameHeaderBuffer)));
            // frame.printSingleFrame();

            // calculate the point positions based on frame details
            pointListPointer = modelHeader.getNumberOfPoints() * j;
            for (int i = 0; i < modelHeader.getNumberOfPoints(); i++) {
                try {
                    // read frame points and put them in pointList
                    pointList[pointListPointer*3+X] = frame.getScale(0) * frameDataWinDataInputStream.readUnsignedByte() + frame.getTranslate(0);
                    pointList[pointListPointer*3+Y] = frame.getScale(1) * frameDataWinDataInputStream.readUnsignedByte() + frame.getTranslate(1);
                    pointList[pointListPointer*3+Z] = frame.getScale(2) * frameDataWinDataInputStream.readUnsignedByte() + frame.getTranslate(2);

                    // throw away normalIndex
                    frameDataWinDataInputStream.readByte();
                    pointListPointer++;
                }
                catch (Exception e) {
                    LOG.severe(e.getMessage());
                }
            }
        }

        // allocate memory for the model texture coordinates
        textureCoordinates = new TextureCoordinate[numST];
        for (int i = 0; i < numST; i++)
            textureCoordinates[i] = new TextureCoordinate();

        // calculate offset of texture coordinate data
        textureCoordinatesOffset = modelHeader.getOffsetTextureCoordinates();
        // LOG.fine(textureCoordinatesOffset);
        // create buffer for texture coordinates data
        textureCoordinatesDataBuffer = new byte[numST * 4];
        // copy texture coordinate data from model file buffer
        System.arraycopy(buffer, textureCoordinatesOffset, textureCoordinatesDataBuffer, 0, numST * 4);
        textureCoordinatesWinDataInputStream = new f_LittleEndianDataInputStream(new ByteArrayInputStream(textureCoordinatesDataBuffer));

        // calculate and store the texture coordinates for the model
        for (int i = 0; i < numST; i++) {
            try {
                textureCoordinates[i].s = textureCoordinatesWinDataInputStream.readUnsignedShort() / textureWidth;
                textureCoordinates[i].t = textureCoordinatesWinDataInputStream.readUnsignedShort() / textureHeight;
            }
            catch (Exception e) {
                LOG.severe(e.getMessage());
                //e.printStackTrace();
            }
        }

        // fill list of triangles with mesh- and stIndex data
        // allocate the list of triangle indeces
        triIndex = new Mesh[numTriangles];
        for (int i = 0; i < triIndex.length; i++)
            triIndex[i] = new Mesh();

        // calculate offset of triangle data
        triOffset = modelHeader.getOffsetTriangles();
        // create buffer for triangle data
        triDataBuffer = new byte[numTriangles * 12];
        // copy triangle data from model file buffer
        System.arraycopy(buffer, triOffset, triDataBuffer, 0, numTriangles * 12);
        triDataWinDataInputStream = new f_LittleEndianDataInputStream(new ByteArrayInputStream(triDataBuffer));

        // loop through triangles
        for(int i = 0; i < numTriangles; i++) {
            // store the mesh and texture indices
            try {
                triIndex[i].meshIndex[0] = triDataWinDataInputStream.readUnsignedShort();
                triIndex[i].meshIndex[1] = triDataWinDataInputStream.readUnsignedShort();
                triIndex[i].meshIndex[2] = triDataWinDataInputStream.readUnsignedShort();
                triIndex[i].stIndex[0] = triDataWinDataInputStream.readUnsignedShort();
                triIndex[i].stIndex[1] = triDataWinDataInputStream.readUnsignedShort();
                triIndex[i].stIndex[2] = triDataWinDataInputStream.readUnsignedShort();
            }
            catch (Exception e) {
                LOG.severe(e.getMessage());
                //e.printStackTrace();
            }
        }
        // initialize animation variables
        currentFrame = 0;
        nextFrame = 1;
        interpolation = 0.0f;
        modelState = STATE_NOTHING;

        // setup display buffers
        int sizeNormalBuffer =   numTriangles * 3 * 3 * 4;
        int sizeTexCoordBuffer = numTriangles * 3 * 2 * 4;
        int sizeVertexBuffer =   numTriangles * 3 * 3 * 4;

        // creating direct byte buffer
        ByteBuffer tempByteBuffer = ByteBuffer.allocateDirect(sizeNormalBuffer + sizeTexCoordBuffer + sizeVertexBuffer);
        // set direct byte buffer to native byte order
        byteBuffer = tempByteBuffer.order(ByteOrder.nativeOrder());

        // creating normalBuffer as view buffer
        byteBuffer.limit(sizeNormalBuffer);
        normalBuffer = byteBuffer.asFloatBuffer();

        // creating texCoordBuffer as view buffer
        byteBuffer.limit(sizeNormalBuffer + sizeTexCoordBuffer);
        byteBuffer.position(sizeNormalBuffer);
        texCoordBuffer = byteBuffer.asFloatBuffer();

        // creating vertexBuffer as view buffer
        byteBuffer.limit(sizeNormalBuffer + sizeTexCoordBuffer + sizeVertexBuffer);
        byteBuffer.position(sizeNormalBuffer + sizeTexCoordBuffer);
        vertexBuffer = byteBuffer.asFloatBuffer();
    } // END constructor JDmd2Model(GLDrawable drawable, File modelFile)

    /**
     * The model data is already in memory, so this constructor uses references to
     * this data and doesn't load the model again.
     *
     * @param model The model to reference.
     */
    public ff_MD2Model(ff_MD2Model model) {

        vertex = new float[3*3];

        meshName = model.getName();

        pointList = model.pointList;
        textureCoordinates = model.textureCoordinates;
        triIndex = model.triIndex;
        //modelTexture = model.modelTexture;

        numFrames = model.numFrames;
        numPoints = model.numPoints;
        numTriangles = model.numTriangles;

        // initialize animation variables
        currentFrame = 0;
        nextFrame = 1;
        interpolation = 0.0f;
        modelState = STATE_NOTHING;

        // setup display buffers
        int sizeNormalBuffer =   numTriangles * 3 * 3 * 4;
        int sizeTexCoordBuffer = numTriangles * 3 * 2 * 4;
        int sizeVertexBuffer =   numTriangles * 3 * 3 * 4;

        ByteBuffer tempByteBuffer = ByteBuffer.allocateDirect(sizeNormalBuffer + sizeTexCoordBuffer + sizeVertexBuffer);
        byteBuffer = tempByteBuffer.order(ByteOrder.nativeOrder());

        // creating normalBuffer as view buffer
        byteBuffer.limit(sizeNormalBuffer);
        normalBuffer = byteBuffer.asFloatBuffer();

        // creating texCoordBuffer as view buffer
        byteBuffer.limit(sizeNormalBuffer + sizeTexCoordBuffer);
        byteBuffer.position(sizeNormalBuffer);
        texCoordBuffer = byteBuffer.asFloatBuffer();

        // creating vertexBuffer as view buffer
        byteBuffer.limit(sizeNormalBuffer + sizeTexCoordBuffer + sizeVertexBuffer);
        byteBuffer.position(sizeNormalBuffer + sizeTexCoordBuffer);
        vertexBuffer = byteBuffer.asFloatBuffer();
    } // END constructor JDmd2Model(JDmd2Model)

    //         enable texturing
    //gl.glEnable(GL.GL_TEXTURE_2D);
    //gl.glBindTexture(GL.GL_TEXTURE_2D, getTextureID());
    //animate(...);
    //gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
    //gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
    //gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
    //gl.glNormalPointer(GL.GL_FLOAT, 0, normalBuffer.rewind());
    //gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, texCoordBuffer.rewind());
    //gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer.rewind());
    //gl.glDrawArrays(GL.GL_TRIANGLES, 0, numTriangles * 3);
    private void animate(float percent) {
        int pointListIndex;			// current frame vertices
        int nextPointListIndex;		// next frame vertices
        float x1, y1, z1;			// current frame point values
        float x2, y2, z2;			// next frame point values

        if ((stateStart < 0) || (stateEnd < 0)) return;
        if ((stateStart >= numFrames) || (stateEnd >= numFrames)) return;
        if (stateStart > currentFrame) currentFrame = stateStart;


        if (interpolation >= 1.0) {
            interpolation = 0.0f;
            currentFrame++;
            if (currentFrame >= stateEnd) currentFrame = stateStart;
            nextFrame = currentFrame + 1;
            if (nextFrame >= stateEnd) {
                nextFrame = stateStart;
                animationCompleted = true;
            }
        }

        pointListIndex = numPoints * currentFrame;
        nextPointListIndex = numPoints * nextFrame;

        // gl.glBegin(GL_TRIANGLES);
        for (int i = 0; i < numTriangles; i++) {
            // get first points of each frame
            x1 = pointList[(pointListIndex + triIndex[i].meshIndex[0])*3+X];
            y1 = pointList[(pointListIndex + triIndex[i].meshIndex[0])*3+Y];
            z1 = pointList[(pointListIndex + triIndex[i].meshIndex[0])*3+Z];
            x2 = pointList[(nextPointListIndex + triIndex[i].meshIndex[0])*3+X];
            y2 = pointList[(nextPointListIndex + triIndex[i].meshIndex[0])*3+Y];
            z2 = pointList[(nextPointListIndex + triIndex[i].meshIndex[0])*3+Z];

            // store first interpolated vertex of triangle
            vertex[0*3+X]= x1 + interpolation * (x2 - x1);
            vertex[0*3+Y]= y1 + interpolation * (y2 - y1);
            vertex[0*3+Z]= z1 + interpolation * (z2 - z1);

            // get second points of each frame
            x1 = pointList[(pointListIndex + triIndex[i].meshIndex[2])*3+X];
            y1 = pointList[(pointListIndex + triIndex[i].meshIndex[2])*3+Y];
            z1 = pointList[(pointListIndex + triIndex[i].meshIndex[2])*3+Z];
            x2 = pointList[(nextPointListIndex + triIndex[i].meshIndex[2])*3+X];
            y2 = pointList[(nextPointListIndex + triIndex[i].meshIndex[2])*3+Y];
            z2 = pointList[(nextPointListIndex + triIndex[i].meshIndex[2])*3+Z];

            // store second interpolated vertex of triangle
            vertex[2*3+X]= x1 + interpolation * (x2 - x1);
            vertex[2*3+Y]= y1 + interpolation * (y2 - y1);
            vertex[2*3+Z]= z1 + interpolation * (z2 - z1);

            // get third points of each frame
            x1 = pointList[(pointListIndex + triIndex[i].meshIndex[1])*3+X];
            y1 = pointList[(pointListIndex + triIndex[i].meshIndex[1])*3+Y];
            z1 = pointList[(pointListIndex + triIndex[i].meshIndex[1])*3+Z];
            x2 = pointList[(nextPointListIndex + triIndex[i].meshIndex[1])*3+X];
            y2 = pointList[(nextPointListIndex + triIndex[i].meshIndex[1])*3+Y];
            z2 = pointList[(nextPointListIndex + triIndex[i].meshIndex[1])*3+Z];

            // store third interpolated vertex of triangle
            vertex[1*3+X]= x1 + interpolation * (x2 - x1);
            vertex[1*3+Y]= y1 + interpolation * (y2 - y1);
            vertex[1*3+Z]= z1 + interpolation * (z2 - z1);

            // calculate the normal of the triangle
            calculateNormal(i, vertex, 0, 2*3, 1*3);  // 0 2 1

            // render properly textured triangle
            texCoordBuffer.position(i * 6);
            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[0]].s);
            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[0]].t);

            vertexBuffer.position(i * 9);
            vertexBuffer.put(vertex,0*3,3);

            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[2]].s);
            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[2]].t);

            vertexBuffer.put(vertex, 2*3, 3);

            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[1]].s);
            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[1]].t);

            vertexBuffer.put(vertex, 1*3, 3);
        }

        // increase percentage of interpolation between frames
        interpolation = interpolation + percent;
    } // END animate

    public void calculateNormal(int index, float[] source, int p1, int p2, int p3) {
        float ax,by,cz, rx,sy,tz, mx,ny,oz;

        ax = source[p1+X]-source[p2+X]; //p1-p2
        by = source[p1+Y]-source[p2+Y]; //p1-p2
        cz = source[p1+Z]-source[p2+Z]; //p1-p2
        
        rx = source[p1+X]-source[p3+X]; //p1-p3
        sy = source[p1+Y]-source[p3+Y]; //p1-p3
        tz = source[p1+Z]-source[p3+Z]; //p1-p3

                                      //abc cross rst
        mx = (by * tz) - (cz * sy);
        ny = (cz * rx) - (ax * tz);
        oz = (ax * sy) - (by * rx);

        // getlength
        float length=(float)Math.sqrt(mx * mx + ny * ny + oz * oz);
        // normalize and specify the normal
        // avoid division by zero
        if (length == 0) length=0.0001f;
        mx /= length;
        ny /= length;
        oz /= length;

       // because we use glDrawArrays() later we need a normal for each vertex,
       // not only one per triangle
       normalBuffer.position(index * 9);
       normalBuffer.put(mx).put(ny).put(oz);
       normalBuffer.put(mx).put(ny).put(oz);
       normalBuffer.put(mx).put(ny).put(oz);
    } // END calculateNormal

    /**
     * @see jd.engine.model.JDModel#render(float)
     */
    public void render(float percent) {
        if (modelState == STATE_NOTHING) {
            renderFrame(0);
            return;
        }
        if (modelState == STATE_DEAD) {
            if (183 < numFrames) renderFrame(183);
            return;
        }
        if (stateEnd < numFrames) {
            animate(percent);
        }
    } // END drawModel

    /**
     * @see jd.engine.model.JDModel#getMaximumNumberOfStates()
     */
    public int getMaximumNumberOfStates() {
        return MAX_NUMBER_OF_STATES;
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
        return nextFrame;
    } // END getNextFrame

    /**
     * @see jd.engine.model.JDModel#getStateEndFrame()
     */
    public int getStateEndFrame() {
        return stateEnd;
    } // ENG getStateEndFrame

    /**
     * @see jd.engine.model.JDModel#getState()
     */
    public int getState() {
        return modelState;
    } // END getState

    /**
     * Tells if animation sequence completed once.
     *
     * @return - true animation sequence completed.
     */
    public boolean isAnimationCompleted() {
        return animationCompleted;
    } // END isAnimationCompleted

    /**
     * Renders a single frame.
     *
     * @param frameIndex The index of the frame to render.
     */
    // enable texturing
    //gl.glEnable(GL.GL_TEXTURE_2D);
    // set the texture
    //gl.glBindTexture(GL.GL_TEXTURE_2D, getTextureID());
    //renderFrame(...);
    //gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
    //gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
    //gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
    //gl.glNormalPointer(GL.GL_FLOAT, 0, normalBuffer);
    //gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, texCoordBuffer);
    //gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer);
    //gl.glDrawArrays(GL.GL_TRIANGLES, 0, numTriangles * 3);
    private void renderFrame(int frameIndex) {

        // calculate frame offset for point list
        int pointListIndex = numPoints * frameIndex;

        // gl.glBegin(GL.GL_TRIANGLES);
        for (int i = 0; i < numTriangles; i++) {

            calculateNormal(i,
                            pointList, (pointListIndex + triIndex[i].meshIndex[0])*3,
                                       (pointListIndex + triIndex[i].meshIndex[2])*3,
                                       (pointListIndex + triIndex[i].meshIndex[1])*3);

            texCoordBuffer.position(i * 6);
            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[0]].s);
            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[0]].t);

            vertexBuffer.position(i * 9);
            vertexBuffer.put(pointList, (pointListIndex + triIndex[i].meshIndex[0])*3, 3);

            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[2]].s);
            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[2]].t);

            vertexBuffer.put(pointList, (pointListIndex + triIndex[i].meshIndex[2])*3, 3);

            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[1]].s);
            texCoordBuffer.put(textureCoordinates[triIndex[i].stIndex[1]].t);

            vertexBuffer.put(pointList, (pointListIndex + triIndex[i].meshIndex[1])*3, 3);
        }
    } // END renderFrame
    public void putNormal(float[] f, int index, float[] source, int p1, int p2, int p3) {
        float ax,by,cz, rx,sy,tz, mx,ny,oz;

        ax = source[p1+X]-source[p2+X]; //p1-p2
        by = source[p1+Y]-source[p2+Y]; //p1-p2
        cz = source[p1+Z]-source[p2+Z]; //p1-p2
        
        rx = source[p1+X]-source[p3+X]; //p1-p3
        sy = source[p1+Y]-source[p3+Y]; //p1-p3
        tz = source[p1+Z]-source[p3+Z]; //p1-p3

                                      //abc cross rst
        mx = (by * tz) - (cz * sy);
        ny = (cz * rx) - (ax * tz);
        oz = (ax * sy) - (by * rx);

        // getlength
        float length=(float)Math.sqrt(mx * mx + ny * ny + oz * oz);
        // normalize and specify the normal
        // avoid division by zero
        if (length == 0) length=0.0001f;
        mx /= length;
        ny /= length;
        oz /= length;

        // because we use glDrawArrays() later we need a normal for each vertex,
        // not only one per triangle
        f[index]=f[index+8]=f[index+16]=mx;
        f[index+1]=f[index+9]=f[index+17]=ny;
        f[index+2]=f[index+10]=f[index+18]=oz;
     } // END calculateNormal
    public float[] getVertices(int frameIndex) {

        // calculate frame offset for point list
        int pointListIndex = numPoints * frameIndex;
        float f[]=new float[numTriangles*(3*3+3*3+3*2)];
        for (int i = 0; i < numTriangles; i++) {
        	int j=i*24;
            f[j]=pointList[(pointListIndex + triIndex[i].meshIndex[0])*3+X];
            f[j+1]=pointList[(pointListIndex + triIndex[i].meshIndex[0])*3+Y];
            f[j+2]=pointList[(pointListIndex + triIndex[i].meshIndex[0])*3+Z];
            putNormal(f,j+3,
                    pointList, (pointListIndex + triIndex[i].meshIndex[0])*3,
                               (pointListIndex + triIndex[i].meshIndex[2])*3,
                               (pointListIndex + triIndex[i].meshIndex[1])*3);

            f[j+6]=textureCoordinates[triIndex[i].stIndex[0]].s;
            f[j+7]=textureCoordinates[triIndex[i].stIndex[0]].t;

            f[j+8]=pointList[(pointListIndex + triIndex[i].meshIndex[2])*3+X];
            f[j+9]=pointList[(pointListIndex + triIndex[i].meshIndex[2])*3+Y];
            f[j+10]=pointList[(pointListIndex + triIndex[i].meshIndex[2])*3+Z];

            f[j+14]=textureCoordinates[triIndex[i].stIndex[2]].s;
            f[j+15]=textureCoordinates[triIndex[i].stIndex[2]].t;

            f[j+16]=pointList[(pointListIndex + triIndex[i].meshIndex[1])*3+X];
            f[j+17]=pointList[(pointListIndex + triIndex[i].meshIndex[1])*3+Y];
            f[j+18]=pointList[(pointListIndex + triIndex[i].meshIndex[1])*3+Z];
            f[j+22]=textureCoordinates[triIndex[i].stIndex[1]].s;
            f[j+23]=textureCoordinates[triIndex[i].stIndex[1]].t;
        }
        return f;
    } // END getVertices
    /**
     * @see jd.engine.model.JDModel#setState(int)
     */
    public void setState(int modelState) {
        animationCompleted = false;
        this.modelState = modelState;
        switch (modelState) {
            case STATE_NOTHING:
                stateStart = 0;
                stateEnd = 1;
            break;
            case STATE_STAND:
                stateStart = 0;
                stateEnd = 40;
            break;
            case STATE_RUN:
                stateStart = 40;
                stateEnd = 46;
            break;
            case STATE_ATTACK:
                stateStart = 46;
                stateEnd = 54;
            break;
            case STATE_PAIN_1:
                stateStart = 54;
                stateEnd = 58;
            break;
            case STATE_PAIN_2:
                stateStart = 58;
                stateEnd = 61;
            break;
            case STATE_PAIN_3:
                stateStart = 62;
                stateEnd = 66;
            break;
            case STATE_JUMP:
                stateStart = 66;
                stateEnd = 72;
            break;
            case STATE_FLIP:
                stateStart = 72;
                stateEnd = 84;
            break;
            case STATE_SALUTE:
                stateStart = 84;
                stateEnd = 95;
            break;
            case STATE_TAUNTE:
                stateStart = 95;
                stateEnd = 112;
            break;
            case STATE_WAVE:
                stateStart = 112;
                stateEnd = 123;
            break;
            case STATE_POINT:
                stateStart = 123;
                stateEnd = 135;
            break;
            case STATE_CREEP_STAND:
                stateStart = 135;
                stateEnd = 154;
            break;
            case STATE_CREEP_WALK:
                stateStart = 154;
                stateEnd = 160;
            break;
            case STATE_CREEP_ATTACK:
                stateStart = 160;
                stateEnd = 169;
            break;
            case STATE_CREEP_PAIN:
                stateStart = 169;
                stateEnd = 173;
            break;
            case STATE_CREEP_DEATH:
                stateStart = 173;
                stateEnd = 178;
            break;
            case STATE_DEATH_1:
                stateStart = 178;
                stateEnd = 184;
            break;
            case STATE_DEATH_2:
                stateStart = 184;
                stateEnd = 190;
            break;
            case STATE_DEATH_3:
                stateStart = 190;
                stateEnd = 198;
            break;
        } // END switch (modelState)
    } // END setState

    /**
     * @see jd.engine.model.JDModel#getNumberOfPolygons()
     */
    public int getNumberOfPolygons() {
        return numTriangles;
    }

    //	Inner classes
    //	Inner classes
    //	Inner classes
    //	Inner classes

    private class MD2ModelHeader {
        private String identity;    //identifies as MD2 file "IDP2"
        private int version;        // shoud be equals to 8
        private int skinwidth;      // width of texture
        private int skinheight;     // height of texture
        private int framesize;       // number of bytes per frame
        private int numSkins;       // number of textures
        private int numXYZ;         // number of points
        private int numST;          // number of texture coordinates
        private int numTris;        // number of triangles
        private int numGLcmds;      // number of OpenGL command types
        private int numFrames;      // total number of frames
        private int offsetSkins;    // offset to skin names (64 bytes each)
        private int offsetST;       // offset of texture s-t values
        private int offsetTris;     // offset of triangle mesh
        private int offsetFrames;   // offset of frame data (points)
        private int offsetGLcmds;   // type of OpenGL commands to use
        private int offsetEnd;      // end of file

        /**
         * Loads the md2 head from the given input stream.
         *
         * @param winDataInputStream The input stream to read from.
         */
        public void loadMD2ModelHeader(f_LittleEndianDataInputStream winDataInputStream) {
            try {
                identity = winDataInputStream.readCString(4, true);
                version = winDataInputStream.readInt();
                skinwidth = winDataInputStream.readInt();
                skinheight = winDataInputStream.readInt();
                framesize = winDataInputStream.readInt();
                numSkins = winDataInputStream.readInt();
                numXYZ = winDataInputStream.readInt();
                numST = winDataInputStream.readInt();
                numTris = winDataInputStream.readInt();
                numGLcmds = winDataInputStream.readInt();
                numFrames = winDataInputStream.readInt();
                offsetSkins = winDataInputStream.readInt();
                offsetST = winDataInputStream.readInt();
                offsetTris = winDataInputStream.readInt();
                offsetFrames = winDataInputStream.readInt();
                offsetGLcmds = winDataInputStream.readInt();
                offsetEnd = winDataInputStream.readInt();
            }
            catch (Exception e) {
                LOG.severe(e.getMessage());
                //e.printStackTrace();
            }
        } // END loadMD2ModelHeader

        /**
         * Returns the frame size.
         *
         * @return The frame size.
         */
        public int getFrameSize() {
            return framesize;
        } // END getFrameSize

        /**
         * Returns the number of frames.
         *
         * @return The number of frames.
         */
        public int getNumberOfFrames() {
            return numFrames;
        } // END getTotalFrames

        /**
         * Returns the number of points.
         *
         * @return The number of points.
         */
        public int getNumberOfPoints() {
            return numXYZ;
        } // END getPoints

        /**
         * Returns the number of texture coordinates.
         *
         * @return The number of texture coordinates.
         */
        public int getNumberOfTextureCoordinates() {
            return numST;
        } // END getNumberOfTextureCoordinates

        /**
         * Returns the number of triangles.
         *
         * @return The number of triangles.
         */
        public int getNumberOfTriangles() {
            return numTris;
        } // END getNumberOfTriangles

        /**
         * Returns the offset of frames.
         *
         * @return The offset of frames.
         */
        public int getOffsetFrames() {
            return offsetFrames;
        } // END getOffsetFrames

        /**
         * Returns the offset of texture coordinates.
         *
         * @return The offset of texture coordinates.
         */
        public int getOffsetTextureCoordinates() {
            return offsetST;
        } // END getOffsetTextureCoordinates

        /**
         * Returns the offset of triangles.
         *
         * @return The offset of triangles.
         */
        public int getOffsetTriangles() {
            return offsetTris;
        } // END getOffsetTriangles

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return  "***** MD2 Header *****" +
                    "\nIdentity: " + identity +
                    "\nVersion: " + version +
                    "\nSkinwidth: " + skinwidth +
                    "\nSkinheight: " + skinheight +
                    "\nFramesize: " + framesize +
                    "\nnumSkins: " + numSkins +
                    "\nnumXYZ: " + numXYZ +
                    "\nnumST: " + numST +
                    "\nnumTris: " + numTris +
                    "\nnumGLcmds: " + numGLcmds +
                    "\nnumFrames: " + numFrames +
                    "\nOffsetSkins: " + offsetSkins +
                    "\nOffsetST: " + offsetST +
                    "\nOffsetTris: " + offsetTris +
                    "\nOffsetFrames: " + offsetFrames +
                    "\nOffsetGLcmds: " + offsetGLcmds +
                    "\nOffsetEnd: " + offsetEnd;
        } // END printMD2ModelHeader

    } // END inner class ModelHeader

    private class Frame {
        float[] scale;              // scaling for frame vertices
        float[] translate;          // translation for frame vertices
        String name;                // name of model
        int firstFramePoint;        // beginning of frame vertex list

        /**
         * Frame of a model.
         */
        public Frame() {
            scale = new float[3];
            translate = new float[3];
        } // END constructor SingleFrame

        /**
         * Returns the first point of the frame.
         *
         * @return The first point of the frame.
         */
        public int getFirstFramePoint() {
            return firstFramePoint;
        } // END getFramePoint

        /**
         * Returns the scale at the given index.
         *
         * @param index The index of the scale.
         * @return The scale at the given index.
         */
        public float getScale(int index) {
            return scale[index];
        } // END getScale

        /**
         * Returns the translation at the given index.
         *
         * @param index The index of the translation.
         * @return The translation at the given index.
         */
        public float getTranslate(int index) {
            return translate[index];
        } // END getTranslate

        /**
         * Loads the frame header from the given input stream.
         *
         * @param frameHeaderStream The input stream to read from.
         */
        public void loadFrameHeader(f_LittleEndianDataInputStream frameHeaderStream) {
            try {
                scale[0] = frameHeaderStream.readFloat();
                scale[1] = frameHeaderStream.readFloat();
                scale[2] = frameHeaderStream.readFloat();

                translate[0] = frameHeaderStream.readFloat();
                translate[1] = frameHeaderStream.readFloat();
                translate[2] = frameHeaderStream.readFloat();

                name = frameHeaderStream.readCString(16, true);
            }
            catch (Exception e) {
                LOG.severe(e.getMessage());
                //e.printStackTrace();
            }
        } // END loadFrameHeader

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            String output = "***** Single Frame ****";
            for (int i = 0; i < scale.length; i++)
                output = output + scale[i] + "\n";
            for (int i = 0; i < translate.length; i++)
                output = output + translate[i] + "\n";
            output += name;
            return output;
        } // END printFrameHeader

    } // END class Frame

    private class TextureCoordinate {
        /** The s coordinate of the texture */
        public float s;

        /** The t coordinate of the texture */
        public float t;
    } // END class TextureCoordinate

    private class Mesh {
        /** The indices of the meshes */
        public int[] meshIndex;

        /** The indices of the s and t texture coordinates */
        public int[] stIndex;

        /**
         * A Mesh.
         */
        public Mesh() {
            meshIndex = new int[3];
            stIndex = new int[3];
        } // END constructor Mesh

    } // END class Mesh

    //	END Inner classes
    //	END Inner classes
    //	END Inner classes
    //	END Inner classes
    public static void main(String args[]) {
    	ff_MD2Model m=new ff_MD2Model(new File("data/cube.md2"), null);
    	System.out.println(m);
    }
} // END class JDmd2Model
