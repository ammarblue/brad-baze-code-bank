package reuze.pending;

/* Fortune's algorithm for computing the voronoi diagram
* Useful when all PVectors are known at the start
* (fastest known algorithm yet)  
* Greatly inspired by the c++ implementation of Matt Brubeck
* http://www.cs.hmc.edu/~mbrubeck/voronoi.html
*/

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Vector;

import com.software.reuze.ga_Vector2;
import com.software.reuze.m_MathUtils;
public class Voronoi
{
  final float epsilon = 0.01f;
  boolean ok;
  
  class Event
  {
    float x;
    ga_Vector2 p;
    Arc a;
    boolean valid;
    
    Event(float x, ga_Vector2 p, Arc a) {
      this.x=x; this.p=p; this.a=a; valid=true;
    }
  }
  
  public final class Site
  {
    ga_Vector2 pos;
    LinkedList<ga_Vector2> points;
    
    Site(ga_Vector2 p) {
      pos = p;
      points = new LinkedList<ga_Vector2>();
    }
    
    boolean addSegment(Segment s) {
      if(points.size() == 0) {
        points.add(s.getStart(this));
        points.add(s.getEnd(this));
        return true;
      } else {
        if(points.getLast() == s.getStart(this)) {
          points.addLast(s.getEnd(this));
          return true;
        }
        else if(points.getFirst() == s.getEnd(this)) {
          points.addFirst(s.getStart(this));
          return true;
        }
        else
          return false;
      }
    }
  }
  
  class Arc
  {
    Site site;
    Arc prev, next; // For the time being, a linked list
    Event e;
    Segment s0, s1;
    
    Arc(Site s, Arc a, Arc b) {
      this.site=s; prev=a; next=b;
    }
  }
  
  class Segment
  {
    ga_Vector2 p0, p1;
    Site s0, s1;
    boolean done;

    Segment(ga_Vector2 p, Site s0, Site s1) {
      p0=pointsLookup.getPoint(p); done=false; this.s0=s0; this.s1=s1;
      segments.add(this);
    }
    
    void finish(ga_Vector2 p) {
      if(done) return;
      p1 = pointsLookup.getPoint(p);
      done = true;
      
      // s0 must be on the "left" of this segment
      ga_Vector2 v0 = p0.tmp().sub(s0.pos),
                 v1 = p1.tmp2().sub(p0);
      if(v0.x*v1.y-v0.y*v1.x < 0) {  // z coordinate of the cross product
        Site tmp = s0; s0 = s1; s1 = tmp;
      }
    }
    
    ga_Vector2 getStart(Site s) {
      if(s == s0) return p0;
      else return p1;
    }
    
    ga_Vector2 getEnd(Site s) {
      if(s == s0) return p1;
      else return p0;
    }
  }
  
  class SegmentPair
  {
    Segment s0, s1;
    SegmentPair(Segment s0, Segment s1) { this.s0=s0; this.s1=s1; }
    
    void fusion() {
      s0.p0 = s1.p1;
      segments.remove(s1);
    }
  }
  
  public class SiteComparator implements Comparator<Site> {
      public int compare(Site p1, Site p2) {
        return (p1.pos.x < p2.pos.x ? -1 : (p1.pos.x > p2.pos.x ? 1 : 0));
      }
  }
  
   class EventComparator implements Comparator<Event> {
      public int compare(Event e1, Event e2) {
        return (e1.x < e2.x ? -1 : (e1.x > e2.x ? 1 : 0));
      }
  }

  PriorityQueue<Site> sitesQueue;
  PriorityQueue<Event> events;
  Vector<Segment> segments;
  Vector<Site> sites;
  Vector<SegmentPair> joinableSegments;
  PointsLookup pointsLookup;
  Arc root;
  float xmin, xmax, ymin, ymax;
  ga_Vector2 tl, tr, bl, br;
  
  public Voronoi(float left, float top, float right, float bottom) {
    xmin=left; xmax=right; ymin=top; ymax=bottom;
    tl = new ga_Vector2(left, top); tr = new ga_Vector2(right, top);
    bl = new ga_Vector2(left, bottom); br = new ga_Vector2(right, bottom);
  }
  
