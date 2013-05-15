//Program Parameters 
int FRAMERATE = 100; 
int WINDOW_WIDTH = 900; 
int WINDOW_HEIGHT = 700; 

//Simulation Parameters 
int SIMULATION_WIDTH = 400; 
int SIMULATION_HEIGHT = 400; 
int NUMBER_OF_ANTS = 30; 
int NUMBER_OF_MINES = 80; 
int NUMBER_OF_STEPS_PER_GENERATION = 2000; 

//Neural Network Parameters 
int[] NEURAL_NETWORK_LAYERS = new int[] 
{ 
  4,15,15,15,2 
}; 
float NEURAL_NETWORK_INITIAL_LOW = -10; 
float NEURAL_NETWORK_INITIAL_HIGH = 10; 

//Ants Parameters 
double MAXIMUM_TURNING_RATE = 0.3; 

//GA Parameters 
double CROSSOVER_RATE = 0.7; 
double MUTATION_RATE = 0.1; 
double MAXIMUM_PERTURBATION = 0.3; 
int ELITISM = 4;

