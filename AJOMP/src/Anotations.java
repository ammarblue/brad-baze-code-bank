
public class Anotations {
	String A1;
	String read;
	int size;
	Anotations(){}
	public boolean check(String in){
		read=in;
		CharSequence s="#pragma";
		if(read.contains(s)){
			return true; 
		}else{
			return false;
		}
	}
}
