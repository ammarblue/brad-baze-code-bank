import java.util.ArrayList;

import processing.core.PApplet;

public class NeuralNet extends PApplet
{ 
  ArrayList layers = new ArrayList(); 
  int largestLayer = 0; 
  int connections = 0; 
  double largestWeight = Double.MIN_VALUE; 
  double smallestWeight = Double.MAX_VALUE; 

  NeuralNet() 
  { 
  } 

  void setGenome(double[] genome) 
  { 
    largestWeight = Double.MIN_VALUE; 
    smallestWeight = Double.MAX_VALUE; 

    int pos = 0; 

    for (int i = 1; i < layers.size(); i++) 
    { 
      Node[] layer = (Node[])layers.get(i); 
      for (int j = 0; j < layer.length; j++) 
      { 
        for (int k = 0; k < layer[j].weights.length; k++) 
        { 
          layer[j].weights[k] = genome[pos]; 
          if (genome[pos] > largestWeight) largestWeight = genome[pos];  
          else if (genome[pos] < smallestWeight) smallestWeight = genome[pos]; 
          pos++; 
        } 
      } 
    } 
  } 

  double[] getGenome() 
  { 
    double[] genome = new double[connections]; 
    int pos = 0; 

    for (int i = 1; i < layers.size(); i++) 
    { 
      Node[] layer = (Node[])layers.get(i); 
      for (int j = 0; j < layer.length; j++) 
      { 
        for (int k = 0; k < layer[j].weights.length; k++) 
        { 
          genome[pos] = layer[j].weights[k]; 
          pos++; 
        } 
      } 
    } 

    return genome; 
  } 

  void addLayer(int neurons) 
  { 
    if (neurons > largestLayer) largestLayer = neurons; 

    Node[] nodes = new Node[neurons]; 
    for (int i = 0; i<neurons; i++) 
    { 
      double[] weights; 
      if (layers.size() > 0) 
      { 
        Node[] prevLayer = (Node[]) layers.get(layers.size() - 1); 
        weights = new double[prevLayer.length + 1]; 
        for (int j = 0; j < weights.length; j++) 
        { 
          weights[j] = random(NEURAL_NETWORK_INITIAL_LOW, NEURAL_NETWORK_INITIAL_HIGH); 

          if (weights[j] > largestWeight) largestWeight = weights[j];  
          else if (weights[j] < smallestWeight) smallestWeight = weights[j]; 

          connections++; 
        } 
      } 
      else 
      { 
        weights = null; 
      } 
      nodes[i] = new Node(weights); 
    } 
    layers.add(nodes); 
  } 

  double[] evaluate(double[] inputs) 
  { 
    for (int currentLayer = 1; currentLayer < layers.size(); currentLayer++) 
    { 
      Node[] layer = (Node[])layers.get(currentLayer); 
      double[] outputs = new double[layer.length]; 
      for (int node = 0; node < layer.length; node++) 
      { 
        outputs[node] = layer[node].output(inputs); 
      } 
      inputs = outputs; 
    } 
    return inputs; 
  } 
} 

class Node 
{ 
  double[] weights; 

  Node(double[] weights) 
  { 
    this.weights = weights; 
  } 

  double output(double[] inputs) 
  { 
    double output = 0; 
    for (int i = 0; i < inputs.length; i++) 
    { 
      output += inputs[i] * weights[i]; 
    } 
    output -= weights[weights.length-1]; 

    return sigmoid(output); 
  } 

  double sigmoid(double a) 
  { 
    return 1/(1+Math.pow(Math.E,-a)); 
  } 
} 


