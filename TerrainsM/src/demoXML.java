

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
		temp.t.sig=false;
		String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<terrain id=\"Terrain\">"
				//City Scape,Terrain,Terrain With Buildings,Block City,Wet Terrain,Crator,Egypt,Wavey,Simple Plane
				+ "<noise>2</noise>"//1 to 10
				+ "<name>City</name>"//any name
				+ "<clouds>true</clouds>" //true or false
				+ "<time>12</time>"//from 7 to 12 am
				+ "</terrain>";
		ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
		XML x = new XML();
		x.loadXML(is,x);
		temp.drive(x.Dstate);
		temp.t.sig=true;
	}

	class XML extends ff_XMLReaderInputStream implements ff_i_XMLInputStates {
		int Nstate=0;
		int Dstate=0;
		@Override
		public void open(String name) {
			System.out.println("open=" + name);
			if(name.compareTo("noise")==0){
				Nstate=1;
			}else if(name.compareTo("name")==0){
				Nstate=2;
			}else if(name.compareTo("clouds")==0){
				Nstate=3;
			}else if(name.compareTo("time")==0){
				Nstate=4;
			}else{
				Nstate=0;
			}
		}

		@Override
		public void attribute(String name, String value, String element) {
			System.out.println("attribute=" + name + " value=" + value
					+ " element=" + element);
			if(element.compareTo("terrain")==0&&value.compareTo("City Scape")==0){
				Dstate=0;
			}else if(element.compareTo("terrain")==0&&value.compareTo("Terrain")==0){
				Dstate=1;
			}else if(element.compareTo("terrain")==0&&value.compareTo("Terrain With Buildings")==0){
				Dstate=2;
			}else if(element.compareTo("terrain")==0&&value.compareTo("Block City")==0){
				Dstate=3;
			}else if(element.compareTo("terrain")==0&&value.compareTo("Wet Terrain")==0){
				Dstate=4;
			}else if(element.compareTo("terrain")==0&&value.compareTo("Crator")==0){
				Dstate=5;
			}else if(element.compareTo("terrain")==0&&value.compareTo("Egypt")==0){
				Dstate=6;
			}else if(element.compareTo("terrain")==0&&value.compareTo("Wavey")==0){
				Dstate=7;
			}else if(element.compareTo("terrain")==0&&value.compareTo("Simple Plane")==0){
				Dstate=8;
			}
		}

		@Override
		public void text(String text) {
			System.out.println("text=" + text);
			if(Nstate==1){
				temp.noise=Integer.parseInt(text);
			}else if(Nstate==2){
				temp.Name=text;
			}else if(Nstate==3){
				temp.clouds=Boolean.parseBoolean(text);
			}else if(Nstate==4){
				temp.time=12-Integer.parseInt(text);
			}
		}

		@Override
		public void close(String element) {
			System.out.println("close=" + element);
		}
	}
}
