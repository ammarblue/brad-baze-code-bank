import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class mpjrun {

	public static void main(String[] args) {
		if (args.length<3) {
			System.out.println("Usage: java mpjrun Secs N executable IO ...");
			System.exit(-1);
		}
		int secs=Integer.valueOf(args[0]);
		if (secs<1 || secs>500) {
			System.out.println("Usage: secs<1 or secs>500");
			System.exit(-1);
		}
		int n=Integer.valueOf(args[1]);
		if (n<1 || n>50) {
			System.out.println("Usage: N<1 or N>50");
			System.exit(-1);
		}
		Runtime x=Runtime.getRuntime();
		Process i[]=new Process[n];
		int nn;
		long time=System.currentTimeMillis()/1000;
		for (nn=0; nn<n; nn++) {
		try {
			String s="java "+args[2]+" "+nn+" local.conf "+args[3];
			System.out.println(s);
			i[nn]=x.exec(s);
		} catch (IOException e) {
			e.printStackTrace();
			for (;nn>0; nn--) i[nn-1].destroy();
			System.exit(-1);
		}
		} //for
		for (nn=0; nn<n; nn++) {
		InputStream os=i[nn].getInputStream();
		InputStream es=i[nn].getErrorStream();
		while (true) { 
		try {
			i[nn].exitValue();
			i[nn].waitFor();
			i[nn]=null;
			break;
		} catch (IllegalThreadStateException e) {
			if ((System.currentTimeMillis()/1000-time)>secs) {
				for (nn=0; nn<n; nn++)
					if (i[nn]!=null) i[nn].destroy();
				System.out.println("***************** TIMED OUT ***************");
				System.exit(-1);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		} //while
		System.out.println("*****************RANK "+nn+" ***************");
		byte b[]=new byte[6000];
		int j=0,k;
		try {
			j=os.read(b);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		for (k=0; k<j; k++) System.out.print((char)b[k]);
		j=0;
		try {
			j=es.read(b);
			es.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		for (k=0; k<j; k++) System.out.print((char)b[k]);
		} //for
		System.out.println("***************** COMPLETED ***************");
	}

}
