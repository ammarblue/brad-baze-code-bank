package reuze.aifiles;

import com.software.reuze.m_MathUtils;

public class dg_Saucer extends dg_Ship {
	public dg_Saucer() {
		super();
		m_type = z_app.game.OBJ_SAUCER;
	}
	public dg_Saucer(float size) {
		super(size);
		m_type = z_app.game.OBJ_SAUCER;
	}
		//---------------------------------------------------------
		public void Draw()
		{ 
			z_app.app.pushStyle();
			z_app.app.pushMatrix();
			z_app.app.stroke(0,0,255);
			z_app.app.strokeWeight(3);
			z_app.app.translate(m_position.x, m_position.y);
			z_app.app.rotate((float) (2*Math.PI-Math.toRadians(m_angle)));
			z_app.app.translate(-m_size, -m_size);
			z_app.app.line(0, 0, m_size*2, m_size);
			z_app.app.line(m_size*2, m_size, 0, m_size*2);
			z_app.app.line(m_size * 0.2f, m_size * 0.2f, m_size*0.2f, m_size*1.8f);
			z_app.app.ellipseMode(z_app.app.CENTER);
			z_app.app.ellipse(m_size*2, m_size, 4,4);
			if (m_thrust) {
				z_app.app.stroke(m_MathUtils.random(127,255),m_MathUtils.random(0,127),0);
				z_app.app.strokeWeight(m_size*3/2);
				z_app.app.line(-m_size * 0.2f, m_size,-m_size * 0.2f-5, m_size);
			}
			z_app.app.popMatrix();
			z_app.app.popStyle();
			//TODO not complete
		}

}
