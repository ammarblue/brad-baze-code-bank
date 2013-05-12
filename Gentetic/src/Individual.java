// An individual has both a genotype and a phenotype

public class Individual implements Comparable {
	Genotype m_genotype;
	Phenotype m_phenotype;
	float m_fitness;
	evoalgo e;
	Individual(evoalgo in) {
		e=in;
		m_genotype = new Genotype();
		m_phenotype = new Phenotype(m_genotype,e);
		m_fitness = 0.0f;
	}

	public int compareTo(Object obj_b) {
		Individual b = (Individual) obj_b;
		if (m_fitness > b.m_fitness) {
			return 1;
		} else if (m_fitness < b.m_fitness) {
			return -1;
		}
		return 0;
	}

	void Draw() {
		m_phenotype.draw();
	}

	void evaluate() {
		m_fitness = m_phenotype.evaluate();
	}

	Individual breed(Individual a, Individual b) {
		Individual c = new Individual(e);
		c.m_genotype = m_genotype.crossover(a.m_genotype, b.m_genotype);
		c.m_genotype.mutate();
		c.m_phenotype = new Phenotype(c.m_genotype,e);
		return c;
	}
}
