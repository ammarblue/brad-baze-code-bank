package reuze.aifiles;

import java.util.ArrayList;
import java.util.Arrays;

import com.software.reuze.gb_Vector3;

public class dg_InfluenceMap {
	public static final int
	    OT_FRIENDLY=1,
	    OT_BULLET=2,
	    OT_ENEMY=3,
	    OT_MISC=4;

	static class RegObject
	{
	    dg_GameObject    m_pObject;
	    int         m_objSizeX;
	    int         m_objSizeY;
	    int         m_objType;
	    gb_Vector3     m_lastPosition;
	    boolean        m_stamped;
	};
	    //constructor/functions
	    public dg_InfluenceMap(int type) {
	    	m_influenceType=type;
	    	m_drawGrid = false;
	    	m_drawInfluence = false;
	    	Init(16,16,z_app.app.width,z_app.app.height);
	    }
	    void Update(float dt) {}
	    void Draw() {
	    	z_app.app.pushStyle();
	    	//Draw Grid lines
	        if(m_drawGrid)
	            DrawTheGrid();
	        if(m_drawInfluence)
	            DrawTheInfluence();
	        z_app.app.popStyle();
	    }
	    void DrawTheGrid() {
	    	z_app.app.stroke(0.2f,0.2f,0.2f);
	        //draw horizontal grid lines
	        float pos = 0.0f;
	        for(int i = 0;i<m_dataSizeY+1;i++)
	        {
	            z_app.app.line(0, pos,z_app.app.width, pos);
	            pos+=m_celResY;
	        }   
	        
	        //draw vertical grid lines
	        pos = 0.0f;
	        for(int i = 0;i<m_dataSizeX+1;i++)
	        {
	            z_app.app.line(pos, 0,pos, z_app.app.height);
	            pos+=m_celResX;
	        } 
	    }
	    void DrawTheInfluence() {
	    	for(int i=0;i<m_numCells;i++)
	        {
	            if(m_map[i]!=0)
	            {
	                int y = i / m_dataSizeY;
	                int x = i - y*m_dataSizeY;
	                z_app.app.fill(200);
	                z_app.app.rect(x*m_celResX, y*m_celResY, m_celResX, m_celResY);
	                z_app.app.fill(0);
	                z_app.app.text(""+m_map[i],x*m_celResX+m_celResX/2, y*m_celResY+m_celResY/2);
	            }
	        }
	    }
	    void Init(int sizeX, int sizeY, int wSizeX, int wSizeY) {
	    	    m_dataSizeX     = sizeX;
	    	    m_dataSizeY     = sizeY;
	    	    m_numCells       = m_dataSizeX*m_dataSizeY;
	    	    m_map           = new int[m_numCells];
	    	    m_registeredObjects=new ArrayList<RegObject>();
	    	    m_worldSizeX    = wSizeX;
	    	    m_worldSizeY    = wSizeY;
	    	    m_celResX       = m_worldSizeX / m_dataSizeX;
	    	    m_celResY       = m_worldSizeY / m_dataSizeY;
	    }
	    void Reset() {
	        //clear out the map
	        Arrays.fill(m_map, 0);	        
	        //get rid off all the objects
	        m_registeredObjects.clear();
	    }

