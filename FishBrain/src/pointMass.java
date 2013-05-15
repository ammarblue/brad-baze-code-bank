import java.util.HashMap;

import processing.core.PApplet;

public class pointMass extends PApplet{
  World world;
  float[] position;
  float[] prevPosition;
  float[] velocity;
  float mass;
  Force[] forces;
  float size;
  HashMap<pointMass, Float> rodConstraints;
  HashMap<pointMass, Float> distanceConstraints;
  HashMap<pointMass, Float[]> springConstraints;
  int[] massRGBZ = {255,255,255,160};

  pointMass(World world, float mass, float size) {
    world.addPointMass(this);
    this.position = new float[2];
    this.prevPosition = new float[2];
    this.velocity = new float[2];
    this.forces = new Force[0];
    this.mass = mass;
    this.size = size;
    rodConstraints = new HashMap<pointMass, Float>();
    distanceConstraints = new HashMap<pointMass, Float>();
    springConstraints = new HashMap<pointMass, Float[]>();
  }

  void setPosition(float x, float y) {
    this.position[0] = x;
    this.position[1] = y;
  }

  void setPrevPosition(float x, float y) {
    this.prevPosition[0] = x;
    this.prevPosition[1] = y;
  }


  void addExistingForce(Force f) {
    forces = (Force[])append(forces, f);
    int id = forces.length - 1;
    f.id = id;
    f.pMass = this;
  }

  Force addNewForce(float x, float y) {
    Force f = new Force(this);
    f.setVector(x, y);
    return f;
  }

  void removeForce(int id) {
    if (id+1 <= forces.length && id >= 0) {
      Force[] newForces = new Force[forces.length-1];
      int k = 0;
      for (int i = 0; i < forces.length-1; i++) {
        if (i != id) {
          if (k == 0) {
            newForces[i] = forces[i];
          } 
          else {
            newForces[i] = forces[i+1];
          }
        } 
        else {
          newForces[i] = forces[i+1];
          k = 1;
        }
      }
      forces = newForces;
    }
  }

  float[] getTotalForces() {
    float[] total = {
      0, 0
    };
    for (int i = 0; i<forces.length; i++) {
      total[0] += forces[i].vector[0];
      total[1] += forces[i].vector[1];
    }
    return total;
  }

  void applyRodConstraint(pointMass pmass, float dLength) {
    rodConstraints.put(pmass, dLength);
  }
  
  void applyDistanceConstraint(pointMass pmass, float distance) {
    distanceConstraints.put(pmass, distance);
  }
  
  void applySpringConstraint(pointMass pmass, float distance, float strength) {
    Float[] values = {distance, strength};
    springConstraints.put(pmass, values);
  }

  void step() {
    float newPositionX = (2-world.damping)*position[0] - (1-world.damping)*prevPosition[0];// + getTotalForces()[0]/mass*sq(world.timeStep);
    float newPositionY = (2-world.damping)*position[1] - (1-world.damping)*prevPosition[1];// + getTotalForces()[1]/mass*sq(world.timeStep);
    prevPosition[0] = position[0];
    prevPosition[1] = position[1];
    position[0] = newPositionX;
    position[1] = newPositionY;

    // Calculate rod constraint forces
    Iterator i = rodConstraints.entrySet().iterator();
    while (i.hasNext ()) {
      Map.Entry me = (Map.Entry)i.next();

      pointMass p2 = (pointMass)(me.getKey());
      
      float deltaX = position[0] - p2.position[0];
      float deltaY = position[1] - p2.position[1];
      float distance = sqrt((deltaX*deltaX) + (deltaY*deltaY));
      float deltaP = (distance - (Float)me.getValue())/2;
      
      position[0] = position[0] - deltaX/distance*deltaP;
      position[1] = position[1] - deltaY/distance*deltaP;
      
      p2.position[0] = p2.position[0] + deltaX/distance*deltaP;
      p2.position[1] = p2.position[1] + deltaY/distance*deltaP;
    }
    
    // Calculate distance constraint forces
    i = distanceConstraints.entrySet().iterator();
    while (i.hasNext ()) {
      Map.Entry me = (Map.Entry)i.next();
      
      pointMass p2 = (pointMass)(me.getKey());
      
      float deltaX = position[0] - p2.position[0];
      float deltaY = position[1] - p2.position[1];
      float distance = ((deltaX*deltaX) + (deltaY*deltaY));
      
      if (distance < sq((Float)me.getValue())) {
        distance = sqrt(distance);
        float deltaP = (distance - (Float)me.getValue())/2;
        
        position[0] = position[0] - deltaX/distance*deltaP;
        position[1] = position[1] - deltaY/distance*deltaP;
        
        p2.position[0] = p2.position[0] + deltaX/distance*deltaP;
        p2.position[1] = p2.position[1] + deltaY/distance*deltaP;
      }
    }
    
    // Calculate spring constraint forces
    i = springConstraints.entrySet().iterator();
    while (i.hasNext ()) {
      Map.Entry me = (Map.Entry)i.next();
      pointMass p2 = (pointMass)(me.getKey());
      
      Float[] values = (Float[])(me.getValue());
      
      float deltaX = position[0] - p2.position[0];
      float deltaY = position[1] - p2.position[1];
      float distance = sqrt((deltaX*deltaX) + (deltaY*deltaY));
      float deltaP = (distance - values[0])*(values[1]);
      
      position[0] = position[0] - deltaX/distance*deltaP;
      position[1] = position[1] - deltaY/distance*deltaP;
      
      p2.position[0] = p2.position[0] + deltaX/distance*deltaP;
      p2.position[1] = p2.position[1] + deltaY/distance*deltaP;
    }
  }
}

