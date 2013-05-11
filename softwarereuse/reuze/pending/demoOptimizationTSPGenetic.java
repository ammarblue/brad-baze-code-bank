package reuze.pending;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
public class demoOptimizationTSPGenetic extends Frame implements KeyListener {
public static final int numCities=100;
public static final int width=300;
public static final int height=300;
private static int[] cities;
private static int[] x;
private static int[] y;
private static chromosome current;
private static chromosome population[];
private static Random rand;
private int generation=1;
public static void main(String args[]) {
    rand=new Random(37);
    demoOptimizationTSPGenetic k = new demoOptimizationTSPGenetic();
    cities = new int[numCities];
    x = new int[numCities];
    y = new int[numCities];
    population = new chromosome[10];
    for (int i = 0; i < numCities; i++) {
      x[i] = rand.nextInt(width - 10)+10;
      y[i] = rand.nextInt(height - 80)+40;
    }
    for (int i=0; i<population.length; i++) {
        population[i]=k.new chromosome();
        population[i].mutate(numCities);
    }
    Arrays.sort(population, population[0]);
    current=population[0];
    k.setTitle("TSP");
    k.setBackground(Color.white);
    k.resize(width,height);
    k.show();
    k.addKeyListener(k);
}
public void paint(Graphics g) {
    g.setColor(Color.black);
    for (int i = 0; i < cities.length; i++)
        g.fillOval(x[i] - 5, y[i] - 5, 10, 10);
    for (int i = 0; i < cities.length; i++) {
        int icity = current.genes[i];
        if (i != 0) {
          int last = current.genes[i - 1];
          g.drawLine(x[icity], y[icity], x[last], y[last]);
       }
  }
  FontMetrics fm = g.getFontMetrics();
  g.drawString(generation+" "+current.cost, 8, height - fm.getHeight());
}
public int distance(int city1, int city2) {
  if (city1 >= numCities) city1=0;
  if (city2 >= numCities) city2=0;
  int xdiff = x[city1] - x[city2];
  int ydiff = y[city1] - y[city2];
  return xdiff*xdiff + ydiff*ydiff;
}
public class chromosome implements Comparator {
int genes[];
int cost;
public chromosome() {
    genes=new int[numCities];
    b=new BitSet(numCities);
    for (int i=0; i< numCities; i++) genes[i]=i;
    cost=cost();
}
public int cost() {
  int d = 0;
  for (int i = 1; i < genes.length; i++)
    d += distance(genes[i], genes[i - 1]);
  return d;
}
public void mutate(int n) {
  while (--n >= 0) {
    int i1 = rand.nextInt(numCities-1), j1, k1;
    do {j1 = rand.nextInt(numCities-1);} while(j1 == i1);
    int old=distance(genes[i1], genes[i1 + 1]) + distance(genes[j1], genes[j1 + 1]);
    int guess =  distance(genes[i1], genes[j1]) + distance(genes[i1 + 1], genes[j1 + 1]);
    if (guess >= old) continue;
    cost -= old-guess;
    if (j1 < i1) {
      k1 = i1;  i1 = j1;  j1 = k1;
    }
    for (; j1 > i1; j1--, i1++) {
      int i2 = genes[i1 + 1];
      genes[i1 + 1] = genes[j1];
      genes[j1] = i2;
   }
 }
}
public int compare(Object o1, Object o2) {
  return ((chromosome)o1).cost-((chromosome)o2).cost;
}
public void crossover(chromosome dad, chromosome mom) {
  int i=rand.nextInt(numCities);
  while (i < numCities-1) {
      int child=distance(dad.genes[i], mom.genes[i+1]);
      if (child < distance(dad.genes[i],dad.genes[i+1]) &&
          child < distance(mom.genes[i],mom.genes[i+1])) {
        mate(dad, mom, i);
        break;
      }
      i++;
  }
}
BitSet b;
private void mate(chromosome dad, chromosome mom, int i) {
  b.clear();
  if (this == mom) {
      chromosome temp=mom;
      mom=dad;   dad=temp;
  }
  for (int j=0; j<=i; j++) {
      genes[j]=dad.genes[j];
      b.set(genes[j]);
  }
  int j, k=i+1;
  for (j=i+1; j<genes.length; j++) {
      if (b.get(mom.genes[j])) continue;
      genes[k]=mom.genes[j];
      b.set(genes[k++]);
  }
  j=0;
  while (k<genes.length) {
      while (b.get(mom.genes[j])) j++;
      genes[k++]=mom.genes[j++];
  }
  cost=cost();
}
}
public void keyPressed(KeyEvent e) {
}
public void keyReleased(KeyEvent e) {
}
public void keyTyped(KeyEvent e) {
   //selection, top 5
   int n=population.length/2, m;
   for (m=population.length-1; m>1; m--) {
     int i=rand.nextInt(n), j;
     do {j=rand.nextInt(n);} while(i == j);
     population[m].crossover(population[i],population[j]);
     population[m].mutate(numCities);
   }
   population[1].crossover(population[0],population[1]);
   population[1].mutate(numCities);
   population[0].mutate(numCities);
   Arrays.sort(population, population[0]);
   current=population[0];
   generation++;
   repaint();
}
}
