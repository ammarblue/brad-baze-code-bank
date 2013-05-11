package com.software.reuze;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;


/**
 * See <a href="http://www.badlogicgames.com/wordpress/?p=1255">http://www.badlogicgames.com/wordpress/?p=1255</a>
 * @author mzechner
 *
 */
public class ied_ParticleEffect implements l_i_Disposable {
	private final d_ArrayEfficientRemove<dg_ParticleEmitter> emitters;
    public vg_i_Sprite sprite;
	public vg_i_Sprite getSprite() {
		return sprite;
	}

	public void setSprite(vg_i_Sprite sprite) {
		this.sprite = sprite;
	}

	public ied_ParticleEffect () {
		emitters = new d_ArrayEfficientRemove<dg_ParticleEmitter>(8);
	}

	public ied_ParticleEffect (ied_ParticleEffect effect) {
		emitters = new d_ArrayEfficientRemove<dg_ParticleEmitter>(true, effect.emitters.size);
		for (int i = 0, n = effect.emitters.size; i < n; i++)
			emitters.add(new dg_ParticleEmitter(effect.emitters.get(i)));
	}
    public void add(dg_ParticleEmitter em) { emitters.add(em); }
	public void start () {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).start();
	}

	public void update (float delta) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).update(delta);
	}

	public void draw (vg_i_SpriteBatch spriteBatch) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).draw(spriteBatch);
	}

	public void draw (vg_i_SpriteBatch spriteBatch, float delta) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).draw(spriteBatch, delta);
	}

	public void allowCompletion () {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).allowCompletion();
	}

	public boolean isComplete () {
		for (int i = 0, n = emitters.size; i < n; i++) {
			dg_ParticleEmitter emitter = emitters.get(i);
			if (emitter.isContinuous()) return false;
			if (!emitter.isComplete()) return false;
		}
		return true;
	}

	public void setDuration (int duration) {
		for (int i = 0, n = emitters.size; i < n; i++) {
			dg_ParticleEmitter emitter = emitters.get(i);
			emitter.setContinuous(false);
			emitter.duration = duration;
			emitter.durationTimer = 0;
		}
	}

	public void setPosition (float x, float y) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).setPosition(x, y);
	}

	public void setFlip (boolean flipX, boolean flipY) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).setFlip(flipX, flipY);
	}

	public d_ArrayEfficientRemove<dg_ParticleEmitter> getEmitters () {
		return emitters;
	}

	/** Returns the emitter with the specified name, or null. */
	public dg_ParticleEmitter findEmitter (String name) {
		for (int i = 0, n = emitters.size; i < n; i++) {
			dg_ParticleEmitter emitter = emitters.get(i);
			if (emitter.getName().equals(name)) return emitter;
		}
		return null;
	}

	public void save (File file) {
		Writer output = null;
		try {
			output = new FileWriter(file);
			int index = 0;
			for (int i = 0, n = emitters.size; i < n; i++) {
				dg_ParticleEmitter emitter = emitters.get(i);
				if (index++ > 0) output.write("\n\n");
				emitter.save(output);
				output.write("- Image Path -\n");
				output.write(emitter.getImagePath() + "\n");
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error saving effect: " + file, ex);
		} finally {
			try {
				if (output != null) output.close();
			} catch (IOException ex) {
			}
		}
	}

	public void load (f_Handle effectFile, f_Handle imagesDir) {
		loadEmitters(effectFile);
		//loadEmitterImages(imagesDir);
	}

	/*public void load (FileHandle effectFile, TextureAtlas atlas) {
		loadEmitters(effectFile);
		loadEmitterImages(atlas);
	}*/

	public void loadEmitters (f_Handle effectFile) {
		InputStream input = effectFile.read();
		emitters.clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input), 512);
			while (true) {
				dg_ParticleEmitter emitter = new dg_ParticleEmitter(reader);
				reader.readLine();
				emitter.setSprite(sprite.copy());
				emitter.setImagePath(reader.readLine());
				emitters.add(emitter);
				if (reader.readLine() == null) break;
				if (reader.readLine() == null) break;
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error loading effect: " + effectFile, ex);
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (IOException ex) {
			}
		}
	}

	/*public void loadEmitterImages (TextureAtlas atlas) {
		for (int i = 0, n = emitters.size; i < n; i++) {
			ParticleEmitter emitter = emitters.get(i);
			String imagePath = emitter.getImagePath();
			if (imagePath == null) continue;
			String imageName = new File(imagePath.replace('\\', '/')).getName();
			int lastDotIndex = imageName.lastIndexOf('.');
			if (lastDotIndex != -1) imageName = imageName.substring(0, lastDotIndex);
			ISprite sprite = atlas.createSprite(imageName);
			if (sprite == null) throw new IllegalArgumentException("SpriteSheet missing image: " + imageName);
			emitter.setSprite(sprite);
		}
	}*/

	public void loadEmitterImages (f_Handle imagesDir) {
		for (int i = 0, n = emitters.size; i < n; i++) {
			dg_ParticleEmitter emitter = emitters.get(i);
			String imagePath = emitter.getImagePath();
			if (imagePath == null) continue;
			String imageName = new File(imagePath.replace('\\', '/')).getName();
			if (imagesDir==null) emitter.setSprite(sprite);
			else {
				vg_i_Sprite s=emitter.getSprite();
				s.create(imagesDir.child(imageName).path());  //load texture
			}
		}
	}

	/*protected Texture loadTexture (FileHandle file) {
		return new Texture(file, false);
	}*/

	/** Disposes the texture for each sprite for each ParticleEmitter. */
	public void dispose () {
		for (int i = 0, n = emitters.size; i < n; i++) {
			dg_ParticleEmitter emitter = emitters.get(i);
			emitter.getSprite().getTexture().dispose();
		}
	}
}
/*
Real quick, a particle effect consists of some images that are moved around.
The images usually use additive blending and some pretty stunning results can be produced with only a few images.
Particle effects are good for fire, explosions, smoke, etc.
Each particle has many properties that control how it behaves: life, velocity, rotation, scale, etc.
The particle editor allows you to manipulate these properties and observe the result in real time.
You can also create effects programmatically, but it is much more difficult and time consuming to create great effects.

The first step to creating an effect is to choose an image.
The default image is just a simple round gradient.
Experiment with different images to create a wide variety of effects.
Images will often combine for some surprising and sometimes very cool looking results.

When you are configuring properties, you are actually configuring the particle emitter that will create and manage the particles.
In code, the emitter is represented by the ParticleEmitter class.
A particle effect is made up of one or more emitters, which is managed in the lower right of the particle editor.
In code, the effect is represented by the ParticleEffect class, which has a list of ParticleEmitters.
Most of emitter properties are self explanatory, but I will run through them quickly.

Delay: When an effect starts, this emitter will do nothing for this many milliseconds.
This can be used to synchronize multiple emitters.

Note the delay property has an “Active” button. Some properties can be turned off, which can minimize some of the work that needs to be done at runtime.

Duration: How long the emitter will emit particles.
Note this is not the same as how long particles will live.

Count: Controls the minimum number of particles that must always exist, and the maximum number of particles that can possibly exist.
The minimum is nice for making sure particles are always visible, and the maximum lets the emitter know how much memory to allocate.
Emission: How many particles will be emitted per second.
Emission is the first property with a little chart, but all properties with a chart work the same way.
Click on the chart to add nodes, click and drag to move nodes, and double click to delete nodes.
The chart allows you to vary the value of the property over time.
Note that the chart says “Duration” in the center.
This means the x-axis represents the duration of the emitter (which of course is set with the duration property).
Other charts may say “Life”, which means the x-axis represents the life of a single particle.
The top of the y-axis represents the value specified for “High” and the bottom of the y-axis represents the value specified for “Low”.
The chart can be expanded with the “+” button for a larger view.

The “High” and “Low” values each have a button marked “>”.
When clicked, this will expand to show a second text box.
A random number (float) is chosen between the text box on the left and the one on the right.
The number is chosen when the effect starts for a “Duration” chart, and when a particle is spawned for a “Life” chart.

Finally, the last piece of functionality for charts is the “Relative” checkbox.
When unchecked, the value at any one point in time for the property will be what the chart shows.
When checked, the value shown on the chart is added to the initial value of the property.
Why? Imagine you have rotation set to start at 0 and go to 360 degrees over the life of a particle.
This is nice, but all the particles start at the same zero rotation, so you change the “Low” value to start between 0 and 360.
Now your particles will start between 0 and 360, and rotate to exactly 360 degrees.
If a particle spawns at 330 degrees, it will only rotate 30 degrees.
Now, if you check “Relative”, a particle that spawns at 330 degrees will rotate to 330 + 360 degrees, which is probably what you want in this case.

Life: How long a single particle will live.
Life Offset: How much life is used up when a particle spawns.
The particle is still moved/rotated/etc for the portion of its life that is used up.
This allows particles to spawn, eg, halfway through their life.
X Offset and Y Offset: The amount in pixels to offset where particles spawn.
Spawn: The shape used to spawn particles: point, line, square, or ellipse.
Ellipse has additional settings.
Spawn Width and Spawn Height: Controls the size of the spawn shape.
Size: The size of the particle.
Velocity: The speed of the particle.
Angle: The direction the particle travels. Not very useful if velocity is not active.
Rotation: The rotation of the particle.
Wind and Gravity: The x-axis or y-axis force to apply to particles, in pixels per second.
Tint: The particle color. Click the little triangle and then use the sliders to change the color. Click in the bar above the triangle to add more triangles. This allows you to make particles change to any number of colors over their lifetime.
Click and drag to move a triangle (if it isn’t at the start or end), double click to delete.
Transparency: Controls the alpha of the particle.

Options, Additive: For additive blending.
Options, Attached: Means existing particles will move when the emitter moves.
Options, Continuous: Means the emitter restarts as soon as its duration expires. 
Note that this means an effect will never end, so other emitters in the effect that are not continuous will never restart.
Options, Aligned: The angle of a particle is added to the rotation.
This allows you to align the particle image to the direction of travel.
Effect settings saved with the particle editor are written to a text file, which can be loaded into a ParticleEffect instance in your game.
The ParticleEffect can load images from a directory, or a SpriteSheet.
Of course, a SpriteSheet is recommended and can easily be made with the SpriteSheetPacker.
Most effects can be simplified to use just a few images.
My most complex effects that use 4 or more emitters typically only need 15 or so total particles alive at once.
My crappy Droid can render 250 32x32 blended particles at 46 fos, 500 particles at 36 fps, and 1000 particles at 26 fps (see ParticleEmitterTest in gdx-tests).
However, the performance varies greatly with the particle image size.
*/