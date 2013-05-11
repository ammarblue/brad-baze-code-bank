package reuze.awt;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.software.reuze.vg_StyleProperties;


public final class ff_SVGJAXBreader {
	public final static vg_StyleProperties getStyle(String s) {
		   StringBuilder sb=new StringBuilder(s);
 		   sb.insert(0, "<style ");
 		   sb.append(" />");
 		   int i=0;
 		   do {
 			   i=sb.indexOf(": ", i);
 			   if (i<=0) break;
 			   sb.replace(i, i+2, "=\"");
 			   i=sb.indexOf(";", i+2);
			   if (i<=0) break;
			   sb.replace(i, i+1, "\" ");
			   i++;
 		   } while (true);
		vg_StyleProperties x=null;
		try {
			 
			StringReader file = new StringReader(sb.toString());
			JAXBContext jaxbContext = JAXBContext.newInstance(vg_StyleProperties.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			x = (vg_StyleProperties) jaxbUnmarshaller.unmarshal(file);
			file.close();
		  } catch (JAXBException e) {
			return null;
		  }
		  return x;
	}
	public final static Object getObject(Class c, String s) {
		Object x=null;
		try {
			 
			StringReader file = new StringReader(s);
			JAXBContext jaxbContext = JAXBContext.newInstance(c);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			x = jaxbUnmarshaller.unmarshal(file);
			file.close();
		  } catch (JAXBException e) {
			return null;
		  }
		  return x;
	}
	public final static Object getObject(Class c, File f) {
		Object x=null;
		try {			 
			FileInputStream file;
			file = new FileInputStream(f);
			JAXBContext jaxbContext = JAXBContext.newInstance(c);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			x = jaxbUnmarshaller.unmarshal(file);
			file.close();
		  } catch (JAXBException e) {
			return null;
		  } catch (FileNotFoundException e) {			  
		  } catch (IOException e) {			  
		  }
		  return x;
	}
}
