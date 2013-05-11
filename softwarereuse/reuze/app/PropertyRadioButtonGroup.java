package reuze.app;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import com.software.reuze.ff_XMLWriter;

class PropertyRadioButtonGroup extends Property {

	private final appGUI appGUI;
	ArrayList<PropertyRadioButton> Group = new ArrayList<PropertyRadioButton>();

	PropertyRadioButtonGroup(reuze.app.appGUI gui, String name) {
		super(gui, name);
		this.appGUI = gui;
	}

	public void AddToGroup(PropertyRadioButton radbtn) {

		int size = Group.size();
		Group.add(size, radbtn);
		radbtn.indexinGroup = new MinyInteger(this.appGUI);
		radbtn.indexinGroup.setValue(size);
		radbtn.Group = this;
	}

	public int getsize() {
		return Group.size();
	}

	void add(MinyGUI parent) {
		_parent = parent;
		for (int i = 0; i < Group.size(); i++) {
			Group.get(i).add(_parent);
		}
	}

	public void Changeselectedbutton(PropertyRadioButton radbtn) {
		int id = Group.indexOf(radbtn);
		for (int i = 0; i < Group.size(); i++) {
			if (i != id) {
				Group.get(i)._value = new MinyBoolean(this.appGUI);
			}
		}
	}

	/*
	 * public ff_XMLWriter writeXML(ff_XMLWriter xml, int i){ try{ xml
	 * .element(String.valueOf(i)) .attribute("type","PropertyRadioButtonGroup")
	 * .element("name", _name) .element("Group") .attribute("elements",
	 * Group.size()); for (int j=0; j<Group.size(); j++) {
	 * xml=Group.get(j).writeXML(xml, j); } xml.pop() .pop(); } catch
	 * (IOException e) {
	 * System.out.println("Error: PropertyRadioButtonGroup.writeXML()");
	 * e.printStackTrace(); }
	 * 
	 * return xml; }
	 */
}