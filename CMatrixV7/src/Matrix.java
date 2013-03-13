import java.util.*;
public class Matrix {
	Matrix(GUI v){in=v;}
	int matrixA[][]=new int[100][100];
	int matrixB[][]=new int[100][100];
	int matrixC[][]=new int[100][100];
	long WORKER,FWORKER, ftime;
	ThreadRun threads[]=new ThreadRun[1000];
	Slice Sarray[]=new Slice[1000];
	int b=0;
	GUI in;
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
		WORKER=System.nanoTime();
		for(int i=0;i<1000;i++){
			Sarray[i]=new Slice(i);
			threads[i]=new ThreadRun(Integer.toString(i),this,Sarray[i]);
		}
		FWORKER=System.nanoTime();
	}
	public void matrixADD(Slice c){
		for(;c.jb<=c.jt;c.jb++){
			matrixC[c.k][c.jb]=matrixA[c.k][c.jb]+matrixB[c.k][c.jb];
		}
	}
	public void mStore(){
		if(b>=999){
			for(int i=0;i<100;i++){
				for(int j=0;j<100;j++){
					//in.matrixOUT[i][j]=matrixC[i][j];
					System.out.println(matrixC[i][j]);
				}
			}
		}
	}
	public String ftime(){
		ftime=FWORKER-WORKER;
		return Long.toString(ftime);
	}
}
