import java.util.*;
public class Matrix {
	Matrix(){}
	int matrixA[][]=new int[100][100];
	int matrixB[][]=new int[100][100];
	int matrixC[][]=new int[100][100];
	Date WORKER,FWORKER;
	ThreadRun threads[]=new ThreadRun[1000];
	Slice Sarray[]=new Slice[1000];
	int b=0;
	public void setMatrix(){
		for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				matrixA[i][j]=(int)(Math.random()*100);
				matrixB[i][j]=(int)(Math.random()*100);
				//matrixA[i][j]=48;
				//matrixB[i][j]=2;
			}
		}
	}
	public void worker(){
		WORKER=new Date();
		for(int i=0;i<1000;i++){
			Sarray[i]=new Slice(i);
			threads[i]=new ThreadRun(Integer.toString(i),this,Sarray[i]);
		}
		FWORKER=new Date();
	}
	public void matrixADD(int i,int j){
		matrixC[i][j]=matrixA[i][j]+matrixB[i][j];
	}
	public void mPrint(){
		if(b>=999){
			for(int i=0;i<100;i++){
				for(int j=0;j<100;j++){
					System.out.println(matrixC[i][j]);
				}
			}
		}
		System.out.println("START AND END TIME OF THREADS "+WORKER+" "+FWORKER);
	}
}
