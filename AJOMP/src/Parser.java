
public class Parser {
	String data[];
	Anotations a;
	Parser(){	
		a=new Anotations();
	}
	public void store(int size){
		data=new String[size];
	}
	public void print(){
		for(int i=0;i<data.length;i++){
			System.out.println(data[i].toString());
		}
	}
	public void change(){
		for(int i=0;i<data.length;i++){
			if(a.check(data[i])==true){
				data[i]="\0";
			}
		}
	}
}
