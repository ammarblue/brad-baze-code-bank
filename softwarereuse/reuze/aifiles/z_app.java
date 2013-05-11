package reuze.aifiles;

import com.software.reuze.gb_Vector3;

import processing.core.PApplet;

public final class z_app {
	public static PApplet app;
	public static dg_GameSession game;

	public static void IncrementScore(int asteroidScoreVal) {
		game.IncrementScore(asteroidScoreVal);
	}
	public static void IncrementScore(dg_GameObject who, int asteroidScoreVal) {
		game.IncrementScore(who, asteroidScoreVal);
	}

	public static void Kill(dg_GameObject dg_Ship) {
		game.Kill(dg_Ship);
	}

	public static void RespawnTimer() {
		game.m_respawnTimer=2.0f;
	}

	public static void ToggleAI() {
		game.m_AIOn = !game.m_AIOn;
	}

	public static void IncTimeScale(int i) {
		if (i<0) game.m_timeScale=Math.max(1, game.m_timeScale+i);
		else game.m_timeScale+=i;
	}

	public static float GetTimeScale() {
		return game.m_timeScale;
	}

	public static void ApplyForce(int type, gb_Vector3 position,
			gb_Vector3 posEnd, gb_Vector3 force, float dt) {
		game.ApplyForce(type, position, posEnd, force, dt);
	}
}
