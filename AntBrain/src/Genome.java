public class Genome implements Comparable 
{ 
  double[] genome; 
  int fitness; 

  Genome(double[] genome, int fitness) 
  { 
    this.genome = genome; 
    this.fitness = fitness; 
  } 

  public int compareTo(Object o) 
  { 
    Genome g = (Genome) o; 
    if (this.fitness < g.fitness) 
    { 
      return 1; 
    } 
    if (this.fitness > g.fitness) 
    { 
      return -1; 
    } 
    return 0; 
  } 
}

