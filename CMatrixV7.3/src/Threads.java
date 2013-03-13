
public class Threads implements Runnable{
	Thread t;
	Slice S;
	Operations O;
	String name;
	int ID;//this is the int version of the threads name, it will be its ID number that it was given when created
	Threads(String name,Slice ins,Operations ino){
		S=ins;//slice
		O=ino;//operations
		ID=Integer.parseInt(name);//thread ID
		t=new Thread(name);//creates the thread
		t.start();//runs the thread
	}
	public void run(){
		for(;)
		O.add();
	}
}
