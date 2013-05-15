import processing.core.PApplet;


// ===== CONSTANTS ===== //
public class Main extends PApplet{
char TYPE = 'l';
int NUM_INPUT_NEURONS = 10;
int NUM_HIDDEN_LAYERS = 2;
int NUM_HIDDEN_PER_LAYER = 6;
int NUM_OUTPUT_NEURONS = 2;
int NUM_VIEW_SENSORS = 5;

float MIN_WEIGHT = -4;
float MAX_WEIGHT = 4;

float NEURON_DISPLAY_SIZE = 13;
int MAX_NEURON_DISTANCE = 70;
int NEURON_Z = 180;

int ARROW_Z = 155;
float ARROW_THICKNESS = 1.0;
float ARROWHEAD_SIZE = 3;

// =========================//

// ===== NETWORK SETUP ===== //

Network net;
Neuron selected;

// ========================= //

// ===== WORLD/PHYSICS SETUP ===== //

World w1;
float TIMESTEP = 0.2;
boolean CLOSED_SYSTEM = true;
float DAMPING = 0.02;
int[] WORLDSIZE = {600, 600};

pointMass p1,p2,p3;
Fish fish1;
Force controlForce;
float initPositionX = 256;
float initPositionY = 256;

pointMass selected1;

boolean printed;
boolean display;
boolean drawOutput;
boolean paused;
boolean smooth;

int bgColor = 0;
PFont f;
boolean drawText;

// =============================== //
void setup() {
  smooth();
  background(bgColor);
  f = loadFont("CourierNewPSMT-12.vlw");
  textFont(f,12);
  
  w1 = new World(WORLDSIZE, null, TIMESTEP);
  size(600,600);
  w1.damping = DAMPING;
  w1.closedSystem = CLOSED_SYSTEM;
   
  printed = false;
  display = false;
  drawOutput = true;
  paused = false;
  drawText = false;
  smooth = true;
  
  fish1 = new Fish(w1, random(0,width),random(0,height), 10, 8);
  w1.addFish(fish1);
  net = fish1.network;
  fish1.viewWidth = 80;
  fish1.viewDistance = 1024;
  fish1.numViewSensors = NUM_VIEW_SENSORS;
  
  for (int i = 0; i<0; i++) {
    Fish f = w1.addNewFish(random(0,width),random(0,height),10,8);
    f.viewWidth = 80;
    f.viewDistance = 1024;
    f.numViewSensors = NUM_VIEW_SENSORS;
  }
  
  controlForce = fish1.masses[0].addNewForce(0,0);
}

void draw() {
  background(bgColor);
  if (drawText) {
    fill(255-bgColor);
    text("Left Click to attract a fish",5,20);
    text("Right Click/\"+\" to remove fish",5,35);
    text("Middle Click/\"-\" to add fish",5,50);
    text("\"N\" to toggle neural network display",5,65);
    text("Left Click on neuron to move it",5,80);
    text("\"R\" to reset fish positions",5,95);
    text("\"F\" to toggle forces display",5,110);
    text("\"B\" to toggle black/white background",5,125);
    text("Space to reset n.networks",5,140);
    text("\"T\" to toggle this text",5,155);
  } else {
    fill(255-bgColor);
    text("\"T\" to show controls",5,20);
  }
  
  fill(255-bgColor);
  text(frameRate+"fps",20,590);
  text(w1.fishes.length+" fish",20,575);
  
  if (keyPressed == true && key == '1') {
    net.neurons[net.neurons.length-1].output = 1;
    net.neurons[net.neurons.length-2].output = 1;
    net.neurons[net.neurons.length-3].output = 1;
    net.neurons[net.neurons.length-4].output = 1;
  }
  
  if (!paused){
    w1.step();
    w1.display();
  } else {
    w1.display();
    filter(BLUR, 1.5);
  }

  
  if (display && w1.fishes.length > 0) {
    //filter(BLUR, 1.5);
    w1.fishes[0].network.display();
  }
  
  selected1 = w1.fishes[0].masses[0];

  if (mousePressed == true) {
    if (mouseButton == LEFT && selected == null) {
      selected1.position[0] += (mouseX - selected1.position[0]) * 0.01;
      selected1.position[1] += (mouseY - selected1.position[1]) * 0.01;
      stroke(255-bgColor,200);
      strokeWeight(1);
      line(selected1.position[0],selected1.position[1],mouseX,mouseY);
    }
  }
  
  if (mousePressed == true && selected != null) {
    selected.posx = int(selected.posx + (mouseX - selected.posx) * 0.5);
    selected.posy = int(selected.posy + (mouseY - selected.posy) * 0.5);
    if (printed == false) {
      println("-- Neuron "+selected.id+" --");
      println("Sum: "+selected.sum);
      println("Output: "+selected.output);
      printed = true;
    }
  }
  if (mousePressed == false) {
    selected = null;
    printed = false;
  }

}


void mousePressed() {
  if (mouseButton == RIGHT){
//    for (int i = 0; i<w1.fishes.length; i++) {
//      w1.fishes[i].network.remakeNetwork();;
//    }
//    println("Remade networks.");
    if (w1.fishes.length > 0) {
      w1.removeFish(w1.fishes[w1.fishes.length-1]);
    }
  }
  if (mouseButton == CENTER){
    Fish f = w1.addNewFish(random(0,width),random(0,height),10,8);
    f.setRGBZ(int(random(0,255)), int(random(0,255)), int(random(0,255)), 160); 
    f.viewWidth = 80;
    f.viewDistance = 1024;
    f.numViewSensors = NUM_VIEW_SENSORS;
    println(w1.fishes.length);
  }
}

void keyPressed() {
  switch(key) {
    case 'n':
      display = !display;
      break;
    case 'f':
      w1.displayForces = !w1.displayForces;
      break;
    case 'p':
      paused = !paused;
      break;
    case 'r':
      for (int i = 0; i<w1.fishes.length; i++) {
        w1.fishes[i].setPosition(random(0,width),random(0,height));
      }
      break;
    case 'b':
      bgColor = 255-bgColor;
      break;
    case ' ':
      for (int i = 0; i<w1.fishes.length; i++) {
        w1.fishes[i].network.remakeNetwork();;
      }
      println("Remade networks.");
      break;
    case 't':
      drawText = !drawText;
      break;
    case 's':
      if (smooth) {
        noSmooth();
      } else {
        smooth();
      }
      smooth = !smooth;
      break;
    case '=':
      Fish f = w1.addNewFish(random(0,width),random(0,height),10,8);
      f.setRGBZ(int(random(0,255)), int(random(0,255)), int(random(0,255)), 160); 
      f.viewWidth = 80;
      f.viewDistance = 1024;
      f.numViewSensors = NUM_VIEW_SENSORS;
      break;
    case '-':
      if (w1.fishes.length > 0) {
        w1.removeFish(w1.fishes[w1.fishes.length-1]);
      }
      break;
  }
}
}
