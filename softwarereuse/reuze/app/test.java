package reuze.app;

public class test extends appGUI {

	public void setup() {
		size(600, 400);
		smooth();
		img = loadImage("../data/mearth.jpg");
		time = 0;
		running = new MinyBoolean(this, true);
		speed = new MinyInteger(this, 2);
		timeCaption = new MinyString(this, "0.0");
		stWidth = new MinyFloat(this, 2.0f);
		rSize = new MinyFloat(this, 1.0f);
		borderColor = new MinyColor(this, color(192));
		rotation = new InterpolatedFloat(1);
		rotation.add(0, 0);
		rotation.add(1.5f, -PI);
		rotation.add(3.0f, PI);
		rotation.add(4.0f, 2 * PI);
		rotation.add(6.0f, 0);

		gradient = new ColorGradient(this, color(0));
		gradient.add(0, color(0, 1));
		gradient.add(0.25f, color(255, 0, 0));
		gradient.add(0.5f, color(0, 255, 0));
		gradient.add(0.75f, color(0, 0, 255));
		gradient.add(1, color(255));

		gui = new MinyGUI(this, 0, 0, 200, height);
		new PropertyButton(this, "Start", new TestButton(this)).add(gui);
		new PropertyButton(this, "Tester", new TestButton(this)).add(gui);
		new PropertyButtonImage(this, "../data/particle.png", new TestButton(
				this)).add(gui);
		new PropertyCheckBox(this, "Running", running).add(gui);
		new PropertyList(this, "Speed", speed,
				"slowest;slow;normal;fast;fastest").add(gui);
		new PropertyDisplay(this, "Time", timeCaption).add(gui);
		new PropertyEditFloat(this, "Border width", stWidth).add(gui);
		new PropertySliderFloat(this, "Rect size", rSize, 0.5f, 2.0f).add(gui);
		new PropertyColorChooser(this, "Fill color", borderColor).add(gui);
		new PropertyGraph(this, "Rotation", rotation).add(gui);
		new PropertyGradient(this, "Gradient", gradient).add(gui);
		// the following lines creates a radio button group, adds two radio
		// buttons to the group, then adds the group to the gui
		PropertyRadioButtonGroup test = new PropertyRadioButtonGroup(this,
				"Test Buttons");
		test.AddToGroup(new PropertyRadioButton(this, "Test"));
		test.AddToGroup(new PropertyRadioButton(this, "Test 2"));
		test.add(gui);

		gui.fg = color(0);
		gui.bg = color(255);
		gui.selectColor = color(196);
		for (Property p : gui)
			System.out.println(p.get().getString());
		// gui.drawBackground=false; //uncomment for image background
		gui.writeXML();
	}

}
