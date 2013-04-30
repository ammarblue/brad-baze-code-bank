import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * This class represents a MD2 model. The MD2_Loader class should be used to
 * create object of this class.
 * 
 * @author Peter Lager
 * 
 */
public class MD2_Model implements PConstants {
	private PApplet app;

	private int numFrames; // number of model frames
	private int numXYZ; // number of vertices
	private int frameSize; // size of each frame in bytes
	private int cFrame; // current frame # in animation
	private int nFrame; // next frame # in animation
	private float interpol; // percent through current frame

	private Vector3[] point; // vertex list

	private Vector3 modOffset; // values to subtract to center model
	private Vector3 modSize; // size of model in 3 dimensions

	private int[] glComannd; // Type (FAN or STRIP and number of vertices to
								// render
	private GL_Vertex[] glVertex; // vertices to render in FAN or STRIP

	private PImage modelTex; // texture data

	private MD2_ModelState[] state; // Array of different model states
	private int stateIndex; // index to current state

	/**
	 * This ctor should only be called from the loader class.
	 * 
	 * @param app
	 * @param numFrames
	 * @param numXYZ
	 * @param frameSize
	 * @param points
	 * @param glCommand
	 * @param vertex
	 * @param modelTex
	 * @param state
	 */
	public MD2_Model(PApplet app, int numFrames, int numXYZ, int frameSize,
			Vector3[] points, Vector3 modOffset, Vector3 modSize,
			int[] glCommand, GL_Vertex[] vertex, PImage modelTex,
			MD2_ModelState[] state) {
		this.app = app;
		this.numFrames = numFrames;
		this.numXYZ = numXYZ;
		this.frameSize = frameSize;
		this.point = points;
		this.modOffset = modOffset;
		this.modSize = modSize;
		this.glComannd = glCommand;
		this.glVertex = vertex;
		this.modelTex = modelTex;
		this.state = state;
		this.stateIndex = 0;
	}

	/**
	 * Gets the current animation position. Integer part is frame number and
	 * fractional part is interpolation value to next frame.
	 * 
	 * @return
	 */
	public float getPosition() {
		return (float) cFrame + interpol;
	}

	/**
	 * Set the model state to be rendered
	 * 
	 * @param stateNo
	 */
	public void setState(int stateNo) {
		if (stateNo < 0 || stateNo >= state.length) {
			System.out.println("Invalid state");
			return;
		}
		stateIndex = stateNo;
		interpol = 0.0f;
		cFrame = state[stateNo].startframe;
		nFrame = cFrame + 1;
		if (nFrame > state[stateNo].endFrame)
			nFrame = state[stateNo].startframe;
	}

	/**
	 * get a particular animation state
	 * 
	 * @param stateNo
	 * @return
	 */
	public MD2_ModelState getState(int stateNo) {
		if (stateNo < 0 || stateNo >= state.length)
			return null;
		else
			return state[stateNo];
	}

	/**
	 * Allows user to get and use any animation states available
	 * 
	 * @return
	 */
	public MD2_ModelState[] getModelStates() {
		return state;
	}

	/**
	 * Update the interpolation value so that the model is animated by
	 * incrementing the interpolation value by percent
	 * 
	 * @param percent
	 *            MUSt be in the range >=0.0 and < 1.0
	 * @return
	 */
	public void update(float percent) {
		int sFrame = state[stateIndex].startframe;
		int eFrame = state[stateIndex].endFrame;

		interpol += percent;
		if (interpol >= 1.0) {
			interpol -= 1.0f;
			cFrame++;
			if (cFrame >= eFrame)
				cFrame = sFrame;
			nFrame = cFrame + 1;
			if (nFrame >= eFrame)
				nFrame = sFrame;
		}
	}

	/**
	 * Render the model
	 */
	public void render() {
		float x, y, z, s, t;
		int cmd;
		int vpos = 0;

		app.textureMode(NORMAL);

		int cfIdx = cFrame * numXYZ;
		int nfIdx = nFrame * numXYZ;

		Vector3 cp, np;

		for (int i = 0; i < glComannd.length; i++) {
			cmd = glComannd[i];
			if (cmd < 0) {
				cmd = -cmd;
				app.beginShape(TRIANGLE_FAN);
			} else if (cmd > 0) {
				app.beginShape(TRIANGLE_STRIP);
			}
			app.texture(modelTex);
			for (int j = 0; j < cmd; j++) {
				cp = point[cfIdx + glVertex[vpos].idx];
				np = point[nfIdx + glVertex[vpos].idx];
				s = glVertex[vpos].s;
				t = glVertex[vpos].t;
				x = cp.x + interpol * (np.x - cp.x);
				y = cp.y + interpol * (np.y - cp.y);
				z = cp.z + interpol * (np.z - cp.z);
				app.vertex(x, y, z, s, t);
				vpos++;
			}
			app.endShape();
		}
	}

	/**
	 * Centres the model so it evenly spaced over (0,0,0) Note: this will change
	 * all the XYZ points in the model
	 */
	public void centreModel() {
		centreModel(modOffset);
	}

	/**
	 * Centres the model so it evenly spaced over a given offset useful if we
	 * have 2 linked models. Note: this will change all the XYZ points in the
	 * model
	 */
	public void centreModel(Vector3 offset) {
		for (int i = 0; i < point.length; i++)
			point[i].subtract(offset);
		modOffset = new Vector3();
	}

	/**
	 * Scales the model Note: this will change all the XYZ points in the model
	 * 
	 * @param f
	 */
	public void scaleModel(float f) {
		for (int i = 0; i < point.length; i++)
			point[i].scale(f);
		modSize.scale(f);
	}

	/**
	 * Get the offset of the model centre from (0,0,0)
	 * 
	 * @return
	 */
	public Vector3 getModOffset() {
		return modOffset;
	}

	/**
	 * Get the size of the model
	 * 
	 * @return
	 */
	public Vector3 getModSize() {
		return modSize;
	}

	/**
	 * Display the model statistics and memory usage
	 */
	public String toString() {
		StringBuilder s = new StringBuilder("");
		s.append("\nMD2 Model details\n");
		s.append("\tThere are " + numFrames + " key framess\n");
		s.append("\tThere are " + point.length + " points (XYZ coordinates)\n");
		s.append("\tFor rendering there are " + glComannd.length
				+ " triangle strips/fans\n");
		s.append("\t and these have " + glVertex.length
				+ " vertex definitions\n");
		s.append("\tThere are " + state.length + " animation sequences\n");
		s.append("Estimated memory used " + memUsage()
				+ " bytes for model excluding texture.");

		return new String(s);
	}

	/**
	 * Calculate the model memory requirements excluding textures.
	 * 
	 * @return
	 */
	public int memUsage() {
		int usage = 36;
		usage += 12 * point.length;
		usage += 4 * glComannd.length;
		usage += 12 * glVertex.length;
		usage += 24 * state.length;
		return usage;
	}
}