class Force {
  float[] vector;
  pointMass pMass;
  int id;

  Force(pointMass pMass) {
    pMass.addExistingForce(this);
    vector = new float[2];
  }

  void setVector(float x, float y) {
    this.vector[0] = x;
    this.vector[1] = y;
  }
}


class World {
  pointMass[] masses;
  Fish[] fishes;
  int[] worldSize;
  float timeStep;
  float damping;
  boolean closedSystem;
  float time;
  boolean displayForces;
  float longestLifespan;
  float[] bestGenome;


  World(int[] worldSize, pointMass[] masses, float timeStep) {
    this.worldSize = worldSize;
    this.timeStep = timeStep;
    fishes = new Fish[0];
    time = 0;
    displayForces = true;
    if (masses != null) {
      this.masses = masses;
    } 
    else {
      this.masses = new pointMass[0];
    }
  }

  void step() {
    for (int i = 0; i < masses.length; i++) {
      pointMass current = masses[i];

      current.step();

      if (closedSystem == true) {
        if (current.position[0] < current.size/2) {
          current.prevPosition[0] = current.position[0];
          current.position[0] = current.size/2;
        }
        if (current.position[0] > width - current.size/2) {
          current.prevPosition[0] = current.position[0];
          current.position[0] = width - current.size/2;
        }
        if (current.position[1] < current.size/2) {
          current.prevPosition[1] = current.position[1];
          current.position[1] = current.size/2;
        }
        if (current.position[1] > height - current.size/2) {
          current.prevPosition[1] = current.position[1];
          current.position[1] = height - current.size/2;
        }
      }
    }
    time+=timeStep;
    for (int i = 0; i < fishes.length; i++) {
      Fish current = fishes[i];
      
      if (current.masses[0] != null) {
        current.swim();
        current.see();
        current.think();
        current.live();
      }
    }
  }

  void display() {
    for (int i = 0; i < masses.length; i++) {
      pointMass current = masses[i];
      noStroke();
      fill(current.massRGBZ[0], current.massRGBZ[1], current.massRGBZ[2], current.massRGBZ[3]);
      ellipse(current.position[0], current.position[1], current.size, current.size);

      // Draw Constraints
      Iterator j = current.rodConstraints.entrySet().iterator();
      while (j.hasNext ()) {
        Map.Entry me = (Map.Entry)j.next();
        pointMass p2 = (pointMass)(me.getKey());
        strokeWeight(current.size);
        stroke(current.massRGBZ[0], current.massRGBZ[1], current.massRGBZ[2], current.massRGBZ[3]);
        line(current.position[0], current.position[1], p2.position[0], p2.position[1]);
      }
    }
  }

  void addPointMass(pointMass m) {
    masses = (pointMass[])append(masses, m);
    m.world = this;
  }
  
  pointMass addNewPointMass(float posx, float posy, float mass, float mSize) {
    pointMass m = new pointMass(this, mass, mSize);
    m.setPosition(posx, posy);
    masses = (pointMass[])append(masses, m);
    m.world = this;
    return m;
  }
  
  void removePointMass(pointMass mass) {
    pointMass[] newMasses = new pointMass[masses.length-1];
    int k = 0;
    for (int i = 0; i < masses.length-1; i++) {
      if (masses[i] != mass) {
        if (k == 0) {
          newMasses[i] = masses[i];
        } 
        else {
          newMasses[i] = masses[i+1];
        }
      } 
      else {
        newMasses[i] = masses[i+1];
        k = 1;
      }
    }
    masses = newMasses;
  }
  
  void addFish(Fish f) {
    fishes = (Fish[])append(fishes, f);
    f.id = fishes.length-1;
    f.world = this;
  }
  
  Fish addNewFish(float initPositionX, float initPositionY, float size, float mass) {
    Fish f = new Fish(this,initPositionX,initPositionY, size, mass);
    fishes = (Fish[])append(fishes, f);
    f.id = fishes.length-1;
    return f;
  }
  
  void removeFish(Fish fish) {
    for (int i = 6; i >= 0; i--) {
      removePointMass(fish.masses[i]);
    }
    Fish[] newFishes = new Fish[fishes.length-1];
    int k = 0;
    for (int i = 0; i < fishes.length-1; i++) {
      if (fish != fishes[i]) {
        if (k == 0) {
          newFishes[i] = fishes[i];
        } 
        else {
          newFishes[i] = fishes[i+1];
        }
      } 
      else {
        newFishes[i] = fishes[i+1];
        k = 1;
      }
    }
    fishes = newFishes;
  }
  
  void replaceFish(Fish fish) {
    if (time - fish.birthTime > longestLifespan) {
      longestLifespan = time - fish.birthTime;
      bestGenome = fish.network.getWeightGenome();
    }
    
    Fish f = w1.addNewFish(random(0,width),random(0,height),10,8);
    f.setRGBZ(int(random(0,255)), int(random(0,255)), int(random(0,255)), 100); 
    f.viewWidth = 80;
    f.viewDistance = 1024;
    f.numViewSensors = NUM_VIEW_SENSORS;
    f.network.setWeightGenome(bestGenome);
    removeFish(fish);
  }
}
