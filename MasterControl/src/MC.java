import java.util.*;
import java.io.*;
public class MC{
	Job[] Queue=new Job[20];
	int count=0;
	Scanner in;
	boolean On=true;
	MC(){
		buffer();
	}
	void buffer(){
		while(On){
			if(count==0){
				call();
			}else{
				On=false;
			}
		}
		startJobs();
	}
	void addJob(){
		Queue[count]=new Job();
		count++;
	}
	void startJobs(){
		int runs=count;
		System.out.println("STARTING THESE JOBS");
		listOut();
		for(int i=0;i<10;i++){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.print(".");
		}
		for(int i=0;i<runs;i++){
			Queue[i].setState(1);
			Queue[i].runJob();
			jobDone(i);
		}
		On=true;
		for(int i=0;i<10;i++){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.print(".");
		}
		System.out.println("\nALL JOBS HAVE COMPLETED");
		//buffer();
	}
	void jobDone(int in){
		Queue[in].setState(2);
		removeJob(in);
		count--;
	}
	void removeJob(int in){
		Queue[in]=null;
	}
	void call(){
		try {
			in=new Scanner(new File("Data.txt"));
			while(in.hasNext()){
				if(in.hasNext("[J][0-9]*")){
					this.addJob();
					Queue[count-1].setJID(in.next());
				}else if(in.hasNext("[A-Z]{1}[a-z]*")){
					Queue[count-1].setUID(in.next());
				}else if(in.hasNext("[A-Z]{2,}")){
					Queue[count-1].setFileType(in.next());
				}else if(in.hasNext("[N][0-9]*")){
					Queue[count-1].setNodes(Integer.parseInt((in.next().substring(1))));
				}else if(in.hasNext("[T][0-9]*")){
					Queue[count-1].setTerminate(Integer.parseInt((in.next().substring(1))));
				}else if(in.hasNext("@[a-zA-Z_]*")){
					Queue[count-1].setFileIn(in.next().substring(1));
				}else{
					System.out.println("ERROR ON FILE READ");
					System.exit(0);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	void listOut(){
		for(int i=0;i<count;i++){
			System.out.println(Queue[i].printOut());
		}
	}
}
