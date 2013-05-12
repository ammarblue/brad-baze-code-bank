import processing.core.PApplet;

// Phenotype -- the external expression of the genotype
// can be evaluated

public class Phenotype extends PApplet {

	float m_width;
	float m_height;
	float m_depth;
	evoalgo e;
	Phenotype(Genotype g,evoalgo in) {
		e=in;
		m_width = g.m_genes[0] * width;
		m_height = g.m_genes[1] * height;
		m_depth = g.m_genes[2] * height;
	}

	public void draw() {
		e.box(m_width, m_height, m_depth);
	}

	float evaluate() {
		float fitness = 0.0f;
		fitness += sq(m_width + m_height + m_depth);
		fitness -= m_width * m_height * m_depth;
		return fitness;
	}
}
