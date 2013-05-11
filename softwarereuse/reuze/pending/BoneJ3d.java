package reuze.pending;



import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;



import com.software.reuze.gb_Vector3;
import com.sun.j3d.utils.universe.SimpleUniverse;
public class BoneJ3d{
    //private BoneGeom _boneGeom; // the displayed geometry of the bone/joint
    //private JointGeom _jointGeom;
    private Switch _textSwitch;
    private TransformGroup _baseTransTG, _transTG, _baseRotTG, _rotTG,
            _invBaseRotTG;
    public Transform3D _t1 = new Transform3D();
    public Transform3D _t2 = new Transform3D();
    public Transform3D _transTF = new Transform3D();
    public Transform3D _tworld = new Transform3D();
    private gb_Vector3 _geomDirection;
    private double _geomRadius;
    public BoneJ3d() {
        _baseTransTG = new TransformGroup();
        _baseTransTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _baseTransTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        _transTG = new TransformGroup();
        _transTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _baseRotTG = new TransformGroup();
        _baseRotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _baseRotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        //        _baseRotTG.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        _baseRotTG.setCapability(TransformGroup.ALLOW_LOCAL_TO_VWORLD_READ);
        _rotTG = new TransformGroup();
        _rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _invBaseRotTG = new TransformGroup();
        _invBaseRotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // wire up TG's
        _baseTransTG.addChild(_transTG);
        _transTG.addChild(_baseRotTG);
        _baseRotTG.addChild(_rotTG);
        _rotTG.addChild(_invBaseRotTG);
    }
    public void set (Transform3D t1, Transform3D t2) {
    	t1=new Transform3D(t2);
    }
    public void setRotation(Transform3D t1) {
    	_rotTG.setTransform(t1);
    }
    public void scale(double factor, gb_Vector3 _trans) {
    	_baseTransTG.getTransform(_t1);
    	Vector3d pp=new Vector3d();
        _t1.get(pp);
        pp.scale(factor);
        _t1.set(pp);
        _baseTransTG.setTransform(_t1);
        _trans.set((float)pp.x,(float)pp.y,(float)pp.z);
    }
    public void getWorldPosition(gb_Vector3 p)
    {
    	Vector3d pp=new Vector3d();
        getFullTransform(_baseRotTG, _tworld);
        //        getWorldTransform(_tworld);
        _tworld.transform(pp);
        p.set((float)pp.x,(float)pp.y,(float)pp.z);
    }
    public void getPosition(SceneGraphPath path, gb_Vector3 p)
    {
        Vector3d pp=new Vector3d();
        _baseRotTG.getLocalToVworld(path, _tworld);
        _tworld.transform(pp);
        p.set((float)pp.x,(float)pp.y,(float)pp.z);
    }
    public void setSelected(boolean val)
    {
        assert false; //_boneGeom.setSelected(val);
    }
    /**
     * Sets bone and children to zero rotation.
     */
    public void reset()
    {
        _t1.setIdentity();
        _rotTG.setTransform(_t1);
    }
    public double getLength()
    {
        assert false;
        return 0d; //return _boneGeom != null ? _boneGeom.getLength() : 0d;
    }
    public void selectGeom(int style)
    {
        assert false; //if (_boneGeom != null) {_boneGeom.select(style);}
    }
    public void selectJointGeom(int style)
    {
        assert false; //if (_jointGeom != null) {_jointGeom.select(style);}
    }
    private List<Node> collectScenegraphPath(Bone initialBone, boolean all)
    {
        //        System.out.println(">> collect " + getName() + "  initial=" + initialBone);
        List<Node> result = new ArrayList<Node>();
        if (!this.equals(initialBone)) {
            //TODO RPC result.addAll(_parent.j3d.collectScenegraphPath(initialBone, true));
            result.add(_baseTransTG);
            result.add(_transTG);
        }

        if (all) {
            result.add(_baseRotTG);
            result.add(_rotTG);
            result.add(_invBaseRotTG);
        }
        return result;
    }

    public SceneGraphPath getSceneGraphPath(Bone initialBone)
    {
        List<Node> path = collectScenegraphPath(initialBone, false);
        Node[] p = new Node[path.size()];
        p = (Node[]) path.toArray(p);
        SceneGraphPath sgp = new SceneGraphPath();
        sgp.setNodes(p);
        return sgp;
    }
    public void attachGeom(Bone _parent, gb_Vector3 direction, double maxRadius)
    {
        _geomDirection = new gb_Vector3(direction);
        _geomRadius = maxRadius;
        assert false; //_boneGeom = new BoneGeom(_invBaseRotTG, direction);
        double maxlength = _parent == null ? getLength() : Math.min(
                getLength(), _parent.getLength());
        //_jointGeom = new JointGeom(_baseRotTG, (float) Math.min(maxRadius, maxlength / 4));
    }
    public TransformGroup getBaseTransTG()
    {
        return _baseTransTG;
    }

    public TransformGroup getBaseTG()
    {
        return _baseTransTG;
    }

