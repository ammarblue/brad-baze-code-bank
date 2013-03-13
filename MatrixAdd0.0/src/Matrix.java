
public class Matrix{
	Matrix(){}
	int[][] matrixA=new int[100][100];
	int[][] matrixB=new int[100][100];
	int[][] matrixC=new int[100][100];
	public void setMatrix(){
		for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				matrixA[i][j]=(int)(Math.random()*100);
				matrixB[i][j]=(int)(Math.random()*100);
			}
		}
	}
	public int getA(int Apoint1,int Apoint2){
		return matrixA[Apoint1][Apoint2];
	}
	public int getB(int Bpoint1,int Bpoint2){
		return matrixB[Bpoint1][Bpoint2];
	}
	public void setMatrixC(int C,int I,int J){
		matrixC[I][J]=C;
	}
	public void Print(){
		for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				System.out.print(" "+matrixC[i][j]);
			}
		}
	}
}