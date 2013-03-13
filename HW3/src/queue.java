
public class queue {
	int size;
	int count=0;
	Object q[];
	queue(){}
	queue(int size){
		this.size=size;
		q=new Object[size];
	}
	void dequeue(){
		if(count>0){
			for(int i=0;i<count-1;i++){
				q[i]=q[i+1];
			}
			count--;
		}else{
			System.out.println("Queue is Empty");
		}
	}
	void enqueue(Object in){
		if(count<size){
			q[count]=in;
		}else{
			System.out.println("Queue Full");
		}
		count++;
	}
	int size(){
		return size;
	}
	int numElm(){
		return count;
	}
	void printOut(){
		for(int i=0;i<count;i++){
			System.out.println(q[i].toString());
		}
	}
}
