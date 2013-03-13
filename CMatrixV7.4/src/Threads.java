
public class Threads implements Runnable{
	Thread t;
	Slice S;
	Operations O;
	Data D;
	Schedualer Sc;
	String name;
	int ID;//this is the int version of the threads name, it will be its ID number that it was given when created
	Threads(String inname,Slice ins,Operations ino,Data ind,Schedualer insc){
		S=ins;//slice
		O=ino;//operations
		D=ind;
		Sc=insc;
		name=inname;
		ID=Integer.parseInt(name);//thread ID
		t=new Thread(this,name);//creates the thread
		t.start();//runs the thread
	}
	public void run(){
		S.SliceNum(ID);
		int Tfloor=S.floor;
		int Tceiling=S.ceiling;
		for(;Tfloor<=Tceiling;Tfloor++){
			 D.MC[Tfloor][Tceiling]=O.add(D.MA[Tfloor][Tceiling],D.MB[Tfloor][Tceiling]);
		}
		S.cnt--;
		if(S.cnt<=0){
			Sc.done=true;
			Sc.print();
		}else{
			Sc.done=false;
		}
	}
}
