package reuze.pending;
import reuze.pending.OptimizationPairwiseI;

public class SimulatedAnnealing {

  /**
   	 * The delta change in temperature 0.5<delta<1.
   	 */
  public double delta;
  /**
   	 * The current temperature.
   	 */
  public double temperature;

  /**
   	 * The current cost.
   	 */
  public double current;

  /**
   	 * The best cost.
   	 */
  public double best;

  /**
   	 * The current order of entities.
   	 */
  public int order[];

  /**
   	 * The best order of entities.
   	 */
  public int minimalorder[];
  /**
   	 * Defines optimization costs.
   	 */
  OptimizationPairwiseI client;
  private int cycle, sameCount;
  /**
   	 * Constructor
   	 *
         * @param client interface to cost client.
   	 * @param size of the problem.
   	 * @param temp starting temperature.
         * @param delta fraction to multiply times temperature >=0.5 && <1.
   	 */
  public SimulatedAnnealing(OptimizationPairwiseI client, int size, double temp, double delta) {
    order = new int[size];
    minimalorder = new int[size];
    reset(client, size, temp, delta);
  }
  public double getCost() {
    return best;
  }
  public void reset(OptimizationPairwiseI client, int size, double temp, double delta)
  {
    temperature=temp;
    if (delta<=0.5) delta = 0.5;
    if (delta>=1) delta=0.99;
    this.delta=delta;
    for (int i = 0; i < order.length; i++) //permutation vector initialized 0..n-1
      order[i]=minimalorder[i]=i;
    best = current = Integer.MAX_VALUE;
    this.client=client;
    cycle=1;
    sameCount=0;
  }

  /**
   	 * Should annealing take place.
   	 *
   	 * @param E value to heat or cool.
   	 * @return True if annealing should take place.
   	 */
  private boolean anneal(double E) {
    if (temperature < 1.0E-4) return E>0;
    double x= -E / temperature;
    if (x>=0) return true;
    if (x<-8) return false;
    return (Math.random() < Math.exp(x));
  }

  /**
   	 * perform the simulated annealing.
   	 */
  public boolean compute()
  {
    int n=order.length;
    if (sameCount>=50) {
      System.out.println("Solution found at Cycle=" + cycle + ",Cost=" + best + ",Temp=" + temperature );
      return false;
    }
    // make adjustments to order(annealing)
    for (int j2 = 0; j2 < n*n; j2++) {
      int i1 = (int)Math.floor((double)n * Math.random());
      int j1 = (int)Math.floor((double)n * Math.random());
      if (i1>n-2) i1=0;
      if (j1>n-2) j1=0;
      if (i1==j1) continue;
      double old=client.getCost(order[i1], order[i1 + 1]) + client.getCost(order[j1], order[j1 + 1]);
      double guess =  client.getCost(order[i1], order[j1]) +client.getCost(order[i1 + 1], order[j1 + 1]);
      if ((guess<old||temperature>9.5) && anneal(guess/old)) {
        if (j1 < i1) {
          int k1 = i1;
          i1 = j1;
          j1 = k1;
        }
        for (; j1 > i1; j1--,i1++) {
          int i2 = order[i1 + 1];
          order[i1 + 1] = order[j1];
          order[j1] = i2;
        }
      }
    }

    // See if this improved anything
    current = client.getTotalCost(order);
    if (current < best) {
      best = current;
      System.arraycopy(order, 0, minimalorder, 0, n);
      sameCount=0;
      // update the screen
      System.out.println("Cycle=" + cycle + ",Cost=" + best + ",Temp=" + temperature );
    } 
    else {
      sameCount++;
      System.arraycopy(minimalorder, 0, order, 0, n);
    }
    temperature = delta * temperature;
    cycle++;
    return true;
  }
}
