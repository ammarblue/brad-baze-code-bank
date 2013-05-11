package reuze.pending;
import java.util.Vector;

import com.software.reuze.ga_Vector2;
import com.software.reuze.m_MathUtils;

//Because I am messy when creating polygons, I have to look for similar points
public class PointsLookup
{
  public PointsLookup(float xmin, float ymin, float xmax, float ymax, int cellSize, float tolerance)
  {
    _xmin = xmin; _xmax=xmax;
    _ymin = ymin; _ymax=ymax;
    _cs = cellSize;
    _tol = tolerance;
    _tol2 = tolerance*tolerance;
    _data = new Vector<Vector<ga_Vector2>>();
    float fw = xmax - xmin, fh = ymax - ymin;
    _w = (int) Math.ceil(fw / (float)_cs); _h = (int) Math.ceil(fh / (float)_cs); // grid of 10x10 pixels
    int s = _w * _h; 
    for(int i=0; i<s; i++)
      _data.add(new Vector<ga_Vector2>());
    _points = new Vector<ga_Vector2>();
  }
  
  public ga_Vector2 getPoint(float x, float y)
  {
    // if outside boundaries
    if((x < _xmin - _tol || x > _xmax + _tol) && (y < _ymin - _tol || y > _ymax + _tol))
      return new ga_Vector2(x, y); // don't save that point for lookup
      
    int px = m_MathUtils.clamp((int) Math.floor((x-_xmin)/_cs), 0, _w-1);
    int py = m_MathUtils.clamp((int)Math.floor((y-_ymin)/_cs), 0, _h-1);
    
    // first try the most plausible location
    Vector<ga_Vector2> pts = _data.get(py*_w+px);
    for(ga_Vector2 p : pts) {
      float dx = x-p.x, dy = y-p.y;
      if(dx*dx+dy*dy <= _tol2)
        return p;
    }
    
    // then check the neighbors
    for(int gy=Math.max(py-1, 0); gy<=Math.min(py+1, _h-1); gy++) {
      for(int gx=Math.max(px-1, 0); gx<=Math.min(px+1, _w-1); gx++) {
        if(gx==px && gy==py) // already tested
          continue;
        pts = _data.get(gy*_w+gx);
        for(ga_Vector2 p : pts) {
          float dx = x-p.x, dy = y-p.y;
          if(dx*dx+dy*dy <= _tol2)
            return p;
        }
      }
    }
      
    ga_Vector2 pt = new ga_Vector2(x, y);
    _points.add(pt);
    _data.get(py*_w+px).add(pt);
    return pt;
  }
  
  public ga_Vector2 getPoint(ga_Vector2 p) { 
    if(p == null) return null;
    return getPoint(p.x, p.y); 
  }
  
  public Vector<ga_Vector2> getPoints() { return _points; }
  
  public  float _tol, _tol2;
  public  int _w, _h, _cs;
  public  float _xmin, _xmax, _ymin, _ymax;
  public  Vector<ga_Vector2> _points;
  public  Vector<Vector<ga_Vector2>> _data;
}