import java.util.LinkedList;
public class linked_queue {
	int count=0;
	int size;
	LinkedList q[];
	linked_queue(){}
	linked_queue(int size){
		this.size=size;
		q=new LinkedList[size];
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
	void enqueue(LinkedList in){
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
