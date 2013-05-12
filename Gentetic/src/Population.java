import java.util.Arrays;

import processing.core.PApplet;

public class Population extends PApplet {
	Individual[] m_pop;
	evoalgo e;
	Population(evoalgo in) {
		e=in;
		m_pop = new Individual[100];
		for (int i = 0; i < m_pop.length; i++) {
			m_pop[i] = new Individual(e);
			m_pop[i].evaluate();
		}
		Arrays.sort(m_pop);
	}

	void evolve() {
		Individual a = select();
		Individual b = select();
		Individual x = m_pop[0].breed(a, b);
		x.evaluate();
		m_pop[0] = x;
		Arrays.sort(m_pop);
	}

	Individual select() {
		// Selection requires some form of bias to fitter individuals,
		// while
		int which =  floor((100.0f - 1e-6f) * (1.0f - sq(random(0, 1))));
		return m_pop[which];
	}
}
