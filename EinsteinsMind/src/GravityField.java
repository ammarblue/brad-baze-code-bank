import processing.core.PApplet;

/* 



 experimenting with Gravity (just for fun)

 */

public class GravityField extends PApplet {
	int id;
	float x, y, radius;
	int col;
	boolean isSelected;
	GravityGrid gg;

	GravityField(int id, float x, float y, float radius, int col,GravityGrid in) {
		gg=in;
		this.id = id;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.col = col;
	} // end constructor

	void setSelectionState(boolean isSelectedNew) {
		isSelected = isSelectedNew;
	}

	float rotZ = random(3);
	float rotSpeed = random(-0.05f, 0.05f);
	int sphereRow = 1; // (int)random(8);
	int sphereRowInc = 2;
	int sphereColumn = 1; // (int)random(8);
	int sphereColumnInc = 2;

	void drawSphere(int heightSteps, int points) {
		rotZ -= rotSpeed;

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
		if (frameCount % 4 == 0) {
			sphereRow += sphereRowInc;
			if (sphereRow >= cx.length - 1 || sphereRow <= 0)
				sphereRowInc *= -1;

			sphereColumn += sphereColumnInc;
			if (sphereColumn >= points)
				sphereColumn = 1;
		}
		for (int i = 0; i < cx.length - 1; i++) {
			for (int j = 0; j < cx[i].length; j++) {
				gg.fill(255 - (i + 1) * 255 / heightSteps * 1.0f);
				if (i == sphereRow || j == sphereColumn)
					gg.fill(255 - (i + 1) * 255 / heightSteps * 1.0f, 0, 0);
				if (isSelected)
					gg.fill(255 - (i + 1) * 255 / heightSteps * 1.0f, 150
							- (i + 1) * 150 / heightSteps * 1.0f, 0);
				if (isSelected && (i == sphereRow || j == sphereColumn))
					gg.fill(255 - (i + 1) * 255 / heightSteps * 1.0f);
				// else continue;
				if ((j) % 2 == 0 && i % 2 == 0)
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
				gg.stroke(0);
				gg.strokeWeight(0.2f);
				// line(x1, y1, z1, x2, y2, z2); line(x2, y2, z2, x3, y3, z3);
				// line(x3, y3, z3, x4, y4, z4); line(x4, y4, z4, x1, y1, z1);
			} // end for j
		} // end for i
	} // end void drawSphere(int heightSteps, int points)

	void drawLabel() {
		float gavityValue = (int) (radius * 100);
		gavityValue /= 100;
		String label = "gravityField " + id + "\n gravity: " + gavityValue;
		float labelWidth = gg.textWidth(label);
		float textHeight = 8;

		gg.pushMatrix();
		gg.translate(x, y, radius + textHeight * 3);
		// rotateZ(-rotZ/2);
		gg.pushMatrix();
		gg.rotateX(-HALF_PI);
		gg.fill(0);
		gg.rect(-labelWidth / 2 - 2, -textHeight - 1.5f, labelWidth + 4,
				textHeight * 2 + 6);
		gg.translate(0, 0, .1f);
		gg.fill(255);
		gg.textSize(textHeight);
		gg.textAlign(CENTER);
		gg.text(label, 0, 0, 0);
		gg.popMatrix();

		gg.rotateZ(PI);
		gg.pushMatrix();
		gg.rotateX(-HALF_PI);
		gg.translate(0, 0, .1f);
		gg.fill(255);
		gg.textSize(textHeight);
		gg.textAlign(CENTER);
		gg.text(label, 0, 0, 0);
		gg.popMatrix();
		gg.popMatrix();

	}
} // end class GravityField
