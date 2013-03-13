import mpjbuf.Buffer;
import mpjbuf.BufferException;
import mpjbuf.Type;
import xdev.ProcessID;
import xdev.smpdev.*;

public class test2 extends Thread {
	String args[];
	static SMPDevice smp=new SMPDevice();
	final static int N=10000;
	public test2(String xargs[]) {
		super(new SMPDevProcess(xargs[0]),xargs[0]);
		args=xargs;
	}
	public void test1(String args[]) {
		ProcessID[] pid=smp.init(args);
		int rank=Integer.parseInt(args[0]);
		//Buffer b = new Buffer(1000);
		if ((rank&1)==0) {
			for (int j=1; j<=N; j++) {
			Buffer b = new Buffer(1000);
			try {
				b.putSectionHeader(Type.INT);
				int x[]={j,j,j,j,j};
				b.write(x, 0, x.length);
				smp.isend(b, pid[rank+1], 5, 6);
				if ((j%500)==0) System.out.print(rank);
				//b.clear();
			} catch (BufferException e) {
				e.printStackTrace();
				return;
			}
			}
		} else {
			Buffer b = new Buffer(1000);
			int y[]=new int[N];
			for (int ii=0; ii<y.length; ii++) y[ii]=0;
			for (int j=1;j<=N; j++) {
			int x[]={-1,-2,-3,-4,-5};
			try {
				b.clear();
				b.putSectionHeader(Type.INT);
				b.write(x, 0, x.length);
			} catch (BufferException e) {
				e.printStackTrace();
				return;
			}
			smp.recv(b, pid[rank-1], 5, 6);
			if ((j%500)==0) System.out.print(rank);
			b.setWritable(false);
			try {
				b.getSectionHeader();
				b.read(x, 0, x.length);
				for (int i=0; i<x.length; i++) if (x[i]!=x[0]) System.out.println("err at "+j);
				y[x[0]-1]++;
			} catch (BufferException e) {
				e.printStackTrace();
			}
			}
			for (int i=0; i<y.length; i++) if (y[i]!=1) System.out.println("cnt err at "+i);
			System.out.println(y[N-1]==1?"OK":"NO");
		}
		smp.finish();
	}
	public void run() { 
		test1(args);
    }
	public static void main(String args[]) {
		test2 t=new test2(new String[]{"0","4"});
		t.start();
		t=new test2(new String[]{"1","4"});
		t.start();
		t=new test2(new String[]{"2","4"});
		t.start();
		t=new test2(new String[]{"3","4"});
		t.start();
	}
}