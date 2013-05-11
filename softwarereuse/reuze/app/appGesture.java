package reuze.app;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.geom.Rectangle2D;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

public class appGesture extends PApplet {

	OneDollar one;

	public void setup() {
		size(400, 400);
		background(241);
		smooth();
		noFill();
		stroke(0);

		one = new OneDollar(this);
		one.setVerbose(true); // activate verbose mode
		println(one); // print the settings

		// add template gestures:
		one.add("triangle", new Integer[] { 137, 139, 135, 141, 133, 144, 132,
				146, 130, 149, 128, 151, 126, 155, 123, 160, 120, 166, 116,
				171, 112, 177, 107, 183, 102, 188, 100, 191, 95, 195, 90, 199,
				86, 203, 82, 206, 80, 209, 75, 213, 73, 213, 70, 216, 67, 219,
				64, 221, 61, 223, 60, 225, 62, 226, 65, 225, 67, 226, 74, 226,
				77, 227, 85, 229, 91, 230, 99, 231, 108, 232, 116, 233, 125,
				233, 134, 234, 145, 233, 153, 232, 160, 233, 170, 234, 177,
				235, 179, 236, 186, 237, 193, 238, 198, 239, 200, 237, 202,
				239, 204, 238, 206, 234, 205, 230, 202, 222, 197, 216, 192,
				207, 186, 198, 179, 189, 174, 183, 170, 178, 164, 171, 161,
				168, 154, 160, 148, 155, 143, 150, 138, 148, 136, 148 });
		one.add("circle", new Integer[] { 127, 141, 124, 140, 120, 139, 118,
				139, 116, 139, 111, 140, 109, 141, 104, 144, 100, 147, 96, 152,
				93, 157, 90, 163, 87, 169, 85, 175, 83, 181, 82, 190, 82, 195,
				83, 200, 84, 205, 88, 213, 91, 216, 96, 219, 103, 222, 108,
				224, 111, 224, 120, 224, 133, 223, 142, 222, 152, 218, 160,
				214, 167, 210, 173, 204, 178, 198, 179, 196, 182, 188, 182,
				177, 178, 167, 170, 150, 163, 138, 152, 130, 143, 129, 140,
				131, 129, 136, 126, 139 });
		one.add("rectangle", new Integer[] { 78, 149, 78, 153, 78, 157, 78,
				160, 79, 162, 79, 164, 79, 167, 79, 169, 79, 173, 79, 178, 79,
				183, 80, 189, 80, 193, 80, 198, 80, 202, 81, 208, 81, 210, 81,
				216, 82, 222, 82, 224, 82, 227, 83, 229, 83, 231, 85, 230, 88,
				232, 90, 233, 92, 232, 94, 233, 99, 232, 102, 233, 106, 233,
				109, 234, 117, 235, 123, 236, 126, 236, 135, 237, 142, 238,
				145, 238, 152, 238, 154, 239, 165, 238, 174, 237, 179, 236,
				186, 235, 191, 235, 195, 233, 197, 233, 200, 233, 201, 235,
				201, 233, 199, 231, 198, 226, 198, 220, 196, 207, 195, 195,
				195, 181, 195, 173, 195, 163, 194, 155, 192, 145, 192, 143,
				192, 138, 191, 135, 191, 133, 191, 130, 190, 128, 188, 129,
				186, 129, 181, 132, 173, 131, 162, 131, 151, 132, 149, 132,
				138, 132, 136, 132, 122, 131, 120, 131, 109, 130, 107, 130, 90,
				132, 81, 133, 76, 133 });
		one.add("x", new Integer[] { 87, 142, 89, 145, 91, 148, 93, 151, 96,
				155, 98, 157, 100, 160, 102, 162, 106, 167, 108, 169, 110, 171,
				115, 177, 119, 183, 123, 189, 127, 193, 129, 196, 133, 200,
				137, 206, 140, 209, 143, 212, 146, 215, 151, 220, 153, 222,
				155, 223, 157, 225, 158, 223, 157, 218, 155, 211, 154, 208,
				152, 200, 150, 189, 148, 179, 147, 170, 147, 158, 147, 148,
				147, 141, 147, 136, 144, 135, 142, 137, 140, 139, 135, 145,
				131, 152, 124, 163, 116, 177, 108, 191, 100, 206, 94, 217, 91,
				222, 89, 225, 87, 226, 87, 224 });
		one.add("check", new Integer[] { 91, 185, 93, 185, 95, 185, 97, 185,
				100, 188, 102, 189, 104, 190, 106, 193, 108, 195, 110, 198,
				112, 201, 114, 204, 115, 207, 117, 210, 118, 212, 120, 214,
				121, 217, 122, 219, 123, 222, 124, 224, 126, 226, 127, 229,
				129, 231, 130, 233, 129, 231, 129, 228, 129, 226, 129, 224,
				129, 221, 129, 218, 129, 212, 129, 208, 130, 198, 132, 189,
				134, 182, 137, 173, 143, 164, 147, 157, 151, 151, 155, 144,
				161, 137, 165, 131, 171, 122, 174, 118, 176, 114, 177, 112,
				177, 114, 175, 116, 173, 118 });
		one.add("caret", new Integer[] { 79, 245, 79, 242, 79, 239, 80, 237,
				80, 234, 81, 232, 82, 230, 84, 224, 86, 220, 86, 218, 87, 216,
				88, 213, 90, 207, 91, 202, 92, 200, 93, 194, 94, 192, 96, 189,
				97, 186, 100, 179, 102, 173, 105, 165, 107, 160, 109, 158, 112,
				151, 115, 144, 117, 139, 119, 136, 119, 134, 120, 132, 121,
				129, 122, 127, 124, 125, 126, 124, 129, 125, 131, 127, 132,
				130, 136, 139, 141, 154, 145, 166, 151, 182, 156, 193, 157,
				196, 161, 209, 162, 211, 167, 223, 169, 229, 170, 231, 173,
				237, 176, 242, 177, 244, 179, 250, 181, 255, 182, 257 });
		one.add("zig-zag", new Integer[] { 307, 216, 333, 186, 356, 215, 375,
				186, 399, 216, 418, 186 });
		one.add("arrow",
				new Integer[] { 68, 222, 70, 220, 73, 218, 75, 217, 77, 215,
						80, 213, 82, 212, 84, 210, 87, 209, 89, 208, 92, 206,
						95, 204, 101, 201, 106, 198, 112, 194, 118, 191, 124,
						187, 127, 186, 132, 183, 138, 181, 141, 180, 146, 178,
						154, 173, 159, 171, 161, 170, 166, 167, 168, 167, 171,
						166, 174, 164, 177, 162, 180, 160, 182, 158, 183, 156,
						181, 154, 178, 153, 171, 153, 164, 153, 160, 153, 150,
						154, 147, 155, 141, 157, 137, 158, 135, 158, 137, 158,
						140, 157, 143, 156, 151, 154, 160, 152, 170, 149, 179,
						147, 185, 145, 192, 144, 196, 144, 198, 144, 200, 144,
						201, 147, 199, 149, 194, 157, 191, 160, 186, 167, 180,
						176, 177, 179, 171, 187, 169, 189, 165, 194, 164, 196 });
		one.add("leftsquarebracket",
				new Integer[] { 140, 124, 138, 123, 135, 122, 133, 123, 130,
						123, 128, 124, 125, 125, 122, 124, 120, 124, 118, 124,
						116, 125, 113, 125, 111, 125, 108, 124, 106, 125, 104,
						125, 102, 124, 100, 123, 98, 123, 95, 124, 93, 123, 90,
						124, 88, 124, 85, 125, 83, 126, 81, 127, 81, 129, 82,
						131, 82, 134, 83, 138, 84, 141, 84, 144, 85, 148, 85,
						151, 86, 156, 86, 160, 86, 164, 86, 168, 87, 171, 87,
						175, 87, 179, 87, 182, 87, 186, 88, 188, 88, 195, 88,
						198, 88, 201, 88, 207, 89, 211, 89, 213, 89, 217, 89,
						222, 88, 225, 88, 229, 88, 231, 88, 233, 88, 235, 89,
						237, 89, 240, 89, 242, 91, 241, 94, 241, 96, 240, 98,
						239, 105, 240, 109, 240, 113, 239, 116, 240, 121, 239,
						130, 240, 136, 237, 139, 237, 144, 238, 151, 237, 157,
						236, 159, 237 });
		one.add("rightsquarebracket",
				new Integer[] { 112, 138, 112, 136, 115, 136, 118, 137, 120,
						136, 123, 136, 125, 136, 128, 136, 131, 136, 134, 135,
						137, 135, 140, 134, 143, 133, 145, 132, 147, 132, 149,
						132, 152, 132, 153, 134, 154, 137, 155, 141, 156, 144,
						157, 152, 158, 161, 160, 170, 162, 182, 164, 192, 166,
						200, 167, 209, 168, 214, 168, 216, 169, 221, 169, 223,
						169, 228, 169, 231, 166, 233, 164, 234, 161, 235, 155,
						236, 147, 235, 140, 233, 131, 233, 124, 233, 117, 235,
						114, 238, 112, 238 });
		one.add("v", new Integer[] { 89, 164, 90, 162, 92, 162, 94, 164, 95,
				166, 96, 169, 97, 171, 99, 175, 101, 178, 103, 182, 106, 189,
				108, 194, 111, 199, 114, 204, 117, 209, 119, 214, 122, 218,
				124, 222, 126, 225, 128, 228, 130, 229, 133, 233, 134, 236,
				136, 239, 138, 240, 139, 242, 140, 244, 142, 242, 142, 240,
				142, 237, 143, 235, 143, 233, 145, 229, 146, 226, 148, 217,
				149, 208, 149, 205, 151, 196, 151, 193, 153, 182, 155, 172,
				157, 165, 159, 160, 162, 155, 164, 150, 165, 148, 166, 146 });
		one.add("delete", new Integer[] { 123, 129, 123, 131, 124, 133, 125,
				136, 127, 140, 129, 142, 133, 148, 137, 154, 143, 158, 145,
				161, 148, 164, 153, 170, 158, 176, 160, 178, 164, 183, 168,
				188, 171, 191, 175, 196, 178, 200, 180, 202, 181, 205, 184,
				208, 186, 210, 187, 213, 188, 215, 186, 212, 183, 211, 177,
				208, 169, 206, 162, 205, 154, 207, 145, 209, 137, 210, 129,
				214, 122, 217, 118, 218, 111, 221, 109, 222, 110, 219, 112,
				217, 118, 209, 120, 207, 128, 196, 135, 187, 138, 183, 148,
				167, 157, 153, 163, 145, 165, 142, 172, 133, 177, 127, 179,
				127, 180, 125 });
		one.add("leftcurlybrace", new Integer[] { 150, 116, 147, 117, 145, 116,
				142, 116, 139, 117, 136, 117, 133, 118, 129, 121, 126, 122,
				123, 123, 120, 125, 118, 127, 115, 128, 113, 129, 112, 131,
				113, 134, 115, 134, 117, 135, 120, 135, 123, 137, 126, 138,
				129, 140, 135, 143, 137, 144, 139, 147, 141, 149, 140, 152,
				139, 155, 134, 159, 131, 161, 124, 166, 121, 166, 117, 166,
				114, 167, 112, 166, 114, 164, 116, 163, 118, 163, 120, 162,
				122, 163, 125, 164, 127, 165, 129, 166, 130, 168, 129, 171,
				127, 175, 125, 179, 123, 184, 121, 190, 120, 194, 119, 199,
				120, 202, 123, 207, 127, 211, 133, 215, 142, 219, 148, 220,
				151, 221 });
		one.add("rightcurlybrace", new Integer[] { 117, 132, 115, 132, 115,
				129, 117, 129, 119, 128, 122, 127, 125, 127, 127, 127, 130,
				127, 133, 129, 136, 129, 138, 130, 140, 131, 143, 134, 144,
				136, 145, 139, 145, 142, 145, 145, 145, 147, 145, 149, 144,
				152, 142, 157, 141, 160, 139, 163, 137, 166, 135, 167, 133,
				169, 131, 172, 128, 173, 126, 176, 125, 178, 125, 180, 125,
				182, 126, 184, 128, 187, 130, 187, 132, 188, 135, 189, 140,
				189, 145, 189, 150, 187, 155, 186, 157, 185, 159, 184, 156,
				185, 154, 185, 149, 185, 145, 187, 141, 188, 136, 191, 134,
				191, 131, 192, 129, 193, 129, 195, 129, 197, 131, 200, 133,
				202, 136, 206, 139, 211, 142, 215, 145, 220, 147, 225, 148,
				231, 147, 239, 144, 244, 139, 248, 134, 250, 126, 253, 119,
				253, 115, 253 });
		one.add("star", new Integer[] { 75, 250, 75, 247, 77, 244, 78, 242, 79,
				239, 80, 237, 82, 234, 82, 232, 84, 229, 85, 225, 87, 222, 88,
				219, 89, 216, 91, 212, 92, 208, 94, 204, 95, 201, 96, 196, 97,
				194, 98, 191, 100, 185, 102, 178, 104, 173, 104, 171, 105, 164,
				106, 158, 107, 156, 107, 152, 108, 145, 109, 141, 110, 139,
				112, 133, 113, 131, 116, 127, 117, 125, 119, 122, 121, 121,
				123, 120, 125, 122, 125, 125, 127, 130, 128, 133, 131, 143,
				136, 153, 140, 163, 144, 172, 145, 175, 151, 189, 156, 201,
				161, 213, 166, 225, 169, 233, 171, 236, 174, 243, 177, 247,
				178, 249, 179, 251, 180, 253, 180, 255, 179, 257, 177, 257,
				174, 255, 169, 250, 164, 247, 160, 245, 149, 238, 138, 230,
				127, 221, 124, 220, 112, 212, 110, 210, 96, 201, 84, 195, 74,
				190, 64, 182, 55, 175, 51, 172, 49, 170, 51, 169, 56, 169, 66,
				169, 78, 168, 92, 166, 107, 164, 123, 161, 140, 162, 156, 162,
				171, 160, 173, 160, 186, 160, 195, 160, 198, 161, 203, 163,
				208, 163, 206, 164, 200, 167, 187, 172, 174, 179, 172, 181,
				153, 192, 137, 201, 123, 211, 112, 220, 99, 229, 90, 237, 80,
				244, 73, 250, 69, 254, 69, 252 });
		one.add("pigtail",
				new Integer[] { 81, 219, 84, 218, 86, 220, 88, 220, 90, 220,
						92, 219, 95, 220, 97, 219, 99, 220, 102, 218, 105, 217,
						107, 216, 110, 216, 113, 214, 116, 212, 118, 210, 121,
						208, 124, 205, 126, 202, 129, 199, 132, 196, 136, 191,
						139, 187, 142, 182, 144, 179, 146, 174, 148, 170, 149,
						168, 151, 162, 152, 160, 152, 157, 152, 155, 152, 151,
						152, 149, 152, 146, 149, 142, 148, 139, 145, 137, 141,
						135, 139, 135, 134, 136, 130, 140, 128, 142, 126, 145,
						122, 150, 119, 158, 117, 163, 115, 170, 114, 175, 117,
						184, 120, 190, 125, 199, 129, 203, 133, 208, 138, 213,
						145, 215, 155, 218, 164, 219, 166, 219, 177, 219, 182,
						218, 192, 216, 196, 213, 199, 212, 201, 211 });
		// http://depts.washington.edu/aimgroup/proj/dollar/unistrokes.gif

		one.bind("circle", "detected");
		one.bind("triangle", "detected");
		one.bind("rectangle", "detected");
	}

