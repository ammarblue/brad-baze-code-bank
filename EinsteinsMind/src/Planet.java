import processing.core.PApplet;

public class Planet extends PApplet {
	int id;
	float x, y, dirX, dirY, radius;
	int col;
	float colR, colG, colB;
	GravityGrid gg;

	Planet(int id, float x, float y, float dirX, float dirY, float radius,
			int col,GravityGrid in) {
		gg=in;
		this.id = id;
		this.x = x;
		this.y = y;
		this.dirX = dirX;
		this.dirY = dirY;
		this.radius = radius;
		this.col = col;
		colR = col>>16&0xFF;
		colG = col>>8&0xFF;
		colB = col&0xFF;
	} // end constructor

	void setNewDirection(float gravityFieldposX, float gravityFieldPosY,
			float gravityFieldRadius, float speed, float disFac) {
		float distanceXY = dist(gravityFieldposX, gravityFieldPosY, x, y);
		float distanceX = (gravityFieldposX - x) / distanceXY;
		float distanceY = (gravityFieldPosY - y) / distanceXY;
		dirX += distanceX * gravityFieldRadius * 100
				/ pow(abs(distanceXY), disFac) / speed; // dirX += distanceX *
														// gravityFieldRadius *
														// 100 /
														// pow(abs(distanceXY),disFac);
		dirY += distanceY * gravityFieldRadius * 100
				/ pow(abs(distanceXY), disFac) / speed; // dirY += distanceY *
														// gravityFieldRadius *
														// 100 /
														// pow(abs(distanceXY),disFac);
		// x += dirX / speed;
		// y += dirY / speed;
	} // end void setNewPosition

	void setNewPosition() {
		x += dirX;
		y += dirY;
	} // end void setNewPosition()

	void drawPSphere() {
		gg.fill(col);
		noStroke();
		pushMatrix();
		translate(x, y);
		sphereDetail(25);
		sphere(radius);
		popMatrix();
	} // end void draw

	float rotZ = random(3);
	float rotSpeed = random(-0.05f, 0.05f);

	void drawSphere(int heightSteps, int points) {
		rotZ += rotSpeed;
		float cx[][] = new float[heightSteps][points], cy[][] = new float[heightSteps][points], cz[][] = new float[heightSteps][points];

		for (int i = 0; i < heightSteps; i++) {
			float czTmp = radius * cos(i * TWO_PI / (heightSteps - 1) / 2);
			float radiusTmp = sqrt(sq(radius) - sq(czTmp));
			for (int j = 0; j < points; j++) {
				float cxTmp = radiusTmp * sin(j * TWO_PI / points + rotZ);
				float cyTmp = radiusTmp * cos(j * TWO_PI / points + rotZ);
				cx[i][j] = cxTmp + x;
				cy[i][j] = cyTmp + y;
				cz[i][j] = czTmp + 0;
				// stroke(255); strokeWeight(2);
				// point(cxTmp, cyTmp, czTmp);
			} // end for j
				// cz += heightStepsDis;
		} // end for i

		// draw sphere
		for (int i = 0; i < cx.length - 1; i++) {
			for (int j = 0; j < cx[i].length; j++) {

				gg.fill(255 - (i + 1) * 255 / heightSteps * 1.0f);
				gg.fill(colR - (i + 1) * colR / heightSteps * 1.0f, colG - (i + 1)
						* colG / heightSteps * 1.0f, colB - (i + 1) * colB
						/ heightSteps * 1.0f);

				if ((j) % 2 == 0 && (i) % 2 == 0)
					continue;
				float scaleSphere = 1;
				int indexNext = j + 1;
				if (j == cx[i].length - 1)
					indexNext = 0;
				float x1 = cx[i + 0][j + 0] * scaleSphere, y1 = cy[i + 0][j + 0]
						* scaleSphere, z1 = cz[i + 0][j + 0] * scaleSphere;
				float x2 = cx[i + 0][indexNext] * scaleSphere, y2 = cy[i + 0][indexNext]
						* scaleSphere, z2 = cz[i + 0][indexNext] * scaleSphere;
				float x3 = cx[i + 1][indexNext] * scaleSphere, y3 = cy[i + 1][indexNext]
						* scaleSphere, z3 = cz[i + 1][indexNext] * scaleSphere;
				float x4 = cx[i + 1][j + 0] * scaleSphere, y4 = cy[i + 1][j + 0]
						* scaleSphere, z4 = cz[i + 1][j + 0] * scaleSphere;

				gg.noStroke();
				gg.beginShape(TRIANGLE_STRIP);
				gg.vertex(x1, y1, z1);
				gg.vertex(x2, y2, z2);
				gg.vertex(x4, y4, z4);
				gg.vertex(x3, y3, z3);
				gg.endShape();
				// stroke(0); strokeWeight(0.2);
				// line(x1, y1, z1, x2, y2, z2); line(x2, y2, z2, x3, y3, z3);
				// line(x3, y3, z3, x4, y4, z4); line(x4, y4, z4, x1, y1, z1);
			} // end for j
		} // end for i
	} // end void drawSphere(int heightSteps, int points)
} // end class Planet
