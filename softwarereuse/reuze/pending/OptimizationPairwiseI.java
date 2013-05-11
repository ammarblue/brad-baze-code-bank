package reuze.pending;

public interface OptimizationPairwiseI {
	double getCost(int i, int j);
	double getTotalCost(int order[]);
}
