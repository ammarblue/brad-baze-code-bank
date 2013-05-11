package reuze.app;

import java.io.File;
import java.util.ArrayList;

import com.software.reuze.ff_XMLReaderInputStream;

import com.software.reuze.ff_i_XMLInputStates;

public class XMLMinyGUIcreator extends appGUI {

	element currentelement, previouselement;
	appGUI aGUI;
	PropertyRadioButtonGroup Group;

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

		Group = new PropertyRadioButtonGroup(this, "Radio Buttons");

		aGUI = this;

		XML x = new XML();
		x.loadXML(new File("MinyGUI.xml"), x);
		if (!Group.Group.isEmpty()) {
			Group.add(gui);
		}
	}

	class XML extends ff_XMLReaderInputStream implements ff_i_XMLInputStates {

		@Override
		public void open(String name) {
			System.out.println("open=" + name);

			if (currentelement == null) {
				currentelement = new element(name);
			} else {
				previouselement = currentelement;
				currentelement = new element(name);
				currentelement.parentelement = previouselement;
				previouselement.children.add(currentelement);
			}
		}

		@Override
		public void attribute(String name, String value, String element) {
			System.out.println("attribute=" + name + " value=" + value
					+ " element=" + element);
			if (currentelement.type.equalsIgnoreCase("properties")) {
			} else {
				currentelement.type = value;
			}

		}

		@Override
		public void text(String text) {
			System.out.println("text=" + text);
			currentelement.value = text;
		}

		@Override
		public void close(String element) {
			System.out.println("close=" + element);

			if (currentelement.type.equalsIgnoreCase("Rect")) {
				gui = new MinyGUI(aGUI, Integer.valueOf(currentelement.children
						.get(0).value), Integer.valueOf(currentelement.children
						.get(1).value), Integer.valueOf(currentelement.children
						.get(2).value), Integer.valueOf(currentelement.children
						.get(3).value));
			} else if (currentelement.type.equalsIgnoreCase("PropertyButton")) {
				new PropertyButton(aGUI, currentelement.children.get(0).value,
						new TestButton(aGUI)).add(gui);
			} else if (currentelement.type
					.equalsIgnoreCase("PropertyButtonImage")) {
				new PropertyButtonImage(aGUI,
						currentelement.children.get(0).value, new TestButton(
								aGUI)).add(gui);
			} else if (currentelement.type.equalsIgnoreCase("PropertyCheckBox")) {
				new PropertyCheckBox(aGUI,
						currentelement.children.get(0).value, new MinyBoolean(
								aGUI, Boolean.valueOf(currentelement.children
										.get(1).value))).add(gui);
			} else if (currentelement.type.equalsIgnoreCase("PropertyList")) {
				new PropertyList(
						aGUI,
						currentelement.children.get(0).value,
						new MinyInteger(aGUI, Integer
								.valueOf(currentelement.children.get(1).value)),
						currentelement.children.get(2).value).add(gui);
			} else if (currentelement.type.equalsIgnoreCase("PropertyDisplay")) {
				new PropertyDisplay(aGUI, currentelement.children.get(0).value,
						new MinyString(aGUI,
								currentelement.children.get(1).value)).add(gui);
			} else if (currentelement.type
					.equalsIgnoreCase("PropertyEditFloat")) {
				new PropertyEditFloat(aGUI,
						currentelement.children.get(0).value, new MinyFloat(
								aGUI, Float.valueOf(currentelement.children
										.get(1).value))).add(gui);
			} else if (currentelement.type
					.equalsIgnoreCase("PropertySliderFloat")) {
				new PropertySliderFloat(aGUI,
						currentelement.children.get(0).value, new MinyFloat(
								aGUI, Float.valueOf(currentelement.children
										.get(1).value)),
						Float.valueOf(currentelement.children.get(2).value),
						Float.valueOf(currentelement.children.get(3).value))
						.add(gui);
			} else if (currentelement.type
					.equalsIgnoreCase("PropertyColorChooser")) {
				new PropertyColorChooser(aGUI,
						currentelement.children.get(0).value, new MinyColor(
								aGUI, Integer.valueOf(currentelement.children
										.get(1).value))).add(gui);
			} else if (currentelement.type.equalsIgnoreCase("PropertyGraph")) {
				new PropertyGraph(aGUI, currentelement.children.get(0).value,
						rotation).add(gui);
			} else if (currentelement.type.equalsIgnoreCase("PropertyGradient")) {
				new PropertyGradient(aGUI,
						currentelement.children.get(0).value, gradient)
						.add(gui);
			} else if (currentelement.type
					.equalsIgnoreCase("PropertyRadioButton")) {
				Group.AddToGroup(new PropertyRadioButton(aGUI,
						currentelement.children.get(0).value));
			} else if (currentelement.type.equalsIgnoreCase("fg")) {
				gui.fg = Integer.valueOf(currentelement.value);
			} else if (currentelement.type.equalsIgnoreCase("bg")) {
				gui.bg = Integer.valueOf(currentelement.value);
			} else if (currentelement.type.equalsIgnoreCase("selectColor")) {
				gui.selectColor = Integer.valueOf(currentelement.value);
			}

			currentelement = previouselement;
			if (currentelement != null) {
				previouselement = currentelement.parentelement;
			} else {
				previouselement = null;
			}
		}
	}

	class element {
		public element parentelement;
		public ArrayList<element> children = new ArrayList<element>();
		public String value, type;

		public element(String t) {
			type = t;
		}

		public element() {
		}
	}
}
