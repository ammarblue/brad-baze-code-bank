import java.util.LinkedList;
public class Main {
	public static void main(String[] args) {
		queue q=new queue(4);
		q.enqueue(1);
		q.enqueue(2);
		q.enqueue(3);
		q.enqueue(4);
		q.printOut();
		q.dequeue();
		q.printOut();
		linked_queue l=new linked_queue(2);
		LinkedList a=new LinkedList();
		a.addFirst(2);
		a.add(4);
		LinkedList b=new LinkedList();
		b.addFirst(1);
		b.add(3);
		l.enqueue(a);
		l.enqueue(b);
		l.printOut();
		l.dequeue();
		l.printOut();
	}
}
