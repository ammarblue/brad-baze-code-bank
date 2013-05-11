package reuze.demo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import processing.core.PApplet;

public class demoToxiSpeechFromText  extends PApplet {
	public void setup() {
		  googleTTS("Google text to speech is awesome!", "en"); // en, es, fr, de, it
		  exit();
		}

		void googleTTS(String txt, String language) {
		  String u = "http://translate.google.com/translate_tts?tl=";
		  u = u + language + "&q=" + txt;
		  u = u.replace(" ", "%20"); // replace spaces by %20
		  try {
		    URL url = new URL(u);
		    try {
		      URLConnection connection = url.openConnection();
		      // pose as web browser
		      connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; .NET CLR 1.2.30703)");
		      connection.connect();
		      InputStream is = connection.getInputStream();
		      // create a file named after the text
		      File f = new File("../data/" + "speech.mp3");
		      OutputStream out = new FileOutputStream(f);
		      byte buf[] = new byte[1024];
		      int len;
		      while ((len = is.read(buf)) > 0) {
		        out.write(buf, 0, len);
		      }
		      out.close();
		      is.close();
		      println("File created for: " + txt); // report back via the console
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  } catch (MalformedURLException e) {
		    e.printStackTrace();
		  }
		}
}