	    void RegisterGameObj(dg_GameObject object) {
	        int sizeY,sizeX;
	        sizeX = sizeY = 1;
	        
	        RegObject temp;
	        temp = new RegObject();
	        temp.m_pObject      = object;
	        temp.m_objSizeX     = sizeX;
	        temp.m_objSizeY     = sizeY;
	        temp.m_lastPosition = object.m_position;
	        temp.m_stamped      = false;
	        m_registeredObjects.add(temp);
	    }
	    void RemoveGameObj(dg_GameObject object) {
	        m_registeredObjects.remove(object);
	    }
	    void StampInfluenceShape(gb_Vector3 center,int sizeX,int sizeY, int value) {
	        int gridX = (int) (center.x/ m_celResX);
	        int gridY = (int) (center.y/ m_celResY);
	        //TODO objects are using top left, this uses center, change to be consistent
	        int startX = gridX - sizeX/2;
	        if(startX < 0) startX += m_dataSizeX;
	        int startY = gridY - sizeY/2;
	        if(startY < 0) startY += m_dataSizeY;
	        
	        for(int y = startY;y<startY + sizeY;y++)
	        {
	            for(int x = startX;x<startX + sizeX;x++)
	            {
	                int i=(y%m_dataSizeY)*m_dataSizeX + (x%m_dataSizeX); 
	                m_map[i] += value;
	            }
	        }
	    }
	    void StampInfluenceGradient(gb_Vector3 location, int initValue) {
	        int gridX = (int) (location.x/ m_celResX);
	        int gridY = (int) (location.y/ m_celResY);
	        
	        float stopDist = Math.abs(initValue)*0.75f;//*(m_dataSizeX/32);
	        int halfStopDist = (int) Math.max(1,stopDist / 2);
	        int startX = gridX - halfStopDist;
	        if(startX < 0) startX += m_dataSizeX;
	        int startY = gridY - halfStopDist;
	        if(startY < 0) startY += m_dataSizeY;
	        
	        for(int y = startY;y<startY + stopDist;y++)
	        {
	            for(int x = startX;x<startX + stopDist;x++)
	            {
	                int value;

	                int distX = Math.abs(x - (startX + halfStopDist));
	                int distY = Math.abs(y - (startY + halfStopDist));

	                value = initValue* (halfStopDist - Math.max(distX,distY))/halfStopDist;
	                m_map[(y%m_dataSizeY)*m_dataSizeX + (x%m_dataSizeX)] += value;
	            }
	        }
	    }
	    int  SumInfluenceShape(gb_Vector3 location,int sizeX,int sizeY) {
	        int sum = 0;
	        int gridX = (int) (location.x/ m_celResX);
	        int gridY = (int) (location.y/ m_celResY);
	        
	        int startX = gridX - sizeX/2;
	        if(startX < 0) startX += m_dataSizeX;
	        int startY = gridY - sizeY/2;
	        if(startY < 0) startY += m_dataSizeY;
	        
	        for(int y = startY;y<startY + sizeY;y++)
	        {
	            for(int x = startX;x<startX + sizeX;x++)
	            {
	                sum += m_map[(y%m_dataSizeY)*m_dataSizeX + (x%m_dataSizeX)];
	            }
	        }
	        return sum;
	    }

	    int  GetInfluenceValue(gb_Vector3 location) {
	    	location=location.tmp();
	        location.x = (int) (location.x/ m_celResX);
	        location.y = (int) (location.y/ m_celResY);
	    	BoundsClamp(location);
	    	return m_map[(int) (location.y*m_dataSizeX+location.x)];
	    }
	    int  GetInfluenceValue(int x, int y) {
	    	gb_Vector3 v=new gb_Vector3(x,y,0);
	    	BoundsClamp(v); //System.out.println(v.x+" "+v.y);
	    	return m_map[(int) (v.y*m_dataSizeX+v.x)];
	    }
	    void ConvertPositionToGrid(gb_Vector3 location/*inout*/) {
	    	location.x = (int)(location.x/ m_celResX);
	    	location.y = (int)(location.y/ m_celResY);
	    }
	    void ConvertGridToPosition(int gridX, int gridY, gb_Vector3 location/*out*/) {
	    	location.set(gridX*m_celResX,gridY*m_celResY,0.0f);
	    }
	    void SetType(int type) {
	    	m_influenceType = type;
	    	}
	    void DrawGrid(boolean on) {m_drawGrid = on;}
	    void DrawInfluence(boolean on) {m_drawInfluence = on;}
	    int  GetSizeX() {return m_dataSizeX;}
	    int  GetSizeY() {return m_dataSizeY;}
		float GetResX() {return m_celResX;}
		float GetResY() {return m_celResY;}
		void BoundsClamp(gb_Vector3 v/*inout*/) {
			//bounds check the params
			if(v.x >= m_dataSizeX)
				v.x %= m_dataSizeX;
			while(v.x < 0)
				v.x += m_dataSizeX;
			if(v.y >= m_dataSizeY)
				v.y %= m_dataSizeY;
			while(v.y < 0)
				v.y += m_dataSizeY;
		}
		boolean IsOutOfBounds(int x, int y) {
			//bounds check the params
			if(x >= m_dataSizeX)
				return true;
			if(x < 0)
				return true;
			if(y >= m_dataSizeY)
				return true;
			if(y < 0)
				return true;

			return false;
		}

	    //influence map types
	    public static final int
	        IM_NONE=1,
	        IM_OCCUPANCE=2,
	        IM_CONTROL=3,
	        IM_BITWISE=4,
	        
	        DIR_LEFT  = 0x01,
	        DIR_RIGHT = 0x02,
	        DIR_UP    = 0x04,
	        DIR_DOWN  = 0x08;

	    //data members
	    int    m_map[];
	    ArrayList<RegObject> m_registeredObjects;

	    int     m_dataSizeX;     
	    int     m_dataSizeY;
	    int     m_numCells;
	    int     m_worldSizeX;
	    int     m_worldSizeY;
	    float   m_celResX;
	    float   m_celResY;
	    int     m_influenceType;

	    boolean    m_drawGrid;
	    boolean    m_drawInfluence;
}