	public void detected(String gesture, int x, int y, int c_x, int c_y) {
		println("# gesture: " + gesture + " ( " + x + " / " + y + " )");
	}

	public void draw() {
		background(241);
		one.draw();
		one.check();
	}

	public void mousePressed() {
		one.start(100);
	}

	public void mouseDragged() {
		one.update(100, mouseX, mouseY);
	}

	public void mouseReleased() {
		one.end(100);
	}

	// --------------------------------------------------
	public static class OneDollar {

		private PApplet parent;
		private HashMap<Integer, Candidate> candidates;
		private HashMap<String, Gesture> templates;
		private HashMap<String, Callback> callbacks;
		private Recognizer recognizer;
		private Boolean online, verbose;
		private Integer maxLength, maxTime;

		/**
		 * Constructor of the recognizer.
		 * 
		 * @param parent
		 *            reference of the processing sketch
		 */
		public OneDollar(PApplet parent) {

			System.out
					.println("# OneDollar-Unistroke-Recognizer - v"
							+ this.getVersion()
							+ " - https://github.com/voidplus/OneDollar-Unistroke-Recognizer");

			parent.registerDispose(this);
			this.parent = parent;
			this.candidates = new HashMap<Integer, Candidate>();
			this.callbacks = new HashMap<String, Callback>();
			this.recognizer = new Recognizer(parent, 64, 250, 45, 2);

			this.setOnline(true);
			this.setMinLength(50);
			this.setMaxLength(2500);
			this.setMaxTime(3000);

			this.setVerbose(false);
		}

