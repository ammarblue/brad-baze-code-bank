

import java.io.ByteArrayInputStream;
import processing.core.PApplet;
import com.XML.*;

public class demoXML {
	Test temp;
	demoXML(){}
	demoXML(Test in){
		temp=in;
		runXML();
	}
	public void runXML() {
		temp.t.state=false;
		String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<terrain id=\"CityScape\">"
				+ "<noise>29</noise>"
				+ "<name>City</name>" + "</terrain>";
		ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
		XML x = new XML();
		x.loadXML(is, x);
	}

	class XML extends ff_XMLReaderInputStream implements ff_i_XMLInputStates {
		@Override
		public void open(String name) {
			System.out.println("open=" + name);
		}

		@Override
		public void attribute(String name, String value, String element) {
			System.out.println("attribute=" + name + " value=" + value
					+ " element=" + element);
			if(element.compareTo("terrain")==0&&value.compareTo("CityScape")==0){
				temp.drive();
			}
		}

		@Override
		public void text(String text) {
			System.out.println("text=" + text);
		}

		@Override
		public void close(String element) {
			System.out.println("close=" + element);
		}
	}
}
