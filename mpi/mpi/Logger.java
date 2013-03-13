package mpi;

public class Logger {
	private static Logger root=new Logger();
public boolean isDebugEnabled() {return true;}
public void debug(String msg) {
	System.out.println("D:: "+msg);
}
public static Logger getRootLogger() {
	return root;
}
public static Logger getLogger(String string) {
	return root;
}
public void info(String string) {
	System.out.println("I:: "+string);
}
public void fatal(String string) {
	System.out.println("F:: "+string);
	System.exit(-1);
}
}
