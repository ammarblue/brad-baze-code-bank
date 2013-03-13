
public class stack{
	Object array[];
	int size;
	int numElm=0;
	stack(){}
	stack(int size){
		this.size=size;
		array=new Object[size];
		for(int i=0;i<size;i++){
			array[i]=new Object();
		}
	}
	public void push(Object in){
		array[numElm]=in;
		numElm++;
	}
	public void pop(){
		numElm--;
	}
	public boolean isEmpty(){
		if(numElm>0){
			return false;
		}else{
			return true;
		}
	}
	public int size(){
		return numElm;
	}
	public Object peek(){
		return array[numElm];
	}
}
