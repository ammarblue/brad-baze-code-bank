package reuze.aifiles;

public class dg_Target extends dg_GameObject {
	public dg_Target() {this(1);}
	public dg_Target(int size) {
		super(size);
		m_type = z_app.game.OBJ_TARGET;
	}
	@Override
	public void Draw() {
		z_app.app.text('X',m_position.x, m_position.y);
	}

}