		/**
		 * Add new template to recognizer.
		 * 
		 * @param name
		 *            name of template
		 * @param points
		 *            points as array of template
		 * @return
		 */
		public OneDollar addGesture(String name, Integer[] points) {
			if ((points.length % 2) == 0 && points.length > 0) {
				LinkedList<PVector> vectors = new LinkedList<PVector>();
				for (int i = 0, l = points.length; i < l; i += 2) {
					vectors.add(new PVector(points[i], points[i + 1]));
				}
				if (this.templates == null) {
					this.templates = new HashMap<String, Gesture>();
				}
				templates
						.put(name, new Gesture(name, vectors, this.recognizer));
			} else {
				System.err.println("Error.");
			}
			return this;
		}

		public OneDollar add(String name, Integer[] points) {
			return this.addGesture(name, points);
		}

		/**
		 * Remove specified template from recognizer.
		 * 
		 * @param name
		 *            name of template
		 * @return
		 */
		public OneDollar removeGesture(String name) {
			if (templates.containsKey(name)) {
				templates.remove(name);
			}
			return this;
		}

		public OneDollar remove(String name) {
			return this.removeGesture(name);
		}

		/**
		 * Run the recognition and in case of success execute the binded
		 * callback.
		 * 
		 * @return
		 */
		public synchronized Result check() {
			Result result = null;
			if (this.templates.size() > 0) {
				Candidate motion = null;

				if (this.candidates.size() > 0) {
					for (Integer id : this.candidates.keySet()) {
						motion = this.candidates.get(id);

						Deque<PointInTime> line = motion.getLine();
						LinkedList<PVector> positions = new LinkedList<PVector>();

						ListIterator<PointInTime> iterator = (ListIterator<PointInTime>) line
								.iterator();
						while (iterator.hasNext()) {
							PointInTime point = (PointInTime) iterator.next();
							PVector position = point.getPosition();
							positions.add(position);
						}

						// binded templates
						if (motion.hasBinds()) {
							result = this.recognizer.check(positions,
									this.templates, motion.getBinds());
							if (result != null) {

								if (this.verbose) {
									String object = this.candidates.get(id)
											.getBind(result.getName())
											.getObjectClass();
									String method = this.candidates.get(id)
											.getBind(result.getName())
											.getCallbackString();
									System.out.println("# Candidate: " + id
											+ " # Template: "
											+ result.getName() + " ("
											+ result.getScore() + "%)"
											+ " # Object: " + object
											+ " # Method: " + method);
								}
								motion.fire(result.getName());

								if (this.callbacks
										.containsKey(result.getName())) {
									if (this.verbose) {
										String object = this.callbacks.get(
												result.getName())
												.getObjectClass();
										String method = this.callbacks.get(
												result.getName())
												.getCallbackString();
										System.out.println("# Candidate: " + id
												+ " # Template: "
												+ result.getName() + " ("
												+ result.getScore() + "%)"
												+ " # Object: " + object
												+ " # Method: " + method);
									}
									this.callbacks.get(result.getName()).fire(
											motion, result.getName());
								}
								this.candidates.get(id).clear(
										positions.getLast());
								return result;
							}
						}

						// all templates
						if (this.callbacks.size() > 0) {
							result = this.recognizer.check(positions,
									this.templates, this.callbacks);
							if (result != null) {
								if (this.verbose) {
									String object = this.callbacks.get(
											result.getName()).getObjectClass();
									String method = this.callbacks.get(
											result.getName())
											.getCallbackString();
									System.out.println("# Candidate: " + id
											+ " # Template: "
											+ result.getName() + " ("
											+ result.getScore() + "%)"
											+ " # Object: " + object
											+ " # Method: " + method);
								}
								this.callbacks.get(result.getName()).fire(
										motion, result.getName());
								this.candidates.get(id).clear(
										positions.getLast());
								return result;
							}
						}

					}
				}

			}
			return result;
		}

