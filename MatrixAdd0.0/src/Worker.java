
public class Worker {
	Worker(){}
	int tempA;
	int tempB;
	int tempC;
	public void getDataA(int A){
		int tempA=A;
	}
	public void getDataB(int B){
		int tempB=B;
	}
	public int runThread(){
		ThreadRun ONE=new ThreadRun("NAME",tempA,tempB);
		tempC=ONE.getMC();
		return tempC;
	}
}
