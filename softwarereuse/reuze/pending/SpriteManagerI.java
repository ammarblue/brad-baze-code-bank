package reuze.pending;

public interface SpriteManagerI {
	public void render(Object graphics, int screenX, int screenY, int x, int y, int w, int h, Object o);
	public void render(Object graphics, int screenX, int screenY, Object o);
	public void render(Object graphics, int screenX, int screenY, float rotate, float scale, Object object);
	public void render(Object graphics, int screenX, int screenY, int x, int y,
			                 int w, int h, float rotate, float scale, Object sheet);
}
