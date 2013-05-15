import java.util.HashMap;

import processing.core.PApplet;

public class Neuron extends PApplet{

  Network network;
  int id;
  HashMap<Integer, Float> weights = new HashMap<Integer, Float>();
  HashMap connections = new HashMap();
  int[] outputs = new int[0];
  float sum;
  float bias;
  float signalThreshold;
  float output;

  int posx;
  int posy;

  int[] neuronRGBZ = {255, 255, 255, NEURON_Z};

  char type; //"i","h", or "o"

  Neuron(char nType, Network network) {
    bias = random(-0.5f,0.5f);
    sum = 0;
    type = nType;
    switch(nType) {
    case 'i':
      neuronRGBZ[0] = 70;
      neuronRGBZ[1] = 191;
      neuronRGBZ[2] = 232;
      network.addInputNeuron(this);
      break;
    case 'h':
      neuronRGBZ[0] = 173;
      neuronRGBZ[1] = 26;
      neuronRGBZ[2] = 154;
      network.addHiddenNeuron(this);
      break;
    case 'o':
      neuronRGBZ[0] = 83;
      neuronRGBZ[1] = 212;
      neuronRGBZ[2] = 57;
      network.addOutputNeuron(this);
      break;
    }
    if (network.neurons.length == 1) {
      posx = int(random(100, width-100));
      posy = int(random(100, height-100));
    } else {
      float angle = random(0,PI*2);
      posx = network.neurons[id-1].posx + int(cos(angle)*MAX_NEURON_DISTANCE);
      while (posx > width - NEURON_DISPLAY_SIZE || posx < NEURON_DISPLAY_SIZE) {
        angle = random(0,PI*2);
        posx = network.neurons[id-1].posx + int(cos(angle)*MAX_NEURON_DISTANCE);
      }
      posy = network.neurons[id-1].posy + int(sin(angle)*MAX_NEURON_DISTANCE);
      while (posy > height - NEURON_DISPLAY_SIZE || posy < NEURON_DISPLAY_SIZE) {
        angle = random(0,PI*2);
        posy = network.neurons[id-1].posy + int(sin(angle)*MAX_NEURON_DISTANCE);
      }
    }
  }

  void makeOutputConnection(int nId) {
    outputs = append(outputs, nId);
    network.neurons[nId].weights.put(id, random(MIN_WEIGHT, MAX_WEIGHT));
    connections.put(nId, new OutputConnection(network,id,nId));
  }
  
  void displayConnections() {
    Iterator i = connections.entrySet().iterator();
    
    while (i.hasNext()) {
      Map.Entry me = (Map.Entry)i.next();
      OutputConnection c = (OutputConnection)me.getValue();
      c.display();
    }
  }
}

class Network {
  int[] inputNeurons = new int[0];
  int[] hiddenNeurons = new int[0];
  int[] outputNeurons = new int[0];
  Neuron[] neurons = new Neuron[0];
  int numInputs = 0;
  int numHidden = 0;
  int numHiddenLayers = 0;
  int numHiddenPerLayer = 0;
  int numOutputs = 0;
  int totalNeurons = 0;
  char type;
  int numConnections;

  void addInputNeuron(Neuron n) {
    neurons = (Neuron[])append(neurons, n);
    numInputs += 1;
    totalNeurons += 1;
    int id = neurons.length - 1;
    inputNeurons = append(inputNeurons, id);
    n.id = id;
    n.network = this;
  }

  void addHiddenNeuron(Neuron n) {
    neurons = (Neuron[])append(neurons, n);
    numHidden += 1;
    totalNeurons += 1;
    int id = neurons.length - 1;
    hiddenNeurons = append(hiddenNeurons, id);
    n.id = id;
    n.network = this;
  }

  void addOutputNeuron(Neuron n) {
    neurons = (Neuron[])append(neurons, n);
    numOutputs += 1;
    totalNeurons += 1;
    int id = neurons.length - 1;
    outputNeurons = append(outputNeurons, id);
    n.id = id;
    n.network = this;
  }
  
  Network(char type, int numInputs, int numHiddenLayers, int numHiddenPerLayer, int numOutputs) {
    this.type = type;
    this.numHiddenLayers = numHiddenLayers;
    this.numHiddenPerLayer = numHiddenPerLayer;
    if (type == 'o') { // Open Network, i.e. random connections
      setupOpenNetwork(numInputs,numHiddenLayers*numHiddenPerLayer,numOutputs);
    }
    if (type == 'l') { // Layered Network
      setupLayeredNetwork(numInputs, numHiddenLayers, numHiddenPerLayer, numOutputs);
    }
  }
  