		/**
		 * Draw all candidates points as lines.
		 * 
		 * @return
		 */
		public synchronized OneDollar draw() {
			for (Integer id : candidates.keySet()) {
				candidates.get(id).draw();
			}
			return this;
		}

		/**
		 * Bind sketch callback to candidate.
		 * 
		 * @param template
		 *            name of added template
		 * @param callback
		 *            name of callback in current sketch
		 * @return
		 */
		public OneDollar bind(String template, String callback) {
			this.bind(template, this.parent, callback);
			return this;
		}

		/**
		 * Bind object callback to candidate.
		 * 
		 * @param template
		 *            name of added template
		 * @param object
		 *            object, which implemented the callback
		 * @param callback
		 *            name of callback
		 * @return
		 */
		public OneDollar bind(String template, Object object, String callback) {
			if (!this.callbacks.containsKey(template)) {
				this.callbacks.put(template, new Callback(object, callback));
			}
			return this;
		}

		/**
		 * Bind sketch callback to candidate.
		 * 
		 * @param id
		 *            unique id of candidate
		 * @param template
		 *            name of added template
		 * @param callback
		 *            name of callback in current sketch
		 * @return
		 */
		public OneDollar bind(Integer id, String template, String callback) {
			this.bind(id, template, this.parent, callback);
			return this;
		}

		/**
		 * Bind object callback to candidate.
		 * 
		 * @param id
		 *            unique id of candidate
		 * @param template
		 *            name of added template
		 * @param object
		 *            object, which implemented the callback
		 * @param callback
		 *            name of callback
		 * @return
		 */
		public OneDollar bind(Integer id, String template, Object object,
				String callback) {
			if (candidates.containsKey(id) && templates.containsKey(template)) {
				candidates.get(id).addBind(template, object, callback);
			}
			return this;
		}