    public TransformGroup getBaseRotTG()
    {
        return _baseRotTG;
    }

    public TransformGroup getEndTG()
    {
        return _invBaseRotTG;
    }
    public void setBaseTranslation(gb_Vector3 vec)
    {
        Transform3D tf = new Transform3D();
        tf.setTranslation(new Vector3d(vec.x,vec.y,vec.z));
        _baseTransTG.setTransform(tf);
    }
    public gb_Vector3 getUpVector()
    {
        Transform3D xform = new Transform3D();
        _baseTransTG.getLocalToVworld(xform);

        /// transform the northward vector from world to local space
        Vector3d v3dUpVector = new Vector3d(0, 1, 0);
        //				xform.invert();
        xform.transform(v3dUpVector);
        System.out.println("UpVector: " + v3dUpVector);
        getFullTransform(_baseRotTG, _tworld);
        //        getWorldTransform(_tworld);
        return new gb_Vector3((float)v3dUpVector.x,(float)v3dUpVector.y,(float)v3dUpVector.z);

    }
    public void setBaseRotDeg(gb_Vector3 eulerDegrees)
    {
        _t1.rotX(Math.toRadians(eulerDegrees.x));
        _t2.rotY(Math.toRadians(eulerDegrees.y));
        _t2.mul(_t1);

        Transform3D baseRot = new Transform3D();

        baseRot.rotZ(Math.toRadians(eulerDegrees.z));
        baseRot.mul(_t2);
        _baseRotTG.setTransform(baseRot);

        Transform3D invBaseRot = new Transform3D(baseRot);
        invBaseRot.invert();
        _invBaseRotTG.setTransform(invBaseRot);
    }
    public void displayName(int type)
    {
        if (_textSwitch != null) {
            _textSwitch.setWhichChild(type == Bone.NO_NAME
                    ? Switch.CHILD_NONE
                    : type);
        }
    }
    public void getRelativePosition(Bone ref, gb_Vector3 p)
    {
        Vector3d pp=new Vector3d();
        getRelativeTransform(_baseRotTG, ref.j3d.getBaseRotTG(), _t1);
        _t1.transform(pp);
        p.set((float)pp.x,(float)pp.y,(float)pp.z);
    }
	public void setTranslation(gb_Vector3 _trans) {
		_transTF.setIdentity();
        _transTF.setTranslation(new Vector3d(_trans.x,_trans.y,_trans.z));
        _transTG.setTransform(_transTF);
	}
    /**
     * Computes the full transform from root to the given transform group,
     * including the transform *in* the TG.
     *
     * The resulting transform can be used to find the origin of the TG by
     * applying the transform to (0,0,0).
     *
     * @param tg The TG in question
     * @param tf Resulting transform is returned here (old transform is overwritten)
     */
    public static void getFullTransform(TransformGroup tg, Transform3D tf)
    {
        Transform3D tf2 = new Transform3D();
        tf.setIdentity();
        tg.getTransform(tf2);
        tg.getLocalToVworld(tf);
        tf.mul(tf2);
    }

    /**
     * Computes the transform that takes a point that is *local* in locationTG
     * to the frame of reference of frameTG.
     *
     * If one applies the resulting transform to (0,0,0) one obtains the location
     * of the origin of locationTG in the frame of reference of frameTG.
     *
     * @param locationTG The TG in question, must be (grand)child of frameTG
     * @param frameTG The new frame of reference, must be parent of locationTG
     * @param tf Resulting transform is returned here (old transform is overwritten)
     */
    public static void getRelativeTransform(TransformGroup locationTG, TransformGroup frameTG, Transform3D tf)
    {
        Transform3D tf2 = new Transform3D();
        getFullTransform(locationTG, tf);
        getFullTransform(frameTG, tf2);
        tf2.invert();
        tf.mul(tf2, tf);
    }
    public static BranchGroup _root,_branchGroup;
    public static TransformGroup _tg;
    public static Canvas3D _canvas;
    public static SimpleUniverse _su;
    public static void initScene(Bone skeleton) {
    	_root = createScenegraph();
        _root.compile();
        _canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration(), true);
        _su = new SimpleUniverse(_canvas);
        _su.getViewer().getView().setBackClipDistance(500);
        _su.getViewingPlatform().getViewPlatform().setActivationRadius(250);
        _su.addBranchGraph(_root);
        _branchGroup = new BranchGroup();
        _branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        _tg = new TransformGroup();
        _tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        _tg.addChild(skeleton.j3d.getBaseTG());
        _branchGroup.addChild(_tg);
        _branchGroup.compile();
        _root.addChild(_branchGroup);
    }
    public static BranchGroup createScenegraph()
    {
        BranchGroup r = new BranchGroup();
        r.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        //lightScene(r);
        //r.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        //_coordCrossSwitch = createCoordCross(r, 15f, .01f);
        //r.addChild(_coordCrossSwitch);
        r.setBounds(new BoundingSphere(new Point3d(0, 0, 0), 10d));
        return r;
    }
}
