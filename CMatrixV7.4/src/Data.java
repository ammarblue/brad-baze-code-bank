
public class Data {
	int MA[][];
	int MB[][];
	int MC[][];
	int n=1000;//this is the number of values in the arrays
	Data(){
		//this will not be here on the implementation
		MA=new int[n][n];
		MB=new int[n][n];
		MC=new int[n][n];
		for(int i=0;i<n/2;i++){
			for(int j=0;j<n/2;j++){
				MA[i][j]=(int)(Math.random()*100);
				MB[i][j]=(int)(Math.random()*100);
			}
		}
		//end of array creation
	}
}