		/**
		 * Unbind callback from candidate.
		 * 
		 * @param id
		 *            unique id of candidate
		 * @param template
		 *            name of the added template
		 * @return
		 */
		public OneDollar unbind(Integer id, String template) {
			if (candidates.containsKey(id)) {
				candidates.get(id).removeBind(template);
			}
			return this;
		}

		/**
		 * Start new candidate.
		 * 
		 * @param id
		 *            unique id of candidate
		 * @param online
		 *            explicit decision for online gesture
		 * @see #setOnline(Boolean)
		 * @return
		 */
		public synchronized OneDollar start(Integer id, Boolean online) {
			if (!candidates.containsKey(id)) {
				candidates.put(id, new Candidate(this.parent, id, online,
						this.maxLength, this.maxTime));
			}
			return this;
		}

		/**
		 * Start new candidate with the global defined setting for online
		 * gestures.
		 * 
		 * @param id
		 *            unique id of candidate
		 * @see #setOnline(Boolean)
		 */
		public synchronized void start(Integer id) {
			this.start(id, this.online);
		}

		/**
		 * Add a new point to specified candidate.
		 * 
		 * @param id
		 *            unique id of candidate
		 * @param point
		 *            new x and y position of the candidate
		 * @return
		 */
		public synchronized void update(Integer id, PVector point) {
			if (candidates.containsKey(id)) {
				candidates.get(id).addPosition(point);
			}
		}

		/**
		 * Add a new point to specified candidate.
		 * 
		 * @param id
		 *            unique id of candidate
		 * @param x
		 *            new x position of the candidate
		 * @param y
		 *            new y position of the candidate
		 */
		public synchronized void update(Integer id, float x, float y) {
			this.update(id, new PVector(x, y));
		}

		/**
		 * Stop and delete a candidate.
		 * 
		 * @param id
		 *            unique id of candidate
		 */
		public synchronized void end(Integer id) {
			if (candidates.containsKey(id)) {
				candidates.remove(id);
			}
		}

		/**
		 * Show result messages.
		 * 
		 * @param value
		 * @return
		 */
		public OneDollar setVerbose(Boolean value) {
			this.verbose = value;
			return this;
		}

		/**
		 * Set the minimum equality in percent between candidate and template.
		 * 
		 * @param percent
		 *            integer between 0 and 100
		 * @return
		 */
		public OneDollar setMinScore(Integer percent) {
			this.recognizer.setMinScore((float) percent);
			return this;
		}

		/**
		 * Set, whether you use online gestures.
		 * 
		 * @param bool
		 * @return
		 */
		public OneDollar setOnline(Boolean bool) {
			this.online = bool;
			return this;
		}

		/**
		 * Set the time to live of candidates points.
		 * 
		 * @param ms
		 *            time in millisecond
		 * @return
		 */
		public OneDollar setMaxTime(Integer ms) {
			if (ms > 0) {
				this.maxTime = ms;
			}
			return this;
		}

		/**
		 * Set the minimum length of a candidate.
		 * 
		 * @param length
		 *            length in pixel
		 * @return
		 */
		public OneDollar setMinLength(Integer length) {
			if (length > 0) {
				this.recognizer.setMinLength(length);
			}
			return this;
		}

		/**
		 * Set the maximum length of a candidate.
		 * 
		 * @param length
		 *            length in pixel
		 * @return
		 */
		public OneDollar setMaxLength(Integer length) {
			if (length > 0) {
				this.maxLength = length;
			}
			return this;
		}

		/**
		 * Set the rotation angle of the Unistroke Recognition algorithm.
		 * 
		 * @param angle
		 *            angle in degree
		 * @return
		 */
		public OneDollar setRotationAngle(Integer degree) {
			if (degree > 0) {
				this.recognizer.setRotationAngle(degree);
			}
			return this;
		}

		/**
		 * Set the fragmentation rate of the Unistroke Recognition algorithm.
		 * 
		 * @param number
		 * @return
		 */
		public OneDollar setFragmentationRate(Integer number) {
			if (number > 0) {
				this.recognizer.setFragmentationRate(number);
			}
			return this;
		}

		/**
		 * Print all settings.
		 */
		public String toString() {
			String feedback = "# OneDollar-Unistroke-Recognizer\n"
					+ "#    Gesture Recognition Settings:\n"
					// +
					// "#       Online Gestures:                "+this.online+"\n"
					+ "#       Minimum Score:                  "
					+ this.recognizer.getScore() + " %\n"
					+ "#       Minimum Path Length:            "
					+ this.recognizer.getMinLength() + "\n"
					+ "#       Maximum Path Length:            "
					+ this.maxLength + "\n"
					+ "#       Maximum Time Length:            " + this.maxTime
					+ "\n" + "#    Unistroke Algorithm Settings:\n"
					+ "#       Fragmentation/Resampling Rate:  "
					+ this.recognizer.getFragmentationRate() + "\n"
					+ "#       Rotation Angle:                 "
					+ this.recognizer.getRotationAngle() + "\n";
			return feedback;
		}

		/**
		 * Delete references.
		 */
		public void dispose() {
			this.parent = null;
			this.candidates = null;
			this.templates = null;
			this.recognizer = null;
			this.online = null;
			this.verbose = null;
		}

		/**
		 * Return the version of the library.
		 * 
		 * @return String
		 */
		public static String getVersion() {
			return VERSION;
		}

		public final static String VERSION = "0.2";
	}