  void setupOpenNetwork(int numInputs, int numHidden, int numOutputs) {
    for (int i = 0; i<numOutputs; i++) {
      Neuron n = new Neuron('o',this);
    }
    for (int i = 0; i<numHidden; i++) {
      Neuron n = new Neuron('h',this);
      int[] possible = outputNeurons;
      for (int j = 0; j<hiddenNeurons.length; j++) {
        possible = append(possible,hiddenNeurons[j]);
      }
      int index = int(random(1,possible.length))-1;
      int selection = possible[index];
      n.makeOutputConnection(selection);
      numConnections+=1;
    }
    for (int i = 0; i<numInputs; i++) {
      Neuron n = new Neuron('i',this);
      int index = int(random(1,hiddenNeurons.length))-1;
      int selection = hiddenNeurons[index];
      n.makeOutputConnection(selection);
      numConnections+=1;
    }
  }
  
  void setupLayeredNetwork(int numInputs, int numHiddenLayers, int numHiddenPerLayer, int numOutputs) {
    this.numHiddenLayers = numHiddenLayers;
    this.numHiddenPerLayer = numHiddenPerLayer;
    for (int i = 0; i<numOutputs; i++) {
      Neuron n = new Neuron('o',this);
      n.posy = int(height - NEURON_DISPLAY_SIZE*2);
      n.posx = int((width-NEURON_DISPLAY_SIZE*4)/(numOutputs-1)*i+NEURON_DISPLAY_SIZE*2);
    }
    int[] prevLayer = new int[numHiddenPerLayer];
    for (int i = 0; i<numHiddenLayers; i++) {
      int[] thisLayer = new int[numHiddenPerLayer];
      for (int j = 0; j<numHiddenPerLayer; j++) {
        Neuron n = new Neuron('h',this);
        n.posx = int((width-NEURON_DISPLAY_SIZE*4)/(numHiddenPerLayer-1)*j+NEURON_DISPLAY_SIZE*2);
        n.posy = int((height-NEURON_DISPLAY_SIZE*16)/(numHiddenLayers-1)*(numHiddenLayers-i-1)+NEURON_DISPLAY_SIZE*8);
        if (i == 0) {
          for (int k = 0; k<outputNeurons.length; k++) {
            n.makeOutputConnection(outputNeurons[k]);
            numConnections+=1;
          }
        } else {
          for (int k = 0; k<numHiddenPerLayer; k++) {
            n.makeOutputConnection(prevLayer[k]);
            numConnections+=1;
          }
        }
        thisLayer[j] = n.id;
      }
      arrayCopy(thisLayer,prevLayer);
    }
    for (int i = 0; i<numInputs; i++) {
      Neuron n = new Neuron('i',this);
      for (int j = 0; j<numHiddenPerLayer; j++) {
        n.makeOutputConnection(prevLayer[j]);
        numConnections+=1;
      }
      n.posy = int(NEURON_DISPLAY_SIZE*2);
      n.posx = int((width-NEURON_DISPLAY_SIZE*4)/(numInputs-1)*i+NEURON_DISPLAY_SIZE*2);
    }
  }
  
  void remakeNetwork() {
    int oldNumInputs = numInputs;
    int oldNumHidden = numHidden;
    int oldNumOutputs = numOutputs;
    int oldNumHiddenLayers = numHiddenLayers;
    int oldNumHiddenPerLayer = numHiddenPerLayer;
    inputNeurons = new int[0];
    hiddenNeurons = new int[0];
    outputNeurons = new int[0];
    neurons = new Neuron[0];
    numInputs = 0;
    numHidden = 0;
    numOutputs = 0;
    numHiddenLayers = 0;
    numHiddenPerLayer = 0;
    totalNeurons = 0;
    numConnections=0;
    if (type == 'o') {
      setupOpenNetwork(oldNumInputs,oldNumHidden,oldNumOutputs);
    }
    if (type == 'l') {
      setupLayeredNetwork(oldNumInputs,oldNumHiddenLayers,oldNumHiddenPerLayer,oldNumOutputs);
    }
  }
  
  void display() {
    ellipseMode(CENTER);
    for (int i = 0; i < totalNeurons; i++) {
      Neuron current = neurons[i];
      //strokeWeight(1);
      //stroke(current.neuronRGBZ[0]*3, current.neuronRGBZ[1]*3, current.neuronRGBZ[2]*3);
      noStroke();
      float disX = current.posx - mouseX;
      float disY = current.posy - mouseY;
      if(((disX*disX) + (disY*disY)) < (NEURON_DISPLAY_SIZE*NEURON_DISPLAY_SIZE/4) ) {
        //Mouse over neuron
        fill(current.neuronRGBZ[0]*2, current.neuronRGBZ[1]*2, current.neuronRGBZ[2]*2, 255);
        if (mousePressed == true && mouseButton == LEFT && selected == null) {
          selected = current;
        }
      } else {
        //Mouse out of neuron
        fill(current.neuronRGBZ[0], current.neuronRGBZ[1], current.neuronRGBZ[2], current.neuronRGBZ[3]/2);
      }
      ellipse(current.posx, current.posy, NEURON_DISPLAY_SIZE, NEURON_DISPLAY_SIZE);
    }
    for (int i = 0; i < totalNeurons; i++) {
      Neuron current = neurons[i];
      current.displayConnections();
    }
  }
  
