/**
 * Simple class to store that details of an animation sequence in the model.
 * 
 * @author Peter Lager
 * 
 */
public class MD2_ModelState {

	public String name;
	public int startframe;
	public int endFrame;

	/**
	 * @param name
	 * @param startframe
	 * @param endFrame
	 */
	public MD2_ModelState(String name, int startframe, int endFrame) {
		super();
		this.name = name;
		this.startframe = startframe;
		this.endFrame = endFrame;
	}

	public String toString() {
		return "STATE " + name + "(" + startframe + " - " + endFrame + ")";
	}
}
