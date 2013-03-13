import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
public class Job {
	String uid="";
	String job_id;
	int terminate_time;
	String file_in;
	String runFile_in;
	String file_type;
	Date job_time;
	int num_nodes;
	int state;
	Process p;
	String main_cmd;
	boolean start=false;
	Job(){
		job_time=new Date();
		state=0;
	}
	void setTerminate(int in){
		terminate_time=in;
	}
	void setFileIn(String in){
		file_in=in;
		runFile_in="~/cMPI/"+in;
	}
	void setFileType(String in){
		file_type=in;
		if(in.compareTo("MPI")==0){
			main_cmd="mpirun";
		}else{
			System.out.println("ERROR ON RUN TYPE");
			System.exit(0);
		}
	}
	void runJob(){
		start=true;
		String ping_out="";
		String cmd=main_cmd+" -n "+num_nodes+" -machinefile ~/machines "+runFile_in;
		System.out.println(cmd);
		try{
			Runtime r=Runtime.getRuntime();
			p=r.exec(cmd);
			clock c=new clock(this);
			BufferedReader in=new BufferedReader(new InputStreamReader(p.getInputStream()));
			String input_line;
			PrintWriter out=new PrintWriter(new FileWriter(uid+file_in+".txt"));
			while((input_line=in.readLine())!=null){
				out.println(input_line);
				ping_out+=input_line;
			}
			in.close();
		}catch(IOException e){
			System.out.println(e);
		}
		start=false;
	}
	void setState(int in){//0=wait 1=running 2=done
		state=in;
	}
	int getState(){
		return state;
	}
	void setUID(String in){
		uid=uid+in;
	}
	void setJID(String in){
		job_id=in;
	}
	void setNodes(int in){
		num_nodes=in;
	}
	String printOut(){
		return job_id+" "+uid+" "+file_type+" "+num_nodes+" "+terminate_time+" "+file_in;
	}
}
