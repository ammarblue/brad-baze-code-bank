package com.software.reuze;

public interface vg_i_Sprite {
	public float getWidth();
	public float getHeight();
	public float getOriginX();
	public float getOriginY();
	public vg_i_Sprite copy();
	public void flip(boolean flipX, boolean flipY);
	public void setOrigin(float originX, float originY);
	public void translate(float xAmount, float yAmount);
	public void setColor(float color0, float color1, float color2, float transparency);
	public void setRotation(float degrees);
	public void setScale(float scale);
	public void setBounds(float x, float y, float spriteWidth, float spriteHeight);
	public vg_i_Texture getTexture();
	public void setTexture(vg_i_Texture tex);
	public void draw(vg_i_SpriteBatch spriteBatch);
	public void setProperty(String string);
	public void create(String path); //with texture
}
