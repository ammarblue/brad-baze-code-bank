package reuze.aifiles;

public class dg_ControlHuman extends dg_Control {
	static final int CONTROLTYPE_HUMAN=2;
	//---------------------------------------------------------
	public dg_ControlHuman(dg_Ship ship)
	{
		super(ship);
		if (m_ship!=null)
			m_ship.m_control = this;
		m_type = CONTROLTYPE_HUMAN;
	}
	@Override
	public void Key(int key, int x, int y)
	{
		switch (key) 
		{
		//handle normal controls
		case KEY_ESC:
			System.exit(0);
			break;
		case KEY_SPACE:
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY)
				z_app.game.UseControl(z_app.game.CONTROL_SHOOT);
			else if(z_app.game.m_state == z_app.game.m_state.STATE_NEXTWAVE)
				z_app.game.StartNextWave();
			else if(z_app.game.m_state == z_app.game.m_state.STATE_STEPWAIT)
				z_app.game.m_state = z_app.game.m_state.STATE_STEP;
			break;
		case KEY_DOT:
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY)
				z_app.IncTimeScale(1);
			break;
		case KEY_COMMA:
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY)
				z_app.IncTimeScale(-1);
			break;
		case KEY_A:
			z_app.ToggleAI();
			break;
		case KEY_H:
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY)
				z_app.game.UseControl(z_app.game.CONTROL_HYPERSPACE);	 
			break;

			//handle pause controls
		case KEY_P:
			if(z_app.game.m_state != z_app.game.m_state.STATE_GAMEOVER)
				z_app.game.UseControl(z_app.game.CONTROL_PAUSE);
			break;

			//handle "step" controls
		case KEY_S:
			if(z_app.game.m_state != z_app.game.m_state.STATE_GAMEOVER)
				z_app.game.UseControl(z_app.game.CONTROL_STEP);
			break;

			//handle game over controls
		case KEY_Y:
			if(z_app.game.m_state == z_app.game.m_state.STATE_GAMEOVER)
				z_app.game.StartGame();
			break;
		case KEY_N:
			if(z_app.game.m_state == z_app.game.m_state.STATE_GAMEOVER)
				System.exit(0);
			break;
		case KEY_LEFT:
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY) 
				z_app.game.UseControl(z_app.game.CONTROL_LEFT_ON);	 
			break;
		case KEY_RIGHT:
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY)
				z_app.game.UseControl(z_app.game.CONTROL_RIGHT_ON);	 
			break;  
		case KEY_UP   :
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY)
				z_app.game.UseControl(z_app.game.CONTROL_THRUST_ON);	 
			break;
		case KEY_DOWN:
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY)
				z_app.game.UseControl(z_app.game.CONTROL_STOP);	 
			//					Game.UseControl(GameSession::CONTROL_THRUST_REVERSE);	 
			break;
		case KEYUP_RIGHT: 
		case KEYUP_LEFT:
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY)
				z_app.game.UseControl(z_app.game.CONTROL_STOP_TURN);	 
			break;
		case KEYUP_UP: 
		case KEYUP_DOWN:
			if(z_app.game.m_state == z_app.game.m_state.STATE_PLAY)
				z_app.game.UseControl(z_app.game.CONTROL_THRUST_OFF);	 
			break;
		default:
			break;
		}
	}
	/*
	//---------------------------------------------------------
	// Glut Callback for up event of normal key input.
	void KeyUp(unsigned char key, int x, int y)
	{
		switch (tolower(key) )
		{
		case 'a':
			if(z_app.game.m_AIOn)
				z_app.game.UseControl(z_app.gameSession.CONTROL_AI_OFF);
			else
				z_app.game.UseControl(z_app.gameSession.CONTROL_AI_ON);
			break;
	    case '.':
	        if(z_app.game.m_state == z_app.gameSession.STATE_PLAY)
	            z_app.game.m_timeScale++;
	        break;
	    case ',':
	        if(z_app.game.m_state == z_app.gameSession.STATE_PLAY)
	            z_app.game.m_timeScale = MAX(1,z_app.game.m_timeScale-1);
	        break;
		default:
			break;
		}
	}

	//---------------------------------------------------------
	// Glut Callback for down/up events of arrow keys.
	void  SpecialKey(int key, int x, int y)
	{
		if(z_app.game.m_state != z_app.gameSession.STATE_PLAY)
			return;

		switch (key) 
		{
		case GLUT_KEY_LEFT: 
			z_app.game.UseControl(z_app.gameSession.CONTROL_LEFT_ON);	 
			break;
		case GLUT_KEY_RIGHT: 
			z_app.game.UseControl(z_app.gameSession.CONTROL_RIGHT_ON);	 
			break;
		case GLUT_KEY_UP: 
			z_app.game.UseControl(z_app.gameSession.CONTROL_THRUST_ON);	 
			break;
		case GLUT_KEY_DOWN: 
			z_app.game.UseControl(z_app.gameSession.CONTROL_STOP);	 
//			z_app.game.UseControl(z_app.gameSession.CONTROL_THRUST_REVERSE);	 
			break;
		default:
			break;
		}
	}

	//---------------------------------------------------------
	void SpecialKeyUp(int key, int x, int y)
	{
		if(z_app.game.m_state != z_app.gameSession.STATE_PLAY)
			return;

		switch (key) 
		{
		case GLUT_KEY_RIGHT: 
		case GLUT_KEY_LEFT: 
			z_app.game.UseControl(z_app.gameSession.CONTROL_STOP_TURN);	 
			break;
		case GLUT_KEY_UP: 
		case GLUT_KEY_DOWN: 
			z_app.game.UseControl(z_app.gameSession.CONTROL_THRUST_OFF);	 
			break;
		default:
			break;
		}
	}
	 */
}
