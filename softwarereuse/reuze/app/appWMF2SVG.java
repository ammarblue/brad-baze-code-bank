package reuze.app;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import reuze.awt.ff_WMF2SVG;

public final class appWMF2SVG {
	public static void main(String args[]) {

		ff_WMF2SVG wmf;
		File source_file;
		File dest_file;
		FileInputStream source = null;
		FileOutputStream destination = null;
		PrintStream dataOut = null;
		DataInputStream dataIn;

		if (args.length == 0) {
			System.out.println("Usage: java wmf2svg <source files.wmf>");
			// System.exit(0);
			args = new String[] { "/Users/bobcook/Desktop/svg2/aangel.wmf" };
		}
		int n = args.length;
		while (--n >= 0) {
			System.out.print(args[n] + " ");
			source_file = new File(args[n]);
			int i = args[n].lastIndexOf(".wmf");
			if (i <= 0) {
				System.out.println("No .wmf in source name " + args[n]);
				continue;
			}
			String s = args[n].substring(0, i) + ".svg";
			dest_file = new File(s);
			try {
				source = new FileInputStream(source_file);
				destination = new FileOutputStream(dest_file);
				dataOut = new PrintStream(destination);
				dataIn = new DataInputStream(source);
				wmf = new ff_WMF2SVG(dataIn, dataOut);
				dataIn.close();
				dataOut.close();
				source.close();
				destination.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
		System.exit(0);
	}
}