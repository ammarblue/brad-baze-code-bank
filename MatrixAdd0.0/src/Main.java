
public class Main {
	public static void main(String args[]){
		Matrix A=new Matrix();
		Worker W=new Worker();
		A.setMatrix();
		for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				W.getDataA(A.getA(i, j));
				W.getDataB(A.getB(i, j));
				A.setMatrixC(W.runThread(),i,j);
			}
		}
		A.Print();
	}
}