import processing.core.PVector;

class PolarCamera {
	/**
	 * 
	 */
	private final terrainmarch PolarCamera;
	PVector m_camPos;
	float m_CamAngleX;
	float m_CamAngleY;
	PVector m_camDir;

	PolarCamera(terrainmarch terrainmarch, PVector cpos, float angX, float angY) {
		PolarCamera = terrainmarch;
		m_camPos = cpos;
		m_CamAngleX = angX;
		m_CamAngleY = angY;
		update(0, 0);
	}

	public void update(float dy, float dz) {
		m_CamAngleX += dy;
		m_CamAngleY += -dz;
		m_camDir = new PVector(terrainmarch.sin(m_CamAngleX)
				* terrainmarch.cos(m_CamAngleY), terrainmarch.sin(m_CamAngleY),
				terrainmarch.cos(m_CamAngleX) * terrainmarch.cos(m_CamAngleY));
	}

	public void forward(float v) {
		PVector f = new PVector();
		f.set(m_camDir);
		f.mult(v * 0.25f);
		m_camPos.add(f);
	}

	public void side(float v) {
		PVector f = m_camDir.cross(new PVector(0, 1, 0));
		f.normalize();
		;
		f.mult(v * 0.25f);
		m_camPos.add(f);
	}
}