  void update() {
    int i = 0;
    for (i = 0; i < neurons.length; i++) {
      Neuron current = neurons[i];
      if (current.type == 'o' || current.type == 'h') {
        float sum = current.sum + current.bias;
        float p = 1.0;
        current.output = (1/(1+pow((float)Math.E,-sum/p))-0.5)*2;
      }
      for (int j = 0; j < current.outputs.length; j++) {
        Neuron out = neurons[current.outputs[j]];
        float currentWeight = out.weights.get(current.id);
        out.sum += current.output * currentWeight;
        OutputConnection c = (OutputConnection)current.connections.get(current.outputs[j]);
        if (display) {
          c.arrowRGBZ[3] = int(255*(abs(current.output)));
        }
      }
      if (display) {
        current.neuronRGBZ[3] = int(105*(abs(current.output))+150);
      }
      current.sum = 0;
    }
  }
  
  float[] getWeightGenome() {
    int k = 0;
    float[] genome = new float[numConnections];
    for (int i = 0; i < neurons.length; i++) {
      Neuron current = neurons[i];
      //HashMap weights = current.weights;
      Iterator j = current.weights.entrySet().iterator();
      while (j.hasNext ()) {
        Map.Entry me = (Map.Entry)j.next();
        genome[k] = (Float)me.getValue();
        k+=1;
      }
    }
    return genome;
  }
  
  void setWeightGenome(float[] genome) {
    int k = 0;
    for (int i = 0; i < neurons.length; i++) {
      Neuron current = neurons[i];
      HashMap weights = current.weights;
      Iterator j = weights.entrySet().iterator();
      while (j.hasNext ()) {
        Map.Entry me = (Map.Entry)j.next();
        weights.put(me.getKey(),genome[k]);
        k+=1;
      }
    }
  }
  
  void mutateWeightGenome(float mutRate, float mutStrength) {
    for (int i = 0; i < neurons.length; i++) {
      Neuron current = neurons[i];
      //HashMap weights = current.weights;
      Iterator j = current.weights.entrySet().iterator();
      while (j.hasNext()) {
        Map.Entry me = (Map.Entry)j.next();
        if (random(0,1) < mutRate) {
          float currWeight = current.weights.get(me.getKey());
          float newWeight = currWeight + random(-mutStrength,mutStrength);
          current.weights.put((Integer)me.getKey(),newWeight);
        }
      }
    }
  }
}

class OutputConnection {
  Network network;
  
  int senderId;
  int receiverId;
  
  int[] arrowRGBZ = new int[4];
  float arrowThickness = ARROW_THICKNESS;
  
  OutputConnection(Network n, int s, int r) {
    network = n;
    senderId = s;
    receiverId = r;
    arrayCopy(network.neurons[senderId].neuronRGBZ,arrowRGBZ);
    arrowRGBZ[3] = ARROW_Z;
  }
  
  void display() {
    stroke(arrowRGBZ[0], arrowRGBZ[1], arrowRGBZ[2], arrowRGBZ[3]/1.4);
    strokeWeight(arrowThickness);
    Neuron sender = network.neurons[senderId];
    Neuron receiver = network.neurons[receiverId];
    //arrow(sender.posx, sender.posy, receiver.posx, receiver.posy);
    float x1 = sender.posx;
    float y1 = sender.posy;
    float x2 = receiver.posx;
    float y2 = receiver.posy;
    float d, normX, normY;
    d = pow((pow((x2-x1), 2) + pow((y2-y1), 2)), 0.5);
    normX = (x2-x1)/d;
    normY = (y2-y1)/d;
    x1 = int(x1+normX*NEURON_DISPLAY_SIZE/2);
    y1 = int(y1+normY*NEURON_DISPLAY_SIZE/2);
    x2 = int(x2-normX*NEURON_DISPLAY_SIZE/2);
    y2 = int(y2-normY*NEURON_DISPLAY_SIZE/2);
    line(x1, y1, x2, y2);
    pushMatrix();
    translate(x2, y2);
    float a = atan2(x1-x2, y2-y1);
    rotate(a);
    line(0, 0, -ARROWHEAD_SIZE, -ARROWHEAD_SIZE*7/5);
    line(0, 0, ARROWHEAD_SIZE, -ARROWHEAD_SIZE*7/5);
    popMatrix();
  }
}
