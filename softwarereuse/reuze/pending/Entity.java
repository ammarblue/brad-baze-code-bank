package reuze.pending;

import java.util.HashMap;
import com.software.reuze.d_RefFloat;
import com.software.reuze.d_RefInt;

public abstract class Entity {

    private final int id;
    private final String sname;
    private static int NextID = 0;
    //used by the constructor to give each entity a unique ID

    private static int NextValidID() {
        return NextID++;
    }

    public Entity() {
    	id = NextValidID();
        this.sname = "E"+id;
    }

    public Entity(String name) {
        id = NextValidID();
        if(name == null || name.length() == 0) this.sname = "E"+id; else this.sname = name;
    }

    //all entities must implement an update function
    public abstract void Update(float time);

    //accessors
    public int ID() {
        return id;
    }

    public String Name() {
        return sname;
    }
    public Object map; //allows sub-classes to define their own data structures
    public int gi(String name) {
    	if (name.length()==2 &&name.charAt(1)=='d') return id;
    	return ((d_RefInt)((HashMap<String,Object>)map).get(name)).value;
    }
    public float gf(String name) {
    	return ((d_RefFloat)((HashMap<String,Object>)map).get(name)).value;
    }
    public String gs(String name) {
    	if (name.length()==5 && name.equals("sname")) return sname;
    	return (String)(((HashMap<String,Object>)map).get(name));
    }
    public Object go(String name) {
    	return ((HashMap<String,Object>)map).get(name);
    }
    public boolean is(String name) {
    	return map!=null && ((HashMap<String,Object>)map).containsKey(name);
    }
    public void var(String name) {
    	if (map==null) map=new HashMap<String,Object>();
    	Object o=null;
    	char c=name.charAt(0);
    	if (c=='f') o=new d_RefFloat();
    	else if (c=='i') o=new d_RefInt();
    	else if (c=='s') o="";
    	((HashMap<String,Object>)map).put(name, o);
    }
    public void si(String name, int i) {
    	((d_RefInt)((HashMap<String,Object>)map).get(name)).value=i;
    }
    public void sf(String name, float f) {
    	((d_RefFloat)((HashMap<String,Object>)map).get(name)).value=f;
    }
    public void ai(String name, int i) {
    	((d_RefInt)((HashMap<String,Object>)map).get(name)).value+=i;
    }
    public void af(String name, float f) {
    	((d_RefFloat)((HashMap<String,Object>)map).get(name)).value+=f;
    }
    public void ss(String name, String s) {
    	((HashMap<String,Object>)map).put(name, s);
    }
    public void so(String name, Object o) {
    	((HashMap<String,Object>)map).put(name, o);
    }
    @Override
    public String toString() {
    	return "";
    }
}