	// --------------------------------------------------
	/**
	 * 
	 * The $1 Gesture Recognizer is a research project by Wobbrock, Wilson and
	 * Li of the University of Washington and Microsoft Research. It describes a
	 * simple algorithm for accurate and fast recognition of drawn gestures.
	 * 
	 * Gestures can be recognized at any position, scale, and under any
	 * rotation. The system requires little training, achieving a 97%
	 * recognition rate with only one template for each gesture.
	 * 
	 * http://depts.washington.edu/aimgroup/proj/dollar/
	 * 
	 */

	public static class Recognizer {

		private final Float PHI, HALFDIAGONAL;
		private Integer fragmentation, size, angle, angleStep;
		private Integer minLength;
		private Float score;

		public Recognizer(PApplet _parent, Integer _fragmentation,
				Integer _size, Integer _angle, Integer _angleStep) {

			this.PHI = 0.5f * (-1.0f + PApplet.sqrt(5.0f));
			this.HALFDIAGONAL = 0.5f * PApplet.sqrt(_size * _size + _size
					* _size);

			this.fragmentation = _fragmentation;
			this.size = _size;
			this.angle = _angle;

			this.setRotationAngle(_angleStep);
			this.setMinScore(85.0f);
			this.setMinLength(75);
		}

		protected LinkedList<PVector> getSample(LinkedList<PVector> _points) {

			Float seperator = this.getPathLength(_points)
					/ (this.getFragmentation() - 1);
			Float distance = 0.0f;

			LinkedList<PVector> result = new LinkedList<PVector>();
			Stack<PVector> stack = new Stack<PVector>();

			ListIterator<PVector> iterator = _points.listIterator();
			while (iterator.hasNext()) {
				PVector point = (PVector) iterator.next();
				stack.push(point);
			}

			while (!stack.empty()) {
				PVector pPos = stack.pop();
				if (result.isEmpty()) {
					result.add(pPos);
				} else {
					if (stack.empty()) {
						result.add(pPos);
						continue;
					}
					PVector pos = stack.peek();
					Float localDistance = PVector.dist(pPos, pos);
					if ((distance + localDistance) >= seperator) {
						PVector resample = new PVector(pPos.x
								+ ((seperator - distance) / localDistance)
								* (pos.x - pPos.x), pPos.y
								+ ((seperator - distance) / localDistance)
								* (pos.y - pPos.y));
						result.add(resample);
						if (result.size() == (this.getFragmentation() - 1)) {
							result.add(stack.lastElement());
							stack.clear();
							return result;
						}
						stack.push(resample);
						distance = 0.0f;
					} else {
						distance += localDistance;
					}
				}
			}

			return result;
		}

		protected Float getPathLength(LinkedList<PVector> _points) {

			Float length = 0.0f;
			PVector tempVector = null;
			ListIterator<PVector> iterator = _points.listIterator();

			while (iterator.hasNext()) {
				PVector point = (PVector) iterator.next();
				if (tempVector != null) {
					length += PVector.dist(tempVector, point);
				}
				tempVector = point;
			}

			return length;
		}

		protected Integer getFragmentation() {
			return this.fragmentation;
		}

		protected LinkedList<PVector> getRotateToZero(
				LinkedList<PVector> _points) {
			PVector centroid = this.getCentroid(_points);
			Float theta = PApplet.atan2(centroid.y - _points.getFirst().y,
					centroid.x - _points.getFirst().x);

			return this.getRotateBy(_points, -theta);
		}

		protected PVector getCentroid(LinkedList<PVector> _points) {

			PVector centroid = new PVector(0.0f, 0.0f);
			Integer length = _points.size();

			ListIterator<PVector> iterator = _points.listIterator();
			while (iterator.hasNext()) {
				PVector point = (PVector) iterator.next();
				centroid.x += point.x;
				centroid.y += point.y;
			}
			centroid.x /= length;
			centroid.y /= length;

			return centroid;
		}

		protected LinkedList<PVector> getRotateBy(LinkedList<PVector> _points,
				Float _theta) {

			PVector centroid = this.getCentroid(_points);
			Float sin = PApplet.sin(_theta);
			Float cos = PApplet.cos(_theta);
			LinkedList<PVector> result = new LinkedList<PVector>();

			ListIterator<PVector> iterator = _points.listIterator();
			while (iterator.hasNext()) {
				PVector point = (PVector) iterator.next();
				result.add(new PVector((point.x - centroid.x) * cos
						- (point.y - centroid.y) * sin + centroid.x,
						(point.x - centroid.x) * sin + (point.y - centroid.y)
								* cos + centroid.y, 0.0f));
			}

			return result;
		}

		protected Rectangle2D.Float getBoundingBox(LinkedList<PVector> _points) {

			Float minX, maxX, minY, maxY;
			maxX = maxY = Float.NEGATIVE_INFINITY;
			minX = minY = Float.POSITIVE_INFINITY;

			ListIterator<PVector> iterator = _points.listIterator();
			while (iterator.hasNext()) {
				PVector point = (PVector) iterator.next();
				minX = PApplet.min(point.x, minX);
				maxX = PApplet.max(point.x, maxX);
				minY = PApplet.min(point.y, minY);
				maxY = PApplet.max(point.y, maxY);
			}

			return new Rectangle2D.Float((float) minX, (float) minY, maxX
					- minX, maxY - minY);
		}

		protected LinkedList<PVector> getScaleToSquare(
				LinkedList<PVector> _points, Rectangle2D.Float _box) {

			LinkedList<PVector> result = new LinkedList<PVector>();

			ListIterator<PVector> iterator = _points.listIterator();
			while (iterator.hasNext()) {
				PVector point = (PVector) iterator.next();
				result.add(new PVector(point.x * (this.size / _box.width),
						point.y * (this.size / _box.height), 0.0f));
			}

			return result;
		}

