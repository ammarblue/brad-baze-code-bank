package com.software.reuze;
	public class dg_Particle implements vg_i_Sprite {
		int life, currentLife, lifeOffset;
		float scale, scaleDiff;
		float rotation, rotationDiff;
		float velocity, velocityDiff;
		float angle, angleDiff;
		float angleCos, angleSin;
		float transparency, transparencyDiff;
		float wind, windDiff;
		float gravity, gravityDiff;
		float[] tint;
		vg_i_Sprite sprite;
		public dg_Particle (vg_i_Sprite sprite) {
			this.sprite = sprite.copy();
		}
		public void setProperty(String string) {
			sprite.setProperty(string);
		}
		public void draw(vg_i_SpriteBatch spriteBatch) {
			//System.out.println(currentLife+" "+lifeOffset);
			if (currentLife < lifeOffset) sprite.draw(spriteBatch);
		}
		public float getWidth() {
			return sprite.getWidth();
		}
		public float getHeight() {
			return sprite.getHeight();
		}
		public float getOriginX() {
			return sprite.getOriginX();
		}
		public float getOriginY() {
			return sprite.getOriginY();
		}
		public vg_i_Sprite copy() {
			// TODO Auto-generated method stub
			return null;
		}
		public void flip(boolean flipX, boolean flipY) {
			sprite.flip(flipX, flipY);
		}
		public void setOrigin(float originX, float originY) {
			sprite.setOrigin(originX, originY);
		}
		public void translate(float xAmount, float yAmount) {
			sprite.translate(xAmount, yAmount);
		}
		public void setColor(float color0, float color1, float color2,
				float transparency) {
			sprite.setColor(color0, color1, color2, transparency);			
		}
		public void setRotation(float rotation) {
			sprite.setRotation(rotation);
		}
		public void setScale(float scale) {
			sprite.setScale(scale);
		}
		public void setBounds(float x, float y, float spriteWidth,
				float spriteHeight) {
			sprite.setBounds(x, y, spriteWidth, spriteHeight);
		}
		public vg_i_Texture getTexture() {
			return sprite.getTexture();
		}
		public void setTexture(vg_i_Texture tex) {
			sprite.setTexture(tex);
		}
		public void create(String path) {
			sprite.create(path);
		}
		public boolean endOfLife() { //true sets particle to inactive
			return true;
		}
	}