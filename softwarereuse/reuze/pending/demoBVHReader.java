package reuze.pending;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.software.reuze.gb_Vector3;
import com.software.reuze.vam_BonesData;


/**
 * A BVH file contains first the skeleton hierarchy and then the animation data.
 * 
 * See here for a very good summary:
 * 
 * http://www.cs.wisc.edu/graphics/Courses/cs-838-1999/Jeff/BVH.html
 * 
 * @author Michael Kipp
 */
public class demoBVHReader
{

	public List<BVHJoint> _jointList = new ArrayList<BVHJoint>(); // sequential list of joints
	private double _scale = 1d; // scaling factor used for bone length and position
	private int _motionVectorLength;
	private int _indexCounter;
	private double _maxRootDistance = 0;
	private double _targetHeight = 5;

	/**
	 * Stores results.
	 */
	public class BVHResult
	{

		public Bone skeleton;
		public vam_BonesData animation;
	}

	/**
	 * Intermediate representation for 1st pass.
	 */
	abstract class BVHNode
	{

		gb_Vector3 offset; // joint position (= dir/length of parent's bone)
		gb_Vector3 rootOffset = new gb_Vector3(); // position in root space (for computing overall size)
	}

	class BVHEndSite extends BVHNode
	{
	}

	class BVHJoint extends BVHNode
	{

		String name;
		List<BVHJoint> subjoints = new ArrayList<BVHJoint>();
		BVHEndSite endSite = null;
		int[] dof = new int[0];
		boolean isRoot = false;

		double findMax(double max)
		{
			double l = offset.len();
			for (BVHJoint j : subjoints) {
				l = j.findMax(l);
			}
			return l > max ? l : max;
		}

		void scale(double scale)
		{

			offset.mul((float)scale);
			for (BVHJoint j : subjoints) {
				j.scale(scale);
			}
			if (endSite != null) {
				endSite.offset.mul((float)scale);
			}
			//            System.out.println("scale " + name + ": " + offset);
		}
	}

	/**
	 * 
	 * @param targetHeight
	 *            The height that the skeleton should have (automatic scaling). Specify -1 if you do
	 *            not want to rescale.
	 */
	public demoBVHReader(double targetHeight)
	{
		//        _scale = scale;
		_targetHeight = targetHeight;
		_indexCounter = 0;
	}