		protected LinkedList<PVector> getTranslateToOrigin(
				LinkedList<PVector> _points) {

			PVector centroid = this.getCentroid(_points);
			LinkedList<PVector> result = new LinkedList<PVector>();

			ListIterator<PVector> iterator = _points.listIterator();
			while (iterator.hasNext()) {
				PVector point = (PVector) iterator.next();
				result.add(new PVector(point.x - centroid.x, point.y
						- centroid.y));
			}

			return result;
		}

		protected synchronized Result check(LinkedList<PVector> _gesture,
				HashMap<String, Gesture> _templates,
				HashMap<String, Callback> _binds) {

			if (this.getPathLength(_gesture) > this.minLength) {

				LinkedList<PVector> points;
				points = this.getSample(_gesture);
				points = this.getRotateToZero(points);
				points = this.getScaleToSquare(points,
						this.getBoundingBox(points));
				points = this.getTranslateToOrigin(points);

				Float a = Float.POSITIVE_INFINITY;
				Float b = Float.POSITIVE_INFINITY;

				Gesture template = null;
				Gesture tempTemplate = null;

				for (String key : _binds.keySet()) {
					tempTemplate = _templates.get(key);

					Float distance = getDistanceAtBestAngle(points,
							tempTemplate, -(this.angle), this.angle,
							this.angleStep);
					if (distance < a) {
						b = a;
						a = distance;
						template = tempTemplate;
					} else if (distance < b) {
						b = distance;
					}
				}

				if (template != null) {

					Float score = 1.0f - (a / HALFDIAGONAL);
					Float otherScore = 1.0f - (b / HALFDIAGONAL);
					Float ratio = otherScore / score;

					if (score > this.score) {
						return new Result(template.getName(), score, ratio);
					}
				}
			}

			return null;
		}

		private Float getDistanceAtBestAngle(LinkedList<PVector> _points,
				Gesture _template, Integer _aDegree, Integer _bDegree,
				Integer _tresholdDegree) {

			Float a = PApplet.radians(_aDegree);
			Float b = PApplet.radians(_bDegree);
			Float treshold = PApplet.radians(_tresholdDegree);

			Float alpha = (PHI * a) + (1.0f - PHI) * b;
			Float beta = (1.0f - PHI) * a + (PHI * b);
			Float pathA = getDistanceAtAngle(_points, _template, alpha);
			Float pathB = getDistanceAtAngle(_points, _template, beta);

			if (pathA != Float.POSITIVE_INFINITY
					&& pathB != Float.POSITIVE_INFINITY) {
				while (PApplet.abs(b - a) > treshold) {
					if (pathA < pathB) {
						b = beta;
						beta = alpha;
						pathB = pathA;
						alpha = PHI * a + (1.0f - PHI) * b;
						pathA = getDistanceAtAngle(_points, _template, alpha);
					} else {
						a = alpha;
						alpha = beta;
						pathA = pathB;
						beta = (1.0f - PHI) * a + PHI * b;
						pathB = getDistanceAtAngle(_points, _template, beta);
					}
				}
				return PApplet.min(pathA, pathB);
			} else {
				return Float.POSITIVE_INFINITY;
			}
		}

		private Float getDistanceAtAngle(LinkedList<PVector> _points,
				Gesture _template, Float _theta) {
			_points = getRotateBy(_points, _theta);
			return getPathDistance(_points, _template.getPoints());
		}

		private Float getPathDistance(LinkedList<PVector> _pointsA,
				LinkedList<PVector> _pointsB) {
			if (_pointsA.size() != _pointsB.size()) {
				return Float.POSITIVE_INFINITY;
			} else {
				Float length = 0.0f;
				ListIterator<PVector> iterator = _pointsA.listIterator();
				while (iterator.hasNext()) {
					Integer index = iterator.nextIndex();
					iterator.next();
					length += PVector.dist(_pointsA.get(index),
							_pointsB.get(index));
				}
				return (length / _pointsA.size());
			}
		}

		protected void setRotationAngle(Integer _degree) {
			if (_degree > 0) {
				this.angleStep = _degree;
			} else {
				this.angleStep = 2;
			}
		}

		protected void setMinScore(Float _score) {
			if (_score > 0 && _score < 100) {
				this.score = _score / 100;
			} else {
				this.score = 0.85f;
			}
		}

		protected Integer getScore() {
			return (int) (this.score * 100);
		}

		protected void setMinLength(Integer _length) {
			if (_length > 0) {
				this.minLength = _length;
			} else {
				this.minLength = 75;
			}
		}

		protected Integer getMinLength() {
			return this.minLength;
		}

		protected Integer getRotationAngle() {
			return this.angleStep;
		}

		protected Integer getFragmentationRate() {
			return this.fragmentation;
		}

		public Integer getSize() {
			return this.size;
		}

		public void setFragmentationRate(Integer _number) {
			if (_number > 0) {
				this.fragmentation = _number;
			} else {
				this.fragmentation = 64;
			}
		}

	}

	// --------------------------------------------------
	public static class Result {

		private String name;
		private Float score, ratio;

		protected Result(String _name, Float _score, Float _ratio) {
			this.name = _name;
			this.score = (float) (Math.round(_score * 10000) / 100.);
			this.ratio = _ratio;
		}

		protected String getName() {
			return this.name;
		}

		protected Float getScore() {
			return this.score;
		}

		protected Float getRatio() {
			return this.ratio;
		}

	}

	// --------------------------------------------------
	public static class Candidate {

		private PApplet parent;
		private Integer id;
		private Integer birth;
		private Deque<PointInTime> line = new LinkedList<PointInTime>();
		private HashMap<String, Callback> binds;
		private Boolean online;
		private Integer ruleMaxLength, ruleMaxTime;

