public class herp{
	public static void main(String args[]){
		#pragma jomp parallel for()
		for(int i=0;i<10;i++){
			System.out.println(i);
		}
	}
}