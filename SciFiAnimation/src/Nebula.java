import processing.core.PApplet;

/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/1412*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */
//NEBULA
//Matt Schroeter
//December 1st, 2008
//matthanns.com
public class Nebula extends PApplet {
	float depth = 400;

	public void setup() {
		size(800, 600, P3D);
		noStroke();
	}

	public void draw() {
		background(0, 0, 0);

		float cameraY = height / 1;
		float cameraX = width / 1;

		translate(width / 2, height / 2, -depth / 2);

		rotateY(frameCount * PI / 500);

		float fov = cameraX / (float) width * PI / 2;
		float cameraZ = cameraY / tan(fov / 2.0f);
		float aspect = (float) (width) / (float) (height);

		perspective(fov, aspect, cameraZ / 2000.0f, cameraZ * 4000.0f);

		translate(width / 10, height / 10, depth / 2);

		for (int i = 0; i < 2; i++) {
			float r = random(100);
			directionalLight(2, 83, 115, // Color
					1, 10, 0); // The x-, y-, z-axis direction'
			directionalLight(3, 115, 140, // Color
					10, 10, 0); // The x-, y-, z-axis direction'
		}

		for (int i = 0; i < 10; i++) {

			float r = random(20);

			rotateX(frameCount * PI / 1000);

			// alt effect
			// rotateY(frameCount*PI/1000);

			for (int y = -2; y < 2; y++) {
				for (int x = -2; x < 2; x++) {
					for (int z = -2; z < 2; z++) {

						pushMatrix();
						translate(400 * x, 300 * y, 300 * z);
						box(5, 5, 100);
						popMatrix();

						pushMatrix();
						translate(400 * x, 300 * y, 50 * z);
						box(100, 5, 5);
						popMatrix();

						pushMatrix();
						translate(400 * x, 10 * y, 50 * z);
						box(50, 5, 5);
						popMatrix();

						pushMatrix();
						rotateY(frameCount * PI / 400);
						translate(100 * x, 300 * y, 300 * z);
						box(60, 40, 20);
						popMatrix();

					}
				}
			}
		}
	}
}