		protected Candidate(PApplet _parent, Integer _id, Boolean _online,
				Integer _ruleMaxLength, Integer _ruleMaxTime) {
			this.parent = _parent;
			this.id = _id;
			this.birth = _parent.millis();
			this.line = new LinkedList<PointInTime>();
			this.online = _online;
			this.ruleMaxLength = _ruleMaxLength;
			this.ruleMaxTime = _ruleMaxTime;
		}

		protected Candidate addPosition(PVector _point) {

			Integer deltaTime = this.parent.millis() - birth;
			this.line.add(new PointInTime(_point, deltaTime));

			if (this.line.size() > 1 && this.online) {
				while (true) {
					if (this.isTooOld(this.line, this.ruleMaxTime)) {
						this.line.removeFirst();
					} else {
						if (this.isTooLong(this.line, this.ruleMaxLength)) {
							this.line.removeFirst();
						} else {
							break;
						}
					}
				}
			}

			return this;
		}

		protected PVector getMiddlePoint() {
			ListIterator<PointInTime> iterator = (ListIterator<PointInTime>) line
					.iterator();
			PVector result = null;
			while (iterator.hasNext()) {
				PVector point = ((PointInTime) iterator.next()).getPosition();
				if (result == null) {
					result = point;
				} else {
					result.x += point.x;
					result.y += point.y;
				}
			}
			result.x = (int) (result.x / line.size());
			result.y = (int) (result.y / line.size());
			return result;
		}

		protected PVector getFirstPoint() {
			return line.getLast().getPosition();
		}

		protected Candidate addBind(String _template, Object _object,
				String _callback) {
			if (this.binds == null) {
				this.binds = new HashMap<String, Callback>();
			}
			if (!binds.containsKey(_template)) {
				this.binds.put(_template, new Callback(_object, _callback));
			}
			return this;
		}

		protected Candidate removeBind(String _template) {
			if (binds.containsKey(_template)) {
				this.binds.remove(_template);
			}
			return this;
		}

		protected void fire(String _template) {
			if (binds.containsKey(_template)) {
				binds.get(_template).fire(this, _template);
			}
		}

		protected void clear(PVector _point) {
			this.line.clear();
			this.birth = this.parent.millis();
			this.addPosition(_point);
		}

		protected Deque<PointInTime> getLine() {
			return this.line;
		}

		protected void draw() {
			this.parent.beginShape();
			ListIterator<PointInTime> iterator = (ListIterator<PointInTime>) line
					.iterator();
			while (iterator.hasNext()) {
				PVector point = ((PointInTime) iterator.next()).getPosition();
				this.parent.vertex(point.x, point.y);
				this.parent.ellipse(point.x, point.y, 2, 2);
			}
			this.parent.endShape();
		}

		protected Callback getBind(String _template) {
			return this.binds.get(_template);
		}

		protected HashMap<String, Callback> getBinds() {
			return this.binds;
		}

		protected Boolean hasBinds() {
			if (binds != null) {
				if (binds.size() > 0) {
					return true;
				}
			}
			return false;
		}

		private Boolean isTooOld(Deque<PointInTime> _line, Integer _ms) {
			Integer first = _line.getFirst().getTime();
			Integer last = _line.getLast().getTime();
			return (last > (first + _ms)) ? true : false;
		}

		private Boolean isTooLong(Deque<PointInTime> _line, Integer _length) {
			return (this.calcLength(_line) > _length) ? true : false;
		}

		private Float calcLength(Deque<PointInTime> _line) {
			Float length = 0.0f;
			PVector tempPosition = null;
			ListIterator<PointInTime> iterator = (ListIterator<PointInTime>) _line
					.iterator();
			while (iterator.hasNext()) {
				PointInTime point = (PointInTime) iterator.next();
				PVector position = point.getPosition();
				if (tempPosition != null) {
					length += PVector.dist(tempPosition, position);
				}
				tempPosition = position;
			}
			return length;
		}

	}

	// --------------------------------------------------
	public static class PointInTime {

		private Integer time;
		private PVector position;

		protected PointInTime(PVector _position, Integer _time) {
			this.position = _position;
			this.time = _time;
		}

		protected PVector getPosition() {
			return this.position;
		}

		protected Integer getTime() {
			return time;
		}

	}

	// --------------------------------------------------
	public static class Gesture {

		private String name;
		private LinkedList<PVector> points;

		protected Gesture(String _name, LinkedList<PVector> _points,
				Recognizer _recognizer) {
			this.name = _name;
			this.points = _recognizer.getSample(_points);
			this.points = _recognizer.getRotateToZero(this.points);
			this.points = _recognizer.getScaleToSquare(this.points,
					_recognizer.getBoundingBox(this.points));
			this.points = _recognizer.getTranslateToOrigin(this.points);
		}

		protected LinkedList<PVector> getPoints() {
			return this.points;
		}

		protected String getName() {
			return this.name;
		}

	}

	// --------------------------------------------------
	public static class Callback {

		private final Object object;
		private final String callback;
		private Method m;

		protected Callback(Object _object, String _callback) { // callback
																// method must
																// be public
			this.object = _object;
			this.callback = _callback;
			try {
				m = this.object.getClass().getMethod(this.callback,
						String.class, int.class, int.class, int.class,
						int.class);
			} catch (Exception e) {
				PApplet.println(e.getMessage());
			}
		}

		protected void fire(Candidate _motion, String _template) {
			if (this.object != null) {
				try {
					m.invoke(this.object, _template,
							(int) _motion.getFirstPoint().x,
							(int) _motion.getFirstPoint().y,
							(int) _motion.getMiddlePoint().x,
							(int) _motion.getMiddlePoint().y);
				} catch (Exception e) {
					PApplet.println(e.getMessage());
				}
			}
		}

		protected String getObjectClass() {
			return this.object.getClass().getName();
		}

		protected String getCallbackString() {
			return this.callback;
		}

	}
	// --------------------------------------------------
}
