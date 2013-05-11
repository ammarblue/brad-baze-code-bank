package reuze.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.software.reuze.l_ParseM;
import com.software.reuze.l_ParseM.MTYPE;
import com.software.reuze.l_ParseM.Str;

public class appM {
	public static void main(String args[]) {
		MTYPE p[] = new MTYPE[27];
		StringBuffer output;
		int i;
		for (i = 0; i < 27; i++)
			p[i] = new MTYPE();
		p[13].x = new Str("java.lang.Math");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		output = new StringBuffer("");
		while (true) {
			System.out.print(">");
			if (output.length() > 0)
				output.delete(0, output.length());
			String line = null;
			// read the username from the command-line; need to use try/catch
			// with the
			// readLine() method
			try {
				line = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error!");
				System.exit(1);
			}
			line = line.trim();
			if (line.length() == 0)
				continue;
			if (line.charAt(0) == '.') {
				String t[] = line.split("\\s");
				for (String u : t)
					System.out.println(u);
				try {
					FileReader fis = new FileReader(t[0].substring(1) + ".txt");
					char c[] = new char[9999];
					i = fis.read(c, 0, c.length);
					System.out.println(i);
					if (i <= 0)
						continue;
					line = new String(c, 0, i);
					fis.close();
				} catch (FileNotFoundException fe) {
					System.out.println(t[0].substring(1) + ".txt not found");
					continue;
				} catch (IOException ioe) {
					System.out.println("IO error!");
					System.exit(1);
				}
			}
			l_ParseM.Expr(p, line, output);
			System.out.println(output);
		}
	}
}