  public void compute(Vector<ga_Vector2> pts) {
	  sites = new Vector<Site>(pts.size());
	  sitesQueue = new PriorityQueue<Site>(pts.size(), new SiteComparator());
	  for(ga_Vector2 p : pts) {
	      Site s = new Site(p);
	      sites.add(s);
	      sitesQueue.offer(s);
	    }
	  compute();
  }
  public void compute(ga_Vector2 pts[], int startIndex, int endIndex) {
	  sites = new Vector<Site>(endIndex-startIndex+1);
	  sitesQueue = new PriorityQueue<Site>(endIndex-startIndex+1, new SiteComparator());
	  for(int i=startIndex; i<=endIndex; i++) {
	      Site s = new Site(pts[i]);
	      sites.add(s);
	      sitesQueue.offer(s);
	    }
	  compute();
  }
  public void compute() {
    ok = true;
    root = null;
    events = new PriorityQueue<Event>(sites.size(), new EventComparator());
    segments = new Vector<Segment>();
    joinableSegments = new Vector<SegmentPair>();
    pointsLookup = new PointsLookup(xmin, ymin, xmax, ymax, 10, epsilon);
    
    // Select the next event or ga_Vector2 with smaller x value
    while(!sitesQueue.isEmpty()) {
      if(!events.isEmpty() && events.peek().x <= sitesQueue.peek().pos.x)
        processEvent();
      else
        processSite();
    }
    
    // Only circle events remaining
    while(!events.isEmpty())
      processEvent();
    
    // Cut dangling half lines
    finishEdges();
    
    // Join half-segments into whole segments
    joinSegments();
    
    // Remove segments outside boundaries and cut to the boundaries
    cleanSegments();
    
    // Use the segments to create polygons around sites
    createPolygons();
  }
  
  void processSite()
  {
    // We will add a new arc to the beach front, using the next site
    Site s = sitesQueue.poll();
    if(root == null) {
      root = new Arc(s, null, null);
      return;
    }
    
    // Find the arc at the same height of p
    for(Arc i=root; i!=null; i=i.next) {
      ga_Vector2 pt = new ga_Vector2();
      if(intersectionParabolaArc(s.pos, i, pt)) {
        Site s2 = i.site;
        // New parabola intersects arc i, we my have to duplicate it
        if(i.next!=null && !intersectionParabolaArc(s.pos, i.next, null)) {
          i.next.prev = new Arc(i.site, i, i.next);
          i.next = i.next.prev;
        } else
          i.next = new Arc(i.site, i, null);
        i.next.s1 = i.s1;
        
        // Place the arc from p between i and i.next
        i.next.prev = new Arc(s, i, i.next);
        i.next = i.next.prev;
        
        i = i.next;
        
        // Start new half lines
        i.prev.s1 = i.s0 = new Segment(pt, i.site, s2);
        i.next.s0 = i.s1 = new Segment(pt, i.site, s2);
        joinableSegments.add(new SegmentPair(i.s0, i.s1));
        
        // Create new events if necessary
        checkCircleEvent(i, s.pos.x);
        checkCircleEvent(i.prev, s.pos.x);
        checkCircleEvent(i.next, s.pos.x);
        
        return;
      }
    }
    
    // If there are no intersections, put the new arc at the end
    Arc i;
    for(i=root; i.next!=null; i=i.next) ;
    i.next = new Arc(s, i, null);
    // New segment between p and i
    ga_Vector2 start = new ga_Vector2(xmin, (i.next.site.pos.y + i.site.pos.y)/2);
    i.s1 = i.next.s0 = new Segment(start, s, null);
  }
  
  void processEvent()
  {
    Event e = events.poll();
    if(!e.valid) return;
    
    Segment s = new Segment(e.p, e.a.next.site, e.a.prev.site);   
    
    // Remove corresponding arc
    Arc a = e.a;
    if(a.prev != null) {
      a.prev.next = a.next;
      a.prev.s1 = s;
    }
    if(a.next != null) {
      a.next.prev = a.prev;
      a.next.s0 = s;
    }
    
    // Finish segments
    if(a.s0 != null) a.s0.finish(e.p);
    if(a.s1 != null) a.s1.finish(e.p);
    
    // Check for events
    if(a.prev != null) checkCircleEvent(a.prev, e.x);
    if(a.next != null) checkCircleEvent(a.next, e.x);
  }
  
  boolean intersectionParabolaArc(ga_Vector2 p, Arc a, ga_Vector2 res)
  {
    if(a.site.pos.x == p.x) return false;
    
    double i0=0, i1=0;
    if(a.prev != null)  // Intersection of i.prev, i
      i0 = intersectionParabolas(a.prev.site.pos, a.site.pos, p.x).y;
    if(a.next != null) // Intersection of i, i.next
      i1 = intersectionParabolas(a.site.pos, a.next.site.pos, p.x).y;
      
    if( (a.prev==null || i0<=p.y) && (a.next==null || i1>=p.y) ) {
      if(res != null) {
        res.y = p.y;
        res.x = (a.site.pos.x*a.site.pos.x 
          + (a.site.pos.y-res.y)*(a.site.pos.y-res.y) - p.x*p.x)
          / (2*a.site.pos.x - 2*p.x);
      }
      return true;
    }
    
    return false;
  }
  
