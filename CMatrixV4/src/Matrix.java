import java.util.*;
public class Matrix {
	int matrixA[][]=new int[100][100];
	int matrixB[][]=new int[100][100];
	int matrixC[][]=new int[100][100];
	Date WORKER,FWORKER;
	ThreadRun threads[]=new ThreadRun[1000];
	int b=0;
	int cnt,k,jb,jt;
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
		for(int i=0;i<30;i++){//wait your damn turn!!! Oh and put it back to i<1000
			cnt=i;
			k=cnt/10;
			jb=(cnt-(10*k))*10;//sync error here 
			jt=jb+9;//and here too lol
			System.out.println(i+": k "+k+", jb "+jb+", jt "+jt);
			threads[i]=new ThreadRun(Integer.toString(i),this);
		}
		FWORKER=new Date();
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
