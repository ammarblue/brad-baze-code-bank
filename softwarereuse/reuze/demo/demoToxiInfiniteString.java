package reuze.demo;
import java.util.Iterator;

import com.software.reuze.ga_Vector2;
import com.software.reuze.pma_BehaviorAttraction;
import com.software.reuze.pma_BehaviorForceGravity;
import com.software.reuze.pma_ParticleVerlet;
import com.software.reuze.pma_PhysicsVerlet;
import com.software.reuze.pma_Spring;
import com.software.reuze.pma_i_BehaviorParticle;
import com.software.reuze.z_Colors;

import processing.core.PApplet;

public class demoToxiInfiniteString  extends PApplet {
//  by Amnon Owed (05/05/2011)
//  minor refactorings by Karsten Schmidt (06/05/2011)
pma_PhysicsVerlet physics;
pma_ParticleVerlet prev;

int continuous,current; // variables to create a new continuous line on each mouse drag
 
public void setup() {
  size(1280,720,P3D);
  physics = new pma_PhysicsVerlet();
  // add gravity in positive Y direction
  physics.addBehavior(new pma_BehaviorForceGravity(new ga_Vector2(0,0.1f)));
  // set the stroke weight of the line
  strokeWeight(2);
}
 
public void draw() {
  background(255);
  // update all the physics stuff (particles, springs, gravity)
  physics.update();
 
  // draw a line segment for each spring and change the color of it based on the x position
  for(pma_Spring s : physics.springs) {
    // map the direction of each spring to a hue
    float currHue=map(s.b.tmp().sub(s.a).heading(),-PI,PI,0,1);
    // define a color in HSV and convert into ARGB format (32bit packed integer)
    stroke(z_Colors.newHSV(currHue,1,1).toARGB());
    line(s.a.x,s.a.y,s.b.x,s.b.y);
  }
 
  // remove stuff that is off the screen to keep things running smoothly ;-)
  removeOffscreen();
}
 
void removeOffscreen() {
  // remove off-screen springs
  for (Iterator<pma_Spring> i=physics.springs.iterator(); i.hasNext();) {
    pma_Spring s=i.next();
    if (s.a.y > height+100 || s.b.y > height+100) {
      i.remove();
    }
  }
 
  // remove off-screen particles &amp; behaviors
  for (int i=physics.particles.size()-1; i>=0; i--) {
    pma_ParticleVerlet p = physics.particles.get(i);
    if (p.y > height+200) {
      physics.removeParticle(p);
      pma_i_BehaviorParticle b = physics.behaviors.get(i+1);
      physics.removeBehavior(b);
    }
  }
}
 
public void mouseDragged() {
  // create a locked (unmovable) particle at the mouse position
  pma_ParticleVerlet p = new pma_ParticleVerlet(mouseX,mouseY);
  p.lock();
  // if there is at least one particle and this is the current continuous line
  if (physics.particles.size() > 0 && continuous == current) {
    // get the previous particle (aka the last in the list)
    pma_ParticleVerlet prev = physics.particles.get(physics.particles.size()-1);
    // create a spring between the previous and the current particle of length 10 and strength 1
    pma_Spring s = new pma_Spring(p,prev,10,1);
    // add the spring to the physics system
    physics.addSpring(s);
  } else {
    current = continuous;
  }
  // unlock previous particle
  if (prev!=null) {
    prev.unlock();
  }
  // add the particle to the physics system
  physics.addParticle(p);
  // create a forcefield around this particle with radius 20 and force -1.5 (aka push)
  pma_i_BehaviorParticle b = new pma_BehaviorAttraction(p,20,-1.5f);
  // add the behavior to the physics system (will be applied to all particles)
  physics.addBehavior(b);
  // make current particle the previous one...
  prev=p;
}
 
public void mouseReleased() {
  if (prev!=null) {
    prev.unlock();
  }
  continuous++;
}
}