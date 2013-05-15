import processing.core.PApplet;

public class Genetics extends PApplet{
float MUTATION_RATE = 0.2;
float MUTATION_SIZE = 0.1;

void mateFish(Fish f1, Fish f2) {
  // Uniform crossover
  float[] genome1 = f1.network.getWeightGenome();
  float[] genome2 = f2.network.getWeightGenome();
  
  float[] offspringGenome = new float[genome1.length];
  
  for (int i = 0; i < offspringGenome.length; i++) {
    if (random(0,1) < 0.5) {
      offspringGenome[i] = genome1[i];
    } else {
      offspringGenome[i] = genome2[i];
    }
    if (random(0,1) < MUTATION_RATE) {
      offspringGenome[i] += random(MUTATION_SIZE);
    }
  }
  
  for (int i = 0; i < 2; i++) {
    Fish f = f1.world.addNewFish(random(0,width),random(0,height),10,8);
    f.setRGBZ(int(random(0,255)), int(random(0,255)), int(random(0,255)), 100); 
    f.viewWidth = 80;
    f.viewDistance = 1024;
    f.numViewSensors = NUM_VIEW_SENSORS;
    f.network.setWeightGenome(offspringGenome);
    //f.network.setWeightGenome(genome1);
  }
  
  f1.world.removeFish(f1);
  f2.world.removeFish(f2);
}
}  
  