	/**
	 * Reads BVH file and returns skeleton and motion data.
	 * 
	 * @throws java.io.IOException
	 */
	public BVHResult readFile(File file) throws IOException
	{
		BVHResult res = new BVHResult();
		String line;
		BufferedReader in = new BufferedReader(new FileReader(file));
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("HIERARCHY")) {
				_maxRootDistance = 0;
				BVHJoint r = readHierarchy(in);

				//                System.out.println("### max = " + _maxRootDistance);

				// scale skeleton to target height
				if (_targetHeight > -1) {
					double s = _targetHeight / (2 * _maxRootDistance);
					//                System.out.println("### scaling: " + s);
					r.scale(s);
					_scale = s;
				}

				// 2nd pass through skeleton
				res.skeleton = processJoint(r, null, r.findMax(0) / 15);
			}
			if (line.startsWith("MOTION")) {
				res.animation = readMotion(in);
			}
		}
		return res;
	}

	public double getScale()
	{
		return _scale;
	}

	/**
	 * 2nd pass for constructing the skeleton (recursively).
	 */
	private Bone processJoint(BVHJoint n, Bone parent, double maxRadius)
	{
		Bone b = new Bone(n.name, _indexCounter++, n.dof);
		b.setRotationType(Bone.MOVING_AXES);
		if (parent != null) {
			b.setParent(parent);
			parent.attachGeom(n.offset, maxRadius);
		}
		b.setBaseTranslation(n.offset);

		// create children
		Bone[] ch = new Bone[n.subjoints.size()];
		int i = 0;
		for (BVHNode c : n.subjoints) {
			if (c instanceof BVHJoint) {
				ch[i++] = processJoint((BVHJoint) c, b, maxRadius);
			}
		}
		b.setChildren(ch);

		if (n.endSite != null) {
			b.attachGeom(n.endSite.offset, maxRadius);
		}
		return b;
	}

	/**
	 * Starts 1st pass to construct the skeleton.
	 */
	private BVHJoint readHierarchy(BufferedReader in) throws IOException
	{
		String line;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("ROOT")) {
				BVHJoint r = readJoint(in, null, line.substring(line
						.indexOf(' ') + 1));
				r.isRoot = true;
				return r;
			}
			break;
		}
		return null;
	}

	/**
	 * 1st pass joint processing.
	 */
	private BVHJoint readJoint(BufferedReader in, BVHJoint parent, String name)
			throws IOException
	{
		BVHJoint n = new BVHJoint();
		_jointList.add(n);
		n.name = name;

		// do not count root location
		if (parent != null) {
			n.rootOffset.set(parent.offset);
		}
		String line;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("JOINT")) {
				BVHJoint sub = readJoint(in, n, line.substring(line
						.indexOf(' ') + 1));
				n.subjoints.add(sub);
			} else if (line.startsWith("OFFSET")) {
				n.offset = readVector(line.substring(6).trim());
				n.offset.mul((float)_scale);
				n.rootOffset.add(n.offset);
			} else if (line.startsWith("CHANNELS")) {
				n.dof = readChannels(line.substring(8).trim());
			} else if (line.startsWith("}")) {
				break;
			} else if (line.startsWith("End Site")) {
				n.endSite = readEndSite(in);
				n.endSite.rootOffset.set(n.rootOffset);
				n.endSite.rootOffset.add(n.endSite.offset);
				_maxRootDistance = Math.max(_maxRootDistance,
						n.endSite.rootOffset.len());
			}
		}
		return n;
	}

	private gb_Vector3 readVector(String str)
	{
		String[] arr = str.trim().split("\\s+");
		return new gb_Vector3((float)Double.parseDouble(arr[0]), (float)Double
				.parseDouble(arr[1]), (float)Double.parseDouble(arr[2]));
	}

	private int[] readChannels(String str)
	{
		String[] tok = str.split(" ");
		int num = Integer.parseInt(tok[0]);
		int[] res = new int[num];
		for (int i = 0; i < num; i++) {
			res[i] = getDOF(tok[i + 1]);
		}
		return res;
	}

	private int getDOF(String w)
	{
		if (w.equals("Xrotation")) {
			return Bone.RX;
		} else if (w.equals("Yrotation")) {
			return Bone.RY;
		} else if (w.equals("Zrotation")) {
			return Bone.RZ;
		} else if (w.equals("Xposition")) {
			return Bone.TX;
		} else if (w.equals("Yposition")) {
			return Bone.TY;
		} else if (w.equals("Zposition")) {
			return Bone.TZ;
		}
		return -1;
	}

	private BVHEndSite readEndSite(BufferedReader in) throws IOException
	{
		BVHEndSite n = new BVHEndSite();
		String line;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("OFFSET")) {
				n.offset = readVector(line.substring(6));
				n.offset.mul((float)_scale);
			} else if (line.startsWith("}")) {
				return n;
			}
		}
		return n;
	}

	/**
	 * Reads motion (one line contains one frame) and stores the data in the respective Bone
	 * objects.
	 */
	private vam_BonesData readMotion(BufferedReader in/*, Bone root*/)
			throws IOException
	{
		computeMotionVectorLength();
		float[][] motion = new float[_jointList.size()][]; // 1st index = joint, 2nd = data
		vam_BonesData data = new vam_BonesData(_jointList.size());
		String line;
		int framecount = 0;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("Frames:")) {
				int frames = Integer.parseInt(line.substring(7).trim());
				data.setNumFrames(frames);
				for (int i = 0; i < _jointList.size(); i++) {
					motion[i] = new float[frames * _jointList.get(i).dof.length];
				}
			} else if (line.startsWith("Frame Time:")) {
				float frametime = Float.parseFloat(line.substring(11).trim());
				data.setFps(1 / frametime);
			} else if (line.length() > 0) {
				// motion line
				readMotionLine(motion, line, framecount++);
			}
		}
		for (int i = 0; i < motion.length; i++) {
			data.putBoneData(i, motion[i]);
		}
		return data;
	}

	private void readMotionLine(float[][] motion, String line, int frame)
	{
		String[] tok = line.replaceAll("\\s+", " ").split(" ");
		if (tok.length != _motionVectorLength) {
			System.out.println("ERROR! Incorrect motion vector length: "
					+ (frame + 1) + "th frame, " + tok.length + " found, "
					+ _motionVectorLength + " required.");
		}
		int pos = 0;
		for (int i = 0; i < _jointList.size(); i++) {
			BVHJoint jnt = _jointList.get(i);
			int dof = jnt.dof.length;
			int motionindex = frame * dof;
			for (int j = 0; j < dof; j++) {
				// distinguish special case of 3 translational values for root
				double v = jnt.isRoot && j < 3 ? _scale
						* Float.parseFloat(tok[pos + j].trim()) : (float) Math
						.toRadians(Float.parseFloat(tok[pos + j].trim()));
				motion[i][motionindex + j] = (float) v;
			}
			pos += dof;
		}
	}

	/**
	 * Computes number of values expected in each line of the MOTION part of the BVH file.
	 */
	private void computeMotionVectorLength()
	{
		_motionVectorLength = 0;
		for (int i = 0; i < _jointList.size(); i++) {
			_motionVectorLength += _jointList.get(i).dof.length;
		}
	}

	public static void main(String[] args)
	{
		demoBVHReader bvh=new demoBVHReader(-1);
		BVHResult bvhr=null; //TODO doesn't work
		try {
			bvhr=bvh.readFile(new File("/Users/bobcook/Desktop/bvh-mocap1-9/09/09_02.bvh"));
			bvhr.skeleton.print();
			//MocapPlayer mo=new MocapPlayer(bvhr.skeleton, bvhr.animation, new Vector3());
			BoneJ3d.initScene(bvhr.skeleton);
			PrintStream ps=new PrintStream("/Users/bobcook/Desktop/mocap.txt");
	        List<Bone> ls = new ArrayList<Bone>();
	        bvhr.skeleton.collectBones(ls);
	        Bone[] bones = new Bone[ls.size()];
	        bones = (Bone[]) ls.toArray(bones);
	        gb_Vector3 p=new gb_Vector3();
	        for (int frame=0; frame<bvhr.animation.getNumFrames(); frame++)
	        for (int i = 0; i < bones.length; i++) {
	            Bone bone = bones[i];

	            if (bone.getParent()!=null) {
	                bone.setPose(frame, bvhr.animation.getBoneData(bone.getIndex()), null);
	            }
	            p.set(0,0,0);
	            bone.j3d.getWorldPosition(p);
	            ps.printf("%d %s %5.3f %5.3f %5.3f\n",frame, bone.getName(),p.x,p.y,p.z);
	        }
	        ps.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