  ga_Vector2 intersectionParabolas(ga_Vector2 p0, ga_Vector2 p1, float l)
  {
    ga_Vector2 res=new ga_Vector2(), p=p0;
    if(p0.x == p1.x)
      res.y = (p0.y + p1.y) / 2;
    else if(p1.x == l)
      res.y = p1.y;
    else if(p0.x == l) {
      res.y = p0.y;
      p = p1;
    } else {
      float s0 = 2 * (p0.x - l), s1 = 2 * (p1.x - l);
      float a = 1/s0 - 1/s1;
      float b = -2 * (p0.y / s0 - p1.y / s1);
      float c = (p0.y*p0.y + p0.x*p0.x - l*l) / s0
                -(p1.y*p1.y + p1.x*p1.x - l*l) / s1;
      res.y = (-b - (float)Math.sqrt(b*b - 4*a*c)) / (2*a);
    }
    res.x = (p.x*p.x + (p.y-res.y)*(p.y-res.y) - l*l)/(2*p.x-2*l);
    return res;
  }
  
  void checkCircleEvent(Arc a, float x0)
  {
    // Invalidate events
    if(a.e != null && a.e.x != x0)
      a.e.valid = false;
    a.e = null;
    if(a.prev == null || a.next == null)
      return;
      
    CircleResponse r = computeCircle(a.prev.site.pos, a.site.pos, a.next.site.pos);
    if(r.b) {
      a.e = new Event(r.x, r.p, a);
      events.offer(a.e);
    }
  }
  
  class CircleResponse {
    ga_Vector2 p;
    float x;
    boolean b;
    CircleResponse(boolean b) { p=new ga_Vector2(); this.b=b; }
  }
  
  CircleResponse computeCircle(ga_Vector2 a, ga_Vector2 b, ga_Vector2 c)
  {
    // BC must be a right turn from AB
    if((b.x-a.x)*(c.y-a.y) - (c.x-a.x)*(b.y-a.y) > 0)
      return new CircleResponse(false);
    
    // Algorithm from O'Rourke 2ed p. 189.
    float A = b.x - a.x,  B = b.y - a.y,
          C = c.x - a.x,  D = c.y - a.y,
          E = A*(a.x+b.x) + B*(a.y+b.y),
          F = C*(a.x+c.x) + D*(a.y+c.y),
          G = 2*(A*(c.y-b.y) - B*(c.x-b.x));
    
    // co-linear   
    if(G == 0) return new CircleResponse(false);
    
    // p is the center
    CircleResponse r = new CircleResponse(true);
    r.p.x = (D*E-B*F)/G;
    r.p.y = (A*F-C*E)/G;
    
    // max x coordinate
    r.x = (float) (r.p.x + Math.sqrt(Math.pow(a.x-r.p.x, 2)+Math.pow(a.y-r.p.y, 2)));
    return r;
  }
  
  void finishEdges()
  {
    float l = xmax + (xmax-xmin) + (ymax-ymin);
    for(Arc i=root; i.next!=null; i=i.next)
      if(i.s1 != null)
        i.s1.finish(intersectionParabolas(i.site.pos, i.next.site.pos, l*2));
  }
  
  void joinSegments()
  {
    for(SegmentPair sp : joinableSegments)
      sp.fusion();
    joinableSegments.clear();
  }
  
