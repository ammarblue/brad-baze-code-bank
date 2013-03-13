import xdev.ProcessID;
import xdev.niodev.NIODevice;
import xdev.smpdev.*;

public class test3 extends Thread {
	String args[];
	static NIODevice smp=new NIODevice();
	final static int N=10000;
	public test3(String xargs[]) {
		//super(new SMPDevProcess(xargs[0]),xargs[0]);
		args=xargs;
	}
	public void test1(String args[]) {
		ProcessID[] pid=smp.init(args);
		int rank=Integer.parseInt(args[0]);
		smp.finish();
	}
	public void run() { 
		test1(args);
    }
	public static void main(String args[]) {
		test3 t=new test3(args);
		t.start();
	}
}