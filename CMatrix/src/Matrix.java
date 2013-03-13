import java.util.*;
public class Matrix {
	int matrixA[][]=new int[100][100];
	int matrixB[][]=new int[100][100];
	int matrixC[][]=new int[100][100];
	Date WORKER,FWORKER;
	ThreadRun threads[]=new ThreadRun[1000];
	public void setMatrix(){
		for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				matrixA[i][j]=(int)(Math.random()*100);
				matrixB[i][j]=(int)(Math.random()*100);
			}
		}
	}
	public void worker(){
		WORKER=new Date();
		for(int i=0;i<100;i++){
			int threadnum=i*10;
			for(int j=0;j<10;j++){
				int tk=j*10;
				threads[threadnum+j]=new ThreadRun(Integer.toString(threadnum+tk));
				for(int k=0;k<10;k++){
					threads[threadnum+j].tempMA[k]=matrixA[i][k+tk];
					threads[threadnum+j].tempMB[k]=matrixB[i][k+tk];
				}
			}
		}
		FWORKER=new Date();
	}
	public void store(int temp[]){//alpha
		int ct=0;	
		for(int i=0;i<100;i++){
			for(int j=0;j<10;j++){
				int tk=j*10;
				for(int k=0;k<10;k++){
					matrixC[i][k+tk]=threads[ct].tempMC[k];
					ct++;
				}
			}
		}
	}
	public void mPrint(){
		int ct=0;
		for(int i=0;i<100;i++){
			for(int j=0;j<10;j++){
				for(int t=0;t<10;t++){
					matrixC[i][j]=threads[ct].tempMC[t];
				}
				System.out.println(matrixC[i][j]);
				ct++;
			}
		}
		System.out.println("START AND END TIME OF THREADS "+WORKER+" "+FWORKER);
	}
}