  void cleanSegments()
  {
    Iterator<Segment> iter = segments.iterator();
    while(iter.hasNext()) {
      Segment s = iter.next();
      if(s.p0 == null || s.p1 == null || s.s1 == null) {
       iter.remove();
       continue;
      }
      
      // Testing if a ga_Vector2 lies outside the boundaries
      boolean o0 = false, o1 = false;
      if(s.p0.x < xmin || s.p0.x > xmax || s.p0.y < ymin || s.p0.y > ymax) o0 = true;
      if(s.p1.x < xmin || s.p1.x > xmax || s.p1.y < ymin || s.p1.y > ymax) o1 = true;
      
      // 2 PVectors outside
      if(o0 && o1) {
        iter.remove();
        continue;
      }
      
      if(o0) {
        ga_Vector2 t = pointsLookup.getPoint(intersectionSegments(s, tl, tr));
        if(t != null) { s.p0 = t; continue; }
        t = intersectionSegments(s, tr, br);
        if(t != null) { s.p0 = t; continue; }
        t = intersectionSegments(s, br, bl);
        if(t != null) { s.p0 = t; continue; }
        t = intersectionSegments(s, bl, tl);
        if(t != null) { s.p0 = t; continue; }
      }
      
      if(o1) {
        ga_Vector2 t = pointsLookup.getPoint(intersectionSegments(s, tl, tr));
        if(t != null) { s.p1 = t; continue; }
        t = intersectionSegments(s, tr, br);
        if(t != null) { s.p1 = t; continue; }
        t = intersectionSegments(s, br, bl);
        if(t != null) { s.p1 = t; continue; }
        t = intersectionSegments(s, bl, tl);
        if(t != null) { s.p1 = t; continue; }
      }
    }
  }
  static final float eps = 1e-10f;
  ga_Vector2 intersectionSegments(Segment s, ga_Vector2 p1, ga_Vector2 p2)
  {
  
    ga_Vector2 p3 = s.p0, p4 = s.p1;
    float den = (p4.y-p3.y)*(p2.x-p1.x) - (p4.x-p3.x)*(p2.y-p1.y);
    float numa = (p4.x-p3.x)*(p1.y-p3.y) - (p4.y-p3.y)*(p1.x-p3.x);
    float numb = (p2.x-p1.x)*(p1.y-p3.y) - (p2.y-p1.y)*(p1.x-p3.x);
    
    if(Math.abs(numa)<eps && Math.abs(numb)<eps && Math.abs(den)<eps) { // Coincident
      return p1.copy().add(p2).mul(0.5f);
    }
      
    if(Math.abs(den)<eps) return null; // Parallel
    
    float mua = numa / den, mub = numb / den;
    if(mua < 0 || mua > 1 || mub < 0 || mub > 1) return null; // Outside of the segments
    p3=p1.tmp().mul(1-mua);
    p4=p2.tmp2().mul(mua);
    return p3.copy().add(p4);
  }
  
  class SegmentSitePair
  {
    Segment seg;
    Site site;
    
    SegmentSitePair(Segment se, Site si) { seg = se; site = si; }
  }
  
  void createPolygons()
  {
    // Register segments to corresponding sites
    LinkedList<SegmentSitePair> queue = new LinkedList<SegmentSitePair>();
    for(Segment s : segments) {
      if(s.p0 == s.p1) continue; // 0 length 
      if(!s.s0.addSegment(s)) queue.offer(new SegmentSitePair(s, s.s0));
      if(!s.s1.addSegment(s)) queue.offer(new SegmentSitePair(s, s.s1));
    }
    
    for(int i=0; !queue.isEmpty() && i<10; i++) {
      Iterator<SegmentSitePair> iter = queue.iterator();
      while(iter.hasNext()) {
        SegmentSitePair s = iter.next();
        if(s.site.addSegment(s.seg)) iter.remove();
      }
    }
    
    if(queue.size() > 0) {
      ok = false;
      System.out.println("Queue non empty : " + queue.size());
      for(SegmentSitePair s : queue) {
      //  println("(" + s.seg.p0.x + ":" + s.seg.p0.y + ") (" + s.seg.p1.x + ":" + s.seg.p1.y + ") " + s.site);
        System.out.println(s.seg.p0 + " " + s.seg.p1 + " " + s.site);
      }
    }
    
    // Removing empty sites (it happens)
/*    Iterator<Site> it = sites.iterator();
    int i=0;
    while(it.hasNext()) {
      Site s = it.next();
      if(s.points.size() == 0) {
        println("Removed site : " + i + " (" + s.pos.x + ":" + s.pos.y + ")");
        it.remove();
      }
      i++;
    }
    */
    // Closing holes (at the boundaries)
    for(Site s : sites) {
      if(s.points.isEmpty()) continue;
      if(s.points.getFirst() == s.points.getLast()) continue;
      int test = 0;
      ga_Vector2 p = s.points.getFirst();
      if(p.x < xmin + epsilon) test += 1;
      else if(p.x > xmax - epsilon) test += 2;
      if(p.y < ymin + epsilon) test += 4;
      else if(p.y > ymax - epsilon) test += 8;
      
      p = s.points.getLast();
      if(p.x < xmin + epsilon) test += 1;
      else if(p.x > xmax - epsilon) test += 2;
      if(p.y < ymin + epsilon) test += 4;
      else if(p.y > ymax - epsilon) test += 8;
      
      switch(test) {
        case 5: s.points.addFirst(tl); s.points.addLast(tl); break;
        case 6: s.points.addFirst(tr); s.points.addLast(tr); break;
        case 9: s.points.addFirst(bl); s.points.addLast(bl); break;
        case 10: s.points.addFirst(br); s.points.addLast(br); break;
        default: s.points.addLast(s.points.getFirst());
      }
    }
    
    // Make sure all points are within the boundaries
    for(ga_Vector2 p : pointsLookup.getPoints()) {
      p.x = m_MathUtils.clamp(p.x, xmin, xmax);
      p.y = m_MathUtils.clamp(p.y, ymin, ymax);
    }
  }
}
